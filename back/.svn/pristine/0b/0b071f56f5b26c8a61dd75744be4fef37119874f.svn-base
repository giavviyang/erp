package com.erp.sales.controller;

import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.CollectionRecord;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.sales.service.CollectionRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-11-19 14:16
 */
@RestController
@RequestMapping("/sales/collection")
@Api(tags = "销售管理-收款管理")
public class CollectionRecordController {
    @Resource
    private CollectionRecordService collectionRecordService;

    @ApiOperation(value = "查询收款记录")
    @PostMapping("/orderList")
    public CommonResult<List<OrderRecord>> orderList(@RequestBody OrderRecordVo orderRecordVo) {
        return collectionRecordService.orderList(orderRecordVo);
    }


    @ApiOperation(value = "查询收款记录")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query"),
    })
    public CommonResult<List<CollectionRecord>> list(String id) {
        return collectionRecordService.list(id);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增收款记录")
    public CommonResult add(@RequestBody CollectionRecord collectionRecord) {
        return collectionRecordService.add(collectionRecord);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑收款记录")
    public CommonResult edit(@RequestBody CollectionRecord collectionRecord) {
        return collectionRecordService.edit(collectionRecord);
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除收款记录")
    public CommonResult del(@RequestBody CollectionRecord collectionRecord) {
        return collectionRecordService.del(collectionRecord);
    }






}
