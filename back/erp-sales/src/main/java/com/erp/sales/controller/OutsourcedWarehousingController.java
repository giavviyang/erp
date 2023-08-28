package com.erp.sales.controller;

import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OutsourcedStockProduct;
import com.erp.floor.pojo.sales.domain.OutsourcedWarehousing;
import com.erp.sales.service.OutsourcedWarehousingService;
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
 * @date： 2022-09-23 14:16
 */
@RestController
@RequestMapping("/sales/warehousing")
@Api(tags = "销售管理-外协入库")
public class OutsourcedWarehousingController {
    @Resource
    private OutsourcedWarehousingService outsourcedWarehousingService;


    @ApiOperation("查询入库信息")
    @PostMapping("/queryData")
    public CommonResult<List<OutsourcedWarehousing>> queryData(@RequestBody OutsourcedWarehousing outsourcedWarehousing) {
        return outsourcedWarehousingService.queryData(outsourcedWarehousing);
    }

    @ApiOperation("新增入库信息")
    @PostMapping("/addData")
    public CommonResult addData(@RequestBody OutsourcedWarehousing outsourcedWarehousing) {
        return outsourcedWarehousingService.addData(outsourcedWarehousing);
    }

    @ApiOperation("查看明细")
    @PostMapping("/detailedView")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "入库id集合" , dataType = "String", paramType = "query"),
    })
    public CommonResult<List<OutsourcedStockProduct>> detailedView(String ids) {
        return outsourcedWarehousingService.detailedView(ids);
    }

    @ApiOperation("删除入库")
    @PostMapping("/delData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "入库id集合" , dataType = "String", paramType = "query"),
    })
    public CommonResult delData(String ids) {
        return outsourcedWarehousingService.delData(ids);
    }

    @ApiOperation(value = "导出入库记录")
    @PostMapping("/exportData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "入库id集合" , dataType = "String", paramType = "query"),
    })
    public void exportData(HttpServletResponse response, String ids) {
        outsourcedWarehousingService.exportData(response , ids);
    }

    @ApiOperation(value = "导出入库记录明细")
    @PostMapping("/exportDataDetailed")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "入库id集合" , dataType = "String", paramType = "query"),
    })
    public void exportDataDetailed(HttpServletResponse response, String ids) {
        outsourcedWarehousingService.exportDataDetailed(response , ids);
    }









}

