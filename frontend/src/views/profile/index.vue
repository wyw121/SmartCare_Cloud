<template>
  <div class="profile-container" v-loading="dataLoading">
    <!-- 未登录提示 -->
    <div v-if="!userStore.isLoggedIn" class="login-prompt">
      <el-result
        icon="warning"
        title="未登录"
        sub-title="请先登录以查看个人信息"
      >
        <template #extra>
          <el-button type="primary" @click="goToLogin">去登录</el-button>
        </template>
      </el-result>
    </div>

    <!-- 个人中心内容 -->
    <div v-else>
      <div class="profile-header">
        <div class="avatar-section">
          <el-avatar :size="120" :src="userInfo.avatar">
            <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          </el-avatar>
          <el-button type="primary" size="small" @click="handleAvatarUpload" style="margin-top: 15px;">
            更换头像
          </el-button>
        </div>
      
      <div class="info-section">
        <h2>{{ userInfo.realName }}</h2>
        <p class="username">@{{ userInfo.username }}</p>
        <div class="user-tags">
          <el-tag type="primary">{{ userInfo.roleName }}</el-tag>
          <el-tag type="success">{{ userInfo.departmentName || '未分配部门' }}</el-tag>
        </div>
        
        <div class="user-stats">
          <div class="stat-item">
            <div class="stat-value">{{ userStats.loginCount }}</div>
            <div class="stat-label">登录次数</div>
          </div>
          <div class="stat-item" v-if="userStore.userRole !== 'family'">
            <div class="stat-value">{{ userStats.workDays }}</div>
            <div class="stat-label">工作天数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ userStats.taskCount }}</div>
            <div class="stat-label">{{ userStore.userRole === 'family' ? '查看预警' : '处理任务' }}</div>
          </div>
          <div class="stat-item" v-if="userStore.userRole === 'family'">
            <div class="stat-value">{{ userInfo.elderlyIds?.length || 2 }}</div>
            <div class="stat-label">关联长辈</div>
          </div>
        </div>
      </div>
    </div>

    <el-row :gutter="20" style="margin-top: 30px;">
      <!-- 基本信息 -->
      <el-col :span="12">
        <el-card header="基本信息" class="info-card">
          <el-form
            ref="basicFormRef"
            :model="basicForm"
            :rules="basicRules"
            label-width="100px"
          >
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="basicForm.realName" placeholder="请输入真实姓名" />
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="basicForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="basicForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="basicForm.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="生日" prop="birthday">
              <el-date-picker
                v-model="basicForm.birthday"
                type="date"
                placeholder="请选择生日"
                style="width: 100%"
              />
            </el-form-item>
            
            <el-form-item label="个人简介">
              <el-input
                v-model="basicForm.bio"
                type="textarea"
                :rows="4"
                placeholder="请输入个人简介"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" :loading="basicLoading" @click="handleUpdateBasic">
                保存基本信息
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 安全设置 -->
      <el-col :span="12">
        <el-card header="安全设置" class="info-card">
          <div class="security-item">
            <div class="security-info">
              <h4>登录密码</h4>
              <p class="security-desc">用于登录系统的密码</p>
            </div>
            <el-button @click="handleChangePassword">修改密码</el-button>
          </div>
          
          <el-divider />
          
          <div class="security-item">
            <div class="security-info">
              <h4>邮箱验证</h4>
              <p class="security-desc">
                当前邮箱：{{ userInfo.email || '未设置' }}
                <el-tag v-if="userInfo.emailVerified" type="success" size="small">已验证</el-tag>
                <el-tag v-else type="warning" size="small">未验证</el-tag>
              </p>
            </div>
            <el-button v-if="!userInfo.emailVerified" @click="handleVerifyEmail">
              验证邮箱
            </el-button>
          </div>
          
          <el-divider />
          
          <div class="security-item">
            <div class="security-info">
              <h4>手机验证</h4>
              <p class="security-desc">
                当前手机：{{ userInfo.phone || '未设置' }}
                <el-tag v-if="userInfo.phoneVerified" type="success" size="small">已验证</el-tag>
                <el-tag v-else type="warning" size="small">未验证</el-tag>
              </p>
            </div>
            <el-button v-if="!userInfo.phoneVerified" @click="handleVerifyPhone">
              验证手机
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近活动 -->
    <el-card header="最近活动" style="margin-top: 20px;" v-loading="dataLoading">
      <el-timeline v-if="activities.length > 0">
        <el-timeline-item
          v-for="activity in activities"
          :key="activity.id"
          :timestamp="formatDateTime(activity.time)"
          :type="activity.type"
        >
          <div class="activity-content">
            <h4>{{ activity.title }}</h4>
            <p>{{ activity.description }}</p>
          </div>
        </el-timeline-item>
      </el-timeline>
      <div v-else class="no-data">
        <el-empty description="暂无活动记录" />
      </div>
    </el-card>

    <!-- 修改密码弹窗 -->
    <el-dialog
      v-model="passwordDialogVisible"
      title="修改密码"
      width="500px"
      :before-close="handlePasswordDialogClose"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入原密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="handlePasswordDialogClose">取消</el-button>
        <el-button type="primary" :loading="passwordLoading" @click="handleUpdatePassword">
          确定修改
        </el-button>
      </template>
    </el-dialog>

    <!-- 头像上传（隐藏的文件输入） -->
    <input
      ref="avatarInputRef"
      type="file"
      accept="image/*"
      style="display: none"
      @change="handleAvatarChange"
    />
    </div>
  </div>
