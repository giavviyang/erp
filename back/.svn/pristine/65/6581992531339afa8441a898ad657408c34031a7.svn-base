package com.erp.system.controller;

import com.erp.common.constant.ResultConstants;
import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysProduct;
import com.erp.floor.pojo.system.domain.SysProductType;
import com.erp.floor.pojo.system.domain.SysSemiProduct;
import com.erp.system.service.ISysProductService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/25 13:48
 */
@RestController
@Api(tags = "系统管理-产品管理")
@RequestMapping("/system/product")
public class SysProductController extends BaseController {

    @Resource
    private ISysProductService sysProductService;

    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增产品")
    public CommonResult add(@RequestBody SysProduct sysProduct)
    {
        try {
            return sysProductService.insertProduct(sysProduct);
        } catch (Exception e) {
            System.out.println(e);
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑产品")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysProduct sysProduct)
    {
        try {
            return sysProductService.updatedProduct(id,sysProduct);
        } catch (Exception e) {
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:product:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除产品")
    public CommonResult del(@PathVariable("id") String id)
    {
        try {
            List<String> ids = Arrays.asList(id.split(","));
            return sysProductService.deletedProduct(ids);
        } catch (Exception e) {
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    @GetMapping("/list")
    @ApiOperation("产品列表")
    @ApiOperationSupport(ignoreParameters = {"semiProducts","created_at","updated_at"})
    public CommonResult<List<SysProduct>> list(SysProduct sysProduct)
    {
        startPage();
        return sysProductService.getProductList(sysProduct);
    }

    @GetMapping("/getSemiProduct/{id}")
    @ApiOperation("当前产品下的半产品")
    public CommonResult<List<SysSemiProduct>> getSemiProduct(@PathVariable("id") String id)
    {
        return sysProductService.getSemiProduct(id);
    }

    @PostMapping("/nameGetSemiProduct")
    @ApiOperation("用名称获取当前产品下的半产品")
    public CommonResult<List<SysSemiProduct>> nameGetSemiProduct(@RequestBody Map<String,String> dataMap)
    {
        if(dataMap.containsKey("productNames")){
            List<String> productNames = Arrays.asList(dataMap.get("productNames").split(","));
            return sysProductService.nameGetSemiProduct(productNames);
        }
        return CommonResult.success(ResultConstants.QUERY_SUCCESS);
    }

    @PostMapping("/queryProduct")
    @ApiOperation("依据产品名称查询产品")
    public CommonResult<List<SysProduct>> queryProduct(String productName)
    {
        return sysProductService.queryProduct(productName);
    }
}
