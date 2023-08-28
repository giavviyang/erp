package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysMill;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.system.service.ISysNumberingRulesService;
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
 * @date 2022/8/1 16:19
 */

@RestController
@Api(tags = "系统管理-编号规则管理")
@RequestMapping("/system/number")
public class SysNumberingRulesController extends BaseController {
    @Resource
    private ISysNumberingRulesService service;

    @PreAuthorize("@ss.hasPermi('system:number:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑编号规则")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysNumberingRules sysNumberingRules)
    {
        return service.updatedNumberingRules(id,sysNumberingRules);
    }

    @GetMapping("/list")
    @ApiOperation("编号规则列表")
    public CommonResult<List<SysNumberingRules>> list()
    {
        startPage();
        return service.getNumberingRulesList();
    }
}
