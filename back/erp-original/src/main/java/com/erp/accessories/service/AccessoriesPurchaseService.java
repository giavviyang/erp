package com.erp.accessories.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesPurchase;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 17:45
 */
public interface AccessoriesPurchaseService extends IService<AccessoriesPurchase> {

    /**
     * 自动生成编号
     */
    String productionNumber();

    /**
     * 辅料采购查询
     * @param accessoriesPurchase 采购
     */
    CommonResult<List<AccessoriesPurchase>> queryPurchaseData(AccessoriesPurchase accessoriesPurchase);

    /**
     * 查看明细
     * @param id 采购id
     */
    CommonResult<List<AccessoriesPurchase>> viewDetail(String id);

    /**
     * 辅料采购新增
     * @param accessoriesPurchase 采购
     */
    CommonResult addPurchaseData(AccessoriesPurchase accessoriesPurchase);

    /**
     * 查询编辑明细
     * @param id 采购id
     */
    CommonResult<List<Accessories>> updPurchaseDataQuery(String id);

    /**
     * 辅料采购编辑
     * @param accessoriesPurchase 采购
     */
    CommonResult updPurchaseData(AccessoriesPurchase accessoriesPurchase);

    /**
     * 辅料采购删除
     * @param ids 采购id
     * @return
     */
    CommonResult delPurchaseData(String ids);

    /**
     * 导出辅料采购单
     * @param ids 采购id集合
     */
    void exportPurchase(HttpServletResponse response, String ids);

    /**
     * 导出辅料采购明细
     * @param ids 采购id集合
     */
    void exportPurchaseData(HttpServletResponse response, String ids);
    
}
