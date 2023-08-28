package com.erp.produce.controller;

import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.produce.domain.PatchBusiness;
import com.erp.floor.pojo.produce.domain.Scheduling;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.produce.service.SchedulingService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/19 14:53
 */
@RestController
@Api(tags = "生产管理-生产计划")
@RequestMapping("/produce/scheduling")
public class SchedulingController extends BaseController {

    @Resource
    private SchedulingService service;

    @PreAuthorize("@ss.hasPermi('system:scheduling:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增计划")
    public CommonResult add(@RequestBody Scheduling scheduling){
        return service.addScheduling(scheduling);
    }


    @GetMapping("/list")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("排产列表")
    public CommonResult list(Scheduling scheduling){
        startPage();
        return service.getSchedulingList(scheduling);
    }

    @PreAuthorize("@ss.hasPermi('system:scheduling:info')")
    @GetMapping("/info/{id}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("查看明细")
    public CommonResult getInfo(@PathVariable("id") String id){
        return service.getInfo(id);
    }

    @PreAuthorize("@ss.hasPermi('system:scheduling:edit')")
    @PutMapping("/edit")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("修改排产")
    public CommonResult edit(@RequestBody Scheduling scheduling){
        return service.editScheduling(scheduling);
    }

    @PreAuthorize("@ss.hasPermi('system:scheduling:del')")
    @DeleteMapping("/del/{ids}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("删除排产")
    public CommonResult edit(@PathVariable("ids") String ids){
        return service.delScheduling(ids);
    }

    @GetMapping("/getOrderList")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("获取订单信息")
    public CommonResult getOrderList(OrderRecord orderRecord){
        startPage();
        return service.getOrderList(orderRecord);
    };

    @GetMapping("/getFlowCardList")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("获取订单信息")
    public CommonResult getFlowCardList(FlowCard flowCard, @RequestParam Map<String,String> params){
        startPage();
        return service.getFlowCardList(flowCard,params);
    };

    @ApiOperation(value = "导出报损")
    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermi('system:scheduling:exportDetail')")
    public void export(HttpServletResponse response, String ids){
        service.exportAll(response, ids);
    }

    @ApiOperation(value = "导出报损详细")
    @PostMapping("/exportDetail")
    @PreAuthorize("@ss.hasPermi('system:scheduling:exportDetail')")
    public void exportDetail(HttpServletResponse response, String id){
        service.exportDetail(response, id);
    }
}
