import request from '@/utils/request'

export function getAllCraftList() {
  return request({
    url: '/system/craft/allList',
    method: 'GET'
  })
}
export function getCraftList(params) {
  return request({
    url: '/system/craft/list',
    method: 'GET',
    params
  })
}
export function addCraft(data) {
  return request({
    url: '/system/craft/add',
    method: 'POST',
    data
  })
}
export function editCraft(id,data) {
  return request({
    url: `/system/craft/edit/${id}`,
    method: 'PUT',
    data
  })
}
export function delCraft(id) {
  return request({
    url: `/system/craft/del/${id}`,
    method: 'DELETE'
  })
}
export function getCraftFlowList(params) {
  return request({
    url: '/system/craftflow/list',
    method: 'GET',
    params
  })
}
export function getAllCraftFlowList() {
  return request({
    url: '/system/craftflow/getAllList',
    method: 'GET'
  })
}
export function addCraftFlow(data) {
  return request({
    url: '/system/craftflow/add',
    method: 'POST',
    data
  })
}
export function editCraftFlow(id,data) {
  return request({
    url: `/system/craftflow/edit/${id}`,
    method: 'PUT',
    data
  })
}
export function delCraftFlow(id) {
  return request({
    url: `/system/craftflow/del/${id}`,
    method: 'DELETE'
  })
}
