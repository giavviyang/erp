package com.erp.system.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysMill;
import com.erp.floor.pojo.system.domain.SysMillType;
import com.erp.system.service.ISysMillService;
import com.erp.system.service.ISysMillTypeService;
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
 * @date 2022/7/22 14:53
 */

@RestController
@Api(tags = "系统管理-厂商类型管理")
@RequestMapping("/system/mill/type")
public class SysMillTypeController {

    @Resource
    private ISysMillTypeService sysMillTypeService;

    @PreAuthorize("@ss.hasPermi('system:mill_type:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增厂商类型")
    public CommonResult add(@RequestBody SysMillType sysMillType)
    {
        return sysMillTypeService.insertMillType(sysMillType);
    }

    @PreAuthorize("@ss.hasPermi('system:mill_type:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑厂商类型")
    public CommonResult edit(@PathVariable("id") String id,@RequestBody SysMillType sysMillType)
    {
        return sysMillTypeService.updatedMillType(id,sysMillType);
    }

    @PreAuthorize("@ss.hasPermi('system:mill_type:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除厂商类型")
    public CommonResult del(@PathVariable("id") String id)
    {
        return sysMillTypeService.deletedMillType(id);
    }

    @GetMapping("/list")
    @ApiOperation("厂商类型列表")
    public CommonResult<List<SysMillType>> list()
    {
        return sysMillTypeService.getAllMillType();
    }
}
