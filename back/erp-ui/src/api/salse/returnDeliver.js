import request from '@/utils/request'

// 查询订单列表
export function queryData(data) {
  return request({
    url: '/sales/return/queryData',
    method: 'post',
    data: data
  })
}

// 获取发货编号
export function productionNumber() {
  return request({
    url: '/sales/return/productionNumber',
    method: 'post',
  })
}

// 查询发货信息
export function queryDeliverData(data) {
  return request({
    url: '/sales/return/queryDeliverData',
    method: 'post',
    data: data
  })
}

// 新增发货
export function addReturnData(data) {
  return request({
    url: '/sales/return/addReturnData',
    method: 'post',
    data: data
  })
}

//查看明细
export function reviewDetailed(data) {
  return request({
    url: '/sales/return/reviewDetailed',
    method: 'post',
    params: data
  })
}

// 编辑发货
export function updateReturnData(data) {
  return request({
    url: '/sales/return/updateReturnData',
    method: 'post',
    data: data
  })
}

// 删除发货
export function delReturnData(data) {
  return request({
    url: '/sales/return/delReturnData',
    method: 'post',
    params: data
  })
}

//审核
export function examineReturnData(data) {
  return request({
    url: '/sales/return/examineReturnData',
    method: 'post',
    params: data
  })
}

//消审
export function cancelExamine(data) {
  return request({
    url: '/sales/return/cancelExamine',
    method: 'post',
    params: data
  })
}
