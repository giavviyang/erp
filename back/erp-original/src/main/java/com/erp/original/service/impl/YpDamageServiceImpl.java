package com.erp.original.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.original.OriginalFilmMapper;
import com.erp.floor.mapper.original.YpDamageDetailMapper;
import com.erp.floor.mapper.original.YpDamageMapper;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.original.domain.*;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.original.service.YpDamageService;
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
 * @date： 2022-09-14 10:14
 */
@Service
public class YpDamageServiceImpl extends ServiceImpl<YpDamageMapper, YpDamage> implements YpDamageService {
    @Resource
    private YpDamageMapper ypDamageMapper;
    @Resource
    private YpDamageDetailMapper ypDamageDetailMapper;
    @Resource
    private SysNumberingRulesMapper sysNumberingRulesMapper;
    @Resource
    private OriginalFilmMapper originalFilmMapper;

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
        LambdaQueryWrapper<YpDamage> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(YpDamage::getDamageNo, format1);
        wrapper.like(YpDamage::getCreatedAt, orderFormat.format(date));
        wrapper.orderByDesc(YpDamage::getCreatedAt).last("limit 1");
        YpDamage originalFilmPurchase = ypDamageMapper.selectOne(wrapper);
        //查询订单编号生成规则
        SysNumberingRules sysNumberingRules = sysNumberingRulesMapper.selectById("18");
        //根据规则设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(sysNumberingRules.getRuleContent());
        //编号拼接对象
        StringBuilder stringBuilder = new StringBuilder();
        if (originalFilmPurchase != null) {
            String orderNo = originalFilmPurchase.getDamageNo();
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
     * @param ypDamage 对象
     */
    @Override
    public CommonResult<List<YpDamage>> queryDamageData(YpDamage ypDamage) {
        LambdaQueryWrapper<YpDamage> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(ypDamage.getDamageNo()))
            wrapper.eq(YpDamage::getDamageNo, ypDamage.getDamageNo());
        if (StringUtils.isNotEmpty(ypDamage.getPerson()))
            wrapper.like(YpDamage::getPerson, ypDamage.getPerson());
        if (StringUtils.isNotEmpty(ypDamage.getReason()))
            wrapper.like(YpDamage::getReason, ypDamage.getReason());
        if (StringUtils.isNotEmpty(ypDamage.getDamageDateStart()) && StringUtils.isNotEmpty(ypDamage.getDamageDateEnd()))
            wrapper.between(YpDamage::getDamageDate, ypDamage.getDamageDateStart(), ypDamage.getDamageDateEnd());
        wrapper.orderByDesc(YpDamage::getDamageNo);
        Integer integer = ypDamageMapper.selectCount(wrapper);
        PageHelper.startPage(ypDamage.getPageNum(), ypDamage.getPageSize());
        List<YpDamage> ypDamages = ypDamageMapper.selectList(wrapper);
        PageInfo<YpDamage> info = new PageInfo<>(ypDamages, ypDamage.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, info.getList());
    }

    /**
     * 查看明细
     *
     * @param id 报损id
     */
    @Override
    public CommonResult<List<YpDamageDetail>> viewDetail(String id) {
        return CommonResult.success(ypDamageDetailMapper.viewDetail(new String[]{id}));
    }

    /**
     * 新增报损
     *
     * @param ypDamage 对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult addDamageData(YpDamage ypDamage) {
        try {
            //获取发货明细
            List<OriginalFilm> originalFilmList = ypDamage.getOriginalFilmList();
            String id = UUID.randomUUID().toString();
            Integer totalNum = 0;
            Double totalArea = 0.000;
            Double totalAmout = 0.000;
            //遍历明细
            for (OriginalFilm originalFilm : originalFilmList) {
                YpDamageDetail one = new YpDamageDetail();
                one.setDamageId(id);
                one.setDamageNo(ypDamage.getDamageNo());
                one.setOriginalFilmId(originalFilm.getId());
                one.setOriginalFilmName(originalFilm.getOriginalName());
                one.setDamageNum(originalFilm.getOperationNum());
                Double area = (double) originalFilm.getOriginalWidth() * originalFilm.getOriginalLong() / 1000000 * originalFilm.getOperationNum();
                one.setDamageArea(area);
                totalNum += originalFilm.getOperationNum();
                totalArea += area;
                totalAmout += originalFilm.getReferencePrice() * one.getDamageNum();
                //新增明细
                ypDamageDetailMapper.insert(one);
                //修改仓库数量
                originalFilm.setStock(originalFilm.getStock() - one.getDamageNum());
                originalFilmMapper.updateById(originalFilm);
            }
            //新增报损记录
            ypDamage.setId(id);
            ypDamage.setDamageNum(totalNum);
            ypDamage.setDamageArea(totalArea);
            ypDamage.setDamageAmount(totalAmout);
            ypDamage.setCreatedAt(new Date());
            ypDamage.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            ypDamageMapper.insert(ypDamage);
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 编辑报损查询
     *
     * @param id 报损id
     */
    @Override
    public CommonResult updDamageDataQuery(String id) {
        List<YpDamageDetail> ypDamageDetails = ypDamageDetailMapper.selectList(Wrappers.<YpDamageDetail>query().lambda().eq(YpDamageDetail::getDamageId, id));
        Set<String> set = ypDamageDetails.stream().collect(Collectors.groupingBy(YpDamageDetail::getOriginalFilmId)).keySet();
        List<OriginalFilm> originalFilms = originalFilmMapper.selectList(Wrappers.<OriginalFilm>query().lambda().in(OriginalFilm::getId, set));
        for (OriginalFilm originalFilm : originalFilms) {
            for (YpDamageDetail ypDamageDetail : ypDamageDetails) {
                if (originalFilm.getId().equals(ypDamageDetail.getOriginalFilmId())) {
                    originalFilm.setOperationNum(ypDamageDetail.getDamageNum());
                }
            }
        }
        return CommonResult.success(originalFilms);
    }

