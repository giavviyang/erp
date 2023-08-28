package com.erp.original.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.original.domain.OriginalFilmPurchase;
import com.erp.original.service.OriginalFilmPurchaseService;
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
 * @date： 2022-09-09 14:18
 */
@RestController
@RequestMapping(value = "/original/purchase")
@Api(tags = "原片管理-原片采购")
public class OriginalFilmPurchaseController extends BaseController {
    @Resource
    private OriginalFilmPurchaseService originalFilmPurchaseService;

    /**
     * 自动生成编号
     *
     * @return 编号
     */
    @ApiOperation("自动生成编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(originalFilmPurchaseService.productionNumber());
    }


    @ApiOperation(value = "原片采购查询")
    @PostMapping("/queryPurchaseData")
    public CommonResult<List<OriginalFilmPurchase>> queryPurchaseData(@RequestBody OriginalFilmPurchase originalFilmPurchase) {
        return originalFilmPurchaseService.queryPurchaseData(originalFilmPurchase);
    }

    @ApiOperation(value = "查看明细")
    @PreAuthorize("@ss.hasPermi('original:purchase:viewDetail')")
    @PostMapping("/viewDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "采购id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<OriginalFilmPurchase>> viewDetail(String id) {
        return originalFilmPurchaseService.viewDetail(id);
    }

    @ApiOperation(value = "原片采购新增")
    @PreAuthorize("@ss.hasPermi('original:purchase:addPurchaseData')")
    @PostMapping("/addPurchaseData")
    public CommonResult addPurchaseData(@RequestBody OriginalFilmPurchase originalFilmPurchase) {
        return originalFilmPurchaseService.addPurchaseData(originalFilmPurchase);
    }

    @ApiOperation(value = "原片采购编辑查询")
    @PostMapping("/updPurchaseDataQuery")
    public CommonResult updPurchaseDataQuery(String id) {
        return originalFilmPurchaseService.updPurchaseDataQuery(id);
    }

    @ApiOperation(value = "原片采购编辑")
    @PreAuthorize("@ss.hasPermi('original:purchase:updPurchaseData')")
    @PostMapping("/updPurchaseData")
    public CommonResult updPurchaseData(@RequestBody OriginalFilmPurchase originalFilmPurchase) {
        return originalFilmPurchaseService.updPurchaseData(originalFilmPurchase);
    }

    @ApiOperation(value = "原片采购删除")
    @PreAuthorize("@ss.hasPermi('original:purchase:delPurchaseData')")
    @PostMapping("/delPurchaseData")
    public CommonResult delPurchaseData(String ids) {
        return originalFilmPurchaseService.delPurchaseData(ids);
    }

    @ApiOperation(value = "导出原片采购单")
    @PostMapping("/exportPurchase")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "采购id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('original:purchase:exportPurchaseData')")
    public void exportPurchase(HttpServletResponse response, String ids) {
        originalFilmPurchaseService.exportPurchase(response, ids);
    }

    @ApiOperation(value = "导出原片采购明细")
    @PostMapping("/exportPurchaseData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "采购id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('original:purchase:exportPurchaseData')")
    public void exportPurchaseData(HttpServletResponse response, String ids) {
        originalFilmPurchaseService.exportPurchaseData(response, ids);
    }

}
