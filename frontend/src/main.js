import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { createPinia } from 'pinia'
import { createApp } from 'vue'

// 正确导入 ECharts
import {
  BarChart,
  LineChart,
  PieChart
} from 'echarts/charts'
import {
  GridComponent,
  LegendComponent,
  TitleComponent,
  TooltipComponent
} from 'echarts/components'
import * as echarts from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'

// 正确导入 vue-echarts
import VChart, { THEME_KEY } from 'vue-echarts'

import App from './App.vue'
import router from './router'
import './styles/index.scss'
import { permissionDirective, roleDirective } from './utils/permission'
import directives from './directives' // 导入自定义指令

// 注册 ECharts 组件
echarts.use([
  BarChart,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  CanvasRenderer
])

const app = createApp(App)

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册 vue-echarts 组件
app.component('v-chart', VChart)
app.provide(THEME_KEY, 'light')

// 注册权限指令
app.directive('permission', permissionDirective)
app.directive('role', roleDirective)

// 注册自定义指令（图片懒加载等）
app.use(directives)

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn,
})

console.log('App is mounting...')
app.mount('#app')
console.log('App mounted successfully')
