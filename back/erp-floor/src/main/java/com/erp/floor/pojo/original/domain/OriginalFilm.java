package com.erp.floor.pojo.original.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
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
 * @date： 2022-09-08 18:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "original_film")
@ApiModel(value = "原片定义", description = "原片定义实体类")
public class OriginalFilm {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "原片ID-长度(40)")
    private String id;

    @Excel(name = "原片名称")
    @TableField(value = "original_name")
    @ApiModelProperty(value = "原片名称-长度(20)")
    private String originalName;

    @Excel(name = "原片编号")
    @TableField(value = "original_no")
    @ApiModelProperty(value = "原片编号-长度(20)")
    private String originalNo;

    @Excel(name = "原片类型")
    @TableField(value = "original_type_name")
    @ApiModelProperty(value = "原片类型-长度(20)")
    private String originalTypeName;

    @TableField(value = "original_type_id")
    @ApiModelProperty(value = "原片类型ID-长度(40)")
    private String originalTypeId;

    @Excel(name = "颜色膜系")
    @TableField(value = "original_color")
    @ApiModelProperty(value = "颜色膜系-长度(20)")
    private String originalColor;

    @Excel(name = "厂家名称")
    @TableField(value = "mill_name")
    @ApiModelProperty(value = "厂家名称-长度(20)")
    private String millName;

    @Excel(name = "原片等级")
    @TableField(value = "original_grade")
    @ApiModelProperty(value = "原片等级-长度(10)")
    private String originalGrade;

    @Excel(name = "厚度(mm)")
    @TableField(value = "original_thick")
    @ApiModelProperty(value = "厚度-长度(0)")
    private Integer originalThick;

    @Excel(name = "长度(mm)")
    @TableField(value = "original_long")
    @ApiModelProperty(value = "长度-长度(0)")
    private Integer originalLong;

    @Excel(name = "宽度(mm)")
    @TableField(value = "original_width")
    @ApiModelProperty(value = "宽度-长度(0)")
    private Integer originalWidth;

    @Excel(name = "参考采购价(元)")
    @TableField(value = "reference_price")
    @ApiModelProperty(value = "参考采购价-长度(0)")
    private Double referencePrice;

    @Excel(name = "预警库存")
    @TableField(value = "early_warning")
    @ApiModelProperty(value = "预警库存-长度(0)")
    private Integer earlyWarning;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度(255)")
    private String remarks;

    @Excel(name = "原片库存数量")
    @TableField(value = "stock")
    @ApiModelProperty(value = "原片库存数量-长度(0)")
    private Integer stock;

    @Excel(name = "厂库总金额(元)")
    @TableField(value = "total_amount")
    @ApiModelProperty(value = "厂库总金额-长度(0)")
    private Double totalAmount;

    @Excel(name = "厂库单价(元)")
    @TableField(value = "unit_price")
    @ApiModelProperty(value = "厂库单价-长度(0)")
    private Double unitPrice;

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

    /*——————————————查询参数————————————————*/
    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer operationNum;

}
