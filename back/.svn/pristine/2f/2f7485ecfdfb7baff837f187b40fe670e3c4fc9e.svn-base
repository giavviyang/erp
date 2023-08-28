package com.erp.accessories.controller;

import com.erp.accessories.service.AccessoriesService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesType;
import io.swagger.annotations.Api;
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
 * @date： 2022-09-14 16:35
 */
@RestController
@RequestMapping("/accessories/definition")
@Api(tags = "辅料管理-辅料定义")
public class AccessoriesController {
    @Resource
    private AccessoriesService accessoriesService;

    @ApiOperation(value = "左侧树查询")
    @PostMapping("/queryTree")
    public CommonResult<List<AccessoriesType>> queryTree() {
        return accessoriesService.queryTree();
    }

    @ApiOperation(value = "新增辅料类型")
    @PreAuthorize("@ss.hasPermi('accessories:definition:addType')")
    @PostMapping("/addAccessoriesType")
    public CommonResult addAccessoriesType(@RequestBody AccessoriesType accessoriesType) {
        return accessoriesService.addAccessoriesType(accessoriesType);
    }

    @ApiOperation(value = "编辑辅料类型")
    @PreAuthorize("@ss.hasPermi('accessories:definition:editType')")
    @PostMapping("/updAccessoriesType")
    public CommonResult updAccessoriesType(@RequestBody AccessoriesType accessoriesType) {
        return accessoriesService.updAccessoriesType(accessoriesType);
    }

    @ApiOperation(value = "删除辅料类型")
    @PreAuthorize("@ss.hasPermi('accessories:definition:delType')")
    @PostMapping("/delAccessoriesType")
    public CommonResult delAccessoriesType(String id) {
        return accessoriesService.delAccessoriesType(id);
    }

    @ApiOperation(value = "辅料数据查询")
    @PostMapping("/queryAccessoriesFilm")
    public CommonResult<List<Accessories>> queryAccessoriesFilm(@RequestBody Accessories accessories) {
        return accessoriesService.queryAccessories(accessories);
    }

    @ApiOperation(value = "辅料新增")
    @PreAuthorize("@ss.hasPermi('accessories:definition:add')")
    @PostMapping("/addAccessoriesFilm")
    public CommonResult addAccessoriesFilm(@RequestBody Accessories accessories) {
        return accessoriesService.addAccessories(accessories);
    }

    @ApiOperation(value = "辅料编辑")
    @PreAuthorize("@ss.hasPermi('accessories:definition:edit')")
    @PostMapping("/updAccessoriesFilm")
    public CommonResult updAccessoriesFilm(@RequestBody Accessories accessories) {
        return accessoriesService.updAccessories(accessories);
    }

    @ApiOperation(value = "辅料删除")
    @PreAuthorize("@ss.hasPermi('accessories:definition:del')")
    @PostMapping("/delAccessoriesFilm")
    public CommonResult delAccessoriesFilm(String ids) {
        return accessoriesService.delAccessories(ids);
    }

    @ApiOperation(value = "导出辅料")
    @PreAuthorize("@ss.hasPermi('accessories:definition:export')")
    @PostMapping("/exportAccessoriesFilm")
    public void exportAccessoriesFilm(HttpServletResponse response, String ids) {
        accessoriesService.exportAccessories(response, ids);
    }

}
