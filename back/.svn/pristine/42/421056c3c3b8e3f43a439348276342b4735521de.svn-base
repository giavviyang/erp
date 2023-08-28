package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-05 18:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "return_business")
@ApiModel(value = "退货明细", description = "退货明细实体类")
public class ReturnBusiness extends OrderProductDTO{

    @TableField(value = "id")
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "return_id")
    @ApiModelProperty(value = "退货id-长度(40)")
    private String returnId;

    @Excel(name = "退货编号")
    @TableField(value = "return_no")
    @ApiModelProperty(value = "退货编号-长度(40)")
    private String returnNo;

    @TableField(value = "deliver_business_id")
    @ApiModelProperty(value = "发货明细id-长度(40)")
    private String deliverBusinessId;

    @TableField(value = "product_id")
    @ApiModelProperty(value = "产品id-长度(40)")
    private String productId;

    @Excel(name = "退货数量")
    @TableField(value = "return_num")
    @ApiModelProperty(value = "退货数量-长度(0)")
    private Integer returnNum;

    @Excel(name = "退货金额(元)")
    @TableField(value = "return_money")
    @ApiModelProperty(value = "退货金额-长度(0)")
    private Double returnMoney;

    @Excel(name = "退货面积(m²)")
    @TableField(value = "return_area")
    @ApiModelProperty(value = "退货面积-长度(0)")
    private Double returnArea;

    @Excel(name = "退货重量(kg)")
    @TableField(value = "return_weight")
    @ApiModelProperty(value = "退货重量-长度(0)")
    private Double returnWeight;

    /*——————————————————————————查看明细字段————————————————————————————*/

    @Excel(name = "发货编号")
    @TableField(exist = false)
    @ApiModelProperty(value = "发货编号")
    private String deliverNo;

    @TableField(exist = false)
    @ApiModelProperty(value = "发货数量")
    private Integer deliverNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "发货金额")
    private Double deliverAmount;

    @Excel(name = "发货单价(元)")
    @TableField(exist = false)
    @ApiModelProperty(value = "发货单价")
    private Integer deliverPrice;

    @TableField(exist = false)
    @ApiModelProperty(value = "发货已退货数量")
    private Integer deliverReturnNum;

    @Excel(name = "订单编号")
    @ApiModelProperty(value = "订单编号-长度(40)")
    @TableField(exist = false)
    private String orderNo;

    @Excel(name = "自定义编号")
    @ApiModelProperty(value = "自定义编号-长度(255)")
    @TableField(exist = false)
    private String customNo;

    @Excel(name = "项目名称")
    @ApiModelProperty(value = "项目名称-长度(40)")
    @TableField(exist = false)
    private String entryName;

    @Excel(name = "客户名称")
    @ApiModelProperty(value = "客户名称-长度(40)")
    @TableField(exist = false)
    private String customerName;



}
