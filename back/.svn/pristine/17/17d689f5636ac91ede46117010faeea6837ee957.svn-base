package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.PackingBusiness;
import com.erp.floor.pojo.sales.domain.PackingRecord;
import com.erp.floor.pojo.sales.domain.vo.PackingRecordVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-24 20:41
 */
public interface PackingRecordService extends IService<PackingRecord> {
    /**
     * 自动生成编号
     *
     * @return 编号
     */
    String productionNumber();

    /**
     * 查询打包信息
     *
     * @param packingRecordVo 查询实体类
     */
    CommonResult<List<PackingRecord>> queryPackData(PackingRecordVo packingRecordVo);

    /**
     * 新增打包
     *
     * @param packingRecord 打包信息
     */
    CommonResult addPack(PackingRecord packingRecord);

    /**
     * 查看明细
     *
     * @param id 打包id
     */
    CommonResult<List<PackingBusiness>> detailsView(String id);

    /**
     * 编辑打包-查询明细
     *
     * @param id 打包id
     */
    CommonResult<List<PackingBusiness>> updateDetails(String id);

    /**
     * 编辑打包 —— 保存
     *
     * @param packingRecord 打包对象
     */
    CommonResult updatePack(PackingRecord packingRecord);

    /**
     * 删除打包
     * @param id 打包id
     */
    CommonResult delPack(String id);

    /**
     * 导出打包信息
     * @param ids 打包id
     */
    void exportPack(HttpServletResponse response, String ids);

    /**
     * 导出打包明细
     * @param ids 打包id
     */
    void exportPackDetailed(HttpServletResponse response, String ids);

    /**
     * 订单管理-查询打包记录
     * @param id 订单id
     */
    CommonResult<List<PackingBusiness>> queryPack(String id);

}
