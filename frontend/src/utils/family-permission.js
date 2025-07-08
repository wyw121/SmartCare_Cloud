/**
 * 家属权限专用工具
 * 实现符合医疗信息隐私保护标准的家属权限控制
 */

import { useUserStore } from '@/store/user'
import { hasPermission } from './permission'

/**
 * 家属权限常量定义
 */
export const FAMILY_PERMISSIONS = {
  // 基础查看权限
  VIEW_BASIC: 'elderly:view-related',
  VIEW_HEALTH_SUMMARY: 'health:view-summary',
  VIEW_EMERGENCY_ALERTS: 'health-warning:view-related',
  
  // 交互权限
  EMERGENCY_CONTACT: 'contact:emergency',
  VISIT_SCHEDULE: 'visit:schedule',
  
  // 个人资料权限
  VIEW_PROFILE: 'profile:view',
  EDIT_BASIC_PROFILE: 'profile:edit-basic'
}

/**
 * 家属数据访问限制
 */
export const FAMILY_DATA_RESTRICTIONS = {
  // 老人信息访问级别
  ELDERLY_INFO: {
    basic: ['name', 'age', 'gender', 'room', 'status'], // 基本信息
    contact: ['phone', 'emergency_contact'], // 联系信息（脱敏）
    forbidden: ['id_card', 'medical_history', 'financial_info'] // 禁止访问
  },
  
  // 健康信息访问级别
  HEALTH_INFO: {
    summary: ['general_status', 'last_checkup', 'alert_level'], // 概要信息
    alerts: ['emergency_alerts', 'medication_reminders'], // 预警信息
    forbidden: ['detailed_diagnosis', 'prescription_details', 'lab_results'] // 禁止访问
  },
  
  // 时间限制
  TIME_RESTRICTIONS: {
    history_days: 30, // 只能查看30天内的记录
    future_days: 7 // 只能查看7天内的预约
  }
}

/**
 * 检查家属是否可以访问指定老人的信息
 * @param {number} elderlyId 老人ID
 * @param {number} familyId 家属ID（可选，默认使用当前用户）
 * @returns {Promise<boolean>}
 */
export async function checkFamilyElderlyAccess(elderlyId, familyId = null) {
  const userStore = useUserStore()
  
  // 检查是否是家属角色
  if (userStore.userRole !== 'family') {
    return false
  }
  
  const currentFamilyId = familyId || userStore.userId
  
  try {
    // 调用后端API检查关联关系
    const response = await fetch(`/api/family/check-elderly-access/${elderlyId}/${currentFamilyId}`)
    const result = await response.json()
    return result.success && result.data.hasAccess
  } catch (error) {
    console.error('检查家属权限失败:', error)
    return false
  }
}

/**
 * 获取家属关联的老人列表
 * @param {number} familyId 家属ID（可选）
 * @returns {Promise<Array>}
 */
export async function getFamilyElderlyList(familyId = null) {
  const userStore = useUserStore()
  
  if (userStore.userRole !== 'family') {
    return []
  }
  
  const currentFamilyId = familyId || userStore.userId
  
  try {
    const response = await fetch(`/api/family/elderly-list/${currentFamilyId}`)
    const result = await response.json()
    
    if (result.success) {
      // 对返回的数据进行脱敏处理
      return result.data.map(elderly => filterElderlyDataForFamily(elderly))
    }
    
    return []
  } catch (error) {
    console.error('获取家属关联老人列表失败:', error)
    return []
  }
}

/**
 * 过滤老人数据，只保留家属可查看的信息
 * @param {Object} elderlyData 原始老人数据
 * @returns {Object} 过滤后的数据
 */
export function filterElderlyDataForFamily(elderlyData) {
  const allowedFields = FAMILY_DATA_RESTRICTIONS.ELDERLY_INFO.basic
  const contactFields = FAMILY_DATA_RESTRICTIONS.ELDERLY_INFO.contact
  
  const filteredData = {}
  
  // 添加基本信息
  allowedFields.forEach(field => {
    if (elderlyData[field] !== undefined) {
      filteredData[field] = elderlyData[field]
    }
  })
  
  // 添加联系信息（脱敏）
  contactFields.forEach(field => {
    if (elderlyData[field] !== undefined) {
      filteredData[field] = maskContactInfo(elderlyData[field], field)
    }
  })
  
  return filteredData
}

/**
 * 过滤健康数据，只保留家属可查看的信息
 * @param {Object} healthData 原始健康数据
 * @returns {Object} 过滤后的数据
 */
