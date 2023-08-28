import request from '@/utils/request'

//硬件加密-信息采集
export function add(params) {
  return request({
    url: '/dongle/add',
    params: params,
    method: 'post'
  })
}

//硬件加密-删除信息
export function del() {
  return request({
    url: '/dongle/del',
    method: 'post'
  })
}

//硬件加密 -- 获取加密信息
export function check() {
  return request({
    url: '/dongle/check',
    headers: {
      isToken: false
    },
    method: 'post'
  })
}
