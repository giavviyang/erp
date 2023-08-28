package com.erp.sales.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.pojo.sales.domain.ShelfManage;
import com.erp.floor.pojo.sales.domain.ShelfRecord;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 15:49
 */
public interface ShelfManageService extends IService<ShelfManage> {

    /**
     *
     * @param frameNo 铁架编号
     * @param frameName 铁架名称
     */
    CommonResult<List<ShelfManage>> queryAll(String frameNo, String frameName, Integer pageNum, Integer pageSize);

    /**
     * 新增铁架
     * @param shelfManage 铁架对象
     */
    CommonResult addShelf(@RequestBody ShelfManage shelfManage);

    /**
     * 编辑铁架
     * @param shelfManage 铁架对象
     */
    CommonResult updateShelf(@RequestBody ShelfManage shelfManage);

    /**
     * 删除铁架
     * @param ids 铁架id
     */
    CommonResult delShelf(String ids);

    /**
     * 查看出入库记录
     * @param id 铁架id
     */
    CommonResult<List<ShelfRecord>> queryShelfRecord(String id);
}
