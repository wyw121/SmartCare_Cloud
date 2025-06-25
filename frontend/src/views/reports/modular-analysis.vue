<template>
  <div class="analysis-container">
    <PageHeader 
      title="大数据分析"
      subtitle="基于老人健康数据的深度分析和智能洞察"
    />

    <!-- 数据概览 -->
    <OverviewSection :overview-data="overviewData" />

    <!-- 图表分析区域 -->
    <ChartsSection 
      :health-trend-filter="healthTrendFilter"
      @filter-change="updateHealthTrend"
    />

    <!-- 数据表格 -->
    <el-card header="详细数据分析" style="margin-top: 20px;">
      <DataTableSection
        :table-data="analysisData"
        :loading="tableLoading"
        :pagination="pageParams"
        :date-range="dateRange"
        :analysis-type="analysisType"
        @date-change="handleDateChange"
        @analyze="handleAnalyze"
        @export="handleExport"
        @page-change="handlePageChange"
        @page-size-change="handlePageSizeChange"
      />
    </el-card>

    <!-- AI洞察报告 -->
    <el-card header="AI智能洞察" style="margin-top: 20px;">
      <AIInsightsSection :insights="aiInsights" />
    </el-card>
  </div>
</template>

<script setup>
import PageHeader from '@/components/common/PageHeader.vue'
import AIInsightsSection from '@/components/reports/AIInsightsSection.vue'
import ChartsSection from '@/components/reports/ChartsSection.vue'
import DataTableSection from '@/components/reports/DataTableSection.vue'
import OverviewSection from '@/components/reports/OverviewSection.vue'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

// 响应式数据
const tableLoading = ref(false)
const healthTrendFilter = ref('30d')
const analysisType = ref('health')
const dateRange = ref([])
const analysisData = ref([])

// 分页参数
const pageParams = reactive({
  current: 1,
  size: 10,
  total: 0
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

/**
 * 页面加载时初始化
 */
onMounted(() => {
  initAnalysisData()
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
    pageParams.total = mockData.length
  } catch (error) {
    ElMessage.error('获取分析数据失败')
  } finally {
    tableLoading.value = false
  }
}

/**
 * 更新健康趋势筛选
 */
const updateHealthTrend = (filter) => {
  healthTrendFilter.value = filter
  console.log('健康趋势筛选更新为:', filter)
}

/**
 * 处理日期变化
 */
const handleDateChange = (value) => {
  dateRange.value = value
  console.log('日期范围更新为:', value)
}

/**
 * 处理分析
 */
const handleAnalyze = (params) => {
  console.log('开始分析:', params)
  ElMessage.success('分析已开始，请稍候...')
  initAnalysisData()
}

/**
 * 处理导出
 */
const handleExport = (type) => {
  console.log('导出类型:', type)
  ElMessage.success(`正在导出${type.toUpperCase()}文件...`)
}

/**
 * 处理分页变化
 */
const handlePageChange = (page) => {
  pageParams.current = page
  initAnalysisData()
}

/**
 * 处理页面大小变化
 */
const handlePageSizeChange = (size) => {
  pageParams.size = size
  pageParams.current = 1
  initAnalysisData()
}
</script>

<style scoped>
.analysis-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}
</style>
