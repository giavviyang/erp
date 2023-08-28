package com.erp.system.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysProductType;
import com.erp.floor.pojo.system.domain.SysQrRule;
import com.erp.system.service.ISysProductTypeService;
import com.erp.system.service.ISysQrRuleService;
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
 * @date 2022/8/16 10:00
 */

@RestController
@Api(tags = "系统管理-二维码规则管理")
@RequestMapping("/system/qrcode")
public class SysQrRuleController extends BaseController {

    @Resource
    private ISysQrRuleService service;

    @PreAuthorize("@ss.hasPermi('system:qrcode:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增二维码规则")
    public CommonResult add(@RequestBody SysQrRule sysQrRule)
    {
        return service.insertQrRule(sysQrRule);
    }

    @PreAuthorize("@ss.hasPermi('system:qrcode:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑二维码规则")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysQrRule sysQrRule)
    {
        return service.updatedQrRule(id,sysQrRule);
    }

    @PreAuthorize("@ss.hasPermi('system:qrcode:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除二维码规则")
    public CommonResult del(@PathVariable("id") String id)
    {
        List<String> ids = Arrays.asList(id.split(","));
        return service.deletedQrRule(ids);
    }

    @GetMapping("/list")
    @ApiOperation("二维码规则列表")
    public CommonResult<List<SysProductType>> list(SysQrRule sysQrRule)
    {
        startPage();
        return service.getQrRuleList(sysQrRule);
    }
}
