<template>
  <div class="dashboard-container">
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><Platform /></el-icon>
        首页仪表板
      </h1>
      <p class="page-subtitle">智慧医养大数据公共服务平台</p>
    </div>

    <!-- 统计卡片区域 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="stat in stats" :key="stat.title">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" :style="{ backgroundColor: stat.color + '20', color: stat.color }">
              <component :is="stat.icon" />
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stat.value }}</div>
              <div class="stats-title">{{ stat.title }}</div>
            </div>
          </div>
          <div class="stats-footer">
            <span class="stats-trend" :class="stat.trend">
              <el-icon v-if="stat.trend === 'up'"><ArrowUp /></el-icon>
              <el-icon v-else><ArrowDown /></el-icon>
              {{ stat.change }}
            </span>
            <span class="stats-label">较昨日</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">健康趋势分析</span>
              <el-button-group>
                <el-button size="small" :type="activeTab === '7d' ? 'primary' : ''" @click="changeTab('7d')">7天</el-button>
                <el-button size="small" :type="activeTab === '30d' ? 'primary' : ''" @click="changeTab('30d')">30天</el-button>
                <el-button size="small" :type="activeTab === '90d' ? 'primary' : ''" @click="changeTab('90d')">90天</el-button>
              </el-button-group>
            </div>
          </template>
          <div class="chart-container">
            <v-chart 
              :option="trendChartOption" 
              style="height: 300px;" 
              v-if="chartReady" 
              :key="chartKey"
            />
            <div v-else class="chart-loading">
              <el-icon style="font-size: 48px; color: #ddd;"><TrendCharts /></el-icon>
              <p>图表加载中...</p>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">健康状况分布</span>
            </div>
          </template>
          <div class="chart-container">
            <v-chart 
              :option="pieChartOption" 
              style="height: 300px;" 
              v-if="chartReady"
              :key="'pie-' + chartKey"
            />
            <div v-else class="chart-loading">
              <el-icon style="font-size: 48px; color: #ddd;"><PieChart /></el-icon>
              <p>图表加载中...</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预警信息和快速操作 -->
    <el-row :gutter="20" class="content-row">
      <el-col :xs="24" :lg="14">
        <el-card class="warning-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><Warning /></el-icon>
                健康预警
              </span>
              <el-button text type="primary" @click="handleViewAll">查看全部</el-button>
            </div>
          </template>
          <div class="warning-list">
            <div 
              class="warning-item" 
              v-for="warning in warningList" 
              :key="warning.id"
              :class="`warning-${warning.level}`"
            >
              <div class="warning-info">
                <div class="warning-title">{{ warning.elderlyName }}</div>
                <div class="warning-content">{{ warning.message }}</div>
              </div>
              <div class="warning-meta">
                <div class="warning-time">{{ warning.time }}</div>
                <el-button size="small" type="primary" text @click="handleWarning(warning)">
                  处理
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :lg="10">
        <el-card class="action-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">快速操作</span>
            </div>
          </template>
          <div class="action-grid">
            <div 
              class="action-item" 
              v-for="action in quickActions" 
              :key="action.name"
              @click="handleAction(action)"
            >
              <div class="action-icon" :style="{ backgroundColor: action.color + '20', color: action.color }">
                <component :is="action.icon" />
              </div>
              <span class="action-name">{{ action.name }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 调试信息 -->
    <el-card v-if="showDebug" class="debug-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">调试信息</span>
          <el-button size="small" @click="showDebug = false">隐藏</el-button>
        </div>
      </template>
      <div class="debug-info">
        <p><strong>图表状态：</strong>{{ chartReady ? '已加载' : '加载中' }}</p>
        <p><strong>当前标签：</strong>{{ activeTab }}</p>
        <p><strong>图表键：</strong>{{ chartKey }}</p>
        <p><strong>Vue版本：</strong>{{ vueVersion }}</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {
    ArrowDown,
    ArrowUp,
    DocumentAdd,
    Monitor,
    PieChart,
    Platform,
    Setting,
    TrendCharts,
    User,
    Warning
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { nextTick, onMounted, reactive, ref, version } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 响应式数据
const activeTab = ref('7d')
const chartReady = ref(false)
const chartKey = ref(0)
const showDebug = ref(false)
const vueVersion = version

// 统计数据
const stats = reactive([
  {
    title: '老人总数',
    value: '1,258',
    change: '+12',
    trend: 'up',
    icon: User,
    color: '#409EFF'
  },
  {
    title: '健康预警',
    value: '23',
    change: '-5',
    trend: 'down',
    icon: Warning,
    color: '#F56C6C'
  },
  {
    title: '在线设备',
    value: '89',
    change: '+3',
    trend: 'up',
    icon: Monitor,
    color: '#67C23A'
  },
  {
    title: '今日体检',
    value: '45',
    change: '+8',
    trend: 'up',
    icon: DocumentAdd,
    color: '#E6A23C'
  }
])

// 预警列表
const warningList = reactive([
  {
    id: 1,
    elderlyName: '张大爷',
    message: '血压异常，建议立即就医',
    level: 'high',
    time: '10分钟前'
  },
  {
    id: 2,
    elderlyName: '李奶奶',
    message: '心率过快，需要关注',
    level: 'medium',
    time: '25分钟前'
  },
  {
    id: 3,
    elderlyName: '王大爷',
    message: '血糖偏高，建议调整饮食',
    level: 'low',
    time: '1小时前'
  }
])

// 快速操作
const quickActions = reactive([
  { name: '新增老人', icon: User, color: '#409EFF', route: '/elderly/add' },
  { name: '健康记录', icon: DocumentAdd, color: '#67C23A', route: '/health/records' },
  { name: '设备监控', icon: Monitor, color: '#E6A23C', route: '/equipment/monitor' },
  { name: '系统设置', icon: Setting, color: '#909399', route: '/system/users' }
])

// 基础图表数据
const baseData = {
  '7d': {
    categories: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    healthy: [820, 932, 901, 934, 1290, 1330, 1320],
    warning: [20, 25, 18, 23, 35, 28, 23]
  },
  '30d': {
    categories: ['1周', '2周', '3周', '4周'],
    healthy: [6000, 6200, 6100, 6300],
    warning: [150, 180, 160, 140]
  },
  '90d': {
    categories: ['1月', '2月', '3月'],
    healthy: [18000, 19000, 18500],
    warning: [500, 520, 480]
  }
}

// 图表配置 - 使用计算属性或响应式更新
const trendChartOption = reactive({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    }
  },
  legend: {
    data: ['健康人数', '预警人数'],
    bottom: 10
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '15%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: baseData['7d'].categories,
    axisLine: {
      lineStyle: {
        color: '#E4E7ED'
      }
    }
  },
  yAxis: {
    type: 'value',
    axisLine: {
      lineStyle: {
        color: '#E4E7ED'
      }
    },
    splitLine: {
      lineStyle: {
        color: '#F2F6FC'
      }
    }
  },
  series: [
    {
      name: '健康人数',
      type: 'line',
      data: baseData['7d'].healthy,
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: {
        color: '#67C23A',
        width: 3
      },
      itemStyle: {
        color: '#67C23A'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: 'rgba(103, 194, 58, 0.3)'
          }, {
            offset: 1, color: 'rgba(103, 194, 58, 0.05)'
          }]
        }
      }
    },
    {
      name: '预警人数',
      type: 'line',
      data: baseData['7d'].warning,
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: {
        color: '#F56C6C',
        width: 3
      },
      itemStyle: {
        color: '#F56C6C'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: 'rgba(245, 108, 108, 0.3)'
          }, {
            offset: 1, color: 'rgba(245, 108, 108, 0.05)'
          }]
        }
      }
    }
  ]
})

