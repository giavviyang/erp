package com.erp.floor.pojo.accessories.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @date： 2022-09-15 10:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "accessories_damage_detail")
@ApiModel(value = "辅料报损记录", description = "辅料报损记录实体类")
public class AccessoriesDamageDetail extends AccessoriesDTO {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "id-长度(40)")
    private String id;

    @TableField(value = "damage_id")
    @ApiModelProperty(value = "报损id-长度(40)")
    private String damageId;

    @Excel(name = "报损编号")
    @TableField(value = "damage_no")
    @ApiModelProperty(value = "报损编号-长度(20)")
    private String damageNo;

    @TableField(value = "accessories_id")
    @ApiModelProperty(value = "辅料id-长度(40)")
    private String accessoriesId;

    @Excel(name = "报损数量")
    @TableField(value = "damage_num")
    @ApiModelProperty(value = "报损数量-长度(0)")
    private Integer damageNum;

    @Excel(name = "辅料名称")
    @TableField(value = "accessories_name")
    @ApiModelProperty(value = "辅料名称-长度(10)")
    private String accessoriesName;

}
