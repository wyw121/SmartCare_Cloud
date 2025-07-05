<template>
  <div class="register-container">
    <div class="register-form">
      <div class="register-header">
        <h2>智慧医养大数据平台</h2>
        <p>用户注册</p>
      </div>

      <!-- 角色选择 -->
      <div class="role-selection" v-if="!selectedRole">
        <h3>请选择注册角色</h3>
        <div class="role-cards">
          <div 
            class="role-card admin-role"
            @click="selectRole('admin')"
          >
            <div class="role-icon">
              <el-icon><Setting /></el-icon>
            </div>
            <div class="role-info">
              <h4>系统管理员</h4>
              <p>拥有系统全部管理权限</p>
              <span class="role-features">用户管理 • 系统配置 • 数据分析</span>
            </div>
          </div>
          
          <div 
            class="role-card doctor-role"
            @click="selectRole('doctor')"
          >
            <div class="role-icon">
              <el-icon><Avatar /></el-icon>
            </div>
            <div class="role-info">
              <h4>医生</h4>
              <p>拥有医疗管理相关权限</p>
              <span class="role-features">健康管理 • 预警处理 • 评估报告</span>
            </div>
          </div>
          
          <div 
            class="role-card family-role"
            @click="selectRole('family')"
          >
            <div class="role-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="role-info">
              <h4>家属</h4>
              <p>拥有查看权限</p>
              <span class="role-features">老人信息 • 健康状况 • 报告查看</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 注册表单 -->
      <div class="register-form-content" v-if="selectedRole">
        <div class="selected-role-info">
          <span class="role-badge" :class="`${selectedRole}-badge`">
            {{ getRoleText(selectedRole) }}
          </span>
          <el-button text type="primary" @click="resetRole">重新选择角色</el-button>
        </div>

        <el-form 
          ref="registerFormRef" 
          :model="registerForm" 
          :rules="registerRules" 
          label-width="100px"
          class="register-form-main"
        >
          <!-- 基本信息 -->
          <el-divider content-position="left">基本信息</el-divider>
          
          <el-form-item label="用户名" prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="请输入用户名（3-50个字符）"
              :prefix-icon="User"
              @blur="checkUsername"
            >
              <template #suffix>
                <el-icon v-if="usernameChecking" class="is-loading"><Loading /></el-icon>
                <el-icon v-else-if="usernameStatus === 'success'" style="color: #67c23a"><Check /></el-icon>
                <el-icon v-else-if="usernameStatus === 'error'" style="color: #f56c6c"><Close /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="真实姓名" prop="name">
            <el-input
              v-model="registerForm.name"
              placeholder="请输入真实姓名"
              :prefix-icon="Avatar"
            />
          </el-form-item>

          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="registerForm.gender">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="手机号" prop="phone">
            <el-input
              v-model="registerForm.phone"
              placeholder="请输入手机号"
              :prefix-icon="Phone"
              @blur="checkPhone"
            >
              <template #suffix>
                <el-icon v-if="phoneChecking" class="is-loading"><Loading /></el-icon>
                <el-icon v-else-if="phoneStatus === 'success'" style="color: #67c23a"><Check /></el-icon>
                <el-icon v-else-if="phoneStatus === 'error'" style="color: #f56c6c"><Close /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="请输入邮箱（可选）"
              :prefix-icon="Message"
              @blur="checkEmail"
            >
              <template #suffix>
                <el-icon v-if="emailChecking" class="is-loading"><Loading /></el-icon>
                <el-icon v-else-if="emailStatus === 'success'" style="color: #67c23a"><Check /></el-icon>
                <el-icon v-else-if="emailStatus === 'error'" style="color: #f56c6c"><Close /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="部门" prop="department">
            <el-input
              v-model="registerForm.department"
              placeholder="请输入所属部门（可选）"
              :prefix-icon="OfficeBuilding"
            />
          </el-form-item>

          <!-- 密码设置 -->
          <el-divider content-position="left">密码设置</el-divider>

          <el-form-item label="密码" prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码（6-20个字符）"
              :prefix-icon="Lock"
              show-password
              @input="checkPasswordStrength"
            />
            <div class="password-strength" v-if="registerForm.password">
              <span class="strength-label">密码强度：</span>
              <div class="strength-bar">
                <div 
                  class="strength-fill" 
                  :class="`strength-${passwordStrength}`"
                  :style="{ width: `${passwordStrength * 33.33}%` }"
                ></div>
              </div>
              <span class="strength-text" :class="`strength-${passwordStrength}`">
                {{ getPasswordStrengthText() }}
              </span>
            </div>
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <!-- 角色专属字段 -->
          <template v-if="selectedRole === 'doctor'">
            <el-divider content-position="left">医生专业信息</el-divider>
            
            <el-form-item label="职称" prop="doctorTitle">
              <el-select v-model="registerForm.doctorTitle" placeholder="请选择职称">
                <el-option label="住院医师" value="住院医师" />
                <el-option label="主治医师" value="主治医师" />
                <el-option label="副主任医师" value="副主任医师" />
                <el-option label="主任医师" value="主任医师" />
              </el-select>
            </el-form-item>

            <el-form-item label="专业特长" prop="doctorSpeciality">
              <el-input
                v-model="registerForm.doctorSpeciality"
                type="textarea"
                :rows="3"
                placeholder="请输入专业特长和擅长领域"
              />
            </el-form-item>

            <el-form-item label="执业证书号" prop="doctorLicenseNumber">
              <el-input
                v-model="registerForm.doctorLicenseNumber"
                placeholder="请输入医生执业证书号"
              />
            </el-form-item>

            <el-form-item label="从业年限" prop="doctorExperienceYears">
              <el-input-number
                v-model="registerForm.doctorExperienceYears"
                :min="0"
                :max="50"
                placeholder="年"
                style="width: 150px"
              />
            </el-form-item>
          </template>

          <template v-if="selectedRole === 'family'">
            <el-divider content-position="left">家属关系信息</el-divider>
            
            <el-form-item label="与老人关系" prop="familyRelationship">
              <el-select v-model="registerForm.familyRelationship" placeholder="请选择与老人的关系">
                <el-option label="子女" value="子女" />
                <el-option label="配偶" value="配偶" />
                <el-option label="父母" value="父母" />
                <el-option label="兄弟姐妹" value="兄弟姐妹" />
                <el-option label="其他亲属" value="其他亲属" />
              </el-select>
            </el-form-item>

            <el-form-item label="关联老人" prop="familyElderlyIds">
              <el-input
                v-model="registerForm.familyElderlyIds"
                placeholder="请输入关联老人的ID，多个用逗号分隔"
              />
              <div class="form-tip">
                请联系管理员获取老人ID，或注册后由管理员分配
              </div>
            </el-form-item>
          </template>

          <!-- 备注信息 -->
          <el-form-item label="备注" prop="description">
            <el-input
              v-model="registerForm.description"
              type="textarea"
              :rows="2"
              placeholder="请输入备注信息（可选）"
            />
          </el-form-item>

          <!-- 协议同意 -->
          <el-form-item prop="agreeTerms">
            <el-checkbox v-model="registerForm.agreeTerms">
              我已阅读并同意
              <el-link type="primary" @click="showTerms">《用户协议》</el-link>
              和
              <el-link type="primary" @click="showPrivacy">《隐私政策》</el-link>
            </el-checkbox>
          </el-form-item>

          <!-- 注册按钮 -->
          <el-form-item>
            <el-button 
              type="primary" 
              size="large" 
              class="register-button"
              :loading="registerLoading"
              @click="handleRegister"
            >
              {{ registerLoading ? '注册中...' : '立即注册' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="register-footer">
        <p>已有账号？ <el-link type="primary" @click="goToLogin">立即登录</el-link></p>
        <p>© 2025 智慧医养大数据平台 版权所有</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { auth } from '@/api'
import {
  Avatar, Check, Close, Loading, Lock, Message,
  OfficeBuilding, Phone, Setting, User
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 响应式数据
const registerFormRef = ref()
const registerLoading = ref(false)
const selectedRole = ref('')

// 验证状态
const usernameChecking = ref(false)
const usernameStatus = ref('')
const phoneChecking = ref(false)
const phoneStatus = ref('')
const emailChecking = ref(false)
const emailStatus = ref('')
const passwordStrength = ref(0)

// 注册表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  gender: null,
  phone: '',
  email: '',
  department: '',
  roleCode: '',
  
  // 医生专用字段
  doctorTitle: '',
  doctorSpeciality: '',
  doctorLicenseNumber: '',
  doctorExperienceYears: null,
  
  // 家属专用字段
  familyRelationship: '',
  familyElderlyIds: '',
  
  description: '',
  agreeTerms: false
})

// 表单验证规则
const registerRules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度在3-50个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  name: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { max: 50, message: '姓名长度不能超过50个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  agreeTerms: [
    { 
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请同意用户协议和隐私政策'))
        } else {
          callback()
        }
      }, 
      trigger: 'change' 
    }
  ]
})