const pieChartOption = reactive({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: ['健康', '亚健康', '疾病', '重病']
  },
  series: [
    {
      name: '健康状况',
      type: 'pie',
      radius: ['30%', '70%'],
      center: ['60%', '50%'],
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { 
          value: 856, 
          name: '健康', 
          itemStyle: { 
            color: '#67C23A',
            shadowBlur: 10,
            shadowColor: 'rgba(103, 194, 58, 0.3)'
          } 
        },
        { 
          value: 234, 
          name: '亚健康', 
          itemStyle: { 
            color: '#E6A23C',
            shadowBlur: 10,
            shadowColor: 'rgba(230, 162, 60, 0.3)'
          } 
        },
        { 
          value: 145, 
          name: '疾病', 
          itemStyle: { 
            color: '#F56C6C',
            shadowBlur: 10,
            shadowColor: 'rgba(245, 108, 108, 0.3)'
          } 
        },
        { 
          value: 23, 
          name: '重病', 
          itemStyle: { 
            color: '#909399',
            shadowBlur: 10,
            shadowColor: 'rgba(144, 147, 153, 0.3)'
          } 
        }
      ]
    }
  ]
})

// 方法
const changeTab = (tab) => {
  activeTab.value = tab
  updateTrendChart()
}

const updateTrendChart = () => {
  const data = baseData[activeTab.value]
  trendChartOption.xAxis.data = data.categories
  trendChartOption.series[0].data = data.healthy
  trendChartOption.series[1].data = data.warning
  chartKey.value++
}

