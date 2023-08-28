package com.erp.floor.pojo.sales.domain.vo;

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

/**流程卡查询Vo类
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-29 16:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "流程卡查询", description = "流程卡查询实体类")
public class FlowCardVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("流程卡编号")
    private String flowCardNo;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("订单自定义编号")
    private String customNo;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("单片名称")
    private String monolithicName;

    @ApiModelProperty("分架日期 - 开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String splitDateStart;

    @ApiModelProperty("分架日期 - 结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String splitDateEnd;

    @ApiModelProperty("分架人")
    private String splitPerson;

    @ApiModelProperty("生产状态(0未生产，1生产中，2完成生产)")
    private Integer productionStatus;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页容量")
    private Integer pageSize;
}
