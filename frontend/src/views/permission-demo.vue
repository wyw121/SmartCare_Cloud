<template>
  <div class="permission-demo">
    <el-card class="header-card">
      <div class="demo-header">
        <h2>权限系统演示</h2>
        <p>当前角色：<el-tag type="primary">{{ userRole }}</el-tag> | {{ userRoleText }}</p>
        <p>权限列表：<el-tag v-for="permission in userPermissions" :key="permission" size="small" style="margin-right: 5px;">{{ permission }}</el-tag></p>
      </div>
    </el-card>

    <el-row :gutter="20">
      <!-- 管理员权限区域 -->
      <el-col :span="8">
        <el-card>
          <template #header>
            <h3>管理员专享功能</h3>
          </template>
          <div class="permission-section">
            <el-button type="primary" v-role="'admin'">用户管理</el-button>
            <el-button type="primary" v-role="'admin'">系统配置</el-button>
            <el-button type="primary" v-role="'admin'">数据备份</el-button>
            <el-button type="primary" v-role="'admin'">权限设置</el-button>
            <p v-role="'admin'" class="permission-text">✅ 您有管理员权限，可以看到这些功能</p>
            <p v-role="['doctor', 'family']" class="permission-text denied">❌ 您没有管理员权限，无法访问这些功能</p>
          </div>
        </el-card>
      </el-col>

      <!-- 医生权限区域 -->
      <el-col :span="8">
        <el-card>
          <template #header>
            <h3>医生可用功能</h3>
          </template>
          <div class="permission-section">
            <el-button type="success" v-role="['admin', 'doctor']">老人档案编辑</el-button>
            <el-button type="success" v-role="['admin', 'doctor']">健康记录管理</el-button>
            <el-button type="success" v-role="['admin', 'doctor']">医疗报告</el-button>
            <el-button type="success" v-role="['admin', 'doctor']">预警处理</el-button>
            <p v-role="['admin', 'doctor']" class="permission-text">✅ 您有医生权限，可以管理医疗相关数据</p>
            <p v-role="'family'" class="permission-text denied">❌ 您是家属角色，只能查看不能编辑</p>
          </div>
        </el-card>
      </el-col>

      <!-- 家属权限区域 -->
      <el-col :span="8">
        <el-card>
          <template #header>
            <h3>家属可用功能</h3>
          </template>
          <div class="permission-section">
            <el-button type="info">查看老人信息</el-button>
            <el-button type="info">查看健康状况</el-button>
            <el-button type="info">查看历史记录</el-button>
            <el-button type="info">接收通知</el-button>
            <p class="permission-text">✅ 所有角色都可以查看基础信息</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 权限指令演示 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3>基于权限的内容显示</h3>
          </template>
          <div class="permission-section">
            <div v-permission="'elderly:edit'">
              <el-alert title="老人信息编辑权限" type="success" :closable="false" />
            </div>
            <div v-permission="'health:manage'">
              <el-alert title="健康管理权限" type="success" :closable="false" />
            </div>
            <div v-permission="'system:manage'">
              <el-alert title="系统管理权限" type="success" :closable="false" />
            </div>
            <div v-permission="'nonexistent:permission'">
              <el-alert title="这个内容不会显示" type="error" :closable="false" />
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 角色切换演示 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3>快速角色切换（开发模式）</h3>
          </template>
          <div class="permission-section">
            <el-button 
              type="primary" 
              @click="switchRole('admin')"
              :disabled="userRole === 'admin'"
            >
              切换到管理员
            </el-button>
            <el-button 
              type="success" 
              @click="switchRole('doctor')"
              :disabled="userRole === 'doctor'"
            >
              切换到医生
            </el-button>
            <el-button 
              type="warning" 
              @click="switchRole('family')"
              :disabled="userRole === 'family'"
            >
              切换到家属
            </el-button>
            <p class="permission-text" style="margin-top: 10px;">
              切换角色后，页面内容会根据权限自动调整显示
            </p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- API权限测试 -->
    <el-row style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <h3>API权限测试</h3>
          </template>
          <div class="permission-section">
            <el-space wrap>
              <el-button @click="testAPI('elderly:view')" v-permission="'elderly:view'">
                查看老人信息
              </el-button>
              <el-button @click="testAPI('elderly:edit')" v-permission="'elderly:edit'">
                编辑老人信息
              </el-button>
              <el-button @click="testAPI('health:manage')" v-permission="'health:manage'">
                管理健康记录
              </el-button>
              <el-button @click="testAPI('system:manage')" v-permission="'system:manage'">
                系统管理
              </el-button>
            </el-space>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { useUserStore } from '@/store/user'
import { hasPermission } from '@/utils/permission'
import { ElMessage } from 'element-plus'
import { computed } from 'vue'

const userStore = useUserStore()

const userRole = computed(() => userStore.userRole)
const userRoleText = computed(() => userStore.userRoleText)
const userPermissions = computed(() => userStore.userPermissions)

/**
 * 切换角色
 */
const switchRole = async (role) => {
  try {
    await userStore.quickLogin(role)
    ElMessage.success(`已切换到${getRoleText(role)}角色`)
    // 强制刷新页面以更新权限显示
    location.reload()
  } catch (error) {
    ElMessage.error(error.message || '角色切换失败')
  }
}

/**
 * 获取角色文本
 */
const getRoleText = (role) => {
  const roleMap = {
    admin: '管理员',
    doctor: '医生',
    family: '家属'
  }
  return roleMap[role] || '未知角色'
}

/**
 * 测试API权限
 */
const testAPI = (permission) => {
  if (hasPermission(permission)) {
    ElMessage.success(`✅ 权限验证通过：${permission}`)
  } else {
    ElMessage.error(`❌ 权限验证失败：${permission}`)
  }
}
</script>

<style scoped>
.permission-demo {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.demo-header {
  text-align: center;
  
  h2 {
    margin-bottom: 10px;
    color: #303133;
  }
  
  p {
    margin: 8px 0;
    color: #606266;
  }
}

.permission-section {
  .el-button {
    margin: 5px;
  }
  
  .permission-text {
    margin: 10px 0;
    font-size: 14px;
    
    &.denied {
      color: #f56c6c;
    }
  }
}
</style>
