import request from '@/utils/request'

// 查询入库信息
export function queryData(data) {
  return request({
    url: '/sales/warehousing/queryData',
    method: 'post',
    data: data
  })
}

// 新增入库信息
export function addData(data) {
  return request({
    url: '/sales/warehousing/addData',
    method: 'post',
    data: data
  })
}

// 查询明细
export function detailedView(data) {
  return request({
    url: '/sales/warehousing/detailedView',
    method: 'post',
    params: data
  })
}

// 删除记录
export function delData(data) {
  return request({
    url: '/sales/warehousing/delData',
    method: 'post',
    params: data
  })
}



