<template>
  <el-dialog
    v-model="visible"
    title="照护统计分析"
    width="90%"
    :before-close="handleClose"
    class="care-statistics-dialog"
  >
    <div class="statistics-container" v-loading="loading">
      <!-- 顶部汇总信息 -->
      <div class="summary-cards">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="card-content">
                <h3>{{ statistics.summary?.totalElderly || 0 }}</h3>
                <p>总老人数</p>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-icon">
                <el-icon><Star /></el-icon>
              </div>
              <div class="card-content">
                <h3>{{ statistics.summary?.avgServiceScore || 0 }}</h3>
                <p>平均服务评分</p>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-icon">
                <el-icon><Trophy /></el-icon>
              </div>
              <div class="card-content">
                <h3>{{ statistics.summary?.avgSatisfaction || 0 }}</h3>
                <p>平均满意度</p>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-icon">
                <el-icon><Operation /></el-icon>
              </div>
              <div class="card-content">
                <h3>{{ statistics.summary?.monthlyServiceCount || 0 }}</h3>
                <p>月服务次数</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 图表区域 -->
      <div class="charts-section">
        <el-row :gutter="20">
          <!-- 照护等级分布饼图 -->
          <el-col :span="12">
            <el-card class="chart-card">
              <template #header>
                <div class="card-header">
                  <span>照护等级分布</span>
                  <el-button link type="primary" size="small" @click="refreshCareLevelData">
                    <el-icon><Refresh /></el-icon>
                    刷新
                  </el-button>
                </div>
              </template>
              <div ref="careLevelChart" class="chart-container"></div>
            </el-card>
          </el-col>
          
          <!-- 年龄段照护需求柱状图 -->
          <el-col :span="12">
            <el-card class="chart-card">
              <template #header>
                <div class="card-header">
                  <span>年龄段照护需求</span>
                  <el-button link type="primary" size="small" @click="refreshAgeCareData">
                    <el-icon><Refresh /></el-icon>
                    刷新
                  </el-button>
                </div>
              </template>
              <div ref="ageCareChart" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px;">
          <!-- 服务质量趋势图 -->
          <el-col :span="12">
            <el-card class="chart-card">
              <template #header>
                <div class="card-header">
                  <span>服务质量趋势</span>
                  <el-button link type="primary" size="small" @click="refreshQualityData">
                    <el-icon><Refresh /></el-icon>
                    刷新
                  </el-button>
                </div>
              </template>
              <div ref="qualityChart" class="chart-container"></div>
            </el-card>
          </el-col>
          
          <!-- 费用趋势图 -->
          <el-col :span="12">
            <el-card class="chart-card">
              <template #header>
                <div class="card-header">
                  <span>费用趋势分析</span>
                  <el-button link type="primary" size="small" @click="refreshCostData">
                    <el-icon><Refresh /></el-icon>
                    刷新
                  </el-button>
                </div>
              </template>
              <div ref="costChart" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 详细数据表格 -->
      <div class="tables-section">
        <el-tabs v-model="activeTab" type="card">
          <!-- 护理人员工作量 -->
          <el-tab-pane label="护理人员工作量" name="workload">
            <el-table :data="statistics.caregiverWorkloads" style="width: 100%">
              <el-table-column prop="caregiverName" label="护理人员" width="120"/>
              <el-table-column prop="elderlyCount" label="负责老人数" width="120"/>
              <el-table-column prop="serviceCount" label="月服务次数" width="120"/>
              <el-table-column prop="totalServiceHours" label="总服务时长(小时)" width="150"/>
              <el-table-column prop="avgRating" label="平均评分" width="100">
                <template #default="scope">
                  <el-rate v-model="scope.row.avgRating" disabled show-score text-color="#ff9900"/>
                </template>
              </el-table-column>
              <el-table-column prop="workloadLevel" label="工作负荷" width="100">
                <template #default="scope">
                  <el-tag :type="getWorkloadTagType(scope.row.workloadLevel)">
                    {{ scope.row.workloadLevel }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          
          <!-- 服务统计详情 -->
          <el-tab-pane label="服务统计详情" name="service">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card>
                  <template #header>
                    <span>服务类型分布</span>
                  </template>
                  <div v-if="statistics.careServiceStats?.serviceTypeDistribution">
                    <div v-for="(count, type) in statistics.careServiceStats.serviceTypeDistribution" 
                         :key="type" class="service-type-item">
                      <span>{{ type }}：</span>
                      <el-tag type="info">{{ count }} 次</el-tag>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <template #header>
                    <span>服务质量指标</span>
                  </template>
                  <div class="quality-metrics">
                    <div class="metric-item">
                      <span>服务完成率：</span>
                      <el-progress :percentage="statistics.careServiceStats?.serviceCompletionRate || 0" />
                    </div>
                    <div class="metric-item">
                      <span>平均服务时长：</span>
                      <span class="metric-value">{{ statistics.careServiceStats?.avgServiceDuration || 0 }} 分钟</span>
                    </div>
                    <div class="metric-item">
                      <span>问题事件数量：</span>
                      <el-tag :type="getIncidentTagType(statistics.careQualityStats?.incidentCount)">
                        {{ statistics.careQualityStats?.incidentCount || 0 }} 起
                      </el-tag>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="exportStatistics" type="primary">
          <el-icon><Download /></el-icon>
          导出报告
        </el-button>
        <el-button @click="handleClose">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { getCareLevelStatistics } from '@/api/elderly'
import { Download, Operation, Refresh, Star, Trophy, User } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { nextTick, onMounted, ref, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(false)
const loading = ref(false)
const activeTab = ref('workload')

// 图表引用
const careLevelChart = ref(null)
const ageCareChart = ref(null)
const qualityChart = ref(null)
const costChart = ref(null)

// 图表实例
let careLevelChartInstance = null
let ageCareChartInstance = null
let qualityChartInstance = null
let costChartInstance = null

// 统计数据
const statistics = ref({
  careLevelDistribution: [],
  ageCareNeeds: [],
  careServiceStats: {},
  careQualityStats: {},
  careCostStats: {},
  caregiverWorkloads: [],
  summary: {}
})

watch(() => props.modelValue, (newVal) => {
  visible.value = newVal
  if (newVal) {
    loadStatistics()
  }
})

watch(visible, (newVal) => {
  emit('update:modelValue', newVal)
  if (!newVal) {
    destroyCharts()
  }
})

const handleClose = () => {
  visible.value = false
}

const loadStatistics = async () => {
  loading.value = true
  try {
    const response = await getCareLevelStatistics()
    if (response.code === 200) {
      statistics.value = response.data || {}
      await nextTick()
      initCharts()
    } else {
      ElMessage.error('获取统计数据失败：' + response.message)
    }
  } catch (error) {
    console.error('加载照护统计数据失败:', error)
    ElMessage.error('加载统计数据失败')
  } finally {
    loading.value = false
  }
}

const initCharts = () => {
  initCareLevelChart()
  initAgeCareChart()
  initQualityChart()
  initCostChart()
}

// 照护等级分布饼图
const initCareLevelChart = () => {
  if (!careLevelChart.value) return
  
  careLevelChartInstance = echarts.init(careLevelChart.value)
  
  const data = statistics.value.careLevelDistribution?.map(item => ({
    name: item.careLevelName,
    value: item.count
  })) || []
  
  const option = {
    title: {
      text: '照护等级分布',
      left: 'center',
      top: 20,
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'middle'
    },
    series: [
      {
        name: '照护等级',
        type: 'pie',
        radius: '60%',
        center: ['60%', '55%'],
        data: data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        label: {
          formatter: '{b}: {c}\n({d}%)'
        },
        labelLine: {
          show: true
        }
      }
    ],
    color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de']
  }
  
  careLevelChartInstance.setOption(option)
}

// 年龄段照护需求柱状图
const initAgeCareChart = () => {
  if (!ageCareChart.value) return
  
  ageCareChartInstance = echarts.init(ageCareChart.value)
  
  const ageRanges = statistics.value.ageCareNeeds?.map(item => item.ageRange) || []
  const selfCareData = statistics.value.ageCareNeeds?.map(item => item.selfCareCount) || []
  const semiCareData = statistics.value.ageCareNeeds?.map(item => item.semiCareCount) || []
  const fullCareData = statistics.value.ageCareNeeds?.map(item => item.fullCareCount) || []
  
  const option = {
    title: {
      text: '年龄段照护需求分布',
      left: 'center',
      top: 20,
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['自理', '半自理', '完全照护'],
      top: 50
    },
    xAxis: {
      type: 'category',
      data: ageRanges
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '自理',
        type: 'bar',
        data: selfCareData,
        itemStyle: {
          color: '#5470c6'
        }
      },
      {
        name: '半自理',
        type: 'bar',
        data: semiCareData,
        itemStyle: {
          color: '#91cc75'
        }
      },
      {
        name: '完全照护',
        type: 'bar',
        data: fullCareData,
        itemStyle: {
          color: '#fac858'
        }
      }
    ]
  }
  
  ageCareChartInstance.setOption(option)
}

// 服务质量趋势图
const initQualityChart = () => {
  if (!qualityChart.value) return
  
  qualityChartInstance = echarts.init(qualityChart.value)
  
  const dates = statistics.value.careQualityStats?.qualityTrends?.map(item => item.date) || []
  const qualityScores = statistics.value.careQualityStats?.qualityTrends?.map(item => item.qualityScore) || []
  const satisfactionScores = statistics.value.careQualityStats?.qualityTrends?.map(item => item.satisfactionScore) || []
  
  const option = {
    title: {
      text: '服务质量趋势',
      left: 'center',
      top: 20,
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['质量评分', '满意度评分'],
      top: 50
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 5
    },
    series: [
      {
        name: '质量评分',
        type: 'line',
        data: qualityScores,
        smooth: true,
        itemStyle: {
          color: '#5470c6'
        }
      },
      {
        name: '满意度评分',
        type: 'line',
        data: satisfactionScores,
        smooth: true,
        itemStyle: {
          color: '#91cc75'
        }
      }
    ]
  }
  
  qualityChartInstance.setOption(option)
}

// 费用趋势图
const initCostChart = () => {
  if (!costChart.value) return
  
  costChartInstance = echarts.init(costChart.value)
  
  const months = statistics.value.careCostStats?.costTrends?.map(item => item.month) || []
  const amounts = statistics.value.careCostStats?.costTrends?.map(item => item.amount) || []
  const growthRates = statistics.value.careCostStats?.costTrends?.map(item => item.growthRate) || []
  
  const option = {
    title: {
      text: '费用趋势分析',
      left: 'center',
      top: 20,
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['费用金额', '增长率'],
      top: 50
    },
    xAxis: {
      type: 'category',
      data: months
    },
    yAxis: [
      {
        type: 'value',
        name: '费用(元)',
        position: 'left'
      },
      {
        type: 'value',
        name: '增长率(%)',
        position: 'right'
      }
    ],
    series: [
      {
        name: '费用金额',
        type: 'bar',
        data: amounts,
        itemStyle: {
          color: '#5470c6'
        }
      },
      {
        name: '增长率',
        type: 'line',
        yAxisIndex: 1,
        data: growthRates,
        smooth: true,
        itemStyle: {
          color: '#ee6666'
        }
      }
    ]
  }
  
  costChartInstance.setOption(option)
}

const destroyCharts = () => {
  if (careLevelChartInstance) {
    careLevelChartInstance.dispose()
    careLevelChartInstance = null
  }
  if (ageCareChartInstance) {
    ageCareChartInstance.dispose()
    ageCareChartInstance = null
  }
  if (qualityChartInstance) {
    qualityChartInstance.dispose()
    qualityChartInstance = null
  }
  if (costChartInstance) {
    costChartInstance.dispose()
    costChartInstance = null
  }
}

// 刷新各个数据
const refreshCareLevelData = () => {
  loadStatistics()
}

const refreshAgeCareData = () => {
  loadStatistics()
}

const refreshQualityData = () => {
  loadStatistics()
}

const refreshCostData = () => {
  loadStatistics()
}

// 获取工作负荷标签类型
const getWorkloadTagType = (level) => {
  switch (level) {
    case '高负荷': return 'danger'
    case '中负荷': return 'warning'
    case '正常': return 'success'
    default: return 'info'
  }
}

// 获取事件标签类型
const getIncidentTagType = (count) => {
  if (count > 5) return 'danger'
  if (count > 2) return 'warning'
  return 'success'
}

// 导出统计报告
const exportStatistics = () => {
  ElMessage.info('导出功能开发中...')
}

onMounted(() => {
  if (props.modelValue) {
    loadStatistics()
  }
})
</script>

<style scoped>
.care-statistics-dialog {
  .statistics-container {
    min-height: 600px;
  }
  
  .summary-cards {
    margin-bottom: 20px;
    
    .summary-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;
      height: 100px;
      
      .card-icon {
        font-size: 28px;
        margin-right: 15px;
        opacity: 0.8;
      }
      
      .card-content {
        h3 {
          font-size: 24px;
          font-weight: bold;
          margin: 0 0 5px 0;
        }
        
        p {
          font-size: 14px;
          margin: 0;
          opacity: 0.9;
        }
      }
    }
  }
  
  .charts-section {
    margin-bottom: 20px;
    
    .chart-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      
      .chart-container {
        height: 300px;
        width: 100%;
      }
    }
  }
  
  .tables-section {
    .service-type-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 0;
      border-bottom: 1px solid #f0f0f0;
      
      &:last-child {
        border-bottom: none;
      }
    }
    
    .quality-metrics {
      .metric-item {
        margin-bottom: 15px;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        .metric-value {
          font-weight: bold;
          color: #409eff;
        }
      }
    }
  }
  
  .dialog-footer {
    text-align: right;
  }
}

/* 汇总卡片渐变色彩 */
.summary-cards .el-col:nth-child(1) .summary-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.summary-cards .el-col:nth-child(2) .summary-card {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.summary-cards .el-col:nth-child(3) .summary-card {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.summary-cards .el-col:nth-child(4) .summary-card {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}
</style>
