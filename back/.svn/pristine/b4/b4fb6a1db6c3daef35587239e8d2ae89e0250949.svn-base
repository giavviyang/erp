package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCustomerType;
import com.erp.floor.pojo.system.domain.SysDevice;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 15:32
 */
public interface ISysDeviceService extends IService<SysDevice> {

    /**
     * 插入一条客户类型的数据
     * @return 插入结果
     */
    CommonResult insertDevice(SysDevice sysDevice);

    /**
     * 插入一条客户类型的数据
     * @return 修改结果
     */
    CommonResult updatedDevice(String id,SysDevice sysDevice);

    /**
     * 删除此id客户类型的数据
     * @return 删除结果
     */
    CommonResult deletedDevice(List<String> ids);

    /**
     * 获取全部客户类型的数据
     * @return 数据列表
     */
    CommonResult getDeviceList(SysDevice sysDevice);
}