    /**
     * 编辑报损
     *
     * @param ypDamage 对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updDamageData(YpDamage ypDamage) {
        try {
            //获取旧明细
            List<YpDamageDetail> ypDamageDetails = ypDamageDetailMapper.selectList(Wrappers.<YpDamageDetail>query().lambda().eq(YpDamageDetail::getDamageId, ypDamage.getId()));
            //删除旧发货明细
            ypDamageDetailMapper.delete(Wrappers.<YpDamageDetail>query().lambda().eq(YpDamageDetail::getDamageId, ypDamage.getId()));
            //获取发货明细
            List<OriginalFilm> originalFilmList = ypDamage.getOriginalFilmList();
            Integer totalNum = 0;
            Double totalArea = 0.000;
            Double totalAmout = 0.000;
            //遍历明细
            for (OriginalFilm originalFilm : originalFilmList) {
                YpDamageDetail one = new YpDamageDetail();
                one.setDamageId(ypDamage.getId());
                one.setDamageNo(ypDamage.getDamageNo());
                one.setOriginalFilmId(originalFilm.getId());
                one.setOriginalFilmName(originalFilm.getOriginalName());
                one.setDamageNum(originalFilm.getOperationNum());
                Double area = (double) originalFilm.getOriginalWidth() * originalFilm.getOriginalLong() / 1000000 * originalFilm.getOperationNum();
                one.setDamageArea(area);
                totalNum += originalFilm.getOperationNum();
                totalArea += area;
                totalAmout += originalFilm.getReferencePrice() * one.getDamageNum();
                //新增明细
                ypDamageDetailMapper.insert(one);
                //修改仓库数量
                for (YpDamageDetail ypDamageDetail : ypDamageDetails) {
                    if (ypDamageDetail.getOriginalFilmId().equals(originalFilm.getId())) {
                        originalFilm.setStock(originalFilm.getStock() + ypDamageDetail.getDamageNum() - one.getDamageNum());
                        originalFilmMapper.updateById(originalFilm);
                    }
                }
            }
            //编辑报损记录
            ypDamage.setDamageNum(totalNum);
            ypDamage.setDamageArea(totalArea);
            ypDamage.setDamageAmount(totalAmout);
            ypDamage.setUpdatedAt(new Date());
            ypDamage.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            ypDamageMapper.updateById(ypDamage);
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
            List<YpDamageDetail> ypDamageDetails = ypDamageDetailMapper.selectList(Wrappers.<YpDamageDetail>query().lambda().in(YpDamageDetail::getDamageId, split));
            //还原仓库数量
            Map<String, List<YpDamageDetail>> collect = ypDamageDetails.stream().collect(Collectors.groupingBy(YpDamageDetail::getOriginalFilmId));
            //获取原片数据
            List<OriginalFilm> originalFilms = originalFilmMapper.selectList(Wrappers.<OriginalFilm>query().lambda().in(OriginalFilm::getId, collect.keySet()));
            //还原仓库数量
            for (Map.Entry<String, List<YpDamageDetail>> stringListEntry : collect.entrySet()) {
                for (OriginalFilm originalFilm : originalFilms) {
                    if (stringListEntry.getKey().equals(originalFilm.getId())) {
                        int sum = stringListEntry.getValue().stream().mapToInt(YpDamageDetail::getDamageNum).sum();
                        originalFilm.setStock(originalFilm.getStock() + sum);
                        originalFilmMapper.updateById(originalFilm);
                    }
                }
            }
            //删除旧发货明细
            ypDamageDetailMapper.delete(Wrappers.<YpDamageDetail>query().lambda().in(YpDamageDetail::getDamageId, split));
            //删除旧发货记录
            ypDamageMapper.delete(Wrappers.<YpDamage>query().lambda().in(YpDamage::getId, split));
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
        LambdaQueryWrapper<YpDamage> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(YpDamage::getId, split);
        }
        wrapper.orderByDesc(YpDamage::getDamageNo);
        List<YpDamage> deliverRecords = ypDamageMapper.selectList(wrapper);
        ExcelUtil<YpDamage> util = new ExcelUtil<YpDamage>(YpDamage.class);
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
        List<YpDamageDetail> deliverRecords = null;
        if (StringUtils.isNotEmpty(ids)) {
            deliverRecords = ypDamageDetailMapper.viewDetail(ids.split(","));
        }else {
            deliverRecords = ypDamageDetailMapper.viewDetail(null);
        }
        ExcelUtil<YpDamageDetail> util = new ExcelUtil<YpDamageDetail>(YpDamageDetail.class);
        util.exportExcel(response, deliverRecords, "参数数据");
    }


}
