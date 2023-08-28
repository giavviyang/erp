package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.*;
import com.erp.floor.pojo.sales.domain.vo.DeliverRecordVo;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.floor.pojo.sales.domain.vo.PackingRecordVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 11:06
 */
public interface DeliverRecordService extends IService<DeliverRecord> {

    /**
     * 自动生成发货编号
     */
    String productionNumber();

    /**
     * 查询发货数据
     *
     * @param deliverRecordVo 发货查询类
     */
    CommonResult<List<DeliverRecord>> queryDeliver(DeliverRecordVo deliverRecordVo);

    /**
     * 获取可发货产品
     *
     * @param orderRecordVo
     */
    CommonResult<List<OrderProduct>> obtainOrderProduct(OrderRecordVo orderRecordVo);

    /**
     * 打包获取
     *
     * @param packingRecordVo
     */
    CommonResult<List<PackingBusiness>> obtainPackProduct(PackingRecordVo packingRecordVo);

    /**
     * 新增发货
     *
     * @param deliverRecord 发货信息
     */
    CommonResult saveDeliver(DeliverRecord deliverRecord);

    /**
     * 查看明细
     *
     * @param deliverId 发货id
     */
    CommonResult<DeliverRecord> detailsView(String deliverId);

    /**
     * 编辑发货
     *
     * @param deliverRecord 发货对象
     */
    CommonResult updateDeliver(DeliverRecord deliverRecord);

    /**
     * 审核发货
     *
     * @param deliverId    发货id
     * @param reviewCode   审核状态
     * @param reviewPerson 审核人
     * @param reviewReason 审核不通过原因
     */
    CommonResult reviewDeliver(String deliverId, Integer reviewCode, String reviewPerson, String reviewReason);

    /**
     * 消审
     *
     * @param deliverId 发货id
     */
    CommonResult cancelReview(String deliverId);

    /**
     * 删除发货
     * @param deliverIds 发货id
     */
    CommonResult delDeliver(String deliverIds);

    /**
     * 回执信息查询
     * @param deliverId 发货id
     */
    CommonResult<List<DeliverReceipt>> queryReceiptData(String deliverId);

    /**
     * 回执文件查询
     * @param deliverReceiptId 发货回执id
     */
    CommonResult<List<DeliverReceiptFile>> queryReceipt(String deliverReceiptId);

    /**
     * 回执货架查询
     * @param deliverReceiptId 发货回执id
     */
    CommonResult<List<ShelfRecord>> queryDeliverShelf(String deliverReceiptId);

    /**
     * 回执文件上传
     * @param deliverId 发货id
     * @param deliverReceiptId 发货回执id
     * @param file 文件
     */
    CommonResult uploadReceipt(String deliverId, String deliverReceiptId, MultipartFile file);

    /**
     * 回执文件下载
     * @param id 文件id
     * @param response 请求头
     */
    void downloadReceiptFile(String id, HttpServletResponse response);

    /**
     *  回执文件删除
     * @param id 文件id
     */
    CommonResult delReceiptFile(String id);

    /**
     * 新增回执
     * @param deliverReceipt 回执信息
     * @return
     */
    CommonResult receiptShelf(DeliverReceipt deliverReceipt);

    /**
     * 导出发货明细
     * @param ids 发货id集合
     */
    void exportDeliverBus(HttpServletResponse response, String ids);

    /**
     * 导出发货单
     * @param ids 发货id集合
     */
    void exportDeliver(HttpServletResponse response, String ids);

    /**
     * 订单管理-查询发货明细
     * @param id 订单id
     */
    CommonResult<List<DeliverBusiness>> queryDeliverBus(String id);

    /**
     * 订单管理-查询发货进度
     * @param id 订单id
     */
    CommonResult<List<DeliverRecord>> queryDeliverProgress(String id);

}
