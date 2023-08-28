package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysQrRuleMapper;
import com.erp.floor.pojo.system.domain.SysAdditional;
import com.erp.floor.pojo.system.domain.SysQrRule;
import com.erp.system.service.ISysQrRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/16 9:56
 */

@Service
public class SysQrRuleServiceImpl extends ServiceImpl<SysQrRuleMapper, SysQrRule> implements ISysQrRuleService {

    @Resource
    private SysQrRuleMapper mapper;

    @Override
    public CommonResult insertQrRule(SysQrRule sysQrRule) {
        if (checkStatus(new ArrayList<String>(){{add(sysQrRule.getId());}}, 0) && sysQrRule.getStatus() == 1) {
            return CommonResult.error("新增失败，存在已启用数据！");
        }
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysQrRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysQrRule::getQrName,sysQrRule.getQrName());
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此规则名称已存在");
        }
        int i = mapper.insert(sysQrRule);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedQrRule(String id, SysQrRule sysQrRule) {
        if (checkStatus(new ArrayList<String>(){{add(id);}}, 0) && sysQrRule.getStatus() == 1) {
            return CommonResult.error("编辑失败，存在已启用数据！");
        }
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysQrRule.setId(id);
        LambdaQueryWrapper<SysQrRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysQrRule::getQrName,sysQrRule.getQrName());
        wrapper.ne(SysQrRule::getId,id);
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此规则名称已存在");
        }
        int i = mapper.updateById(sysQrRule);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedQrRule(List<String> ids) {
        if (checkStatus(ids, 1)) {
            return CommonResult.error("删除失败，已启用数据无法删除！");
        }
        int i = mapper.deleteBatchIds(ids);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getQrRuleList(SysQrRule sysQrRule) {
        LambdaQueryWrapper<SysQrRule> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(sysQrRule.getQrName())){
            queryWrapper.like(SysQrRule::getQrName,sysQrRule.getQrName());
        }
        queryWrapper.orderByDesc(SysQrRule::getCreatedAt);
        List<SysQrRule> list = mapper.selectList(queryWrapper);
        int count = mapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }

    boolean checkStatus(List<String> ids, int type) {
        List<SysQrRule> sysQrRules = null;
        if (type == 1) {
            sysQrRules = mapper.selectList(Wrappers.<SysQrRule>query().lambda().in(SysQrRule::getId, ids));
        }else {
            sysQrRules = mapper.selectList(Wrappers.<SysQrRule>query().lambda().notIn(SysQrRule::getId, ids));
        }
        for (SysQrRule sysQrRule : sysQrRules) {
            if (sysQrRule.getStatus() == 1) {
                return true;
            }
        }
        return false;
    }
}
