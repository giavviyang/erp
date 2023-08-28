package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysProduct;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/25 13:49
 */
public interface ISysProductService extends IService<SysProduct> {
    /**
     * 新增产品
     * @param sysProduct
     * @return
     */
    CommonResult insertProduct(SysProduct sysProduct) throws Exception;

    /**
     * 修改产品
     * @param sysProduct
     * @return
     */
    CommonResult updatedProduct(String id,SysProduct sysProduct) throws Exception;

    /**
     * 删除产品
     * @param id
     * @return
     * @throws Exception
     */
    CommonResult deletedProduct(List<String> ids) throws Exception;

    /**
     * 获取列表
     * @return
     */
    CommonResult getProductList(SysProduct sysProduct);

    /**
     * 根据产品id查询半产品
     * @return
     */
    CommonResult getSemiProduct(String id);


    /**
     * 根据产品名称查询半产品
     * @return
     */
    CommonResult nameGetSemiProduct(List<String> productNames);

    /**
     * 依据产品名称查询产品
     * @param productName 产品名称集合
     */
    CommonResult<List<SysProduct>> queryProduct(String productName);
}
