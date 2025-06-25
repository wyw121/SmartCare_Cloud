<!-- 仪表板图表区域 -->
<template>
  <el-row :gutter="20" class="charts-section">
    <!-- 健康状况分布 -->
    <el-col :xs="24" :md="12">
      <ChartContainer
        title="健康状况分布"
        :icon="PieChart"
        :option="healthDistributionOption"
        :loading="loading"
        height="300"
        @refresh="refreshHealthChart"
      />
    </el-col>

    <!-- 年龄分布 -->
    <el-col :xs="24" :md="12">
      <ChartContainer
        title="年龄分布"
        :icon="BarChart"
        :option="ageDistributionOption"
        :loading="loading"
        height="300"
        @refresh="refreshAgeChart"
      />
    </el-col>

    <!-- 健康趋势 -->
    <el-col :xs="24">
      <ChartContainer
        title="健康数据趋势"
        :icon="LineChart"
        :option="healthTrendOption"
        :loading="loading"
        height="400"
        @refresh="refreshTrendChart"
      />
    </el-col>
  </el-row>
</template>

<script setup>
import ChartContainer from '@/components/common/ChartContainer.vue'
import { BarChart, LineChart, PieChart } from '@element-plus/icons-vue'
import { computed } from 'vue'

// Props
const props = defineProps({
  data: { type: Object, default: () => ({}) },
  loading: { type: Boolean, default: false }
})

// Emits
const emit = defineEmits(['refresh-chart'])

// 健康状况分布图表配置
const healthDistributionOption = computed(() => ({
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
      name: '健康状况',
      type: 'pie',
      radius: '50%',
      center: ['60%', '50%'],
      data: props.data.healthDistribution || [
        { value: 452, name: '健康' },
        { value: 321, name: '亚健康' },
        { value: 234, name: '轻微异常' },
        { value: 156, name: '需要关注' },
        { value: 95, name: '重点监护' }
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
}))

// 年龄分布图表配置
const ageDistributionOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: ['60-65岁', '66-70岁', '71-75岁', '76-80岁', '81-85岁', '86岁以上']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '人数',
      type: 'bar',
      data: props.data.ageDistribution || [89, 156, 234, 321, 198, 145],
      itemStyle: {
        color: '#409EFF'
      }
    }
  ]
}))

// 健康趋势图表配置
const healthTrendOption = computed(() => ({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['血压异常', '血糖异常', '心率异常', '体温异常']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: props.data.trendDates || ['1月', '2月', '3月', '4月', '5月', '6月', '7月']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '血压异常',
      type: 'line',
      stack: 'Total',
      data: props.data.bloodPressureData || [12, 13, 10, 13, 9, 23, 21],
      smooth: true
    },
    {
      name: '血糖异常',
      type: 'line',
      stack: 'Total',
      data: props.data.bloodSugarData || [22, 18, 19, 23, 29, 33, 31],
      smooth: true
    },
    {
      name: '心率异常',
      type: 'line',
      stack: 'Total',
      data: props.data.heartRateData || [15, 23, 20, 15, 19, 33, 28],
      smooth: true
    },
    {
      name: '体温异常',
      type: 'line',
      stack: 'Total',
      data: props.data.temperatureData || [8, 12, 11, 9, 13, 18, 15],
      smooth: true
    }
  ]
}))

// 刷新图表方法
const refreshHealthChart = () => {
  emit('refresh-chart', 'health')
}

const refreshAgeChart = () => {
  emit('refresh-chart', 'age')
}

const refreshTrendChart = () => {
  emit('refresh-chart', 'trend')
}
</script>

<style scoped>
.charts-section {
  margin-bottom: 20px;
}
</style>
