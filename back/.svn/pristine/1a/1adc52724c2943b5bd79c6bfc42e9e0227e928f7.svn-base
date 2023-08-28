/**
 * 判断字符串是否非空或空白
 * @param str
 * @returns {boolean}
 */
export function isNotBlank(str) {
  return (str && str == "")
}

/**
 * 保存文件
 * @param resp response
 * @param name 下载文件的名字，非必传，不传会先读取返回的content-disposition中数据，若无则按浏览器下载规则命名
 * @returns {Promise<any>}
 */
export function saveFile(resp, name) {
  return new Promise((resolve, reject) => {
    // 下载的数据
    const blob = new Blob([resp.data], { type: resp.data.type })
    // 下载方法实现
    const download = () => {
      const disposition = resp.headers['content-disposition']
      // 下载文件名称
      const fileName = isNotBlank(name)
        ? name
        : decodeURI(disposition.substring(disposition.indexOf('filename=') + 9, disposition.length-1))
      const elink = document.createElement('a')
      if ('msSaveOrOpenBlob' in navigator) {
        // Microsoft Edge and Microsoft Internet Explorer 10-11
        window.navigator.msSaveOrOpenBlob(blob, fileName)
      } else {
        elink.download = fileName
        elink.style.display = 'none'
        elink.href = URL.createObjectURL(blob)
        document.body.appendChild(elink)
        elink.click()
        URL.revokeObjectURL(elink.href) // 释放URL 对象
        document.body.removeChild(elink)
      }
      resolve()
    }
    if (resp.data.type === 'application/json') {
      const reader = new FileReader()
      reader.readAsText(blob)
      reader.onload = e => {
        let json
        try {
          json = JSON.parse(e.target.result)
        } catch (e) {
          download()
          return
        }
        if (+json.status === 0) {
          download()
        } else {
          reject(json.statusText || '未知错误')
        }
      }
    } else {
      download()
    }
  })
}
/**
 * 时间
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string | null}
 */
export function parseTime(time, cFormat = '{y}-{m}-{d} {h}:{i}:{s}') {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  const date = compatibleDate(time)
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, k) => {
    let value = formatObj[k]
    // Note: getDay() returns 0 on Sunday
    if (k === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}
