package com.erp.original.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.original.domain.OriginalFilmPurchase;
import com.erp.floor.pojo.original.domain.YpDamage;
import com.erp.floor.pojo.original.domain.YpDamageDetail;
import com.erp.original.service.YpDamageService;
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
 * @date： 2022-09-14 10:17
 */
@RestController
@RequestMapping(value = "/original/damage")
@Api(tags = "原片管理-原片报损")
public class YpDamageController {
    @Resource
    private YpDamageService ypDamageService;


    /**
     * 自动生成编号
     *
     * @return 编号
     */
    @ApiOperation("生成原片报损编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(ypDamageService.productionNumber());
    }


    @ApiOperation(value = "报损数据查询")
    @PostMapping("/queryDamageData")
    public CommonResult<List<YpDamage>> queryDamageData(@RequestBody YpDamage ypDamage) {
        return ypDamageService.queryDamageData(ypDamage);
    }

    @ApiOperation(value = "查看明细")
    @PreAuthorize("@ss.hasPermi('originalFilm:damage:viewDetail')")
    @PostMapping("/viewDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报损id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<YpDamageDetail>> viewDetail(String id) {
        return ypDamageService.viewDetail(id);
    }

    @ApiOperation(value = "新增报损")
    @PreAuthorize("@ss.hasPermi('originalFilm:damage:addDamageData')")
    @PostMapping("/addDamageData")
    public CommonResult addDamageData(@RequestBody YpDamage ypDamage) {
        return ypDamageService.addDamageData(ypDamage);
    }

    @ApiOperation(value = "编辑报损查询")
    @PostMapping("/updDamageDataQuery")
    public CommonResult updDamageDataQuery(String id) {
        return ypDamageService.updDamageDataQuery(id);
    }

    @ApiOperation(value = "编辑报损")
    @PreAuthorize("@ss.hasPermi('originalFilm:damage:updDamageData')")
    @PostMapping("/updDamageData")
    public CommonResult updDamageData(@RequestBody YpDamage ypDamage) {
        return ypDamageService.updDamageData(ypDamage);
    }

    @ApiOperation(value = "删除报损")
    @PreAuthorize("@ss.hasPermi('originalFilm:damage:delDamageData')")
    @PostMapping("/delDamageData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "报损id集合", dataType = "String", paramType = "query"),
    })
    public CommonResult delDamageData(String ids) {
        return ypDamageService.delDamageData(ids);
    }

    @ApiOperation(value = "导出报损记录")
    @PostMapping("/exportDamage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('original:damage:exportDamageDetails')")
    public void exportDamage(HttpServletResponse response, String ids) {
        ypDamageService.exportDamage(response, ids);
    }

    @ApiOperation(value = "导出报损明细")
    @PostMapping("/exportDamageDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('original:damage:exportDamageDetails')")
    public void exportDamageDetails(HttpServletResponse response, String ids) {
        ypDamageService.exportDamageDetails(response, ids);
    }


}
