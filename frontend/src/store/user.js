import { auth } from '@/api'
import { defineStore } from 'pinia'

// 智慧医养平台三角色权限数据配置
const ROLE_DATA = {
  admin: {
    id: 1,
    username: 'admin',
    password: '123456',
    name: '系统管理员',
    role: 'admin',
    roleText: '系统管理员',
    permissions: ['*'], // 拥有所有权限
    avatar: '',
    department: '系统管理部',
    phone: '13800138000',
    email: 'admin@smartcare.com',
    description: '拥有系统全部管理权限，负责平台整体运营管理',
    features: {
      // 系统管理功能
      system: ['user:manage', 'role:manage', 'permission:manage', 'config:manage'],
      // 老人档案管理
      elderly: ['view', 'add', 'edit', 'delete', 'export'],
      // 健康管理
      health: ['view', 'add', 'edit', 'delete', 'manage', 'warning:handle'],
      // 评估报告
      assessment: ['view', 'add', 'edit', 'delete', 'export'],
      // 设备管理
      equipment: ['view', 'add', 'edit', 'delete', 'monitor'],
      // 报表统计
      report: ['view', 'export', 'analyze'],
      // 数据分析
      analysis: ['view', 'manage']
    }
  },
  doctor: {
    id: 2,
    username: 'doctor',
    password: '123456',
    name: '张医生',
    role: 'doctor',
    roleText: '医生',
    permissions: [
      // 基础权限
      'dashboard:view',
      // 老人档案权限
      'elderly:view', 'elderly:add', 'elderly:edit', 'elderly:export',
      // 健康管理权限
      'health:view', 'health:add', 'health:edit', 'health:manage',
      // 预警处理权限
      'health-warning:view', 'health-warning:handle', 'health-warning:export',
      // 评估报告权限
      'assessment:view', 'assessment:add', 'assessment:edit', 'assessment:export',
      // 设备权限
      'equipment:view', 'equipment:monitor',
      // 报表权限
      'report:view', 'report:export',
      // 数据分析权限
      'analysis:view'
    ],
    avatar: '',
    department: '内科',
    title: '主治医师',
    phone: '13800138001',
    email: 'doctor@smartcare.com',
    description: '负责老人医疗健康管理，处理健康预警和评估',
    features: {
      elderly: ['view', 'add', 'edit', 'export'],
      health: ['view', 'add', 'edit', 'manage'],
      assessment: ['view', 'add', 'edit', 'export'],
      equipment: ['view', 'monitor'],
      report: ['view', 'export'],
      analysis: ['view']
    }
  },
  family: {
    id: 3,
    username: 'family',
    password: '123456',
    name: '李家属',
    role: 'family',
    roleText: '家属',
    permissions: [
      // 基础查看权限
      'dashboard:view',
      // 老人档案查看权限（仅限关联老人）
      'elderly:view',
      // 健康信息查看权限（仅限关联老人）
      'health:view',
      // 预警查看权限（仅限关联老人）
      'health-warning:view',
      // 报表查看权限
      'report:view'
    ],
    avatar: '',
    relationship: '儿子',
    elderlyIds: [1, 2], // 关联的老人ID列表
    phone: '13800138002',
    email: 'family@smartcare.com',
    description: '家属用户，可查看关联老人的健康信息和预警',
    features: {
      elderly: ['view'], // 只能查看关联老人
      health: ['view'],  // 只能查看关联老人健康信息
      report: ['view']   // 只能查看基础报表
    }
  }
}

