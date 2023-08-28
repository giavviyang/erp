package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.Outsourced;
import com.erp.floor.pojo.sales.domain.OutsourcedProducts;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-23 14:14
 */
public interface OutsourcedService extends IService<Outsourced> {

    /**
     * 自动生成编号
     */
    String productionNumber();

    /**
     * 查询外协记录
     * @param outsourced 外协对象
     */
    CommonResult<List<Outsourced>> queryOutsourced(Outsourced outsourced);

    /**
     * 查询可外协产品
     * @param orderProduct 产品对象
     */
    CommonResult<List<OrderProduct>> queryOutsourcedProduct(OrderProduct orderProduct);

    /**
     * 新增外协单
     * @param outsourced 外协对象
     */
    CommonResult addOutsourced(Outsourced outsourced);

    /**
     * 查看明细
     * @param id 外协id
     */
    CommonResult<List<OutsourcedProducts>> detailedView(String id, int type);

    /**
     * 编辑查询
     * @param id 外协id
     */
    CommonResult<List<OrderProduct>> updateQuery(String id);

    /**
     * 编辑保存
     * @param outsourced 外协id
     */
    CommonResult updateOutsourced(@RequestBody Outsourced outsourced);

    /**
     * 删除外协
     * @param ids 外协ids
     */
    CommonResult delOutsourced(String ids);

    /**
     * 审核-消审
     * @param ids 外协ids
     * @param type
     */
    CommonResult outsourcedExamine(String ids, int type, String people, String text);

    /**
     * 导出外协记录
     * @param ids 外协ids
     */
    void exportOutsourced(HttpServletResponse response, String ids);

    /**
     * 导出外协记录明细
     * @param ids 外协ids
     */
    void exportOutsourcedDetailed(HttpServletResponse response, String ids);
}
