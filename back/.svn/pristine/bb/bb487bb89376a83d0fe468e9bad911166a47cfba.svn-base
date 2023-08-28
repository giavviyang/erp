package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OutsourcedWarehousing;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-23 14:02
 */
@Mapper
public interface OutsourcedWarehousingMapper extends BaseMapper<OutsourcedWarehousing> {

    /**
     * 查询入库信息
     * @param outsourcedWarehousing 入库记录
     */
    List<OutsourcedWarehousing> queryData(OutsourcedWarehousing outsourcedWarehousing);

    /**
     * 查询导出信息
     * @param ids 入库id集合
     */
    List<OutsourcedWarehousing> queryExportData(String[] ids);


}
