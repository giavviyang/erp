package com.erp.accessories.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.accessories.service.AccessoriesService;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.accessories.AccessoriesMapper;
import com.erp.floor.mapper.accessories.AccessoriesTypeMapper;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 16:35
 */
@Service
public class AccessoriesServiceImpl extends ServiceImpl<AccessoriesMapper, Accessories> implements AccessoriesService {
    @Resource
    private AccessoriesTypeMapper accessoriesTypeMapper;
    @Resource
    private AccessoriesMapper accessoriesMapper;


    /**
     * 查询辅料类型左侧树
     */
    @Override
    public CommonResult<List<AccessoriesType>> queryTree() {
        return CommonResult.success(accessoriesTypeMapper.selectList(Wrappers.<AccessoriesType>query().lambda()));
    }

    /**
     * 新增辅料类型
     *
     * @param accessoriesType 辅料类型
     */
    @Override
    public CommonResult addAccessoriesType(AccessoriesType accessoriesType) {
        LambdaQueryWrapper<AccessoriesType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessoriesType::getAccessoriesTypeName, accessoriesType.getAccessoriesTypeName());
        wrapper.eq(AccessoriesType::getTid, accessoriesType.getTid());
        if (accessoriesTypeMapper.selectCount(wrapper) > 0) {
            return CommonResult.error("所在类型中，" + accessoriesType.getAccessoriesTypeName() + "类型已经存在，无法创建");
        }
        accessoriesType.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        accessoriesType.setCreatedAt(new Date());
        accessoriesTypeMapper.insert(accessoriesType);
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    /**
     * 编辑辅料类型
     *
     * @param accessoriesType 辅料类型
     */
    @Override
    public CommonResult updAccessoriesType(AccessoriesType accessoriesType) {
        LambdaQueryWrapper<AccessoriesType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessoriesType::getAccessoriesTypeName, accessoriesType.getAccessoriesTypeName());
        wrapper.eq(AccessoriesType::getTid, accessoriesType.getTid());
        if (accessoriesTypeMapper.selectCount(wrapper) > 0) {
            return CommonResult.error("所在类型中，" + accessoriesType.getAccessoriesTypeName() + "类型已经存在，无法创建");
        }
        accessoriesType.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        accessoriesType.setUpdatedAt(new Date());
        accessoriesTypeMapper.updateById(accessoriesType);
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    /**
     * 删除辅料类型
     *
     * @param id 辅料类型id
     */
    @Override
    public CommonResult delAccessoriesType(String id) {
        if (accessoriesMapper.selectCount(Wrappers.<Accessories>query().lambda().eq(Accessories::getTypeId, id)) > 0) {
            return CommonResult.error("该类型下存在数据，无法删除！");
        }
        accessoriesTypeMapper.deleteById(id);
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 辅料数据查询
     *
     * @param accessories 辅料
     */
    @Override
    public CommonResult<List<Accessories>> queryAccessories(Accessories accessories) {
        LambdaQueryWrapper<Accessories> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(accessories.getAccessoriesNo()))
            wrapper.like(Accessories::getAccessoriesNo, accessories.getAccessoriesNo());
        if (StringUtils.isNotEmpty(accessories.getAccessoriesSpecifications()))
            wrapper.like(Accessories::getAccessoriesSpecifications, accessories.getAccessoriesSpecifications());
        if (StringUtils.isNotEmpty(accessories.getAccessoriesName()))
            wrapper.like(Accessories::getAccessoriesName, accessories.getAccessoriesName());
        if (StringUtils.isNotEmpty(accessories.getTypeId()) && !accessories.getTypeId().equals("0"))
            wrapper.like(Accessories::getTypeId, accessories.getTypeId());
        if (StringUtils.isNotEmpty(accessories.getAccessoriesMill()))
            wrapper.like(Accessories::getAccessoriesMill, accessories.getAccessoriesMill());
        if (StringUtils.isNotEmpty(accessories.getAccessoriesCompany()))
            wrapper.eq(Accessories::getAccessoriesCompany, accessories.getAccessoriesCompany());
        wrapper.orderByDesc(Accessories::getCreatedAt);
        Integer integer = accessoriesMapper.selectCount(wrapper);
        PageHelper.startPage(accessories.getPageNum(), accessories.getPageSize());
        List<Accessories> accessoriess = accessoriesMapper.selectList(wrapper);
        PageInfo<Accessories> pageInfo = new PageInfo<>(accessoriess, accessories.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, pageInfo.getList());
    }

    /**
     * 辅料新增
     *
     * @param accessories 辅料
     */
    @Override
    public CommonResult addAccessories(Accessories accessories) {
        if(accessories.getTypeId().equals("0")) {
            accessories.setTypeName("辅料类型");
        }else {
            AccessoriesType accessoriesType = accessoriesTypeMapper.selectById(accessories.getTypeId());
            accessories.setTypeName(accessoriesType.getAccessoriesTypeName());
        }
        accessories.setStock(0);
        accessories.setCreatedAt(new Date());
        accessories.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        accessoriesMapper.insert(accessories);
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    /**
     * 辅料编辑
     *
     * @param accessories 辅料
     */
    @Override
    public CommonResult updAccessories(Accessories accessories) {
        AccessoriesType accessoriesType = accessoriesTypeMapper.selectById(accessories.getTypeId());
        accessories.setTypeName(accessoriesType.getAccessoriesTypeName());
        accessories.setUpdatedAt(new Date());
        accessories.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        accessoriesMapper.updateById(accessories);
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    /**
     * 辅料删除
     *
     * @param ids 辅料id
     */
    @Override
    public CommonResult delAccessories(String ids) {
        String[] split = ids.split(",");
        accessoriesMapper.delete(Wrappers.<Accessories>query().lambda().in(Accessories::getId, split));
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 导出辅料
     *
     * @param response
     * @param ids      辅料id
     */
    @Override
    public void exportAccessories(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<Accessories> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(Accessories::getId, split);
        }
        wrapper.orderByDesc(Accessories::getCreatedAt);
        List<Accessories> orderRecords = accessoriesMapper.selectList(wrapper);
        ExcelUtil<Accessories> util = new ExcelUtil<Accessories>(Accessories.class);
        util.exportExcel(response, orderRecords, "参数数据");
    }
}
