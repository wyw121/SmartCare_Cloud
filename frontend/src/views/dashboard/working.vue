<template>
  <div class="dashboard-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><Platform /></el-icon>
        智慧医养仪表板
      </h1>
      <p class="page-subtitle">数据统计与监控中心</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: rgba(64, 158, 255, 0.2); color: #409EFF;">
              <el-icon><User /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">1,258</div>
              <div class="stats-title">老人总数</div>
            </div>
          </div>
          <div class="stats-footer">
            <span class="stats-trend up">
              <el-icon><ArrowUp /></el-icon>
              +12
            </span>
            <span class="stats-label">较昨日</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: rgba(245, 108, 108, 0.2); color: #F56C6C;">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">23</div>
              <div class="stats-title">健康预警</div>
            </div>
          </div>
          <div class="stats-footer">
            <span class="stats-trend down">
              <el-icon><ArrowDown /></el-icon>
              -5
            </span>
            <span class="stats-label">较昨日</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: rgba(103, 194, 58, 0.2); color: #67C23A;">
              <el-icon><Monitor /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">89</div>
              <div class="stats-title">在线设备</div>
            </div>
          </div>
          <div class="stats-footer">
            <span class="stats-trend up">
              <el-icon><ArrowUp /></el-icon>
              +3
            </span>
            <span class="stats-label">较昨日</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: rgba(230, 162, 60, 0.2); color: #E6A23C;">
              <el-icon><DocumentAdd /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">45</div>
              <div class="stats-title">今日体检</div>
            </div>
          </div>
          <div class="stats-footer">
            <span class="stats-trend up">
              <el-icon><ArrowUp /></el-icon>
              +8
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
              <span class="card-title">
                <el-icon><TrendCharts /></el-icon>
                健康趋势分析
              </span>
              <div class="chart-controls">
                <el-button-group size="small">
                  <el-button :type="timeRange === '7d' ? 'primary' : ''" @click="timeRange = '7d'">7天</el-button>
                  <el-button :type="timeRange === '30d' ? 'primary' : ''" @click="timeRange = '30d'">30天</el-button>
                  <el-button :type="timeRange === '90d' ? 'primary' : ''" @click="timeRange = '90d'">90天</el-button>
                </el-button-group>
              </div>
            </div>
          </template>
          <div class="chart-container">
            <v-chart :option="lineChartOption" style="height: 350px;" />
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><PieChart /></el-icon>
                健康状况分布
              </span>
            </div>
          </template>
          <div class="chart-container">
            <v-chart :option="pieChartOption" style="height: 350px;" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预警信息 -->
    <el-row :gutter="20" class="bottom-row">
      <el-col :xs="24" :lg="14">
        <el-card class="warning-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><Warning /></el-icon>
                实时预警信息
              </span>
              <el-button text type="primary">查看全部</el-button>
            </div>
          </template>
          <div class="warning-list">
            <div class="warning-item high">
              <div class="warning-info">
                <div class="warning-title">张大爷 - 血压异常</div>
                <div class="warning-desc">收缩压185mmHg，建议立即就医</div>
              </div>
              <div class="warning-meta">
                <span class="warning-time">5分钟前</span>
                <el-button size="small" type="danger">立即处理</el-button>
              </div>
            </div>
            
            <div class="warning-item medium">
              <div class="warning-info">
                <div class="warning-title">李奶奶 - 心率异常</div>
                <div class="warning-desc">心率102次/分钟，需要关注</div>
              </div>
              <div class="warning-meta">
                <span class="warning-time">15分钟前</span>
                <el-button size="small" type="warning">查看详情</el-button>
              </div>
            </div>
            
            <div class="warning-item low">
              <div class="warning-info">
                <div class="warning-title">王大爷 - 血糖偏高</div>
                <div class="warning-desc">餐后血糖8.5mmol/L，建议调整饮食</div>
              </div>
              <div class="warning-meta">
                <span class="warning-time">1小时前</span>
                <el-button size="small" type="info">已关注</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :lg="10">
        <el-card class="action-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><Operation /></el-icon>
                快速操作
              </span>
            </div>
          </template>
          <div class="action-grid">
            <div class="action-item" @click="quickAction('add-elderly')">
              <div class="action-icon" style="background-color: rgba(64, 158, 255, 0.2); color: #409EFF;">
                <el-icon><User /></el-icon>
              </div>
              <span class="action-name">新增老人</span>
            </div>
            
            <div class="action-item" @click="quickAction('health-record')">
              <div class="action-icon" style="background-color: rgba(103, 194, 58, 0.2); color: #67C23A;">
                <el-icon><DocumentAdd /></el-icon>
              </div>
              <span class="action-name">健康记录</span>
            </div>
            
            <div class="action-item" @click="quickAction('device-monitor')">
              <div class="action-icon" style="background-color: rgba(230, 162, 60, 0.2); color: #E6A23C;">
                <el-icon><Monitor /></el-icon>
              </div>
              <span class="action-name">设备监控</span>
            </div>
            
            <div class="action-item" @click="quickAction('report')">
              <div class="action-icon" style="background-color: rgba(144, 147, 153, 0.2); color: #909399;">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <span class="action-name">数据报告</span>
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
    DataAnalysis,
    DocumentAdd,
    Monitor,
    Operation,
    PieChart,
    Platform,
    TrendCharts,
    User, Warning
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const timeRange = ref('7d')

