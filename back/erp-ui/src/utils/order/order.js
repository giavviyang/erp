// 保留三位小数
export function keepOneNum(value) {
  let resValue = 0
  //小数点的位置
  let index = value && value.toString().indexOf('.') + 1
  //小数的位数
  let num = value && Math.abs(Number(value)).toString().length - index
  if (index && num > 1) {
    resValue = Math.floor(value && Number(value)*10)/10
  } else {
    resValue = value
  }
  return resValue
}
// 保留三位小数
export function keepThreeNum(value) {
  let resValue = 0
  //小数点的位置
  let index = value && value.toString().indexOf('.') + 1
  //小数的位数
  let num = value && Math.abs(Number(value)).toString().length - index
  if (index && num > 3) {
    resValue = Math.ceil(value && Number(value)*1000)/1000
  } else {
    resValue = value
  }
  return resValue
}
//获取当前日期
export function getCurrentDay() {
  //获取当前日期
  let nowDate = new Date()
  let date = {
    year: nowDate.getFullYear(),
    month: nowDate.getMonth() + 1,
    date: nowDate.getDate(),
    hours: nowDate.getHours(),
    minutes: nowDate.getMinutes() < 10 ? '0' + nowDate.getMinutes() : nowDate.getMinutes(),
    seconds: nowDate.getSeconds() < 10 ? '0' + nowDate.getSeconds() : nowDate.getSeconds()
  }
  let currentDay = `${date.year}-${date.month}-${date.date} ${date.hours}:${date.minutes}:${date.seconds}`;
  return currentDay
}
/**
 * 校验只要是数字（包含正负整数，0以及正负浮点数）就返回true
 **/
export function isNumber(val){
  var regPos =  /^[+]?(0|([1-9]\d*))(\.\d+)?$/g; //非负浮点数
  // var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
  if(regPos.test(val)){
    return true;
  }else{
    return false;
  }
}

//合计
export function sum(array) {
  // let sum = 0;
  // for (let i = 0; i < array.length; i++) {
  //   sum += array[i]
  // }
  // return sum;
  return array.reduce((total, num) => total + num, 0)
}