const handleViewAll = () => {
  router.push('/health/warning')
}

const handleWarning = (warning) => {
  ElMessage.success(`正在处理 ${warning.elderlyName} 的预警信息`)
}

const handleAction = (action) => {
  router.push(action.route)
}

// 生命周期
onMounted(async () => {
  console.log('仪表板开始加载...')
  
  // 模拟加载延迟
  await new Promise(resolve => setTimeout(resolve, 800))
  
  chartReady.value = true
  chartKey.value++
  
  await nextTick()
  
  console.log('仪表板加载完成')
  
  // 3秒后显示调试信息
  setTimeout(() => {
    showDebug.value = true
  }, 3000)
  
  // 5秒后自动隐藏调试信息
  setTimeout(() => {
    showDebug.value = false
  }, 8000)
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  .page-header {
    margin-bottom: 24px;
    text-align: center;

    .page-title {
      margin: 0 0 8px 0;
      font-size: 28px;
      font-weight: 600;
      color: var(--el-text-color-primary);
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
    }

    .page-subtitle {
      margin: 0;
      font-size: 14px;
      color: var(--el-text-color-secondary);
    }
  }

  .stats-row {
    margin-bottom: 24px;

    .stats-card {
      border-radius: 8px;
      
      .stats-content {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        .stats-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          font-size: 24px;
        }

        .stats-info {
          flex: 1;

          .stats-value {
            font-size: 24px;
            font-weight: 600;
            color: var(--el-text-color-primary);
            line-height: 1;
          }

          .stats-title {
            font-size: 14px;
            color: var(--el-text-color-regular);
            margin-top: 4px;
          }
        }
      }

      .stats-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .stats-trend {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 12px;
          
          &.up {
            color: var(--el-color-success);
          }
          
          &.down {
            color: var(--el-color-danger);
          }
        }

        .stats-label {
          font-size: 12px;
          color: var(--el-text-color-placeholder);
        }
      }
    }
  }

  .charts-row,
  .content-row {
    margin-bottom: 24px;

    .chart-card,
    .warning-card,
    .action-card {
      border-radius: 8px;

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .card-title {
          font-size: 16px;
          font-weight: 600;
          color: var(--el-text-color-primary);
          display: flex;
          align-items: center;
          gap: 8px;
        }
      }

      .chart-container {
        padding: 16px 0;
        
        .chart-loading {
          text-align: center;
          padding: 60px 20px;
          color: var(--el-text-color-placeholder);
          
          p {
            margin: 12px 0 0 0;
            font-size: 14px;
          }
        }
      }
    }
  }

  .warning-list {
    .warning-item {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      padding: 16px;
      border-radius: 8px;
      margin-bottom: 12px;
      border-left: 4px solid transparent;

      &.warning-high {
        background-color: rgba(245, 108, 108, 0.1);
        border-left-color: var(--el-color-danger);
      }

      &.warning-medium {
        background-color: rgba(230, 162, 60, 0.1);
        border-left-color: var(--el-color-warning);
      }

      &.warning-low {
        background-color: rgba(144, 147, 153, 0.1);
        border-left-color: var(--el-color-info);
      }

      .warning-info {
        flex: 1;

        .warning-title {
          font-weight: 600;
          color: var(--el-text-color-primary);
          margin-bottom: 4px;
        }

        .warning-content {
          font-size: 14px;
          color: var(--el-text-color-regular);
        }
      }

      .warning-meta {
        text-align: right;

        .warning-time {
          font-size: 12px;
          color: var(--el-text-color-placeholder);
          margin-bottom: 8px;
        }
      }
    }
  }

  .action-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;

    .action-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      border-radius: 8px;
      background-color: var(--el-fill-color-blank);
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        background-color: var(--el-fill-color-light);
        transform: translateY(-2px);
      }

      .action-icon {
        width: 48px;
        height: 48px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12px;
        font-size: 24px;
      }

      .action-name {
        font-size: 14px;
        color: var(--el-text-color-regular);
        font-weight: 500;
      }
    }
  }

  .debug-card {
    margin-top: 24px;
    border: 1px dashed #ddd;

    .debug-info {
      font-size: 12px;
      line-height: 1.5;
      
      p {
        margin: 8px 0;
      }
    }
  }
}
</style>
