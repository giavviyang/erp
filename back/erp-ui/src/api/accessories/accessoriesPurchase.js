import request from '@/utils/request'
/* 生成采购编号 */
export function productionNumber() {
  return request({
    url: '/accessories/purchase/productionNumber',
    method: 'post'
  })
}
/* 查询采购单 */
export function queryPurchaseData(data) {
  return request({
    url: '/accessories/purchase/queryPurchaseData',
    method: 'post',
    data: data
  })
}

/* 查看明细 */
export function viewDetail(data) {
  return request({
    url: '/accessories/purchase/viewDetail',
    method: 'post',
    params: data
  })
}

/* 新增采购单 */
export function addPurchaseData(data) {
  return request({
    url: '/accessories/purchase/addPurchaseData',
    method: 'post',
    data: data
  })
}

/* 编辑采购单明细查询 */
export function updPurchaseDataQuery(data) {
  return request({
    url: '/accessories/purchase/updPurchaseDataQuery',
    method: 'post',
    params: data
  })
}

/* 编辑采购单 */
export function updPurchaseData(data) {
  return request({
    url: '/accessories/purchase/updPurchaseData',
    method: 'post',
    data: data
  })
}

/* 删除采购单 */
export function delPurchaseData(data) {
  return request({
    url: '/accessories/purchase/delPurchaseData',
    method: 'post',
    params: data
  })
}

/* 导出采购单 */
export function exportPurchase(data) {
  return request({
    url: '/accessories/purchase/exportPurchase',
    method: 'post',
    params: data
  })
}

/* 导出采购单明细 */
export function exportPurchaseData(data) {
  return request({
    url: '/accessories/purchase/exportPurchaseData',
    method: 'post',
    params: data
  })
}
