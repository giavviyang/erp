import request from '@/utils/request'

// 查询流程卡数据
export function queryList(data) {
  return request({
    url: '/sales/flowCard/queryList',
    method: 'post',
    data: data
  })
}

// 查询流程卡明细
export function queryFlowDetailed(data) {
  return request({
    url: '/sales/flowCard/queryFlowDetailed',
    method: 'post',
    params: data
  })
}

// 查询未分架订单
export function queryFlowOrder(data) {
  return request({
    url: '/sales/flowCard/queryFlowOrder',
    method: 'post',
    data: data
  })
}

// 查询订单未分架产品
export function selectProduct(data) {
  return request({
    url: '/sales/flowCard/selectProduct',
    method: 'post',
    data: data
  })
}

//自动分架
export function automaticShelf(data) {
  return request({
    url: '/sales/flowCard/automaticShelf',
    method: 'post',
    data: data
  })
}

//保存分架后的流程卡信息
export function saveFlowCard(data) {
  return request({
    url: '/sales/flowCard/saveFlowCard',
    method: 'post',
    data: data
  })
}

//手动分家查询未分架半产品信息
export function selectSemiProduct(data) {
  return request({
    url: '/sales/flowCard/selectSemiProduct',
    method: 'post',
    data: data
  })
}

//生成流程卡编号
export function generateFlowNumber(data) {
  return request({
    url: '/sales/flowCard/generateFlowNumber',
    method: 'post',
    params: data
  })
}

//手动分架-保存分架信息
export function saveSemiProduct(data) {
  return request({
    url: '/sales/flowCard/saveSemiProduct',
    method: 'post',
    data: data
  })
}

//编辑流程卡-查询
export function updateQuery(data) {
  return request({
    url: '/sales/flowCard/updateQuery',
    method: 'post',
    params: data
  })
}

//编辑流程卡-保存分架信息
export function saveUpdate(data) {
  return request({
    url: '/sales/flowCard/saveUpdate',
    method: 'post',
    data: data
  })
}

//删除流程卡
export function delFlowCard(data) {
  return request({
    url: '/sales/flowCard/delFlowCard',
    method: 'post',
    params: data
  })
}

//校验流程卡状态
export function checkFlowCard(data) {
  return request({
    url: '/sales/flowCard/checkFlowCard',
    method: 'post',
    params: data
  })
}

//导出迪赛模板perfectcut
export function exportMachineFlow(orderId) {
  return request({
    url: `/sales/flowCard/exportMachineFlow`,
    method: 'post',
    params:orderId,
    handleResponse: true,
    responseType: 'blob'
  })
}


