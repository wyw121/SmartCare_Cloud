<template>
  <div class="login-container">
    <div class="login-form">
      <div class="login-header">
        <h2>智慧医养大数据平台</h2>
        <p>云端后台管理系统</p>
      </div>
      
      <!-- 开发模式快速登录 -->
      <div class="quick-login-section" v-if="isDev">
        <h3>🚀 开发模式 - 快速登录</h3>
        <div class="role-cards">
          <div 
            class="role-card admin-card"
            @click="handleQuickLogin('admin')"
            :class="{ active: quickLoading === 'admin' }"
          >
            <div class="role-icon">
              <el-icon><Setting /></el-icon>
            </div>
            <div class="role-info">
              <h4>系统管理员</h4>
              <p>admin / 123456</p>
              <span class="role-desc">🛡️ 全系统权限</span>
              <span class="role-features">用户管理 • 系统配置 • 数据分析</span>
            </div>
            <div class="loading-overlay" v-if="quickLoading === 'admin'">
              <el-icon class="is-loading"><Loading /></el-icon>
            </div>
          </div>
          
          <div 
            class="role-card doctor-card"
            @click="handleQuickLogin('doctor')"
            :class="{ active: quickLoading === 'doctor' }"
          >
            <div class="role-icon">
              <el-icon><Avatar /></el-icon>
            </div>
            <div class="role-info">
              <h4>医生</h4>
              <p>doctor / 123456</p>
              <span class="role-desc">🩺 医疗管理权限</span>
              <span class="role-features">健康管理 • 预警处理 • 评估报告</span>
            </div>
            <div class="loading-overlay" v-if="quickLoading === 'doctor'">
              <el-icon class="is-loading"><Loading /></el-icon>
            </div>
          </div>
          
          <div 
            class="role-card family-card"
            @click="handleQuickLogin('family')"
            :class="{ active: quickLoading === 'family' }"
          >
            <div class="role-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="role-info">
              <h4>家属</h4>
              <p>family / 123456</p>
              <span class="role-desc">👨‍👩‍👧‍👦 查看权限</span>
              <span class="role-features">老人信息 • 健康状况 • 报告查看</span>
            </div>
            <div class="loading-overlay" v-if="quickLoading === 'family'">
              <el-icon class="is-loading"><Loading /></el-icon>
            </div>
          </div>
        </div>
        
        <div class="dev-tips">
          <el-alert
            title="开发提示"
            description="点击上方角色卡片可快速登录，账号密码会自动填充到下方表单中"
            type="info"
            show-icon
            :closable="false"
          />
        </div>
        
        <el-divider>或手动输入</el-divider>
      </div>
      
      <!-- 常规登录表单 -->
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules" 
        class="login-form-content"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="loginForm.rememberMe">
            记住密码
          </el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>© 2025 智慧医养大数据平台 版权所有</p>
        <div class="footer-links">
          <el-link type="primary" href="/permission-demo" target="_blank">
            权限演示页面
          </el-link>
          <el-divider direction="vertical" />
          <el-link type="info" @click="toggleDevMode">
            {{ isDev ? '隐藏' : '显示' }}开发模式
          </el-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/store/user'
import { Avatar, Lock, User, Setting, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()
const loading = ref(false)
const quickLoading = ref('')
const isDev = ref(process.env.NODE_ENV === 'development') // 开发模式自动检测

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

/**
 * 处理常规登录
 */
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    
    loading.value = true
    
    // 调用登录接口
    await userStore.login(loginForm)
    
    ElMessage.success('登录成功')
    
    // 跳转到首页
    router.push('/dashboard')
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}

/**
 * 处理快速角色登录
 */
const handleQuickLogin = async (role) => {
  if (quickLoading.value) return // 防止重复点击
  
  try {
    quickLoading.value = role
    
    // 先填充表单显示给用户
    fillForm(role)
    
    // 调用快速登录
    await userStore.quickLogin(role)
    
    const roleText = getRoleText(role)
    ElMessage.success({
      message: `🎉 以${roleText}身份登录成功！正在跳转...`,
      duration: 2000
    })
    
    // 延迟跳转，让用户看到成功提示和表单变化
    setTimeout(() => {
      router.push('/dashboard')
    }, 800)
    
  } catch (error) {
    console.error('快速登录失败:', error)
    ElMessage.error(error.message || '登录失败，请重试')
  } finally {
    setTimeout(() => {
      quickLoading.value = ''
    }, 1500)
  }
}

