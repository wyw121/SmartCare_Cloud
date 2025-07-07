<template>
  <div class="equipment-management">
    <!-- 页面头部 -->
    <el-card class="page-header">
      <div class="header-content">
        <h2>设备管理</h2>
        <p>智慧医养设备接入与管理中心</p>
      </div>
    </el-card>

    <!-- 搜索筛选区 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="设备类型">
          <el-select 
            v-model="searchForm.deviceType" 
            placeholder="请选择设备类型" 
            clearable 
            style="width: 150px"
            @change="handleSearch"
          >
            <el-option label="智能血压计" value="BLOOD_PRESSURE" />
            <el-option label="血糖仪" value="BLOOD_GLUCOSE" />
            <el-option label="智能手环" value="SMART_WATCH" />
            <el-option label="体温计" value="THERMOMETER" />
            <el-option label="心电监护仪" value="ECG_MONITOR" />
            <el-option label="智能体重秤" value="WEIGHT_SCALE" />
            <el-option label="定位器" value="LOCATOR" />
            <el-option label="紧急呼叫器" value="EMERGENCY_BUTTON" />
            <el-option label="环境监测器" value="ENVIRONMENT_SENSOR" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择设备状态" 
            clearable 
            style="width: 150px"
            @change="handleSearch"
          >
            <el-option label="在线" value="ONLINE" />
            <el-option label="离线" value="OFFLINE" />
            <el-option label="故障" value="FAULT" />
            <el-option label="维护中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="搜索关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入设备名称、编号或品牌"
            style="width: 250px"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card">
      <div class="toolbar">
        <el-button type="primary" @click="showAddDialog">
          <el-icon><Plus /></el-icon>
          新增设备
        </el-button>
        <el-button type="success" @click="showIntegrationGuide">
          <el-icon><Document /></el-icon>
          接入指南
        </el-button>
        <el-button type="warning" @click="exportData">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
        <el-button type="info" @click="showStatistics">
          <el-icon><DataAnalysis /></el-icon>
          统计分析
        </el-button>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-card class="statistics-card">
      <div class="statistics-cards">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-content">
                <div class="stat-icon online">
                  <el-icon><Connection /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ statistics.onlineCount || 0 }}</div>
                  <div class="stat-label">在线设备</div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-content">
                <div class="stat-icon total">
                  <el-icon><Monitor /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
                  <div class="stat-label">设备总数</div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-content">
                <div class="stat-icon fault">
                  <el-icon><Warning /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ statistics.faultCount || 0 }}</div>
                  <div class="stat-label">故障设备</div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-content">
                <div class="stat-icon rate">
                  <el-icon><Checked /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ onlineRate }}%</div>
                  <div class="stat-label">在线率</div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 设备列表表格 -->
    <el-card class="table-card">
      <el-table 
        :data="equipmentList" 
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="deviceId" label="设备编号" width="140" show-overflow-tooltip />
        <el-table-column prop="deviceName" label="设备名称" width="160" show-overflow-tooltip />
        <el-table-column label="设备类型" width="130">
          <template #default="scope">
            <el-tag :type="getDeviceTypeTagType(scope.row.deviceType)">
              {{ getDeviceTypeName(scope.row.deviceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="brand" label="品牌" width="100" show-overflow-tooltip />
        <el-table-column prop="model" label="型号" width="120" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="位置" width="140" show-overflow-tooltip />
        <el-table-column label="电池电量" width="120">
          <template #default="scope">
            <div v-if="scope.row.batteryLevel !== null">
              <el-progress 
                :percentage="scope.row.batteryLevel" 
                :status="getBatteryStatus(scope.row.batteryLevel)"
                :stroke-width="6"
                :show-text="false"
              />
              <span style="margin-left: 8px; font-size: 12px;">{{ scope.row.batteryLevel }}%</span>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastOnlineTime" label="最后在线" min-width="150" show-overflow-tooltip>
          <template #default="scope">
            {{ formatTime(scope.row.lastOnlineTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="360" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-tooltip content="查看详情" placement="top">
                <el-button 
                  text 
                  type="primary" 
                  size="small" 
                  @click="viewDetail(scope.row)"
                >
                  查看
                </el-button>
              </el-tooltip>
              
              <el-tooltip content="编辑信息" placement="top">
                <el-button 
                  text 
                  type="primary" 
                  size="small" 
                  @click="editEquipment(scope.row)"
                >
                  编辑
                </el-button>
              </el-tooltip>
              
              <el-tooltip content="设备控制" placement="top">
                <el-button 
                  text 
                  :type="scope.row.status === 'ONLINE' ? 'warning' : 'success'"
                  size="small"
                  @click="toggleStatus(scope.row)"
                >
                  {{ scope.row.status === 'ONLINE' ? '离线' : '上线' }}
                </el-button>
              </el-tooltip>
              
              <el-tooltip content="删除设备" placement="top">
                <el-button 
                  text 
                  type="danger" 
                  size="small" 
                  @click="deleteEquipment(scope.row)"
                >
                  删除
                </el-button>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>

    <!-- 设备接入指南对话框 -->
    <el-dialog
      v-model="integrationDialogVisible"
      title="设备接入指南"
      width="90%"
      :close-on-click-modal="false"
    >
      <device-integration-guide />
    </el-dialog>

    <!-- 设备详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="设备详情"
      width="800px"
      :close-on-click-modal="false"
    >
      <equipment-detail :equipment="currentEquipment" />
    </el-dialog>

    <!-- 添加/编辑设备对话框 -->
    <el-dialog
      v-model="formDialogVisible"
      :title="isEdit ? '编辑设备' : '添加设备'"
      width="600px"
      :close-on-click-modal="false"
    >
      <equipment-form 
        :equipment="currentEquipment"
        :is-edit="isEdit"
        @success="handleFormSuccess"
        @cancel="formDialogVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { equipment } from '@/api'
import { Checked, Connection, DataAnalysis, Document, Download, Monitor, Plus, Refresh, Search, Warning } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, onMounted, reactive, ref } from 'vue'
import DeviceIntegrationGuide from './components/DeviceIntegrationGuide.vue'
import EquipmentDetail from './components/EquipmentDetail.vue'
import EquipmentForm from './components/EquipmentForm.vue'

// 响应式数据
const loading = ref(false)
const equipmentList = ref([])
const statistics = ref({})
const integrationDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const formDialogVisible = ref(false)
const currentEquipment = ref(null)
const isEdit = ref(false)
const selectedRows = ref([])

// 搜索表单
const searchForm = reactive({
  deviceType: '',
  status: '',
  keyword: ''
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 计算在线率
const onlineRate = computed(() => {
  if (statistics.value.totalCount > 0) {
    return Math.round((statistics.value.onlineCount / statistics.value.totalCount) * 100)
  }
  return 0
})

// 获取设备列表
const fetchEquipmentList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      deviceType: searchForm.deviceType,
      status: searchForm.status,
      keyword: searchForm.keyword
    }
    
    const response = await equipment.getList(params)
    if (response.code === 200) {
      equipmentList.value = response.data.records || []
      pagination.total = response.data.total || 0
    }
  } catch (error) {
    console.error('获取设备列表失败:', error)
    ElMessage.warning('后端服务连接失败，显示模拟数据')
    
    // 开发模式下使用模拟数据
    equipmentList.value = [
      {
        id: 1,
        deviceId: 'DEV001',
        deviceName: '智能血压计A1',
        deviceType: 'BLOOD_PRESSURE',
        brand: '欧姆龙',
        model: 'HEM-7200',
        status: 'ONLINE',
        location: '201房间',
        elderlyId: 1,
        elderlyName: '张三',
        lastOnlineTime: '2025-07-08 10:30:00',
        installTime: '2025-01-15',
        batteryLevel: 85,
        signalStrength: 95
      },
      {
        id: 2,
        deviceId: 'DEV002',
        deviceName: '血糖仪B2',
        deviceType: 'BLOOD_GLUCOSE',
        brand: '强生',
        model: 'OneTouch',
        status: 'ONLINE',
        location: '202房间',
        elderlyId: 2,
        elderlyName: '李四',
        lastOnlineTime: '2025-07-08 09:45:00',
        installTime: '2025-02-01',
        batteryLevel: 70,
        signalStrength: 88
      }
    ]
    pagination.total = 89
  } finally {
    loading.value = false
  }
}

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const response = await equipment.getStatistics()
    if (response.code === 200) {
      statistics.value = response.data || {}
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    // 开发模式下使用模拟数据
    statistics.value = {
      totalCount: 89,
      onlineCount: 76,
      offlineCount: 13,
      faultCount: 5
    }
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchEquipmentList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    deviceType: '',
    status: '',
    keyword: ''
  })
  handleSearch()
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.size = size
  fetchEquipmentList()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  fetchEquipmentList()
}

// 表格多选处理
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 显示添加对话框
const showAddDialog = () => {
  currentEquipment.value = null
  isEdit.value = false
  formDialogVisible.value = true
}

// 显示接入指南
const showIntegrationGuide = () => {
  integrationDialogVisible.value = true
}

// 显示统计分析
const showStatistics = () => {
  ElMessage.info('统计分析功能开发中...')
}

// 查看详情
const viewDetail = (row) => {
  currentEquipment.value = row
  detailDialogVisible.value = true
}

// 编辑设备
const editEquipment = (row) => {
  currentEquipment.value = { ...row }
  isEdit.value = true
  formDialogVisible.value = true
}

// 切换设备状态
const toggleStatus = async (row) => {
  const newStatus = row.status === 'ONLINE' ? 'OFFLINE' : 'ONLINE'
  try {
    const response = await equipment.updateStatus(row.id, newStatus)
    if (response.code === 200) {
      ElMessage.success('设备状态更新成功')
      fetchEquipmentList()
      fetchStatistics()
    }
  } catch (error) {
    console.error('更新设备状态失败:', error)
    ElMessage.error('更新设备状态失败')
  }
}

// 删除设备
const deleteEquipment = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除设备"${row.deviceName}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await equipment.delete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      fetchEquipmentList()
      fetchStatistics()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除设备失败:', error)
      ElMessage.error('删除设备失败')
    }
  }
}

