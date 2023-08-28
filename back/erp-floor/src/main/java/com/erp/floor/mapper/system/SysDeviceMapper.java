package com.erp.floor.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.system.domain.SysDevice;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Resource;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 15:28
 */

@Mapper
public interface SysDeviceMapper extends BaseMapper<SysDevice> {
}