// 折线图配置
const lineChartOption = reactive({
  title: {
    text: '健康趋势分析',
    left: 'center',
    textStyle: {
      fontSize: 16,
      color: '#303133'
    }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    }
  },
  legend: {
    data: ['健康人数', '预警人数'],
    top: 40
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '20%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '健康人数',
      type: 'line',
      stack: 'Total',
      smooth: true,
      lineStyle: {
        color: '#67C23A'
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
            offset: 1, color: 'rgba(103, 194, 58, 0.1)'
          }]
        }
      },
      data: [820, 932, 901, 934, 1290, 1330, 1320]
    },
    {
      name: '预警人数',
      type: 'line',
      stack: 'Total',
      smooth: true,
      lineStyle: {
        color: '#F56C6C'
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
            offset: 1, color: 'rgba(245, 108, 108, 0.1)'
          }]
        }
      },
      data: [20, 25, 18, 23, 35, 28, 23]
    }
  ]
})

// 饼图配置
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
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    top: 'middle',
    data: ['健康', '亚健康', '疾病', '重病']
  },
  series: [
    {
      name: '健康状况',
      type: 'pie',
      radius: ['40%', '70%'],
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
        { value: 856, name: '健康', itemStyle: { color: '#67C23A' } },
        { value: 234, name: '亚健康', itemStyle: { color: '#E6A23C' } },
        { value: 145, name: '疾病', itemStyle: { color: '#F56C6C' } },
        { value: 23, name: '重病', itemStyle: { color: '#909399' } }
      ]
    }
  ]
})

// 快速操作
const quickAction = (action) => {
  switch (action) {
    case 'add-elderly':
      router.push('/elderly/add')
      break
    case 'health-record':
      router.push('/health/records')
      break
    case 'device-monitor':
      router.push('/equipment/monitor')
      break
    case 'report':
      router.push('/reports/statistics')
      break
    default:
      ElMessage.info(`执行操作: ${action}`)
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 140px);

  .page-header {
    text-align: center;
    margin-bottom: 30px;

    .page-title {
      margin: 0 0 8px 0;
      font-size: 28px;
      font-weight: 600;
      color: #303133;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
    }

    .page-subtitle {
      margin: 0;
      font-size: 14px;
      color: #909399;
    }
  }

  .stats-row {
    margin-bottom: 30px;

    .stats-card {
      border-radius: 8px;
      border: none;
      
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
            font-size: 28px;
            font-weight: 700;
            color: #303133;
            line-height: 1;
          }

          .stats-title {
            font-size: 14px;
            color: #606266;
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
          font-weight: 600;
          
          &.up {
            color: #67C23A;
          }
          
          &.down {
            color: #F56C6C;
          }
        }

        .stats-label {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .charts-row,
  .bottom-row {
    margin-bottom: 30px;

    .chart-card,
    .warning-card,
    .action-card {
      border-radius: 8px;
      border: none;

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .card-title {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          display: flex;
          align-items: center;
          gap: 8px;
        }
      }

      .chart-container {
        padding: 10px 0;
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
      background: #fafafa;

      &.high {
        border-left-color: #F56C6C;
        background: rgba(245, 108, 108, 0.05);
      }

      &.medium {
        border-left-color: #E6A23C;
        background: rgba(230, 162, 60, 0.05);
      }

      &.low {
        border-left-color: #909399;
        background: rgba(144, 147, 153, 0.05);
      }

      .warning-info {
        flex: 1;

        .warning-title {
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }

        .warning-desc {
          font-size: 14px;
          color: #606266;
        }
      }

      .warning-meta {
        text-align: right;

        .warning-time {
          display: block;
          font-size: 12px;
          color: #909399;
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
      padding: 24px;
      border-radius: 8px;
      background: #fff;
      cursor: pointer;
      transition: all 0.3s;
      border: 1px solid #ebeef5;

      &:hover {
        background: #f5f7fa;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
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
        color: #606266;
        font-weight: 500;
      }
    }
  }
}
</style>
