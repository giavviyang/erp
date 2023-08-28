package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.OrderEnclosure;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-08 13:52
 */
public interface OrderEnclosureService extends IService<OrderEnclosure> {

    CommonResult<List<OrderEnclosure>> queryEnclosure(String orderId);

    /**
     * 上传附件
     * @param file 附件
     * @param orderId 订单id
     */
    CommonResult uploadEnclosure(MultipartFile file , String orderId);

    /**
     * 下载附件
     * @param id 主键
     */
    void downloadEnclosure(String id, HttpServletResponse response);

    /**
     * 删除附件
     * @param id 主键
     */
    CommonResult delEnclosure(String id);
}
