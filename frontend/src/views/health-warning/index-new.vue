<template>
  <!-- 根据用户角色动态切换健康预警组件 -->
  <component :is="currentComponent" />
</template>

<script setup>
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import { computed, onMounted } from 'vue'

// 导入不同角色的组件
import AdminHealthWarning from './admin-index.vue'
import FamilyHealthWarning from './family-index.vue'

const userStore = useUserStore()

// 根据用户角色计算当前应该显示的组件
const currentComponent = computed(() => {
  const userRole = userStore.userRole
  
  console.log('当前用户角色:', userRole)
  
  switch (userRole) {
    case 'family':
      return FamilyHealthWarning
    case 'admin':
    case 'doctor':
    default:
      return AdminHealthWarning
  }
})

// 页面挂载时显示提示
onMounted(() => {
  const userRole = userStore.userRole
  const roleText = userStore.userRoleText
  
  console.log(`健康预警页面已加载 - 当前用户: ${roleText} (${userRole})`)
  
  if (userRole === 'family') {
    ElMessage.info('您正在使用家属专用的健康预警查看界面')
  }
})
</script>
