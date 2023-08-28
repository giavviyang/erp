package com.erp.original.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.original.domain.OriginalFilm;
import com.erp.floor.pojo.original.domain.OriginalFilmType;
import com.erp.floor.pojo.sales.domain.Contract;
import com.erp.original.service.OriginalFilmService;
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
 * @date： 2022-09-09 10:21
 */
@RestController
@RequestMapping("/originalFilm/definition")
@Api(tags = "原片管理-原片定义")
public class OriginalFilmController extends BaseController {
    @Resource
    private OriginalFilmService originalFilmService;

    @ApiOperation(value = "左侧树查询")
    @PostMapping("/queryTree")
    public CommonResult<List<OriginalFilmType>> queryTree() {
        return originalFilmService.queryTree();
    }

    @ApiOperation(value = "新增原片类型")
    @PreAuthorize("@ss.hasPermi('originalFilm:definition:addType')")
    @PostMapping("/addOriginalType")
    public CommonResult addOriginalType(@RequestBody OriginalFilmType originalFilmType) {
        return originalFilmService.addOriginalType(originalFilmType);
    }

    @ApiOperation(value = "编辑原片类型")
    @PreAuthorize("@ss.hasPermi('originalFilm:definition:editType')")
    @PostMapping("/updOriginalType")
    public CommonResult updOriginalType(@RequestBody OriginalFilmType originalFilmType) {
        return originalFilmService.updOriginalType(originalFilmType);
    }

    @ApiOperation(value = "删除原片类型")
    @PreAuthorize("@ss.hasPermi('originalFilm:definition:delType')")
    @PostMapping("/delOriginalType")
    public CommonResult delOriginalType(String id) {
        return originalFilmService.delOriginalType(id);
    }

    @ApiOperation(value = "原片数据查询")
    @PostMapping("/queryOriginalFilm")
    public CommonResult<List<OriginalFilm>> queryOriginalFilm(@RequestBody OriginalFilm originalFilm) {
        return originalFilmService.queryOriginalFilm(originalFilm);
    }

    @ApiOperation(value = "原片新增")
    @PreAuthorize("@ss.hasPermi('originalFilm:definition:addOriginalFilm')")
    @PostMapping("/addOriginalFilm")
    public CommonResult addOriginalFilm(@RequestBody OriginalFilm originalFilm) {
        return originalFilmService.addOriginalFilm(originalFilm);
    }

    @ApiOperation(value = "原片编辑")
    @PreAuthorize("@ss.hasPermi('originalFilm:definition:updOriginalFilm')")
    @PostMapping("/updOriginalFilm")
    public CommonResult updOriginalFilm(@RequestBody OriginalFilm originalFilm) {
        return originalFilmService.updOriginalFilm(originalFilm);
    }

    @ApiOperation(value = "原片删除")
    @PreAuthorize("@ss.hasPermi('originalFilm:definition:delOriginalFilm')")
    @PostMapping("/delOriginalFilm")
    public CommonResult delOriginalFilm(String ids) {
        return originalFilmService.delOriginalFilm(ids);
    }

    @ApiOperation(value = "导出原片")
    @PreAuthorize("@ss.hasPermi('originalFilm:definition:exportOriginalFilm')")
    @PostMapping("/exportOriginalFilm")
    public void exportOriginalFilm(HttpServletResponse response, String ids) {
        originalFilmService.exportOriginalFilm(response, ids);
    }


}
