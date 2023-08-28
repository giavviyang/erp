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

/**产品单片工艺对应表
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-27 15:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "product_process")
@ApiModel(value = "产品单品对应工艺", description = "产品单品对应工艺实体类")
public class ProductProcess {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty("订单编号")
    @TableField(value = "order_no")
    private String orderNo;

    @ApiModelProperty("产品id")
    @TableField(value = "product_id")
    private String productId;

    @ApiModelProperty("半产品id")
    @TableField(value = "semi_product_id")
    private String semiProductId;

    @ApiModelProperty("产品名称")
    @TableField(value = "product_name")
    private String productName;

    @ApiModelProperty("位置")
    @TableField(value = "position")
    private String position;

    @ApiModelProperty("成品面")
    @TableField(value = "item_surface")
    private String itemSurface;

    @ApiModelProperty("工艺流程id")
    @TableField(value = "process_id")
    private String processId;

    @ApiModelProperty("工艺流程内容")
    @TableField(value = "process_content")
    private String processContent;

    @ApiModelProperty("单片名称")
    @TableField(value = "item_name")
    private String itemName;

    @ApiModelProperty("单片类型")
    @TableField(value = "item_type")
    private String itemType;

    @ApiModelProperty("单片厚度")
    @TableField(value = "item_thick")
    private Integer itemThick;

    @ApiModelProperty("单片宽")
    @TableField(value = "item_wide")
    private Integer itemWide;

    @ApiModelProperty("单片高")
    @TableField(value = "item_high")
    private Integer itemHigh;

    @ApiModelProperty("单片面积")
    @TableField(value = "item_area")
    private Double itemArea;

    @ApiModelProperty("单片重量")
    @TableField(value = "item_weight")
    private Double itemWeight;

    @ApiModelProperty("产品数量")
    @TableField(value = "num")
    private Integer num;

    @ApiModelProperty("完工数量")
    @TableField(value = "completion_num")
    private Integer completionNum;



    /*----------------------------产品信息-------------------*/

    @ApiModelProperty("自定义编号")
    @TableField(exist = false)
    private String customNo;

    @ApiModelProperty("项目名称")
    @TableField(exist = false)
    private String entryName;

    @ApiModelProperty("客户名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty("加工要求")
    @TableField(exist = false)
    private String requirement;

    @ApiModelProperty("备注")
    @TableField(exist = false)
    private String remarks;

    @ApiModelProperty("未分架数量(剩余数量)")
    @TableField(exist = false)
    private Integer noShelfNum;

    @ApiModelProperty("分架数量")
    @TableField(exist = false)
    private Integer shelfNum;

    @ApiModelProperty("已分架数量")
    @TableField(exist = false)
    private Integer yesShelfNum;

}
