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
 * @date 2022/7/26 10:41
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_craft")
@ApiModel(value = "工艺", description = "工艺实体")
public class SysCraft {


    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("工艺ID-(长度255)")
    private String id;

    @TableField(value = "craft_name")
    @ApiModelProperty("工艺名称-(长度255)")
    private String craftName;

    @TableField(value = "craft_describe")
    @ApiModelProperty("工艺描述-(长度255)")
    private String craftDescribe;

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
    @ApiModelProperty("修改时间)")
    private Date updatedAt;
}
