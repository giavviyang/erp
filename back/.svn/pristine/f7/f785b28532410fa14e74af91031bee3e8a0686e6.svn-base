package com.erp.floor.pojo.accessories.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import com.erp.floor.pojo.accessories.domain.VO.AccessoriesDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 17:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "accessories_purchase_detail")
@ApiModel(value = "辅料采购明细", description = "辅料采购明细实体类")
public class AccessoriesPurchaseDetail extends AccessoriesDTO {

    @TableField(value = "id")
    @ApiModelProperty(value = "id-长度(40)")
    private String id;

    @TableField(value = "purchase_id")
    @ApiModelProperty(value = "采购id-长度(40)")
    private String purchaseId;

    @Excel(name = "采购编号")
    @TableField(value = "purchase_no")
    @ApiModelProperty(value = "采购编号-长度(20)")
    private String purchaseNo;

    @TableField(value = "accessories_id")
    @ApiModelProperty(value = "辅料id-长度(40)")
    private String accessoriesId;

    @Excel(name = "辅料编号")
    @TableField(value = "accessories_no")
    @ApiModelProperty(value = "辅料编号-长度(20)")
    private String accessoriesNo;

    @Excel(name = "辅料名称")
    @TableField(value = "accessories_name")
    @ApiModelProperty(value = "辅料名称-长度(20)")
    private String accessoriesName;

    @Excel(name = "采购数量")
    @TableField(value = "purchase_num")
    @ApiModelProperty(value = "采购数量-长度(0)")
    private Integer purchaseNum;

    @Excel(name = "采购金额(元)")
    @TableField(value = "purchase_amount")
    @ApiModelProperty(value = "采购金额-长度(0)")
    private Double purchaseAmount;

    @Excel(name = "采购单价(元)")
    @TableField(value = "purchase_price")
    @ApiModelProperty(value = "采购单价-长度(0)")
    private Double purchasePrice;

    @Excel(name = "采购单位")
    @TableField(value = "purchase_company")
    @ApiModelProperty(value = "采购单位-长度(20)")
    private String purchaseCompany;

    @TableField(exist = false)
    private Integer operationNum;

}