// 表单操作成功
const handleFormSuccess = () => {
  formDialogVisible.value = false
  fetchEquipmentList()
  fetchStatistics()
}

// 导出数据
const exportData = () => {
  ElMessage.info('导出功能开发中...')
}

// 工具函数
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

// 初始化
onMounted(() => {
  fetchEquipmentList()
  fetchStatistics()
})
</script>

<style scoped>
.equipment-management {
  padding: 20px;
}

/* 页面头部样式 */
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  padding: 20px;
}

.header-content h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.header-content p {
  margin: 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

/* 搜索卡片样式 */
.search-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.search-form {
  padding: 10px 0;
}

.search-form .el-form-item {
  margin-bottom: 10px;
}

/* 工具栏卡片样式 */
.toolbar-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  gap: 12px;
  align-items: center;
}

/* 统计卡片样式 */
.statistics-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.statistics-cards {
  padding: 10px 0;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  margin-right: 15px;
  flex-shrink: 0;
}

.stat-icon.online {
  background: linear-gradient(135deg, #67C23A, #85CE61);
}

.stat-icon.total {
  background: linear-gradient(135deg, #409EFF, #66B1FF);
}

.stat-icon.fault {
  background: linear-gradient(135deg, #F56C6C, #F78989);
}

.stat-icon.rate {
  background: linear-gradient(135deg, #E6A23C, #EEBE77);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 表格卡片样式 */
.table-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  gap: 4px;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}

.action-buttons .el-button {
  padding: 4px 8px;
  font-size: 12px;
  border-radius: 3px;
  transition: all 0.3s ease;
}

.action-buttons .el-button:hover {
  transform: translateY(-1px);
}

.action-buttons .el-button:active {
  transform: scale(0.95);
}

/* 分页样式 */
.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .equipment-management {
    padding: 10px;
  }
  
  .toolbar {
    flex-direction: column;
    gap: 8px;
  }
  
  .toolbar .el-button {
    width: 100%;
    min-height: 44px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 4px;
  }
  
  .action-buttons .el-button {
    width: 100%;
    min-height: 44px;
    padding: 6px 10px;
    font-size: 14px;
  }
  
  .statistics-cards .el-col {
    margin-bottom: 10px;
  }
  
  .search-form .el-form-item {
    width: 100%;
  }
  
  .search-form .el-form-item .el-input,
  .search-form .el-form-item .el-select {
    width: 100%;
  }
}

@media (max-width: 1024px) and (min-width: 769px) {
  .action-buttons .el-button {
    padding: 3px 6px;
    font-size: 11px;
  }
}
</style>