/**
 * 获取角色文本
 */
const getRoleText = (role) => {
  const roleMap = {
    admin: '系统管理员',
    doctor: '医生',
    family: '家属'
  }
  return roleMap[role] || '未知角色'
}

/**
 * 自动填充表单（便于测试）
 */
const fillForm = (role) => {
  const accounts = {
    admin: { username: 'admin', password: '123456' },
    doctor: { username: 'doctor', password: '123456' },
    family: { username: 'family', password: '123456' }
  }
  
  if (accounts[role]) {
    loginForm.username = accounts[role].username
    loginForm.password = accounts[role].password
  }
}

/**
 * 切换开发模式显示
 */
const toggleDevMode = () => {
  isDev.value = !isDev.value
  ElMessage.info(isDev.value ? '已显示开发模式' : '已隐藏开发模式')
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-form {
  width: 480px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
  
  h2 {
    color: #303133;
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 8px;
  }
  
  p {
    color: #909399;
    font-size: 14px;
    margin: 0;
  }
}

.quick-login-section {
  margin-bottom: 20px;
  
  h3 {
    font-size: 16px;
    color: #303133;
    margin-bottom: 15px;
    text-align: center;
  }
}

.role-cards {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.role-card {
  flex: 1;
  min-width: 140px;
  padding: 20px 15px;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.1);
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
    
    &::before {
      opacity: 1;
    }
  }
  
  &.active {
    transform: scale(0.95);
    opacity: 0.8;
    
    .role-icon {
      animation: pulse 0.8s ease-in-out;
    }
  }
  
  .role-icon {
    font-size: 28px;
    margin-bottom: 10px;
    color: rgba(255, 255, 255, 0.9);
  }
  
  .role-info {
    h4 {
      font-size: 16px;
      margin: 0 0 6px 0;
      font-weight: 600;
      color: white;
    }
    
    p {
      font-size: 12px;
      margin: 0 0 8px 0;
      opacity: 0.9;
      color: rgba(255, 255, 255, 0.8);
      font-family: 'Courier New', monospace;
    }
    
    .role-desc {
      display: block;
      font-size: 12px;
      opacity: 0.9;
      margin-bottom: 6px;
      font-weight: 500;
    }
    
    .role-features {
      display: block;
      font-size: 10px;
      opacity: 0.7;
      line-height: 1.3;
      color: rgba(255, 255, 255, 0.7);
    }
  }
  
  .loading-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.3);
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 12px;
    
    .el-icon {
      font-size: 24px;
      color: white;
    }
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.dev-tips {
  margin: 15px 0;
  
  :deep(.el-alert) {
    border-radius: 8px;
    
    .el-alert__title {
      font-size: 13px;
    }
    
    .el-alert__description {
      font-size: 12px;
      margin-top: 4px;
    }
  }
}

.admin-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  
  &:hover {
    background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
  }
}

.doctor-card {
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
  color: white;
  
  &:hover {
    background: linear-gradient(135deg, #38a169 0%, #2f855a 100%);
  }
}

.family-card {
  background: linear-gradient(135deg, #ed8936 0%, #dd6b20 100%);
  color: white;
  
  &:hover {
    background: linear-gradient(135deg, #dd6b20 0%, #c05621 100%);
  }
}

.login-form-content {
  .el-form-item {
    margin-bottom: 20px;
  }
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 6px;
}

.login-footer {
  text-align: center;
  margin-top: 30px;
  
  p {
    color: #909399;
    font-size: 12px;
    margin: 0 0 10px 0;
  }
  
  .footer-links {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
  }
}

:deep(.el-input__wrapper) {
  border-radius: 6px;
}

:deep(.el-input__inner) {
  height: 45px;
  line-height: 45px;
}

:deep(.el-divider__text) {
  background: rgba(255, 255, 255, 0.95);
  color: #909399;
  font-size: 12px;
}
</style>
