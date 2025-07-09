import { useUserStore } from '@/store'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    
    // 添加认证token
    // 开发环境：暂时移除医生、老人、健康预警等API的认证要求
    const openApis = ['/doctor/', '/elderly/', '/health-warning/']
    const isOpenApi = openApis.some(api => config.url.includes(api))
    
    // 只有在生产环境或者token是有效JWT格式时才添加Authorization头
    if (userStore.token && !isOpenApi) {
      // 检查token是否是有效的JWT格式（包含两个点）
      if (userStore.token.includes('.') && userStore.token.split('.').length === 3) {
        config.headers['Authorization'] = `Bearer ${userStore.token}`
      } else if (import.meta.env.PROD) {
        // 生产环境下token格式不正确，清除token
        userStore.logout()
        window.location.href = '/login.html'
        return Promise.reject(new Error('Token格式不正确'))
      }
      // 开发环境下的mock token不添加Authorization头
    }
    
    // 添加请求时间戳，避免缓存
    config.headers['X-Requested-With'] = 'XMLHttpRequest'
    config.headers['Cache-Control'] = 'no-cache'
    
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const { code, data, message } = response.data
    
    // 请求成功
    if (code === 200 || code === 0) {
      return response.data
    }
    
    // 业务错误处理
    // 开发模式下，登录API失败时不显示错误消息，因为会有模拟登录的回退机制
    const isDevMode = process.env.NODE_ENV === 'development'
    const isLoginApi = response.config && response.config.url && response.config.url.includes('/auth/login')
    
    if (!(isDevMode && isLoginApi)) {
      ElMessage.error(message || '请求失败')
    }
    
    return Promise.reject(new Error(message || '请求失败'))
  },
  error => {
    const { response, message } = error
    const userStore = useUserStore()
    
    if (response) {
      const { status, data } = response
      
      switch (status) {
        case 401:
          // 未授权，清除token并跳转到登录页
          ElMessageBox.confirm(
            '登录状态已过期，请重新登录',
            '系统提示',
            {
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              type: 'warning'
            }
          ).then(() => {
            userStore.logout()
            location.reload()
          })
          break
        case 403:
          ElMessage.error('权限不足，拒绝访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data?.message || `请求失败: ${status}`)
      }
    } else {
      // 网络错误
      if (message.includes('timeout')) {
        ElMessage.error('请求超时，请稍后重试')
      } else if (message.includes('Network Error')) {
        ElMessage.error('网络连接异常')
      } else {
        ElMessage.error('请求失败，请检查网络连接')
      }
    }
    
    return Promise.reject(error)
  }
)

export default service
