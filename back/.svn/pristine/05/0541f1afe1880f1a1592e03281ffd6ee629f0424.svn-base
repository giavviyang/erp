package com.erp.floor.pojo.sales.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.erp.common.annotation.Excel;
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
 * @date： 2022-08-29 13:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "发货查询", description = "发货查询实体类")
public class DeliverRecordVo {
    @ApiModelProperty(value = "发货ID-长度(255)")
    private String id;

    @ApiModelProperty(value = "发货编号-长度(20)")
    private String deliverNo;

    @ApiModelProperty(value = "发货审核状态-长度(0)-(0审核通过、1审核未通过、2未审核)")
    private Integer deliverStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发货日期-开始")
    private String deliverDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发货日期-结束")
    private String deliverDateEnd;

    @ApiModelProperty(value = "发货负责人-长度(10)")
    private String deliverPerson;

    @ApiModelProperty(value = "发货方式-长度(10)")
    private String deliverMode;

    @ApiModelProperty(value = "发货地址-长度(255)")
    private String deliverAddress;

    @ApiModelProperty(value = "审核人-长度(10)")
    @TableField(value = "review_person")
    private String reviewPerson;

    @ApiModelProperty(value = "订单id-长度(255)")
    private String orderId;

    @ApiModelProperty(value = "订单编号-长度(255)")
    private String orderNo;

    @ApiModelProperty(value = "自定义编号-长度(255)")
    private String customNo;

    @ApiModelProperty(value = "产品名称-长度(255)")
    private String productName;

    @ApiModelProperty(value = "客户名称-长度(40)")
    private String customerName;

    @ApiModelProperty(value = "项目名称-长度(40)")
    private String entryName;

    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "页容量")
    private Integer pageSize;

    @ApiModelProperty(value = "是否删除-长度(0)（0未删除，1已删除）")
    private Integer isDel;

}
