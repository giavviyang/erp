package com.erp.floor.mapper.original;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.floor.pojo.original.domain.OriginalFilmPurchaseDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-09 16:29
 */
@Mapper
public interface OriginalFilmPurchaseDetailMapper extends BaseMapper<OriginalFilmPurchaseDetail> {

    List<OriginalFilmPurchaseDetail> viewDetail(String[] ids);
}
