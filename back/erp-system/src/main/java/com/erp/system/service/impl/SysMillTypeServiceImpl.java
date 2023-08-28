package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysMillMapper;
import com.erp.floor.mapper.system.SysMillTypeMapper;
import com.erp.floor.pojo.system.domain.SysMill;
import com.erp.floor.pojo.system.domain.SysMillType;
import com.erp.floor.pojo.system.domain.SysProductType;
import com.erp.system.service.ISysMillTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 14:36
 */
@Service
public class SysMillTypeServiceImpl extends ServiceImpl<SysMillTypeMapper, SysMillType> implements ISysMillTypeService {
    @Resource
    private SysMillTypeMapper sysMillTypeMapper;

    @Resource
    private SysMillMapper sysMillMapper;

    @Override
    public CommonResult insertMillType(SysMillType sysMillType) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysMillType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMillType::getTypeName,sysMillType.getTypeName());
        int count = sysMillTypeMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此类型名称已存在");
        }
        int i = sysMillTypeMapper.insert(sysMillType);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedMillType(String id, SysMillType sysMillType) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysMillType.setId(id);
        LambdaQueryWrapper<SysMillType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMillType::getTypeName,sysMillType.getTypeName());
        wrapper.ne(SysMillType::getId,id);
        int count = sysMillTypeMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此类型名称已存在");
        }
        int i = sysMillTypeMapper.updateById(sysMillType);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedMillType(String id) {
        LambdaQueryWrapper<SysMill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMill::getTypeId,id);
        int count = sysMillMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此类型下存在厂家数据");
        }
        int i = sysMillTypeMapper.deleteById(id);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getAllMillType() {
        LambdaQueryWrapper<SysMillType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SysMillType::getCreatedAt);
        List<SysMillType> list = sysMillTypeMapper.selectList(queryWrapper);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
    }
}
