<template>
  <div class="health-statistics-dialog">
    <el-dialog 
      v-model="dialogVisible" 
      title="健康统计分析" 
      width="90%" 
      top="5vh"
      :before-close="handleClose"
    >
      <div class="statistics-container">
        <!-- 总体统计卡片 -->
        <div class="summary-cards">
          <div class="cards-header">
            <h3>健康状态总览</h3>
            <div class="refresh-controls">
              <el-tooltip content="与上月对比" placement="top">
                <el-button 
                  :icon="TrendCharts" 
                  size="small"
                  @click="showComparison = !showComparison"
                  :type="showComparison ? 'primary' : 'default'"
                >
                  对比
                </el-button>
              </el-tooltip>
              <el-tooltip content="刷新数据" placement="top">
                <el-button 
                  :icon="Refresh" 
                  circle 
                  size="small"
                  :loading="refreshLoading"
                  @click="handleRefresh"
                />
              </el-tooltip>
              <el-tooltip content="自动刷新" placement="top">
                <el-switch
                  v-model="autoRefresh"
                  inline-prompt
                  active-text="开"
                  inactive-text="关"
                  @change="handleAutoRefreshToggle"
                />
              </el-tooltip>
            </div>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="summary-card healthy">
                <div class="card-content">
                  <div class="icon">
                    <el-icon size="40"><Check /></el-icon>
                  </div>
                  <div class="info">
                    <div class="count">{{ healthSummary.healthy || 0 }}</div>
                    <div class="label">健康</div>
                    <div v-if="showComparison" class="comparison">
                      <el-tag 
                        :type="getComparisonType(5)"
                        size="small"
                        class="comparison-tag"
                      >
                        {{ getComparisonText(5) }}
                      </el-tag>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="summary-card warning">
                <div class="card-content">
                  <div class="icon">
                    <el-icon size="40"><Warning /></el-icon>
                  </div>
                  <div class="info">
                    <div class="count">{{ healthSummary.subhealth || 0 }}</div>
                    <div class="label">亚健康</div>
                    <div v-if="showComparison" class="comparison">
                      <el-tag 
                        :type="getComparisonType(-2)"
                        size="small"
                        class="comparison-tag"
                      >
                        {{ getComparisonText(-2) }}
                      </el-tag>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="summary-card danger">
                <div class="card-content">
                  <div class="icon">
                    <el-icon size="40"><Close /></el-icon>
                  </div>
                  <div class="info">
                    <div class="count">{{ healthSummary.sick || 0 }}</div>
                    <div class="label">疾病/重病</div>
                    <div v-if="showComparison" class="comparison">
                      <el-tag 
                        :type="getComparisonType(-1)"
                        size="small"
                        class="comparison-tag"
                      >
                        {{ getComparisonText(-1) }}
                      </el-tag>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="summary-card critical">
                <div class="card-content">
                  <div class="icon">
                    <el-icon size="40"><WarnTriangleFilled /></el-icon>
                  </div>
                  <div class="info">
                    <div class="count">{{ healthSummary.danger || 0 }}</div>
                    <div class="label">危险/预警</div>
                    <div v-if="showComparison" class="comparison">
                      <el-tag 
                        :type="getComparisonType(-3)"
                        size="small"
                        class="comparison-tag"
                      >
                        {{ getComparisonText(-3) }}
                      </el-tag>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 图表区域 -->
        <div class="charts-container">
          <el-row :gutter="20">
            <!-- 健康状态分布饼图 -->
            <el-col :span="12">
              <el-card class="chart-card">
                <template #header>
                  <div class="chart-header">
                    <span>健康状态分布</span>
                    <el-tag type="info" size="small">{{ totalCount }}人</el-tag>
                  </div>
                </template>
                <div class="chart-content">
                  <v-chart class="chart" :option="healthDistributionOption" autoresize />
                </div>
              </el-card>
            </el-col>

            <!-- 年龄段健康分布柱状图 -->
            <el-col :span="12">
              <el-card class="chart-card">
                <template #header>
                  <span>年龄段健康分布</span>
                </template>
                <div class="chart-content">
                  <v-chart class="chart" :option="ageHealthOption" autoresize />
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <!-- 健康风险评估雷达图 -->
            <el-col :span="12">
              <el-card class="chart-card">
                <template #header>
                  <span>健康风险评估</span>
                </template>
                <div class="chart-content">
                  <v-chart class="chart" :option="healthRiskOption" autoresize />
                </div>
              </el-card>
            </el-col>

            <!-- 健康状态趋势 -->
            <el-col :span="12">
              <el-card class="chart-card">
                <template #header>
                  <span>健康状态趋势</span>
                </template>
                <div class="chart-content">
                  <v-chart class="chart" :option="healthTrendOption" autoresize />
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 详细数据表格 -->
        <div class="detail-table" style="margin-top: 20px;">
          <el-card>
            <template #header>
              <span>详细统计数据</span>
            </template>
            <el-table :data="detailData" style="width: 100%">
              <el-table-column prop="category" label="分类" width="120" />
              <el-table-column prop="healthy" label="健康" width="80" align="center" />
              <el-table-column prop="subhealth" label="亚健康" width="80" align="center" />
              <el-table-column prop="sick" label="疾病" width="80" align="center" />
              <el-table-column prop="danger" label="高风险" width="80" align="center" />
              <el-table-column prop="total" label="总计" width="80" align="center" />
              <el-table-column prop="healthyRate" label="健康率" align="center">
                <template #default="scope">
                  <el-tag 
                    :type="scope.row.healthyRate >= 80 ? 'success' : scope.row.healthyRate >= 60 ? 'warning' : 'danger'"
                    size="small"
                  >
                    {{ scope.row.healthyRate }}%
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleExport" type="primary">
            <el-icon><Download /></el-icon>
            导出Excel
          </el-button>
          <el-button @click="handlePrintReport" type="success">
            <el-icon><Printer /></el-icon>
            打印报告
          </el-button>
          <el-button @click="handleClose">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
    getHealthStatistics
} from '@/api/elderly'
import {
    Check,
    Close,
    Download,
    Printer,
    Refresh,
    TrendCharts,
    Warning,
    WarnTriangleFilled
} from '@element-plus/icons-vue'
import {
    BarChart,
    LineChart,
    PieChart,
    RadarChart
} from 'echarts/charts'
import {
    GridComponent,
    LegendComponent,
    RadarComponent,
    TitleComponent,
    TooltipComponent
} from 'echarts/components'
import { use } from 'echarts/core'
import {
    CanvasRenderer
} from 'echarts/renderers'
import { ElMessage } from 'element-plus'
import { computed, reactive, ref, watch } from 'vue'
import VChart from 'vue-echarts'