/**
 * 选择角色
 */
const selectRole = (role) => {
  selectedRole.value = role
  registerForm.roleCode = role
  
  // 根据角色设置特定验证规则
  if (role === 'doctor') {
    registerRules.doctorLicenseNumber = [
      { required: true, message: '请输入执业证书号', trigger: 'blur' }
    ]
  } else if (role === 'family') {
    registerRules.familyRelationship = [
      { required: true, message: '请选择与老人的关系', trigger: 'change' }
    ]
  }
}

/**
 * 重置角色选择
 */
const resetRole = () => {
  selectedRole.value = ''
  registerForm.roleCode = ''
  
  // 清空角色专属字段
  registerForm.doctorTitle = ''
  registerForm.doctorSpeciality = ''
  registerForm.doctorLicenseNumber = ''
  registerForm.doctorExperienceYears = null
  registerForm.familyRelationship = ''
  registerForm.familyElderlyIds = ''
}

/**
 * 获取角色文本
 */
const getRoleText = (role) => {
  const roleMap = {
    admin: '系统管理员',
    doctor: '医生',
    family: '家属'
  }
  return roleMap[role] || '未知角色'
}

/**
 * 检查用户名可用性
 */
const checkUsername = async () => {
  if (!registerForm.username || registerForm.username.length < 3) {
    usernameStatus.value = ''
    return
  }
  
  usernameChecking.value = true
  try {
    const response = await auth.checkUsername(registerForm.username)
    if (response.code === 200) {
      usernameStatus.value = response.data ? 'success' : 'error'
    }
  } catch (error) {
    usernameStatus.value = 'error'
  } finally {
    usernameChecking.value = false
  }
}

