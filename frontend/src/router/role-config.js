/**
 * 智慧医养平台 - 角色路由权限配置
 * 基于《三级角色权限架构深度分析与优化方案.md》设计
 * 
 * 角色体系:
 * - system_admin: 系统管理员 (技术管理,不涉及业务数据)
 * - business_admin: 业务管理员 (业务数据管理,机构运营)
 * - doctor: 医生 (医疗诊断,健康评估)
 * - nurse: 护工 (日常照护,生活护理)
 * - social_worker: 社工 (心理关怀,活动组织)
 * - family: 家属 (查看关联长辈信息)
 */

/**
 * 角色路由权限映射表
 * 
 * key: 路由名称 (route.name)
 * value: 允许访问的角色列表
 */
export const ROLE_ROUTE_MAP = {
  // ==================== 公共页面 ====================
  // 所有角色都可以访问
  'Dashboard': ['system_admin', 'business_admin', 'doctor', 'nurse', 'social_worker', 'family'],
  'Profile': ['system_admin', 'business_admin', 'doctor', 'nurse', 'social_worker', 'family'],

  // ==================== 系统管理模块 ====================
  // 仅系统管理员可访问
  'SystemUsers': ['system_admin'],
  'SystemRoles': ['system_admin'],
  'SystemPermissions': ['system_admin'],

  // ==================== 老人档案管理模块 ====================
  // 老人档案列表 - 管理员和医生使用完整管理页面
  'ElderlyList': ['business_admin', 'doctor'],
  
  // 家属专用页面 - 仅家属访问
  'ElderlyFamilyView': ['family'],
  
  // 老人详情 - 根据角色有不同的数据权限
  'ElderlyDetail': ['business_admin', 'doctor', 'nurse', 'social_worker', 'family'],
  'ElderlyProfile': ['business_admin', 'doctor', 'nurse', 'social_worker', 'family'],
  
  // 老人档案编辑 - 仅业务管理员
  'ElderlyAdd': ['business_admin'],
  'ElderlyEdit': ['business_admin'],
  
  // 健康档案查看 - 医护和家属
  'ElderlyHealthRecords': ['business_admin', 'doctor', 'nurse', 'family'],

  // ==================== 医生管理模块 ====================
  // 仅业务管理员可访问
  'DoctorList': ['business_admin'],

  // ==================== 工作台页面 ====================
  // 各角色专用工作台
  'DoctorWorkbench': ['doctor'],        // 医生工作台 - 我负责的老人
  'NurseWorkbench': ['nurse'],          // 护工工作台 - 今日护理任务
  'SocialWorkerWorkbench': ['social_worker'], // 社工工作台 - 本周活动计划

  // ==================== 健康管理模块 ====================
  // 健康预警 - 医护和家属查看,医生和护工处理
  'HealthWarning': ['business_admin', 'doctor', 'nurse', 'family'],
  
  // 健康记录 - 医护查看和编辑,家属只读
  'HealthRecords': ['business_admin', 'doctor', 'nurse', 'family'],
  
  // 评估报告 - 医生编辑,护工和家属只读
  'HealthAssessment': ['business_admin', 'doctor', 'nurse', 'family'],

  // ==================== 护理与服务模块 ====================
  // 护理记录 - 护工录入,医生和家属查看
  'NursingRecord': ['business_admin', 'doctor', 'nurse', 'family'],
  
  // 用药记录 - 医生开具,护工执行,家属查看
  'MedicationRecord': ['business_admin', 'doctor', 'nurse', 'family'],
  
  // 巡诊记录 - 医生专用
  'PatrolRecord': ['business_admin', 'doctor'],

  // ==================== 设备管理模块 ====================
  // 业务管理员全部权限,医生查看,护工使用记录
  'EquipmentList': ['business_admin', 'doctor', 'nurse'],
  'EquipmentMonitor': ['business_admin', 'doctor', 'nurse'],

  // ==================== 报表统计模块 ====================
  // 业务管理员查看全部,医护查看个人统计
  'ReportStatistics': ['business_admin', 'doctor', 'nurse', 'social_worker'],
  
  // 数据分析 - 仅业务管理员
  'DataAnalysis': ['business_admin'],
}

