package com.erp.accessories.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesType;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 16:34
 */
public interface AccessoriesService extends IService<Accessories> {
    /**
     * 查询辅料类型左侧树
     */
    CommonResult<List<AccessoriesType>> queryTree();

    /**
     * 新增辅料类型
     *
     * @param accessoriesType 辅料类型
     */
    CommonResult addAccessoriesType(AccessoriesType accessoriesType);

    /**
     * 编辑辅料类型
     *
     * @param accessoriesType 辅料类型
     */
    CommonResult updAccessoriesType(AccessoriesType accessoriesType);

    /**
     * 删除辅料类型
     *
     * @param id 辅料类型id
     */
    CommonResult delAccessoriesType(String id);

    /**
     * 辅料数据查询
     * @param accessories 辅料
     */
    CommonResult<List<Accessories>> queryAccessories(Accessories accessories);

    /**
     * 辅料新增
     * @param accessories 辅料
     */
    CommonResult addAccessories(Accessories accessories);

    /**
     * 辅料编辑
     * @param accessories 辅料
     */
    CommonResult updAccessories(Accessories accessories);

    /**
     * 辅料删除
     * @param ids 辅料id
     */
    CommonResult delAccessories(String ids);

    /**
     * 导出辅料
     * @param response
     * @param ids 辅料id
     */
    void exportAccessories(HttpServletResponse response, String ids);
}
