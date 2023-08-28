package com.erp.floor.pojo.sales.domain;

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
 * @date： 2022-09-21 14:27 productName
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "cp_record")
@ApiModel(value = "成品操作记录", description = "成品操作记录实体类")
public class CpRecord {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "operation_no")
    @ApiModelProperty(value = "操作编号-长度(20)")
    private String operationNo;

    @TableField(value = "operation_people")
    @ApiModelProperty(value = "操作负责人-长度(255)")
    private String operationPeople;

    @TableField(value = "operation_date")
    @ApiModelProperty(value = "操作日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationDate;

    @TableField(value = "operation_type")
    @ApiModelProperty(value = "操作类型-长度(255)")
    private String operationType;

    @TableField(value = "operation_number")
    @ApiModelProperty(value = "操作数量-长度(0)")
    private Integer operationNumber;

    @TableField(value = "operation_area")
    @ApiModelProperty(value = "操作面积-长度(0)")
    private Double operationArea;

    @TableField(value = "operation_weight")
    @ApiModelProperty(value = "操作重量-长度(0)")
    private Double operationWeight;

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

    /*———————————————————————新增明细—————————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "产品集合")
    private List<OrderProduct> orderProducts;

    /*—————————————————————查询数据—————————————————————*/
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
