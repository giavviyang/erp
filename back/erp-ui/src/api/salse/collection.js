import request from '@/utils/request'

// 查询订单
export function orderList(data) {
  return request({
    url: '/sales/collection/orderList',
    method: 'post',
    data: data
  })
}
// 查询收款列表
export function list(data) {
  return request({
    url: '/sales/collection/list',
    method: 'post',
    params: data
  })
}
// 新增
export function add(data) {
  return request({
    url: '/sales/collection/add',
    method: 'post',
    data: data
  })
}
// 编辑
export function edit(data) {
  return request({
    url: '/sales/collection/edit',
    method: 'post',
    data: data
  })
}
// 删除
export function del(data) {
  return request({
    url: '/sales/collection/del',
    method: 'post',
    data: data
  })
}
