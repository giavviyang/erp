package com.erp.original.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.original.*;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.original.domain.*;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.original.service.YpWarehouseOperationService;
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
 * @date： 2022-09-13 13:47
 */
@Service
public class YpWarehouseOperationServiceImpl extends ServiceImpl<YpWarehouseOperationMapper, YpWarehouseOperation> implements YpWarehouseOperationService {
    @Resource
    private YpWarehouseOperationMapper ypWarehouseOperationMapper;
    @Resource
    private YpOperationDetailMapper ypOperationDetailMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private OriginalFilmMapper originalFilmMapper;
    @Resource
    private OriginalFilmPurchaseDetailMapper originalFilmPurchaseDetailMapper;
    @Resource
    private OriginalFilmPurchaseMapper originalFilmPurchaseMapper;


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
        LambdaQueryWrapper<YpWarehouseOperation> wrapper = new LambdaQueryWrapper<>();
        if (type == 0) {
            wrapper.eq(YpWarehouseOperation::getOperationType, "入库");
            sysNumberingRules = sysNumberingRulesMapper.selectById("15");
        }else if (type == 1) {
            wrapper.eq(YpWarehouseOperation::getOperationType, "出库");
            sysNumberingRules = sysNumberingRulesMapper.selectById("16");
        }else {
            wrapper.eq(YpWarehouseOperation::getOperationType, "盘库");
            sysNumberingRules = sysNumberingRulesMapper.selectById("17");
        }
        wrapper.like(YpWarehouseOperation::getOperationNo, format1);
        wrapper.like(YpWarehouseOperation::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(YpWarehouseOperation::getCreatedAt).last("limit 1");
        YpWarehouseOperation orderRecord = ypWarehouseOperationMapper.selectOne(wrapper);
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
     * 原片入库
     *
     * @param ypWarehouseOperation
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult originalWarehouse(YpWarehouseOperation ypWarehouseOperation) {
        try {
            //获取入库明细
            List<OriginalFilm> originalFilmList = ypWarehouseOperation.getOriginalFilmList();
            String id = UUID.randomUUID().toString();
            Integer totalNum = 0;
            Double totalArea = 0.000;
            Double totalAmount = 0.000;
            //遍历明细
            for (OriginalFilm originalFilm : originalFilmList) {
                if (originalFilm != null) {
                    YpOperationDetail one = new YpOperationDetail();
                    one.setOperationId(id);
                    one.setOriginalFilmId(originalFilm.getId());
                    one.setOriginalFilmName(originalFilm.getOriginalName());
                    one.setOperationNum(originalFilm.getOperationNum());
                    one.setOperationPrice(originalFilm.getReferencePrice());
                    Double area = (double) originalFilm.getOriginalWidth() * originalFilm.getOriginalLong() / 1000000 * originalFilm.getOperationNum();
                    one.setOperationArea(area);
                    ypOperationDetailMapper.insert(one);
                    totalNum += originalFilm.getOperationNum();
                    totalArea += area;
                    totalAmount += (originalFilm.getOperationNum() * originalFilm.getReferencePrice());
                    //修改仓库总数
                    if (ypWarehouseOperation.getOperationType().equals("入库")) {
                        originalFilm.setStock(originalFilm.getStock() + originalFilm.getOperationNum());
                    } else if (ypWarehouseOperation.getOperationType().equals("出库")) {
                        originalFilm.setStock(originalFilm.getStock() - originalFilm.getOperationNum());
                    } else {//盘库
                        originalFilm.setStock(originalFilm.getOperationNum());
                    }
                    originalFilmMapper.updateById(originalFilm);
                }
            }
            ypWarehouseOperation.setId(id);
            ypWarehouseOperation.setOriginalNum(totalNum);
            ypWarehouseOperation.setOriginalArea(totalArea);
            ypWarehouseOperation.setTotalAmount(totalAmount);
            ypWarehouseOperation.setCreatedAt(new Date());
            ypWarehouseOperation.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            ypWarehouseOperationMapper.insert(ypWarehouseOperation);
            return CommonResult.success(ResultConstants.CONSERVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.CONSERVE_SUCCESS);
        }
    }

    /**
     * 采购单入库
     *
     * @param ypWarehouseOperation 入库记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult purchaseWarehouse(YpWarehouseOperation ypWarehouseOperation) {
        try {
            //获取发货单明细
            List<OriginalFilmPurchase> originalFilmPurchaseList = ypWarehouseOperation.getOriginalFilmPurchaseList();
            //获取采购id集合
            Set<String> set = originalFilmPurchaseList.stream().collect(Collectors.groupingBy(OriginalFilmPurchase::getId)).keySet();
            //获取采购明细
            List<OriginalFilmPurchaseDetail> originalFilmPurchaseDetails = originalFilmPurchaseDetailMapper.selectList(Wrappers.<OriginalFilmPurchaseDetail>query().lambda().in(OriginalFilmPurchaseDetail::getPurchaseId, set));
            String id = UUID.randomUUID().toString();
            Integer totalNum = 0;
            Double totalArea = 0.000;
            Double totalAmount = 0.000;
            //遍历发货单明细
            for (OriginalFilmPurchaseDetail originalFilmPurchaseDetail : originalFilmPurchaseDetails) {
                YpOperationDetail one = new YpOperationDetail();
                one.setOperationId(id);
                one.setOriginalFilmId(originalFilmPurchaseDetail.getOriginalFilmId());
                one.setPurchaseId(originalFilmPurchaseDetail.getPurchaseId());
                one.setPurchaseNo(originalFilmPurchaseDetail.getPurchaseNo());
                one.setOriginalFilmName(originalFilmPurchaseDetail.getOriginalFilmName());
                one.setOperationNum(originalFilmPurchaseDetail.getPurchaseNum());
                one.setOperationArea(originalFilmPurchaseDetail.getPurchaseArea());
                totalArea += originalFilmPurchaseDetail.getPurchaseArea();
                totalNum += originalFilmPurchaseDetail.getPurchaseNum();
                totalAmount += originalFilmPurchaseDetail.getPurchaseAmount();
                //新增明细
                ypOperationDetailMapper.insert(one);
                //修改原片仓库数量
                OriginalFilm originalFilm = originalFilmMapper.selectById(originalFilmPurchaseDetail.getOriginalFilmId());
                originalFilm.setStock(originalFilm.getStock() + one.getOperationNum());
                originalFilmMapper.updateById(originalFilm);
            }
            //修改采购单入库状态
            originalFilmPurchaseMapper.update(null, Wrappers.<OriginalFilmPurchase>update().lambda().set(OriginalFilmPurchase::getStatus, "已入库").in(OriginalFilmPurchase::getId, set));
            ypWarehouseOperation.setId(id);
            ypWarehouseOperation.setOriginalNum(totalNum);
            ypWarehouseOperation.setOperationType("入库");
            ypWarehouseOperation.setOriginalArea(totalArea);
            ypWarehouseOperation.setTotalAmount(totalAmount);
            ypWarehouseOperation.setCreatedAt(new Date());
            ypWarehouseOperation.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            ypWarehouseOperationMapper.insert(ypWarehouseOperation);
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
     * @param ypWarehouseOperation 出入盘库记录
     */
    @Override
    public CommonResult<List<YpWarehouseOperation>> warehouseRecord(YpWarehouseOperation ypWarehouseOperation) {
        LambdaQueryWrapper<YpWarehouseOperation> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(ypWarehouseOperation.getOperationNo()))
            wrapper.eq(YpWarehouseOperation::getOperationNo, ypWarehouseOperation.getOperationNo());
        if (StringUtils.isNotEmpty(ypWarehouseOperation.getOperationType()))
            wrapper.eq(YpWarehouseOperation::getOperationType, ypWarehouseOperation.getOperationType());
        if (StringUtils.isNotEmpty(ypWarehouseOperation.getPerson()))
            wrapper.eq(YpWarehouseOperation::getPerson, ypWarehouseOperation.getPerson());
        if (StringUtils.isNotEmpty(ypWarehouseOperation.getMillId()))
            wrapper.like(YpWarehouseOperation::getMillId, ypWarehouseOperation.getMillId());
        if (StringUtils.isNotEmpty(ypWarehouseOperation.getOperationDateStart()) && StringUtils.isNotEmpty(ypWarehouseOperation.getOperationDateEnd()))
            wrapper.between(YpWarehouseOperation::getOperationDate, ypWarehouseOperation.getOperationDateStart(), ypWarehouseOperation.getOperationDateEnd());
        wrapper.eq(YpWarehouseOperation::getOperationType, ypWarehouseOperation.getOperationType());
        wrapper.orderByDesc(YpWarehouseOperation::getOperationNo);
        Integer integer = ypWarehouseOperationMapper.selectCount(wrapper);
        PageHelper.startPage(ypWarehouseOperation.getPageNum(), ypWarehouseOperation.getPageSize());
        List<YpWarehouseOperation> ypWarehouseOperations = ypWarehouseOperationMapper.selectList(wrapper);
        PageInfo<YpWarehouseOperation> info = new PageInfo<>(ypWarehouseOperations, ypWarehouseOperation.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, info.getList());
    }

