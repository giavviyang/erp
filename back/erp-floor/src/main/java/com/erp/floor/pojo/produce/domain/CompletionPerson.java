package com.erp.floor.pojo.produce.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/29 13:38
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "completion_person")
@ApiModel(value = "完工人员记录", description = "完工记录相关人员实体")
public class CompletionPerson {

    @TableId(value = "id" , type = IdType.UUID)
    @ApiModelProperty("完工人员记录ID-(长度255)")
    private String id;

    @TableField(value = "completion_person")
    @ApiModelProperty("完工人员-(长度255)")
    private String completionPerson;

    @TableField(value = "completion_person_id")
    @ApiModelProperty("完工人员id-(长度255)")
    private String completionPersonId;

    @TableField(value = "completion_record_id")
    @ApiModelProperty("完工单id-(长度255)")
    private String completionRecordId;

    @TableField(value = "completion_proportion")
    @ApiModelProperty("完工比例-(长度255)")
    private String completionProportion;

    @TableField(value = "completion_coefficient")
    @ApiModelProperty("完工系数-(长度255)")
    private String completionCoefficient;

}
