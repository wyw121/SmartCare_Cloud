<template>
  <div class="health-warning-container">
    <PageHeader 
      title="健康预警"
      subtitle="实时监控老人健康状况，及时发现风险并预警"
    />

    <!-- 统计卡片 -->
    <StatisticsSection :statistics="statistics" />

    <!-- 搜索区域 -->
    <SearchSection 
      @search="handleSearch"
      @reset="handleReset"
    />

    <!-- 预警表格 -->
    <TableSection
      :warning-list="warningList"
      :loading="loading"
      :pagination="pageParams"
      @view="handleViewWarning"
      @process="handleProcessWarning"
      @ignore="handleIgnoreWarning"
      @batch-handle="handleBatchHandle"
      @batch-ignore="handleBatchIgnore"
      @export="handleExport"
      @refresh="getWarningList"
      @page-change="handlePageChange"
      @page-size-change="handlePageSizeChange"
    />

    <!-- 预警详情对话框 -->
    <WarningDetailDialog
      v-model:visible="detailDialogVisible"
      :warning-data="currentWarning"
    />

    <!-- 处理预警对话框 -->
    <ProcessWarningDialog
      v-model:visible="processDialogVisible"
      :warning-data="currentWarning"
      @submit="handleSubmitProcess"
    />
  </div>
</template>

<script setup>
import PageHeader from '@/components/common/PageHeader.vue'
import ProcessWarningDialog from '@/components/health-warning/ProcessWarningDialog.vue'
import SearchSection from '@/components/health-warning/SearchSection.vue'
import StatisticsSection from '@/components/health-warning/StatisticsSection.vue'
import TableSection from '@/components/health-warning/TableSection.vue'
import WarningDetailDialog from '@/components/health-warning/WarningDetailDialog.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

// 响应式数据
const loading = ref(false)
const detailDialogVisible = ref(false)
const processDialogVisible = ref(false)
const warningList = ref([])
const currentWarning = ref({})

// 统计数据
const statistics = ref({
  urgent: 5,
  high: 12,
  medium: 23,
  low: 45
})

// 分页参数
const pageParams = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 搜索参数
const searchParams = reactive({
  elderlyName: '',
  warningType: '',
  warningLevel: null,
  status: null,
  triggerTime: []
})

onMounted(() => {
  getWarningList()
  getStatistics()
})

/**
 * 获取预警统计
 */
const getStatistics = async () => {
  try {
    // 模拟API调用
    const mockStats = {
      urgent: 5,
      high: 12,
      medium: 23,
      low: 45
    }
    statistics.value = mockStats
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

/**
 * 获取预警列表
 */
const getWarningList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    const mockData = {
      records: [
        {
          id: 1,
          elderlyName: '张三',
          warningType: '血压异常',
          warningLevel: 3,
          warningContent: '血压持续偏高，收缩压达到160mmHg，建议立即就医',
          triggerTime: '2024-01-15 14:30:00',
          status: 0,
          handlePerson: '',
          handleTime: '',
          elderlyAge: 75,
          elderlyPhone: '13800138001'
        },
        {
          id: 2,
          elderlyName: '李四',
          warningType: '心率异常',
          warningLevel: 2,
          warningContent: '心率不规律，出现间歇性过缓现象',
          triggerTime: '2024-01-15 13:45:00',
          status: 1,
          handlePerson: '王医生',
          handleTime: '2024-01-15 14:00:00',
          elderlyAge: 68,
          elderlyPhone: '13800138002'
        }
      ],
      total: 2
    }
    
    warningList.value = mockData.records
    pageParams.total = mockData.total
  } catch (error) {
    ElMessage.error('获取预警列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 搜索预警
 */
const handleSearch = (params) => {
  Object.assign(searchParams, params)
  pageParams.current = 1
  getWarningList()
}

/**
 * 重置搜索
 */
const handleReset = () => {
  Object.assign(searchParams, {
    elderlyName: '',
    warningType: '',
    warningLevel: null,
    status: null,
    triggerTime: []
  })
  pageParams.current = 1
  getWarningList()
}

/**
 * 查看预警详情
 */
const handleViewWarning = (row) => {
  currentWarning.value = row
  detailDialogVisible.value = true
}

/**
 * 处理预警
 */
const handleProcessWarning = (row) => {
  currentWarning.value = row
  processDialogVisible.value = true
}

/**
 * 忽略预警
 */
const handleIgnoreWarning = async (row) => {
  try {
    await ElMessageBox.confirm('确定要忽略这条预警吗？', '确认忽略', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    ElMessage.success('预警已忽略')
    getWarningList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('忽略失败')
    }
  }
}

/**
 * 批量处理
 */
const handleBatchHandle = async (selectedWarnings) => {
  if (!selectedWarnings.length) {
    ElMessage.warning('请选择要处理的预警')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要批量处理选中的 ${selectedWarnings.length} 条预警吗？`, '批量处理确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('批量处理成功')
    getWarningList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量处理失败')
    }
  }
}

/**
 * 批量忽略
 */
const handleBatchIgnore = async (selectedWarnings) => {
  if (!selectedWarnings.length) {
    ElMessage.warning('请选择要忽略的预警')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要批量忽略选中的 ${selectedWarnings.length} 条预警吗？`, '批量忽略确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('批量忽略成功')
    getWarningList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量忽略失败')
    }
  }
}

/**
 * 导出数据
 */
const handleExport = () => {
  ElMessage.success('导出功能开发中...')
}

/**
 * 提交处理结果
 */
const handleSubmitProcess = async (processData) => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('处理成功')
    processDialogVisible.value = false
    getWarningList()
  } catch (error) {
    ElMessage.error('处理失败')
  }
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  pageParams.current = page
  getWarningList()
}

/**
 * 页面大小变化
 */
const handlePageSizeChange = (size) => {
  pageParams.size = size
  pageParams.current = 1
  getWarningList()
}
</script>

<style scoped>
.health-warning-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}
</style>
