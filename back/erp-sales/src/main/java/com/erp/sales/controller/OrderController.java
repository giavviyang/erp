package com.erp.sales.controller;

import com.erp.common.constant.Constants;
import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.sales.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 订单控制层
 *
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-21 14:07
 */
@RestController
@RequestMapping("/sales/order")
@Api(tags = "销售管理-订单管理")
public class OrderController extends BaseController {
    @Resource
    private OrderService orderService;

       /**
     * 自动生成编号
     *
     * @return 编号
     */
    @ApiOperation("自动生成编号")
    @PostMapping("/productionNumber")
    public AjaxResult productionNumber() {
        return AjaxResult.success(orderService.productionNumber());
    }

    @ApiOperation("订单状态校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId",value = "订单id" , dataType = "String", paramType = "query"),
    })
    @PostMapping("/checkOrder")
    public AjaxResult checkOrder(String orderId){
        return orderService.checkOrder(orderId);
    }

    @ApiOperation(value = "查询订单")
    @PostMapping("/list")
    public CommonResult<List<OrderRecord>> list(@RequestBody OrderRecordVo orderProductVo) {
        return orderService.queryAllOrder(orderProductVo);
    }

    @ApiOperation(value = "新增订单")
    @PreAuthorize("@ss.hasPermi('sales:order:add')")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody OrderRecord orderRecord) {
        //先校验订单编号是否重复
        if (Constants.FAIL.equals(orderService.checkNumber(orderRecord))) {
            return AjaxResult.error("订单编号重复！");
        }
        return orderService.addOrder(orderRecord);
    }


    @ApiOperation(value = "新增订单缓存")
    @PostMapping("/addCache")
    public AjaxResult addCache(@RequestBody OrderRecord orderRecord) {
        //先校验订单编号是否重复
        if (Constants.FAIL.equals(orderService.checkNumber(orderRecord))) {
            return AjaxResult.error("订单编号重复！");
        }
        return orderService.addOrderCache(orderRecord);
    }


    /**
     * 查询产品信息
     *
     * @param orderProduct 订单产品对象
     * @return 产品数据
     */
    @ApiOperation(value = "查询产品信息")
    @PostMapping("/queryProduct")
    public CommonResult<List<OrderProduct>> queryProduct(@RequestBody OrderProduct orderProduct) {
        return orderService.queryProduct(orderProduct);
    }

    /**
     * 编辑订单
     */
    @ApiOperation(value = "编辑订单")
    @PreAuthorize("@ss.hasPermi('sales:order:update')")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody OrderRecord orderRecord) {
        return orderService.updateOrder(orderRecord);
    }

    /**
     * 废置订单
     */
    @ApiOperation(value = "废置订单")
    @PreAuthorize("@ss.hasPermi('sales:order:del')")
    @PostMapping("/del")
    public AjaxResult del(String orderId) {
        return orderService.updateOrderDel(orderId);
    }

    /**
     * 产品审核
     *
     * @param orderId    订单id
     * @param fieldName  审核字段
     * @param fieldValue 审核结果
     * @return
     */
    @ApiOperation(value = "产品审核")
    @PreAuthorize("@ss.hasPermi('sales:order:auditSize')")
    @PostMapping("/auditSize")
    public AjaxResult auditSize(String orderId, String fieldName, Integer fieldValue) {
        return orderService.auditSize(orderId, fieldName, fieldValue);
    }

    /**
     * 订单审核
     *
     * @param orderId       订单id
     * @param orderResult   审核结果
     * @param reviewedBy    审核人
     * @param failureReason 审核不通过原因
     */
    @ApiOperation(value = "订单审核")
    @PreAuthorize("@ss.hasPermi('sales:order:auditOrder')")
    @PostMapping("/auditOrder")
    public AjaxResult auditOrder(String orderId, Integer orderResult, String reviewedBy, String failureReason) {
        return orderService.auditOrder(orderId, orderResult, reviewedBy, failureReason);
    }

    /**
     * 订单消审
     *
     * @param orderId 订单id
     * @return
     */
    @ApiOperation(value = "订单消审")
    @PreAuthorize("@ss.hasPermi('sales:order:undoAudit')")
    @PostMapping("/undoAudit")
    public AjaxResult undoAudit(String orderId) {
        return orderService.undoAudit(orderId);
    }

    @ApiOperation(value = "删除订单")
    @PreAuthorize("@ss.hasPermi('sales:order:delOrder')")
    @PostMapping("/delOrder")
    public CommonResult delOrder(String orderIds) {
        return orderService.delOrder(orderIds);
    }

    @ApiOperation(value = "导出任务单")
    @PostMapping("/exportTask")
    @PreAuthorize("@ss.hasPermi('sales:orderProduct:exportProduct')")
    public ResponseEntity<byte[]> exportTask(String orderId) throws IOException {
        return orderService.exportTask(orderId);
    }

    @ApiOperation(value = "导出订单")
    @PostMapping("/exportOrder")
    @PreAuthorize("@ss.hasPermi('sales:orderProduct:exportProduct')")
    public void exportOrder(HttpServletResponse response, String ids) {
        orderService.exportOrder(response, ids);
    }

    @ApiOperation(value = "还原订单")
    @PreAuthorize("@ss.hasPermi('sales:order:reductionOrder')")
    @PostMapping("/reductionOrder")
    public AjaxResult reductionOrder(String orderId) {
        return orderService.reductionOrder(orderId);
    }

    @ApiOperation(value = "删除流程卡")
    @PreAuthorize("@ss.hasPermi('sales:order:delFlow')")
    @PostMapping("/delFlow")
    public CommonResult delFlow(String ids) {
        return orderService.delFlow(ids);
    }

    @ApiOperation(value = "下载Excel模板")
    @PostMapping("/downloadTemplate")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        return orderService.downloadTemplate();
    }

    @ApiOperation(value = "导入Excel")
    @PostMapping("/importTemplate")
    public CommonResult<List<OrderProduct>> importTemplate(MultipartFile file) throws IOException {
        return orderService.importTemplate(file);
    }


}
