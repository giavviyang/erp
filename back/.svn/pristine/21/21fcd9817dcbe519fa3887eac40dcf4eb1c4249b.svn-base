package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysProductMapper;
import com.erp.floor.mapper.system.SysProductTypeMapper;
import com.erp.floor.mapper.system.SysSemiProductMapper;
import com.erp.floor.pojo.system.domain.SysCustomerType;
import com.erp.floor.pojo.system.domain.SysProduct;
import com.erp.floor.pojo.system.domain.SysProductType;
import com.erp.floor.pojo.system.domain.SysSemiProduct;
import com.erp.system.service.ISysProductService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/25 13:50
 */
@Service
public class SysProductServiceImpl extends ServiceImpl<SysProductMapper, SysProduct> implements ISysProductService {

    @Resource
    private SysProductMapper sysProductMapper;

    @Resource
    private SysProductTypeMapper sysProductTypeMapper;
    @Resource
    private SysSemiProductMapper sysSemiProductMapper;
    @Override
    @Transactional
    public CommonResult insertProduct(SysProduct sysProduct) throws Exception {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());

        if(StringUtils.isNotEmpty(sysProduct.getTypeId()) && sysProduct.getTypeId().equals("000")){
            SysProductType type = new SysProductType();
            type.setTypeName(sysProduct.getType());
            sysProductTypeMapper.insert(type);
            sysProduct.setTypeId(type.getId());
        }

        LambdaQueryWrapper<SysProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProduct::getProductName,sysProduct.getProductName());
        int count = sysProductMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此产品名称已存在");
        }
        int i = sysProductMapper.insert(sysProduct);
        if(i <= 0){
            throw new Exception(ResultConstants.SAVE_ERROR);
        }
        for (SysSemiProduct s:sysProduct.getSemiProduct()) {
            s.setProductId(sysProduct.getId());
            i = sysSemiProductMapper.insert(s);
            if(i <= 0){
                throw new Exception(ResultConstants.SAVE_ERROR);
            }
        }
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    @Override
    @Transactional
    public CommonResult updatedProduct(String id, SysProduct sysProduct) throws Exception {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProduct::getProductName,sysProduct.getProductName());
        wrapper.ne(SysProduct::getId,id);
        int count = sysProductMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此产品名称已存在");
        }
        sysProduct.setId(id);
        sysProductMapper.updateById(sysProduct);
        for (SysSemiProduct s:sysProduct.getSemiProduct()) {
            s.setProductId(sysProduct.getId());
            sysSemiProductMapper.updateById(s);
        }
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    @Override
    @Transactional
    public CommonResult deletedProduct(List<String> ids) throws Exception {
        int i = sysProductMapper.deleteBatchIds(ids);
        if(i <= 0){
            throw new Exception(ResultConstants.DEL_ERROR);
        }
        LambdaQueryWrapper<SysSemiProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysSemiProduct::getProductId,ids);
        i = sysSemiProductMapper.delete(wrapper);
        if(i <= 0){
            throw new Exception(ResultConstants.DEL_ERROR);
        }
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    @Override
    public CommonResult getProductList(SysProduct sysProduct) {
        LambdaQueryWrapper<SysProduct> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(sysProduct.getTypeId())){
            queryWrapper.eq(SysProduct::getTypeId,sysProduct.getTypeId());
        }
        if(StringUtils.isNotEmpty(sysProduct.getNo())){
            queryWrapper.like(SysProduct::getNo,sysProduct.getNo());
        }
        if(StringUtils.isNotEmpty(sysProduct.getProductName())){
            queryWrapper.like(SysProduct::getProductName,sysProduct.getProductName());
        }
        queryWrapper.orderByDesc(SysProduct::getCreatedAt);
        List<SysProduct> list = sysProductMapper.selectList(queryWrapper);
        int count = sysProductMapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }

    @Override
    public CommonResult getSemiProduct(String id) {
        LambdaQueryWrapper<SysSemiProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysSemiProduct::getProductId,id);
        queryWrapper.orderByAsc(SysSemiProduct::getSort);
        List<SysSemiProduct> list = sysSemiProductMapper.selectList(queryWrapper);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
    }

    @Override
    public CommonResult nameGetSemiProduct(List<String> productNames) {
        List<Map<String,Object>> result = new ArrayList<>();
        for (String productName:productNames) {
            LambdaQueryWrapper<SysProduct> productLambdaQueryWrapper = new LambdaQueryWrapper<>();
            productLambdaQueryWrapper.eq(SysProduct::getProductName,productName);
            SysProduct product = sysProductMapper.selectOne(productLambdaQueryWrapper);
            if(product != null){
                LambdaQueryWrapper<SysSemiProduct> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysSemiProduct::getProductId,product.getId());
                queryWrapper.orderByAsc(SysSemiProduct::getSort);
                List<SysSemiProduct> list = sysSemiProductMapper.selectList(queryWrapper);
                Map<String,Object> productMap = new HashMap<>();
                productMap.put("productName",productName);
                productMap.put("productId",product.getId());
                productMap.put("semiProduct",list);
                result.add(productMap);
            }
        }
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,result);
    }

    /**
     * 依据产品名称查询产品
     *
     * @param productName 产品名称集合
     */
    @Override
    public CommonResult<List<SysProduct>> queryProduct(String productName) {
        String[] split = productName.split(",");
        return CommonResult.success(sysProductMapper.selectList(Wrappers.<SysProduct>query().lambda().in(SysProduct::getProductName, split)));
    }
}
