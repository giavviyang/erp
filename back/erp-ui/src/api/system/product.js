import request from '@/utils/request';

// 查询产品类型信息列表
export function getProductType() {
  return request({
    url: '/system/product/type/list',
    method: 'get',
  })
}

// 查询产品类型信息列表
export function addProductType(data) {
  return request({
    url: '/system/product/type/add',
    method: 'POST',
    data
  })
}

// 查询产品类型信息列表
export function editProductType(id,data) {
  return request({
    url: `/system/product/type/edit/${id}`,
    method: 'PUT',
    data
  })
}
export function delProductType(id) {
  return request({
    url: `/system/product/type/del/${id}`,
    method: 'DELETE'
  })
}

// 查询产品信息列表
export function getProduct(query) {
  return request({
    url: '/system/product/list',
    method: 'get',
    params: query
  })
}

export function getSemiProduct(id) {
  return request({
    url: `/system/product/getSemiProduct/${id}`,
    method: 'get'
  })
}

// 查询产品类型信息列表
export function addProduct(data) {
  return request({
    url: '/system/product/add',
    method: 'POST',
    data
  })
}

// 查询产品类型信息列表
export function editProduct(id,data) {
  return request({
    url: `/system/product/edit/${id}`,
    method: 'PUT',
    data
  })
}
export function delProduct(id) {
  return request({
    url: `/system/product/del/${id}`,
    method: 'DELETE'
  })
}

// 查询产品信息列表
export function queryProduct(query) {
  return request({
    url: '/system/product/queryProduct',
    method: 'POST',
    params: query
  })
}
//根据产品名称查询当前产品下的半产品
export function nameGetSemiProduct(data) {
  return request({
    url: `/system/product/nameGetSemiProduct`,
    method: 'POST',
    data
  })
}
