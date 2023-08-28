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
 * @date 2022/7/22 14:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_mill")
@ApiModel(value = "厂家", description = "厂家实体")
public class SysMill {

    @TableId(value = "id",type = IdType.UUID)
    @ApiModelProperty("厂家ID-(长度255)")
    private String id;

    @TableField(value = "mill_name")
    @ApiModelProperty("厂家名称-(长度255)")
    private String millName;

    @TableField(value = "no")
    @ApiModelProperty("厂家编号-(长度255)")
    private String no;

    @TableField(value = "type_name")
    @ApiModelProperty("厂家类型-(长度255)")
    private String typeName;

    @TableField(value = "type_id")
    @ApiModelProperty("厂家类型ID-(长度255)")
    private String typeId;

    @TableField(value = "person")
    @ApiModelProperty("厂家联系人-(长度255)")
    private String person;

    @TableField(value = "phone")
    @ApiModelProperty("联系电话-(长度255)")
    private String phone;

    @TableField(value = "tel")
    @ApiModelProperty("固定电话-(长度255)")
    private String tel;

    @TableField(value = "fax")
    @ApiModelProperty("传真-(长度255)")
    private String fax;

    @TableField(value = "mail")
    @ApiModelProperty("电子邮箱-(长度255)")
    private String mail;

    @TableField(value = "address")
    @ApiModelProperty("地址-(长度255)")
    private String address;

    @TableField(value = "remarks")
    @ApiModelProperty("备注-(长度255)")
    private String remarks;

    @TableField(value = "created_person")
    @ApiModelProperty("创建人-(长度255)")
    private String createdPerson;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty("修改人-(长度255)")
    private String updatedPerson;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;
}
