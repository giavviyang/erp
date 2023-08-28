package com.erp.original.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.original.domain.OriginalFilmPurchase;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-09 12:28
 */
public interface OriginalFilmPurchaseService extends IService<OriginalFilmPurchase> {

    /**
     * 自动生成编号
     */
    String productionNumber();

    /**
     * 原片采购查询
     * @param originalFilmPurchase 采购
     */
    CommonResult<List<OriginalFilmPurchase>> queryPurchaseData(OriginalFilmPurchase originalFilmPurchase);

    /**
     * 查看明细
     * @param id 采购id
     */
    CommonResult<List<OriginalFilmPurchase>> viewDetail(String id);

    /**
     * 原片采购新增
     * @param originalFilmPurchase 采购
     */
    CommonResult addPurchaseData(OriginalFilmPurchase originalFilmPurchase);

    /**
     * 原片采购编辑查询
     * @param id 采购id
     * @return
     */
    CommonResult updPurchaseDataQuery(String id);

    /**
     * 原片采购编辑
     * @param originalFilmPurchase 采购
     */
    CommonResult updPurchaseData(OriginalFilmPurchase originalFilmPurchase);

    /**
     * 原片采购删除
     * @param ids 采购id
     * @return
     */
    CommonResult delPurchaseData(String ids);

    /**
     * 导出原片采购单
     * @param ids 采购id集合
     */
    void exportPurchase(HttpServletResponse response, String ids);

    /**
     * 导出原片采购明细
     * @param ids 采购id集合
     */
    void exportPurchaseData(HttpServletResponse response, String ids);
}
