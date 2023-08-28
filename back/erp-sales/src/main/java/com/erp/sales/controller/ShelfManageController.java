package com.erp.sales.controller;

import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.ShelfManage;
import com.erp.floor.pojo.sales.domain.ShelfRecord;
import com.erp.sales.service.ShelfManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 15:54
 */
@RestController
@RequestMapping("/sales/shelfManage")
@Api(tags = "销售管理-铁架管理")
public class ShelfManageController {
    @Autowired
    private ShelfManageService shelfManageService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "frameNo",value = "铁架编号" , dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "frameName",value = "铁架名称" , dataType = "String", paramType = "query"),
    })
    @ApiOperation("查询铁架数据")
    @PostMapping("/queryAll")
    public CommonResult<List<ShelfManage>> queryAll(String frameNo, String frameName, Integer pageNum, Integer pageSize) {
        return shelfManageService.queryAll(frameNo, frameName, pageNum, pageSize);
    }

    @ApiOperation("新增铁架")
    @PostMapping("/addShelf")
    @PreAuthorize("@ss.hasPermi('sales:shelfManage:add')")
    public CommonResult addShelf(@RequestBody ShelfManage shelfManage) {
        return shelfManageService.addShelf(shelfManage);
    }

    @ApiOperation("编辑铁架")
    @PostMapping("/updateShelf")
    @PreAuthorize("@ss.hasPermi('sales:shelfManage:edit')")
    public CommonResult updateShelf(@RequestBody ShelfManage shelfManage) {
        return shelfManageService.updateShelf(shelfManage);
    }

    @ApiOperation("删除铁架")
    @PostMapping("/delShelf")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "铁架id" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:shelfManage:del')")
    public CommonResult delShelf(String ids) {
        return shelfManageService.delShelf(ids);
    }

    @ApiOperation("查看出入库记录")
    @PostMapping("/queryShelfRecord")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "铁架id" , dataType = "String", paramType = "query"),
    })
    @PreAuthorize("@ss.hasPermi('sales:shelfManage:queryShelfRecord')")
    public CommonResult<List<ShelfRecord>> queryShelfRecord(String id) {
        return shelfManageService.queryShelfRecord(id);
    }

}
