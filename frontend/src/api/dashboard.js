/**
 * 仪表板相关API
 */
import request from '@/utils/request'

/**
 * 获取仪表板数据
 */
export function getDashboardData() {
  return request({
    url: '/dashboard/data',
    method: 'get'
  })
}

/**
 * 获取统计数据
 */
export function getStatsData() {
  return request({
    url: '/dashboard/stats',
    method: 'get'
  })
}

/**
 * 获取图表数据
 * @param {string} chartType - 图表类型
 */
export function getChartData(chartType) {
  return request({
    url: `/dashboard/charts/${chartType}`,
    method: 'get'
  })
}

/**
 * 获取健康状况分布数据
 */
export function getHealthDistribution() {
  return request({
    url: '/dashboard/health-distribution',
    method: 'get'
  })
}

/**
 * 获取年龄分布数据
 */
export function getAgeDistribution() {
  return request({
    url: '/dashboard/age-distribution',
    method: 'get'
  })
}

/**
 * 获取健康趋势数据
 * @param {Object} params - 查询参数
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 */
export function getHealthTrend(params) {
  return request({
    url: '/dashboard/health-trend',
    method: 'get',
    params
  })
}

/**
 * 获取系统状态
 */
export function getSystemStatus() {
  return request({
    url: '/dashboard/system-status',
    method: 'get'
  })
}

/**
 * 获取实时数据
 */
export function getRealTimeData() {
  return request({
    url: '/dashboard/realtime',
    method: 'get'
  })
}
