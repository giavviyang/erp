package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 17:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "shelf_record")
@ApiModel(value = "铁架出入库", description = "铁架出入库实体类")
public class ShelfRecord {


    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "shelf_id")
    @ApiModelProperty(value = "铁架id-长度(40)")
    private String shelfId;

    @TableField(value = "record_type")
    @ApiModelProperty(value = "出入库类型-长度(2)")
    private String recordType;

    @TableField(value = "shipment_no")
    @ApiModelProperty(value = "发货单号-长度(20)")
    private String shipmentNo;

    @TableField(value = "deliver_id")
    @ApiModelProperty(value = "发货id-长度(40)")
    private String deliverId;

    @TableField(value = "receipt_id")
    @ApiModelProperty(value = "回执id-长度(20)")
    private String receiptId;

    @TableField(value = "num")
    @ApiModelProperty(value = "出入库数量-长度(0)")
    private Integer num;

    @TableField(value = "operation_date")
    @ApiModelProperty(value = "出入库日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationDate;

    @TableField(value = "person")
    @ApiModelProperty(value = "负责人-长度(10)")
    private String person;

    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度(255)")
    private String remarks;

    @TableField(exist = false)
    @ApiModelProperty(value = "铁架编号-长度(255)")
    private String frameNo;

    @TableField(exist = false)
    @ApiModelProperty(value = "铁架名称-长度(255)")
    private String frameName;

    @TableField(exist = false)
    @ApiModelProperty(value = "铁架规格-长度(255)")
    private String frameSpecs;

}
