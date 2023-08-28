package com.erp.floor.pojo.original.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.common.annotation.Excel;
import com.erp.floor.pojo.original.domain.Vo.OriginalFilmDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-09 15:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "original_film_purchase_detail")
@ApiModel(value = "原片采购明细", description = "原片采购明细实体类")
public class OriginalFilmPurchaseDetail extends OriginalFilmDTO {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "id-长度(40)")
    private String id;

    @TableField(value = "purchase_id")
    @ApiModelProperty(value = "采购id-长度(40)")
    private String purchaseId;

    @Excel(name = "采购编号")
    @TableField(value = "purchase_no")
    @ApiModelProperty(value = "采购编号-长度(40)")
    private String purchaseNo;

    @Excel(name = "原片名称")
    @TableField(value = "original_film_name")
    @ApiModelProperty(value = "原片名称-长度(20)")
    private String originalFilmName;

    @TableField(value = "original_film_id")
    @ApiModelProperty(value = "原片id-长度(40)")
    private String originalFilmId;

    @Excel(name = "采购单价(元)")
    @TableField(value = "reference_price")
    @ApiModelProperty(value = "采购单价-长度(0)")
    private Double referencePrice;

    @Excel(name = "采购金额(元)")
    @TableField(value = "purchase_amount")
    @ApiModelProperty(value = "采购金额-长度(0)")
    private Double purchaseAmount;

    @Excel(name = "采购数量")
    @TableField(value = "purchase_num")
    @ApiModelProperty(value = "采购数量-长度(0)")
    private Integer purchaseNum;

    @Excel(name = "采购面积(m²)")
    @TableField(value = "purchase_area")
    @ApiModelProperty(value = "采购面积-长度(0)")
    private Double purchaseArea;


}
