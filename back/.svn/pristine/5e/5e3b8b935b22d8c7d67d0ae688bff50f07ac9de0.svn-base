package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-21 13:44
 */
@Mapper
public interface OrderProductMapper extends BaseMapper<OrderProduct> {

    /**
     * 查询可发货产品
     */
    List<OrderProduct> obtainOrderProduct(OrderRecordVo orderRecordVo);

    /**
     * 依据id查询产品
     */
    List<OrderProduct> selectByIds(String[] ids);

    /**
     * 查询可入库产品
     * @param orderProduct
     */
    List<OrderProduct> warehouseProduct(OrderProduct orderProduct);

    /**
     * 查询外协产品产品
     * @param orderProduct
     */
    List<OrderProduct> outsourcedProduct(OrderProduct orderProduct);

}
