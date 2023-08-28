package com.erp.accessories.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.accessories.service.AccessoriesOperationService;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.accessories.*;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.accessories.domain.*;
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
 * @date： 2022-09-15 10:02
 */
@Service
public class AccessoriesOperationServiceImpl extends ServiceImpl<AccessoriesOperationMapper, AccessoriesOperation> implements AccessoriesOperationService {

    @Resource
    private AccessoriesOperationMapper accessoriesOperationMapper;
    @Resource
    private AccessoriesOperationDetailMapper accessoriesOperationDetailMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private AccessoriesMapper accessoriesMapper;
    @Resource
    private AccessoriesPurchaseDetailMapper accessoriesPurchaseDetailMapper;
    @Resource
    private AccessoriesPurchaseMapper accessoriesPurchaseMapper;


    /**
     * 生成入库编号
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
        LambdaQueryWrapper<AccessoriesOperation> wrapper = new LambdaQueryWrapper<>();
        if (type == 0) {
            wrapper.eq(AccessoriesOperation::getOperationType, "入库");
            sysNumberingRules = sysNumberingRulesMapper.selectById("flrk");
        } else if (type == 1) {
            wrapper.eq(AccessoriesOperation::getOperationType, "出库");
            sysNumberingRules = sysNumberingRulesMapper.selectById("flck");
        } else {
            wrapper.eq(AccessoriesOperation::getOperationType, "盘库");
            sysNumberingRules = sysNumberingRulesMapper.selectById("flpk");
        }
        wrapper.like(AccessoriesOperation::getOperationNo, format1);
        wrapper.like(AccessoriesOperation::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(AccessoriesOperation::getCreatedAt).last("limit 1");
        AccessoriesOperation orderRecord = accessoriesOperationMapper.selectOne(wrapper);
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
     * 辅料入库
     *
     * @param accessoriesOperation
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult accessoriesWarehouse(AccessoriesOperation accessoriesOperation) {
        try {
            //获取入库明细
            List<Accessories> accessoriesList = accessoriesOperation.getAccessoriesList();
            String id = UUID.randomUUID().toString();
            Integer totalNum = 0;
            Double totalAmount = 0.00;
            //遍历明细
            for (Accessories accessories : accessoriesList) {
                AccessoriesOperationDetail one = new AccessoriesOperationDetail();
                one.setOperationId(id);
                one.setOperationNo(accessories.getAccessoriesNo());
                one.setAccessoriesId(accessories.getId());
                one.setAccessoriesName(accessories.getAccessoriesName());
                one.setOperationNum(accessories.getOperationNum());
                one.setOperationPrice(accessories.getReferencePrice());
                accessoriesOperationDetailMapper.insert(one);
                totalNum += accessories.getOperationNum();
                totalAmount += (accessories.getOperationNum() * accessories.getReferencePrice());
                //修改仓库总数
                if (accessoriesOperation.getOperationType().equals("入库")) {
                    accessories.setStock(accessories.getStock() + accessories.getOperationNum());
                } else if (accessoriesOperation.getOperationType().equals("出库")) {
                    accessories.setStock(accessories.getStock() - accessories.getOperationNum());
                } else {//盘库
                    accessories.setStock(accessories.getOperationNum());
                }
                accessoriesMapper.updateById(accessories);
            }
            accessoriesOperation.setId(id);
            accessoriesOperation.setOriginalNum(totalNum);
            accessoriesOperation.setTotalAmount(totalAmount);
            accessoriesOperation.setCreatedAt(new Date());
            accessoriesOperation.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            accessoriesOperationMapper.insert(accessoriesOperation);
            return CommonResult.success(ResultConstants.CONSERVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.CONSERVE_ERROR);
        }
    }

    /**
     * 采购单入库
     *
     * @param accessoriesOperation 入库记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult purchaseWarehouse(AccessoriesOperation accessoriesOperation) {
        try {
            //获取发货单明细
            List<AccessoriesPurchase> accessoriesPurchaseList = accessoriesOperation.getAccessoriesPurchaseList();
            //获取采购id集合
            Set<String> set = accessoriesPurchaseList.stream().collect(Collectors.groupingBy(AccessoriesPurchase::getId)).keySet();
            //获取采购明细
            List<AccessoriesPurchaseDetail> accessoriesPurchaseDetails = accessoriesPurchaseDetailMapper.selectList(Wrappers.<AccessoriesPurchaseDetail>query().lambda().in(AccessoriesPurchaseDetail::getPurchaseId, set));
            String id = UUID.randomUUID().toString();
            Integer totalNum = 0;
            Double totalAmount = 0.00;
            //遍历发货单明细
            for (AccessoriesPurchaseDetail accessoriesPurchaseDetail : accessoriesPurchaseDetails) {
                AccessoriesOperationDetail one = new AccessoriesOperationDetail();
                one.setOperationId(id);
                one.setOperationNo(accessoriesPurchaseDetail.getAccessoriesNo());
                one.setOperationPrice(accessoriesPurchaseDetail.getPurchasePrice());
                one.setAccessoriesId(accessoriesPurchaseDetail.getAccessoriesId());
                one.setPurchaseId(accessoriesPurchaseDetail.getPurchaseId());
                one.setPurchaseNo(accessoriesPurchaseDetail.getPurchaseNo());
                one.setAccessoriesName(accessoriesPurchaseDetail.getAccessoriesName());
                one.setOperationNum(accessoriesPurchaseDetail.getPurchaseNum());
                totalNum += accessoriesPurchaseDetail.getPurchaseNum();
                totalAmount += (accessoriesPurchaseDetail.getPurchaseNum() * accessoriesPurchaseDetail.getPurchasePrice());
                //新增明细
                accessoriesOperationDetailMapper.insert(one);
                //修改辅料仓库数量
                Accessories accessories = accessoriesMapper.selectById(accessoriesPurchaseDetail.getAccessoriesId());
                accessories.setStock(accessories.getStock() + one.getOperationNum());
                accessoriesMapper.updateById(accessories);
            }
            //修改采购单状态
            accessoriesPurchaseMapper.update(null, Wrappers.<AccessoriesPurchase>update().lambda().set(AccessoriesPurchase::getStatus, "已入库").in(AccessoriesPurchase::getId, set));
            accessoriesOperation.setId(id);
            accessoriesOperation.setOperationType("入库");
            accessoriesOperation.setTotalAmount(totalAmount);
            accessoriesOperation.setTotalAmount(totalAmount);
            accessoriesOperation.setOriginalNum(totalNum);
            accessoriesOperation.setCreatedAt(new Date());
            accessoriesOperation.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            accessoriesOperationMapper.insert(accessoriesOperation);
            return CommonResult.success(ResultConstants.RK_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.RK_ERROR);
        }
    }

    /**
     * 查询出入盘库记录
     *
     * @param accessoriesOperation 出入盘库记录
     */
    @Override
    public CommonResult<List<AccessoriesOperation>> warehouseRecord(AccessoriesOperation accessoriesOperation) {
        LambdaQueryWrapper<AccessoriesOperation> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(accessoriesOperation.getOperationNo()))
            wrapper.eq(AccessoriesOperation::getOperationNo, accessoriesOperation.getOperationNo());
        if (StringUtils.isNotEmpty(accessoriesOperation.getPerson()))
            wrapper.like(AccessoriesOperation::getPerson, accessoriesOperation.getPerson());
        if (StringUtils.isNotEmpty(accessoriesOperation.getMillId()))
            wrapper.like(AccessoriesOperation::getMillId, accessoriesOperation.getMillId());
        if (StringUtils.isNotEmpty(accessoriesOperation.getOperationType()))
            wrapper.like(AccessoriesOperation::getOperationType, accessoriesOperation.getOperationType());
        if (StringUtils.isNotEmpty(accessoriesOperation.getOperationDateStart()) && StringUtils.isNotEmpty(accessoriesOperation.getOperationDateEnd()))
            wrapper.between(AccessoriesOperation::getOperationDate, accessoriesOperation.getOperationDateStart(), accessoriesOperation.getOperationDateEnd());
        wrapper.orderByDesc(AccessoriesOperation::getOperationNo);
        Integer integer = accessoriesOperationMapper.selectCount(wrapper);
        PageHelper.startPage(accessoriesOperation.getPageNum(), accessoriesOperation.getPageSize());
        List<AccessoriesOperation> accessoriesOperations = accessoriesOperationMapper.selectList(wrapper);
        PageInfo<AccessoriesOperation> info = new PageInfo<>(accessoriesOperations, accessoriesOperation.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, info.getList());
    }

    /**
     * 查看出入盘库明细
     *
     * @param id 出入盘库id
     */
    @Override
    public CommonResult<List<AccessoriesOperationDetail>> warehouseDetailsView(String id) {
        return CommonResult.success(accessoriesOperationDetailMapper.selectByOperationId(new String[]{id}, null));
    }

    /**
     * 删除出入库记录
     *
     * @param ids 出入库ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delWarehouseDetails(String ids, Integer type) {
        try {
            String[] split = ids.split(",");
            List<AccessoriesOperationDetail> ypOperationDetails = accessoriesOperationDetailMapper.selectList(Wrappers.<AccessoriesOperationDetail>query().lambda().in(AccessoriesOperationDetail::getOperationId, split));
            Map<String, List<AccessoriesOperationDetail>> collect = ypOperationDetails.stream().collect(Collectors.groupingBy(AccessoriesOperationDetail::getAccessoriesId));
            //获取辅料
            List<Accessories> accessoriess = accessoriesMapper.selectList(Wrappers.<Accessories>query().lambda().in(Accessories::getId, collect.keySet()));
            //修改仓库数量
            for (Accessories accessories : accessoriess) {
                for (Map.Entry<String, List<AccessoriesOperationDetail>> stringListEntry : collect.entrySet()) {
                    if (accessories.getId().equals(stringListEntry.getKey())) {
                        int sum = stringListEntry.getValue().stream().mapToInt(AccessoriesOperationDetail::getOperationNum).sum();
                        if (type == 0) {
                            accessories.setStock(accessories.getStock() - sum);
                        } else {
                            accessories.setStock(accessories.getStock() + sum);
                        }
                        accessoriesMapper.updateById(accessories);
                    }
                }
            }
            //删除入库记录
            accessoriesOperationMapper.delete(Wrappers.<AccessoriesOperation>query().lambda().in(AccessoriesOperation::getId, split));
            //删除明细
            accessoriesOperationDetailMapper.delete(Wrappers.<AccessoriesOperationDetail>query().lambda().in(AccessoriesOperationDetail::getOperationId, split));
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 删除盘库记录
     *
     * @param id 盘库id
     */
    @Override
    public CommonResult delInventoryDetails(String id) {
        try {
            List<AccessoriesOperationDetail> ypOperationDetails = accessoriesOperationDetailMapper.selectList(Wrappers.<AccessoriesOperationDetail>query().lambda().eq(AccessoriesOperationDetail::getOperationId, id));
            Map<String, List<AccessoriesOperationDetail>> collect = ypOperationDetails.stream().collect(Collectors.groupingBy(AccessoriesOperationDetail::getAccessoriesId));
            //获取辅料
            List<Accessories> accessoriess = accessoriesMapper.selectList(Wrappers.<Accessories>query().lambda().in(Accessories::getId, collect.keySet()));
            //修改仓库数量
            for (Accessories accessories : accessoriess) {
                for (AccessoriesOperationDetail ypOperationDetail : ypOperationDetails) {
                    if (accessories.getId().equals(ypOperationDetail.getAccessoriesId())){
                        accessories.setStock(accessories.getStock() - ypOperationDetail.getOperationNum());
                        accessoriesMapper.updateById(accessories);
                    }
                }
            }
            //删除盘库记录
            accessoriesOperationMapper.delete(Wrappers.<AccessoriesOperation>query().lambda().eq(AccessoriesOperation::getId, id));
            //删除明细
            accessoriesOperationDetailMapper.delete(Wrappers.<AccessoriesOperationDetail>query().lambda().eq(AccessoriesOperationDetail::getOperationId, id));
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 导出出入盘库记录
     *
     * @param response
     * @param ids      id集合
     */
    @Override
    public void exportWarehouse(HttpServletResponse response, String ids, String type) {
        LambdaQueryWrapper<AccessoriesOperation> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(AccessoriesOperation::getId, split);
        }
        wrapper.eq(AccessoriesOperation::getOperationType, type);
        wrapper.orderByDesc(AccessoriesOperation::getOperationNo);
        List<AccessoriesOperation> deliverRecords = accessoriesOperationMapper.selectList(wrapper);
        ExcelUtil<AccessoriesOperation> util = new ExcelUtil<AccessoriesOperation>(AccessoriesOperation.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }

    /**
     * 导出出入盘库明细
     *
     * @param response
     * @param ids      id集合
     */
    @Override
    public void exportWarehouseDetails(HttpServletResponse response, String ids, String type) {
        List<AccessoriesOperationDetail> deliverRecords = null;
        if (StringUtils.isNotEmpty(ids)) {
            deliverRecords = accessoriesOperationDetailMapper.selectByOperationId(ids.split(","), type);
        }else {
            deliverRecords = accessoriesOperationDetailMapper.selectByOperationId(null, type);
        }
        ExcelUtil<AccessoriesOperationDetail> util = new ExcelUtil<AccessoriesOperationDetail>(AccessoriesOperationDetail.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }

}
