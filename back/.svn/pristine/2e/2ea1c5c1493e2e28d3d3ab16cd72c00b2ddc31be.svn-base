package com.erp.original.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.original.domain.YpDamage;
import com.erp.floor.pojo.original.domain.YpDamageDetail;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-14 10:14
 */
public interface YpDamageService extends IService<YpDamage> {

    /**
     * 生成原片报损编号
     */
    String productionNumber();

    /**
     * 报损数据查询
     * @param ypDamage 对象
     */
    CommonResult<List<YpDamage>> queryDamageData(@RequestBody YpDamage ypDamage);

    /**
     * 查看明细
     * @param id 报损id
     */
    CommonResult<List<YpDamageDetail>> viewDetail(String id);

    /**
     * 新增报损
     * @param ypDamage 对象
     */
    CommonResult addDamageData(@RequestBody YpDamage ypDamage);

    /**
     * 编辑报损查询
     * @param id 报损id
     */
    CommonResult updDamageDataQuery(String id);

    /**
     * 编辑报损
     * @param ypDamage 对象
     */
    CommonResult updDamageData(@RequestBody YpDamage ypDamage);

    /**
     *  删除报损
     * @param ids 报损id
     */
    CommonResult delDamageData(String ids);

    /**
     * 导出报损记录
     * @param response
     * @param ids 报损id
     */
    void exportDamage(HttpServletResponse response, String ids);

    /**
     * 导出报损明细
     * @param response
     * @param ids 报损id
     */
    void exportDamageDetails(HttpServletResponse response, String ids);

}
