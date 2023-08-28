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
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 13:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_team")
@ApiModel(value = "班组", description = "班组实体")
public class SysTeam {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("班组ID-(长度255)")
    private String id;

    @TableField(value = "team_name")
    @ApiModelProperty("班组名称-(长度255)")
    private String teamName;

    @TableField(value = "dept_name")
    @ApiModelProperty("所属部门-(长度255)")
    private String deptName;

    @TableField(value = "dept_id")
    @ApiModelProperty("部门id-(长度255)")
    private String deptId;

    @TableField(value = "created_person",fill = FieldFill.INSERT)
    @ApiModelProperty("创建人-(长度255)")
    private String createdPerson;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @TableField(value = "updated_person",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改人-(长度255)")
    private String updatedPerson;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;

}
