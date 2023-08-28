package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.sales.domain.DeliverBusiness;
import com.erp.floor.pojo.sales.domain.DeliveryShelf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-30 14:16
 */
@Mapper
public interface DeliveryShelfMapper extends BaseMapper<DeliveryShelf> {

    /**
     * 查询发货货架
     * @param deliverId 发货id
     * @return
     */
    List<DeliveryShelf> byDeliverId(String deliverId);
}
