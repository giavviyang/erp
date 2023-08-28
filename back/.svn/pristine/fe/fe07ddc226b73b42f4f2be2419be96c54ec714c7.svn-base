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
 * @date 2022/7/22 16:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_semi_product")
@ApiModel(value = "半产品", description = "半产品实体")
public class SysSemiProduct {
    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("半产品ID-(长度255)")
    private String id;

    @TableField(value = "semi_product_name")
    @ApiModelProperty("半产品名称-(长度255)")
    private String semiProductName;

    @TableField(value = "attribute")
    @ApiModelProperty("半产品属性-(长度255)")
    private String attribute;

    @TableField(value = "thick")
    @ApiModelProperty("厚度-(长度255)")
    private String thick;

    @TableField(value = "craft_flow_id")
    @ApiModelProperty("工艺流程id-(长度255)")
    private String craftFlowId;

    @TableField(value = "craft_flow")
    @ApiModelProperty("工艺流程-(长度255)")
    private String craftFlow;

    @TableField(value = "sort")
    @ApiModelProperty("顺序-(长度255)")
    private String sort;

    @TableField(value = "product_id")
    @ApiModelProperty("产品ID-(长度255)")
    private String productId;

    @TableField(value = "type")
    @ApiModelProperty("半产品种类-(长度255)")
    private String type;
}
