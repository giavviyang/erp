package com.erp.common.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-05 15:50
 */

@ApiModel(value = "通用返回信息")
public class ResultConstants {

    @ApiModelProperty("新增成功")
    public static final String SAVE_SUCCESS = "新增成功";

    @ApiModelProperty("编辑成功")
    public static final String UPD_SUCCESS = "编辑成功";

    @ApiModelProperty("查询成功")
    public static final String QUERY_SUCCESS = "查询成功";

    @ApiModelProperty("删除成功")
    public static final String DEL_SUCCESS = "删除成功";

    @ApiModelProperty("审核成功")
    public static final String EXAMINE_SUCCESS = "审核成功";

    @ApiModelProperty("新增失败")
    public static final String SAVE_ERROR = "新增失败";

    @ApiModelProperty("编辑失败")
    public static final String UPD_ERROR = "编辑失败";

    @ApiModelProperty("查询失败")
    public static final String QUERY_ERROR = "查询失败";

    @ApiModelProperty("删除失败")
    public static final String DEL_ERROR = "删除失败";

    @ApiModelProperty("审核失败")
    public static final String EXAMINE_ERROR = "审核失败";

    @ApiModelProperty("上传成功")
    public static final String UPLOAD_SUCCESS = "上传成功";

    @ApiModelProperty("上传失败")
    public static final String UPLOAD_ERROR = "上传失败";

    @ApiModelProperty("文件丢失，删除失败，已将文件信息删除！")
    public static final String DELFILE_ERROR = "文件丢失，删除失败，已将文件信息删除！";

    @ApiModelProperty("下载成功")
    public static final String DOWNLOAD_SUCCESS = "下载成功";

    @ApiModelProperty("下载失败")
    public static final String DOWNLOAD_ERROR = "下载失败";

    @ApiModelProperty("保存成功")
    public static final String CONSERVE_SUCCESS = "保存成功";

    @ApiModelProperty("保存失败")
    public static final String CONSERVE_ERROR = "保存失败";

    @ApiModelProperty("请验证传入页码")
    public static final String PAGE_NUM = "请验证传入页码";

    @ApiModelProperty("请验证传入页容量")
    public static final String PAGE_SIZE = "请验证传入页容量";

    @ApiModelProperty("入库成功")
    public static final String RK_SUCCESS = "入库成功";

    @ApiModelProperty("入库失败")
    public static final String RK_ERROR = "入库失败";

}
