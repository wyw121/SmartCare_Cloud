<template>
  <div class="charts-section">
    <el-row :gutter="20">
      <!-- 健康趋势分析 -->
      <el-col :span="12">
        <el-card header="健康趋势分析" class="chart-card">
          <div class="chart-filters">
            <el-select v-model="trendFilter" @change="handleFilterChange">
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
  </div>
</template>

<script setup>
import * as echarts from 'echarts'
import { nextTick, onMounted, ref, watch } from 'vue'

const props = defineProps({
  healthTrendFilter: {
    type: String,
    default: '30d'
  }
})

const emit = defineEmits(['filter-change'])

const trendFilter = ref(props.healthTrendFilter)

// 图表引用
const healthTrendChart = ref()
const diseaseChart = ref()
const ageChart = ref()
const regionChart = ref()
const qualityChart = ref()

// 图表实例
let healthChart = null
let diseaseChartInstance = null
let ageChartInstance = null
let regionChartInstance = null
let qualityChartInstance = null

watch(() => props.healthTrendFilter, (newVal) => {
  trendFilter.value = newVal
  if (healthChart) {
    updateHealthTrendChart()
  }
})

onMounted(() => {
  nextTick(() => {
    initCharts()
  })
})

const handleFilterChange = (value) => {
  emit('filter-change', value)
  updateHealthTrendChart()
}

const initCharts = () => {
  initHealthTrendChart()
  initDiseaseChart()
  initAgeChart()
  initRegionChart()
  initQualityChart()
}

const initHealthTrendChart = () => {
  healthChart = echarts.init(healthTrendChart.value)
  
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
        data: [85, 87, 86, 89, 88, 90, 92, 91, 93, 94, 95, 93],
        smooth: true,
        itemStyle: { color: '#409EFF' }
      },
      {
        name: '血糖正常率',
        type: 'line',
        data: [78, 79, 77, 81, 80, 82, 84, 83, 85, 86, 87, 85],
        smooth: true,
        itemStyle: { color: '#67C23A' }
      },
      {
        name: '心率正常率',
        type: 'line',
        data: [92, 91, 93, 94, 95, 94, 96, 95, 97, 96, 98, 97],
        smooth: true,
        itemStyle: { color: '#E6A23C' }
      }
    ]
  }
  
  healthChart.setOption(option)
}

const updateHealthTrendChart = () => {
  if (!healthChart) return
  
  // 根据筛选条件更新数据
  const data = getHealthTrendData(trendFilter.value)
  healthChart.setOption({
    xAxis: {
      data: data.categories
    },
    series: data.series
  })
}

const getHealthTrendData = (filter) => {
  // 根据不同时间范围返回不同数据
  const dataMap = {
    '7d': {
      categories: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      series: [
        { data: [85, 87, 86, 89, 88, 90, 92] },
        { data: [78, 79, 77, 81, 80, 82, 84] },
        { data: [92, 91, 93, 94, 95, 94, 96] }
      ]
    },
    '30d': {
      categories: Array.from({length: 30}, (_, i) => `${i + 1}日`),
      series: [
        { data: Array.from({length: 30}, () => Math.floor(Math.random() * 20) + 80) },
        { data: Array.from({length: 30}, () => Math.floor(Math.random() * 15) + 75) },
        { data: Array.from({length: 30}, () => Math.floor(Math.random() * 10) + 90) }
      ]
    },
    '3m': {
      categories: ['1月', '2月', '3月'],
      series: [
        { data: [85, 87, 90] },
        { data: [78, 80, 82] },
        { data: [92, 94, 96] }
      ]
    },
    '1y': {
      categories: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      series: [
        { data: [85, 87, 86, 89, 88, 90, 92, 91, 93, 94, 95, 93] },
        { data: [78, 79, 77, 81, 80, 82, 84, 83, 85, 86, 87, 85] },
        { data: [92, 91, 93, 94, 95, 94, 96, 95, 97, 96, 98, 97] }
      ]
    }
  }
  
  return dataMap[filter] || dataMap['30d']
}

const initDiseaseChart = () => {
  diseaseChartInstance = echarts.init(diseaseChart.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '疾病分布',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 35, name: '高血压' },
          { value: 25, name: '糖尿病' },
          { value: 20, name: '心脏病' },
          { value: 15, name: '骨质疏松' },
          { value: 5, name: '其他' }
        ]
      }
    ]
  }
  
  diseaseChartInstance.setOption(option)
}

const initAgeChart = () => {
  ageChartInstance = echarts.init(ageChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['60-65', '65-70', '70-75', '75-80', '80-85', '85+']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [120, 200, 150, 80, 70, 30],
        type: 'bar',
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  }
  
  ageChartInstance.setOption(option)
}

const initRegionChart = () => {
  regionChartInstance = echarts.init(regionChart.value)
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '地区分布',
        type: 'pie',
        radius: ['40%', '70%'],
        data: [
          { value: 40, name: '城东区' },
          { value: 30, name: '城西区' },
          { value: 20, name: '城南区' },
          { value: 10, name: '城北区' }
        ]
      }
    ]
  }
  
  regionChartInstance.setOption(option)
}

const initQualityChart = () => {
  qualityChartInstance = echarts.init(qualityChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    radar: {
      indicator: [
        { name: '护理服务', max: 100 },
        { name: '医疗咨询', max: 100 },
        { name: '健康监测', max: 100 },
        { name: '紧急响应', max: 100 },
        { name: '心理关怀', max: 100 }
      ]
    },
    series: [
      {
        name: '服务质量',
        type: 'radar',
        data: [
          {
            value: [85, 90, 88, 92, 80],
            name: '本月'
          }
        ]
      }
    ]
  }
  
  qualityChartInstance.setOption(option)
}
</script>

<style scoped>
.charts-section {
  margin-bottom: 30px;
}

.chart-card {
  height: 400px;
}

.chart-filters {
  margin-bottom: 15px;
  text-align: right;
}

.chart-container {
  height: 300px;
}
</style>
