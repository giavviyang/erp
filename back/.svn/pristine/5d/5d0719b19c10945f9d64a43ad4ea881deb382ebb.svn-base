package com.erp.floor.pojo.produce.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.erp.common.annotation.Excel;
import com.erp.common.core.domain.BaseEntity;
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
 * @date 2022/8/29 13:37
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "completion_record")
@ApiModel(value = "完工记录", description = "完工记录实体")
public class CompletionRecord {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("完工记录ID-(长度255)")
    private String id;

    @TableField(value = "completion_no")
    @ApiModelProperty("完工单号-(长度255)")
    @Excel(name = "完工单号")
    private String completionNo;

    @TableField(value = "completion_craft_name")
    @ApiModelProperty("完工工艺名称-(长度255)")
    @Excel(name = "工艺")
    private String completionCraftName;

    @TableField(value = "completion_craft_id")
    @ApiModelProperty("完工工艺id-(长度255)")
    private String completionCraftId;

    @TableField(value = "completion_shift")
    @ApiModelProperty("完工班次-(长度255)")
    @Excel(name = "班次")
    private String completionShift;

    @TableField(value = "completion_team")
    @ApiModelProperty("完工班组-(长度255)")
    private String completionTeam;

    @TableField(value = "completion_num")
    @ApiModelProperty("完工数量-(长度255)")
    @Excel(name = "完工数量")
    private String completionNum;

    @TableField(value = "completion_date")
    @ApiModelProperty("完工日期-(长度255)")
    @Excel(name = "完工日期",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completionDate;

    @TableField(value = "flow_card_no")
    @ApiModelProperty("流程卡id-(长度255)")
    @Excel(name = "流程卡号")
    private String flowCardNo;

    @TableField(value = "remarks")
    @ApiModelProperty("备注-(长度255)")
    @Excel(name = "备注")
    private String remarks;

    @ApiModelProperty("List<完工人员>")
    @TableField(exist = false)
    private List<CompletionPerson> completionPersonList;

    @ApiModelProperty("List<完工流程卡>")
    @TableField(exist = false)
    private List<CompletionBusiness> completionBusinessList;
    @TableField(value = "created_person",fill = FieldFill.INSERT)
    @ApiModelProperty("创建人-(长度255)")
    @Excel(name = "创建人")
    private String createdPerson;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间)")
    @Excel(name = "创建时间",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改人-(长度255)")
    private String updatedPerson;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改时间")
    private Date updatedAt;

}
