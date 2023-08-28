package com.erp.floor.pojo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**编号规则表
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-25 16:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_numbering_rules")
public class SysNumberingRules {
    /*规则ID*/
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty("编号规则ID-(长度255)")
    private String id;
    /*规则名称*/
    @TableField(value = "rule_name")
    @ApiModelProperty("规则名称-(长度255)")
    private String ruleName;
    /*编号规则展示*/
    @TableField(value = "rule_display")
    @ApiModelProperty("编号规则展示-(长度255)")
    private String ruleDisplay;
    /*描述*/
    @TableField(value = "rule_describe")
    @ApiModelProperty("描述-(长度255)")
    private String ruleDescribe;
    /*备注*/
    @TableField(value = "remarks")
    @ApiModelProperty("备注-(长度255)")
    private String remarks;
    /*编号前缀*/
    @TableField(value = "rule_prefix")
    @ApiModelProperty("编号前缀-(长度255)")
    private String rulePrefix;
    /*编号中部*/
    @TableField(value = "rule_content")
    @ApiModelProperty("编号中部-(长度255)")
    private String ruleContent;
    /*编号后缀(0两位数，1三位数)*/
    @TableField(value = "rule_suffix")
    @ApiModelProperty("编号后缀(0两位数，1三位数)")
    private Integer ruleSuffix;
    /*前中连接符*/
    @TableField(value = "front_connection")
    @ApiModelProperty("前中连接符-(长度255)")
    private String frontConnection;
    /*中后连接符*/
    @TableField(value = "rear_connection")
    @ApiModelProperty("中后连接符-(长度255)")
    private String rearConnection;
    /*修改者*/
    @TableField(value = "updated_person")
    @ApiModelProperty("修改者-(长度255)")
    private String updatedPerson;
    /*修改日期*/
    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改日期")
    private Date updatedAt;
    /*创建者*/
    @TableField(value = "create_person")
    @ApiModelProperty("创建者-(长度255)")
    private String createPerson;
    /*创建日期*/
    @TableField(value = "creat_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createAt;

}
