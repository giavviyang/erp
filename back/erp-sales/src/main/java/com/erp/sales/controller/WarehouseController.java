package com.erp.sales.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.mapper.sales.OrderMapper;
import com.erp.floor.mapper.sales.OrderProductMapper;
import com.erp.floor.pojo.sales.domain.CpProductRecord;
import com.erp.floor.pojo.sales.domain.CpRecord;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.sales.service.OrderService;
import com.erp.sales.service.WarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-20 15:21
 */
@RestController
@RequestMapping("/order/warehouse")
@Api(tags = "销售管理-成品仓库")
public class WarehouseController {
    @Resource
    private WarehouseService warehouseService;


    @ApiOperation(value = "查询已入库订单")
    @PostMapping("/queryWarehouseOrder")
    public CommonResult<List<OrderRecord>> queryWarehouseOrder(@RequestBody OrderRecordVo orderProductVo) {
        return warehouseService.queryWarehouseOrder(orderProductVo);
    }

    @ApiOperation(value = "查询订单产品")
    @PostMapping("/queryOrderProduct")
    public CommonResult<List<OrderProduct>> queryOrderProduct(String id) {
        return warehouseService.queryOrderProduct(id);
    }

    @ApiOperation(value = "查询未完成入库订单产品")
    @PreAuthorize("@ss.hasPermi('sales:warehouse:queryProduct')")
    @PostMapping("/queryProduct")
    public CommonResult<List<OrderProduct>> queryProduct(@RequestBody OrderProduct orderProduct) {
        return warehouseService.queryProduct(orderProduct);
    }

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
        return AjaxResult.success(warehouseService.productionNumber(type));
    }

    @ApiOperation(value = "操作出入库")
    @PostMapping("/productWarehouse")
    public CommonResult productWarehouse(@RequestBody CpRecord cpRecord) {
        return warehouseService.productWarehouse(cpRecord);
    }

    @ApiOperation(value = "查询操作信息")
    @PreAuthorize("@ss.hasPermi('sales:warehouse:queryWarehouseData')")
    @PostMapping("/queryWarehouseData")
    public CommonResult<List<CpRecord>> queryWarehouseData(@RequestBody CpRecord cpRecord) {
        return warehouseService.queryWarehouseData(cpRecord);
    }

    @ApiOperation(value = "查询操作信息明细")
    @PostMapping("/warehouseDeliverData")
    public CommonResult<List<CpProductRecord>> warehouseDeliverData(String id) {
        return warehouseService.warehouseDeliverData(id);
    }

}
