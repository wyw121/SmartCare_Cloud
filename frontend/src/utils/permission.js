/**
 * 智慧医养平台权限管理工具
 * 支持三类角色的权限检查：管理员、医生、家属
 */

import { useUserStore } from '@/store/user'

/**
 * 检查用户是否有指定权限
 * @param {string|array} permission - 权限标识或权限数组
 * @returns {boolean}
 */
export function hasPermission(permission) {
  const userStore = useUserStore()
  const userPermissions = userStore.userPermissions || []
  
  // 管理员拥有所有权限
  if (userPermissions.includes('*')) {
    return true
  }
  
  // 检查单个权限
  if (typeof permission === 'string') {
    return userPermissions.includes(permission)
  }
  
  // 检查权限数组（需要包含所有权限）
  if (Array.isArray(permission)) {
    return permission.every(p => userPermissions.includes(p))
  }
  
  return false
}

/**
 * 检查用户是否有指定权限（任意一个）
 * @param {array} permissions - 权限数组
 * @returns {boolean}
 */
export function hasAnyPermission(permissions) {
  const userStore = useUserStore()
  const userPermissions = userStore.userPermissions || []
  
  // 管理员拥有所有权限
  if (userPermissions.includes('*')) {
    return true
  }
  
  return permissions.some(p => userPermissions.includes(p))
}

/**
 * 检查用户角色
 * @param {string|array} role - 角色标识或角色数组
 * @returns {boolean}
 */
export function hasRole(role) {
  const userStore = useUserStore()
  const userRole = userStore.userRole
  
  if (typeof role === 'string') {
    return userRole === role
  }
  
  if (Array.isArray(role)) {
    return role.includes(userRole)
  }
  
  return false
}

/**
 * 检查路由访问权限（简化版）
 * @param {string} routeName - 路由名称
 * @param {string} userRole - 用户角色
 * @returns {boolean}
 */
export function canAccessRoute(routeName, userRole) {
  // 管理员可以访问所有页面
  if (userRole === 'admin') {
    return true
  }
  
  // 公共页面，所有角色都可以访问
  const publicRoutes = [
    'Dashboard', 'Profile', 'Home', 'ElderlyModular', 'DashboardModular'
  ]
  
  if (publicRoutes.includes(routeName)) {
    return true
  }
  
  // 医生权限页面
  const doctorRoutes = [
    'ElderlyList', 'ElderlyProfile', 'ElderlyAdd', 'ElderlyEdit', 'ElderlyDetail',
    'ElderlyHealthRecords', 'ElderlyModular',
    'HealthWarning', 'HealthWarningModular', 'HealthRecords', 'HealthAssessment',
    'EquipmentList', 'EquipmentMonitor',
    'ReportStatistics', 'DataAnalysis', 'ModularDataAnalysis',
    'DoctorList', 'DoctorModular', 'KeyPopulation'
  ]
  
  if (userRole === 'doctor' && doctorRoutes.includes(routeName)) {
    return true
  }
  
  // 家属权限页面（严格限制，只能查看关联老人信息）
  const familyRoutes = [
    'Dashboard', 'DashboardModular', // 基础仪表板
    'ElderlyFamilyView', 'ElderlyProfile', 'ElderlyDetail', // 家属专用老人查看页面
    'ElderlyHealthRecords', // 健康档案查看
    'HealthWarning', 'HealthWarningPage', // 健康预警查看
    'Profile' // 个人资料
  ]
  
  if (userRole === 'family' && familyRoutes.includes(routeName)) {
    return true
  }
  
  // 系统管理页面仅管理员可访问
  const adminOnlyRoutes = [
    'SystemUsers', 'SystemModularUsers', 'SystemRoles', 'SystemPermissions',
    'UserManagement', 'RoleManagement', 'PermissionManagement'
  ]
  
  // 非管理员不能访问系统管理页面
  return !(adminOnlyRoutes.includes(routeName) && userRole !== 'admin')
}

/**
 * Vue指令：v-permission
 * 用法：v-permission="'user:create'" 或 v-permission="['user:create', 'user:edit']"
 */
export const permissionDirective = {
  mounted(el, binding) {
    const { value } = binding
    
    if (value) {
      const hasAuth = hasPermission(value)
      if (!hasAuth) {
        el.style.display = 'none'
      }
    }
  },
  updated(el, binding) {
    const { value } = binding
    
    if (value) {
      const hasAuth = hasPermission(value)
      el.style.display = hasAuth ? '' : 'none'
    }
  }
}

/**
 * Vue指令：v-role
 * 用法：v-role="'admin'" 或 v-role="['admin', 'doctor']"
 */
export const roleDirective = {
  mounted(el, binding) {
    const { value } = binding
    
    if (value) {
      const hasAuth = hasRole(value)
      if (!hasAuth) {
        el.style.display = 'none'
      }
    }
  },
  updated(el, binding) {
    const { value } = binding
    
    if (value) {
      const hasAuth = hasRole(value)
      el.style.display = hasAuth ? '' : 'none'
    }
  }
}

