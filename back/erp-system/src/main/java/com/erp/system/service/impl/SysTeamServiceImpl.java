package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysTeamMapper;
import com.erp.floor.pojo.system.domain.SysTeam;
import com.erp.floor.pojo.system.domain.SysTemplate;
import com.erp.system.service.ISysTeamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 13:55
 */
@Service
public class SysTeamServiceImpl extends ServiceImpl<SysTeamMapper, SysTeam> implements ISysTeamService {

    @Resource
    private SysTeamMapper mapper;

    @Override
    public CommonResult insertTeam(SysTeam sysTeam) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysTeam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTeam::getDeptId,sysTeam.getDeptId());
        wrapper.eq(SysTeam::getTeamName,sysTeam.getTeamName());
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此班组已经存在");
        }
        int i = mapper.insert(sysTeam);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedTeam(String id, SysTeam sysTeam) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysTeam.setId(id);
        LambdaQueryWrapper<SysTeam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTeam::getDeptId,sysTeam.getDeptId());
        wrapper.eq(SysTeam::getTeamName,sysTeam.getTeamName());
        wrapper.ne(SysTeam::getId,id);
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此班组已经存在");
        }
        int i = mapper.updateById(sysTeam);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedTeam(List<String> ids) {
        int i = mapper.deleteBatchIds(ids);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getTeamList(SysTeam sysTeam) {
        LambdaQueryWrapper<SysTeam> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(sysTeam.getTeamName())){
            queryWrapper.like(SysTeam::getTeamName,sysTeam.getTeamName());
        }
        if(StringUtils.isNotEmpty(sysTeam.getDeptId())){
            queryWrapper.like(SysTeam::getDeptId,sysTeam.getDeptId());
        }
        queryWrapper.orderByDesc(SysTeam::getCreatedAt);
        List<SysTeam> list = mapper.selectList(queryWrapper);
        int count = mapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }
}
