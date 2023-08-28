package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.CpProductRecord;
import com.erp.floor.pojo.sales.domain.CpRecord;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-20 16:47
 */
public interface WarehouseService extends IService<CpRecord> {

    /**
     *
     * @param orderRecordVo
     */
    CommonResult<List<OrderRecord>> queryWarehouseOrder(OrderRecordVo orderRecordVo);

    /**
     * 查询订单产品
     * @param id 订单id
     */
    CommonResult<List<OrderProduct>> queryOrderProduct(String id);

    /**
     * 查询未入库完成订单产品
     */
    CommonResult<List<OrderProduct>> queryProduct(OrderProduct orderProduct);

    /**
     * 自动给生成编号
     * @param type
     * @return
     */
    String productionNumber(int type);

    /**
     * 操作出入库
     * @param cpRecord 操作记录
     */
    CommonResult productWarehouse(CpRecord cpRecord);

    /**
     * 查询操作信息
     * @param cpRecord
     */
    CommonResult<List<CpRecord>> queryWarehouseData(@RequestBody CpRecord cpRecord);

    /**
     * 查询操作信息明细
     * @param id
     */
    CommonResult<List<CpProductRecord>> warehouseDeliverData(String id);

}