/**
 * 角色权限映射表
 * 定义智慧医养平台三个角色的详细权限配置
 */
export const ROLE_PERMISSIONS = {
  admin: {
    name: '系统管理员',
    permissions: ['*'], // 所有权限
    routes: ['*'], // 所有路由
    description: '系统管理员，拥有系统全部管理权限',
    features: {
      elderly: ['view', 'add', 'edit', 'delete', 'export'],
      health: ['view', 'add', 'edit', 'delete', 'manage', 'export'],
      user: ['view', 'add', 'edit', 'delete', 'reset-password'],
      role: ['view', 'add', 'edit', 'delete', 'assign-permissions'],
      permission: ['view', 'add', 'edit', 'delete'],
      equipment: ['view', 'add', 'edit', 'delete', 'monitor'],
      report: ['view', 'export', 'analyze'],
      system: ['config', 'log', 'backup']
    }
  },
  doctor: {
    name: '医生',
    permissions: [
      'dashboard:view',
      'elderly:view', 'elderly:add', 'elderly:edit', 'elderly:export',
      'health:view', 'health:add', 'health:edit', 'health:manage',
      'health-warning:view', 'health-warning:handle', 'health-warning:export',
      'assessment:view', 'assessment:add', 'assessment:edit',
      'equipment:view', 'equipment:monitor',
      'report:view', 'report:export',
      'doctor:view', 'doctor:edit'
    ],
    routes: [
      'Dashboard', 'DashboardModular',
      'ElderlyList', 'ElderlyAdd', 'ElderlyEdit', 'ElderlyDetail', 'ElderlyModular',
      'HealthWarning', 'HealthWarningModular', 'HealthRecords', 'HealthAssessment',
      'EquipmentList', 'EquipmentMonitor',
      'ReportStatistics', 'DataAnalysis', 'ModularDataAnalysis',
      'DoctorList', 'DoctorModular', 'KeyPopulation',
      'Profile'
    ],
    description: '医生角色，拥有医疗管理相关权限',
    features: {
      elderly: ['view', 'add', 'edit', 'export'],
      health: ['view', 'add', 'edit', 'manage', 'export'],
      assessment: ['view', 'add', 'edit'],
      equipment: ['view', 'monitor'],
      report: ['view', 'export'],
      doctor: ['view', 'edit']
    }
  },
  family: {
    name: '家属',
    permissions: [
      'dashboard:view-basic', // 基础仪表板，不含敏感统计
      'elderly:view-related', // 只能查看关联老人的基本信息
      'health:view-summary', // 只能查看关联老人的健康概要（脱敏）
      'health-warning:view-related', // 只能查看关联老人的紧急预警
      'contact:emergency', // 紧急联系功能
      'visit:schedule', // 探视预约功能
      'profile:view' // 个人资料查看
    ],
    routes: [
      'Dashboard', 'DashboardModular', // 基础仪表板
      'FamilyElderlyList', 'FamilyElderlyDetail', // 专用的家属查看页面
      'FamilyHealthSummary', // 健康概要页面（脱敏）
      'FamilyEmergencyContact', // 紧急联系页面
      'FamilyVisitSchedule', // 探视预约页面
      'Profile' // 个人资料
    ],
    description: '家属角色，只能查看关联老人的基本信息和健康概要，具有紧急联系权限',
    features: {
      elderly: ['view-basic'], // 只能查看关联老人的基本信息（姓名、年龄等）
      health: ['view-summary'], // 只能查看健康概要，不包含详细医疗记录
      emergency: ['contact', 'alert'], // 紧急情况联系和接收预警
      visit: ['schedule', 'view'], // 探视预约和查看
      profile: ['view', 'edit-basic'] // 个人资料基本编辑
    },
    restrictions: {
      // 数据访问限制
      dataAccess: 'related-only', // 只能访问关联老人的数据
      medicalRecords: 'summary-only', // 只能查看医疗记录概要
      personalInfo: 'basic-only', // 只能查看基本个人信息
      sensitiveData: 'masked', // 敏感数据脱敏显示
      // 功能限制
      editPermission: false, // 不能编辑任何信息
      deletePermission: false, // 不能删除任何信息
      exportPermission: false, // 不能导出数据
      systemAccess: false // 不能访问系统管理功能
    }
  }
}

/**
 * 根据角色过滤菜单
 * @param {array} menus 菜单数组
 * @param {string} userRole 用户角色
 * @returns {array} 过滤后的菜单
 */
export function filterMenusByRole(menus, userRole) {
  if (!userRole || !ROLE_PERMISSIONS[userRole]) {
    return []
  }
  
  return menus.filter(menu => {
    // 如果菜单项定义了roles属性，则检查当前用户角色是否在允许列表中
    if (menu.roles && Array.isArray(menu.roles)) {
      return menu.roles.includes(userRole)
    }
    
    // 如果没有定义roles，则使用默认的角色路由配置
    const allowedRoutes = ROLE_PERMISSIONS[userRole].routes
    
    // 如果是管理员，显示所有菜单
    if (userRole === 'admin') {
      return true
    }
    
    // 检查菜单是否在允许的路由中
    return allowedRoutes.includes(menu.name)
  })
}

