import request from '@/utils/request'

// 上传附件
export function uploadEnclosure(data) {
  return request({
    url: '/sales/orderEnclosure/uploadEnclosure',
    method: 'post',
    data: data
  })
}
//查询附件
export function queryEnclosure(data) {
  return request({
    url: '/sales/orderEnclosure/queryEnclosure',
    method: 'post',
    params: data
  })
}
//移除附件
export function delEnclosure(data) {
  return request({
    url: '/sales/orderEnclosure/delEnclosure',
    method: 'post',
    params: data
  })
}
//下载附件
export function downloadEnclosure(data) {
  return request({
    url: '/sales/orderEnclosure/downloadEnclosure',
    method: 'post',
    params: data
  })
}
