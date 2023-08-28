package com.erp.floor.pojo.original.domain.Vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-13 16:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "原片信息传输类")
public class OriginalFilmDTO {
    @Excel(name = "原片编号")
    @TableField(exist = false)
    @ApiModelProperty(value = "原片编号-长度(20)")
    private String originalNo;

    @Excel(name = "原片类型")
    @TableField(exist = false)
    @ApiModelProperty(value = "原片类型-长度(20)")
    private String originalTypeName;

    @TableField(exist = false)
    @ApiModelProperty(value = "原片类型ID-长度(40)")
    private String originalTypeId;

    @TableField(exist = false)
    @Excel(name = "颜色膜系")
    @ApiModelProperty(value = "颜色膜系-长度(20)")
    private String originalColor;

    @Excel(name = "厂家名称")
    @TableField(exist = false)
    @ApiModelProperty(value = "厂家名称-长度(20)")
    private String millName;

    @Excel(name = "原片等级")
    @TableField(exist = false)
    @ApiModelProperty(value = "原片等级-长度(10)")
    private String originalGrade;

    @Excel(name = "厚度(mm)")
    @TableField(exist = false)
    @ApiModelProperty(value = "厚度-长度(0)")
    private Integer originalThick;

    @Excel(name = "长度(mm)")
    @TableField(exist = false)
    @ApiModelProperty(value = "长度-长度(0)")
    private Integer originalLong;

    @Excel(name = "宽度(mm)")
    @TableField(exist = false)
    @ApiModelProperty(value = "宽度-长度(0)")
    private Integer originalWidth;
}
