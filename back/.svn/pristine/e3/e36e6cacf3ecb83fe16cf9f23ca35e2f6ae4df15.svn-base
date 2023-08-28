import request from '@/utils/request'

// 订单状态校验
export function getCheckOrder(data) {
  return request({
    url: '/sales/order/checkOrder',
    method: 'post',
    params: data
  })
}

// 查询订单列表
export function listOrder(data) {
  return request({
    url: '/sales/order/list',
    method: 'post',
    data: data
  })
}

// 查询订单明细  产品信息
export function listProduct(data) {
  return request({
    url: '/sales/order/queryProduct',
    method: 'post',
    data: data
  })
}


// 查询订单明细  产品信息
export function listProcess(data) {
  return request({
    url: '/sales/orderProduct/queryProcess',
    method: 'post',
    params: data
  })
}

// 新增订单后自动生成订单编号
export function orderNumber() {
  return request({
    url: '/sales/order/productionNumber',
    method: 'post',
  })
}

// 新增订单
export function addRecord(data) {
  return request({
    url: '/sales/order/add',
    method: 'post',
    data: data
  })
}

// 新增订单缓存
export function addRecordCache(data) {
  return request({
    url: '/sales/order/addCache',
    method: 'post',
    data: data
  })
}

// 废置订单
export function delOrder(data) {
  return request({
    url: '/sales/order/del',
    method: 'post',
    params: data
  })
}

// 废置订单
export function undoAudit(data) {
  return request({
    url: '/sales/order/undoAudit',
    method: 'post',
    params: data
  })
}

// 新增订单缓存
export function update(data) {
  return request({
    url: '/sales/order/update',
    method: 'post',
    data: data
  })
}

// 产品审核（尺寸审核  与  工艺审核）
export function auditSize(data) {
  return request({
    url: '/sales/order/auditSize',
    method: 'post',
    params: data
  })
}

// 订单审核
export function auditOrder(data) {
  return request({
    url: '/sales/order/auditOrder',
    method: 'post',
    params: data
  })
}

// 导出任务单
export function exportTask(orderId) {
  return request({
    url: `/sales/order/exportTask`,
    method: 'post',
    params:orderId,
    handleResponse: true,
    responseType: 'blob'
  })
}

// 删除订单
export function reallyDelOrder(data) {
  return request({
    url: '/sales/order/delOrder',
    method: 'post',
    params: data
  })
}

// 还原订单
export function reductionOrder(data) {
  return request({
    url: '/sales/order/reductionOrder',
    method: 'post',
    params: data
  })
}

// 删除流程卡
export function delFlow(data) {
  return request({
    url: '/sales/order/delFlow',
    method: 'post',
    params: data
  })
}

// 导出下载模板
export function downloadTemplate() {
  return request({
    url: `/sales/order/downloadTemplate`,
    method: 'post',
    handleResponse: true,
    responseType: 'blob'
  })
}

// 上传模板
export function importTemplate(data) {
  return request({
    url: '/sales/order/importTemplate',
    method: 'post',
    data: data
  })
}
