import request from '@/utils/request'

/**
 * 分页查询老人档案
 * @param {Object} params 查询参数
 */
export function getElderlyPage(params) {
  return request({
    url: '/elderly/page',
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
    url: `/elderly/${id}`,
    method: 'get'
  })
}

/**
 * 新增老人档案
 * @param {Object} data 老人档案数据
 */
export function createElderly(data) {
  return request({
    url: '/elderly',
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
    url: '/elderly',
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
    url: `/elderly/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除老人档案
 * @param {Array} ids 老人ID列表
 */
export function batchDeleteElderly(ids) {
  return request({
    url: '/elderly/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 获取重点关注老人列表
 */
export function getKeyElderlyList() {
  return request({
    url: '/elderly/key',
    method: 'get'
  })
}

/**
 * 获取老人健康状态统计
 */
export function getElderlyHealthStatistics() {
  return request({
    url: '/elderly/health-statistics',
    method: 'get'
  })
}

/**
 * 获取老人健康档案
 * @param {Number} id 老人ID
 */
export function getElderlyHealthRecords(id) {
  return request({
    url: `/elderly/${id}/health-records`,
    method: 'get'
  })
}

/**
 * 添加健康记录
 * @param {Number} id 老人ID
 * @param {Object} data 健康记录数据
 */
export function addHealthRecord(id, data) {
  return request({
    url: `/elderly/${id}/health-records`,
    method: 'post',
    data
  })
}

/**
 * 获取老人紧急联系人
 * @param {Number} id 老人ID
 */
export function getEmergencyContacts(id) {
  return request({
    url: `/elderly/${id}/emergency-contacts`,
    method: 'get'
  })
}

/**
 * 更新紧急联系人
 * @param {Number} id 老人ID
 * @param {Object} data 紧急联系人数据
 */
export function updateEmergencyContacts(id, data) {
  return request({
    url: `/elderly/${id}/emergency-contacts`,
    method: 'put',
    data
  })
}

/**
 * 获取老人照护计划
 * @param {Number} id 老人ID
 */
export function getCarePlan(id) {
  return request({
    url: `/elderly/${id}/care-plan`,
    method: 'get'
  })
}

/**
 * 更新照护计划
 * @param {Number} id 老人ID
 * @param {Object} data 照护计划数据
 */
export function updateCarePlan(id, data) {
  return request({
    url: `/elderly/${id}/care-plan`,
    method: 'put',
    data
  })
}

/**
 * 获取健康预警信息
 * @param {Number} id 老人ID
 */
export function getHealthWarnings(id) {
  return request({
    url: `/elderly/${id}/health-warnings`,
    method: 'get'
  })
}

/**
 * 生成健康评估报告
 * @param {Number} id 老人ID
 */
export function generateAssessmentReport(id) {
  return request({
    url: `/elderly/${id}/assessment-report`,
    method: 'post'
  })
}

/**
 * 批量导出老人档案
 * @param {Array} ids 老人ID列表
 */
export function exportElderlyData(ids) {
  return request({
    url: '/elderly/export',
    method: 'post',
    data: ids
  })
}

/**
 * 批量导入老人档案
 * @param {Object} data 导入数据
 */
export function importElderlyData(data) {
  return request({
    url: '/elderly/import',
    method: 'post',
    data
  })
}

/**
 * 获取照护等级统计
 */
export function getCareLevelStatistics() {
  return request({
    url: '/elderly/care-level-statistics',
    method: 'get'
  })
}

/**
 * 获取年龄段分布统计
 */
export function getAgeDistribution() {
  return request({
    url: '/elderly/age-distribution',
    method: 'get'
  })
}

/**
 * 搜索老人档案（支持模糊搜索）
 * @param {String} keyword 搜索关键词
 * @param {Number} pageNum 页码
 * @param {Number} pageSize 每页大小
 */
export function searchElderly(keyword, pageNum, pageSize) {
  return request({
    url: '/elderly/search',
    method: 'get',
    params: {
      keyword,
      pageNum,
      pageSize
    }
  })
}
