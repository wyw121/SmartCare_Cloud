<template>
  <div class="elderly-management">
    <el-card class="page-header">
      <div class="header-content">
        <h2>老人档案管理</h2>
        <p>管理老人基本信息、健康状况、照护等级等档案数据</p>
      </div>
    </el-card>

    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="searchForm.idCard" placeholder="请输入身份证号" clearable />
        </el-form-item>
        <el-form-item label="健康状态">
          <el-select v-model="searchForm.healthStatus" placeholder="请选择健康状态" clearable>
            <el-option label="健康" value="HEALTHY" />
            <el-option label="亚健康" value="SUBHEALTH" />
            <el-option label="疾病" value="SICK" />
            <el-option label="重病" value="SERIOUS" />
            <el-option label="危险" value="DANGER" />
            <el-option label="预警" value="WARNING" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <el-card class="toolbar-card">
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <i class="el-icon-plus"></i>
          新增老人档案
        </el-button>
        <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleBatchDelete">
          <i class="el-icon-delete"></i>
          批量删除
        </el-button>
        <el-button type="success" @click="handleExport">
          <i class="el-icon-download"></i>
          导出数据
        </el-button>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.gender === 1 ? 'primary' : 'warning'">
              {{ scope.row.gender === 1 ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="healthStatus" label="健康状态" width="100">
          <template #default="scope">
            <el-tag :type="getHealthStatusType(scope.row.healthStatus)">
              {{ getHealthStatusText(scope.row.healthStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="careLevel" label="照护等级" width="100">
          <template #default="scope">
            <el-tag>{{ getCareLevelText(scope.row.careLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="居住地址" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="800px"
      :before-close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="formData.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="formData.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="formData.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="formData.birthDate"
                type="date"
                placeholder="请选择出生日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="健康状态" prop="healthStatus">
              <el-select v-model="formData.healthStatus" placeholder="请选择健康状态">
                <el-option label="健康" value="HEALTHY" />
                <el-option label="亚健康" value="SUBHEALTH" />
                <el-option label="疾病" value="SICK" />
                <el-option label="重病" value="SERIOUS" />
                <el-option label="危险" value="DANGER" />
                <el-option label="预警" value="WARNING" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="居住地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="紧急联系人" prop="emergencyContactName">
              <el-input v-model="formData.emergencyContactName" placeholder="请输入紧急联系人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急联系电话" prop="emergencyContactPhone">
              <el-input v-model="formData.emergencyContactPhone" placeholder="请输入紧急联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="既往病史">
          <el-input
            v-model="formData.medicalHistory"
            type="textarea"
            :rows="3"
            placeholder="请输入既往病史"
          />
        </el-form-item>
        <el-form-item label="过敏史">
          <el-input
            v-model="formData.allergyHistory"
            type="textarea"
            :rows="3"
            placeholder="请输入过敏史"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { batchDeleteElderly, createElderly, deleteElderly, getElderlyPage, updateElderly } from '@/api/elderly'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

export default {
  name: 'ElderlyManagement',
  setup() {
    const loading = ref(false)
    const submitLoading = ref(false)
    const dialogVisible = ref(false)
    const dialogTitle = ref('新增老人档案')
    const isEdit = ref(false)
    const formRef = ref(null)
    const selectedRows = ref([])

    // 搜索表单
    const searchForm = reactive({
      name: '',
      idCard: '',
      healthStatus: ''
    })

    // 分页
    const pagination = reactive({
      current: 1,
      size: 10,
      total: 0
    })

    // 表格数据
    const tableData = ref([])

    // 表单数据
    const formData = reactive({
      id: null,
      name: '',
      gender: 1,
      idCard: '',
      birthDate: '',
      phone: '',
      address: '',
      healthStatus: 'HEALTHY',
      emergencyContactName: '',
      emergencyContactPhone: '',
      medicalHistory: '',
      allergyHistory: ''
    })

    // 表单验证规则
    const formRules = {
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      gender: [
        { required: true, message: '请选择性别', trigger: 'change' }
      ],
      idCard: [
        { required: true, message: '请输入身份证号', trigger: 'blur' },
        { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '身份证号格式不正确', trigger: 'blur' }
      ],
      phone: [
        { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
      ],
      healthStatus: [
        { required: true, message: '请选择健康状态', trigger: 'change' }
      ]
    }

    // 健康状态映射
    const getHealthStatusText = (status) => {
      const statusMap = {
        'HEALTHY': '健康',
        'SUBHEALTH': '亚健康',
        'SICK': '疾病',
        'SERIOUS': '重病',
        'DANGER': '危险',
        'WARNING': '预警'
      }
      return statusMap[status] || '未知'
    }

    const getHealthStatusType = (status) => {
      const typeMap = {
        'HEALTHY': 'success',
        'SUBHEALTH': 'info',
        'SICK': 'warning',
        'SERIOUS': 'danger',
        'DANGER': 'danger',
        'WARNING': 'warning'
      }
      return typeMap[status] || 'info'
    }

    // 照护等级映射
    const getCareLevelText = (level) => {
      const levelMap = {
        1: '自理',
        2: '半自理',
        3: '不能自理'
      }
      return levelMap[level] || '未知'
    }

    // 获取列表数据
    const fetchData = async () => {
      loading.value = true
      try {
        const params = {
          current: pagination.current,
          size: pagination.size,
          ...searchForm
        }
        const response = await getElderlyPage(params)
        if (response.code === 200) {
          tableData.value = response.data.records
          pagination.total = response.data.total
        }
      } catch (error) {
        ElMessage.error('获取数据失败：' + error.message)
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.current = 1
      fetchData()
    }

    // 重置
    const handleReset = () => {
      Object.keys(searchForm).forEach(key => {
        searchForm[key] = ''
      })
      pagination.current = 1
      fetchData()
    }

    // 新增
    const handleAdd = () => {
      dialogTitle.value = '新增老人档案'
      isEdit.value = false
      resetFormData()
      dialogVisible.value = true
    }

    // 编辑
    const handleEdit = (row) => {
      dialogTitle.value = '编辑老人档案'
      isEdit.value = true
      Object.assign(formData, row)
      dialogVisible.value = true
    }

    // 查看
    const handleView = (row) => {
      // TODO: 实现查看详情功能
      ElMessage.info('查看功能开发中...')
    }

    // 删除
    const handleDelete = (row) => {
      ElMessageBox.confirm(
        `确定要删除老人档案 "${row.name}" 吗？`,
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          await deleteElderly(row.id)
          ElMessage.success('删除成功')
          fetchData()
        } catch (error) {
          ElMessage.error('删除失败：' + error.message)
        }
      })
    }

    // 批量删除
    const handleBatchDelete = () => {
      const ids = selectedRows.value.map(row => row.id)
      ElMessageBox.confirm(
        `确定要删除选中的 ${ids.length} 条老人档案吗？`,
        '批量删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          await batchDeleteElderly(ids)
          ElMessage.success('批量删除成功')
          fetchData()
        } catch (error) {
          ElMessage.error('批量删除失败：' + error.message)
        }
      })
    }

    // 导出
    const handleExport = () => {
      ElMessage.info('导出功能开发中...')
    }

    // 表格选择变化
    const handleSelectionChange = (selection) => {
      selectedRows.value = selection
    }

    // 分页大小变化
    const handleSizeChange = (size) => {
      pagination.size = size
      pagination.current = 1
      fetchData()
    }

    // 页码变化
    const handleCurrentChange = (current) => {
      pagination.current = current
      fetchData()
    }

    // 提交表单
    const handleSubmit = () => {
      formRef.value.validate(async (valid) => {
        if (valid) {
          submitLoading.value = true
          try {
            if (isEdit.value) {
              await updateElderly(formData)
              ElMessage.success('更新成功')
            } else {
              await createElderly(formData)
              ElMessage.success('新增成功')
            }
            dialogVisible.value = false
            fetchData()
          } catch (error) {
            ElMessage.error('操作失败：' + error.message)
          } finally {
            submitLoading.value = false
          }
        }
      })
    }

    // 关闭对话框
    const handleDialogClose = (done) => {
      ElMessageBox.confirm('确定关闭吗？未保存的数据将丢失。')
        .then(() => {
          done()
        })
        .catch(() => {})
    }

    // 重置表单数据
    const resetFormData = () => {
      Object.assign(formData, {
        id: null,
        name: '',
        gender: 1,
        idCard: '',
        birthDate: '',
        phone: '',
        address: '',
        healthStatus: 'HEALTHY',
        emergencyContactName: '',
        emergencyContactPhone: '',
        medicalHistory: '',
        allergyHistory: ''
      })
    }

    onMounted(() => {
      fetchData()
    })

    return {
      loading,
      submitLoading,
      dialogVisible,
      dialogTitle,
      formRef,
      selectedRows,
      searchForm,
      pagination,
      tableData,
      formData,
      formRules,
      getHealthStatusText,
      getHealthStatusType,
      getCareLevelText,
      fetchData,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleView,
      handleDelete,
      handleBatchDelete,
      handleExport,
      handleSelectionChange,
      handleSizeChange,
      handleCurrentChange,
      handleSubmit,
      handleDialogClose
    }
  }
}
</script>

<style scoped>
.elderly-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.header-content h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.header-content p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.toolbar-card {
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  gap: 12px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
