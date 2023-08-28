package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.config.RuoYiConfig;
import com.erp.common.constant.HttpStatus;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.MapUtil;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.file.FileUploadUtils;
import com.erp.common.utils.file.FileUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.sales.*;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.sales.domain.vo.DeliverRecordVo;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.floor.pojo.sales.domain.vo.PackingRecordVo;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.sales.service.DeliverBusinessService;
import com.erp.sales.service.DeliverRecordService;
import com.erp.sales.service.DeliveryShelfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 11:06
 */
@Service
public class DeliverRecordServiceImpl extends ServiceImpl<DeliverRecordMapper, DeliverRecord> implements DeliverRecordService {
    @Resource
    private DeliverRecordMapper deliverRecordMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private OrderProductMapper orderProductMapper;
    @Resource
    private PackingBusinessMapper packingBusinessMapper;
    @Resource
    private DeliverBusinessService deliverBusinessService;
    @Resource
    private DeliveryShelfService deliveryShelfService;
    @Resource
    private DeliverBusinessMapper deliverBusinessMapper;
    @Resource
    private DeliveryShelfMapper deliveryShelfMapper;
    @Resource
    private DeliverReceiptMapper deliverReceiptMapper;
    @Resource
    private DeliverReceiptFileMapper deliverReceiptFileMapper;
    @Resource
    private ShelfRecordMapper shelfRecordMapper;


