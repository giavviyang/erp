package com.erp.floor.pojo.original.domain;

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
 * @date： 2022-09-14 09:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "yp_damage")
@ApiModel(value = "原片报损记录", description = "原片报损记录实体类")
public class YpDamage {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "报损id-(长度-40)")
    private String id;

    @Excel(name = "报损编号")
    @TableField(value = "damage_no")
    @ApiModelProperty(value = "报损编号-(长度-20)")
    private String damageNo;

    @Excel(name = "报损数量")
    @TableField(value = "damage_num")
    @ApiModelProperty(value = "报损数量-(长度-0)")
    private Integer damageNum;

    @Excel(name = "报损面积(m²)")
    @TableField(value = "damage_area")
    @ApiModelProperty(value = "报损面积-(长度-0)")
    private Double damageArea;

    @Excel(name = "报损金额(元)")
    @TableField(value = "damage_amount")
    @ApiModelProperty(value = "报损金额-(长度-0)")
    private Double damageAmount;

    @Excel(name = "报损人")
    @TableField(value = "person")
    @ApiModelProperty(value = "报损人-(长度-10)")
    private String person;

    @Excel(name = "报损时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "damage_date")
    @ApiModelProperty(value = "报损时间-(长度-0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date damageDate;

    @Excel(name = "报损原因")
    @TableField(value = "reason")
    @ApiModelProperty(value = "报损原因-(长度-255)")
    private String reason;

    @Excel(name = "备注")
    @TableField(value = "remarks")
    @ApiModelProperty(value = "备注-(长度-255)")
    private String remarks;

    @TableField(value = "created_person")
    @ApiModelProperty(value = "创建人-(长度-255)")
    private String createdPerson;

    @TableField(value = "created_at")
    @ApiModelProperty(value = "创建时间-(长度-0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty(value = "修改人-(长度-255)")
    private String updatedPerson;

    @TableField(value = "updated_at")
    @ApiModelProperty(value = "修改时间-(长度-0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /*—————————————————————查询数据—————————————————————*/
    @TableField(exist = false)
    @ApiModelProperty(value = "报损日期-开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String damageDateStart;

    @TableField(exist = false)
    @ApiModelProperty(value = "报损日期-结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String damageDateEnd;

    @TableField(exist = false)
    @ApiModelProperty(value = "页容量")
    private Integer pageSize;

    @TableField(exist = false)
    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    /*——————————————————新增信息————————————————————*/
    @ApiModelProperty(value = "入库明细")
    @TableField(exist = false)
    private List<OriginalFilm> originalFilmList;

}
