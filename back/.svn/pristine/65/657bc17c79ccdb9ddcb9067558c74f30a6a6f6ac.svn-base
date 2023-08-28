package com.erp.accessories.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.AccessoriesOperation;
import com.erp.floor.pojo.accessories.domain.AccessoriesOperationDetail;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-15 10:01
 */
public interface AccessoriesOperationService extends IService<AccessoriesOperation> {

    /**
     * 生成入库编号
     */
    String productionNumber(int type);

    /**
     * 辅料入库
     *
     * @param accessoriesOperation 入库记录
     */
    CommonResult accessoriesWarehouse(AccessoriesOperation accessoriesOperation);

    /**
     * 采购单入库
     *
     * @param accessoriesOperation 入库记录
     */
    CommonResult purchaseWarehouse(AccessoriesOperation accessoriesOperation);

    /**
     * 查询出入盘库记录
     *
     * @param accessoriesOperation 出入盘库记录
     */
    CommonResult<List<AccessoriesOperation>> warehouseRecord(AccessoriesOperation accessoriesOperation);

    /**
     * 查看出入盘库明细
     * @param id 出入盘库id
     */
    CommonResult<List<AccessoriesOperationDetail>> warehouseDetailsView(String id);

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
