import request from "@/utils/request";

export function getCountList(params){
  return request({
    url:"/produce/patch/count/list",
    method:"GET",
    params
  })
}

export function getCountInfo(id){
  return request({
    url:`/produce/patch/count/info/${id}`,
    method:"GET"
  })
}

