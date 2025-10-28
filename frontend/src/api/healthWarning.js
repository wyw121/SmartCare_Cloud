import request from '@/utils/request';
// import { getMockLevelStatistics, getMockWarningPage } from './mockHealthWarning';

/**
 * 健康预警API
 */

// 开发模式
const isDev = false; // 是否使用Mock数据,生产环境设为false

/**
 * 获取预警统计
 */
export function getWarningStatistics() {
  if (isDev) {
    return Promise.resolve({ 
      data: {
        code: 200,
        message: '成功',
        data: { urgent: 15, high: 32, medium: 48, low: 25 }
      }
    })
  }
  return request({
    url: '/health-warning/statistics/level',
    method: 'get'
  })
}

/**
 * 获取预警列表
 * @param {Object} params - 查询参数
 */
export function getWarningList(params) {
  return request({
    url: '/health-warning/page',
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
    url: `/health-warning/${id}`,
    method: 'get'
  })
}

/**
 * 更新预警状态
 * @param {number} id - 预警ID
 * @param {string} status - 预警状态
 */
export function updateWarningStatus(id, status) {
  return request({
    url: `/health-warning/${id}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 更新健康预警状态（别名）
 * @param {number} id - 预警ID
 * @param {string} status - 预警状态
 */
export function updateHealthWarningStatus(id, status) {
  return request({
    url: `/health-warning/${id}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 批量处理预警
 * @param {Array} ids - 预警ID数组
 * @param {string} action - 处理动作
 */
export function batchHandleWarnings(ids, action) {
  return request({
    url: '/health-warning/batch',
    method: 'post',
    data: { ids, action }
  })
}

/**
 * 分页获取健康预警数据
 * @param {Object} data - 查询参数
 */
export function getHealthWarningPageList(data) {
  if (isDev) {
    // 使用模拟Mock数据
    return Promise.resolve({ 
      data: getMockWarningPage(data)
    })
  }
  return request({
    url: '/health-warning/page',
    method: 'post',
    data
  })
}

// 添加预警
export function addHealthWarning(data) {
  return request({
    url: '/health-warning/add',
    method: 'post',
    data
  })
}

// 更新预警
export function updateHealthWarning(data) {
  return request({
    url: '/health-warning/update',
    method: 'put',
    data
  })
}

// 根据ID获取预警信息
export function getHealthWarningById(id) {
  return request({
    url: `/health-warning/${id}`,
    method: 'get'
  })
}

// 删除预警
export function deleteHealthWarning(id) {
  return request({
    url: `/health-warning/${id}`,
    method: 'delete'
  })
}

// 批量删除预警
export function deleteHealthWarningBatch(ids) {
  return request({
    url: '/health-warning/batch',
    method: 'delete',
    data: ids
  })
}

// 处理预警
export function handleWarning(id, data) {
  return request({
    url: `/health-warning/${id}/handle`,
    method: 'put',
    data
  })
}

// 忽略预警
export function ignoreWarning(id) {
  return request({
    url: `/health-warning/${id}/ignore`,
    method: 'put'
  })
}

// 处理预警
export function handleHealthWarning(id, data) {
  return request({
    url: `/health-warning/${id}/handle`,
    method: 'put',
    data
  })
}

// 批量处理预警
export function batchHandleHealthWarning(ids, data) {
  return request({
    url: '/health-warning/batch-handle',
    method: 'put',
    data: { ids, ...data }
  })
}

// 获取预警类型统计
export function getWarningTypeStatistics() {
  return request({
    url: '/health-warning/statistics/type',
    method: 'get'
  })
}

// 获取预警级别统计
export function getWarningLevelStatistics() {
  if (isDev) {
    // 使用模拟Mock数据
    return Promise.resolve({ 
      data: getMockLevelStatistics()
    })
  }
  return request({
    url: '/health-warning/statistics/level',
    method: 'get'
  })
}

// 获取未处理预警数量
export function getUnhandledWarningCount() {
  return request({
    url: '/health-warning/count/unhandled',
    method: 'get'
  })
}

// 获取预警趋势统计
export function getWarningTrendStatistics(params) {
  return request({
    url: '/health-warning/statistics/trend',
    method: 'get',
    params
  })
}

// 按级别获取预警统计数据
export function getWarningStatisticsByLevel() {
  return request({
    url: '/health-warning/statistics/level',
    method: 'get'
  })
}

// 按类型获取预警统计数据
export function getWarningStatisticsByType() {
  return request({
    url: '/health-warning/statistics/type',
    method: 'get'
  })
}

// 导出预警数据
export function exportWarningData(params) {
  return request({
    url: '/health-warning/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// 获取预警配置
export function getWarningConfig() {
  return request({
    url: '/health-warning/config',
    method: 'get'
  })
}

// 更新预警配置
export function updateWarningConfig(data) {
  return request({
    url: '/health-warning/config',
    method: 'put',
    data
  })
}