/**
 * 检查手机号可用性
 */
const checkPhone = async () => {
  if (!registerForm.phone || !/^1[3-9]\d{9}$/.test(registerForm.phone)) {
    phoneStatus.value = ''
    return
  }
  
  phoneChecking.value = true
  try {
    const response = await auth.checkPhone(registerForm.phone)
    if (response.code === 200) {
      phoneStatus.value = response.data ? 'success' : 'error'
    }
  } catch (error) {
    phoneStatus.value = 'error'
  } finally {
    phoneChecking.value = false
  }
}

/**
 * 检查邮箱可用性
 */
const checkEmail = async () => {
  if (!registerForm.email) {
    emailStatus.value = ''
    return
  }
  
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(registerForm.email)) {
    emailStatus.value = ''
    return
  }
  
  emailChecking.value = true
  try {
    const response = await auth.checkEmail(registerForm.email)
    if (response.code === 200) {
      emailStatus.value = response.data ? 'success' : 'error'
    }
  } catch (error) {
    emailStatus.value = 'error'
  } finally {
    emailChecking.value = false
  }
}

/**
 * 检查密码强度
 */
const checkPasswordStrength = () => {
  const password = registerForm.password
  if (!password) {
    passwordStrength.value = 0
    return
  }
  
  let score = 0
  
  // 长度检查
  if (password.length >= 8) score++
  
  // 包含数字
  if (/\d/.test(password)) score++
  
  // 包含小写字母
  if (/[a-z]/.test(password)) score++
  
  // 包含大写字母
  if (/[A-Z]/.test(password)) score++
  
  // 包含特殊字符
  if (/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) score++
  
  if (score <= 2) {
    passwordStrength.value = 1
  } else if (score <= 3) {
    passwordStrength.value = 2
  } else {
    passwordStrength.value = 3
  }
}

