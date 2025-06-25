<template>
  <div class="error-container">
    <div class="error-content">
      <div class="error-image">
        <div class="error-illustration">
          <h1 class="error-illustration-text">404</h1>
        </div>
      </div>
      
      <div class="error-info">
        <h1 class="error-code">404</h1>
        <h2 class="error-title">页面不存在</h2>
        <p class="error-description">
          抱歉，您访问的页面不存在或已被删除。
          <br />
          请检查URL是否正确，或返回首页。
        </p>
        
        <div class="error-actions">
          <el-button type="primary" size="large" @click="goHome">
            <el-icon><HomeFilled /></el-icon>
            返回首页
          </el-button>
          <el-button size="large" @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            返回上页
          </el-button>
          <el-button size="large" @click="refresh">
            <el-icon><Refresh /></el-icon>
            刷新页面
          </el-button>
        </div>
        
        <div class="error-suggestions">
          <h3>您可以尝试：</h3>
          <ul>
            <li>检查URL拼写是否正确</li>
            <li>通过导航菜单访问相关页面</li>
            <li>使用搜索功能查找内容</li>
            <li>联系系统管理员获取帮助</li>
          </ul>
        </div>
      </div>
    </div>
    
    <div class="error-footer">
      <p>如果问题持续存在，请联系技术支持：support@smartcare.com</p>
    </div>
  </div>
</template>

<script setup>
import { ArrowLeft, HomeFilled, Refresh } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

/**
 * 返回首页
 */
const goHome = () => {
  router.push('/')
}

/**
 * 返回上一页
 */
const goBack = () => {
  router.back()
}

/**
 * 刷新页面
 */
const refresh = () => {
  window.location.reload()
}
</script>

<style lang="scss" scoped>
.error-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 20px;
}

.error-content {
  display: flex;
  align-items: center;
  gap: 60px;
  max-width: 1000px;
  width: 100%;
  
  @media (max-width: 768px) {
    flex-direction: column;
    gap: 40px;
    text-align: center;
  }
}

.error-image {
  flex-shrink: 0;
  
  .error-illustration {
    width: 300px;
    height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    backdrop-filter: blur(10px);
    
    @media (max-width: 768px) {
      width: 200px;
      height: 200px;
    }
    
    .error-illustration-text {
      font-size: 120px;
      font-weight: 900;
      color: rgba(255, 255, 255, 0.8);
      margin: 0;
      
      @media (max-width: 768px) {
        font-size: 80px;
      }
    }
  }
}

.error-info {
  flex: 1;
  max-width: 500px;
}

.error-code {
  font-size: 80px;
  font-weight: 700;
  margin: 0 0 10px 0;
  background: linear-gradient(45deg, #fff, #f0f0f0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  
  @media (max-width: 768px) {
    font-size: 60px;
  }
}

.error-title {
  font-size: 32px;
  font-weight: 600;
  margin: 0 0 15px 0;
  
  @media (max-width: 768px) {
    font-size: 24px;
  }
}

.error-description {
  font-size: 16px;
  line-height: 1.6;
  margin: 0 0 30px 0;
  opacity: 0.9;
}

.error-actions {
  display: flex;
  gap: 15px;
  margin-bottom: 40px;
  
  @media (max-width: 768px) {
    flex-direction: column;
    align-items: center;
  }
}

.error-suggestions {
  background: rgba(255, 255, 255, 0.1);
  padding: 20px;
  border-radius: 8px;
  backdrop-filter: blur(10px);
  
  h3 {
    font-size: 18px;
    margin: 0 0 15px 0;
    color: #fff;
  }
  
  ul {
    margin: 0;
    padding-left: 20px;
    
    li {
      font-size: 14px;
      line-height: 1.8;
      opacity: 0.9;
    }
  }
}

.error-footer {
  margin-top: 40px;
  text-align: center;
  opacity: 0.7;
  
  p {
    margin: 0;
    font-size: 14px;
  }
}

:deep(.el-button) {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.3);
  color: #fff;
  
  &:hover {
    background: rgba(255, 255, 255, 0.3);
    border-color: rgba(255, 255, 255, 0.5);
  }
  
  &.el-button--primary {
    background: rgba(64, 158, 255, 0.8);
    border-color: #409eff;
    
    &:hover {
      background: #409eff;
      border-color: #409eff;
    }
  }
}
</style>
