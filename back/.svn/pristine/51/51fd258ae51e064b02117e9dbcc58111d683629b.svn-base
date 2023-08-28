package com.erp.floor.pojo.produce.domain;

import com.baomidou.mybatisplus.annotation.*;
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
 * @date 2022/9/14 11:01
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "patch_record")
@ApiModel(value = "补片记录", description = "补片记录实体")
public class PatchRecord {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("补片记录ID-(长度255)")
    private String id;

    @TableField(value = "patch_no")
    @ApiModelProperty("补片编号-(长度255)")
    private String patchNo;

    @TableField(value = "patch_person")
    @ApiModelProperty("补片人-(长度255)")
    private String patchPerson;

    @TableField(value = "patch_date")
    @ApiModelProperty("补片日期-(长度255)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date patchDate;

    @TableField(value = "patch_num")
    @ApiModelProperty("补片数量-(长度255)")
    private String patchNum;

    @TableField(value = "patch_area")
    @ApiModelProperty("补片面积-(长度255)")
    private String patchArea;

    @TableField(value = "damage_id")
    @ApiModelProperty("报损id-(长度255)")
    private String damageId;

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

    @ApiModelProperty("补片业务")
    @TableField(exist = false)
    private List<PatchBusiness> patchBusinessList;
}
