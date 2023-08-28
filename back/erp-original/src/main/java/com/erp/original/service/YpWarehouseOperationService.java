package com.erp.original.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.original.domain.YpOperationDetail;
import com.erp.floor.pojo.original.domain.YpWarehouseOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-13 13:46
 */
public interface YpWarehouseOperationService extends IService<YpWarehouseOperation> {

    /**
     * 生成入库编号
     */
    String productionNumber(int type);

    /**
     * 原片入库
     *
     * @param ypWarehouseOperation 入库记录
     */
    CommonResult originalWarehouse(YpWarehouseOperation ypWarehouseOperation);

    /**
     * 采购单入库
     *
     * @param ypWarehouseOperation 入库记录
     */
    CommonResult purchaseWarehouse(YpWarehouseOperation ypWarehouseOperation);

    /**
     * 查询出入盘库记录
     *
     * @param ypWarehouseOperation 出入盘库记录
     */
    CommonResult<List<YpWarehouseOperation>> warehouseRecord(YpWarehouseOperation ypWarehouseOperation);

    /**
     * 查看出入盘库明细
     * @param id 出入盘库id
     */
    CommonResult<List<YpOperationDetail>> warehouseDetailsView(String id);

    /**
     * 删除出入库记录
     * @param ids 出入库ids
     * @param type 出入库类型
     */
    CommonResult delWarehouseDetails(String ids, Integer type);

    /**
     * 删除盘库记录
     * @param id 盘库id
     */
    CommonResult delInventoryDetails(String id);

    /**
     * 导出出入盘库记录
     * @param response
     * @param ids id集合
     */
    void exportWarehouse(HttpServletResponse response, String ids, String type);

    /**
     * 导出出入盘库明细
     * @param response
     * @param ids id集合
     */
    void exportWarehouseDetails(HttpServletResponse response, String ids, String type);


}
