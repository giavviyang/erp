package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.erp.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-31 20:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "订单产品数据传输实体类")
public class OrderProductDTO {

    @Excel(name = "产品名称")
    @ApiModelProperty("产品名称")
    @TableField(exist = false)
    private String productName;

    @ApiModelProperty("产品id")
    @TableField(exist = false)
    private String productId;

    @ApiModelProperty("系统产品定义id")
    @TableField(exist = false)
    private String sysProductId;

    @Excel(name = "宽(mm)")
    @ApiModelProperty("宽大头(mm)")
    @TableField(exist = false)
    private Integer wideHead;

//    @Excel(name = "宽小头(mm)")
    @ApiModelProperty("宽小头")
    @TableField(exist = false)
    private Integer wideLittleHead;

    @Excel(name = "高(mm)")
    @ApiModelProperty("高大头")
    @TableField(exist = false)
    private Integer highHead;

//    @Excel(name = "高小头(mm)")
    @ApiModelProperty("高小头")
    @TableField(exist = false)
    private Integer highLittleHead;

//    @Excel(name = "角度")
    @ApiModelProperty("角度")
    @TableField(exist = false)
    private Integer angle;

//    @Excel(name = "直径(mm)")
    @ApiModelProperty("直径")
    @TableField(exist = false)
    private Integer diameter;

//    @Excel(name = "直径增量(mm)")
    @ApiModelProperty("直径增量")
    @TableField(exist = false)
    private Integer diameterIncrement;

//    @Excel(name = "内弧长(mm)")
    @ApiModelProperty("内弧长")
    @TableField(exist = false)
    private Integer innerArcLength;

//    @Excel(name = "弯弧直径(mm)")
    @ApiModelProperty("弯弧直径")
    @TableField(exist = false)
    private Integer arcDiameter;

    @ApiModelProperty("入库数量")
    @TableField(exist = false)
    private Integer warehousingQuantity;

    @ApiModelProperty("仓库位置")
    @TableField(exist = false)
    private String warehouseLocation;

    @ApiModelProperty("货架位置")
    @TableField(exist = false)
    private String shelfLocation;

    @Excel(name = "产品数量")
    @ApiModelProperty("产品数量")
    @TableField(exist = false)
    private Integer num;

    @ApiModelProperty("分架数量")
    @TableField(exist = false)
    private Integer shelfNum;

    @Excel(name = "产品单价(元)")
    @ApiModelProperty("产品单价")
    @TableField(exist = false)
    private Double unitPrice;

    @Excel(name = "位置")
    @ApiModelProperty("位置")
    @TableField(exist = false)
    private String position;

    @Excel(name = "加工要求")
    @ApiModelProperty("加工要求")
    @TableField(exist = false)
    private String requirement;

    @Excel(name = "备注")
    @ApiModelProperty("备注")
    @TableField(exist = false)
    private String remarks;

//    @Excel(name = "产品面积(m²)")
    @ApiModelProperty("产品面积")
    @TableField(exist = false)
    private Double productArea;

//    @Excel(name = "产品重量(kg)")
    @ApiModelProperty("产品重量")
    @TableField(exist = false)
    private Double productWeight;

    @ApiModelProperty("附加项")
    @TableField(exist = false)
    private String additionalItem;

    @ApiModelProperty("排序")
    @TableField(exist = false)
    private Integer sort;

//    @Excel(name = "总金额(元)")
    @ApiModelProperty("总金额")
    @TableField(exist = false)
    private Double productAmount;

//    @Excel(name = "延长米数(m)")
    @ApiModelProperty("延长米数")
    @TableField(exist = false)
    private Double curve;

//    @Excel(name = "总周长(m)")
    @ApiModelProperty("总周长")
    @TableField(exist = false)
    private Double lengthen;

    @ApiModelProperty("单片实际面积")
    @TableField(exist = false)
    private Double singleArea;

    @ApiModelProperty("单片结算面积")
    @TableField(exist = false)
    private Double singleClearingArea;

    @ApiModelProperty("打包数量")
    @TableField(exist = false)
    private Integer orderPackNum;

    @ApiModelProperty("已发货数量")
    @TableField(exist = false)
    private Integer orderDeliverNum;

    @ApiModelProperty("完工数量")
    @TableField(exist = false)
    private Integer completionNum;

    @ApiModelProperty("操作数量")
    @TableField(exist = false)
    private Integer noShelfNum;

}
