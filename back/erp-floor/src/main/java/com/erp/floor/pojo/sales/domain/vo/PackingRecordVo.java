package com.erp.floor.pojo.sales.domain.vo;

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
 * @date： 2022-08-24 20:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "打包记录", description = "打包记录查询实体类")
public class PackingRecordVo {

    @ApiModelProperty(value = "主键-长度(40)")
    private String id;

    @ApiModelProperty(value = "订单id-长度(40)")
    private String orderId;

    @ApiModelProperty(value = "订单编号-长度(40)")
    private String orderNo;

    @ApiModelProperty(value = "自定义编号-长度(40)")
    private String customNo;

    @ApiModelProperty(value = "打包编号-长度(40)")
    private String packagingNo;

    @ApiModelProperty(value = "打包名称-长度(20)")
    private String packagingName;

    @ApiModelProperty(value = "产品名称-长度(40)")
    private String productName;

    @ApiModelProperty(value = "客户名称-长度(40)")
    private String customerName;

    @ApiModelProperty(value = "项目名称-长度(40)")
    private String entryName;

    @ApiModelProperty(value = "打包负责人-长度(10)")
    private String responsiblePerson;

    @ApiModelProperty(value = "开始打包日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String packagingDateStart;

    @ApiModelProperty(value = "结束打包日期-长度(0)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String packagingDateEnd;

    @ApiModelProperty(value = "打包方式-长度(20)")
    private String packagingMode;

    @ApiModelProperty(value = "发货负责人-长度(10)")
    private String deliverPerson;

    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "页容量")
    private Integer pageSize;

}
