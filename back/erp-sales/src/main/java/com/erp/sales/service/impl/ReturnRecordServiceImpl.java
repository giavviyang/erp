package com.erp.sales.service.impl;

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
import com.erp.floor.mapper.sales.DeliverBusinessMapper;
import com.erp.floor.mapper.sales.ReturnBusinessMapper;
import com.erp.floor.mapper.sales.ReturnRecordMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.sales.domain.DeliverBusiness;
import com.erp.floor.pojo.sales.domain.DeliverRecord;
import com.erp.floor.pojo.sales.domain.ReturnBusiness;
import com.erp.floor.pojo.sales.domain.ReturnRecord;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.sales.service.ReturnRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
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
 * @date： 2022-09-05 14:42
 */
@Service
public class ReturnRecordServiceImpl extends ServiceImpl<ReturnRecordMapper, ReturnRecord> implements ReturnRecordService {
    @Resource
    private ReturnRecordMapper returnRecordMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private DeliverBusinessMapper deliverBusinessMapper;
    @Resource
    private ReturnBusinessMapper returnBusinessMapper;


    /**
     * 自动生成编号
     */
    @Override
    public String productionNumber() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<ReturnRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ReturnRecord::getReturnNo, format1);
        wrapper.like(ReturnRecord::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(ReturnRecord::getCreatedAt).last("limit 1");
        ReturnRecord returnRecord = returnRecordMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("14");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (returnRecord != null) {
            String packagingNo = returnRecord.getReturnNo();
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
     * @param returnRecord 退货对象
     */
    @Override
    public CommonResult<List<ReturnRecord>> queryData(ReturnRecord returnRecord) {
        if (returnRecord.getPageNum() == null && returnRecord.getPageNum() == 0) {
            return CommonResult.error(ResultConstants.PAGE_NUM);
        }
        if (returnRecord.getPageSize() == null && returnRecord.getPageSize() == 0) {
            return CommonResult.error(ResultConstants.PAGE_SIZE);
        }
        LambdaQueryWrapper<ReturnRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(returnRecord.getId()))
            wrapper.eq(ReturnRecord::getId, returnRecord.getId());
        if (StringUtils.isNotEmpty(returnRecord.getReturnNo()))
            wrapper.eq(ReturnRecord::getReturnNo, returnRecord.getReturnNo());
        if (returnRecord.getExamineStarts() != null)
            wrapper.eq(ReturnRecord::getExamineStarts, returnRecord.getExamineStarts());
        if (StringUtils.isNotEmpty(returnRecord.getReturnPerson()))
            wrapper.like(ReturnRecord::getReturnPerson, returnRecord.getReturnPerson());
        if (StringUtils.isNotEmpty(returnRecord.getDeliverNo()))
            wrapper.like(ReturnRecord::getDeliverNo, returnRecord.getDeliverNo());
        if (StringUtils.isNotEmpty(returnRecord.getOrderNo()))
            wrapper.like(ReturnRecord::getOrderNo, returnRecord.getOrderNo());
        if (StringUtils.isNotEmpty(returnRecord.getCustomNo()))
            wrapper.like(ReturnRecord::getCustomNo, returnRecord.getCustomNo());
        if (StringUtils.isNotEmpty(returnRecord.getProductName()))
            wrapper.like(ReturnRecord::getProductName, returnRecord.getProductName());
        if (StringUtils.isNotEmpty(returnRecord.getCustomerName()))
            wrapper.like(ReturnRecord::getCustomerName, returnRecord.getCustomerName());
        if (StringUtils.isNotEmpty(returnRecord.getEntryName()))
            wrapper.like(ReturnRecord::getEntryName, returnRecord.getEntryName());
        if (StringUtils.isNotEmpty(returnRecord.getReturnDateStart()) && StringUtils.isNotEmpty(returnRecord.getReturnDateEnd()))
            wrapper.between(ReturnRecord::getReturnDate, returnRecord.getReturnDateStart(), returnRecord.getReturnDateEnd());
        wrapper.orderByDesc(ReturnRecord::getReturnNo);
        Integer integer = returnRecordMapper.selectCount(wrapper);
        PageHelper.startPage(returnRecord.getPageNum(), returnRecord.getPageSize());
        List<ReturnRecord> returnRecords = returnRecordMapper.selectList(wrapper);
        PageInfo<ReturnRecord> info = new PageInfo<>(returnRecords, returnRecord.getPageSize());
        return new CommonResult(200, ResultConstants.QUERY_SUCCESS, integer, info.getList());
    }

    /**
     * 发货数据查询
     */
    @Override
    public CommonResult<List<DeliverBusiness>> queryDeliverData(DeliverBusiness deliverBusiness) {
        List<DeliverBusiness> deliverBusinessList = deliverBusinessMapper.queryBusiness(deliverBusiness);
        //过滤退货数量大于发货数量的发货明细
        List<DeliverBusiness> collect = deliverBusinessList.stream().filter(item -> item.getDeliverNum() > item.getDeliverReturnNum()).collect(Collectors.toList());
        return CommonResult.success(collect);
    }

    /**
     * 新增退货
     *
     * @param returnRecord
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult addReturnData(ReturnRecord returnRecord) {
        try {
            //获取退货明细
            List<DeliverBusiness> deliverBusinessList = returnRecord.getDeliverBusinessList();
            //声明汇总信息
            Set<Object> deliverNo = new HashSet<>();
            Set<Object> orderNo = new HashSet<>();
            Set<Object> customNo = new HashSet<>();
            Set<Object> entryName = new HashSet<>();
            Set<Object> customerName = new HashSet<>();
            Set<Object> productName = new HashSet<>();
            Integer num = 0;
            Double totalMoney = 0.000;
            Double totalArea = 0.000;
            Double totalWeight = 0.000;
            //遍历退货明细
            for (DeliverBusiness deliverBusiness : deliverBusinessList) {
                ReturnBusiness one = new ReturnBusiness();
                one.setReturnId(returnRecord.getId());
                one.setReturnNo(returnRecord.getReturnNo());
                one.setDeliverBusinessId(deliverBusiness.getId());
                one.setProductId(deliverBusiness.getProductId());
                one.setReturnNum(deliverBusiness.getNoShelfNum());
                Double money = deliverBusiness.getDeliverAmount() / deliverBusiness.getDeliverNum() * one.getReturnNum();
                Double area = deliverBusiness.getDeliverArea() / deliverBusiness.getDeliverNum() * one.getReturnNum();
                Double weight = deliverBusiness.getDeliverWeight() / deliverBusiness.getDeliverNum() * one.getReturnNum();
                one.setReturnMoney(MapUtil.round(money, 3));
                one.setReturnArea(MapUtil.round(area, 3));
                one.setReturnWeight(MapUtil.round(weight, 3));
                deliverNo.add(deliverBusiness.getDeliverNo());
                orderNo.add(deliverBusiness.getOrderNo());
                customerName.add(deliverBusiness.getCustomerName());
                customNo.add(deliverBusiness.getCustomNo());
                entryName.add(deliverBusiness.getEntryName());
                productName.add(deliverBusiness.getProductName());
                num += one.getReturnNum();
                totalMoney += money;
                totalArea += area;
                totalWeight += weight;
                //新增退货明细
                returnBusinessMapper.insert(one);
                //修改发货明细中的退货数量
                deliverBusiness.setDeliverReturnNum(deliverBusiness.getDeliverReturnNum() + one.getReturnNum());
                deliverBusinessMapper.updateById(deliverBusiness);
            }
            returnRecord.setReturnNum(num);
            returnRecord.setReturnAmount(totalMoney);
            returnRecord.setReturnArea(totalArea);
            returnRecord.setReturnWeight(totalWeight);
            returnRecord.setCreatedAt(new Date());
            returnRecord.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            returnRecord.setDeliverNo(deliverNo.toString().substring(1, deliverNo.toString().length() - 1));
            returnRecord.setOrderNo(orderNo.toString().substring(1, orderNo.toString().length() - 1));
            returnRecord.setCustomNo(customNo.toString().substring(1, customNo.toString().length() - 1));
            returnRecord.setCustomerName(customerName.toString().substring(1, customerName.toString().length() - 1));
            returnRecord.setProductName(productName.toString().substring(1, productName.toString().length() - 1));
            returnRecord.setEntryName(entryName.toString().substring(1, entryName.toString().length() - 1));
            returnRecord.setExamineStarts(2);
            returnRecordMapper.insert(returnRecord);
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
     * @param id 退货id
     */
    @Override
    public CommonResult reviewDetailed(String id) {
        //查询退货信息
        ReturnRecord returnRecord = returnRecordMapper.selectById(id);
        //查询退货明细
        List<ReturnBusiness> returnBusinesses = returnBusinessMapper.queryBusiness(id);
        returnRecord.setReturnBusinesses(returnBusinesses);
        return CommonResult.success(returnRecord);
    }

    /**
     * 编辑退货
     *
     * @param returnRecord
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateReturnData(ReturnRecord returnRecord) {
        try {
            //获取退货明细
            List<ReturnBusiness> returnBusinesses = returnRecord.getReturnBusinesses();
            //声明汇总信息
            Set<Object> deliverNo = new HashSet<>();
            Set<Object> orderNo = new HashSet<>();
            Set<Object> customNo = new HashSet<>();
            Set<Object> entryName = new HashSet<>();
            Set<Object> customerName = new HashSet<>();
            Set<Object> productName = new HashSet<>();
            Integer num = 0;
            Double totalMoney = 0.000;
            Double totalArea = 0.000;
            Double totalWeight = 0.000;
            //查询旧退货明细
            List<ReturnBusiness> returnBusinessesOld =
                    returnBusinessMapper.selectList(Wrappers.<ReturnBusiness>query().lambda().eq(ReturnBusiness::getReturnId, returnRecord.getId()));
            Map<String, List<ReturnBusiness>> collect = returnBusinessesOld.stream().collect(Collectors.groupingBy(ReturnBusiness::getDeliverBusinessId));
            Set<String> deliverIds = new HashSet<>();
            deliverIds.addAll(collect.keySet());
            //删除旧发货明细
            returnBusinessMapper.delete(Wrappers.<ReturnBusiness>query().lambda().eq(ReturnBusiness::getReturnId, returnRecord.getId()));
            //遍历退货明细
            for (ReturnBusiness one : returnBusinesses) {
                Double money = one.getDeliverAmount() / one.getDeliverNum() * one.getNoShelfNum();
                Double area = one.getProductArea() / one.getNum() * one.getNoShelfNum();
                Double weight = one.getProductWeight() / one.getNum() * one.getNoShelfNum();
                one.setReturnMoney(MapUtil.round(money, 3));
                one.setReturnArea(MapUtil.round(area, 3));
                one.setReturnWeight(MapUtil.round(weight, 3));
                one.setReturnNum(one.getNoShelfNum());
                deliverNo.add(one.getDeliverNo());
                orderNo.add(one.getOrderNo());
                customerName.add(one.getCustomerName());
                customNo.add(one.getCustomNo());
                entryName.add(one.getEntryName());
                productName.add(one.getProductName());
                num += one.getNoShelfNum();
                totalMoney += money;
                totalArea += area;
                totalWeight += weight;
                //判断是新增明细吗
                if (!StringUtils.isNotEmpty(one.getReturnId())) {
                    one.setReturnId(returnRecord.getId());
                    one.setReturnNo(returnRecord.getReturnNo());
                    one.setDeliverBusinessId(one.getId());
                    one.setProductId(one.getProductId());
                    one.setReturnNum(one.getNoShelfNum());
                    deliverIds.add(one.getId());
                }
                returnBusinessMapper.insert(one);
            }
            returnRecord.setReturnNum(num);
            returnRecord.setReturnAmount(totalMoney);
            returnRecord.setReturnArea(totalArea);
            returnRecord.setReturnWeight(totalWeight);
            returnRecord.setUpdatedAt(new Date());
            returnRecord.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            returnRecord.setDeliverNo(deliverNo.toString().substring(1, deliverNo.toString().length() - 1));
            returnRecord.setOrderNo(orderNo.toString().substring(1, orderNo.toString().length() - 1));
            returnRecord.setCustomNo(customNo.toString().substring(1, customNo.toString().length() - 1));
            returnRecord.setCustomerName(customerName.toString().substring(1, customerName.toString().length() - 1));
            returnRecord.setProductName(productName.toString().substring(1, productName.toString().length() - 1));
            returnRecord.setEntryName(entryName.toString().substring(1, entryName.toString().length() - 1));
            returnRecordMapper.updateById(returnRecord);
            //查询发货明细
            for (DeliverBusiness deliverBusiness : deliverBusinessMapper.selectList(Wrappers.<DeliverBusiness>query().lambda().in(DeliverBusiness::getId, deliverIds))) {
                int updateNum = 0;
                //加上旧数量
                for (ReturnBusiness returnBusiness : returnBusinessesOld) {
                    if (deliverBusiness.getId().equals(returnBusiness.getDeliverBusinessId())) {
                        updateNum += returnBusiness.getReturnNum();
                    }
                }
                //减去新数量
                for (ReturnBusiness returnBusiness : returnBusinesses) {
                    if (deliverBusiness.getId().equals(returnBusiness.getDeliverBusinessId())) {
                        updateNum -= returnBusiness.getNoShelfNum();
                    }
                }
                deliverBusiness.setDeliverReturnNum(deliverBusiness.getDeliverReturnNum() - updateNum);
                deliverBusinessMapper.updateById(deliverBusiness);
            }
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    /**
     * 删除退货
     *
     * @param ids 退货id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delReturnData(String ids) {
        try {
            String[] split = ids.split(",");
            //获取退货明细
            List<ReturnBusiness> returnBusinesses = returnBusinessMapper.selectList(Wrappers.<ReturnBusiness>query().lambda().in(ReturnBusiness::getReturnId, split));
            //依据发货明细分组
            Map<String, List<ReturnBusiness>> collect = returnBusinesses.stream().collect(Collectors.groupingBy(ReturnBusiness::getDeliverBusinessId));
            //获取发货明细
            List<DeliverBusiness> deliverBusinessList = deliverBusinessMapper.selectList(Wrappers.<DeliverBusiness>query().lambda().in(DeliverBusiness::getId, collect.keySet()));
            //修改发货明细中的  退货数量
            for (Map.Entry<String, List<ReturnBusiness>> stringListEntry : collect.entrySet()) {
                //获取退货数量总和
                int sum = stringListEntry.getValue().stream().mapToInt(ReturnBusiness::getReturnNum).sum();
                for (DeliverBusiness deliverBusiness : deliverBusinessList) {
                    if (stringListEntry.getKey().equals(deliverBusiness.getId())) {
                        deliverBusinessMapper.update(null,
                                Wrappers.<DeliverBusiness>update().lambda()
                                        .set(DeliverBusiness::getDeliverReturnNum, deliverBusiness.getDeliverReturnNum() - sum)
                                        .eq(DeliverBusiness::getId, deliverBusiness.getId())
                        );
                    }
                }
            }
            returnRecordMapper.deleteBatchIds(Arrays.asList(split));
            returnBusinessMapper.delete(Wrappers.<ReturnBusiness>update().lambda().in(ReturnBusiness::getReturnId, split));
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 退货审核
     *
     * @param id            退货id
     * @param examinePerson 审核人
     * @param examineReason examineReason
     */
    @Override
    public CommonResult examineReturnData(String id, int examineStarts, String examinePerson, String examineReason) {
        LambdaUpdateWrapper<ReturnRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(ReturnRecord::getExamineDate, new Date());
        wrapper.set(ReturnRecord::getExaminePerson, examinePerson);
        wrapper.set(ReturnRecord::getExamineReason, examineReason);
        wrapper.set(ReturnRecord::getExamineStarts, examineStarts);
        wrapper.eq(ReturnRecord::getId, id);
        returnRecordMapper.update(null, wrapper);
        return CommonResult.success(ResultConstants.EXAMINE_SUCCESS);
    }

    /**
     * 退货消审
     *
     * @param id 退货id
     */
    @Override
    public CommonResult cancelExamine(String id) {
        LambdaUpdateWrapper<ReturnRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(ReturnRecord::getExamineDate, null);
        wrapper.set(ReturnRecord::getExaminePerson, null);
        wrapper.set(ReturnRecord::getExamineReason, null);
        wrapper.set(ReturnRecord::getExamineStarts, 2);
        wrapper.eq(ReturnRecord::getId, id);
        returnRecordMapper.update(null, wrapper);
        return CommonResult.success("消审成功！");
    }

    /**
     * 导出退货单
     *
     * @param response
     * @param ids      退货id集合
     */
    @Override
    public void exportReturn(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<ReturnRecord> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(ReturnRecord::getId, split);
        }
        wrapper.orderByDesc(ReturnRecord::getReturnNo);
        List<ReturnRecord> returnRecords = returnRecordMapper.selectList(wrapper);
        ExcelUtil<ReturnRecord> util = new ExcelUtil<ReturnRecord>(ReturnRecord.class);
        util.exportExcel(response, returnRecords, "参数数据");
    }

    /**
     * 导出退货明细
     *
     * @param response
     * @param ids      退货id集合
     */
    @Override
    public void exportReturnDetailed(HttpServletResponse response, String ids) {
        List<ReturnBusiness> returnBusinesses = null;
        if (StringUtils.isNotEmpty(ids)) {
            returnBusinesses = returnBusinessMapper.queryBusinessByIds(ids.split(","));
        } else {
            returnBusinesses = returnBusinessMapper.queryBusinessByIds(null);
        }
        ExcelUtil<ReturnBusiness> util = new ExcelUtil<ReturnBusiness>(ReturnBusiness.class);
        util.exportExcel(response, returnBusinesses, "参数数据");
    }
}