/**
 * 角色菜单配置
 * 定义每个角色在侧边栏显示的菜单结构
 */
export const ROLE_MENU_CONFIG = {
  // ==================== 系统管理员菜单 ====================
  system_admin: [
    {
      path: '/dashboard',
      name: 'Dashboard',
      meta: { title: '首页仪表板', icon: 'DataBoard' }
    },
    {
      path: '/system',
      name: 'System',
      meta: { title: '系统管理', icon: 'Setting' },
      children: [
        {
          path: '/system/users',
          name: 'SystemUsers',
          meta: { title: '用户管理', icon: 'User' }
        },
        {
          path: '/system/roles',
          name: 'SystemRoles',
          meta: { title: '角色管理', icon: 'Avatar' }
        },
        {
          path: '/system/permissions',
          name: 'SystemPermissions',
          meta: { title: '权限管理', icon: 'Key' }
        }
      ]
    },
    {
      path: '/profile',
      name: 'Profile',
      meta: { title: '个人中心', icon: 'User' }
    }
  ],

  // ==================== 业务管理员菜单 ====================
  business_admin: [
    {
      path: '/dashboard',
      name: 'Dashboard',
      meta: { title: '首页仪表板', icon: 'DataBoard' }
    },
    {
      path: '/elderly',
      name: 'Elderly',
      meta: { title: '老人档案管理', icon: 'User' },
      children: [
        {
          path: '/elderly/list',
          name: 'ElderlyList',
          meta: { title: '老人档案列表', icon: 'List' }
        }
      ]
    },
    {
      path: '/doctor',
      name: 'Doctor',
      meta: { title: '医生管理', icon: 'Avatar' },
      children: [
        {
          path: '/doctor/list',
          name: 'DoctorList',
          meta: { title: '医生列表', icon: 'List' }
        }
      ]
    },
    {
      path: '/health',
      name: 'Health',
      meta: { title: '健康管理', icon: 'Monitor' },
      children: [
        {
          path: '/health/warning',
          name: 'HealthWarning',
          meta: { title: '健康预警', icon: 'Warning' }
        },
        {
          path: '/health/records',
          name: 'HealthRecords',
          meta: { title: '健康记录', icon: 'Document' }
        },
        {
          path: '/health/assessment',
          name: 'HealthAssessment',
          meta: { title: '评估报告', icon: 'DataAnalysis' }
        }
      ]
    },
    {
      path: '/equipment',
      name: 'Equipment',
      meta: { title: '设备管理', icon: 'Cpu' },
      children: [
        {
          path: '/equipment/list',
          name: 'EquipmentList',
          meta: { title: '设备列表', icon: 'List' }
        },
        {
          path: '/equipment/monitor',
          name: 'EquipmentMonitor',
          meta: { title: '设备监控', icon: 'Monitor' }
        }
      ]
    },
    {
      path: '/reports',
      name: 'Reports',
      meta: { title: '报表统计', icon: 'DataLine' },
      children: [
        {
          path: '/reports/statistics',
          name: 'ReportStatistics',
          meta: { title: '统计报表', icon: 'PieChart' }
        },
        {
          path: '/reports/analysis',
          name: 'DataAnalysis',
          meta: { title: '大数据分析', icon: 'DataAnalysis' }
        }
      ]
    },
    {
      path: '/profile',
      name: 'Profile',
      meta: { title: '个人中心', icon: 'User' }
    }
  ],

  // ==================== 医生菜单 ====================
  doctor: [
    {
      path: '/dashboard',
      name: 'Dashboard',
      meta: { title: '首页仪表板', icon: 'DataBoard' }
    },
    {
      path: '/doctor/workbench',
      name: 'DoctorWorkbench',
      meta: { title: '医生工作台', icon: 'Briefcase' }
    },
    {
      path: '/elderly',
      name: 'Elderly',
      meta: { title: '老人管理', icon: 'User' },
      children: [
        {
          path: '/elderly/list',
          name: 'ElderlyList',
          meta: { title: '我负责的老人', icon: 'List' }
        }
      ]
    },
    {
      path: '/health',
      name: 'Health',
      meta: { title: '健康管理', icon: 'Monitor' },
      children: [
        {
          path: '/health/warning',
          name: 'HealthWarning',
          meta: { title: '健康预警', icon: 'Warning' }
        },
        {
          path: '/health/records',
          name: 'HealthRecords',
          meta: { title: '健康记录', icon: 'Document' }
        },
        {
          path: '/health/assessment',
          name: 'HealthAssessment',
          meta: { title: '评估报告', icon: 'DataAnalysis' }
        }
      ]
    },
    {
      path: '/medical',
      name: 'Medical',
      meta: { title: '医疗记录', icon: 'Document' },
      children: [
        {
          path: '/medication/record',
          name: 'MedicationRecord',
          meta: { title: '用药记录', icon: 'Medicine' }
        },
        {
          path: '/patrol/record',
          name: 'PatrolRecord',
          meta: { title: '巡诊记录', icon: 'Document' }
        }
      ]
    },
    {
      path: '/reports',
      name: 'Reports',
      meta: { title: '个人统计', icon: 'DataLine' },
      children: [
        {
          path: '/reports/statistics',
          name: 'ReportStatistics',
          meta: { title: '工作统计', icon: 'PieChart' }
        }
      ]
    },
    {
      path: '/profile',
      name: 'Profile',
      meta: { title: '个人中心', icon: 'User' }
    }
  ],

  // ==================== 护工菜单 ====================
  nurse: [
    {
      path: '/dashboard',
      name: 'Dashboard',
      meta: { title: '首页仪表板', icon: 'DataBoard' }
    },
    {
      path: '/nurse/workbench',
      name: 'NurseWorkbench',
      meta: { title: '护工工作台', icon: 'Briefcase' }
    },
    {
      path: '/nursing',
      name: 'Nursing',
      meta: { title: '护理管理', icon: 'Document' },
      children: [
        {
          path: '/nursing/record',
          name: 'NursingRecord',
          meta: { title: '护理记录', icon: 'Document' }
        },
        {
          path: '/medication/record',
          name: 'MedicationRecord',
          meta: { title: '用药执行', icon: 'Medicine' }
        }
      ]
    },
    {
      path: '/health/warning',
      name: 'HealthWarning',
      meta: { title: '健康预警', icon: 'Warning' }
    },
    {
      path: '/equipment',
      name: 'Equipment',
      meta: { title: '设备使用', icon: 'Cpu' },
      children: [
        {
          path: '/equipment/list',
          name: 'EquipmentList',
          meta: { title: '设备列表', icon: 'List' }
        }
      ]
    },
    {
      path: '/reports',
      name: 'Reports',
      meta: { title: '个人统计', icon: 'DataLine' },
      children: [
        {
          path: '/reports/statistics',
          name: 'ReportStatistics',
          meta: { title: '工作统计', icon: 'PieChart' }
        }
      ]
    },
    {
      path: '/profile',
      name: 'Profile',
      meta: { title: '个人中心', icon: 'User' }
    }
  ],

  // ==================== 社工菜单 ====================
  social_worker: [
    {
      path: '/dashboard',
      name: 'Dashboard',
      meta: { title: '首页仪表板', icon: 'DataBoard' }
    },
    {
      path: '/social-worker/workbench',
      name: 'SocialWorkerWorkbench',
      meta: { title: '社工工作台', icon: 'Briefcase' }
    },
    {
      path: '/reports',
      name: 'Reports',
      meta: { title: '个人统计', icon: 'DataLine' },
      children: [
        {
          path: '/reports/statistics',
          name: 'ReportStatistics',
          meta: { title: '工作统计', icon: 'PieChart' }
        }
      ]
    },
    {
      path: '/profile',
      name: 'Profile',
      meta: { title: '个人中心', icon: 'User' }
    }
  ],

  // ==================== 家属菜单 ====================
  family: [
    {
      path: '/dashboard',
      name: 'Dashboard',
      meta: { title: '关爱首页', icon: 'HomeFilled' }
    },
    {
      path: '/elderly/family-view',
      name: 'ElderlyFamilyView',
      meta: { title: '我的关联长辈', icon: 'User' }
    },
    {
      path: '/health/warning',
      name: 'HealthWarning',
      meta: { title: '健康提醒', icon: 'Warning' }
    },
    {
      path: '/profile',
      name: 'Profile',
      meta: { title: '个人中心', icon: 'Avatar' }
    }
  ]
}

