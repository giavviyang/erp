package com.erp.produce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.produce.CompletionBusinessMpper;
import com.erp.floor.mapper.produce.DamageFlowCardMapper;
import com.erp.floor.mapper.produce.DamageRecordMapper;
import com.erp.floor.mapper.sales.FlowCardMapper;
import com.erp.floor.mapper.sales.ShelfDivisionBusinessMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.produce.domain.CompletionBusiness;
import com.erp.floor.pojo.produce.domain.CompletionRecord;
import com.erp.floor.pojo.produce.domain.DamageFlowCard;
import com.erp.floor.pojo.produce.domain.DamageRecord;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.ShelfDivisionBusiness;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.produce.service.DamageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/6 11:32
 */

@Service
public class DamageServiceImpl extends ServiceImpl<DamageRecordMapper, DamageRecord> implements DamageService {

    @Resource
    private DamageRecordMapper recordMapper;
    @Resource
    private DamageFlowCardMapper damageFlowCardMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;

    @Resource
    private FlowCardMapper flowCardMapper;

    @Resource
    private ShelfDivisionBusinessMapper divisionBusinessMapper;

    @Resource
    private CompletionBusinessMpper completionBusinessMpper;

    @Override
    @Transactional
    public CommonResult insertDamage(DamageRecord record) throws Exception {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        List<DamageFlowCard> damageFlowCardList = record.getDamageFlowCardList();
        Set<String> flowCardSet = new HashSet<>();
        damageFlowCardList.forEach(item -> {
            flowCardSet.add(item.getFlowCardNo());
        });
        Map<String, Map<String, List<DamageFlowCard>>> flowCardMap = new HashMap<>();
        for (String flowCardNo : flowCardSet) {
            List<DamageFlowCard> flowCardGroupByList = damageFlowCardList.stream().filter(s -> s.getFlowCardNo().equals(flowCardNo)).collect(Collectors.toList());
            Set<String> productSet = new HashSet<>();
            flowCardGroupByList.forEach(item -> {
                productSet.add(item.getProductId());
            });
            Map<String, List<DamageFlowCard>> productMap = new HashMap<>();
            productSet.forEach(item -> {
                List<DamageFlowCard> groupByList = flowCardGroupByList.stream().filter(s -> s.getProductId().equals(item)).collect(Collectors.toList());
                productMap.put(item, groupByList);
            });
            flowCardMap.put(flowCardNo, productMap);
        }
        for (Map.Entry<String, Map<String, List<DamageFlowCard>>> entry : flowCardMap.entrySet()) {
            String flowCraftNo = entry.getKey();
            Map<String, List<DamageFlowCard>> map = entry.getValue();
            for (Map.Entry<String, List<DamageFlowCard>> entrychild : map.entrySet()) {
                List<DamageFlowCard> value = entrychild.getValue();
                record.setId(null);
                record.setDamageNo(productionNumber());
                record.setFlowCardNo(flowCraftNo);
                int area = 0;
                int num = 0;
                for (DamageFlowCard flowCard : value) {
                    area += flowCard.getDamageArea();
                    num += flowCard.getDamageNum();
                }
                record.setDamageArea(area);
                record.setDamageNum(num);
                int i = recordMapper.insert(record);
                if (i <= 0) {
                    throw new Exception();
                }
                for (DamageFlowCard flowCard : value) {
                    flowCard.setDamageId(record.getId());
                    i = damageFlowCardMapper.insert(flowCard);
                    if (i <= 0) {
                        throw new Exception();
                    }
                }
            }
        }
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    @Override
    @Transactional
    public CommonResult editDamage(String id, DamageRecord record) throws Exception {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        record.setId(id);
        int i = recordMapper.updateById(record);
        if (i <= 0) {
            throw new Exception();
        }
        List<DamageFlowCard> delList = record.getDamageFlowCardList().stream().filter(s -> s.getDamageNum() == 0).collect(Collectors.toList());
        for (DamageFlowCard flowCard : delList) {
            i = damageFlowCardMapper.deleteById(flowCard.getId());
            if (i <= 0) {
                throw new Exception();
            }
        }
        List<DamageFlowCard> damageFlowCardList = record.getDamageFlowCardList().stream().filter(s -> s.getDamageNum() != 0).collect(Collectors.toList());
        for (DamageFlowCard flowCard : damageFlowCardList) {
            flowCard.setDamageId(record.getId());
            if (StringUtils.isNotEmpty(flowCard.getId())) {
                i = damageFlowCardMapper.updateById(flowCard);
                if (i <= 0) {
                    throw new Exception();
                }
            } else {
                i = damageFlowCardMapper.insert(flowCard);
                if (i <= 0) {
                    throw new Exception();
                }
            }

        }
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    @Override
    @Transactional
    public CommonResult delDamage(String ids) throws Exception {
        List<String> split = Arrays.asList(ids.split(","));
        int i = recordMapper.deleteBatchIds(split);
        if (i <= 0) {
            throw new Exception();
        }
        LambdaQueryWrapper<DamageFlowCard> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DamageFlowCard::getDamageId, split);
        i = damageFlowCardMapper.delete(wrapper);
        if (i <= 0) {
            throw new Exception();
        }
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    @Override
    public CommonResult getDamageList(Map<String, Object> params) {
        List<DamageRecord> records = recordMapper.selectDamageList(params);
        List<DamageRecord> recordList = recordMapper.selectDamageAll(params);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS, records);
        result.setCount(recordList.size());
        return result;
    }

    @Override
    public CommonResult getOrderList(ShelfDivisionBusiness business) {
        LambdaQueryWrapper<ShelfDivisionBusiness> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(business.getOrderNo())) {
            wrapper.like(ShelfDivisionBusiness::getOrderNo, business.getOrderNo());
        }
        if (StringUtils.isNotEmpty(business.getCustomNo())) {
            wrapper.like(ShelfDivisionBusiness::getCustomNo, business.getCustomNo());
        }
        if (StringUtils.isNotEmpty(business.getCustomerName())) {
            wrapper.like(ShelfDivisionBusiness::getCustomerName, business.getCustomerName());
        }
        if (StringUtils.isNotEmpty(business.getEntryName())) {
            wrapper.like(ShelfDivisionBusiness::getEntryName, business.getEntryName());
        }
        wrapper.ne(ShelfDivisionBusiness::getItemSurface, "中间层");
        List<ShelfDivisionBusiness> businesses = divisionBusinessMapper.selectList(wrapper);
        for (int i = 0; i < businesses.size(); i++) {
            ShelfDivisionBusiness b = businesses.get(i);
            LambdaQueryWrapper<DamageFlowCard> damageFlowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getFlowCardNo, b.getFlowCardNo());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getProductId, b.getProductId());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getSemiProductId, b.getSemiProductId());
            List<DamageFlowCard> flowCards = damageFlowCardMapper.selectList(damageFlowCardLambdaQueryWrapper);
            int num = 0;
            for (DamageFlowCard f : flowCards) {
                num += f.getDamageNum();
            }
            b.setDamageNum(num);
            if (num == b.getItemNum()) {
                businesses.remove(i);
            }
        }
        //获取当前明细的当前工艺 及 当前工艺下的完工数量
        for (int i = 0; i < businesses.size(); i++) {
            ShelfDivisionBusiness divisionBusiness = businesses.get(i);
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getFlowCardNo, divisionBusiness.getFlowCardNo());
            flowCardLambdaQueryWrapper.eq(FlowCard::getId, divisionBusiness.getFlowCardId());
            FlowCard flowCard = flowCardMapper.selectOne(flowCardLambdaQueryWrapper);
            //判断是否完工 完工了就不能报损了
            if (flowCard != null && flowCard.getProductionStatus() != 2) {
                //查询待完成的下一工艺
                if (!StringUtils.isNotEmpty(flowCard.getCompleteProcess())) {
                    flowCard.setCompleteProcess("");
                }
                String[] craftArr = Arrays.stream(flowCard.getCompleteProcess().split(" → ")).filter(s -> !"".equals(s)).toArray(String[]::new);
                String currentCraft = flowCard.getProcessContent().split(" → ")[craftArr.length];
                divisionBusiness.setCurrentCraft(currentCraft);
                //TODO 查询工艺下的完工数量
                LambdaQueryWrapper<CompletionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                businessLambdaQueryWrapper.eq(CompletionBusiness::getFlowCardNo, divisionBusiness.getFlowCardNo());
                businessLambdaQueryWrapper.eq(CompletionBusiness::getProductId, divisionBusiness.getProductId());
                businessLambdaQueryWrapper.eq(CompletionBusiness::getSemiProductId, divisionBusiness.getSemiProductId());
                businessLambdaQueryWrapper.eq(CompletionBusiness::getCraft, currentCraft);
                List<CompletionBusiness> completionBusiness = completionBusinessMpper.selectList(businessLambdaQueryWrapper);
                int completeNum = 0;
                for (CompletionBusiness b : completionBusiness) {
                    completeNum += b.getNum();
                }
                divisionBusiness.setCompleteNum(completeNum);
                businesses.set(i, divisionBusiness);
            } else {
                businesses.remove(i);
            }
        }
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS, businesses);
        result.setCount(businesses.size());
        if (StringUtils.isNotEmpty(business.getOrderNo()) &&
                StringUtils.isNotEmpty(business.getCustomNo()) &&
                StringUtils.isNotEmpty(business.getCustomerName()) &&
                StringUtils.isNotEmpty(business.getEntryName())) {
            CommonResult all = getOrderList(new ShelfDivisionBusiness());
            result.setCount(all.getCount());
        }
        return result;
    }

    @Override
    public CommonResult getFlowCardList(ShelfDivisionBusiness business) {
        LambdaQueryWrapper<ShelfDivisionBusiness> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(business.getFlowCardNo())) {
            wrapper.like(ShelfDivisionBusiness::getFlowCardNo, business.getFlowCardNo());
        }
        if (StringUtils.isNotEmpty(business.getOrderNo())) {
            wrapper.like(ShelfDivisionBusiness::getOrderNo, business.getOrderNo());
        }
        if (StringUtils.isNotEmpty(business.getEntryName())) {
            wrapper.like(ShelfDivisionBusiness::getEntryName, business.getEntryName());
        }
        if (StringUtils.isNotEmpty(business.getCustomerName())) {
            wrapper.like(ShelfDivisionBusiness::getCustomerName, business.getCustomerName());
        }
        wrapper.ne(ShelfDivisionBusiness::getItemSurface, "中间层");
        List<ShelfDivisionBusiness> businesses = divisionBusinessMapper.selectList(wrapper);
        for (int i = 0; i < businesses.size(); i++) {
            ShelfDivisionBusiness b = businesses.get(i);
            LambdaQueryWrapper<DamageFlowCard> damageFlowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getFlowCardNo, b.getFlowCardNo());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getProductId, b.getProductId());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getSemiProductId, b.getSemiProductId());
            List<DamageFlowCard> flowCards = damageFlowCardMapper.selectList(damageFlowCardLambdaQueryWrapper);
            int num = 0;
            for (DamageFlowCard f : flowCards) {
                num += f.getDamageNum();
            }
            b.setDamageNum(num);
            if (num == b.getItemNum()) {
                businesses.remove(i);
            }
        }
        //获取当前明细的当前工艺 及 当前工艺下的完工数量
        for (int i = 0; i < businesses.size(); i++) {
            ShelfDivisionBusiness divisionBusiness = businesses.get(i);
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getFlowCardNo, divisionBusiness.getFlowCardNo());
            flowCardLambdaQueryWrapper.eq(FlowCard::getId, divisionBusiness.getFlowCardId());
            FlowCard flowCard = flowCardMapper.selectOne(flowCardLambdaQueryWrapper);
            //判断是否完工 完工了就不能报损了
            if (flowCard != null && flowCard.getProductionStatus() != 2) {
                //查询待完成的下一工艺
                if (!StringUtils.isNotEmpty(flowCard.getCompleteProcess())) {
                    flowCard.setCompleteProcess("");
                }
                String[] craftArr = Arrays.stream(flowCard.getCompleteProcess().split(" → ")).filter(s -> !"".equals(s)).toArray(String[]::new);

                String currentCraft = flowCard.getProcessContent().split(" → ")[craftArr.length];
                divisionBusiness.setCurrentCraft(currentCraft);
                //TODO 查询工艺下的完工数量
                LambdaQueryWrapper<CompletionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                businessLambdaQueryWrapper.eq(CompletionBusiness::getFlowCardNo, divisionBusiness.getFlowCardNo());
                businessLambdaQueryWrapper.eq(CompletionBusiness::getProductId, divisionBusiness.getProductId());
                businessLambdaQueryWrapper.eq(CompletionBusiness::getSemiProductId, divisionBusiness.getSemiProductId());
                businessLambdaQueryWrapper.eq(CompletionBusiness::getCraft, currentCraft);
                businessLambdaQueryWrapper.ne(CompletionBusiness::getItemSurface, "中间层");
                List<CompletionBusiness> completionBusiness = completionBusinessMpper.selectList(businessLambdaQueryWrapper);
                int completeNum = 0;
                for (CompletionBusiness b : completionBusiness) {
                    completeNum += b.getNum();
                }
                divisionBusiness.setCompleteNum(completeNum);
                businesses.set(i, divisionBusiness);
            } else {
                businesses.remove(i);
            }
        }
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS, businesses);
        result.setCount(businesses.size());
        if (StringUtils.isNotEmpty(business.getOrderNo()) &&
                StringUtils.isNotEmpty(business.getFlowCardNo()) &&
                StringUtils.isNotEmpty(business.getCustomerName()) &&
                StringUtils.isNotEmpty(business.getEntryName())) {
            CommonResult all = getFlowCardList(new ShelfDivisionBusiness());
            result.setCount(all.getCount());
        }
        return result;
    }

    @Override
    public CommonResult getDamageNo() {
        Map<String, Object> result = new HashMap<>();
        result.put("damageNo", productionNumber());
        result.put("damageUser", SecurityUtils.getLoginUser().getUser().getNickName());
        return CommonResult.success(ResultConstants.QUERY_SUCCESS, result);
    }

    @Override
    public CommonResult getDamageDetail(String id) {
        DamageRecord record = recordMapper.selectById(id);
        LambdaQueryWrapper<DamageFlowCard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DamageFlowCard::getDamageId, id);
        List<DamageFlowCard> flowCards = damageFlowCardMapper.selectList(wrapper);
        for (int i = 0; i < flowCards.size(); i++) {
            DamageFlowCard flowCard = flowCards.get(i);
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getFlowCardNo, flowCard.getFlowCardNo());
            FlowCard card = flowCardMapper.selectOne(flowCardLambdaQueryWrapper);
            LambdaQueryWrapper<ShelfDivisionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getFlowCardNo, flowCard.getFlowCardNo());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getProductId, flowCard.getProductId());
            businessLambdaQueryWrapper.eq(ShelfDivisionBusiness::getSemiProductId, flowCard.getSemiProductId());
            ShelfDivisionBusiness business = divisionBusinessMapper.selectOne(businessLambdaQueryWrapper);
            flowCard.setFlowCard(card);
            flowCard.setBusiness(business);
            flowCards.set(i, flowCard);
        }
        record.setDamageFlowCardList(flowCards);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS, record);
    }

    @Override
    public void exportDamage(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<DamageRecord> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(DamageRecord::getId, split);
        }
        wrapper.orderByDesc(DamageRecord::getCreatedAt);
        List<DamageRecord> damageRecord = recordMapper.selectList(wrapper);
        ExcelUtil<DamageRecord> util = new ExcelUtil<>(DamageRecord.class);
        util.exportExcel(response, damageRecord, "报损数据", "报损数据");
    }

    @Transactional
    String productionNumber() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<DamageRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(DamageRecord::getDamageNo, format1);
        wrapper.like(DamageRecord::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(DamageRecord::getCreatedAt).last("limit 1");
        DamageRecord record = recordMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("bs");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (record != null) {
            String damageNo = record.getDamageNo();
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
