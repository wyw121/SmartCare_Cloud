<template>
  <div class="sidebar">
    <div class="sidebar-logo">
      <div class="logo-icon">
        <el-icon><Cpu /></el-icon>
      </div>
      <h1 class="logo-text" v-if="!collapsed">智慧医养平台</h1>
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
import { useAppStore } from '@/store/app'
import { useUserStore } from '@/store/user'
import { filterMenusByRole } from '@/utils/permission'
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SidebarItem from './SidebarItem.vue'

export default {
  name: 'Sidebar',
  components: {
    SidebarItem
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const appStore = useAppStore()
    const userStore = useUserStore()

    const collapsed = computed(() => !appStore.sidebar.opened)
    
    const activeMenu = computed(() => {
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    })

    // 基于角色过滤路由
    const routes = computed(() => {
      const allRoutes = router.getRoutes()
      const userRole = userStore.userRole
      
      // 预定义的菜单结构
      const menuItems = [
        {
          path: '/dashboard',
          name: 'dashboard',
          meta: { title: '仪表板', icon: 'DataBoard' }
        },
        {
          path: '/elderly',
          name: 'elderly',
          meta: { title: '老人管理', icon: 'User' }
        },
        {
          path: '/doctor',
          name: 'doctor',
          meta: { title: '医生管理', icon: 'Avatar' }
        },
        {
          path: '/health-warning',
          name: 'health-warning',
          meta: { title: '健康预警', icon: 'Warning' }
        },
        {
          path: '/reports',
          name: 'reports',
          meta: { title: '报表统计', icon: 'DataAnalysis' }
        },
        {
          path: '/system',
          name: 'system',
          meta: { title: '系统管理', icon: 'Setting' }
        }
      ]
      
      // 根据角色过滤菜单
      return filterMenusByRole(menuItems, userRole)
    })

    return {
      collapsed,
      activeMenu,
      routes
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
