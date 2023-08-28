package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCraft;
import com.erp.floor.pojo.system.domain.SysCraftFlow;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 11:26
 */
public interface ISysCraftFlowService extends IService<SysCraftFlow> {
    /**
     * 插入工艺
     * @param sysCraftFlow
     * @return
     */
    CommonResult insertCraftFlow(SysCraftFlow sysCraftFlow);

    /**
     * 修改工艺
     * @param id
     * @param sysCraftFlow
     * @return
     */
    CommonResult updatedCraftFlow(String id,SysCraftFlow sysCraftFlow);

    /**
     * 删除工艺
     * @param id
     * @return
     */
    CommonResult deletedCraftFlow(List<String> id);

    /**
     * 获取列表
     * @return
     */
    CommonResult getCraftFlowList(SysCraftFlow sysCraftFlow);
}
