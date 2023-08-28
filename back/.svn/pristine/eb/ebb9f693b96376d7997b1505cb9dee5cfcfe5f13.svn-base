package com.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.common.utils.SecurityUtils;
import com.erp.floor.mapper.sales.ShelfManageMapper;
import com.erp.floor.mapper.sales.ShelfRecordMapper;
import com.erp.floor.pojo.sales.domain.ShelfManage;
import com.erp.floor.pojo.sales.domain.ShelfRecord;
import com.erp.sales.service.ShelfManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-29 15:49
 */
@Service
public class ShelfManageServiceImpl extends ServiceImpl<ShelfManageMapper, ShelfManage> implements ShelfManageService {
    @Resource
    private ShelfManageMapper shelfManageMapper;
    @Resource
    private ShelfRecordMapper shelfRecordMapper;

    /**
     * @param frameNo   铁架编号
     * @param frameName 铁架名称
     */
    @Override
    public CommonResult<List<ShelfManage>> queryAll(String frameNo, String frameName, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<ShelfManage> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ShelfManage::getFrameNo, frameNo);
        wrapper.like(ShelfManage::getFrameName, frameName);
        wrapper.orderByDesc(ShelfManage::getCreatedAt);
        Integer integer = shelfManageMapper.selectCount(wrapper);
        PageHelper.startPage(pageNum, pageSize);
        List<ShelfManage> shelfManages = shelfManageMapper.selectList(wrapper);
        PageInfo<ShelfManage> pageInfo = new PageInfo<>(shelfManages, pageSize);
        return new CommonResult<List<ShelfManage>>(200, ResultConstants.QUERY_SUCCESS, integer, pageInfo.getList());
    }

    /**
     * 新增铁架
     *
     * @param shelfManage 铁架对象
     */
    @Override
    public CommonResult addShelf(ShelfManage shelfManage) {
        try {
            shelfManage.setId(UUID.randomUUID().toString());
            shelfManage.setCreatedAt(new Date());
            shelfManage.setCreatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            shelfManageMapper.insert(shelfManage);
            return CommonResult.success(ResultConstants.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultConstants.SAVE_ERROR);
        }
    }

    /**
     * 编辑铁架
     *
     * @param shelfManage 铁架对象
     */
    @Override
    public CommonResult updateShelf(ShelfManage shelfManage) {
        try {
            shelfManage.setUpdatedAt(new Date());
            shelfManage.setUpdatedPerson(SecurityUtils.getLoginUser().getUser().getNickName());
            shelfManageMapper.updateById(shelfManage);
            return CommonResult.success(ResultConstants.UPD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultConstants.UPD_ERROR);
        }
    }

    /**
     * 删除铁架
     *
     * @param ids 铁架id
     */
    @Override
    public CommonResult delShelf(String ids) {
        try {
            String[] split = ids.split(",");
            shelfManageMapper.delete(Wrappers.<ShelfManage>query().lambda().in(ShelfManage::getId, split));
            return CommonResult.success(ResultConstants.DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultConstants.DEL_ERROR);
        }
    }

    /**
     * 查看出入库记录
     *
     * @param id 铁架id
     */
    @Override
    public CommonResult<List<ShelfRecord>> queryShelfRecord(String id) {
        List<ShelfRecord> shelfRecords = shelfRecordMapper.selectList(Wrappers.<ShelfRecord>query().lambda().eq(ShelfRecord::getShelfId, id));
        return CommonResult.success(shelfRecords);
    }
}
