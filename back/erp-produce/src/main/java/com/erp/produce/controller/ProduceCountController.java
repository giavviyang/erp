package com.erp.produce.controller;

import com.erp.common.core.domain.CommonResult;
import com.erp.produce.service.CompletionService;
import com.erp.produce.service.PatchService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/26 10:42
 */

@RestController
@Api(tags = "生产管理-生产统计")
@RequestMapping("/produce/production/count")
public class ProduceCountController {

    @Resource
    private CompletionService service;


    @GetMapping("/list")
    @ApiOperationSupport(ignoreParameters = {"id", "created_at", "updated_at"})
    @ApiOperation("获取生产统计列表")
    public CommonResult list(@RequestParam Map<String, String> data) {
        return service.getCountList(data);
    }

    @PreAuthorize("@ss.hasPermi('system:production_count:info')")
    @GetMapping("/info/{id}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("获取生产统计明细")
    public CommonResult info(@PathVariable("id") String id){
        return service.getCountInfo(id);
    }

    @ApiOperation(value = "导出生产统计")
    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermi('system:production_count:export')")
    public void export(HttpServletResponse response, String ids){
        service.exportCount(response, ids);
    }
}