package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.floor.mapper.sales.CollectionRecordMapper;
import com.erp.floor.mapper.sales.OrderMapper;
import com.erp.floor.pojo.sales.domain.CollectionRecord;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.sales.service.CollectionRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-11-19 14:12
 */
@Service
public class CollectionRecordServiceImpl extends ServiceImpl<CollectionRecordMapper, CollectionRecord> implements CollectionRecordService {
    @Resource
    private CollectionRecordMapper collectionRecordMapper;
    @Resource
    private OrderMapper orderMapper;

    /**
     * 查询订单
     *
     * @param orderRecordVo
     * @return
     */
    @Override
    public CommonResult<List<OrderRecord>> orderList(OrderRecordVo orderRecordVo) {
        PageHelper.startPage(orderRecordVo.getPageNum(), orderRecordVo.getPageSize());
        List<OrderRecord> orderRecords = orderMapper.queryCollectionOrder(orderRecordVo);
        PageInfo<OrderRecord> info = new PageInfo<>(orderRecords, orderRecordVo.getPageSize());
        return new CommonResult<List<OrderRecord>>(200, "查询成功！", (int)info.getTotal(), info.getList());
    }

    /**
     * 查询当前订单收款记录
     *
     * @param id 订单id
     */
    @Override
    public CommonResult<List<CollectionRecord>> list(String id) {
        List<CollectionRecord> collectionRecords = collectionRecordMapper
                .selectList(Wrappers.<CollectionRecord>query().lambda().eq(CollectionRecord::getOrderId, id));
        return CommonResult.success(collectionRecords);
    }

    /**
     * 新增收款记录
     *
     * @param collectionRecord 收款记录
     */
    @Override
    public CommonResult add(CollectionRecord collectionRecord) {
        try {
            collectionRecord.setId(UUID.randomUUID().toString());
            collectionRecord.setCreatedAt(new Date());
            collectionRecord.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            collectionRecordMapper.insert(collectionRecord);
            updateOrder(0, collectionRecord);
            return CommonResult.success("收款成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error("收款失败");
        }
    }

    /**
     * 编辑收款记录
     *
     * @param collectionRecord 收款记录
     */
    @Override
    public CommonResult edit(CollectionRecord collectionRecord) {
        try {
            collectionRecord.setUpdatedAt(new Date());
            collectionRecord.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            updateOrder(2, collectionRecord);
            collectionRecordMapper.updateById(collectionRecord);
            return CommonResult.success("编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error("编辑失败");
        }
    }

    /**
     * 删除收款记录
     *
     * @param collectionRecord 收款记录
     */
    @Override
    public CommonResult del(CollectionRecord collectionRecord) {
        try {
            collectionRecordMapper.deleteById(collectionRecord.getId());
            updateOrder(1, collectionRecord);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error("删除失败");
        }
    }


    //修改订单收款金额和优惠金额
    @Async
    public void updateOrder(int type, CollectionRecord collectionRecord) {
        //先查询订单
        OrderRecord orderRecord = orderMapper.selectById(collectionRecord.getOrderId());
        if (type == 0) {//新增
            orderRecord.setReceivedAmount(orderRecord.getReceivedAmount() + collectionRecord.getCollectionAmount());
            orderRecord.setDiscountAmount(orderRecord.getDiscountAmount() + collectionRecord.getPreferentialAmount());
            if(orderRecord.getReceivedAmount() + orderRecord.getDiscountAmount() >= orderRecord.getOrderAmount()) {
                orderRecord.setCollectionStatus(2);
            }else {
                orderRecord.setCollectionStatus(1);
            }
        }else if (type == 1) {//删除
            orderRecord.setReceivedAmount(orderRecord.getReceivedAmount() - collectionRecord.getCollectionAmount());
            orderRecord.setDiscountAmount(orderRecord.getDiscountAmount() - collectionRecord.getPreferentialAmount());
            if(orderRecord.getReceivedAmount() + orderRecord.getDiscountAmount() != 0) {
                orderRecord.setCollectionStatus(1);
            }else {
                orderRecord.setCollectionStatus(0);
            }
        }else {//编辑
            //查询旧收款记录
            CollectionRecord old = collectionRecordMapper.selectById(collectionRecord.getId());
            orderRecord.setReceivedAmount(orderRecord.getReceivedAmount() + collectionRecord.getCollectionAmount() - old.getCollectionAmount());
            orderRecord.setDiscountAmount(orderRecord.getDiscountAmount() + collectionRecord.getPreferentialAmount() - old.getPreferentialAmount());
            if(orderRecord.getReceivedAmount() + orderRecord.getDiscountAmount() >= orderRecord.getOrderAmount()) {
                orderRecord.setCollectionStatus(2);
            }else {
                orderRecord.setCollectionStatus(1);
            }
        }
        orderMapper.updateById(orderRecord);
    }




}