/**
 * 检查用户角色是否可以访问指定路由
 * @param {string} routeName - 路由名称
 * @param {string} userRole - 用户角色
 * @returns {boolean}
 */
export function canAccessRoute(routeName, userRole) {
  // 公共路由,所有人都可以访问
  const publicRoutes = ['Login', 'Register', 'Home', 'NotFound']
  if (publicRoutes.includes(routeName)) {
    return true
  }

  // 检查路由权限映射表
  const allowedRoles = ROLE_ROUTE_MAP[routeName]
  
  if (!allowedRoles) {
    // 如果路由未在映射表中定义,默认拒绝访问
    console.warn(`[权限检查] 路由 ${routeName} 未定义权限,默认拒绝访问`)
    return false
  }

  return allowedRoles.includes(userRole)
}

/**
 * 获取用户角色对应的菜单配置
 * @param {string} userRole - 用户角色
 * @returns {Array} 菜单配置数组
 */
export function getMenuByRole(userRole) {
  const menus = ROLE_MENU_CONFIG[userRole]
  
  if (!menus) {
    console.warn(`[菜单配置] 角色 ${userRole} 未定义菜单,返回空数组`)
    return []
  }

  return menus
}

/**
 * 角色功能权限配置
 * 定义每个角色的具体操作权限
 */
