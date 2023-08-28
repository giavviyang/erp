package com.erp.system.controller;

import com.erp.common.constant.ResultConstants;
import com.erp.common.core.controller.BaseController;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysTemplate;
import com.erp.floor.pojo.system.domain.SysTemplateType;
import com.erp.system.service.ISysTemplateService;
import com.erp.system.service.ISysTemplateTypeService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 16:08
 */
@RestController
@Api(tags = "系统管理-模板管理")
@RequestMapping("/system/template")
public class SysTemplateController  extends BaseController {

    @Resource
    private ISysTemplateService service;

    @PreAuthorize("@ss.hasPermi('system:template:add')")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {"id","created_at","updated_at"})
    @ApiOperation("新增模板")
    public CommonResult add(@RequestBody SysTemplate sysTemplate)
    {
        try {
            return service.insertTemplate(sysTemplate);
        } catch (Exception e) {
            System.out.println(e);
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:template:edit')")
    @PutMapping("/edit/{id}")
    @ApiOperationSupport(ignoreParameters = {"created_at","updated_at"})
    @ApiOperation("编辑模板")
    public CommonResult edit(@PathVariable("id") String id, @RequestBody SysTemplate sysTemplate)
    {
        try {
            return service.updatedTemplate(id,sysTemplate);
        } catch (Exception e) {
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:template:del')")
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除模板")
    public CommonResult del(@PathVariable("id") String id)
    {
        try {
            List<String> ids = Arrays.asList(id.split(","));
            return service.deletedTemplate(ids);
        } catch (Exception e) {
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:template:download')")
    @GetMapping("/down/{id}")
    @ApiOperation("下载模板文件")
    public ResponseEntity<byte[]> down(@PathVariable("id") String id) throws IOException {
        return service.download(id);
    }

    @GetMapping("/list")
    @ApiOperation("模板列表")
    public CommonResult<List<SysTemplate>> list(SysTemplate sysTemplate)
    {
        startPage();
        return service.getTemplateList(sysTemplate);
    }

    @PreAuthorize("@ss.hasPermi('system:template:detail')")
    @GetMapping("/detailed/{id}")
    @ApiOperation("模板详情")
    public CommonResult<SysTemplate> detailed(@PathVariable("id") String id)
    {
        return service.getTemplateDetailed(id);
    }

    @PreAuthorize("@ss.hasPermi('system:template:upload')")
    @PostMapping("/upload")
    @ApiOperation("模板文件上传")
    public CommonResult upload(@RequestParam MultipartFile file)
    {
        try {
            return service.uploadTemplat(file);
        } catch (IOException e) {
            System.out.println(e);
            return CommonResult.error(ResultConstants.UPLOAD_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('system:template:getInfo')")
    @PostMapping("/getInfo/{id}")
    @ApiOperation("模板详情")
    public CommonResult getInfo(@PathVariable("id") String id,@RequestBody Map<String, Object> formData)
    {
        return service.getTemplateData(id,formData);
    }
}
