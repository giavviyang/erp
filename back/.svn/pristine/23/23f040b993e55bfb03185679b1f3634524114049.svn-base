package com.erp.accessories.controller;

import com.erp.accessories.service.AccessoriesDamageService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesDamage;
import com.erp.floor.pojo.accessories.domain.AccessoriesDamageDetail;
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
 * @date： 2022-09-15 10:59
 */
@RestController
@RequestMapping("/accessories/damage")
@Api(tags = "辅料管理-辅料报损")
public class AccessoriesDamageController {

    @Resource
    private AccessoriesDamageService accessoriesDamageService;


    /**
     * 自动生成编号
     *
     * @return 编号
     */
    @ApiOperation("生成辅料报损编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(accessoriesDamageService.productionNumber());
    }


    @ApiOperation(value = "报损数据查询")
    @PostMapping("/queryDamageData")
    public CommonResult<List<AccessoriesDamage>> queryDamageData(@RequestBody AccessoriesDamage accessoriesDamage) {
        return accessoriesDamageService.queryDamageData(accessoriesDamage);
    }

    @ApiOperation(value = "查看明细")
    @PreAuthorize("@ss.hasPermi('accessories:damage:detail')")
    @PostMapping("/viewDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报损id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<AccessoriesDamageDetail>> viewDetail(String id) {
        return accessoriesDamageService.viewDetail(id);
    }

    @ApiOperation(value = "新增报损")
    @PreAuthorize("@ss.hasPermi('accessories:damage:add')")
    @PostMapping("/addDamageData")
    public CommonResult addDamageData(@RequestBody AccessoriesDamage accessoriesDamage) {
        return accessoriesDamageService.addDamageData(accessoriesDamage);
    }

    @ApiOperation(value = "编辑报损数据查询")
    @PostMapping("/updDamageDataQuery")
    public CommonResult<List<Accessories>> updDamageDataQuery(String id) {
        return accessoriesDamageService.updDamageDataQuery(id);
    }

    @ApiOperation(value = "编辑报损")
    @PreAuthorize("@ss.hasPermi('accessories:damage:edit')")
    @PostMapping("/updDamageData")
    public CommonResult updDamageData(@RequestBody AccessoriesDamage accessoriesDamage) {
        return accessoriesDamageService.updDamageData(accessoriesDamage);
    }

    @ApiOperation(value = "删除报损")
    @PreAuthorize("@ss.hasPermi('accessories:damage:del')")
    @PostMapping("/delDamageData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "报损id集合", dataType = "String", paramType = "query"),
    })
    public CommonResult delDamageData(String ids) {
        return accessoriesDamageService.delDamageData(ids);
    }

    @ApiOperation(value = "导出报损记录")
    @PostMapping("/exportDamage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('accessories:damage:exportDamageDetails')")
    public void exportDamage(HttpServletResponse response, String ids) {
        accessoriesDamageService.exportDamage(response, ids);
    }

    @ApiOperation(value = "导出报损明细")
    @PostMapping("/exportDamageDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('accessories:damage:exportDamageDetails')")
    public void exportDamageDetails(HttpServletResponse response, String ids) {
        accessoriesDamageService.exportDamageDetails(response, ids);
    }

}
