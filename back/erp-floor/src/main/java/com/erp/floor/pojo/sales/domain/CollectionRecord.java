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
 * @date： 2022-11-19 14:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "collection_record")
@ApiModel(description = "收款记录实体类")
public class CollectionRecord {


    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(name = "收款id-（长度-40）")
    private String id;

    @TableField(value = "order_id")
    @ApiModelProperty(name = "订单ID-（长度-255）")
    private String orderId;

    @TableField(value = "deliver_id")
    @ApiModelProperty(name = "发货ID-（长度-255）")
    private String deliverId;

    @TableField(value = "status")
    @ApiModelProperty(name = "审核状态-（长度-0）")
    private Integer status;

    @TableField(value = "collection_no")
    @ApiModelProperty(name = "收款编号-（长度-20）")
    private String collectionNo;

    @TableField(value = "collection_date")
    @ApiModelProperty(name = "收款日期-（长度-0）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectionDate;

    @TableField(value = "collection_person")
    @ApiModelProperty(name = "收款人-（长度-255）")
    private String collectionPerson;

    @TableField(value = "collection_customer_id")
    @ApiModelProperty(name = "收款客户ID-（长度-255）")
    private String collectionCustomerId;

    @TableField(value = "collection_customer_name")
    @ApiModelProperty(name = "收款客户名称-（长度-255）")
    private String collectionCustomerName;

    @TableField(value = "collection_mode")
    @ApiModelProperty(name = "收款方式-（长度-255）")
    private String collectionMode;

    @TableField(value = "collection_amount")
    @ApiModelProperty(name = "收款金额-（长度-0）")
    private Double collectionAmount;

    @TableField(value = "preferential_amount")
    @ApiModelProperty(name = "优惠金额-（长度-0）")
    private Double preferentialAmount;

    @TableField(value = "remarks")
    @ApiModelProperty(name = "备注-（长度-255）")
    private String remarks;

    @TableField(value = "allocated_amount")
    @ApiModelProperty(name = "已分配金额-（长度-0）")
    private Double allocatedAmount;

    @TableField(value = "unallocated_amount")
    @ApiModelProperty(name = "未分配金额-（长度-0）")
    private Double unallocatedAmount;

    @TableField(value = "assignment_date")
    @ApiModelProperty(name = "分配日期-（长度-0）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date assignmentDate;

    @TableField(value = "created_person")
    @ApiModelProperty(name = "创建人-（长度-255）")
    private String createdPerson;

    @TableField(value = "created_at")
    @ApiModelProperty(name = "创建日期-（长度-0）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty(name = "修改人-（长度-255）")
    private String updatedPerson;

    @TableField(value = "updated_at")
    @ApiModelProperty(name = "修改日期-（长度-0）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;


}
