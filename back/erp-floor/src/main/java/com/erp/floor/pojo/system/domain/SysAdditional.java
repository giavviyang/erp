package com.erp.floor.pojo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 附加项表
 *
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-25 14:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_additional")
@ApiModel(value = "附加项", description = "附加项实体")
public class SysAdditional {
    /*附加项ID*/
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty("附加项ID-(长度255)")
    private String id;
    /*附加项名称*/
    @TableField(value = "additional_name")
    @ApiModelProperty("附加项名称-(长度255)")
    private String additionalName;

    /*附加项别名*/
    @TableField(value = "additional_alias")
    @ApiModelProperty("附加项别名-(长度255)")
    private String additionalAlias;

    /*计算方式*/
    @TableField(value = "compute_type")
    @ApiModelProperty("计算方式-(长度255)")
    private String computeType;
    /*创建人*/
    @TableField(value = "created_person")
    @ApiModelProperty("创建人-(长度255)")
    private String createdPerson;
    /*创建时间*/
    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdAt;
    /*修改人*/
    @TableField(value = "updated_person")
    @ApiModelProperty("修改人-(长度255)")
    private String updatedPerson;
    /*修改时间*/
    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;

}
