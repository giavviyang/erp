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

import java.util.Date;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-05 11:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "deliver_receipt_file")
@ApiModel(value = "回执文件表", description = "回执文件表实体类")
public class DeliverReceiptFile {

    @ApiModelProperty(value = "主键-长度(40)")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "发货id-长度(40)")
    @TableField(value = "deliver_id")
    private String deliverId;

    @ApiModelProperty(value = "发货回执id-长度(40)")
    @TableField(value = "deliver_receipt_id")
    private String deliverReceiptId;

    @ApiModelProperty(value = "文件名称-长度(255)")
    @TableField(value = "file_name")
    private String fileName;

    @ApiModelProperty(value = "文件大小-长度(255)")
    @TableField(value = "file_size")
    private String fileSize;

    @ApiModelProperty(value = "文件地址-长度(255)")
    @TableField(value = "file_address")
    private String fileAddress;

    @ApiModelProperty(value = "上传人-长度(20)")
    @TableField(value = "created_person")
    private String createdPerson;

    @ApiModelProperty(value = "上传日期-长度(0)")
    @TableField(value = "created_at")
    private Date createdAt;

}