</template>

<script setup>
import { user } from '@/api'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

const userStore = useUserStore()

// 响应式数据
const basicLoading = ref(false)
const passwordLoading = ref(false)
const passwordDialogVisible = ref(false)
const dataLoading = ref(false)
const basicFormRef = ref()
const passwordFormRef = ref()
const avatarInputRef = ref()

// 用户信息
const userInfo = ref({
  id: null,
  username: '',
  realName: '',
  email: '',
  phone: '',
  avatar: '',
  roleName: '',
  departmentName: '',
  gender: 1,
  birthday: null,
  bio: '',
  emailVerified: false,
  phoneVerified: false,
  createTime: '',
  lastLoginTime: ''
})

// 用户统计数据
const userStats = ref({
  loginCount: 0,
  workDays: 0,
  taskCount: 0
})

// 基本信息表单
const basicForm = reactive({
  realName: '',
  email: '',
  phone: '',
  gender: 1,
  birthday: null,
  bio: ''
})

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 基本信息验证规则
const basicRules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
}

// 密码验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 最近活动
const activities = ref([])

/**
 * 页面初始化
 */
onMounted(async () => {
  await loadUserData()
})

/**
 * 加载用户数据
 */
const loadUserData = async () => {
  try {
    dataLoading.value = true
    
    // 检查是否有有效的登录令牌
    const userStore = useUserStore()
    if (!userStore.token) {
      ElMessage.error('请先登录')
      return
    }
    
    // 检查是否是开发环境的模拟token
    if (userStore.token.startsWith('dev_auto_token_') || 
        userStore.token.startsWith('mock_token_') || 
        userStore.token.startsWith('dev_switch_token_')) {
      console.log('检测到开发环境token，加载模拟数据')
      // 开发环境模拟数据
      loadMockData()
      return
    }
    
    // 尝试调用真实API
    try {
      // 并行加载用户信息、统计数据和活动记录
      const [userResponse, statsResponse, activitiesResponse] = await Promise.all([
        user.getInfo(),
        user.getStatistics(),
        user.getActivities()
      ])

      if (userResponse.success) {
        userInfo.value = userResponse.data
        initUserForm()
      }

      if (statsResponse.success) {
        userStats.value = statsResponse.data
      }

      if (activitiesResponse.success) {
        activities.value = activitiesResponse.data
      }
    } catch (apiError) {
      console.error('真实API调用失败，降级到模拟数据:', apiError)
      
      // API调用失败时，降级到模拟数据
      loadMockData()
    }
  } catch (error) {
    console.error('加载用户数据失败:', error)
    
    // 如果是JWT错误或其他错误，尝试加载模拟数据
    if (error.message.includes('JWT') || error.message.includes('token')) {
      console.log('JWT令牌无效，加载模拟数据')
      loadMockData()
    } else {
      ElMessage.error('加载用户数据失败')
    }
  } finally {
    dataLoading.value = false
  }
}

/**
 * 加载模拟数据（开发环境使用）
 */