export function filterHealthDataForFamily(healthData) {
  const allowedFields = FAMILY_DATA_RESTRICTIONS.HEALTH_INFO.summary
  const alertFields = FAMILY_DATA_RESTRICTIONS.HEALTH_INFO.alerts
  
  const filteredData = {}
  
  // 添加概要信息
  allowedFields.forEach(field => {
    if (healthData[field] !== undefined) {
      filteredData[field] = healthData[field]
    }
  })
  
  // 添加预警信息
  alertFields.forEach(field => {
    if (healthData[field] !== undefined) {
      filteredData[field] = healthData[field]
    }
  })
  
  // 添加脱敏后的概要描述
  if (healthData.diagnosis) {
    filteredData.health_summary = generateHealthSummary(healthData)
  }
  
  return filteredData
}

/**
 * 生成健康概要信息（脱敏）
 * @param {Object} healthData 原始健康数据
 * @returns {string} 健康概要
 */
function generateHealthSummary(healthData) {
  const statusMap = {
    good: '健康状况良好',
    fair: '健康状况一般，需注意',
    poor: '健康状况需要关注，建议联系医生',
    critical: '健康状况紧急，请立即联系医生'
  }
  
  return statusMap[healthData.general_status] || '健康状况信息不完整'
}

/**
 * 联系信息脱敏处理
 * @param {string} value 原始值
 * @param {string} type 类型
 * @returns {string} 脱敏后的值
 */
function maskContactInfo(value, type) {
  if (!value) return value
  
  switch (type) {
    case 'phone':
      return value.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
    case 'emergency_contact':
      // 紧急联系人信息部分脱敏
      if (typeof value === 'object') {
        return {
          name: value.name || '未设置',
          phone: value.phone ? value.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') : '未设置',
          relationship: value.relationship || '未设置'
        }
      }
      return value
    default:
      return value
  }
}

/**
 * 检查家属是否可以查看指定时间范围的数据
 * @param {Date} startDate 开始时间
 * @param {Date} endDate 结束时间
 * @returns {boolean}
 */
export function checkFamilyTimeAccess(startDate, endDate) {
  const now = new Date()
  const restrictions = FAMILY_DATA_RESTRICTIONS.TIME_RESTRICTIONS
  
  // 检查历史数据访问限制
  const earliestAllowed = new Date(now - restrictions.history_days * 24 * 60 * 60 * 1000)
  if (startDate < earliestAllowed) {
    return false
  }
  
  // 检查未来数据访问限制
  const latestAllowed = new Date(now + restrictions.future_days * 24 * 60 * 60 * 1000)
  if (endDate > latestAllowed) {
    return false
  }
  
  return true
}

/**
 * 记录家属操作日志
 * @param {string} action 操作类型
 * @param {Object} details 操作详情
 */
export async function logFamilyAction(action, details = {}) {
  const userStore = useUserStore()
  
  if (userStore.userRole !== 'family') {
    return
  }
  
  const logData = {
    familyId: userStore.userId,
    action,
    details,
    timestamp: new Date().toISOString(),
    ip: await getClientIP()
  }
  
  try {
    await fetch('/api/family/log-action', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(logData)
    })
  } catch (error) {
    console.error('记录家属操作日志失败:', error)
  }
}

/**
 * 获取客户端IP地址
 * @returns {Promise<string>}
 */
async function getClientIP() {
  try {
    const response = await fetch('/api/common/get-client-ip')
    const result = await response.json()
    return result.ip || 'unknown'
  } catch (error) {
    return 'unknown'
  }
}

/**
 * 家属权限验证装饰器
 * @param {Function} apiCall 原始API调用
 * @param {string} permission 需要的权限
 * @returns {Function} 包装后的API调用
 */
export function withFamilyPermission(apiCall, permission) {
  return async (...args) => {
    const userStore = useUserStore()
    
    // 检查是否是家属角色
    if (userStore.userRole !== 'family') {
      throw new Error('权限不足：非家属用户')
    }
    
    // 检查具体权限
    if (!hasPermission(permission)) {
      throw new Error(`权限不足：缺少 ${permission} 权限`)
    }
    
    // 记录操作日志
    await logFamilyAction('api_call', {
      permission,
      args: args.map(arg => typeof arg === 'object' ? JSON.stringify(arg) : arg)
    })
    
    // 执行原始API调用
    return await apiCall(...args)
  }
}

export default {
  FAMILY_PERMISSIONS,
  FAMILY_DATA_RESTRICTIONS,
  checkFamilyElderlyAccess,
  getFamilyElderlyList,
  filterElderlyDataForFamily,
  filterHealthDataForFamily,
  checkFamilyTimeAccess,
  logFamilyAction,
  withFamilyPermission
}
