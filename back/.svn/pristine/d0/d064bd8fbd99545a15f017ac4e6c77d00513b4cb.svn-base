package com.erp.floor.pojo.produce.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.erp.floor.pojo.sales.domain.ShelfDivisionBusiness;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/6 10:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "damage_flow_card")
@ApiModel(value = "报损流程卡", description = "报损流程卡实体")
public class DamageFlowCard {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("报损流程卡ID-(长度255)")
    private String id;

    @TableField(value = "damage_id")
    @ApiModelProperty("报损ID-(长度255)")
    private String damageId;

    @TableField(value = "flow_card_no")
    @ApiModelProperty("流程卡编号-(长度255)")
    private String flowCardNo;

    @TableField(value = "product_id")
    @ApiModelProperty("产品ID-(长度255)")
    private String productId;

    @TableField(value = "semi_product_id")
    @ApiModelProperty("半产品ID-(长度255)")
    private String semiProductId;

    @TableField(value = "damage_area")
    @ApiModelProperty("报损面积-(长度255)")
    private int damageArea;
    @TableField(value = "damage_num")
    @ApiModelProperty("报损数量-(长度255)")
    private int damageNum;

    @TableField(value = "responsible_process")
    @ApiModelProperty("责任工序-(长度255)")
    private String responsibleProcess;

    @TableField(value = "responsible_team")
    @ApiModelProperty("责任班组-(长度255)")
    private String responsibleTeam;

    @TableField(value = "responsible_person")
    @ApiModelProperty("责任人-(长度255)")
    private String responsiblePerson;

    @TableField(value = "damage_reason")
    @ApiModelProperty("报损原因-(长度255)")
    private String damageReason;

    @TableField(value = "remarks")
    @ApiModelProperty("备注-(长度255)")
    private String remarks;

    @TableField(exist = false)
    @ApiModelProperty("流程卡信息")
    private FlowCard flowCard;

    @TableField(exist = false)
    @ApiModelProperty("流程卡半产品信息")
    private ShelfDivisionBusiness business;
}
