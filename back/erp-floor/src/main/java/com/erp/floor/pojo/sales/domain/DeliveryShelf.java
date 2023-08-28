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

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-30 14:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "delivery_shelf")
@ApiModel(value = "发货货架", description = "发货货架实体类")
public class DeliveryShelf {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "deliver_id")
    @ApiModelProperty(value = "发货id-长度(40)")
    private String deliverId;

    @TableField(value = "frame_id")
    @ApiModelProperty(value = "货架id-长度(40)")
    private String frameId;

    @TableField(value = "frame_num")
    @ApiModelProperty(value = "货架数量-长度(0)")
    private Integer frameNum;

    @TableField(value = "frame_price")
    @ApiModelProperty(value = "货架价格-长度(0)")
    private Integer framePrice;

    @TableField(value = "receipt_num")
    @ApiModelProperty(value = "回执数量-长度(0)")
    private Integer receiptNum;

    @TableField(value = "is_bring")
    @ApiModelProperty(value = "是否带回(0带回，1不带回)-长度(0)")
    private Integer isBring;

    @TableField(exist = false)
    @ApiModelProperty(value = "操作数量")
    private Integer operationNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "货架名称")
    private String frameNo;

    @TableField(exist = false)
    @ApiModelProperty(value = "货架名称")
    private String frameName;

    @TableField(exist = false)
    @ApiModelProperty(value = "货架规格")
    private String frameSpecs;



}
