package com.erp.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.produce.domain.CompletionRecord;
import com.erp.floor.pojo.system.domain.SysAdditional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/8/29 14:45
 */
public interface CompletionService extends IService<CompletionRecord> {

    /**
     * 新增完工管理
     * @param record
     * @return
     */
    CommonResult insertCompletion(CompletionRecord record) throws Exception;

    /**
     * 编辑完工管理
     * @return
     */
    CommonResult editCompletion(String id,CompletionRecord record) throws Exception;

    /**
     * 编辑完工管理
     * @return
     */
    CommonResult delCompletion(List<String> ids) throws Exception;

    /**
     * 完工列表
     * @return
     */
    CommonResult getCompletionRecord(Map<String,Object> params);

    CommonResult getCompletionInfo(String id);

    /**
     * 获取工艺下未完工的流程卡明细
     * @param params
     * @return
     */
    CommonResult getFlowCardInfo(Map<String,Object> params);

    void exportCompletion(HttpServletResponse response, String ids);

    void flowCardCompare(List<String> ids,String craft);
    void orderCompare(List<String> ids,String craft);

    /**
     * 生产统计列表
     * @return
     */
    CommonResult getCountList(Map<String,String> data);

    /**
     * 获取产品生产详情
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
