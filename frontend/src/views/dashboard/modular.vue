<!-- 模块化仪表板主页面 -->
<template>
  <div class="dashboard-container">
    <!-- 页面头部 -->
    <PageHeader
      title="智慧医养仪表板"
      subtitle="数据统计与监控中心"
      :icon="Platform"
    >
      <template #extra>
        <el-button @click="refreshDashboard">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </template>
    </PageHeader>

    <!-- 统计卡片区域 -->
    <StatsSection
      :data="dashboardData.stats"
      @stat-click="handleStatClick"
    />

    <!-- 图表区域 -->
    <ChartsSection
      :data="dashboardData.charts"
      :loading="chartsLoading"
      @refresh-chart="handleRefreshChart"
    />

    <!-- 快捷操作区域 -->
    <QuickActionsSection
      :data="dashboardData.system"
      @quick-action="handleQuickAction"
    />
  </div>
</template>

<script setup>
import { Platform, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

// 组件导入
import PageHeader from '@/components/common/PageHeader.vue'
import ChartsSection from '@/components/dashboard/ChartsSection.vue'
import QuickActionsSection from '@/components/dashboard/QuickActionsSection.vue'
import StatsSection from '@/components/dashboard/StatsSection.vue'

// API导入
import { getChartData, getDashboardData } from '@/api/dashboard'

const router = useRouter()

// 响应式数据
const chartsLoading = ref(false)
const dashboardData = reactive({
  stats: {},
  charts: {},
  system: {}
})

// 加载仪表板数据
const loadDashboardData = async () => {
  try {
    const response = await getDashboardData()
    Object.assign(dashboardData, response.data)
  } catch (error) {
    console.error('Failed to load dashboard data:', error)
    // 使用模拟数据
    loadMockData()
  }
}

// 加载模拟数据
const loadMockData = () => {
  dashboardData.stats = {
    elderlyCount: 1258,
    elderlyTrend: 12,
    elderlyTrendText: '+12',
    warningCount: 23,
    warningTrend: -5,
    warningTrendText: '-5',
    equipmentOnline: 156,
    equipmentTrend: 3,
    equipmentTrendText: '+3',
    checkupCount: 89,
    checkupTrend: 15,
    checkupTrendText: '+15'
  }

  dashboardData.charts = {
    healthDistribution: [
      { value: 452, name: '健康' },
      { value: 321, name: '亚健康' },
      { value: 234, name: '轻微异常' },
      { value: 156, name: '需要关注' },
      { value: 95, name: '重点监护' }
    ],
    ageDistribution: [89, 156, 234, 321, 198, 145],
    trendDates: ['1月', '2月', '3月', '4月', '5月', '6月', '7月'],
    bloodPressureData: [12, 13, 10, 13, 9, 23, 21],
    bloodSugarData: [22, 18, 19, 23, 29, 33, 31],
    heartRateData: [15, 23, 20, 15, 19, 33, 28],
    temperatureData: [8, 12, 11, 9, 13, 18, 15]
  }

  dashboardData.system = {
    databaseStatus: '正常',
    cacheStatus: '正常',
    apiStatus: '正常',
    storageUsage: '68%'
  }
}

// 刷新仪表板
const refreshDashboard = async () => {
  ElMessage.info('正在刷新数据...')
  await loadDashboardData()
  ElMessage.success('数据刷新完成')
}

// 刷新特定图表
const handleRefreshChart = async (chartType) => {
  chartsLoading.value = true
  try {
    const response = await getChartData(chartType)
    dashboardData.charts = { ...dashboardData.charts, ...response.data }
    ElMessage.success(`${chartType}图表刷新完成`)
  } catch (error) {
    console.error('Failed to refresh chart:', error)
    ElMessage.error('图表刷新失败')
  } finally {
    chartsLoading.value = false
  }
}

// 处理统计卡片点击
const handleStatClick = (stat) => {
  const routeMap = {
    elderly: '/elderly/list',
    warning: '/health/warning',
    equipment: '/equipment/list',
    checkup: '/health/records'
  }
  
  const route = routeMap[stat.key]
  if (route) {
    router.push(route)
  }
}

// 处理快捷操作
const handleQuickAction = (action) => {
  if (action.route) {
    router.push(action.route)
  }
}

// 生命周期
onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.dashboard-container > * {
  margin-bottom: 20px;
}

.dashboard-container > *:last-child {
  margin-bottom: 0;
}
</style>
