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
 * @date： 2022-09-15 09:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "accessories_operation_detail")
@ApiModel(value = "辅料仓库明细", description = "辅料仓库明细实体类")
public class AccessoriesOperationDetail  extends AccessoriesDTO {

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

    @TableField(value = "accessories_id")
    @ApiModelProperty(value = "辅料id-(长度-40)")
    private String accessoriesId;

    @Excel(name = "辅料名称")
    @TableField(value = "accessories_name")
    @ApiModelProperty(value = "辅料名称-(长度-20)")
    private String accessoriesName;

    @Excel(name = "操作数量")
    @TableField(value = "operation_num")
    @ApiModelProperty(value = "操作数量-(长度-0)")
    private Integer operationNum;

    @Excel(name = "操作单价(元)")
    @TableField(value = "operation_price")
    @ApiModelProperty(value = "操作单价-(长度-0)")
    private Double operationPrice;


}
