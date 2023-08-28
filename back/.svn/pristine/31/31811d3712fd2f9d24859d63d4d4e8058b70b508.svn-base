package com.erp.produce.controller;

import com.erp.common.constant.ResultConstants;
import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.produce.domain.DamageRecord;
import com.erp.floor.pojo.produce.domain.PatchBusiness;
import com.erp.floor.pojo.produce.domain.PatchRecord;
import com.erp.produce.service.PatchService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/14 14:51
 */
@RestController
@Api(tags = "生产管理-补片管理")
@RequestMapping("/produce/patch")
public class PatchController extends BaseController {

    @Resource
    private PatchService service;

    @PostMapping("/getDamageInfo")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("获取报损补片单")
    public CommonResult getDamageInfo(@RequestBody Map<String,String> data){
        return service.getDamageInfo(data);
    }

    @PreAuthorize("@ss.hasPermi('system:patch:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增补片")
    public CommonResult add(@RequestBody List<PatchBusiness> patchBusinesses){
        return service.addPatch(patchBusinesses);
    }


    @GetMapping("/list")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("补片列表")
    public CommonResult list(PatchRecord record,@RequestParam Map<String,String> params){
        startPage();
        return service.getPatchList(params);
    }

    @PreAuthorize("@ss.hasPermi('system:patch:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("作废补片")
    public CommonResult del(@PathVariable("id") String id){
        return service.delPatch(id);
    }

    @PreAuthorize("@ss.hasPermi('system:patch:getInfo')")
    @GetMapping("/getInfo/{id}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("获取报损详情")
    public CommonResult getInfo(@PathVariable("id") String id){
        return service.getPatchInfo(id);
    }
}
