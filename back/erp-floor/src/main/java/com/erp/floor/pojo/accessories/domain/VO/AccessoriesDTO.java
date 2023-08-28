package com.erp.floor.pojo.accessories.domain.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 17:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "辅料信息传递类")
public class AccessoriesDTO {

    @Excel(name = "辅料类型")
    @TableField(exist = false)
    @ApiModelProperty(value = "辅料类型-长度-(20)")
    private String typeName;

    @TableField(exist = false)
    @ApiModelProperty(value = "辅料类型id-长度-(40)")
    private String typeId;

    @Excel(name = "规格型号")
    @TableField(exist = false)
    @ApiModelProperty(value = "规格型号-长度-(10)")
    private String accessoriesSpecifications;

    @Excel(name = "单位")
    @TableField(exist = false)
    @ApiModelProperty(value = "单位-长度-(20)")
    private String accessoriesCompany;

    @Excel(name = "厂家名称")
    @TableField(exist = false)
    @ApiModelProperty(value = "厂家名称-长度-(20)")
    private String accessoriesMill;

    @TableField(exist = false)
    @ApiModelProperty(value = "参考采购价-长度-(0)")
    private Double referencePrice;

    @TableField(exist = false)
    @ApiModelProperty(value = "预警库存-长度-(0)")
    private Integer alarmInventory;

    @TableField(exist = false)
    @ApiModelProperty(value = "备注-长度-(255)")
    private String remarks;
}
