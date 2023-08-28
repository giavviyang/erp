import request from "@/utils/request";

export function getList(params){
  return request({
    url:"/produce/damage/list",
    method:"GET",
    params
  })
}

export function getDetail(id){
  return request({
    url:`/produce/damage/info/${id}`,
    method:"GET"
  })
}


export function getOrderList(params){
  return request({
    url:"/produce/damage/getOrderList",
    method:"GET",
    params
  })
}

export function getFlowCardList(params){
  return request({
    url:"/produce/damage/getFlowCardList",
    method:"GET",
    params
  })
}

export function getNo(){
  return request({
    url:"/produce/damage/getDamageNo",
    method:"GET"
  })
}

export function addDamage(data){
  return request({
    url:"/produce/damage/add",
    method:"POST",
    data
  })
}
export function editDamage(id,data){
  return request({
    url:`/produce/damage/edit/${id}`,
    method:"PUT",
    data
  })
}

export function delDamage(ids){
  return request({
    url:`/produce/damage/del`,
    method:"DELETE",
    data:{ids}
  })
}

