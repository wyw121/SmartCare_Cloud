import request from '@/utils/request'

/**
 * 报表分析相关API
 */

/**
 * 获取数据概览统计
 */
export function getOverviewData() {
  return request({
    url: '/reports/overview',
    method: 'get'
  })
}

/**
 * 获取健康趋势数据
 * @param {Object} params - 查询参数
 * @param {string} params.timeRange - 时间范围 (7d, 30d, 3m, 1y)
 */
export function getHealthTrendData(params) {
  return request({
    url: '/reports/health-trend',
    method: 'get',
    params
  })
}

/**
 * 获取疾病分布数据
 */
export function getDiseaseDistribution() {
  return request({
    url: '/reports/disease-distribution',
    method: 'get'
  })
}

/**
 * 获取年龄段分析数据
 */
export function getAgeAnalysis() {
  return request({
    url: '/reports/age-analysis',
    method: 'get'
  })
}

/**
 * 获取地区分布数据
 */
export function getRegionDistribution() {
  return request({
    url: '/reports/region-distribution',
    method: 'get'
  })
}

/**
 * 获取服务质量评分
 */
export function getServiceQuality() {
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
