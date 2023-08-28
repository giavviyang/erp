package com.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.system.domain.SysTemplate;
import com.erp.floor.pojo.system.domain.SysTemplateType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/21 15:43
 */
public interface ISysTemplateService extends IService<SysTemplate> {
    /**
     * 插入一条模板的数据
     * @return 插入结果
     */
    CommonResult insertTemplate(SysTemplate sysTemplate) throws Exception;

    /**
     * 修改此id模板的数据
     * @return 修改结果
     */
    CommonResult updatedTemplate(String id,SysTemplate sysTemplate) throws Exception;

    /**
     * 删除此id模板的数据
     * @return 删除结果
     */
    CommonResult deletedTemplate(List<String> ids) throws Exception;

    /**
     * 获取分页的数据
     * @return 数据列表
     */
    CommonResult getTemplateList(SysTemplate sysTemplate);


    /**
     * 获取id的详细数据
     * @return 数据
     */
    CommonResult getTemplateDetailed(String id);

    /**
     * 上传模板文件
     * @param file
     * @return
     */
    CommonResult uploadTemplat(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> download(String id) throws IOException;

    /**
     * 获取模板的数据
     * @param id 模板id
     * @param formData 参数条件
     * @return
     */
    CommonResult getTemplateData(String id, Map<String, Object> formData);
}
