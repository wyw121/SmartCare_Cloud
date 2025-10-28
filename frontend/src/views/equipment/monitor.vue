<template>
  <div class="equipment-monitor">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1><el-icon><Monitor /></el-icon> 设备实时监控</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleRefresh" :loading="loading">
          <el-icon><Refresh /></el-icon> 刷新
        </el-button>
        <el-switch
          v-model="autoRefresh"
          active-text="自动刷新"
          @change="handleAutoRefreshChange"
        />
      </div>
    </div>

    <!-- 设备状态统计 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="stat in statistics" :key="stat.status">
        <el-card class="stat-card" :class="stat.class" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: stat.bgColor, color: stat.color }">
              <el-icon><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.count }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="filterForm" inline>
        <el-form-item label="设备类型">
          <el-select v-model="filterForm.deviceType" placeholder="全部类型" clearable style="width: 180px">
            <el-option label="全部" value="" />
            <el-option label="血压计" value="血压计" />
            <el-option label="血糖仪" value="血糖仪" />
            <el-option label="心电仪" value="心电仪" />
            <el-option label="体温计" value="体温计" />
            <el-option label="智能手环" value="智能手环" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备状态">
          <el-select v-model="filterForm.status" placeholder="全部状态" clearable style="width: 180px">
            <el-option label="全部" value="" />
            <el-option label="在线" value="在线" />
            <el-option label="离线" value="离线" />
            <el-option label="故障" value="故障" />
            <el-option label="维护中" value="维护中" />
          </el-select>
        </el-form-item>
        <el-form-item label="搜索">
          <el-input 
            v-model="filterForm.keyword" 
            placeholder="设备编号/名称" 
            clearable 
            style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">
            <el-icon><Search /></el-icon> 查询
          </el-button>
          <el-button @click="handleResetFilter">
            <el-icon><RefreshLeft /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 设备列表 -->
    <el-card class="device-list-card" shadow="never">
      <el-row :gutter="20" v-loading="loading">
        <el-col 
          :xs="24" 
          :sm="12" 
          :md="8" 
          :lg="6" 
          v-for="device in filteredDevices" 
          :key="device.id">
          <el-card class="device-card" :class="device.status" shadow="hover" @click="handleDeviceClick(device)">
            <!-- 设备状态标签 -->
            <div class="device-status-badge" :class="device.status">
              <el-icon><component :is="getStatusIcon(device.status)" /></el-icon>
              {{ device.status }}
            </div>

            <!-- 设备图标 -->
            <div class="device-icon" :style="{ backgroundColor: getDeviceColor(device.deviceType) }">
              <el-icon><component :is="getDeviceIcon(device.deviceType)" /></el-icon>
            </div>

            <!-- 设备信息 -->
            <div class="device-info">
              <h3 class="device-name">{{ device.deviceName }}</h3>
              <p class="device-type">{{ device.deviceType }}</p>
              <p class="device-sn">SN: {{ device.serialNumber }}</p>
            </div>

            <!-- 设备数据 -->
            <div class="device-data">
              <div class="data-item">
                <span class="data-label">使用老人:</span>
                <span class="data-value">{{ device.elderlyName || '-' }}</span>
              </div>
              <div class="data-item">
                <span class="data-label">最后在线:</span>
                <span class="data-value">{{ device.lastOnlineTime }}</span>
              </div>
              <div class="data-item" v-if="device.batteryLevel !== undefined">
                <span class="data-label">电量:</span>
                <el-progress 
                  :percentage="device.batteryLevel" 
                  :color="getBatteryColor(device.batteryLevel)"
                  :show-text="false"
                  style="width: 60px"
                />
                <span class="data-value">{{ device.batteryLevel }}%</span>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="device-actions">
              <el-button size="small" type="primary" link @click.stop="handleViewDetail(device)">
                查看详情
              </el-button>
              <el-button 
                size="small" 
                type="warning" 
                link 
                v-if="device.status === '故障'"
                @click.stop="handleRepair(device)">
                报修
              </el-button>
            </div>
          </el-card>
        </el-col>

        <!-- 空状态 -->
        <el-col :span="24" v-if="filteredDevices.length === 0 && !loading">
          <el-empty description="暂无设备数据" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 设备详情对话框 -->
    <el-dialog 
      v-model="detailVisible" 
      :title="`${currentDevice?.deviceName} - 设备详情`"
      width="800px">
      <div v-if="currentDevice" class="device-detail" v-loading="detailLoading">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="设备名称">{{ currentDevice.deviceName }}</el-descriptions-item>
          <el-descriptions-item label="设备类型">{{ currentDevice.deviceType }}</el-descriptions-item>
          <el-descriptions-item label="序列号">{{ currentDevice.serialNumber }}</el-descriptions-item>
          <el-descriptions-item label="设备状态">
            <el-tag :type="getStatusType(currentDevice.status)">
              {{ currentDevice.status }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="使用老人">
            {{ currentDevice.elderlyName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="位置">{{ currentDevice.location || '-' }}</el-descriptions-item>
          <el-descriptions-item label="最后在线时间" :span="2">
            {{ currentDevice.lastOnlineTime }}
          </el-descriptions-item>
        </el-descriptions>

        <!-- 实时数据图表 -->
        <div class="chart-section" v-if="currentDevice.status === '在线'">
          <h3>实时监测数据</h3>
          <v-chart :option="deviceChartOption" autoresize style="height: 300px" />
        </div>

        <!-- 告警记录 -->
        <div class="alert-section" v-if="deviceAlerts.length > 0">
          <h3>近期告警记录</h3>
          <el-timeline>
            <el-timeline-item 
              v-for="alert in deviceAlerts" 
              :key="alert.id"
              :timestamp="alert.time"
              :type="alert.level === '严重' ? 'danger' : 'warning'">
              {{ alert.message }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExportDeviceData">
          <el-icon><Download /></el-icon> 导出数据
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Monitor, Refresh, Search, RefreshLeft, Download,
  CircleCheck, WarningFilled, CircleClose, Setting,
  Stopwatch, Odometer, DataLine, Connection
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import VChart from 'vue-echarts'

// 统计数据
const statistics = ref([
  { status: 'online', label: '在线设备', count: 0, icon: 'CircleCheck', class: 'online', bgColor: 'rgba(103, 194, 58, 0.2)', color: '#67C23A' },
  { status: 'offline', label: '离线设备', count: 0, icon: 'CircleClose', class: 'offline', bgColor: 'rgba(144, 147, 153, 0.2)', color: '#909399' },
  { status: 'fault', label: '故障设备', count: 0, icon: 'WarningFilled', class: 'fault', bgColor: 'rgba(245, 108, 108, 0.2)', color: '#F56C6C' },
  { status: 'maintain', label: '维护中', count: 0, icon: 'Setting', class: 'maintain', bgColor: 'rgba(230, 162, 60, 0.2)', color: '#E6A23C' }
])

// 筛选表单
const filterForm = reactive({
  deviceType: '',
  status: '',
  keyword: ''
})

// 设备列表
const loading = ref(false)
const devices = ref([])

// 自动刷新
const autoRefresh = ref(false)
let refreshTimer = null

// 设备详情
const detailVisible = ref(false)
const detailLoading = ref(false)
const currentDevice = ref(null)
const deviceAlerts = ref([])

// 模拟设备数据
const mockDevices = [
  {
    id: 1,
    deviceName: '血压计-001',
    deviceType: '血压计',
    serialNumber: 'BP-2024-001',
    status: '在线',
    elderlyName: '张三',
    location: '101房间',
    lastOnlineTime: '2024-01-20 14:30:25',
    batteryLevel: 85
  },
  {
    id: 2,
    deviceName: '血糖仪-001',
    deviceType: '血糖仪',
    serialNumber: 'BG-2024-001',
    status: '在线',
    elderlyName: '李四',
    location: '102房间',
    lastOnlineTime: '2024-01-20 14:28:15',
    batteryLevel: 60
  },
  {
    id: 3,
    deviceName: '心电仪-001',
    deviceType: '心电仪',
    serialNumber: 'ECG-2024-001',
    status: '故障',
    elderlyName: '王五',
    location: '103房间',
    lastOnlineTime: '2024-01-20 10:15:30',
    batteryLevel: 25
  },
  {
    id: 4,
    deviceName: '智能手环-001',
    deviceType: '智能手环',
    serialNumber: 'SB-2024-001',
    status: '离线',
    elderlyName: '赵六',
    location: '104房间',
    lastOnlineTime: '2024-01-19 18:20:00',
    batteryLevel: 5
  },
  {
    id: 5,
    deviceName: '体温计-001',
    deviceType: '体温计',
    serialNumber: 'TM-2024-001',
    status: '维护中',
    elderlyName: null,
    location: '设备室',
    lastOnlineTime: '2024-01-20 09:00:00',
    batteryLevel: 100
  }
]

// 图表配置
const deviceChartOption = computed(() => ({
  title: {
    text: '近24小时数据趋势',
    left: 'center',
    textStyle: {
      fontSize: 14
    }
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00']
  },
  yAxis: {
    type: 'value',
    name: '测量值'
  },
  series: [
    {
      name: '监测数据',
      type: 'line',
      smooth: true,
      data: [120, 125, 118, 130, 128, 135, 132],
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
          { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
        ])
      },
      itemStyle: {
        color: '#409EFF'
      }
    }
  ]
}))

// 筛选后的设备列表
const filteredDevices = computed(() => {
  let result = devices.value

  if (filterForm.deviceType) {
    result = result.filter(d => d.deviceType === filterForm.deviceType)
  }

  if (filterForm.status) {
    result = result.filter(d => d.status === filterForm.status)
  }

  if (filterForm.keyword) {
    const keyword = filterForm.keyword.toLowerCase()
    result = result.filter(d => 
      d.deviceName.toLowerCase().includes(keyword) || 
      d.serialNumber.toLowerCase().includes(keyword)
    )
  }

  return result
})

// 获取设备列表
const fetchDevices = async () => {
  loading.value = true
  try {
    // 模拟延迟
    await new Promise(resolve => setTimeout(resolve, 500))
    
    devices.value = mockDevices
    updateStatistics()
  } catch (error) {
    console.error('获取设备列表失败:', error)
    ElMessage.error('获取设备列表失败')
  } finally {
    loading.value = false
  }
}

// 更新统计数据
const updateStatistics = () => {
  const counts = { '在线': 0, '离线': 0, '故障': 0, '维护中': 0 }
  for (const device of devices.value) {
    if (counts.hasOwnProperty(device.status)) {
      counts[device.status]++
    }
  }
  statistics.value[0].count = counts['在线']
  statistics.value[1].count = counts['离线']
  statistics.value[2].count = counts['故障']
  statistics.value[3].count = counts['维护中']
}

// 刷新
const handleRefresh = () => {
  fetchDevices()
}

// 筛选
const handleFilter = () => {
  // 筛选由computed自动处理
}

// 重置筛选
const handleResetFilter = () => {
  filterForm.deviceType = ''
  filterForm.status = ''
  filterForm.keyword = ''
}

// 自动刷新切换
const handleAutoRefreshChange = (value) => {
  if (value) {
    refreshTimer = setInterval(() => {
      fetchDevices()
    }, 30000) // 30秒刷新一次
    ElMessage.success('已开启自动刷新 (30秒/次)')
  } else {
    if (refreshTimer) {
      clearInterval(refreshTimer)
      refreshTimer = null
    }
    ElMessage.info('已关闭自动刷新')
  }
}

// 设备卡片点击
const handleDeviceClick = (device) => {
  handleViewDetail(device)
}

// 查看详情
const handleViewDetail = async (device) => {
  currentDevice.value = device
  detailVisible.value = true
  detailLoading.value = true

  try {
    // 模拟加载告警记录
    await new Promise(resolve => setTimeout(resolve, 300))
    
    deviceAlerts.value = [
      { id: 1, time: '2024-01-20 12:30:00', level: '警告', message: '电量低于30%' },
      { id: 2, time: '2024-01-19 15:20:00', level: '严重', message: '数据传输异常' }
    ]
  } catch (error) {
    console.error('获取设备详情失败:', error)
  } finally {
    detailLoading.value = false
  }
}

// 报修
const handleRepair = (device) => {
  ElMessage.info(`提交 ${device.deviceName} 的维修申请...`)
}

// 导出设备数据
const handleExportDeviceData = () => {
  ElMessage.info('导出设备数据功能开发中...')
}

// 获取设备图标
const getDeviceIcon = (type) => {
  const iconMap = {
    '血压计': 'Odometer',
    '血糖仪': 'DataLine',
    '心电仪': 'Connection',
    '体温计': 'Stopwatch',
    '智能手环': 'Stopwatch'
  }
  return iconMap[type] || 'Monitor'
}

// 获取设备颜色
const getDeviceColor = (type) => {
  const colorMap = {
    '血压计': 'rgba(64, 158, 255, 0.2)',
    '血糖仪': 'rgba(103, 194, 58, 0.2)',
    '心电仪': 'rgba(245, 108, 108, 0.2)',
    '体温计': 'rgba(230, 162, 60, 0.2)',
    '智能手环': 'rgba(144, 147, 153, 0.2)'
  }
  return colorMap[type] || 'rgba(64, 158, 255, 0.2)'
}

// 获取状态图标
const getStatusIcon = (status) => {
  const iconMap = {
    '在线': 'CircleCheck',
    '离线': 'CircleClose',
    '故障': 'WarningFilled',
    '维护中': 'Setting'
  }
  return iconMap[status] || 'Monitor'
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    '在线': 'success',
    '离线': 'info',
    '故障': 'danger',
    '维护中': 'warning'
  }
  return typeMap[status] || 'info'
}

// 获取电量颜色
const getBatteryColor = (level) => {
  if (level > 60) return '#67C23A'
  if (level > 20) return '#E6A23C'
  return '#F56C6C'
}

onMounted(() => {
  fetchDevices()
})

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
})
</script>

