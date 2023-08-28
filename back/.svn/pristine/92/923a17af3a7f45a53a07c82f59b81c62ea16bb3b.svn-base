package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.Contract;
import com.erp.floor.pojo.sales.domain.ContractFile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-17 15:01
 */
public interface ContractService extends IService<Contract> {

    /**
     * 查询合同
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param contractTitle 合同标题
     * @param contractPerson 合同负责人
     */
    CommonResult<List<Contract>> queryAll(String startDate, String endDate, String contractTitle, String contractPerson, int pageSize, int pageNum);

    /**
     * 新增合同
     * @param contract 合同对象
     */
    CommonResult addContract( Contract contract);

    /**
     * 编辑合同
     * @param contract 合同对象
     */
    CommonResult updateContract( Contract contract);

    /**
     * 删除合同
     * @param ids 合同id集合
     */
    CommonResult delContract(String ids);

    /**
     * 查询合同文件
     * @param id 合同id
     */
    CommonResult<List<ContractFile>> queryContractFile(String id);

    /**
     * 依据文件id查询合同文件
     * @param id 合同文件id
     */
    CommonResult<List<ContractFile>> queryAllFile(String id, Integer pageNum, Integer pageSize);

    /**
     * 上传合同文件
     * @param file 文件
     * @param id 合同id
     */
    CommonResult uploadContractFile(MultipartFile file , String id);

    /**
     * 下载合同文件
     * @param id 合同文件id
     */
    void downloadContractFile(String id, HttpServletResponse response);

    /**
     * 删除合同文件
     * @param id 合同文件id
     */
    CommonResult delContractFile(String id);

    /**
     * 预览文件
     * @param id 合同文件id
     */
    void pdfView(String id, HttpServletResponse response);
}
