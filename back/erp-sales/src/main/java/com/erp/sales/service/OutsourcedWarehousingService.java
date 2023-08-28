package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OutsourcedStockProduct;
import com.erp.floor.pojo.sales.domain.OutsourcedWarehousing;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-23 14:15
 */
@Service
public interface OutsourcedWarehousingService extends IService<OutsourcedWarehousing> {

    /**
     * 查询入库信息
     * @param outsourcedWarehousing
     */
    CommonResult<List<OutsourcedWarehousing>> queryData(OutsourcedWarehousing outsourcedWarehousing);

    /**
     * 新增入库信息
     * @param outsourcedWarehousing
     */
    CommonResult addData(OutsourcedWarehousing outsourcedWarehousing);

    /**
     * 查看明细
     * @param ids 入库id集合
     */
    CommonResult<List<OutsourcedStockProduct>> detailedView(String ids);

    /**
     * 删除明细
     * @param ids 入库id集合
     */
    CommonResult delData(String ids);

    /**
     * 导出入库记录
     * @param ids 入库id集合
     */
    void exportData(HttpServletResponse response, String ids);

    /**
     * 导出入库记录明细
     * @param ids 入库id集合
     */
    void exportDataDetailed(HttpServletResponse response, String ids);
}
