<template>
  <div class="settings-panel">
    <div class="settings-trigger" @click="togglePanel">
      <el-icon class="settings-icon">
        <Setting />
      </el-icon>
    </div>
    
    <el-drawer
      v-model="showPanel"
      title="系统设置"
      direction="rtl"
      size="300px"
      :before-close="handleClose"
    >
      <div class="settings-content">
        <!-- 主题设置 -->
        <div class="setting-group">
          <h4 class="group-title">主题设置</h4>
          
          <div class="setting-item">
            <span class="setting-label">主题色</span>
            <el-color-picker 
              v-model="themeColor" 
              :predefine="predefineColors"
              @change="handleThemeColorChange"
            />
          </div>
          
          <div class="setting-item">
            <span class="setting-label">暗色模式</span>
            <el-switch 
              v-model="darkMode" 
              @change="handleDarkModeChange"
            />
          </div>
        </div>
        
        <!-- 布局设置 -->
        <div class="setting-group">
          <h4 class="group-title">布局设置</h4>
          
          <div class="setting-item">
            <span class="setting-label">固定头部</span>
            <el-switch 
              v-model="fixedHeader" 
              @change="handleFixedHeaderChange"
            />
          </div>
          
          <div class="setting-item">
            <span class="setting-label">显示标签页</span>
            <el-switch 
              v-model="showTabs" 
              @change="handleShowTabsChange"
            />
          </div>
          
          <div class="setting-item">
            <span class="setting-label">显示页脚</span>
            <el-switch 
              v-model="showFooter" 
              @change="handleShowFooterChange"
            />
          </div>
          
          <div class="setting-item">
            <span class="setting-label">侧边栏Logo</span>
            <el-switch 
              v-model="sidebarLogo" 
              @change="handleSidebarLogoChange"
            />
          </div>
        </div>
        
        <!-- 功能设置 -->
        <div class="setting-group">
          <h4 class="group-title">功能设置</h4>
          
          <div class="setting-item">
            <span class="setting-label">页面缓存</span>
            <el-switch 
              v-model="pageCache" 
              @change="handlePageCacheChange"
            />
          </div>
          
          <div class="setting-item">
            <span class="setting-label">面包屑导航</span>
            <el-switch 
              v-model="breadcrumb" 
              @change="handleBreadcrumbChange"
            />
          </div>
          
          <div class="setting-item">
            <span class="setting-label">页面动画</span>
            <el-select 
              v-model="pageTransition" 
              @change="handlePageTransitionChange"
              style="width: 120px"
            >
              <el-option label="淡入淡出" value="fade" />
              <el-option label="左右滑动" value="slide" />
              <el-option label="缩放" value="zoom" />
              <el-option label="无动画" value="none" />
            </el-select>
          </div>
        </div>
        
        <!-- 操作按钮 -->
        <div class="setting-actions">
          <el-button type="primary" @click="saveSettings">
            保存设置
          </el-button>
          <el-button @click="resetSettings">
            重置设置
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Setting } from '@element-plus/icons-vue'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()

// 响应式数据
const showPanel = ref(false)

// 预定义颜色
const predefineColors = ref([
  '#409EFF',
  '#67C23A',
  '#E6A23C',
  '#F56C6C',
  '#909399',
  '#00D2FF',
  '#3FB1CE',
  '#6BE6C1',
  '#626AEF',
  '#A0A0FF'
])

// 设置项的计算属性
const themeColor = computed({
  get: () => appStore.theme.color,
  set: (value) => appStore.setThemeColor(value)
})

const darkMode = computed({
  get: () => appStore.theme.darkMode,
  set: (value) => appStore.setDarkMode(value)
})

const fixedHeader = computed({
  get: () => appStore.layout.fixedHeader,
  set: (value) => appStore.setFixedHeader(value)
})

const showTabs = computed({
  get: () => appStore.layout.showTabs,
  set: (value) => appStore.setShowTabs(value)
})

