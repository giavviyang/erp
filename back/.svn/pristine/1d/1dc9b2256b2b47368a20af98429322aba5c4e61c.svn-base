package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysTeam;
import com.erp.floor.pojo.system.domain.SysTemplate;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 13:53
 */
public interface ISysTeamService extends IService<SysTeam> {
    /**
     * 插入一条班组的数据
     * @return 插入结果
     */
    CommonResult insertTeam(SysTeam sysTeam);

    /**
     * 修改此id班组的数据
     * @return 修改结果
     */
    CommonResult updatedTeam(String id,SysTeam sysTeam);

    /**
     * 删除此id班组的数据
     * @return 删除结果
     */
    CommonResult deletedTeam(List<String> ids);

    /**
     * 获取分页的数据
     * @return 数据列表
     */
    CommonResult getTeamList(SysTeam sysTeam);
}
