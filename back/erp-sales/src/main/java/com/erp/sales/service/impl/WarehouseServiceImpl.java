package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.HttpStatus;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.MapUtil;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.mapper.sales.*;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.sales.service.WarehouseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-20 16:48
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<CpRecordMapper, CpRecord> implements WarehouseService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderProductMapper orderProductMapper;
    @Resource
    private OrderReviewMapper orderReviewMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private CpRecordMapper cpRecordMapper;
    @Resource
    private CpProductRecordMapper cpProductRecordMapper;


    /**
     * @param orderRecordVo
     */
    @Override
    public CommonResult<List<OrderRecord>> queryWarehouseOrder(OrderRecordVo orderRecordVo) {
        if (orderRecordVo.getPageNum() != null && orderRecordVo.getPageSize() != null) {
            LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderRecord::getIsDel, 0);
            wrapper.eq(OrderRecord::getIsCache, 0);
            wrapper.gt(OrderRecord::getWarehouseQuantity, 0);
            if (StringUtils.isNotEmpty(orderRecordVo.getContactNumber())) {
                wrapper.like(OrderRecord::getContactNumber, orderRecordVo.getContactNumber());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getContacts())) {
                wrapper.like(OrderRecord::getContacts, orderRecordVo.getContacts());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getCustomerName())) {
                wrapper.like(OrderRecord::getCustomerName, orderRecordVo.getCustomerName());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getCustomNo())) {
                wrapper.eq(OrderRecord::getCustomNo, orderRecordVo.getCustomNo());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getOrderNo())) {
                wrapper.eq(OrderRecord::getOrderNo, orderRecordVo.getOrderNo());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getEntryName())) {
                wrapper.like(OrderRecord::getEntryName, orderRecordVo.getEntryName());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getPreparer())) {
                wrapper.like(OrderRecord::getPreparer, orderRecordVo.getPreparer());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getPreparationDateStart()) && StringUtils.isNotEmpty(orderRecordVo.getPreparationDateEnd())) {
                wrapper.between(OrderRecord::getPreparationDate, orderRecordVo.getPreparationDateStart(), orderRecordVo.getPreparationDateEnd());
            }
            wrapper.orderByDesc(OrderRecord::getOrderNo);
            Integer integer = orderMapper.selectCount(wrapper);
            PageHelper.startPage(orderRecordVo.getPageNum(), orderRecordVo.getPageSize());
            List<OrderRecord> orderRecords = orderMapper.selectList(wrapper);
            PageInfo<OrderRecord> pageInfo = new PageInfo<>(orderRecords, orderRecordVo.getPageSize());
            for (OrderRecord record : pageInfo.getList()) {
                LambdaQueryWrapper<OrderReview> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(OrderReview::getOrderId, record.getId());
                record.setOrderReview(orderReviewMapper.selectOne(queryWrapper));
            }
            return new CommonResult<>(HttpStatus.SUCCESS, ResultConstants.QUERY_SUCCESS, integer, pageInfo.getList());
        } else {
            return CommonResult.error("请输入页容量和页码");
        }
    }

    /**
     * 查询订单产品
     *
     * @param id 订单id
     */
    @Override
    public CommonResult<List<OrderProduct>> queryOrderProduct(String id) {
        List<OrderProduct> orderProducts = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().eq(OrderProduct::getOrderId, id));
        return CommonResult.success(orderProducts);
    }

    /**
     * 查询未完成入库订单产品
     */
    @Override
    public CommonResult<List<OrderProduct>> queryProduct(OrderProduct orderProduct) {
        //查询已通过审核的订单产品
        List<OrderProduct> orderProducts = orderProductMapper.warehouseProduct(orderProduct);
        //过滤已经完成入库产品
        List<OrderProduct> collect = null;
        if (orderProduct.getWarehousingQuantity() == 0) {
            //查询未入库产品
            collect = orderProducts.stream().filter(item -> item.getWarehousingQuantity() < item.getNum()).collect(Collectors.toList());
        } else {
            //查询已入库产品
            collect = orderProducts.stream().filter(item -> item.getWarehousingQuantity() != null && item.getWarehousingQuantity() > 0).collect(Collectors.toList());
        }
        List<OrderProduct> collect1 = collect.stream().skip((orderProduct.getPageNum() - 1) * orderProduct.getPageSize()).limit(orderProduct.getPageSize()).
                collect(Collectors.toList());
        return new CommonResult<>(200,"查询成功", collect.size(), collect1);
    }

    /**
     * 自动给生成编号
     *
     * @param type
     * @return
     */
    @Override
    public String productionNumber(int type) {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = null;
        LambdaQueryWrapper<CpRecord> wrapper = new LambdaQueryWrapper<>();
        if (type == 0) {
            wrapper.eq(CpRecord::getOperationType, "入库");
            sysNumberingRules = sysNumberingRulesMapper.selectById("cprk");
        } else if (type == 1) {
            wrapper.eq(CpRecord::getOperationType, "出库");
            sysNumberingRules = sysNumberingRulesMapper.selectById("cpck");
        }
        wrapper.like(CpRecord::getOperationNo, format1);
        wrapper.like(CpRecord::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(CpRecord::getCreatedAt).last("limit 1");
        CpRecord orderRecord = cpRecordMapper.selectOne(wrapper);
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (orderRecord != null) {
            String orderNo = orderRecord.getOperationNo();
            //查询拼接符字典
            String[] splicer = {"[+]", "-"};
            int integer = 1;
            //遍历拆分     找出编号后缀
            for (String ss : splicer) {
                String[] split = orderNo.split(ss);
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
                String[] split = orderNo.split(format1);
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

    /**
     * 操作出入库
     *
     * @param cpRecord 操作记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult productWarehouse(CpRecord cpRecord) {
        try {
            //操作id
            String id = UUID.randomUUID().toString();
            Double totalArea = 0.000;
            Double totalWeight = 0.000;
            List<OrderProduct> orderProducts = cpRecord.getOrderProducts();
            //新增成品入库明细
            for (OrderProduct orderProduct : orderProducts) {
                CpProductRecord one = new CpProductRecord();
                one.setCpRecordId(id);
                one.setOrderProductId(orderProduct.getId());
                one.setProductNumber(orderProduct.getNoShelfNum());
                Double area = orderProduct.getSingleArea() * orderProduct.getNoShelfNum();
                Double weight = orderProduct.getProductWeight() / orderProduct.getNum() * orderProduct.getNoShelfNum();
                totalArea += area;
                totalWeight += weight;
                one.setWarehouseLocation(orderProduct.getWarehouseLocation());
                one.setShelfLocation(orderProduct.getShelfLocation());
                one.setProductArea(MapUtil.round(area, 3));
                one.setProductWeight(MapUtil.round(weight, 3));
                cpProductRecordMapper.insert(one);
                if (cpRecord.getOperationType().equals("入库")) {
                    //修改产品的入库数量
                    orderProduct.setWarehousingQuantity(orderProduct.getNoShelfNum() + orderProduct.getWarehousingQuantity());
                } else {
                    //修改产品的出库数量
                    orderProduct.setWarehousingQuantity(orderProduct.getWarehousingQuantity() - orderProduct.getNoShelfNum());
                }
                orderProductMapper.updateById(orderProduct);
            }
            //修改订单入库 数量
            Map<String, List<OrderProduct>> collect = orderProducts.stream().collect(Collectors.groupingBy(OrderProduct::getOrderId));
            List<OrderRecord> orderRecords = orderMapper.selectList(Wrappers.<OrderRecord>query().lambda().in(OrderRecord::getId, collect.keySet()));
            for (Map.Entry<String, List<OrderProduct>> stringListEntry : collect.entrySet()) {
                for (OrderRecord orderRecord : orderRecords) {
                    if (stringListEntry.getKey().equals(orderRecord.getId())) {
                        int sum = stringListEntry.getValue().stream().mapToInt(OrderProduct::getNoShelfNum).sum();
                        if (cpRecord.getOperationType().equals("入库")) {
                            orderRecord.setWarehouseQuantity(orderRecord.getWarehouseQuantity() + sum);
                        } else {
                            orderRecord.setWarehouseQuantity(orderRecord.getWarehouseQuantity() - sum);
                        }
                        orderMapper.updateById(orderRecord);
                    }
                }
            }
            //新增入库记录
            cpRecord.setId(id);
            cpRecord.setCreatedAt(new Date());
            cpRecord.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            cpRecord.setOperationNumber(orderProducts.stream().mapToInt(OrderProduct::getNoShelfNum).sum());
            cpRecord.setOperationArea(MapUtil.round(totalArea, 3));
            cpRecord.setOperationWeight(MapUtil.round(totalWeight, 3));
            cpRecordMapper.insert(cpRecord);
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 查询操作信息
     *
     * @param cpRecord
     */
    @Override
    public CommonResult<List<CpRecord>> queryWarehouseData(CpRecord cpRecord) {
        LambdaQueryWrapper<CpRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(cpRecord.getOperationPeople()))
            wrapper.like(CpRecord::getOperationPeople, cpRecord.getOperationPeople());
        if (StringUtils.isNotEmpty(cpRecord.getOperationType()))
            wrapper.eq(CpRecord::getOperationType, cpRecord.getOperationType());
        if (StringUtils.isNotEmpty(cpRecord.getOperationNo()))
            wrapper.eq(CpRecord::getOperationNo, cpRecord.getOperationNo());
        if (StringUtils.isNotEmpty(cpRecord.getOperationDateStart()) && StringUtils.isNotEmpty(cpRecord.getOperationDateEnd()))
            wrapper.between(CpRecord::getOperationDate, cpRecord.getOperationDateStart(), cpRecord.getOperationDateEnd());
        wrapper.orderByDesc(CpRecord::getOperationNo);
        Integer integer = cpRecordMapper.selectCount(wrapper);
        PageHelper.startPage(cpRecord.getPageNum(), cpRecord.getPageSize());
        List<CpRecord> cpRecords = cpRecordMapper.selectList(wrapper);
        PageInfo<CpRecord> info = new PageInfo<>(cpRecords, cpRecord.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, info.getList());
    }

    /**
     * 查询操作信息明细
     *
     * @param id
     */
    @Override
    public CommonResult<List<CpProductRecord>> warehouseDeliverData(String id) {
        return CommonResult.success(cpProductRecordMapper.queryProduct(id));
    }


}
