package com.erp.floor.pojo.produce.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.erp.common.annotation.Excel;
import com.erp.floor.pojo.sales.domain.FlowCard;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/19 14:04
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "scheduling")
@ApiModel(value = "生产计划", description = "生产计划实体")
public class Scheduling {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("生产计划ID-(长度255)")
    private String id;

    @TableField(value = "scheduling_no")
    @ApiModelProperty("排产编号-(长度255)")
    @Excel(name = "排产编号")
    private String schedulingNo;

    @TableField(value = "scheduling_num")
    @ApiModelProperty("排产数量-(长度255)")
    @Excel(name = "排产数量")
    private String schedulingNum;

    @TableField(value = "scheduling_area")
    @ApiModelProperty("排产面积-(长度255)")
    @Excel(name = "排产面积(m²)")
    private String schedulingArea;

    @TableField(value = "start_date")
    @ApiModelProperty("开始时间-(长度255)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间",dateFormat = "yyyy-MM-dd")
    private Date startDate;

    @TableField(value = "end_date")
    @ApiModelProperty("结束时间-(长度255)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间",dateFormat = "yyyy-MM-dd")
    private Date endDate;

    @TableField(value = "scheduling_person")
    @ApiModelProperty("排产人-(长度255)")
    @Excel(name = "排产人")
    private String schedulingPerson;

    @TableField(value = "scheduling_date")
    @ApiModelProperty("排产日期-(长度255)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "排产日期",dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date schedulingDate;

    @TableField(value = "remarks")
    @ApiModelProperty("备注-(长度255)")
    @Excel(name = "备注")
    private String remarks;

    @TableField(value = "created_person",fill = FieldFill.INSERT)
    @ApiModelProperty("创建人")
    private String createdPerson;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改人")
    private String updatedPerson;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updated_at;

    @ApiModelProperty("排产流程卡")
    @TableField(exist = false)
    private List<FlowCard> flowCardList;
}
