<!-- 模块化老人档案列表页面 -->
<template>
  <div class="elderly-list-container">
    <!-- 页面头部 -->
    <PageHeader
      title="老人档案管理"
      subtitle="管理老人基本信息、健康状况、照护等级等档案数据"
      :icon="User"
      :breadcrumb="breadcrumb"
    />

    <!-- 搜索区域 -->
    <ElderlySearchSection
      v-model="searchParams"
      @search="handleSearch"
      @reset="handleReset"
      @export="handleExport"
    />

    <!-- 表格区域 -->
    <ElderlyTableSection
      :data="tableData"
      :loading="tableLoading"
      :pagination="pagination"
      @add="handleAdd"
      @edit="handleEdit"
      @view="handleView"
      @delete="handleDelete"
      @batch-delete="handleBatchDelete"
      @health-record="handleHealthRecord"
      @more-action="handleMoreAction"
      @refresh="handleRefresh"
      @page-change="handlePageChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script setup>
import { User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

// 组件导入
import PageHeader from '@/components/common/PageHeader.vue'
import ElderlySearchSection from '@/components/elderly/ElderlySearchSection.vue'
import ElderlyTableSection from '@/components/elderly/ElderlyTableSection.vue'

// API导入
import {
    batchDeleteElderly,
    deleteElderly,
    exportElderlyData,
    getElderlyList
} from '@/api/elderly'

const router = useRouter()

// 响应式数据
const tableLoading = ref(false)
const tableData = ref([])
const searchParams = ref({})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 面包屑导航
const breadcrumb = ref([
  { title: '首页', path: '/dashboard' },
  { title: '老人管理', path: '/elderly' },
  { title: '档案列表', path: '/elderly/list' }
])

// 加载数据
const loadData = async () => {
  tableLoading.value = true
  try {
    const params = {
      ...searchParams.value,
      page: pagination.current,
      size: pagination.size
    }
    
    const response = await getElderlyList(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('Failed to load elderly data:', error)
    // 使用模拟数据
    loadMockData()
  } finally {
    tableLoading.value = false
  }
}

// 加载模拟数据
const loadMockData = () => {
  tableData.value = [
    {
      id: 1,
      name: '张明',
      gender: 'MALE',
      birthDate: '1950-01-01',
      phone: '13800138001',
      healthStatus: 'HEALTHY',
      emergencyContact: '张小明',
      emergencyPhone: '13900139001',
      address: '北京市朝阳区幸福路123号',
      createdTime: '2024-01-15 10:30:00'
    },
    {
      id: 2,
      name: '李芳',
      gender: 'FEMALE',
      birthDate: '1955-05-05',
      phone: '13800138002',
      healthStatus: 'ATTENTION',
      emergencyContact: '李大明',
      emergencyPhone: '13900139002',
      address: '北京市海淀区康乐街456号',
      createdTime: '2024-01-16 14:20:00'
    },
    {
      id: 3,
      name: '王德',
      gender: 'MALE',
      birthDate: '1948-01-01',
      phone: '13800138003',
      healthStatus: 'CRITICAL',
      emergencyContact: '王小德',
      emergencyPhone: '13900139003',
      address: '北京市西城区安康路789号',
      createdTime: '2024-01-17 09:15:00'
    }
  ]
  pagination.total = 100
}

// 搜索处理
const handleSearch = (params) => {
  searchParams.value = params
  pagination.current = 1
  loadData()
}

// 重置处理
const handleReset = () => {
  searchParams.value = {}
  pagination.current = 1
  loadData()
}

// 导出处理
const handleExport = async (params) => {
  try {
    ElMessage.info('正在导出数据...')
    await exportElderlyData(params)
    ElMessage.success('数据导出完成')
  } catch (error) {
    console.error('Export failed:', error)
    ElMessage.error('数据导出失败')
  }
}

// 新增处理
const handleAdd = () => {
  router.push('/elderly/add')
}

// 编辑处理
const handleEdit = (row) => {
  router.push(`/elderly/edit/${row.id}`)
}

// 查看处理
const handleView = (row) => {
  router.push(`/elderly/profile/${row.id}`)
}

// 删除处理
const handleDelete = async (row) => {
  try {
    await deleteElderly(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Delete failed:', error)
    ElMessage.error('删除失败')
  }
}

// 批量删除处理
const handleBatchDelete = async (rows) => {
  try {
    const ids = rows.map(row => row.id)
    await batchDeleteElderly(ids)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    console.error('Batch delete failed:', error)
    ElMessage.error('批量删除失败')
  }
}

// 健康记录处理
const handleHealthRecord = (row) => {
  router.push(`/health/records?elderlyId=${row.id}`)
}

// 更多操作处理
const handleMoreAction = ({ command, row }) => {
  const actionMap = {
    family: () => router.push(`/elderly/family/${row.id}`),
    device: () => router.push(`/elderly/device/${row.id}`),
    care: () => router.push(`/elderly/care/${row.id}`),
    delete: () => handleDelete(row)
  }
  
  const action = actionMap[command]
  if (action) {
    action()
  }
}

// 刷新处理
const handleRefresh = () => {
  loadData()
}

// 分页处理
const handlePageChange = (page) => {
  pagination.current = page
  loadData()
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  loadData()
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.elderly-list-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}
</style>
