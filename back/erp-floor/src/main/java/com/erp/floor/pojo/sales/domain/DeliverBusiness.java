package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @date： 2022-08-29 12:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "deliver_business")
@ApiModel(value = "发货明细", description = "发货明细实体类")
public class DeliverBusiness extends OrderProductDTO{

    @ApiModelProperty(value = "id-长度(255)")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "发货id-长度(40)")
    @TableField(value = "deliver_id")
    private String deliverId;

    @Excel(name = "发货编号")
    @ApiModelProperty(value = "发货编号-长度(40)")
    @TableField(value = "deliver_no")
    private String deliverNo;

    @ApiModelProperty(value = "流程卡id-长度(255)")
    @TableField(value = "flow_card_id")
    private String flowCardId;

    @ApiModelProperty(value = "订单id-长度(40)")
    @TableField(value = "order_id")
    private String orderId;

    @ApiModelProperty(value = "打包id-长度(40)")
    @TableField(value = "pack_id")
    private String packId;

    @ApiModelProperty(value = "产品id-长度(40)")
    @TableField(value = "product_id")
    private String productId;

    @Excel(name = "发货面积(m²)")
    @ApiModelProperty(value = "发货面积-长度(0)")
    @TableField(value = "deliver_area")
    private Double deliverArea;

    @Excel(name = "发货重量(kg)")
    @ApiModelProperty(value = "发货重量-长度(0)")
    @TableField(value = "deliver_weight")
    private Double deliverWeight;

    @Excel(name = "发货产品金额(元)")
    @ApiModelProperty(value = "发货产品金额-长度(0)")
    @TableField(value = "deliver_amount")
    private Double deliverAmount;

    @Excel(name = "发货数量")
    @ApiModelProperty(value = "发货数量-长度(0)")
    @TableField(value = "deliver_num")
    private Integer deliverNum;

    @Excel(name = "发货单价(元)")
    @ApiModelProperty(value = "发货单价-长度(0)")
    @TableField(value = "deliver_price")
    private Integer deliverPrice;

    @Excel(name = "退货数量")
    @ApiModelProperty(value = "退货数量-长度(0)")
    @TableField(value = "deliver_return_num")
    private Integer deliverReturnNum;



    @ApiModelProperty(value = "打包编号-长度(40)")
    @TableField(exist = false)
    private String packNo;

    @ApiModelProperty(value = "订单编号-长度(40)")
    @TableField(exist = false)
    private String orderNo;

    @ApiModelProperty(value = "流程卡编号-长度(20)")
    @TableField(exist = false)
    private String flowCardNo;

    @ApiModelProperty(value = "自定义编号-长度(255)")
    @TableField(exist = false)
    private String customNo;

    @ApiModelProperty(value = "项目名称-长度(40)")
    @TableField(exist = false)
    private String entryName;

    @ApiModelProperty(value = "客户名称-长度(40)")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty(value = "打包数量")
    @TableField(exist = false)
    private String packNum;

    @ApiModelProperty(value = "打包数量")
    @TableField(exist = false)
    private String packDeliverNum;

    @ApiModelProperty(value = "发货负责人")
    @TableField(exist = false)
    private String deliverPeople;

    @ApiModelProperty(value = "发货日期")
    @TableField(exist = false)
    private String deliverDate;



}
