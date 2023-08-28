package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysCustomerMapper;
import com.erp.floor.mapper.system.SysCustomerTypeMapper;
import com.erp.floor.pojo.system.domain.SysCustomer;
import com.erp.floor.pojo.system.domain.SysCustomerType;
import com.erp.floor.pojo.system.domain.SysQrRule;
import com.erp.system.service.ISysCustomerTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/21 15:46
 */
@Service
public class SysCustomerTypeServiceImpl extends ServiceImpl<SysCustomerTypeMapper, SysCustomerType> implements ISysCustomerTypeService {

    @Resource
    private SysCustomerTypeMapper sysCustomerTypeMapper;

    @Resource
    private SysCustomerMapper sysCustomerMapper;

    @Override
    public CommonResult insertCustomerType(SysCustomerType sysCustomerType) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysCustomerType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCustomerType::getTypeName,sysCustomerType.getTypeName());
        int count = sysCustomerTypeMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此客户类型已存在");
        }
        int i = sysCustomerTypeMapper.insert(sysCustomerType);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedCustomerType(String id, SysCustomerType sysCustomerType) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysCustomerType.setId(id);
        LambdaQueryWrapper<SysCustomerType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCustomerType::getTypeName,sysCustomerType.getTypeName());
        wrapper.ne(SysCustomerType::getId,id);
        int count = sysCustomerTypeMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此客户类型已存在");
        }
        int i = sysCustomerTypeMapper.updateById(sysCustomerType);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedCustomerType(String id) {
        LambdaQueryWrapper<SysCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCustomer::getTypeId,id);
        int count = sysCustomerMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此类型下存在客户数据，无法删除");
        }
        int i = sysCustomerTypeMapper.deleteById(id);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getAllCustomerType() {
        LambdaQueryWrapper<SysCustomerType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SysCustomerType::getCreatedAt);
        List<SysCustomerType> list = sysCustomerTypeMapper.selectList(queryWrapper);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
    }
}
