package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-23 09:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "outsourced")
@ApiModel(value = "外协记录", description = "外协记录实体类")
public class Outsourced {

    @TableField(value = "id")
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @Excel(name = "外协编号")
    @TableField(value = "outsourced_no")
    @ApiModelProperty(value = "外协编号-长度(20)")
    private String outsourcedNo;

    @Excel(name = "审核状态")
    @TableField(value = "audit_status")
    @ApiModelProperty(value = "审核状态-长度(10)")
    private String auditStatus;

    @Excel(name = "入库状态")
    @TableField(value = "warehousing_status")
    @ApiModelProperty(value = "入库状态-长度(10)")
    private String warehousingStatus;

    @Excel(name = "付款状态")
    @TableField(value = "payment_status")
    @ApiModelProperty(value = "付款状态-长度(10)")
    private String paymentStatus;

    @Excel(name = "订单编号")
    @TableField(value = "order_no")
    @ApiModelProperty(value = "订单编号-长度(255)")
    private String orderNo;

    @Excel(name = "自定义编号")
    @TableField(value = "custom_no")
    @ApiModelProperty(value = "自定义编号-长度(255)")
    private String customNo;

    @Excel(name = "客户名称")
    @TableField(value = "customer_name")
    @ApiModelProperty(value = "客户名称-长度(255)")
    private String customerName;

    @Excel(name = "外协负责人")
    @TableField(value = "outsourcing_leader")
    @ApiModelProperty(value = "外协负责人-长度(10)")
    private String outsourcingLeader;

    @Excel(name = "外协单位")
    @TableField(value = "outsourcing_unit")
    @ApiModelProperty(value = "外协单位-长度(20)")
    private String outsourcingUnit;

    @Excel(name = "外协联系人")
    @TableField(value = "outsourcing_contact")
    @ApiModelProperty(value = "外协联系人-长度(10)")
    private String outsourcingContact;

    @Excel(name = "联系人电话")
    @TableField(value = "contact_phone")
    @ApiModelProperty(value = "联系人电话-长度(20)")
    private String contactPhone;

    @Excel(name = "外协日期", width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "outgoing_date")
    @ApiModelProperty(value = "外协日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outgoingDate;

    @Excel(name = "提货日期", width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "pick_up_date")
    @ApiModelProperty(value = "提货日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pickUpDate;

    @Excel(name = "外协数量")
    @TableField(value = "outsourcing_num")
    @ApiModelProperty(value = "外协数量-长度(0)")
    private Integer outsourcingNum;

    @Excel(name = "入库数量")
    @TableField(value = "warehousing_num")
    @ApiModelProperty(value = "入库数量-长度(0)")
    private Integer warehousingNum;

    @Excel(name = "结款方式")
    @TableField(value = "payment_method")
    @ApiModelProperty(value = "结款方式-长度(20)")
    private String paymentMethod;

    @Excel(name = "其他费用(元)")
    @TableField(value = "other_cost")
    @ApiModelProperty(value = "其他费用-长度(0)")
    private Double otherCost;

    @Excel(name = "外协总金额(元)")
    @TableField(value = "outsourcing_amount")
    @ApiModelProperty(value = "外协总金额-长度(0)")
    private Double outsourcingAmount;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度(255)")
    private String remarks;

    @ApiModelProperty(value = "创建人-长度(10)")
    @TableField(value = "created_person")
    private String createdPerson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间-长度(0)")
    @TableField(value = "created_at")
    private Date createdAt;

    @ApiModelProperty(value = "修改人-长度(10)")
    @TableField(value = "updated_person")
    private String updatedPerson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间-长度(0)")
    @TableField(value = "updated_at")
    private Date updatedAt;

    @Excel(name = "审核人")
    @ApiModelProperty(value = "审核人-长度(10)")
    @TableField(value = "review_people")
    private String reviewPeople;

    @Excel(name = "审核原因")
    @ApiModelProperty(value = "审核原因-长度(255)")
    @TableField(value = "review_reason")
    private String reviewReason;


    /*———————————————————————————新增信息—————————————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "产品集合")
    private List<OrderProduct> orderProductList;

    /*———————————————————————————查询信息—————————————————————————————*/
    @ApiModelProperty("外协日期 - 开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private String outgoingDateStart;

    @ApiModelProperty("外协日期 - 结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private String outgoingDateEnd;

    @ApiModelProperty("页码")
    @TableField(exist = false)
    private Integer pageNum;

    @ApiModelProperty("页容量")
    @TableField(exist = false)
    private Integer pageSize;

}
