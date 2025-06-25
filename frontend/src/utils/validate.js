/**
 * 表单验证工具函数
 * 智慧医养大数据公共服务平台
 */

/**
 * 判断是否为外部链接
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 验证用户名
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'doctor', 'nurse', 'manager']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * 验证URL
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * 验证小写字母
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * 验证大写字母
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * 验证字母
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * 验证邮箱
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * 验证手机号
 * @param {string} phone
 * @returns {Boolean}
 */
export function validPhone(phone) {
  const reg = /^1[3|4|5|6|7|8|9][0-9]\d{8}$/
  return reg.test(phone)
}

/**
 * 验证身份证号
 * @param {string} idCard
 * @returns {Boolean}
 */
export function validIdCard(idCard) {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  return reg.test(idCard)
}

/**
 * 验证密码强度
 * @param {string} password
 * @returns {Boolean}
 */
export function validPassword(password) {
  // 至少8位，包含大小写字母、数字
  const reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/
  return reg.test(password)
}

/**
 * 验证中文姓名
 * @param {string} name
 * @returns {Boolean}
 */
export function validChineseName(name) {
  const reg = /^[\u4e00-\u9fa5]{2,8}$/
  return reg.test(name)
}

/**
 * 验证年龄
 * @param {number} age
 * @returns {Boolean}
 */
export function validAge(age) {
  return age >= 0 && age <= 150
}

/**
 * 验证血压值
 * @param {number} value
 * @param {string} type - 'systolic' | 'diastolic'
 * @returns {Boolean}
 */
export function validBloodPressure(value, type = 'systolic') {
  if (type === 'systolic') {
    return value >= 60 && value <= 250
  } else if (type === 'diastolic') {
    return value >= 40 && value <= 150
  }
  return false
}

/**
 * 验证心率
 * @param {number} heartRate
 * @returns {Boolean}
 */
export function validHeartRate(heartRate) {
  return heartRate >= 30 && heartRate <= 200
}

/**
 * 验证血糖值
 * @param {number} glucose
 * @returns {Boolean}
 */
export function validGlucose(glucose) {
  return glucose >= 1.0 && glucose <= 30.0
}

/**
 * 验证体温
 * @param {number} temperature
 * @returns {Boolean}
 */
export function validTemperature(temperature) {
  return temperature >= 30.0 && temperature <= 45.0
}

/**
 * 验证体重
 * @param {number} weight
 * @returns {Boolean}
 */
export function validWeight(weight) {
  return weight >= 20 && weight <= 300
}

/**
 * 验证身高
 * @param {number} height
 * @returns {Boolean}
 */
export function validHeight(height) {
  return height >= 50 && height <= 250
}

/**
 * 验证BMI值
 * @param {number} bmi
 * @returns {Boolean}
 */
export function validBMI(bmi) {
  return bmi >= 10 && bmi <= 50
}
