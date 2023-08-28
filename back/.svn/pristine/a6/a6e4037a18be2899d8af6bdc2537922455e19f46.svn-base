package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
import java.util.Map;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 10:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "deliver_record")
@ApiModel(value = "发货记录", description = "发货记录实体类")
public class DeliverRecord {

    @ApiModelProperty(value = "发货ID-长度(255)")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @Excel(name = "发货编号")
    @ApiModelProperty(value = "发货编号-长度(20)")
    @TableField(value = "deliver_no")
    private String deliverNo;

    @Excel(name = "发货审核状态", readConverterExp = "0=审核通过,1=审核未通过,2=未审核")
    @ApiModelProperty(value = "发货审核状态-长度(0)-(0审核通过、1审核未通过、2未审核)")
    @TableField(value = "deliver_status")
    private Integer deliverStatus;

    @Excel(name = "发货日期", width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发货日期-长度(0)")
    @TableField(value = "deliver_date")
    private Date deliverDate;

    @Excel(name = "发货负责人")
    @ApiModelProperty(value = "发货负责人-长度(10)")
    @TableField(value = "deliver_person")
    private String deliverPerson;

    @Excel(name = "发货方式")
    @ApiModelProperty(value = "发货方式-长度(10)")
    @TableField(value = "deliver_mode")
    private String deliverMode;

    @Excel(name = "发货地址")
    @ApiModelProperty(value = "发货地址-长度(255)")
    @TableField(value = "deliver_address")
    private String deliverAddress;

    @Excel(name = "发货数量")
    @ApiModelProperty(value = "发货数量-长度(0)")
    @TableField(value = "all_deliver_num")
    private Integer allDeliverNum;

    @Excel(name = "发货面积(m²)")
    @ApiModelProperty(value = "发货面积-长度(0)")
    @TableField(value = "deliver_area")
    private Double deliverArea;

    @Excel(name = "发货重量(kg)")
    @ApiModelProperty(value = "发货重量-长度(0)")
    @TableField(value = "deliver_weight")
    private Double deliverWeight;

    @Excel(name = "发货备注")
    @ApiModelProperty(value = "发货备注-长度(255)")
    @TableField(value = "deliver_remarks")
    private String deliverRemarks;

    @Excel(name = "发货联系人")
    @ApiModelProperty(value = "发货联系人-长度(10)")
    @TableField(value = "deliver_contacts")
    private String deliverContacts;

    @Excel(name = "发货联系人电话")
    @ApiModelProperty(value = "发货联系人电话-长度(20)")
    @TableField(value = "deliver_phone")
    private String deliverPhone;

    @Excel(name = "发货总费用(元)")
    @ApiModelProperty(value = "发货总费用-长度(0)")
    @TableField(value = "deliver_cost")
    private Double deliverCost;

    @Excel(name = "发货类型", readConverterExp = "0=订单发货,1=打包发货")
    @ApiModelProperty(value = "发货类型-长度(0)(0订单发货，1打包发货)")
    @TableField(value = "deliver_type")
    private Integer deliverType;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "删除时间-长度(0)")
    @TableField(value = "deleted_at")
    private Date deletedAt;

    @Excel(name = "运输方式")
    @ApiModelProperty(value = "运输方式-长度(20)")
    @TableField(value = "shipping_type")
    private String shippingType;

    @Excel(name = "司机")
    @ApiModelProperty(value = "司机-长度(10)")
    @TableField(value = "driver")
    private String driver;

    @Excel(name = "安装员")
    @ApiModelProperty(value = "安装员-长度(10)")
    @TableField(value = "installer")
    private String installer;

    @Excel(name = "车号")
    @ApiModelProperty(value = "车号-长度(20)")
    @TableField(value = "car_code")
    private String carCode;

    @ApiModelProperty(value = "货架品种-长度(255)")
    @TableField(value = "shelf_variety")
    private String shelfVariety;

    @Excel(name = "货架数量")
    @ApiModelProperty(value = "货架数量-长度(0)")
    @TableField(value = "shelves_num")
    private Integer shelvesNum;

    @ApiModelProperty(value = "运输费-长度(0)")
    @TableField(value = "freight")
    private Integer freight;

    @ApiModelProperty(value = "装卸费-长度(0)")
    @TableField(value = "handling_charges")
    private Integer handlingCharges;

    @ApiModelProperty(value = "包装费-长度(0)")
    @TableField(value = "packing_fee")
    private Integer packingFee;

    @ApiModelProperty(value = "安装费-长度(0)")
    @TableField(value = "installation_fee")
    private Integer installationFee;

    @ApiModelProperty(value = "其他费用-长度(0)")
    @TableField(value = "other_expenses")
    private Integer otherExpenses;

    @Excel(name = "回执负责人")
    @ApiModelProperty(value = "回执负责人-长度(10)")
    @TableField(value = "receipt_person")
    private String receiptPerson;

    @Excel(name = "回执日期", width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "回执日期-长度(0)")
    @TableField(value = "receipt_date")
    private Date receiptDate;

    @ApiModelProperty(value = "审核人-长度(10)")
    @TableField(value = "review_person")
    private String reviewPerson;

    @ApiModelProperty(value = "审核不通过原因-长度(10)")
    @TableField(value = "review_reason")
    private String reviewReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间-长度(0)")
    @TableField(value = "review_date")
    private Date reviewDate;

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

    @TableField(value = "is_del")
    @ApiModelProperty(value = "是否删除-长度(0)（0未删除，1已删除）")
    private Integer isDel;

    @ApiModelProperty(value = "发货货架")
    @TableField(exist = false)
    private List<DeliveryShelf> deliveryShelfList;

    @ApiModelProperty(value = "发货明细")
    @TableField(exist = false)
    private List<Map<String, Object>> deliverBusiness;

    @ApiModelProperty(value = "发货明细")
    @TableField(exist = false)
    private List<DeliverBusiness> deliverBusinessList;

}
