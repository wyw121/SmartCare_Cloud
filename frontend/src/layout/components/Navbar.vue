<template>
  <div class="navbar">
    <div class="navbar-left">
      <!-- 菜单折叠按钮 -->
      <hamburger
        :is-active="sidebar.opened"
        class="hamburger-container"
        @toggle-click="toggleSideBar"
      />
      
      <!-- 面包屑导航 -->
      <breadcrumb class="breadcrumb-container" />
    </div>
    
    <div class="navbar-right">
      <!-- 全屏按钮 -->
      <div class="right-menu-item hover-effect" @click="toggleFullScreen">
        <el-icon>
          <component :is="isFullscreen ? 'Aim' : 'FullScreen'" />
        </el-icon>
      </div>
      
      <!-- 消息通知 -->
      <el-badge :value="12" :max="99" class="right-menu-item">
        <el-icon class="hover-effect">
          <Bell />
        </el-icon>
      </el-badge>
      
      <!-- 用户头像和菜单 -->
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <el-avatar :src="userInfo.avatar" :size="32">
            <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          </el-avatar>
          <span class="username">{{ userInfo.realName }}</span>
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </div>
        
        <template #dropdown>
          <el-dropdown-menu>
            <router-link to="/profile">
              <el-dropdown-item>
                <el-icon><User /></el-icon>
                个人中心
              </el-dropdown-item>
            </router-link>
            <el-dropdown-item @click="handleSettings">
              <el-icon><Setting /></el-icon>
              系统设置
            </el-dropdown-item>
            <el-dropdown-item divided @click="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  FullScreen, 
  Aim, 
  Bell, 
  User, 
  Setting, 
  SwitchButton, 
  ArrowDown 
} from '@element-plus/icons-vue'
import { useAppStore } from '@/store/app'
import { useUserStore } from '@/store/user'
import Hamburger from './Hamburger.vue'
import Breadcrumb from './Breadcrumb.vue'

const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

// 响应式数据
const isFullscreen = ref(false)

// 计算属性
const sidebar = computed(() => appStore.sidebar)
const userInfo = computed(() => userStore.userInfo)

/**
 * 切换侧边栏
 */
const toggleSideBar = () => {
  appStore.toggleSidebar()
}

/**
 * 切换全屏
 */
const toggleFullScreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
    isFullscreen.value = true
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
      isFullscreen.value = false
    }
  }
}

/**
 * 系统设置
 */
const handleSettings = () => {
  ElMessage.info('系统设置功能开发中...')
}

/**
 * 退出登录
 */
const logout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 清除用户信息
    await userStore.logout()
    
    // 跳转到登录页
    router.push('/login')
    
    ElMessage.success('退出登录成功')
  } catch (error) {
    // 取消退出
  }
}

// 监听全屏变化
document.addEventListener('fullscreenchange', () => {
  isFullscreen.value = !!document.fullscreenElement
})
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.navbar-left {
  display: flex;
  align-items: center;
  
  .hamburger-container {
    line-height: 46px;
    height: 100%;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;
    margin-right: 15px;
    
    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }
  
  .breadcrumb-container {
    font-size: 14px;
  }
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 15px;
  
  .right-menu-item {
    display: inline-block;
    padding: 0 8px;
    height: 40px;
    line-height: 40px;
    font-size: 18px;
    color: #5a5e66;
    cursor: pointer;
    border-radius: 4px;
    transition: background 0.3s;
    
    &.hover-effect:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }
  
  .avatar-container {
    cursor: pointer;
    
    .avatar-wrapper {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 5px 10px;
      border-radius: 4px;
      transition: background 0.3s;
      
      &:hover {
        background: rgba(0, 0, 0, 0.025);
      }
      
      .username {
        font-size: 14px;
        color: #303133;
        font-weight: 500;
      }
      
      .el-icon {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .el-icon {
    font-size: 16px;
  }
}
</style>
