import request from '@/utils/request'

// 查询订单列表
export function queryDeliver(data) {
  return request({
    url: '/sales/deliver/queryDeliver',
    method: 'post',
    data: data
  })
}

// 获取发货编号
export function productionNumber() {
  return request({
    url: '/sales/deliver/productionNumber',
    method: 'post',
  })
}


// 订单获取
export function obtainOrderProduct(data) {
  return request({
    url: '/sales/deliver/obtainOrderProduct',
    method: 'post',
    data: data
  })
}

// 打包获取
export function obtainPackProduct(data) {
  return request({
    url: '/sales/deliver/obtainPackProduct',
    method: 'post',
    data: data
  })
}

//新增发货
export function saveDeliver(data) {
  return request({
    url: '/sales/deliver/saveDeliver',
    method: 'post',
    data: data
  })
}

//查看明细
export function detailsView(data) {
  return request({
    url: '/sales/deliver/detailsView',
    method: 'post',
    params: data
  })
}

//编辑发货
export function updateDeliver(data) {
  return request({
    url: '/sales/deliver/updateDeliver',
    method: 'post',
    data: data
  })
}

//废置发货
export function delDeliver(data) {
  return request({
    url: '/sales/deliver/delDeliver',
    method: 'post',
    params: data
  })
}

//审核
export function reviewDeliver(data) {
  return request({
    url: '/sales/deliver/reviewDeliver',
    method: 'post',
    params: data
  })
}

//消审
export function cancelReview(data) {
  return request({
    url: '/sales/deliver/cancelReview',
    method: 'post',
    params: data
  })
}

//订单管理-查询发货进度
export function queryDeliverProgress(data) {
  return request({
    url: '/sales/deliver/queryDeliverProgress',
    method: 'post',
    params: data
  })
}

//订单管理-查询发货明细
export function queryDeliverBus(data) {
  return request({
    url: '/sales/deliver/queryDeliverBus',
    method: 'post',
    params: data
  })
}

//回执信息查询
export function queryReceiptData(data) {
  return request({
    url: '/sales/deliver/queryReceiptData',
    method: 'post',
    params:data
  })
}

//回执文件查询
export function queryReceipt(data) {
  return request({
    url: '/sales/deliver/queryReceipt',
    method: 'post',
    params:data
  })
}

//回执货架查询
export function queryDeliverShelf(data) {
  return request({
    url: '/sales/deliver/queryDeliverShelf',
    method: 'post',
    params:data
  })
}

//新增回执
export function receiptShelf(data) {
  return request({
    url: '/sales/deliver/receiptShelf',
    method: 'post',
    data:data
  })
}

//回执文件上传
export function uploadReceipt(data) {
  return request({
    url: '/sales/deliver/uploadReceipt',
    method: 'post',
    data: data
  })
}

//回执文件下载
export function downloadReceiptFile(data) {
  return request({
    url: '/sales/deliver/downloadReceiptFile',
    method: 'post',
    params: data
  })
}

//回执文件删除
export function delReceiptFile(data) {
  return request({
    url: '/sales/deliver/delReceiptFile',
    method: 'post',
    params: data
  })
}



