<template>
  <div class="sidebar">
    <div class="sidebar-logo">
      <div class="logo-icon">
        <el-icon><Cpu /></el-icon>
      </div>
      <h1 class="logo-text" v-if="!collapsed">
        {{ userRole === 'family' ? '关爱长辈' : '智慧医养平台' }}
      </h1>
    </div>
    
    <el-menu
      :default-active="activeMenu"
      :collapse="collapsed"
      :unique-opened="false"
      :collapse-transition="false"
      mode="vertical"
      router
    >
      <sidebar-item
        v-for="route in routes"
        :key="route.path"
        :item="route"
        :base-path="route.path"
      />
    </el-menu>
  </div>
</template>

<script>
import { getMenuByRole } from '@/router/role-config'
import { useAppStore } from '@/store/app'
import { useUserStore } from '@/store/user'
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import SidebarItem from './SidebarItem.vue'

export default {
  name: 'Sidebar',
  components: {
    SidebarItem
  },
  setup() {
    const route = useRoute()
    const appStore = useAppStore()
    const userStore = useUserStore()

    const collapsed = computed(() => !appStore.sidebar.opened)
    const userRole = computed(() => userStore.userRole)
    
    const activeMenu = computed(() => {
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    })

    // 基于角色过滤路由 - 使用新的菜单配置
    const routes = computed(() => {
      const currentRole = userStore.userRole
      
      // 调试输出
      console.log('🔍 [侧边栏] 当前用户角色:', currentRole)
      console.log('🔍 [侧边栏] 用户信息:', userStore.userInfo)
      
      // 使用新的角色菜单配置获取菜单
      const menuItems = getMenuByRole(currentRole)
      
      console.log('🔍 [侧边栏] 菜单配置:', menuItems)
      
      return menuItems
    })

    return {
      collapsed,
      activeMenu,
      routes,
      userRole
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar {
  height: 100vh;
  overflow-y: auto;
  background-color: #304156;
  width: 100%;
  
  .sidebar-logo {
    display: flex;
    align-items: center;
    padding: 20px;
    border-bottom: 1px solid #434a50;
    
    .logo-icon {
      width: 32px;
      height: 32px;
      margin-right: 12px;
      background: #409eff;
      border-radius: 6px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 20px;
    }
    
    .logo-text {
      color: #fff;
      font-size: 18px;
      font-weight: 600;
      margin: 0;
      white-space: nowrap;
    }
  }
  
  :deep(.el-menu) {
    border-right: none;
    background-color: #304156;
    
    .el-menu-item,
    .el-sub-menu__title {
      color: #bfcbd9;
      
      &:hover {
        background-color: #434a50 !important;
        color: #fff;
      }
      
      &.is-active {
        background-color: #409eff !important;
        color: #fff;
      }
    }
    
    .el-sub-menu .el-menu-item {
      background-color: #1f2d3d;
      
      &:hover {
        background-color: #001528 !important;
      }
      
      &.is-active {
        background-color: #409eff !important;
      }
    }
  }
}
</style>
