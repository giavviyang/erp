package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OutsourcedStockProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-23 14:02
 */
@Mapper
public interface OutsourcedStockProductMapper extends BaseMapper<OutsourcedStockProduct> {

    List<OutsourcedStockProduct> detailedView(String[] ids);

}
