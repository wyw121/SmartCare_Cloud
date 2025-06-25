<template>
  <div style="padding: 40px; font-family: Arial, sans-serif;">
    <h1 style="color: #409EFF; text-align: center;">🏥 智慧医养大数据公共服务平台</h1>
    
    <div style="text-align: center; margin: 30px 0;">
      <p style="font-size: 18px; color: #666;">系统正在运行...</p>
    </div>
    
    <div style="display: flex; justify-content: center; gap: 20px; margin: 30px 0;">
      <el-button type="primary" @click="goToDashboard">
        <el-icon><Platform /></el-icon>
        进入仪表板
      </el-button>
      <el-button type="success" @click="goToTest">
        <el-icon><Setting /></el-icon>
        测试页面
      </el-button>
    </div>
    
    <el-card style="margin-top: 30px;">
      <template #header>
        <div style="text-align: center;">
          <h3>系统状态</h3>
        </div>
      </template>
      <div style="text-align: center;">
        <el-tag type="success" size="large">前端服务 ✓ 正常</el-tag>
        <el-tag type="warning" size="large" style="margin-left: 10px;">后端服务 ⚠ 未连接</el-tag>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Platform, Setting } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const goToDashboard = () => {
  router.push('/dashboard/index')
}

const goToTest = () => {
  router.push('/test')
}
</script>
