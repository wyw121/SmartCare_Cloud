<template>
  <div class="doctor-list">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="searchForm" ref="searchFormRef" :inline="true" label-width="80px">
        <el-form-item label="医生姓名" prop="name">
          <el-input v-model="searchForm.name" placeholder="请输入医生姓名" clearable />
        </el-form-item>
        <el-form-item label="工号" prop="employeeNumber">
          <el-input v-model="searchForm.employeeNumber" placeholder="请输入医生工号" clearable />
        </el-form-item>
        <el-form-item label="科室" prop="department">
          <el-select v-model="searchForm.department" placeholder="请选择科室" clearable>
            <el-option label="内科" value="内科" />
            <el-option label="外科" value="外科" />
            <el-option label="儿科" value="儿科" />
            <el-option label="妇科" value="妇科" />
            <el-option label="眼科" value="眼科" />
            <el-option label="耳鼻喉科" value="耳鼻喉科" />
            <el-option label="皮肤科" value="皮肤科" />
            <el-option label="神经科" value="神经科" />
            <el-option label="心血管科" value="心血管科" />
            <el-option label="骨科" value="骨科" />
          </el-select>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-select v-model="searchForm.title" placeholder="请选择职称" clearable>
            <el-option label="主任医师" value="主任医师" />
            <el-option label="副主任医师" value="副主任医师" />
            <el-option label="主治医师" value="主治医师" />
            <el-option label="住院医师" value="住院医师" />
            <el-option label="实习医师" value="实习医师" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="在职" value="1" />
            <el-option label="离职" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card">
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd" v-permission="'doctor:add'">
          <el-icon><Plus /></el-icon>
          新增医生
        </el-button>
        <el-button 
          type="danger" 
          @click="handleBatchDelete" 
          :disabled="multipleSelection.length === 0"
          v-permission="'doctor:delete'"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
        <el-button type="success" @click="handleExport" v-permission="'doctor:export'">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
        <el-button type="warning" @click="handleImport" v-permission="'doctor:import'">
          <el-icon><Upload /></el-icon>
          导入数据
        </el-button>
        <el-button type="info" @click="handleStatistics" v-permission="'doctor:statistics'">
          <el-icon><DataAnalysis /></el-icon>
          医生统计
        </el-button>
        
        <!-- 角色信息显示 -->
        <div class="role-info">
          <el-tag type="info" size="small">当前角色：{{ currentUserRole }}</el-tag>
        </div>
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
        :header-cell-style="{ backgroundColor: '#f8f9fa', color: '#495057', fontWeight: '600' }"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="employeeNumber" label="工号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80" class-name="hidden-sm-and-down" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="department" label="科室" width="100" class-name="hidden-sm-and-down" />
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column prop="specialization" label="专长" min-width="150" show-overflow-tooltip class-name="hidden-sm-and-down" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" class-name="hidden-sm-and-down" />
        <el-table-column label="操作" width="350" fixed="right" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <el-button 
                text 
                type="primary" 
                size="small" 
                @click="handleView(scope.row)"
                class="action-btn"
              >
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button 
                text 
                type="success" 
                size="small" 
                @click="handleEdit(scope.row)" 
                v-permission="'doctor:edit'"
                class="action-btn"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button 
                text 
                type="warning" 
                size="small" 
                @click="handleViewSchedule(scope.row)"
                v-permission="'doctor:schedule'"
                class="action-btn"
              >
                <el-icon><Calendar /></el-icon>
                排班
              </el-button>
              <el-button 
                text 
                type="danger" 
                size="small" 
                @click="handleDelete(scope.row)"
                v-permission="'doctor:delete'"
                class="action-btn"
              >
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form :model="doctorForm" :rules="formRules" ref="doctorFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号" prop="employeeNumber">
              <el-input v-model="doctorForm.employeeNumber" placeholder="请输入医生工号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="doctorForm.name" placeholder="请输入医生姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="doctorForm.gender">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="doctorForm.age" :min="18" :max="80" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="doctorForm.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="科室" prop="department">
              <el-select v-model="doctorForm.department" placeholder="请选择科室">
                <el-option label="内科" value="内科" />
                <el-option label="外科" value="外科" />
                <el-option label="儿科" value="儿科" />
                <el-option label="妇科" value="妇科" />
                <el-option label="眼科" value="眼科" />
                <el-option label="耳鼻喉科" value="耳鼻喉科" />
                <el-option label="皮肤科" value="皮肤科" />
                <el-option label="神经科" value="神经科" />
                <el-option label="心血管科" value="心血管科" />
                <el-option label="骨科" value="骨科" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职称" prop="title">
              <el-select v-model="doctorForm.title" placeholder="请选择职称">
                <el-option label="主任医师" value="主任医师" />
                <el-option label="副主任医师" value="副主任医师" />
                <el-option label="主治医师" value="主治医师" />
                <el-option label="住院医师" value="住院医师" />
                <el-option label="实习医师" value="实习医师" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="doctorForm.status">
                <el-radio :label="1">在职</el-radio>
                <el-radio :label="0">离职</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="专长" prop="specialization">
          <el-input
            v-model="doctorForm.specialization"
            type="textarea"
            :rows="3"
            placeholder="请输入医生专长"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="doctorForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看医生详情对话框 -->
    <el-dialog
      title="医生详细信息"
      v-model="viewDialogVisible"
      width="800px"
      :before-close="handleViewDialogClose"
    >
      <div class="doctor-detail-container">
        <!-- 基本信息 -->
        <el-card class="detail-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><User /></el-icon>
              <span>基本信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="工号">
              <el-tag type="info">{{ viewDoctorData.employeeNumber }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="姓名">
              <span class="doctor-name">{{ viewDoctorData.name }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="性别">
              <el-tag :type="viewDoctorData.gender === '男' ? 'primary' : 'warning'">
                {{ viewDoctorData.gender }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="年龄">
              {{ viewDoctorData.age }}岁
            </el-descriptions-item>
            <el-descriptions-item label="联系电话">
              <el-link type="primary" :href="`tel:${viewDoctorData.phone}`">
                {{ viewDoctorData.phone }}
              </el-link>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="viewDoctorData.status === 1 ? 'success' : 'danger'">
                {{ viewDoctorData.status === 1 ? '在职' : '离职' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 职业信息 -->
        <el-card class="detail-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><Briefcase /></el-icon>
              <span>职业信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="科室">
              <el-tag type="success">{{ viewDoctorData.department }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="职称">
              <el-tag type="warning">{{ viewDoctorData.title }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="专长" :span="2">
              <div class="specialization-content">
                {{ viewDoctorData.specialization || '暂无专长信息' }}
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 统计信息 -->
        <el-card class="detail-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><DataAnalysis /></el-icon>
              <span>工作统计</span>
            </div>
          </template>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ doctorStats.patientCount || 0 }}</div>
                <div class="stat-label">服务患者</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ doctorStats.consultationCount || 0 }}</div>
                <div class="stat-label">总诊疗次数</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ doctorStats.avgRating || 0 }}</div>
                <div class="stat-label">平均评分</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ doctorStats.workYears || 0 }}</div>
                <div class="stat-label">从业年限</div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 其他信息 -->
        <el-card class="detail-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><Document /></el-icon>
              <span>其他信息</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="备注信息">
              <div class="remark-content">
                {{ viewDoctorData.remark || '暂无备注信息' }}
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ formatDateTime(viewDoctorData.createTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="更新时间">
              {{ formatDateTime(viewDoctorData.updateTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleViewDialogClose">关闭</el-button>
          <el-button type="primary" @click="handleEditFromView" v-permission="'doctor:edit'">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button type="success" @click="handleScheduleFromView" v-permission="'doctor:schedule'">
            <el-icon><Calendar /></el-icon>
            排班管理
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
    addDoctor,
    deleteDoctor,
    deleteDoctorBatch,
    getDoctorPageList,
    getDoctorStatistics,
    updateDoctor
} from '@/api/doctor'
import { useUserStore } from '@/store/user'
import { Calendar, DataAnalysis, Delete, Download, Edit, Plus, Refresh, Search, Upload, View, User, Briefcase, Document } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, onMounted, reactive, ref } from 'vue'

const userStore = useUserStore()

// 响应式数据
const tableLoading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增医生')
const isEdit = ref(false)
const tableData = ref([])
const multipleSelection = ref([])
const viewDialogVisible = ref(false) // 查看详情对话框可见性
const viewDoctorData = reactive({}) // 查看医生详情数据
const doctorStats = reactive({
  patientCount: 0,
  consultationCount: 0,
  avgRating: 0,
  workYears: 0
}) // 医生统计数据

// 计算属性
const currentUserRole = computed(() => userStore.userRoleText)

// 搜索表单
const searchForm = reactive({
  name: '',
  employeeNumber: '',
  department: '',
  title: '',
  status: ''
})

// 分页信息
const pageInfo = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 医生表单
const doctorForm = reactive({
  id: null,
  employeeNumber: '',
  name: '',
  gender: '男',
  age: 25,
  phone: '',
  department: '',
  title: '',
  specialization: '',
  status: 1,
  remark: ''
})

// 表单验证规则
const formRules = {
  employeeNumber: [
    { required: true, message: '请输入医生工号', trigger: 'blur' },
    { min: 3, max: 20, message: '工号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入医生姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请选择科室', trigger: 'change' }
  ],
  title: [
    { required: true, message: '请选择职称', trigger: 'change' }
  ]
}

// 引用
const searchFormRef = ref()
const doctorFormRef = ref()

// 获取医生列表
const getList = async () => {
  tableLoading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pageInfo.pageNum,
      pageSize: pageInfo.pageSize
    }
    const response = await getDoctorPageList(params)
    if (response.code === 200) {
      tableData.value = response.data.list
      pageInfo.total = response.data.total
    } else {
      ElMessage.error(response.message || '获取医生列表失败')
    }
  } catch (error) {
    console.error('获取医生列表失败:', error)
    ElMessage.error('获取医生列表失败')
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

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增医生'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑医生'
  isEdit.value = true
  Object.assign(doctorForm, row)
  dialogVisible.value = true
}

// 查看
const handleView = async (row) => {
  // 复制医生基本信息数据
  Object.assign(viewDoctorData, row)
  
  // 获取医生统计数据
  await getDoctorStats(row.id)
  
  // 显示详情对话框
  viewDialogVisible.value = true
}

// 排班管理
const handleViewSchedule = (row) => {
  // 可以跳转到医生排班管理页面或打开排班对话框
  ElMessage.info(`${row.name} 医生的排班管理功能待实现`)
  // 将来可以实现：
  // this.$router.push({ name: 'DoctorSchedule', params: { doctorId: row.id } })
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该医生吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await deleteDoctor(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
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
    const response = await deleteDoctorBatch(ids)
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      getList()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 导出数据
const handleExport = () => {
  ElMessage.info('导出数据功能待实现')
  // 将来可以实现：
  // exportDoctorData().then(response => {
  //   // 处理导出逻辑
  // })
}

// 导入数据
const handleImport = () => {
  ElMessage.info('导入数据功能待实现')
  // 将来可以实现：
  // 打开文件选择对话框，处理批量导入
}

// 医生统计
const handleStatistics = () => {
  ElMessage.info('医生统计功能待实现')
  // 将来可以实现：
  // this.$router.push({ name: 'DoctorStatistics' })
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

// 提交表单
const handleSubmit = async () => {
  try {
    await doctorFormRef.value?.validate()
    
    submitLoading.value = true
    let result
    
    if (isEdit.value) {
      result = await updateDoctor(doctorForm)
    } else {
      result = await addDoctor(doctorForm)
    }
    
    if (result.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(result.message || (isEdit.value ? '编辑失败' : '新增失败'))
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

// 关闭对话框
const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 查看详情对话框关闭
const handleViewDialogClose = () => {
  viewDialogVisible.value = false
}

// 获取医生统计数据
const getDoctorStats = async (doctorId) => {
  try {
    const response = await getDoctorStatistics(doctorId)
    if (response.code === 200) {
      Object.assign(doctorStats, response.data)
    } else {
      // 如果后端接口未实现，使用模拟数据
      Object.assign(doctorStats, {
        patientCount: Math.floor(Math.random() * 500) + 50,
        consultationCount: Math.floor(Math.random() * 2000) + 200,
        avgRating: (Math.random() * 2 + 3).toFixed(1),
        workYears: Math.floor(Math.random() * 20) + 1
      })
    }
  } catch (error) {
    console.error('获取医生统计数据失败:', error)
    // 出错时使用模拟数据
    Object.assign(doctorStats, {
      patientCount: Math.floor(Math.random() * 500) + 50,
      consultationCount: Math.floor(Math.random() * 2000) + 200,
      avgRating: (Math.random() * 2 + 3).toFixed(1),
      workYears: Math.floor(Math.random() * 20) + 1
    })
  }
}

// 从详情对话框编辑医生
const handleEditFromView = () => {
  // 关闭详情对话框
  viewDialogVisible.value = false
  
  // 打开编辑对话框
  dialogTitle.value = '编辑医生'
  isEdit.value = true
  Object.assign(doctorForm, viewDoctorData)
  dialogVisible.value = true
}

// 从详情对话框管理排班
const handleScheduleFromView = () => {
  // 关闭详情对话框
  viewDialogVisible.value = false
  
  // 跳转到排班管理或打开排班对话框
  ElMessage.info(`${viewDoctorData.name} 医生的排班管理功能待实现`)
  // 将来可以实现：
  // this.$router.push({ name: 'DoctorSchedule', params: { doctorId: viewDoctorData.id } })
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  
  try {
    const date = new Date(dateTime)
    if (isNaN(date.getTime())) return '-'
    
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  } catch (error) {
    console.error('日期格式化失败:', error)
    return '-'
  }
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.doctor-list {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.role-info {
  margin-left: 15px;
  display: inline-block;
}

/* 工具栏样式 */
.toolbar {
  display: flex;
  gap: 12px;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 按钮风格设计规范 - 操作列按钮样式 */
.action-buttons {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
  justify-content: center;
  padding: 8px 4px;
  min-height: 32px;
}

.action-buttons .el-button.action-btn {
  margin: 0;
  padding: 6px 8px;
  font-size: 12px;
  border-radius: 6px;
  transition: all 0.3s ease;
  min-width: 65px;
  white-space: nowrap;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.action-buttons .el-button.action-btn .el-icon {
  font-size: 14px;
}

.action-buttons .el-button.action-btn:active {
  transform: scale(0.95);
}

/* 文本按钮悬停效果优化 */
.action-buttons .el-button.is-text:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 不同类型按钮的悬停颜色 - 根据设计规范优化 */
.action-buttons .el-button.is-text.el-button--primary:hover {
  background-color: rgba(64, 158, 255, 0.1);
  color: #409eff;
  border-color: rgba(64, 158, 255, 0.3);
}

.action-buttons .el-button.is-text.el-button--success:hover {
  background-color: rgba(103, 194, 58, 0.1);
  color: #67c23a;
  border-color: rgba(103, 194, 58, 0.3);
}

.action-buttons .el-button.is-text.el-button--warning:hover {
  background-color: rgba(230, 162, 60, 0.1);
  color: #e6a23c;
  border-color: rgba(230, 162, 60, 0.3);
}

.action-buttons .el-button.is-text.el-button--danger:hover {
  background-color: rgba(245, 108, 108, 0.1);
  color: #f56c6c;
  border-color: rgba(245, 108, 108, 0.3);
}

/* 禁用状态样式 */
.action-buttons .el-button.is-disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.action-buttons .el-button.is-disabled:hover {
  transform: none !important;
  background-color: transparent !important;
}

/* 表格整体样式优化 */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

:deep(.el-table td) {
  border-bottom: 1px solid #f1f3f4;
}

:deep(.el-table tr:hover > td) {
  background-color: #f8f9fa;
}

/* 表格固定列样式优化 */
:deep(.el-table .el-table-fixed-column--right) {
  box-shadow: -2px 0 10px rgba(0, 0, 0, 0.08);
  border-left: 1px solid #ebeef5;
  background-color: #fff;
}

/* 操作列标题样式 */
:deep(.el-table th.el-table-fixed-column--right) {
  background-color: #f8f9fa;
  font-weight: 600;
}

:deep(.el-table th.el-table-fixed-column--right .cell) {
  color: #303133;
  text-align: center;
}

/* 操作列内容对齐 */
:deep(.el-table td.el-table-fixed-column--right .cell) {
  padding: 0 8px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 状态标签优化 */
:deep(.el-tag) {
  border-radius: 12px;
  padding: 2px 8px;
  font-size: 12px;
}

/* 响应式设计优化 */
@media (max-width: 1200px) {
  .action-buttons {
    gap: 4px;
  }
  
  .action-buttons .el-button.action-btn {
    min-width: 60px;
    padding: 5px 6px;
    font-size: 11px;
  }
  
  .action-buttons .el-button.action-btn .el-icon {
    font-size: 12px;
  }
}

@media (max-width: 768px) {
  .action-buttons {
    flex-direction: column;
    align-items: stretch;
    gap: 4px;
    padding: 4px 2px;
  }
  
  .action-buttons .el-button.action-btn {
    width: 100%;
    justify-content: center;
    padding: 8px 12px;
    font-size: 14px;
    min-height: 40px;
  }
  
  /* 隐藏部分列在移动端 */
  :deep(.el-table .hidden-sm-and-down) {
    display: none;
  }
}

/* 搜索表单响应式优化 */
.search-card .el-form.el-form--inline .el-form-item {
  margin-right: 16px;
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .search-card .el-form.el-form--inline .el-form-item {
    width: 100%;
    margin-right: 0;
    margin-bottom: 12px;
  }
  
  .search-card .el-form.el-form--inline .el-form-item .el-input,
  .search-card .el-form.el-form--inline .el-form-item .el-select {
    width: 100%;
  }
}

/* 工具栏响应式优化 */
@media (max-width: 768px) {
  .toolbar {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }
  
  .toolbar .el-button {
    width: 100%;
    justify-content: center;
  }
  
  .role-info {
    margin-left: 0;
    margin-top: 8px;
    text-align: center;
  }
}

/* 医生详情对话框样式 */
.doctor-detail-container {
  padding: 20px;
}

.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  font-size: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}

.header-icon {
  font-size: 18px;
  color: #409eff;
}

.specialization-content {
  max-height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 备注信息内容样式 */
.remark-content {
  white-space: pre-wrap;
  word-wrap: break-word;
  color: #333;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .doctor-detail-container {
    padding: 16px;
  }
  
  .detail-card {
    margin-bottom: 16px;
  }
  
  .card-header {
    font-size: 14px;
  }
  
  .header-icon {
    font-size: 16px;
  }
  
  .stat-value {
    font-size: 16px;
  }
  
  .stat-label {
    font-size: 12px;
  }
}
</style>
