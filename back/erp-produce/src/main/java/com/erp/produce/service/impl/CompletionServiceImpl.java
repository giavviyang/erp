package com.erp.produce.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.produce.*;
import com.erp.floor.mapper.sales.*;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.mapper.system.SysSemiProductMapper;
import com.erp.floor.pojo.produce.domain.*;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.floor.pojo.system.domain.SysSemiProduct;
import com.erp.produce.service.CompletionService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.nio.MappedByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/29 14:45
 */
@Service
public class CompletionServiceImpl extends ServiceImpl<CompletionRecordMapper, CompletionRecord> implements CompletionService {

    @Resource
    private CompletionRecordMapper mapper;
    @Resource
    private CompletionPersonMapper personMapper;
    @Resource
    private CompletionBusinessMpper businessMpper;
    @Resource
    private FlowCardMapper flowCardMapper;
    @Resource
    private ShelfDivisionBusinessMapper divisionBusinessMapper;

    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;

    @Resource
    private ProductProcessMapper productProcessMapper;

    @Resource
    private OrderProductMapper orderProductMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private DamageFlowCardMapper damageFlowCardMapper;

    @Resource
    private PatchRecordMapper patchRecordMapper;

    @Resource
    private SysSemiProductMapper semiProductMapper;


    @Override
    @Transactional
    public CommonResult insertCompletion(CompletionRecord record) throws Exception {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        record.setCompletionNo(productionNumber());
        List<CompletionPerson> persons = record.getCompletionPersonList();
        List<CompletionBusiness> businesses = record.getCompletionBusinessList();
        int i = mapper.insert(record);
        if (i <= 0) {
            throw new Exception(ResultConstants.SAVE_ERROR);
        }
        for (CompletionPerson p : persons) {
            p.setCompletionRecordId(record.getId());
            i = personMapper.insert(p);
            if (i <= 0) {
                throw new Exception(ResultConstants.SAVE_ERROR);
            }
        }
        String[] flowCardNoArr = record.getFlowCardNo().split(",");
        Map<String, List<CompletionBusiness>> groupByBusiness = new HashMap<>();
        for (String item : flowCardNoArr) {
            List<CompletionBusiness> currentCB = businesses.stream().filter(s -> s.getFlowCardNo().equals(item)).collect(Collectors.toList());
            groupByBusiness.put(item, currentCB);
        }
        groupByBusiness.forEach((key, value) -> {
            for (CompletionBusiness b : value) {
                b.setRecordId(record.getId());
                businessMpper.insert(b);
            }
            LambdaQueryWrapper<FlowCard> flowCardQueryWrapper = new LambdaQueryWrapper<>();
            flowCardQueryWrapper.eq(FlowCard::getFlowCardNo, key);
            FlowCard flowCard = flowCardMapper.selectOne(flowCardQueryWrapper);
            List<String> craftFlow = Arrays.asList(flowCard.getProcessContent().split(" → "));
            if (craftFlow.indexOf(record.getCompletionCraftName()) == 0) {
                //第一道工艺 0=未生产,1=生产中,2=完成生产
                List<ShelfDivisionBusiness> currentList = getComparisonResults(key, record.getCompletionCraftName());
                if (flowCard.getProductionStatus() == 0 || flowCard.getProductionStatus() == 1) {
                    FlowCard currentFlowCard = new FlowCard();
                    currentFlowCard.setProductionStatus(1);
                    if (currentList.size() == 0) {
                        if (flowCard.getCompleteProcess() == null || flowCard.getCompleteProcess().equals("")) {
                            currentFlowCard.setCompleteProcess(record.getCompletionCraftName());
                        } else {
                            currentFlowCard.setCompleteProcess(flowCard.getCompleteProcess() + " → " + record.getCompletionCraftName());
                        }
                    }
                    LambdaUpdateWrapper<FlowCard> flowCardWrapper = new LambdaUpdateWrapper<>();
                    flowCardWrapper.eq(FlowCard::getFlowCardNo, key);
                    flowCardMapper.update(currentFlowCard, flowCardWrapper);
                }
                //如果订单状态是未生产则改成生产中
                LambdaUpdateWrapper<OrderRecord> orderRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                orderRecordLambdaUpdateWrapper.in(OrderRecord::getOrderNo, Arrays.asList(flowCard.getOrderNo().split(", ")));
                List<OrderRecord> orderRecords = orderMapper.selectList(orderRecordLambdaUpdateWrapper);
                for (OrderRecord orderRecord:orderRecords) {
                    if (orderRecord.getProductionStatus() == 0) {
                        orderRecord.setProductionStatus(1);
                        orderMapper.updateById(orderRecord);
                    }
                }
            } else if (craftFlow.indexOf(record.getCompletionCraftName()) == craftFlow.size() - 1) {
                //最后一道工艺
                List<ShelfDivisionBusiness> currentList = getComparisonResults(key, record.getCompletionCraftName());
                if (currentList.size() == 0) {
                    FlowCard currentFlowCard = new FlowCard();
                    currentFlowCard.setProductionStatus(2);
                    if (flowCard.getCompleteProcess() == null || flowCard.getCompleteProcess().equals("")) {
                        currentFlowCard.setCompleteProcess(record.getCompletionCraftName());
                    } else {
                        currentFlowCard.setCompleteProcess(flowCard.getCompleteProcess() + " → " + record.getCompletionCraftName());
                    }
                    LambdaUpdateWrapper<FlowCard> flowCardWrapper = new LambdaUpdateWrapper<>();
                    flowCardWrapper.eq(FlowCard::getFlowCardNo, key);
                    flowCardMapper.update(currentFlowCard, flowCardWrapper);
                }
                //将相对应的订单半产品进行修改
                for (CompletionBusiness b : value) {
                    LambdaQueryWrapper<ProductProcess> productProcessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    productProcessLambdaQueryWrapper.in(ProductProcess::getOrderNo, Arrays.asList(flowCard.getOrderNo().split(", ")));
                    productProcessLambdaQueryWrapper.eq(ProductProcess::getProductId, b.getProductId());
                    productProcessLambdaQueryWrapper.eq(ProductProcess::getSemiProductId, b.getSemiProductId());
                    List<ProductProcess> productProcessList = productProcessMapper.selectList(productProcessLambdaQueryWrapper);
                    for (ProductProcess productProcess:
                    productProcessList) {
                        if (productProcess.getCompletionNum() == null || productProcess.getCompletionNum() == 0) {
                            productProcess.setCompletionNum(b.getNum());
                        } else {
                            productProcess.setCompletionNum(productProcess.getCompletionNum() + b.getNum());
                        }
                        productProcessMapper.updateById(productProcess);
                    }

                    LambdaQueryWrapper<CompletionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    businessLambdaQueryWrapper.eq(CompletionBusiness::getId,b.getId());
                    CompletionBusiness business = new CompletionBusiness();
                    business.setLastCraft(1);
                    businessMpper.update(business,businessLambdaQueryWrapper);
                }
                //待计算订单产品完成数量
                LambdaQueryWrapper<OrderProduct> orderProductLambdaQueryWrapper = new LambdaQueryWrapper<>();
                orderProductLambdaQueryWrapper.in(OrderProduct::getOrderNo, Arrays.asList(flowCard.getOrderNo().split(", ")));
                List<OrderProduct> orderProducts = orderProductMapper.selectList(orderProductLambdaQueryWrapper);
                for (OrderProduct o : orderProducts) {
                    String[] orderNos = flowCard.getOrderNo().split(", ");
                    for (String orderNo:
                         orderNos) {
                        LambdaQueryWrapper<ProductProcess> productProcessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getOrderNo, orderNo);
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getProductId, o.getId());
                        productProcessLambdaQueryWrapper.orderByAsc(ProductProcess::getCompletionNum);
                        List<ProductProcess> productProcess = productProcessMapper.selectList(productProcessLambdaQueryWrapper);
                        if(productProcess.size() > 0){
                            o.setCompletionNum(productProcess.get(0).getCompletionNum());
                            orderProductMapper.updateById(o);
                        }
                    }

                }
                //判断订单下所有流程卡的状态是否已完成 如果完成 订单则完成
                LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
                flowCardLambdaQueryWrapper.in(FlowCard::getOrderNo, Arrays.asList(flowCard.getOrderNo().split(", ")));
                flowCardLambdaQueryWrapper.ne(FlowCard::getProductionStatus, 2);
                List<FlowCard> flowCards = flowCardMapper.selectList(flowCardLambdaQueryWrapper);
                if (flowCards.size() == 0) {
                    OrderRecord orderRecord = new OrderRecord();
                    orderRecord.setProductionStatus(2);
                    String[] orderNos = flowCard.getOrderNo().split(", ");
                    for (String orderNo:
                         orderNos) {
                        LambdaQueryWrapper<OrderRecord> orderRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        orderRecordLambdaQueryWrapper.eq(OrderRecord::getOrderNo,orderNo);
                        OrderRecord currentOrder = orderMapper.selectOne(orderRecordLambdaQueryWrapper);
                        if(currentOrder.getRackSplittingStatus() == 2){
                            LambdaUpdateWrapper<OrderRecord> orderRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                            orderRecordLambdaUpdateWrapper.in(OrderRecord::getOrderNo, orderNo);
                            orderMapper.update(orderRecord, orderRecordLambdaUpdateWrapper);
                        }
                    }

                }
            } else {
                List<ShelfDivisionBusiness> currentList = getComparisonResults(key, record.getCompletionCraftName());
                if (currentList.size() == 0) {
                    FlowCard currentFlowCard = new FlowCard();
                    if (flowCard.getCompleteProcess() == null || flowCard.getCompleteProcess().equals("")) {
                        currentFlowCard.setCompleteProcess(record.getCompletionCraftName());
                    } else {
                        currentFlowCard.setCompleteProcess(flowCard.getCompleteProcess() + " → " + record.getCompletionCraftName());
                    }
                    LambdaUpdateWrapper<FlowCard> flowCardWrapper = new LambdaUpdateWrapper<>();
                    flowCardWrapper.eq(FlowCard::getFlowCardNo, key);
                    flowCardMapper.update(currentFlowCard, flowCardWrapper);
                }
            }
        });

        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult editCompletion(String id, CompletionRecord record) throws Exception {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        List<CompletionPerson> persons = record.getCompletionPersonList();
        List<CompletionBusiness> businesses = record.getCompletionBusinessList();
        record.setId(id);
        int i = mapper.updateById(record);
        if (i <= 0) {
            throw new Exception(ResultConstants.UPD_ERROR);
        }
        LambdaQueryWrapper<CompletionPerson> delWrapper = new LambdaQueryWrapper<>();
        delWrapper.eq(CompletionPerson::getCompletionRecordId, id);
        personMapper.delete(delWrapper);
        for (CompletionPerson p : persons) {
            p.setCompletionRecordId(record.getId());
            i = personMapper.insert(p);
            if (i <= 0) {
                throw new Exception(ResultConstants.UPD_ERROR);
            }
        }
        //查询已存在的完工明细
        LambdaQueryWrapper<CompletionBusiness> completionBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        completionBusinessLambdaQueryWrapper.eq(CompletionBusiness::getRecordId, id);
        List<CompletionBusiness> completionBusinesses = businessMpper.selectList(completionBusinessLambdaQueryWrapper);
        //需要删除的完工
        List<CompletionBusiness> noExist = new ArrayList<>();
        //需要修改的完工
        List<CompletionBusiness> exist = new ArrayList<>();
        //需要新增的完工
        List<CompletionBusiness> addBusiness = businesses.stream().filter(s -> s.getId() == null || s.getId().equals("")).collect(Collectors.toList());
        for (i = 0; i < completionBusinesses.size(); i++) {
            //已存在完工明细
            CompletionBusiness current = completionBusinesses.get(i);
            CompletionBusiness parBusiness = new CompletionBusiness();
            //是否存在
            boolean isExits = false;
            for (int j = 0; j < businesses.size(); j++) {
                //修改参数完工明细
                parBusiness = businesses.get(j);
                CompletionBusiness paramsBusiness = businesses.get(j);
                //判断是否存在
                if (current.getFlowCardNo().equals(paramsBusiness.getFlowCardNo())
                        && current.getProductId().equals(paramsBusiness.getProductId())
                        && current.getSemiProductId().equals(paramsBusiness.getSemiProductId())) {
                    isExits = true;
                }
            }
            if (!isExits) {
                noExist.add(parBusiness);
            } else {
                exist.add(parBusiness);
            }
        }
        String[] flowCardNoArr = record.getFlowCardNo().split(",");
        //删除的分组数据
        Map<String, List<CompletionBusiness>> groupDelByBusiness = new HashMap<>();
        for (String item : flowCardNoArr) {
            List<CompletionBusiness> currentCB = noExist.stream().filter(s -> s.getFlowCardNo().equals(item)).collect(Collectors.toList());
            groupDelByBusiness.put(item, currentCB);
        }
        //修改的分组数据
        Map<String, List<CompletionBusiness>> groupUpdByBusiness = new HashMap<>();
        for (String item : flowCardNoArr) {
            List<CompletionBusiness> currentCB = exist.stream().filter(s -> s.getFlowCardNo().equals(item)).collect(Collectors.toList());
            groupUpdByBusiness.put(item, currentCB);
        }
        //修改的分组数据
        Map<String, List<CompletionBusiness>> groupAddByBusiness = new HashMap<>();
        for (String item : flowCardNoArr) {
            List<CompletionBusiness> currentCB = addBusiness.stream().filter(s -> s.getFlowCardNo().equals(item)).collect(Collectors.toList());
            groupAddByBusiness.put(item, currentCB);
        }
        //需要删除的流程卡数据
        for (Map.Entry<String, List<CompletionBusiness>> entry : groupDelByBusiness.entrySet()) {
            String key = entry.getKey();
            List<CompletionBusiness> value = entry.getValue();
            //判断value等长度是否大于0
            if (value.size() > 0) {
                LambdaQueryWrapper<FlowCard> flowCardQueryWrapper = new LambdaQueryWrapper<>();
                flowCardQueryWrapper.eq(FlowCard::getFlowCardNo, key);
                //当前流程卡信息
                FlowCard flowCard = flowCardMapper.selectOne(flowCardQueryWrapper);
                //流程卡工艺
                List<String> craftFlow = Arrays.asList(flowCard.getProcessContent().split(" → "));
                List<String> currentCraftFlow = Arrays.asList(flowCard.getCompleteProcess().split(" → "));
                if (currentCraftFlow.indexOf(record.getCompletionCraftName()) != currentCraftFlow.size() - 1
                        && craftFlow.indexOf(record.getCompletionCraftName()) != craftFlow.size() - 1) {
                    throw new Exception("上一工艺正在进行，当前工艺无法编辑");
                }
                //如果是最后一道工艺
                if (craftFlow.indexOf(record.getCompletionCraftName()) == craftFlow.size() - 1) {
                    for (CompletionBusiness b : value) {
                        LambdaQueryWrapper<ProductProcess> productProcessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        productProcessLambdaQueryWrapper.in(ProductProcess::getOrderNo, Arrays.asList(flowCard.getOrderNo().split(", ")));
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getProductId, b.getProductId());
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getSemiProductId, b.getSemiProductId());
                        List<ProductProcess> productProcessList = productProcessMapper.selectList(productProcessLambdaQueryWrapper);
                        for (ProductProcess productProcess:
                                productProcessList) {
                            if (productProcess.getCompletionNum() == null || productProcess.getCompletionNum() == 0) {
                                productProcess.setCompletionNum(b.getNum());
                            } else {
                                productProcess.setCompletionNum(productProcess.getCompletionNum() + b.getNum());
                            }
                            productProcessMapper.updateById(productProcess);
                        }
                    }
                }
                //删除当前流程卡下的完工明细
                for (CompletionBusiness b : value) {
                    businessMpper.deleteById(b.getId());
                }
            }
        }
        //需要修改的流程卡数据
        for (Map.Entry<String, List<CompletionBusiness>> entry : groupUpdByBusiness.entrySet()) {
            String key = entry.getKey();
            List<CompletionBusiness> value = entry.getValue();
            //判断value等长度是否大于0
            if (value.size() > 0) {
                LambdaQueryWrapper<FlowCard> flowCardQueryWrapper = new LambdaQueryWrapper<>();
                flowCardQueryWrapper.eq(FlowCard::getFlowCardNo, key);
                //当前流程卡信息
                FlowCard flowCard = flowCardMapper.selectOne(flowCardQueryWrapper);
                //流程卡工艺
                List<String> craftFlow = Arrays.asList(flowCard.getProcessContent().split(" → "));
                List<String> currentCraftFlow = Arrays.asList(flowCard.getCompleteProcess().split(" → "));
                if (currentCraftFlow.indexOf(record.getCompletionCraftName()) != currentCraftFlow.size() - 1
                        && craftFlow.indexOf(record.getCompletionCraftName()) != craftFlow.size() - 1) {
                    throw new Exception("上一工艺正在进行，当前工艺无法编辑");
                }
                //如果是最后一道工艺
                if (craftFlow.indexOf(record.getCompletionCraftName()) == craftFlow.size() - 1) {
                    for (CompletionBusiness b : value) {
                        LambdaQueryWrapper<ProductProcess> productProcessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        productProcessLambdaQueryWrapper.in(ProductProcess::getOrderNo, Arrays.asList(flowCard.getOrderNo().split(", ")));
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getProductId, b.getProductId());
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getSemiProductId, b.getSemiProductId());
                        List<ProductProcess> productProcessList = productProcessMapper.selectList(productProcessLambdaQueryWrapper);
                        for (ProductProcess productProcess:
                        productProcessList) {
                            if (productProcess.getCompletionNum() == null || productProcess.getCompletionNum() == 0) {
                                productProcess.setCompletionNum(b.getNum());
                            } else {
                                if (b.getNum() > productProcess.getNum()) {
                                    int num = b.getNum() - productProcess.getNum();
                                    productProcess.setCompletionNum(productProcess.getCompletionNum() + num);
                                } else if (b.getNum() < productProcess.getNum()) {
                                    int num = productProcess.getNum() - b.getNum();
                                    productProcess.setCompletionNum(productProcess.getCompletionNum() - num);
                                }
                            }
                            productProcessMapper.updateById(productProcess);
                        }
                    }
                }
                //修改当前流程卡下的完工明细
                for (CompletionBusiness b : value) {
                    businessMpper.updateById(b);
                }
            }
        }
        //需要新增的流程卡数据
        for (Map.Entry<String, List<CompletionBusiness>> entry : groupAddByBusiness.entrySet()) {
            String key = entry.getKey();
            List<CompletionBusiness> value = entry.getValue();
            if (value.size() > 0) {
                LambdaQueryWrapper<FlowCard> flowCardQueryWrapper = new LambdaQueryWrapper<>();
                flowCardQueryWrapper.eq(FlowCard::getFlowCardNo, key);
                //当前流程卡信息
                FlowCard flowCard = flowCardMapper.selectOne(flowCardQueryWrapper);
                //流程卡工艺
                List<String> craftFlow = Arrays.asList(flowCard.getProcessContent().split(" → "));
                List<String> currentCraftFlow = Arrays.asList(flowCard.getCompleteProcess().split(" → "));
                if (currentCraftFlow.indexOf(record.getCompletionCraftName()) != currentCraftFlow.size() - 1
                        && craftFlow.indexOf(record.getCompletionCraftName()) != craftFlow.size() - 1) {
                    throw new Exception("上一工艺正在进行，当前工艺无法编辑");
                }
                //如果是最后一道工艺
                if (craftFlow.indexOf(record.getCompletionCraftName()) == craftFlow.size() - 1) {
                    for (CompletionBusiness b : value) {
                        LambdaQueryWrapper<ProductProcess> productProcessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        productProcessLambdaQueryWrapper.in(ProductProcess::getOrderNo, Arrays.asList(flowCard.getOrderNo().split(", ")));
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getProductId, b.getProductId());
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getSemiProductId, b.getSemiProductId());
                        List<ProductProcess> productProcessList = productProcessMapper.selectList(productProcessLambdaQueryWrapper);
                        for (ProductProcess productProcess:
                        productProcessList) {
                            if (productProcess.getCompletionNum() == null || productProcess.getCompletionNum() == 0) {
                                productProcess.setCompletionNum(b.getNum());
                            } else {
                                productProcess.setCompletionNum(productProcess.getCompletionNum() + b.getNum());
                            }
                            productProcessMapper.updateById(productProcess);
                        }

                    }
                }
                //修改当前流程卡下的完工明细
                for (CompletionBusiness b : value) {
                    b.setRecordId(id);
                    businessMpper.insert(b);
                }
            }
        }
        //修改流程卡状态
        autoFlowCardStatus(Arrays.asList(flowCardNoArr), record.getCompletionCraftName());
        //进行订单 订单产品的修改
        operationalRelationshipData(Arrays.asList(flowCardNoArr), record.getCompletionCraftName());
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    //根据工艺查询流程卡的状态
    @Transactional
    public void autoFlowCardStatus(List<String> flowCardIds, String craft) {
        for (String flowCard : flowCardIds) {
            LambdaQueryWrapper<CompletionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
            businessLambdaQueryWrapper.eq(CompletionBusiness::getFlowCardNo, flowCard);
            List<CompletionBusiness> businessList = businessMpper.selectList(businessLambdaQueryWrapper);
            LambdaQueryWrapper<FlowCard> flowCardQueryWrapper = new LambdaQueryWrapper<>();
            flowCardQueryWrapper.eq(FlowCard::getFlowCardNo, flowCard);
            //当前流程卡信息
            FlowCard flowCardData = flowCardMapper.selectOne(flowCardQueryWrapper);
            //已完成流程卡工艺
            List<String> craftFlowArr = new ArrayList(Arrays.asList(flowCardData.getProcessContent().split(" → ")));
            //已完成流程卡工艺
            List<String> craftFlow = new ArrayList(Arrays.asList(flowCardData.getCompleteProcess().split(" → ")));
            if (businessList.size() == 0) {
                //当前工艺无任何记录 对比流程卡状态进行删除
                if (craftFlow.indexOf(craft) != -1) {
                    craftFlow.remove(craftFlow.indexOf(craft));
                    if (craftFlow.size() > 0) {
                        String craftFlowStr = "";
                        for (String str : craftFlow) {
                            if (craftFlowStr.equals("")) {
                                craftFlowStr = str;
                            } else {
                                craftFlowStr = craftFlowStr + " → " + str;
                            }
                        }
                        if (flowCardData.getProductionStatus() == 0) {
                            flowCardData.setProductionStatus(1);
                        }
                        flowCardData.setCompleteProcess(craftFlowStr);
                    } else {
                        flowCardData.setCompleteProcess("");
                        if (craftFlowArr.indexOf(craft) == 0) {
                            flowCardData.setProductionStatus(0);
                        }
                    }
                    flowCardMapper.updateById(flowCardData);
                }
            } else {
                List<ShelfDivisionBusiness> list = getComparisonResults(flowCard, craft);
                if (list.size() == 0) {
                    //全部完工了 查询流程卡状态中是否存在此状态 没有则补上
                    if (craftFlow.indexOf(craft) == -1) {
                        craftFlow.add(craft);
                        String craftFlowStr = "";
                        for (String str : craftFlow) {
                            if (craftFlowStr.equals("")) {
                                craftFlowStr = str;
                            } else {
                                craftFlowStr = craftFlowStr + " → " + str;
                            }
                        }
                        if (craftFlow.indexOf(craft) == craftFlow.size() - 1 || flowCardData.getProductionStatus() == 1) {
                            flowCardData.setProductionStatus(2);
                        } else if (flowCardData.getProductionStatus() == 0) {
                            flowCardData.setProductionStatus(1);
                        }
                        flowCardData.setCompleteProcess(craftFlowStr);
                        flowCardMapper.updateById(flowCardData);
                    }
                } else {
                    //未全部完工 则查询流程卡状态中是否存在此状态 如果存在则删除
                    if (craftFlow.indexOf(craft) != -1) {
                        craftFlow.remove(craftFlow.indexOf(craft));
                        if (craftFlow.size() > 0) {
                            String craftFlowStr = "";
                            for (String str : craftFlow) {
                                if (craftFlowStr.equals("")) {
                                    craftFlowStr = str;
                                } else {
                                    craftFlowStr = craftFlowStr + " → " + str;
                                }
                            }
                            flowCardData.setProductionStatus(1);
                            flowCardData.setCompleteProcess(craftFlowStr);
                        } else {
                            flowCardData.setCompleteProcess("");
                        }
                        flowCardMapper.updateById(flowCardData);
                    }
                }
            }
        }
    }

    @Transactional
    public void operationalRelationshipData(List<String> flowCardIds, String craft) {
        for (String flowCardId : flowCardIds) {
            LambdaQueryWrapper<FlowCard> flowCardQueryWrapper = new LambdaQueryWrapper<>();
            flowCardQueryWrapper.eq(FlowCard::getFlowCardNo, flowCardId);
            //当前流程卡信息
            FlowCard flowCardData = flowCardMapper.selectOne(flowCardQueryWrapper);
            if (flowCardData.getProductionStatus() == 2) {
                //待计算订单产品完成数量
                LambdaQueryWrapper<OrderProduct> orderProductLambdaQueryWrapper = new LambdaQueryWrapper<>();
                orderProductLambdaQueryWrapper.in(OrderProduct::getOrderNo, Arrays.asList(flowCardData.getOrderNo().split(", ")));
                List<OrderProduct> orderProducts = orderProductMapper.selectList(orderProductLambdaQueryWrapper);
                for (OrderProduct o : orderProducts) {
                    List<String> orderNos = Arrays.asList(flowCardData.getOrderNo().split(", "));
                    for (String orderNo:
                            orderNos) {
                        LambdaQueryWrapper<ProductProcess> productProcessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getOrderNo, orderNo);
                        productProcessLambdaQueryWrapper.eq(ProductProcess::getProductId, o.getId());
                        productProcessLambdaQueryWrapper.orderByAsc(ProductProcess::getCompletionNum);
                        List<ProductProcess> productProcess = productProcessMapper.selectList(productProcessLambdaQueryWrapper);
                        if(productProcess.size() > 0){
                            o.setCompletionNum(productProcess.get(0).getCompletionNum());
                            orderProductMapper.updateById(o);
                        }
                    }

                }
                //判断订单下所有流程卡的状态是否已完成 如果完成 订单则完成
                LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
                flowCardLambdaQueryWrapper.in(FlowCard::getOrderNo, Arrays.asList(flowCardData.getOrderNo().split(", ")));
                flowCardLambdaQueryWrapper.ne(FlowCard::getProductionStatus, 2);
                List<FlowCard> flowCards = flowCardMapper.selectList(flowCardLambdaQueryWrapper);
                if (flowCards.size() == 0) {
                    OrderRecord orderRecord = new OrderRecord();
                    orderRecord.setProductionStatus(2);
                    List<String> orderNos = Arrays.asList(flowCardData.getOrderNo().split(", "));
                    for (String orderNo:
                         orderNos) {
                        LambdaQueryWrapper<OrderRecord> orderRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        orderRecordLambdaQueryWrapper.eq(OrderRecord::getOrderNo,orderNo);
                        OrderRecord currentOrder = orderMapper.selectOne(orderRecordLambdaQueryWrapper);
                        if(currentOrder.getRackSplittingStatus() == 2) {
                            LambdaUpdateWrapper<OrderRecord> orderRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                            orderRecordLambdaUpdateWrapper.eq(OrderRecord::getOrderNo, orderNo);
                            orderMapper.update(orderRecord, orderRecordLambdaUpdateWrapper);
                        }
                    }
                }
            }else if(flowCardData.getProductionStatus() == 0) {
                List<String> orderNos = Arrays.asList(flowCardData.getOrderNo().split(", "));
                for (String orderNo:
                     orderNos) {
                    OrderRecord orderRecord = new OrderRecord();
                    orderRecord.setProductionStatus(0);
                    String sql = "SELECT COUNT(*) as total FROM completion_record r LEFT JOIN flow_card f ON f.flow_card_no = r.flow_card_no WHERE order_no LIKE '%"+orderNo+"%'";
                    int comTotal = Integer.parseInt(String.valueOf(patchRecordMapper.getCountList(sql).get(0).get("total")));
                    if(comTotal <= 1){
                        LambdaUpdateWrapper<OrderRecord> orderRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        orderRecordLambdaUpdateWrapper.eq(OrderRecord::getOrderNo, orderNo);
                        orderMapper.update(orderRecord, orderRecordLambdaUpdateWrapper);
                    }
                }
            }
        }
    }


    @Override
    @Transactional
    public CommonResult delCompletion(List<String> ids) throws Exception {
        LambdaQueryWrapper<CompletionPerson> personWrapper = new LambdaQueryWrapper<>();
        personWrapper.in(CompletionPerson::getCompletionRecordId, ids);
        personMapper.delete(personWrapper);
        for (String id : ids) {
            CompletionRecord record = mapper.selectById(id);
            LambdaQueryWrapper<CompletionBusiness> businessWrapper = new LambdaQueryWrapper<>();
            businessWrapper.eq(CompletionBusiness::getRecordId, id);
            List<CompletionBusiness> businessList = businessMpper.selectList(businessWrapper);
            HashSet<String> flowCardArr = new HashSet<>();
            for (CompletionBusiness b : businessList) {
                flowCardArr.add(b.getFlowCardNo());
            }
            Map<String, List<CompletionBusiness>> groupDelByBusiness = new HashMap<>();
            for (String item : flowCardArr) {
                List<CompletionBusiness> currentCB = businessList.stream().filter(s -> s.getFlowCardNo().equals(item)).collect(Collectors.toList());
                groupDelByBusiness.put(item, currentCB);
            }
            //需要删除的流程卡数据
            for (Map.Entry<String, List<CompletionBusiness>> entry : groupDelByBusiness.entrySet()) {
                String key = entry.getKey();
                List<CompletionBusiness> value = entry.getValue();
                //判断value等长度是否大于0
                if (value.size() > 0) {
                    //删除当前流程卡下的完工明细
                    for (CompletionBusiness b : value) {
                        businessMpper.deleteById(b.getId());
                    }
                    LambdaQueryWrapper<FlowCard> flowCardQueryWrapper = new LambdaQueryWrapper<>();
                    flowCardQueryWrapper.eq(FlowCard::getFlowCardNo, key);
                    //当前流程卡信息
                    FlowCard flowCard = flowCardMapper.selectOne(flowCardQueryWrapper);
                    //流程卡工艺
                    List<String> craftFlow = Arrays.asList(flowCard.getProcessContent().split(" → "));
                    List<String> currentCraftFlow = Arrays.asList(flowCard.getCompleteProcess().split(" → "));
                    if (currentCraftFlow.indexOf(record.getCompletionCraftName()) != currentCraftFlow.size() - 1
                            && craftFlow.indexOf(record.getCompletionCraftName()) != craftFlow.size() - 1) {
                        return CommonResult.error("上一工艺正在进行，当前工艺无法删除");
                    }
                    //如果是最后一道工艺
                    if (craftFlow.indexOf(record.getCompletionCraftName()) == craftFlow.size() - 1) {
                        for (CompletionBusiness b : value) {
                            LambdaQueryWrapper<ProductProcess> productProcessLambdaQueryWrapper = new LambdaQueryWrapper<>();
                            productProcessLambdaQueryWrapper.in(ProductProcess::getOrderNo, Arrays.asList(flowCard.getOrderNo().split(", ")));
                            productProcessLambdaQueryWrapper.eq(ProductProcess::getProductId, b.getProductId());
                            productProcessLambdaQueryWrapper.eq(ProductProcess::getSemiProductId, b.getSemiProductId());
                            List<ProductProcess> productProcessList = productProcessMapper.selectList(productProcessLambdaQueryWrapper);
                            for (ProductProcess productProcess:
                            productProcessList) {
                                if (productProcess.getCompletionNum() == null || productProcess.getCompletionNum() == 0) {
                                    productProcess.setCompletionNum(b.getNum());
                                } else {
                                    productProcess.setCompletionNum(productProcess.getCompletionNum() + b.getNum());
                                }
                                productProcessMapper.updateById(productProcess);
                            }
                        }
                    }
                }
            }
            //修改流程卡状态
            autoFlowCardStatus(new ArrayList<>(flowCardArr), record.getCompletionCraftName());
            //进行订单 订单产品的修改
            operationalRelationshipData(new ArrayList<>(flowCardArr), record.getCompletionCraftName());
        }
        int i = mapper.deleteBatchIds(ids);
        if (i <= 0) {
            throw new Exception(ResultConstants.DEL_ERROR);
        }
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    @Override
    public CommonResult getCompletionRecord(Map<String, Object> params) {
        List<CompletionRecord> list = mapper.selectCompletionRecordList(params);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS, list);
        LambdaQueryWrapper<CompletionRecord> wrapper = new LambdaQueryWrapper<>();
        if (params.containsKey("beginDate") && params.containsKey("endDate")) {
            wrapper.between(CompletionRecord::getCompletionDate, params.get("beginDate").toString(), params.get("endDate").toString());
        }
        if(params.containsKey("completionNo")){
            wrapper.like(CompletionRecord::getCompletionNo,params.get("completionNo"));
        }
        if(params.containsKey("flowCardNo")){
            wrapper.like(CompletionRecord::getFlowCardNo,params.get("flowCardNo"));
        }
        if(params.containsKey("completionShift")){
            wrapper.like(CompletionRecord::getCompletionShift,params.get("completionShift"));
        }
        if(params.containsKey("completionTeam")){
            wrapper.like(CompletionRecord::getCompletionTeam,params.get("completionTeam"));
        }
        int count = mapper.selectCount(wrapper);
        result.setCount(count);
        return result;
    }

    @Override
    public CommonResult getCompletionInfo(String id) {
        CompletionRecord record = mapper.selectById(id);
        LambdaQueryWrapper<CompletionPerson> personLambdaQueryWrapper = new LambdaQueryWrapper<>();
        personLambdaQueryWrapper.eq(CompletionPerson::getCompletionRecordId, id);
        List<CompletionPerson> personList = personMapper.selectList(personLambdaQueryWrapper);
        record.setCompletionPersonList(personList);
        LambdaQueryWrapper<CompletionBusiness> businessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        businessLambdaQueryWrapper.eq(CompletionBusiness::getRecordId, id);
        List<CompletionBusiness> businessList = businessMpper.selectList(businessLambdaQueryWrapper);
        for (CompletionBusiness b : businessList) {
            LambdaQueryWrapper<ShelfDivisionBusiness> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.eq(ShelfDivisionBusiness::getFlowCardNo, b.getFlowCardNo());
            flowCardLambdaQueryWrapper.eq(ShelfDivisionBusiness::getSemiProductId, b.getSemiProductId());
            flowCardLambdaQueryWrapper.eq(ShelfDivisionBusiness::getProductId, b.getProductId());
            ShelfDivisionBusiness flowCard = divisionBusinessMapper.selectOne(flowCardLambdaQueryWrapper);
            b.setFlowCardInfo(flowCard);
        }
        record.setCompletionBusinessList(businessList);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS, record);
    }

    @Override
    public CommonResult getFlowCardInfo(Map<String, Object> params) {
        // 根据流程卡查询工艺的工艺流程
        LambdaQueryWrapper<FlowCard> flowCardQueryWrapper = new LambdaQueryWrapper<>();
        flowCardQueryWrapper.eq(FlowCard::getFlowCardNo, params.get("flowCardNo"));
        FlowCard flowCard = flowCardMapper.selectOne(flowCardQueryWrapper);
        if (flowCard != null) {
            List<String> craftFlow = Arrays.asList(flowCard.getProcessContent().split(" → "));
            if (craftFlow.indexOf(params.get("craft")) == 0) {
                return CommonResult.success(ResultConstants.QUERY_SUCCESS, getComparisonResults(params.get("flowCardNo").toString(), params.get("craft").toString()));
            } else if (craftFlow.indexOf(params.get("craft")) == -1) {
                return CommonResult.error("此流程卡无此工艺");
            } else {
                //如果不是第一个则查询上一工艺的是否完工
                int topIndex = craftFlow.indexOf(params.get("craft")) - 1;
                //如果上一工艺全部完工
                if (getCraftOver(params.get("flowCardNo").toString(), craftFlow.get(topIndex))) {
                    return CommonResult.success(ResultConstants.QUERY_SUCCESS, getComparisonResults(params.get("flowCardNo").toString(), params.get("craft").toString()));
                } else {
                    //未完工进行提示
                    return CommonResult.error("请先进行上一工艺完工");
                }
            }
        }else {
            return CommonResult.error("此工艺无此流程卡");
        }
    }

    @Override
    public void exportCompletion(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<CompletionRecord> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(CompletionRecord::getId, split);
        }
        wrapper.orderByDesc(CompletionRecord::getCreatedAt);
        List<CompletionRecord> completionRecords = mapper.selectList(wrapper);
        ExcelUtil<CompletionRecord> util = new ExcelUtil<>(CompletionRecord.class);
        util.exportExcel(response, completionRecords, "完工数据", "完工数据");
    }

    @Override
    public void flowCardCompare(List<String> ids, String craft) {
        try {
            autoFlowCardStatus(ids, craft);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void orderCompare(List<String> ids, String craft) {
        try {
            operationalRelationshipData(ids, craft);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public CommonResult getCountList(Map<String, String> data) {
        String whereStr = "";
        String timerStr = "";
        if (data.containsKey("product_name")) {
            whereStr = "AND p.product_name like '%"+data.get("product_name")+"%'";
        }
        if(data.containsKey("startDate") && data.containsKey("endDate")){
            timerStr = "AND r.completion_date >= '" + data.get("startDate") + " 00:00:00' AND r.completion_date <= '" + data.get("endDate") + " 23:59:59'";
        }
        String sql = "SELECT DISTINCT p.product_name," +
                "p.id," +
                "SUM(CASE WHEN b.last_craft = 1 "+timerStr+" THEN b.num ELSE 0 END ) num," +
                "SUM( CASE WHEN b.last_craft = 1 "+timerStr+" THEN ROUND((f.total_area/f.split_num)*b.num,2) ELSE 0 END ) area " +
                "FROM " +
                "sys_semi_product s " +
                "LEFT JOIN sys_product p ON s.product_id = p.id " +
                "LEFT JOIN completion_business b on b.semi_product_id = s.id " +
                "LEFT JOIN flow_card f ON f.flow_card_no = b.flow_card_no " +
                "LEFT JOIN completion_record r on r.id = b.record_id  " +
                "WHERE 1 = 1 "+
                whereStr +
                "GROUP BY " +
                "p.product_name " +
                "ORDER BY num DESC";
        List<Map<String, Object>> result = patchRecordMapper.getCountList(sql);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS, result);
    }

    @Override
    public CommonResult getCountInfo(String id) {
        LambdaQueryWrapper<SysSemiProduct> sysSemiProductLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysSemiProductLambdaQueryWrapper.eq(SysSemiProduct::getProductId,id);
        List<SysSemiProduct> semiProducts = semiProductMapper.selectList(sysSemiProductLambdaQueryWrapper);
        List<String> semiProductIds = semiProducts.stream().map(SysSemiProduct::getId).collect(Collectors.toList());
        LambdaQueryWrapper<ShelfDivisionBusiness> shelfDivisionBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shelfDivisionBusinessLambdaQueryWrapper.in(ShelfDivisionBusiness::getSemiProductId,semiProductIds);
        List<ShelfDivisionBusiness> businesses = divisionBusinessMapper.selectList(shelfDivisionBusinessLambdaQueryWrapper);
        Set<String> flowCardNos = new HashSet<>();
        for (int i = 0; i < businesses.size(); i++){
            ShelfDivisionBusiness business = businesses.get(i);
            flowCardNos.add(business.getFlowCardNo());
        }
        if(flowCardNos.size() > 0){
            LambdaQueryWrapper<FlowCard> flowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flowCardLambdaQueryWrapper.in(FlowCard::getFlowCardNo,flowCardNos);
            flowCardLambdaQueryWrapper.eq(FlowCard::getProductionStatus,2);
            List<FlowCard> flowCards = flowCardMapper.selectList(flowCardLambdaQueryWrapper);
            return CommonResult.success(ResultConstants.QUERY_SUCCESS,flowCards);
        }
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,new ArrayList<>());
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
        String sql = "SELECT DISTINCT p.product_name," +
                "p.id," +
                "SUM(case when f.production_status = 2 then f.split_num else 0 end ) num," +
                "SUM(case when f.production_status = 2 then f.total_area else 0 end ) area " +
                "FROM " +
                "sys_semi_product s " +
                "LEFT JOIN sys_product p ON s.product_id = p.id " +
                "LEFT JOIN shelf_division_business b ON b.semi_product_id = s.id " +
                "LEFT JOIN flow_card f ON f.flow_card_no = b.flow_card_no " +
                "WHERE 1 = 1 "+
                whereStr +
                "GROUP BY " +
                "p.product_name " +
                "ORDER BY num DESC";
        List<Map<String,Object>> result = patchRecordMapper.getCountList(sql);
        List<ProduceCount> patchCount = new ArrayList<>();
        for (Map<String,Object> map:result) {
            ProduceCount p = new ProduceCount();
            p.setProductName(map.get("product_name").toString());
            p.setNum(map.get("num").toString());
            p.setArea(map.get("area").toString());
            patchCount.add(p);
        }
        ExcelUtil<ProduceCount> util = new ExcelUtil<>(ProduceCount.class);
        util.exportExcel(response, patchCount,"生产统计","生产统计");
    }

    /**
     * 比对流程卡明细和完工明细 将可完工的数据比对出来
     *
     * @param craft      工艺
     * @param flowCardNo 流程卡号
     * @return 比对结果
     */
    private List<ShelfDivisionBusiness> getComparisonResults(String flowCardNo, String craft) {
        //根据流程卡号和工艺 查询完工明细中数据
        LambdaQueryWrapper<CompletionBusiness> businessQueryWrapper = new LambdaQueryWrapper<>();
        businessQueryWrapper.eq(CompletionBusiness::getFlowCardNo, flowCardNo);
        businessQueryWrapper.eq(CompletionBusiness::getCraft, craft);
        List<CompletionBusiness> completionBusinessList = businessMpper.selectList(businessQueryWrapper);
        //根据流程卡号查询 流程卡明细
        LambdaQueryWrapper<ShelfDivisionBusiness> shelfDivisionBusinessWrapper = new LambdaQueryWrapper<>();
        shelfDivisionBusinessWrapper.eq(ShelfDivisionBusiness::getFlowCardNo, flowCardNo);
        List<ShelfDivisionBusiness> flowCards = divisionBusinessMapper.selectList(shelfDivisionBusinessWrapper);
        //如果完工明细中的数据大于0条 则根据完工明细表比对将已完工的数据消除返回
        if (completionBusinessList.size() > 0) {
            //循环完工明细 进行比对
            for (int i = 0; i < completionBusinessList.size(); i++) {
                CompletionBusiness currentBus = completionBusinessList.get(i);
                int key = -1;
                //循环查询结果 查询是否存在完工明细 如果存在则赋值下标
                for (int j = 0; j < flowCards.size(); j++) {
                    ShelfDivisionBusiness business = flowCards.get(j);
                    if (business.getFlowCardNo().equals(flowCardNo) &&
                            business.getItemSurface().equals(currentBus.getItemSurface()) &&
                            business.getSemiProductId().equals(currentBus.getSemiProductId()) &&
                            business.getProductId().equals(currentBus.getProductId())) {
                        key = j;
                    }
                }
                //如果下标不等于-1 则对返回数据进行处理
                if (key != -1) {
                    //如果状态等于 就直接进行删除 否则插入完工数
                    if (currentBus.getStatus() == 1) {
                        flowCards.remove(key);
                    } else {
                        if (flowCards.get(key).getCompleteNum() == null || flowCards.get(key).getCompleteNum() == 0) {
                            flowCards.get(key).setCompleteNum(currentBus.getNum());
                        } else {
                            flowCards.get(key).setCompleteNum(flowCards.get(key).getCompleteNum() + currentBus.getNum());
                        }
                        //获取报损数量 然后加上报损数量
                        ShelfDivisionBusiness business = flowCards.get(key);
                        LambdaQueryWrapper<DamageFlowCard> damageFlowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getFlowCardNo, business.getFlowCardNo());
                        damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getSemiProductId, business.getSemiProductId());
                        damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getProductId, business.getProductId());
                        List<DamageFlowCard> damageFlowCardList = damageFlowCardMapper.selectList(damageFlowCardLambdaQueryWrapper);
                        int damageNum = 0;
                        for (DamageFlowCard d : damageFlowCardList) {
                            damageNum += d.getDamageNum();
                        }
                        if (flowCards.get(key).getCompleteNum() + damageNum >= flowCards.get(key).getItemNum()) {
                            flowCards.remove(key);
                        }
                    }
                }
            }
        }
        //开始遍历查询报损数量 条件为工艺、流程卡、订单、产品的报损数量
        for (int i = 0; i < flowCards.size(); i++) {
            ShelfDivisionBusiness business = flowCards.get(i);
            LambdaQueryWrapper<DamageFlowCard> damageFlowCardLambdaQueryWrapper = new LambdaQueryWrapper<>();
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getFlowCardNo, business.getFlowCardNo());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getSemiProductId, business.getSemiProductId());
            damageFlowCardLambdaQueryWrapper.eq(DamageFlowCard::getProductId, business.getProductId());
            List<DamageFlowCard> damageFlowCardList = damageFlowCardMapper.selectList(damageFlowCardLambdaQueryWrapper);
            int damageNum = 0;
            for (DamageFlowCard d : damageFlowCardList) {
                damageNum += d.getDamageNum();
            }
            business.setDamageNum(damageNum);
            flowCards.set(i, business);
        }
        //返回可完工的数据
        return flowCards;
    }

    /**
     * 查询流程卡某工艺是否完工
     *
     * @param flowCradNo 流程卡号
     * @param craft      工艺
     * @return
     */
    private boolean getCraftOver(String flowCradNo, String craft) {
        //查询完工数据
        LambdaQueryWrapper<CompletionBusiness> businessQueryWrapper = new LambdaQueryWrapper<>();
        businessQueryWrapper.eq(CompletionBusiness::getFlowCardNo, flowCradNo);
        businessQueryWrapper.eq(CompletionBusiness::getCraft, craft);
        businessQueryWrapper.eq(CompletionBusiness::getStatus, 1);
        List<CompletionBusiness> completionBusinessList = businessMpper.selectList(businessQueryWrapper);

        //根据流程卡号查询 流程卡明细
        LambdaQueryWrapper<ShelfDivisionBusiness> shelfDivisionBusinessWrapper = new LambdaQueryWrapper<>();
        shelfDivisionBusinessWrapper.eq(ShelfDivisionBusiness::getFlowCardNo, flowCradNo);
        List<ShelfDivisionBusiness> flowCards = divisionBusinessMapper.selectList(shelfDivisionBusinessWrapper);
        if (completionBusinessList.size() != 0 && completionBusinessList.size() == flowCards.size()) {
            return true;
        }
        List<ShelfDivisionBusiness> result = getComparisonResults(flowCradNo, craft);
        if (result.size() == 0) {
            return true;
        }
        return false;
    }

    private String productionNumber() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<CompletionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(CompletionRecord::getCompletionNo, format1);
        wrapper.like(CompletionRecord::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(CompletionRecord::getCreatedAt).last("limit 1");
        CompletionRecord record = mapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("wg");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (record != null) {
            String completionNo = record.getCompletionNo();
            //查询拼接符字典
            String[] splicer = {"\\+", "-"};
            int integer = 1;
            //遍历拆分     找出编号后缀
            for (String ss : splicer) {
                String[] split = completionNo.split(ss);
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
                String[] split = completionNo.split(format1);
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
