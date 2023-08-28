package com.erp.original.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.original.domain.YpOperationDetail;
import com.erp.floor.pojo.original.domain.YpWarehouseOperation;
import com.erp.original.service.YpWarehouseOperationService;
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
 * @date： 2022-09-13 13:47
 */
@RestController
@RequestMapping(value = "/original/warehouse")
@Api(tags = "原片管理-原片仓库")
public class YpWarehouseOperationController {
    @Resource
    private YpWarehouseOperationService ypWarehouseOperationService;

    /**
     * 生成入库编号
     *
     * @return 编号
     */
    @ApiOperation("自动生成编号")
    @PostMapping("/productionNumber")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "操作类型（0入库，1出库、2盘库）", dataType = "String", paramType = "query"),
    })
    public AjaxResult productionNumber(int type) {
        return AjaxResult.success(ypWarehouseOperationService.productionNumber(type));
    }


    @ApiOperation(value = "原片出-入-盘库")
    @PostMapping("/originalWarehouse")
    public CommonResult originalWarehouse(@RequestBody YpWarehouseOperation ypWarehouseOperation) {
        return ypWarehouseOperationService.originalWarehouse(ypWarehouseOperation);
    }

    @ApiOperation(value = "采购单入库")
    @PreAuthorize("@ss.hasPermi('originalFilm:warehouse:purchaseWarehouse')")
    @PostMapping("/purchaseWarehouse")
    public CommonResult purchaseWarehouse(@RequestBody YpWarehouseOperation ypWarehouseOperation) {
        return ypWarehouseOperationService.purchaseWarehouse(ypWarehouseOperation);
    }

    @ApiOperation(value = "查询出入盘库记录")
    @PostMapping("/warehouseRecord")
    public CommonResult<List<YpWarehouseOperation>> warehouseRecord(@RequestBody YpWarehouseOperation ypWarehouseOperation) {
        return ypWarehouseOperationService.warehouseRecord(ypWarehouseOperation);
    }

    @ApiOperation(value = "查看出入盘库明细")
    @PostMapping("/warehouseDetailsView")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<YpOperationDetail>> warehouseDetailsView(String id) {
        return ypWarehouseOperationService.warehouseDetailsView(id);
    }

    @ApiOperation(value = "删除入库记录")
    @PostMapping("/delWarehouseDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "入库id集合", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "入库类型（0入库，1出库）", dataType = "int", paramType = "query"),
    })
    public CommonResult delWarehouseDetails(String ids, Integer type) {
        return ypWarehouseOperationService.delWarehouseDetails(ids, type);
    }

    @ApiOperation(value = "删除盘库记录")
    @PostMapping("/delInventoryDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "盘库id", dataType = "String", paramType = "query"),
    })
    public CommonResult delInventoryDetails(String id) {
        return ypWarehouseOperationService.delInventoryDetails(id);
    }

    @ApiOperation(value = "导出出入盘库记录")
    @PostMapping("/exportWarehouse")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "int", paramType = "query"),
    })
    public void exportWarehouse(HttpServletResponse response, String ids, String type) {
        ypWarehouseOperationService.exportWarehouse(response, ids, type);
    }

    @ApiOperation(value = "导出出入盘库明细")
    @PostMapping("/exportWarehouseDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合", dataType = "String", paramType = "query"),
    })
    public void exportWarehouseDetails(HttpServletResponse response, String ids, String type) {
        ypWarehouseOperationService.exportWarehouseDetails(response, ids, type);
    }

}
