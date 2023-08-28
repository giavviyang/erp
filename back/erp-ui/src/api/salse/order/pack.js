import request from '@/utils/request'

// 自动生成打包编号
export function productionNumber() {
  return request({
    url: '/sales/pack/productionNumber',
    method: 'post',
  })
}

// 查询打包信息
export function queryPackData(data) {
  return request({
    url: '/sales/pack/queryPackData',
    method: 'post',
    data: data
  })
}

// 查询可以打包的产品
export function queryPackProduct(data) {
  return request({
    url: '/sales/pack/queryPackProduct',
    method: 'post',
    data: data
  })
}


// 新增打包
export function addPack(data) {
  return request({
    url: '/sales/pack/addPack',
    method: 'post',
    data: data
  })
}

// 查看明细
export function detailsView(data) {
  return request({
    url: '/sales/pack/detailsView',
    method: 'post',
    params: data
  })
}

// 编辑打包-查询明细
export function updateDetails(data) {
  return request({
    url: '/sales/pack/updateDetails',
    method: 'post',
    params: data
  })
}

// 编辑打包 —— 保存
export function updatePack(data) {
  return request({
    url: '/sales/pack/updatePack',
    method: 'post',
    data: data
  })
}

// 编辑打包-查询明细
export function delPack(data) {
  return request({
    url: '/sales/pack/delPack',
    method: 'post',
    params: data
  })
}

// 订单管理-查询打包记录
export function queryPack(data) {
  return request({
    url: '/sales/pack/queryPack',
    method: 'post',
    params: data
  })
}
