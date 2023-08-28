package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.config.RuoYiConfig;
import com.erp.common.constant.HttpStatus;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.file.FileUploadUtils;
import com.erp.common.utils.file.FileUtils;
import com.erp.floor.mapper.sales.OrderEnclosureMapper;
import com.erp.floor.pojo.sales.domain.OrderEnclosure;
import com.erp.sales.service.OrderEnclosureService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-08 13:52
 */
@Service
public class OrderEnclosureServiceImpl extends ServiceImpl<OrderEnclosureMapper, OrderEnclosure> implements OrderEnclosureService {
    @Resource
    private OrderEnclosureMapper orderEnclosureMapper;

    @Override
    public CommonResult<List<OrderEnclosure>> queryEnclosure(String orderId) {
        LambdaQueryWrapper<OrderEnclosure> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderEnclosure::getOrderId, orderId);
        List<OrderEnclosure> orderEnclosures = orderEnclosureMapper.selectList(wrapper);
        CommonResult commonResult = new CommonResult();
        commonResult.setData(orderEnclosures);
        return commonResult;
    }

    /**
     * 上传附件
     *
     * @param file    附件
     * @param orderId 订单id
     * @return
     */
    @Override
    public CommonResult uploadEnclosure(MultipartFile file, String orderId) {
        try {
                String uploadPath = RuoYiConfig.getUploadPath();
                String upload = FileUploadUtils.upload(uploadPath, file);
                OrderEnclosure orderEnclosure = new OrderEnclosure();
                orderEnclosure.setId(UUID.randomUUID().toString());
                orderEnclosure.setOrderId(orderId);
                orderEnclosure.setFileName(file.getOriginalFilename());
                orderEnclosure.setUploadDate(new Date());
                orderEnclosure.setFileSize(FileUtils.byteCountToDisplaySize(file.getSize(), 1024) + "KB");
                String y = uploadPath.substring(0,uploadPath.lastIndexOf("/")) + upload.substring(8);
                orderEnclosure.setFileAddress(y);
                orderEnclosure.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+ 1) );
                orderEnclosureMapper.insert(orderEnclosure);
            return new CommonResult(HttpStatus.SUCCESS, ResultConstants.SAVE_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResult(HttpStatus.ERROR, ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 下载附件
     *
     * @param id 主键
     */
    @Override
    public void downloadEnclosure(String id, HttpServletResponse response) {
        try {
            OrderEnclosure orderEnclosure = orderEnclosureMapper.selectById(id);
            String fileAddress = orderEnclosure.getFileAddress();
            String fileName = orderEnclosure.getFileName();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, fileName);
            FileUtils.writeBytes(fileAddress, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除附件
     *
     * @param id 主键
     */
    @Override
    public CommonResult delEnclosure(String id) {
        OrderEnclosure orderEnclosure = orderEnclosureMapper.selectById(id);
        if (FileUtils.deleteFile(orderEnclosure.getFileAddress())) {
            orderEnclosureMapper.deleteById(id);
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        }else {
            orderEnclosureMapper.deleteById(id);
            return CommonResult.error(ResultConstants.DELFILE_ERROR);
        }
    }
}
