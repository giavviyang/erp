package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.Constants;
import com.erp.common.constant.HttpStatus;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.MapUtil;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.file.FileUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.sales.*;
import com.erp.floor.mapper.system.SysAdditionalMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.floor.pojo.system.domain.SysAdditional;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.sales.service.OrderProductService;
import com.erp.sales.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-21 14:03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderRecord> implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderProductMapper orderProductMapper;
    @Resource
    private OrderReviewMapper orderReviewMapper;
    @Resource
    private SysAdditionalMapper sysAdditionalMapper;
    @Resource
    private AdditionalMapper additionalMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private OrderProductService orderProductService;
    @Resource
    private OrderEnclosureMapper orderEnclosureMapper;
    @Resource
    private FlowCardMapper flowCardMapper;
    @Resource
    private ShelfDivisionBusinessMapper shelfDivisionBusinessMapper;

    /**
     * 自动生成编号
     *
     * @return 编号
     */
    @Override
    public String productionNumber() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(OrderRecord::getOrderNo, format1);
        wrapper.like(OrderRecord::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(OrderRecord::getCreatedAt).last("limit 1");
        OrderRecord orderRecord = orderMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("1");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (orderRecord != null) {
            String orderNo = orderRecord.getOrderNo();
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
     * 校验编号是否重复
     *
     * @param orderRecord
     * @return
     */
    @Override
    public String checkNumber(OrderRecord orderRecord) {
        if (orderRecord.getIsCache() == 1) {
            //删除旧缓存  订单
            LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderRecord::getOrderNo, orderRecord.getOrderNo());
            orderMapper.delete(wrapper);
        } else {
            LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderRecord::getOrderNo, orderRecord.getOrderNo());
            if (orderMapper.selectOne(wrapper) != null) {
                return Constants.FAIL;
            }
        }
        return Constants.SUCCESS;
    }

    /**
     * 查询vo
     *
     * @param orderRecordVo 订单查询vo
     * @return
     */
    @Override
    public CommonResult<List<OrderRecord>> queryAllOrder(OrderRecordVo orderRecordVo) {
        if (orderRecordVo.getPageNum() != null && orderRecordVo.getPageSize() != null && orderRecordVo.getIsDel() != null) {
            LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderRecord::getIsDel, orderRecordVo.getIsDel());
            if (orderRecordVo.getIsCache() != null) {
                wrapper.eq(OrderRecord::getIsCache, orderRecordVo.getIsCache());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getId())) {
                wrapper.eq(OrderRecord::getId, orderRecordVo.getId());
            }
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
            if (orderRecordVo.getCollectionStatus() != null) {
                wrapper.eq(OrderRecord::getCollectionStatus, orderRecordVo.getCollectionStatus());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getOrderType())) {
                wrapper.eq(OrderRecord::getOrderType, orderRecordVo.getOrderType());
            }
            if (orderRecordVo.getPackagingStatus() != null) {
                wrapper.eq(OrderRecord::getPackagingStatus, orderRecordVo.getPackagingStatus());
            }
            if (orderRecordVo.getProductionStatus() != null) {
                wrapper.eq(OrderRecord::getProductionStatus, orderRecordVo.getProductionStatus());
            }
            if (orderRecordVo.getRackSplittingStatus() != null) {
                wrapper.eq(OrderRecord::getRackSplittingStatus, orderRecordVo.getRackSplittingStatus());
            }
            if (orderRecordVo.getShipmentStatus() != null) {
                wrapper.eq(OrderRecord::getShipmentStatus, orderRecordVo.getShipmentStatus());
            }
            if (StringUtils.isNotEmpty(orderRecordVo.getPreparationDateStart()) && StringUtils.isNotEmpty(orderRecordVo.getPreparationDateEnd())) {
                wrapper.between(OrderRecord::getPreparationDate, orderRecordVo.getPreparationDateStart(), orderRecordVo.getPreparationDateEnd());
            }
            wrapper.orderByDesc(OrderRecord::getCreatedAt);
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
     * 新增订单
     *
     * @param orderRecord 订单对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public AjaxResult addOrder(OrderRecord orderRecord) {
        try {
            MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
            //获取产品信息
            List<Map<String, Object>> productList = orderRecord.getProductList();
            //获取附加项别名集合
            List<String> additionalIds = orderRecord.getAdditionalIds();
            //新增订单信息
            String orderId = UUID.randomUUID().toString();
            String string = additionalIds.toString();
            orderRecord.setId(orderId);
            orderRecord.setIsCache(0);
            orderRecord.setCreatedAt(new Date());
            orderRecord.setOrderAdditional(string.substring(1, string.length() - 1));
            int insert = orderMapper.insert(orderRecord);
            if (insert > 0) {
                new Thread() {
                    @Override
                    public void run() {
                        //新增一条审核信息
                        OrderReview orderReview = new OrderReview(null, orderId, 2, 2, 2, null, null, null);
                        orderReviewMapper.insert(orderReview);
                    }
                }.start();
                //查询附加项集合
                List<SysAdditional> sysAdditionals = new ArrayList<>();
                if (additionalIds.size() > 0) {
                    LambdaQueryWrapper<SysAdditional> wrapper = new LambdaQueryWrapper<>();
                    wrapper.in(SysAdditional::getId, additionalIds);
                    sysAdditionals = sysAdditionalMapper.selectList(wrapper);
                }
                //处理产品数据
                for (int i = 0; i < productList.size(); i++) {
                    Map<String, Object> map = productList.get(i);
                    if (map.size() <= 0 || map.get("productId") == null) {
                        continue;
                    }
                    //获取产品的基本信息
                    OrderProduct orderProduct = MapUtil.mapToEntity(map, OrderProduct.class);
                    String ID = UUID.randomUUID().toString();
                    orderProduct.setId(ID);
                    orderProduct.setOrderId(orderId);
                    orderProduct.setOrderNo(orderRecord.getOrderNo());
                    orderProduct.setSort(i);
                    int insert1 = orderProductMapper.insert(orderProduct);
                    List<SysAdditional> finalSysAdditionals = sysAdditionals;
                    new Thread() {
                        public void run() {
                            if (insert1 > 0) {
                                //新增产品和工艺流程对应关系
                                orderProductService.addProductProcess(orderProduct, 1);
                                if (finalSysAdditionals.size() > 0) {
                                    //新增附加项记录
                                    for (SysAdditional sysAdditional : finalSysAdditionals) {
                                        if (map.get(sysAdditional.getAdditionalAlias() + "_unitPrice") != null && !map.get(sysAdditional.getAdditionalAlias() + "_unitPrice").equals("")) {
                                            Additional additional = new Additional();
                                            additional.setProductId(ID);
                                            additional.setAdditionalName(sysAdditional.getAdditionalName());
                                            additional.setAdditionalAlias(sysAdditional.getAdditionalAlias());
                                            additional.setUnitPrice(Double.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_unitPrice").toString()));
                                            if (map.get(sysAdditional.getAdditionalAlias() + "_allAmount") != null && !map.get(sysAdditional.getAdditionalAlias() + "_allAmount").equals("")) {
                                                additional.setAllAmount(Double.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_allAmount").toString()));
                                            }
                                            if (map.get(sysAdditional.getAdditionalAlias() + "_oneNumber") != null && !map.get(sysAdditional.getAdditionalAlias() + "_oneNumber").equals("")) {
                                                additional.setOneNumber(Integer.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_oneNumber").toString()));
                                            }
                                            additionalMapper.insert(additional);
                                        }
                                    }
                                }
                            }
                        }
                    }.start();
                }
                return AjaxResult.success(ResultConstants.SAVE_SUCCESS);
            } else {
                return AjaxResult.error(ResultConstants.SAVE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 新增订单缓存
     *
     * @param orderRecord 订单对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public AjaxResult addOrderCache(OrderRecord orderRecord) {
        try {
            MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
            //获取产品信息
            List<Map<String, Object>> productList = orderRecord.getProductList();
            //获取附加项别名集合
            List<String> additionalIds = orderRecord.getAdditionalIds();
            //新增订单信息
            String orderId = UUID.randomUUID().toString();
            String string = additionalIds.toString();
            orderRecord.setId(orderId);
            orderRecord.setIsCache(1);
            orderRecord.setOrderAdditional(string.substring(1, string.length() - 1));
            int insert = orderMapper.insert(orderRecord);
            if (insert > 0) {
                new Thread() {
                    @Override
                    public void run() {
                        //新增一条审核信息
                        OrderReview orderReview = new OrderReview(null, orderId, 2, 2, 2, null, null, null);
                        orderReviewMapper.insert(orderReview);
                    }
                }.start();
                //查询附加项集合
                List<SysAdditional> sysAdditionals = new ArrayList<>();
                if (additionalIds.size() > 0) {
                    LambdaQueryWrapper<SysAdditional> wrapper = new LambdaQueryWrapper<>();
                    wrapper.in(SysAdditional::getId, additionalIds);
                    sysAdditionals = sysAdditionalMapper.selectList(wrapper);
                }
                //处理产品数据
                for (int i = 0; i < productList.size(); i++) {
                    Map<String, Object> map = productList.get(i);
                    if (map.size() <= 0 || map.get("productId") == null) {
                        continue;
                    }
                    //获取产品的基本信息
                    OrderProduct orderProduct = MapUtil.mapToEntity(map, OrderProduct.class);
                    String ID = UUID.randomUUID().toString();
                    orderProduct.setId(ID);
                    orderProduct.setOrderId(orderId);
                    orderProduct.setOrderNo(orderRecord.getOrderNo());
                    orderProduct.setSort(i);
                    int insert1 = orderProductMapper.insert(orderProduct);
                    List<SysAdditional> finalSysAdditionals = sysAdditionals;
                    new Thread() {
                        public void run() {
                            if (insert1 > 0) {
                                //新增产品和工艺流程对应关系
                                orderProductService.addProductProcess(orderProduct, 1);
                                if (finalSysAdditionals.size() > 0) {
                                    //新增附加项记录
                                    for (SysAdditional sysAdditional : finalSysAdditionals) {
                                        if (map.get(sysAdditional.getAdditionalAlias() + "_unitPrice") != null && !map.get(sysAdditional.getAdditionalAlias() + "_unitPrice").equals("")) {
                                            Additional additional = new Additional();
                                            additional.setProductId(ID);
                                            additional.setAdditionalName(sysAdditional.getAdditionalName());
                                            additional.setAdditionalAlias(sysAdditional.getAdditionalAlias());
                                            additional.setUnitPrice(Double.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_unitPrice").toString()));
                                            if (map.get(sysAdditional.getAdditionalAlias() + "_allAmount") != null && !map.get(sysAdditional.getAdditionalAlias() + "_allAmount").equals("")) {
                                                additional.setAllAmount(Double.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_allAmount").toString()));
                                            }
                                            if (map.get(sysAdditional.getAdditionalAlias() + "_oneNumber") != null && !map.get(sysAdditional.getAdditionalAlias() + "_oneNumber").equals("")) {
                                                additional.setOneNumber(Integer.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_oneNumber").toString()));
                                            }
                                            additionalMapper.insert(additional);
                                        }
                                    }
                                }
                            }
                        }
                    }.start();
                }
                return AjaxResult.success(ResultConstants.SAVE_SUCCESS);
            } else {
                return AjaxResult.error(ResultConstants.SAVE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 查询产品明细
     *
     * @param orderProduct 订单产品类
     * @return
     */
    @Override
    public CommonResult<List<OrderProduct>> queryProduct(OrderProduct orderProduct) {
        List<OrderProduct> orderProducts = null;
        try {
            //查询指定订单的产品
            LambdaQueryWrapper<OrderProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderProduct::getOrderId, orderProduct.getOrderId());
            if (StringUtils.isNotEmpty(orderProduct.getProductName()))
                wrapper.like(OrderProduct::getProductName, orderProduct.getProductName());
            if (StringUtils.isNotEmpty(orderProduct.getPosition()))
                wrapper.like(OrderProduct::getPosition, orderProduct.getPosition());
            if (StringUtils.isNotNull(orderProduct.getWideHead()) && orderProduct.getWideHead() != 0)
                wrapper.eq(OrderProduct::getWideHead, orderProduct.getWideHead());
            if (StringUtils.isNotNull(orderProduct.getHighHead()) && orderProduct.getHighHead() != 0)
                wrapper.eq(OrderProduct::getHighHead, orderProduct.getHighHead());
            if (StringUtils.isNotEmpty(orderProduct.getRequirement()))
                wrapper.like(OrderProduct::getRequirement, orderProduct.getRequirement());
            if (StringUtils.isNotEmpty(orderProduct.getRemarks()))
                wrapper.like(OrderProduct::getRemarks, orderProduct.getRemarks());
            wrapper.orderByAsc(OrderProduct::getSort);
            orderProducts = orderProductMapper.selectList(wrapper);
            //查询每个产品下面的附加项   并且分装产品
            for (OrderProduct product : orderProducts) {
                //查询产品对应附加项
                LambdaQueryWrapper<Additional> additionalWrapper = new LambdaQueryWrapper<>();
                additionalWrapper.eq(Additional::getProductId, product.getId());
                List<Additional> additionals = additionalMapper.selectList(additionalWrapper);
                if (additionals != null && additionals.size() > 0) {
                    Map<String, Object> map = new HashMap<>();
                    for (Additional additional : additionals) {
                        //将附加项信息添加到产品数据map中
                        entityToMap(additional, additional.getAdditionalAlias(), map);
                    }
                    product.setAdditionalMap(map);
                }
            }
        } catch (Exception e) {
            return new CommonResult<>(HttpStatus.ERROR, ResultConstants.QUERY_ERROR);
        }
        return new CommonResult<>(HttpStatus.SUCCESS, ResultConstants.QUERY_SUCCESS, orderProducts);
    }

    /**
     * 校验订单状态
     *
     * @param orderId
     * @return
     */
    @Override
    public AjaxResult checkOrder(String orderId) {
        //查询订单数据
        OrderRecord orderRecord = orderMapper.selectById(orderId);
        LambdaQueryWrapper<OrderReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderReview::getOrderId, orderRecord.getId());
        OrderReview orderReview = orderReviewMapper.selectOne(wrapper);
//        if (orderRecord.getRackSplittingStatus() != 0) {
//            return AjaxResult.error("订单已分架，无法执行此操作！");
//        }
        if (orderReview != null) {
            if (orderReview.getOrderResult() != 2) {
                return AjaxResult.error("订单已审核，无法执行此操作！");
            }
        }
        return AjaxResult.success();
    }

    /**
     * 修改订单
     *
     * @param orderRecord 订单对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult updateOrder(OrderRecord orderRecord) {
        //获取产品信息
        List<Map<String, Object>> productList = orderRecord.getProductList();
        //获取附加项别名集合
        List<String> additionalIds = orderRecord.getAdditionalIds();
        String string = additionalIds.toString();
        orderRecord.setOrderAdditional(string.substring(1, string.length() - 1));
        //编辑订单信息
        int update = orderMapper.updateById(orderRecord);
        if (update > 0) {
            //删除旧产品所有信息
            List<String> orderId = new ArrayList<>();
            orderId.add(orderRecord.getId());
            orderProductService.delProduct(orderId);
            //查询附加项集合
            List<SysAdditional> sysAdditionals = new ArrayList<>();
            if (additionalIds.size() > 0) {
                LambdaQueryWrapper<SysAdditional> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(SysAdditional::getId, additionalIds);
                sysAdditionals = sysAdditionalMapper.selectList(wrapper);
            }
            //处理产品数据
            for (int i = 0; i < productList.size(); i++) {
                Map<String, Object> map = productList.get(i);
                if (map.size() <= 0 || map.get("productId") == null) {
                    continue;
                }
                //获取产品的基本信息
                OrderProduct orderProduct = MapUtil.mapToEntity(map, OrderProduct.class);
                //修改产品信息
                int updateProduct = 0;
                String id = "";
                if (orderProduct.getId() != null && !orderProduct.getId().equals("")) {
                    //修改旧产品
                    id = orderProduct.getId();
                    updateProduct = orderProductMapper.updateById(orderProduct);
                } else {
                    //新增新产品
                    id = UUID.randomUUID().toString();
                    orderProduct.setId(id);
                    orderProduct.setOrderId(orderRecord.getId());
                    orderProduct.setOrderNo(orderRecord.getOrderNo());
                    orderProduct.setSort(i);
                    updateProduct = orderProductMapper.insert(orderProduct);
                }
                int finalUpdateProduct = updateProduct;
                List<SysAdditional> finalSysAdditionals = sysAdditionals;
                String finalId = id;
                new Thread() {
                    @Override
                    public void run() {
                        //新增产品工艺流程对应关系
                        orderProductService.addProductProcess(orderProduct, 0);
                        if (finalUpdateProduct > 0 && finalSysAdditionals.size() > 0) {
                            //先将旧数据删除
                            LambdaQueryWrapper<Additional> additionalWrapper = new LambdaQueryWrapper<>();
                            additionalWrapper.eq(Additional::getProductId, finalId);
                            additionalMapper.delete(additionalWrapper);
                            //新增附加项记录
                            for (SysAdditional sysAdditional : finalSysAdditionals) {
                                if (map.get(sysAdditional.getAdditionalAlias() + "_unitPrice") != null && !map.get(sysAdditional.getAdditionalAlias() + "_unitPrice").equals("")) {
                                    Additional additional = new Additional();
                                    additional.setProductId(finalId);
                                    additional.setAdditionalName(sysAdditional.getAdditionalName());
                                    additional.setAdditionalAlias(sysAdditional.getAdditionalAlias());
                                    additional.setUnitPrice(Double.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_unitPrice").toString()));
                                    if (map.get(sysAdditional.getAdditionalAlias() + "_allAmount") != null && !map.get(sysAdditional.getAdditionalAlias() + "_allAmount").equals("")) {
                                        additional.setAllAmount(Double.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_allAmount").toString()));
                                    }
                                    if (map.get(sysAdditional.getAdditionalAlias() + "_oneNumber") != null && !map.get(sysAdditional.getAdditionalAlias() + "_oneNumber").equals("")) {
                                        additional.setOneNumber(Integer.valueOf(map.get(sysAdditional.getAdditionalAlias() + "_oneNumber").toString()));
                                    }
                                    additionalMapper.insert(additional);
                                }
                            }
                        }
                    }
                }.start();
            }
            return AjaxResult.success(ResultConstants.UPD_SUCCESS);
        } else {
            return AjaxResult.error(ResultConstants.UPD_ERROR);
        }
    }

    /**
     * 软删除
     *
     * @param orderId 订单对象
     * @return
     */
    @Override
    public AjaxResult updateOrderDel(String orderId) {
        UpdateWrapper<OrderRecord> wrapper = new UpdateWrapper<OrderRecord>();
        wrapper.eq("id", orderId);
        wrapper.set("is_del", 1);
        wrapper.set("deleted_date", new Date(System.currentTimeMillis()));
        int update = orderMapper.update(null, wrapper);
        if (update > 0) {
            //删除流程卡
            delFlow(orderId);
            return AjaxResult.success(ResultConstants.DEL_SUCCESS);
        }
        return AjaxResult.error(ResultConstants.DEL_ERROR);
    }

    /**
     * 尺寸审核
     *
     * @param orderId    订单id
     * @param fieldName  审核字段
     * @param fieldValue 审核结果
     * @return
     */
    @Override
    public AjaxResult auditSize(String orderId, String fieldName, Integer fieldValue) {
        UpdateWrapper<OrderReview> wrapper = new UpdateWrapper<>();
        wrapper.eq("order_id", orderId);
        wrapper.set(fieldName, fieldValue);
        int update = 0;
        try {
            update = orderReviewMapper.update(null, wrapper);
        } catch (Exception e) {
            return AjaxResult.error(ResultConstants.EXAMINE_ERROR);
        }
        if (update > 0) {
            return AjaxResult.success(ResultConstants.EXAMINE_SUCCESS);
        }
        return AjaxResult.error(ResultConstants.EXAMINE_ERROR);
    }

    /**
     * 订单审核
     *
     * @param orderId       订单id
     * @param orderResult   审核结果
     * @param reviewedBy    审核人
     * @param failureReason 审核不通过原因
     */
    @Override
    public AjaxResult auditOrder(String orderId, Integer orderResult, String reviewedBy, String failureReason) {
        UpdateWrapper<OrderReview> wrapper = new UpdateWrapper<>();
        wrapper.eq("order_id", orderId)
                .set("order_result", orderResult)
                .set("reviewed_by", reviewedBy)
                .set("audit_time", new Date())
                .set("failure_reason", failureReason);
        int update = 0;
        try {
            update = orderReviewMapper.update(null, wrapper);
        } catch (Exception e) {
            return AjaxResult.error("订单审核失败!");
        }
        if (update > 0) {
            return AjaxResult.success("订单审核成功！");
        }
        return AjaxResult.error("订单审核失败！");
    }

    /**
     * 订单消审
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public AjaxResult undoAudit(String orderId) {
        UpdateWrapper<OrderReview> wrapper = new UpdateWrapper<>();
        wrapper.eq("order_id", orderId)
                .set("dimensions_result", 2)
                .set("workmanship_result", 2)
                .set("order_result", 2)
                .set("reviewed_by", null)
                .set("audit_time", null)
                .set("failure_reason", null);
        if (orderReviewMapper.update(null, wrapper) <= 0) {
            OrderReview orderReview = new OrderReview(UUID.randomUUID().toString(), orderId, 2, 2, 2, null, null, null);
            orderReviewMapper.insert(orderReview);
        }
        return AjaxResult.success("订单消审成功！");
    }

    /**
     * 删除订单
     *
     * @param orderIds 订单id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delOrder(String orderIds) {
        //删除对应订单id集合
        String[] split = null;
        if (StringUtils.isNotEmpty(orderIds)) {
            split = orderIds.split(",");
        }else {
            List<OrderRecord> orderRecords = orderMapper.selectList(Wrappers.<OrderRecord>query().lambda().eq(OrderRecord::getIsDel, 1));
            split = orderRecords.stream().collect(Collectors.groupingBy(OrderRecord::getId)).keySet().toArray(new String[0]);
        }
        LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderRecord::getId, split);
        orderMapper.delete(wrapper);
        //删除产品所有信息
        List<OrderEnclosure> orderEnclosures = orderEnclosureMapper.selectList(Wrappers.<OrderEnclosure>query().lambda().in(OrderEnclosure::getOrderId, split));
        for (OrderEnclosure orderEnclosure : orderEnclosures) {
            FileUtils.deleteFile(orderEnclosure.getFileAddress());
        }
        orderEnclosureMapper.delete(Wrappers.<OrderEnclosure>query().lambda().in(OrderEnclosure::getOrderId, split));
        orderReviewMapper.delete(Wrappers.<OrderReview>update().lambda().in(OrderReview::getOrderId, split));
        orderProductService.delProduct(Arrays.asList(split));
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 导出任务单
     *
     * @param orderId 订单id
     */
    @Override
    public ResponseEntity<byte[]> exportTask(String orderId) throws IOException {
        //查询订单信息
        OrderRecord orderRecord = orderMapper.selectById(orderId);
        //创建一个Excel
        XSSFWorkbook book = new XSSFWorkbook();
        //创建一个Sheet页
        XSSFSheet sheet = book.createSheet();
        XSSFCellStyle reportNameStyle = ExcelUtil.style(book, sheet, 20, 40, "宋体", 15, true, true);
        // 行号
        int rowNum = 0;
        // 创建第一页的第一行，索引从0开始
        XSSFRow row0 = sheet.createRow(rowNum++);
        //创建第一行第一个
        XSSFCell c00 = row0.createCell(0);
        //赋值
        c00.setCellValue("任务单");
        //设置样式
        c00.setCellStyle(reportNameStyle);
        //设置跨行,前两个参数是需要合并的开始行数和结束行数,后两个是开始列数和结束列数
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        //声明一个表头集合
        List<String[]> headList = new ArrayList<>();
        //表头文字
        String[] text = new String[]{"客户名称", "项目名称", "订单类型"};
        String[] text1 = new String[]{"订单编号", "自定义编号", "交货日期"};
        String[] text2 = new String[]{"收货人", "联系方式", "收货地址"};
        headList.add(text);
        headList.add(text1);
        headList.add(text2);
        //循环添加  展示字段
        for (int i = 0; i < headList.size(); i++) {
            String[] strings = headList.get(i);
            //创建行
            XSSFRow row = sheet.createRow(rowNum++);
            //为每行添加展示字段
            for (int i1 = 0; i1 < strings.length; i1++) {
                XSSFCell cell = row.createCell((i1 * 4) - i1);
                XSSFCell cell1 = row.createCell((i1 + 1) * 3 - 1);
                cell.setCellStyle(reportNameStyle);
                cell1.setCellStyle(reportNameStyle);
                cell.setCellValue(strings[i1]);
            }
            //批量合并单元格
            for (int j = 1; j < 8; j += 3) {
                CellRangeAddress cellAddresses = new CellRangeAddress(i + 1, i + 1, j, j + 1);
                sheet.addMergedRegion(cellAddresses);
            }
            //为每个展示字段   添加数据
            if (i == 0) {
                XSSFCell cell = row.createCell(1);
                cell.setCellStyle(reportNameStyle);
                cell.setCellValue(orderRecord.getCustomerName());
                XSSFCell cell1 = row.createCell(4);
                cell1.setCellStyle(reportNameStyle);
                cell1.setCellValue(orderRecord.getEntryName());
                XSSFCell cell2 = row.createCell(7);
                cell2.setCellStyle(reportNameStyle);
                cell2.setCellValue(orderRecord.getOrderType());
            } else if (i == 1) {
                XSSFCell cell = row.createCell(1);
                cell.setCellStyle(reportNameStyle);
                cell.setCellValue(orderRecord.getOrderNo());
                XSSFCell cell1 = row.createCell(4);
                cell1.setCellStyle(reportNameStyle);
                cell1.setCellValue(orderRecord.getCustomNo());
                XSSFCell cell2 = row.createCell(7);
                cell2.setCellStyle(reportNameStyle);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (orderRecord.getReceiptDate() != null) {
                    cell2.setCellValue(format.format(orderRecord.getReceiptDate()));
                }
            } else {
                XSSFCell cell = row.createCell(1);
                cell.setCellStyle(reportNameStyle);
                cell.setCellValue(orderRecord.getContacts());
                XSSFCell cell1 = row.createCell(4);
                cell1.setCellStyle(reportNameStyle);
                cell1.setCellValue(orderRecord.getContactNumber());
                XSSFCell cell2 = row.createCell(7);
                cell2.setCellStyle(reportNameStyle);
                cell2.setCellValue(orderRecord.getReceiptAddress());
            }
        }
        //添加产品列表
        LambdaQueryWrapper<OrderProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderProduct::getOrderId, orderId);
        List<OrderProduct> orderProducts = orderProductMapper.selectList(wrapper);
        String[] product = new String[]{"序号", "产品名称", "位置", "宽*高", "数量", "面积", "重量", "加工要求", "备注"};
        XSSFRow productRow = sheet.createRow(rowNum++);
        for (int i = 0; i < product.length; i++) {
            XSSFCell cell = productRow.createCell(i);
            cell.setCellStyle(reportNameStyle);
            cell.setCellValue(product[i]);
        }
        XSSFCellStyle style = ExcelUtil.style(book, sheet, 20, 30, "宋体", 12, false, true);
        for (int j = 0; j < orderProducts.size(); j++) {
            OrderProduct orderProduct = orderProducts.get(j);
            Row temp = sheet.createRow((rowNum++));
            for (int i = 0; i < 9; i++) {
                Cell cell = temp.createCell(i);
                cell.setCellStyle(style);
                switch (i) {
                    case 0:
                        cell.setCellValue(j);
                        break;
                    case 1:
                        cell.setCellValue(orderProduct.getProductName());
                        break;
                    case 2:
                        cell.setCellValue(orderProduct.getPosition());
                        break;
                    case 3:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(orderProduct.getWideHead());
                        if (StringUtils.isNotNull(orderProduct.getWideLittleHead()))
                            stringBuilder.append("/").append(orderProduct.getWideLittleHead());
                        stringBuilder.append("*").append(orderProduct.getHighHead());
                        if (StringUtils.isNotNull(orderProduct.getHighLittleHead()))
                            stringBuilder.append("/").append(orderProduct.getHighLittleHead());
                        cell.setCellValue(stringBuilder.toString());
                        break;
                    case 4:
                        cell.setCellValue(orderProduct.getNum());
                        break;
                    case 5:
                        cell.setCellValue(orderProduct.getProductArea());
                        break;
                    case 6:
                        cell.setCellValue(orderProduct.getProductWeight());
                        break;
                    case 7:
                        cell.setCellValue(orderProduct.getRequirement());
                        break;
                    case 8:
                        cell.setCellValue(orderProduct.getRemarks());
                        break;
                }
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        book.write(outputStream);
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的方式打开文件
        headers.setContentDispositionFormData("attachment", "RWD.xlsx");
        //定义以流的形式下载返回文件数据
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return new ResponseEntity<>(outputStream.toByteArray(), headers, org.springframework.http.HttpStatus.OK);
    }

    /**
     * 导出订单
     *
     * @param response
     * @param ids      订单集合
     */
    @Override
    public void exportOrder(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderRecord::getIsCache,0);
        wrapper.eq(OrderRecord::getIsDel,0);
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(OrderRecord::getId, split);
        }
        wrapper.orderByDesc(OrderRecord::getPreparationDate);
        List<OrderRecord> orderRecords = orderMapper.selectList(wrapper);
        ExcelUtil<OrderRecord> util = new ExcelUtil<OrderRecord>(OrderRecord.class);
        util.exportExcel(response, orderRecords, "参数数据");
    }

    /**
     * 订单查询
     *
     * @param orderRecordVo 订单查询vo
     * @return
     */
    @Override
    public CommonResult<List<OrderRecord>> queryFlowOrder(OrderRecordVo orderRecordVo) {
        if (orderRecordVo.getPageNum() != null && orderRecordVo.getPageSize() != null && orderRecordVo.getIsDel() != null) {
            PageHelper.startPage(orderRecordVo.getPageNum(), orderRecordVo.getPageSize());
            List<OrderRecord> orderRecords = orderMapper.queryReviewOrder(orderRecordVo);
            System.out.println("size"+orderRecords.size());
            PageInfo<OrderRecord> pageInfo = new PageInfo<>(orderRecords, orderRecordVo.getPageSize());
            return new CommonResult<>(HttpStatus.SUCCESS, ResultConstants.QUERY_SUCCESS, (int)pageInfo.getTotal(), pageInfo.getList());
        } else {
            return CommonResult.error("请输入页容量和页码");
        }
    }

    /**
     * 还原订单
     *
     * @param orderId 订单id
     */
    @Override
    public AjaxResult reductionOrder(String orderId) {
        UpdateWrapper<OrderRecord> wrapper = new UpdateWrapper<OrderRecord>();
        wrapper.eq("id", orderId);
        wrapper.set("is_del", 0);
        int update = orderMapper.update(null, wrapper);
        if (update > 0) {
            return AjaxResult.success("还原成功！");
        }
        return AjaxResult.error("还原失败！");
    }

    /**
     * 删除流程卡
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delFlow(String ids) {
        try {
            String[] split = ids.split(",");
            //查询订单的生成状态
            List<OrderRecord> orderRecords = orderMapper.selectList(Wrappers.<OrderRecord>query().lambda().in(OrderRecord::getId, split));
            //过滤已经生成的订单
            List<OrderRecord> collect = orderRecords.stream().filter(item -> item.getProductionStatus() != 0).collect(Collectors.toList());
            if (collect.size() > 0) {
                return CommonResult.error("请选择未生产的订单！");
            }
            //订单编号分组
            Set<String> orderNos = orderRecords.stream().collect(Collectors.groupingBy(OrderRecord::getOrderNo)).keySet();
            //获取需要修改的流程卡
            Set<String> flowNos = new HashSet<>();
            for (String orderNo : orderNos) {
                Set<String> set = flowCardMapper.selectList(
                        Wrappers.<FlowCard>query().lambda().like(FlowCard::getOrderNo, orderNo)
                ).stream().collect(Collectors.groupingBy(FlowCard::getFlowCardNo)).keySet();
                flowNos.addAll(set);
            }
            //获取流程卡明细
            Map<String, List<ShelfDivisionBusiness>> flowCards = new HashMap<>();
            if (flowNos.size() > 0) {
                flowCards =  shelfDivisionBusinessMapper.selectList(
                        Wrappers.<ShelfDivisionBusiness>query().lambda().in(ShelfDivisionBusiness::getFlowCardNo, flowNos)
                ).stream().collect(Collectors.groupingBy(ShelfDivisionBusiness::getFlowCardNo));
                shelfDivisionBusinessMapper.delete(Wrappers.<ShelfDivisionBusiness>query().lambda().in(ShelfDivisionBusiness::getFlowCardNo, flowNos));
                //删除旧流程卡
                flowCardMapper.delete(Wrappers.<FlowCard>query().lambda().in(FlowCard::getFlowCardNo, flowNos));
            }
            //修改订单分架状态
            orderMapper.update(null, Wrappers.<OrderRecord>update().lambda().set(OrderRecord::getRackSplittingStatus, 0).in(OrderRecord::getId, split));
            //修改订单产品分架数量
            orderProductMapper.update(null, Wrappers.<OrderProduct>update().lambda().set(OrderProduct::getShelfNum, 0).in(OrderProduct::getOrderId, split));
            //分装流程卡
            for (Map.Entry<String, List<ShelfDivisionBusiness>> stringListEntry : flowCards.entrySet()) {
                //删除订单相关流程卡明细
                stringListEntry.getValue().removeIf(item -> orderNos.contains(item.getOrderNo()));
                //计算总数、总面积、总重量
                Integer num = 0;
                Double area = 0.000;
                Double weight = 0.000;
                Set<String> orderNoSet = new HashSet<>();
                Set<String> customNoSet = new HashSet<>();
                Set<String> productNameSet = new HashSet<>();
                Set<String> customerNameSet = new HashSet<>();
                for (ShelfDivisionBusiness shelfDivisionBusiness : stringListEntry.getValue()) {
                    num += shelfDivisionBusiness.getItemNum();
                    area += shelfDivisionBusiness.getTotalArea();
                    weight += shelfDivisionBusiness.getTotalWeight();
                    orderNoSet.add(shelfDivisionBusiness.getOrderNo());
                    customNoSet.add(shelfDivisionBusiness.getCustomNo());
                    productNameSet.add(shelfDivisionBusiness.getProductName());
                    customerNameSet.add(shelfDivisionBusiness.getCustomerName());
                    shelfDivisionBusinessMapper.insert(shelfDivisionBusiness);
                }
                if (stringListEntry.getValue().size() > 0) {
                    //封装流程卡对象
                    FlowCard flowCard = new FlowCard();
                    flowCard.setId(stringListEntry.getValue().get(0).getFlowCardId());
                    flowCard.setFlowCardNo(stringListEntry.getKey());
                    flowCard.setMonolithicThick(stringListEntry.getValue().get(0).getItemThick());
                    flowCard.setOrderNo(orderNoSet.toString().substring(1, orderNoSet.toString().length() - 1));
                    flowCard.setCustomNo(customNoSet.toString().substring(1, customNoSet.toString().length() - 1));
                    flowCard.setProductName(productNameSet.toString().substring(1, productNameSet.toString().length() - 1));
                    flowCard.setCustomerName(customerNameSet.toString().substring(1, customerNameSet.toString().length() - 1));
                    flowCard.setMonolithicName(stringListEntry.getValue().get(0).getItemName());
                    flowCard.setProcessContent(stringListEntry.getValue().get(0).getProcessContent());
                    flowCard.setSplitDate(new Date());
                    flowCard.setSplitPerson(SecurityUtils.getLoginUser().getUser().getNickName());
                    flowCard.setSplitNum(num);
                    flowCard.setTotalArea(MapUtil.round(area, 3));
                    flowCard.setTotalWeight(MapUtil.round(weight, 3));
                    flowCard.setProductionStatus(0);
                    flowCardMapper.insert(flowCard);
                }
            }
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 下载Excel模板
     */
    @Override
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        //创建一个excel对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建产品模板sheet页对象
        XSSFSheet sheetOne = workbook.createSheet("产品模板");
        //行号
        int rowNum = 0;
        //创建第一页的第一行
        XSSFRow row = sheetOne.createRow(rowNum++);
        /* 处理sheetOne */
        //表头文字
        String[] text = new String[] {"产品名称", "宽", "高", "数量", "单价", "位置", "加工要求", "备注"};
        //冻结首行
        sheetOne.createFreezePane(0, 1, 0, 1);
        //遍历表头
        for (int i = 0; i < text.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(text[i]);
        }
        //添加虚拟数据
        List<Object[]> objectList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Object [] objects = new Object[text.length];
            objects[0] = "8mm钢化白玻";
            objects[1] = 800;
            objects[2] = 900;
            objects[3] = 20;
            objects[4] = 120;
            objects[5] = "一楼";
            objects[6] = "精磨边";
            objects[7] = "无";
            objectList.add(objects);
        }
        //将数据放入表格中
        for (int i = 0; i < objectList.size(); i++) {
            Row temp = sheetOne.createRow((rowNum++));
            Object [] objects = objectList.get(i);
            for (int i1 = 0; i1 < objects.length; i1++) {
                Cell c = temp.createCell(i1);
                c.setCellValue(objects[i1].toString());
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的方式打开文件
        headers.setContentDispositionFormData("attachment", "template.xlsx");
        //定义以流的形式下载返回文件数据
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return new ResponseEntity<>(outputStream.toByteArray(), headers, org.springframework.http.HttpStatus.OK);
    }

    /**
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public CommonResult<List<OrderProduct>> importTemplate(MultipartFile file) throws IOException {
        //创建文件数据接收对象
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        //产品接收集合
        List<Map<String, Object>> list = new ArrayList<>();
        //用户存储表格行数据
        Map<String, Object> map = new HashMap<>();
        //获取Excel文件表格
        Sheet sheet = workbook.getSheetAt(0);
        //获取表格最大行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        //声明每个字段属性
        String[] colNames = new String[]{"productName", "wideHead", "highHead", "num", "unitPrice", "position", "requirement", "remarks"};
        //遍历行数
        for (int i = 1; i < rowNum; i++) {
            map = new HashMap<>();
            //获取行数据
            Row row = sheet.getRow(i);
            //遍历行中每个单元格数据
            for (int i1 = 0; i1 < colNames.length; i1++) {
                Object cellData = null;
                try {
                    cellData = (String)row.getCell(i1).getStringCellValue();
                } catch (Exception e) {
                    try{    //如果某一个单元格为纯数字的字符串时会报错，需特殊处理
                        String[] a = new DecimalFormat().format(row.getCell(i1).getNumericCellValue()).split(",");
                        cellData = a[0];
                        for(int k = 1; k < a.length ; k++){
                            cellData += a[k];
                        }
                    }catch(Exception ex){
                        cellData = null;
                    }
                }
                map.put(colNames[i1],cellData);
            }
            list.add(map);
        }
        return CommonResult.success(list);
    }

    //将附加项相关字段添加到产品map
    public static Map<String, Object> entityToMap(Object object, String alias, Map<String, Object> map) {
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                if (!field.getName().equals("id") && !field.getName().equals("productId") && !field.getName().equals("additionalName") && !field.getName().equals("additionalAlias")) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object o = field.get(object);
                    if (o != null && !o.equals("")) {
                        String key = alias + "_" + field.getName();
                        map.put(key, o);
                    }
                    field.setAccessible(flag);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
