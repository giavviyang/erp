package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysCustomerMapper;
import com.erp.floor.mapper.system.SysCustomerTypeMapper;
import com.erp.floor.pojo.system.domain.SysCustomer;
import com.erp.floor.pojo.system.domain.SysCustomerType;
import com.erp.system.service.ISysCustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 10:22
 */
@Service
public class SysCustomerServiceImpl extends ServiceImpl<SysCustomerMapper, SysCustomer> implements ISysCustomerService {

    @Resource
    private SysCustomerMapper sysCustomerMapper;

    @Resource
    private SysCustomerTypeMapper sysCustomerTypeMapper;

    @Override
    public CommonResult insertCustomer(SysCustomer sysCustomer) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());

        if(StringUtils.isNotEmpty(sysCustomer.getTypeId()) && sysCustomer.getTypeId().equals("000")){
            SysCustomerType type = new SysCustomerType();
            type.setTypeName(sysCustomer.getTypeName());
            sysCustomerTypeMapper.insert(type);
            sysCustomer.setTypeId(type.getId());
        }

        LambdaQueryWrapper<SysCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCustomer::getCustomerName,sysCustomer.getCustomerName()).or().eq(SysCustomer::getPhone,sysCustomer.getPhone());
        int count = sysCustomerMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此客户名称或手机号已存在");
        }
        int i = sysCustomerMapper.insert(sysCustomer);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedCustomer(String id, SysCustomer sysCustomer) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysCustomer.setId(id);
        LambdaQueryWrapper<SysCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCustomer::getCustomerName,sysCustomer.getCustomerName());
        wrapper.eq(SysCustomer::getPhone,sysCustomer.getPhone());
        wrapper.ne(SysCustomer::getId,id);
        int count = sysCustomerMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此客户名称或手机号已存在");
        }
        int i = sysCustomerMapper.updateById(sysCustomer);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedCustomer(List<String> ids) {
        int i = sysCustomerMapper.deleteBatchIds(ids);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getCustomerList(SysCustomer sysCustomer) {
        LambdaQueryWrapper<SysCustomer> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(sysCustomer.getTypeId())){
            queryWrapper.eq(SysCustomer::getTypeId,sysCustomer.getTypeId());
        }
        if(StringUtils.isNotEmpty(sysCustomer.getNumber())){
            queryWrapper.like(SysCustomer::getNumber,sysCustomer.getNumber());
        }
        if(StringUtils.isNotEmpty(sysCustomer.getCustomerName())){
            queryWrapper.like(SysCustomer::getCustomerName,sysCustomer.getCustomerName());
        }
        if(StringUtils.isNotEmpty(sysCustomer.getPhone())){
            queryWrapper.like(SysCustomer::getPhone,sysCustomer.getPhone());
        }
        if(StringUtils.isNotEmpty(sysCustomer.getAddress())){
            queryWrapper.like(SysCustomer::getAddress,sysCustomer.getAddress());
        }
        queryWrapper.orderByDesc(SysCustomer::getCreatedAt);
        List<SysCustomer> list = sysCustomerMapper.selectList(queryWrapper);
        int count = sysCustomerMapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }
}
