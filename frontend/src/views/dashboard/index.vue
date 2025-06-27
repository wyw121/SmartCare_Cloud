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
      <el-col :xs="12" :sm="12" :md="6" :lg="6" v-for="stat in stats" :key="stat.title">
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
      <el-col :xs="24" :sm="24" :md="24" :lg="16" :xl="16">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">健康趋势分析</span>
              <el-button-group class="chart-controls">
                <el-button size="small" :type="activeTab === '7d' ? 'primary' : ''" @click="activeTab = '7d'">7天</el-button>
                <el-button size="small" :type="activeTab === '30d' ? 'primary' : ''" @click="activeTab = '30d'">30天</el-button>
                <el-button size="small" :type="activeTab === '90d' ? 'primary' : ''" @click="activeTab = '90d'">90天</el-button>
              </el-button-group>
            </div>
          </template>
          <div class="chart-container">
            <v-chart :option="trendChartOption" class="responsive-chart" v-if="chartReady" />
            <div v-else class="chart-loading">
              <el-icon style="font-size: 48px; color: #ddd;"><TrendCharts /></el-icon>
              <p>图表加载中...</p>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="24" :md="24" :lg="8" :xl="8">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">健康状况分布</span>
            </div>
          </template>
          <div class="chart-container">
            <v-chart :option="pieChartOption" class="responsive-chart" v-if="chartReady" />
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
      <el-col :xs="24" :sm="24" :md="24" :lg="14" :xl="14">
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
      
      <el-col :xs="24" :sm="24" :md="24" :lg="10" :xl="10">
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
  console.log('仪表盘组件已挂载')
  console.log('统计数据:', stats)
  console.log('预警列表:', warningList)
  console.log('快速操作:', quickActions)
  
  // 模拟图表加载延迟
  setTimeout(() => {
    chartReady.value = true
    console.log('仪表盘图表已加载')
  }, 1000)
  console.log('仪表盘已加载')
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  width: 100%;
  min-height: 100%;
  padding: 20px;
  background-color: #f0f2f5;
  box-sizing: border-box;
}

.page-header {
  margin-bottom: 20px;
  padding: 15px 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.06);
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-subtitle {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin-top: 4px;
}

/* 统计卡片样式 */
.stats-row {
  margin-bottom: 24px;

  .stats-card {
    border-radius: 8px;
    margin-bottom: 16px;
    
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
        flex-shrink: 0;
      }

      .stats-info {
        flex: 1;
        min-width: 0;

        .stats-value {
          font-size: 24px;
          font-weight: 600;
          color: var(--el-text-color-primary);
          line-height: 1;
          word-break: break-all;
        }

        .stats-title {
          font-size: 14px;
          color: var(--el-text-color-regular);
          margin-top: 4px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }

    .stats-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: 8px;

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

/* 图表和内容区域样式 */
.charts-row,
.content-row {
  margin-bottom: 32px;

  &:last-child {
    margin-bottom: 60px;
  }

  .chart-card,
  .warning-card,
  .action-card {
    border-radius: 8px;
    margin-bottom: 16px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: 12px;

      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: var(--el-text-color-primary);
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .chart-controls {
        flex-shrink: 0;
      }
    }

    .chart-container {
      padding: 16px 0;
      
      .responsive-chart {
        width: 100%;
        height: 300px;
        min-height: 250px;
      }
      
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

/* 预警信息列表样式 */
.warning-list {
  max-height: 400px;
  overflow-y: auto;
  
  .warning-item {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 12px;
    border-left: 4px solid transparent;
    gap: 12px;

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
      min-width: 0;

      .warning-title {
        font-weight: 600;
        color: var(--el-text-color-primary);
        margin-bottom: 4px;
      }

      .warning-content {
        font-size: 14px;
        color: var(--el-text-color-regular);
        word-break: break-word;
        line-height: 1.5;
      }
    }

    .warning-meta {
      text-align: right;
      flex-shrink: 0;

      .warning-time {
        font-size: 12px;
        color: var(--el-text-color-placeholder);
        margin-bottom: 8px;
        white-space: nowrap;
      }
    }
  }
}

/* 快速操作网格样式 */
.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
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
    min-height: 120px;
    justify-content: center;

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
      flex-shrink: 0;
    }

    .action-name {
      font-size: 14px;
      color: var(--el-text-color-regular);
      font-weight: 500;
      text-align: center;
      word-break: break-word;
    }
  }
}

/* 确保 Element Plus 布局组件正常工作 */
:deep(.el-row) {
  width: 100%;
  margin: 0;
}

:deep(.el-col) {
  min-height: 1px;
}

/* 强制显示右侧内容 */
.content-row {
  .warning-card,
  .action-card {
    width: 100%;
    min-height: 400px;
  }
}

/* 响应式断点优化 */
@media (max-width: 1200px) {
  .dashboard-container {
    .charts-row {
      .chart-container .responsive-chart {
        height: 280px;
      }
    }
  }
}

@media (max-width: 992px) {
  .dashboard-container {
    padding: 16px;
    
    .charts-row {
      .chart-card {
        margin-bottom: 20px;
      }
      
      .chart-container .responsive-chart {
        height: 260px;
      }
    }
    
    .content-row {
      .warning-card,
      .action-card {
        margin-bottom: 20px;
      }
    }
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 12px;
    
    .page-header {
      .page-title {
        font-size: 24px;
        flex-direction: column;
        gap: 4px;
      }
    }
    
    .stats-card {
      .stats-content {
        .stats-icon {
          width: 40px;
          height: 40px;
          font-size: 20px;
          margin-right: 12px;
        }
        
        .stats-info {
          .stats-value {
            font-size: 20px;
          }
          
          .stats-title {
            font-size: 13px;
          }
        }
      }
      
      .stats-footer {
        .stats-trend,
        .stats-label {
          font-size: 11px;
        }
      }
    }
    
    .charts-row {
      .card-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;
        
        .chart-controls {
          align-self: stretch;
          
          .el-button-group {
            width: 100%;
            
            .el-button {
              flex: 1;
            }
          }
        }
      }
      
      .chart-container .responsive-chart {
        height: 240px;
      }
    }
    
    .warning-list {
      .warning-item {
        flex-direction: column;
        align-items: stretch;
        gap: 8px;
        
        .warning-meta {
          text-align: left;
          display: flex;
          justify-content: space-between;
          align-items: center;
        }
      }
    }
    
    .action-grid {
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;
      
      .action-item {
        padding: 16px 12px;
        min-height: 100px;
        
        .action-icon {
          width: 40px;
          height: 40px;
          font-size: 20px;
          margin-bottom: 8px;
        }
        
        .action-name {
          font-size: 13px;
        }
      }
    }
  }
}

@media (max-width: 480px) {
  .dashboard-container {
    padding: 8px;
    
    .stats-row {
      .el-col {
        margin-bottom: 12px;
      }
    }
    
    .charts-row,
    .content-row {
      margin-bottom: 16px;
      
      .el-col {
        margin-bottom: 16px;
      }
    }
    
    .chart-container .responsive-chart {
      height: 200px;
    }
    
    .warning-list {
      max-height: 300px;
    }
    
    .action-grid {
      .action-item {
        min-height: 80px;
        padding: 12px 8px;
        
        .action-icon {
          width: 32px;
          height: 32px;
          font-size: 16px;
          margin-bottom: 6px;
        }
        
        .action-name {
          font-size: 12px;
        }
      }
    }
  }
}
</style>
