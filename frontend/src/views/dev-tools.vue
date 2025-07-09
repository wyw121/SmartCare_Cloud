<template>
  <div class="dev-tools">
    <el-card class="header-card">
      <template #header>
        <h2>🛠️ 开发工具 - 智慧医养平台</h2>
      </template>
      
      <el-alert 
        title="开发环境专用工具" 
        type="warning" 
        show-icon 
        :closable="false"
        description="这些工具仅在开发环境可用，用于测试不同用户角色和功能"
        style="margin-bottom: 20px"
      />

      <!-- 用户切换工具 -->
      <el-card class="tool-card">
        <template #header>
          <h3>👤 用户角色切换</h3>
        </template>
        
        <div class="current-user">
          <el-tag type="primary" size="large">
            当前用户: {{ userStore.userName }} ({{ userStore.userRoleText }})
          </el-tag>
        </div>
        
        <div class="user-buttons">
          <el-button 
            type="primary" 
            @click="switchUser('admin')"
            :disabled="userStore.userRole === 'admin'"
          >
            🔧 管理员
          </el-button>
          <el-button 
            type="success" 
            @click="switchUser('doctor')"
            :disabled="userStore.userRole === 'doctor'"
          >
            👨‍⚕️ 医生
          </el-button>
          <el-button 
            type="warning" 
            @click="switchUser('family')"
            :disabled="userStore.userRole === 'family'"
          >
            👨‍👩‍👧‍👦 家属
          </el-button>
        </div>
      </el-card>

      <!-- 快速导航 -->
      <el-card class="tool-card">
        <template #header>
          <h3>🔗 快速导航</h3>
        </template>
        
        <div class="nav-buttons">
          <el-button @click="goToPage('/dashboard')">📊 首页仪表板</el-button>
          <el-button @click="goToPage('/elderly')">👴 老人档案</el-button>
          <el-button @click="goToPage('/health-warning')">⚠️ 健康预警</el-button>
          <el-button @click="goToPage('/health-record')">📋 健康记录</el-button>
          <el-button @click="goToPage('/equipment')">🏥 设备管理</el-button>
        </div>
      </el-card>

      <!-- API测试工具 -->
      <el-card class="tool-card">
        <template #header>
          <h3>🧪 API测试</h3>
        </template>
        
        <div class="api-buttons">
          <el-button @click="testFamilyAPI">测试家属API</el-button>
          <el-button @click="testHealthWarningAPI">测试健康预警API</el-button>
          <el-button @click="initTestData">初始化测试数据</el-button>
        </div>
        
        <div v-if="apiResult" class="api-result">
          <h4>API测试结果:</h4>
          <pre>{{ apiResult }}</pre>
        </div>
      </el-card>

      <!-- 用户权限信息 -->
      <el-card class="tool-card">
        <template #header>
          <h3>🔐 当前用户权限</h3>
        </template>
        
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">{{ userStore.userInfo.id }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ userStore.userInfo.username }}</el-descriptions-item>
          <el-descriptions-item label="角色">{{ userStore.userRoleText }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ userStore.userInfo.department }}</el-descriptions-item>
          <el-descriptions-item label="关联老人ID" v-if="userStore.userRole === 'family'">
            {{ userStore.userInfo.elderlyIds?.join(', ') || '无' }}
          </el-descriptions-item>
        </el-descriptions>
        
        <el-divider>权限列表</el-divider>
        <el-tag 
          v-for="permission in userStore.userPermissions" 
          :key="permission"
          style="margin: 2px"
          size="small"
        >
          {{ permission }}
        </el-tag>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import { getFamilyWarningStatistics } from '@/api/family'

const router = useRouter()
const userStore = useUserStore()
const apiResult = ref('')

// 切换用户
const switchUser = (userType) => {
  userStore.switchToDemoUser(userType)
}

// 页面导航
const goToPage = (path) => {
  router.push(path)
}

// 测试家属API
const testFamilyAPI = async () => {
  try {
    const response = await getFamilyWarningStatistics()
    apiResult.value = JSON.stringify(response, null, 2)
    ElMessage.success('家属API测试成功')
  } catch (error) {
    apiResult.value = '错误: ' + error.message
    ElMessage.error('家属API测试失败')
  }
}

// 测试健康预警API
const testHealthWarningAPI = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/family/warnings/statistics')
    const data = await response.json()
    apiResult.value = JSON.stringify(data, null, 2)
    ElMessage.success('健康预警API测试成功')
  } catch (error) {
    apiResult.value = '错误: ' + error.message
    ElMessage.error('健康预警API测试失败')
  }
}

// 初始化测试数据
const initTestData = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/family/init-test-data', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    const data = await response.json()
    apiResult.value = JSON.stringify(data, null, 2)
    ElMessage.success('测试数据初始化成功')
  } catch (error) {
    apiResult.value = '错误: ' + error.message
    ElMessage.error('测试数据初始化失败')
  }
}
</script>

<style scoped>
.dev-tools {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header-card {
  margin-bottom: 20px;
}

.tool-card {
  margin-bottom: 20px;
}

.current-user {
  margin-bottom: 20px;
  text-align: center;
}

.user-buttons,
.nav-buttons,
.api-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: center;
  margin-bottom: 20px;
}

.api-result {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.api-result pre {
  white-space: pre-wrap;
  word-break: break-all;
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

h2 {
  margin: 0;
  color: #409eff;
}

h3 {
  margin: 0;
  color: #606266;
}
</style>
