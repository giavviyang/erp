package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-21 16:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "cp_product_record")
@ApiModel(value = "成品操作记录明细", description = "成品操作记录明细实体类")
public class CpProductRecord extends OrderProductDTO {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @TableField(value = "cp_record_id")
    @ApiModelProperty(value = "成品操作记录id-长度(40)")
    private String cpRecordId;

    @TableField(value = "order_product_id")
    @ApiModelProperty(value = "产品id-长度(0)")
    private String orderProductId;

    @TableField(value = "product_number")
    @ApiModelProperty(value = "操作数量-长度(0)")
    private Integer productNumber;

    @TableField(value = "product_area")
    @ApiModelProperty(value = "操作面积-长度(0)")
    private Double productArea;

    @TableField(value = "product_weight")
    @ApiModelProperty(value = "操作重量-长度(0)")
    private Double productWeight;

    @ApiModelProperty("仓库位置")
    @TableField(value = "warehouse_location")
    private String warehouseLocation;

    @ApiModelProperty("货架位置")
    @TableField(value = "shelf_location")
    private String shelfLocation;


}
