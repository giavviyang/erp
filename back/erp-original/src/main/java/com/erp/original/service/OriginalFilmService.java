package com.erp.original.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.original.domain.OriginalFilm;
import com.erp.floor.pojo.original.domain.OriginalFilmType;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-09 10:19
 */
public interface OriginalFilmService extends IService<OriginalFilm> {

    /**
     * 查询原片类型左侧树
     */
    CommonResult<List<OriginalFilmType>> queryTree();

    /**
     * 新增原片类型
     *
     * @param originalFilmType 原片类型
     */
    CommonResult addOriginalType(OriginalFilmType originalFilmType);

    /**
     * 编辑原片类型
     *
     * @param originalFilmType 原片类型
     */
    CommonResult updOriginalType(OriginalFilmType originalFilmType);

    /**
     * 删除原片类型
     *
     * @param id 原片类型id
     */
    CommonResult delOriginalType(String id);

    /**
     * 原片数据查询
     * @param originalFilm 原片
     */
    CommonResult<List<OriginalFilm>> queryOriginalFilm(OriginalFilm originalFilm);

    /**
     * 原片新增
     * @param originalFilm 原片
     */
    CommonResult addOriginalFilm(OriginalFilm originalFilm);

    /**
     * 原片编辑
     * @param originalFilm 原片
     */
    CommonResult updOriginalFilm(OriginalFilm originalFilm);

    /**
     * 原片删除
     * @param ids 原片id
     */
    CommonResult delOriginalFilm(String ids);

    /**
     * 导出原片
     * @param response
     * @param ids 原片id
     */
    void exportOriginalFilm(HttpServletResponse response, String ids);
}
