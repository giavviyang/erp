import request from '@/utils/request'

// 查询铁架信息
export function queryShelfData(data) {
  return request({
    url: '/sales/shelfManage/queryAll',
    method: 'post',
    params: data
  })
}
// 新增铁架
export function addShelf(data) {
  return request({
    url: '/sales/shelfManage/addShelf',
    method: 'post',
    data: data
  })
}
// 编辑铁架
export function updateShelf(data) {
  return request({
    url: '/sales/shelfManage/updateShelf',
    method: 'post',
    data: data
  })
}
// 删除铁架
export function delShelf(data) {
  return request({
    url: '/sales/shelfManage/delShelf',
    method: 'post',
    params: data
  })
}
// 查看出入库记录
export function queryShelfRecord(data) {
  return request({
    url: '/sales/shelfManage/queryShelfRecord',
    method: 'post',
    params: data
  })
}
