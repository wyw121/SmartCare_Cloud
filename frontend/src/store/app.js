import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    // 侧边栏状态
    sidebar: {
      opened: localStorage.getItem('sidebarStatus') === 'opened' || true,
      withoutAnimation: false
    },
    // 设备类型
    device: 'desktop',
    // 主题配置
    theme: {
      // 主题模式: light | dark | auto
      mode: localStorage.getItem('theme-mode') || 'light',
      // 主题色
      primaryColor: localStorage.getItem('theme-primary') || '#409EFF',
      // 是否显示设置
      showSettings: false
    },
    // 布局配置
    layout: {
      // 布局模式: vertical | horizontal | mix
      mode: 'vertical',
      // 是否显示标签页
      showTabs: true,
      // 是否显示面包屑
      showBreadcrumb: true,
      // 是否显示顶部导航
      showHeader: true,
      // 是否显示页脚
      showFooter: false
    },
    // 页面加载状态
    pageLoading: false
  }),

  getters: {
    isMobile: (state) => state.device === 'mobile',
    isTablet: (state) => state.device === 'tablet',
    isDesktop: (state) => state.device === 'desktop',
    isDark: (state) => state.theme.mode === 'dark'
  },

  actions: {
    // 切换侧边栏
    toggleSidebar() {
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = false
      localStorage.setItem('sidebarStatus', this.sidebar.opened ? 'opened' : 'closed')
    },

    // 关闭侧边栏
    closeSidebar(withoutAnimation = false) {
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withoutAnimation
      localStorage.setItem('sidebarStatus', 'closed')
    },

    // 设置设备类型
    setDevice(device) {
      this.device = device
    },

    // 设置主题模式
    setThemeMode(mode) {
      this.theme.mode = mode
      localStorage.setItem('theme-mode', mode)
      
      // 应用主题
      const html = document.documentElement
      if (mode === 'dark') {
        html.classList.add('dark')
      } else {
        html.classList.remove('dark')
      }
    },

    // 设置主题色
    setPrimaryColor(color) {
      this.theme.primaryColor = color
      localStorage.setItem('theme-primary', color)
      
      // 设置CSS变量
      document.documentElement.style.setProperty('--el-color-primary', color)
    },

    // 切换设置面板
    toggleSettings() {
      this.theme.showSettings = !this.theme.showSettings
    },

    // 设置页面加载状态
    setPageLoading(loading) {
      this.pageLoading = loading
    }
  }
})
