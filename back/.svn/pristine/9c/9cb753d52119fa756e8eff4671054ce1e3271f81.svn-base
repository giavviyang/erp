export function groupBy(data,key,id = 'id'){
  var data = [...data].map(item=>{
    delete item.children
    return item
  });
  data.forEach((father) => {
    var branchArr = data.filter((child) => {
      if (father[`${id}`] == child[`${key}`]) child._hasParent = true;
      return father[`${id}`] == child[`${key}`];
    });
    if (branchArr.length > 0) father.children = branchArr;
  });
  return data.filter((item) => {
    return !item._hasParent;
  })
}
