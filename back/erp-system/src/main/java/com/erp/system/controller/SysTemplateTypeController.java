package com.erp.system.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysProductType;
import com.erp.floor.pojo.system.domain.SysTemplateType;
import com.erp.system.service.ISysTemplateTypeService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 16:08
 */
@RestController
@Api(tags = "系统管理-模板类型管理")
@RequestMapping("/system/template/type")
public class SysTemplateTypeController {
    @Resource
    private ISysTemplateTypeService service;

    @PreAuthorize("@ss.hasPermi('system:template_type:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增模板类型")
    public CommonResult add(@RequestBody SysTemplateType sysTemplateType)
    {
        return service.insertTemplateType(sysTemplateType);
    }

    @PreAuthorize("@ss.hasPermi('system:template_type:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑模板类型")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysTemplateType sysTemplateType)
    {
        return service.updatedTemplateType(id,sysTemplateType);
    }

    @PreAuthorize("@ss.hasPermi('system:template_type:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除模板类型")
    public CommonResult del(@PathVariable("id") String id)
    {
        return service.deletedTemplateType(id);
    }

    @GetMapping("/list")
    @ApiOperation("模板类型列表")
    public CommonResult list()
    {
        return service.getAllTemplateType();
    }
}
