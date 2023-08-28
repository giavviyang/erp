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
 * @date： 2022-08-17 14:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "contract")
@ApiModel(value = "合同信息", description = "合同信息实体类")
public class Contract {

    @ApiModelProperty("合同id(长度-255)")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty("合同标题(长度-255)")
    @TableField(value = "contract_title")
    private String contractTitle;

    @ApiModelProperty("合同介绍(长度-255)")
    @TableField(value = "contract_introduce")
    private String contractIntroduce;

    @ApiModelProperty("合同负责人(长度-255)")
    @TableField(value = "contract_person")
    private String contractPerson;

    @ApiModelProperty("合同金额(长度-10)")
    @TableField(value = "contract_money")
    private String contractMoney;

    @ApiModelProperty("合同日期(长度-0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "contract_date")
    private Date contractDate;

    @ApiModelProperty("合同备注(长度-255)")
    @TableField(value = "contract_remarks")
    private String contractRemarks;

    @ApiModelProperty("创建人(长度-255)")
    @TableField(value = "created_person")
    private String createdPerson;

    @ApiModelProperty("创建时间(长度-0)")
    @TableField(value = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @ApiModelProperty("修改人(长度-255)")
    @TableField(value = "updated_person")
    private String updatedPerson;

    @ApiModelProperty("修改时间(长度-0)")
    @TableField(value = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
