const defaultIds = []
export function getDefaultTree(treeList){
  if(treeList.length > 0){
    if(treeList[0].id != 0) defaultIds.push(treeList[0].id)
    if(treeList[0].hasOwnProperty("children")){
      getDefaultTree(treeList[0].children)
    }
  }
  if(defaultIds.length == 0){
    return 0
  }else if(defaultIds.length >= 1){
    return defaultIds[defaultIds.length - 1]
  }
  return defaultIds
}
