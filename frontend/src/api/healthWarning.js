import request from '@/utils/request'
import { getMockLevelStatistics, getMockWarningPage } from './mockHealthWarning'

/**
 * 健康预警相关API
 */

// 开发环境标识
const isDev = false; // 强制关闭开发环境Mock，使用真实后端数据

/**
 * 获取预警统计数据
 */
export function getWarningStatistics() {
  if (isDev) {
    return Promise.resolve({ 
      data: {
        code: 200,
        message: '查询成功',
        data: { urgent: 15, high: 32, medium: 48, low: 25 }
      }
    })
  }
  return request({
    url: '/api/health-warning/statistics/level',
    method: 'get'
  })
}

/**
 * 获取预警列表
 * @param {Object} params - 查询参数
 */
export function getWarningList(params) {
  return request({
    url: '/api/health-warning/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取预警详情
 * @param {number} id - 预警ID
 */
export function getWarningDetail(id) {
  return request({
    url: `/api/health-warning/${id}`,
    method: 'get'
  })
}

/**
 * 处理预警
 * @param {number} id - 预警ID
 * @param {Object} data - 处理数据
 */
export function processWarning(id, data) {
  return request({
    url: `/api/health-warning/${id}/handle`,
    method: 'put',
    data
  })
}

/**
 * 忽略预警
 * @param {number} id - 预警ID
 * @param {string} reason - 忽略原因
 */
export function ignoreWarning(id, reason) {
  return request({
    url: `/api/health-warning/${id}/status/4`,
    method: 'put',
    data: { reason }
  })
}

// 分页查询健康预警列表（兼容旧版API）
export function getHealthWarningPageList(data) {
  if (isDev) {
    // 开发环境使用Mock数据
    return Promise.resolve({ 
      data: getMockWarningPage(data)
    })
  }
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
  if (isDev) {
    // 开发环境模拟删除操作
    return Promise.resolve({ 
      data: { code: 200, message: '删除成功' }
    })
  }
  return request({
    url: `/api/health-warning/${id}`,
    method: 'delete'
  })
}

// 批量删除健康预警
export function deleteHealthWarningBatch(ids) {
  if (isDev) {
    // 开发环境模拟批量删除操作
    return Promise.resolve({ 
      data: { code: 200, message: '批量删除成功' }
    })
  }
  return request({
    url: '/api/health-warning/batch',
    method: 'delete',
    data: ids
  })
}

// 处理健康预警
export function handleHealthWarning(id, data) {
  if (isDev) {
    // 开发环境模拟处理操作
    return Promise.resolve({ 
      data: { code: 200, message: '处理成功' }
    })
  }
  return request({
    url: `/api/health-warning/${id}/handle`,
    method: 'put',
    data
  })
}

// 更新预警状态
export function updateHealthWarningStatus(id, status) {
  if (isDev) {
    // 开发环境模拟状态更新操作
    return Promise.resolve({ 
      data: { code: 200, message: '状态更新成功' }
    })
  }
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
  if (isDev) {
    // 开发环境使用Mock数据
    return Promise.resolve({ 
      data: getMockLevelStatistics()
    })
  }
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
