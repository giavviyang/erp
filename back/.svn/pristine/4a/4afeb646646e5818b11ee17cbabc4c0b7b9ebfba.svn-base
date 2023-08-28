package com.erp.floor.pojo.sales.domain;

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
 * @date： 2022-08-08 13:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "order_enclosure")
@ApiModel(value = "订单附件", description = "订单附件实体类")
public class OrderEnclosure {

    @ApiModelProperty("主键")
    @TableId(value = "id" , type = IdType.UUID)
    private String id;

    @ApiModelProperty("订单id")
    @TableField(value = "order_id")
    private String orderId;

    @ApiModelProperty("文件名称")
    @TableField(value = "file_name")
    private String fileName;

    @ApiModelProperty("文件类型")
    @TableField(value = "file_type")
    private String fileType;

    @ApiModelProperty("文件大小")
    @TableField(value = "file_size")
    private String fileSize;

    @ApiModelProperty("文件地址")
    @TableField(value = "file_address")
    private String fileAddress;

    @ApiModelProperty("上传日期")
    @TableField(value = "upload_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadDate;

}
