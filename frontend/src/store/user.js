import { defineStore } from 'pinia'

// 预定义的三类角色数据 - 智慧医养平台RBAC
const ROLE_DATA = {
  admin: {
    id: 1,
    username: 'admin',
    password: '123456',
    name: '系统管理员',
    role: 'admin',
    roleText: '管理员',
    permissions: ['*'], // 所有权限
    avatar: '',
    department: '系统管理部',
    phone: '13800138000',
    email: 'admin@smartcare.com',
    description: '拥有系统全部管理权限，负责平台整体运营管理'
  },
  doctor: {
    id: 2,
    username: 'doctor',
    password: '123456',
    name: '张医生',
    role: 'doctor',
    roleText: '医生',
    permissions: [
      'dashboard:view',           // 仪表板查看
      'elderly:view',            // 老人档案查看
      'elderly:edit',            // 老人档案编辑
      'health:view',             // 健康数据查看
      'health:manage',           // 健康管理
      'warning:view',            // 健康预警查看
      'warning:handle',          // 预警处理
      'report:view',             // 评估报告查看
      'report:create',           // 创建报告
      'equipment:view',          // 设备查看
      'statistics:view',         // 报表统计查看
      'analysis:view'            // 数据分析查看
    ],
    avatar: '',
    department: '内科',
    title: '主治医师',
    phone: '13800138001',
    email: 'doctor@smartcare.com',
    description: '负责老人医疗健康管理，处理健康预警和评估'
  },
  family: {
    id: 3,
    username: 'family',
    password: '123456',
    name: '李家属',
    role: 'family',
    roleText: '家属',
    permissions: [
      'dashboard:view',           // 仪表板查看（限制）
      'elderly:view',            // 关联老人信息查看
      'health:view',             // 健康数据查看（限制）
      'warning:view',            // 预警信息查看（限制）
      'report:view'              // 评估报告查看（限制）
    ],
    avatar: '',
    department: '',
    relationship: '女儿',
    relatedElderly: [1], // 关联的老人ID
    phone: '13800138002',
    email: 'family@smartcare.com',
    description: '老人家属，可查看关联老人的基本信息和健康状况'
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
        // 模拟登录验证
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
      } catch (error) {
        throw error
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
        // 开发阶段返回当前用户信息
        if (this.userInfo && this.userInfo.id) {
          return { 
            data: { 
              userInfo: this.userInfo, 
              roles: [this.userInfo.role], 
              permissions: this.userInfo.permissions 
            } 
          }
        }
        
        // 生产环境下调用真实API
        // const response = await api.user.getInfo()
        // const { userInfo, roles, permissions } = response.data
        // this.userInfo = userInfo
        // this.roles = roles  
        // this.permissions = permissions
        // localStorage.setItem('userInfo', JSON.stringify(userInfo))
        // return response
        
        throw new Error('用户信息获取失败')
      } catch (error) {
        throw error
      }
    },

    // 登出
    logout() {
      this.token = ''
      this.userInfo = {}
      this.roles = []
      this.permissions = []
      
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
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
