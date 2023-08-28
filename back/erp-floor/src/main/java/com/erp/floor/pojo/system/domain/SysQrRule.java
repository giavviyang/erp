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
 * @date 2022/8/16 9:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_qr_rule")
@ApiModel(value = "二维码规则", description = "二维码规则实体")
public class SysQrRule {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("二维码规则ID-(长度255)")
    private String id;

    @TableField(value = "qr_name")
    @ApiModelProperty("规则名称-(长度255)")
    private String qrName;

    @TableField(value = "qr_content")
    @ApiModelProperty("规则内容-(长度255)")
    private String qrContent;

    @TableField(value = "qr_connect")
    @ApiModelProperty("字符连接符-(长度255)")
    private String qrConnect;

    @TableField(value = "status")
    @ApiModelProperty("启用状态-(长度0(0未启动，1已启动))")
    private Integer status;

    @TableField(value = "created_person",fill = FieldFill.INSERT)
    @ApiModelProperty("创建人-(长度255)")
    private String createdPerson;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @TableField(value = "updated_person",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新人-(长度255)")
    private String updatedPerson;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updatedAt;
}
