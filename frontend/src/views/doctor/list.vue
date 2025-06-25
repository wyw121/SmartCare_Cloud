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
      <el-row>
        <el-col :span="24">
          <el-button type="primary" @click="handleAdd" :icon="Plus">新增医生</el-button>
          <el-button 
            type="danger" 
            @click="handleBatchDelete" 
            :disabled="multipleSelection.length === 0"
            :icon="Delete"
          >
            批量删除
          </el-button>
          <el-button type="success" @click="handleExport" :icon="Download">导出数据</el-button>
        </el-col>
      </el-row>
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
        <el-table-column prop="employeeNumber" label="工号" min-width="120" />
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="phone" label="联系电话" min-width="120" />
        <el-table-column prop="department" label="科室" min-width="100" />
        <el-table-column prop="title" label="职称" min-width="120" />
        <el-table-column prop="specialization" label="专长" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)" :icon="View">
              查看
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(scope.row)" :icon="Edit">
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
              :icon="Delete"
            >
              删除
            </el-button>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete, Download, View, Edit } from '@element-plus/icons-vue'
import {
  getDoctorPageList,
  addDoctor,
  updateDoctor,
  deleteDoctor,
  deleteDoctorBatch
} from '@/api/doctor'

// 响应式数据
const tableLoading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增医生')
const isEdit = ref(false)
const tableData = ref([])
const multipleSelection = ref([])

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
    const { data } = await getDoctorPageList(params)
    if (data.code === 200) {
      tableData.value = data.data.list
      pageInfo.total = data.data.total
    } else {
      ElMessage.error(data.message || '获取医生列表失败')
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
const handleView = (row) => {
  // 可以打开查看详情的对话框或跳转到详情页面
  ElMessage.info('查看功能待实现')
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该医生吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const { data } = await deleteDoctor(row.id)
    if (data.code === 200) {
      ElMessage.success('删除成功')
      getList()
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
    const { data } = await deleteDoctorBatch(ids)
    if (data.code === 200) {
      ElMessage.success('批量删除成功')
      getList()
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
  // 导出功能待实现
  ElMessage.info('导出功能待实现')
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
    
    if (result.data.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(result.data.message || (isEdit.value ? '编辑失败' : '新增失败'))
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

// 重置表单
const resetForm = () => {
  Object.assign(doctorForm, {
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
  doctorFormRef.value?.clearValidate()
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
