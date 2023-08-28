package com.erp.sales.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.sales.domain.vo.DeliverRecordVo;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.floor.pojo.sales.domain.vo.PackingRecordVo;
import com.erp.sales.service.DeliverRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 13:53
 */
@RestController
@RequestMapping("/sales/deliver")
@Api(tags = "销售管理-发货管理")
public class DeliverRecordController {
    @Resource
    private DeliverRecordService deliverRecordService;

    @ApiOperation("自动生成编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(deliverRecordService.productionNumber());
    }

    @ApiOperation("发货数据查询")
    @PostMapping("/queryDeliver")
    public CommonResult<List<DeliverRecord>> queryDeliver(@RequestBody DeliverRecordVo deliverRecordVo) {
        return deliverRecordService.queryDeliver(deliverRecordVo);
    }

    @ApiOperation("订单获取")
    @PostMapping("/obtainOrderProduct")
    public CommonResult<List<OrderProduct>> obtainOrderProduct(@RequestBody OrderRecordVo orderRecordVo) {
        return deliverRecordService.obtainOrderProduct(orderRecordVo);
    }

    @ApiOperation("打包获取")
    @PostMapping("/obtainPackProduct")
    public CommonResult<List<PackingBusiness>> obtainPackProduct(@RequestBody PackingRecordVo packingRecordVo) {
        return deliverRecordService.obtainPackProduct(packingRecordVo);
    }

    @ApiOperation("新增发货")
    @PostMapping("/saveDeliver")
    @PreAuthorize("@ss.hasPermi('sales:deliver:saveDeliver')")
    public CommonResult saveDeliver(@RequestBody DeliverRecord deliverRecord) {
        return deliverRecordService.saveDeliver(deliverRecord);
    }

    @ApiOperation("查看明细")
    @PostMapping("/detailsView")
    @PreAuthorize("@ss.hasPermi('sales:deliver:detailsView')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverId", value = "发货id", dataType = "String", paramType = "query"),
    })
    public CommonResult<DeliverRecord> detailsView(String deliverId) {
        return deliverRecordService.detailsView(deliverId);
    }

    @ApiOperation("编辑发货")
    @PostMapping("/updateDeliver")
    @PreAuthorize("@ss.hasPermi('sales:deliver:updateDeliver')")
    public CommonResult updateDeliver(@RequestBody DeliverRecord deliverRecord) {
        return deliverRecordService.updateDeliver(deliverRecord);
    }


    @ApiOperation("审核发货")
    @PostMapping("/reviewDeliver")
    @PreAuthorize("@ss.hasPermi('sales:deliver:reviewDeliver')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverId", value = "发货id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reviewCode", value = "审核状态", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reviewPerson", value = "审核人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reviewReason", value = "审核不通过原因", dataType = "String", paramType = "query"),
    })
    public CommonResult reviewDeliver(String deliverId, Integer reviewCode, String reviewPerson, String reviewReason) {
        return deliverRecordService.reviewDeliver(deliverId, reviewCode, reviewPerson, reviewReason);
    }

    @ApiOperation("消审")
    @PostMapping("/cancelReview")
    @PreAuthorize("@ss.hasPermi('sales:deliver:cancelReview')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverId", value = "发货id", dataType = "String", paramType = "query"),
    })
    public CommonResult cancelReview(String deliverId) {
        return deliverRecordService.cancelReview(deliverId);
    }

    @ApiOperation("删除发货")
    @PostMapping("/delDeliver")
    @PreAuthorize("@ss.hasPermi('sales:deliver:delDeliver')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverIds", value = "发货id", dataType = "String", paramType = "query"),
    })
    public CommonResult delDeliver(String deliverIds) {
        return deliverRecordService.delDeliver(deliverIds);
    }

    @ApiOperation("回执信息查询")
    @PreAuthorize("@ss.hasPermi('sales:deliver:queryReceiptData')")
    @PostMapping("/queryReceiptData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverId", value = "发货id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<DeliverReceipt>> queryReceiptData(String deliverId) {
        return deliverRecordService.queryReceiptData(deliverId);
    }

    @ApiOperation("回执文件查询")
    @PreAuthorize("@ss.hasPermi('sales:deliver:queryReceiptData')")
    @PostMapping("/queryReceipt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverReceiptId", value = "发货回执id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<DeliverReceiptFile>> queryReceipt(String deliverReceiptId) {
        return deliverRecordService.queryReceipt(deliverReceiptId);
    }

    @ApiOperation("回执货架查询")
    @PreAuthorize("@ss.hasPermi('sales:deliver:queryReceiptData')")
    @PostMapping("/queryDeliverShelf")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverReceiptId", value = "发货回执id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<ShelfRecord>> queryDeliverShelf(String deliverReceiptId) {
        return deliverRecordService.queryDeliverShelf(deliverReceiptId);
    }

    @ApiOperation("回执文件上传")
    @PreAuthorize("@ss.hasPermi('sales:deliver:uploadReceipt')")
    @PostMapping("/uploadReceipt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverId", value = "发货id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deliverReceiptId", value = "发货回执id", dataType = "String", paramType = "query"),
    })
    public CommonResult uploadReceipt(String deliverId, String deliverReceiptId, MultipartFile file) {
        return deliverRecordService.uploadReceipt(deliverId, deliverReceiptId, file);
    }

    @ApiOperation(value = "回执文件下载")
    @PostMapping("/downloadReceiptFile")
    public void downloadReceiptFile(String id, HttpServletResponse response) {
        deliverRecordService.downloadReceiptFile(id, response);
    }

    @ApiOperation(value = "回执文件删除")
    @PostMapping("/delReceiptFile")
    public CommonResult delReceiptFile(String id) {
        return deliverRecordService.delReceiptFile(id);
    }

    @ApiOperation(value = "新增回执")
    @PreAuthorize("@ss.hasPermi('sales:deliver:uploadReceipt')")
    @PostMapping("/receiptShelf")
    public CommonResult receiptShelf(@RequestBody DeliverReceipt deliverReceipt) {
        return deliverRecordService.receiptShelf(deliverReceipt);
    }

    @ApiOperation(value = "导出发货明细")
    @PostMapping("/exportDeliverBus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "发货id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:deliver:exportDeliverBus')")
    public void exportDeliverBus(HttpServletResponse response, String ids) {
        deliverRecordService.exportDeliverBus(response, ids);
    }

    @ApiOperation(value = "导出发货单")
    @PostMapping("/exportDeliver")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "发货id集合", dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:deliver:exportDeliverBus')")
    public void exportDeliver(HttpServletResponse response, String ids) {
        deliverRecordService.exportDeliver(response, ids);
    }

    @ApiOperation(value = "订单管理-查询发货明细")
    @PostMapping("/queryDeliverBus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<DeliverBusiness>> queryDeliverBus(String id) {
        return deliverRecordService.queryDeliverBus(id);
    }

    @ApiOperation(value = "订单管理-查询发货进度")
    @PostMapping("/queryDeliverProgress")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<DeliverRecord>> queryDeliverProgress(String id) {
        return deliverRecordService.queryDeliverProgress(id);
    }

}
