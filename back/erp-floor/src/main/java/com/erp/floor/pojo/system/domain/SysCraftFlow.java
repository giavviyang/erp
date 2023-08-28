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
@TableName(value = "sys_craft_flow")
@ApiModel(value = "工艺流程", description = "工艺流程实体")
public class SysCraftFlow {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("编号-(长度255)")
    private String id;

    @TableField(value = "craft_flow_id")
    @ApiModelProperty("工艺流程id")
    private String craftFlowId;

    @TableField(value = "craft_flow_txt")
    @ApiModelProperty("工艺流程文本-(长度255)")
    private String craftFlowTxt;

    @TableField(value = "craft_flow_describe")
    @ApiModelProperty("流程描述-(长度255)")
    private String craftFlowDescribe;

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
