package com.erp.floor.pojo.original.domain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import com.erp.floor.pojo.original.domain.Vo.OriginalFilmDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-13 12:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "yp_operation_detail")
@ApiModel(value = "原片仓库记录明细", description = "原片仓库记录明细实体类")
public class YpOperationDetail extends OriginalFilmDTO {

    @TableField(value = "id")
    @ApiModelProperty(value = "id-(长度-40)")
    private String id;

    @TableField(value = "operation_id")
    @ApiModelProperty(value = "操作id-(长度-40)")
    private String operationId;

    @Excel(name = "操作编号")
    @TableField(value = "operation_no")
    @ApiModelProperty(value = "操作编号 （入库、出库、盘库）-(长度-20)")
    private String operationNo;

    @TableField(value = "purchase_id")
    @ApiModelProperty(value = "采购id-长度(40)")
    private String purchaseId;

    @Excel(name = "采购编号")
    @TableField(value = "purchase_no")
    @ApiModelProperty(value = "采购编号-长度(40)")
    private String purchaseNo;

    @TableField(value = "original_film_id")
    @ApiModelProperty(value = "原片id-(长度-40)")
    private String originalFilmId;

    @Excel(name = "原片名称")
    @TableField(value = "original_film_name")
    @ApiModelProperty(value = "原片名称-(长度-20)")
    private String originalFilmName;

    @Excel(name = "操作数量")
    @TableField(value = "operation_num")
    @ApiModelProperty(value = "操作数量-(长度-0)")
    private Integer operationNum;

    @Excel(name = "操作单价(元)")
    @TableField(value = "operation_price")
    @ApiModelProperty(value = "操作单价-(长度-0)")
    private Double operationPrice;

    @Excel(name = "操作面积(m²)")
    @TableField(value = "operation_area")
    @ApiModelProperty(value = "操作面积-(长度-0)")
    private Double operationArea;

}
