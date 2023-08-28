package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysDeviceMapper;
import com.erp.floor.pojo.system.domain.SysDevice;
import com.erp.floor.pojo.system.domain.SysTeam;
import com.erp.system.service.ISysDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 15:33
 */

@Service
public class SysDeviceServiceImpl extends ServiceImpl<SysDeviceMapper, SysDevice> implements ISysDeviceService {

    @Resource
    private SysDeviceMapper mapper;

    @Override
    public CommonResult insertDevice(SysDevice sysDevice) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        LambdaQueryWrapper<SysDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDevice::getDeviceName,sysDevice.getDeviceName());
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此设备已经存在");
        }
        int i = mapper.insert(sysDevice);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedDevice(String id, SysDevice sysDevice) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        sysDevice.setId(id);
        LambdaQueryWrapper<SysDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDevice::getDeviceName,sysDevice.getDeviceName());
        wrapper.ne(SysDevice::getId,id);
        int count = mapper.selectCount(wrapper);
        if(count > 0){
            return CommonResult.error("此设备已经存在");
        }
        int i = mapper.updateById(sysDevice);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedDevice(List<String> ids) {
        int i = mapper.deleteBatchIds(ids);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getDeviceList(SysDevice sysDevice) {
        LambdaQueryWrapper<SysDevice> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(sysDevice.getNo())){
            queryWrapper.like(SysDevice::getNo,sysDevice.getNo());
        }
        if(StringUtils.isNotEmpty(sysDevice.getDeviceName())){
            queryWrapper.like(SysDevice::getDeviceName,sysDevice.getDeviceName());
        }
        queryWrapper.orderByDesc(SysDevice::getCreatedAt);
        List<SysDevice> list = mapper.selectList(queryWrapper);
        int count =  mapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }
}
