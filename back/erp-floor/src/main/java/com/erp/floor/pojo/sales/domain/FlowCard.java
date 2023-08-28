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

import java.util.Date;
import java.util.List;

/** 流程卡实体类
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-28 11:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "flow_card")
@ApiModel(value = "流程卡", description = "流程卡实体类")
public class FlowCard {

    @ApiModelProperty("主键")
    @TableField(value = "id")
    private String id;

    @Excel(name = "流程卡编号")
    @ApiModelProperty("流程卡编号")
    @TableField(value = "flow_card_no")
    private String flowCardNo;

    @Excel(name = "订单编号")
    @ApiModelProperty("订单编号")
    @TableField(value = "order_no")
    private String orderNo;

    @Excel(name = "订单自定义编号")
    @ApiModelProperty("订单自定义编号")
    @TableField(value = "custom_no")
    private String customNo;

    @Excel(name = "产品名称")
    @ApiModelProperty("产品名称")
    @TableField(value = "product_name")
    private String productName;

    @Excel(name = "客户名称")
    @ApiModelProperty("客户名称")
    @TableField(value = "customer_name")
    private String customerName;

    @Excel(name = "单片名称")
    @ApiModelProperty("单片名称")
    @TableField(value = "monolithic_name")
    private String monolithicName;

    @Excel(name = "单片厚度(mm)")
    @ApiModelProperty("单片厚度")
    @TableField(value = "monolithic_thick")
    private Integer monolithicThick;

    @Excel(name = "工艺流程内容")
    @ApiModelProperty("工艺流程内容")
    @TableField(value = "process_content")
    private String processContent;

    @Excel(name = "分架日期")
    @ApiModelProperty("分架日期")
    @TableField(value = "split_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date splitDate;

    @Excel(name = "分架人")
    @ApiModelProperty("分架人")
    @TableField(value = "split_person")
    private String splitPerson;

    @Excel(name = "分架数量")
    @ApiModelProperty("分架数量")
    @TableField(value = "split_num")
    private Integer splitNum;

    @Excel(name = "损坏数量")
    @ApiModelProperty("损坏数量")
    @TableField(value = "damage_num")
    private Integer damageNum;

    @Excel(name = "补片数量")
    @ApiModelProperty("补片数量")
    @TableField(value = "patch_num")
    private Integer patchNum;

    @ApiModelProperty("补片id")
    @TableField(value = "patch_id")
    private String patchId;

    @ApiModelProperty("补片流程卡id")
    @TableField(value = "patch_flow_card_id")
    private String patchFlowCardId;

    @Excel(name = "补片流程卡编号")
    @ApiModelProperty("补片流程卡编号")
    @TableField(value = "patch_flow_card_no")
    private String patchFlowCardNo;

    @Excel(name = "总面积(m²)")
    @ApiModelProperty("总面积")
    @TableField(value = "total_area")
    private Double totalArea;

    @Excel(name = "总重量(kg)")
    @ApiModelProperty("总重量")
    @TableField(value = "total_weight")
    private Double totalWeight;

    @Excel(name = "生产状态", readConverterExp = "0=未生产,1=生产中,2=完成生产")
    @ApiModelProperty("生产状态(0未生产，1生产中，2完成生产)")
    @TableField(value = "production_status")
    private Integer productionStatus;

    @Excel(name = "已完成工艺")
    @ApiModelProperty("已完成工艺")
    @TableField(value = "complete_process")
    private String completeProcess;

    @ApiModelProperty("排产id")
    @TableField(value = "scheduling_id")
    private String schedulingId;

    @Excel(name = "排产日期", width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("排产日期")
    @TableField(value = "scheduling_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date schedulingDate;

    @Excel(name = "排产数量")
    @ApiModelProperty("排产数量")
    @TableField(value = "scheduling_num")
    private Integer schedulingNum;

    @Excel(name = "发货单号")
    @ApiModelProperty("发货单号")
    @TableField(value = "delivery_no")
    private String deliveryNo;

    @Excel(name = "发货人")
    @ApiModelProperty("发货人")
    @TableField(value = "delivery_people")
    private String deliveryPeople;

    @Excel(name = "发货日期" , width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发货日期")
    @TableField(value = "delivery_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryDate;

    @ApiModelProperty("修改人")
    @TableField(value = "updated_people")
    private String updatedPeople;

    @ApiModelProperty("修改日期")
    @TableField(value = "updated_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDate;

    @TableField(exist = false)
    @ApiModelProperty("品种+厚度+工艺流程")
    private String key;
    @TableField(exist = false)
    @ApiModelProperty("流程卡明细集合")
    private List<ShelfDivisionBusiness> list;

    @TableField(exist = false)
    @ApiModelProperty("项目名称")
    private String entryName;

}
