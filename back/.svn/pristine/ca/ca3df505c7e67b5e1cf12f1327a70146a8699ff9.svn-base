package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.config.RuoYiConfig;
import com.erp.common.constant.HttpStatus;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.StringUtils;
import com.erp.common.utils.file.FileUploadUtils;
import com.erp.common.utils.file.FileUtils;
import com.erp.floor.mapper.sales.ContractFileMapper;
import com.erp.floor.mapper.sales.ContractMapper;
import com.erp.floor.pojo.sales.domain.Contract;
import com.erp.floor.pojo.sales.domain.ContractFile;
import com.erp.floor.pojo.sales.domain.OrderEnclosure;
import com.erp.floor.pojo.sales.domain.OrderRecord;
import com.erp.sales.service.ContractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-17 15:01
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractFileMapper contractFileMapper;


    /**
     * 查询合同
     *
     * @param startDate      开始日期
     * @param endDate        结束日期
     * @param contractTitle  合同标题
     * @param contractPerson 合同负责人
     */
    @Override
    public CommonResult<List<Contract>> queryAll(String startDate, String endDate, String contractTitle, String contractPerson, int pageSize, int pageNum) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        if (startDate != null && endDate != null) wrapper.between(Contract::getContractDate, startDate, endDate);
        if (StringUtils.isNotEmpty(contractTitle)) wrapper.like(Contract::getContractTitle, contractTitle);
        if (StringUtils.isNotEmpty(contractPerson)) wrapper.like(Contract::getContractPerson, contractPerson);
        wrapper.orderByDesc(Contract::getContractDate);
        Integer integer = contractMapper.selectCount(wrapper);
        PageHelper.startPage(pageNum, pageSize);
        List<Contract> contracts = contractMapper.selectList(wrapper);
        PageInfo<Contract> pageInfo = new PageInfo<>(contracts, pageSize);
        return new CommonResult<>(HttpStatus.SUCCESS, ResultConstants.QUERY_SUCCESS, integer, pageInfo.getList());
    }

    /**
     * 新增合同
     *
     * @param contract 合同对象
     */
    @Override
    public CommonResult addContract(Contract contract) {
        try {
            contract.setCreatedAt(new Date());
            contract.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            contractMapper.insert(contract);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
        return CommonResult.success(ResultConstants.SAVE_SUCCESS);
    }

    /**
     * 编辑合同
     *
     * @param contract 合同对象
     */
    @Override
    public CommonResult updateContract(Contract contract) {
        try {
            contract.setUpdatedAt(new Date());
            contract.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            contractMapper.updateById(contract);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
        return CommonResult.success(ResultConstants.UPD_SUCCESS);
    }

    /**
     * 删除合同
     *
     * @param ids 合同id集合
     */
    @Override
    public CommonResult delContract(String ids) {
        String[] split = ids.split(",");
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Contract::getId, split);
        contractMapper.delete(wrapper);
        LambdaQueryWrapper<ContractFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractFile::getContractId, split);
        List<ContractFile> contractFiles = contractFileMapper.selectList(queryWrapper);
        for (ContractFile contractFile : contractFiles) {
            FileUtils.deleteFile(contractFile.getFileAddress());
        }
        contractFileMapper.delete(queryWrapper);
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 查询合同文件
     *
     * @param id 合同id
     */
    @Override
    public CommonResult<List<ContractFile>> queryContractFile(String id) {
        LambdaQueryWrapper<ContractFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractFile::getContractId, id);
        return CommonResult.success(contractFileMapper.selectList(wrapper));
    }

    /**
     * 依据文件id查询合同文件
     *
     * @param id 合同文件id
     */
    @Override
    public CommonResult<List<ContractFile>> queryAllFile(String id, Integer pageNum, Integer pageSize) {
        ContractFile contractFile = contractFileMapper.selectById(id);
        List<ContractFile> contractFiles = contractFileMapper.selectList(Wrappers.<ContractFile>query().lambda().eq(ContractFile::getContractId, contractFile.getContractId()));
        Integer integer = contractFileMapper.selectCount(Wrappers.<ContractFile>query().lambda().eq(ContractFile::getContractId, contractFile.getContractId()));
        return new CommonResult<>(200, ResultConstants.QUERY_SUCCESS, integer, contractFiles);
    }

    /**
     * 上传合同文件
     *
     * @param file 文件
     * @param id   合同id
     */
    @Override
    public CommonResult uploadContractFile(MultipartFile file, String id) {
        try {
            String uploadPath = RuoYiConfig.getContractUploadPath();
            String upload = FileUploadUtils.upload(uploadPath, file);
            ContractFile contractFile = new ContractFile();
            contractFile.setId(UUID.randomUUID().toString());
            contractFile.setContractId(id);
            contractFile.setFileName(file.getOriginalFilename());
            contractFile.setCreatedAt(new Date());
            contractFile.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            contractFile.setFileSize(FileUtils.byteCountToDisplaySize(file.getSize(), 1024) + "KB");
            String y = uploadPath.substring(0,uploadPath.lastIndexOf("/")) + upload.substring(8);
            contractFile.setFileAddress(y);
            contractFileMapper.insert(contractFile);
            return new CommonResult(HttpStatus.SUCCESS, ResultConstants.SAVE_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResult(HttpStatus.ERROR, ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 下载合同文件
     * @param id 合同文件id
     */
    @Override
    public void downloadContractFile(String id, HttpServletResponse response) {
        try {
            ContractFile contractFile = contractFileMapper.selectById(id);
            String fileAddress = contractFile.getFileAddress();
            String fileName = contractFile.getFileName();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, fileName);
            FileUtils.writeBytes(fileAddress, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除合同文件
     * @param id 合同文件id
     */
    @Override
    public CommonResult delContractFile(String id) {
        ContractFile contractFile = contractFileMapper.selectById(id);
        FileUtils.deleteFile(contractFile.getFileAddress());
        contractFileMapper.deleteById(id);
        return CommonResult.success(ResultConstants.DEL_SUCCESS);
    }

    /**
     * 预览文件
     *
     * @param id 合同文件id
     */
    @Override
    public void pdfView(String id, HttpServletResponse response) {
        ContractFile contractFile = contractFileMapper.selectById(id);
        try {
            //本地存储
            byte[] fileByte = FileUtils.getFileByte(contractFile.getFileAddress());
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" +
                    java.net.URLEncoder.encode(contractFile.getFileName(), "UTF-8"));
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(fileByte);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
