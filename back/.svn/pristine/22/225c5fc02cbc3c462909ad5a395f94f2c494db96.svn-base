package com.erp.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.produce.domain.CompletionRecord;
import com.erp.floor.pojo.produce.domain.DamageRecord;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.ShelfDivisionBusiness;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/6 11:28
 */
public interface DamageService extends IService<DamageRecord> {

    /**
     * 新增报损
     * @param record
     * @return
     */
    CommonResult insertDamage(DamageRecord record) throws Exception;

    /**
     * 编辑报损
     * @param record
     * @return
     */
    CommonResult editDamage(String id,DamageRecord record) throws Exception;

    /**
     * 删除报损
     * @return
     */
    CommonResult delDamage(String ids) throws Exception;

    /**
     * 报损列表
     * @return
     */
    CommonResult getDamageList(Map<String,Object> params);

    /**
     * 通过订单信息获取可报损半产品
     * @param business
     * @return
     */
    CommonResult getOrderList(ShelfDivisionBusiness business);

    /**
     * 通过流程卡信息获取可报损半产品
     * @param business
     * @return
     */
    CommonResult getFlowCardList(ShelfDivisionBusiness business);

    /**
     * 获取报损单号
     * @return
     */
    CommonResult getDamageNo();

    /**
     * 获取报损详情
     * @param id
     * @return
     */
    CommonResult getDamageDetail(String id);

    /**
     * 导出excel
     * @param response
     * @param ids
     */
    void exportDamage(HttpServletResponse response, String ids);
}
