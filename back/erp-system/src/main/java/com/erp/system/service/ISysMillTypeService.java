package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysMill;
import com.erp.floor.pojo.system.domain.SysMillType;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 14:34
 */
public interface ISysMillTypeService extends IService<SysMillType> {
    /**
     * 插入一条厂商类型信息
     * @param sysMillType
     * @return 插入结果
     */
    CommonResult insertMillType(SysMillType sysMillType);

    /**
     * 修改一条厂商类型的数据
     * @return 修改结果
     */
    CommonResult updatedMillType(String id, SysMillType sysMillType);

    /**
     * 删除此id厂商类型的数据
     * @return 删除结果
     */
    CommonResult deletedMillType(String id);

    /**
     * 分页获取厂商类型的数据
     * @return 数据列表
     */
    CommonResult getAllMillType();
}
