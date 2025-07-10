<!-- 这是管理员和医生专用的健康预警管理页面 -->
<template>
  <div class="health-warning">
    <!-- 页面头部 -->
    <el-card class="page-header">
      <div class="header-content">
        <h2>健康预警管理</h2>
        <p>智能监测老人健康状况，及时发现异常情况并提供预警提醒</p>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistic-card urgent">
          <div class="statistic-content">
            <div class="statistic-number">{{ statistics.urgent }}</div>
            <div class="statistic-label">紧急预警</div>
          </div>
          <el-icon class="statistic-icon"><Warning /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card high">
          <div class="statistic-content">
            <div class="statistic-number">{{ statistics.high }}</div>
            <div class="statistic-label">高风险</div>
          </div>
          <el-icon class="statistic-icon"><Warning /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card medium">
          <div class="statistic-content">
            <div class="statistic-number">{{ statistics.medium }}</div>
            <div class="statistic-label">中风险</div>
          </div>
          <el-icon class="statistic-icon"><InfoFilled /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card low">
          <div class="statistic-content">
            <div class="statistic-number">{{ statistics.low }}</div>
            <div class="statistic-label">低风险</div>
          </div>
          <el-icon class="statistic-icon"><CircleCheck /></el-icon>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="searchForm" ref="searchFormRef" :inline="true" label-width="70px" class="compact-search-form">
        <el-form-item label="姓名" prop="elderlyName">
          <el-input 
            v-model="searchForm.elderlyName" 
            placeholder="老人姓名" 
            clearable 
            style="width: 130px"
          />
        </el-form-item>
        <el-form-item label="类型" prop="warningType">
          <el-select 
            v-model="searchForm.warningType" 
            placeholder="预警类型" 
            clearable
            multiple
            collapse-tags
            collapse-tags-tooltip
            style="width: 150px"
          >
            <el-option label="血压异常" value="血压异常" />
            <el-option label="血糖异常" value="血糖异常" />
            <el-option label="心率异常" value="心率异常" />
            <el-option label="体温异常" value="体温异常" />
            <el-option label="长期不活动" value="长期不活动" />
            <el-option label="药物未按时服用" value="药物未按时服用" />
            <el-option label="跌倒风险" value="跌倒风险" />
            <el-option label="其他异常" value="其他异常" />
          </el-select>
        </el-form-item>
        <el-form-item label="级别" prop="warningLevel">
          <el-select 
            v-model="searchForm.warningLevel" 
            placeholder="预警级别" 
            clearable
            multiple
            collapse-tags
            collapse-tags-tooltip
            style="width: 130px"
          >
            <el-option label="低风险" :value="1" />
            <el-option label="中风险" :value="2" />
            <el-option label="高风险" :value="3" />
            <el-option label="紧急" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select 
            v-model="searchForm.status" 
            placeholder="处理状态" 
            clearable
            multiple
            collapse-tags
            collapse-tags-tooltip
            style="width: 120px"
          >
            <el-option label="未处理" :value="0" />
            <el-option label="已查看" :value="1" />
            <el-option label="处理中" :value="2" />
            <el-option label="已处理" :value="3" />
            <el-option label="已忽略" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间" prop="triggerTime">
          <el-date-picker
            v-model="searchForm.triggerTime"
            type="datetimerange"
            start-placeholder="开始"
            end-placeholder="结束"
            format="MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 280px"
          />
        </el-form-item>
        <el-form-item class="search-buttons">
          <el-button type="primary" @click="handleSearch" :icon="Search" size="default">
            搜索
          </el-button>
          <el-button @click="handleReset" :icon="Refresh" size="default">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card">
      <div class="toolbar">
        <el-button type="success" @click="handleBatchHandle" :disabled="multipleSelection.length === 0">
          <el-icon><Select /></el-icon>
          批量处理
        </el-button>
        <el-button type="warning" @click="handleBatchIgnore" :disabled="multipleSelection.length === 0">
          <el-icon><Hide /></el-icon>
          批量忽略
        </el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="multipleSelection.length === 0">
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
        <el-button type="info" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
        <el-button type="primary" @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="elderlyName" label="老人姓名" min-width="100" />
        <el-table-column prop="warningType" label="预警类型" min-width="120" />
        <el-table-column prop="warningLevel" label="预警级别" width="100">
          <template #default="scope">
            <el-tag :type="getWarningLevelType(scope.row.warningLevel)">
              {{ getWarningLevelText(scope.row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="预警标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="triggerData" label="触发数据" min-width="150" show-overflow-tooltip />
        <el-table-column prop="dataSource" label="数据来源" width="120" />
        <el-table-column prop="status" label="处理状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handlerName" label="处理人" width="100" />
        <el-table-column prop="triggerTime" label="触发时间" width="180" />
        <el-table-column label="操作" width="320" fixed="right" align="center">
          <template #default="scope">
            <div class="action-buttons-grid">
              <!-- 第一列：查看（始终存在） -->
              <div class="action-slot">
                <el-button 
                  text 
                  type="primary" 
                  size="small" 
                  @click="handleView(scope.row)"
                >
                  查看
                </el-button>
              </div>
              
              <!-- 第二列：处理（条件显示） -->
              <div class="action-slot">
                <el-button 
                  v-if="scope.row.status === 0 || scope.row.status === 1"
                  text 
                  type="success" 
                  size="small" 
                  @click="handleProcess(scope.row)"
                >
                  处理
                </el-button>
              </div>
              
              <!-- 第三列：忽略（条件显示） -->
              <div class="action-slot">
                <el-button 
                  v-if="scope.row.status === 0"
                  text 
                  type="warning" 
                  size="small" 
                  @click="handleIgnore(scope.row)"
                >
                  忽略
                </el-button>
              </div>
              
              <!-- 第四列：删除（始终存在） -->
              <div class="action-slot">
                <el-button 
                  text 
                  type="danger" 
                  size="small" 
                  @click="handleDelete(scope.row)"
                >
                  删除
                </el-button>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageInfo.pageNum"
          v-model:page-size="pageInfo.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pageInfo.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog
      title="预警详情"
      v-model="detailDialogVisible"
      width="800px"
    >
      <div v-if="currentWarning" class="warning-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="老人姓名">{{ currentWarning.elderlyName }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">{{ currentWarning.warningType }}</el-descriptions-item>
          <el-descriptions-item label="预警级别">
            <el-tag :type="getWarningLevelType(currentWarning.warningLevel)">
              {{ getWarningLevelText(currentWarning.warningLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="数据来源">{{ currentWarning.dataSource }}</el-descriptions-item>
          <el-descriptions-item label="触发时间">{{ currentWarning.triggerTime }}</el-descriptions-item>
          <el-descriptions-item label="处理状态">
            <el-tag :type="getStatusType(currentWarning.status)">
              {{ getStatusText(currentWarning.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理人">{{ currentWarning.handlerName || '暂无' }}</el-descriptions-item>
          <el-descriptions-item label="处理时间">{{ currentWarning.handleTime || '暂无' }}</el-descriptions-item>
        </el-descriptions>
        <el-divider>预警内容</el-divider>
        <div class="warning-content">
          <h4>{{ currentWarning.title }}</h4>
          <p>{{ currentWarning.content }}</p>
        </div>
        <el-divider>触发数据</el-divider>
        <div class="trigger-data">
          <p>{{ currentWarning.triggerData }}</p>
        </div>
        <el-divider v-if="currentWarning.handleRemark">处理记录</el-divider>
        <div v-if="currentWarning.handleRemark" class="handle-remark">
          <p>{{ currentWarning.handleRemark }}</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 处理预警对话框 -->
    <el-dialog
      title="处理预警"
      v-model="processDialogVisible"
      width="500px"
    >
      <el-form :model="processForm" :rules="processRules" ref="processFormRef" label-width="80px">
        <el-form-item label="处理备注" prop="handleRemark">
          <el-input
            v-model="processForm.handleRemark"
            type="textarea"
            :rows="5"
            placeholder="请输入处理情况说明"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleProcessSubmit" :loading="processLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
    deleteHealthWarning,
    deleteHealthWarningBatch,
    getHealthWarningPageList,
    getWarningLevelStatistics,
    handleHealthWarning,
    updateHealthWarningStatus
} from '@/api/healthWarning'
import {
    CircleCheck,
    Delete,
    Download,
    Hide,
    InfoFilled,
    Refresh,
    Search,
    Select,
    Warning
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, onUnmounted, reactive, ref } from 'vue'

// 响应式数据
const tableLoading = ref(false)
const processLoading = ref(false)
const detailDialogVisible = ref(false)
const processDialogVisible = ref(false)
const tableData = ref([])
const multipleSelection = ref([])
const currentWarning = ref(null)
const currentProcessId = ref(null)

// 统计数据
const statistics = reactive({
  urgent: 0,
  high: 0,
  medium: 0,
  low: 0
})

// 搜索表单
const searchForm = reactive({
  elderlyName: '',
  warningType: [],
  warningLevel: [],
  status: [],
  triggerTime: []
})

// 分页信息
const pageInfo = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 处理表单
const processForm = reactive({
  handleRemark: ''
})

// 处理表单验证规则
const processRules = {
  handleRemark: [
    { required: true, message: '请输入处理备注', trigger: 'blur' },
    { min: 10, message: '处理备注至少10个字符', trigger: 'blur' }
  ]
}

// 引用
const searchFormRef = ref()
const processFormRef = ref()

// 获取预警级别类型
const getWarningLevelType = (level) => {
  const types = {
    1: 'info',
    2: 'warning',
    3: 'danger',
    4: 'danger'
  }
  return types[level] || 'info'
}

// 获取预警级别文本
const getWarningLevelText = (level) => {
  const texts = {
    1: '低风险',
    2: '中风险',
    3: '高风险',
    4: '紧急'
  }
  return texts[level] || '未知'
}

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    0: 'danger',
    1: 'warning',
    2: 'primary',
    3: 'success',
    4: 'info'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    0: '未处理',
    1: '已查看',
    2: '处理中',
    3: '已处理',
    4: '已忽略'
  }
  return texts[status] || '未知'
}

// 获取统计数据
const getStatistics = async () => {
  try {
    const response = await getWarningLevelStatistics()
    
    if (response.code === 200) {
      const stats = response.data || []
      statistics.urgent = stats.find(item => item.level === 4)?.count || 0
      statistics.high = stats.find(item => item.level === 3)?.count || 0
      statistics.medium = stats.find(item => item.level === 2)?.count || 0
      statistics.low = stats.find(item => item.level === 1)?.count || 0
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 处理搜索参数
const processSearchParams = (form) => {
  const params = {
    ...form,
    pageNum: pageInfo.pageNum,
    pageSize: pageInfo.pageSize
  }
  
  // 处理时间范围
  if (form.triggerTime && form.triggerTime.length === 2) {
    params.startTime = form.triggerTime[0]
    params.endTime = form.triggerTime[1]
  }
  delete params.triggerTime
  
  return params
}

// 处理多选参数
const processMultiSelectParams = (params) => {
  const processParam = (key) => {
    if (Array.isArray(params[key])) {
      if (params[key].length > 0) {
        params[key] = params[key].join(',')
      } else {
        delete params[key]
      }
    } else if (!params[key] || params[key] === '') {
      delete params[key]
    }
  }
  
  processParam('warningType')
  processParam('warningLevel')
  processParam('status')
  
  // 清理空字符串参数
  if (!params.elderlyName || params.elderlyName.trim() === '') {
    delete params.elderlyName
  }
  
  return params
}

// 获取预警列表
const getList = async () => {
  tableLoading.value = true
  try {
    let params = processSearchParams(searchForm)
    params = processMultiSelectParams(params)
    
    console.log('管理员查询预警参数:', params)
    
    const response = await getHealthWarningPageList(params)
    
    if (response.code === 200) {
      tableData.value = response.data.list || []
      pageInfo.total = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取预警列表失败')
    }
  } catch (error) {
    console.error('获取预警列表失败:', error)
    ElMessage.error('获取预警列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pageInfo.pageNum = 1
  getList()
}

// 重置
const handleReset = () => {
  searchFormRef.value?.resetFields()
  pageInfo.pageNum = 1
  getList()
}

// 查看详情
const handleView = (row) => {
  currentWarning.value = row
  detailDialogVisible.value = true
  
  // 如果是未处理状态，标记为已查看
  if (row.status === 0) {
    updateHealthWarningStatus(row.id, 1)
    row.status = 1
  }
}

// 处理预警
const handleProcess = (row) => {
  currentProcessId.value = row.id
  processForm.handleRemark = ''
  processDialogVisible.value = true
}

// 提交处理
const handleProcessSubmit = async () => {
  try {
    await processFormRef.value?.validate()
    
    processLoading.value = true
    const { data } = await handleHealthWarning(currentProcessId.value, {
      status: 3,
      handleRemark: processForm.handleRemark
    })
    
    if (data.code === 200) {
      ElMessage.success('处理成功')
      processDialogVisible.value = false
      getList()
      getStatistics()
    } else {
      ElMessage.error(data.message || '处理失败')
    }
  } catch (error) {
    console.error('处理失败:', error)
    ElMessage.error('处理失败')
  } finally {
    processLoading.value = false
  }
}

// 忽略预警
const handleIgnore = async (row) => {
  try {
    await ElMessageBox.confirm('确定要忽略该预警吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const { data } = await updateHealthWarningStatus(row.id, 4)
    if (data.code === 200) {
      ElMessage.success('操作成功')
      getList()
      getStatistics()
    } else {
      ElMessage.error(data.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 删除预警
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该预警吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const { data } = await deleteHealthWarning(row.id)
    if (data.code === 200) {
      ElMessage.success('删除成功')
      getList()
      getStatistics()
    } else {
      ElMessage.error(data.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量处理
const handleBatchHandle = () => {
  ElMessage.info('批量处理功能待实现')
}

// 批量忽略
const handleBatchIgnore = () => {
  ElMessage.info('批量忽略功能待实现')
}

// 批量删除
const handleBatchDelete = async () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${multipleSelection.value.length} 条记录吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = multipleSelection.value.map(item => item.id)
    const { data } = await deleteHealthWarningBatch(ids)
    if (data.code === 200) {
      ElMessage.success('批量删除成功')
      getList()
      getStatistics()
    } else {
      ElMessage.error(data.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能待实现')
}

// 刷新数据
const handleRefresh = () => {
  getList()
  getStatistics()
  ElMessage.success('数据已刷新')
}

// 多选
const handleSelectionChange = (selection) => {
  multipleSelection.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pageInfo.pageSize = size
  pageInfo.pageNum = 1
  getList()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pageInfo.pageNum = page
  getList()
}

// 初始化
onMounted(() => {
  console.log('🚀 [HealthWarning] 管理员健康预警页面挂载完成')
  getList()
  getStatistics()
})

onUnmounted(() => {
  console.log('💀 [HealthWarning] 管理员健康预警页面卸载')
})
</script>

<style scoped>
.health-warning {
  padding: 20px;
}

/* 页面头部样式 */
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: none;
}

.page-header .header-content {
  padding: 20px;
}

.page-header .header-content h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: white;
}

.page-header .header-content p {
  margin: 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  line-height: 1.5;
}

.statistics-row {
  margin-bottom: 20px;
}

.statistic-card {
  position: relative;
  overflow: hidden;
}

.statistic-card.urgent {
  border-left: 4px solid #f56c6c;
}

.statistic-card.high {
  border-left: 4px solid #e6a23c;
}

.statistic-card.medium {
  border-left: 4px solid #409eff;
}

.statistic-card.low {
  border-left: 4px solid #67c23a;
}

.statistic-content {
  text-align: center;
}

.statistic-number {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.statistic-label {
  font-size: 14px;
  color: #909399;
}

.statistic-icon {
  position: absolute;
  top: 10px;
  right: 15px;
  font-size: 24px;
  opacity: 0.3;
}

/* 工具栏样式 */
.toolbar-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.search-card,
.toolbar-card,
.table-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.warning-detail {
  padding: 20px 0;
}

.warning-content h4 {
  margin: 0 0 10px 0;
  color: #303133;
}

.warning-content p,
.trigger-data p,
.handle-remark p {
  margin: 0;
  line-height: 1.6;
  color: #606266;
}

/* 紧凑搜索表单样式 */
.compact-search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 16px;
}

.compact-search-form .el-form-item {
  margin-bottom: 0;
  margin-right: 0;
}

.compact-search-form .el-form-item__label {
  max-width: 60px;
  font-size: 14px;
  font-weight: normal;
  padding-right: 8px;
}

.compact-search-form .el-form-item__content {
  margin-left: 0 !important;
}

/* 搜索按钮组样式 */
.search-buttons {
  margin-left: auto;
}

.search-buttons .el-form-item__content {
  display: flex;
  gap: 8px;
}

/* 表格操作按钮网格布局 */
.action-buttons-grid {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr; /* 四列等宽布局 */
  gap: 4px;
  align-items: center;
  padding: 4px 0;
  min-height: 32px;
}

/* 按钮插槽容器 */
.action-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 32px;
}

/* 网格布局中的按钮样式 */
.action-buttons-grid .el-button {
  margin: 0;
  padding: 3px 6px;
  font-size: 12px;
  border-radius: 4px;
  transition: all 0.3s ease;
  min-width: 50px;
  white-space: nowrap;
  flex-shrink: 0;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
