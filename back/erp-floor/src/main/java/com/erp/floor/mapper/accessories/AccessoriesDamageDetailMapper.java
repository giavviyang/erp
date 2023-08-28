package com.erp.floor.mapper.accessories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.AccessoriesDamageDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-15 10:57
 */
@Mapper
public interface AccessoriesDamageDetailMapper extends BaseMapper<AccessoriesDamageDetail> {

    /**
     * 查看明细
     * @param ids
     * @return
     */
    List<AccessoriesDamageDetail> viewDetail(String[] ids);
}
