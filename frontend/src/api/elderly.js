import request from '@/utils/request'

/**
 * 分页查询老人档案
 * @param {Object} params 查询参数
 */
export function getElderlyPage(params) {
  return request({
    url: '/api/elderly/page',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID获取老人档案详情
 * @param {Number} id 老人ID
 */
export function getElderlyById(id) {
  return request({
    url: `/api/elderly/${id}`,
    method: 'get'
  })
}

/**
 * 新增老人档案
 * @param {Object} data 老人档案数据
 */
export function createElderly(data) {
  return request({
    url: '/api/elderly',
    method: 'post',
    data
  })
}

/**
 * 更新老人档案
 * @param {Object} data 老人档案数据
 */
export function updateElderly(data) {
  return request({
    url: '/api/elderly',
    method: 'put',
    data
  })
}

/**
 * 删除老人档案
 * @param {Number} id 老人ID
 */
export function deleteElderly(id) {
  return request({
    url: `/api/elderly/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除老人档案
 * @param {Array} ids 老人ID列表
 */
export function batchDeleteElderly(ids) {
  return request({
    url: '/api/elderly/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 获取重点关注老人列表
 */
export function getKeyElderlyList() {
  return request({
    url: '/api/elderly/key',
    method: 'get'
  })
}

/**
 * 获取老人健康状态统计
 */
export function getElderlyHealthStatistics() {
  return request({
    url: '/api/elderly/health-statistics',
    method: 'get'
  })
}
