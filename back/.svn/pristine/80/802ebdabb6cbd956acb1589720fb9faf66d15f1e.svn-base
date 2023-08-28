package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.sales.OutsourcedMapper;
import com.erp.floor.mapper.sales.OutsourcedProductsMapper;
import com.erp.floor.mapper.sales.OutsourcedStockProductMapper;
import com.erp.floor.mapper.sales.OutsourcedWarehousingMapper;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.sales.service.OutsourcedWarehousingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-23 14:16
 */
@Service
public class OutsourcedWarehousingServiceImpl extends ServiceImpl<OutsourcedWarehousingMapper, OutsourcedWarehousing> implements OutsourcedWarehousingService {
    @Resource
    private OutsourcedWarehousingMapper outsourcedWarehousingMapper;
    @Resource
    private OutsourcedStockProductMapper outsourcedStockProductMapper;
    @Resource
    private OutsourcedProductsMapper outsourcedProductsMapper;
    @Resource
    private OutsourcedMapper outsourcedMapper;




    /**
     * 查询入库信息
     *
     * @param outsourcedWarehousing
     */
    @Override
    public CommonResult<List<OutsourcedWarehousing>> queryData(OutsourcedWarehousing outsourcedWarehousing) {
        LambdaQueryWrapper<OutsourcedWarehousing> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(outsourcedWarehousing.getWarehousedNo()))
            wrapper.eq(OutsourcedWarehousing::getWarehousedNo, outsourcedWarehousing.getWarehousedNo());
        if (StringUtils.isNotEmpty(outsourcedWarehousing.getOutsourcingNo()))
            wrapper.eq(OutsourcedWarehousing::getOutsourcingNo, outsourcedWarehousing.getOutsourcingNo());
        if (StringUtils.isNotEmpty(outsourcedWarehousing.getWarehousedPerson()))
            wrapper.like(OutsourcedWarehousing::getWarehousedPerson, outsourcedWarehousing.getWarehousedPerson());
        if (StringUtils.isNotEmpty(outsourcedWarehousing.getWarehousedDateStart()) && StringUtils.isNotEmpty(outsourcedWarehousing.getWarehousedDateEnd()))
            wrapper.between(OutsourcedWarehousing::getWarehousedDate, outsourcedWarehousing.getWarehousedDateStart(), outsourcedWarehousing.getWarehousedDateEnd());
        wrapper.orderByDesc(OutsourcedWarehousing::getWarehousedDate);
        Integer integer = outsourcedWarehousingMapper.selectCount(wrapper);
        PageHelper.startPage(outsourcedWarehousing.getPageNum(), outsourcedWarehousing.getPageSize());
        List<OutsourcedWarehousing> outsourcedWarehousings = outsourcedWarehousingMapper.queryData(outsourcedWarehousing);
        PageInfo<OutsourcedWarehousing> info = new PageInfo<>(outsourcedWarehousings, outsourcedWarehousing.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, info.getList());
    }

    /**
     * 新增入库信息
     *
     * @param outsourcedWarehousing
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult addData(OutsourcedWarehousing outsourcedWarehousing) {
        try {
            List<OutsourcedProducts> outsourcedProductsList = outsourcedWarehousing.getOutsourcedProductsList();
            String id = UUID.randomUUID().toString();
            Integer num = 0;
            for (OutsourcedProducts outsourcedProducts : outsourcedProductsList) {
                OutsourcedStockProduct one = new OutsourcedStockProduct();
                one.setOutsourcedProductsId(outsourcedProducts.getId());
                one.setOutsourcedWarehousingId(id);
                one.setWarehousingNum(outsourcedProducts.getNoShelfNum());
                outsourcedStockProductMapper.insert(one);
                //修改入库数量
                outsourcedProducts.setWarehouseNum(outsourcedProducts.getWarehouseNum() + outsourcedProducts.getNoShelfNum());
                outsourcedProductsMapper.updateById(outsourcedProducts);
                num += one.getWarehousingNum();
            }
            outsourcedWarehousing.setId(id);
            outsourcedWarehousing.setOutsourcingNo(outsourcedProductsList.get(0).getOutsourcingNo());
            outsourcedWarehousing.setOutsourcingId(outsourcedProductsList.get(0).getOutsourcingId());
            outsourcedWarehousing.setWarehousedNum(num);
            outsourcedWarehousingMapper.insert(outsourcedWarehousing);
            //修改外协记录入库数量
            Outsourced outsourced = outsourcedMapper.selectById(outsourcedWarehousing.getOutsourcingId());
            Integer warehousNum = outsourced.getWarehousingNum() + num;
            outsourced.setWarehousingNum(warehousNum);
            if (warehousNum.equals(outsourced.getOutsourcingNum())) {
                outsourced.setWarehousingStatus("已入库");
            }else {
                outsourced.setWarehousingStatus("入库中");
            }
            outsourcedMapper.updateById(outsourced);
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
     * @param ids 入库id集合
     */
    @Override
    public CommonResult<List<OutsourcedStockProduct>> detailedView(String ids) {
        return CommonResult.success(outsourcedStockProductMapper.detailedView(ids.split(",")));
    }

    /**
     * 删除明细
     *
     * @param ids 入库id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult delData(String ids) {
        try {
            String[] split = ids.split(",");
            //查询所有明细
            List<OutsourcedStockProduct> outsourcedStockProducts =
                    outsourcedStockProductMapper.selectList(Wrappers.<OutsourcedStockProduct>query().lambda().in(OutsourcedStockProduct::getOutsourcedWarehousingId, split));
            //依据外协产品分组
            Map<String, List<OutsourcedStockProduct>> collect = outsourcedStockProducts.stream().collect(Collectors.groupingBy(OutsourcedStockProduct::getOutsourcedProductsId));
            //查询涉及外协产品
            List<OutsourcedProducts> outsourcedProducts =
                    outsourcedProductsMapper.selectList(Wrappers.<OutsourcedProducts>query().lambda().in(OutsourcedProducts::getId, collect.keySet()));
            //依据外协记录分组
            Map<String, List<OutsourcedProducts>> collect1 = outsourcedProducts.stream().collect(Collectors.groupingBy(OutsourcedProducts::getOutsourcingId));
            //修改外协产品入库数量
            for (OutsourcedProducts outsourcedProduct : outsourcedProducts) {
                for (Map.Entry<String, List<OutsourcedStockProduct>> stringListEntry : collect.entrySet()) {
                    if (outsourcedProduct.getId().equals(stringListEntry.getKey())) {
                        int sum = stringListEntry.getValue().stream().mapToInt(OutsourcedStockProduct::getWarehousingNum).sum();
                        outsourcedProduct.setWarehouseNum(outsourcedProduct.getWarehouseNum() - sum);
                        outsourcedProductsMapper.updateById(outsourcedProduct);
                    }
                }
            }
            //查询修改后外协产品
            List<OutsourcedProducts> outsourcedProductsList =
                    outsourcedProductsMapper.selectList(Wrappers.<OutsourcedProducts>query().lambda().in(OutsourcedProducts::getOutsourcingId, collect1.keySet()));
            Map<String, List<OutsourcedProducts>> collect2 = outsourcedProductsList.stream().collect(Collectors.groupingBy(OutsourcedProducts::getOutsourcingId));
            //修改外协记录中入库数量
            for (Map.Entry<String, List<OutsourcedProducts>> stringListEntry : collect2.entrySet()) {
                int sum = stringListEntry.getValue().stream().mapToInt(OutsourcedProducts::getWarehouseNum).sum();
                LambdaUpdateWrapper<Outsourced> wrapper = new LambdaUpdateWrapper<>();
                wrapper.set(Outsourced::getWarehousingNum, sum);
                wrapper.eq(Outsourced::getId, stringListEntry.getKey());
                outsourcedMapper.update(null, wrapper);
            }
            //删除入库记录
            outsourcedWarehousingMapper.delete(Wrappers.<OutsourcedWarehousing>query().lambda().in(OutsourcedWarehousing::getId, split));
            //删除入库产品
            outsourcedStockProductMapper.delete(Wrappers.<OutsourcedStockProduct>query().lambda().in(OutsourcedStockProduct::getOutsourcedWarehousingId, split));
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 导出入库记录
     *
     * @param response
     * @param ids      入库id集合
     */
    @Override
    public void exportData(HttpServletResponse response, String ids) {
        List<OutsourcedWarehousing> outsourcedWarehousings = null;
        if (StringUtils.isNotEmpty(ids)) {
            outsourcedWarehousings = outsourcedWarehousingMapper.queryExportData(ids.split(","));
        }else {
            outsourcedWarehousings = outsourcedWarehousingMapper.queryExportData(null);
        }
        ExcelUtil<OutsourcedWarehousing> util = new ExcelUtil<OutsourcedWarehousing>(OutsourcedWarehousing.class);
        util.exportExcel(response, outsourcedWarehousings, "参数数据");
    }

    /**
     * 导出入库记录明细
     *
     * @param response
     * @param ids      入库id集合
     */
    @Override
    public void exportDataDetailed(HttpServletResponse response, String ids) {
        List<OutsourcedStockProduct> outsourcedStockProducts = null;
        if (StringUtils.isNotEmpty(ids)) {
            outsourcedStockProducts = outsourcedStockProductMapper.detailedView(ids.split(","));
        }else {
            outsourcedStockProducts = outsourcedStockProductMapper.detailedView(null);
        }
        ExcelUtil<OutsourcedStockProduct> util = new ExcelUtil<OutsourcedStockProduct>(OutsourcedStockProduct.class);
        util.exportExcel(response, outsourcedStockProducts, "参数数据");
    }
}
