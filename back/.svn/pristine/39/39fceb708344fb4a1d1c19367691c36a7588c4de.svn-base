package com.erp.accessories.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.accessories.domain.Accessories;
import com.erp.floor.pojo.accessories.domain.AccessoriesDamage;
import com.erp.floor.pojo.accessories.domain.AccessoriesDamageDetail;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-09-15 10:58
 */
public interface AccessoriesDamageService extends IService<AccessoriesDamage> {


    /**
     * 生成辅料报损编号
     */
    String productionNumber();

    /**
     * 报损数据查询
     * @param accessoriesDamage 对象
     */
    CommonResult<List<AccessoriesDamage>> queryDamageData(@RequestBody AccessoriesDamage accessoriesDamage);

    /**
     * 查看明细
     * @param id 报损id
     */
    CommonResult<List<AccessoriesDamageDetail>> viewDetail(String id);

    /**
     * 新增报损
     * @param accessoriesDamage 对象
     */
    CommonResult addDamageData(@RequestBody AccessoriesDamage accessoriesDamage);

    /**
     *
     * @param id 报损id
     */
    CommonResult<List<Accessories>> updDamageDataQuery(String id);

    /**
     * 编辑报损
     * @param accessoriesDamage 对象
     */
    CommonResult updDamageData(@RequestBody AccessoriesDamage accessoriesDamage);

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
