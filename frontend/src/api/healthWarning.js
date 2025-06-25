import request from '@/utils/request'

/**
 * 健康预警相关API
 */

// 分页查询健康预警列表
export function getHealthWarningPageList(data) {
  return request({
    url: '/api/health-warning/page',
    method: 'post',
    data
  })
}

// 新增健康预警
export function addHealthWarning(data) {
  return request({
    url: '/api/health-warning/add',
    method: 'post',
    data
  })
}

// 更新健康预警
export function updateHealthWarning(data) {
  return request({
    url: '/api/health-warning/update',
    method: 'put',
    data
  })
}

// 根据ID查询健康预警详情
export function getHealthWarningById(id) {
  return request({
    url: `/api/health-warning/${id}`,
    method: 'get'
  })
}

// 删除健康预警
export function deleteHealthWarning(id) {
  return request({
    url: `/api/health-warning/${id}`,
    method: 'delete'
  })
}

// 批量删除健康预警
export function deleteHealthWarningBatch(ids) {
  return request({
    url: '/api/health-warning/batch',
    method: 'delete',
    data: ids
  })
}

// 处理健康预警
export function handleHealthWarning(id, data) {
  return request({
    url: `/api/health-warning/${id}/handle`,
    method: 'put',
    data
  })
}

// 更新预警状态
export function updateHealthWarningStatus(id, status) {
  return request({
    url: `/api/health-warning/${id}/status/${status}`,
    method: 'put'
  })
}

// 根据老人ID查询预警列表
export function getHealthWarningByElderlyId(elderlyId) {
  return request({
    url: `/api/health-warning/elderly/${elderlyId}`,
    method: 'get'
  })
}

// 获取未处理预警数量
export function getUnhandledWarningCount() {
  return request({
    url: '/api/health-warning/count/unhandled',
    method: 'get'
  })
}

// 获取各级别预警统计
export function getWarningLevelStatistics() {
  return request({
    url: '/api/health-warning/statistics/level',
    method: 'get'
  })
}

// 获取预警类型统计
export function getWarningTypeStatistics() {
  return request({
    url: '/api/health-warning/statistics/type',
    method: 'get'
  })
}
