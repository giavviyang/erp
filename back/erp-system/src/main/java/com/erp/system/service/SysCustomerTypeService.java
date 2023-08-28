package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.floor.pojo.system.domain.SysCustomerType;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/21 15:43
 */
public interface SysCustomerTypeService extends IService<SysCustomerType> {
    /**
     * 插入一条客户类型的数据
     * @return 插入结果
     */
    AjaxResult insertCustomerType(SysCustomerType sysCustomerType);

    /**
     * 插入一条客户类型的数据
     * @return 修改结果
     */
    AjaxResult updatedCustomerType(String id,SysCustomerType sysCustomerType);

    /**
     * 删除此id客户类型的数据
     * @return 删除结果
     */
    AjaxResult deletedCustomerType(String id);

    /**
     * 获取全部客户类型的数据
     * @return 数据列表
     */
    AjaxResult getAllCustomerType();
}
