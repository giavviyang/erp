package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysMill;
import com.erp.floor.pojo.system.domain.SysNumberingRules;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 16:13
 */
public interface ISysNumberingRulesService extends IService<SysNumberingRules> {

    /**
     * 修改编号规则的数据
     * @return 修改结果
     */
    CommonResult updatedNumberingRules(String id, SysNumberingRules sysNumberingRules);

    /**
     * 分页获取编号规则的数据
     * @return 数据列表
     */
    CommonResult getNumberingRulesList();
}
