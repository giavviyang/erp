package com.erp.produce.controller;

import com.bstek.ureport.definition.Order;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.StringUtils;
import com.erp.floor.pojo.produce.domain.DamageRecord;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.ShelfDivisionBusiness;
import com.erp.produce.service.DamageService;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/6 10:58
 */
@RestController
@Api(tags = "生产管理-报损管理")
@RequestMapping("/produce/damage")
public class DamageController extends BaseController {

    @Resource
    private DamageService service;

    @PreAuthorize("@ss.hasPermi('system:damage:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增报损")
    public CommonResult add(@RequestBody DamageRecord record){
        try {
            return service.insertDamage(record);
        } catch (Exception e) {
            System.out.println(e);
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:damage:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("编辑报损")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody DamageRecord record){
        try {
            return service.editDamage(id,record);
        } catch (Exception e) {
            System.out.println(e);
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:damage:del')")
    @DeleteMapping("/del")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("删除报损")
    public CommonResult del(@RequestBody Map<String,Object> data){
        try {
            return service.delDamage(data.get("ids").toString());
        } catch (Exception e) {
            System.out.println(e);
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    @GetMapping("/list")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("报损列表")
    public CommonResult list(@RequestParam Map<String,Object> params){
        if(params.get("pageSize") == null){
            params.put("pageSize",20);
        }
        int startIndex = 0;
        if(StringUtils.isNotEmpty((String) params.get("pageNum")) && Integer.parseInt(String.valueOf(params.get("pageNum"))) != 0){
            startIndex = (Integer.parseInt(String.valueOf(params.get("pageNum"))) - 1) * Integer.parseInt(String.valueOf(params.get("pageSize")));
        }
        params.put("startIndex",startIndex);
        params.put("endIndex",params.get("pageSize"));
        params.remove("pageSize");
        params.remove("pageNum");
        PageHelper.clearPage();
        return service.getDamageList(params);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("@ss.hasPermi('system:damage:info')")
    @ApiOperation("获取详情")
    public CommonResult getDamageDetail(@PathVariable("id") String id){
        return service.getDamageDetail(id);
    }
    @GetMapping("/getOrderList")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("获取订单")
    public CommonResult getOrderList(ShelfDivisionBusiness business){
        startPage();
        return service.getOrderList(business);
    }

    @GetMapping("/getFlowCardList")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("获取流程卡")
    public CommonResult getFlowCardList(ShelfDivisionBusiness business){
        startPage();
        return service.getFlowCardList(business);
    }

    @GetMapping("/getDamageNo")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("获取报损单号")
    public CommonResult getDamageNo(){
        return service.getDamageNo();
    }

    @ApiOperation(value = "导出订单")
    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermi('system:damage:export')")
    public void export(HttpServletResponse response, String ids){
        service.exportDamage(response, ids);
    }
}
