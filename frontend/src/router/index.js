import Layout from '@/layout/index.vue'
import { useTagsViewStore } from '@/store/tagsView'
import { useUserStore } from '@/store/user'
import { canAccessRoute } from '@/utils/permission'
import { createRouter, createWebHistory } from 'vue-router'

/**
 * 路由配置
 * meta 配置项说明:
 * - title: 页面标题，用于浏览器标签页和面包屑导航
 * - icon: 菜单图标
 * - roles: 页面权限，如果不设置则表示所有角色都可以访问
 * - permissions: 页面权限，如果不设置则表示所有权限都可以访问
 * - noCache: 如果设置为true，则不会被 <keep-alive> 缓存
 * - affix: 如果设置为true，tag将无法被删除
 * - breadcrumb: 如果设置为false，则不会在breadcrumb面包屑中显示
 * - activeMenu: 如果设置路径，sidebar会高亮你设置的路径
 * - hidden: 如果设置为true，该路由将不会在侧边栏中显示
 */

const routes = [
  // 测试路由已移至 __tests__ 目录，仅在开发环境使用
  // 如需使用测试页面，请从 frontend/__tests__/views/ 目录访问
  
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/index.vue'),
    meta: { title: '注册', hidden: true }
  },
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/home.vue'),
    meta: { title: '智慧医养平台首页', hidden: true }
  },
  {
    path: '/dashboard',
    component: Layout,
    redirect: '/dashboard/index',
    children: [
      {
        path: 'index',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/working.vue'),
        meta: { title: '首页仪表板', icon: 'Platform', affix: true }
      }
      // 模块化仪表板已归档至 __archive__/modular-pages/
    ]
  },
  {
    path: '/elderly',
    component: Layout,
    redirect: '/elderly/list',
    meta: { title: '老人档案管理', icon: 'user' },
    children: [
      {
        path: 'list',
        name: 'ElderlyList',
        component: () => import('@/views/elderly/list.vue'),
        meta: { 
          title: '老人档案列表', 
          icon: 'list',
          roles: ['admin', 'doctor'] // 管理员和医生使用完整的管理页面
        }
      },
      {
        path: 'family-view',
        name: 'ElderlyFamilyView',
        component: () => import('@/views/elderly/family-view.vue'),
        meta: { 
          title: '我的关联长辈', 
          icon: 'user',
          roles: ['family'] // 家属使用专门的家属视图
        }
      },
      {
        path: 'detail/:id(\\d+)',
        name: 'ElderlyDetail',
        component: () => import('@/views/elderly/Detail.vue'),
        meta: { title: '老人详情', activeMenu: '/elderly/list', hidden: true }
      },
      {
        path: 'profile/:id(\\d+)',
        name: 'ElderlyProfile',
        component: () => import('@/views/elderly/profile.vue'),
        meta: { title: '老人详细档案', activeMenu: '/elderly/list', hidden: true }
      },
      {
        path: 'add',
        name: 'ElderlyAdd',
        component: () => import('@/views/elderly/form.vue'),
        meta: { title: '新增老人档案', activeMenu: '/elderly/list', hidden: true }
      },
      {
        path: 'edit/:id(\\d+)',
        name: 'ElderlyEdit',
        component: () => import('@/views/elderly/form.vue'),
        meta: { title: '编辑老人档案', activeMenu: '/elderly/list', hidden: true }
      },
      {
        path: 'health-records/:id(\\d+)',
        name: 'ElderlyHealthRecords',
        component: () => import('@/views/elderly/health-records.vue'),
        meta: { title: '健康档案', activeMenu: '/elderly/list', hidden: true }
      }
      // 模块化老人管理已归档至 __archive__/modular-pages/
    ]
  },
  {
    path: '/doctor',
    component: Layout,
    redirect: '/doctor/list',
    meta: { title: '医生管理', icon: 'user' },
    children: [
      {
        path: 'list',
        name: 'DoctorList',
        component: () => import('@/views/doctor/list.vue'),
        meta: { title: '医生列表', icon: 'list' }
      }
      // 模块化医生管理已归档至 __archive__/modular-pages/
    ]
  },
  {
    path: '/health',
    component: Layout,
    redirect: '/health/warning',
    meta: { title: '健康管理', icon: 'monitor' },
    children: [
      {
        path: 'warning',
        name: 'HealthWarning',
        component: () => import('@/views/health-warning/index.vue'),
        meta: { title: '健康预警', icon: 'warning' }
      },
      {
        path: 'records',
        name: 'HealthRecords',
        component: () => import('@/views/health/records.vue'),
        meta: { title: '健康记录', icon: 'document' }
      },
      {
        path: 'assessment',
        name: 'HealthAssessment',
        component: () => import('@/views/health/assessment.vue'),
        meta: { title: '评估报告', icon: 'data-analysis' }
      }
    ]
  },
  {
    path: '/equipment',
    component: Layout,
    redirect: '/equipment/list',
    meta: { title: '设备管理', icon: 'cpu' },
    children: [
      {
        path: 'list',
        name: 'EquipmentList',
        component: () => import('@/views/equipment/list.vue'),
        meta: { title: '设备列表', icon: 'list' }
      },
      {
        path: 'monitor',
        name: 'EquipmentMonitor',
        component: () => import('@/views/equipment/monitor.vue'),
        meta: { title: '设备监控', icon: 'monitor' }
      }
    ]
  },
  {
    path: '/reports',
    component: Layout,
    redirect: '/reports/statistics',
    meta: { title: '报表统计', icon: 'data-line' },
    children: [
      {
        path: 'statistics',
        name: 'ReportStatistics',
        component: () => import('@/views/reports/statistics.vue'),
        meta: { title: '统计报表', icon: 'pie-chart' }
      },
      {
        path: 'analysis',
        name: 'DataAnalysis',
        component: () => import('@/views/reports/analysis.vue'),
        meta: { title: '大数据分析', icon: 'data-analysis' }
      }
      // 模块化数据分析已归档至 __archive__/modular-pages/
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/users',
    meta: { title: '系统管理', icon: 'setting', roles: ['admin'] },
    children: [
      {
        path: 'users',
        name: 'SystemUsers',
        component: () => import('@/views/system/users.vue'),
        meta: { title: '用户管理', icon: 'user' }
      },
      // 模块化用户管理已归档至 __archive__/modular-pages/
      {
        path: 'roles',
        name: 'SystemRoles',
        component: () => import('@/views/system/roles.vue'),
        meta: { title: '角色管理', icon: 'avatar' }
      },
      {
        path: 'permissions',
        name: 'SystemPermissions',
        component: () => import('@/views/system/permissions.vue'),
        meta: { title: '权限管理', icon: 'key' }
      }
    ]
  },

  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    children: [
      {
        path: 'index',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  // 测试页面已归档
  // {
  //   path: '/doctor-test',
  //   name: 'DoctorTest',
  //   component: () => import('@/views/__tests__/doctor-simple-test.vue'),
  //   meta: { title: '医生测试页面', hidden: true }
  // },
  // {
  //   path: '/user-switcher',
  //   name: 'UserSwitcher',
  //   component: () => import('@/views/__tests__/user-switcher.vue'),
  //   meta: { title: '用户切换（测试）', hidden: true }
  // },
  // 404页面必须放在最后
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '页面不存在', hidden: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  console.log('🚀 [路由守卫] beforeEach 开始', {
    from: from.path,
    to: to.path,
    toName: to.name,
    toMeta: to.meta
  })
  
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 智慧医养大数据平台` : '智慧医养大数据平台'
  
  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn
  const userRole = userStore.userRole
  
  console.log('🔐 [路由守卫] 用户状态', {
    isLoggedIn,
    userRole,
    userInfo: userStore.userInfo
  })
  
  // 如果是登录页面或注册页面，直接通过
  if (to.path === '/login' || to.path === '/register') {
    console.log('✅ [路由守卫] 登录/注册页面，直接通过')
    next()
    return
  }
  
  // 如果未登录，跳转到登录页
  if (!isLoggedIn) {
    console.log('❌ [路由守卫] 未登录，跳转到登录页')
    next('/login')
    return
  }
  
  // 家属用户访问管理页面时重定向到专用页面
  if (userRole === 'family') {
    console.log('👨‍👩‍👧‍👦 [路由守卫] 家属用户权限检查', { toPath: to.path })
    
    // 家属访问老人档案列表时，重定向到家属专用页面
    if (to.path === '/elderly/list' || to.name === 'ElderlyList') {
      console.log('🔄 [路由守卫] 家属重定向到专用页面')
      next('/elderly/family-view')
      return
    }
    
    // 家属访问其他管理页面时，重定向到首页
    const restrictedPaths = ['/doctor', '/system', '/equipment', '/reports']
    if (restrictedPaths.some(path => to.path.startsWith(path))) {
      console.log('🚫 [路由守卫] 家属访问受限页面，重定向到首页')
      next('/dashboard')
      return
    }
  }
  
  // 检查页面访问权限
  const routeName = to.name
  if (routeName && !canAccessRoute(routeName, userRole)) {
    console.log('🚫 [路由守卫] 权限检查失败，跳转到首页', { routeName, userRole })
    next('/dashboard')
    return
  }
  
  console.log('✅ [路由守卫] 权限检查通过，继续导航')
  next()
})

// 路由后置守卫 - 管理页面缓存
router.afterEach((to, from) => {
  console.log('🎯 [路由守卫] afterEach 开始', {
    from: from.path,
    to: to.path,
    toName: to.name,
    toMeta: to.meta
  })
  
  const tagsViewStore = useTagsViewStore()
  
  console.log('📋 [标签页] 当前标签状态', {
    visitedViewsCount: tagsViewStore.visitedViews.length,
    cachedViewsCount: tagsViewStore.cachedViews.length,
    visitedViews: tagsViewStore.visitedViews.map(v => ({ path: v.path, name: v.name, title: v.title })),
    cachedViews: tagsViewStore.cachedViews
  })
  
  // 添加访问的页面到标签页（避免重复添加）
  if (to.name && !to.meta?.hidden) {
    console.log('➕ [标签页] 添加新标签', { name: to.name, path: to.path, title: to.meta?.title })
    tagsViewStore.addView(to)
    
    console.log('📋 [标签页] 添加后状态', {
      visitedViewsCount: tagsViewStore.visitedViews.length,
      cachedViewsCount: tagsViewStore.cachedViews.length
    })
  } else {
    console.log('⏭️ [标签页] 跳过添加（隐藏页面或无名称）', { name: to.name, hidden: to.meta?.hidden })
  }
})

export default router
