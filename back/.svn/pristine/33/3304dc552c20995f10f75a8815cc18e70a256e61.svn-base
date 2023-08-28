package com.erp.floor.mapper.original;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.original.domain.YpOperationDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-13 12:28
 */
@Mapper
public interface YpOperationDetailMapper extends BaseMapper<YpOperationDetail> {

    //查询明细
    List<YpOperationDetail> selectByOperationId(@Param("ids") String[] ids, @Param("type") String type);


}
