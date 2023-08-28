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
 * @date 2022/7/21 18:11
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_customer")
@ApiModel(value = "客户", description = "客户实体")
public class SysCustomer {
    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("客户id-(长度255)")
    private String id;
    @TableField(value = "customer_name")
    @ApiModelProperty("客户名称-(长度100)")
    private String customerName;

    @TableField(value = "type_id")
    @ApiModelProperty("客户类型-(长度255)")
    private String typeId;

    @TableField(exist = false)
    private String typeName;

    @TableField(value = "number")
    @ApiModelProperty("客户编号-(长度100)")
    private String number;

    @TableField(value = "salesman")
    @ApiModelProperty("业务员-(长度100)")
    private String salesman;

    @TableField(value = "contacts")
    @ApiModelProperty("联系人-(长度100)")
    private String contacts;

    @TableField(value = "phone")
    @ApiModelProperty("联系电话-(长度20)")
    private String phone;

    @TableField(value = "credit")
    @ApiModelProperty("信用额度-(长度20)")
    private String credit;

    @TableField(value = "mail")
    @ApiModelProperty("电子邮箱-(长度50)")
    private String mail;

    @TableField(value = "address")
    @ApiModelProperty("地址-(长度100)")
    private String address;

    @TableField(value = "fax")
    @ApiModelProperty("传真-(长度100)")
    private String fax;

    @TableField(value = "remarks")
    @ApiModelProperty("备注-(长度255)")
    private String remarks;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;
}
