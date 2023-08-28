package com.erp.sales.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.MapUtil;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.sales.*;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.sales.domain.vo.FlowCardVo;
import com.erp.floor.pojo.sales.domain.vo.FlowParameter;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.sales.service.FlowCardService;
import com.erp.sales.service.OrderProductService;
import com.erp.sales.service.ShelfDivisionBusinessService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-28 11:40
 */
@Service
public class FlowCardServiceImpl extends ServiceImpl<FlowCardMapper, FlowCard> implements FlowCardService {

    @Resource
    private FlowCardMapper flowCardMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private ShelfDivisionBusinessMapper shelfDivisionBusinessMapper;
    @Resource
    private OrderProductMapper orderProductMapper;
    @Resource
    private ProductProcessMapper productProcessMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderProductService orderProductService;
    @Resource
    private ShelfDivisionBusinessService shelfDivisionBusinessService;


    /**
     * 生成新的流程卡编号
     *
     * @param oldFlowNumber
     * @return
     */
    @Override
    public String generateFlowNumber(String oldFlowNumber) {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (oldFlowNumber == null) {
            Date date = new Date(System.currentTimeMillis());
            LambdaQueryWrapper<FlowCard> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(FlowCard::getSplitDate, orderFormat.format(date));
            wrapper.orderByDesc(FlowCard::getFlowCardNo).last("limit 1");
            FlowCard flowCard = flowCardMapper.selectOne(wrapper);
            if (flowCard != null) {
                oldFlowNumber = flowCard.getFlowCardNo();
            }
        }
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("10");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        int integer = 1;
        String format1 = format.format(new Date()).substring(4);
        if (oldFlowNumber != null && oldFlowNumber.contains(format1)) {
            //去处_A
            if (oldFlowNumber.contains("_")) {
                oldFlowNumber = oldFlowNumber.substring(0, oldFlowNumber.indexOf("_"));
            }
            //查询拼接符字典
            String[] splicer = {"[+]", "-"};
            //遍历拆分     找出编号后缀
            for (String ss : splicer) {
                String[] split = oldFlowNumber.split(ss);
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
                String[] split = oldFlowNumber.split(format1);
                String s = split[split.length - 1];
                integer = Integer.parseInt(s) + 1;
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
            stringBuilder.append(sysNumberingRules.getRulePrefix());
            if (sysNumberingRules.getFrontConnection() != null && !sysNumberingRules.getFrontConnection().equals("")) {
                stringBuilder.append(sysNumberingRules.getFrontConnection());
            }
            stringBuilder.append(dateFormat.format(new Date()));
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

    /**
     * 流程卡查询
     *
     * @param flowCardVo 流程卡查询实体类
     */
    @Override
    public CommonResult<List<FlowCard>> queryList(FlowCardVo flowCardVo) {
        try {
            LambdaQueryWrapper<FlowCard> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(flowCardVo.getId()))
                wrapper.eq(FlowCard::getId, flowCardVo.getId());
            if (StringUtils.isNotEmpty(flowCardVo.getFlowCardNo()))
                wrapper.eq(FlowCard::getFlowCardNo, flowCardVo.getFlowCardNo());
            if (StringUtils.isNotEmpty(flowCardVo.getOrderNo()))
                wrapper.like(FlowCard::getOrderNo, flowCardVo.getOrderNo());
            if (StringUtils.isNotEmpty(flowCardVo.getCustomNo()))
                wrapper.like(FlowCard::getCustomNo, flowCardVo.getCustomNo());
            if (StringUtils.isNotEmpty(flowCardVo.getProductName()))
                wrapper.like(FlowCard::getProductName, flowCardVo.getProductName());
            if (StringUtils.isNotEmpty(flowCardVo.getCustomerName()))
                wrapper.like(FlowCard::getCustomerName, flowCardVo.getCustomerName());
            if (StringUtils.isNotEmpty(flowCardVo.getMonolithicName()))
                wrapper.like(FlowCard::getMonolithicName, flowCardVo.getMonolithicName());
            if (StringUtils.isNotEmpty(flowCardVo.getSplitPerson()))
                wrapper.like(FlowCard::getSplitPerson, flowCardVo.getSplitPerson());
            if (StringUtils.isNotNull(flowCardVo.getProductionStatus()))
                wrapper.eq(FlowCard::getProductionStatus, flowCardVo.getProductionStatus());
            if (StringUtils.isNotEmpty(flowCardVo.getSplitDateStart()) && StringUtils.isNotEmpty(flowCardVo.getSplitDateEnd()))
                wrapper.between(FlowCard::getSplitDate, flowCardVo.getSplitDateStart(), flowCardVo.getSplitDateEnd());
            wrapper.orderByDesc(FlowCard::getSplitDate);
            wrapper.orderByDesc(FlowCard::getFlowCardNo);
            Integer integer = flowCardMapper.selectCount(wrapper);
            PageHelper.startPage(flowCardVo.getPageNum(), flowCardVo.getPageSize());
            List<FlowCard> flowCards = flowCardMapper.selectList(wrapper);
            PageInfo<FlowCard> pageInfo = new PageInfo<>(flowCards, flowCardVo.getPageSize());
            return new CommonResult(200, ResultConstants.QUERY_SUCCESS, integer, pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultConstants.QUERY_ERROR);
        }
    }

    /**
     * 查询流程卡明细
     *
     * @param flowId 流程卡id
     * @return
     */
    @Override
    public CommonResult<List<ShelfDivisionBusiness>> queryFlowDetailed(String flowId) {
        try {
            LambdaQueryWrapper<ShelfDivisionBusiness> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShelfDivisionBusiness::getFlowCardId, flowId);
            List<ShelfDivisionBusiness> shelfDivisionBusinesses = shelfDivisionBusinessMapper.selectList(wrapper);
            shelfDivisionBusinesses.forEach(one -> one.setOldNum(one.getItemNum()));
            return CommonResult.success(ResultConstants.QUERY_SUCCESS, shelfDivisionBusinesses);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultConstants.QUERY_ERROR);
        }
    }

