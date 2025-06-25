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
                <el-button size="small" :type="activeTab === '7d' ? 'primary' : ''" @click="activeTab = '7d'">7天</el-button>
                <el-button size="small" :type="activeTab === '30d' ? 'primary' : ''" @click="activeTab = '30d'">30天</el-button>
                <el-button size="small" :type="activeTab === '90d' ? 'primary' : ''" @click="activeTab = '90d'">90天</el-button>
              </el-button-group>
            </div>
          </template>
          <div class="chart-container">
            <v-chart :option="trendChartOption" style="height: 300px;" v-if="chartReady" />
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
            <v-chart :option="pieChartOption" style="height: 300px;" v-if="chartReady" />
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
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 响应式数据
const activeTab = ref('7d')
const chartReady = ref(false)

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

// 图表配置
const trendChartOption = reactive({
  title: {
    text: '健康趋势分析',
    textStyle: {
      fontSize: 16,
      color: '#303133'
    }
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['健康人数', '预警人数'],
    bottom: 0
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '健康人数',
      type: 'line',
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      smooth: true,
      itemStyle: { color: '#67C23A' },
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
            offset: 1, color: 'rgba(103, 194, 58, 0.1)'
          }]
        }
      }
    },
    {
      name: '预警人数',
      type: 'line',
      data: [20, 25, 18, 23, 35, 28, 23],
      smooth: true,
      itemStyle: { color: '#F56C6C' },
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
            offset: 1, color: 'rgba(245, 108, 108, 0.1)'
          }]
        }
      }
    }
  ]
})

const pieChartOption = reactive({
  title: {
    text: '健康状况分布',
    left: 'center',
    textStyle: {
      fontSize: 16,
      color: '#303133'
    }
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
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
      radius: '50%',
      center: ['50%', '60%'],
      data: [
        { value: 856, name: '健康', itemStyle: { color: '#67C23A' } },
        { value: 234, name: '亚健康', itemStyle: { color: '#E6A23C' } },
        { value: 145, name: '疾病', itemStyle: { color: '#F56C6C' } },
        { value: 23, name: '重病', itemStyle: { color: '#909399' } }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
})

// 方法
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
onMounted(() => {
  // 模拟图表加载延迟
  setTimeout(() => {
    chartReady.value = true
    console.log('仪表板图表已加载')
  }, 1000)
  console.log('仪表板已加载')
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
}
</style>
