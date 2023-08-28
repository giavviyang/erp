package com.erp.floor.pojo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/22 16:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_product")
@ApiModel(value = "产品", description = "产品实体")
public class SysProduct {
    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("产品ID-(长度255)")
    private String id;

    @TableField(value = "product_name")
    @ApiModelProperty("产品名称-(长度255)")
    private String productName;

    @TableField(value = "full_name")
    @ApiModelProperty("产品名称全称-(长度255)")
    private String fullName;

    @TableField(value = "thick")
    @ApiModelProperty("产品厚度-(长度255)")
    private String thick;

    @TableField(value = "type")
    @ApiModelProperty("产品类型-(长度255)")
    private String type;

    @TableField(value = "type_id")
    @ApiModelProperty("产品类型id-(长度255)")
    private String typeId;

    @TableField(value = "no")
    @ApiModelProperty("产品编号-(长度255)")
    private String no;

    @TableField(value = "semi_num")
    @ApiModelProperty("玻璃层数-(长度255)")
    private String semiNum;

    @TableField(value = "price")
    @ApiModelProperty("产品价格-(长度255)")
    private String price;

    @TableField(exist = false)
    private List<SysSemiProduct> semiProduct;
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

