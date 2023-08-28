package com.erp.sales.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.sales.service.OrderProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 订单产品控制层
 *
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-26 18:28
 */
@RestController
@RequestMapping("/sales/orderProduct")
@Api(tags = "销售管理-订单产品")
public class OrderProductController {
    @Resource
    private OrderProductService orderProductService;

    /**
     * 查看工艺
     *
     * @param orderId 订单id
     * @return
     */
    @ApiOperation(value = "查看工艺")
    @PostMapping("/queryProcess")
    public AjaxResult queryProcess(String orderId) {
        return AjaxResult.success(orderProductService.queryProcess(orderId));
    }

    /**
     * 修改产品工艺
     *
     * @param productProcess 集合
     * @return
     */
    @ApiOperation(value = "修改产品工艺")
    @PostMapping("/updateProcess")
    public AjaxResult updateProcess(@RequestBody List<Map<String, Object>> productProcess) {
        return orderProductService.updateProcess(productProcess);
    }

    @ApiOperation(value = "导出产品")
    @PreAuthorize("@ss.hasPermi('sales:orderProduct:exportProduct')")
    @PostMapping("/exportProduct")
    public void exportProduct(HttpServletResponse response, String ids) {
        orderProductService.exportProduct(response, ids);
    }

    @ApiOperation(value = "导出迪赛模板")
    @PostMapping("/exportMachineFlow")
    public void exportMachineFlow(HttpServletResponse response, String id) {
        orderProductService.exportMachineFlow(response, id);
    }

}
