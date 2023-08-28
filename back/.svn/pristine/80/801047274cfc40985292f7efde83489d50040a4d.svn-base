package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCustomer;
import com.erp.floor.pojo.system.domain.SysMill;
import com.erp.system.service.ISysCustomerService;
import com.erp.system.service.ISysMillService;
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
 * @date 2022/7/22 14:52
 */

@RestController
@Api(tags = "系统管理-厂商管理")
@RequestMapping("/system/mill")
public class SysMillController extends BaseController {

    @Resource
    private ISysMillService sysMillService;

    @PreAuthorize("@ss.hasPermi('system:mill:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增厂商")
    public CommonResult add(@RequestBody SysMill sysMill)
    {
        return sysMillService.insertMill(sysMill);
    }

    @PreAuthorize("@ss.hasPermi('system:mill:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑厂商")
    public CommonResult edit(@PathVariable("id") String id,@RequestBody SysMill sysMill)
    {
        return sysMillService.updatedMill(id,sysMill);
    }

    @PreAuthorize("@ss.hasPermi('system:mill:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除厂商")
    public CommonResult del(@PathVariable("id") String id)
    {
        List<String> ids = Arrays.asList(id.split(","));
        return sysMillService.deletedMill(ids);
    }


    @GetMapping("/list")
    @ApiOperation("厂商列表")
    public CommonResult<List<SysMill>> list(SysMill sysMill)
    {
        startPage();
        return sysMillService.getMillList(sysMill);
    }
}