/**
 * 检查是否是管理员
 * @returns {boolean}
 */
export function isAdmin() {
  const userStore = useUserStore()
  return userStore.userRole === 'admin'
}

/**
 * 检查是否是医生
 * @returns {boolean}
 */
export function isDoctor() {
  const userStore = useUserStore()
  return userStore.userRole === 'doctor'
}

/**
 * 检查家属是否可以查看指定老人信息
 * @param {number} elderlyId 老人ID
 * @param {number} familyId 家属ID
 * @returns {boolean}
 */
export function canFamilyViewElderly(elderlyId, familyId) {
  // 这里应该检查数据库中的关联关系
  // 暂时返回true，实际项目中需要调用后端API验证
  return true
}

/**
 * 检查家属是否可以查看健康信息
 * @param {number} elderlyId 老人ID
 * @returns {boolean}
 */
export function canFamilyViewHealth(elderlyId) {
  const userStore = useUserStore()
  if (userStore.userRole !== 'family') {
    return false
  }
  
  // 检查是否有关联关系
  return canFamilyViewElderly(elderlyId, userStore.userId)
}

/**
 * 检查家属是否可以接收紧急联系
 * @returns {boolean}
 */
export function canFamilyReceiveEmergency() {
  const userStore = useUserStore()
  return userStore.userRole === 'family' && hasPermission('contact:emergency')
}

/**
 * 获取家属可查看的老人ID列表
 * @param {number} familyId 家属ID
 * @returns {Array} 老人ID列表
 */
export function getFamilyRelatedElderlyIds(familyId) {
  // 这里应该从后端获取关联的老人ID列表
  // 暂时返回空数组，实际项目中需要调用后端API
  return []
}

/**
 * 对敏感信息进行脱敏处理
 * @param {Object} data 原始数据
 * @param {string} type 数据类型
 * @returns {Object} 脱敏后的数据
 */
export function maskSensitiveData(data, type) {
  if (!data) return data
  
  const maskedData = { ...data }
  
  switch (type) {
    case 'health':
      // 健康信息脱敏：隐藏详细病历，只显示概要
      if (maskedData.diagnosis) {
        maskedData.diagnosis = maskedData.diagnosis.replace(/\d{4}-\d{2}-\d{2}/g, '****-**-**')
      }
      if (maskedData.medication) {
        maskedData.medication = '用药信息已隐藏，如需详细信息请联系医生'
      }
      break
    case 'personal':
      // 个人信息脱敏：隐藏身份证号等敏感信息
      if (maskedData.idCard) {
        maskedData.idCard = maskedData.idCard.replace(/(\d{6})\d{8}(\d{4})/, '$1********$2')
      }
      if (maskedData.phone) {
        maskedData.phone = maskedData.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
      }
      break
    case 'medical':
      // 医疗记录脱敏：只显示概要信息
      if (maskedData.details) {
        maskedData.details = '详细医疗记录已隐藏，如需查看请联系医生'
      }
      break
  }
  
  return maskedData
}

/**
 * 检查是否是家属
 * @returns {boolean}
 */
export function isFamily() {
  const userStore = useUserStore()
  return userStore.userRole === 'family'
}

/**
 * 检查是否可以编辑老人信息
 * @returns {boolean}
 */
export function canEditElderly() {
  return hasAnyPermission(['elderly:edit', 'elderly:add'])
}

/**
 * 检查是否可以删除老人信息
 * @returns {boolean}
 */
export function canDeleteElderly() {
  return hasPermission('elderly:delete')
}

/**
 * 检查是否可以处理健康预警
 * @returns {boolean}
 */
export function canHandleHealthWarning() {
  return hasPermission('health-warning:handle')
}

/**
 * 检查是否可以管理用户
 * @returns {boolean}
 */
export function canManageUsers() {
  return hasAnyPermission(['user:add', 'user:edit', 'user:delete'])
}

/**
 * 检查是否可以管理角色权限
 * @returns {boolean}
 */
export function canManageRoles() {
  return hasAnyPermission(['role:add', 'role:edit', 'role:delete', 'role:assign-permissions'])
}

/**
 * 检查特定功能权限
 * @param {string} module 模块名称 (elderly, health, user, role, etc.)
 * @param {string} action 操作名称 (view, add, edit, delete, etc.)
 * @returns {boolean}
 */
export function canAccess(module, action) {
  const userStore = useUserStore()
  const userRole = userStore.userRole
  
  if (!userRole || !ROLE_PERMISSIONS[userRole]) {
    return false
  }
  
  const roleConfig = ROLE_PERMISSIONS[userRole]
  
  // 管理员拥有所有权限
  if (userRole === 'admin') {
    return true
  }
  
  // 检查具体功能权限
  if (roleConfig.features && roleConfig.features[module]) {
    return roleConfig.features[module].includes(action)
  }
  
  return false
}
