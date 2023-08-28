package com.erp.floor.mapper.accessories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.accessories.domain.AccessoriesOperationDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-15 09:58
 */
@Mapper
public interface AccessoriesOperationDetailMapper extends BaseMapper<AccessoriesOperationDetail> {

    //查询明细
    List<AccessoriesOperationDetail> selectByOperationId(@Param("ids") String[] ids, @Param("type") String type);

}
