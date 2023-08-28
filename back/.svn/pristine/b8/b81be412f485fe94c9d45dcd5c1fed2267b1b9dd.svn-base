package com.erp.produce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.produce.SchedulingMapper;
import com.erp.floor.mapper.sales.FlowCardMapper;
import com.erp.floor.mapper.sales.OrderMapper;
import com.erp.floor.mapper.sales.ShelfDivisionBusinessMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.produce.domain.DamageRecord;
import com.erp.floor.pojo.produce.domain.PatchRecord;
import com.erp.floor.pojo.produce.domain.Scheduling;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.ShelfDivisionBusiness;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.produce.service.SchedulingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/19 15:06
 */
@Service
public class SchedulingServiceImpl extends ServiceImpl<SchedulingMapper, Scheduling> implements SchedulingService {

    @Resource
    private SchedulingMapper schedulingMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private FlowCardMapper flowCardMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ShelfDivisionBusinessMapper shelfDivisionBusinessMapper;
    @Override
    @Transactional
    public CommonResult addScheduling(Scheduling scheduling) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        scheduling.setSchedulingNo(getSchedulingNo());
        scheduling.setSchedulingPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        scheduling.setSchedulingDate(new Date());
        schedulingMapper.insert(scheduling);
        int schedulingNum = 0;
        int schedulingArea = 0;
        for (int i = 0;i < scheduling.getFlowCardList().size();i++) {
            FlowCard flowCard = scheduling.getFlowCardList().get(i);
            schedulingNum += flowCard.getSplitNum();
            schedulingArea += flowCard.getTotalArea();
            flowCard.setSchedulingId(scheduling.getId());
            flowCard.setSchedulingDate(scheduling.getSchedulingDate());
            flowCardMapper.updateById(flowCard);
        }
        scheduling.setSchedulingNum(schedulingNum+"");
        scheduling.setSchedulingArea(schedulingArea+"");
        schedulingMapper.updateById(scheduling);
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    @Override
    @Transactional
    public CommonResult editScheduling(Scheduling scheduling) {
        List<FlowCard> paramFlowCards = scheduling.getFlowCardList();
        LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flowCardLambdaQueryWrapper.eq(FlowCard::getSchedulingId,scheduling.getId());
        List<FlowCard> flowCardList = flowCardMapper.selectList(flowCardLambdaQueryWrapper);
        List<String> oldListStr = flowCardList.stream().map(FlowCard::getFlowCardNo).collect(Collectors.toList());
        List<String> newListStr = paramFlowCards.stream().map(FlowCard::getFlowCardNo).collect(Collectors.toList());
        List<FlowCard> addList = paramFlowCards.stream().filter(f -> !oldListStr.contains(f.getFlowCardNo())).collect(Collectors.toList());
        List<FlowCard> remList = flowCardList.stream().filter(f -> !newListStr.contains(f.getFlowCardNo())).collect(Collectors.toList());
        for (FlowCard f:addList) {
            f.setSchedulingId(scheduling.getId());
            f.setSchedulingDate(scheduling.getSchedulingDate());
            flowCardMapper.updateById(f);
        }
        for (FlowCard f:remList) {
            f.setSchedulingId("");
            f.setSchedulingDate(null);
            flowCardMapper.updateById(f);
        }
        schedulingMapper.updateById(scheduling);
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }
    @Override
    @Transactional
    public CommonResult delScheduling(String ids) {
        List<String> idsList = Arrays.asList(ids.split(","));
        for (String id:idsList) {
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(FlowCard::getSchedulingId,id);
            List<FlowCard> flowCardList = flowCardMapper.selectList(flowCardLambdaQueryWrapper);
            for (FlowCard f:flowCardList) {
                f.setSchedulingId(null);
                f.setSchedulingDate(null);
                flowCardMapper.updateById(f);
            }
            schedulingMapper.deleteById(id);
        }
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    @Override
    public CommonResult getSchedulingList(Scheduling scheduling) {
        LambdaQueryWrapper<Scheduling> schedulingLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(scheduling.getStartDate() != null && scheduling.getEndDate() != null){
            schedulingLambdaQueryWrapper.between(Scheduling::getSchedulingDate,scheduling.getStartDate(),scheduling.getEndDate());
        }
        if(StringUtils.isNotEmpty(scheduling.getSchedulingNo())){
            schedulingLambdaQueryWrapper.like(Scheduling::getSchedulingNo,scheduling.getSchedulingNo());
        }
        if(StringUtils.isNotEmpty(scheduling.getRemarks())){
            schedulingLambdaQueryWrapper.like(Scheduling::getRemarks,scheduling.getRemarks());
        }
        List<Scheduling> schedulingList = schedulingMapper.selectList(schedulingLambdaQueryWrapper);
        int count = schedulingMapper.selectCount(schedulingLambdaQueryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,schedulingList);
        result.setCount(count);
        return result;
    }

    @Override
    public CommonResult getInfo(String id) {
        Scheduling scheduling = schedulingMapper.selectById(id);
        LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flowCardLambdaQueryWrapper.eq(FlowCard::getSchedulingId,id);
        List<FlowCard> flowCardList = flowCardMapper.selectList(flowCardLambdaQueryWrapper);
        scheduling.setFlowCardList(flowCardList);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,scheduling);
    }

