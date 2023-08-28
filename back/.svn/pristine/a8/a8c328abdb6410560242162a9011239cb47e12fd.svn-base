package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableField;


/** 分架业务表(流程卡明细表)
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-28 11:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "shelf_division_business")
@ApiModel(value = "流程卡明细", description = "流程卡明细实体类")
public class ShelfDivisionBusiness {

    @ApiModelProperty("主键")
    @TableField(value = "id")
    private String id;

    @ApiModelProperty("产品id")
    @TableField(value = "product_id")
    private String productId;

    @ApiModelProperty("系统半产品id")
    @TableField(value = "semi_product_id")
    private String semiProductId;

    @ApiModelProperty("半产品id")
    @TableField(value = "product_process_id")
    private String productProcessId;


    @Excel(name = "产品名称")
    @ApiModelProperty("产品名称")
    @TableField(value = "product_name")
    private String productName;

    @Excel(name = "位置")
    @ApiModelProperty("位置")
    @TableField(value = "position")
    private String position;

    @Excel(name = "成品面")
    @ApiModelProperty("成品面")
    @TableField(value = "item_surface")
    private String  itemSurface;

    @ApiModelProperty("流程卡id")
    @TableField(value = "flow_card_id")
    private String flowCardId;

    @Excel(name = "流程卡编号")
    @ApiModelProperty("流程卡编号")
    @TableField(value = "flow_card_no")
    private String flowCardNo;

    @Excel(name = "订单编号")
    @ApiModelProperty("订单编号")
    @TableField(value = "order_no")
    private String orderNo;

    @Excel(name = "订单自定义编号")
    @ApiModelProperty("订单自定义编号")
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

    @Excel(name = "单片名称")
    @ApiModelProperty("单片名称")
    @TableField(value = "item_name")
    private String itemName;

    @Excel(name = "单片类型")
    @ApiModelProperty("单片类型")
    @TableField(value = "item_type")
    private String itemType;

    @Excel(name = "单片厚度(mm)")
    @ApiModelProperty("单片厚度")
    @TableField(value = "item_thick")
    private Integer itemThick;

    @ApiModelProperty("工艺流程id")
    @TableField(value = "process_id")
    private String processId;

    @Excel(name = "工艺流程内容")
    @ApiModelProperty("工艺流程内容")
    @TableField(value = "process_content")
    private String processContent;

    @Excel(name = "宽(mm)")
    @ApiModelProperty("宽")
    @TableField(value = "item_w")
    private Integer itemW;

    @Excel(name = "高(mm)")
    @ApiModelProperty("高")
    @TableField(value = "item_h")
    private Integer itemH;

    @Excel(name = "分架数量")
    @ApiModelProperty("分架数量")
    @TableField(value = "item_num")
    private Integer itemNum;

    @Excel(name = "总面积(m²)")
    @ApiModelProperty("总面积")
    @TableField(value = "total_area")
    private Double totalArea;

    @Excel(name = "总重量(kg)")
    @ApiModelProperty("总重量")
    @TableField(value = "total_weight")
    private Double totalWeight;

    @Excel(name = "加工要求")
    @ApiModelProperty("加工要求")
    @TableField(value = "requirement")
    private String requirement;

    @ApiModelProperty("编辑前分架数量")
    @TableField(exist = false)
    private Integer oldNum;

    @ApiModelProperty("备注一")
    @TableField(exist = false)
    private String remarks;

    @ApiModelProperty("备注er")
    @TableField(exist = false)
    private String remarksOne;

    @ApiModelProperty("备注三")
    @TableField(exist = false)
    private String remarksTwo;

    @ApiModelProperty("完工数量")
    @TableField(exist = false)
    private Integer completeNum;

    @ApiModelProperty("报损数量")
    @TableField(exist = false)
    private Integer damageNum;

    @ApiModelProperty("当前工艺")
    @TableField(exist = false)
    private String currentCraft;
}
