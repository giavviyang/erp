package com.erp.produce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.bean.BeanUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.produce.DamageFlowCardMapper;
import com.erp.floor.mapper.produce.DamageRecordMapper;
import com.erp.floor.mapper.produce.PatchBusinessMapper;
import com.erp.floor.mapper.produce.PatchRecordMapper;
import com.erp.floor.mapper.sales.FlowCardMapper;
import com.erp.floor.mapper.sales.ShelfDivisionBusinessMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.mapper.system.SysSemiProductMapper;
import com.erp.floor.pojo.produce.domain.*;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.ShelfDivisionBusiness;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.floor.pojo.system.domain.SysSemiProduct;
import com.erp.produce.service.CompletionService;
import com.erp.produce.service.PatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/14 14:54
 */
@Service
public class PatchServiceImpl extends ServiceImpl<PatchRecordMapper, PatchRecord> implements PatchService {

    @Resource
    private PatchRecordMapper patchRecordMapper;

    @Resource
    private PatchBusinessMapper patchBusinessMapper;

    @Resource
    private DamageFlowCardMapper damageFlowCardMapper;

    @Resource
    private DamageRecordMapper damageRecordMapper;

    @Resource
    private ShelfDivisionBusinessMapper shelfDivisionBusinessMapper;

    @Resource
    private FlowCardMapper flowCardMapper;

    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;

    @Resource
    private SysSemiProductMapper semiProductMapper;

    @Resource
    CompletionService completionService;

