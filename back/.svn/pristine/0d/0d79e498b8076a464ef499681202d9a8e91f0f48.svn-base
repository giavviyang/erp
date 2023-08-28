package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.DeliverBusiness;
import com.erp.floor.pojo.sales.domain.ReturnRecord;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-05 14:38
 */
public interface ReturnRecordService extends IService<ReturnRecord> {

    /**
     * 自动生成编号
     */
    String productionNumber();

    /**
     * 退货数据查询
     *
     * @param returnRecord 退货对象
     */
    CommonResult<List<ReturnRecord>> queryData(ReturnRecord returnRecord);

    /**
     * 发货数据查询
     */
    CommonResult<List<DeliverBusiness>> queryDeliverData(DeliverBusiness deliverBusiness);

    /**
     * 新增退货
     *
     * @param returnRecord
     */
    CommonResult addReturnData(ReturnRecord returnRecord);

    /**
     * 查看明细
     * @param id 退货id
     */
    CommonResult reviewDetailed(String id);

    /**
     * 编辑退货
     * @param returnRecord
     */
    CommonResult updateReturnData(ReturnRecord returnRecord);

    /**
     * 删除退货
     * @param ids 退货id
     */
    CommonResult delReturnData(String ids);

    /**
     * 退货审核
     * @param id 退货id
     * @param examinePerson 审核人
     * @param examineReason examineReason
     */
    CommonResult examineReturnData(String id, int examineStarts, String examinePerson, String examineReason);

    /**
     * 退货消审
     * @param id 退货id
     */
    CommonResult cancelExamine(String id);

    /**
     * 导出退货单
     * @param response
     * @param ids 退货id集合
     */
    void exportReturn(HttpServletResponse response, String ids);

    /**
     * 导出退货明细
     * @param response
     * @param ids 退货id集合
     */
    void exportReturnDetailed(HttpServletResponse response, String ids);
}
