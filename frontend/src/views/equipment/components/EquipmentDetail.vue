<template>
  <div class="equipment-detail" v-if="equipment">
    <!-- 基本信息 -->
    <div class="detail-section">
      <h4><el-icon><Monitor /></el-icon> 基本信息</h4>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="设备编号">
          {{ equipment.deviceId }}
        </el-descriptions-item>
        <el-descriptions-item label="设备名称">
          {{ equipment.deviceName }}
        </el-descriptions-item>
        <el-descriptions-item label="设备类型">
          <el-tag :type="getDeviceTypeTagType(equipment.deviceType)">
            {{ getDeviceTypeName(equipment.deviceType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="设备状态">
          <el-tag :type="getStatusTagType(equipment.status)">
            {{ getStatusName(equipment.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="设备品牌">
          {{ equipment.brand || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="设备型号">
          {{ equipment.model || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="制造商">
          {{ equipment.manufacturer || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="设备位置">
          {{ equipment.location || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="设备描述" :span="2">
          {{ equipment.description || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 技术参数 -->
    <div class="detail-section">
      <h4><el-icon><Setting /></el-icon> 技术参数</h4>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="序列号">
          {{ equipment.serialNumber || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="MAC地址">
          {{ equipment.macAddress || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">
          {{ equipment.ipAddress || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="通信协议">
          <el-tag v-if="equipment.protocol">{{ equipment.protocol }}</el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="数据频率">
          {{ equipment.dataFrequency ? `${equipment.dataFrequency}秒` : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="固件版本">
          {{ equipment.firmwareVersion || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="API端点" :span="2">
          {{ equipment.apiEndpoint || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 运行状态 -->
    <div class="detail-section">
      <h4><el-icon><DataLine /></el-icon> 运行状态</h4>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="status-card">
            <div class="status-item">
              <div class="status-icon battery">
                <el-icon><Connection /></el-icon>
              </div>
              <div class="status-info">
                <div class="status-value">{{ equipment.batteryLevel || 0 }}%</div>
                <div class="status-label">电池电量</div>
              </div>
            </div>
            <el-progress 
              :percentage="equipment.batteryLevel || 0" 
              :status="getBatteryStatus(equipment.batteryLevel)"
              :stroke-width="8"
              style="margin-top: 10px;"
            />
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="status-card">
            <div class="status-item">
              <div class="status-icon online">
                <el-icon><Connection /></el-icon>
              </div>
              <div class="status-info">
                <div class="status-value">{{ onlineStatus }}</div>
                <div class="status-label">连接状态</div>
              </div>
            </div>
            <div class="status-time">
              最后在线：{{ formatTime(equipment.lastOnlineTime) }}
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="status-card">
            <div class="status-item">
              <div class="status-icon data">
                <el-icon><Upload /></el-icon>
              </div>
              <div class="status-info">
                <div class="status-value">{{ dataStatus }}</div>
                <div class="status-label">数据状态</div>
              </div>
            </div>
            <div class="status-time">
              最后上报：{{ formatTime(equipment.lastDataTime) }}
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 维护信息 -->
    <div class="detail-section">
      <h4><el-icon><Tools /></el-icon> 维护信息</h4>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="安装日期">
          {{ formatDate(equipment.installDate) }}
        </el-descriptions-item>
        <el-descriptions-item label="保修到期">
          {{ formatDate(equipment.warrantyExpiry) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatTime(equipment.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          {{ formatTime(equipment.updateTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="绑定老人" :span="2">
          <span v-if="equipment.elderlyId">
            老人ID: {{ equipment.elderlyId }}
            <el-button size="small" type="primary" link @click="viewElderly">
              查看详情
            </el-button>
          </span>
          <span v-else>未绑定</span>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 配置参数 -->
    <div class="detail-section" v-if="equipment.configParams">
      <h4><el-icon><Setting /></el-icon> 配置参数</h4>
      <el-card class="config-card">
        <pre class="config-content">{{ formatConfig(equipment.configParams) }}</pre>
      </el-card>
    </div>

    <!-- 操作按钮 -->
    <div class="detail-actions">
      <el-button type="primary" @click="editEquipment">
        <el-icon><Edit /></el-icon>
        编辑设备
      </el-button>
      <el-button 
        :type="equipment.status === 'ONLINE' ? 'warning' : 'success'"
        @click="toggleStatus"
      >
        <el-icon><Switch /></el-icon>
        {{ equipment.status === 'ONLINE' ? '设为离线' : '设为在线' }}
      </el-button>
      <el-button type="info" @click="bindElderly">
        <el-icon><Link /></el-icon>
        绑定老人
      </el-button>
      <el-button type="danger" @click="deleteEquipment">
        <el-icon><Delete /></el-icon>
        删除设备
      </el-button>
    </div>
  </div>
</template>

<script setup>
import {
    Connection,
    DataLine,
    Delete,
    Edit,
    Link,
    Monitor, Setting,
    Switch,
    Tools,
    Upload
} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { computed } from 'vue'

// Props
const props = defineProps({
  equipment: {
    type: Object,
    default: () => null
  }
})

// Emits
const emit = defineEmits(['edit', 'delete', 'toggle-status', 'bind-elderly', 'view-elderly'])

// 计算属性
const onlineStatus = computed(() => {
  if (!props.equipment) return '-'
  return props.equipment.status === 'ONLINE' ? '在线' : '离线'
})

const dataStatus = computed(() => {
  if (!props.equipment || !props.equipment.lastDataTime) return '无数据'
  
  const lastTime = new Date(props.equipment.lastDataTime)
  const now = new Date()
  const diffMinutes = Math.floor((now - lastTime) / (1000 * 60))
  
  if (diffMinutes < 5) return '正常'
  if (diffMinutes < 30) return '延迟'
  return '异常'
})

// 方法
const getDeviceTypeName = (type) => {
  const typeMap = {
    'BLOOD_PRESSURE': '智能血压计',
    'BLOOD_GLUCOSE': '血糖仪',
    'SMART_WATCH': '智能手环',
    'THERMOMETER': '体温计',
    'ECG_MONITOR': '心电监护仪',
    'WEIGHT_SCALE': '智能体重秤',
    'LOCATOR': '定位器',
    'EMERGENCY_BUTTON': '紧急呼叫器',
    'ENVIRONMENT_SENSOR': '环境监测器'
  }
  return typeMap[type] || type
}

const getDeviceTypeTagType = (type) => {
  const typeMap = {
    'BLOOD_PRESSURE': 'danger',
    'BLOOD_GLUCOSE': 'warning',
    'SMART_WATCH': 'success',
    'THERMOMETER': 'info',
    'ECG_MONITOR': 'danger',
    'WEIGHT_SCALE': 'primary',
    'LOCATOR': 'warning',
    'EMERGENCY_BUTTON': 'danger',
    'ENVIRONMENT_SENSOR': 'success'
  }
  return typeMap[type] || ''
}

const getStatusName = (status) => {
  const statusMap = {
    'ONLINE': '在线',
    'OFFLINE': '离线',
    'FAULT': '故障',
    'MAINTENANCE': '维护中'
  }
  return statusMap[status] || status
}

const getStatusTagType = (status) => {
  const statusMap = {
    'ONLINE': 'success',
    'OFFLINE': 'info',
    'FAULT': 'danger',
    'MAINTENANCE': 'warning'
  }
  return statusMap[status] || ''
}

const getBatteryStatus = (level) => {
  if (level <= 20) return 'exception'
  if (level <= 50) return 'warning'
  return 'success'
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const formatConfig = (config) => {
  try {
    const obj = JSON.parse(config)
    return JSON.stringify(obj, null, 2)
  } catch {
    return config
  }
}

// 事件处理
const editEquipment = () => {
  emit('edit', props.equipment)
}

const toggleStatus = () => {
  emit('toggle-status', props.equipment)
}

const bindElderly = () => {
  emit('bind-elderly', props.equipment)
}

const viewElderly = () => {
  emit('view-elderly', props.equipment.elderlyId)
}

const deleteEquipment = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除设备"${props.equipment.deviceName}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    emit('delete', props.equipment)
  } catch (error) {
    // 用户取消
  }
}
</script>

<style scoped>
.equipment-detail {
  padding: 20px;
}

.detail-section {
  margin-bottom: 30px;
}

.detail-section h4 {
  color: #303133;
  font-size: 16px;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-card {
  height: 100%;
}

.status-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.status-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
  margin-right: 12px;
}

.status-icon.battery {
  background: linear-gradient(135deg, #E6A23C, #EEBE77);
}

.status-icon.online {
  background: linear-gradient(135deg, #67C23A, #85CE61);
}

.status-icon.data {
  background: linear-gradient(135deg, #409EFF, #66B1FF);
}

.status-info {
  flex: 1;
}

.status-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 2px;
}

.status-label {
  font-size: 12px;
  color: #909399;
}

.status-time {
  font-size: 12px;
  color: #909399;
  text-align: center;
  margin-top: 8px;
}

.config-card {
  background: #f8f9fa;
}

.config-content {
  margin: 0;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.5;
  color: #303133;
  white-space: pre-wrap;
  word-break: break-all;
}

.detail-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.detail-actions .el-button {
  margin: 0 8px;
}

:deep(.el-descriptions__cell) {
  padding: 12px !important;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  color: #606266;
}
</style>