const loadMockData = () => {
  console.log('开始加载模拟数据')
  const userStore = useUserStore()
  const mockUser = userStore.userInfo || {}
  
  console.log('从store获取的用户信息:', mockUser)
  
  // 根据用户角色设置不同的模拟数据
  let mockData = {}
  
  if (mockUser.role === 'family') {
    // 家属用户的模拟数据
    mockData = {
      id: mockUser.id || 3,
      username: mockUser.username || 'family',
      realName: mockUser.name || '李家属',
      email: mockUser.email || 'family@smartcare.com',
      phone: mockUser.phone || '13800138002',
      avatar: mockUser.avatar || '',
      roleName: mockUser.roleText || '家属',
      departmentName: '',
      gender: 1,
      birthday: null,
      bio: mockUser.description || '家属用户，负责关心长辈健康',
      emailVerified: true,
      phoneVerified: true,
      createTime: '2024-01-01 00:00:00',
      lastLoginTime: new Date().toISOString(),
      relationship: mockUser.relationship || '儿子', // 家属关系
      elderlyIds: mockUser.elderlyIds || [1, 2] // 关联的长辈ID
    }
    
    // 家属的统计数据
    userStats.value = {
      loginCount: 45,
      workDays: 0, // 家属没有工作天数概念
      taskCount: 8 // 查看预警次数
    }
    
    // 家属的活动记录
    activities.value = [
      {
        id: 1,
        title: '查看健康预警',
        description: '查看了关联长辈的健康预警信息',
        time: new Date().toISOString(),
        type: 'warning'
      },
      {
        id: 2,
        title: '登录系统',
        description: '通过Web端登录智慧医养平台',
        time: new Date(Date.now() - 30 * 60 * 1000).toISOString(),
        type: 'primary'
      },
      {
        id: 3,
        title: '查看长辈档案',
        description: '查看了关联长辈的详细档案信息',
        time: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
        type: 'info'
      },
      {
        id: 4,
        title: '关联长辈',
        description: '成功关联了长辈账户',
        time: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
        type: 'success'
      }
    ]
  } else if (mockUser.role === 'doctor') {
    // 医生用户的模拟数据
    mockData = {
      id: mockUser.id || 2,
      username: mockUser.username || 'doctor',
      realName: mockUser.name || '张医生',
      email: mockUser.email || 'doctor@smartcare.com',
      phone: mockUser.phone || '13800138001',
      avatar: mockUser.avatar || '',
      roleName: mockUser.roleText || '医生',
      departmentName: mockUser.department || '内科',
      gender: 1,
      birthday: null,
      bio: mockUser.description || '负责老人医疗健康管理，处理健康预警和评估',
      emailVerified: true,
      phoneVerified: true,
      createTime: '2024-01-01 00:00:00',
      lastLoginTime: new Date().toISOString()
    }
    
    // 医生的统计数据
    userStats.value = {
      loginCount: 89,
      workDays: 22,
      taskCount: 156
    }
    
    // 医生的活动记录
    activities.value = [
      {
        id: 1,
        title: '处理健康预警',
        description: '处理了多个老人的健康预警信息',
        time: new Date().toISOString(),
        type: 'warning'
      },
      {
        id: 2,
        title: '登录系统',
        description: '通过Web端登录智慧医养平台',
        time: new Date(Date.now() - 15 * 60 * 1000).toISOString(),
        type: 'primary'
      },
      {
        id: 3,
        title: '编写评估报告',
        description: '为老人编写了健康评估报告',
        time: new Date(Date.now() - 60 * 60 * 1000).toISOString(),
        type: 'success'
      }
    ]
  } else {
    // 管理员或其他用户的模拟数据
    mockData = {
      id: mockUser.id || 1,
      username: mockUser.username || 'admin',
      realName: mockUser.name || '系统管理员',
      email: mockUser.email || 'admin@smartcare.com',
      phone: mockUser.phone || '13800138000',
      avatar: mockUser.avatar || '',
      roleName: mockUser.roleText || '超级管理员',
      departmentName: mockUser.department || '系统管理部',
      gender: 1,
      birthday: null,
      bio: mockUser.description || '负责系统整体管理和维护',
      emailVerified: true,
      phoneVerified: true,
      createTime: '2024-01-01 00:00:00',
      lastLoginTime: new Date().toISOString()
    }
    
    // 管理员的统计数据
    userStats.value = {
      loginCount: 128,
      workDays: 25,
      taskCount: 45
    }
    
    // 管理员的活动记录
    activities.value = [
      {
        id: 1,
        title: '登录系统',
        description: '通过Web端登录智慧医养平台',
        time: new Date().toISOString(),
        type: 'primary'
      },
      {
        id: 2,
        title: '查看个人中心',
        description: '访问了个人中心页面',
        time: new Date(Date.now() - 30 * 60 * 1000).toISOString(),
        type: 'info'
      },
      {
        id: 3,
        title: '系统初始化',
        description: '完成了系统初始化配置',
        time: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
        type: 'success'
      }
    ]
  }
  
  // 设置用户信息
  userInfo.value = mockData
  console.log('设置的用户信息:', userInfo.value)
  console.log('设置的统计数据:', userStats.value)
  console.log('设置的活动记录:', activities.value)
  
  initUserForm()
}

