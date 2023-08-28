package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysAdditional;
import com.erp.floor.pojo.system.domain.SysCustomer;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 15:22
 */
public interface ISysAdditionalService extends IService<SysAdditional> {
    /**
     * 插入附加项
     * @param sysAdditional
     * @return 插入结果
     */
    CommonResult insertAdditional(SysAdditional sysAdditional);

    /**
     * 修改附加项
     * @return 修改结果
     */
    CommonResult updatedAdditional(String id, SysAdditional sysAdditional);

    /**
     * 删除此id附加项的数据
     * @return 删除结果
     */
    CommonResult deletedAdditional(List<String> ids);

    /**
     * 分页附加项
     * @return 数据列表
     */
    CommonResult getAdditionalList(SysAdditional sysAdditional);
}
