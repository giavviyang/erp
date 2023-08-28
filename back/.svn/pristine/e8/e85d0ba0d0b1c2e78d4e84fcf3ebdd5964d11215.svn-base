package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysTeam;
import com.erp.floor.pojo.system.domain.SysTemplate;
import com.erp.system.service.ISysTeamService;
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
 * @date 2022/8/1 13:52
 */
@RestController
@Api(tags = "系统管理-班组管理")
@RequestMapping("/system/team")
public class SysTeamController extends BaseController {

    @Resource
    private ISysTeamService service;

    @PreAuthorize("@ss.hasPermi('system:team:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增班组")
    public CommonResult add(@RequestBody SysTeam sysTeam)
    {
        return service.insertTeam(sysTeam);
    }

    @PreAuthorize("@ss.hasPermi('system:team:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑编组")
    public CommonResult edit(@PathVariable("id") String id,@RequestBody SysTeam sysTeam)
    {
        return service.updatedTeam(id,sysTeam);
    }

    @PreAuthorize("@ss.hasPermi('system:team:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除班组")
    public CommonResult del(@PathVariable("id") String id)
    {
        List<String> ids = Arrays.asList(id.split(","));
        return service.deletedTeam(ids);
    }


    @GetMapping("/list")
    @ApiOperation("班组列表")
    public CommonResult list(SysTeam sysTeam)
    {
        startPage();
        return service.getTeamList(sysTeam);
    }


    @GetMapping("/allList")
    @ApiOperation("班组列表")
    public CommonResult allList(SysTeam sysTeam)
    {
        return service.getTeamList(sysTeam);
    }
}
