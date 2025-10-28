import request from '@/utils/request'

/**
 * 家属专用API
 * 用于家属用户获取关联长辈的信息
 */

/**
 * 家属批量获取老人信息
 * @param {Array} elderlyIds 老人ID列表
 */
export function getElderlyByIds(elderlyIds) {
  return request({
    url: '/family/elderly/batch',
    method: 'post',
    data: {
      elderlyIds: elderlyIds
    }
  })
}

/**
 * 获取老人最新体征数据
 * @param {Number} elderlyId 老人ID
 */
export function getLatestVitals(elderlyId) {
  return request({
    url: `/family/elderly/${elderlyId}/vitals/latest`,
    method: 'get'
  })
}

/**
 * 获取老人预警信息
 * @param {Number} elderlyId 老人ID
 */
export function getWarnings(elderlyId) {
  return request({
    url: `/family/elderly/${elderlyId}/warnings`,
    method: 'get'
  })
}

/**
 * 标记预警为已读
 * @param {Array} warningIds 预警ID列表
 */
export function markWarningsAsRead(warningIds) {
  return request({
    url: '/family/warnings/mark-read',
    method: 'post',
    data: {
      warningIds: warningIds
    }
  })
}

/**
 * 发送联系医护请求
 * @param {Object} contactData 联系请求数据
 */
export function sendContactRequest(contactData) {
  return request({
    url: '/family/contact/send',
    method: 'post',
    data: contactData
  })
}

/**
 * 家属专用 - 分页查询关联老人的健康预警
 * @param {Object} params 查询参数
 */
export function getFamilyWarningsPage(params) {
  return request({
    url: '/family/warnings/page',
    method: 'post',
    data: params
  })
}

/**
 * 家属专用 - 获取关联老人健康预警统计
 */
export function getFamilyWarningStatistics() {
  return request({
    url: '/family/warnings/statistics',
    method: 'get'
  })
}

/**
 * 家属专用 - 获取关联老人列表
 */
export function getFamilyElderlyList() {
  return request({
    url: '/family/elderly/list',
    method: 'get'
  })
}

/**
 * 获取家属对指定老人的权限列表
 * @param {Number} elderlyId 老人ID
 */
export function getFamilyPermissions(elderlyId) {
  return request({
    url: `/family/elderly/${elderlyId}/permissions`,
    method: 'get'
  })
}

/**
 * 申请家属权限
 * @param {Object} data 权限申请数据
 */
export function requestFamilyPermission(data) {
  return request({
    url: '/family/permissions/request',
    method: 'post',
    data
  })
}

/**
 * 订阅健康预警推送
 * @param {Object} data 订阅配置
 */
export function subscribeWarningNotification(data) {
  return request({
    url: '/family/warnings/subscribe',
    method: 'post',
    data
  })
}

/**
 * 取消订阅健康预警推送
 * @param {Number} elderlyId 老人ID
 */
export function unsubscribeWarningNotification(elderlyId) {
  return request({
    url: `/family/warnings/unsubscribe/${elderlyId}`,
    method: 'post'
  })
}
