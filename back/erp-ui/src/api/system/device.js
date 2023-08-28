import request from '@/utils/request'

export function getDeviceList(query) {
  return request({
    url: '/system/device/list',
    method: 'GET',
    params: query
  })
}
export function addDevice(data) {
  return request({
    url: '/system/device/add',
    method: 'POST',
    data
  })
}
export function editDevice(id,data) {
  return request({
    url: `/system/device/edit/${id}`,
    method: 'PUT',
    data
  })
}
export function delDevice(id) {
  return request({
    url: `/system/device/del/${id}`,
    method: 'DELETE'
  })
}
