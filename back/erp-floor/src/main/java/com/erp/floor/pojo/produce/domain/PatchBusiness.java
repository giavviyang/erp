package com.erp.floor.pojo.produce.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.ShelfDivisionBusiness;
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
 * @date 2022/9/15 14:46
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "patch_business")
@ApiModel(value = "补片业务记录", description = "补片业务记录实体")
public class PatchBusiness {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("补片业务记录ID-(长度255)")
    private String id;

    @TableField(value = "patch_id")
    @ApiModelProperty("补片记录id-(长度255)")
    private String patchId;

    @TableField(value = "damage_id")
    @ApiModelProperty("报损id-(长度255)")
    private String damageId;

    @TableField(value = "damage_business_id")
    @ApiModelProperty("报损业务id-(长度255)")
    private String damageBusinessId;

    @TableField(value = "flow_card_no")
    @ApiModelProperty("流程卡号-(长度255)")
    private String flowCardNo;

    @TableField(value = "product_id")
    @ApiModelProperty("产品id-(长度255)")
    private String productId;

    @TableField(value = "semi_product_id")
    @ApiModelProperty("半产品id-(长度255)")
    private String semiProductId;

    @TableField(value = "patch_num")
    @ApiModelProperty("补片数量-(长度255)")
    private int patchNum;

    @TableField(value = "patch_area")
    @ApiModelProperty("补片面积-(长度255)")
    private int patchArea;

    @TableField(value = "patch_flow_card_no")
    @ApiModelProperty("补片流程卡编号-(长度255)")
    private String patchFlowCardNo;

    @TableField(value = "patch_flow_card_id")
    @ApiModelProperty("补片流程卡id-(长度255)")
    private String patchFlowCardId;

    @ApiModelProperty("流程卡信息")
    @TableField(exist = false)
    private FlowCard flowCardInfo;

    @ApiModelProperty("流程卡明细信息")
    @TableField(exist = false)
    private ShelfDivisionBusiness businessInfo;

    @ApiModelProperty("流程卡明细信息")
    @TableField(exist = false)
    private DamageFlowCard damageFlowCardInfo;
}
