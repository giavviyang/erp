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
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-05 14:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "return_record")
@ApiModel(value = "退货", description = "退货实体类")
public class ReturnRecord {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "退货ID-长度(40)")
    private String id;

    @Excel(name = "退货编号")
    @TableField(value = "return_no")
    @ApiModelProperty(value = "退货编号-长度(20)")
    private String returnNo;

    @Excel(name = "退货日期", width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "return_date")
    @ApiModelProperty(value = "退货日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnDate;

    @Excel(name = "退货方式")
    @TableField(value = "return_mode")
    @ApiModelProperty(value = "退货方式-长度(20)")
    private String returnMode;

    @Excel(name = "退货原因")
    @TableField(value = "return_reason")
    @ApiModelProperty(value = "退货原因-长度(255)")
    private String returnReason;

    @Excel(name = "退货负责人")
    @TableField(value = "return_person")
    @ApiModelProperty(value = "退货负责人-长度(10)")
    private String returnPerson;

    @Excel(name = "退货数量")
    @TableField(value = "return_num")
    @ApiModelProperty(value = "退货数量-长度(0)")
    private Integer returnNum;

    @Excel(name = "退货金额(元)")
    @TableField(value = "return_amount")
    @ApiModelProperty(value = "退货金额-长度(0)")
    private Double returnAmount;

    @Excel(name = "退货面积(m²)")
    @TableField(value = "return_area")
    @ApiModelProperty(value = "退货面积-长度(0)")
    private Double returnArea;

    @Excel(name = "退货重量(kg)")
    @TableField(value = "return_weight")
    @ApiModelProperty(value = "退货重量-长度(0)")
    private Double returnWeight;

    @TableField(value = "created_person")
    @ApiModelProperty(value = "创建人-长度(255)")
    private String createdPerson;

    @TableField(value = "created_at")
    @ApiModelProperty(value = "创建时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty(value = "修改人-长度(255)")
    private String updatedPerson;

    @TableField(value = "updated_at")
    @ApiModelProperty(value = "修改时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @Excel(name = "发货编号")
    @TableField(value = "deliver_no")
    @ApiModelProperty(value = "发货编号-长度(255)")
    private String deliverNo;

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
    @ApiModelProperty(value = "项目名称-长度(255)")
    private String entryName;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度(255)")
    private String remarks;

    @Excel(name = "审核状态", readConverterExp = "0=审核通过,1=审核未通过,2=未审核")
    @TableField(value = "examine_starts")
    @ApiModelProperty(value = "审核状态-长度(10)(0审核通过、1审核未通过、2未审核)")
    private Integer examineStarts;

    @Excel(name = "审核人")
    @TableField(value = "examine_person")
    @ApiModelProperty(value = "审核人-长度(10)")
    private String examinePerson;

    @Excel(name = "审核日期")
    @TableField(value = "examine_date")
    @ApiModelProperty(value = "审核日期-长度(0)")
    private String examineDate;

    @Excel(name = "审核不通过原因")
    @TableField(value = "examine_reason")
    @ApiModelProperty(value = "审核不通过原因-长度(255)")
    private String examineReason;


    /*____________________查询字段——————————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "页容量")
    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    @ApiModelProperty(value = "退货日期-开始")
    private String returnDateStart;

    @TableField(exist = false)
    @ApiModelProperty(value = "退货日期-结束")
    private String returnDateEnd;

    /*—————————————————————————新增字段—————————————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "新增明细")
    List<DeliverBusiness> deliverBusinessList;

    /*—————————————————————————查看明细字段———————————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "退货明细")
    List<ReturnBusiness> returnBusinesses;

}
