package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.MapUtil;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.sales.OrderMapper;
import com.erp.floor.mapper.sales.OrderProductMapper;
import com.erp.floor.mapper.sales.PackingBusinessMapper;
import com.erp.floor.mapper.sales.PackingRecordMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.sales.domain.vo.PackingRecordVo;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.sales.service.PackingRecordService;
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
 * @date： 2022-08-24 20:41
 */
@Service
public class PackingRecordServiceImpl extends ServiceImpl<PackingRecordMapper, PackingRecord> implements PackingRecordService {
    @Resource
    private PackingRecordMapper packingRecordMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private PackingBusinessMapper packingBusinessMapper;
    @Resource
    private OrderProductMapper orderProductMapper;
    @Resource
    private OrderMapper orderMapper;


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
        LambdaQueryWrapper<PackingRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(PackingRecord::getPackagingNo, format1);
        wrapper.like(PackingRecord::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(PackingRecord::getCreatedAt).last("limit 1");
        PackingRecord packingRecord = packingRecordMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("11");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (packingRecord != null) {
            String packagingNo = packingRecord.getPackagingNo();
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
     * 查询打包信息
     *
     * @param packingRecordVo 查询实体类
     */
    @Override
    public CommonResult<List<PackingRecord>> queryPackData(PackingRecordVo packingRecordVo) {
        try {
            if (packingRecordVo.getPageNum() == null || packingRecordVo.getPageNum() == 0) {
                return CommonResult.error(ResultConstants.PAGE_NUM);
            }
            if (packingRecordVo.getPageSize() == null || packingRecordVo.getPageSize() == 0) {
                return CommonResult.error(ResultConstants.PAGE_SIZE);
            }
            LambdaQueryWrapper<PackingRecord> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(packingRecordVo.getId()))
                wrapper.eq(PackingRecord::getId, packingRecordVo.getId());
            if (StringUtils.isNotEmpty(packingRecordVo.getOrderId()))
                wrapper.eq(PackingRecord::getOrderId, packingRecordVo.getOrderId());
            if (StringUtils.isNotEmpty(packingRecordVo.getOrderNo()))
                wrapper.eq(PackingRecord::getOrderNo, packingRecordVo.getOrderNo());
            if (StringUtils.isNotEmpty(packingRecordVo.getPackagingNo()))
                wrapper.eq(PackingRecord::getPackagingNo, packingRecordVo.getPackagingNo());
            if (StringUtils.isNotEmpty(packingRecordVo.getPackagingName()))
                wrapper.like(PackingRecord::getPackagingName, packingRecordVo.getPackagingName());
            if (StringUtils.isNotEmpty(packingRecordVo.getProductName()))
                wrapper.like(PackingRecord::getProductName, packingRecordVo.getProductName());
            if (StringUtils.isNotEmpty(packingRecordVo.getCustomerName()))
                wrapper.like(PackingRecord::getCustomerName, packingRecordVo.getCustomerName());
            if (StringUtils.isNotEmpty(packingRecordVo.getEntryName()))
                wrapper.like(PackingRecord::getEntryName, packingRecordVo.getEntryName());
            if (StringUtils.isNotEmpty(packingRecordVo.getResponsiblePerson()))
                wrapper.like(PackingRecord::getResponsiblePerson, packingRecordVo.getResponsiblePerson());
            if (StringUtils.isNotEmpty(packingRecordVo.getPackagingMode()))
                wrapper.like(PackingRecord::getPackagingMode, packingRecordVo.getPackagingMode());
            if (StringUtils.isNotEmpty(packingRecordVo.getPackagingDateStart()) && StringUtils.isNotEmpty(packingRecordVo.getPackagingDateEnd()))
                wrapper.between(PackingRecord::getPackagingDate, packingRecordVo.getPackagingDateStart(), packingRecordVo.getPackagingDateEnd());
            wrapper.orderByDesc(PackingRecord::getPackagingDate);
            Integer integer = packingRecordMapper.selectCount(wrapper);
            PageHelper.startPage(packingRecordVo.getPageNum(), packingRecordVo.getPageSize());
            List<PackingRecord> packingRecords = packingRecordMapper.selectList(wrapper);
            PageInfo<PackingRecord> pageInfo = new PageInfo<>(packingRecords, packingRecordVo.getPageSize());
            return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultConstants.QUERY_ERROR);
        }
    }