/**
 * 初始化用户表单
 */
const initUserForm = () => {
  // 将用户信息同步到表单
  Object.assign(basicForm, {
    realName: userInfo.value.realName || '',
    email: userInfo.value.email || '',
    phone: userInfo.value.phone || '',
    gender: userInfo.value.gender || 1,
    birthday: userInfo.value.birthday || null,
    bio: userInfo.value.bio || ''
  })
}

/**
 * 更新基本信息
 */
const handleUpdateBasic = async () => {
  if (!basicFormRef.value) return
  
  try {
    const valid = await basicFormRef.value.validate()
    if (!valid) return
    
    basicLoading.value = true
    
    const userStore = useUserStore()
    
    // 如果是开发环境的模拟token，直接更新本地数据
    if (userStore.token.startsWith('dev_auto_token_') || userStore.token.startsWith('mock_token_')) {
      // 模拟API延迟
      await new Promise(resolve => setTimeout(resolve, 800))
      
      // 更新本地用户信息
      Object.assign(userInfo.value, basicForm)
      
      // 更新store中的用户信息
      const updatedUserInfo = { ...userStore.userInfo }
      updatedUserInfo.name = basicForm.realName
      updatedUserInfo.email = basicForm.email
      updatedUserInfo.phone = basicForm.phone
      userStore.userInfo = updatedUserInfo
      localStorage.setItem('userInfo', JSON.stringify(updatedUserInfo))
      
      ElMessage.success('基本信息更新成功')
      return
    }
    
    // 真实API调用
    const response = await user.updateInfo(basicForm)
    
    if (response.success) {
      // 更新本地用户信息
      Object.assign(userInfo.value, basicForm)
      ElMessage.success('基本信息更新成功')
    } else {
      ElMessage.error(response.message || '基本信息更新失败')
    }
  } catch (error) {
    console.error('更新基本信息失败:', error)
    ElMessage.error('基本信息更新失败')
  } finally {
    basicLoading.value = false
  }
}

/**
 * 修改密码
 */
const handleChangePassword = () => {
  // 重置密码表单
  Object.assign(passwordForm, {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  })
  passwordDialogVisible.value = true
}

/**
 * 更新密码
 */
const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return
  
  try {
    const valid = await passwordFormRef.value.validate()
    if (!valid) return
    
    // 验证确认密码
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      ElMessage.error('两次输入密码不一致')
      return
    }
    
    passwordLoading.value = true
    
    const userStore = useUserStore()
    
    // 如果是开发环境的模拟token，直接模拟成功
    if (userStore.token.startsWith('dev_auto_token_') || userStore.token.startsWith('mock_token_')) {
      // 模拟API延迟
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      ElMessage.success('密码修改成功')
      passwordDialogVisible.value = false
      return
    }
    
    // 真实API调用
    const response = await user.changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
      confirmPassword: passwordForm.confirmPassword
    })
    
    if (response.success) {
      ElMessage.success('密码修改成功')
      passwordDialogVisible.value = false
    } else {
      ElMessage.error(response.message || '密码修改失败')
    }
  } catch (error) {
    console.error('修改密码失败:', error)
    ElMessage.error('密码修改失败')
  } finally {
    passwordLoading.value = false
  }
}

/**
 * 关闭密码弹窗
 */
const handlePasswordDialogClose = () => {
  passwordDialogVisible.value = false
  if (passwordFormRef.value) {
    passwordFormRef.value.clearValidate()
  }
}

/**
 * 验证邮箱
 */