    /**
     * 查看出入盘库明细
     *
     * @param id 出入盘库id
     */
    @Override
    public CommonResult<List<YpOperationDetail>> warehouseDetailsView(String id) {
        return CommonResult.success(ypOperationDetailMapper.selectByOperationId(new String[]{id}, null));
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
            List<YpOperationDetail> ypOperationDetails = ypOperationDetailMapper.selectList(Wrappers.<YpOperationDetail>query().lambda().in(YpOperationDetail::getOperationId, split));
            Map<String, List<YpOperationDetail>> collect = ypOperationDetails.stream().collect(Collectors.groupingBy(YpOperationDetail::getOriginalFilmId));
            //获取原片
            List<OriginalFilm> originalFilms = originalFilmMapper.selectList(Wrappers.<OriginalFilm>query().lambda().in(OriginalFilm::getId, collect.keySet()));
            //修改仓库数量
            for (OriginalFilm originalFilm : originalFilms) {
                for (Map.Entry<String, List<YpOperationDetail>> stringListEntry : collect.entrySet()) {
                    if (originalFilm.getId().equals(stringListEntry.getKey())) {
                        int sum = stringListEntry.getValue().stream().mapToInt(YpOperationDetail::getOperationNum).sum();
                        if (type == 0) {
                            originalFilm.setStock(originalFilm.getStock() - sum);
                        } else {
                            originalFilm.setStock(originalFilm.getStock() + sum);
                        }
                        originalFilmMapper.updateById(originalFilm);
                    }
                }
            }
            //删除入库记录
            ypWarehouseOperationMapper.delete(Wrappers.<YpWarehouseOperation>query().lambda().in(YpWarehouseOperation::getId, split));
            //删除明细
            ypOperationDetailMapper.delete(Wrappers.<YpOperationDetail>query().lambda().in(YpOperationDetail::getOperationId, split));
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
            List<YpOperationDetail> ypOperationDetails = ypOperationDetailMapper.selectList(Wrappers.<YpOperationDetail>query().lambda().eq(YpOperationDetail::getOperationId, id));
            Map<String, List<YpOperationDetail>> collect = ypOperationDetails.stream().collect(Collectors.groupingBy(YpOperationDetail::getOriginalFilmId));
            //获取原片
            List<OriginalFilm> originalFilms = originalFilmMapper.selectList(Wrappers.<OriginalFilm>query().lambda().in(OriginalFilm::getId, collect.keySet()));
            //修改仓库数量
            for (OriginalFilm originalFilm : originalFilms) {
                for (YpOperationDetail ypOperationDetail : ypOperationDetails) {
                    if (originalFilm.getId().equals(ypOperationDetail.getOriginalFilmId())) {
                        originalFilm.setStock(originalFilm.getStock() - ypOperationDetail.getOperationNum());
                        originalFilmMapper.updateById(originalFilm);
                    }
                }
            }
            //删除入库记录
            ypWarehouseOperationMapper.delete(Wrappers.<YpWarehouseOperation>query().lambda().eq(YpWarehouseOperation::getId, id));
            //删除明细
            ypOperationDetailMapper.delete(Wrappers.<YpOperationDetail>query().lambda().eq(YpOperationDetail::getOperationId, id));
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
        LambdaQueryWrapper<YpWarehouseOperation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YpWarehouseOperation::getOperationType, type);
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(YpWarehouseOperation::getId, split);
        }
        wrapper.orderByDesc(YpWarehouseOperation::getOperationNo);
        List<YpWarehouseOperation> deliverRecords = ypWarehouseOperationMapper.selectList(wrapper);
        ExcelUtil<YpWarehouseOperation> util = new ExcelUtil<YpWarehouseOperation>(YpWarehouseOperation.class);
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
        List<YpOperationDetail> deliverRecords = null;
        if (StringUtils.isNotEmpty(ids)) {
            deliverRecords = ypOperationDetailMapper.selectByOperationId(ids.split(","), type);
        }else {
            deliverRecords = ypOperationDetailMapper.selectByOperationId(null, type);
        }
        ExcelUtil<YpOperationDetail> util = new ExcelUtil<YpOperationDetail>(YpOperationDetail.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }

}
