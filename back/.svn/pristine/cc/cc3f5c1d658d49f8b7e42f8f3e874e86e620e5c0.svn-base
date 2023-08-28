package com.erp.sales.controller;

import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.Contract;
import com.erp.floor.pojo.sales.domain.ContractFile;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.floor.pojo.sales.domain.vo.OrderRecordVo;
import com.erp.sales.service.ContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-17 15:02
 */
@RestController
@RequestMapping("/sales/contract")
@Api(tags = "销售管理-合同管理")
public class ContractController {
    @Resource
    private ContractService contractService;

    @ApiOperation(value = "查询合同")
    @PostMapping("/list")
    public CommonResult<List<Contract>> list(String startDate, String endDate, String contractTitle, String contractPerson, int pageSize, int pageNum) {
        return contractService.queryAll(startDate, endDate, contractTitle, contractPerson, pageSize, pageNum);
    }

    @ApiOperation(value = "新增合同")
    @PreAuthorize("@ss.hasPermi('sales:contract:addContract')")
    @PostMapping("/addContract")
    public CommonResult addContract(@RequestBody Contract contract) {
        return contractService.addContract(contract);
    }

    @ApiOperation(value = "编辑合同")
    @PreAuthorize("@ss.hasPermi('sales:contract:updateContract')")
    @PostMapping("/updateContract")
    public CommonResult updateContract(@RequestBody Contract contract) {
        return contractService.updateContract(contract);
    }

    @ApiOperation(value = "删除合同")
    @PreAuthorize("@ss.hasPermi('sales:contract:delContract')")
    @PostMapping("/delContract")
    public CommonResult delContract(String ids) {
        return contractService.delContract(ids);
    }

    @ApiOperation(value = "查询合同文件")
    @PostMapping("/queryContractFile")
    public CommonResult<List<ContractFile>> queryContractFile(String id) {
        return contractService.queryContractFile(id);
    }

    @ApiOperation(value = "依据文件id查询合同文件")
    @PostMapping("/queryAllFile")
    public CommonResult<List<ContractFile>> queryAllFile(String id, Integer pageNum, Integer pageSize) {
        return contractService.queryAllFile(id, pageNum, pageSize);
    }

    @ApiOperation(value = "上传合同文件")
    @PreAuthorize("@ss.hasPermi('sales:contract:uploadContractFile')")
    @PostMapping("/uploadContractFile")
    public CommonResult uploadContractFile(MultipartFile file ,String id) {
        return contractService.uploadContractFile(file, id);
    }

    @ApiOperation(value = "下载合同文件")
    @PreAuthorize("@ss.hasPermi('sales:contract:downloadContractFile')")
    @PostMapping("/downloadContractFile")
    public void downloadContractFile(String id, HttpServletResponse response) {
        contractService.downloadContractFile(id, response);
    }

    @ApiOperation(value = "删除合同文件")
    @PreAuthorize("@ss.hasPermi('sales:contract:delContractFile')")
    @PostMapping("/delContractFile")
    public CommonResult delContractFile(String id) {
        return contractService.delContractFile(id);
    }

    @ApiOperation(value = "预览文件")
    @GetMapping("/pdfView")
    public void pdfView(String id, HttpServletResponse response) {
        contractService.pdfView(id, response);
    }

}
