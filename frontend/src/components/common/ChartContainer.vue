<!-- 图表容器组件 -->
<template>
  <el-card class="chart-container" :shadow="shadow">
    <template #header v-if="showHeader">
      <div class="chart-header">
        <div class="chart-title">
          <el-icon v-if="icon" :size="18">
            <component :is="icon" />
          </el-icon>
          {{ title }}
        </div>
        <div class="chart-tools">
          <slot name="tools">
            <el-button 
              v-if="showRefresh" 
              type="text" 
              size="small" 
              @click="handleRefresh"
            >
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button 
              v-if="showFullscreen" 
              type="text" 
              size="small" 
              @click="handleFullscreen"
            >
              <el-icon><FullScreen /></el-icon>
            </el-button>
          </slot>
        </div>
      </div>
    </template>
    
    <div class="chart-content" v-loading="loading">
      <div 
        :id="chartId" 
        :ref="chartRef"
        class="chart-wrapper"
        :style="chartStyle"
      ></div>
      
      <!-- 空状态 -->
      <div v-if="!loading && isEmpty" class="chart-empty">
        <el-empty :description="emptyText" :image-size="80" />
      </div>
      
      <!-- 错误状态 -->
      <div v-if="hasError" class="chart-error">
        <el-alert 
          :title="errorText" 
          type="error" 
          :closable="false"
          show-icon
        />
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { FullScreen, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { computed, nextTick, onMounted, onUnmounted, ref, watch } from 'vue'

// Props
const props = defineProps({
  // 基本配置
  title: { type: String, default: '' },
  icon: { type: [String, Object], default: null },
  shadow: { type: String, default: 'hover' },
  
  // 图表配置
  option: { type: Object, required: true },
  theme: { type: String, default: '' },
  height: { type: [String, Number], default: 300 },
  width: { type: [String, Number], default: '100%' },
  
  // 功能开关
  showHeader: { type: Boolean, default: true },
  showRefresh: { type: Boolean, default: true },
  showFullscreen: { type: Boolean, default: true },
  autoResize: { type: Boolean, default: true },
  
  // 状态
  loading: { type: Boolean, default: false },
  isEmpty: { type: Boolean, default: false },
  hasError: { type: Boolean, default: false },
  emptyText: { type: String, default: '暂无数据' },
  errorText: { type: String, default: '图表加载失败' }
})

// Emits
const emit = defineEmits(['refresh', 'fullscreen', 'ready', 'click'])

// 响应式数据
const chartRef = ref()
const chartInstance = ref(null)
const resizeObserver = ref(null)

// 生成唯一ID
const chartId = computed(() => `chart-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`)

// 图表样式
const chartStyle = computed(() => ({
  width: typeof props.width === 'number' ? `${props.width}px` : props.width,
  height: typeof props.height === 'number' ? `${props.height}px` : props.height
}))

// 初始化图表
const initChart = async () => {
  await nextTick()
  
  const chartDom = document.getElementById(chartId.value)
  if (!chartDom) return
  
  try {
    // 销毁已存在的实例
    if (chartInstance.value) {
      chartInstance.value.dispose()
    }
    
    // 创建新实例
    chartInstance.value = echarts.init(chartDom, props.theme)
    
    // 设置配置项
    chartInstance.value.setOption(props.option, true)
    
    // 绑定点击事件
    chartInstance.value.on('click', (params) => {
      emit('click', params)
    })
    
    // 设置自动调整大小
    if (props.autoResize) {
      setupResizeObserver()
    }
    
    emit('ready', chartInstance.value)
  } catch (error) {
    console.error('Chart initialization failed:', error)
    emit('error', error)
  }
}

// 更新图表
const updateChart = () => {
  if (chartInstance.value && props.option) {
    chartInstance.value.setOption(props.option, true)
  }
}

// 调整图表大小
const resizeChart = () => {
  if (chartInstance.value) {
    chartInstance.value.resize()
  }
}

// 设置大小监听器
const setupResizeObserver = () => {
  if (!window.ResizeObserver) return
  
  const chartDom = document.getElementById(chartId.value)
  if (!chartDom) return
  
  resizeObserver.value = new ResizeObserver(() => {
    resizeChart()
  })
  
  resizeObserver.value.observe(chartDom)
}

// 销毁图表
const destroyChart = () => {
  if (chartInstance.value) {
    chartInstance.value.dispose()
    chartInstance.value = null
  }
  
  if (resizeObserver.value) {
    resizeObserver.value.disconnect()
    resizeObserver.value = null
  }
}

// 事件处理
const handleRefresh = () => {
  emit('refresh')
}

const handleFullscreen = () => {
  emit('fullscreen')
}

// 监听器
watch(() => props.option, updateChart, { deep: true })

// 生命周期
onMounted(() => {
  initChart()
})

onUnmounted(() => {
  destroyChart()
})

// 暴露方法
defineExpose({
  chartInstance: computed(() => chartInstance.value),
  resize: resizeChart,
  refresh: updateChart,
  destroy: destroyChart
})
</script>

<style scoped>
.chart-container {
  height: 100%;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  font-size: 16px;
}

.chart-tools {
  display: flex;
  gap: 4px;
}

.chart-content {
  position: relative;
  width: 100%;
}

.chart-wrapper {
  width: 100%;
  min-height: 200px;
}

.chart-empty,
.chart-error {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

.chart-empty {
  color: #909399;
}
</style>
