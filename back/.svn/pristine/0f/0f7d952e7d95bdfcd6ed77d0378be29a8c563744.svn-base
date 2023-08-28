package com.erp.floor.pojo.sales.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单表查询类
 *
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-21 17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "订单查询", description = "订单查询实体类")
public class OrderRecordVo {

    @ApiModelProperty("订单ID")
    private String id;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("自定义编号")
    private String customNo;

    @ApiModelProperty("分架状态")
    private Integer rackSplittingStatus;

    @ApiModelProperty("生产状态")
    private Integer productionStatus;

    @ApiModelProperty("打包状态")
    private Integer packagingStatus;

    @ApiModelProperty("发货状态")
    private Integer shipmentStatus;

    @ApiModelProperty("收款状态")
    private Integer collectionStatus;

    @ApiModelProperty("制单日期   开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String preparationDateStart;

    @ApiModelProperty("制单日期   结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String preparationDateEnd;

    @ApiModelProperty("制单人")
    private String preparer;

    @ApiModelProperty("订单类型")
    private String orderType;

    @ApiModelProperty("项目名称")
    private String entryName;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("联系人")
    private String contacts;

    @ApiModelProperty("联系电话")
    private String contactNumber;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页容量")
    private Integer pageSize;

    @ApiModelProperty("缓存数据（1为缓存数据）")
    private Integer isCache;

    @ApiModelProperty("软删除")
    private Integer isDel;

    @ApiModelProperty("产品名称")
    private String productName;
}
