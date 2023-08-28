package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**附加项记录表
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-22 17:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "additional")
@ApiModel(value = "附加项", description = "附加项记录实体类")
public class Additional {

    @ApiModelProperty("附加项ID")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty("订单产品ID")
    @TableField(value = "product_id")
    private String productId;

    @ApiModelProperty("附加项名称")
    @TableField(value = "additional_name")
    private String additionalName;

    @ApiModelProperty("附加项别名")
    @TableField(value = "additional_alias")
    private String additionalAlias;

    @ApiModelProperty("边长类型")
    @TableField(value = "additional_side")
    private String additionalSide;

    @ApiModelProperty("执行单价")
    @TableField(value = "unit_price")
    private Double unitPrice;

    @ApiModelProperty("附加项总金额")
    @TableField(value = "all_amount")
    private Double allAmount;

    @ApiModelProperty("单片数量")
    @TableField(value = "one_number")
    private Integer oneNumber;

}
