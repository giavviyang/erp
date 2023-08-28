package com.erp.floor.pojo.original.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @date： 2022-09-14 10:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "yp_damage_detail")
@ApiModel(value = "原片报损明细", description = "原片报损明细实体类")
public class YpDamageDetail extends OriginalFilmDTO {

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

    @TableField(value = "original_film_id")
    @ApiModelProperty(value = "原片id-长度(40)")
    private String originalFilmId;

    @Excel(name = "报损数量")
    @TableField(value = "damage_num")
    @ApiModelProperty(value = "报损数量-长度(0)")
    private Integer damageNum;

    @Excel(name = "报损面积(m²)")
    @TableField(value = "damage_area")
    @ApiModelProperty(value = "报损面积-长度(0)")
    private Double damageArea;

    @Excel(name = "原片名称")
    @TableField(value = "original_film_name")
    @ApiModelProperty(value = "原片名称-长度(10)")
    private String originalFilmName;

}
