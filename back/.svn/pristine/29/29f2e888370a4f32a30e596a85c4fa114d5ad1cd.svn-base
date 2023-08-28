package com.erp.sales.controller;

import com.erp.common.constant.ResultConstants;
import com.erp.common.core.domain.CommonResult;
import com.erp.floor.mapper.produce.PatchRecordMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/index")
@Api(tags = "首页")
public class IndexController {

    @Resource
    private PatchRecordMapper patchRecordMapper;


    @ApiOperation("首页数据")
    @GetMapping("/data")
    public CommonResult getData() {
        //发货量
        String deliveringAmount = "SELECT IFNULL(SUM( IF( DATE_FORMAT( deliver_date, '%Y%m') = DATE_FORMAT( CURDATE(), '%Y%m' ), deliver_area, 0 )),0) AS today, IFNULL(SUM( IF ( DATE_FORMAT( deliver_date, '%Y%m' ) = DATE_FORMAT( CURDATE(), '%Y%m' ), deliver_cost, 0 )),0) AS todayPrice FROM deliver_record";
        List<Map<String, Object>> deliveringAmountData = patchRecordMapper.getCountList(deliveringAmount);
        //下单量
        String placeOrder = "SELECT IFNULL(SUM( IF( DATE_FORMAT( created_at, '%Y%m') = DATE_FORMAT( CURDATE(), '%Y%m' ), total_area, 0 )),0) AS today, IFNULL(SUM( IF ( DATE_FORMAT( created_at, '%Y%m' ) = DATE_FORMAT( CURDATE(), '%Y%m' ), order_amount, 0 )),0) AS todayPrice FROM order_record";
        List<Map<String, Object>> placeOrderData = patchRecordMapper.getCountList(placeOrder);
        //收款统计
        String collection = "SELECT IFNULL(SUM( IF( DATE_FORMAT( collection_date, '%Y%m') = DATE_FORMAT( CURDATE(), '%Y%m' ), collection_amount, 0 ) ),0) AS monthAmount, IFNULL(SUM( IF ( DATE_SUB( CURDATE(), INTERVAL 7 DAY ) <= date( collection_date ), collection_amount, 0 ) ),0) AS weekAmount FROM collection_record";
        List<Map<String, Object>> collectionData = patchRecordMapper.getCountList(collection);
        //成品量
        String finishedProduct = "SELECT IFNULL( SUM( IF( DATE_FORMAT( c.created_at, '%Y%m') = DATE_FORMAT( CURDATE(), '%Y%m' ), b.num * p.single_area, 0 )), 0 ) AS today FROM completion_record c LEFT JOIN completion_business b ON b.record_id = c.id LEFT JOIN order_product p ON p.id = b.product_id WHERE b.last_craft = 1";
        List<Map<String, Object>> finishedProductData = patchRecordMapper.getCountList(finishedProduct);
        //补片量 - 未补片
        String patchCount = "SELECT IFNULL(SUM(damage_num),0) as damage_num , IFNULL(SUM(patch_num),0) AS patch_num FROM damage_record WHERE damage_num ";
        List<Map<String, Object>> patchCountData = patchRecordMapper.getCountList(patchCount);
        //补片统计
        String patchListCount = "SELECT DISTINCT p.product_name,p.id,COALESCE(SUM(b.patch_num),0) patch_num,COALESCE(SUM(b.patch_area),0) patch_area FROM sys_semi_product s LEFT JOIN sys_product p ON s.product_id = p.id LEFT JOIN patch_business b ON b.semi_product_id = s.id LEFT JOIN patch_record r ON b.patch_id = r.id WHERE 1=1 GROUP BY p.product_name ORDER BY patch_num DESC ";
        List<Map<String, Object>> patchListCountData = patchRecordMapper.getCountList(patchListCount);
        //今日生产流程卡
        String currentFlowCard = "SELECT c.flow_card_no, o.entry_name, c.custom_no, c.monolithic_name, c.total_area, c.complete_process, c.product_name FROM completion_record r LEFT JOIN flow_card c on c.flow_card_no = r.flow_card_no LEFT JOIN order_record o on c.order_no = o.order_no WHERE DATEDIFF( r.created_at, NOW())=0 GROUP BY c.flow_card_no ORDER BY r.created_at DESC";
        List<Map<String, Object>> currentFlowCardData = patchRecordMapper.getCountList(currentFlowCard);
        Map<String,Object> result = new HashMap<>();
        result.put("deliveringAmountData",deliveringAmountData.get(0));
        result.put("placeOrderData",placeOrderData.get(0));
        result.put("collectionData",collectionData.get(0));
        result.put("finishedProductData",finishedProductData.get(0));
        result.put("patchCountData",patchCountData.get(0));
        result.put("patchListCountData",patchListCountData);
        result.put("currentFlowCardData",currentFlowCardData);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,result);
    }

    @ApiOperation("生产统计")
    @GetMapping("/product/{type}")
    public CommonResult getProduct(@PathVariable("type") String type){
        String sql = "";
        switch (type){
            case "currentWeek":
                sql = "SELECT a.timeDate, IFNULL(SUM( CASE WHEN 1 = 1 THEN ROUND(( d.total_area / d.split_num)* c.num, 2 ) ELSE 0 END ),0) area FROM( SELECT curdate() AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 1 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 2 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 3 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 4 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 5 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 6 DAY ) AS timeDate ) a LEFT JOIN ( SELECT id, DATE_FORMAT(completion_date,'%Y-%m-%d') AS datetime FROM completion_record GROUP BY DATE_FORMAT(completion_date,'%Y-%m-%d') ) b ON a.timeDate = b.datetime LEFT JOIN completion_business c ON c.record_id = b.id LEFT JOIN flow_card d ON d.flow_card_no = c.flow_card_no GROUP BY a.timeDate ORDER BY a.timeDate";
                break;
            case "currentMonth":
                sql = "SELECT a.timeDate, SUM( CASE WHEN 1 = 1 THEN IFNULL(ROUND(( d.total_area / d.split_num)* c.num, 2 ),0) ELSE 0 END ) area FROM( SELECT curdate() AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 1 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 2 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 3 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 4 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 5 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 7 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 8 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 9 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 10 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 11 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 12 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 13 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 14 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 15 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 16 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 17 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 18 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 19 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 20 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 21 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 22 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 23 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 24 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 25 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 26 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 27 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 28 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 29 DAY ) AS timeDate UNION ALL SELECT date_sub( curdate(), INTERVAL 30 DAY ) AS timeDate ) a LEFT JOIN ( SELECT id, date( completion_date ) AS datetime FROM completion_record GROUP BY date( completion_date ) ) b ON a.timeDate = b.datetime LEFT JOIN completion_business c ON c.record_id = b.id LEFT JOIN flow_card d ON d.flow_card_no = c.flow_card_no GROUP BY a.timeDate ORDER BY a.timeDate ";
                break;
            case "halfYear":
                sql = "SELECT a.timeDate, IFNULL(SUM( CASE WHEN 1 = 1 THEN ROUND(( d.total_area / d.split_num)* c.num, 2 ) ELSE 0 END ),0) area FROM( SELECT date_format( @lastDay := last_day( date_add( @lastDay, INTERVAL 1 MONTH ) ), '%Y-%m' ) timeDate FROM ( SELECT @lastDay := date_add( curdate( ), INTERVAL - 6 MONTH ) FROM mysql.help_topic LIMIT 6 ) a ORDER BY timeDate DESC ) a LEFT JOIN ( SELECT id, DATE_FORMAT(completion_date,'%Y-%m') AS datetime FROM completion_record GROUP BY date( completion_date ) ) b ON a.timeDate = b.datetime LEFT JOIN completion_business c ON c.record_id = b.id LEFT JOIN flow_card d ON d.flow_card_no = c.flow_card_no GROUP BY a.timeDate ORDER BY a.timeDate ";
                break;
            case "almostYear":
                sql = "SELECT a.timeDate, IFNULL(SUM( CASE WHEN 1 = 1 THEN ROUND(( d.total_area / d.split_num)* c.num, 2 ) ELSE 0 END ),0) area FROM( SELECT date_format( @lastDay := last_day( date_add( @lastDay, INTERVAL 1 MONTH ) ), '%Y-%m' ) timeDate FROM ( SELECT @lastDay := date_add( curdate( ), INTERVAL - 12 MONTH ) FROM mysql.help_topic LIMIT 12 ) a ORDER BY timeDate DESC ) a LEFT JOIN ( SELECT id, DATE_FORMAT(completion_date,'%Y-%m') AS datetime FROM completion_record GROUP BY date( completion_date ) ) b ON a.timeDate = b.datetime LEFT JOIN completion_business c ON c.record_id = b.id LEFT JOIN flow_card d ON d.flow_card_no = c.flow_card_no GROUP BY a.timeDate ORDER BY a.timeDate";
                break;
        }
        List<Map<String, Object>> result = patchRecordMapper.getCountList(sql);
        return CommonResult.success(ResultConstants.QUERY_SUCCESS,result);
    }
    @ApiOperation("订单统计")
    @GetMapping("/order/{type}")
    public CommonResult getOrder(@PathVariable("type") String type){
        String sql = "";
        switch (type){
            case "currentWeek":
                sql = "SELECT COUNT(*) as total, COUNT(CASE v.order_result WHEN 0 THEN 1 END) as reviewed, COUNT(CASE r.rack_splitting_status WHEN 2 THEN 1 END ) as split, COUNT(CASE r.production_status WHEN 2 THEN 1 END ) as produced, COUNT(CASE r.shipment_status WHEN 2 THEN 1 END ) as shipped, COUNT(CASE r.collection_status WHEN 2 THEN 1 END ) as received FROM order_record r LEFT JOIN order_review v on v.order_id = r.id WHERE DATE_SUB( CURDATE(), INTERVAL 7 DAY ) <= date( created_at ) AND r.is_del = 0 AND is_cache = 0";
                break;
            case "currentMonth":
                sql = "SELECT COUNT(*) as total, COUNT(CASE v.order_result WHEN 0 THEN 1 END) as reviewed, COUNT(CASE r.rack_splitting_status WHEN 2 THEN 1 END ) as split, COUNT(CASE r.production_status WHEN 2 THEN 1 END ) as produced, COUNT(CASE r.shipment_status WHEN 2 THEN 1 END ) as shipped, COUNT(CASE r.collection_status WHEN 2 THEN 1 END ) as received FROM order_record r LEFT JOIN order_review v on v.order_id = r.id WHERE DATE_SUB( CURDATE(), INTERVAL 30 DAY ) <= date( created_at ) AND r.is_del = 0 AND is_cache = 0";
                break;
            case "halfYear":
                sql = "SELECT COUNT(*) as total, COUNT(CASE v.order_result WHEN 0 THEN 1 END) as reviewed, COUNT(CASE r.rack_splitting_status WHEN 2 THEN 1 END ) as split, COUNT(CASE r.production_status WHEN 2 THEN 1 END ) as produced, COUNT(CASE r.shipment_status WHEN 2 THEN 1 END ) as shipped, COUNT(CASE r.collection_status WHEN 2 THEN 1 END ) as received FROM order_record r LEFT JOIN order_review v on v.order_id = r.id WHERE created_at between date_sub(now(),interval 6 month) and now() AND r.is_del = 0 AND is_cache = 0";
                break;
            case "almostYear":
                sql = "SELECT COUNT(*) as total, COUNT(CASE v.order_result WHEN 0 THEN 1 END) as reviewed, COUNT(CASE r.rack_splitting_status WHEN 2 THEN 1 END ) as split, COUNT(CASE r.production_status WHEN 2 THEN 1 END ) as produced, COUNT(CASE r.shipment_status WHEN 2 THEN 1 END ) as shipped, COUNT(CASE r.collection_status WHEN 2 THEN 1 END ) as received FROM order_record r LEFT JOIN order_review v on v.order_id = r.id WHERE created_at between date_sub(now(),interval 12 month) and now() AND r.is_del = 0 AND is_cache = 0";
                break;
        }
        List<Map<String, Object>> data = patchRecordMapper.getCountList(sql);
        String totalSql = "SELECT COUNT(*) as total FROM order_record";
        List<Map<String, Object>> total = patchRecordMapper.getCountList(totalSql);
        CommonResult result = CommonResult.success(ResultConstants.QUERY_SUCCESS,data.get(0));
        result.setCount(Integer.parseInt(String.valueOf(total.get(0).get("total"))));
        return result;
    }
}
