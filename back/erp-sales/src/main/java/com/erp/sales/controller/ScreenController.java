package com.erp.sales.controller;

import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.mapper.produce.PatchRecordMapper;
import com.erp.floor.pojo.sales.domain.ShelfManage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/11/13 14:08
 */
@RestController
@RequestMapping("/screen")
@Api(tags = "可视化大屏")
public class ScreenController {

    @Resource
    private PatchRecordMapper patchRecordMapper;

    @ApiOperation("大屏数据")
    @GetMapping("/data")
    public CommonResult getData() {
        //发货量
        String deliveringAmount = "SELECT SUM( CASE WHEN DATE_FORMAT( deliver_date, '%Y%m') = DATE_FORMAT( CURDATE( ), '%Y%m' ) THEN deliver_area ElSE 0 END ) AS currentMonth, SUM( CASE WHEN PERIOD_DIFF( date_format( now( ), '%Y%m' ), date_format( deliver_date, '%Y%m' ) ) =1 THEN deliver_area ElSE 0 END ) AS topMonth FROM deliver_record ";
        List<Map<String, Object>> deliveringAmountData = patchRecordMapper.getCountList(deliveringAmount);
        //破片量
        String fragmentQuantity = "SELECT SUM( CASE WHEN DATE_FORMAT( created_at, '%Y%m') = DATE_FORMAT( CURDATE( ), '%Y%m' ) THEN damage_area ElSE 0 END ) AS currentMonth, SUM( CASE WHEN PERIOD_DIFF( date_format( now( ), '%Y%m' ), date_format( created_at, '%Y%m' ) ) =1 THEN damage_area ElSE 0 END ) AS topMonth FROM damage_record ";
        List<Map<String, Object>> fragmentQuantityData = patchRecordMapper.getCountList(fragmentQuantity);
        //补片量
        String patchQuantity = "SELECT SUM( CASE WHEN DATE_FORMAT( created_at, '%Y%m') = DATE_FORMAT( CURDATE( ), '%Y%m' ) THEN patch_area ElSE 0 END ) AS currentMonth, SUM( CASE WHEN PERIOD_DIFF( date_format( now( ), '%Y%m' ), date_format( created_at, '%Y%m' ) ) = 1 THEN patch_area ElSE 0 END ) AS topMonth FROM patch_record ";
        List<Map<String, Object>> patchQuantityData = patchRecordMapper.getCountList(patchQuantity);
        //本周生产量
        String weekOutput = "SELECT a.click_date, SUM( CASE WHEN c.last_craft = 1 THEN ROUND(( d.total_area / d.split_num)* c.num, 2 ) ELSE 0 END ) area FROM( SELECT curdate() AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 1 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 2 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 3 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 4 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 5 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 6 DAY ) AS click_date ) a LEFT JOIN ( SELECT id, date( completion_date ) AS datetime FROM completion_record GROUP BY date( completion_date ) ) b ON a.click_date = b.datetime LEFT JOIN completion_business c ON c.record_id = b.id LEFT JOIN flow_card d ON d.flow_card_no = c.flow_card_no GROUP BY a.click_date ORDER BY a.click_date";
        List<Map<String, Object>> weekOutputData = patchRecordMapper.getCountList(weekOutput);
        //本月工艺产量对比
        String monthProcessComparison = "SELECT s.craft_name, SUM( CASE WHEN DATE_FORMAT( r.completion_date, '%Y%m') = DATE_FORMAT( CURDATE( ), '%Y%m' ) THEN(b.total_area/b.item_num)*c.num ELSE 0 END ) AS area FROM sys_craft s LEFT JOIN completion_business c ON s.craft_name = c.craft LEFT JOIN completion_record r on c.record_id = r.id LEFT JOIN shelf_division_business b on b.product_id = c.product_id GROUP BY s.craft_name ORDER BY area DESC";
        List<Map<String, Object>> monthProcessComparisonData = patchRecordMapper.getCountList(monthProcessComparison);
        //近7天排产
        String weekScheduling = "SELECT a.click_date, SUM( CASE WHEN c.last_craft = 1 THEN ROUND(( d.total_area / d.split_num)* c.num, 2 ) ELSE 0 END ) area FROM( SELECT curdate() AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 1 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 2 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 3 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 4 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 5 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 6 DAY ) AS click_date ) a LEFT JOIN ( SELECT id, date( completion_date ) AS datetime FROM completion_record GROUP BY date( completion_date ) ) b ON a.click_date = b.datetime LEFT JOIN completion_business c ON c.record_id = b.id LEFT JOIN flow_card d ON d.flow_card_no = c.flow_card_no GROUP BY a.click_date ORDER BY a.click_date";
        List<Map<String, Object>> weekSchedulingData = patchRecordMapper.getCountList(weekScheduling);
        //近7天报损
        String weekDamage = "SELECT a.click_date, SUM( CASE WHEN c.last_craft = 1 THEN ROUND(( d.total_area / d.split_num)* c.num, 2 ) ELSE 0 END ) area FROM( SELECT curdate() AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 1 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 2 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 3 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 4 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 5 DAY ) AS click_date UNION ALL SELECT date_sub( curdate(), INTERVAL 6 DAY ) AS click_date ) a LEFT JOIN ( SELECT id, date( completion_date ) AS datetime FROM completion_record GROUP BY date( completion_date ) ) b ON a.click_date = b.datetime LEFT JOIN completion_business c ON c.record_id = b.id LEFT JOIN flow_card d ON d.flow_card_no = c.flow_card_no GROUP BY a.click_date ORDER BY a.click_date";
        List<Map<String, Object>> weekDamageData = patchRecordMapper.getCountList(weekDamage);
        //今日生产流程卡
        String currentFlowCard = "SELECT c.flow_card_no, o.entry_name, c.custom_no, c.monolithic_name, c.total_area, c.complete_process, c.product_name FROM completion_record r LEFT JOIN flow_card c on c.flow_card_no = r.flow_card_no LEFT JOIN order_record o on c.order_no = o.order_no WHERE DATEDIFF( r.created_at, NOW())=0 GROUP BY c.flow_card_no ORDER BY r.created_at DESC";
        List<Map<String, Object>> currentFlowCardData = patchRecordMapper.getCountList(currentFlowCard);
        //本月订单总分架量,总完工量，总分架量，本月完工面积
        String monthOrder = "SELECT SUM(p.completion_num) AS completion_num, SUM(o.order_num) AS all_num, SUM( CASE WHEN DATE_FORMAT( r.completion_date, '%Y%m') = DATE_FORMAT( CURDATE( ), '%Y%m' ) THEN(p.single_area * p.completion_num) END ) AS completion_area, SUM(p.shelf_num) AS shelf_num FROM completion_record r LEFT JOIN completion_business b on b.record_id = r.id LEFT JOIN order_product p on p.id = b.product_id LEFT JOIN order_record o on o.id = p.order_id LEFT JOIN shelf_division_business d on d.product_id = p.id LEFT JOIN flow_card f ON f.flow_card_no = d.flow_card_no";
        List<Map<String, Object>> monthOrderData = patchRecordMapper.getCountList(monthOrder);
        //本月原片报损面积
        String monthOriginalFilmLoss = "SELECT SUM( CASE WHEN DATE_FORMAT( damage_date, '%Y%m') = DATE_FORMAT( CURDATE( ), '%Y%m' ) THEN damage_area END ) AS square FROM yp_damage";
        List<Map<String, Object>> monthOriginalFilmLossData = patchRecordMapper.getCountList(monthOriginalFilmLoss);
        Map<String,Object> result = new HashMap<>();
        result.put("deliveringAmountData",deliveringAmountData.get(0));
        result.put("fragmentQuantityData",fragmentQuantityData.get(0));
        result.put("patchQuantityData",patchQuantityData.get(0));
        result.put("weekOutputData",weekOutputData);
        result.put("monthProcessComparisonData",monthProcessComparisonData);
        result.put("weekSchedulingData",weekSchedulingData);
        result.put("weekDamageData",weekDamageData);
        result.put("currentFlowCardData",currentFlowCardData);
        result.put("monthOrderData",monthOrderData.get(0));
        result.put("monthOriginalFilmLossData",monthOriginalFilmLossData.get(0));
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,result);
    }
}
