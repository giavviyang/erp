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
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-09 12:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "original_film_purchase")
@ApiModel(value = "原片采购", description = "原片采购实体类")
public class OriginalFilmPurchase {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "采购ID-长度(40)")
    private String id;

    @Excel(name = "采购编号")
    @TableField(value = "purchase_no")
    @ApiModelProperty(value = "采购编号-长度(20)")
    private String purchaseNo;

    @Excel(name = "采购日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "purchase_date")
    @ApiModelProperty(value = "采购日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseDate;

    @Excel(name = "采购负责人")
    @TableField(value = "purchase_person")
    @ApiModelProperty(value = "采购负责人-长度(10)")
    private String purchasePerson;

    @Excel(name = "供应商")
    @TableField(value = "mill_name")
    @ApiModelProperty(value = "供应商-长度(10)")
    private String millName;

    @Excel(name = "采购数量")
    @TableField(value = "purchase_num")
    @ApiModelProperty(value = "采购数量-长度(0)")
    private Integer purchaseNum;

    @Excel(name = "采购面积(m²)")
    @TableField(value = "purchase_area")
    @ApiModelProperty(value = "采购面积-长度(0)")
    private Double purchaseArea;

    @Excel(name = "采购金额")
    @TableField(value = "purchase_amount")
    @ApiModelProperty(value = "采购金额-长度(0)")
    private Double purchaseAmount;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度(255)")
    private String remarks;

    @Excel(name = "入库状态")
    @TableField(value = "status")
    @ApiModelProperty(value = "入库状态-长度(10)")
    private String status;

    @TableField(value = "created_person")
    @ApiModelProperty(value = "创建人-长度(10)")
    private String createdPerson;

    @TableField(value = "created_at")
    @ApiModelProperty(value = "创建时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty(value = "修改人-长度(0)")
    private String updatedPerson;

    @TableField(value = "updated_at")
    @ApiModelProperty(value = "修改时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /*——————————————————查询参数————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "采购日期-开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String purchaseDateStart;

    @TableField(exist = false)
    @ApiModelProperty(value = "采购日期-结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String purchaseDateEnd;

    @TableField(exist = false)
    @ApiModelProperty(value = "页容量")
    private Integer pageSize;

    @TableField(exist = false)
    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    /*——————————————————————新增参数————————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "原片集合")
    private List<OriginalFilm> originalFilmList;




}
