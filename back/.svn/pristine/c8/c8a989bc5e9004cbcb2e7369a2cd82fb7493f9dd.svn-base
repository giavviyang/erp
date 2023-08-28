package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.sales.domain.PackingBusiness;
import com.erp.floor.pojo.sales.domain.vo.PackingRecordVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-25 17:24
 */
@Mapper
public interface PackingBusinessMapper extends BaseMapper<PackingBusiness> {

    /**
     * 获取可发货的打包信息
     * @param packingRecordVo 打包查询对象
     */
    List<PackingBusiness> obtainPackProduct(PackingRecordVo packingRecordVo);

    /**
     * 获取可发货的打包信息
     * @param orderId 打包查询对象
     */
    List<PackingBusiness> queryPack(String orderId);

}
