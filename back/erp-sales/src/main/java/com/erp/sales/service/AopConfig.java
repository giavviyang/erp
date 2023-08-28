package com.erp.sales.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.erp.common.utils.StringUtils;
import com.erp.floor.mapper.sales.*;
import com.erp.floor.pojo.sales.domain.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-18 16:56
 */
@Aspect
@Component
public class AopConfig {
    @Resource
    private OrderProductMapper orderProductMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private ProductProcessMapper productProcessMapper;
    @Resource
    private ShelfDivisionBusinessMapper shelfDivisionBusinessMapper;
    @Resource
    private FlowCardMapper flowCardMapper;
    @Resource
    private ShelfRecordMapper shelfRecordMapper;
    @Resource
    private PackingRecordMapper packingRecordMapper;
    @Resource
    private PackingBusinessMapper packingBusinessMapper;
    @Resource
    private DeliverBusinessMapper deliverBusinessMapper;
    @Resource
    private DeliveryShelfMapper deliveryShelfMapper;


    /**
     * 创建切入点：execution表达式指定针对某个（些）方法，进行切入
     */
    //自然分架保存---切人点
    @Pointcut("execution(* com.erp.sales.controller.FlowCardController.saveFlowCard(..))")
    public void saveFlowCard() {
    }

    //手动分架保存---切人点
    @Pointcut("execution(* com.erp.sales.service.FlowCardService.saveSemiProduct(..))")
    public void saveSemiProduct() {
    }

    //编辑流程卡---切人点
    @Pointcut("execution(* com.erp.sales.service.FlowCardService.saveUpdate(..))")
    public void saveUpdate() {
    }

    //删除流程卡---切人点
    @Pointcut("execution(* com.erp.sales.service.FlowCardService.delFlowCard(..))")
    public void delFlowCard() {
    }

    //新增发货单---切人点
    @Pointcut("execution(* com.erp.sales.service.DeliverRecordService.saveDeliver(..))")
    public void saveDeliver() {
    }

    //编辑发货单---切人点
    @Pointcut("execution(* com.erp.sales.service.DeliverRecordService.updateDeliver(..))")
    public void updateDeliver() {
    }

    //废置发货单---切人点
    @Pointcut("execution(* com.erp.sales.service.DeliverRecordService.delDeliver(..))")
    public void delDeliver() {
    }


