package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.sales.domain.OutsourcedProducts;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-23 14:01
 */
@Mapper
public interface OutsourcedProductsMapper extends BaseMapper<OutsourcedProducts> {

    /**
     * 查看明细
     * @param ids 外协ids
     */
    List<OutsourcedProducts> detailedView(String[] ids);

}
