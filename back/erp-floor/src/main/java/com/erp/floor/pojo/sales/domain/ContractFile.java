package com.erp.floor.pojo.sales.domain;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date： 2022-08-17 15:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "contract_file")
@ApiModel(value = "合同文件信息", description = "合同文件信息实体类")
public class ContractFile {

    @ApiModelProperty("文件ID(长度-255)")
    @TableField(value = "id")
    private String id;

    @ApiModelProperty("合同ID(长度-255)")
    @TableField(value = "contract_id")
    private String contractId;

    @ApiModelProperty("文件名称(长度-255)")
    @TableField(value = "file_name")
    private String fileName;

    @ApiModelProperty("文件大小(长度-0)")
    @TableField(value = "file_size")
    private String fileSize;

    @ApiModelProperty("文件地址(长度-255)")
    @TableField(value = "file_address")
    private String fileAddress;

    @ApiModelProperty("上传人(长度-255)")
    @TableField(value = "created_person")
    private String createdPerson;

    @ApiModelProperty("上传日期(长度-0)")
    @TableField(value = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

}
