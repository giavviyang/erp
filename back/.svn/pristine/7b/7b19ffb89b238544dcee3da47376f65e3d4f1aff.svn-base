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

/**订单审核表
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-21 13:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "order_review")
@ApiModel(value = "订单审核", description = "订单审核实体类")
public class OrderReview {

    @ApiModelProperty("审核ID")
    @TableId(value = "id" , type = IdType.UUID)
    private String id;

    @ApiModelProperty("订单ID")
    @TableField(value = "order_id")
    private String orderId;

    @ApiModelProperty("尺寸审核结果(0审核通过、1审核未通过、2未审核)")
    @TableField(value = "dimensions_result")
    private Integer dimensionsResult;

    @ApiModelProperty("工艺审核结果(0审核通过、1审核未通过、2未审核)")
    @TableField(value = "workmanship_result")
    private Integer workmanshipResult;

    @ApiModelProperty("订单审核结果(0审核通过、1审核未通过、2未审核)")
    @TableField(value = "order_result")
    private Integer orderResult;

    @ApiModelProperty("审核人")
    @TableField(value = "reviewed_by")
    private String reviewedBy;

    @ApiModelProperty("审核时间")
    @TableField(value = "audit_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    @ApiModelProperty("审核不通过原因")
    @TableField(value = "failure_reason")
    private String failureReason;

}
