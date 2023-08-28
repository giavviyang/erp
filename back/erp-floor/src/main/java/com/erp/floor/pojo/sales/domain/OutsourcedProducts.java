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
 * @date： 2022-09-23 10:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "outsourced_products")
@ApiModel(value = "外协产品", description = "外协产品实体类")
public class OutsourcedProducts extends OrderProductDTO{

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "product_id")
    @ApiModelProperty(value = "产品id-长度(40)")
    private String productId;

    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单id-长度(40)")
    private String orderId;

    @Excel(name = "订单编号")
    @ApiModelProperty("订单编号")
    @TableField(value = "order_no")
    private String orderNo;

    @Excel(name = "自定义编号")
    @ApiModelProperty("自定义编号")
    @TableField(value = "custom_no")
    private String customNo;

    @Excel(name = "项目名称")
    @ApiModelProperty("项目名称")
    @TableField(value = "entry_name")
    private String entryName;

    @Excel(name = "客户名称")
    @ApiModelProperty("客户名称")
    @TableField(value = "customer_name")
    private String customerName;

    @Excel(name = "外协工艺")
    @TableField(value = "outsourcing_workmanship")
    @ApiModelProperty(value = "外协工艺-长度(255)")
    private String outsourcingWorkmanship;

    @Excel(name = "外协数量")
    @TableField(value = "outsourcing_num")
    @ApiModelProperty(value = "外协数量-长度(0)")
    private Integer outsourcingNum;

    @Excel(name = "入库数量")
    @TableField(value = "warehouse_num")
    @ApiModelProperty(value = "入库数量-长度(0)")
    private Integer warehouseNum;

    @Excel(name = "外协玻璃单价(元)")
    @TableField(value = "outsourcing_price")
    @ApiModelProperty(value = "外协玻璃单价-长度(0)")
    private Double outsourcingPrice;

    @TableField(value = "outsourcing_id")
    @ApiModelProperty(value = "外协id-长度(40)")
    private String outsourcingId;

    @Excel(name = "外协编号")
    @TableField(value = "outsourcing_no")
    @ApiModelProperty(value = "外协编号-长度(20)")
    private String outsourcingNo;

    @Excel(name = "加工要求")
    @TableField(value = "processing_requirements")
    @ApiModelProperty(value = "加工要求-长度(255)")
    private String processingRequirements;


    @ApiModelProperty("外协数量")
    @TableField(exist = false)
    private Integer outsourcedNum;
}
