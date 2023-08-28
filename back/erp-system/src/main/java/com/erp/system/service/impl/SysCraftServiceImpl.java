package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysCraftMapper;
import com.erp.floor.pojo.system.domain.SysCraft;
import com.erp.floor.pojo.system.domain.SysCustomer;
import com.erp.system.service.ISysCraftService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 10:59
 */
@Service
public class SysCraftServiceImpl extends ServiceImpl<SysCraftMapper, SysCraft> implements ISysCraftService {

    @Resource
    private SysCraftMapper sysCraftMapper;

    @Override
    public CommonResult insertCraft(SysCraft sysCraft) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysCraft> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCraft::getCraftName,sysCraft.getCraftName());
        int count = sysCraftMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此工艺已存在");
        }
        int i = sysCraftMapper.insert(sysCraft);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedCraft(String id, SysCraft sysCraft) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysCraft.setId(id);
        LambdaQueryWrapper<SysCraft> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCraft::getId,sysCraft.getId());
        SysCraft one = sysCraftMapper.selectOne(wrapper);
        if (!one.getCraftName().equals(sysCraft.getCraftName())) {
            int count = sysCraftMapper.selectCount(Wrappers.<SysCraft>query().lambda().eq(SysCraft::getCraftName, sysCraft.getCraftName()));
            if(count > 0){
                return CommonResult.error("此工艺已存在");
            }
        }
        int i = sysCraftMapper.updateById(sysCraft);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedCraft(List<String> ids) {
        int i = sysCraftMapper.deleteBatchIds(ids);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getCraftList(SysCraft sysCraft) {
        LambdaQueryWrapper<SysCraft> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(sysCraft.getCraftName())){
            queryWrapper.like(SysCraft::getCraftName,sysCraft.getCraftName());
        }
        queryWrapper.orderByDesc(SysCraft::getCreatedAt);
        List<SysCraft> list = sysCraftMapper.selectList(queryWrapper);
        int count = sysCraftMapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }
}
