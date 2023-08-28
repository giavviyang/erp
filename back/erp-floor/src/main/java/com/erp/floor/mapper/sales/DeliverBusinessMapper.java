package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.sales.domain.DeliverBusiness;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 12:20
 */
@Mapper
public interface DeliverBusinessMapper extends BaseMapper<DeliverBusiness> {

    //查询订单发货明细
    List<DeliverBusiness> queryOrderBusiness(String[] ids);

    //查询打包发货明细
    List<DeliverBusiness> queryPackBusiness(String deliverId);

    //查询可退货的发货明细
    List<DeliverBusiness> queryBusiness(DeliverBusiness deliverBusiness);

    //订单管理-查询发货明细
    List<DeliverBusiness> queryDeliverBus(String id);

    //收款管理 - 查询发货明细

}
