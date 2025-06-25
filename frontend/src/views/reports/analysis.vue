<template>
  <div class="analysis-container">
    <div class="header-section">
      <h2>大数据分析</h2>
      <p>基于老人健康数据的深度分析和智能洞察</p>
    </div>

    <!-- 数据概览 -->
    <el-row :gutter="20" class="overview-section">
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon health">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-title">健康指数</div>
              <div class="overview-value">{{ overviewData.healthIndex }}</div>
              <div class="overview-trend positive">
                <el-icon><ArrowUp /></el-icon>
                +3.2%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon warning">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-title">预警事件</div>
              <div class="overview-value">{{ overviewData.warningCount }}</div>
              <div class="overview-trend negative">
                <el-icon><ArrowDown /></el-icon>
                -15.8%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon satisfaction">
              <el-icon><Star /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-title">满意度</div>
              <div class="overview-value">{{ overviewData.satisfaction }}%</div>
              <div class="overview-trend positive">
                <el-icon><ArrowUp /></el-icon>
                +2.1%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon coverage">
              <el-icon><PieChart /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-title">覆盖率</div>
              <div class="overview-value">{{ overviewData.coverage }}%</div>
              <div class="overview-trend positive">
                <el-icon><ArrowUp /></el-icon>
                +1.5%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表分析区域 -->
    <el-row :gutter="20" style="margin-top: 30px;">
      <!-- 健康趋势分析 -->
      <el-col :span="12">
        <el-card header="健康趋势分析" class="chart-card">
          <div class="chart-filters">
            <el-select v-model="healthTrendFilter" @change="updateHealthTrend">
              <el-option label="最近7天" value="7d" />
              <el-option label="最近30天" value="30d" />
              <el-option label="最近3个月" value="3m" />
              <el-option label="最近1年" value="1y" />
            </el-select>
          </div>
          <div ref="healthTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 疾病分布统计 -->
      <el-col :span="12">
        <el-card header="疾病分布统计" class="chart-card">
          <div ref="diseaseChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 年龄段分析 -->
      <el-col :span="8">
        <el-card header="年龄段分析" class="chart-card">
          <div ref="ageChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 地区分布 -->
      <el-col :span="8">
        <el-card header="地区分布" class="chart-card">
          <div ref="regionChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 服务质量评分 -->
      <el-col :span="8">
        <el-card header="服务质量评分" class="chart-card">
          <div ref="qualityChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card header="详细数据分析" style="margin-top: 20px;">
      <div class="table-toolbar">
        <div class="filter-section">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleDateChange"
          />
          <el-select v-model="analysisType" placeholder="分析类型" style="margin-left: 10px;">
            <el-option label="健康指标" value="health" />
            <el-option label="预警分析" value="warning" />
            <el-option label="满意度" value="satisfaction" />
            <el-option label="服务质量" value="quality" />
          </el-select>
          <el-button type="primary" @click="handleAnalyze" style="margin-left: 10px;">
            开始分析
          </el-button>
        </div>
        
        <div class="export-section">
          <el-button @click="handleExport('excel')">
            <el-icon><Document /></el-icon>
            导出Excel
          </el-button>
          <el-button @click="handleExport('pdf')">
            <el-icon><Printer /></el-icon>
            导出PDF
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="tableLoading"
        :data="analysisData"
        stripe
        border
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="indicator" label="指标名称" width="150" />
        <el-table-column prop="value" label="数值" width="100" align="center" />
        <el-table-column prop="unit" label="单位" width="80" align="center" />
        <el-table-column prop="trend" label="趋势" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getTrendType(row.trend)">
              {{ getTrendText(row.trend) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comparison" label="同比" width="100" align="center">
          <template #default="{ row }">
            <span :class="getComparisonClass(row.comparison)">
              {{ row.comparison }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" min-width="200" />
        <el-table-column prop="updateTime" label="更新时间" width="160" />
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageParams.current"
          v-model:page-size="pageParams.size"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlePageSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- AI洞察报告 -->
    <el-card header="AI智能洞察" style="margin-top: 20px;">
      <div class="ai-insights">
        <div class="insight-item" v-for="insight in aiInsights" :key="insight.id">
          <div class="insight-header">
            <el-icon class="insight-icon"><Lightning /></el-icon>
            <span class="insight-title">{{ insight.title }}</span>
            <el-tag :type="insight.level === 'high' ? 'danger' : insight.level === 'medium' ? 'warning' : 'success'" size="small">
              {{ getLevelText(insight.level) }}
            </el-tag>
          </div>
          <div class="insight-content">
            {{ insight.content }}
          </div>
          <div class="insight-recommendation">
            <strong>建议：</strong>{{ insight.recommendation }}
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {
    ArrowDown,
    ArrowUp,
    Document,
    Lightning,
    PieChart,
    Printer,
    Star,
    TrendCharts,
    Warning
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { nextTick, onMounted, reactive, ref } from 'vue'

// 响应式数据
const tableLoading = ref(false)
const healthTrendFilter = ref('30d')
const analysisType = ref('health')
const dateRange = ref([])
const analysisData = ref([])
const total = ref(0)

// 分页参数
const pageParams = reactive({
  current: 1,
  size: 10
})

// 概览数据
const overviewData = ref({
  healthIndex: 85.6,
  warningCount: 23,
  satisfaction: 94.2,
  coverage: 98.7
})

// AI洞察
const aiInsights = ref([
  {
    id: 1,
    title: '老年糖尿病患者增长趋势明显',
    level: 'high',
    content: '根据最近3个月的数据分析，老年糖尿病患者数量增长了18.5%，主要集中在65-75岁年龄段。',
    recommendation: '建议加强糖尿病预防宣传，增加定期血糖检测服务，制定专门的糖尿病管理方案。'
  },
  {
    id: 2,
    title: '心血管疾病预警准确率提升',
    level: 'medium',
    content: '心血管疾病预警系统准确率达到92.3%，比上月提升了5.8个百分点。',
    recommendation: '继续优化预警算法，扩大心血管疾病风险因子监测范围，提高早期干预效果。'
  },
  {
    id: 3,
    title: '居家养老服务满意度持续提升',
    level: 'low',
    content: '居家养老服务整体满意度达到94.2%，其中护理服务和健康咨询服务评分最高。',
    recommendation: '保持现有服务质量，可考虑扩大服务覆盖范围，增加更多个性化服务项目。'
  }
])

// 图表引用
const healthTrendChart = ref()
const diseaseChart = ref()
const ageChart = ref()
const regionChart = ref()
const qualityChart = ref()

/**
 * 页面加载时初始化
 */
onMounted(() => {
  initAnalysisData()
  nextTick(() => {
    initCharts()
  })
})

/**
 * 初始化分析数据
 */
const initAnalysisData = async () => {
  tableLoading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 模拟数据
    const mockData = [
      {
        indicator: '平均血压',
        value: 125.8,
        unit: 'mmHg',
        trend: 'up',
        comparison: '+2.3%',
        description: '老人平均收缩压水平，需要关注高血压风险',
        updateTime: '2024-01-15 10:30:00'
      },
      {
        indicator: '血糖达标率',
        value: 78.5,
        unit: '%',
        trend: 'down',
        comparison: '-3.2%',
        description: '糖尿病患者血糖控制达标比例',
        updateTime: '2024-01-15 10:25:00'
      },
      {
        indicator: '运动频次',
        value: 4.2,
        unit: '次/周',
        trend: 'up',
        comparison: '+12.1%',
        description: '老人平均每周运动次数',
        updateTime: '2024-01-15 10:20:00'
      },
      {
        indicator: '服药依从性',
        value: 89.6,
        unit: '%',
        trend: 'stable',
        comparison: '+0.8%',
        description: '老人按时服药的比例',
        updateTime: '2024-01-15 10:15:00'
      }
    ]
    
    analysisData.value = mockData
    total.value = mockData.length
  } catch (error) {
    ElMessage.error('获取分析数据失败')
  } finally {
    tableLoading.value = false
  }
}

/**
 * 初始化图表
 */
const initCharts = () => {
  initHealthTrendChart()
  initDiseaseChart()
  initAgeChart()
  initRegionChart()
  initQualityChart()
}

/**
 * 初始化健康趋势图表
 */
const initHealthTrendChart = () => {
  const chart = echarts.init(healthTrendChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['血压正常率', '血糖正常率', '心率正常率']
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: [
      {
        name: '血压正常率',
        type: 'line',
        data: [82, 85, 88, 87, 89, 91, 93, 92, 94, 95, 96, 94],
        smooth: true,
        itemStyle: { color: '#409EFF' }
      },
      {
        name: '血糖正常率',
        type: 'line',
        data: [75, 78, 80, 79, 82, 84, 86, 85, 87, 89, 88, 90],
        smooth: true,
        itemStyle: { color: '#67C23A' }
      },
      {
        name: '心率正常率',
        type: 'line',
        data: [88, 90, 92, 91, 93, 95, 94, 96, 95, 97, 98, 96],
        smooth: true,
        itemStyle: { color: '#E6A23C' }
      }
    ]
  }
  
  chart.setOption(option)
}

/**
 * 初始化疾病分布图表
 */
const initDiseaseChart = () => {
  const chart = echarts.init(diseaseChart.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
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
          { value: 452, name: '高血压', itemStyle: { color: '#FF6B6B' } },
          { value: 298, name: '糖尿病', itemStyle: { color: '#4ECDC4' } },
          { value: 186, name: '心脏病', itemStyle: { color: '#45B7D1' } },
          { value: 134, name: '关节炎', itemStyle: { color: '#96CEB4' } },
          { value: 89, name: '其他', itemStyle: { color: '#FECA57' } }
        ]
      }
    ]
  }
  
  chart.setOption(option)
}

