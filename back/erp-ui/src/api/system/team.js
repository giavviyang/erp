import request from "@/utils/request";

// 查询班组列表
export function listTeam(query) {
  return request({
    url: '/system/team/list',
    method: 'get',
    params: query
  })
}

// 查询班组列表
export function getAllTeam(params) {
  return request({
    url: '/system/team/allList',
    method: 'get',
    params
  })
}


// 新增班组
export function addTeam(data) {
  return request({
    url: '/system/team/add',
    method: 'post',
    data: data
  })
}

// 修改班组
export function updateTeam(id,data) {
  return request({
    url: `/system/team/edit/${id}`,
    method: 'put',
    data
  })
}

// 删除班组
export function delTeam(id) {
  return request({
    url:`/system/team/del/${id}`,
    method: 'delete'
  })
}