/**
 * 获取密码强度文本
 */
const getPasswordStrengthText = () => {
  const texts = ['', '弱', '中', '强']
  return texts[passwordStrength.value] || ''
}

/**
 * 处理注册
 */
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    const valid = await registerFormRef.value.validate()
    if (!valid) return
    
    // 检查必要的验证状态
    if (usernameStatus.value === 'error') {
      ElMessage.error('用户名已存在，请重新输入')
      return
    }
    
    if (phoneStatus.value === 'error') {
      ElMessage.error('手机号已存在，请重新输入')
      return
    }
    
    if (registerForm.email && emailStatus.value === 'error') {
      ElMessage.error('邮箱已存在，请重新输入')
      return
    }
    
    registerLoading.value = true
    
    // 构建注册数据
    const registerData = {
      username: registerForm.username,
      password: registerForm.password,
      confirmPassword: registerForm.confirmPassword,
      realName: registerForm.name, // 修复：后端期望的字段名是realName
      gender: registerForm.gender,
      phone: registerForm.phone,
      email: registerForm.email || null, // 修复：空字符串转换为null，避免数据库唯一约束冲突
      department: registerForm.department || null,
      roleCode: registerForm.roleCode,
      description: registerForm.description || null
    }
    
    // 根据角色添加专属字段
    if (registerForm.roleCode === 'doctor') {
      registerData.doctorTitle = registerForm.doctorTitle
      registerData.doctorSpeciality = registerForm.doctorSpeciality
      registerData.doctorLicenseNumber = registerForm.doctorLicenseNumber
      registerData.doctorExperienceYears = registerForm.doctorExperienceYears
    } else if (registerForm.roleCode === 'family') {
      registerData.familyRelationship = registerForm.familyRelationship
      registerData.familyElderlyIds = registerForm.familyElderlyIds
    }
    
    console.log('注册数据:', registerData)
    
    const response = await auth.register(registerData)
    
    if (response.code === 200) {
      ElMessage.success('注册成功！请使用新账号登录')
      
      // 延迟跳转到登录页
      setTimeout(() => {
        router.push('/login')
      }, 1500)
    } else {
      ElMessage.error(response.message || '注册失败')
    }
  } catch (error) {
    console.error('注册失败:', error)
    
    // 详细的错误处理
    let errorMessage = '注册失败，请重试'
    
    if (error.response) {
      // 服务器响应错误
      const status = error.response.status
      const data = error.response.data
      
      if (status === 400) {
        errorMessage = data.message || '请求参数错误，请检查输入信息'
      } else if (status === 500) {
        errorMessage = '服务器内部错误，请联系管理员'
      } else {
        errorMessage = data.message || `服务器错误 (${status})`
      }
    } else if (error.request) {
      // 网络错误
      errorMessage = '网络连接失败，请检查网络设置'
    } else {
      // 其他错误
      errorMessage = error.message || '未知错误'
    }
    
    ElMessage.error(errorMessage)
  } finally {
    registerLoading.value = false
  }
}

/**
 * 显示用户协议
 */