    /**
     * 自然分架保存---后置通知
     */
    @Async
    @AfterReturning("saveFlowCard()")
    public void afterSaveFlowCard(JoinPoint point) {
        Object[] args = point.getArgs();
        List<FlowCard> flowCards = (List<FlowCard>) args[0];
        //分架明细集合
        List<ShelfDivisionBusiness> businesses = new ArrayList<>();
        for (FlowCard flowCard : flowCards) {
            businesses.addAll(flowCard.getList());
        }
        Map<String, List<ShelfDivisionBusiness>> collect = businesses.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getProductId));
        for (Map.Entry<String, List<ShelfDivisionBusiness>> stringListEntry : collect.entrySet()) {
            //查询每个产品的层数
            LambdaQueryWrapper<ProductProcess> queryWrapper = new LambdaQueryWrapper<>();
            //修改产品分架数量
            LambdaQueryWrapper<OrderProduct> updateWrapper = new LambdaQueryWrapper<>();
            String productId = stringListEntry.getKey();
            queryWrapper.eq(ProductProcess::getProductId, productId);
            Integer integer = productProcessMapper.selectCount(queryWrapper);
            List<ShelfDivisionBusiness> value = stringListEntry.getValue();
            //计算分架片数总和
            int sum = value.stream().mapToInt(ShelfDivisionBusiness::getItemNum).sum() / integer;
            //查询旧产品
            updateWrapper.eq(OrderProduct::getId, productId);
            OrderProduct orderProduct = orderProductMapper.selectOne(updateWrapper);
            orderProduct.setShelfNum(orderProduct.getShelfNum() + sum);
            orderProductMapper.updateById(orderProduct);
        }
        /*         处理订单  ---   修改订单的分架状态         */
        Map<String, List<ShelfDivisionBusiness>> orderRecord = businesses.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getOrderNo));
        updateOrder(orderRecord.keySet());
        System.out.println("自然分架保存---后置通知");
    }

    /**
     * 手动分架保存---后置通知
     */
    @Async
    @AfterReturning("saveSemiProduct()")
    public void afterSaveSemiProduct(JoinPoint point) {
        Object[] args = point.getArgs();
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) args[0];
        /*      修改产品分架数量       */
        //依据产品id去重    查找分架的产品，以及分架数量
        List<Map<String, Object>> newList = mapList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparing(m -> m.get("productId").toString()))
                        ), ArrayList::new
                )
        );
        //修改产品的已分架信息
        for (Map<String, Object> map : newList) {
            LambdaUpdateWrapper<OrderProduct> wrapper = new LambdaUpdateWrapper<>();
            OrderProduct orderProduct = orderProductMapper.selectById(map.get("productId").toString());
            Integer shelfNum = orderProduct.getShelfNum() + Integer.parseInt(map.get("itemNum").toString());
            wrapper.set(OrderProduct::getShelfNum, shelfNum).eq(OrderProduct::getId, map.get("productId"));
            orderProductMapper.update(null, wrapper);
        }
        //依据订单编号分组   ---   查找涉及分架订单
        Map<String, List<Map<String, Object>>> collect = mapList.stream().collect(Collectors.groupingBy(item -> item.get("orderNo").toString()));
        updateOrder(collect.keySet());
        System.out.println("手动分架保存---后置通知");
    }

    /**
     * 编辑流程卡保存---后置通知
     */
    @Async
    @AfterReturning("saveUpdate()")
    public void afterSaveUpdate(JoinPoint point) {
        Object[] args = point.getArgs();
        Map<String, Object> processMap = (Map<String, Object>) args[0];
        //获取未分架信息
        List<ProductProcess> process = JSON.parseArray(JSON.toJSONString(processMap.get("noManualData")), ProductProcess.class);
        //依据产品分组
        Map<String, List<ProductProcess>> collect = process.stream().collect(Collectors.groupingBy(ProductProcess::getProductId));
        boolean isAll = true;
        //遍历每个产品
        for (Map.Entry<String, List<ProductProcess>> stringListEntry : collect.entrySet()) {
            int min = stringListEntry.getValue().stream().mapToInt(ProductProcess::getNoShelfNum).min().getAsInt();
            if (min != 0) {
                isAll = false;
            }
            LambdaUpdateWrapper<OrderProduct> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(OrderProduct::getShelfNum, stringListEntry.getValue().get(0).getNum() - min).eq(OrderProduct::getId, stringListEntry.getValue().get(0).getProductId());
            orderProductMapper.update(null, wrapper);
        }
        //判断修改的产品是否全部分架
        if (isAll) {
            List<OrderProduct> orderProducts =
                    orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().eq(OrderProduct::getOrderNo, process.get(0).getOrderNo()));
            //获取未完成分架产品数量
            int size = orderProducts.stream().filter(item -> item.getShelfNum() < item.getNum()).collect(Collectors.toList()).size();
            if (size == 0) {
                //全部分架    修改分架状态
                orderMapper.update(null,
                        Wrappers.<OrderRecord>update().lambda()
                                .set(OrderRecord::getRackSplittingStatus, 2)
                                .eq(OrderRecord::getOrderNo,process.get(0).getOrderNo())
                );
            }

        }

    }

    /**
     * 删除流程卡保存---后置通知
     */
    @AfterReturning("delFlowCard()")
    public void afterDelFlowCard(JoinPoint point) {
        Object[] args = point.getArgs();
        String id = args[0].toString();
        //先校验删除流程卡中   每个产品的半产品数量是否相同
        LambdaQueryWrapper<ShelfDivisionBusiness> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShelfDivisionBusiness::getFlowCardId, id);
        List<ShelfDivisionBusiness> shelfDivisionBusinesses = shelfDivisionBusinessMapper.selectList(queryWrapper);
        if (shelfDivisionBusinesses.size() > 0) {
            //依据产品分组
            Map<String, List<ShelfDivisionBusiness>> productIds = shelfDivisionBusinesses.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getProductId));
            for (Map.Entry<String, List<ShelfDivisionBusiness>> product : productIds.entrySet()) {
                int max = product.getValue().stream().mapToInt(ShelfDivisionBusiness::getItemNum).max().getAsInt();
                OrderProduct orderProduct = orderProductMapper.selectOne(Wrappers.<OrderProduct>query().lambda().eq(OrderProduct::getId, product.getKey()));
                LambdaUpdateWrapper<OrderProduct> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(OrderProduct::getShelfNum, orderProduct.getShelfNum() - max)
                        .eq(OrderProduct::getId, product.getKey());
                orderProductMapper.update(null, updateWrapper);
            }
            flowCardMapper.delete(Wrappers.<FlowCard>query().lambda().eq(FlowCard::getId, id));
            shelfDivisionBusinessMapper.delete(Wrappers.<ShelfDivisionBusiness>query().lambda().eq(ShelfDivisionBusiness::getFlowCardId, id));
            //修改订单状态
            Map<String, List<ShelfDivisionBusiness>> collect = shelfDivisionBusinesses.stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getOrderNo));
            updateOrder(collect.keySet());
        }
    }

    /* 修改订单分架状态 */
    public void updateOrder(Set<String> orderNos) {
        if (orderNos.size() > 0) {
            for (String s : orderNos) {
                if (StringUtils.isNotEmpty(s)) {
                    //查询订单下的所有产品
                    LambdaQueryWrapper<OrderProduct> queryWrapper = new LambdaQueryWrapper<>();
                    //修改订单的分架状态
                    LambdaUpdateWrapper<OrderRecord> updateWrapper = new LambdaUpdateWrapper<>();
                    queryWrapper.eq(OrderProduct::getOrderNo, s);
                    //查询订单下的所有产品
                    List<OrderProduct> orderProducts = orderProductMapper.selectList(queryWrapper);
                    //过滤掉全部分架的产品
                    List<OrderProduct> flag = orderProducts.stream().filter(ont -> !ont.getShelfNum().equals(ont.getNum())).collect(Collectors.toList());
                    //判断该订单下的所有产品是否全部分架
                    if (flag.size() == 0) {
                        updateWrapper.set(OrderRecord::getRackSplittingStatus, 2).eq(OrderRecord::getOrderNo, s);
                    } else {
                        updateWrapper.set(OrderRecord::getRackSplittingStatus, 1).eq(OrderRecord::getOrderNo, s);
                    }
                    List<OrderProduct> collect = orderProducts.stream().filter(one -> one.getShelfNum() == 0).collect(Collectors.toList());
                    if (collect.size() == orderProducts.size()) {
                        updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper.set(OrderRecord::getRackSplittingStatus, 0).eq(OrderRecord::getOrderNo, s);
                    }
                    orderMapper.update(null, updateWrapper);
                }
            }
        }

    }

    @Async
    @AfterReturning("saveDeliver()")
    public void saveDeliver(JoinPoint point) {
        Object[] args = point.getArgs();
        DeliverRecord deliverRecord = (DeliverRecord) args[0];
        List<Map<String, Object>> deliverBusiness = deliverRecord.getDeliverBusiness();
        List<DeliveryShelf> deliveryShelfList = deliverRecord.getDeliveryShelfList();
        //添加出入库记录
        for (DeliveryShelf deliveryShelf : deliveryShelfList) {
            ShelfRecord shelfRecord = new ShelfRecord();
            shelfRecord.setDeliverId(deliverRecord.getId());
            shelfRecord.setShipmentNo(deliverRecord.getDeliverNo());
            shelfRecord.setRecordType("出库");
            shelfRecord.setNum(deliveryShelf.getFrameNum());
            shelfRecord.setShelfId(deliveryShelf.getFrameId());
            shelfRecord.setOperationDate(new Date());
            shelfRecord.setPerson(deliverRecord.getDeliverPerson());
            shelfRecordMapper.insert(shelfRecord);
        }
        //获取   打包或产品id
        Set<Object> productIds = deliverBusiness.stream().collect(Collectors.groupingBy(item -> item.get("productId"))).keySet();
        //修改产品发货数量
        List<OrderProduct> orderProducts = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().in(OrderProduct::getId, productIds));
        for (Map<String, Object> business : deliverBusiness) {
            for (OrderProduct orderProduct : orderProducts) {
                if (business.get("productId").equals(orderProduct.getId())) {
                    int num = (Integer) business.get("completionNum") + orderProduct.getOrderDeliverNum();
                    orderProduct.setOrderDeliverNum(num);
                    orderProductMapper.updateById(orderProduct);
                }
            }
        }
        //修改订单发货状态
        updateOrderDeliverState(orderProducts);
        //修改打包数据
        if (deliverBusiness.get(0).get("packId") != null && !deliverBusiness.get(0).get("packId").equals("")) {
            Set<Object> id = deliverBusiness.stream().collect(Collectors.groupingBy(item -> item.get("id"))).keySet();
            //获取打包集合
            List<PackingBusiness> packList = packingBusinessMapper.selectList(Wrappers.<PackingBusiness>query().lambda().in(PackingBusiness::getId, id));
            for (Map<String, Object> business : deliverBusiness) {
                for (PackingBusiness packingBusiness : packList) {
                    if (business.get("id").equals(packingBusiness.getId())) {
                        int num = (Integer) business.get("completionNum") + packingBusiness.getPackDeliverNum();
                        packingBusiness.setPackDeliverNum(num);
                        packingBusinessMapper.updateById(packingBusiness);
                    }
                }
            }
            //获取打包id
            Set<String> set = packList.stream().collect(Collectors.groupingBy(PackingBusiness::getPackId)).keySet();
            List<PackingBusiness> packListAll = packingBusinessMapper.selectList(Wrappers.<PackingBusiness>query().lambda().in(PackingBusiness::getPackId, set));
            updatePackDeliverState(packListAll);
        }
    }

    @Async
    @Before("updateDeliver()")
    public void updateDeliver(JoinPoint point) {
        Object[] args = point.getArgs();
        //获取发货信息
        DeliverRecord deliverRecord = (DeliverRecord) args[0];
        //获取发货明细
        List<Map<String, Object>> deliverBusiness = deliverRecord.getDeliverBusiness();
        //获取发货货架
        List<DeliveryShelf> deliveryShelfList = deliverRecord.getDeliveryShelfList();
        //删除货架出入库信息
        shelfRecordMapper.delete(Wrappers.<ShelfRecord>query().lambda().eq(ShelfRecord::getDeliverId, deliverRecord.getId()));
        //修改发货货架
        for (DeliveryShelf deliveryShelf : deliveryShelfList) {
            ShelfRecord shelfRecord = new ShelfRecord();
            //判断   新增货架
            if (!StringUtils.isNotEmpty(deliveryShelf.getFrameId())) {
                shelfRecord.setShelfId(deliveryShelf.getFrameId());
            } else {
                shelfRecord.setShelfId(deliveryShelf.getId());
            }
            shelfRecord.setShipmentNo(deliverRecord.getDeliverNo());
            shelfRecord.setDeliverId(deliverRecord.getId());
            shelfRecord.setRecordType("出库");
            shelfRecord.setNum(deliveryShelf.getFrameNum());
            shelfRecord.setOperationDate(new Date());
            shelfRecord.setPerson(deliverRecord.getDeliverPerson());
            shelfRecordMapper.insert(shelfRecord);
        }
        //查询旧发货明细
        List<DeliverBusiness> deliverBusinessList = deliverBusinessMapper.selectList(
                Wrappers.<DeliverBusiness>query().lambda().eq(DeliverBusiness::getDeliverId, deliverRecord.getId()));

        /*     获取需要修改产品         */
        Set<String> productIdSet = new HashSet<>();
        productIdSet.addAll(deliverBusinessList.stream().collect(Collectors.groupingBy(DeliverBusiness::getProductId)).keySet());
        /*     获取需要修改发货单       */
        Set<String> packIdSet = new HashSet<>();
        for (DeliverBusiness business : deliverBusinessList) {
            if (StringUtils.isNotEmpty(business.getPackId())) packIdSet.add(business.getPackId());
        }
        for (Map<String, Object> business : deliverBusiness) {
            Object productId = business.get("productId");
            productIdSet.add(productId.toString());
            if (deliverRecord.getDeliverType() == 1) {
                packIdSet.add(business.get("packId").toString());
            }
        }
        //查询需要修改的产品
        List<OrderProduct> orderProducts = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().in(OrderProduct::getId, productIdSet));
        //修改产品的发货数量
        for (OrderProduct orderProduct : orderProducts) {
            int updateNum = 0;
            //加上修改前数量
            for (DeliverBusiness business : deliverBusinessList) {
                if (business.getProductId().equals(orderProduct.getId())) {
                    updateNum += business.getDeliverNum();
                }
            }
            //减去修改后数量
            for (Map<String, Object> business : deliverBusiness) {
                if (orderProduct.getId().equals(business.get("productId"))) {
                    updateNum -= Integer.parseInt(business.get("noShelfNum").toString());
                }
            }
            //判断是否需要修改
            if (updateNum != 0) {
                orderProduct.setOrderDeliverNum(orderProduct.getOrderDeliverNum() - updateNum);
                orderProductMapper.updateById(orderProduct);
            }
        }
        //修改订单发货状态
        updateOrderDeliverState(orderProducts);
        //判断是否是打包发货
        if (deliverRecord.getDeliverType() == 1) {
            List<PackingBusiness> list = packingBusinessMapper.selectList(Wrappers.<PackingBusiness>query().lambda().in(PackingBusiness::getPackId, packIdSet));
            for (PackingBusiness packingBusiness : list) {
                int updateNum = 0;
                //加上修改前数量
                for (DeliverBusiness business : deliverBusinessList) {
                    if (packingBusiness.getPackId().equals(business.getPackId()) && business.getProductId().equals(packingBusiness.getProductId())) {
                        updateNum += business.getDeliverNum();
                    }
                }
                //减去修改后数量
                for (Map<String, Object> business : deliverBusiness) {
                    if (packingBusiness.getPackId().equals(business.get("packId")) && packingBusiness.getProductId().equals(business.get("productId"))) {
                        updateNum -= Integer.parseInt(business.get("noShelfNum").toString());
                    }
                }
                if (updateNum != 0) {
                    packingBusiness.setPackDeliverNum(packingBusiness.getPackDeliverNum() - updateNum);
                    packingBusinessMapper.updateById(packingBusiness);
                }
            }
            //修改打包数据的发货状态
            updatePackDeliverState(list);
        }
    }

    @Async
    @AfterReturning("delDeliver()")
    public void delDeliver(JoinPoint point) {
        Object[] args = point.getArgs();
        //获取发货信息
        String ids = (String) args[0];
        String[] deliverIds = ids.split(",");
        //删除货架记录
        shelfRecordMapper.delete(Wrappers.<ShelfRecord>update().lambda().in(ShelfRecord::getDeliverId, deliverIds));
        //查询发货明细
        List<DeliverBusiness> deliverBusinessList = deliverBusinessMapper.selectList(Wrappers.<DeliverBusiness>query().lambda().in(DeliverBusiness::getDeliverId, deliverIds));
        //依据产品id分组
        Map<String, List<DeliverBusiness>> collect = deliverBusinessList.stream().collect(Collectors.groupingBy(DeliverBusiness::getProductId));
        //查询产品信息
        List<OrderProduct> orderProducts = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().in(OrderProduct::getId, collect.keySet()));
        //修改产品中发货数量
        for (OrderProduct orderProduct : orderProducts) {
            for (Map.Entry<String, List<DeliverBusiness>> stringListEntry : collect.entrySet()){
                if (stringListEntry.getKey().equals(orderProduct.getId())) {
                    orderProduct.setOrderDeliverNum(orderProduct.getOrderDeliverNum() - stringListEntry.getValue().stream().mapToInt(DeliverBusiness::getDeliverNum).sum());
                    orderProductMapper.updateById(orderProduct);
                }
            }
        }
        //修改订单状态
        updateOrderDeliverState(orderProducts);
        //
        List<PackingBusiness> packingBusinesses = new ArrayList<>();
        //修改打包中发货数量
        for (DeliverBusiness deliverBusiness : deliverBusinessList) {
            if (StringUtils.isNotEmpty(deliverBusiness.getPackId())) {
                LambdaQueryWrapper<PackingBusiness> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(PackingBusiness::getPackId, deliverBusiness.getPackId());
                wrapper.eq(PackingBusiness::getProductId, deliverBusiness.getProductId());
                PackingBusiness packingBusiness = packingBusinessMapper.selectOne(wrapper);
                packingBusinesses.add(packingBusiness);
                packingBusiness.setPackDeliverNum(packingBusiness.getPackDeliverNum() - deliverBusiness.getDeliverNum());
                packingBusinessMapper.updateById(packingBusiness);
            }
        }
        if (packingBusinesses.size() > 0) {
            Set<String> set = packingBusinesses.stream().collect(Collectors.groupingBy(PackingBusiness::getPackId)).keySet();
            updatePackDeliverState(packingBusinessMapper.selectList(Wrappers.<PackingBusiness>query().lambda().in(PackingBusiness::getPackId, set)));
        }
        //删除发货明细
        deliverBusinessMapper.delete(Wrappers.<DeliverBusiness>query().lambda().in(DeliverBusiness::getDeliverId, deliverIds));
    }

    void updateOrderDeliverState(List<OrderProduct> orderProducts) {
        Set<String> orderIds = orderProducts.stream().collect(Collectors.groupingBy(OrderProduct::getOrderId)).keySet();
        List<OrderProduct> orderProducts1 = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().in(OrderProduct::getOrderId, orderIds));
        for (Map.Entry<String, List<OrderProduct>> stringListEntry : orderProducts1.stream().collect(Collectors.groupingBy(OrderProduct::getOrderId)).entrySet()) {
            List<OrderProduct> collect = stringListEntry.getValue().stream().filter(item -> item.getNum() > item.getOrderDeliverNum()).collect(Collectors.toList());
            if (collect.size() <= 0) {
                orderMapper.update(null,
                        Wrappers.<OrderRecord>update().lambda()
                                .set(OrderRecord::getShipmentStatus, 2)
                                .eq(OrderRecord::getId, stringListEntry.getKey()));
            }
            List<OrderProduct> collect1 = stringListEntry.getValue().stream().filter(item -> item.getOrderDeliverNum() == 0).collect(Collectors.toList());
            if (collect.size() == stringListEntry.getValue().size() && collect1.size() != stringListEntry.getValue().size()) {
                orderMapper.update(null,
                        Wrappers.<OrderRecord>update().lambda()
                                .set(OrderRecord::getShipmentStatus, 1)
                                .eq(OrderRecord::getId, stringListEntry.getKey()));
            }
            if (collect1.size() == stringListEntry.getValue().size()) {
                orderMapper.update(null,
                        Wrappers.<OrderRecord>update().lambda()
                                .set(OrderRecord::getShipmentStatus, 0)
                                .eq(OrderRecord::getId, stringListEntry.getKey()));
            }
        }
    }

    void updatePackDeliverState(List<PackingBusiness> packListAll) {
        for (Map.Entry<String, List<PackingBusiness>> stringListEntry : packListAll.stream().collect(Collectors.groupingBy(PackingBusiness::getPackId)).entrySet()) {
            List<PackingBusiness> collect = stringListEntry.getValue().stream().filter(item -> item.getPackNum() > item.getPackDeliverNum()).collect(Collectors.toList());
            if (collect.size() <= 0) {
                packingRecordMapper.update(null,
                        Wrappers.<PackingRecord>update().lambda()
                                .set(PackingRecord::getDeliverState, "全部发货")
                                .eq(PackingRecord::getId, stringListEntry.getKey()));
            }
            List<PackingBusiness> collect1 = stringListEntry.getValue().stream().filter(item -> item.getPackDeliverNum() != 0).collect(Collectors.toList());
            if (collect1.size() > 0 && collect.size() >0) {
                packingRecordMapper.update(null,
                        Wrappers.<PackingRecord>update().lambda()
                                .set(PackingRecord::getDeliverState, "部分发货")
                                .eq(PackingRecord::getId, stringListEntry.getKey()));
            }else {
                packingRecordMapper.update(null,
                        Wrappers.<PackingRecord>update().lambda()
                                .set(PackingRecord::getDeliverState, "未发货")
                                .eq(PackingRecord::getId, stringListEntry.getKey()));
            }
        }
    }
}
