package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-21 13:46
 */
public interface OrderService extends IService<OrderRecord> {

    /**
     * 自动生成编号
     *
     * @return 编号
     */
    String productionNumber();

    /**
     * 校验编号是否重复
     *
     * @param orderRecord
     * @return
     */
    String checkNumber(OrderRecord orderRecord);

    /**
     * 订单查询
     *
     * @param orderRecordVo 订单查询vo
     * @return
     */
    CommonResult<List<OrderRecord>> queryAllOrder(OrderRecordVo orderRecordVo);

    /**
     * 新增订单
     *
     * @param orderRecord 订单对象
     * @return 新增结果
     */
    AjaxResult addOrder(OrderRecord orderRecord);

    /**
     * 新增订单缓存（供返回按钮使用）
     *
     * @param orderRecord 订单对象
     * @return 新增结果
     */
    AjaxResult addOrderCache(OrderRecord orderRecord);

    /**
     * 查询产品明细
     *
     * @param orderProduct 订单产品类
     * @return
     */
    CommonResult<List<OrderProduct>> queryProduct(OrderProduct orderProduct);

    /**
     * 校验订单状态
     *
     * @param orderId
     * @return
     */
    AjaxResult checkOrder(String orderId);

    /**
     * 修改订单
     *
     * @param orderRecord 订单对象
     * @return
     */
    AjaxResult updateOrder(OrderRecord orderRecord);

    /**
     * 软删除
     *
     * @param orderId 订单对象
     * @return
     */
    AjaxResult updateOrderDel(String orderId);

    /**
     * 尺寸审核
     *
     * @param orderId    订单id
     * @param fieldName  审核字段
     * @param fieldValue 审核结果
     * @return
     */
    AjaxResult auditSize(String orderId, String fieldName, Integer fieldValue);

    /**
     * 订单审核
     *
     * @param orderId       订单id
     * @param orderResult   审核结果
     * @param reviewedBy    审核人
     * @param failureReason 审核不通过原因
     */
    AjaxResult auditOrder(String orderId, Integer orderResult, String reviewedBy, String failureReason);

    /**
     * 订单消审
     *
     * @param orderId 订单id
     * @return
     */
    AjaxResult undoAudit(String orderId);

    /**
     * 删除订单
     *
     * @param orderIds
     * @return
     */
    CommonResult delOrder(String orderIds);

    /**
     * 导出任务单
     *
     * @param orderId 订单id
     */
    ResponseEntity<byte[]> exportTask(String orderId) throws IOException;

    /**
     * 导出订单
     * @param ids 订单集合
     */
    void exportOrder(HttpServletResponse response, String ids);

    /**
     * 新建流程卡---订单查询
     *
     * @param orderRecordVo 订单查询vo
     * @return
     */
    CommonResult<List<OrderRecord>> queryFlowOrder(OrderRecordVo orderRecordVo);

    /**
     * 还原订单
     * @param orderId 订单id
     */
    AjaxResult reductionOrder(String orderId);

    /**
     * 删除流程卡
     * @param ids
     * @return
     */
    CommonResult delFlow(String ids);

    /**
     * 下载Excel模板
     */
    ResponseEntity<byte[]> downloadTemplate() throws IOException;

    /**
     *
     * @return
     * @throws IOException
     */
    CommonResult<List<OrderProduct>> importTemplate(MultipartFile file) throws IOException;
}
