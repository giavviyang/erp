package com.erp.floor.pojo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @author hanfei
 * @version 1.0
 * @Description 客户类型
 * @date 2022/7/21 14:45
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_customer_type")
@ApiModel(value = "客户类型", description = "客户类型实体")
public class SysCustomerType {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("编号-(长度255)")
    private String id;

    @TableField(value = "type_name")
    @ApiModelProperty(value = "类型名称-(长度100)")
    private String typeName;

    @TableField(value = "tid")
    @ApiModelProperty("父类型-(长度255)")
    private String tid;
    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;
}
