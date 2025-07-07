import request from '@/utils/request';
import {
    mockAIInsights,
    mockCareLevelStatistics,
    mockEquipmentUsageStatistics,
    mockHealthStatusStatistics,
    mockMedicalServiceStatistics,
    mockOverviewStatistics,
    mockRegionDistribution,
    mockServiceQuality,
    mockStatisticsData,
    mockTrendAnalysis,
    mockWarningAnalysis
} from './mockReports';

/**
 * 报表分析相关API
 */

// 开发模式控制
const isDev = false; // 是否使用Mock数据，生产环境设为false

/**
 * 获取数据概览统计
 */
export function getOverviewStatistics() {
  if (isDev) {
    return Promise.resolve(mockOverviewStatistics)
  }
  return request({
    url: '/reports/overview',
    method: 'get'
  })
}

/**
 * 获取健康状况统计
 */
export function getHealthStatusStatistics() {
  if (isDev) {
    return Promise.resolve(mockHealthStatusStatistics)
  }
  return request({
    url: '/reports/health-status',
    method: 'get'
  })
}

/**
 * 获取预警统计分析
 */
export function getWarningAnalysis() {
  if (isDev) {
    return Promise.resolve(mockWarningAnalysis)
  }
  return request({
    url: '/reports/warning-analysis',
    method: 'get'
  })
}

/**
 * 获取医疗服务统计
 */
export function getMedicalServiceStatistics() {
  if (isDev) {
    return Promise.resolve(mockMedicalServiceStatistics)
  }
  return request({
    url: '/reports/medical-service',
    method: 'get'
  })
}

/**
 * 获取趋势分析数据
 * @param {string} timeRange - 时间范围
 */
export function getTrendAnalysis(timeRange = '30') {
  if (isDev) {
    return Promise.resolve(mockTrendAnalysis)
  }
  return request({
    url: '/reports/trend-analysis',
    method: 'get',
    params: { timeRange }
  })
}

/**
 * 获取照护等级统计
 */
export function getCareLevelStatistics() {
  if (isDev) {
    return Promise.resolve(mockCareLevelStatistics)
  }
  return request({
    url: '/reports/care-level',
    method: 'get'
  })
}

/**
 * 获取设备使用统计
 */
export function getEquipmentUsageStatistics() {
  if (isDev) {
    return Promise.resolve(mockEquipmentUsageStatistics)
  }
  return request({
    url: '/reports/equipment-usage',
    method: 'get'
  })
}

/**
 * 获取地区分布数据
 */
export function getRegionDistribution() {
  if (isDev) {
    return Promise.resolve(mockRegionDistribution)
  }
  return request({
    url: '/reports/region-distribution',
    method: 'get'
  })
}

/**
 * 获取服务质量评分
 */
export function getServiceQuality() {
  if (isDev) {
    return Promise.resolve(mockServiceQuality)
  }
  return request({
    url: '/reports/service-quality',
    method: 'get'
  })
}

/**
 * 获取分析数据列表
 * @param {Object} params - 查询参数
 * @param {string} params.analysisType - 分析类型
 * @param {Array} params.dateRange - 日期范围
 * @param {number} params.current - 当前页
 * @param {number} params.size - 页面大小
 */
export function getAnalysisData(params) {
  return request({
    url: '/reports/analysis-data',
    method: 'get',
    params
  })
}

/**
 * 导出分析数据
 * @param {Object} params - 导出参数
 * @param {string} params.type - 导出类型 (excel, pdf)
 * @param {Object} params.filters - 筛选条件
 */
export function exportAnalysisData(params) {
  return request({
    url: '/reports/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 获取AI洞察报告
 */
export function getAIInsights() {
  if (isDev) {
    return Promise.resolve(mockAIInsights)
  }
  return request({
    url: '/reports/ai-insights',
    method: 'get'
  })
}

/**
 * 生成AI洞察报告
 * @param {Object} params - 生成参数
 * @param {string} params.analysisType - 分析类型
 * @param {Array} params.dateRange - 分析时间范围
 */
export function generateAIInsights(params) {
  return request({
    url: '/reports/ai-insights/generate',
    method: 'post',
    data: params
  })
}

/**
 * 获取报表统计数据
 */
export function getStatisticsData() {
  if (isDev) {
    return Promise.resolve(mockStatisticsData)
  }
  return request({
    url: '/reports/statistics',
    method: 'get'
  })
}

/**
 * 获取图表配置
 * @param {string} chartType - 图表类型
 */
export function getChartConfig(chartType) {
  return request({
    url: `/reports/chart-config/${chartType}`,
    method: 'get'
  })
}
