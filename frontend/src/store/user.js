import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    roles: [],
    permissions: []
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    userName: (state) => state.userInfo.name || '',
    userAvatar: (state) => state.userInfo.avatar || '',
    userRole: (state) => state.userInfo.role || 'user'
  },

  actions: {
    // 登录
    async login(loginForm) {
      try {
        // 这里调用登录API
        const response = await api.auth.login(loginForm)
        const { token, userInfo } = response.data
        
        this.token = token
        this.userInfo = userInfo
        
        // 存储到本地
        localStorage.setItem('token', token)
        localStorage.setItem('userInfo', JSON.stringify(userInfo))
        
        return response
      } catch (error) {
        throw error
      }
    },

    // 获取用户信息
    async getUserInfo() {
      try {
        // 这里调用获取用户信息API
        const response = await api.user.getInfo()
        const { userInfo, roles, permissions } = response.data
        
        this.userInfo = userInfo
        this.roles = roles
        this.permissions = permissions
        
        localStorage.setItem('userInfo', JSON.stringify(userInfo))
        
        return response
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
