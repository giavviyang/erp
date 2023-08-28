import request from "@/utils/request";

//queryWarehouseOrder
export function queryWarehouseOrder(data){
  return request({
    url:"/order/warehouse/queryWarehouseOrder",
    method:"POST",
    data:data
  })
}

//查询订单产品
export function queryOrderProduct(data){
  return request({
    url:"/order/warehouse/queryOrderProduct",
    method:"POST",
    params:data
  })
}

//查询未入库完成订单产品
export function queryProduct(data){
  return request({
    url:"/order/warehouse/queryProduct",
    method:"POST",
    data:data
  })
}

/* 生成采购编号 */
export function productionNumber(data) {
  return request({
    url: '/order/warehouse/productionNumber',
    method: 'post',
    params: data
  })
}

/* 操作入库 */
export function productWarehouse(data) {
  return request({
    url: '/order/warehouse/productWarehouse',
    method: 'post',
    data: data
  })
}

/* 查询操作记录 */
export function queryWarehouseData(data) {
  return request({
    url: '/order/warehouse/queryWarehouseData',
    method: 'post',
    data: data
  })
}

/* 查询操作记录明细 */
export function warehouseDeliverData(data) {
  return request({
    url: '/order/warehouse/warehouseDeliverData',
    method: 'post',
    params: data
  })
}
