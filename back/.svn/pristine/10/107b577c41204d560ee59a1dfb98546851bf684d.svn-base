package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysMill;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 14:33
 */
public interface ISysMillService extends IService<SysMill> {
    /**
     * 插入一条厂商信息
     * @param sysMill
     * @return 插入结果
     */
    CommonResult insertMill(SysMill sysMill);

    /**
     * 修改一条厂商的数据
     * @return 修改结果
     */
    CommonResult updatedMill(String id, SysMill sysMill);

    /**
     * 删除此id厂商的数据
     * @return 删除结果
     */
    CommonResult deletedMill(List<String> id);

    /**
     * 分页获取厂商的数据
     * @return 数据列表
     */
    CommonResult getMillList(SysMill sysMill);
}
