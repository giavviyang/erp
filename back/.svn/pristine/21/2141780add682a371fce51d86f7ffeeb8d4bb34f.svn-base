import request from '@/utils/request'

export function getPrintTemplate(id,data) {
  return request({
    url: `/system/template/getInfo/${id}`,
    method: 'POST',
    data
  })
}
export function getTemplateTypeList() {
  return request({
    url: '/system/template/type/list',
    method: 'GET'
  })
}

export function addTemplateType(data) {
  return request({
    url: '/system/template/type/add',
    method: 'POST',
    data
  })
}
export function editTemplateType(id,data) {
  return request({
    url: `/system/template/type/edit/${id}`,
    method: 'PUT',
    data
  })
}
export function delTemplateType(id) {
  return request({
    url: `/system/template/type/del/${id}`,
    method: 'DELETE'
  })
}
export function getTemplateList(params) {
  return request({
    url: '/system/template/list',
    method: 'GET',
    params
  })
}

export function addTemplate(data) {
  return request({
    url: '/system/template/add',
    method: 'POST',
    data
  })
}

export function getTemplate(id) {
  return request({
    url: `/system/template/detailed/${id}`,
    method: 'GET'
  })
}
export function editTemplate(id,data) {
  return request({
    url: `/system/template/edit/${id}`,
    method: 'PUT',
    data
  })
}
export function delTemplate(ids) {
  return request({
    url: `/system/template/del/${ids}`,
    method: 'DELETE'
  })
}
export function downloadFile(id) {
  return request({
    url: `/system/template/down/${id}`,
    method: 'GET',
    handleResponse: true,
    responseType: 'blob'
  })
}
