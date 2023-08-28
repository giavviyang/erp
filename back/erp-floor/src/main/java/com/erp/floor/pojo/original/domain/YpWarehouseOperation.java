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
 * @date： 2022-09-13 12:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "yp_warehouse_operation")
@ApiModel(value = "原片仓库记录", description = "原片仓库记录实体类")
public class YpWarehouseOperation {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "操作ID-(长度-40)")
    private String id;

    @Excel(name = "操作编号")
    @TableField(value = "operation_no")
    @ApiModelProperty(value = "操作编号 （入库、出库、盘库）-(长度-20)")
    private String operationNo;

    @Excel(name = "操作类型")
    @TableField(value = "operation_type")
    @ApiModelProperty(value = "操作类型 （入库、出库、盘库）-(长度-5)")
    private String operationType;

    @Excel(name = "采购单号")
    @TableField(value = "purchase_no")
    @ApiModelProperty(value = "采购单号-(长度-255)")
    private String purchaseNo;

    @Excel(name = "供应商")
    @TableField(value = "mill_id")
    @ApiModelProperty(value = "供应商-(长度-10)")
    private String millId;

    @Excel(name = "数量")
    @TableField(value = "original_num")
    @ApiModelProperty(value = "数量-(长度-0)")
    private Integer originalNum;

    @Excel(name = "面积(m²)")
    @TableField(value = "original_area")
    @ApiModelProperty(value = "面积-(长度-0)")
    private Double originalArea;

    @Excel(name = "重量(kg)")
    @TableField(value = "original_weight")
    @ApiModelProperty(value = "重量-(长度-0)")
    private Double originalWeight;

    @Excel(name = "总金额(元)")
    @TableField(value = "total_amount")
    @ApiModelProperty(value = "总金额-(长度-0)")
    private Double totalAmount;

    @Excel(name = "负责人")
    @TableField(value = "person")
    @ApiModelProperty(value = "负责人-(长度-10)")
    private String person;

    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "operation_date")
    @ApiModelProperty(value = "操作时间-(长度-0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationDate;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-(长度-255)")
    private String remarks;

    @TableField(value = "created_person")
    @ApiModelProperty(value = "创建人-(长度-10)")
    private String createdPerson;

    @TableField(value = "created_at")
    @ApiModelProperty(value = "创建时间-(长度-0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty(value = "修改人-(长度-10)")
    private String updatedPerson;

    @TableField(value = "updated_at")
    @ApiModelProperty(value = "修改时间-(长度-0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /*——————————————————新增信息————————————————————*/
    @ApiModelProperty(value = "入库明细")
    @TableField(exist = false)
    private List<OriginalFilm> originalFilmList;

    @ApiModelProperty(value = "采购单明细")
    @TableField(exist = false)
    private List<OriginalFilmPurchase> originalFilmPurchaseList;

    /*——————————————————查询信息————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "操作日期-开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String operationDateStart;

    @TableField(exist = false)
    @ApiModelProperty(value = "操作日期-结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String operationDateEnd;

    @TableField(exist = false)
    @ApiModelProperty(value = "页容量")
    private Integer pageSize;

    @TableField(exist = false)
    @ApiModelProperty(value = "页码")
    private Integer pageNum;


}
