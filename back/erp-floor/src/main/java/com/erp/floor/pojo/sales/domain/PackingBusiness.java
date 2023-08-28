package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import com.erp.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-25 17:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "packing_business")
@ApiModel(value = "打包明细", description = "打包明细实体类")
public class PackingBusiness {

    @ApiModelProperty(value = "主键-长度(40)")
    @TableId(value = "id" , type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "订单id-长度(40)")
    @TableField(value = "order_id")
    private String orderId;

    @Excel(name = "订单编号")
    @ApiModelProperty(value = "订单编号-长度(40)")
    @TableField(value = "order_no")
    private String orderNo;

    @Excel(name = "自定义编号")
    @TableField(value = "custom_no")
    @ApiModelProperty(value = "自定义编号-长度(255)")
    private String customNo;

    @ApiModelProperty(value = "打包id-长度(40)")
    @TableField(value = "pack_id")
    private String packId;

    @Excel(name = "打包编号")
    @ApiModelProperty(value = "打包编号-长度(40)")
    @TableField(value = "pack_no")
    private String packNo;

    @Excel(name = "项目名称")
    @ApiModelProperty(value = "项目名称-长度(40)")
    @TableField(value = "entry_name")
    private String entryName;

    @Excel(name = "客户名称")
    @TableField(value = "customer_name")
    @ApiModelProperty(value = "客户名称-长度(40)")
    private String customerName;

    @ApiModelProperty(value = "产品id-长度(40)")
    @TableField(value = "product_id")
    private String productId;

    @Excel(name = "产品名称")
    @ApiModelProperty(value = "产品名称-长度(255)")
    @TableField(value = "product_name")
    private String productName;

    @Excel(name = "宽(mm)")
    @ApiModelProperty(value = "宽大头-长度(0)")
    @TableField(value = "wide_head")
    private Integer wideHead;

    @ApiModelProperty(value = "宽小头-长度(0)")
    @TableField(value = "wide_little_head")
    private Integer wideLittleHead;

    @Excel(name = "高(mm)")
    @ApiModelProperty(value = "高大头-长度(0)")
    @TableField(value = "high_head")
    private Integer highHead;

    @ApiModelProperty(value = "高小头-长度(0)")
    @TableField(value = "high_little_head")
    private Integer highLittleHead;

    @ApiModelProperty(value = "角度-长度(0)")
    @TableField(value = "angle")
    private Integer angle;

    @ApiModelProperty(value = "直径-长度(0)")
    @TableField(value = "diameter")
    private Integer diameter;

    @ApiModelProperty(value = "直径增量-长度(0)")
    @TableField(value = "diameter_increment")
    private Integer diameterIncrement;

    @ApiModelProperty(value = "内弧长-长度(0)")
    @TableField(value = "inner_arc_length")
    private Integer innerArcLength;

    @ApiModelProperty(value = "弯弧直径-长度(0)")
    @TableField(value = "arc_diameter")
    private Integer arcDiameter;

    @Excel(name = "产品数量")
    @ApiModelProperty(value = "产品数量-长度(0)")
    @TableField(value = "num")
    private Integer num;

    @Excel(name = "单价(元)")
    @ApiModelProperty(value = "单价-长度(0)")
    @TableField(value = "unit_price")
    private Double unitPrice;

    @Excel(name = "位置")
    @ApiModelProperty(value = "位置-长度(255)")
    @TableField(value = "position")
    private String position;

    @Excel(name = "加工要求")
    @ApiModelProperty(value = "加工要求-长度(255)")
    @TableField(value = "requirement")
    private String requirement;

    @Excel(name = "打包面积(m²)")
    @ApiModelProperty(value = "打包面积-长度(0)")
    @TableField(value = "product_area")
    private Double productArea;

    @Excel(name = "打包重量(kg)")
    @ApiModelProperty(value = "打包重量-长度(0)")
    @TableField(value = "product_weight")
    private Double productWeight;

    @ApiModelProperty(value = "产品总金额-长度(0)")
    @TableField(value = "product_amount")
    private Double productAmount;

    @Excel(name = "打包数量")
    @ApiModelProperty(value = "打包数量-长度(0)")
    @TableField(value = "pack_num")
    private Integer packNum;

    @ApiModelProperty(value = "发货数量-长度(0)")
    @TableField(value = "pack_deliver_num")
    private Integer packDeliverNum;

    @ApiModelProperty(value = "未打包数量")
    @TableField(exist = false)
    private Integer notPackNum;

    @ApiModelProperty(value = "编辑后打包数量")
    @TableField(exist = false)
    private Integer updatePackNum;

    @ApiModelProperty(value = "打包名称")
    @TableField(exist = false)
    private String packName;

    @ApiModelProperty(value = "可发货数量")
    @TableField(exist = false)
    private Integer noShelfNum;

    @ApiModelProperty(value = "产品已发货数量")
    @TableField(exist = false)
    private Integer orderDeliverNum;

    @ApiModelProperty(value = "产品完工数量")
    @TableField(exist = false)
    private Integer completionNum;





}
