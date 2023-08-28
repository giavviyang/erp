import request from "@/utils/request";

export function getDamageInfo(data){
  return request({
    url:"/produce/patch/getDamageInfo",
    method:"POST",
    data
  })
}

export function getPatchList(params){
  return request({
    url:"/produce/patch/list",
    method:"GET",
    params
  })
}

export function getPatchInfo(id){
  return request({
    url:`/produce/patch/getInfo/${id}`,
    method:"GET"
  })
}

export function delPatch(id){
  return request({
    url:`/produce/patch/del/${id}`,
    method:"DELETE"
  })
}


export function addPatch(data){
  return request({
    url:"/produce/patch/add",
    method:"POST",
    data
  })
}
