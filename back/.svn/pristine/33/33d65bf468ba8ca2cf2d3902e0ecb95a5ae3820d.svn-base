package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysCraftFlowMapper;
import com.erp.floor.mapper.system.SysCraftMapper;
import com.erp.floor.pojo.system.domain.SysCraft;
import com.erp.floor.pojo.system.domain.SysCraftFlow;
import com.erp.system.service.ISysCraftFlowService;
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
public class SysCraftFlowServiceImpl extends ServiceImpl<SysCraftFlowMapper, SysCraftFlow> implements ISysCraftFlowService {

    @Resource
    private SysCraftFlowMapper sysCraftFlowMapper;

    @Override
    public CommonResult insertCraftFlow(SysCraftFlow sysCraftFlow) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysCraftFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCraftFlow::getCraftFlowId,sysCraftFlow.getCraftFlowId());
        int count = sysCraftFlowMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此工艺流程已存在");
        }
        int i = sysCraftFlowMapper.insert(sysCraftFlow);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedCraftFlow(String id, SysCraftFlow sysCraftFlow) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysCraftFlow.setId(id);
        LambdaQueryWrapper<SysCraftFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCraftFlow::getCraftFlowId,sysCraftFlow.getCraftFlowId());
        wrapper.ne(SysCraftFlow::getId,id);
        int count = sysCraftFlowMapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此工艺流程已存在");
        }
        int i = sysCraftFlowMapper.updateById(sysCraftFlow);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedCraftFlow(List<String> ids) {
        int i = sysCraftFlowMapper.deleteBatchIds(ids);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getCraftFlowList(SysCraftFlow sysCraftFlow) {
        LambdaQueryWrapper<SysCraftFlow> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(sysCraftFlow.getCraftFlowTxt())){
            queryWrapper.like(SysCraftFlow::getCraftFlowTxt,sysCraftFlow.getCraftFlowTxt());
        }
        if(StringUtils.isNotEmpty(sysCraftFlow.getCraftFlowDescribe())){
            queryWrapper.like(SysCraftFlow::getCraftFlowDescribe,sysCraftFlow.getCraftFlowDescribe());
        }
        queryWrapper.orderByDesc(SysCraftFlow::getCreatedAt);
        List<SysCraftFlow> list = sysCraftFlowMapper.selectList(queryWrapper);
        int count = sysCraftFlowMapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }
}
