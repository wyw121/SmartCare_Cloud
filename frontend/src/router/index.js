import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

/**
 * 路由配置
 * meta 配置项说明:
 * - title: 页面标题，用于浏览器标签页和面包屑导航
 * - icon: 菜单图标
 * - roles: 页面权限，如果不设置则表示所有角色都可以访问
 * - noCache: 如果设置为true，则不会被 <keep-alive> 缓存
 * - affix: 如果设置为true，tag将无法被删除
 * - breadcrumb: 如果设置为false，则不会在breadcrumb面包屑中显示
 * - activeMenu: 如果设置路径，sidebar会高亮你设置的路径
 * - hidden: 如果设置为true，该路由将不会在侧边栏中显示
 */

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页仪表板', icon: 'dashboard', affix: true }
      }
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
        meta: { title: '老人档案列表', icon: 'list' }
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
      }
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
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 智慧医养大数据平台` : '智慧医养大数据平台'
  next()
})

export default router
