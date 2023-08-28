package com.erp.sales.controller;

import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.StringUtils;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.sales.domain.vo.FlowCardVo;
import com.erp.floor.pojo.sales.domain.vo.FlowParameter;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.sales.service.FlowCardService;
import com.erp.sales.service.OrderProductService;
import com.erp.sales.service.OrderService;
import com.erp.sales.service.ShelfDivisionBusinessService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-28 11:45
 */
@RestController
@RequestMapping("/sales/flowCard")
@Api(tags = "销售管理-流程卡管理")
public class FlowCardController {
    @Resource
    private FlowCardService flowCardService;
    @Resource
    private OrderService orderService;
    @Resource
    private OrderProductService orderProductService;
    @Resource
    private ShelfDivisionBusinessService shelfDivisionBusinessService;

    @ApiOperation("生成流程卡编号")
    @PostMapping("/generateFlowNumber")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldFlowNumber",value = "旧流程卡编号" , dataType = "String", paramType = "query"),
    })
    public CommonResult generateFlowNumber(String oldFlowNumber) {
        return CommonResult.success(flowCardService.generateFlowNumber(oldFlowNumber));
    }

    @ApiOperation("查询流程卡")
    @PostMapping("/queryList")
    public CommonResult<List<FlowCard>> queryList(@RequestBody FlowCardVo flowCardVo) {
        return flowCardService.queryList(flowCardVo);
    }

    @ApiOperation("查询流程卡明细")
    @PreAuthorize("@ss.hasPermi('sales:flowCard:queryFlowDetailed')")
    @PostMapping("/queryFlowDetailed")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flowId",value = "流程卡id" , dataType = "String", paramType = "query"),
    })
    public CommonResult<List<ShelfDivisionBusiness>> queryFlowDetailed (String flowId) {
        return flowCardService.queryFlowDetailed(flowId);
    }

    @ApiOperation(value = "查询未分架订单")
    @PostMapping("/queryFlowOrder")
    public CommonResult<List<OrderRecord>> queryFlowOrder(@RequestBody OrderRecordVo orderProductVo) {
        return orderService.queryFlowOrder(orderProductVo);
    }

    /**
     * 自然分架中查询产品信息
     * @param orderRecords
     * @return
     */
    @ApiOperation(value = "自然分架查询订单中未分架产品信息")
    @PostMapping("/selectProduct")
    public CommonResult<List<OrderProduct>> selectProduct(@RequestBody List<OrderRecord> orderRecords) {
        return CommonResult.success(orderProductService.selectProduct(orderRecords));
    }

    /**
     * 自然分架
     * @param flowParameter 产品集合 yu 参数对象
     */
    @ApiOperation("自然分架")
    @PostMapping("/automaticShelf")
    public CommonResult<List<FlowCard>> automaticShelf(@RequestBody FlowParameter flowParameter) {
        return flowCardService.automaticShelf(flowParameter);
    }

    @ApiOperation("自然分架-保存流程卡")
    @PostMapping("/saveFlowCard")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveFlowCard(@RequestBody List<FlowCard> flowCards) {
        List<ShelfDivisionBusiness> shelfDivisionBusinesses = new ArrayList<>();
        for (FlowCard flowCard : flowCards) {
            shelfDivisionBusinesses.addAll(flowCard.getList());
        }
        try {
            flowCardService.saveBatch(flowCards);
            shelfDivisionBusinessService.saveBatch(shelfDivisionBusinesses);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    @ApiOperation(value = "手动分架查询订单中未分架产品单片信息")
    @PostMapping("/selectSemiProduct")
    public CommonResult<List<ProductProcess>> selectSemiProduct(@RequestBody List<OrderRecord> orderRecords) {
        return CommonResult.success(orderProductService.selectSemiProduct(orderRecords));
    }

    @ApiOperation(value = "手动分架-保存分架信息")
    @PostMapping("/saveSemiProduct")
    public CommonResult saveSemiProduct(@RequestBody List<Map<String, Object>> processList) {
        return flowCardService.saveSemiProduct(processList);
    }

    @ApiOperation(value = "编辑流程卡-查询")
    @PostMapping("/updateQuery")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flowId",value = "流程卡id" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:flowCard:saveUpdate')")
    public CommonResult<Map<String, Object>> updateQuery(String flowId) {
        return flowCardService.updateQuery(flowId);
    }

    @ApiOperation(value = "编辑流程卡-保存分架信息")
    @PostMapping("/saveUpdate")
    @PreAuthorize("@ss.hasPermi('sales:flowCard:saveUpdate')")
    public CommonResult saveUpdate(@RequestBody Map<String, Object> processMap) {
        return flowCardService.saveUpdate(processMap);
    }

    @ApiOperation(value = "删除流程卡")
    @PostMapping("/delFlowCard")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "流程卡id" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:flowCard:delFlowCard')")
    public CommonResult delFlowCard(String id) {
        return flowCardService.delFlowCard(id);
    }


    @ApiOperation(value = "校验流程卡状态")
    @PostMapping("/checkFlowCard")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "流程卡id" , dataType = "String", paramType = "query"),
    })
    public CommonResult checkFlowCard(String id) {
        return flowCardService.checkFlowCard(id);
    }

    @ApiOperation(value = "导出流程卡明细")
    @PostMapping("/exportFlowDetailed")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "流程卡id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:flowCard:exportFlowDetail')")
    public void exportFlowDetailed(HttpServletResponse response, String ids) {
        flowCardService.exportFlowDetailed(response , ids);
    }

    @ApiOperation(value = "导出流程卡")
    @PostMapping("/exportFlow")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "流程卡id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:flowCard:exportFlowDetail')")
    public void exportFlow(HttpServletResponse response, String ids) {
        flowCardService.exportFlow(response , ids);
    }

    @ApiOperation(value = "导出迪赛perfectcut模板")
    @PostMapping("/exportMachineFlow")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "流程卡id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:flowCard:exportFlowDetail')")
    public void exportMachineFlow(HttpServletResponse response, String id){
        flowCardService.exportMachineFlow(response, id);
    }

    @ApiOperation(value = "导出迪赛optimat模板")
    @PostMapping("/exportOptima")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "流程卡id集合" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:flowCard:exportFlowDetail')")
    public void exportOptima(HttpServletResponse response, String id){
        flowCardService.exportOptima(response, id);
    }




}
