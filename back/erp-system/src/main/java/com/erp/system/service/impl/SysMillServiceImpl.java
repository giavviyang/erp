package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysMillMapper;
import com.erp.floor.pojo.system.domain.SysMill;
import com.erp.floor.pojo.system.domain.SysMillType;
import com.erp.system.service.ISysMillService;
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
public class SysMillServiceImpl extends ServiceImpl<SysMillMapper, SysMill> implements ISysMillService {
    @Resource
    private SysMillMapper sysMillMapper;

    @Override
    public CommonResult insertMill(SysMill sysMill) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysMill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMill::getMillName,sysMill.getMillName());
        wrapper.eq(SysMill::getPhone,sysMill.getPhone());
        int count = sysMillMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("厂家名称或手机号已存在");
        }
        int i = sysMillMapper.insert(sysMill);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedMill(String id, SysMill sysMill) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysMill.setId(id);
        LambdaQueryWrapper<SysMill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMill::getMillName,sysMill.getMillName());
        wrapper.eq(SysMill::getPhone,sysMill.getPhone());
        wrapper.ne(SysMill::getId,id);
        int count = sysMillMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("厂家名称或手机号已存在");
        }
        int i = sysMillMapper.updateById(sysMill);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedMill(List<String> id) {
        int i = sysMillMapper.deleteBatchIds(id);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getMillList(SysMill sysMill) {
        LambdaQueryWrapper<SysMill> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(sysMill.getTypeId())) {
            queryWrapper.eq(SysMill::getTypeId,sysMill.getTypeId());
        }
        if (StringUtils.isNotEmpty(sysMill.getNo())) {
            queryWrapper.like(SysMill::getNo,sysMill.getNo());
        }
        if (StringUtils.isNotEmpty(sysMill.getMillName())) {
            queryWrapper.like(SysMill::getMillName,sysMill.getMillName());
        }
        if (StringUtils.isNotEmpty(sysMill.getPhone())) {
            queryWrapper.like(SysMill::getPhone,sysMill.getPhone());
        }
        queryWrapper.orderByDesc(SysMill::getCreatedAt);
        List<SysMill> list = sysMillMapper.selectList(queryWrapper);
        int count = sysMillMapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }
}
