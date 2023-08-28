package com.erp.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.produce.domain.Scheduling;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.OrderRecord;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/19 15:06
 */
public interface SchedulingService extends IService<Scheduling> {

    /**
     * 新增排产
     * @param scheduling
     * @return
     */
    CommonResult addScheduling(Scheduling scheduling);

    /**
     * 修改排产
     * @param scheduling
     * @return
     */
    CommonResult editScheduling(Scheduling scheduling);

    /**
     * 删除排产
     * @param
     * @return
     */
     CommonResult delScheduling(String id);

    /**
     * 获取列表
     * @param
     * @return
     */
    CommonResult getSchedulingList(Scheduling scheduling);

    /**
     * 获取详细信息
     * @return
     */
    CommonResult getInfo(String id);

    /**
     * 获取订单
     * @return
     */
    CommonResult getOrderList(OrderRecord orderRecord);

    /**
     * 获取流程卡
     * @return
     */
    CommonResult getFlowCardList(FlowCard flowCard, Map<String,String> params);

    /**
     * 导出excel
     * @param response
     * @param ids
     */
    void exportAll(HttpServletResponse response, String ids);

    /**
     * 导出详情excel
     * @param response
     * @param id
     */
    void exportDetail(HttpServletResponse response, String id);
}
