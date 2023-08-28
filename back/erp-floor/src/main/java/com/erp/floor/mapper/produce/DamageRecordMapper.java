package com.erp.floor.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.produce.domain.DamageRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/6 10:56
 */
@Mapper
public interface DamageRecordMapper extends BaseMapper<DamageRecord> {
    List<DamageRecord> selectDamageList(Map<String,Object> params);

    List<DamageRecord> selectDamageAll(Map<String,Object> params);
}
