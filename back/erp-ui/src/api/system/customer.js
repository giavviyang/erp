import request from '@/utils/request'
/* 查询客户类型列表 */
export function customerTypeList() {
  return request({
    url: '/system/customer/type/list',
    method: 'GET'
  })
}

export function addCustomerType(data) {
  return request({
    url: '/system/customer/type/add',
    method: 'POST',
    data
  })
}

export function editCustomerType(id,data) {
  return request({
    url: `/system/customer/type/edit/${id}`,
    method: 'PUT',
    data
  })
}

export function delCustomerType(id) {
  return request({
    url: `/system/customer/type/del/${id}`,
    method: 'DELETE'
  })
}

/* 根据客户类型id查询客户列表 */
export function customerList(params) {
  return request({
    url: '/system/customer/list',
    method: 'GET',
    params
  })
}
export function addCustomer(data) {
  return request({
    url: '/system/customer/add',
    method: 'POST',
    data
  })
}
export function addOrderCustomer(data) {
  return request({
    url: '/system/customer/orderadd',
    method: 'POST',
    data
  })
}

export function editCustomer(id,data) {
  return request({
    url: `/system/customer/edit/${id}`,
    method: 'PUT',
    data
  })
}

export function delCustomer(id) {
  return request({
    url: `/system/customer/del/${id}`,
    method: 'DELETE'
  })
}
