package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCraft;
import com.erp.system.service.ISysCraftService;
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
 * @date 2022/7/26 10:52
 */

@RestController
@Api(tags = "系统管理-工艺管理")
@RequestMapping("/system/craft")
public class SysCraftController extends BaseController {
    @Resource
    private ISysCraftService sysCraftService;

    @PreAuthorize("@ss.hasPermi('system:craft:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增工艺")
    public CommonResult add(@RequestBody SysCraft sysCraft)
    {
        return sysCraftService.insertCraft(sysCraft);
    }

    @PreAuthorize("@ss.hasPermi('system:craft:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑工艺")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysCraft sysCraft)
    {
        return sysCraftService.updatedCraft(id,sysCraft);
    }

    @PreAuthorize("@ss.hasPermi('system:craft:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除工艺")
    public CommonResult del(@PathVariable("id") String id)
    {
        List<String> ids = Arrays.asList(id.split(","));
        return sysCraftService.deletedCraft(ids);
    }


    @GetMapping("/list")
    @ApiOperation("工艺列表")
    public CommonResult<List<SysCraft>> list(SysCraft sysCraft)
    {
        startPage();
        return sysCraftService.getCraftList(sysCraft);
    }

    @GetMapping("/allList")
    @ApiOperation("全部工艺")
    public CommonResult<List<SysCraft>> allList()
    {
        return sysCraftService.getCraftList(new SysCraft());
    }
}
