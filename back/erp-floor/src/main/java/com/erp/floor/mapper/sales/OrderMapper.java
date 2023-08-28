package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-21 13:44
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderRecord> {

    /**
     * 查询可分架订单
     * @param orderRecordVo 订单查询vo类
     */
    List<OrderRecord> queryReviewOrder(OrderRecordVo orderRecordVo);

    /**
     * 查询可分架订单
     * @param orderRecordVo 订单查询vo类
     */
    List<OrderRecord> queryCollectionOrder(OrderRecordVo orderRecordVo);


}
