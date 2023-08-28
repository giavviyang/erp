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
 * @date： 2022-08-24 20:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "packing_record")
@ApiModel(value = "打包记录", description = "打包记录实体类")
public class PackingRecord {

    @TableField(value = "id")
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单id-长度(255)")
    private String orderId;

    @Excel(name = "订单编号")
    @TableField(value = "order_no")
    @ApiModelProperty(value = "订单编号-长度(255)")
    private String orderNo;

    @Excel(name = "自定义编号")
    @TableField(value = "custom_no")
    @ApiModelProperty(value = "自定义编号-长度(255)")
    private String customNo;

    @Excel(name = "打包编号")
    @TableField(value = "packaging_no")
    @ApiModelProperty(value = "打包编号-长度(40)")
    private String packagingNo;

    @Excel(name = "打包名称")
    @TableField(value = "packaging_name")
    @ApiModelProperty(value = "打包名称-长度(20)")
    private String packagingName;

    @Excel(name = "产品名称")
    @TableField(value = "product_name")
    @ApiModelProperty(value = "产品名称-长度(255)")
    private String productName;

    @Excel(name = "客户名称")
    @TableField(value = "customer_name")
    @ApiModelProperty(value = "客户名称-长度(40)")
    private String customerName;

    @Excel(name = "项目名称")
    @TableField(value = "entry_name")
    @ApiModelProperty(value = "项目名称-长度(40)")
    private String entryName;

    @Excel(name = "打包负责人")
    @TableField(value = "responsible_person")
    @ApiModelProperty(value = "打包负责人-长度(10)")
    private String responsiblePerson;

    @Excel(name = "打包日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "packaging_date")
    @ApiModelProperty(value = "打包日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date packagingDate;

    @Excel(name = "打包方式")
    @TableField(value = "packaging_mode")
    @ApiModelProperty(value = "打包方式-长度(20)")
    private String packagingMode;

    @Excel(name = "打包费用(元)")
    @TableField(value = "packaging_cost")
    @ApiModelProperty(value = "打包费用-长度(0)")
    private Integer packagingCost;

    @Excel(name = "打包数量")
    @TableField(value = "pack_num")
    @ApiModelProperty(value = "打包数量-长度(0)")
    private Integer packNum;

    @Excel(name = "打包面积(m²)")
    @TableField(value = "packaging_area")
    @ApiModelProperty(value = "打包面积-长度(0)")
    private Double packagingArea;

    @Excel(name = "打包重量(kg)")
    @TableField(value = "packaging_weight")
    @ApiModelProperty(value = "打包重量-长度(0)")
    private Double packagingWeight;

    @TableField(value = "deliver_state")
    @ApiModelProperty(value = "发货状态-长度(10)（未发货、部分发货、全部发货）")
    private String deliverState;

    @TableField(value = "created_people")
    @ApiModelProperty(value = "创建人-长度(255)")
    private String createdPeople;

    @TableField(value = "created_at")
    @ApiModelProperty(value = "创建日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_people")
    @ApiModelProperty(value = "修改人-长度(255)")
    private String updatedPeople;

    @TableField(value = "updated_at")
    @ApiModelProperty(value = "修改日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度(255)")
    private String remarks;

    @TableField(exist = false)
    @ApiModelProperty(value = "打包明细集合-新增")
    private List<OrderProduct> list;

    @TableField(exist = false)
    @ApiModelProperty(value = "打包明细集合-修改")
    private List<PackingBusiness> packList;
}
