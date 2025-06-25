<template>
  <div class="ai-insights-section">
    <div class="insights-container">
      <div class="insight-item" v-for="insight in insights" :key="insight.id">
        <div class="insight-header">
          <el-icon class="insight-icon"><Lightning /></el-icon>
          <span class="insight-title">{{ insight.title }}</span>
          <el-tag 
            :type="insight.level === 'high' ? 'danger' : insight.level === 'medium' ? 'warning' : 'success'" 
            size="small"
          >
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
  </div>
</template>

<script setup>
import { Lightning } from '@element-plus/icons-vue'

const props = defineProps({
  insights: {
    type: Array,
    default: () => []
  }
})

const getLevelText = (level) => {
  const levelMap = {
    'high': '高优先级',
    'medium': '中优先级',
    'low': '低优先级'
  }
  return levelMap[level] || '未知'
}
</script>

<style scoped>
.ai-insights-section {
  margin-top: 20px;
}

.insights-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.insight-item {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 12px;
  padding: 20px;
  border-left: 4px solid #409eff;
  transition: all 0.3s ease;
}

.insight-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.insight-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.insight-icon {
  color: #409eff;
  font-size: 18px;
  margin-right: 8px;
}

.insight-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  flex: 1;
}

.insight-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 12px;
  text-align: justify;
}

.insight-recommendation {
  color: #333;
  line-height: 1.6;
  background: rgba(255, 255, 255, 0.8);
  padding: 12px;
  border-radius: 8px;
  border-left: 3px solid #67c23a;
}

.insight-recommendation strong {
  color: #67c23a;
}
</style>
