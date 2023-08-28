package com.erp.floor.pojo.accessories.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @date： 2022-09-14 16:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "accessories_type")
@ApiModel(value = "辅料类型", description = "辅料类型实体类")
public class AccessoriesType {

    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "类型id-长度(40)")
    private String id;

    @TableField(value = "accessories_type_name")
    @ApiModelProperty(value = "类型名称-长度(10)")
    private String accessoriesTypeName;

    @TableField(value = "tid")
    @ApiModelProperty(value = "父类型id-长度(10)")
    private String tid;

    @TableField(value = "created_person")
    @ApiModelProperty(value = "创建人-长度(10)")
    private String createdPerson;

    @TableField(value = "created_at")
    @ApiModelProperty(value = "创建时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @TableField(value = "updated_person")
    @ApiModelProperty(value = "修改人-长度(10)")
    private String updatedPerson;

    @TableField(value = "updated_at")
    @ApiModelProperty(value = "修改时间-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