/**
 * 初始化年龄段图表
 */
const initAgeChart = () => {
  const chart = echarts.init(ageChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: ['60-65岁', '65-70岁', '70-75岁', '75-80岁', '80-85岁', '85岁以上']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        type: 'bar',
        data: [
          { value: 320, itemStyle: { color: '#409EFF' } },
          { value: 456, itemStyle: { color: '#67C23A' } },
          { value: 398, itemStyle: { color: '#E6A23C' } },
          { value: 298, itemStyle: { color: '#F56C6C' } },
          { value: 186, itemStyle: { color: '#909399' } },
          { value: 89, itemStyle: { color: '#C0C4CC' } }
        ]
      }
    ]
  }
  
  chart.setOption(option)
}

/**
 * 初始化地区分布图表
 */
const initRegionChart = () => {
  const chart = echarts.init(regionChart.value)
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        type: 'pie',
        radius: '70%',
        data: [
          { value: 156, name: '市中心区', itemStyle: { color: '#FF6B6B' } },
          { value: 234, name: '南山区', itemStyle: { color: '#4ECDC4' } },
          { value: 189, name: '福田区', itemStyle: { color: '#45B7D1' } },
          { value: 142, name: '罗湖区', itemStyle: { color: '#96CEB4' } },
          { value: 98, name: '盐田区', itemStyle: { color: '#FECA57' } }
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
  }
  
  chart.setOption(option)
}

