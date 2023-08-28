package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**订单产品表
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-21 12:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "order_product")
@ApiModel(value = "订单产品", description = "订单产品实体类")
public class OrderProduct {

    @ApiModelProperty("产品id")
    @TableField(value = "id")
    private String id;

    @ApiModelProperty("订单id")
    @TableField(value = "order_id")
    private String orderId;

    @Excel(name = "订单编号")
    @ApiModelProperty("订单编号")
    @TableField(value = "order_no")
    private String orderNo;

    @Excel(name = "产品名称")
    @ApiModelProperty("产品名称")
    @TableField(value = "product_name")
    private String productName;

    @ApiModelProperty("打印名称")
    @TableField(value = "print_name")
    private String printName;

    @ApiModelProperty("系统产品定义id")
    @TableField(value = "product_id")
    private String productId;

    @Excel(name = "宽(mm)")
    @ApiModelProperty("宽大头(mm)")
    @TableField(value = "wide_head")
    private Integer wideHead;

//    @Excel(name = "宽小头(mm)")
    @ApiModelProperty("宽小头")
    @TableField(value = "wide_little_head")
    private Integer wideLittleHead;

    @Excel(name = "高(mm)")
    @ApiModelProperty("高大头")
    @TableField(value = "high_head")
    private Integer highHead;

//    @Excel(name = "高小头(mm)")
    @ApiModelProperty("高小头")
    @TableField(value = "high_little_head")
    private Integer highLittleHead;

//    @Excel(name = "角度")
    @ApiModelProperty("角度")
    @TableField(value = "angle")
    private Integer angle;

//    @Excel(name = "直径(mm)")
    @ApiModelProperty("直径")
    @TableField(value = "diameter")
    private Integer diameter;

//    @Excel(name = "直径增量(mm)")
    @ApiModelProperty("直径增量")
    @TableField(value = "diameter_increment")
    private Integer diameterIncrement;

//    @Excel(name = "内弧长(mm)")
    @ApiModelProperty("内弧长")
    @TableField(value = "inner_arc_length")
    private Integer innerArcLength;

//    @Excel(name = "弯弧直径(mm)")
    @ApiModelProperty("弯弧直径")
    @TableField(value = "arc_diameter")
    private Integer arcDiameter;

    @Excel(name = "入库数量")
    @ApiModelProperty("入库数量")
    @TableField(value = "warehousing_quantity")
    private Integer warehousingQuantity;

    @Excel(name = "仓库位置")
    @ApiModelProperty("仓库位置")
    @TableField(value = "warehouse_location")
    private String warehouseLocation;

    @Excel(name = "货架位置")
    @ApiModelProperty("货架位置")
    @TableField(value = "shelf_location")
    private String shelfLocation;

    @Excel(name = "数量")
    @ApiModelProperty("数量")
    @TableField(value = "num")
    private Integer num;

    @ApiModelProperty("分架数量")
    @TableField(value = "shelf_num")
    private Integer shelfNum;

    @Excel(name = "单价(元)")
    @ApiModelProperty("单价")
    @TableField(value = "unit_price")
    private Double unitPrice;

    @Excel(name = "位置")
    @ApiModelProperty("位置")
    @TableField(value = "position")
    private String position;

    @Excel(name = "加工要求")
    @ApiModelProperty("加工要求")
    @TableField(value = "requirement")
    private String requirement;

    @Excel(name = "备注一")
    @ApiModelProperty("备注一")
    @TableField(value = "remarks")
    private String remarks;

    @Excel(name = "备注二")
    @ApiModelProperty("备注二")
    @TableField(value = "remarks_one")
    private String remarksOne;

    @Excel(name = "备注三")
    @ApiModelProperty("备注三")
    @TableField(value = "remarks_two")
    private String remarksTwo;

    @Excel(name = "产品面积(m²)")
    @ApiModelProperty("产品面积")
    @TableField(value = "product_area")
    private Double productArea;

    @Excel(name = "产品重量(kg)")
    @ApiModelProperty("产品重量")
    @TableField(value = "product_weight")
    private Double productWeight;

    @ApiModelProperty("附加项")
    @TableField(value = "additional_item")
    private String additionalItem;

    @ApiModelProperty("流程卡ID")
    @TableField(value = "flow_card_id")
    private String flowCardId;

    @ApiModelProperty("打包ID")
    @TableField(value = "packing_id")
    private String packingId;

    @ApiModelProperty("发货ID")
    @TableField(value = "deliver_id")
    private String deliverId;

    @ApiModelProperty("排序")
    @TableField(value = "sort")
    private Integer sort;

    @Excel(name = "总金额(元)")
    @ApiModelProperty("总金额")
    @TableField(value = "product_amount")
    private Double productAmount;

    @Excel(name = "延长米数(m)")
    @ApiModelProperty("延长米数")
    @TableField(value = "curve")
    private Double curve;

    @Excel(name = "总周长(m)")
    @ApiModelProperty("总周长")
    @TableField(value = "lengthen")
    private Double lengthen;

    @Excel(name = "单片实际面积(m²)")
    @ApiModelProperty("单片实际面积")
    @TableField(value = "single_area")
    private Double singleArea;

    @ApiModelProperty("单片结算面积")
    @TableField(value = "single_clearing_area")
    private Double singleClearingArea;

    @ApiModelProperty("打包数量")
    @TableField(value = "order_pack_num")
    private Integer orderPackNum;

    @ApiModelProperty("已发货数量")
    @TableField(value = "order_deliver_num")
    private Integer orderDeliverNum;

    @ApiModelProperty("完工数量")
    @TableField(value = "completion_num")
    private Integer completionNum;

    @ApiModelProperty("外协数量")
    @TableField(value = "outsourced_num")
    private Integer outsourcedNum;


    /*---------------------订单信息----------------------*/
    @ApiModelProperty("分架数量")
    @TableField(exist = false)
    private Integer noShelfNum;

    @ApiModelProperty("自定义编号")
    @TableField(exist = false)
    private String customNo;

    @ApiModelProperty("项目名称")
    @TableField(exist = false)
    private String entryName;

    @ApiModelProperty("客户名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty("附加项集合")
    @TableField(exist = false)
    private Map<String ,Object> additionalMap;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private Integer pageNum;

}
