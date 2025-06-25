<template>
  <div class="warning-statistics">
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6" v-for="item in statisticsItems" :key="item.key">
        <el-card class="statistic-card" :class="item.key">
          <div class="statistic-content">
            <div class="statistic-number">{{ item.value }}</div>
            <div class="statistic-label">{{ item.label }}</div>
          </div>
          <el-icon class="statistic-icon">
            <component :is="item.icon" />
          </el-icon>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { CircleCheck, InfoFilled, Warning } from '@element-plus/icons-vue'
import { computed } from 'vue'

const props = defineProps({
  statistics: {
    type: Object,
    default: () => ({
      urgent: 0,
      high: 0,
      medium: 0,
      low: 0
    })
  }
})

const statisticsItems = computed(() => [
  {
    key: 'urgent',
    label: '紧急预警',
    value: props.statistics.urgent,
    icon: Warning
  },
  {
    key: 'high',
    label: '高风险',
    value: props.statistics.high,
    icon: Warning
  },
  {
    key: 'medium',
    label: '中风险',
    value: props.statistics.medium,
    icon: InfoFilled
  },
  {
    key: 'low',
    label: '低风险',
    value: props.statistics.low,
    icon: CircleCheck
  }
])
</script>

<style scoped>
.warning-statistics {
  margin-bottom: 20px;
}

.statistics-row {
  margin-bottom: 20px;
}

.statistic-card {
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.statistic-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.statistic-content {
  text-align: center;
  padding: 20px 0;
}

.statistic-number {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 8px;
}

.statistic-label {
  font-size: 14px;
  color: #666;
}

.statistic-icon {
  position: absolute;
  top: 15px;
  right: 15px;
  font-size: 24px;
  opacity: 0.3;
}

/* 不同级别的预警卡片颜色 */
.statistic-card.urgent .statistic-number {
  color: #ff4757;
}

.statistic-card.urgent .statistic-icon {
  color: #ff4757;
}

.statistic-card.high .statistic-number {
  color: #ff6b6b;
}

.statistic-card.high .statistic-icon {
  color: #ff6b6b;
}

.statistic-card.medium .statistic-number {
  color: #ffa502;
}

.statistic-card.medium .statistic-icon {
  color: #ffa502;
}

.statistic-card.low .statistic-number {
  color: #2ed573;
}

.statistic-card.low .statistic-icon {
  color: #2ed573;
}
</style>