<style scoped lang="scss">
.equipment-monitor {
  padding: 20px;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h1 {
      font-size: 24px;
      font-weight: 500;
      color: #303133;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: 15px;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-4px);
      }

      .stat-content {
        display: flex;
        align-items: center;
        gap: 16px;

        .stat-icon {
          width: 60px;
          height: 60px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 28px;
        }

        .stat-info {
          flex: 1;

          .stat-value {
            font-size: 28px;
            font-weight: bold;
            line-height: 1;
            margin-bottom: 8px;
          }

          .stat-label {
            font-size: 14px;
            color: #909399;
          }
        }
      }
    }
  }

  .filter-card {
    margin-bottom: 20px;
  }

  .device-list-card {
    .device-card {
      margin-bottom: 20px;
      cursor: pointer;
      position: relative;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      .device-status-badge {
        position: absolute;
        top: 10px;
        right: 10px;
        padding: 4px 12px;
        border-radius: 12px;
        font-size: 12px;
        display: flex;
        align-items: center;
        gap: 4px;

        &.在线 {
          background-color: #f0f9ff;
          color: #67C23A;
        }

        &.离线 {
          background-color: #f4f4f5;
          color: #909399;
        }

        &.故障 {
          background-color: #fef0f0;
          color: #F56C6C;
        }

        &.维护中 {
          background-color: #fdf6ec;
          color: #E6A23C;
        }
      }

      .device-icon {
        width: 80px;
        height: 80px;
        margin: 0 auto 15px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 40px;
        color: #409EFF;
      }

      .device-info {
        text-align: center;
        margin-bottom: 15px;

        .device-name {
          font-size: 16px;
          font-weight: 500;
          margin: 0 0 5px 0;
          color: #303133;
        }

        .device-type {
          font-size: 14px;
          color: #909399;
          margin: 0 0 5px 0;
        }

        .device-sn {
          font-size: 12px;
          color: #C0C4CC;
          margin: 0;
        }
      }

      .device-data {
        border-top: 1px solid #EBEEF5;
        padding-top: 15px;
        margin-bottom: 15px;

        .data-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;
          font-size: 13px;

          .data-label {
            color: #909399;
          }

          .data-value {
            color: #303133;
            font-weight: 500;
          }
        }
      }

      .device-actions {
        display: flex;
        justify-content: center;
        gap: 10px;
        border-top: 1px solid #EBEEF5;
        padding-top: 15px;
      }
    }
  }

  .device-detail {
    .chart-section,
    .alert-section {
      margin-top: 20px;

      h3 {
        font-size: 15px;
        font-weight: 500;
        margin-bottom: 15px;
        color: #303133;
      }
    }
  }
}
</style>
