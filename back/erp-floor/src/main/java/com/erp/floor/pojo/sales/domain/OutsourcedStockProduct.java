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
 * @date： 2022-09-23 12:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "outsourced_stock_product")
@ApiModel(value = "外协入库产品", description = "外协入库产品实体类")
public class OutsourcedStockProduct {

    @Excel(name = "入库编号")
    @ApiModelProperty("入库编号")
    @TableField(exist = false)
    private String warehousedNo;

    @TableField(value = "id")
    @ApiModelProperty(value = "主键-长度-(40)")
    private String id;

    @TableField(value = "outsourced_products_id")
    @ApiModelProperty(value = "外协产品id-长度-(40)")
    private String outsourcedProductsId;

    @TableField(value = "outsourced_warehousing_id")
    @ApiModelProperty(value = "外协入库id-长度-(40)")
    private String outsourcedWarehousingId;

    @Excel(name = "入库数量")
    @TableField(value = "warehousing_num")
    @ApiModelProperty(value = "入库数量-长度-(0)")
    private Integer warehousingNum;

    /*————————————————————————查询数据——————————————————————————*/
    @Excel(name = "订单编号")
    @ApiModelProperty("订单编号")
    @TableField(exist = false)
    private String orderNo;

    @Excel(name = "自定义编号")
    @ApiModelProperty("自定义编号")
    @TableField(exist = false)
    private String customNo;

    @Excel(name = "项目名称")
    @ApiModelProperty("项目名称")
    @TableField(exist = false)
    private String entryName;

    @Excel(name = "客户名称")
    @ApiModelProperty("客户名称")
    @TableField(exist = false)
    private String customerName;

    @Excel(name = "外协工艺")
    @TableField(exist = false)
    @ApiModelProperty(value = "外协工艺-长度(255)")
    private String outsourcingWorkmanship;

    @Excel(name = "外协加工要求")
    @TableField(exist = false)
    @ApiModelProperty(value = "外协加工要求-长度(255)")
    private String processingRequirements;

    @Excel(name = "产品名称")
    @ApiModelProperty("产品名称")
    @TableField(exist = false)
    private String productName;

    @ApiModelProperty("系统产品定义id")
    @TableField(exist = false)
    private String sysProductId;

    @Excel(name = "宽大头(mm)")
    @ApiModelProperty("宽大头")
    @TableField(exist = false)
    private Integer wideHead;

    @Excel(name = "高大头(mm)")
    @ApiModelProperty("高大头")
    @TableField(exist = false)
    private Integer highHead;

    @Excel(name = "位置")
    @ApiModelProperty("位置")
    @TableField(exist = false)
    private String position;

}
