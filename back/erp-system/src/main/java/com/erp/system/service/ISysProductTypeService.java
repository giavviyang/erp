package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysProductType;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 17:18
 */
public interface ISysProductTypeService extends IService<SysProductType> {
    /**
     * 插入一条产品类型的数据
     * @return 插入结果
     */
    CommonResult insertProductType(SysProductType sysProductType);

    /**
     * 插入一条产品类型的数据
     * @return 修改结果
     */
    CommonResult updatedProductType(String id,SysProductType sysProductType);

    /**
     * 删除此id产品类型的数据
     * @return 删除结果
     */
    CommonResult deletedProductType(String id);

    /**
     * 获取全部产品类型的数据
     * @return 数据列表
     */
    CommonResult getAllProductType();
}
