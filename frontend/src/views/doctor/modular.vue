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
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    const mockData = {
      records: [
        {
          id: 1,
          employeeNumber: 'D001',
          name: '张医生',
          gender: '男',
          age: 45,
          phone: '13800138001',
          email: 'zhang@hospital.com',
          department: '心血管科',
          title: '主任医师',
          specialization: '心血管疾病诊治，心脏介入手术',
          education: '医学博士',
          workYears: 20,
          status: 1,
          createTime: '2024-01-01 09:00:00',
          address: '北京市朝阳区',
          introduction: '从事心血管疾病诊治20年，擅长冠心病、高血压等疾病的治疗'
        },
        {
          id: 2,
          employeeNumber: 'D002',
          name: '李医生',
          gender: '女',
          age: 38,
          phone: '13800138002',
          email: 'li@hospital.com',
          department: '内分泌科',
          title: '副主任医师',
          specialization: '糖尿病、甲状腺疾病',
          education: '医学硕士',
          workYears: 15,
          status: 1,
          createTime: '2024-01-02 10:00:00',
          address: '北京市海淀区',
          introduction: '专注内分泌疾病诊治，在糖尿病管理方面有丰富经验'
        }
      ],
      total: 2
    }
    
    doctorList.value = mockData.records
    pageParams.total = mockData.total
  } catch (error) {
    ElMessage.error('获取医生列表失败')
    console.error(error)
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
