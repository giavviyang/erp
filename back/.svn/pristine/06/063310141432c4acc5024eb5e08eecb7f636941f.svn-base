package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-08 17:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "deliver_receipt")
@ApiModel(value = "回执信息", description = "回执信息实体类")
public class DeliverReceipt {

    @TableField(value = "id")
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "deliver_id")
    @ApiModelProperty(value = "发货id-长度(40)")
    private String deliverId;

    @TableField(value = "receipt_no")
    @ApiModelProperty(value = "回执编号-长度(10)")
    private String receiptNo;

    @TableField(value = "receipt_person")
    @ApiModelProperty(value = "回执负责人-长度(10)")
    private String receiptPerson;

    @TableField(value = "receipt_date")
    @ApiModelProperty(value = "回执日期-长度(0)")
    private String receiptDate;

    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度(255)")
    private String remarks;

    @TableField(exist = false)
    @ApiModelProperty(value = "回执货架")
    private List<DeliveryShelf> deliveryShelfList;

    @TableField(exist = false)
    @ApiModelProperty(value = "回执编号")
    private String deliverNo;



}
