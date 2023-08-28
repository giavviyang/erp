package com.erp.floor.pojo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/26 15:57
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_template_type")
@ApiModel(value = "系统模板类型", description = "系统模板类型实体")
public class SysTemplateType {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("类型ID-(长度255)")
    private String id;

    @TableField(value = "type_name")
    @ApiModelProperty("类型名称-(长度255)")
    private String typeName;

    @TableField(value = "tid")
    @ApiModelProperty("父类型ID-(长度255)")
    private String tid;

    @TableField(value = "created_person",fill = FieldFill.INSERT)
    @ApiModelProperty("创建人-(长度255)")
    private String createdPerson;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @TableField(value = "updated_person",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改人-(长度255)")
    private String updatedPerson;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;
}
