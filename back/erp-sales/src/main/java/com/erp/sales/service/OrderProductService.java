package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OrderProduct;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.ProductProcess;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-26 18:29
 */
public interface OrderProductService extends IService<OrderProduct> {

    /**
     * 新增产品工艺对应关系
     * @param orderProduct 产品对象
     * @param del 是否是新增操作
     */
    void addProductProcess(OrderProduct orderProduct, int del);

    /**
     * 删除产品所有信息
     * @param orderIds 订单id集合
     */
    void delProduct(List<String> orderIds);

    /**
     * 查看工艺-----工艺审核展示
     * @param orderId 订单id
     * @return
     */
    List<Map<String, Object>> queryProcess(String orderId);

    /**
     * 修改产品工艺对应关系
     * @param productProcess
     * @return
     */
    AjaxResult updateProcess(List<Map<String, Object>> productProcess);

    /**
     * 自然分架中查询产品数据
     * @param orderRecords
     * @return
     */
    List<OrderProduct> selectProduct(List<OrderRecord> orderRecords);

    /**
     * 查询每个产品的各个单片属性
     * @param orderProducts 产品集合
     */
    List<ProductProcess> selectProcess(List<OrderProduct> orderProducts);

    /**
     * 导出产品
     * @param ids 订单id
     */
    void exportProduct(HttpServletResponse response, String ids);

    /**
     * 导出迪赛模板
     * @param id 订单id
     */
    void exportMachineFlow(HttpServletResponse response, String id);

    /**
     * 手动分架-查询订单中未分架的半产品信息
     * @param orderRecords
     */
    List<ProductProcess> selectSemiProduct(List<OrderRecord> orderRecords);

    /**
     * 查询可打包产品
     * @param orderProduct
     */
    CommonResult<List<OrderProduct>> queryPackProduct(OrderProduct orderProduct);

}