    /**
     * 自动生成发货编号
     */
    @Override
    public String productionNumber() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<DeliverRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(DeliverRecord::getDeliverNo, format1);
        wrapper.like(DeliverRecord::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(DeliverRecord::getCreatedAt).last("limit 1");
        DeliverRecord deliverRecord = deliverRecordMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("12");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (deliverRecord != null) {
            String packagingNo = deliverRecord.getDeliverNo();
            //查询拼接符字典
            String[] splicer = {"[+]", "-"};
            int integer = 1;
            //遍历拆分     找出编号后缀
            for (String ss : splicer) {
                String[] split = packagingNo.split(ss);
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
                String[] split = packagingNo.split(format1);
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
     * 查询发货数据
     *
     * @param deliverRecordVo 发货查询类
     */
    @Override
    public CommonResult<List<DeliverRecord>> queryDeliver(DeliverRecordVo deliverRecordVo) {
        if (deliverRecordVo.getPageNum() == null) {
            return CommonResult.error(ResultConstants.PAGE_NUM);
        }
        if (deliverRecordVo.getPageSize() == null && deliverRecordVo.getPageSize() == 0) {
            return CommonResult.error(ResultConstants.PAGE_SIZE);
        }
        LambdaQueryWrapper<DeliverRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeliverRecord::getIsDel, deliverRecordVo.getIsDel());
        if (StringUtils.isNotEmpty(deliverRecordVo.getId()))
            wrapper.eq(DeliverRecord::getId, deliverRecordVo.getId());
        if (StringUtils.isNotEmpty(deliverRecordVo.getDeliverNo()))
            wrapper.eq(DeliverRecord::getDeliverNo, deliverRecordVo.getDeliverNo());
        if (StringUtils.isNotNull(deliverRecordVo.getDeliverStatus()))
            wrapper.eq(DeliverRecord::getDeliverStatus, deliverRecordVo.getDeliverStatus());
        if (StringUtils.isNotEmpty(deliverRecordVo.getDeliverPerson()))
            wrapper.eq(DeliverRecord::getDeliverPerson, deliverRecordVo.getDeliverPerson());
        if (StringUtils.isNotEmpty(deliverRecordVo.getDeliverMode()))
            wrapper.eq(DeliverRecord::getDeliverMode, deliverRecordVo.getDeliverMode());
        if (StringUtils.isNotEmpty(deliverRecordVo.getDeliverAddress()))
            wrapper.like(DeliverRecord::getDeliverAddress, deliverRecordVo.getDeliverAddress());
        if (StringUtils.isNotEmpty(deliverRecordVo.getReviewPerson()))
            wrapper.eq(DeliverRecord::getReviewPerson, deliverRecordVo.getReviewPerson());
        if (StringUtils.isNotEmpty(deliverRecordVo.getOrderNo()))
            wrapper.like(DeliverRecord::getOrderNo, deliverRecordVo.getOrderNo());
        if (StringUtils.isNotEmpty(deliverRecordVo.getCustomNo()))
            wrapper.like(DeliverRecord::getCustomNo, deliverRecordVo.getCustomNo());
        if (StringUtils.isNotEmpty(deliverRecordVo.getProductName()))
            wrapper.like(DeliverRecord::getProductName, deliverRecordVo.getProductName());
        if (StringUtils.isNotEmpty(deliverRecordVo.getCustomerName()))
            wrapper.like(DeliverRecord::getCustomerName, deliverRecordVo.getCustomerName());
        if (StringUtils.isNotEmpty(deliverRecordVo.getEntryName()))
            wrapper.like(DeliverRecord::getEntryName, deliverRecordVo.getEntryName());
        if (StringUtils.isNotNull(deliverRecordVo.getDeliverDateStart()) && StringUtils.isNotNull(deliverRecordVo.getDeliverDateEnd())) {
            wrapper.between(DeliverRecord::getDeliverDate, deliverRecordVo.getDeliverStatus(), deliverRecordVo.getDeliverDateEnd());
        }
        Integer integer = deliverRecordMapper.selectCount(wrapper);
        PageHelper.startPage(deliverRecordVo.getPageNum(), deliverRecordVo.getPageSize());
        List<DeliverRecord> deliverRecords = deliverRecordMapper.selectList(wrapper);
        PageInfo<DeliverRecord> pageInfo = new PageInfo<>(deliverRecords, deliverRecordVo.getPageSize());
        return new CommonResult(200, ResultConstants.QUERY_SUCCESS, integer, pageInfo.getList());
    }

    /**
     * 获取可发货产品
     *
     * @param orderRecordVo
     * @return
     */
    @Override
    public CommonResult<List<OrderProduct>> obtainOrderProduct(OrderRecordVo orderRecordVo) {
        //查询完工数量不为0的产品
        List<OrderProduct> orderProducts = orderProductMapper.obtainOrderProduct(orderRecordVo);
        //过滤可发货产品
        Iterator<OrderProduct> iterator = orderProducts.iterator();
        while (iterator.hasNext()) {
            OrderProduct next = iterator.next();
            if (next.getNum() > next.getOrderDeliverNum()) {
                next.setNoShelfNum(next.getNum() - next.getOrderDeliverNum());
            } else {
                iterator.remove();
            }
        }
        List<OrderProduct> collect1 = orderProducts.stream().skip((orderRecordVo.getPageNum() - 1) * orderRecordVo.getPageSize()).limit(orderRecordVo.getPageSize()).collect(Collectors.toList());
        return new CommonResult<>(200, "查询成功", orderProducts.size(), collect1);
    }

    /**
     * 打包获取
     *
     * @param packingRecordVo
     */
    @Override
    public CommonResult<List<PackingBusiness>> obtainPackProduct(PackingRecordVo packingRecordVo) {
        //查询打包信息
        List<PackingBusiness> list = packingBusinessMapper.obtainPackProduct(packingRecordVo);
        Iterator<PackingBusiness> iterator = list.iterator();
        while (iterator.hasNext()) {
            PackingBusiness next = iterator.next();
            //判断订单产品和打包产品是否有可发货数量
            if (next.getNum() > next.getOrderDeliverNum() && next.getCompletionNum() > next.getOrderDeliverNum() && next.getPackNum() > next.getPackDeliverNum()) {
                //判断产品的可发货数量是否大于打包产品的可发货数量
                if (next.getCompletionNum() - next.getOrderDeliverNum() > next.getPackNum() - next.getPackDeliverNum()) {
                    next.setNoShelfNum(next.getPackNum() - next.getPackDeliverNum());
                } else {
                    next.setNoShelfNum(next.getCompletionNum() - next.getOrderDeliverNum());
                }
            } else {
                iterator.remove();
            }
        }
        List<PackingBusiness> collect1 = list.stream().skip((packingRecordVo.getPageNum() - 1) * packingRecordVo.getPageSize()).limit(packingRecordVo.getPageSize()).collect(Collectors.toList());
        return new CommonResult<>(200, "查询成功", list.size(), collect1);
    }

    /**
     * 新增发货
     *
     * @param deliverRecord 发货信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult saveDeliver(DeliverRecord deliverRecord) {
        try {
            //获取发货货架
            List<DeliveryShelf> deliveryShelfList = deliverRecord.getDeliveryShelfList();
            //获取发货明细
            List<Map<String, Object>> deliverBusiness = deliverRecord.getDeliverBusiness();
            //遍历发货货架
            Double shelfAmount = 0.000;
            for (DeliveryShelf deliveryShelf : deliveryShelfList) {
                deliveryShelf.setDeliverId(deliverRecord.getId());
                deliveryShelf.setFrameId(deliveryShelf.getId());
                deliveryShelf.setId(UUID.randomUUID().toString());
                deliveryShelf.setReceiptNum(0);
                if(deliveryShelf.getFramePrice() != null) {
                    shelfAmount += deliveryShelf.getFrameNum() * deliveryShelf.getFramePrice();
                }
            }
            List<DeliverBusiness> deliverBusinessList = new ArrayList<>();
            Set<Object> orderId = new HashSet<>();
            Set<Object> orderNo = new HashSet<>();
            Set<Object> customNo = new HashSet<>();
            Set<Object> entryName = new HashSet<>();
            Set<Object> customerName = new HashSet<>();
            Set<Object> productName = new HashSet<>();
            //遍历发货产品
            for (Map<String, Object> business : deliverBusiness) {
                //封装发货明细
                DeliverBusiness deliver = new DeliverBusiness();
                deliver.setId(UUID.randomUUID().toString());
                deliver.setDeliverId(deliverRecord.getId());
                deliver.setDeliverNo(deliverRecord.getDeliverNo());
                deliver.setOrderId(business.get("orderId").toString());
                deliver.setProductId(business.get("productId").toString());
                if (business.get("packId") != null && !business.get("packId").equals("")) {
                    deliver.setPackId(business.get("packId").toString());
                    deliverRecord.setDeliverType(1);
                } else {
                    if (business.get("flowCardId") != null && !business.get("flowCardId").equals("")) {
                        deliver.setFlowCardId(business.get("flowCardId").toString());
                    } else {
                        deliverRecord.setDeliverType(0);
                    }
                }
                deliver.setDeliverPrice(Integer.parseInt(business.get("newUnitPrice").toString()));
                deliver.setDeliverNum((Integer) business.get("completionNum"));
                double area = Double.parseDouble(business.get("productArea").toString()) / Integer.parseInt(business.get("num").toString()) * deliver.getDeliverNum();
                double weight = Double.parseDouble(business.get("productWeight").toString()) / Integer.parseInt(business.get("num").toString()) * deliver.getDeliverNum();
                deliver.setDeliverArea(MapUtil.round(area, 3));
                deliver.setDeliverWeight(MapUtil.round(weight, 3));
                double money = (
                        Double.parseDouble(business.get("productAmount").toString())
                                / Integer.parseInt(business.get("num").toString())
                                - Integer.parseInt(business.get("unitPrice").toString())
                                + Integer.parseInt(business.get("newUnitPrice").toString())
                ) * deliver.getDeliverNum();
                deliver.setDeliverAmount(MapUtil.round(money, 3));
                deliverBusinessList.add(deliver);
                orderId.add(business.get("orderId"));
                orderNo.add(business.get("orderNo"));
                customNo.add(business.get("customNo"));
                customerName.add(business.get("customerName"));
                entryName.add(business.get("entryName"));
                productName.add(business.get("productName"));
            }
            //获取发货信息
            int num = deliverBusinessList.stream().mapToInt(DeliverBusiness::getDeliverNum).sum();
            double amount = deliverBusinessList.stream().mapToDouble(DeliverBusiness::getDeliverAmount).sum();
            double area = deliverBusinessList.stream().mapToDouble(DeliverBusiness::getDeliverArea).sum();
            double weight = deliverBusinessList.stream().mapToDouble(DeliverBusiness::getDeliverWeight).sum();
            deliverRecord.setAllDeliverNum(num);
            deliverRecord.setDeliverArea(MapUtil.round(area, 3));
            deliverRecord.setDeliverWeight(MapUtil.round(weight, 3));
            amount = amount + deliverRecord.getFreight()
                    + deliverRecord.getHandlingCharges()
                    + deliverRecord.getPackingFee()
                    + deliverRecord.getInstallationFee()
                    + deliverRecord.getOtherExpenses()
                    + shelfAmount;
            deliverRecord.setDeliverCost(MapUtil.round(amount, 3));
            deliverRecord.setDeliverStatus(2);
            deliverRecord.setId(deliverRecord.getId());
            deliverRecord.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            deliverRecord.setCreatedAt(new Date());
            deliverRecord.setOrderId(orderId.toString().substring(1, orderId.toString().length() - 1));
            deliverRecord.setOrderNo(orderNo.toString().substring(1, orderNo.toString().length() - 1));
            deliverRecord.setCustomNo(customNo.toString().substring(1, customNo.toString().length() - 1));
            deliverRecord.setCustomerName(customerName.toString().substring(1, customerName.toString().length() - 1));
            deliverRecord.setProductName(productName.toString().substring(1, productName.toString().length() - 1));
            deliverRecord.setEntryName(entryName.toString().substring(1, entryName.toString().length() - 1));
            deliverRecord.setIsDel(0);
            //新增发货记录
            deliverRecordMapper.insert(deliverRecord);
            //新增发货货架
            deliveryShelfService.saveBatch(deliveryShelfList);
            //新增发货明细
            deliverBusinessService.saveBatch(deliverBusinessList);
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 查看明细
     *
     * @param deliverId 发货id
     */
    @Override
    public CommonResult<DeliverRecord> detailsView(String deliverId) {
        //查询发货信息
        DeliverRecord deliverRecord = deliverRecordMapper.selectById(deliverId);
        //查询发货货架
        List<DeliveryShelf> deliveryShelves = deliveryShelfMapper.byDeliverId(deliverId);
        List<DeliverBusiness> deliverBusinessList = new ArrayList<>();
        if (deliverRecord.getDeliverType() == 0) {
            //查询发货明细
            deliverBusinessList = deliverBusinessMapper.queryOrderBusiness(new String[] {deliverId});
        } else if (deliverRecord.getDeliverType() == 1) {
            deliverBusinessList = deliverBusinessMapper.queryPackBusiness(deliverId);
        }
        deliverBusinessList.forEach(item -> {
            item.setNoShelfNum(item.getDeliverNum());
        });
        deliverRecord.setDeliverBusinessList(deliverBusinessList);
        deliverRecord.setDeliveryShelfList(deliveryShelves);
        return CommonResult.success(deliverRecord);
    }

    /**
     * 编辑发货
     *
     * @param deliverRecord 发货对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult updateDeliver(DeliverRecord deliverRecord) {
        try {
            //获取发货货架
            List<DeliveryShelf> deliveryShelfList = deliverRecord.getDeliveryShelfList();
            //获取发货明细
            List<Map<String, Object>> deliverBusiness = deliverRecord.getDeliverBusiness();
            //修改货架明细
            AtomicReference<Double> shelfAmount = new AtomicReference<>(0.000);
            deliveryShelfList.forEach(item -> {
                //判断是否是新增货架
                if (!StringUtils.isNotEmpty(item.getFrameId())) {
                    item.setFrameId(item.getId());
                    item.setDeliverId(deliverRecord.getId());
                }
                if (item.getFramePrice() != null) {
                    shelfAmount.updateAndGet(v -> v + item.getFrameNum() * item.getFramePrice());
                }
            });
            //发货信息
            Set<Object> orderId = new HashSet<>();
            Set<Object> orderNo = new HashSet<>();
            Set<Object> customNo = new HashSet<>();
            Set<Object> entryName = new HashSet<>();
            Set<Object> customerName = new HashSet<>();
            Set<Object> productName = new HashSet<>();
            //遍历发货信息
            List<DeliverBusiness> deliverBusinessList = new ArrayList<>();
            for (Map<String, Object> business : deliverBusiness) {
                DeliverBusiness one = new DeliverBusiness();
                one.setDeliverId(deliverRecord.getId());
                one.setDeliverNo(deliverRecord.getDeliverNo());
                one.setOrderId(business.get("orderId").toString());
                if (deliverRecord.getDeliverType() == 1) {
                    one.setPackId(business.get("packId").toString());
                }
                one.setProductId(business.get("productId").toString());
                one.setDeliverNum(Integer.valueOf(business.get("noShelfNum").toString()));
                double area = Double.parseDouble(business.get("productArea").toString()) / Integer.parseInt(business.get("num").toString()) * one.getDeliverNum();
                double weight = Double.parseDouble(business.get("productWeight").toString()) / Integer.parseInt(business.get("num").toString()) * one.getDeliverNum();
                double money = (
                        Double.parseDouble(business.get("productAmount").toString())
                                / Integer.parseInt(business.get("num").toString())
                                - Integer.parseInt(business.get("unitPrice").toString())
                                + Integer.parseInt(business.get("deliverPrice").toString())
                ) * one.getDeliverNum();
                one.setDeliverArea(MapUtil.round(area, 3));
                one.setDeliverWeight(MapUtil.round(weight, 3));
                one.setDeliverAmount(MapUtil.round(money, 3));
                one.setDeliverPrice(Integer.valueOf(business.get("deliverPrice").toString()));
                deliverBusinessList.add(one);
                orderId.add(business.get("orderId"));
                orderNo.add(business.get("orderNo"));
                customNo.add(business.get("customNo"));
                customerName.add(business.get("customerName"));
                entryName.add(business.get("entryName"));
                productName.add(business.get("productName"));
            }
            int num = deliverBusinessList.stream().mapToInt(DeliverBusiness::getDeliverNum).sum();
            double amount = deliverBusinessList.stream().mapToDouble(DeliverBusiness::getDeliverAmount).sum();
            double area = deliverBusinessList.stream().mapToDouble(DeliverBusiness::getDeliverArea).sum();
            double weight = deliverBusinessList.stream().mapToDouble(DeliverBusiness::getDeliverWeight).sum();
            deliverRecord.setAllDeliverNum(num);
            deliverRecord.setDeliverArea(MapUtil.round(area, 3));
            deliverRecord.setDeliverWeight(MapUtil.round(weight, 3));
            amount = amount + deliverRecord.getFreight()
                    + deliverRecord.getHandlingCharges()
                    + deliverRecord.getPackingFee()
                    + deliverRecord.getInstallationFee()
                    + deliverRecord.getOtherExpenses()
                    + shelfAmount.get();
            deliverRecord.setDeliverCost(MapUtil.round(amount, 3));
            deliverRecord.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            deliverRecord.setUpdatedAt(new Date());
            deliverRecord.setOrderId(orderId.toString().substring(1, orderId.toString().length() - 1));
            deliverRecord.setOrderNo(orderNo.toString().substring(1, orderNo.toString().length() - 1));
            deliverRecord.setCustomNo(customNo.toString().substring(1, customNo.toString().length() - 1));
            deliverRecord.setCustomerName(customerName.toString().substring(1, customerName.toString().length() - 1));
            deliverRecord.setProductName(productName.toString().substring(1, productName.toString().length() - 1));
            deliverRecord.setEntryName(entryName.toString().substring(1, entryName.toString().length() - 1));
            deliverRecord.setIsDel(0);
            //修改发货信息
            deliverRecordMapper.updateById(deliverRecord);
            //删除旧发货明细
            deliverBusinessMapper.delete(Wrappers.<DeliverBusiness>query().lambda().eq(DeliverBusiness::getDeliverId, deliverRecord.getId()));
            //新增新发货明细
            deliverBusinessService.saveBatch(deliverBusinessList);
            //删除旧发货货架
            deliveryShelfMapper.delete(Wrappers.<DeliveryShelf>query().lambda().eq(DeliveryShelf::getDeliverId, deliverRecord.getId()));
            //修改货架明细
            if (deliveryShelfList.size() > 0) {
                deliveryShelfService.saveOrUpdateBatch(deliveryShelfList);
            }
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    /**
     * 审核发货
     *
     * @param deliverId 发货id
     */
    @Override
    public CommonResult reviewDeliver(String deliverId, Integer reviewCode, String reviewPerson, String reviewReason) {
        LambdaUpdateWrapper<DeliverRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(DeliverRecord::getReviewDate, new Date());
        wrapper.set(DeliverRecord::getReviewPerson, reviewPerson);
        wrapper.set(DeliverRecord::getReviewReason, reviewReason);
        wrapper.set(DeliverRecord::getDeliverStatus, reviewCode);
        wrapper.eq(DeliverRecord::getId, deliverId);
        deliverRecordMapper.update(null, wrapper);
        return CommonResult.success(ResultConstants.EXAMINE_SUCCESS);
    }

    /**
     * 消审
     *
     * @param deliverId 发货id
     */
    @Override
    public CommonResult cancelReview(String deliverId) {
        LambdaUpdateWrapper<DeliverRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(DeliverRecord::getReviewDate, new Date());
        wrapper.set(DeliverRecord::getReviewPerson, null);
        wrapper.set(DeliverRecord::getReviewReason, null);
        wrapper.set(DeliverRecord::getDeliverStatus, 2);
        wrapper.eq(DeliverRecord::getId, deliverId);
        deliverRecordMapper.update(null, wrapper);
        return CommonResult.success("消审成功!");
    }

    /**
     * 删除发货
     *
     * @param deliverIds 发货ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult delDeliver(String deliverIds) {
        try {
            String[] split = deliverIds.split(",");
            //删除发货信息
            deliverRecordMapper.delete(Wrappers.<DeliverRecord>query().lambda().in(DeliverRecord::getId, split));
            //删除发货货架
            deliveryShelfMapper.delete(Wrappers.<DeliveryShelf>query().lambda().in(DeliveryShelf::getDeliverId, split));
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 回执信息查询
     *
     * @param deliverId 发货id
     */
    @Override
    public CommonResult<List<DeliverReceipt>> queryReceiptData(String deliverId) {
        return CommonResult.success(deliverReceiptMapper.selectList(Wrappers.<DeliverReceipt>query().lambda().eq(DeliverReceipt::getDeliverId, deliverId)));
    }

    /**
     * 回执文件查询
     *
     * @param deliverReceiptId 发货回执id
     */
    @Override
    public CommonResult<List<DeliverReceiptFile>> queryReceipt(String deliverReceiptId) {
        return CommonResult.success(deliverReceiptFileMapper.selectList(Wrappers.<DeliverReceiptFile>query().lambda().eq(DeliverReceiptFile::getDeliverReceiptId, deliverReceiptId)));
    }

    /**
     * 回执货架查询
     *
     * @param deliverReceiptId 发货回执id
     */
    @Override
    public CommonResult<List<ShelfRecord>> queryDeliverShelf(String deliverReceiptId) {
        return CommonResult.success(shelfRecordMapper.queryByReceiptId(deliverReceiptId));
    }

    /**
     * 回执文件上传
     *
     * @param deliverId 发货id
     * @param deliverReceiptId 发货回执id
     * @param file      文件
     */
    @Override
    public CommonResult uploadReceipt(String deliverId, String deliverReceiptId, MultipartFile file) {
        try {
            String uploadPath = RuoYiConfig.getReceiptUploadPath();
            String upload = FileUploadUtils.upload(uploadPath, file);
            DeliverReceiptFile deliverReceiptFile = new DeliverReceiptFile();
            deliverReceiptFile.setDeliverId(deliverId);
            deliverReceiptFile.setDeliverReceiptId(deliverReceiptId);
            deliverReceiptFile.setFileName(file.getOriginalFilename());
            deliverReceiptFile.setCreatedAt(new Date());
            deliverReceiptFile.setFileSize(FileUtils.byteCountToDisplaySize(file.getSize(), 1024) + "KB");
            String y = uploadPath.substring(0, uploadPath.lastIndexOf("/")) + upload.substring(8);
            deliverReceiptFile.setFileAddress(y);
            deliverReceiptFileMapper.insert(deliverReceiptFile);
            return new CommonResult(HttpStatus.SUCCESS, ResultConstants.SAVE_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResult(HttpStatus.ERROR, ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 回执文件下载
     *
     * @param id       文件id
     * @param response 请求头
     */
    @Override
    public void downloadReceiptFile(String id, HttpServletResponse response) {
        try {
            DeliverReceiptFile deliverReceiptFile = deliverReceiptFileMapper.selectById(id);
            String fileAddress = deliverReceiptFile.getFileAddress();
            String fileName = deliverReceiptFile.getFileName();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, fileName);
            FileUtils.writeBytes(fileAddress, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id 文件id
     */
    @Override
    public CommonResult delReceiptFile(String id) {
        DeliverReceiptFile deliverReceiptFile = deliverReceiptFileMapper.selectById(id);
        FileUtils.deleteFile(deliverReceiptFile.getFileAddress());
        deliverReceiptFileMapper.deleteById(id);
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 新增回执
     *
     * @param deliverReceipt 回执信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult receiptShelf(DeliverReceipt deliverReceipt) {
        try {
            List<DeliveryShelf> deliveryShelfList = deliverReceipt.getDeliveryShelfList();
            for (DeliveryShelf deliveryShelf : deliveryShelfList) {
                ShelfRecord shelfRecord = new ShelfRecord();
                shelfRecord.setDeliverId(deliverReceipt.getDeliverId());
                shelfRecord.setReceiptId(deliverReceipt.getId());
                shelfRecord.setPerson(SecurityUtils.getLoginUser().getUser().getNickName());
                shelfRecord.setShipmentNo(deliverReceipt.getDeliverNo());
                shelfRecord.setNum(deliveryShelf.getOperationNum());
                shelfRecord.setRecordType("入库");
                shelfRecord.setOperationDate(new Date());
                shelfRecord.setShelfId(deliveryShelf.getFrameId());
                shelfRecordMapper.insert(shelfRecord);
                deliveryShelf.setReceiptNum(deliveryShelf.getOperationNum() + deliveryShelf.getReceiptNum());
            }
            deliverReceiptMapper.insert(deliverReceipt);
            //修改发货记录
            deliverRecordMapper.update(null,
                    Wrappers.<DeliverRecord>update().lambda()
                            .set(DeliverRecord::getReceiptPerson, deliverReceipt.getReceiptPerson())
                            .set(DeliverRecord::getReceiptDate, deliverReceipt.getReceiptDate())
                            .eq(DeliverRecord::getId, deliverReceipt.getDeliverId()));
            if (deliveryShelfList.size() > 0) {
                deliveryShelfService.updateBatchById(deliveryShelfList);
            }
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 导出发货明细
     *
     * @param response
     * @param ids      发货id集合
     */
    @Override
    public void exportDeliverBus(HttpServletResponse response, String ids) {
        List<DeliverBusiness> deliverBusinessList = null;
        if (StringUtils.isNotEmpty(ids)) {
            deliverBusinessList = deliverBusinessMapper.queryOrderBusiness(ids.split(","));
        }else {
            deliverBusinessList = deliverBusinessMapper.queryOrderBusiness(null);
        }
        ExcelUtil<DeliverBusiness> util = new ExcelUtil<DeliverBusiness>(DeliverBusiness.class);
        util.exportExcel(response, deliverBusinessList, "参数数据");
    }

    /**
     * 导出发货单
     *
     * @param response
     * @param ids      发货id集合
     */
    @Override
    public void exportDeliver(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<DeliverRecord> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(DeliverRecord::getId, split);
        }
        wrapper.orderByDesc(DeliverRecord::getDeliverNo);
        List<DeliverRecord> deliverRecords = deliverRecordMapper.selectList(wrapper);
        ExcelUtil<DeliverRecord> util = new ExcelUtil<DeliverRecord>(DeliverRecord.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }

    /**
     * 订单管理-查询发货明细
     *
     * @param id 订单id
     */
    @Override
    public CommonResult<List<DeliverBusiness>> queryDeliverBus(String id) {
        return CommonResult.success(deliverBusinessMapper.queryDeliverBus(id));
    }

    /**
     * 订单管理-查询发货进度
     *
     * @param id 订单id
     */
    @Override
    public CommonResult<List<DeliverRecord>> queryDeliverProgress(String id) {
        LambdaQueryWrapper<DeliverRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(DeliverRecord::getOrderId, id);
        List<DeliverRecord> deliverRecords = deliverRecordMapper.selectList(wrapper);
        return CommonResult.success(deliverRecords);
    }

}
