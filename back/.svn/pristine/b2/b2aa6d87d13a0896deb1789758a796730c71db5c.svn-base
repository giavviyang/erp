package com.erp.accessories.controller;

import com.erp.accessories.service.AccessoriesPurchaseService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesPurchase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 17:46
 */
@RestController
@RequestMapping("/accessories/purchase")
@Api(tags = "辅料管理-辅料采购")
public class AccessoriesPurchaseController {
    @Resource
    private AccessoriesPurchaseService accessoriesPurchaseService;

    /**
     * 自动生成编号
     *
     * @return 编号
     */
    @ApiOperation("自动生成编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(accessoriesPurchaseService.productionNumber());
    }


    @ApiOperation(value = "辅料采购查询")
    @PostMapping("/queryPurchaseData")
    public CommonResult<List<AccessoriesPurchase>> queryPurchaseData(@RequestBody AccessoriesPurchase accessoriesPurchase) {
        return accessoriesPurchaseService.queryPurchaseData(accessoriesPurchase);
    }

    @ApiOperation(value = "查看明细")
    @PreAuthorize("@ss.hasPermi('accessories:purchase:detail')")
    @PostMapping("/viewDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "采购id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<AccessoriesPurchase>> viewDetail(String id) {
        return accessoriesPurchaseService.viewDetail(id);
    }

    @ApiOperation(value = "辅料采购新增")
    @PreAuthorize("@ss.hasPermi('accessories:purchase:add')")
    @PostMapping("/addPurchaseData")
    public CommonResult addPurchaseData(@RequestBody AccessoriesPurchase accessoriesPurchase) {
        return accessoriesPurchaseService.addPurchaseData(accessoriesPurchase);
    }

    @ApiOperation(value = "查询编辑明细")
    @PostMapping("/updPurchaseDataQuery")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "采购id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<Accessories>> updPurchaseDataQuery(String id) {
        return accessoriesPurchaseService.updPurchaseDataQuery(id);
    }

    @ApiOperation(value = "辅料采购编辑")
    @PreAuthorize("@ss.hasPermi('accessories:purchase:edit')")
    @PostMapping("/updPurchaseData")
    public CommonResult updPurchaseData(@RequestBody AccessoriesPurchase accessoriesPurchase) {
        return accessoriesPurchaseService.updPurchaseData(accessoriesPurchase);
    }

    @ApiOperation(value = "辅料采购删除")
    @PreAuthorize("@ss.hasPermi('accessories:purchase:del')")
    @PostMapping("/delPurchaseData")
    public CommonResult delPurchaseData(String ids) {
        return accessoriesPurchaseService.delPurchaseData(ids);
    }

    @ApiOperation(value = "导出辅料采购单")
    @PostMapping("/exportPurchase")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "采购id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('accessories:purchase:export')")
    public void exportPurchase(HttpServletResponse response, String ids) {
        accessoriesPurchaseService.exportPurchase(response, ids);
    }

    @ApiOperation(value = "导出辅料采购明细")
    @PostMapping("/exportPurchaseData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "采购id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('accessories:purchase:export')")
    public void exportPurchaseData(HttpServletResponse response, String ids) {
        accessoriesPurchaseService.exportPurchaseData(response, ids);
    }

}