const handleVerifyEmail = async () => {
  try {
    await ElMessageBox.confirm('确定要发送验证邮件吗？', '邮箱验证', {
      type: 'info'
    })
    
    // 模拟发送验证邮件
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('验证邮件已发送，请查收')
    // 注意：实际项目中应该调用真实的邮箱验证API
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发送验证邮件失败')
    }
  }
}

/**
 * 验证手机
 */
const handleVerifyPhone = async () => {
  try {
    await ElMessageBox.confirm('确定要发送验证短信吗？', '手机验证', {
      type: 'info'
    })
    
    // 模拟发送验证短信
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('验证短信已发送，请查收')
    // 注意：实际项目中应该调用真实的手机验证API
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发送验证短信失败')
    }
  }
}

/**
 * 头像上传
 */
const handleAvatarUpload = () => {
  avatarInputRef.value.click()
}

/**
 * 头像文件选择变化
 */
const handleAvatarChange = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  // 检查文件大小（限制2MB）
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.error('头像文件大小不能超过2MB')
    return
  }
  
  // 检查文件类型
  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }
  
  try {
    ElMessage.info('正在上传头像...')
    
    const userStore = useUserStore()
    
    // 如果是开发环境的模拟token，直接创建预览URL
    if (userStore.token.startsWith('dev_auto_token_') || userStore.token.startsWith('mock_token_')) {
      // 模拟上传延迟
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      // 创建预览URL
      const avatarUrl = URL.createObjectURL(file)
      userInfo.value.avatar = avatarUrl
      
      // 更新store中的头像
      const updatedUserInfo = { ...userStore.userInfo }
      updatedUserInfo.avatar = avatarUrl
      userStore.userInfo = updatedUserInfo
      localStorage.setItem('userInfo', JSON.stringify(updatedUserInfo))
      
      ElMessage.success('头像上传成功')
      return
    }
    
    // 真实API调用
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await user.uploadAvatar(formData)
    
    if (response.success) {
      userInfo.value.avatar = response.data.avatarUrl
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(response.message || '头像上传失败')
    }
  } catch (error) {
    console.error('上传头像失败:', error)
    ElMessage.error('头像上传失败')
  }
  
  // 清空文件输入
  event.target.value = ''
}

/**
 * 格式化性别显示
 */
const formatGender = (gender) => {
  return gender === 1 ? '男' : '女'
}

/**
 * 格式化时间显示
 */
const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return new Date(datetime).toLocaleString()
}

/**
 * 跳转到登录页面
 */
const goToLogin = () => {
  window.location.href = '/login.html'
}
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 20px;
}

.login-prompt {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.profile-header {
  display: flex;
  gap: 30px;
  padding: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
  
  .avatar-section {
    text-align: center;
    
    .el-avatar {
      border: 4px solid rgba(255, 255, 255, 0.2);
    }
  }
  
  .info-section {
    flex: 1;
    
    h2 {
      margin: 0 0 8px 0;
      font-size: 28px;
      font-weight: 600;
    }
    
    .username {
      margin: 0 0 15px 0;
      font-size: 16px;
      opacity: 0.8;
    }
    
    .user-tags {
      margin-bottom: 20px;
      
      .el-tag {
        margin-right: 10px;
      }
    }
    
    .user-stats {
      display: flex;
      gap: 30px;
      
      .stat-item {
        text-align: center;
        
        .stat-value {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 5px;
        }
        
        .stat-label {
          font-size: 14px;
          opacity: 0.8;
        }
      }
    }
  }
}

.info-card {
  height: 100%;
  
  :deep(.el-card__body) {
    height: calc(100% - 60px);
  }
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .security-info {
    flex: 1;
    
    h4 {
      margin: 0 0 8px 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
    
    .security-desc {
      margin: 0;
      color: #606266;
      font-size: 14px;
      
      .el-tag {
        margin-left: 8px;
      }
    }
  }
}

.activity-content {
  h4 {
    margin: 0 0 5px 0;
    color: #303133;
    font-size: 14px;
    font-weight: 600;
  }
  
  p {
    margin: 0;
    color: #606266;
    font-size: 13px;
  }
}

:deep(.el-timeline-item__content) {
  padding-bottom: 15px;
}

:deep(.el-card__header) {
  background-color: #fafafa;
  border-bottom: 1px solid #ebeef5;
  font-weight: 600;
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
