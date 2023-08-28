package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.sales.OrderProductMapper;
import com.erp.floor.mapper.sales.OutsourcedMapper;
import com.erp.floor.mapper.sales.OutsourcedProductsMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.sales.service.OutsourcedService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-23 14:14
 */
@Service
public class OutsourcedServiceImpl extends ServiceImpl<OutsourcedMapper, Outsourced> implements OutsourcedService {
    @Resource
    private OutsourcedMapper outsourcedMapper;
    @Resource
    private OutsourcedProductsMapper outsourcedProductsMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private OrderProductMapper orderProductMapper;


    /**
     * 自动生成编号
     *
     * @return
     */
    @Override
    public String productionNumber() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<Outsourced> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Outsourced::getOutsourcedNo, format1);
        wrapper.like(Outsourced::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(Outsourced::getCreatedAt).last("limit 1");
        Outsourced orderRecord = outsourcedMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("wx");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (orderRecord != null) {
            String orderNo = orderRecord.getOutsourcedNo();
            //查询拼接符字典
            String[] splicer = {"[+]" , "-"};
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
     * 查询外协记录
     *
     * @param outsourced 外协对象
     */
    @Override
    public CommonResult<List<Outsourced>> queryOutsourced(Outsourced outsourced) {
        LambdaQueryWrapper<Outsourced> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(outsourced.getOutsourcedNo()))
            wrapper.eq(Outsourced::getOutsourcedNo, outsourced.getOutsourcedNo());
        if (StringUtils.isNotEmpty(outsourced.getOrderNo()))
            wrapper.like(Outsourced::getOrderNo, outsourced.getOrderNo());
        if (StringUtils.isNotEmpty(outsourced.getCustomerName()))
            wrapper.like(Outsourced::getCustomerName, outsourced.getCustomerName());
        if (StringUtils.isNotEmpty(outsourced.getCustomNo()))
            wrapper.like(Outsourced::getCustomNo, outsourced.getCustomNo());
        if (StringUtils.isNotEmpty(outsourced.getAuditStatus()))
            wrapper.eq(Outsourced::getAuditStatus, outsourced.getAuditStatus());
        if (StringUtils.isNotEmpty(outsourced.getPaymentStatus()))
            wrapper.eq(Outsourced::getPaymentStatus, outsourced.getPaymentStatus());
        if (StringUtils.isNotEmpty(outsourced.getWarehousingStatus()))
            wrapper.eq(Outsourced::getWarehousingStatus, outsourced.getWarehousingStatus());
        if (StringUtils.isNotEmpty(outsourced.getOutsourcingLeader()))
            wrapper.eq(Outsourced::getOutsourcingLeader, outsourced.getOutsourcingLeader());
        if (StringUtils.isNotEmpty(outsourced.getOutgoingDateStart()) && StringUtils.isNotEmpty(outsourced.getOutgoingDateEnd()))
            wrapper.between(Outsourced::getOutgoingDate, outsourced.getOutgoingDateStart(), outsourced.getOutgoingDateEnd());
        wrapper.orderByDesc(Outsourced::getOutsourcedNo);
        Integer integer = outsourcedMapper.selectCount(wrapper);
        PageHelper.startPage(outsourced.getPageNum(), outsourced.getPageSize());
        List<Outsourced> outsourceds = outsourcedMapper.selectList(wrapper);
        PageInfo<Outsourced> info = new PageInfo<>(outsourceds, outsourced.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, info.getList());
    }

    /**
     * 查询可外协产品
     *
     * @param orderProduct
     * @return
     */
    @Override
    public CommonResult<List<OrderProduct>> queryOutsourcedProduct(OrderProduct orderProduct) {
        List<OrderProduct> collect = orderProductMapper.outsourcedProduct(orderProduct)
                .stream().filter(item -> item.getNum() > item.getOutsourcedNum()).collect(Collectors.toList());
        List<OrderProduct> collect1 = collect.stream()
                .skip((orderProduct.getPageNum() - 1) * orderProduct.getPageSize()).limit(orderProduct.getPageSize())
                .collect(Collectors.toList());
        return new CommonResult(200, "查询成功", collect.size(), collect1);
    }

    /**
     * 新增外协单
     *
     * @param outsourced
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult addOutsourced(Outsourced outsourced) {
        try {
            List<OrderProduct> orderProductList = outsourced.getOrderProductList();
            //外协信息
            String id = UUID.randomUUID().toString();
            Set<String> orderNo = new HashSet<>();
            Set<String> customNo = new HashSet<>();
            Set<String> customerName = new HashSet<>();
            Integer totalNum = 0;
            Double totalAmount = 0.000;
            //封装外协产品信息
            for (OrderProduct orderProduct : orderProductList) {
                //新增外协产品信息
                OutsourcedProducts one = new OutsourcedProducts();
                one.setProductId(orderProduct.getId());
                one.setOrderId(orderProduct.getOrderId());
                one.setOrderNo(orderProduct.getOrderNo());
                one.setCustomNo(orderProduct.getCustomNo());
                one.setEntryName(orderProduct.getEntryName());
                one.setCustomerName(orderProduct.getCustomerName());
                one.setOutsourcingNum(orderProduct.getNoShelfNum());
                one.setWarehouseNum(0);
                one.setOutsourcingPrice(orderProduct.getUnitPrice());
                one.setOutsourcingId(id);
                one.setOutsourcingNo(outsourced.getOutsourcedNo());
                one.setOutsourcingWorkmanship(orderProduct.getRemarks());
                one.setProcessingRequirements(orderProduct.getRequirement());
                outsourcedProductsMapper.insert(one);
                //汇总信息
                orderNo.add(orderProduct.getOrderNo());
                customNo.add(orderProduct.getCustomNo());
                customerName.add(orderProduct.getCustomerName());
                totalNum += one.getOutsourcingNum();
                totalAmount += (one.getOutsourcingNum() * one.getOutsourcingPrice());
                //修改产品外协数量
                orderProductMapper.update(
                        null,
                        Wrappers.<OrderProduct>update()
                                .lambda()
                                .set(OrderProduct::getOutsourcedNum, orderProduct.getOutsourcedNum() + orderProduct.getNoShelfNum())
                                .eq(OrderProduct::getId, orderProduct.getId())
                );
            }
            outsourced.setId(id);
            outsourced.setAuditStatus("未审核");
            outsourced.setWarehousingStatus("未入库");
            outsourced.setPaymentStatus("未付款");
            outsourced.setOrderNo(orderNo.toString().substring(1, orderNo.toString().length() -1));
            outsourced.setCustomNo(customNo.toString().substring(1, customNo.toString().length() -1));
            outsourced.setCustomerName(customerName.toString().substring(1, customerName.toString().length() -1));
            outsourced.setOutsourcingNum(totalNum);
            outsourced.setWarehousingNum(0);
            outsourced.setOutsourcingAmount(outsourced.getOtherCost() != null ? totalAmount + outsourced.getOtherCost() : totalAmount);
            outsourced.setCreatedAt(new Date());
            outsourced.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            outsourcedMapper.insert(outsourced);
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 查看明细
     *
     * @param id
     */
    @Override
    public CommonResult<List<OutsourcedProducts>> detailedView(String id, int type) {
        List<OutsourcedProducts> outsourcedProducts = outsourcedProductsMapper.detailedView(id.split(","));
        if (type == 1) {
            //过滤入库完成明细
            outsourcedProducts = outsourcedProducts.stream().filter(item -> item.getOutsourcingNum() > item.getWarehouseNum()).collect(Collectors.toList());
        }
        return CommonResult.success(outsourcedProducts);
    }

    /**
     * 编辑查询
     *
     * @param id 外协id
     */
    @Override
    public CommonResult<List<OrderProduct>> updateQuery(String id) {
        //查询外协明细
        List<OutsourcedProducts> outsourcedProducts = outsourcedProductsMapper.selectList(Wrappers.<OutsourcedProducts>query().lambda().eq(OutsourcedProducts::getOutsourcingId, id));
        Set<String> set = outsourcedProducts.stream().collect(Collectors.groupingBy(OutsourcedProducts::getProductId)).keySet();
        //查询产品
        List<OrderProduct> orderProducts = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().in(OrderProduct::getId, set));
        //封装对象
        for (OrderProduct orderProduct : orderProducts) {
            for (OutsourcedProducts outsourcedProduct : outsourcedProducts) {
                if (orderProduct.getId().equals(outsourcedProduct.getProductId())) {
                    orderProduct.setUnitPrice(outsourcedProduct.getOutsourcingPrice());
                    orderProduct.setNoShelfNum(outsourcedProduct.getOutsourcingNum());
                    orderProduct.setRequirement(outsourcedProduct.getProcessingRequirements());
                    orderProduct.setRemarks(outsourcedProduct.getOutsourcingWorkmanship());
                    orderProduct.setOrderNo(outsourcedProduct.getOrderNo());
                    orderProduct.setCustomNo(outsourcedProduct.getCustomNo());
                    orderProduct.setEntryName(outsourcedProduct.getEntryName());
                    orderProduct.setCustomerName(outsourcedProduct.getCustomerName());
                    orderProduct.setOutsourcedNum(orderProduct.getOutsourcedNum() - outsourcedProduct.getOutsourcingNum());
                }
            }
        }
        return CommonResult.success(orderProducts);
    }

    /**
     * 编辑保存
     *
     * @param outsourced
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult updateOutsourced(Outsourced outsourced) {
        try {
            //查询旧明细
            List<OutsourcedProducts> outsourcedProducts = outsourcedProductsMapper.detailedView(outsourced.getId().split(","));
            //还原外协数量
            for (OutsourcedProducts outsourcedProduct : outsourcedProducts) {
                LambdaUpdateWrapper<OrderProduct> wrapper = new LambdaUpdateWrapper<>();
                wrapper.set(OrderProduct::getOutsourcedNum, (outsourcedProduct.getOutsourcedNum() - outsourcedProduct.getOutsourcingNum()));
                wrapper.eq(OrderProduct::getId, outsourcedProduct.getProductId());
                orderProductMapper.update(null, wrapper);
            }
            //删除旧明细
            outsourcedProductsMapper.delete(Wrappers.<OutsourcedProducts>query().lambda().eq(OutsourcedProducts::getOutsourcingId, outsourced.getId()));
            //新增明细
            List<OrderProduct> orderProductList = outsourced.getOrderProductList();
            //外协信息
            String id = outsourced.getId();
            Set<String> orderNo = new HashSet<>();
            Set<String> customNo = new HashSet<>();
            Set<String> customerName = new HashSet<>();
            Integer totalNum = 0;
            Double totalAmount = 0.000;
            //封装外协产品信息
            for (OrderProduct orderProduct : orderProductList) {
                //新增外协产品信息
                OutsourcedProducts one = new OutsourcedProducts();
                one.setProductId(orderProduct.getId());
                one.setOrderId(orderProduct.getOrderId());
                one.setOrderNo(orderProduct.getOrderNo());
                one.setCustomNo(orderProduct.getCustomNo());
                one.setEntryName(orderProduct.getEntryName());
                one.setCustomerName(orderProduct.getCustomerName());
                one.setOutsourcingNum(orderProduct.getNoShelfNum());
                one.setWarehouseNum(0);
                one.setOutsourcingPrice(orderProduct.getUnitPrice());
                one.setOutsourcingId(id);
                one.setOutsourcingNo(outsourced.getOutsourcedNo());
                one.setOutsourcingWorkmanship(orderProduct.getRemarks());
                one.setProcessingRequirements(orderProduct.getRequirement());
                outsourcedProductsMapper.insert(one);
                //汇总信息
                orderNo.add(orderProduct.getOrderNo());
                customNo.add(orderProduct.getCustomNo());
                customerName.add(orderProduct.getCustomerName());
                totalNum += one.getOutsourcingNum();
                totalAmount += (one.getOutsourcingNum() * one.getOutsourcingPrice());

                OrderProduct orderProduct1 = orderProductMapper.selectOne(Wrappers.<OrderProduct>query().lambda().eq(OrderProduct::getId, orderProduct.getId()));
                orderProduct1.setOutsourcedNum(orderProduct1.getOutsourcedNum() + orderProduct.getNoShelfNum());
                //修改产品外协数量
                orderProductMapper.updateById(orderProduct1);
            }
            outsourced.setOrderNo(orderNo.toString().substring(1, orderNo.toString().length() -1));
            outsourced.setCustomNo(customNo.toString().substring(1, customNo.toString().length() -1));
            outsourced.setCustomerName(customerName.toString().substring(1, customerName.toString().length() -1));
            outsourced.setOutsourcingNum(totalNum);
            outsourced.setOutsourcingAmount(outsourced.getOtherCost() != null ? totalAmount + outsourced.getOtherCost() : totalAmount);
            outsourced.setUpdatedAt(new Date());
            outsourced.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            outsourcedMapper.updateById(outsourced);
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    /**
     * 删除外协
     *
     * @param ids 外协ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult delOutsourced(String ids) {
        try {
            for (String id : ids.split(",")) {
                List<OutsourcedProducts> outsourcedProducts = outsourcedProductsMapper.detailedView(id.split(","));
                //还原外协数量
                for (OutsourcedProducts outsourcedProduct : outsourcedProducts) {
                    LambdaUpdateWrapper<OrderProduct> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.set(OrderProduct::getOutsourcedNum, (outsourcedProduct.getOutsourcedNum() - outsourcedProduct.getOutsourcingNum()));
                    wrapper.eq(OrderProduct::getId, outsourcedProduct.getProductId());
                    orderProductMapper.update(null, wrapper);
                }
                //删除旧明细
                outsourcedProductsMapper.delete(Wrappers.<OutsourcedProducts>query().lambda().eq(OutsourcedProducts::getOutsourcingId, id));
                //删除外协记录
                outsourcedMapper.deleteById(id);
            }
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 审核-消审
     *
     * @param ids
     * @param type
     */
    @Override
    public CommonResult outsourcedExamine(String ids, int type, String people, String text) {
        LambdaUpdateWrapper<Outsourced> wrapper = new LambdaUpdateWrapper<>();
        String[] split = ids.split(",");
        if (type == 0) { //审核通过
            wrapper.set(Outsourced::getAuditStatus, "已通过")
                    .set(Outsourced::getReviewPeople, people)
                    .set(Outsourced::getReviewReason, text)
                    .in(Outsourced::getId, split);
        }else if (type == 1) { //审核不通过
            wrapper.set(Outsourced::getAuditStatus, "未通过")
                    .set(Outsourced::getReviewPeople, people)
                    .set(Outsourced::getReviewReason, text)
                    .in(Outsourced::getId, split);
        } else {//  消审
            wrapper.set(Outsourced::getAuditStatus, "未审核")
                    .set(Outsourced::getReviewPeople, null)
                    .set(Outsourced::getReviewReason, null)
                    .in(Outsourced::getId, split);
        }
        outsourcedMapper.update(null, wrapper);
        return CommonResult.success();
    }

    /**
     * 导出外协记录
     *
     * @param response
     * @param ids      外协ids
     */
    @Override
    public void exportOutsourced(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<Outsourced> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(ids)) {
            wrapper.in(Outsourced::getId, ids.split(","));
        }
        wrapper.orderByDesc(Outsourced::getOutsourcedNo);
        List<Outsourced> outsourceds = outsourcedMapper.selectList(wrapper);
        ExcelUtil<Outsourced> util = new ExcelUtil<Outsourced>(Outsourced.class);
        util.exportExcel(response, outsourceds, "参数数据");
    }

    /**
     * 导出外协记录明细
     *
     * @param response
     * @param ids      外协ids
     */
    @Override
    public void exportOutsourcedDetailed(HttpServletResponse response, String ids) {
        List<OutsourcedProducts> outsourcedProductsList = outsourcedProductsMapper.detailedView(ids.split(","));
        ExcelUtil<OutsourcedProducts> util = new ExcelUtil<OutsourcedProducts>(OutsourcedProducts.class);
        util.exportExcel(response, outsourcedProductsList, "参数数据");
    }


}
