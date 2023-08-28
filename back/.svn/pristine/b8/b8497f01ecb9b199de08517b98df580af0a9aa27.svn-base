package com.erp.floor.pojo.produce.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.erp.common.annotation.Excel;
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
 * @date 2022/9/6 10:04
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "damage_record")
@ApiModel(value = "报损记录", description = "报损记录实体")
public class DamageRecord {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("完工记录ID-(长度255)")
    private String id;

    @TableField(value = "damage_date")
    @ApiModelProperty("报损时间")
    @Excel(name = "报损时间",dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date damageDate;

    @TableField(value = "damage_no")
    @ApiModelProperty("报损编号-(长度255)")
    @Excel(name = "报损编号")
    private String damageNo;

    @TableField(value = "damage_person")
    @ApiModelProperty("报损负责人-(长度255)")
    @Excel(name = "报损负责人")
    private String damagePerson;

    @TableField(value = "damage_num")
    @ApiModelProperty("报损数量")
    @Excel(name = "报损数量")
    private int damageNum;

    @TableField(value = "damage_area")
    @ApiModelProperty("报损面积-(长度255)")
    @Excel(name = "报损面积(m²)")
    private int damageArea;

    @TableField(value = "flow_card_no")
    @ApiModelProperty("流程卡id-(长度255)")
    private String flowCardNo;

    @TableField(value = "patch_num")
    @ApiModelProperty("补片数量")
    @Excel(name = "补片数量")
    private int patchNum;

    @TableField(value = "patch_area")
    @ApiModelProperty("补片面积-(长度255)")
    @Excel(name = "补片面积(m²)")
    private String patchArea;

    @TableField(value = "created_person",fill = FieldFill.INSERT)
    @ApiModelProperty("创建人-(长度255)")
    @Excel(name = "创建人")
    private String createdPerson;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    @Excel(name = "创建时间",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改人-(长度255)")
    private String updatedPerson;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @ApiModelProperty("报损流程卡明细")
    @TableField(exist = false)
    private List<DamageFlowCard> damageFlowCardList;
}
