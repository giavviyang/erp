import request from '@/utils/request'

export function getMillTypeList() {
  return request({
    url: '/system/mill/type/list',
    method: 'GET'
  })
}

export function addMillType(data) {
  return request({
    url: '/system/mill/type/add',
    method: 'POST',
    data
  })
}

export function editMillType(id,data) {
  return request({
    url: `/system/mill/type/edit/${id}`,
    method: 'PUT',
    data
  })
}

export function delMillType(id) {
  return request({
    url: `/system/mill/type/del/${id}`,
    method: 'DELETE'
  })
}
export function getMillList(params) {
  return request({
    url: '/system/mill/list',
    method: 'GET',
    params
  })
}

export function addMill(data) {
  return request({
    url: '/system/mill/add',
    method: 'POST',
    data
  })
}

export function editMill(id,data) {
  return request({
    url: `/system/mill/edit/${id}`,
    method: 'PUT',
    data
  })
}

export function delMill(ids) {
  return request({
    url: `/system/mill/del/${ids}`,
    method: 'DELETE'
  })
}
