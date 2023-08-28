import request from "@/utils/request";

export function getOrderList(params){
  return request({
    url:"/produce/scheduling/getOrderList",
    method:"GET",
    params
  })
}

export function getFlowCardList(params){
  return request({
    url:"/produce/scheduling/getFlowCardList",
    method:"GET",
    params
  })
}

export function addScheduling(data){
  return request({
    url:"/produce/scheduling/add",
    method:"POST",
    data
  })
}

export function editScheduling(data){
  return request({
    url:"/produce/scheduling/edit",
    method:"PUT",
    data
  })
}

export function delScheduling(ids){
  return request({
    url:`/produce/scheduling/del/${ids}`,
    method:"DELETE"
  })
}


export function getSchedulingList(params){
  return request({
    url:"/produce/scheduling/list",
    method:"GET",
    params
  })
}

export function getSchedulingListInfo(id){
  return request({
    url:`/produce/scheduling/info/${id}`,
    method:"GET"
  })
}
