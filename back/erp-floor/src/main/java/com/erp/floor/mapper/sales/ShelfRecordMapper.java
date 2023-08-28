package com.erp.floor.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.sales.domain.ShelfRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 17:37
 */
@Mapper
public interface ShelfRecordMapper extends BaseMapper<ShelfRecord> {

    List<ShelfRecord> queryByReceiptId(String deliverReceiptId);

}
