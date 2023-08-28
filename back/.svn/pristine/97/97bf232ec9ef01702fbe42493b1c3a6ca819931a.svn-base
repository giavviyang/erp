package com.erp.accessories.controller;

import com.erp.accessories.service.AccessoriesOperationService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.AccessoriesOperation;
import com.erp.floor.pojo.accessories.domain.AccessoriesOperationDetail;
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
 * @date： 2022-09-15 10:03
 */
@RestController
@RequestMapping("/accessories/operation")
@Api(tags = "辅料管理-辅料仓库")
public class AccessoriesOperationController {
    @Resource
    private AccessoriesOperationService operationService;

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
        return AjaxResult.success(operationService.productionNumber(type));
    }


    @ApiOperation(value = "辅料出-入-盘库")
    @PostMapping("/accessoriesWarehouse")
    public CommonResult accessoriesWarehouse(@RequestBody AccessoriesOperation accessoriesOperation) {
        return operationService.accessoriesWarehouse(accessoriesOperation);
    }

    @ApiOperation(value = "采购单入库")
    @PreAuthorize("@ss.hasPermi('accessories:operation:warehousing')")
    @PostMapping("/purchaseWarehouse")
    public CommonResult purchaseWarehouse(@RequestBody AccessoriesOperation accessoriesOperation) {
        return operationService.purchaseWarehouse(accessoriesOperation);
    }

    @ApiOperation(value = "查询出入盘库记录")
    @PostMapping("/warehouseRecord")
    public CommonResult<List<AccessoriesOperation>> warehouseRecord(@RequestBody AccessoriesOperation accessoriesOperation) {
        return operationService.warehouseRecord(accessoriesOperation);
    }

    @ApiOperation(value = "查看出入盘库明细")
    @PostMapping("/warehouseDetailsView")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "入库id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<AccessoriesOperationDetail>> warehouseDetailsView(String id) {
        return operationService.warehouseDetailsView(id);
    }

    @ApiOperation(value = "删除出入库记录")
    @PostMapping("/delWarehouseDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "入库id集合", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "入库类型（0入库，1出库）", dataType = "int", paramType = "query"),
    })
    public CommonResult delWarehouseDetails(String ids, Integer type) {
        return operationService.delWarehouseDetails(ids, type);
    }

    @ApiOperation(value = "删除盘库记录")
    @PostMapping("/delInventoryDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "盘库id", dataType = "String", paramType = "query"),
    })
    public CommonResult delInventoryDetails(String id) {
        return operationService.delInventoryDetails(id);
    }

    @ApiOperation(value = "导出出入盘库记录")
    @PostMapping("/exportWarehouse")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('accessories:operation:export')")
    public void exportWarehouse(HttpServletResponse response, String ids, String type) {
        operationService.exportWarehouse(response, ids, type);
    }

    @ApiOperation(value = "导出出入盘库明细")
    @PostMapping("/exportWarehouseDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('accessories:operation:export')")
    public void exportWarehouseDetails(HttpServletResponse response, String ids, String type) {
        operationService.exportWarehouseDetails(response, ids, type);
    }
    
}
