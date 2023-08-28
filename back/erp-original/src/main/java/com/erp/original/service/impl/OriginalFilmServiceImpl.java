package com.erp.original.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.poi.ExcelUtil;
import com.erp.floor.mapper.original.OriginalFilmMapper;
import com.erp.floor.mapper.original.OriginalFilmTypeMapper;
import com.erp.floor.pojo.original.domain.OriginalFilm;
import com.erp.floor.pojo.original.domain.OriginalFilmType;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.original.service.OriginalFilmService;
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
 * @date： 2022-09-09 10:21
 */
@Service
public class OriginalFilmServiceImpl extends ServiceImpl<OriginalFilmMapper, OriginalFilm> implements OriginalFilmService {
    @Resource
    private OriginalFilmTypeMapper originalFilmTypeMapper;
    @Resource
    private OriginalFilmMapper originalFilmMapper;


    /**
     * 查询原片类型左侧树
     */
    @Override
    public CommonResult<List<OriginalFilmType>> queryTree() {
        return CommonResult.success(originalFilmTypeMapper.selectList(Wrappers.<OriginalFilmType>query().lambda()));
    }

    /**
     * 新增原片类型
     *
     * @param originalFilmType 原片类型
     */
    @Override
    public CommonResult addOriginalType(OriginalFilmType originalFilmType) {
        LambdaQueryWrapper<OriginalFilmType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OriginalFilmType::getOriginalTypeName, originalFilmType.getOriginalTypeName());
        wrapper.eq(OriginalFilmType::getTid, originalFilmType.getTid());
        if (originalFilmTypeMapper.selectCount(wrapper) > 0 ) {
            return CommonResult.error("所在类型中，" + originalFilmType.getOriginalTypeName() + "类型已经存在，无法创建");
        }
        originalFilmType.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        originalFilmType.setCreatedAt(new Date());
        originalFilmTypeMapper.insert(originalFilmType);
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    /**
     * 编辑原片类型
     *
     * @param originalFilmType 原片类型
     */
    @Override
    public CommonResult updOriginalType(OriginalFilmType originalFilmType) {
        LambdaQueryWrapper<OriginalFilmType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OriginalFilmType::getOriginalTypeName, originalFilmType.getOriginalTypeName());
        wrapper.eq(OriginalFilmType::getTid, originalFilmType.getTid());
        if (originalFilmTypeMapper.selectCount(wrapper) > 0 ) {
            return CommonResult.error("所在类型中，" + originalFilmType.getOriginalTypeName() + "类型已经存在，无法创建");
        }
        originalFilmType.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        originalFilmType.setUpdatedAt(new Date());
        originalFilmTypeMapper.updateById(originalFilmType);
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    /**
     * 删除原片类型
     *
     * @param id 原片类型id
     */
    @Override
    public CommonResult delOriginalType(String id) {
        if (originalFilmMapper.selectCount(Wrappers.<OriginalFilm>query().lambda().eq(OriginalFilm::getOriginalTypeId, id)) > 0) {
            return CommonResult.error("该类型下存在数据，无法删除！");
        }
        originalFilmTypeMapper.deleteById(id);
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 原片数据查询
     *
     * @param originalFilm 原片
     */
    @Override
    public CommonResult<List<OriginalFilm>> queryOriginalFilm(OriginalFilm originalFilm) {
        LambdaQueryWrapper<OriginalFilm> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(originalFilm.getOriginalTypeId()))
            wrapper.like(OriginalFilm::getOriginalTypeId, originalFilm.getOriginalTypeId());
        if (StringUtils.isNotEmpty(originalFilm.getOriginalName()))
            wrapper.like(OriginalFilm::getOriginalName, originalFilm.getOriginalName());
        if (StringUtils.isNotEmpty(originalFilm.getOriginalNo()))
            wrapper.like(OriginalFilm::getOriginalNo, originalFilm.getOriginalNo());
        if (StringUtils.isNotEmpty(originalFilm.getOriginalColor()))
            wrapper.like(OriginalFilm::getOriginalColor, originalFilm.getOriginalColor());
        if (StringUtils.isNotEmpty(originalFilm.getMillName()))
            wrapper.like(OriginalFilm::getMillName, originalFilm.getMillName());
        if (originalFilm.getOriginalThick() != null)
            wrapper.eq(OriginalFilm::getOriginalThick, originalFilm.getOriginalThick());
        wrapper.orderByDesc(OriginalFilm::getCreatedAt);
        Integer integer = originalFilmMapper.selectCount(wrapper);
        PageHelper.startPage(originalFilm.getPageNum(), originalFilm.getPageSize());
        List<OriginalFilm> originalFilms = originalFilmMapper.selectList(wrapper);
        PageInfo<OriginalFilm> pageInfo = new PageInfo<>(originalFilms, originalFilm.getPageSize());
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, pageInfo.getList());
    }

    /**
     * 原片新增
     *
     * @param originalFilm 原片
     */
    @Override
    public CommonResult addOriginalFilm(OriginalFilm originalFilm) {
        OriginalFilmType originalFilmType = originalFilmTypeMapper.selectById(originalFilm.getOriginalTypeId());
        originalFilm.setOriginalTypeName(originalFilmType.getOriginalTypeName());
        originalFilm.setStock(0);
        originalFilm.setCreatedAt(new Date());
        originalFilm.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        originalFilmMapper.insert(originalFilm);
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    /**
     * 原片编辑
     *
     * @param originalFilm 原片
     */
    @Override
    public CommonResult updOriginalFilm(OriginalFilm originalFilm) {
        OriginalFilmType originalFilmType = originalFilmTypeMapper.selectById(originalFilm.getOriginalTypeId());
        originalFilm.setOriginalTypeName(originalFilmType.getOriginalTypeName());
        originalFilm.setUpdatedAt(new Date());
        originalFilm.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
        originalFilmMapper.updateById(originalFilm);
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    /**
     * 原片删除
     *
     * @param ids 原片id
     */
    @Override
    public CommonResult delOriginalFilm(String ids) {
        String[] split = ids.split(",");
        originalFilmMapper.delete(Wrappers.<OriginalFilm>query().lambda().in(OriginalFilm::getId, split));
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 导出原片
     *
     * @param response
     * @param ids      原片id
     */
    @Override
    public void exportOriginalFilm(HttpServletResponse response, String ids) {
        LambdaQueryWrapper<OriginalFilm> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
            String[] split = ids.split(",");
            wrapper.in(OriginalFilm::getId, split);
        }
        wrapper.orderByDesc(OriginalFilm::getCreatedAt);
        List<OriginalFilm> orderRecords = originalFilmMapper.selectList(wrapper);
        ExcelUtil<OriginalFilm> util = new ExcelUtil<OriginalFilm>(OriginalFilm.class);
        util.exportExcel(response, orderRecords, "参数数据");
    }
}
