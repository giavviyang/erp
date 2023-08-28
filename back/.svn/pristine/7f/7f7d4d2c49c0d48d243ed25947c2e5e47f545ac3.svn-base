package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysDevice;
import com.erp.floor.pojo.system.domain.SysTeam;
import com.erp.system.service.ISysDeviceService;
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
 * @date 2022/8/1 15:39
 */
@RestController
@Api(tags = "系统管理-设备管理")
@RequestMapping("/system/device")
public class SysDeviceController extends BaseController {

    @Resource
    private ISysDeviceService service;

    @PreAuthorize("@ss.hasPermi('system:device:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增设备")
    public CommonResult add(@RequestBody SysDevice sysDevice)
    {
        return service.insertDevice(sysDevice);
    }

    @PreAuthorize("@ss.hasPermi('system:device:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑设备")
    public CommonResult edit(@PathVariable("id") String id,@RequestBody SysDevice sysDevice)
    {
        return service.updatedDevice(id,sysDevice);
    }

    @PreAuthorize("@ss.hasPermi('system:device:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除设备")
    public CommonResult del(@PathVariable("id") String id)
    {
        List<String> ids = Arrays.asList(id.split(","));
        return service.deletedDevice(ids);
    }

    @GetMapping("/list")
    @ApiOperation("设备列表")
    public CommonResult<List<SysDevice>> list(SysDevice sysDevice)
    {
        startPage();
        return service.getDeviceList(sysDevice);
    }
}