export const useUserStore = defineStore('user', {
  state: () => {
    // 开发环境自动登录
    let token = localStorage.getItem('token') || ''
    let userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    
    // 如果是开发环境且没有登录，自动登录为admin
    if (import.meta.env.DEV && !token) {
      const adminData = ROLE_DATA.admin
      token = `dev_auto_token_${Date.now()}`
      userInfo = { ...adminData }
      
      localStorage.setItem('token', token)
      localStorage.setItem('userInfo', JSON.stringify(adminData))
    }
    
    return {
      token,
      userInfo,
      roles: [],
      permissions: []
    }
  },

  getters: {
    isLoggedIn: (state) => !!state.token,
    userName: (state) => state.userInfo.name || '',
    userAvatar: (state) => state.userInfo.avatar || '',
    userRole: (state) => state.userInfo.role || 'user',
    userRoleText: (state) => state.userInfo.roleText || '用户',
    userPermissions: (state) => state.userInfo.permissions || [],
    userDepartment: (state) => state.userInfo.department || '',
    userPhone: (state) => state.userInfo.phone || '',
    userEmail: (state) => state.userInfo.email || '',
    
    // 权限检查方法
    hasPermission: (state) => (permission) => {
      const permissions = state.userInfo.permissions || []
      return permissions.includes('*') || permissions.includes(permission)
    },
    
    // 角色检查方法
    hasRole: (state) => (role) => {
      return state.userInfo.role === role
    },
    
    // 检查是否为管理员
    isAdmin: (state) => state.userInfo.role === 'admin',
    
    // 检查是否为医生
    isDoctor: (state) => state.userInfo.role === 'doctor',
    
    // 检查是否为家属
    isFamily: (state) => state.userInfo.role === 'family'
  },

  actions: {
    // 登录
    async login(loginForm) {
      try {
        // 先尝试真实API登录
        const response = await auth.login(loginForm)
        
        if (response.code === 200) {
          const { token, userInfo } = response.data
          
          this.token = token
          this.userInfo = userInfo
          this.permissions = userInfo.permissions || []
          
          // 存储到本地
          localStorage.setItem('token', token)
          localStorage.setItem('userInfo', JSON.stringify(userInfo))
          
          return response
        } else {
          throw new Error(response.message || '登录失败')
        }
      } catch (error) {
        console.error('真实API登录失败，尝试模拟登录:', error)
        
        // API失败时，回退到模拟登录
        const roleData = Object.values(ROLE_DATA).find(
          user => user.username === loginForm.username && user.password === loginForm.password
        )
        
        if (!roleData) {
          throw new Error('用户名或密码错误')
        }
        
        // 模拟生成token
        const token = `mock_token_${roleData.role}_${Date.now()}`
        
        this.token = token
        this.userInfo = { ...roleData }
        this.permissions = roleData.permissions
        
        // 存储到本地
        localStorage.setItem('token', token)
        localStorage.setItem('userInfo', JSON.stringify(roleData))
        
        return { data: { token, userInfo: roleData } }
      }
    },

    // 快速角色登录
    async quickLogin(role) {
      const roleData = ROLE_DATA[role]
      if (!roleData) {
        throw new Error('角色不存在')
      }
      
      return this.login({
        username: roleData.username,
        password: roleData.password
      })
    },

    // 获取用户信息
    async getUserInfo() {
      try {
        // 尝试调用真实API获取用户信息
        const response = await auth.getUserInfo()
        
        if (response.code === 200) {
          const { userInfo, roles, permissions } = response.data
          this.userInfo = userInfo
          this.roles = roles || []
          this.permissions = permissions || []
          localStorage.setItem('userInfo', JSON.stringify(userInfo))
          return response
        } else {
          throw new Error(response.message || '获取用户信息失败')
        }
      } catch (error) {
        console.error('真实API获取用户信息失败，使用本地缓存:', error)
        
        // API失败时，返回本地存储的用户信息
        if (this.userInfo && this.userInfo.id) {
          return { 
            data: { 
              userInfo: this.userInfo, 
              roles: [this.userInfo.role], 
              permissions: this.userInfo.permissions 
            } 
          }
        }
        
        throw new Error('用户信息获取失败')
      }
    },

    // 登出
    async logout() {
      try {
        // 调用真实API登出
        await auth.logout()
      } catch (error) {
        console.error('API登出失败:', error)
      } finally {
        // 无论API是否成功，都清理本地数据
        this.token = ''
        this.userInfo = {}
        this.roles = []
        this.permissions = []
        
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
      }
    },

    // 重置状态
    resetState() {
      this.token = ''
      this.userInfo = {}
      this.roles = []
      this.permissions = []
    }
  }
})
