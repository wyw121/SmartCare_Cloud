<template>
  <div class="overview-section">
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in overviewItems" :key="item.key">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon" :class="item.key">
              <el-icon><component :is="item.icon" /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-title">{{ item.title }}</div>
              <div class="overview-value">{{ item.value }}</div>
              <div class="overview-trend" :class="item.trendType">
                <el-icon v-if="item.trendType === 'positive'"><ArrowUp /></el-icon>
                <el-icon v-else-if="item.trendType === 'negative'"><ArrowDown /></el-icon>
                {{ item.change }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ArrowDown, ArrowUp, PieChart, Star, TrendCharts, Warning } from '@element-plus/icons-vue'
import { computed } from 'vue'

const props = defineProps({
  overviewData: {
    type: Object,
    default: () => ({})
  }
})

const overviewItems = computed(() => [
  {
    key: 'health',
    title: '健康指数',
    value: props.overviewData.healthIndex || 0,
    icon: TrendCharts,
    trendType: 'positive',
    change: '+3.2%'
  },
  {
    key: 'warning',
    title: '预警事件',
    value: props.overviewData.warningCount || 0,
    icon: Warning,
    trendType: 'negative',
    change: '-15.8%'
  },
  {
    key: 'satisfaction',
    title: '满意度',
    value: `${props.overviewData.satisfaction || 0}%`,
    icon: Star,
    trendType: 'positive',
    change: '+2.1%'
  },
  {
    key: 'coverage',
    title: '覆盖率',
    value: `${props.overviewData.coverage || 0}%`,
    icon: PieChart,
    trendType: 'positive',
    change: '+1.5%'
  }
])
</script>

<style scoped>
.overview-section {
  margin-bottom: 30px;
}

.overview-card {
  height: 140px;
}

.overview-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.overview-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
}

.overview-icon.health {
  background-color: rgba(64, 158, 255, 0.1);
  color: #409eff;
}

.overview-icon.warning {
  background-color: rgba(245, 108, 108, 0.1);
  color: #f56c6c;
}

.overview-icon.satisfaction {
  background-color: rgba(103, 194, 58, 0.1);
  color: #67c23a;
}

.overview-icon.coverage {
  background-color: rgba(230, 162, 60, 0.1);
  color: #e6a23c;
}

.overview-info {
  flex: 1;
}

.overview-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.overview-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.overview-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
}

.overview-trend.positive {
  color: #67c23a;
}

.overview-trend.negative {
  color: #f56c6c;
}
</style>
