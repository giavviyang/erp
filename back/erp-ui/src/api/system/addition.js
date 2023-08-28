import request from '@/utils/request'

// 查询订单列表
export function listAddition(params) {
  return request({
    url: '/system/additional/list',
    method: 'GET',
    params
  })
}
// 查询订单列表
export function addAddition(data) {
  return request({
    url: '/system/additional/add',
    method: 'POST',
    data
  })
}
// 查询订单列表
export function editAddition(id,data) {
  return request({
    url: `/system/additional/edit/${id}`,
    method: 'PUT',
    data
  })
}
// 查询订单列表
export function delAddition(ids) {
  return request({
    url: `/system/additional/del/${ids}`,
    method: 'DELETE'
  })
}