/**
 * 初始化服务质量图表
 */
const initQualityChart = () => {
  const chart = echarts.init(qualityChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    radar: {
      indicator: [
        { name: '服务态度', max: 100 },
        { name: '专业程度', max: 100 },
        { name: '响应速度', max: 100 },
        { name: '服务质量', max: 100 },
        { name: '设备先进性', max: 100 },
        { name: '环境舒适度', max: 100 }
      ]
    },
    series: [
      {
        type: 'radar',
        data: [
          {
            value: [95, 92, 88, 94, 89, 91],
            name: '整体评分',
            itemStyle: { color: '#409EFF' }
          }
        ]
      }
    ]
  }
  
  chart.setOption(option)
}

/**
 * 更新健康趋势图表
 */
const updateHealthTrend = () => {
  // 根据选择的时间范围更新图表数据
  console.log('更新健康趋势:', healthTrendFilter.value)
  // 这里可以重新获取数据并更新图表
}

/**
 * 日期范围变化
 */
const handleDateChange = (dates) => {
  console.log('日期范围变化:', dates)
}

/**
 * 开始分析
 */
const handleAnalyze = () => {
  ElMessage.info('正在分析数据...')
  initAnalysisData()
}

/**
 * 导出数据
 */
const handleExport = (type) => {
  ElMessage.success(`正在导出${type.toUpperCase()}文件...`)
}

