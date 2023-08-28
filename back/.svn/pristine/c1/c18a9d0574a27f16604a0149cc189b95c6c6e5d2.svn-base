package com.erp.floor.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.produce.domain.PatchRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/14 14:50
 */
@Mapper
public interface PatchRecordMapper extends BaseMapper<PatchRecord> {

    @Select({"<script>" +
            " "+
            "${sql}" +
            " "+
            "</script>"})
    List<Map<String,Object>> getCountList(@Param("sql") String sql);
}
