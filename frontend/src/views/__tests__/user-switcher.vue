<template>
  <div class="user-switcher">
    <el-card header="用户身份切换（开发测试）">
      <div class="current-user">
        <p><strong>当前用户：</strong>{{ userStore.userName }}</p>
        <p><strong>角色：</strong>{{ userStore.userRoleText }}</p>
      </div>
      
      <div class="user-options">
        <el-button 
          v-for="(userData, role) in ROLE_DATA" 
          :key="role"
          :type="userStore.userRole === role ? 'primary' : 'default'"
          @click="switchUser(role)"
        >
          切换到{{ userData.roleText }}
        </el-button>
      </div>
      
      <div class="permissions" v-if="userStore.userRole">
        <h4>当前权限：</h4>
        <div class="permission-tags">
          <el-tag 
            v-for="permission in userStore.userPermissions" 
            :key="permission"
            size="small"
            style="margin: 2px;"
          >
            {{ permission }}
          </el-tag>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

// 从store中获取角色数据
const ROLE_DATA = {
  admin: {
    id: 1,
    username: 'admin',
    password: '123456',
    name: '系统管理员',
    role: 'admin',
    roleText: '系统管理员',
    permissions: ['*'],
    avatar: '',
    department: '系统管理部',
    phone: '13800138000',
    email: 'admin@smartcare.com',
    description: '拥有系统全部管理权限，负责平台整体运营管理'
  },
  doctor: {
    id: 2,
    username: 'doctor',
    password: '123456',
    name: '张医生',
    role: 'doctor',
    roleText: '医生',
    permissions: [
      'dashboard:view',
      'elderly:view', 'elderly:add', 'elderly:edit', 'elderly:export',
      'health:view', 'health:add', 'health:edit', 'health:manage',
      'health-warning:view', 'health-warning:handle', 'health-warning:export',
      'assessment:view', 'assessment:add', 'assessment:edit', 'assessment:export',
      'equipment:view', 'equipment:monitor',
      'report:view', 'report:export',
      'analysis:view'
    ],
    avatar: '',
    department: '内科',
    title: '主治医师',
    phone: '13800138001',
    email: 'doctor@smartcare.com',
    description: '负责老人医疗健康管理，处理健康预警和评估'
  },
  family: {
    id: 3,
    username: 'family',
    password: '123456',
    name: '李家属',
    role: 'family',
    roleText: '家属',
    permissions: [
      'dashboard:view',
      'elderly:view',
      'health:view',
      'health-warning:view',
      'report:view'
    ],
    avatar: '',
    relationship: '儿子',
    elderlyIds: [1, 2],
    phone: '13800138002',
    email: 'family@smartcare.com',
    description: '家属用户，可查看关联老人的健康信息和预警'
  }
}

const switchUser = async (role) => {
  try {
    const userData = ROLE_DATA[role]
    const token = `dev_auto_token_${role}_${Date.now()}`
    
    // 更新store
    userStore.setToken(token)
    userStore.setUserInfo(userData)
    
    // 更新localStorage
    localStorage.setItem('token', token)
    localStorage.setItem('userInfo', JSON.stringify(userData))
    
    ElMessage.success(`已切换到${userData.roleText}身份`)
    
    // 根据角色跳转到合适的页面
    if (role === 'family') {
      router.push('/elderly/family-view')
    } else {
      router.push('/dashboard')
    }
    
    // 刷新页面以确保权限生效
    setTimeout(() => {
      window.location.reload()
    }, 500)
    
  } catch (error) {
    console.error('切换用户失败:', error)
    ElMessage.error('切换用户失败')
  }
}
</script>

<style scoped>
.user-switcher {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}

.current-user {
  margin-bottom: 20px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
}

.current-user p {
  margin: 8px 0;
}

.user-options {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.permissions {
  margin-top: 20px;
}

.permissions h4 {
  margin-bottom: 12px;
  color: #303133;
}

.permission-tags {
  max-height: 200px;
  overflow-y: auto;
}
</style>
