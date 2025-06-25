<!-- 统计卡片组件 -->
<template>
  <el-card class="stats-card" :shadow="shadow" @click="handleClick">
    <div class="stats-content">
      <div class="stats-icon" :style="iconStyle">
        <el-icon :size="iconSize">
          <component :is="icon" />
        </el-icon>
      </div>
      <div class="stats-info">
        <div class="stats-value" :style="valueStyle">
          {{ formattedValue }}
        </div>
        <div class="stats-title" :style="titleStyle">
          {{ title }}
        </div>
        <div v-if="subtitle" class="stats-subtitle">
          {{ subtitle }}
        </div>
      </div>
    </div>
    
    <div v-if="showFooter" class="stats-footer">
      <span class="stats-trend" :class="trendClass">
        <el-icon v-if="showTrendIcon">
          <ArrowUp v-if="trend > 0" />
          <ArrowDown v-if="trend < 0" />
          <Minus v-if="trend === 0" />
        </el-icon>
        {{ trendText }}
      </span>
      <span class="stats-label">{{ footerText }}</span>
    </div>
  </el-card>
</template>

<script setup>
import { ArrowDown, ArrowUp, Minus } from '@element-plus/icons-vue'
import { computed } from 'vue'

// Props
const props = defineProps({
  // 基本信息
  title: { type: String, required: true },
  value: { type: [String, Number], required: true },
  subtitle: { type: String, default: '' },
  
  // 图标配置
  icon: { type: [String, Object], required: true },
  iconSize: { type: Number, default: 24 },
  iconColor: { type: String, default: '#409EFF' },
  iconBackground: { type: String, default: 'rgba(64, 158, 255, 0.2)' },
  
  // 样式配置
  shadow: { type: String, default: 'hover' },
  valueColor: { type: String, default: '#303133' },
  titleColor: { type: String, default: '#606266' },
  
  // 趋势配置
  showFooter: { type: Boolean, default: true },
  trend: { type: Number, default: 0 },
  trendText: { type: String, default: '' },
  footerText: { type: String, default: '较昨日' },
  showTrendIcon: { type: Boolean, default: true },
  
  // 数值格式化
  formatter: { type: Function, default: null },
  precision: { type: Number, default: 0 },
  unit: { type: String, default: '' },
  
  // 交互
  clickable: { type: Boolean, default: false }
})

// Emits
const emit = defineEmits(['click'])

// 计算属性
const iconStyle = computed(() => ({
  backgroundColor: props.iconBackground,
  color: props.iconColor
}))

const valueStyle = computed(() => ({
  color: props.valueColor
}))

const titleStyle = computed(() => ({
  color: props.titleColor
}))

const formattedValue = computed(() => {
  if (props.formatter && typeof props.formatter === 'function') {
    return props.formatter(props.value)
  }
  
  const num = Number(props.value)
  if (isNaN(num)) {
    return props.value
  }
  
  let formatted = num.toFixed(props.precision)
  
  // 添加千分位分隔符
  if (num >= 1000) {
    formatted = formatted.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  }
  
  return formatted + props.unit
})

const trendClass = computed(() => {
  if (props.trend > 0) return 'up'
  if (props.trend < 0) return 'down'
  return 'neutral'
})

// 方法
const handleClick = () => {
  if (props.clickable) {
    emit('click')
  }
}
</script>

<style scoped>
.stats-card {
  transition: all 0.3s ease;
  cursor: v-bind('props.clickable ? "pointer" : "default"');
}

.stats-card:hover {
  transform: v-bind('props.clickable ? "translateY(-2px)" : "none"');
}

.stats-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stats-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stats-info {
  flex: 1;
  min-width: 0;
}

.stats-value {
  font-size: 24px;
  font-weight: 600;
  line-height: 1.2;
  margin-bottom: 4px;
}

.stats-title {
  font-size: 14px;
  line-height: 1.4;
  margin-bottom: 2px;
}

.stats-subtitle {
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}

.stats-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.stats-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  font-weight: 500;
}

.stats-trend.up {
  color: #67c23a;
}

.stats-trend.down {
  color: #f56c6c;
}

.stats-trend.neutral {
  color: #909399;
}

.stats-label {
  font-size: 12px;
  color: #909399;
}
</style>
