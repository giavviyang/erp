package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.CollectionRecord;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-11-19 14:11
 */
public interface CollectionRecordService extends IService<CollectionRecord> {

    /**
     * 查询订单
     * @param orderRecordVo
     * @return
     */
    CommonResult<List<OrderRecord>> orderList(OrderRecordVo orderRecordVo);

    /**
     * 查询当前订单收款记录
     * @param id 订单id
     */
    CommonResult<List<CollectionRecord>> list(String id);

    /**
     * 新增收款记录
     * @param collectionRecord 收款记录
     */
    CommonResult add(@RequestBody CollectionRecord collectionRecord);

    /**
     * 编辑收款记录
     * @param collectionRecord 收款记录
     */
    CommonResult edit(@RequestBody CollectionRecord collectionRecord);

    /**
     * 删除收款记录
     * @param collectionRecord 收款记录
     */
    CommonResult del(@RequestBody CollectionRecord collectionRecord);
}
