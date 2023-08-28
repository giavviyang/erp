import request from "@/utils/request";

export function getCountList(params){
  return request({
    url:"/produce/production/count/list",
    method:"GET",
    params
  })
}

export function getCountInfo(id){
  return request({
    url:`/produce/production/count/info/${id}`,
    method:"GET"
  })
}

