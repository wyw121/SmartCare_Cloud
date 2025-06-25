<!-- 仪表板统计卡片区域 -->
<template>
  <el-row :gutter="20" class="stats-section">
    <el-col :xs="24" :sm="12" :md="6" v-for="stat in statsData" :key="stat.key">
      <StatsCard
        :title="stat.title"
        :value="stat.value"
        :icon="stat.icon"
        :icon-color="stat.iconColor"
        :icon-background="stat.iconBackground"
        :trend="stat.trend"
        :trend-text="stat.trendText"
        :footer-text="stat.footerText"
        :clickable="true"
        @click="handleStatClick(stat)"
      />
    </el-col>
  </el-row>
</template>

<script setup>
import StatsCard from '@/components/common/StatsCard.vue'
import { Calendar, Monitor, User, Warning } from '@element-plus/icons-vue'
import { computed } from 'vue'

// Props
const props = defineProps({
  data: { type: Object, default: () => ({}) }
})

// Emits
const emit = defineEmits(['stat-click'])

// 统计数据配置
const statsData = computed(() => [
  {
    key: 'elderly',
    title: '老人总数',
    value: props.data.elderlyCount || 1258,
    icon: User,
    iconColor: '#409EFF',
    iconBackground: 'rgba(64, 158, 255, 0.2)',
    trend: props.data.elderlyTrend || 12,
    trendText: props.data.elderlyTrendText || '+12',
    footerText: '较昨日'
  },
  {
    key: 'warning',
    title: '健康预警',
    value: props.data.warningCount || 23,
    icon: Warning,
    iconColor: '#F56C6C',
    iconBackground: 'rgba(245, 108, 108, 0.2)',
    trend: props.data.warningTrend || -5,
    trendText: props.data.warningTrendText || '-5',
    footerText: '较昨日'
  },
  {
    key: 'equipment',
    title: '设备在线',
    value: props.data.equipmentOnline || 156,
    icon: Monitor,
    iconColor: '#67C23A',
    iconBackground: 'rgba(103, 194, 58, 0.2)',
    trend: props.data.equipmentTrend || 3,
    trendText: props.data.equipmentTrendText || '+3',
    footerText: '较昨日'
  },
  {
    key: 'checkup',
    title: '今日体检',
    value: props.data.checkupCount || 89,
    icon: Calendar,
    iconColor: '#E6A23C',
    iconBackground: 'rgba(230, 162, 60, 0.2)',
    trend: props.data.checkupTrend || 15,
    trendText: props.data.checkupTrendText || '+15',
    footerText: '较昨日'
  }
])

// 处理统计卡片点击
const handleStatClick = (stat) => {
  emit('stat-click', stat)
}
</script>

<style scoped>
.stats-section {
  margin-bottom: 20px;
}
</style>
