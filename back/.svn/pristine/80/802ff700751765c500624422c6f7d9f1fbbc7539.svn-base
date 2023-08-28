package com.erp.original.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.MapUtil;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.original.OriginalFilmMapper;
import com.erp.floor.mapper.original.OriginalFilmPurchaseDetailMapper;
import com.erp.floor.mapper.original.OriginalFilmPurchaseMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.original.domain.OriginalFilm;
import com.erp.floor.pojo.original.domain.OriginalFilmPurchase;
import com.erp.floor.pojo.original.domain.OriginalFilmPurchaseDetail;
import com.erp.floor.pojo.sales.domain.DeliverRecord;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.original.service.OriginalFilmPurchaseService;
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
 * @date： 2022-09-09 12:28
 */
@Service
public class OriginalFilmPurchaseServiceImpl extends ServiceImpl<OriginalFilmPurchaseMapper, OriginalFilmPurchase> implements OriginalFilmPurchaseService {

    @Resource
    private OriginalFilmPurchaseMapper originalFilmPurchaseMapper;
    @Resource
    private OriginalFilmPurchaseDetailMapper originalFilmPurchaseDetailMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private OriginalFilmMapper originalFilmMapper;

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
        LambdaQueryWrapper<OriginalFilmPurchase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(OriginalFilmPurchase::getPurchaseNo, format1);
        wrapper.like(OriginalFilmPurchase::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(OriginalFilmPurchase::getCreatedAt).last("limit 1");
        OriginalFilmPurchase originalFilmPurchase = originalFilmPurchaseMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("ypcg");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (originalFilmPurchase != null) {
            String orderNo = originalFilmPurchase.getPurchaseNo();
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
     * 原片采购查询
     *
     * @param originalFilmPurchase
     */
    @Override
    public CommonResult<List<OriginalFilmPurchase>> queryPurchaseData(OriginalFilmPurchase originalFilmPurchase) {
        LambdaQueryWrapper<OriginalFilmPurchase> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(originalFilmPurchase.getPurchaseNo()))
            wrapper.eq(OriginalFilmPurchase::getPurchaseNo, originalFilmPurchase.getPurchaseNo());
        if (StringUtils.isNotEmpty(originalFilmPurchase.getStatus()))
            wrapper.eq(OriginalFilmPurchase::getStatus, originalFilmPurchase.getStatus());
        if (StringUtils.isNotEmpty(originalFilmPurchase.getPurchasePerson()))
            wrapper.like(OriginalFilmPurchase::getPurchasePerson, originalFilmPurchase.getPurchasePerson());
        if (StringUtils.isNotEmpty(originalFilmPurchase.getMillName()))
            wrapper.like(OriginalFilmPurchase::getMillName, originalFilmPurchase.getMillName());
        if (StringUtils.isNotEmpty(originalFilmPurchase.getPurchaseDateStart()) && StringUtils.isNotEmpty(originalFilmPurchase.getPurchaseDateEnd()))
            wrapper.between(OriginalFilmPurchase::getPurchaseDate, originalFilmPurchase.getPurchaseDateStart(), originalFilmPurchase.getPurchaseDateEnd());
        wrapper.orderByDesc(OriginalFilmPurchase::getPurchaseNo);
        Integer integer = originalFilmPurchaseMapper.selectCount(wrapper);
        PageHelper.startPage(originalFilmPurchase.getPageNum(), originalFilmPurchase.getPageSize());
        List<OriginalFilmPurchase> originalFilmPurchases = originalFilmPurchaseMapper.selectList(wrapper);
        PageInfo<OriginalFilmPurchase> in = new PageInfo<>(originalFilmPurchases, originalFilmPurchase.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, in.getList());
    }

    /**
     * 查看明细
     *
     * @param id 采购id
     */
    @Override
    public CommonResult<List<OriginalFilmPurchase>> viewDetail(String id) {
        return CommonResult.success(originalFilmPurchaseDetailMapper.viewDetail(new String[]{id}));
    }

    /**
     * 原片采购新增
     *
     * @param originalFilmPurchase
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult addPurchaseData(OriginalFilmPurchase originalFilmPurchase) {
        try {
            //获取采购明细
            List<OriginalFilm> originalFilmList = originalFilmPurchase.getOriginalFilmList();
            String purchaseId = UUID.randomUUID().toString();
            int totalNum = 0;
            double totalArea = 0.000;
            double totalAmout = 0.000;
            //遍历采购明细
            for (OriginalFilm originalFilm : originalFilmList) {
                if (originalFilm != null) {
                    OriginalFilmPurchaseDetail one = new OriginalFilmPurchaseDetail();
                    one.setPurchaseId(purchaseId);
                    one.setPurchaseNo(originalFilmPurchase.getPurchaseNo());
                    one.setOriginalFilmId(originalFilm.getId());
                    one.setOriginalFilmName(originalFilm.getOriginalName());
                    one.setReferencePrice(originalFilm.getReferencePrice());
                    one.setPurchaseNum(originalFilm.getOperationNum());
                    Double amout = (double) (originalFilm.getOperationNum() * originalFilm.getReferencePrice());
                    one.setPurchaseAmount(amout);
                    Double area = (double) originalFilm.getOriginalLong() * originalFilm.getOriginalWidth() * originalFilm.getOperationNum() / 1000000;
                    one.setPurchaseArea(MapUtil.round(area, 3));
                    totalNum += one.getPurchaseNum();
                    totalArea += area;
                    totalAmout += amout;
                    originalFilmPurchaseDetailMapper.insert(one);
                }
            }
            originalFilmPurchase.setId(purchaseId);
            originalFilmPurchase.setPurchaseNum(totalNum);
            originalFilmPurchase.setPurchaseArea(totalArea);
            originalFilmPurchase.setPurchaseAmount(totalAmout);
            originalFilmPurchase.setStatus("未入库");
            originalFilmPurchase.setCreatedAt(new Date());
            originalFilmPurchase.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            originalFilmPurchaseMapper.insert(originalFilmPurchase);
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 原片采购编辑查询
     *
     * @param id 采购id
     * @return
     */
    @Override
    public CommonResult updPurchaseDataQuery(String id) {
        List<OriginalFilmPurchaseDetail> details = originalFilmPurchaseDetailMapper.selectList(Wrappers.<OriginalFilmPurchaseDetail>query().lambda().eq(OriginalFilmPurchaseDetail::getPurchaseId, id));
        Set<String> set = details.stream().collect(Collectors.groupingBy(OriginalFilmPurchaseDetail::getOriginalFilmId)).keySet();
        List<OriginalFilm> originalFilms = originalFilmMapper.selectList(Wrappers.<OriginalFilm>query().lambda().in(OriginalFilm::getId, set));
        for (OriginalFilm originalFilm : originalFilms) {
            for (OriginalFilmPurchaseDetail detail : details) {
                if (originalFilm.getId().equals(detail.getOriginalFilmId())) {
                    originalFilm.setOperationNum(detail.getPurchaseNum());
                    originalFilm.setReferencePrice(detail.getReferencePrice());
                }
            }
        }
        return CommonResult.success(originalFilms);
    }

    /**
     * 原片采购编辑
     *
     * @param originalFilmPurchase
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updPurchaseData(OriginalFilmPurchase originalFilmPurchase) {
        try {
            //获取采购明细
            List<OriginalFilm> originalFilmList = originalFilmPurchase.getOriginalFilmList();
            int totalNum = 0;
            double totalArea = 0.000;
            double totalAmout = 0.000;
            //删除旧明细
            originalFilmPurchaseDetailMapper.delete(Wrappers.<OriginalFilmPurchaseDetail>query().lambda().eq(OriginalFilmPurchaseDetail::getPurchaseId, originalFilmPurchase.getId()));
            //遍历采购明细
            for (OriginalFilm originalFilm : originalFilmList) {
                if (originalFilm != null) {
                    OriginalFilmPurchaseDetail one = new OriginalFilmPurchaseDetail();
                    one.setPurchaseId(originalFilmPurchase.getId());
                    one.setPurchaseNo(originalFilmPurchase.getPurchaseNo());
                    one.setOriginalFilmId(originalFilm.getId());
                    one.setOriginalFilmName(originalFilm.getOriginalName());
                    one.setReferencePrice(originalFilm.getReferencePrice());
                    one.setPurchaseNum(originalFilm.getOperationNum());
                    Double amout = (double) (originalFilm.getOperationNum() * originalFilm.getReferencePrice());
                    one.setPurchaseAmount(amout);
                    Double area = (double) originalFilm.getOriginalLong() * originalFilm.getOriginalWidth() * originalFilm.getOperationNum() / 1000000;
                    one.setPurchaseArea(MapUtil.round(area, 3));
                    totalNum += one.getPurchaseNum();
                    totalArea += area;
                    totalAmout += amout;
                    originalFilmPurchaseDetailMapper.insert(one);
                }
            }
            originalFilmPurchase.setPurchaseNum(totalNum);
            originalFilmPurchase.setPurchaseArea(totalArea);
            originalFilmPurchase.setPurchaseAmount(totalAmout);
            originalFilmPurchase.setUpdatedAt(new Date());
            originalFilmPurchase.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            originalFilmPurchaseMapper.updateById(originalFilmPurchase);
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
        originalFilmPurchaseDetailMapper.delete(Wrappers.<OriginalFilmPurchaseDetail>query().lambda().in(OriginalFilmPurchaseDetail::getPurchaseId, split));
        originalFilmPurchaseMapper.delete(Wrappers.<OriginalFilmPurchase>query().lambda().in(OriginalFilmPurchase::getId, split));
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 导出原片采购单
     *
     * @param ids 采购id集合
     */
    @Override
    public void exportPurchase(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<OriginalFilmPurchase> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(OriginalFilmPurchase::getId, split);
        }
        wrapper.orderByDesc(OriginalFilmPurchase::getPurchaseNo);
        List<OriginalFilmPurchase> deliverRecords = originalFilmPurchaseMapper.selectList(wrapper);
        ExcelUtil<OriginalFilmPurchase> util = new ExcelUtil<OriginalFilmPurchase>(OriginalFilmPurchase.class);
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
        List<OriginalFilmPurchaseDetail> deliverRecords = null;
        if (StringUtils.isNotEmpty(ids)) {
            deliverRecords = originalFilmPurchaseDetailMapper.viewDetail(ids.split(","));
        }else {
            deliverRecords = originalFilmPurchaseDetailMapper.viewDetail(null);
        }
        ExcelUtil<OriginalFilmPurchaseDetail> util = new ExcelUtil<OriginalFilmPurchaseDetail>(OriginalFilmPurchaseDetail.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }
}
