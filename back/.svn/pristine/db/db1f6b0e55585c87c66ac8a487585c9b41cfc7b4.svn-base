package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysNumberingRulesMapper;
import com.erp.floor.pojo.system.domain.SysDevice;
import com.erp.floor.pojo.system.domain.SysMill;
import com.erp.floor.pojo.system.domain.SysNumberingRules;
import com.erp.floor.pojo.system.domain.SysProduct;
import com.erp.system.service.ISysNumberingRulesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 16:13
 */

@Service
public class SysNumberingRulesServiceImpl extends ServiceImpl<SysNumberingRulesMapper, SysNumberingRules> implements ISysNumberingRulesService {

    @Resource
    private SysNumberingRulesMapper mapper;

    @Override
    public CommonResult updatedNumberingRules(String id, SysNumberingRules sysNumberingRules) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysNumberingRules.setId(id);
        int i = mapper.updateById(sysNumberingRules);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult getNumberingRulesList() {
        LambdaQueryWrapper<SysNumberingRules> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SysNumberingRules::getCreateAt);
        List<SysNumberingRules> list = mapper.selectList(queryWrapper);
        int count = mapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }
}
