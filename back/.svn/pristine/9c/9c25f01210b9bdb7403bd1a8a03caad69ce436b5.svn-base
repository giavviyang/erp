package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCustomer;
import com.erp.system.service.ISysCustomerService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 10:19
 */
@RestController
@Api(tags = "系统管理-客户管理")
@RequestMapping("/system/customer")
public class SysCustomerController extends BaseController {

    @Resource
    private ISysCustomerService sysCustomerService;

    @PreAuthorize("@ss.hasPermi('system:customer:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增客户")
    public CommonResult add(@RequestBody SysCustomer sysCustomer)
    {
        return sysCustomerService.insertCustomer(sysCustomer);
    }

    @PostMapping("/orderadd")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    public CommonResult orderadd(@RequestBody SysCustomer sysCustomer)
    {
        CommonResult result = sysCustomerService.insertCustomer(sysCustomer);
        result.setCode(200);
        return result;
    }
    @PreAuthorize("@ss.hasPermi('system:customer:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑客户")
    public CommonResult edit(@PathVariable("id") String id,@RequestBody SysCustomer sysCustomer)
    {
        return sysCustomerService.updatedCustomer(id,sysCustomer);
    }

    @PreAuthorize("@ss.hasPermi('system:customer:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除客户")
    public CommonResult del(@PathVariable("id") String id)
    {
        List<String> ids = Arrays.asList(id.split(","));
        return sysCustomerService.deletedCustomer(ids);
    }


    @GetMapping("/list")
    @ApiOperation("客户列表")
    public CommonResult<List<SysCustomer>> list(SysCustomer sysCustomer)
    {
        startPage();
        return sysCustomerService.getCustomerList(sysCustomer);
    }
}
