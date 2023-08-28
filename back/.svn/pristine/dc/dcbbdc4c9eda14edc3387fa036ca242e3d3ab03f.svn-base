package com.erp.system.controller;

import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCustomerType;
import com.erp.system.service.ISysCustomerTypeService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.erp.common.core.domain.AjaxResult;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/21 14:53
 */

@RestController
@Api(tags = "系统管理-客户类型")
@RequestMapping("/system/customer/type")
public class SysCustomerTypeController {

    @Resource
    private ISysCustomerTypeService sysCustomerTypeService;
    @PreAuthorize("@ss.hasPermi('system:customer_type:add')")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @PostMapping("/add")
    @ApiOperation("新增客户类型")
    public CommonResult add(@RequestBody SysCustomerType sysCustomerType)
    {
        return sysCustomerTypeService.insertCustomerType(sysCustomerType);
    }

    @PreAuthorize("@ss.hasPermi('system:customer_type:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑客户类型")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysCustomerType sysCustomerType)
    {
        return sysCustomerTypeService.updatedCustomerType(id,sysCustomerType);
    }

    @PreAuthorize("@ss.hasPermi('system:customer_type:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除客户类型")
    public CommonResult del(@PathVariable("id") String id)
    {
        return sysCustomerTypeService.deletedCustomerType(id);
    }

    @GetMapping("/list")
    @ApiOperation("全部客户类型")
    public CommonResult<List<SysCustomerType>> list()
    {
        return sysCustomerTypeService.getAllCustomerType();
    }
}
