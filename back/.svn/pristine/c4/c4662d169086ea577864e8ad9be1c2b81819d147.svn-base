package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysProductTypeMapper;
import com.erp.floor.pojo.system.domain.SysMillType;
import com.erp.floor.pojo.system.domain.SysProductType;
import com.erp.system.service.ISysProductTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 17:21
 */
@Service
public class SysProductTypeServiceMapper extends ServiceImpl<SysProductTypeMapper, SysProductType> implements ISysProductTypeService {

    @Resource
    private SysProductTypeMapper sysProductTypeMapper;

    @Override
    public CommonResult insertProductType(SysProductType sysProductType) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysProductType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProductType::getTypeName,sysProductType.getTypeName());
        int count = sysProductTypeMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此类型名称已存在");
        }
        int i = sysProductTypeMapper.insert(sysProductType);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedProductType(String id, SysProductType sysProductType) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysProductType.setId(id);
        LambdaQueryWrapper<SysProductType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProductType::getTypeName,sysProductType.getTypeName());
        wrapper.ne(SysProductType::getId,id);
        int count = sysProductTypeMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此类型名称已存在");
        }
        int i = sysProductTypeMapper.updateById(sysProductType);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedProductType(String id) {
        LambdaQueryWrapper<SysProductType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProductType::getTid,id);
        int count = sysProductTypeMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此类型存在下级，无法删除");
        }
        int i = sysProductTypeMapper.deleteById(id);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getAllProductType() {
        LambdaQueryWrapper<SysProductType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SysProductType::getCreatedAt);
        List<SysProductType> list = sysProductTypeMapper.selectList(queryWrapper);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
    }
}
