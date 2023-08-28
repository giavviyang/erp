package com.erp.sales.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.PackingBusiness;
import com.erp.floor.pojo.sales.domain.PackingRecord;
import com.erp.floor.pojo.sales.domain.vo.PackingRecordVo;
import com.erp.sales.service.OrderProductService;
import com.erp.sales.service.PackingRecordService;
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

import static com.erp.common.utils.PageUtils.startPage;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-24 20:42
 */
@RestController
@RequestMapping("/sales/pack")
@Api(tags = "销售管理-打包管理")
public class PackingRecordController {
    @Resource
    private PackingRecordService packingRecordService;
    @Resource
    private OrderProductService orderProductService;

    @ApiOperation("自动生成编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(packingRecordService.productionNumber());
    }

    @ApiOperation("打包数据查询")
    @PostMapping("/queryPackData")
    public CommonResult<List<PackingRecord>> queryPackData(@RequestBody PackingRecordVo packingRecordVo) {
        return packingRecordService.queryPackData(packingRecordVo);
    }

    @ApiOperation("查询可打包产品")
    @PostMapping("/queryPackProduct")
    public CommonResult<List<OrderProduct>> queryPackProduct(@RequestBody OrderProduct orderProduct) {
        return orderProductService.queryPackProduct(orderProduct);
    }

    @ApiOperation("新增打包")
    @PostMapping("/addPack")
    @PreAuthorize("@ss.hasPermi('sales:pack:addPack')")
    public CommonResult addPack(@RequestBody PackingRecord packingRecord) {
        return packingRecordService.addPack(packingRecord);
    }

    @ApiOperation("查看明细")
    @PostMapping("/detailsView")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "打包id" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:pack:info')")
    public CommonResult<List<PackingBusiness>> detailsView(String id) {
        return packingRecordService.detailsView(id);
    }

    @ApiOperation("编辑打包-查询明细")
    @PostMapping("/updateDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "打包id" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:pack:updatePack')")
    public CommonResult<List<PackingBusiness>> updateDetails(String id) {
        return packingRecordService.updateDetails(id);
    }

    @ApiOperation("编辑打包 —— 保存")
    @PostMapping("/updatePack")
    @PreAuthorize("@ss.hasPermi('sales:pack:updatePack')")
    public CommonResult updatePack(@RequestBody PackingRecord packingRecord) {
        return packingRecordService.updatePack(packingRecord);
    }

    @ApiOperation("删除打包")
    @PostMapping("/delPack")
    @PreAuthorize("@ss.hasPermi('sales:pack:delPack')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "打包id集合" , dataType = "String", paramType = "query"),
    })
    public CommonResult delPack(String ids) {
        return packingRecordService.delPack(ids);
    }

    @ApiOperation(value = "导出打包信息")
    @PostMapping("/exportPack")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "打包id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:pack:exportDetail')")
    public void exportPack(HttpServletResponse response, String ids) {
        packingRecordService.exportPack(response , ids);
    }

    @ApiOperation(value = "导出打包明细")
    @PostMapping("/exportPackDetailed")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "打包id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:pack:exportDetail')")
    public void exportPackDetailed(HttpServletResponse response, String ids) {
        packingRecordService.exportPackDetailed(response , ids);
    }

    @ApiOperation(value = "订单管理-查询打包记录")
    @PostMapping("/queryPack")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "订单id" , dataType = "String", paramType = "query"),
    })
    public CommonResult<List<PackingBusiness>> queryPack(String id) {
        return packingRecordService.queryPack(id);
    }


}
