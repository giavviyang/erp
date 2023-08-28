package com.erp.accessories.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.accessories.service.AccessoriesPurchaseService;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.accessories.AccessoriesMapper;
import com.erp.floor.mapper.accessories.AccessoriesPurchaseDetailMapper;
import com.erp.floor.mapper.accessories.AccessoriesPurchaseMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesPurchase;
import com.erp.floor.pojo.accessories.domain.AccessoriesPurchaseDetail;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 17:46
 */
@Service
public class AccessoriesPurchaseServiceImpl extends ServiceImpl<AccessoriesPurchaseMapper, AccessoriesPurchase> implements AccessoriesPurchaseService {

    @Resource
    private AccessoriesPurchaseMapper accessoriesPurchaseMapper;
    @Resource
    private AccessoriesPurchaseDetailMapper accessoriesPurchaseDetailMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private AccessoriesMapper accessoriesMapper;

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
        LambdaQueryWrapper<AccessoriesPurchase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(AccessoriesPurchase::getPurchaseNo, format1);
        wrapper.like(AccessoriesPurchase::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(AccessoriesPurchase::getCreatedAt).last("limit 1");
        AccessoriesPurchase accessoriesPurchase = accessoriesPurchaseMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("flcg");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (accessoriesPurchase != null) {
            String orderNo = accessoriesPurchase.getPurchaseNo();
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
     * 原片采购查询
     *
     * @param accessoriesPurchase
     */
    @Override
    public CommonResult<List<AccessoriesPurchase>> queryPurchaseData(AccessoriesPurchase accessoriesPurchase) {
        LambdaQueryWrapper<AccessoriesPurchase> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(accessoriesPurchase.getPurchaseNo()))
            wrapper.eq(AccessoriesPurchase::getPurchaseNo, accessoriesPurchase.getPurchaseNo());
        if (StringUtils.isNotEmpty(accessoriesPurchase.getStatus()))
            wrapper.eq(AccessoriesPurchase::getStatus, accessoriesPurchase.getStatus());
        if (StringUtils.isNotEmpty(accessoriesPurchase.getPurchasePerson()))
            wrapper.like(AccessoriesPurchase::getPurchasePerson, accessoriesPurchase.getPurchasePerson());
        if (StringUtils.isNotEmpty(accessoriesPurchase.getMillName()))
            wrapper.like(AccessoriesPurchase::getMillName, accessoriesPurchase.getMillName());
        if (StringUtils.isNotEmpty(accessoriesPurchase.getPurchaseDateStart()) && StringUtils.isNotEmpty(accessoriesPurchase.getPurchaseDateEnd()))
            wrapper.between(AccessoriesPurchase::getPurchaseDate, accessoriesPurchase.getPurchaseDateStart(), accessoriesPurchase.getPurchaseDateEnd());
        wrapper.orderByDesc(AccessoriesPurchase::getPurchaseNo);
        Integer integer = accessoriesPurchaseMapper.selectCount(wrapper);
        PageHelper.startPage(accessoriesPurchase.getPageNum(), accessoriesPurchase.getPageSize());
        List<AccessoriesPurchase> accessoriesPurchases = accessoriesPurchaseMapper.selectList(wrapper);
        PageInfo<AccessoriesPurchase> in = new PageInfo<>(accessoriesPurchases, accessoriesPurchase.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, in.getList());
    }

    /**
     * 查看明细
     *
     * @param id 采购id
     */
    @Override
    public CommonResult<List<AccessoriesPurchase>> viewDetail(String id) {
        List<AccessoriesPurchaseDetail> accessoriesPurchaseDetails = accessoriesPurchaseDetailMapper.viewDetail(new String[]{id});
        for (AccessoriesPurchaseDetail accessoriesPurchaseDetail : accessoriesPurchaseDetails) {
            accessoriesPurchaseDetail.setOperationNum(accessoriesPurchaseDetail.getPurchaseNum());
        }
        return CommonResult.success(accessoriesPurchaseDetails);
    }

    /**
     * 原片采购新增
     *
     * @param accessoriesPurchase
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult addPurchaseData(AccessoriesPurchase accessoriesPurchase) {
        try {
            //获取采购明细
            List<Accessories> accessoriesList = accessoriesPurchase.getAccessoriesList();
            String purchaseId = UUID.randomUUID().toString();
            int totalNum = 0;
            Double totalAmount = 0.00;
            //遍历采购明细
            for (Accessories accessories : accessoriesList) {
                AccessoriesPurchaseDetail one = new AccessoriesPurchaseDetail();
                one.setPurchaseId(purchaseId);
                one.setPurchaseNo(accessoriesPurchase.getPurchaseNo());
                one.setAccessoriesId(accessories.getId());
                one.setAccessoriesNo(accessories.getAccessoriesNo());
                one.setAccessoriesName(accessories.getAccessoriesName());
                one.setPurchasePrice(accessories.getReferencePrice());
                one.setPurchaseNum(accessories.getOperationNum());
                one.setPurchaseAmount(accessories.getOperationNum() * accessories.getReferencePrice());
                totalNum += one.getPurchaseNum();
                totalAmount += (accessories.getOperationNum() * accessories.getReferencePrice());
                one.setPurchaseCompany(accessories.getAccessoriesMill());
                accessoriesPurchaseDetailMapper.insert(one);
            }
            accessoriesPurchase.setId(purchaseId);
            accessoriesPurchase.setPurchaseNum(totalNum);
            accessoriesPurchase.setCreatedAt(new Date());
            accessoriesPurchase.setPurchaseAmount(totalAmount);
            accessoriesPurchase.setStatus("未入库");
            accessoriesPurchase.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            accessoriesPurchaseMapper.insert(accessoriesPurchase);
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 查询编辑明细
     *
     * @param id 采购id
     */
    @Override
    public CommonResult<List<Accessories>> updPurchaseDataQuery(String id) {
        List<AccessoriesPurchaseDetail> accessoriesPurchaseDetails = accessoriesPurchaseDetailMapper.selectList(Wrappers.<AccessoriesPurchaseDetail>query().lambda().eq(AccessoriesPurchaseDetail::getPurchaseId, id));
        Set<String> set = accessoriesPurchaseDetails.stream().collect(Collectors.groupingBy(AccessoriesPurchaseDetail::getAccessoriesId)).keySet();
        List<Accessories> accessories = accessoriesMapper.selectList(Wrappers.<Accessories>query().lambda().in(Accessories::getId, set));
        for (Accessories accessory : accessories) {
            for (AccessoriesPurchaseDetail accessoriesPurchaseDetail : accessoriesPurchaseDetails) {
                if (accessory.getId().equals(accessoriesPurchaseDetail.getAccessoriesId())) {
                    accessory.setOperationNum(accessoriesPurchaseDetail.getPurchaseNum());
                    accessory.setReferencePrice(accessoriesPurchaseDetail.getPurchasePrice());
                }
            }
        }
        return CommonResult.success(accessories);
    }

    /**
     * 原片采购编辑
     *
     * @param accessoriesPurchase
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updPurchaseData(AccessoriesPurchase accessoriesPurchase) {
        try {
            //获取采购明细
            List<Accessories> accessoriesList = accessoriesPurchase.getAccessoriesList();
            int totalNum = 0;
            Double totalAmount = 0.00;
            //删除旧明细
            accessoriesPurchaseDetailMapper.delete(Wrappers.<AccessoriesPurchaseDetail>query().lambda().eq(AccessoriesPurchaseDetail::getPurchaseId, accessoriesPurchase.getId()));
            //遍历采购明细
            for (Accessories accessories : accessoriesList) {
                AccessoriesPurchaseDetail one = new AccessoriesPurchaseDetail();
                one.setPurchaseId(accessoriesPurchase.getId());
                one.setPurchaseNo(accessoriesPurchase.getPurchaseNo());
                one.setAccessoriesId(accessories.getId());
                one.setAccessoriesName(accessories.getAccessoriesName());
                one.setPurchasePrice(accessories.getReferencePrice());
                one.setPurchaseNum(accessories.getOperationNum());
                one.setPurchaseAmount(accessories.getOperationNum() * accessories.getReferencePrice());
                totalNum += one.getPurchaseNum();
                totalAmount += (accessories.getOperationNum() * accessories.getReferencePrice());
                one.setPurchaseCompany(accessories.getAccessoriesMill());
                accessoriesPurchaseDetailMapper.insert(one);
            }
            accessoriesPurchase.setPurchaseAmount(totalAmount);
            accessoriesPurchase.setPurchaseNum(totalNum);
            accessoriesPurchase.setUpdatedAt(new Date());
            accessoriesPurchase.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            accessoriesPurchaseMapper.updateById(accessoriesPurchase);
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    /**
     * 原片采购删除
     *
     * @param ids 采购id
     * @return
     */
    @Override
    public CommonResult delPurchaseData(String ids) {
        String[] split = ids.split(",");
        accessoriesPurchaseDetailMapper.delete(Wrappers.<AccessoriesPurchaseDetail>query().lambda().in(AccessoriesPurchaseDetail::getPurchaseId, split));
        accessoriesPurchaseMapper.delete(Wrappers.<AccessoriesPurchase>query().lambda().in(AccessoriesPurchase::getId, split));
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 导出原片采购单
     *
     * @param ids 采购id集合
     */
    @Override
    public void exportPurchase(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<AccessoriesPurchase> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(AccessoriesPurchase::getId, split);
        }
        wrapper.orderByDesc(AccessoriesPurchase::getPurchaseNo);
        List<AccessoriesPurchase> deliverRecords = accessoriesPurchaseMapper.selectList(wrapper);
        ExcelUtil<AccessoriesPurchase> util = new ExcelUtil<AccessoriesPurchase>(AccessoriesPurchase.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }

    /**
     * 导出原片采购明细
     *
     * @param response
     * @param ids      采购id集合
     */
    @Override
    public void exportPurchaseData(HttpServletResponse response, String ids) {
        List<AccessoriesPurchaseDetail> deliverRecords = null;
        if (StringUtils.isNotEmpty(ids)) {
            deliverRecords = accessoriesPurchaseDetailMapper.viewDetail(ids.split(","));
        }else {
            deliverRecords = accessoriesPurchaseDetailMapper.viewDetail(null);
        }
        ExcelUtil<AccessoriesPurchaseDetail> util = new ExcelUtil<AccessoriesPurchaseDetail>(AccessoriesPurchaseDetail.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }
}