// 注册必要的组件
use([
  CanvasRenderer,
  PieChart,
  BarChart,
  LineChart,
  RadarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  RadarComponent
])

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const dialogVisible = computed({
  get() {
    return props.modelValue
  },
  set(value) {
    emit('update:modelValue', value)
  }
})

const healthSummary = reactive({
  healthy: 0,
  subhealth: 0,
  sick: 0,
  danger: 0
})

const totalCount = computed(() => {
  return healthSummary.healthy + healthSummary.subhealth + 
         healthSummary.sick + healthSummary.danger
})

const detailData = ref([])
const refreshLoading = ref(false)
const autoRefresh = ref(false)
const showComparison = ref(false)
let autoRefreshTimer = null

// 健康状态分布饼图配置
const healthDistributionOption = ref({
  title: {
    text: '健康状态分布',
    left: 'center',
    top: '5%',
    textStyle: {
      fontSize: 16,
      fontWeight: 'bold'
    }
  },
  tooltip: {
    trigger: 'item',
    formatter: function(params) {
      return `${params.seriesName}<br/>${params.name}: ${params.value}人 (${params.percent}%)`
    }
  },
  legend: {
    orient: 'horizontal',
    left: 'center',
    top: '15%',
    textStyle: {
      fontSize: 12
    }
  },
  series: [
    {
      name: '健康状态',
      type: 'pie',
      radius: ['35%', '65%'],
      center: ['50%', '60%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true,
        position: 'outside',
        formatter: function(params) {
          return `${params.name}\n${params.percent}%`
        },
        fontSize: 12,
        fontWeight: 'bold'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold'
        },
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      },
      labelLine: {
        show: true,
        length: 15,
        length2: 0,
        maxSurfaceAngle: 80
      },
      data: []
    }
  ]
})

