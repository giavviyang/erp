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
 * @date： 2022-09-23 13:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "outsourced_warehousing")
@ApiModel(value = "外协单入库记录", description = "外协单入库记录实体类")
public class OutsourcedWarehousing {

    @TableField(value = "id")
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "outsourcing_id")
    @ApiModelProperty(value = "外协id-长度(40)")
    private String outsourcingId;

    @Excel(name = "外协编号")
    @TableField(value = "outsourcing_no")
    @ApiModelProperty(value = "外协编号-长度(20)")
    private String outsourcingNo;

    @Excel(name = "入库单号")
    @TableField(value = "warehoused_no")
    @ApiModelProperty(value = "入库单号-长度(20)")
    private String warehousedNo;

    @Excel(name = "入库人")
    @TableField(value = "warehoused_person")
    @ApiModelProperty(value = "入库人-长度(10)")
    private String warehousedPerson;

    @Excel(name = "入库时间", width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "warehoused_date")
    @ApiModelProperty(value = "入库时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date warehousedDate;

    @Excel(name = "入库数量")
    @TableField(value = "warehoused_num")
    @ApiModelProperty(value = "入库数量-长度(0)")
    private Integer warehousedNum;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度(255)")
    private String remarks;


    /*———————————————————————————查询信息—————————————————————————————*/
    @ApiModelProperty("入库日期 - 开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private String warehousedDateStart;

    @ApiModelProperty("入库日期 - 结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private String warehousedDateEnd;

    @ApiModelProperty("页码")
    @TableField(exist = false)
    private Integer pageNum;

    @ApiModelProperty("页容量")
    @TableField(exist = false)
    private Integer pageSize;

    @Excel(name = "外协日期", width = 30 , dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("外协日期")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outgoingDate;

    @Excel(name = "外协单位")
    @ApiModelProperty("外协单位")
    @TableField(exist = false)
    private String outsourcingUnit;

    @Excel(name = "外协负责人")
    @ApiModelProperty("外协负责人")
    @TableField(exist = false)
    private String outsourcingLeader;


    /*———————————————————————————新增信息—————————————————————————————*/
    @ApiModelProperty("入库明细")
    @TableField(exist = false)
    List<OutsourcedProducts> outsourcedProductsList;


}