    /**
     * 新增打包
     *
     * @param packingRecord 打包信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult addPack(PackingRecord packingRecord) {
        try {
            Set<String> orderId = new HashSet<>();
            Set<String> orderNo = new HashSet<>();
            Set<String> customNo = new HashSet<>();
            Set<String> projectName = new HashSet<>();
            Set<String> productName = new HashSet<>();
            Integer num = 0;
            Double area = 0.000;
            Double weight = 0.000;
            String packId = UUID.randomUUID().toString();
            List<PackingBusiness> list = new ArrayList<>();
            for (OrderProduct orderProduct : packingRecord.getList()) {
                Map<String, Object> map = MapUtil.objectToMap(orderProduct);
                PackingBusiness packingBusiness = MapUtil.mapToEntity(map, PackingBusiness.class);
                packingBusiness.setId(UUID.randomUUID().toString());
                packingBusiness.setPackId(packId);
                packingBusiness.setPackNo(packingRecord.getPackagingNo());
                packingBusiness.setProductId(orderProduct.getId());
                packingBusiness.setPackNum(orderProduct.getNoShelfNum());
                packingBusiness.setProductArea(MapUtil.round(orderProduct.getSingleClearingArea() * orderProduct.getNoShelfNum(), 3));
                packingBusiness.setProductWeight(MapUtil.round((orderProduct.getProductWeight() / orderProduct.getNum() * packingBusiness.getPackNum()), 3));
                list.add(packingBusiness);
                packingBusinessMapper.insert(packingBusiness);
                orderId.add(orderProduct.getOrderId());
                orderNo.add(orderProduct.getOrderNo());
                customNo.add(orderProduct.getCustomNo());
                projectName.add(orderProduct.getEntryName());
                productName.add(orderProduct.getProductName());
                num += packingBusiness.getPackNum();
                area += packingBusiness.getProductArea();
                weight += packingBusiness.getProductWeight();
            }
            packingRecord.setId(packId);
            packingRecord.setOrderId(orderId.toString().substring(1, orderId.toString().length() - 1));
            packingRecord.setOrderNo(orderNo.toString().substring(1, orderNo.toString().length() - 1));
            packingRecord.setCustomNo(customNo.toString().substring(1, customNo.toString().length() - 1));
            packingRecord.setCustomerName(packingRecord.getList().get(0).getCustomerName());
            packingRecord.setEntryName(projectName.toString().substring(1, projectName.toString().length() - 1));
            packingRecord.setProductName(productName.toString().substring(1, productName.toString().length() - 1));
            packingRecord.setPackNum(num);
            packingRecord.setDeliverState("未发货");
            packingRecord.setPackagingArea(MapUtil.round(area, 3));
            packingRecord.setPackagingWeight(MapUtil.round(weight, 3));
            packingRecord.setCreatedAt(new Date());
            packingRecord.setCreatedPeople(SecurityUtils.getLoginUser().getUser().getNickName());
            packingRecordMapper.insert(packingRecord);
            //修改产品的打包数量
            updatePackNum(list , 0, null);
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
     * @param id 打包id
     */
    @Override
    public CommonResult<List<PackingBusiness>> detailsView(String id) {
        List<PackingBusiness> packingBusinesses = packingBusinessMapper.selectList(Wrappers.<PackingBusiness>query().lambda().eq(PackingBusiness::getPackId, id));
        return CommonResult.success(packingBusinesses);
    }