const showTerms = () => {
  ElMessageBox.alert('用户协议内容...', '用户协议', {
    confirmButtonText: '我知道了'
  })
}

/**
 * 显示隐私政策
 */
const showPrivacy = () => {
  ElMessageBox.alert('隐私政策内容...', '隐私政策', {
    confirmButtonText: '我知道了'
  })
}

/**
 * 跳转到登录页
 */
const goToLogin = () => {
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-form {
  width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
  
  h2 {
    color: #303133;
    font-size: 28px;
    font-weight: 600;
    margin: 0 0 8px 0;
  }
  
  p {
    color: #909399;
    font-size: 16px;
    margin: 0;
  }
}

/* 角色选择样式 */
.role-selection {
  margin-bottom: 30px;
  
  h3 {
    text-align: center;
    color: #303133;
    margin-bottom: 20px;
  }
}

.role-cards {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.role-card {
  flex: 1;
  padding: 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
  border: 2px solid transparent;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }
  
  &.admin-role {
    background: linear-gradient(135deg, #ff6b6b, #ee5a52);
    color: white;
    
    &:hover {
      background: linear-gradient(135deg, #ff5252, #e53935);
    }
  }
  
  &.doctor-role {
    background: linear-gradient(135deg, #4fc3f7, #29b6f6);
    color: white;
    
    &:hover {
      background: linear-gradient(135deg, #29b6f6, #0288d1);
    }
  }
  
  &.family-role {
    background: linear-gradient(135deg, #81c784, #66bb6a);
    color: white;
    
    &:hover {
      background: linear-gradient(135deg, #66bb6a, #4caf50);
    }
  }
}

.role-icon {
  font-size: 32px;
  margin-bottom: 10px;
}

.role-info {
  h4 {
    margin: 0 0 8px 0;
    font-size: 18px;
    font-weight: 600;
  }
  
  p {
    margin: 0 0 8px 0;
    font-size: 14px;
    opacity: 0.9;
  }
}

.role-features {
  font-size: 12px;
  opacity: 0.8;
}

/* 选中角色信息 */
.selected-role-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 12px 16px;
  background: #f5f7fa;
  border-radius: 8px;
}

.role-badge {
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 14px;
  font-weight: 500;
  color: white;
  
  &.admin-badge {
    background: #ff5722;
  }
  
  &.doctor-badge {
    background: #2196f3;
  }
  
  &.family-badge {
    background: #4caf50;
  }
}

/* 表单样式 */
.register-form-main {
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .el-input,
  .el-select {
    width: 100%;
  }
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

/* 密码强度指示器 */
.password-strength {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.strength-label {
  font-size: 12px;
  color: #606266;
}

.strength-bar {
  flex: 1;
  height: 4px;
  background: #f0f0f0;
  border-radius: 2px;
  overflow: hidden;
}

.strength-fill {
  height: 100%;
  transition: all 0.3s ease;
  
  &.strength-1 {
    background: #f56c6c;
  }
  
  &.strength-2 {
    background: #e6a23c;
  }
  
  &.strength-3 {
    background: #67c23a;
  }
}

.strength-text {
  font-size: 12px;
  font-weight: 500;
  
  &.strength-1 {
    color: #f56c6c;
  }
  
  &.strength-2 {
    color: #e6a23c;
  }
  
  &.strength-3 {
    color: #67c23a;
  }
}

.register-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
}

.register-footer {
  text-align: center;
  margin-top: 30px;
  
  p {
    margin: 8px 0;
    color: #909399;
    font-size: 14px;
    
    &:last-child {
      font-size: 12px;
    }
  }
}

/* 分割线样式 */
.el-divider {
  margin: 20px 0;
  
  .el-divider__text {
    font-size: 14px;
    font-weight: 500;
    color: #303133;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-form {
    width: 95%;
    padding: 20px;
  }
  
  .role-cards {
    flex-direction: column;
  }
  
  .role-card {
    margin-bottom: 10px;
  }
}
</style>