    @Override
    public CommonResult getOrderList(OrderRecord orderRecord) {
        LambdaQueryWrapper<OrderRecord> orderRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(orderRecord.getOrderNo())) {
            orderRecordLambdaQueryWrapper.like(OrderRecord::getOrderNo,orderRecord.getOrderNo());
        }
        if (StringUtils.isNotEmpty(orderRecord.getCustomNo())) {
            orderRecordLambdaQueryWrapper.like(OrderRecord::getCustomNo,orderRecord.getCustomNo());
        }
        if (StringUtils.isNotEmpty(orderRecord.getCustomerName())) {
            orderRecordLambdaQueryWrapper.like(OrderRecord::getCustomerName,orderRecord.getCustomerName());
        }
        if (StringUtils.isNotEmpty(orderRecord.getEntryName())) {
            orderRecordLambdaQueryWrapper.like(OrderRecord::getEntryName,orderRecord.getEntryName());
        }
        List<OrderRecord> orderRecords = orderMapper.selectList(orderRecordLambdaQueryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,orderRecords);
        result.setCount(orderMapper.selectCount(orderRecordLambdaQueryWrapper));
        return result;
    }

    @Override
    public CommonResult getFlowCardList(FlowCard flowCard, Map<String, String> params) {
        LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(params.containsKey("orderNos")){
            List<String> orderNos = Arrays.asList(params.get("orderNos").split(","));
            flowCardLambdaQueryWrapper.in(FlowCard::getOrderNo,orderNos);
        }else{
            if(StringUtils.isNotEmpty(flowCard.getFlowCardNo())){
                flowCardLambdaQueryWrapper.like(FlowCard::getFlowCardNo,flowCard.getFlowCardNo());
            }
            if(StringUtils.isNotEmpty(flowCard.getOrderNo())){
                flowCardLambdaQueryWrapper.like(FlowCard::getOrderNo,flowCard.getOrderNo());
            }
            if(StringUtils.isNotEmpty(flowCard.getEntryName())){
                flowCardLambdaQueryWrapper.like(FlowCard::getEntryName,flowCard.getEntryName());
            }
            if(StringUtils.isNotEmpty(flowCard.getCustomerName())){
                flowCardLambdaQueryWrapper.like(FlowCard::getCustomerName,flowCard.getCustomerName());
            }
        }
        flowCardLambdaQueryWrapper.eq(FlowCard::getProductionStatus,0);
        List<FlowCard> flowCardList = flowCardMapper.selectList(flowCardLambdaQueryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,flowCardList);
        result.setCount(flowCardMapper.selectCount(flowCardLambdaQueryWrapper));
        return result;
    }

    @Override
    public void exportAll(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<Scheduling> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(Scheduling::getId, split);
        }
        wrapper.orderByDesc(Scheduling::getCreatedAt);
        List<Scheduling> record = schedulingMapper.selectList(wrapper);
        ExcelUtil<Scheduling> util = new ExcelUtil<>(Scheduling.class);
        util.exportExcel(response, record,"生产计划","生产计划");
    }

    @Override
    public void exportDetail(HttpServletResponse response, String id) {
        LambdaQueryWrapper<FlowCard> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FlowCard::getSchedulingId, id);
        List<FlowCard> record = flowCardMapper.selectList(wrapper);
        ExcelUtil<FlowCard> util = new ExcelUtil<>(FlowCard.class);
        util.exportExcel(response, record,"生产计划明细","生产计划明细");
    }

    @Transactional
    String getSchedulingNo() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<Scheduling> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Scheduling::getSchedulingNo, format1);
        wrapper.like(Scheduling::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(Scheduling::getCreatedAt).last("limit 1");
        Scheduling record = schedulingMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("pc");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (record != null) {
            String damageNo = record.getSchedulingNo();
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
