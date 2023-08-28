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
 * @date 2022/7/22 16:03
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_product_type")
@ApiModel(value = "产品类型", description = "产品类型实体")
public class SysProductType {
    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("产品类型ID-(长度255)")
    private String id;

    @TableField(value = "type_name")
    @ApiModelProperty("类型名称-(长度255)")
    private String typeName;

    @TableField(value = "tid")
    @ApiModelProperty("父类型-(长度255)")
    private String tid;

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
