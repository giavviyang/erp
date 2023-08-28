package com.erp.floor.pojo.accessories.domain;

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
 * @date： 2022-09-14 16:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "accessories")
@ApiModel(value = "辅料", description = "辅料实体类")
public class Accessories {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "辅料id-长度-(40)")
    private String id;

    @Excel(name = "辅料名称")
    @TableField(value = "accessories_name")
    @ApiModelProperty(value = "辅料名称-长度-(20)")
    private String accessoriesName;

    @Excel(name = "辅料编号")
    @TableField(value = "accessories_no")
    @ApiModelProperty(value = "辅料编号-长度-(20)")
    private String accessoriesNo;

    @Excel(name = "辅料类型")
    @TableField(value = "type_name")
    @ApiModelProperty(value = "辅料类型-长度-(20)")
    private String typeName;

    @TableField(value = "type_id")
    @ApiModelProperty(value = "辅料类型id-长度-(40)")
    private String typeId;

    @Excel(name = "规格型号")
    @TableField(value = "accessories_specifications")
    @ApiModelProperty(value = "规格型号-长度-(10)")
    private String accessoriesSpecifications;

    @Excel(name = "单位")
    @TableField(value = "accessories_company")
    @ApiModelProperty(value = "单位-长度-(20)")
    private String accessoriesCompany;

    @Excel(name = "厂家名称")
    @TableField(value = "accessories_mill")
    @ApiModelProperty(value = "厂家名称-长度-(20)")
    private String accessoriesMill;

    @Excel(name = "参考采购价(元)")
    @TableField(value = "reference_price")
    @ApiModelProperty(value = "参考采购价-长度-(0)")
    private Double referencePrice;

    @Excel(name = "预警库存")
    @TableField(value = "alarm_inventory")
    @ApiModelProperty(value = "预警库存-长度-(0)")
    private Integer alarmInventory;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-长度-(255)")
    private String remarks;

    @Excel(name = "辅料库存数量")
    @TableField(value = "stock")
    @ApiModelProperty(value = "辅料库存数量-长度-(0)")
    private Integer stock;

    @Excel(name = "厂库总金额(元)")
    @TableField(value = "total_amount")
    @ApiModelProperty(value = "厂库总金额-长度-(0)")
    private Double totalAmount;

    @Excel(name = "仓库单价(元)")
    @TableField(value = "uni_price")
    @ApiModelProperty(value = "仓库单价-长度-(0)")
    private Double uniPrice;

    @TableField(value = "created_person")
    @ApiModelProperty(value = "创建人-长度-(255)")
    private String createdPerson;

    @TableField(value = "created_at")
    @ApiModelProperty(value = "创建时间-长度-(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty(value = "修改人-长度-(255)")
    private String updatedPerson;

    @TableField(value = "updated_at")
    @ApiModelProperty(value = "修改时间-长度-(0)")
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
