package com.erp.system.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysMillType;
import com.erp.floor.pojo.system.domain.SysProductType;
import com.erp.system.service.ISysProductTypeService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 16:31
 */
@RestController
@Api(tags = "系统管理-产品类型管理")
@RequestMapping("/system/product/type")
public class SysProductTypeController {

    @Resource
    private ISysProductTypeService sysProductTypeService;

    @PreAuthorize("@ss.hasPermi('system:product_type:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增产品类型")
    public CommonResult add(@RequestBody SysProductType sysProductType)
    {
        return sysProductTypeService.insertProductType(sysProductType);
    }

    @PreAuthorize("@ss.hasPermi('system:product_type:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑产品类型")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysProductType sysProductType)
    {
        return sysProductTypeService.updatedProductType(id,sysProductType);
    }

    @PreAuthorize("@ss.hasPermi('system:product_type:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除产品类型")
    public CommonResult del(@PathVariable("id") String id)
    {
        return sysProductTypeService.deletedProductType(id);
    }

    @GetMapping("/list")
    @ApiOperation("产品类型列表")
    public CommonResult<List<SysProductType>> list()
    {
        return sysProductTypeService.getAllProductType();
    }
}