    /**
     * 编辑打包-查询明细
     *
     * @param id 打包id
     */
    @Override
    public CommonResult<List<PackingBusiness>> updateDetails(String id) {
        //查询明细
        List<PackingBusiness> list = packingBusinessMapper.selectList(Wrappers.<PackingBusiness>query().lambda().eq(PackingBusiness::getPackId, id));
        //查询产品
        Map<String, List<PackingBusiness>> collect = list.stream().collect(Collectors.groupingBy(PackingBusiness::getProductId));
        List<OrderProduct> orderProducts = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().in(OrderProduct::getId, collect.keySet()));
        for (PackingBusiness packingBusiness : list) {
            for (OrderProduct orderProduct : orderProducts) {
                if (packingBusiness.getProductId().equals(orderProduct.getId())) {
                    packingBusiness.setNotPackNum(orderProduct.getWarehousingQuantity() - orderProduct.getOrderPackNum());
                    packingBusiness.setUpdatePackNum(packingBusiness.getPackNum());
                }
            }
        }
        return CommonResult.success(list);
    }

    /**
     * 编辑打包 —— 保存
     *
     * @param packingRecord 打包对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult updatePack(PackingRecord packingRecord) {
        try {
            Set<String> orderId = new HashSet<>();
            Set<String> orderNo = new HashSet<>();
            Set<String> customNo = new HashSet<>();
            Set<String> projectName = new HashSet<>();
            Set<String> productName = new HashSet<>();
            Integer num = 0;
            Double area = 0.000;
            Double weight = 0.000;
            List<PackingBusiness> packList = packingRecord.getPackList();
            //修改相关数据
            updatePackNum(packList , 1, packingRecord.getId());
            //删除旧打包明细
            packingBusinessMapper.delete(Wrappers.<PackingBusiness>query().lambda().eq(PackingBusiness::getPackId, packingRecord.getId()));
            for (PackingBusiness packingBusiness : packList) {
                if (packingBusiness.getUpdatePackNum() > 0) {
                    //判断是否是新数据
                    if (StringUtils.isNotEmpty(packingBusiness.getPackId())) {
                        packingBusiness.setProductArea(MapUtil.round(packingBusiness.getProductArea() / packingBusiness.getPackNum() * packingBusiness.getUpdatePackNum(), 3));
                        packingBusiness.setProductWeight(MapUtil.round(packingBusiness.getProductWeight() / packingBusiness.getPackNum() * packingBusiness.getUpdatePackNum(), 3));
                    }else {
                        packingBusiness.setProductId(packingBusiness.getId());
                        packingBusiness.setProductArea(MapUtil.round((packingBusiness.getProductArea() / packingBusiness.getNum() * packingBusiness.getUpdatePackNum()), 3));
                        packingBusiness.setProductWeight(MapUtil.round((packingBusiness.getProductWeight() / packingBusiness.getNum() * packingBusiness.getUpdatePackNum()), 3));
                    }
                    packingBusiness.setPackId(packingRecord.getId());
                    packingBusiness.setPackNo(packingRecord.getPackagingNo());
                    packingBusiness.setId(UUID.randomUUID().toString());
                    packingBusiness.setPackNum(packingBusiness.getUpdatePackNum());
                    packingBusinessMapper.insert(packingBusiness);
                    orderId.add(packingBusiness.getOrderId());
                    orderNo.add(packingBusiness.getOrderNo());
                    customNo.add(packingBusiness.getCustomNo());
                    projectName.add(packingBusiness.getEntryName());
                    productName.add(packingBusiness.getProductName());
                    num += packingBusiness.getPackNum();
                    area += packingBusiness.getProductArea();
                    weight += packingBusiness.getProductWeight();
                }
            }
            packingRecord.setOrderId(orderId.toString().substring(1, orderId.toString().length() - 1));
            packingRecord.setOrderNo(orderNo.toString().substring(1, orderNo.toString().length() - 1));
            packingRecord.setCustomNo(customNo.toString().substring(1, customNo.toString().length() - 1));
            packingRecord.setCustomerName(packingRecord.getPackList().get(0).getCustomerName());
            packingRecord.setEntryName(projectName.toString().substring(1, projectName.toString().length() - 1));
            packingRecord.setProductName(productName.toString().substring(1, productName.toString().length() - 1));
            packingRecord.setPackNum(num);
            packingRecord.setPackagingArea(MapUtil.round(area, 3));
            packingRecord.setPackagingWeight(MapUtil.round(weight, 3));
            packingRecord.setUpdatedAt(new Date());
            packingRecord.setUpdatedPeople(SecurityUtils.getLoginUser().getUser().getNickName());
            packingRecordMapper.updateById(packingRecord);
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    /**
     * 删除打包
     *
     * @param ids 打包id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult delPack(String ids) {
        //删除打包信息
        try {
            String[] split = ids.split(",");
            packingRecordMapper.delete(Wrappers.<PackingRecord>query().lambda().in(PackingRecord::getId,split));
            List<PackingBusiness> list = packingBusinessMapper.selectList(Wrappers.<PackingBusiness>query().lambda().in(PackingBusiness::getPackId, split));
            packingBusinessMapper.delete(Wrappers.<PackingBusiness>query().lambda().in(PackingBusiness::getPackId, split));
            updatePackNum(list , 2, null);
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 导出打包信息
     *
     * @param response
     * @param ids      打包id
     */
    @Override
    public void exportPack(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<PackingRecord> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(PackingRecord::getId , split);
        }
        wrapper.orderByDesc(PackingRecord::getPackagingNo);
        List<PackingRecord> packingRecords = packingRecordMapper.selectList(wrapper);
        ExcelUtil<PackingRecord> util = new ExcelUtil<PackingRecord>(PackingRecord.class);
        util.exportExcel(response, packingRecords, "参数数据");
    }

    /**
     * 导出打包明细
     *
     * @param response
     * @param ids      打包id
     */
    @Override
    public void exportPackDetailed(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<PackingBusiness> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(PackingBusiness::getPackId , split);
        }
        wrapper.orderByDesc(PackingBusiness::getPackNo);
        List<PackingBusiness> packingBusinesses = packingBusinessMapper.selectList(wrapper);
        ExcelUtil<PackingBusiness> util = new ExcelUtil<PackingBusiness>(PackingBusiness.class);
        util.exportExcel(response, packingBusinesses, "参数数据");
    }

    /**
     * 订单管理-查询打包记录
     *
     * @param id 订单id
     */
    @Override
    public CommonResult<List<PackingBusiness>> queryPack(String id) {
        return CommonResult.success(packingBusinessMapper.queryPack(id));
    }


    //修改订单产品的打包数量
    public void updatePackNum(List<PackingBusiness> list, int x, String packId) {
        //获取所有产品id
        Map<String, List<PackingBusiness>> collect = list.stream().collect(Collectors.groupingBy(PackingBusiness::getProductId));
        //查询旧明细
        List<PackingBusiness> packingBusinesses = null;
         //添加id集合
        Set<String> productIds = new HashSet<>();
        productIds.addAll(collect.keySet());
        if (x == 1) {
            packingBusinesses = packingBusinessMapper.selectList(Wrappers.<PackingBusiness>query().lambda().eq(PackingBusiness::getPackId, packId));
            for (PackingBusiness packingBusiness : packingBusinesses) {
                productIds.add(packingBusiness.getProductId());
            }
        }
        //获取要修改的产品
        List<OrderProduct> orderProducts = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().in(OrderProduct::getId, productIds));
        //修改产品打包数量
        for (OrderProduct orderProduct : orderProducts) {
            int packNum = 0;
            //减去旧数据
            if (x == 1){
                for (PackingBusiness packingBusiness : packingBusinesses) {
                    if (orderProduct.getId().equals(packingBusiness.getProductId())) {
                        packNum -= packingBusiness.getPackNum();
                    }
                }
            }
            //加上新数据
            for (Map.Entry<String, List<PackingBusiness>> stringListEntry : collect.entrySet()) {
                if (x == 0 && orderProduct.getId().equals(stringListEntry.getKey())) { //新增
                    packNum += stringListEntry.getValue().stream().mapToInt(PackingBusiness::getPackNum).sum();
                } else if (x == 1 && orderProduct.getId().equals(stringListEntry.getKey())) {//编辑
                    packNum += stringListEntry.getValue().stream().mapToInt(PackingBusiness::getUpdatePackNum).sum();
                } else if (x == 2 && orderProduct.getId().equals(stringListEntry.getKey())){//删除
                    packNum -= stringListEntry.getValue().stream().mapToInt(PackingBusiness::getPackNum).sum();
                }
            }
            orderProduct.setOrderPackNum(orderProduct.getOrderPackNum() + packNum);
            orderProductMapper.updateById(orderProduct);
        }
        Set<String> set = orderProducts.stream().collect(Collectors.groupingBy(OrderProduct::getOrderId)).keySet();
        List<OrderProduct> orderProductAll = orderProductMapper.selectList(Wrappers.<OrderProduct>query().lambda().in(OrderProduct::getOrderId, set));
        for (Map.Entry<String, List<OrderProduct>> stringListEntry : orderProductAll.stream().collect(Collectors.groupingBy(OrderProduct::getOrderId)).entrySet()) {
            List<OrderProduct> collectOne = stringListEntry.getValue().stream().filter(item -> item.getNum() > item.getOrderPackNum()).collect(Collectors.toList());
            if (collectOne.size() <= 0) {
                orderMapper.update(null,
                        Wrappers.<OrderRecord>update().lambda()
                                .set(OrderRecord::getPackagingStatus, 2)
                                .eq(OrderRecord::getId, stringListEntry.getKey()));
            }
            List<OrderProduct> collect1 = stringListEntry.getValue().stream().filter(item -> item.getOrderPackNum() == 0).collect(Collectors.toList());
            if (collectOne.size() == stringListEntry.getValue().size() && collect1.size() != stringListEntry.getValue().size()) {
                orderMapper.update(null,
                        Wrappers.<OrderRecord>update().lambda()
                                .set(OrderRecord::getPackagingStatus, 1)
                                .eq(OrderRecord::getId, stringListEntry.getKey()));
            }
            if (collect1.size() == stringListEntry.getValue().size()) {
                orderMapper.update(null,
                        Wrappers.<OrderRecord>update().lambda()
                                .set(OrderRecord::getPackagingStatus, 0)
                                .eq(OrderRecord::getId, stringListEntry.getKey()));
            }
        }

    }
}
