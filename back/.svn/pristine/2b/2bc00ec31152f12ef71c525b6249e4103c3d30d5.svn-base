package com.erp.floor.pojo.produce.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.ShelfDivisionBusiness;
import com.erp.floor.pojo.system.domain.SysProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/29 13:38
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "completion_business")
@ApiModel(value = "完工业务记录", description = "完工业务记录实体")
public class CompletionBusiness {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("完工业务记录ID-(长度255)")
    private String id;
    @TableField(value = "record_id")
    @ApiModelProperty("完工单id-(长度255)")
    private String recordId;
    @TableField(value = "product_id")
    @ApiModelProperty("产品id-(长度255)")
    private String productId;

    @TableField(value = "flow_card_no")
    @ApiModelProperty("流程卡id-(长度255)")
    private String flowCardNo;

    @TableField(exist = false)
    @ApiModelProperty("半流程卡详情")
    private ShelfDivisionBusiness flowCardInfo;

    @TableField(exist = false)
    @ApiModelProperty("半流程卡详情")
    private FlowCard flowCard;

    @TableField(value = "num")
    @ApiModelProperty("对应数量")
    private Integer num;

    @TableField(value = "status")
    @ApiModelProperty("状态")
    private Integer status;

    @TableField(value = "craft")
    @ApiModelProperty("工艺")
    private String craft;

    @TableField(value = "last_craft")
    @ApiModelProperty("是否最后工艺")
    private int lastCraft;

    @TableField(value = "item_surface")
    @ApiModelProperty("成品面")
    private String itemSurface;

    @TableField(value = "semi_product_id")
    @ApiModelProperty("半产品ID")
    private String semiProductId;
}
