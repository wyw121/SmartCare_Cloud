<template>
  <div class="equipment-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h2>设备管理</h2>
        <p>智慧医养设备接入与管理中心</p>
      </div>
    </div>

    <!-- 功能工具栏 -->
    <div class="toolbar">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-form inline>
            <el-form-item label="设备类型">
              <el-select v-model="searchForm.deviceType" placeholder="请选择设备类型" clearable @change="handleSearch">
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
              <el-select v-model="searchForm.status" placeholder="请选择设备状态" clearable @change="handleSearch">
                <el-option label="在线" value="ONLINE" />
                <el-option label="离线" value="OFFLINE" />
                <el-option label="故障" value="FAULT" />
                <el-option label="维护中" value="MAINTENANCE" />
              </el-select>
            </el-form-item>
            <el-form-item label="搜索">
              <el-input
                v-model="searchForm.keyword"
                placeholder="请输入设备名称、编号或品牌"
                style="width: 250px"
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
        </el-col>
        <el-col :span="8" style="text-align: right">
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加设备
          </el-button>
          <el-button type="success" @click="showIntegrationGuide">
            <el-icon><Document /></el-icon>
            接入指南
          </el-button>
          <el-button type="info" @click="exportData">
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon online">
                <el-icon><Connection /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.onlineCount || 0 }}</div>
                <div class="stat-label">在线设备</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon total">
                <el-icon><Monitor /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
                <div class="stat-label">设备总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon fault">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.faultCount || 0 }}</div>
                <div class="stat-label">故障设备</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon rate">
                <el-icon><Checked /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ onlineRate }}%</div>
                <div class="stat-label">在线率</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 设备列表表格 -->
    <div class="table-container">
      <el-table 
        :data="equipmentList" 
        v-loading="loading"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="deviceId" label="设备编号" width="120" />
        <el-table-column prop="deviceName" label="设备名称" width="150" />
        <el-table-column label="设备类型" width="120">
          <template #default="scope">
            <el-tag :type="getDeviceTypeTagType(scope.row.deviceType)">
              {{ getDeviceTypeName(scope.row.deviceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="brand" label="品牌" width="100" />
        <el-table-column prop="model" label="型号" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="位置" width="150" />
        <el-table-column label="电池电量" width="100">
          <template #default="scope">
            <div v-if="scope.row.batteryLevel !== null">
              <el-progress 
                :percentage="scope.row.batteryLevel" 
                :status="getBatteryStatus(scope.row.batteryLevel)"
                :stroke-width="8"
                :show-text="false"
              />
              <span style="margin-left: 8px;">{{ scope.row.batteryLevel }}%</span>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastOnlineTime" label="最后在线" width="150">
          <template #default="scope">
            {{ formatTime(scope.row.lastOnlineTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewDetail(scope.row)">
              <el-icon><View /></el-icon>
              详情
            </el-button>
            <el-button size="small" type="primary" @click="editEquipment(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button 
              size="small" 
              :type="scope.row.status === 'ONLINE' ? 'warning' : 'success'"
              @click="toggleStatus(scope.row)"
            >
              {{ scope.row.status === 'ONLINE' ? '离线' : '上线' }}
            </el-button>
            <el-button size="small" type="danger" @click="deleteEquipment(scope.row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :small="false"
          :disabled="loading"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

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
import { Checked, Connection, Delete, Document, Download, Edit, Monitor, Plus, Refresh, Search, View, Warning } from '@element-plus/icons-vue'
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
    ElMessage.error('获取设备列表失败')
  } finally {
    loading.value = false
  }
}

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const response = await equipment.getOnlineStatistics()
    if (response.code === 200) {
      statistics.value = response.data || {}
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
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

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  padding: 30px;
  margin-bottom: 20px;
  color: white;
}

.header-content h2 {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
}

.header-content p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.toolbar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.stat-card {
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  margin-right: 15px;
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

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

:deep(.el-table th) {
  background-color: #f8f9fa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}
</style>
