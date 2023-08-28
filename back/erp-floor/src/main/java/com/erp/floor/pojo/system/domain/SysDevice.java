package com.erp.floor.pojo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/1 14:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_device")
@ApiModel(value = "设备", description = "设备实体")
public class SysDevice {

    @TableId(value = "id",type = IdType.UUID)
    @ApiModelProperty("设备ID-(长度255)")
    private String id;

    @TableField(value = "no")
    @ApiModelProperty("设备编号-(长度255)")
    private String no;

    @TableField(value = "device_name")
    @ApiModelProperty("设备名称-(长度255)")
    private String deviceName;

    @TableField(value = "simple_name")
    @ApiModelProperty("设备简称-(长度255)")
    private String simpleName;

    @TableField(value = "brand")
    @ApiModelProperty("设备品牌-(长度255)")
    private String brand;

    @TableField(value = "manufactor")
    @ApiModelProperty("厂家-(长度255)")
    private String manufactor;

    @TableField(value = "action_process")
    @ApiModelProperty("作用工艺-(长度255)")
    private String actionProcess;

    @TableField(value = "total_power")
    @ApiModelProperty("总功率-(长度255)")
    private String totalPower;

    @TableField(value = "weight")
    @ApiModelProperty("重量-(长度255)")
    private String weight;

    @TableField(value = "warranty_period")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("保修时间")
    private Date warrantyPeriod;

    @TableField(value = "delivery_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("出厂时间")
    private Date deliveryDate;

    @TableField(value = "status")
    @ApiModelProperty("状态-(长度255)")
    private Integer status;

    @TableField(value = "remarks")
    @ApiModelProperty("备注-(长度255)")
    private String remarks;

    @TableField(value = "created_person",fill = FieldFill.INSERT)
    @ApiModelProperty("创建人-(长度255)")
    private String createdPerson;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建日期")
    private Date createdAt;

    @TableField(value = "updated_person",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改人-(长度255)")
    private String updatedPerson;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改日期")
    private Date updatedAt;
}
