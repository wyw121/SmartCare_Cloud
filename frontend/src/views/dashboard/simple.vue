<template>
  <div class="dashboard-container">
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><Platform /></el-icon>
        首页仪表板
      </h1>
      <p class="page-subtitle">智慧医养大数据公共服务平台</p>
    </div>

    <!-- 统计卡片区域 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="stat in stats" :key="stat.title">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" :style="{ backgroundColor: stat.color + '20', color: stat.color }">
              <component :is="stat.icon" />
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stat.value }}</div>
              <div class="stats-title">{{ stat.title }}</div>
            </div>
          </div>
          <div class="stats-footer">
            <span class="stats-trend" :class="stat.trend">
              <el-icon v-if="stat.trend === 'up'"><ArrowUp /></el-icon>
              <el-icon v-else><ArrowDown /></el-icon>
              {{ stat.change }}
            </span>
            <span class="stats-label">较昨日</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速操作 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="action-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">快速操作</span>
            </div>
          </template>
          <div class="action-grid">
            <div 
              class="action-item" 
              v-for="action in quickActions" 
              :key="action.name"
              @click="handleAction(action)"
            >
              <div class="action-icon" :style="{ backgroundColor: action.color + '20', color: action.color }">
                <component :is="action.icon" />
              </div>
              <span class="action-name">{{ action.name }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近活动 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card header="最近活动">
          <el-timeline>
            <el-timeline-item
              v-for="activity in recentActivities"
              :key="activity.id"
              :timestamp="activity.time"
              :type="activity.type"
            >
              <h4>{{ activity.title }}</h4>
              <p>{{ activity.description }}</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {
    ArrowDown,
    ArrowUp,
    DocumentAdd,
    Monitor,
    Platform,
    Setting,
    User,
    Warning
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 统计数据
const stats = reactive([
  {
    title: '老人总数',
    value: '1,258',
    change: '+12',
    trend: 'up',
    icon: User,
    color: '#409EFF'
  },
  {
    title: '健康预警',
    value: '23',
    change: '-5',
    trend: 'down',
    icon: Warning,
    color: '#F56C6C'
  },
  {
    title: '在线设备',
    value: '89',
    change: '+3',
    trend: 'up',
    icon: Monitor,
    color: '#67C23A'
  },
  {
    title: '今日体检',
    value: '45',
    change: '+8',
    trend: 'up',
    icon: DocumentAdd,
    color: '#E6A23C'
  }
])

// 快速操作
const quickActions = reactive([
  { name: '新增老人', icon: User, color: '#409EFF', route: '/elderly/list' },
  { name: '健康记录', icon: DocumentAdd, color: '#67C23A', route: '/health/records' },
  { name: '设备监控', icon: Monitor, color: '#E6A23C', route: '/equipment/list' },
  { name: '系统设置', icon: Setting, color: '#909399', route: '/system/users' }
])

// 最近活动
const recentActivities = reactive([
  {
    id: 1,
    title: '新增老人档案',
    description: '添加了张大爷的个人档案信息',
    time: '2小时前',
    type: 'primary'
  },
  {
    id: 2,
    title: '健康预警处理',
    description: '处理了李奶奶的血压异常预警',
    time: '3小时前',
    type: 'success'
  },
  {
    id: 3,
    title: '设备状态更新',
    description: '血压监测设备在线状态已更新',
    time: '5小时前',
    type: 'info'
  }
])

/**
 * 处理快速操作点击
 */
const handleAction = (action) => {
  if (action.route) {
    router.push(action.route)
  } else {
    ElMessage.info(`点击了${action.name}`)
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
  
  .page-title {
    font-size: 28px;
    color: #303133;
    margin: 0 0 10px 0;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    
    .el-icon {
      font-size: 32px;
      color: #409EFF;
    }
  }
  
  .page-subtitle {
    font-size: 16px;
    color: #606266;
    margin: 0;
  }
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  }
}

.stats-content {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  
  .el-icon {
    font-size: 24px;
  }
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.stats-title {
  font-size: 14px;
  color: #606266;
}

.stats-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
}

.stats-trend {
  display: flex;
  align-items: center;
  gap: 2px;
  
  &.up {
    color: #67C23A;
  }
  
  &.down {
    color: #F56C6C;
  }
}

.stats-label {
  color: #909399;
}

.action-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .card-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  
  &:hover {
    background-color: #f8f9fa;
    border-color: #e4e7ed;
    transform: translateY(-2px);
  }
}

.action-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
  
  .el-icon {
    font-size: 20px;
  }
}

.action-name {
  font-size: 14px;
  color: #303133;
  text-align: center;
}

:deep(.el-card__header) {
  background-color: #fafafa;
  border-bottom: 1px solid #ebeef5;
  font-weight: 600;
}

:deep(.el-timeline-item__content) {
  padding-bottom: 15px;
}
</style>
