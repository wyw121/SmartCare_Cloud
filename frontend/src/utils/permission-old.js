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
    'ElderlyList', 'ElderlyProfile', 'ElderlyAdd', 'ElderlyEdit',
    'HealthWarning', 'HealthWarningModular', 'HealthRecords', 'HealthAssessment',
    'EquipmentList', 'EquipmentMonitor',
    'ReportStatistics', 'DataAnalysis', 'ModularDataAnalysis',
    'DoctorList', 'DoctorModular'
  ]
  
  if (userRole === 'doctor' && doctorRoutes.includes(routeName)) {
    return true
  }
  
  // 家属权限页面（查看权限）
  const familyRoutes = [
    'ElderlyList', 'ElderlyProfile', // 只能查看
    'HealthWarning', 'HealthRecords', // 只能查看相关老人的
    'ReportStatistics' // 基础报表查看
  ]
  
  if (userRole === 'family' && familyRoutes.includes(routeName)) {
    return true
  }
  
  // 系统管理页面仅管理员可访问
  const adminOnlyRoutes = [
    'SystemUsers', 'SystemModularUsers', 'SystemRoles', 'SystemPermissions'
  ]
  
  if (adminOnlyRoutes.includes(routeName) && userRole !== 'admin') {
    return false
  }
  
  return false
}

/**
 * 检查路由访问权限（路由对象版本）
 * @param {object} route - 路由对象
 * @param {object} userInfo - 用户信息
 * @returns {boolean}
 */
export function canAccessRouteObject(route, userInfo = null) {
  const userStore = useUserStore()
  const user = userInfo || userStore.userInfo
  
  if (!route.meta) return true
  
  // 检查角色权限
  if (route.meta.roles && route.meta.roles.length > 0) {
    if (!user.role || !route.meta.roles.includes(user.role)) {
      return false
    }
  }
  
  // 检查权限
  if (route.meta.permissions && route.meta.permissions.length > 0) {
    const userPermissions = user.permissions || []
    
    // 管理员拥有所有权限
    if (userPermissions.includes('*')) {
      return true
    }
    
    // 检查是否有任意一个权限
    const hasAnyPerm = route.meta.permissions.some(p => userPermissions.includes(p))
    if (!hasAnyPerm) {
      return false
    }
  }
  
  return true
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
 * 检查是否是管理员
 * @returns {boolean}
 */
export function isAdmin() {
  return hasRole('admin')
}

/**
 * 检查是否是医生
 * @returns {boolean}
 */
export function isDoctor() {
  return hasRole('doctor')
}

/**
 * 检查是否是家属
 * @returns {boolean}
 */
export function isFamily() {
  return hasRole('family')
}

/**
 * 获取角色权限配置
 */
export const ROLE_PERMISSIONS = {
  admin: {
    name: '系统管理员',
    permissions: ['*'],
    routes: ['dashboard', 'elderly', 'doctor', 'health-warning', 'reports', 'system', 'profile'],
    description: '拥有系统全部权限'
  },
  doctor: {
    name: '医生',
    permissions: [
      'elderly:view', 'elderly:edit',
      'health:manage', 'health:view', 'health:edit',
      'report:view', 'report:export',
      'dashboard:view'
    ],
    routes: ['dashboard', 'elderly', 'health-warning', 'reports', 'profile'],
    description: '医疗管理相关权限'
  },
  family: {
    name: '家属',
    permissions: [
      'elderly:view',
      'health:view',
      'report:view'
    ],
    routes: ['dashboard', 'elderly', 'health-warning', 'profile'],
    description: '查看老人信息和健康状况'
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
  
  const allowedRoutes = ROLE_PERMISSIONS[userRole].routes
  
  return menus.filter(menu => {
    // 如果是管理员，显示所有菜单
    if (userRole === 'admin') {
      return true
    }
    
    // 检查菜单是否在允许的路由中
    return allowedRoutes.includes(menu.name)
  })
}

/**
 * 检查页面访问权限
 * @param {string} routeName 路由名称
 * @param {string} userRole 用户角色
 * @returns {boolean}
 */
export function canAccessRoute(routeName, userRole) {
  if (!userRole || !ROLE_PERMISSIONS[userRole]) {
    return false
  }
  
  // 管理员可以访问所有页面
  if (userRole === 'admin') {
    return true
  }
  
  const allowedRoutes = ROLE_PERMISSIONS[userRole].routes
  return allowedRoutes.includes(routeName)
}

/**
 * 权限指令工具
 */
export const permissionDirective = {
  mounted(el, binding) {
    const { value } = binding
    if (value && !hasPermission(value)) {
      el.style.display = 'none'
    }
  },
  updated(el, binding) {
    const { value } = binding
    if (value && !hasPermission(value)) {
      el.style.display = 'none'
    } else {
      el.style.display = ''
    }
  }
}

/**
 * 角色指令工具
 */
export const roleDirective = {
  mounted(el, binding) {
    const { value } = binding
    if (value && !hasRole(value)) {
      el.style.display = 'none'
    }
  },
  updated(el, binding) {
    const { value } = binding
    if (value && !hasRole(value)) {
      el.style.display = 'none'
    } else {
      el.style.display = ''
    }
  }
}
