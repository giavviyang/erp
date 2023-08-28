package com.erp.sales.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.DeliverBusiness;
import com.erp.floor.pojo.sales.domain.ReturnRecord;
import com.erp.sales.service.ReturnRecordService;
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
 * @date： 2022-09-05 14:43
 */
@RestController
@RequestMapping("/sales/return")
@Api(tags = "销售管理-退货管理")
public class ReturnRecordController {
    @Resource
    private ReturnRecordService returnRecordService;

    @ApiOperation("自动生成编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(returnRecordService.productionNumber());
    }

    @ApiOperation("退货数据查询")
    @PostMapping("/queryData")
    public CommonResult<List<ReturnRecord>> queryData(@RequestBody ReturnRecord returnRecord) {
        return returnRecordService.queryData(returnRecord);
    }

    @ApiOperation("发货数据查询")
    @PostMapping("/queryDeliverData")
    public CommonResult<List<DeliverBusiness>> queryDeliverData(@RequestBody DeliverBusiness deliverBusiness) {
        return returnRecordService.queryDeliverData(deliverBusiness);
    }

    @ApiOperation("新增退货")
    @PostMapping("/addReturnData")
    @PreAuthorize("@ss.hasPermi('sales:return:addReturnData')")
    public CommonResult addReturnData(@RequestBody ReturnRecord returnRecord) {
        return returnRecordService.addReturnData(returnRecord);
    }

    @ApiOperation("查看明细")
    @PostMapping("/reviewDetailed")
    @PreAuthorize("@ss.hasPermi('sales:return:reviewDetailed')")
    public CommonResult reviewDetailed(String id) {
        return returnRecordService.reviewDetailed(id);
    }

    @ApiOperation("编辑退货")
    @PostMapping("/updateReturnData")
    @PreAuthorize("@ss.hasPermi('sales:return:updateReturnData')")
    public CommonResult updateReturnData(@RequestBody ReturnRecord returnRecord) {
        return returnRecordService.updateReturnData(returnRecord);
    }

    @ApiOperation("删除退货")
    @PostMapping("/delReturnData")
    @PreAuthorize("@ss.hasPermi('sales:return:delReturnData')")
    public CommonResult delReturnData(String ids) {
        return returnRecordService.delReturnData(ids);
    }

    @ApiOperation("退货审核")
    @PostMapping("/examineReturnData")
    @PreAuthorize("@ss.hasPermi('sales:return:examineReturnData')")
    public CommonResult examineReturnData(String id, int examineStarts, String examinePerson, String examineReason) {
        return returnRecordService.examineReturnData(id, examineStarts, examinePerson, examineReason);
    }

    @ApiOperation("退货消审")
    @PostMapping("/cancelExamine")
    @PreAuthorize("@ss.hasPermi('sales:return:cancelExamine')")
    public CommonResult cancelExamine(String id) {
        return returnRecordService.cancelExamine(id);
    }

    @ApiOperation(value = "导出退货单")
    @PostMapping("/exportReturn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "退货id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:return:exportReturnDetailed')")
    public void exportReturn(HttpServletResponse response, String ids) {
        returnRecordService.exportReturn(response , ids);
    }

    @ApiOperation(value = "导出退货明细")
    @PostMapping("/exportReturnDetailed")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "退货id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:return:exportReturnDetailed')")
    public void exportReturnDetailed(HttpServletResponse response, String ids) {
        returnRecordService.exportReturnDetailed(response , ids);
    }

}
