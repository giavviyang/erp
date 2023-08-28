package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysTemplateMapper;
import com.erp.floor.mapper.system.SysTemplateTypeMapper;
import com.erp.floor.pojo.system.domain.SysCraftFlow;
import com.erp.floor.pojo.system.domain.SysCustomer;
import com.erp.floor.pojo.system.domain.SysTemplate;
import com.erp.floor.pojo.system.domain.SysTemplateType;
import com.erp.system.service.ISysTemplateTypeService;
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
public class SysTemplateTypeServiceImpl extends ServiceImpl<SysTemplateTypeMapper, SysTemplateType> implements ISysTemplateTypeService {

    @Resource
    private SysTemplateTypeMapper mapper;
    @Resource
    private SysTemplateMapper templateMapper;


    @Override
    public CommonResult insertTemplateType(SysTemplateType sysTemplateType) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysTemplateType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTemplateType::getTypeName,sysTemplateType.getTypeName());
        wrapper.eq(SysTemplateType::getTid, sysTemplateType.getTid());
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此模板类型已存在");
        }
        int i = mapper.insert(sysTemplateType);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedTemplateType(String id, SysTemplateType sysTemplateType) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysTemplateType.setId(id);
        LambdaQueryWrapper<SysTemplateType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTemplateType::getTypeName,sysTemplateType.getTypeName());
        wrapper.eq(SysTemplateType::getTid, sysTemplateType.getTid());
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此模板类型已存在");
        }
        int i = mapper.updateById(sysTemplateType);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPLOAD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedTemplateType(String id) {
        LambdaQueryWrapper<SysTemplateType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTemplateType::getTid,id);
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("当前类型下存在子级，无法删除");
        }
        LambdaQueryWrapper<SysTemplate> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(SysTemplate::getTypeId,id);
        count = templateMapper.selectCount(wrapper2);
        if(count > 0){
            return CommonResult.error("当前类型下存在模板数据，无法删除");
        }
        int i = mapper.deleteById(id);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getAllTemplateType() {
        LambdaQueryWrapper<SysTemplateType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(SysTemplateType::getCreatedAt);
        List<SysTemplateType> list = mapper.selectList(queryWrapper);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
    }
}
