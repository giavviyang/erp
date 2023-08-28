package com.erp.floor.mapper.accessories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.accessories.domain.AccessoriesPurchaseDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 17:42
 */
@Mapper
public interface AccessoriesPurchaseDetailMapper extends BaseMapper<AccessoriesPurchaseDetail> {

    /**
     * 查看明细
     * @param ids
     */
    List<AccessoriesPurchaseDetail> viewDetail(String[] ids);
}
