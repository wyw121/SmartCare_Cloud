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
