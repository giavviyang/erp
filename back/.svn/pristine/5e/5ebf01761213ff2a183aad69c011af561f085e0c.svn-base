package com.erp.accessories.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.accessories.service.AccessoriesDamageService;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.accessories.AccessoriesDamageDetailMapper;
import com.erp.floor.mapper.accessories.AccessoriesDamageMapper;
import com.erp.floor.mapper.accessories.AccessoriesMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesDamage;
import com.erp.floor.pojo.accessories.domain.AccessoriesDamageDetail;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
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
 * @date： 2022-09-15 10:59
 */
@Service
public class AccessoriesDamageServiceImpl extends ServiceImpl<AccessoriesDamageMapper, AccessoriesDamage> implements AccessoriesDamageService {

    @Resource
    private AccessoriesDamageMapper accessoriesDamageMapper;
    @Resource
    private AccessoriesDamageDetailMapper accessoriesDamageDetailMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private AccessoriesMapper accessoriesMapper;

    /**
     * 生成原片报损编号
     */
    @Override
    public String productionNumber() {
        //查询当天是否新增过订单
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取月和日
        String format1 = format.format(new Date()).substring(4);
        SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        LambdaQueryWrapper<AccessoriesDamage> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(AccessoriesDamage::getDamageNo, format1);
        wrapper.like(AccessoriesDamage::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(AccessoriesDamage::getCreatedAt).last("limit 1");
        AccessoriesDamage accessoriesPurchase = accessoriesDamageMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("flbs");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (accessoriesPurchase != null) {
            String orderNo = accessoriesPurchase.getDamageNo();
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
     * 报损数据查询
     *
     * @param accessoriesDamage 对象
     */
    @Override
    public CommonResult<List<AccessoriesDamage>> queryDamageData(AccessoriesDamage accessoriesDamage) {
        LambdaQueryWrapper<AccessoriesDamage> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(accessoriesDamage.getDamageNo()))
            wrapper.eq(AccessoriesDamage::getDamageNo, accessoriesDamage.getDamageNo());
        if (StringUtils.isNotEmpty(accessoriesDamage.getPerson()))
            wrapper.like(AccessoriesDamage::getPerson, accessoriesDamage.getPerson());
        if (StringUtils.isNotEmpty(accessoriesDamage.getReason()))
            wrapper.like(AccessoriesDamage::getReason, accessoriesDamage.getReason());
        if (StringUtils.isNotEmpty(accessoriesDamage.getDamageDateStart()) && StringUtils.isNotEmpty(accessoriesDamage.getDamageDateEnd()))
            wrapper.between(AccessoriesDamage::getDamageDate, accessoriesDamage.getDamageDateStart(), accessoriesDamage.getDamageDateEnd());
        wrapper.orderByDesc(AccessoriesDamage::getDamageNo);
        Integer integer = accessoriesDamageMapper.selectCount(wrapper);
        PageHelper.startPage(accessoriesDamage.getPageNum(), accessoriesDamage.getPageSize());
        List<AccessoriesDamage> accessoriesDamages = accessoriesDamageMapper.selectList(wrapper);
        PageInfo<AccessoriesDamage> info = new PageInfo<>(accessoriesDamages, accessoriesDamage.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, info.getList());
    }

    /**
     * 查看明细
     *
     * @param id 报损id
     */
    @Override
    public CommonResult<List<AccessoriesDamageDetail>> viewDetail(String id) {
        return CommonResult.success(accessoriesDamageDetailMapper.viewDetail(new String[]{id}));
    }

    /**
     * 新增报损
     *
     * @param accessoriesDamage 对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult addDamageData(AccessoriesDamage accessoriesDamage) {
        try {
            //获取发货明细
            List<Accessories> accessoriesList = accessoriesDamage.getAccessoriesList();
            String id = UUID.randomUUID().toString();
            Integer totalNum = 0;
            Double totalAmout = 0.000;
            //遍历明细
            for (Accessories accessories : accessoriesList) {
                AccessoriesDamageDetail one = new AccessoriesDamageDetail();
                one.setDamageId(id);
                one.setDamageNo(accessoriesDamage.getDamageNo());
                one.setAccessoriesId(accessories.getId());
                one.setAccessoriesName(accessories.getAccessoriesName());
                one.setDamageNum(accessories.getOperationNum());
                totalNum += accessories.getOperationNum();
                totalAmout += accessories.getReferencePrice() * one.getDamageNum();
                //新增明细
                accessoriesDamageDetailMapper.insert(one);
                //修改仓库数量
                accessories.setStock(accessories.getStock() - one.getDamageNum());
                accessoriesMapper.updateById(accessories);
            }
            //新增报损记录
            accessoriesDamage.setId(id);
            accessoriesDamage.setDamageNum(totalNum);
            accessoriesDamage.setDamageAmount(totalAmout);
            accessoriesDamage.setCreatedAt(new Date());
            accessoriesDamage.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            accessoriesDamageMapper.insert(accessoriesDamage);
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * @param id 报损id
     */
    @Override
    public CommonResult<List<Accessories>> updDamageDataQuery(String id) {
        List<AccessoriesDamageDetail> accessoriesDamageDetails = accessoriesDamageDetailMapper.selectList(Wrappers.<AccessoriesDamageDetail>query().lambda().eq(AccessoriesDamageDetail::getDamageId, id));
        Set<String> set = accessoriesDamageDetails.stream().collect(Collectors.groupingBy(AccessoriesDamageDetail::getAccessoriesId)).keySet();
        List<Accessories> accessories = accessoriesMapper.selectList(Wrappers.<Accessories>query().lambda().in(Accessories::getId, set));
        for (Accessories accessory : accessories) {
            for (AccessoriesDamageDetail accessoriesDamageDetail : accessoriesDamageDetails) {
                accessory.setOperationNum(accessoriesDamageDetail.getDamageNum());
            }
        }
        return CommonResult.success(accessories);
    }

    /**
     * 编辑报损
     *
     * @param accessoriesDamage 对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updDamageData(AccessoriesDamage accessoriesDamage) {
        try {
            //获取旧明细
            List<AccessoriesDamageDetail> accessoriesDamageDetails = accessoriesDamageDetailMapper.selectList(Wrappers.<AccessoriesDamageDetail>query().lambda().eq(AccessoriesDamageDetail::getDamageId, accessoriesDamage.getId()));
            //删除旧发货明细
            accessoriesDamageDetailMapper.delete(Wrappers.<AccessoriesDamageDetail>query().lambda().eq(AccessoriesDamageDetail::getDamageId, accessoriesDamage.getId()));
            //获取发货明细
            List<Accessories> accessoriesList = accessoriesDamage.getAccessoriesList();
            Integer totalNum = 0;
            Double totalAmout = 0.000;
            //遍历明细
            for (Accessories accessories : accessoriesList) {
                AccessoriesDamageDetail one = new AccessoriesDamageDetail();
                one.setDamageId(accessoriesDamage.getId());
                one.setDamageNo(accessoriesDamage.getDamageNo());
                one.setAccessoriesId(accessories.getId());
                one.setAccessoriesName(accessories.getAccessoriesName());
                one.setDamageNum(accessories.getOperationNum());
                totalNum += accessories.getOperationNum();
                totalAmout += accessories.getReferencePrice() * one.getDamageNum();
                //新增明细
                accessoriesDamageDetailMapper.insert(one);
                //修改仓库数量
                for (AccessoriesDamageDetail accessoriesDamageDetail : accessoriesDamageDetails) {
                    if (accessories.getId().equals(accessoriesDamageDetail.getAccessoriesId())){
                        accessories.setStock(accessories.getStock() + accessoriesDamageDetail.getDamageNum() - one.getDamageNum());
                        accessoriesMapper.updateById(accessories);
                    }
                }
            }
            //编辑报损记录
            accessoriesDamage.setDamageNum(totalNum);
            accessoriesDamage.setDamageAmount(totalAmout);
            accessoriesDamage.setUpdatedAt(new Date());
            accessoriesDamage.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            accessoriesDamageMapper.updateById(accessoriesDamage);
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    /**
     * @param ids 删除报损
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delDamageData(String ids) {
        try {
            String[] split = ids.split(",");
            //获取报损明细
            List<AccessoriesDamageDetail> accessoriesDamageDetails = accessoriesDamageDetailMapper.selectList(Wrappers.<AccessoriesDamageDetail>query().lambda().in(AccessoriesDamageDetail::getDamageId, split));
            //还原仓库数量
            Map<String, List<AccessoriesDamageDetail>> collect = accessoriesDamageDetails.stream().collect(Collectors.groupingBy(AccessoriesDamageDetail::getAccessoriesId));
            //获取原片数据
            List<Accessories> accessoriess = accessoriesMapper.selectList(Wrappers.<Accessories>query().lambda().in(Accessories::getId, collect.keySet()));
            //还原仓库数量
            for (Map.Entry<String, List<AccessoriesDamageDetail>> stringListEntry : collect.entrySet()) {
                for (Accessories accessories : accessoriess) {
                    if (stringListEntry.getKey().equals(accessories.getId())) {
                        int sum = stringListEntry.getValue().stream().mapToInt(AccessoriesDamageDetail::getDamageNum).sum();
                        accessories.setStock(accessories.getStock() + sum);
                        accessoriesMapper.updateById(accessories);
                    }
                }
            }
            //删除旧发货明细
            accessoriesDamageDetailMapper.delete(Wrappers.<AccessoriesDamageDetail>query().lambda().in(AccessoriesDamageDetail::getDamageId, split));
            //删除旧发货记录
            accessoriesDamageMapper.delete(Wrappers.<AccessoriesDamage>query().lambda().in(AccessoriesDamage::getId, split));
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 导出报损记录
     *
     * @param response
     * @param ids      报损id
     */
    @Override
    public void exportDamage(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<AccessoriesDamage> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(AccessoriesDamage::getId, split);
        }
        wrapper.orderByDesc(AccessoriesDamage::getDamageNo);
        List<AccessoriesDamage> deliverRecords = accessoriesDamageMapper.selectList(wrapper);
        ExcelUtil<AccessoriesDamage> util = new ExcelUtil<AccessoriesDamage>(AccessoriesDamage.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }

    /**
     * 导出报损明细
     *
     * @param response
     * @param ids      报损id
     */
    @Override
    public void exportDamageDetails(HttpServletResponse response, String ids) {
        List<AccessoriesDamageDetail> deliverRecords = null;
        if (StringUtils.isNotEmpty(ids)) {
            deliverRecords = accessoriesDamageDetailMapper.viewDetail(ids.split(","));
        }else {
            deliverRecords = accessoriesDamageDetailMapper.viewDetail(null);
        }
        ExcelUtil<AccessoriesDamageDetail> util = new ExcelUtil<AccessoriesDamageDetail>(AccessoriesDamageDetail.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }


}
