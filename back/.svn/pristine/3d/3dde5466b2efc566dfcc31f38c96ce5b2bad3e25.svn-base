import request from "@/utils/request";

export function getList(params){
  return request({
    url:"/produce/completion/list",
    method:"GET",
    params
  })
}
export function getCompletionInfo(id){
  return request({
    url:`/produce/completion/getInfo/${id}`,
    method:"GET"
  })
}

export function getFlowCardInfo(params){
  return request({
    url:"/produce/completion/getFlowCardInfo",
    method:"GET",
    params
  })
}
export function addCompletion(data){
  return request({
    url:"/produce/completion/add",
    method:"POST",
    data
  })
}

export function editCompletion(id,data){
  return request({
    url:`/produce/completion/edit/${id}`,
    method:"PUT",
    data
  })
}
export function delCompletion(ids){
  return request({
    url:`/produce/completion/del/${ids}`,
    method:"DELETE"
  })
}

