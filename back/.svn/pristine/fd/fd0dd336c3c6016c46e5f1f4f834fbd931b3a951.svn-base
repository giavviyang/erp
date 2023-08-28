package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCustomer;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 10:21
 */
public interface ISysCustomerService extends IService<SysCustomer> {
    /**
     * 插入一条客户信息
     * @param sysCustomer
     * @return 插入结果
     */
    CommonResult insertCustomer(SysCustomer sysCustomer);

    /**
     * 修改一条客户的数据
     * @return 修改结果
     */
    CommonResult updatedCustomer(String id, SysCustomer sysCustomer);

    /**
     * 删除此id客户的数据
     * @return 删除结果
     */
    CommonResult deletedCustomer(List<String> ids);

    /**
     * 分页获取客户的数据
     * @return 数据列表
     */
    CommonResult getCustomerList(SysCustomer sysCustomer);
}
