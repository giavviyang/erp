package com.erp.floor.pojo.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 16:04
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_template_file")
@ApiModel(value = "系统模板文件", description = "系统模板文件实体")
public class SysTemplateFile {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("文件ID-(长度255)")
    private String id;

    @TableField(value = "template_id")
    @ApiModelProperty("模板ID-(长度255)")
    private String templateId;

    @TableField(value = "file_name")
    @ApiModelProperty("文件名-(长度255)")
    private String fileName;

    @TableField(value = "file_type")
    @ApiModelProperty("文件类型-(长度255)")
    private String fileType;

    @TableField(value = "file_size")
    @ApiModelProperty("文件大小-(长度255)")
    private String fileSize;

    @TableField(value = "file_path")
    @ApiModelProperty("文件地址-(长度255)")
    private String filePath;
}
