package com.erp.sales.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.Outsourced;
import com.erp.floor.pojo.sales.domain.OutsourcedProducts;
import com.erp.sales.service.OutsourcedService;
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
 * @date： 2022-09-23 14:17
 */
@RestController
@RequestMapping("/sales/outsourced")
@Api(tags = "销售管理-外协管理")
public class OutsourcedController {
    @Resource
    private OutsourcedService outsourcedService;

    @ApiOperation("自动生成编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(outsourcedService.productionNumber());
    }

    @ApiOperation("查询外协记录")
    @PostMapping("/queryOutsourced")
    public CommonResult<List<Outsourced>> queryOutsourced(@RequestBody Outsourced outsourced) {
        return outsourcedService.queryOutsourced(outsourced);
    }

    @ApiOperation("查询可外协产品")
    @PostMapping("/queryOutsourcedProduct")
    public CommonResult<List<OrderProduct>> queryOutsourcedProduct(@RequestBody OrderProduct orderProduct) {
        return outsourcedService.queryOutsourcedProduct(orderProduct);
    }

    @ApiOperation("新增外协单")
    @PostMapping("/addOutsourced")
    @PreAuthorize("@ss.hasPermi('sales:outsourced:add')")
    public CommonResult addOutsourced(@RequestBody Outsourced outsourced) {
        return outsourcedService.addOutsourced(outsourced);
    }

    @ApiOperation("查看明细")
    @PostMapping("/detailedView")
    @PreAuthorize("@ss.hasPermi('sales:outsourced:detailedView')")
    public CommonResult<List<OutsourcedProducts>> detailedView(String id, int type) {
        return outsourcedService.detailedView(id, type);
    }

    @ApiOperation("编辑查询")
    @PostMapping("/updateQuery")
    @PreAuthorize("@ss.hasPermi('sales:outsourced:edit')")
    public CommonResult<List<OrderProduct>> updateQuery(String id) {
        return outsourcedService.updateQuery(id);
    }

    @ApiOperation("编辑保存")
    @PostMapping("/updateOutsourced")
    @PreAuthorize("@ss.hasPermi('sales:outsourced:edit')")
    public CommonResult updateOutsourced(@RequestBody Outsourced outsourced) {
        return outsourcedService.updateOutsourced(outsourced);
    }

    @ApiOperation("删除外协")
    @PostMapping("/delOutsourced")
    @PreAuthorize("@ss.hasPermi('sales:outsourced:delOutsourced')")
    public CommonResult delOutsourced(String ids) {
        return outsourcedService.delOutsourced(ids);
    }

    @ApiOperation("审核-消审")
    @PostMapping("/outsourcedExamine")
    public CommonResult outsourcedExamine(String ids, int type, String people, String text) {
        return outsourcedService.outsourcedExamine(ids, type, people, text);
    }

    @ApiOperation(value = "导出外协记录")
    @PostMapping("/exportOutsourced")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "外协id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:outsourced:exportOutsourcedDetailed')")
    public void exportOutsourced(HttpServletResponse response, String ids) {
        outsourcedService.exportOutsourced(response , ids);
    }

    @ApiOperation(value = "导出外协记录明细")
    @PostMapping("/exportOutsourcedDetailed")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "外协id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:outsourced:exportOutsourcedDetailed')")
    public void exportOutsourcedDetailed(HttpServletResponse response, String ids) {
        outsourcedService.exportOutsourcedDetailed(response , ids);
    }


}
