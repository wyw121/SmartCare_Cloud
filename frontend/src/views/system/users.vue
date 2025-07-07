<template>
  <div class="users-container">
    <div class="header-section">
      <h2>用户管理</h2>
      <p>管理系统用户账号、角色权限和基本信息</p>
    </div>

    <!-- 搜索和操作栏 -->
    <div class="toolbar">
      <div class="search-box">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入用户名或姓名搜索"
          style="width: 300px"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select 
          v-model="searchForm.status" 
          placeholder="用户状态" 
          style="width: 120px; margin-left: 10px"
          clearable
        >
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
      
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增用户
        </el-button>
        <el-button 
          type="danger" 
          :disabled="!selectedUsers.length"
          @click="handleBatchDelete"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 用户列表 -->
    <div class="table-container">
      <el-table
        ref="tableRef"
        v-loading="loading"
        :data="userList"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="id" label="用户ID" width="80" align="center" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="roleName" label="角色" width="100" />
        <el-table-column prop="departmentName" label="部门" width="120" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column prop="lastLoginTime" label="最后登录" width="160" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button 
              :type="row.status === 1 ? 'warning' : 'success'" 
              size="small" 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageParams.current"
          v-model:page-size="pageParams.size"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlePageSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 用户表单弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="userForm"
        :rules="userRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="userForm.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色" prop="roleId">
              <el-select v-model="userForm.roleId" placeholder="请选择角色" style="width: 100%">
                <el-option 
                  v-for="role in roleList" 
                  :key="role.id" 
                  :label="role.name" 
                  :value="role.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门" prop="departmentId">
              <el-select v-model="userForm.departmentId" placeholder="请选择部门" style="width: 100%">
                <el-option 
                  v-for="dept in departmentList" 
                  :key="dept.id" 
                  :label="dept.name" 
                  :value="dept.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" v-if="!isEdit">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input 
                v-model="userForm.password" 
                type="password" 
                placeholder="请输入密码" 
                show-password 
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input 
                v-model="userForm.confirmPassword" 
                type="password" 
                placeholder="请确认密码" 
                show-password 
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="备注">
          <el-input 
            v-model="userForm.remark" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入备注信息" 
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
    batchDeleteUsers,
    checkEmail,
    checkUsername,
    createUser,
    deleteUser,
    getUserList as fetchUserList,
    getDepartmentList,
    getRoleList,
    toggleUserStatus,
    updateUser
} from '@/api/system'
import { Delete, Plus, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const tableRef = ref()
const formRef = ref()
const userList = ref([])
const selectedUsers = ref([])
const total = ref(0)

// 分页参数
const pageParams = reactive({
  current: 1,
  size: 10
})

// 搜索表单
const searchForm = reactive({
  keyword: '',
  status: ''
})

// 用户表单
const userForm = reactive({
  id: null,
  username: '',
  realName: '',
  email: '',
  phone: '',
  roleId: null,
  departmentId: null,
  password: '',
  confirmPassword: '',
  status: 1,
  remark: ''
})

// 表单验证规则
const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
    {
      validator: async (rule, value, callback) => {
        if (!value) {
          callback()
          return
        }
        try {
          const result = await checkUsername(value, isEdit.value ? userForm.id : null)
          if (result.code === 200 && result.data.available) {
            callback()
          } else {
            callback(new Error('用户名已存在'))
          }
        } catch (error) {
          // 如果检查失败，跳过验证
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
    {
      validator: async (rule, value, callback) => {
        if (!value || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
          callback()
          return
        }
        try {
          const result = await checkEmail(value, isEdit.value ? userForm.id : null)
          if (result.code === 200 && result.data.available) {
            callback()
          } else {
            callback(new Error('邮箱已被使用'))
          }
        } catch (error) {
          // 如果检查失败，跳过验证
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  roleId: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== userForm.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 角色列表
const roleList = ref([])

// 部门列表
const departmentList = ref([])

/**
 * 页面加载时获取数据
 */
onMounted(async () => {
  await Promise.all([
    getUserList(),
    loadRoleList(),
    loadDepartmentList()
  ])
})

/**
 * 加载角色列表
 */
const loadRoleList = async () => {
  try {
    const result = await getRoleList()
    roleList.value = result.data || []
  } catch (error) {
    console.error('获取角色列表失败:', error)
    // 如果API失败，使用默认数据
    roleList.value = [
      { id: 1, name: '超级管理员' },
      { id: 2, name: '系统管理员' },
      { id: 3, name: '医生' },
      { id: 4, name: '护士' },
      { id: 5, name: '运营人员' }
    ]
  }
}

/**
 * 加载部门列表
 */
const loadDepartmentList = async () => {
  try {
    const result = await getDepartmentList()
    departmentList.value = result.data || []
  } catch (error) {
    console.error('获取部门列表失败:', error)
    // 如果API失败，使用默认数据
    departmentList.value = [
      { id: 1, name: '管理部' },
      { id: 2, name: '医疗部' },
      { id: 3, name: '护理部' },
      { id: 4, name: '运营部' },
      { id: 5, name: '技术部' }
    ]
  }
}

/**
 * 获取用户列表
 */
const getUserList = async () => {
  loading.value = true
  try {
    const params = {
      keyword: searchForm.keyword,
      status: searchForm.status,
      current: pageParams.current,
      size: pageParams.size
    }
    
    const result = await fetchUserList(params)
    
    if (result.code === 200) {
      // 处理分页数据
      const pageData = result.data || {}
      const users = pageData.records || []
      userList.value = users.map(user => ({
        id: user.id,
        username: user.username,
        realName: user.realName,
        roleCode: user.roleCode,
        roleName: user.roleName,
        status: user.status,
        createTime: user.createTime,
        phone: user.phone,
        email: user.email
      }))
      total.value = pageData.total || 0
    } else {
      throw new Error(result.message || '获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error(error.message || '获取用户列表失败')
    
    // 如果API失败，清空列表
    userList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

/**
 * 搜索用户
 */
const handleSearch = () => {
  pageParams.current = 1
  getUserList()
}

/**
 * 重置搜索
 */
const resetSearch = () => {
  Object.assign(searchForm, {
    keyword: '',
    status: ''
  })
  handleSearch()
}

/**
 * 新增用户
 */
const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

/**
 * 编辑用户
 */
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(userForm, { ...row })
  dialogVisible.value = true
}

/**
 * 删除用户
 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '删除确认', {
      type: 'warning'
    })
    
    const result = await deleteUser(row.id)
    
    if (result.code === 200) {
      ElMessage.success('删除成功')
      getUserList()
    } else {
      throw new Error(result.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除用户失败:', error)
      ElMessage.error(error.message || '删除失败')
    }
  }
}

/**
 * 批量删除
 */
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedUsers.value.length} 个用户吗？`, '批量删除确认', {
      type: 'warning'
    })
    
    const ids = selectedUsers.value.map(user => user.id)
    const result = await batchDeleteUsers(ids)
    
    if (result.code === 200) {
      ElMessage.success('批量删除成功')
      selectedUsers.value = []
      getUserList()
    } else {
      throw new Error(result.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error(error.message || '批量删除失败')
    }
  }
}

/**
 * 切换用户状态
 */
const handleToggleStatus = async (row) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    const action = newStatus === 0 ? '禁用' : '启用'
    
    await ElMessageBox.confirm(`确定要${action}该用户吗？`, `${action}确认`, {
      type: 'warning'
    })
    
    const result = await toggleUserStatus(row.id, newStatus)
    
    if (result.code === 200) {
      row.status = newStatus
      ElMessage.success(`${action}成功`)
    } else {
      throw new Error(result.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('切换用户状态失败:', error)
      ElMessage.error(error.message || '操作失败')
    }
  }
}

/**
 * 选择变化
 */
const handleSelectionChange = (selection) => {
  selectedUsers.value = selection
}

/**
 * 页码变化
 */
const handlePageChange = (page) => {
  pageParams.current = page
  getUserList()
}

/**
 * 页大小变化
 */
const handlePageSizeChange = (size) => {
  pageParams.size = size
  pageParams.current = 1
  getUserList()
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    const valid = await formRef.value.validate()
    if (!valid) return
    
    submitLoading.value = true
    
    // 准备提交数据
    const submitData = {
      username: userForm.username,
      realName: userForm.realName,
      email: userForm.email,
      phone: userForm.phone,
      roleId: userForm.roleId,
      departmentId: userForm.departmentId,
      status: userForm.status,
      remark: userForm.remark
    }
    
    // 如果是新增用户，添加密码
    if (!isEdit.value) {
      submitData.password = userForm.password
    }
    
    let result
    if (isEdit.value) {
      result = await updateUser(userForm.id, submitData)
    } else {
      result = await createUser(submitData)
    }
    
    if (result.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      getUserList()
    } else {
      throw new Error(result.message || '操作失败')
    }
  } catch (error) {
    console.error('提交用户数据失败:', error)
    ElMessage.error(error.message || (isEdit.value ? '编辑失败' : '新增失败'))
  } finally {
    submitLoading.value = false
  }
}

/**
 * 关闭弹窗
 */
const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

/**
 * 重置表单
 */
const resetForm = () => {
  Object.assign(userForm, {
    id: null,
    username: '',
    realName: '',
    email: '',
    phone: '',
    roleId: null,
    departmentId: null,
    password: '',
    confirmPassword: '',
    status: 1,
    remark: ''
  })
  
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}
</script>

<style lang="scss" scoped>
.users-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}

.header-section {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  
  h2 {
    color: #303133;
    font-size: 24px;
    font-weight: 600;
    margin: 0 0 8px 0;
  }
  
  p {
    color: #606266;
    font-size: 14px;
    margin: 0;
  }
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.table-container {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

:deep(.el-table) {
  .el-table__header {
    th {
      background-color: #fafafa;
      color: #606266;
      font-weight: 600;
    }
  }
}

:deep(.el-dialog) {
  .el-dialog__header {
    padding: 20px 20px 10px;
    border-bottom: 1px solid #ebeef5;
  }
  
  .el-dialog__body {
    padding: 20px;
  }
}
</style>
