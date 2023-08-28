package com.erp.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.produce.domain.PatchBusiness;
import com.erp.floor.pojo.produce.domain.PatchRecord;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/9/14 14:53
 */
public interface PatchService extends IService<PatchRecord> {

    /**
     * 新增补片
     * @param patchBusinesses
     * @return
     */
    CommonResult addPatch(List<PatchBusiness> patchBusinesses);

    /**
     * 获取报损补片单
     * @param data
     * @return
     */
    CommonResult getDamageInfo(Map<String,String> data);

    /**
     * 获取补片单列表
     * @param
     * @return
     */
    CommonResult getPatchList(Map<String,String> params);

    /**
     * 获取补片详情
     * @param id
     * @return
     */
    CommonResult getPatchInfo(String id);

    /**
     * 作废补片
     * @param id
     * @return
     */
    CommonResult delPatch(String id);

    /**
     * 补片统计列表
     * @return
     */
    CommonResult getCountList(Map<String,String> data);

    /**
     * 获取产品补片详情
     * @param id
     * @return
     */
    CommonResult getCountInfo(String id);

    /**
     * 导出
     * @param response
     * @param ids
     */
    void exportCount(HttpServletResponse response, String ids);
}
