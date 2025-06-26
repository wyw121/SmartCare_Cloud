<template>
  <div class="doctor-container">
    <PageHeader 
      title="医生管理"
      subtitle="管理医院医生信息、科室分配和工作状态"
    />

    <!-- 搜索区域 -->
    <SearchSection 
      @search="handleSearch"
      @reset="handleReset"
    />

    <!-- 医生表格 -->
    <TableSection
      :doctor-list="doctorList"
      :loading="loading"
      :pagination="pageParams"
      @add="handleAddDoctor"
      @edit="handleEditDoctor"
      @delete="handleDeleteDoctor"
      @view="handleViewDoctor"
      @batch-delete="handleBatchDelete"
      @export="handleExport"
      @refresh="getDoctorList"
      @page-change="handlePageChange"
      @page-size-change="handlePageSizeChange"
    />

    <!-- 医生表单对话框 -->
    <DoctorFormDialog
      v-model:visible="dialogVisible"
      :is-edit="isEdit"
      :doctor-data="currentDoctor"
      @submit="handleSubmitDoctor"
    />

    <!-- 医生详情对话框 -->
    <DoctorDetailDialog
      v-model:visible="detailDialogVisible"
      :doctor-data="currentDoctor"
    />
  </div>
</template>

<script setup>
import { getDoctorPageList } from '@/api/doctor'
import PageHeader from '@/components/common/PageHeader.vue'
import DoctorDetailDialog from '@/components/doctor/DetailDialog.vue'
import DoctorFormDialog from '@/components/doctor/FormDialog.vue'
import SearchSection from '@/components/doctor/SearchSection.vue'
import TableSection from '@/components/doctor/TableSection.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const isEdit = ref(false)
const doctorList = ref([])
const currentDoctor = ref({})

// 分页参数
const pageParams = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 搜索参数
const searchParams = reactive({
  name: '',
  employeeNumber: '',
  department: '',
  title: '',
  status: ''
})

onMounted(() => {
  getDoctorList()
})

/**
 * 获取医生列表
 */
const getDoctorList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageParams.current,
      pageSize: pageParams.size,
      ...searchParams
    }
    
    const response = await getDoctorPageList(params)
    
    if (response.code === 200) {
      doctorList.value = response.data.list || []
      pageParams.total = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取医生列表失败')
      doctorList.value = []
      pageParams.total = 0
    }
  } catch (error) {
    ElMessage.error('获取医生列表失败: ' + (error.message || '网络错误'))
    console.error('获取医生列表失败:', error)
    
    // 错误时显示空数据
    doctorList.value = []
    pageParams.total = 0
  } finally {
    loading.value = false
  }
}

/**
 * 搜索医生
 */
const handleSearch = (params) => {
  Object.assign(searchParams, params)
  pageParams.current = 1
  getDoctorList()
}

/**
 * 重置搜索
 */
const handleReset = () => {
  Object.assign(searchParams, {
    name: '',
    employeeNumber: '',
    department: '',
    title: '',
    status: ''
  })
  pageParams.current = 1
  getDoctorList()
}

/**
 * 新增医生
 */
const handleAddDoctor = () => {
  isEdit.value = false
  currentDoctor.value = {}
  dialogVisible.value = true
}

/**
 * 编辑医生
 */
const handleEditDoctor = (row) => {
  isEdit.value = true
  currentDoctor.value = { ...row }
  dialogVisible.value = true
}

/**
 * 查看医生详情
 */
const handleViewDoctor = (row) => {
  currentDoctor.value = row
  detailDialogVisible.value = true
}

/**
 * 删除医生
 */
const handleDeleteDoctor = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该医生吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟删除API
    await new Promise(resolve => setTimeout(resolve, 500))
    ElMessage.success('删除成功')
    getDoctorList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 批量删除医生
 */
const handleBatchDelete = async (selectedDoctors) => {
  if (!selectedDoctors.length) {
    ElMessage.warning('请选择要删除的医生')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedDoctors.length} 位医生吗？`, '批量删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟批量删除API
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('批量删除成功')
    getDoctorList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
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
 * 提交医生数据
 */
const handleSubmitDoctor = async (doctorData) => {
  try {
    // 模拟提交API
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    dialogVisible.value = false
    getDoctorList()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  }
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  pageParams.current = page
  getDoctorList()
}

/**
 * 页面大小变化
 */
const handlePageSizeChange = (size) => {
  pageParams.size = size
  pageParams.current = 1
  getDoctorList()
}
</script>

<style scoped>
.doctor-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}
</style>
