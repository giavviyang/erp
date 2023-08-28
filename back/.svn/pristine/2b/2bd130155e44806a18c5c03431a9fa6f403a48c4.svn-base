package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCraftFlow;
import com.erp.system.service.ISysCraftFlowService;
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
@Api(tags = "系统管理-工艺流程")
@RequestMapping("/system/craftflow")
public class SysCraftFlowController extends BaseController {
    @Resource
    private ISysCraftFlowService sysCraftFlowService;

    @PreAuthorize("@ss.hasPermi('system:craftflow:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增工艺流程")
    public CommonResult add(@RequestBody SysCraftFlow sysCraftFlow)
    {
        return sysCraftFlowService.insertCraftFlow(sysCraftFlow);
    }

    @PreAuthorize("@ss.hasPermi('system:craftflow:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("修改工艺流程")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysCraftFlow sysCraftFlow)
    {
        return sysCraftFlowService.updatedCraftFlow(id,sysCraftFlow);
    }

    @PreAuthorize("@ss.hasPermi('system:craftflow:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除工艺流程")
    public CommonResult del(@PathVariable("id") String id)
    {
        List<String> ids = Arrays.asList(id.split(","));
        return sysCraftFlowService.deletedCraftFlow(ids);
    }


    @GetMapping("/list")
    @ApiOperation("工艺流程列表")
    public CommonResult<List<SysCraftFlow>> list(SysCraftFlow sysCraftFlow)
    {
        startPage();
        return sysCraftFlowService.getCraftFlowList(sysCraftFlow);
    }

    @GetMapping("/getAllList")
    @ApiOperation("全部工艺流程列表")
    public CommonResult<List<SysCraftFlow>> allList(SysCraftFlow sysCraftFlow)
    {
        return sysCraftFlowService.getCraftFlowList(sysCraftFlow);
    }
}
