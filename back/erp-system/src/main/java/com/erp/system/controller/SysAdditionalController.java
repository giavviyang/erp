package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysAdditional;
import com.erp.system.service.ISysAdditionalService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 15:21
 */
@RestController
@Api(tags = "系统管理-附加项管理")
@RequestMapping("/system/additional")
public class SysAdditionalController extends BaseController {
    @Resource
    private ISysAdditionalService service;

    @PreAuthorize("@ss.hasPermi('system:additional:add')")
    @PostMapping("/add")
    @ApiOperation("新增附加项")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    public CommonResult add(@RequestBody SysAdditional sysAdditional)
    {
        return service.insertAdditional(sysAdditional);
    }

    @PreAuthorize("@ss.hasPermi('system:additional:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperation("编辑附加项")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysAdditional sysAdditional)
    {
        return service.updatedAdditional(id,sysAdditional);
    }

    @PreAuthorize("@ss.hasPermi('system:additional:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除附加项")
    public CommonResult del(@PathVariable("id") String id)
    {
        List<String> ids = Arrays.asList(id.split(","));
        return service.deletedAdditional(ids);
    }

    @GetMapping("/list")
    @ApiOperation("附加项列表")
    public CommonResult<List<SysAdditional>> list(SysAdditional sysAdditional)
    {
        startPage();
        return service.getAdditionalList(sysAdditional);
    }
}
