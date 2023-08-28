import request from '@/utils/request'

// 工艺审核-查看工艺
export function queryProcess(data) {
  return request({
    url: '/sales/orderProduct/queryProcess',
    method: 'post',
    params: data
  })
}

//自动分架   --  查询产品
export function selectProduct(data) {
  return request({
    url: '/sales/orderProduct/selectProduct',
    method: 'post',
    data: data
  })
}
