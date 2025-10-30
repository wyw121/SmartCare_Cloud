<template>
  <div class="error-page">
    <div class="error-content">
      <div class="error-icon">
        <el-icon :size="120" color="#F56C6C"><WarnTriangleFilled /></el-icon>
      </div>
      <h1 class="error-code">403</h1>
      <h2 class="error-title">权限不足</h2>
      <p class="error-message">{{ errorMessage }}</p>
      <div class="error-actions">
        <el-button type="primary" @click="goBack">
          <el-icon><Back /></el-icon>
          返回上一页
        </el-button>
        <el-button @click="goHome">
          <el-icon><HomeFilled /></el-icon>
          返回首页
        </el-button>
      </div>
      <div class="error-tips">
        <p>可能的原因:</p>
        <ul>
          <li>您的账户没有访问此资源的权限</li>
          <li>您尝试访问不属于您职责范围的数据</li>
          <li>该功能仅对特定角色开放</li>
        </ul>
        <p class="contact-info">如需帮助,请联系系统管理员</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'

const route = useRoute()
const router = useRouter()

// 获取错误信息
const errorMessage = computed(() => {
  return route.query.message || '抱歉,您没有权限访问此页面或资源'
})

// 返回上一页
const goBack = () => {
  router.go(-1)
}

// 返回首页
const goHome = () => {
  router.push('/dashboard')
}
</script>

<style lang="scss" scoped>
.error-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.error-content {
  text-align: center;
  background: #fff;
  padding: 60px 40px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 100%;
}

.error-icon {
  margin-bottom: 20px;
  animation: shake 0.82s cubic-bezier(.36,.07,.19,.97) both;
}

@keyframes shake {
  10%, 90% {
    transform: translate3d(-1px, 0, 0);
  }
  20%, 80% {
    transform: translate3d(2px, 0, 0);
  }
  30%, 50%, 70% {
    transform: translate3d(-4px, 0, 0);
  }
  40%, 60% {
    transform: translate3d(4px, 0, 0);
  }
}

.error-code {
  font-size: 72px;
  font-weight: 700;
  color: #F56C6C;
  margin: 0 0 10px 0;
  text-shadow: 2px 2px 4px rgba(245, 108, 108, 0.2);
}

.error-title {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 20px 0;
}

.error-message {
  font-size: 16px;
  color: #606266;
  margin: 0 0 30px 0;
  line-height: 1.6;
}

.error-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-bottom: 40px;

  .el-button {
    padding: 12px 24px;
    font-size: 14px;
  }
}

.error-tips {
  background: #fef0f0;
  border: 1px solid #fde2e2;
  border-radius: 8px;
  padding: 20px;
  text-align: left;

  p {
    font-size: 14px;
    color: #606266;
    font-weight: 600;
    margin: 0 0 10px 0;
  }

  ul {
    margin: 0 0 15px 0;
    padding-left: 20px;

    li {
      font-size: 13px;
      color: #909399;
      margin-bottom: 8px;
      line-height: 1.5;
    }
  }

  .contact-info {
    font-size: 13px;
    color: #909399;
    font-weight: normal;
    margin: 15px 0 0 0;
    padding-top: 15px;
    border-top: 1px dashed #fde2e2;
  }
}
</style>
