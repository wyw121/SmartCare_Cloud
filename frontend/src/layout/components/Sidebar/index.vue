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
    
    const activeMenu = computed(() => {
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    })

    // 基于角色过滤路由
    const routes = computed(() => {
      const userRole = userStore.userRole
      
      // 调试输出
      console.log('🔍 侧边栏调试 - 当前用户角色:', userRole)
      console.log('🔍 侧边栏调试 - 用户信息:', userStore.userInfo)
      
      // 预定义的菜单结构 - 基于实际路由配置
      const menuItems = [
        {
          path: '/dashboard',
          name: 'dashboard',
          meta: { title: '首页仪表板', icon: 'DataBoard' },
          roles: ['admin', 'doctor', 'family']
        },
        {
          path: '/elderly',
          name: 'elderly',
          meta: { title: '老人档案管理', icon: 'User' },
          roles: ['admin', 'doctor', 'family']
        },
        {
          path: '/doctor',
          name: 'doctor',
          meta: { title: '医生管理', icon: 'Avatar' },
          roles: ['admin', 'doctor']
        },
        {
          path: '/health-warning',
          name: 'health-warning',
          meta: { title: '健康预警', icon: 'Warning' },
          roles: ['admin', 'doctor', 'family']
        },
        {
          path: '/equipment',
          name: 'equipment',
          meta: { title: '设备管理', icon: 'Monitor' },
          roles: ['admin', 'doctor', 'family']
        },
        {
          path: '/reports',
          name: 'reports',
          meta: { title: '报表统计', icon: 'DataAnalysis' },
          roles: ['admin', 'doctor', 'family']
        },
        {
          path: '/system',
          name: 'system',
          meta: { title: '系统管理', icon: 'Setting' },
          roles: ['admin'] // 仅系统管理员可见
        }
      ]
      
      // 根据角色过滤菜单
      const filteredMenus = menuItems.filter(menu => {
        // 如果菜单项定义了roles属性，则检查当前用户角色是否在允许列表中
        if (menu.roles && Array.isArray(menu.roles)) {
          return menu.roles.includes(userRole)
        }
        
        // 如果没有定义roles，默认允许访问
        return true
      })
      
      console.log('🔍 侧边栏调试 - 过滤后的菜单:', filteredMenus)
      console.log('🔍 侧边栏调试 - 设备管理菜单是否显示:', filteredMenus.some(m => m.name === 'equipment'))
      
      return filteredMenus
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