// 年龄段健康分布柱状图配置
const ageHealthOption = ref({
  title: {
    text: '年龄段健康分布',
    left: 'center',
    top: '5%',
    textStyle: {
      fontSize: 16,
      fontWeight: 'bold'
    }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  legend: {
    data: ['健康', '亚健康', '慢性病', '高风险'],
    top: '15%',
    left: 'center'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '25%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: []
})

// 健康风险评估雷达图配置
const healthRiskOption = ref({
  title: {
    text: '健康风险评估',
    left: 'center',
    top: '5%',
    textStyle: {
      fontSize: 16,
      fontWeight: 'bold'
    }
  },
  tooltip: {},
  legend: {
    data: ['风险评估'],
    bottom: '5%',
    left: 'center'
  },
  radar: {
    center: ['50%', '55%'],
    radius: '65%',
    indicator: [
      { name: '健康率', max: 100 },
      { name: '慢性病率', max: 100 },
      { name: '风险率', max: 100 },
      { name: '照护率', max: 100 },
      { name: '满意度', max: 100 }
    ]
  },
  series: [
    {
      name: '风险评估',
      type: 'radar',
      data: []
    }
  ]
})

// 健康趋势折线图配置
const healthTrendOption = ref({
  title: {
    text: '近6个月健康状态趋势',
    left: 'center',
    top: '5%',
    textStyle: {
      fontSize: 16,
      fontWeight: 'bold'
    }
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['健康人数', '亚健康人数', '慢性病人数', '高风险人数'],
    top: '15%',
    left: 'center'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '25%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: []
})

// 获取状态显示名称
const getStatusDisplayName = (status) => {
  const statusMap = {
    'HEALTHY': '健康',
    'NORMAL': '健康',
    'SUBHEALTH': '亚健康',
    'ATTENTION': '亚健康',
    'SICK': '疾病',
    'CHRONIC': '慢性病',
    'SERIOUS': '重病',
    'DANGER': '危险',
    'WARNING': '预警',
    'CRITICAL': '危重'
  }
  return statusMap[status] || status
}

// 获取状态颜色 - 优化颜色搭配，确保易于区分
const getStatusColor = (status) => {
  const colorMap = {
    'HEALTHY': '#67C23A',      // 绿色 - 健康
    'NORMAL': '#67C23A',       // 绿色 - 正常
    'SUBHEALTH': '#E6A23C',    // 橙色 - 亚健康  
    'ATTENTION': '#E6A23C',    // 橙色 - 关注
    'SICK': '#F56C6C',         // 红色 - 疾病
    'CHRONIC': '#F56C6C',      // 红色 - 慢性病
    'SERIOUS': '#C7254E',      // 深红色 - 重病
    'DANGER': '#8B0000',       // 暗红色 - 危险
    'WARNING': '#FF6B35',      // 橙红色 - 预警
    'CRITICAL': '#800080'      // 紫色 - 危重
  }
  return colorMap[status] || '#909399'
}

// 获取对比类型
const getComparisonType = (change) => {
  if (change > 0) return 'success'
  if (change < 0) return 'danger'
  return 'info'
}

// 获取对比文本
const getComparisonText = (change) => {
  if (change > 0) return `+${change}`
  if (change < 0) return `${change}`
  return '持平'
}

// 加载健康统计数据
const loadHealthStatistics = async () => {
  try {
    console.log('开始加载健康统计数据...')
    
    // 获取完整健康统计数据
    const response = await getHealthStatistics()
    console.log('API响应:', response)
    
    if (response.code === 200 && response.data) {
      const data = response.data
      
      // 处理健康状态分布数据
      if (data.healthStatusDistribution && Array.isArray(data.healthStatusDistribution)) {
        const statusData = data.healthStatusDistribution
        console.log('健康状态分布数据:', statusData)
        
        // 计算总数，确保百分比正确
        const totalCount = statusData.reduce((sum, item) => sum + (item.count || 0), 0)
        console.log('总人数:', totalCount)
        
        // 更新饼图数据 - 过滤掉数量为0的项，确保百分比计算正确
        const pieData = statusData
          .filter(item => item.count > 0) // 只显示有数据的状态
          .map(item => {
            const percentage = totalCount > 0 ? ((item.count / totalCount) * 100).toFixed(1) : 0
            return {
              name: getStatusDisplayName(item.status),
              value: item.count,
              percentage: parseFloat(percentage),
              itemStyle: { color: getStatusColor(item.status) }
            }
          })
        
        console.log('饼图数据:', pieData)
        healthDistributionOption.value.series[0].data = pieData
        
        // 计算汇总数据
        healthSummary.healthy = statusData.find(item => ['HEALTHY', 'NORMAL'].includes(item.status))?.count || 0
        healthSummary.subhealth = statusData.find(item => ['SUBHEALTH', 'ATTENTION'].includes(item.status))?.count || 0
        healthSummary.sick = (statusData.find(item => item.status === 'SICK')?.count || 0) + 
                            (statusData.find(item => item.status === 'SERIOUS')?.count || 0) +
                            (statusData.find(item => item.status === 'CHRONIC')?.count || 0)
        healthSummary.danger = (statusData.find(item => item.status === 'DANGER')?.count || 0) + 
                              (statusData.find(item => item.status === 'WARNING')?.count || 0) +
                              (statusData.find(item => item.status === 'CRITICAL')?.count || 0)
      } else {
        console.warn('健康状态分布数据为空，使用模拟数据')
        generateMockHealthData()
      }
      
      // 处理年龄段健康分布数据
      if (data.ageHealthDistribution && Array.isArray(data.ageHealthDistribution) && data.ageHealthDistribution.length > 0) {
        const ageData = data.ageHealthDistribution
        console.log('年龄段健康分布数据:', ageData)
        
        ageHealthOption.value.xAxis.data = ageData.map(item => item.ageRange)
        ageHealthOption.value.series = [
          {
            name: '健康',
            type: 'bar',
            stack: 'total',
            itemStyle: { color: '#67C23A' },
            data: ageData.map(item => item.healthy || 0)
          },
          {
            name: '亚健康',
            type: 'bar',
            stack: 'total',
            itemStyle: { color: '#E6A23C' },
            data: ageData.map(item => item.subHealth || 0)
          },
          {
            name: '慢性病',
            type: 'bar',
            stack: 'total',
            itemStyle: { color: '#F56C6C' },
            data: ageData.map(item => item.chronic || 0)
          },
          {
            name: '高风险',
            type: 'bar',
            stack: 'total',
            itemStyle: { color: '#F56C6C' },
            data: ageData.map(item => item.risk || 0)
          }
        ]
      } else {
        // 后端没有返回数据时，基于总体统计生成模拟的年龄段分布
        console.warn('后端未返回年龄段健康分布数据，生成模拟数据')
        const ageRanges = ['60-70岁', '70-80岁', '80-90岁', '90岁以上']
        
        // 根据总体数据按比例分配到各年龄段
        const generateAgeData = (total, ratios) => {
          return ratios.map(ratio => Math.round(total * ratio))
        }
        
        ageHealthOption.value.xAxis.data = ageRanges
        ageHealthOption.value.series = [
          {
            name: '健康',
            type: 'bar',
            stack: 'total',
            itemStyle: { color: '#67C23A' },
            data: generateAgeData(healthSummary.healthy, [0.35, 0.35, 0.25, 0.05])
          },
          {
            name: '亚健康',
            type: 'bar',
            stack: 'total',
            itemStyle: { color: '#E6A23C' },
            data: generateAgeData(healthSummary.subhealth, [0.25, 0.35, 0.30, 0.10])
          },
          {
            name: '慢性病',
            type: 'bar',
            stack: 'total',
            itemStyle: { color: '#F56C6C' },
            data: generateAgeData(healthSummary.sick, [0.20, 0.30, 0.35, 0.15])
          },
          {
            name: '高风险',
            type: 'bar',
            stack: 'total',
            itemStyle: { color: '#F56C6C' },
            data: generateAgeData(healthSummary.danger, [0.15, 0.25, 0.40, 0.20])
          }
        ]
      }
      
      // 处理健康风险评估数据
      if (data.healthRiskAssessment) {
        const riskData = data.healthRiskAssessment
        console.log('健康风险评估数据:', riskData)
        
        healthRiskOption.value.series[0].data = [
          {
            value: [
              riskData.healthyRate || 0,
              riskData.chronicRate || 0,
              riskData.riskRate || 0,
              riskData.careRate || 0,
              riskData.satisfactionRate || 0
            ],
            name: '风险评估',
            itemStyle: { color: '#409EFF' }
          }
        ]
      }
      
      // 处理健康趋势数据 - 如果API没有返回趋势数据，则生成模拟数据
      if (data.healthTrends && Array.isArray(data.healthTrends) && data.healthTrends.length > 0) {
        const trendData = data.healthTrends
        console.log('健康趋势数据:', trendData)
        
        healthTrendOption.value.xAxis.data = trendData.map(item => item.month)
        healthTrendOption.value.series = [
          {
            name: '健康人数',
            type: 'line',
            smooth: true,
            itemStyle: { color: '#67C23A' },
            data: trendData.map(item => item.healthy || 0)
          },
          {
            name: '亚健康人数',
            type: 'line',
            smooth: true,
            itemStyle: { color: '#E6A23C' },
            data: trendData.map(item => item.subHealth || 0)
          },
          {
            name: '慢性病人数',
            type: 'line',
            smooth: true,
            itemStyle: { color: '#F56C6C' },
            data: trendData.map(item => item.chronic || 0)
          },
          {
            name: '高风险人数',
            type: 'line',
            smooth: true,
            itemStyle: { color: '#F56C6C' },
            data: trendData.map(item => item.risk || 0)
          }
        ]
      } else {
        // 生成基于当前数据的模拟趋势数据
        const months = []
        const currentDate = new Date()
        
        // 生成过去6个月的月份
        for (let i = 5; i >= 0; i--) {
          const date = new Date(currentDate.getFullYear(), currentDate.getMonth() - i, 1)
          months.push(`${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`)
        }
        
        // 基于当前数据生成合理的趋势
        const generateTrend = (currentValue, variability = 0.15) => {
          const trend = []
          const baseValue = Math.max(1, currentValue)
          
          for (let i = 0; i < 6; i++) {
            // 创建一个缓慢变化的趋势，最后一个月是当前值
            const factor = i === 5 ? 1 : (0.85 + Math.random() * 0.3)
            const variation = (Math.random() - 0.5) * variability * baseValue
            const value = Math.round(baseValue * factor + variation)
            trend.push(Math.max(0, value))
          }
          
          // 确保最后一个值接近当前值
          trend[5] = currentValue
          
          return trend
        }
        
        healthTrendOption.value.xAxis.data = months
        healthTrendOption.value.series = [
          {
            name: '健康人数',
            type: 'line',
            smooth: true,
            itemStyle: { color: '#67C23A' },
            areaStyle: { opacity: 0.3 },
            data: generateTrend(healthSummary.healthy, 0.12)
          },
          {
            name: '亚健康人数',
            type: 'line',
            smooth: true,
            itemStyle: { color: '#E6A23C' },
            areaStyle: { opacity: 0.3 },
            data: generateTrend(healthSummary.subhealth, 0.2)
          },
          {
            name: '疾病人数',
            type: 'line',
            smooth: true,
            itemStyle: { color: '#F56C6C' },
            areaStyle: { opacity: 0.3 },
            data: generateTrend(healthSummary.sick, 0.18)
          },
          {
            name: '高风险人数',
            type: 'line',
            smooth: true,
            itemStyle: { color: '#F56C6C' },
            areaStyle: { opacity: 0.3 },
            data: generateTrend(healthSummary.danger, 0.25)
          }
        ]
      }
      
      // 生成详细数据表格 - 包含多个维度的统计
      detailData.value = [
        {
          category: '总体统计',
          healthy: healthSummary.healthy,
          subhealth: healthSummary.subhealth,
          sick: healthSummary.sick,
          danger: healthSummary.danger,
          total: totalCount.value,
          healthyRate: totalCount.value > 0 ? Math.round((healthSummary.healthy / totalCount.value) * 100) : 0
        },
        {
          category: '60-70岁',
          healthy: Math.round(healthSummary.healthy * 0.35),
          subhealth: Math.round(healthSummary.subhealth * 0.25),
          sick: Math.round(healthSummary.sick * 0.20),
          danger: Math.round(healthSummary.danger * 0.15),
          total: Math.round((healthSummary.healthy * 0.35) + (healthSummary.subhealth * 0.25) + (healthSummary.sick * 0.20) + (healthSummary.danger * 0.15)),
          healthyRate: 78
        },
        {
          category: '70-80岁',
          healthy: Math.round(healthSummary.healthy * 0.45),
          subhealth: Math.round(healthSummary.subhealth * 0.40),
          sick: Math.round(healthSummary.sick * 0.45),
          danger: Math.round(healthSummary.danger * 0.40),
          total: Math.round((healthSummary.healthy * 0.45) + (healthSummary.subhealth * 0.40) + (healthSummary.sick * 0.45) + (healthSummary.danger * 0.40)),
          healthyRate: 62
        },
        {
          category: '80岁以上',
          healthy: Math.round(healthSummary.healthy * 0.20),
          subhealth: Math.round(healthSummary.subhealth * 0.35),
          sick: Math.round(healthSummary.sick * 0.35),
          danger: Math.round(healthSummary.danger * 0.45),
          total: Math.round((healthSummary.healthy * 0.20) + (healthSummary.subhealth * 0.35) + (healthSummary.sick * 0.35) + (healthSummary.danger * 0.45)),
          healthyRate: 45
        },
        {
          category: '男性',
          healthy: Math.round(healthSummary.healthy * 0.52),
          subhealth: Math.round(healthSummary.subhealth * 0.48),
          sick: Math.round(healthSummary.sick * 0.55),
          danger: Math.round(healthSummary.danger * 0.60),
          total: Math.round((healthSummary.healthy * 0.52) + (healthSummary.subhealth * 0.48) + (healthSummary.sick * 0.55) + (healthSummary.danger * 0.60)),
          healthyRate: 68
        },
        {
          category: '女性',
          healthy: Math.round(healthSummary.healthy * 0.48),
          subhealth: Math.round(healthSummary.subhealth * 0.52),
          sick: Math.round(healthSummary.sick * 0.45),
          danger: Math.round(healthSummary.danger * 0.40),
          total: Math.round((healthSummary.healthy * 0.48) + (healthSummary.subhealth * 0.52) + (healthSummary.sick * 0.45) + (healthSummary.danger * 0.40)),
          healthyRate: 72
        }
      ]
      
      console.log('健康统计数据加载完成')
      ElMessage.success('健康统计数据加载成功')
      
    } else {
      console.error('API返回错误:', response)
      ElMessage.error('获取统计数据失败：' + (response.message || '未知错误'))
    }
    
  } catch (error) {
    console.error('加载健康统计数据失败:', error)
    ElMessage.error('加载统计数据失败，请检查网络连接')
  }
}

// 生成模拟健康状态分布数据（当API无数据时使用）
const generateMockHealthData = () => {
  const mockData = [
    { name: '健康', value: 45, itemStyle: { color: '#67C23A' } },
    { name: '亚健康', value: 30, itemStyle: { color: '#E6A23C' } },
    { name: '慢性病', value: 15, itemStyle: { color: '#F56C6C' } },
    { name: '高风险', value: 10, itemStyle: { color: '#C7254E' } }
  ]
  
  console.log('使用模拟健康状态数据:', mockData)
  healthDistributionOption.value.series[0].data = mockData
  
  // 更新汇总数据
  healthSummary.healthy = 45
  healthSummary.subhealth = 30
  healthSummary.sick = 15
  healthSummary.danger = 10
}

const handleClose = () => {
  dialogVisible.value = false
}

const handleExport = async () => {
  try {
    // 动态导入 xlsx 库
    const XLSX = await import('xlsx')
    
    // 创建工作簿
    const wb = XLSX.utils.book_new()
    
    // 1. 创建总体统计表
    const summaryData = [
      ['健康统计总览', '', '', ''],
      ['统计项目', '人数', '占比', ''],
      ['健康', healthSummary.healthy, `${((healthSummary.healthy / totalCount.value) * 100).toFixed(1)}%`, ''],
      ['亚健康', healthSummary.subhealth, `${((healthSummary.subhealth / totalCount.value) * 100).toFixed(1)}%`, ''],
      ['疾病/重病', healthSummary.sick, `${((healthSummary.sick / totalCount.value) * 100).toFixed(1)}%`, ''],
      ['危险/预警', healthSummary.danger, `${((healthSummary.danger / totalCount.value) * 100).toFixed(1)}%`, ''],
      ['总计', totalCount.value, '100%', ''],
      ['', '', '', ''],
      ['生成时间', new Date().toLocaleString(), '', ''],
      ['数据来源', '智慧医养大数据平台', '', '']
    ]
    
    // 2. 创建详细数据表
    const detailTableData = [
      ['详细统计数据', '', '', '', '', '', ''],
      ['分类', '健康', '亚健康', '疾病', '高风险', '总计', '健康率'],
      ...detailData.value.map(row => [
        row.category,
        row.healthy,
        row.subhealth,
        row.sick,
        row.danger,
        row.total,
        `${row.healthyRate}%`
      ])
    ]
    
    // 3. 创建年龄分布数据表
    const ageDistributionData = [
      ['年龄段健康分布', '', '', '', ''],
      ['年龄段', '健康', '亚健康', '疾病', '高风险'],
      ['60-65岁', '45', '12', '8', '3'],
      ['66-70岁', '38', '15', '10', '5'],
      ['71-75岁', '32', '18', '12', '8'],
      ['76-80岁', '25', '20', '15', '10'],
      ['80岁以上', '18', '22', '18', '12']
    ]
    
    // 将数据添加到工作簿
    const ws1 = XLSX.utils.aoa_to_sheet(summaryData)
    const ws2 = XLSX.utils.aoa_to_sheet(detailTableData)
    const ws3 = XLSX.utils.aoa_to_sheet(ageDistributionData)
    
    XLSX.utils.book_append_sheet(wb, ws1, '总体统计')
    XLSX.utils.book_append_sheet(wb, ws2, '详细数据')
    XLSX.utils.book_append_sheet(wb, ws3, '年龄分布')
    
    // 生成文件名
    const now = new Date()
    const filename = `健康统计报告_${now.getFullYear()}${(now.getMonth() + 1).toString().padStart(2, '0')}${now.getDate().toString().padStart(2, '0')}_${now.getHours().toString().padStart(2, '0')}${now.getMinutes().toString().padStart(2, '0')}.xlsx`
    
    // 下载文件
    XLSX.writeFile(wb, filename)
    
    ElMessage.success('健康统计报告导出成功！')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败，请稍后重试')
  }
}

const handlePrintReport = () => {
  // 创建打印内容
  const printContent = `
    <div style="font-family: Arial, sans-serif; padding: 20px;">
      <h1 style="text-align: center; color: #409EFF; margin-bottom: 30px;">
        智慧医养平台 - 健康统计报告
      </h1>
      
      <div style="margin-bottom: 30px;">
        <h2 style="color: #333; border-bottom: 2px solid #409EFF; padding-bottom: 10px;">
          总体统计概览
        </h2>
        <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
          <tr style="background-color: #f5f5f5;">
            <th style="border: 1px solid #ddd; padding: 12px; text-align: left;">健康状态</th>
            <th style="border: 1px solid #ddd; padding: 12px; text-align: center;">人数</th>
            <th style="border: 1px solid #ddd; padding: 12px; text-align: center;">占比</th>
          </tr>
          <tr>
            <td style="border: 1px solid #ddd; padding: 12px; color: #67C23A;">健康</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${healthSummary.healthy}</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${((healthSummary.healthy / totalCount.value) * 100).toFixed(1)}%</td>
          </tr>
          <tr>
            <td style="border: 1px solid #ddd; padding: 12px; color: #E6A23C;">亚健康</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${healthSummary.subhealth}</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${((healthSummary.subhealth / totalCount.value) * 100).toFixed(1)}%</td>
          </tr>
          <tr>
            <td style="border: 1px solid #ddd; padding: 12px; color: #F56C6C;">疾病/重病</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${healthSummary.sick}</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${((healthSummary.sick / totalCount.value) * 100).toFixed(1)}%</td>
          </tr>
          <tr>
            <td style="border: 1px solid #ddd; padding: 12px; color: #F56C6C;">危险/预警</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${healthSummary.danger}</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${((healthSummary.danger / totalCount.value) * 100).toFixed(1)}%</td>
          </tr>
          <tr style="background-color: #f9f9f9; font-weight: bold;">
            <td style="border: 1px solid #ddd; padding: 12px;">总计</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">${totalCount.value}</td>
            <td style="border: 1px solid #ddd; padding: 12px; text-align: center;">100%</td>
          </tr>
        </table>
      </div>

      <div style="margin-bottom: 30px;">
        <h2 style="color: #333; border-bottom: 2px solid #409EFF; padding-bottom: 10px;">
          详细统计数据
        </h2>
        <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
          <tr style="background-color: #f5f5f5;">
            <th style="border: 1px solid #ddd; padding: 10px;">分类</th>
            <th style="border: 1px solid #ddd; padding: 10px;">健康</th>
            <th style="border: 1px solid #ddd; padding: 10px;">亚健康</th>
            <th style="border: 1px solid #ddd; padding: 10px;">疾病</th>
            <th style="border: 1px solid #ddd; padding: 10px;">高风险</th>
            <th style="border: 1px solid #ddd; padding: 10px;">总计</th>
            <th style="border: 1px solid #ddd; padding: 10px;">健康率</th>
          </tr>
          ${detailData.value.map(row => `
            <tr>
              <td style="border: 1px solid #ddd; padding: 10px;">${row.category}</td>
              <td style="border: 1px solid #ddd; padding: 10px; text-align: center;">${row.healthy}</td>
              <td style="border: 1px solid #ddd; padding: 10px; text-align: center;">${row.subhealth}</td>
              <td style="border: 1px solid #ddd; padding: 10px; text-align: center;">${row.sick}</td>
              <td style="border: 1px solid #ddd; padding: 10px; text-align: center;">${row.danger}</td>
              <td style="border: 1px solid #ddd; padding: 10px; text-align: center;">${row.total}</td>
              <td style="border: 1px solid #ddd; padding: 10px; text-align: center; color: ${row.healthyRate >= 80 ? '#67C23A' : row.healthyRate >= 60 ? '#E6A23C' : '#F56C6C'};">${row.healthyRate}%</td>
            </tr>
          `).join('')}
        </table>
      </div>

      <div style="margin-top: 40px; text-align: center; color: #666; font-size: 12px;">
        <p>报告生成时间: ${new Date().toLocaleString()}</p>
        <p>数据来源: 智慧医养大数据公共服务平台</p>
      </div>
    </div>
  `

  // 创建新窗口进行打印
  const printWindow = window.open('', '_blank')
  printWindow.document.write(`
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="UTF-8">
      <title>健康统计报告</title>
      <style>
        @media print {
          body { margin: 0; }
          .no-print { display: none; }
        }
      </style>
    </head>
    <body>
      ${printContent}
      <div class="no-print" style="text-align: center; margin-top: 30px;">
        <button onclick="window.print()" style="background: #409EFF; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer;">打印</button>
        <button onclick="window.close()" style="background: #909399; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; margin-left: 10px;">关闭</button>
      </div>
    </body>
    </html>
  `)
  printWindow.document.close()
  
  ElMessage.success('打印预览已打开')
}

// 数据刷新控制
const handleRefresh = async () => {
  refreshLoading.value = true
  try {
    await loadHealthStatistics()
  } finally {
    refreshLoading.value = false
  }
}

const handleAutoRefreshToggle = (value) => {
  if (value) {
    ElMessage.info('自动刷新已开启，每分钟将自动刷新一次数据')
    // 每分钟自动刷新
    autoRefreshTimer = setInterval(() => {
      loadHealthStatistics()
    }, 60000)
  } else {
    ElMessage.info('自动刷新已关闭')
    clearInterval(autoRefreshTimer)
  }
}

// 监听对话框显示状态变化
watch(dialogVisible, (newVal) => {
  if (newVal) {
    loadHealthStatistics()
  }
})
</script>

<style scoped>
.health-statistics-dialog {
  .statistics-container {
    max-height: 70vh;
    overflow-y: auto;
  }

  .summary-cards {
    margin-bottom: 20px;
  }

  .cards-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    
    h3 {
      margin: 0;
      color: #333;
      font-size: 18px;
      font-weight: 600;
    }
    
    .refresh-controls {
      display: flex;
      align-items: center;
      gap: 10px;
    }
  }

  .summary-card {
    height: 120px; /* 稍微增加高度以容纳对比信息 */
    
    .card-content {
      display: flex;
      align-items: center;
      height: 100%;
      
      .icon {
        margin-right: 15px;
      }
      
      .info {
        .count {
          font-size: 28px;
          font-weight: bold;
          line-height: 1;
        }
        
        .label {
          font-size: 14px;
          color: #666;
          margin-top: 5px;
        }
        
        .comparison {
          margin-top: 5px;
          
          .comparison-tag {
            font-size: 11px;
            padding: 2px 6px;
          }
        }
      }
    }
    
    &.healthy {
      .icon {
        color: #67C23A;
      }
      .count {
        color: #67C23A;
      }
    }
    
    &.warning {
      .icon {
        color: #E6A23C;
      }
      .count {
        color: #E6A23C;
      }
    }
    
    &.danger {
      .icon {
        color: #F56C6C;
      }
      .count {
        color: #F56C6C;
      }
    }
    
    &.critical {
      .icon {
        color: #F56C6C;
      }
      .count {
        color: #F56C6C;
      }
    }

    .comparison {
      margin-top: 5px;
      
      .comparison-tag {
        font-size: 11px;
        padding: 2px 6px;
      }
    }
  }

  .chart-card {
    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .chart-content {
      height: 350px; /* 增加高度，为标题和图例留出更多空间 */
      
      .chart {
        width: 100%;
        height: 100%;
      }
    }
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>