<!-- 仪表板快捷操作区域 -->
<template>
  <el-row :gutter="20" class="quick-actions-section">
    <el-col :xs="24" :md="16">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <span>快捷操作</span>
          </div>
        </template>
        <div class="quick-actions">
          <el-button
            v-for="action in quickActions"
            :key="action.key"
            :type="action.type"
            :icon="action.icon"
            size="large"
            @click="handleQuickAction(action)"
          >
            {{ action.label }}
          </el-button>
        </div>
      </el-card>
    </el-col>

    <el-col :xs="24" :md="8">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <span>系统状态</span>
          </div>
        </template>
        <div class="system-status">
          <div 
            v-for="status in systemStatus" 
            :key="status.key"
            class="status-item"
          >
            <div class="status-indicator" :class="status.status">
              <div class="status-dot"></div>
            </div>
            <div class="status-info">
              <div class="status-label">{{ status.label }}</div>
              <div class="status-value">{{ status.value }}</div>
            </div>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import {
    DataAnalysis,
    Document,
    Monitor,
    Plus,
    Setting,
    Warning
} from '@element-plus/icons-vue'
import { computed, ref } from 'vue'

// Props
const props = defineProps({
  data: { type: Object, default: () => ({}) }
})

// Emits
const emit = defineEmits(['quick-action'])

// 快捷操作配置
const quickActions = ref([
  {
    key: 'add-elderly',
    label: '新增老人档案',
    type: 'primary',
    icon: Plus,
    route: '/elderly/add'
  },
  {
    key: 'health-warning',
    label: '健康预警',
    type: 'warning',
    icon: Warning,
    route: '/health/warning'
  },
  {
    key: 'health-records',
    label: '健康记录',
    type: 'success',
    icon: Document,
    route: '/health/records'
  },
  {
    key: 'equipment-monitor',
    label: '设备监控',
    type: 'info',
    icon: Monitor,
    route: '/equipment/monitor'
  },
  {
    key: 'data-analysis',
    label: '数据分析',
    type: 'primary',
    icon: DataAnalysis,
    route: '/reports/analysis'
  },
  {
    key: 'system-settings',
    label: '系统设置',
    type: 'default',
    icon: Setting,
    route: '/system/users'
  }
])

// 系统状态配置
const systemStatus = computed(() => [
  {
    key: 'database',
    label: '数据库',
    value: props.data.databaseStatus || '正常',
    status: props.data.databaseStatus === '正常' ? 'success' : 'error'
  },
  {
    key: 'cache',
    label: '缓存服务',
    value: props.data.cacheStatus || '正常',
    status: props.data.cacheStatus === '正常' ? 'success' : 'error'
  },
  {
    key: 'api',
    label: 'API服务',
    value: props.data.apiStatus || '正常',
    status: props.data.apiStatus === '正常' ? 'success' : 'error'
  },
  {
    key: 'storage',
    label: '存储空间',
    value: props.data.storageUsage || '68%',
    status: getStorageStatus(props.data.storageUsage)
  }
])

// 获取存储状态
function getStorageStatus(usage) {
  if (!usage) return 'success'
  const percent = parseInt(usage.replace('%', ''))
  if (percent < 70) return 'success'
  if (percent < 85) return 'warning'
  return 'error'
}

// 处理快捷操作
const handleQuickAction = (action) => {
  emit('quick-action', action)
}
</script>

<style scoped>
.quick-actions-section {
  margin-bottom: 20px;
}

.card-header {
  font-weight: 500;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.quick-actions .el-button {
  flex: 1;
  min-width: 120px;
}

.system-status {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.status-indicator {
  position: relative;
  width: 12px;
  height: 12px;
}

.status-dot {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  position: relative;
}

.status-indicator.success .status-dot {
  background-color: #67c23a;
}

.status-indicator.warning .status-dot {
  background-color: #e6a23c;
}

.status-indicator.error .status-dot {
  background-color: #f56c6c;
}

.status-indicator.success .status-dot::after,
.status-indicator.warning .status-dot::after,
.status-indicator.error .status-dot::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: inherit;
  opacity: 0.3;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.5);
    opacity: 0.1;
  }
  100% {
    transform: scale(2);
    opacity: 0;
  }
}

.status-info {
  flex: 1;
}

.status-label {
  font-size: 14px;
  color: #606266;
  line-height: 1.4;
}

.status-value {
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}
</style>