    /**
     * 自然分架
     *
     * @param flowParameter 产品集合 yu 参数对象
     */
    @Override
    public CommonResult<List<FlowCard>> automaticShelf(FlowParameter flowParameter) {
        List<OrderProduct> orderProducts = flowParameter.getOrderProducts();
        Map<String, Object> parameter = flowParameter.getParameter();
        long start = System.currentTimeMillis();
        //流程卡list
        List<FlowCard> flowCards = new ArrayList<>();
        //将为空值的map去掉
        Map<String, Object> map = MapUtil.removeNullString(parameter);
        //查询每个产品中的单片属性
        List<ProductProcess> productProcesses = orderProductService.selectProcess(orderProducts);
        //判断分架参数
        double minArea = productProcesses.stream().mapToDouble(ProductProcess::getItemArea).min().getAsDouble();
        double minWeight = productProcesses.stream().mapToDouble(ProductProcess::getItemWide).min().getAsDouble();
        double minThick = productProcesses.stream().mapToDouble(ProductProcess::getItemThick).min().getAsDouble();
        if (map.get("area") != null && minArea > Double.parseDouble(map.get("area").toString())) {
            return CommonResult.error("设置面积小于单片最小面积:" + minArea);
        }
        if (map.get("weight") != null && minWeight > Double.parseDouble(map.get("weight").toString())) {
            return CommonResult.error("设置重量小于单片最小重量:" + minWeight);
        }
        if (map.get("fold") != null && minThick > Double.parseDouble(map.get("fold").toString())) {
            return CommonResult.error("设置最大叠厚小于单片最小厚度:" + minThick);
        }
        //单品数量list
        Map<String, List<ProductProcess>> mapList = new HashMap<>();
        //现根据默认属性  品种、厚度、工艺流程  以及分架参数划分流程卡
        long one = System.currentTimeMillis();
        System.out.println("查询花费时间" + (one - start));
        assert map != null;
        if (map.size() > 1 && map.get("isPosition").toString().equals("true") || map.size() > 2) {
            //根据产品类型第一次分架
            for (ProductProcess productProcess : productProcesses) {
                //拼接key， 订单编号+品种+厚度+工艺id
                StringBuilder key = new StringBuilder();
//                key.append(productProcess.getOrderNo())
                key.append(productProcess.getItemType())
                        .append(productProcess.getItemThick())
                        .append(productProcess.getProcessId());
                Integer num = productProcess.getShelfNum();
                //判断三种属性是否相同
                if (mapList.containsKey(key.toString())) {
                    List<ProductProcess> processList = new ArrayList<>(num);
                    for (int integer = 0; integer < num; integer++) {
                        productProcess.setShelfNum(1);
                        processList.add(productProcess);
                    }
                    //根据参数
                    mapList.get(key.toString()).addAll(processList);
                } else {
                    List<ProductProcess> processList = new ArrayList<>(num);
                    for (int integer = 0; integer < num; integer++) {
                        productProcess.setShelfNum(1);
                        processList.add(productProcess);
                    }
                    mapList.put(key.toString(), processList);
                }
            }


            List<ProductProcess> one12= new ArrayList<>();
            for (Map.Entry<String, List<ProductProcess>> stringListEntry : mapList.entrySet()) {
                one12.addAll(stringListEntry.getValue());
            }
            Map<String, List<ProductProcess>> collect1 = one12.stream().collect(Collectors.groupingBy(ProductProcess::getProductId));

            //存放所有分架后的数据
            List<Map<String, List<ProductProcess>>> endList = new ArrayList<>(mapList.size());
            //对每种产品进行分架
            for (Map.Entry<String, List<ProductProcess>> stringListEntry : mapList.entrySet()) {
                //流程卡list
                Map<String, List<ProductProcess>> listMap = new HashMap<>();
                //对每个单片进行分架
                for (ProductProcess productProcess : stringListEntry.getValue()) {
                    if (listMap.size() == 0) {
                        List<ProductProcess> processList = new ArrayList<>();
                        processList.add(productProcess);
                        listMap.put(stringListEntry.getKey() + "1", processList);
                    } else {
                        Map<String, List<ProductProcess>> forMap = new HashMap<>(listMap);
                        //遍历分架参数   进行条件赛选
                        for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
                            if (forMap.size() > 0) {
                                String string = stringObjectEntry.getValue().toString();
                                switch (stringObjectEntry.getKey()) {
                                    case "number": //每架最大数量
                                        forMap.entrySet().removeIf(entry -> Integer.parseInt(string) < (productProcess.getShelfNum() + entry.getValue().stream().mapToInt(ProductProcess::getShelfNum).sum()));
                                        break;
                                    case "area": //每架最大面积
                                        forMap.entrySet().removeIf(entry -> Double.parseDouble(string) < (productProcess.getItemArea() + entry.getValue().stream().mapToDouble(ProductProcess::getItemArea).sum()));
                                        break;
                                    case "weight": //每架最大重量
                                        forMap.entrySet().removeIf(entry -> Double.parseDouble(string) < (productProcess.getItemWeight() + entry.getValue().stream().mapToDouble(ProductProcess::getItemWeight).sum()));
                                        break;
                                    case "fold": //每架最大叠厚
                                        forMap.entrySet().removeIf(entry -> Integer.parseInt(string) < (productProcess.getItemThick() + entry.getValue().stream().mapToInt(ProductProcess::getItemThick).sum()));
                                        break;
                                    case "wideBig": //宽最大差距
                                        int i = Integer.parseInt(string);
                                        forMap.entrySet().removeIf(entry -> (productProcess.getItemWide() < (entry.getValue().stream().mapToInt(ProductProcess::getItemWide).max().getAsInt() - i) || i > (entry.getValue().stream().mapToInt(ProductProcess::getItemWide).min().getAsInt() + i)));
                                        break;
                                    case "wideSmall": //宽最小差距   未处理
//                                        lists.get(lists.size() - 1).stream().mapToDouble(ProductProcess::getItemWide).max();
                                        break;
                                    case "highBig": //高最大差距
                                        int i1 = Integer.parseInt(string);
                                        forMap.entrySet().removeIf(entry -> (productProcess.getItemHigh() < entry.getValue().stream().mapToInt(ProductProcess::getItemHigh).max().getAsInt() - i1 || i1 > entry.getValue().stream().mapToInt(ProductProcess::getItemHigh).min().getAsInt() + i1));
                                        break;
                                    case "highSmall": //高最小差距    未处理
//                                        lists.get(lists.size() - 1).stream().mapToDouble(ProductProcess::getItemHigh).max();
                                        break;
                                    case "isPosition":
                                        if (string.equals("true")) {
                                            forMap.entrySet().removeIf(entry -> entry.getValue().stream().noneMatch(productProcess1 -> productProcess1.getPosition().equals(productProcess.getPosition())));
                                        }
                                        break;
                                    default:
                                        //以上条件全部通过    将该片玻璃添加至其中一架
                                        listMap.get(forMap.keySet().toArray()[0]).add(productProcess);
                                        break;
                                }
                            } else {
                                //有一个参数不满足    新建一个架
                                List<ProductProcess> processList = new ArrayList<>();
                                processList.add(productProcess);
                                listMap.put(stringListEntry.getKey() + (listMap.size() + 1), processList);
                                break;
                            }
                        }
                    }
                }
                endList.add(listMap);
            }
            //分装流程卡信息
            Map<String, List<ProductProcess>> flowList = new HashMap<>();
            //遍历分架后的流程卡集合
            for (Map<String, List<ProductProcess>> listMap : endList) {
                //遍历流程卡
                for (Map.Entry<String, List<ProductProcess>> stringListEntry : listMap.entrySet()) {
                    //获取流程卡数量
                    List<ProductProcess> value = stringListEntry.getValue();
                    Map<String, List<ProductProcess>> collect = value.stream().collect(Collectors.groupingBy(ProductProcess::getProductId));
                    List<ProductProcess> processList = new ArrayList<>();
                    for (Map.Entry<String, List<ProductProcess>> listEntry : collect.entrySet()) {
                        ProductProcess process = MapUtil.mapToEntity(MapUtil.objectToMap(listEntry.getValue().get(0)), ProductProcess.class);
                        process.setShelfNum(listEntry.getValue().size());
                        processList.add(process);
                    }
                    flowList.put(stringListEntry.getKey(), processList);
                }
            }
            flowCards = packageFlow(flowList, map);
        } else {
            //依据   品种+厚度+工艺  进行分架
            for (ProductProcess productProcess : productProcesses) {
                //拼接key， 订单编号+品种+厚度+工艺id
                StringBuilder key = new StringBuilder();
                key.append(productProcess.getItemType())
                        .append(productProcess.getItemThick())
                        .append(productProcess.getProcessId());
                //判断三种属性是否相同
                if (mapList.containsKey(key.toString())) { // 相同
                    mapList.get(key.toString()).add(productProcess);
                } else {
                    List<ProductProcess> processList = new ArrayList<>();
                    processList.add(productProcess);
                    mapList.put(key.toString(), processList);
                }
            }
            //生成流程卡信息
            flowCards = packageFlow(mapList, map);
        }
        long end = System.currentTimeMillis();
        System.out.println("总计用时=" + (end - start));
        return CommonResult.success(flowCards);
    }

    /**
     * 手动分架-保存分架信息
     *
     * @param processList 已分架信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult saveSemiProduct(List<Map<String, Object>> processList) {
        try {
            //获取已分架信息
            List<ShelfDivisionBusiness> shelfDivisionBusinessList =
                    JSON.parseArray(JSON.toJSONString(processList), ShelfDivisionBusiness.class);
            //合并每个流程卡中重复的半产品信息
            shelfDivisionBusinessList = new ArrayList<>(shelfDivisionBusinessList.stream()
                    .collect(
                            Collectors.toMap(one -> one.getId() + one.getFlowCardNo(), a -> a, (o1, o2) -> {
                                o1.setItemNum(o1.getItemNum() + o2.getItemNum());
                                o1.setTotalWeight(o1.getTotalWeight() + o2.getTotalWeight());
                                o1.setTotalArea(o1.getTotalArea() + o2.getTotalArea());
                                return o1;
                            })
                    ).values()
            );
            //解析已分架信息    ------   根据流程卡分组
            Map<String, List<ShelfDivisionBusiness>> shelfGroup =
                    shelfDivisionBusinessList.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getFlowCardNo));
            System.out.println();
            //流程卡明细list
            List<ShelfDivisionBusiness> shelfDivisionBusinesses = new ArrayList<>();
            for (Map.Entry<String, List<ShelfDivisionBusiness>> stringListEntry : shelfGroup.entrySet()) {
                //获取流程卡编号
                String flowCardNo = stringListEntry.getKey();
                //流程卡id
                String flowId = UUID.randomUUID().toString();
                //计算总数、总面积、总重量
                int splitNum = 0;
                double totalArea = 0.000;
                double totalWeight = 0.000;
                Set<String> orderNoSet = new HashSet<>();
                Set<String> customNoSet = new HashSet<>();
                Set<String> productNameSet = new HashSet<>();
                Set<String> customerNameSet = new HashSet<>();
                //遍历流程卡明细
                for (ShelfDivisionBusiness shelfDivisionBusiness : stringListEntry.getValue()) {
                    shelfDivisionBusiness.setProductProcessId(shelfDivisionBusiness.getId());
                    shelfDivisionBusiness.setId(UUID.randomUUID().toString());
                    shelfDivisionBusiness.setFlowCardId(flowId);
                    shelfDivisionBusiness.setTotalArea(MapUtil.round(shelfDivisionBusiness.getTotalArea(), 3));
                    shelfDivisionBusiness.setTotalWeight(MapUtil.round(shelfDivisionBusiness.getTotalWeight(), 3));
                    shelfDivisionBusinesses.add(shelfDivisionBusiness);
                    //为流程卡数据做铺垫
                    splitNum += shelfDivisionBusiness.getItemNum();
                    totalArea += shelfDivisionBusiness.getTotalArea();
                    totalWeight += shelfDivisionBusiness.getTotalWeight();
                    orderNoSet.add(shelfDivisionBusiness.getOrderNo());
                    if (StringUtils.isNotEmpty(shelfDivisionBusiness.getCustomNo()))
                        customNoSet.add(shelfDivisionBusiness.getCustomNo());
                    productNameSet.add(shelfDivisionBusiness.getProductName());
                    customerNameSet.add(shelfDivisionBusiness.getCustomerName());
                }
                String orderNo = orderNoSet.toString().substring(1, orderNoSet.toString().length() - 1);
                String customNo = customNoSet.toString().substring(1, customNoSet.toString().length() - 1);
                String productName = productNameSet.toString().substring(1, productNameSet.toString().length() - 1);
                String customerName = customerNameSet.toString().substring(1, customerNameSet.toString().length() - 1);
                //封装流程卡对象
                FlowCard flowCard = new FlowCard();
                flowCard.setId(flowId);
                flowCard.setFlowCardNo(flowCardNo);
                flowCard.setMonolithicThick(stringListEntry.getValue().get(0).getItemThick());
                flowCard.setOrderNo(orderNo);
                flowCard.setCustomNo(customNo);
                flowCard.setProductName(productName);
                flowCard.setCustomerName(customerName);
                flowCard.setMonolithicName(stringListEntry.getValue().get(0).getItemName());
                flowCard.setProcessContent(stringListEntry.getValue().get(0).getProcessContent());
                flowCard.setSplitDate(new Date());
                flowCard.setSplitPerson(SecurityUtils.getLoginUser().getUser().getNickName());
                flowCard.setSplitNum(splitNum);
                flowCard.setTotalArea(MapUtil.round(totalArea, 3));
                flowCard.setTotalWeight(MapUtil.round(totalWeight, 3));
                flowCard.setProductionStatus(0);
                flowCardMapper.insert(flowCard);
            }
            boolean b = shelfDivisionBusinessService.saveBatch(shelfDivisionBusinesses);
            return new CommonResult(200, ResultConstants.CONSERVE_SUCCESS, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new CommonResult(500, ResultConstants.CONSERVE_ERROR, 0, null);
        }
    }

    /**
     * 编辑流程卡-查询产品的分架信息
     *
     * @param flowId 流程卡id
     */
    @Override
    public CommonResult<Map<String, Object>> updateQuery(String flowId) {
        Map<String, Object> returnMap = new HashMap<>();
        //查询流程卡明细
        List<ShelfDivisionBusiness> shelfDivisionBusinessList = queryFlowDetailed(flowId).getData();
        returnMap.put("shelf", shelfDivisionBusinessList);
        Set<String> productId = new HashSet<>();
        Set<String> orderNos = new HashSet<>();
        shelfDivisionBusinessList.forEach(one -> productId.add(one.getProductId()));
        shelfDivisionBusinessList.forEach(one -> orderNos.add(one.getOrderNo()));
        LambdaQueryWrapper<OrderProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderProduct::getId, productId);
        List<OrderProduct> orderProducts = orderProductMapper.selectList(wrapper);
        //查询每个产品单片信息
        if (orderProducts.size() > 0) {
            //查询产品所属订单信息
            LambdaQueryWrapper<OrderRecord> orderRecordWrapper = new LambdaQueryWrapper<>();
            orderRecordWrapper.in(OrderRecord::getOrderNo, orderNos);
            List<OrderRecord> orderRecords = orderMapper.selectList(orderRecordWrapper);
            //查询产品对应单片信息
            LambdaQueryWrapper<ProductProcess> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ProductProcess::getProductId, productId);
            queryWrapper.orderByAsc(ProductProcess::getOrderNo);
            queryWrapper.orderByAsc(ProductProcess::getProductId);
            queryWrapper.orderByAsc(ProductProcess::getItemSurface);
            List<ProductProcess> processList = productProcessMapper.selectList(queryWrapper);
            for (ProductProcess productProcess : processList) {
                for (OrderProduct orderProduct : orderProducts) {
                    if (productProcess.getProductId().equals(orderProduct.getId())) {
                        for (OrderRecord orderRecord : orderRecords) {
                            if (orderProduct.getOrderId().equals(orderRecord.getId()))
                                productProcess.setCustomerName(orderRecord.getCustomerName());
                            productProcess.setEntryName(orderRecord.getEntryName());
                            productProcess.setCustomNo(orderRecord.getCustomNo());
                        }
                        productProcess.setYesShelfNum(orderProduct.getShelfNum());
                        productProcess.setNoShelfNum(orderProduct.getNum() - orderProduct.getShelfNum());
                        productProcess.setShelfNum(orderProduct.getNum() - orderProduct.getShelfNum());
                        productProcess.setRequirement(orderProduct.getRequirement());
                    }
                }
            }
            returnMap.put("product", processList);
            return CommonResult.success(ResultConstants.QUERY_SUCCESS, returnMap);
        } else {
            return CommonResult.error(ResultConstants.QUERY_ERROR);
        }
    }

    /**
     * 编辑流程卡   ---   保存编辑后的数据
     *
     * @param processMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult saveUpdate(Map<String, Object> processMap) {
        try {
            //获取保存类型
            Object isNew = processMap.get("isNew");
            //获取已分架信息
            List<ShelfDivisionBusiness> shelfDivisionBusinessList = JSON.parseArray(JSON.toJSONString(processMap.get("haveManualData")), ShelfDivisionBusiness.class);
            shelfDivisionBusinessList = new ArrayList<>(shelfDivisionBusinessList.stream()
                    .collect(
                            Collectors.toMap(one -> one.getProductProcessId() + one.getFlowCardNo(), a -> a, (o1, o2) -> {
                                o1.setItemNum(o1.getItemNum() + o2.getItemNum());
                                o1.setTotalWeight(o1.getTotalWeight() + o2.getTotalWeight());
                                o1.setTotalArea(o1.getTotalArea() + o2.getTotalArea());
                                return o1;
                            })
                    ).values()
            );
            //解析已分架信息    ------   根据流程卡分组
            Map<String, List<ShelfDivisionBusiness>> shelfGroup = shelfDivisionBusinessList.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getFlowCardNo));
            if (!isNew.equals(2)) {
                //获取未分架信息
                List<ProductProcess> process = JSON.parseArray(JSON.toJSONString(processMap.get("noManualData")), ProductProcess.class);
                //依据产品分组
                Map<String, List<ProductProcess>> orderProducts = process.stream().collect(Collectors.groupingBy(ProductProcess::getProductId));
                //涉及需要修改的旧流程卡
                Map<String, List<ShelfDivisionBusiness>> oldFlowShelfList = null;
                if (isNew.equals(1)) {
                    //查询与产品相关的所有流程卡明细
                    List<ShelfDivisionBusiness> shelfDivisionBusinesses =
                            shelfDivisionBusinessMapper.selectList(
                                    Wrappers.<ShelfDivisionBusiness>query().lambda()
                                            .in(ShelfDivisionBusiness::getProductId, orderProducts.keySet())
                                            .notIn(ShelfDivisionBusiness::getFlowCardNo, shelfGroup.keySet())
                            );
                    //依据流程卡分组
                    oldFlowShelfList = shelfDivisionBusinesses.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getFlowCardNo));
                }
                //遍历未分架产品信息
                for (Map.Entry<String, List<ProductProcess>> stringListEntry : orderProducts.entrySet()) {
                    //获取剩余数量最大值
                    int max = stringListEntry.getValue().stream().mapToInt(ProductProcess::getNoShelfNum).max().getAsInt();
                    //获取剩余数量最小值
                    int min = stringListEntry.getValue().stream().mapToInt(ProductProcess::getNoShelfNum).min().getAsInt();
                    //判断产中中每个半产品的剩余数量是否相等
                    if (max != min) {
                        //过滤出需要修改的半产品
                        List<ProductProcess> productProcesses = stringListEntry.getValue().stream().filter(item -> item.getNoShelfNum() > min).collect(Collectors.toList());
                        if (isNew.equals(0)) { //生成新流程卡
                            shelfGroup = packNewFlow(shelfGroup, productProcesses, min);
                        } else { //修改旧流程卡
                            //遍历半产品
                            for (ProductProcess productProcess : productProcesses) {
                                //计算与最小值的差
                                int num = productProcess.getNoShelfNum() - min;
                                //遍历旧流程卡
                                for (Map.Entry<String, List<ShelfDivisionBusiness>> listEntry : oldFlowShelfList.entrySet()) {
                                    //遍历旧流程卡明细
                                    for (ShelfDivisionBusiness sd : listEntry.getValue()) {
                                        //判断是否是要修改的半产品
                                        if (productProcess.getId().equals(sd.getProductProcessId()) && num > 0) {
                                            sd.setItemNum(num + sd.getItemNum());
                                            sd.setTotalArea(MapUtil.round(sd.getItemNum() * productProcess.getItemArea(), 3));
                                            sd.setTotalWeight(MapUtil.round(sd.getItemNum() * productProcess.getItemWeight(), 3));
                                            num = 0;
                                        }
                                    }
                                }
                                //判断该半产品只有一个流程卡明细
                                if (num > 0) {
                                    shelfGroup = packNewFlow(shelfGroup, productProcesses, min);
                                }
                            }
                            assert oldFlowShelfList != null;
                            shelfGroup.putAll(oldFlowShelfList);
                        }
                    }
                }
            }
            //删除旧流程卡
            flowCardMapper.delete(Wrappers.<FlowCard>query().lambda().in(FlowCard::getFlowCardNo, shelfGroup.keySet()));
            shelfDivisionBusinessMapper.delete(Wrappers.<ShelfDivisionBusiness>query().lambda().in(ShelfDivisionBusiness::getFlowCardNo, shelfGroup.keySet()));
            //已经封装好的所有流程卡明细
            List<ShelfDivisionBusiness> shelfDivisionBusinesses = new ArrayList<>();
            //过滤分架数量为0的流程卡明细
            for (Map.Entry<String, List<ShelfDivisionBusiness>> stringListEntry : shelfGroup.entrySet()) {
                List<ShelfDivisionBusiness> collect = stringListEntry.getValue().stream().filter(item -> item.getItemNum() > 0).collect(Collectors.toList());
                if (collect.size() > 0) {
                    //流程卡id
                    String flowId = UUID.randomUUID().toString();
                    //计算总数、总面积、总重量
                    int splitNum = 0;
                    double totalArea = 0.000;
                    double totalWeight = 0.000;
                    Set<String> orderNoSet = new HashSet<>();
                    Set<String> customNoSet = new HashSet<>();
                    Set<String> productNameSet = new HashSet<>();
                    Set<String> customerNameSet = new HashSet<>();
                    //封装流程卡明细
                    for (ShelfDivisionBusiness shelfDivisionBusiness : stringListEntry.getValue()) {
                        //流程卡明细对象
                        shelfDivisionBusiness.setId(UUID.randomUUID().toString());
                        shelfDivisionBusiness.setFlowCardId(flowId);
                        shelfDivisionBusiness.setFlowCardNo(stringListEntry.getKey());
                        shelfDivisionBusinesses.add(shelfDivisionBusiness);
                        //为流程卡数据做铺垫
                        splitNum += shelfDivisionBusiness.getItemNum();
                        totalArea += shelfDivisionBusiness.getTotalArea();
                        totalWeight += shelfDivisionBusiness.getTotalWeight();
                        orderNoSet.add(shelfDivisionBusiness.getOrderNo());
                        customNoSet.add(shelfDivisionBusiness.getCustomNo());
                        productNameSet.add(shelfDivisionBusiness.getProductName());
                        customerNameSet.add(shelfDivisionBusiness.getCustomerName());
                    }
                    String orderNo = orderNoSet.toString().substring(1, orderNoSet.toString().length() - 1);
                    String customNo = customNoSet.toString().substring(1, customNoSet.toString().length() - 1);
                    String productName = productNameSet.toString().substring(1, productNameSet.toString().length() - 1);
                    String customerName = customerNameSet.toString().substring(1, customerNameSet.toString().length() - 1);
                    //封装流程卡对象
                    FlowCard flowCard = new FlowCard();
                    flowCard.setId(flowId);
                    flowCard.setFlowCardNo(stringListEntry.getKey());
                    flowCard.setMonolithicThick(stringListEntry.getValue().get(0).getItemThick());
                    flowCard.setOrderNo(orderNo);
                    flowCard.setCustomNo(customNo);
                    flowCard.setProductName(productName);
                    flowCard.setCustomerName(customerName);
                    flowCard.setMonolithicName(stringListEntry.getValue().get(0).getItemName());
                    flowCard.setProcessContent(stringListEntry.getValue().get(0).getProcessContent());
                    flowCard.setSplitDate(new Date());
                    flowCard.setSplitPerson(SecurityUtils.getLoginUser().getUser().getNickName());
                    flowCard.setSplitNum(splitNum);
                    flowCard.setTotalArea(MapUtil.round(totalArea, 3));
                    flowCard.setTotalWeight(MapUtil.round(totalWeight, 3));
                    flowCard.setProductionStatus(0);
                    flowCardMapper.insert(flowCard);
                }
            }
            shelfDivisionBusinessService.saveBatch(shelfDivisionBusinesses);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    public Map<String, List<ShelfDivisionBusiness>> packNewFlow(Map<String, List<ShelfDivisionBusiness>> shelfGroup, List<ProductProcess> productProcesses, int min) {
        Map<String, List<ProductProcess>> mapList = new HashMap<>();
        for (ProductProcess productProcess : productProcesses) {
            productProcess.setShelfNum(productProcess.getNoShelfNum() - min);
            //拼接key， 订单编号+品种+厚度+工艺id
            StringBuilder key = new StringBuilder();
            key.append(productProcess.getItemType())
                    .append(productProcess.getItemThick())
                    .append(productProcess.getProcessId());
            //判断三种属性是否相同
            if (mapList.containsKey(key.toString())) {
                mapList.get(key.toString()).add(productProcess);
            } else {
                List<ProductProcess> processList = new ArrayList<>();
                processList.add(productProcess);
                mapList.put(key.toString(), processList);
            }
        }
        List<FlowCard> flowCards = packageFlow(mapList, null);
        //分装流程卡数据
        for (FlowCard flowCard : flowCards) {
            if (shelfGroup.containsKey(flowCard.getFlowCardNo())) {
                String s = "";
                do {
                    s = productionNumberTwo(flowCard.getFlowCardNo());
                }while (shelfGroup.containsKey(s));
                shelfGroup.put(s, flowCard.getList());
            }else {
                shelfGroup.put(flowCard.getFlowCardNo(), flowCard.getList());
            }
        }
        return shelfGroup;
    }


    /**
     * 删除流程卡
     *
     * @param id 流程卡id
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult delFlowCard(String id) {
        try {
            //先校验删除流程卡中   每个产品的半产品数量是否相同
            LambdaQueryWrapper<ShelfDivisionBusiness> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShelfDivisionBusiness::getFlowCardId, id);
            List<ShelfDivisionBusiness> shelfDivisionBusinesses = shelfDivisionBusinessMapper.selectList(queryWrapper);
            if (shelfDivisionBusinesses.size() <= 0) {
                return CommonResult.success("该流程卡已删除，请刷新！");
            }
            //依据产品分组
            Map<String, List<ShelfDivisionBusiness>> productIds = shelfDivisionBusinesses.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getProductId));
            //存放修改流程卡、卡号
            Set<String> flowSet = new HashSet<>();
            //查询与删除产品相关的所有流程卡明细
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ne(ShelfDivisionBusiness::getFlowCardId, id);
            queryWrapper.in(ShelfDivisionBusiness::getProductId, productIds.keySet());
            List<ShelfDivisionBusiness> updateList = shelfDivisionBusinessMapper.selectList(queryWrapper);
            //依据流程卡分组
            Map<String, List<ShelfDivisionBusiness>> flowCardNos = updateList.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getFlowCardNo));
            //查询流程卡下所有明细
            Map<String, List<ShelfDivisionBusiness>> flowCards = new HashMap<>();
            if (flowCardNos.size() > 0) {
                flowCards = shelfDivisionBusinessMapper.selectList(
                        Wrappers.<ShelfDivisionBusiness>query().lambda()
                                .in(ShelfDivisionBusiness::getFlowCardNo, flowCardNos.keySet())
                ).stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getFlowCardNo));
            }
            //遍历每个产品
            for (Map.Entry<String, List<ShelfDivisionBusiness>> stringListEntry : productIds.entrySet()) {
                //声明一个不需要修改的半产品id集合
                Set<String> idSet = new HashSet<>();
                //获取该产品中   半产品数量最多的值
                int max = stringListEntry.getValue().stream().mapToInt(ShelfDivisionBusiness::getItemNum).max().getAsInt();
                stringListEntry.getValue().forEach(one -> {
                    if (one.getItemNum() == max) {
                        idSet.add(one.getProductProcessId());
                    }
                });
                //获取产品层数
                List<ProductProcess> processList = productProcessMapper.selectList(Wrappers.<ProductProcess>query().lambda().eq(ProductProcess::getProductId, stringListEntry.getKey()));
                //判断是否有要修改的  半产品
                if (idSet.size() < processList.size()) {
                    //遍历半产品
                    for (ProductProcess productProcess : processList) {
                        //判断   是否是  要修改的半产品
                        if (!idSet.contains(productProcess.getId())) {
                            //获取要修改的数量
                            int updateNum = 0;
                            for (ShelfDivisionBusiness shelfDivisionBusiness : stringListEntry.getValue()) {
                                if (shelfDivisionBusiness.getProductProcessId().equals(productProcess.getId())) {
                                    updateNum = max - shelfDivisionBusiness.getItemNum();
                                }
                            }
                            if (updateNum == 0) {
                                updateNum = max;
                            }
                            //遍历流程卡
                            for (Map.Entry<String, List<ShelfDivisionBusiness>> listEntry : flowCards.entrySet()) {
                                //判断
                                if (updateNum > 0) {
                                    //遍历流程卡明细
                                    for (ShelfDivisionBusiness sd : listEntry.getValue()) {
                                        //修改流程卡明细
                                        if (sd.getProductProcessId().equals(productProcess.getId())) {
                                            Integer itemNum = sd.getItemNum();
                                            sd.setItemNum(itemNum - updateNum);
                                            sd.setTotalArea(MapUtil.round(sd.getItemNum() * productProcess.getItemArea(), 3));
                                            sd.setTotalWeight(MapUtil.round(sd.getItemNum() * productProcess.getItemWeight(), 3));
                                            updateNum -= itemNum;
                                            flowSet.add(listEntry.getKey());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (flowSet.size() > 0) {
                //修改流程卡信息
                //删除旧流程卡明细
                shelfDivisionBusinessMapper.delete(Wrappers.<ShelfDivisionBusiness>query().lambda().in(ShelfDivisionBusiness::getFlowCardNo, flowCards.keySet()));
                //新增新的流程卡明细
                for (Map.Entry<String, List<ShelfDivisionBusiness>> stringListEntry : flowCards.entrySet()) {
                    List<ShelfDivisionBusiness> collect = stringListEntry.getValue().stream().filter(one -> one.getItemNum() > 0).collect(Collectors.toList());
                    if (collect.size() == 0) {
                        flowCardMapper.delete(Wrappers.<FlowCard>query().lambda().eq(FlowCard::getFlowCardNo, stringListEntry.getKey()));
                    } else {
                        int num = 0 ;
                        double area = 0.000;
                        double weight = 0.000;
                        Set<String> orderNoSet = new HashSet<>();
                        Set<String> customNoSet = new HashSet<>();
                        Set<String> productNameSet = new HashSet<>();
                        Set<String> customerNameSet = new HashSet<>();

                        for (ShelfDivisionBusiness shelfDivisionBusiness : collect) {
                            num += shelfDivisionBusiness.getItemNum();
                            area += shelfDivisionBusiness.getTotalArea();
                            weight += shelfDivisionBusiness.getTotalWeight();
                            orderNoSet.add(shelfDivisionBusiness.getOrderNo());
                            customNoSet.add(shelfDivisionBusiness.getCustomNo());
                            productNameSet.add(shelfDivisionBusiness.getProductName());
                            customerNameSet.add(shelfDivisionBusiness.getCustomerName());
                        }
                        LambdaUpdateWrapper<FlowCard> updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper.set(FlowCard::getSplitNum, num)
                                .set(FlowCard::getTotalArea, MapUtil.round(area, 3))
                                .set(FlowCard::getTotalWeight, MapUtil.round(weight, 3))
                                .set(FlowCard::getOrderNo, orderNoSet.toString().substring(1, orderNoSet.toString().length() - 1))
                                .set(FlowCard::getCustomNo, customNoSet.toString().substring(1, customNoSet.toString().length() - 1))
                                .set(FlowCard::getProductName, productNameSet.toString().substring(1, productNameSet.toString().length() - 1))
                                .set(FlowCard::getCustomerName, customerNameSet.toString().substring(1, customerNameSet.toString().length() - 1))
                                .eq(FlowCard::getFlowCardNo, stringListEntry.getKey());
                        flowCardMapper.update(null, updateWrapper);
                        shelfDivisionBusinessService.saveBatch(collect);
                    }
                }
                //声明一个返回信息对象
                StringBuilder msg = new StringBuilder();
                msg.append("对应流程卡“");
                msg.append(flowSet.toString().substring(1 , flowSet.toString().length()-1));
                msg.append("”的数量发生变化");
                return CommonResult.success(msg.toString());
            } else {
                return CommonResult.success(ResultConstants.DEL_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 校验流程卡状态
     *
     * @param id 流程卡id
     */
    @Override
    public CommonResult checkFlowCard(String id) {
        if (!StringUtils.isNotEmpty(id)) {
            return CommonResult.error("请选择数据！");
        }
        LambdaQueryWrapper<ShelfDivisionBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShelfDivisionBusiness::getFlowCardId, id);
        List<ShelfDivisionBusiness> shelfDivisionBusinesses = shelfDivisionBusinessMapper.selectList(wrapper);
        if (shelfDivisionBusinesses.size() <= 0) {
            return CommonResult.error("当前数据为缓存数据，请刷新！");
        }
        Map<String, List<ShelfDivisionBusiness>> collect = shelfDivisionBusinesses.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getOrderNo));
        Set<String> set = collect.keySet();
        LambdaQueryWrapper<FlowCard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowCard::getOrderNo, set);
        List<FlowCard> flowCards = flowCardMapper.selectList(queryWrapper);
        List<FlowCard> collect1 = flowCards.stream().filter(item -> item.getProductionStatus() != 0).collect(Collectors.toList());
        if (collect1.size() > 0) {
            return CommonResult.error("相关联的流程卡已生成，无法修改！");
        } else {
            return CommonResult.success();
        }
    }

    /**
     * 导出流程卡明细
     *
     * @param ids 流程卡id集合
     */
    @Override
    public void exportFlowDetailed(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<ShelfDivisionBusiness> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(ShelfDivisionBusiness::getFlowCardId, split);
        }
        wrapper.orderByDesc(ShelfDivisionBusiness::getFlowCardNo);
        List<ShelfDivisionBusiness> shelfDivisionBusinesses = shelfDivisionBusinessMapper.selectList(wrapper);
        ExcelUtil<ShelfDivisionBusiness> util = new ExcelUtil<ShelfDivisionBusiness>(ShelfDivisionBusiness.class);
        util.exportExcel(response, shelfDivisionBusinesses, "参数数据");
    }

    /**
     * 导出流程卡
     *
     * @param response
     * @param ids      流程卡id集合
     */
    @Override
    public void exportFlow(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<FlowCard> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(FlowCard::getId, split);
        }
        wrapper.orderByDesc(FlowCard::getFlowCardNo);
        List<FlowCard> flowCards = flowCardMapper.selectList(wrapper);
        ExcelUtil<FlowCard> util = new ExcelUtil<FlowCard>(FlowCard.class);
        util.exportExcel(response, flowCards, "参数数据");
    }

    /**
     * 导出与机器相关流程卡
     *
     * @param response
     * @param id       流程卡id
     */
    @Override
    public void exportMachineFlow(HttpServletResponse response, String id){
        List<ShelfDivisionBusiness> list = shelfDivisionBusinessMapper.selectByIds(id.split(","));
        List<FlowExcelPojo> flowExcelPojos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            FlowExcelPojo flowExcelPojo = new FlowExcelPojo();
            flowExcelPojo.setPIECES("N" + (i + 1) + "=");
            flowExcelPojo.setQuantity(list.get(i).getItemNum());
            flowExcelPojo.setBase(list.get(i).getItemW());
            flowExcelPojo.setHeight(list.get(i).getItemH());
            flowExcelPojo.setCustomer(list.get(i).getCustomerName());
            flowExcelPojo.setOrder(list.get(i).getOrderNo());
            flowExcelPojo.setFamily(list.get(i).getItemName() + list.get(i).getItemType());
            flowExcelPojo.setNote1(list.get(i).getRemarks());
            flowExcelPojo.setNote2(list.get(i).getRemarksOne());
            flowExcelPojo.setNote3(list.get(i).getRemarksTwo());
            flowExcelPojos.add(flowExcelPojo);
        }
        ExcelUtil<FlowExcelPojo> util = new ExcelUtil<FlowExcelPojo>(FlowExcelPojo.class);
        util.exportExcel(response, flowExcelPojos, "IMPORT");
    }

    /**
     * 导出迪赛模板
     *
     * @param response
     * @param id       流程卡id
     */
    @Override
    public void exportOptima(HttpServletResponse response, String id) {
        List<ShelfDivisionBusiness> list = shelfDivisionBusinessMapper.selectByIds(id.split(","));
        List<FlowOptimaPojo> pojoList = new ArrayList<>();
        for (ShelfDivisionBusiness one : list) {
            FlowOptimaPojo optimaPojo = new FlowOptimaPojo();
            optimaPojo.setQTAPZ(one.getItemNum());
            optimaPojo.setDIMXPZ(one.getItemW());
            optimaPojo.setDIMYPZ(one.getItemH());
            optimaPojo.setCLIENTE(one.getCustomerName());
            optimaPojo.setORDINE(one.getOrderNo());
            optimaPojo.setCODMAT(one.getItemName() + one.getItemType());
            optimaPojo.setNOTE1(one.getRemarks());
            optimaPojo.setNOTE2(one.getRemarksOne());
            optimaPojo.setNOTE3(one.getRemarksTwo());
            pojoList.add(optimaPojo);
        }
        ExcelUtil<FlowOptimaPojo> util = new ExcelUtil<FlowOptimaPojo>(FlowOptimaPojo.class);
        util.exportExcel(response, pojoList, "sheet1");
    }

    /**
     * 生成流程卡信息
     *
     * @param mapList
     * @return
     */
    public List<FlowCard> packageFlow(Map<String, List<ProductProcess>> mapList, Map<String, Object> map) {
        Map<String, List<ProductProcess>> listMap = new LinkedHashMap<>();
        if (map != null) {
            Stream<Map.Entry<String, List<ProductProcess>>> sorted = null;
            //排序
            switch (map.get("sortord").toString()) {
                case "weight": //按重量升序
                    sorted = mapList.entrySet().stream().sorted(
                            Comparator.comparing(
                                    x -> x.getValue().stream().mapToDouble(ProductProcess::getItemWeight).sum()
                            )
                    );
                    break;
                case "area": //按面积升序
                    sorted = mapList.entrySet().stream().sorted(
                            Comparator.comparing(
                                    x -> x.getValue().stream().mapToDouble(ProductProcess::getItemArea).sum()
                            )
                    );
                    break;
                case "num": //按数量升序
                    sorted = mapList.entrySet().stream().sorted(
                            Comparator.comparing(
                                    x -> x.getValue().stream().mapToInt(ProductProcess::getShelfNum).sum()
                            )
                    );
                    break;
            }

            assert sorted != null;
            Map<String, List<ProductProcess>> finalListMap = new LinkedHashMap<>();
            sorted.forEach(x -> {
                finalListMap.put(x.getKey(), x.getValue());
            });
            listMap = finalListMap;
        } else {
            listMap = mapList;
        }
        //流程卡list
        List<FlowCard> flowCards = new ArrayList<>();
        int x = 0;
        for (Map.Entry<String, List<ProductProcess>> stringListEntry : listMap.entrySet()) {
            //流程卡明细list
            List<ShelfDivisionBusiness> shelfDivisionBusinesses = new ArrayList<>();
            //分架后的产品
            List<ProductProcess> value = stringListEntry.getValue();
            //流程卡id
            String flowId = UUID.randomUUID().toString();
            //分装流程卡对象
            FlowCard flowCard = new FlowCard();
            flowCard.setId(flowId);
            flowCard.setFlowCardNo(productionNumber(x));
            //计算总数、总面积、总重量
            Integer splitNum = 0;
            Double totalArea = 0.000;
            Double totalWeight = 0.000;
            Set<String> orderNoSet = new HashSet<>();
            Set<String> customNoSet = new HashSet<>();
            Set<String> productNameSet = new HashSet<>();
            Set<String> customerNameSet = new HashSet<>();
            //分装流程卡明细对象
            for (ProductProcess productProcess : value) {
                ShelfDivisionBusiness shelfDivisionBusiness = new ShelfDivisionBusiness();
                shelfDivisionBusiness.setProductId(productProcess.getProductId());
                shelfDivisionBusiness.setSemiProductId(productProcess.getSemiProductId());
                shelfDivisionBusiness.setProductProcessId(productProcess.getId());
                shelfDivisionBusiness.setProductName(productProcess.getProductName());
                shelfDivisionBusiness.setFlowCardId(flowId);
                shelfDivisionBusiness.setFlowCardNo(flowCard.getFlowCardNo());
                shelfDivisionBusiness.setOrderNo(productProcess.getOrderNo());
                shelfDivisionBusiness.setCustomNo(productProcess.getCustomNo());
                shelfDivisionBusiness.setEntryName(productProcess.getEntryName());
                shelfDivisionBusiness.setPosition(productProcess.getPosition());
                shelfDivisionBusiness.setItemSurface(productProcess.getItemSurface());
                shelfDivisionBusiness.setCustomerName(productProcess.getCustomerName());
                shelfDivisionBusiness.setItemName(productProcess.getItemName());
                shelfDivisionBusiness.setItemType(productProcess.getItemType());
                shelfDivisionBusiness.setItemThick(productProcess.getItemThick());
                shelfDivisionBusiness.setProcessContent(productProcess.getProcessContent());
                shelfDivisionBusiness.setProcessId(productProcess.getProcessId());
                shelfDivisionBusiness.setItemW(productProcess.getItemWide());
                shelfDivisionBusiness.setItemH(productProcess.getItemHigh());
                shelfDivisionBusiness.setItemNum(productProcess.getShelfNum());
                shelfDivisionBusiness.setTotalArea(MapUtil.round(productProcess.getShelfNum() * productProcess.getItemArea(), 3));
                shelfDivisionBusiness.setTotalWeight(MapUtil.round(productProcess.getShelfNum() * productProcess.getItemWeight(), 3));
                shelfDivisionBusiness.setRequirement(productProcess.getRequirement());
                shelfDivisionBusinesses.add(shelfDivisionBusiness);

                splitNum += productProcess.getShelfNum();
                totalArea += (productProcess.getShelfNum() * productProcess.getItemArea());
                totalWeight += (productProcess.getShelfNum() * productProcess.getItemWeight());
                orderNoSet.add(productProcess.getOrderNo());
                if (StringUtils.isNotEmpty(productProcess.getCustomNo())) customNoSet.add(productProcess.getCustomNo());
                productNameSet.add(productProcess.getProductName());
                customerNameSet.add(productProcess.getCustomerName());
            }
            //拼接订单编号、自定义编号、产品名称、客户名称
            String orderNo = orderNoSet.toString().substring(1, orderNoSet.toString().length() - 1);
            String customNo = customNoSet.toString().substring(1, customNoSet.toString().length() - 1);
            String productName = productNameSet.toString().substring(1, productNameSet.toString().length() - 1);
            String customerName = customerNameSet.toString().substring(1, customerNameSet.toString().length() - 1);
            //分装流程卡对象
            flowCard.setMonolithicThick(value.get(0).getItemThick());
            flowCard.setOrderNo(orderNo);
            flowCard.setCustomNo(customNo);
            flowCard.setProductName(productName);
            flowCard.setCustomerName(customerName);
            flowCard.setMonolithicName(shelfDivisionBusinesses.get(0).getItemName());
            flowCard.setProcessContent(value.get(0).getProcessContent());
            flowCard.setSplitDate(new Date());
            flowCard.setSplitPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            flowCard.setSplitNum(splitNum);
            flowCard.setTotalArea(MapUtil.round(totalArea, 3));
            flowCard.setTotalWeight(MapUtil.round(totalWeight, 3));
            flowCard.setProductionStatus(0);
            flowCard.setList(shelfDivisionBusinesses);
            flowCards.add(flowCard);
            x++;
        }
        return flowCards;
    }

    //自动生成流程卡编号
    public String productionNumber(int i) {
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
            if (flowCardNo.contains("_")) {
                flowCardNo = flowCardNo.substring(0 , flowCardNo.indexOf("_"));
            }
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

    //编辑流程卡时  -  自动生成流程卡编号
    public String productionNumberTwo(String oldFlowNo) {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("10");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        int integer = 1;
        String format1 = format.format(new Date()).substring(4);
        //拼接符
        String[] splicer = {"[+]", "-"};
        //遍历拆分     找出编号后缀
        for (String ss : splicer) {
            String[] split = oldFlowNo.split(ss);
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
            String[] split = oldFlowNo.split(format1);
            String s = split[split.length - 1];
            integer = Integer.parseInt(s) + 1;
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
        return stringBuilder.toString();
    }


}
