package com.erp.produce.controller;

import com.erp.common.constant.ResultConstants;
import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.StringUtils;
import com.erp.floor.pojo.produce.domain.CompletionRecord;
import com.erp.produce.service.CompletionService;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/29 13:57
 */
@RestController
@Api(tags = "生产管理-完工管理")
@RequestMapping("/produce/completion")
public class CompletionController {

    @Resource
    private CompletionService service;


    @PreAuthorize("@ss.hasPermi('system:completion:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增完工")
    public CommonResult add(@RequestBody CompletionRecord record){
        try {
            return service.insertCompletion(record);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:completion:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("编辑完工")
    public CommonResult edit(@PathVariable("id") String id,@RequestBody CompletionRecord record){
        try {
            return service.editCompletion(id,record);
        } catch (Exception e) {
            return CommonResult.error(e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('system:completion:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("删除完工")
    public CommonResult edit(@PathVariable("id") String id){
        try {
            List<String> ids = Arrays.asList(id.split(","));
            return service.delCompletion(ids);
        } catch (Exception e) {
            System.out.println(e);
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    @GetMapping("/list")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("完工列表")
    public CommonResult<CompletionRecord> list(@RequestParam Map<String,Object> params){
        if(params.get("pageSize") == null){
            params.put("pageSize",20);
        }
        int startIndex = 0;
        if(StringUtils.isNotEmpty((String) params.get("pageNum")) && Integer.parseInt(String.valueOf(params.get("pageNum"))) != 0){
            startIndex = (Integer.parseInt(String.valueOf(params.get("pageNum"))) - 1) * Integer.parseInt(String.valueOf(params.get("pageSize")));
        }
        params.put("startIndex",startIndex);
        params.put("endIndex",params.get("pageSize"));
        params.remove("pageSize");
        PageHelper.clearPage();
        return service.getCompletionRecord(params);
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("完工详情")
    public CommonResult<CompletionRecord> getInfo(@PathVariable("id") String id){
        return service.getCompletionInfo(id);
    }

    @GetMapping("/getFlowCardInfo")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("可完工流程卡")
    public CommonResult<CompletionRecord> getFlowCardInfo(@RequestParam Map<String,Object> params){
        return service.getFlowCardInfo(params);
    }

    @ApiOperation(value = "导出订单")
    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermi('system:completion:export')")
    public void export(HttpServletResponse response, String ids){
        service.exportCompletion(response, ids);
    }

}
