<template>
  <div class="app-wrapper" :class="classObj">
    <!-- 移动端遮罩层 -->
    <div 
      v-if="device === 'mobile' && sidebar.opened" 
      class="drawer-bg" 
      @click="handleClickOutside"
    />
    
    <!-- 侧边栏 -->
    <sidebar class="sidebar-container" />
    
    <!-- 主要内容区域 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <navbar />
      
      <!-- 标签页导航 -->
      <tags-view v-if="showTabs" />
      
      <!-- 页面内容 -->
      <app-main />
      
      <!-- 页脚 -->
      <app-footer v-if="showFooter" />
    </div>
    
    <!-- 设置面板 -->
    <settings v-if="showSettings" />
  </div>
</template>

<script setup>
import { useAppStore } from '@/store/app'
import { computed } from 'vue'
import AppFooter from './components/AppFooter.vue'
import AppMain from './components/AppMain.vue'
import Navbar from './components/Navbar.vue'
import Settings from './components/Settings/index.vue'
import Sidebar from './components/Sidebar/index.vue'
import TagsView from './components/TagsView/index.vue'

const appStore = useAppStore()

// 计算属性
const sidebar = computed(() => appStore.sidebar)
const device = computed(() => appStore.device)
const showTabs = computed(() => appStore.layout.showTabs)
const showFooter = computed(() => appStore.layout.showFooter)
const showSettings = computed(() => appStore.theme.showSettings)

const classObj = computed(() => {
  return {
    hideSidebar: !sidebar.value.opened,
    openSidebar: sidebar.value.opened,
    withoutAnimation: sidebar.value.withoutAnimation,
    mobile: device.value === 'mobile'
  }
})

// 方法
const handleClickOutside = () => {
  appStore.closeSidebar(false)
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  position: relative;
  height: 100vh;
  width: 100%;
  display: flex;
  
  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.sidebar-container {
  width: var(--sidebar-width);
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;
  transition: width var(--transition-base);
  
  .app-wrapper.hideSidebar & {
    width: var(--sidebar-width-collapsed);
  }
  
  .app-wrapper.mobile & {
    transition: transform var(--transition-base);
    
    &.hideSidebar {
      pointer-events: none;
      transition-duration: 0.3s;
      transform: translate3d(-var(--sidebar-width), 0, 0);
    }
  }
}

.main-container {
  min-height: 100vh;
  transition: margin-left var(--transition-base);
  margin-left: var(--sidebar-width);
  position: relative;
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow-y: auto;
}

.app-wrapper.hideSidebar {
  .main-container {
    margin-left: var(--sidebar-width-collapsed);
  }
}

.mobile {
  .main-container {
    margin-left: 0;
  }
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - var(--sidebar-width));
  transition: width var(--transition-base);
  
  .app-wrapper.hideSidebar & {
    width: calc(100% - var(--sidebar-width-collapsed));
  }
  
  .app-wrapper.mobile & {
    width: 100%;
  }
}
</style>
