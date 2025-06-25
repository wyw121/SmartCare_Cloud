<template>
  <div class="profile-container">
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
          <el-tag type="success">{{ userInfo.departmentName }}</el-tag>
        </div>
        
        <div class="user-stats">
          <div class="stat-item">
            <div class="stat-value">{{ userStats.loginCount }}</div>
            <div class="stat-label">登录次数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ userStats.workDays }}</div>
            <div class="stat-label">工作天数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ userStats.taskCount }}</div>
            <div class="stat-label">处理任务</div>
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
                <el-radio label="male">男</el-radio>
                <el-radio label="female">女</el-radio>
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
                当前邮箱：{{ userInfo.email }}
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
                当前手机：{{ userInfo.phone }}
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
    <el-card header="最近活动" style="margin-top: 20px;">
      <el-timeline>
        <el-timeline-item
          v-for="activity in activities"
          :key="activity.id"
          :timestamp="activity.time"
          :type="activity.type"
        >
          <div class="activity-content">
            <h4>{{ activity.title }}</h4>
            <p>{{ activity.description }}</p>
          </div>
        </el-timeline-item>
      </el-timeline>
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
</template>

<script setup>
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

const userStore = useUserStore()

// 响应式数据
const basicLoading = ref(false)
const passwordLoading = ref(false)
const passwordDialogVisible = ref(false)
const basicFormRef = ref()
const passwordFormRef = ref()
const avatarInputRef = ref()

// 用户信息
const userInfo = ref({
  id: 1,
  username: 'admin',
  realName: '系统管理员',
  email: 'admin@smartcare.com',
  phone: '13800138001',
  avatar: '',
  roleName: '超级管理员',
  departmentName: '管理部',
  gender: 'male',
  birthday: '1990-01-01',
  bio: '负责系统整体管理和维护',
  emailVerified: true,
  phoneVerified: true,
  createTime: '2024-01-01 00:00:00',
  lastLoginTime: '2024-01-15 10:30:00'
})

// 用户统计数据
const userStats = ref({
  loginCount: 128,
  workDays: 25,
  taskCount: 45
})

// 基本信息表单
const basicForm = reactive({
  realName: '',
  email: '',
  phone: '',
  gender: 'male',
  birthday: '',
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
const activities = ref([
  {
    id: 1,
    title: '登录系统',
    description: '通过Web端登录智慧医养平台',
    time: '2024-01-15 10:30:00',
    type: 'primary'
  },
  {
    id: 2,
    title: '处理健康预警',
    description: '处理了张大爷的血压异常预警',
    time: '2024-01-15 09:45:00',
    type: 'success'
  },
  {
    id: 3,
    title: '添加老人档案',
    description: '新增了李奶奶的个人档案信息',
    time: '2024-01-14 16:20:00',
    type: 'info'
  },
  {
    id: 4,
    title: '系统配置',
    description: '更新了系统安全配置',
    time: '2024-01-14 14:10:00',
    type: 'warning'
  }
])

/**
 * 页面初始化
 */
onMounted(() => {
  initUserInfo()
})

/**
 * 初始化用户信息
 */
const initUserInfo = () => {
  // 将用户信息同步到表单
  Object.assign(basicForm, {
    realName: userInfo.value.realName,
    email: userInfo.value.email,
    phone: userInfo.value.phone,
    gender: userInfo.value.gender,
    birthday: userInfo.value.birthday,
    bio: userInfo.value.bio
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
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 更新用户信息
    Object.assign(userInfo.value, basicForm)
    
    ElMessage.success('基本信息更新成功')
  } catch (error) {
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
    
    passwordLoading.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('密码修改成功')
    passwordDialogVisible.value = false
  } catch (error) {
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
    // 模拟上传
    ElMessage.info('正在上传头像...')
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // 创建预览URL
    const avatarUrl = URL.createObjectURL(file)
    userInfo.value.avatar = avatarUrl
    
    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  }
  
  // 清空文件输入
  event.target.value = ''
}
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 20px;
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
