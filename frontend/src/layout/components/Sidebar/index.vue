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
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Cpu } from '@element-plus/icons-vue'
import { useAppStore } from '@/store/app'
import SidebarItem from './SidebarItem.vue'
import routes from '@/router'

export default {
  name: 'Sidebar',
  components: {
    SidebarItem
  },
  setup() {
    const route = useRoute()
    const appStore = useAppStore()

    const collapsed = computed(() => !appStore.sidebar.opened)
    
    const activeMenu = computed(() => {
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    })

    // 过滤路由，只显示需要在侧边栏显示的路由
    const routes = computed(() => {
      return filterRoutes(routes.routes)
    })

    const filterRoutes = (routes) => {
      const res = []
      routes.forEach(route => {
        // 跳过hidden的路由和登录页面
        if (!route.meta?.hidden && route.path !== '/login' && route.path !== '/:pathMatch(.*)*') {
          const tmp = { ...route }
          if (tmp.children) {
            tmp.children = filterRoutes(tmp.children)
          }
          res.push(tmp)
        }
      })
      return res
    }

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