/**
 * 页码变化
 */
const handlePageChange = (page) => {
  pageParams.current = page
  initAnalysisData()
}

/**
 * 页大小变化
 */
const handlePageSizeChange = (size) => {
  pageParams.size = size
  pageParams.current = 1
  initAnalysisData()
}

/**
 * 获取趋势类型
 */
const getTrendType = (trend) => {
  const trendMap = {
    up: 'danger',
    down: 'success',
    stable: 'info'
  }
  return trendMap[trend] || 'info'
}

/**
 * 获取趋势文本
 */
const getTrendText = (trend) => {
  const trendMap = {
    up: '上升',
    down: '下降',
    stable: '稳定'
  }
  return trendMap[trend] || '未知'
}

/**
 * 获取对比样式类
 */
const getComparisonClass = (comparison) => {
  if (comparison.startsWith('+')) {
    return 'comparison-positive'
  } else if (comparison.startsWith('-')) {
    return 'comparison-negative'
  }
  return 'comparison-neutral'
}

/**
 * 获取洞察级别文本
 */
const getLevelText = (level) => {
  const levelMap = {
    high: '高优先级',
    medium: '中优先级',
    low: '低优先级'
  }
  return levelMap[level] || '未知'
}
</script>

<style lang="scss" scoped>
.analysis-container {
  padding: 20px;
}

.header-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  
  h2 {
    color: #303133;
    font-size: 24px;
    font-weight: 600;
    margin: 0 0 8px 0;
  }
  
  p {
    color: #606266;
    font-size: 14px;
    margin: 0;
  }
}

.overview-section {
  margin-bottom: 30px;
}

.overview-card {
  height: 120px;
  
  :deep(.el-card__body) {
    padding: 20px;
    height: 100%;
  }
}

.overview-content {
  display: flex;
  align-items: center;
  gap: 15px;
  height: 100%;
}

.overview-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  
  &.health {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.warning {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.satisfaction {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }
  
  &.coverage {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  }
}

.overview-info {
  flex: 1;
}

.overview-title {
  color: #909399;
  font-size: 14px;
  margin-bottom: 5px;
}

.overview-value {
  color: #303133;
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 5px;
}

.overview-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  font-weight: 500;
  
  &.positive {
    color: #67c23a;
  }
  
  &.negative {
    color: #f56c6c;
  }
}

.chart-card {
  height: 400px;
  
  :deep(.el-card__body) {
    height: calc(100% - 60px);
    position: relative;
  }
}

.chart-filters {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 10;
}

.chart-container {
  width: 100%;
  height: 100%;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.filter-section {
  display: flex;
  align-items: center;
  gap: 10px;
}

.export-section {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.comparison-positive {
  color: #67c23a;
  font-weight: 600;
}

.comparison-negative {
  color: #f56c6c;
  font-weight: 600;
}

.comparison-neutral {
  color: #909399;
}

.ai-insights {
  .insight-item {
    margin-bottom: 20px;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 8px;
    border-left: 4px solid #409eff;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .insight-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
    
    .insight-icon {
      color: #e6a23c;
      font-size: 18px;
    }
    
    .insight-title {
      flex: 1;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }
  
  .insight-content {
    color: #606266;
    font-size: 14px;
    line-height: 1.6;
    margin-bottom: 10px;
  }
  
  .insight-recommendation {
    color: #409eff;
    font-size: 14px;
    line-height: 1.6;
  }
}

:deep(.el-card__header) {
  background-color: #fafafa;
  border-bottom: 1px solid #ebeef5;
  font-weight: 600;
}

:deep(.el-table) {
  .el-table__header {
    th {
      background-color: #fafafa;
      color: #606266;
      font-weight: 600;
    }
  }
}
</style>
