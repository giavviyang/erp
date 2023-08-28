package com.erp.sales.controller;

import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OrderEnclosure;
import com.erp.sales.service.OrderEnclosureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-08 13:54
 */
@RestController
@RequestMapping("/sales/orderEnclosure")
@Api(tags = "销售管理-订单附件管理")
public class OrderEnclosureController {
    @Resource
    private OrderEnclosureService orderEnclosureService;

    @ApiOperation("附件信息查询")
    @PostMapping("/queryEnclosure")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId",value = "订单id" , dataType = "String", paramType = "query"),
    })
    public CommonResult<List<OrderEnclosure>> queryEnclosure(String orderId) {
        return orderEnclosureService.queryEnclosure(orderId);
    }

    @ApiOperation("上传附件")
    @PostMapping("/uploadEnclosure")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId",value = "订单id" , dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "file",value = "附件" , dataType = "file", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:orderEnclosure:uploadEnclosure')")
    public CommonResult uploadEnclosure(MultipartFile file ,String orderId) {
        return orderEnclosureService.uploadEnclosure( file , orderId);
    }

    @ApiOperation("下载附件")
    @PostMapping("/downloadEnclosure")
    public void downloadEnclosure(String id, HttpServletResponse response) {
        orderEnclosureService.downloadEnclosure(id, response);
    }

    @ApiOperation("删除附件")
    @PostMapping("/delEnclosure")
    public CommonResult delEnclosure(String id) {
        return orderEnclosureService.delEnclosure(id);
    }
}