    @Override
    @Transactional
    public CommonResult addPatch(List<PatchBusiness> patchBusinesses) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        PatchRecord record = new PatchRecord();
        int allPatchNum = 0;
        int allPatchArea = 0;
        String pathFlowCardNo = "";
        for (int i = 0; i < patchBusinesses.size();i++){
            PatchBusiness patchBusiness = patchBusinesses.get(i);
            allPatchArea += patchBusiness.getPatchArea();
            allPatchNum += patchBusiness.getPatchNum();
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getFlowCardNo,patchBusiness.getFlowCardNo());
            FlowCard flowCard = flowCardMapper.selectOne(flowCardLambdaQueryWrapper);
            //新增补片流程卡
            FlowCard patchFlowCard = new FlowCard();
            BeanUtils.copyBeanProp(patchFlowCard,flowCard);
            patchFlowCard.setFlowCardNo(getflowCardNumber(i));
            pathFlowCardNo = patchFlowCard.getFlowCardNo();
            patchFlowCard.setId(null);
            patchFlowCard.setDamageNum(0);
            patchFlowCard.setPatchNum(0);
            patchFlowCard.setTotalArea((patchFlowCard.getTotalArea() / patchFlowCard.getSplitNum())*patchBusiness.getPatchNum());
            patchFlowCard.setTotalWeight((patchFlowCard.getTotalWeight() / patchFlowCard.getSplitNum())*patchBusiness.getPatchNum());
            patchFlowCard.setSplitDate(new Date());
            patchFlowCard.setSplitNum(Integer.valueOf(patchBusiness.getPatchNum()));
            patchFlowCard.setSplitPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            patchFlowCard.setProductionStatus(0);
            patchFlowCard.setCompleteProcess("");
            patchFlowCard.setSchedulingDate(null);
            patchFlowCard.setSchedulingId(null);
            patchFlowCard.setSchedulingNum(null);
            patchFlowCard.setDeliveryDate(null);
            patchFlowCard.setDeliveryNo(null);
            patchFlowCard.setDeliveryPeople(null);
            patchFlowCard.setUpdatedDate(null);
            patchFlowCard.setUpdatedPeople(null);
            flowCardMapper.insert(patchFlowCard);
            LambdaQueryWrapper<ShelfDivisionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getFlowCardNo,patchBusiness.getFlowCardNo());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getProductId,patchBusiness.getProductId());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getSemiProductId,patchBusiness.getSemiProductId());
            ShelfDivisionBusiness divisionBusiness = shelfDivisionBusinessMapper.selectOne(businessLambdaQueryWrapper);
            divisionBusiness.setId(null);
            divisionBusiness.setFlowCardNo(patchFlowCard.getFlowCardNo());
            divisionBusiness.setFlowCardId(patchFlowCard.getId());
            divisionBusiness.setTotalArea((divisionBusiness.getTotalArea() / divisionBusiness.getItemNum())*patchBusiness.getPatchNum());
            divisionBusiness.setTotalWeight((divisionBusiness.getTotalWeight() / divisionBusiness.getItemNum())*patchBusiness.getPatchNum());
            divisionBusiness.setItemNum(Integer.valueOf(patchBusiness.getPatchNum()));
            shelfDivisionBusinessMapper.insert(divisionBusiness);
            //修改原流程卡补片数量--及补片流程卡
            if(flowCard.getPatchNum() == null){
                flowCard.setPatchNum(0);
            }
            flowCard.setPatchNum(Integer.valueOf(flowCard.getPatchNum()+patchBusiness.getPatchNum()));
            flowCard.setPatchFlowCardId(patchFlowCard.getId());
            flowCard.setPatchFlowCardNo(patchFlowCard.getFlowCardNo());
            flowCardMapper.updateById(flowCard);
            //修改报损信息
            LambdaQueryWrapper<DamageRecord> damageRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
            damageRecordLambdaQueryWrapper.eq(DamageRecord::getId,patchBusiness.getDamageId());
            DamageRecord damageRecord = damageRecordMapper.selectOne(damageRecordLambdaQueryWrapper);
            damageRecord.setPatchNum(damageRecord.getPatchNum()+patchBusiness.getPatchNum());
            if(damageRecord.getPatchArea() == null){
                damageRecord.setPatchArea("0");
            }
            damageRecord.setPatchArea(String.valueOf(Integer.parseInt(damageRecord.getPatchArea())+patchBusiness.getPatchArea()));
            damageRecordMapper.updateById(damageRecord);
            //修改补片明细数据
            patchBusiness.setPatchFlowCardId(patchFlowCard.getId());
            patchBusiness.setPatchFlowCardNo(patchFlowCard.getFlowCardNo());
            patchBusinesses.set(i,patchBusiness);
        }
        record.setPatchNo(getPatchNo());
        record.setPatchArea(String.valueOf(allPatchArea));
        record.setPatchDate(new Date());
        record.setPatchNum(String.valueOf(allPatchNum));
        record.setPatchPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        patchRecordMapper.insert(record);
        for (PatchBusiness b:patchBusinesses) {
            b.setPatchId(record.getId());
            patchBusinessMapper.insert(b);
        }
        return CommonResult.success(pathFlowCardNo);
    }

    @Override
    public CommonResult getDamageInfo(Map<String, String> data) {
        List<String> damageRecord  = Arrays.asList(data.get("ids").split(","));
        LambdaQueryWrapper<DamageFlowCard> damageFlowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        damageFlowCardLambdaQueryWrapper.in(DamageFlowCard::getDamageId,damageRecord);
        List<DamageFlowCard> damageFlowCardList = damageFlowCardMapper.selectList(damageFlowCardLambdaQueryWrapper);
        for (int i = 0;i < damageFlowCardList.size();i++){
            DamageFlowCard currentDamageFlowCard = damageFlowCardList.get(i);
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getFlowCardNo,currentDamageFlowCard.getFlowCardNo());
            FlowCard flowCard = flowCardMapper.selectOne(flowCardLambdaQueryWrapper);
            LambdaQueryWrapper<ShelfDivisionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getFlowCardNo,currentDamageFlowCard.getFlowCardNo());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getProductId,currentDamageFlowCard.getProductId());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getSemiProductId,currentDamageFlowCard.getSemiProductId());
            ShelfDivisionBusiness business = shelfDivisionBusinessMapper.selectOne(businessLambdaQueryWrapper);
            currentDamageFlowCard.setFlowCard(flowCard);
            currentDamageFlowCard.setBusiness(business);
            damageFlowCardList.set(i,currentDamageFlowCard);
        }
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,damageFlowCardList);
    }

    @Override
    public CommonResult getPatchList(Map<String,String> params) {
        LambdaQueryWrapper<PatchRecord> patchRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (params.containsKey("patchNo")){
            patchRecordLambdaQueryWrapper.like(PatchRecord::getPatchNo,params.get("patchNo"));
        }
        if(params.containsKey("patchDateStart") && params.containsKey("patchDateEnd")){
            patchRecordLambdaQueryWrapper.between(PatchRecord::getPatchDate,params.get("patchDateStart"),params.get("patchDateEnd"));
        }
        List<PatchRecord> patchRecords = patchRecordMapper.selectList(patchRecordLambdaQueryWrapper);
        int count = patchRecordMapper.selectCount(patchRecordLambdaQueryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,patchRecords);
        result.setCount(count);
        return result;
    }

    @Override
    public CommonResult getPatchInfo(String id) {
        PatchRecord record = patchRecordMapper.selectById(id);
        LambdaQueryWrapper<PatchBusiness> patchBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        patchBusinessLambdaQueryWrapper.eq(PatchBusiness::getPatchId,id);
        List<PatchBusiness> patchBusinesses = patchBusinessMapper.selectList(patchBusinessLambdaQueryWrapper);
        for (int i = 0;i<patchBusinesses.size();i++){
            PatchBusiness currentPatchBusiness = patchBusinesses.get(i);
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getFlowCardNo,currentPatchBusiness.getFlowCardNo());
            FlowCard flowCard = flowCardMapper.selectOne(flowCardLambdaQueryWrapper);
            LambdaQueryWrapper<ShelfDivisionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getFlowCardNo,currentPatchBusiness.getFlowCardNo());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getProductId,currentPatchBusiness.getProductId());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getSemiProductId,currentPatchBusiness.getSemiProductId());
            ShelfDivisionBusiness business = shelfDivisionBusinessMapper.selectOne(businessLambdaQueryWrapper);
            LambdaQueryWrapper<DamageFlowCard> damageFlowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getFlowCardNo,currentPatchBusiness.getFlowCardNo());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getProductId,currentPatchBusiness.getProductId());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getSemiProductId,currentPatchBusiness.getSemiProductId());
            DamageFlowCard damageFlowCard = damageFlowCardMapper.selectOne(damageFlowCardLambdaQueryWrapper);
            currentPatchBusiness.setFlowCardInfo(flowCard);
            currentPatchBusiness.setBusinessInfo(business);
            currentPatchBusiness.setDamageFlowCardInfo(damageFlowCard);
            patchBusinesses.set(i,currentPatchBusiness);
        }
        record.setPatchBusinessList(patchBusinesses);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,record);
    }

    @Override
    @Transactional
    public CommonResult delPatch(String id) {
        //查询补片单信息
        PatchRecord patchRecord = patchRecordMapper.selectById(id);
        //查询补片明细信息
        LambdaQueryWrapper<PatchBusiness> patchBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        patchBusinessLambdaQueryWrapper.eq(PatchBusiness::getPatchId,id);
        List<PatchBusiness> patchBusinesses = patchBusinessMapper.selectList(patchBusinessLambdaQueryWrapper);
        for (int i = 0;i < patchBusinesses.size();i++){
            PatchBusiness patchBusiness = patchBusinesses.get(i);
            //将原流程卡补片数量恢复
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getFlowCardNo,patchBusiness.getFlowCardNo());
            FlowCard flowCard = flowCardMapper.selectOne(flowCardLambdaQueryWrapper);
            flowCard.setPatchNum(0);
            flowCard.setPatchFlowCardNo("");
            flowCard.setPatchFlowCardId("");
            flowCardMapper.updateById(flowCard);
            //将补片流程卡删除
            flowCardMapper.deleteById(patchBusiness.getPatchFlowCardId());
            //将补片流程卡明细删除
            LambdaQueryWrapper<ShelfDivisionBusiness> shelfDivisionBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
            shelfDivisionBusinessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getFlowCardNo,patchBusiness.getPatchFlowCardNo());
            //恢复报损信息的内容
            DamageRecord damageRecord = damageRecordMapper.selectById(patchBusiness.getDamageId());
            damageRecord.setPatchNum(damageRecord.getPatchNum() - patchBusiness.getPatchNum());
            if (!StringUtils.isNotEmpty(damageRecord.getPatchArea())){
                damageRecord.setPatchArea("0");
            }
            damageRecord.setPatchArea((Integer.parseInt(damageRecord.getPatchArea()) - patchBusiness.getPatchArea())+"");
            damageRecordMapper.updateById(damageRecord);
            //获取工艺
            LambdaQueryWrapper<DamageFlowCard> damageFlowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getFlowCardNo,patchBusiness.getFlowCardNo());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getDamageId,damageRecord.getId());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getProductId,patchBusiness.getProductId());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getSemiProductId,patchBusiness.getSemiProductId());
            DamageFlowCard damageFlowCard = damageFlowCardMapper.selectOne(damageFlowCardLambdaQueryWrapper);
            //修改流程卡状态
            completionService.flowCardCompare(Arrays.asList(new String[]{patchBusiness.getFlowCardNo()}),damageFlowCard.getResponsibleProcess());
            //进行订单 订单产品的修改
            completionService.orderCompare(Arrays.asList(new String[]{patchBusiness.getFlowCardNo()}),damageFlowCard.getResponsibleProcess());
            //将补片记录删除
            patchBusinessMapper.deleteById(patchBusiness.getId());
        }
        patchRecordMapper.deleteById(patchRecord.getId());
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    @Override
    public CommonResult getCountList(Map<String, String> data) {
        String whereStr = "COALESCE(SUM(b.patch_num),0) patch_num," +
                "COALESCE(SUM(b.patch_area),0) patch_area ";
        String nameStr = "";
        if(data.containsKey("startDate") && data.containsKey("endDate")){
            whereStr = "SUM(case when r.patch_date >= '"+ data.get("startDate") +"' and r.patch_date <= '"+ data.get("endDate") +"' then b.patch_num else 0 end ) patch_num," +
                    "SUM(case when r.patch_date >= '"+ data.get("startDate") +"' and r.patch_date <= '"+ data.get("endDate") +"' then b.patch_area else 0 end ) patch_area ";
        }
        if (data.containsKey("product_name")) {
            nameStr = "AND p.product_name like '%"+data.get("product_name")+"%'";
        }
        String sql = "SELECT " +
                "DISTINCT p.product_name," +
                "p.id," +
                whereStr +
                "FROM " +
                "sys_semi_product s " +
                "LEFT JOIN sys_product p ON s.product_id = p.id " +
                "LEFT JOIN patch_business b ON b.semi_product_id = s.id " +
                "LEFT JOIN patch_record r ON b.patch_id = r.id "+
                "WHERE 1=1 "+
                nameStr +
                " GROUP BY p.product_name " +
                "ORDER BY patch_num DESC";
        List<Map<String,Object>> result = patchRecordMapper.getCountList(sql);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,result);
    }

    @Override
    public CommonResult getCountInfo(String id) {
        LambdaQueryWrapper<SysSemiProduct> sysSemiProductLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysSemiProductLambdaQueryWrapper.eq(SysSemiProduct::getProductId,id);
        List<SysSemiProduct> semiProducts = semiProductMapper.selectList(sysSemiProductLambdaQueryWrapper);
        List<String> semiProductIds = semiProducts.stream().map(SysSemiProduct::getId).collect(Collectors.toList());
        LambdaQueryWrapper<PatchBusiness> patchBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        patchBusinessLambdaQueryWrapper.in(PatchBusiness::getSemiProductId,semiProductIds);
        List<PatchBusiness> businesses = patchBusinessMapper.selectList(patchBusinessLambdaQueryWrapper);
        for (int i = 0; i < businesses.size(); i++){
            PatchBusiness currentPatchBusiness = businesses.get(i);
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getFlowCardNo,currentPatchBusiness.getFlowCardNo());
            FlowCard flowCard = flowCardMapper.selectOne(flowCardLambdaQueryWrapper);
            LambdaQueryWrapper<ShelfDivisionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getFlowCardNo,currentPatchBusiness.getFlowCardNo());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getProductId,currentPatchBusiness.getProductId());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getSemiProductId,currentPatchBusiness.getSemiProductId());
            ShelfDivisionBusiness business = shelfDivisionBusinessMapper.selectOne(businessLambdaQueryWrapper);
            LambdaQueryWrapper<DamageFlowCard> damageFlowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getFlowCardNo,currentPatchBusiness.getFlowCardNo());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getProductId,currentPatchBusiness.getProductId());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getSemiProductId,currentPatchBusiness.getSemiProductId());
            DamageFlowCard damageFlowCard = damageFlowCardMapper.selectOne(damageFlowCardLambdaQueryWrapper);
            currentPatchBusiness.setFlowCardInfo(flowCard);
            currentPatchBusiness.setBusinessInfo(business);
            currentPatchBusiness.setDamageFlowCardInfo(damageFlowCard);
            businesses.set(i,currentPatchBusiness);
        }
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,businesses);
    }

    @Override
    public void exportCount(HttpServletResponse response, String ids) {
        String whereStr = "";
        if (ids != null) {
            whereStr = "AND p.id in (";
            List<String> idList = Arrays.asList(ids.split(","));
            for (int i=0;i<idList.size();i++) {
                String id = idList.get(i);
                whereStr += "'"+id+"'";
                if(i < idList.size()-1){
                    whereStr+=",";
                }
            }
            whereStr+=") ";
        }
        String sql = "SELECT " +
                "DISTINCT p.product_name," +
                "p.id," +
                "COALESCE(SUM(b.patch_num),0) patch_num," +
                "COALESCE(SUM(b.patch_area),0) patch_area " +
                "FROM " +
                "sys_semi_product s " +
                "LEFT JOIN sys_product p ON s.product_id = p.id " +
                "LEFT JOIN patch_business b ON b.semi_product_id = s.id " +
                "LEFT JOIN patch_record r ON b.patch_id = r.id "+
                "WHERE 1 = 1 " +
                whereStr+
                "GROUP BY p.product_name " +
                "ORDER BY patch_num DESC";
        List<Map<String,Object>> result = patchRecordMapper.getCountList(sql);
        List<PatchCount> patchCount = new ArrayList<>();
        for (Map<String,Object> map:result) {
            PatchCount p = new PatchCount();
            p.setProductName(map.get("product_name").toString());
            p.setPatchNum(map.get("patch_num").toString());
            p.setPatchArea(map.get("patch_area").toString());
            patchCount.add(p);
        }
        ExcelUtil<PatchCount> util = new ExcelUtil<>(PatchCount.class);
        util.exportExcel(response, patchCount,"补片统计","补片统计");
    }

    @Transactional
    public String getflowCardNumber(int i) {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String substringFormat = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        LambdaQueryWrapper<FlowCard> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(FlowCard::getFlowCardNo, substringFormat);
        wrapper.like(FlowCard::getSplitDate, orderFormat.format(new Date()));
        wrapper.orderByDesc(FlowCard::getFlowCardNo).last("limit 1");
        FlowCard flowCard = flowCardMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("10");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        int integer = 1;
        if (flowCard != null) {
            String flowCardNo = flowCard.getFlowCardNo();
            String format1 = format.format(new Date()).substring(4);
            //拼接符
            String[] splicer = {"[+]", "-"};
            //遍历拆分     找出编号后缀
            for (String ss : splicer) {
                String[] split = flowCardNo.split(ss);
                if (split.length > 1) {
                    String s = split[split.length - 1];
                    int length = s.length();
                    if (length > 3) {
                        continue;
                    }
                    integer = Integer.parseInt(s) + 1 + i;
                }
            }
            if (integer == 1) {
                //说明上一个规则编号没有中后连接符
                String[] split = flowCardNo.split(format1);
                String s = split[split.length - 1];
                integer = Integer.parseInt(s) + 1 + i;
            }
            stringBuilder.append(sysNumberingRules.getRulePrefix());
            if (sysNumberingRules.getFrontConnection() != null && !sysNumberingRules.getFrontConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getFrontConnection());
            }

            stringBuilder.append(dateFormat.format(new Date()));
            if (sysNumberingRules.getRearConnection() != null && !sysNumberingRules.getRearConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getRearConnection());
            }
            if (sysNumberingRules.getRuleSuffix() == 0) {
                //规则为两位数
                if (String.valueOf(integer).length() >= 2) {
                    stringBuilder.append(integer);
                } else {
                    stringBuilder.append(0).append(integer);
                }
            } else {
                //规则为三位数
                if (String.valueOf(integer).length() >= 3) {
                    stringBuilder.append(integer);
                } else if (String.valueOf(integer).length() >= 2) {
                    stringBuilder.append(0).append(integer);
                } else {
                    stringBuilder.append(0).append(0).append(integer);
                }
            }
        } else {
            //当天第一个订单
            stringBuilder.append(sysNumberingRules.getRulePrefix());
            if (sysNumberingRules.getFrontConnection() != null && !sysNumberingRules.getFrontConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getFrontConnection());
            }

            stringBuilder.append(dateFormat.format(new Date()));
            if (sysNumberingRules.getRearConnection() != null && !sysNumberingRules.getRearConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getRearConnection());
            }
            integer += i;
            if (sysNumberingRules.getRuleSuffix() == 0) {
                //规则为两位数
                if (String.valueOf(integer).length() >= 2) {
                    stringBuilder.append(integer);
                } else {
                    stringBuilder.append(0).append(integer);
                }
            } else {
                //规则为三位数
                if (String.valueOf(integer).length() >= 3) {
                    stringBuilder.append(integer);
                } else if (String.valueOf(integer).length() >= 2) {
                    stringBuilder.append(0).append(integer);
                } else {
                    stringBuilder.append(0).append(0).append(integer);
                }
            }
        }
        return stringBuilder.toString();
    }
    @Transactional
    String getPatchNo() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<PatchRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(PatchRecord::getPatchNo, format1);
        wrapper.like(PatchRecord::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(PatchRecord::getCreatedAt).last("limit 1");
        PatchRecord record = patchRecordMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("bs");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (record != null) {
            String damageNo = record.getPatchNo();
            //查询拼接符字典
            String[] splicer = {"\\+", "-"};
            int integer = 1;
            //遍历拆分     找出编号后缀
            for (String ss : splicer) {
                String[] split = damageNo.split(ss);
                if (split.length > 1) {
                    String s = split[split.length - 1];
                    int length = s.length();
                    if (length > 3) {
                        continue;
                    }
                    integer = Integer.parseInt(s) + 1;
                }
            }
            if (integer == 1) {
                //说明上一个规则编号没有中后连接符
                String[] split = damageNo.split(format1);
                String s = split[split.length - 1];
                integer = Integer.parseInt(s) + 1;
            }
            stringBuilder.append(sysNumberingRules.getRulePrefix());
            if (sysNumberingRules.getFrontConnection() != null && !sysNumberingRules.getFrontConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getFrontConnection());
            }

            stringBuilder.append(dateFormat.format(date));
            if (sysNumberingRules.getRearConnection() != null && !sysNumberingRules.getRearConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getRearConnection());
            }
            if (sysNumberingRules.getRuleSuffix() == 0) {
                //规则为两位数
                if (String.valueOf(integer).length() >= 2) {
                    stringBuilder.append(integer);
                } else {
                    stringBuilder.append(0).append(integer);
                }
            } else {
                //规则为三位数
                if (String.valueOf(integer).length() >= 3) {
                    stringBuilder.append(integer);
                } else if (String.valueOf(integer).length() >= 2) {
                    stringBuilder.append(0).append(integer);
                } else {
                    stringBuilder.append(0).append(0).append(integer);
                }
            }
        } else {
            //当天第一个订单
            stringBuilder.append(sysNumberingRules.getRulePrefix());
            if (sysNumberingRules.getFrontConnection() != null && !sysNumberingRules.getFrontConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getFrontConnection());
            }

            stringBuilder.append(dateFormat.format(date));
            if (sysNumberingRules.getRearConnection() != null && !sysNumberingRules.getRearConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getRearConnection());
            }
            if (sysNumberingRules.getRuleSuffix() == 0) {
                stringBuilder.append("01");
            } else {
                stringBuilder.append("001");
            }
        }
        return stringBuilder.toString();
    }
}
