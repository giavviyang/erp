import request from '@/utils/request';

export function getQRCodeLise(params) {
  return request({
    url: '/system/qrcode/list',
    method: 'GET',
    params
  })
}

export function addQRCode(data) {
  return request({
    url: '/system/qrcode/add',
    method: 'POST',
    data
  })
}

export function editQRCode(id,data) {
  return request({
    url: `/system/qrcode/edit/${id}`,
    method: 'PUT',
    data
  })
}

export function delQRCode(id) {
  return request({
    url: `/system/qrcode/del/${id}`,
    method: 'DELETE'
  })
}