export const ROLE_PERMISSIONS = {
  system_admin: {
    system: ['user:manage', 'role:manage', 'permission:manage', 'config:manage'],
  },
  
  business_admin: {
    elderly: ['view', 'add', 'edit', 'delete', 'export'],
    doctor: ['view', 'add', 'edit', 'delete'],
    nurse: ['view', 'add', 'edit', 'delete'],
    health: ['view', 'add', 'edit', 'delete', 'warning:handle'],
    equipment: ['view', 'add', 'edit', 'delete', 'monitor'],
    report: ['view', 'export', 'analyze'],
  },
  
  doctor: {
    elderly: ['view'], // 只读,不能编辑老人档案
    health: ['view', 'add', 'edit', 'warning:handle'],
    assessment: ['view', 'add', 'edit'],
    medication: ['view', 'add', 'edit'],
    patrol: ['view', 'add', 'edit'],
    report: ['view', 'export'], // 仅个人统计
  },
  
  nurse: {
    elderly: ['view'], // 只读基础信息
    health: ['view'], // 只读健康信息
    nursing: ['view', 'add', 'edit'], // 护理记录
    medication: ['view', 'execute'], // 用药执行
    equipment: ['view', 'use'], // 设备使用
    report: ['view'], // 仅个人统计
  },
  
  social_worker: {
    elderly: ['view'], // 只读基础信息
    activity: ['view', 'add', 'edit'], // 活动记录
    visit: ['view', 'add', 'edit'], // 访谈记录
    report: ['view'], // 仅个人统计
  },
  
  family: {
    elderly: ['view'], // 仅关联老人
    health: ['view'], // 仅关联老人健康信息
    warning: ['view'], // 仅关联老人预警
  }
}

/**
 * 检查用户角色是否拥有指定权限
 * @param {string} userRole - 用户角色
 * @param {string} permission - 权限标识 (格式: "module:action")
 * @returns {boolean}
 */
export function hasPermission(userRole, permission) {
  const rolePerms = ROLE_PERMISSIONS[userRole]
  
  if (!rolePerms) {
    return false
  }

  const [module, action] = permission.split(':')
  const modulePerms = rolePerms[module]

  if (!modulePerms) {
    return false
  }

  return modulePerms.includes(action) || modulePerms.includes('*')
}