const showFooter = computed({
  get: () => appStore.layout.showFooter,
  set: (value) => appStore.setShowFooter(value)
})

const sidebarLogo = computed({
  get: () => appStore.layout.sidebarLogo,
  set: (value) => appStore.setSidebarLogo(value)
})

const pageCache = computed({
  get: () => appStore.layout.pageCache,
  set: (value) => appStore.setPageCache(value)
})

const breadcrumb = computed({
  get: () => appStore.layout.breadcrumb,
  set: (value) => appStore.setBreadcrumb(value)
})

const pageTransition = computed({
  get: () => appStore.layout.pageTransition,
  set: (value) => appStore.setPageTransition(value)
})

/**
 * 切换设置面板
 */
const togglePanel = () => {
  showPanel.value = !showPanel.value
}

/**
 * 关闭面板
 */
const handleClose = () => {
  showPanel.value = false
}

/**
 * 主题色变化
 */
const handleThemeColorChange = (color) => {
  appStore.setThemeColor(color)
  // 这里可以添加切换主题色的逻辑
}

/**
 * 暗色模式变化
 */
const handleDarkModeChange = (value) => {
  appStore.setDarkMode(value)
  // 这里可以添加切换暗色模式的逻辑
}

/**
 * 固定头部变化
 */
const handleFixedHeaderChange = (value) => {
  appStore.setFixedHeader(value)
}

/**
 * 显示标签页变化
 */
const handleShowTabsChange = (value) => {
  appStore.setShowTabs(value)
}

/**
 * 显示页脚变化
 */
const handleShowFooterChange = (value) => {
  appStore.setShowFooter(value)
}

/**
 * 侧边栏Logo变化
 */
const handleSidebarLogoChange = (value) => {
  appStore.setSidebarLogo(value)
}

/**
 * 页面缓存变化
 */
const handlePageCacheChange = (value) => {
  appStore.setPageCache(value)
}

/**
 * 面包屑导航变化
 */
const handleBreadcrumbChange = (value) => {
  appStore.setBreadcrumb(value)
}

/**
 * 页面动画变化
 */
const handlePageTransitionChange = (value) => {
  appStore.setPageTransition(value)
}

/**
 * 保存设置
 */
const saveSettings = () => {
  // 保存设置到本地存储
  localStorage.setItem('app-settings', JSON.stringify({
    theme: appStore.theme,
    layout: appStore.layout
  }))
  
  ElMessage.success('设置保存成功')
  showPanel.value = false
}

/**
 * 重置设置
 */
const resetSettings = () => {
  appStore.resetSettings()
  localStorage.removeItem('app-settings')
  ElMessage.success('设置重置成功')
}
</script>

<style lang="scss" scoped>
.settings-panel {
  .settings-trigger {
    position: fixed;
    top: 50%;
    right: 0;
    width: 48px;
    height: 48px;
    background: #409eff;
    border-radius: 6px 0 0 6px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    box-shadow: -2px 0 6px rgba(0, 0, 0, 0.15);
    transition: all 0.3s;
    
    &:hover {
      background: #337ecc;
    }
    
    .settings-icon {
      color: #fff;
      font-size: 18px;
      animation: rotate 2s linear infinite;
    }
  }
}

.settings-content {
  padding: 20px;
  
  .setting-group {
    margin-bottom: 30px;
    
    .group-title {
      margin: 0 0 15px 0;
      padding-bottom: 8px;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      border-bottom: 1px solid #ebeef5;
    }
    
    .setting-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 15px;
      
      .setting-label {
        font-size: 14px;
        color: #606266;
      }
    }
  }
  
  .setting-actions {
    display: flex;
    gap: 10px;
    margin-top: 30px;
    
    .el-button {
      flex: 1;
    }
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

:deep(.el-drawer__header) {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-drawer__body) {
  padding: 0;
}
</style>
