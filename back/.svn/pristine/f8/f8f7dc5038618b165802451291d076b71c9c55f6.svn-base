package com.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.PinYin;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.floor.headler.MyMetaObjectHandler;
import com.erp.floor.mapper.system.SysAdditionalMapper;
import com.erp.floor.mapper.system.SysCraftMapper;
import com.erp.floor.pojo.system.domain.SysAdditional;
import com.erp.floor.pojo.system.domain.SysCraftFlow;
import com.erp.system.service.ISysAdditionalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 15:23
 */
@Service
public class SysAdditionalServiceImpl extends ServiceImpl<SysAdditionalMapper, SysAdditional> implements ISysAdditionalService {

    @Resource
    private SysAdditionalMapper sysAdditionalMapper;

    @Override
    public CommonResult insertAdditional(SysAdditional sysAdditional) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        //获取别名
        StringBuilder firstSpell = new StringBuilder(PinYin.getFirstSpell(sysAdditional.getAdditionalName()));
        LambdaQueryWrapper<SysAdditional> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAdditional::getAdditionalName,sysAdditional.getAdditionalName()).or().eq(SysAdditional::getAdditionalAlias, firstSpell.toString());
        SysAdditional check = sysAdditionalMapper.selectOne(wrapper);
        if (check != null) {
            if(check.getAdditionalName().equals(sysAdditional.getAdditionalName())){
                return CommonResult.error("此附加项已存在");
            }else {
                int x = 1;
                do {
                    if (x != 1) {
                        firstSpell = new StringBuilder(firstSpell.substring(0, firstSpell.length() - 1));
                    }
                    firstSpell.append(x);
                    check = sysAdditionalMapper.selectOne(Wrappers.<SysAdditional>query().lambda().eq(SysAdditional::getAdditionalAlias, firstSpell.toString()));
                    x++;
                }while (check != null);
            }
        }
        sysAdditional.setAdditionalAlias(firstSpell.toString());
        int i = sysAdditionalMapper.insert(sysAdditional);
        if(i > 0){
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        }
        return CommonResult.error(ResultConstants.SAVE_ERROR);
    }

    @Override
    public CommonResult updatedAdditional(String id, SysAdditional sysAdditional) {
        MyMetaObjectHandler.setLoginUser(SecurityUtils.getLoginUser());
        StringBuilder firstSpell = new StringBuilder(PinYin.getFirstSpell(sysAdditional.getAdditionalName()));
        sysAdditional.setId(id);
        LambdaQueryWrapper<SysAdditional> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAdditional::getAdditionalName,sysAdditional.getAdditionalName()).or().eq(SysAdditional::getAdditionalAlias, firstSpell.toString());
        wrapper.ne(SysAdditional::getId,id);
        SysAdditional check = sysAdditionalMapper.selectOne(wrapper);
        if (check != null) {
            if(check.getAdditionalName().equals(sysAdditional.getAdditionalName())){
                return CommonResult.error("此附加项已存在");
            }else {
                int x = 1;
                do {
                    if (x != 1) {
                        firstSpell = new StringBuilder(firstSpell.substring(0, firstSpell.length() - 1));
                    }
                    firstSpell.append(x);
                    check = sysAdditionalMapper.selectOne(Wrappers.<SysAdditional>query().lambda().eq(SysAdditional::getAdditionalAlias, firstSpell.toString()));
                    x++;
                }while (check != null);
            }
        }
        sysAdditional.setAdditionalAlias(firstSpell.toString());
        int i = sysAdditionalMapper.updateById(sysAdditional);
        if(i > 0){
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        }
        return CommonResult.error(ResultConstants.UPD_ERROR);
    }

    @Override
    public CommonResult deletedAdditional(List<String> ids) {
        int i = sysAdditionalMapper.deleteBatchIds(ids);
        if(i > 0){
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }
        return CommonResult.error(ResultConstants.DEL_ERROR);
    }

    @Override
    public CommonResult getAdditionalList(SysAdditional sysAdditional) {
        LambdaQueryWrapper<SysAdditional> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SysAdditional::getCreatedAt);
        if(StringUtils.isNotEmpty(sysAdditional.getAdditionalName())){
            queryWrapper.like(SysAdditional::getAdditionalName,sysAdditional.getAdditionalName());
        }
        List<SysAdditional> list = sysAdditionalMapper.selectList(queryWrapper);
        int count = sysAdditionalMapper.selectCount(queryWrapper);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,list);
        result.setCount(count);
        return result;
    }
}
