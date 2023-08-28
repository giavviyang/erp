package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "shelf_manage")
@ApiModel(value = "铁架信息", description = "铁架信息实体类")
public class ShelfManage {

    @TableField(value = "id")
    @ApiModelProperty(value = "主键-长度(255)")
    private String id;

    @TableField(value = "frame_no")
    @ApiModelProperty(value = "铁架编号-长度(255)")
    private String frameNo;

    @TableField(value = "frame_name")
    @ApiModelProperty(value = "铁架名称-长度(255)")
    private String frameName;

    @TableField(value = "frame_specs")
    @ApiModelProperty(value = "铁架规格-长度(255)")
    private String frameSpecs;

    @TableField(value = "frame_num")
    @ApiModelProperty(value = "铁架数量-长度(0)")
    private Integer frameNum;

    @TableField(value = "created_person")
    @ApiModelProperty(value = "创建人-长度(255)")
    private String createdPerson;

    @TableField(value = "created_at")
    @ApiModelProperty(value = "创建时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty(value = "修改人-长度(255)")
    private String updatedPerson;

    @TableField(value = "updated_at")
    @ApiModelProperty(value = "修改时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否带回")
    private Integer isBring;

}
