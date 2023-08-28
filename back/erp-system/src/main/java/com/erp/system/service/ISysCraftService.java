package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysCraft;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 10:58
 */
public interface ISysCraftService extends IService<SysCraft> {

    /**
     * 插入工艺
     * @param sysCraft
     * @return
     */
    CommonResult insertCraft(SysCraft sysCraft);

    /**
     * 修改工艺
     * @param id
     * @param sysCraft
     * @return
     */
    CommonResult updatedCraft(String id,SysCraft sysCraft);

    /**
     * 删除工艺
     * @param ids
     * @return
     */
    CommonResult deletedCraft(List<String> ids);

    /**
     * 获取列表
     * @return
     */
    CommonResult getCraftList(SysCraft sysCraft);
}
