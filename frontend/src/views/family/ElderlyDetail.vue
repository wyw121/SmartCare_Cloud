<template>
  <div class="app-container family-elderly-detail">
    <el-card v-if="elderlyInfo" class="detail-card">
      <!-- 页面标题 -->
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="header-icon" :size="24"><User /></el-icon>
            <h2>{{ elderlyInfo.name }} 的详细信息</h2>
          </div>
          <div class="header-right">
            <el-tag :type="getAccessLevelType()" effect="dark" size="large">
              {{ getAccessLevelText() }}
            </el-tag>
            <el-button type="primary" @click="goBack">返回列表</el-button>
          </div>
        </div>
      </template>

      <!-- 提示信息 -->
      <el-alert
        v-if="accessLevel === 'basic'"
        title="权限提示"
        type="info"
        :closable="false"
        show-icon
        class="access-hint"
      >
        <template #default>
          您当前拥有<strong>基础信息</strong>访问权限,仅可查看老人的基本信息。如需查看更多健康信息,请联系管理员申请权限。
        </template>
      </el-alert>

      <!-- 基本信息 - 所有家属都可查看 -->
      <div class="section-title">
        <el-icon><InfoFilled /></el-icon>
        <span>基本信息</span>
      </div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="姓名">{{ elderlyInfo.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <el-tag :type="elderlyInfo.gender === 1 ? 'primary' : 'warning'" size="small">
            {{ elderlyInfo.gender === 1 ? '男' : '女' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="年龄">{{ elderlyInfo.age }} 岁</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ elderlyInfo.birthDate }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ elderlyInfo.phone }}</el-descriptions-item>
        <el-descriptions-item label="房间号">{{ elderlyInfo.roomNumber || '未分配' }}</el-descriptions-item>
        <el-descriptions-item label="居住地址" :span="3">{{ elderlyInfo.address }}</el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <!-- 健康信息 - 需要health或full权限 -->
      <template v-if="canViewHealth">
        <div class="section-title">
          <el-icon><Monitor /></el-icon>
          <span>健康信息</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="健康状态">
            <el-tag :type="getHealthStatusType(elderlyInfo.healthStatus)">
              {{ getHealthStatusText(elderlyInfo.healthStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="照护等级">
            <el-tag>{{ getCareLevelText(elderlyInfo.careLevel) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="血型">{{ elderlyInfo.bloodType }}</el-descriptions-item>
          <el-descriptions-item label="身高">{{ elderlyInfo.height }} cm</el-descriptions-item>
          <el-descriptions-item label="体重">{{ elderlyInfo.weight }} kg</el-descriptions-item>
          <el-descriptions-item label="BMI">
            <el-tag :type="getBMIType()">{{ calculateBMI() }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="既往病史" :span="3">
            <div class="medical-info">
              {{ elderlyInfo.medicalHistory || '无' }}
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="过敏史" :span="3">
            <div class="medical-info warning-text">
              {{ elderlyInfo.allergyHistory || '无' }}
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="用药史" :span="3">
            <div class="medical-info">
              {{ elderlyInfo.medicationHistory || '无' }}
            </div>
          </el-descriptions-item>
        </el-descriptions>

        <el-divider />
      </template>

      <!-- 紧急联系人信息 - 所有家属都可查看 -->
      <div class="section-title">
        <el-icon><Phone /></el-icon>
        <span>紧急联系人信息</span>
      </div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="紧急联系人">{{ elderlyInfo.emergencyContactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ elderlyInfo.emergencyContactPhone }}</el-descriptions-item>
        <el-descriptions-item label="监护人">{{ elderlyInfo.guardianName }}</el-descriptions-item>
        <el-descriptions-item label="监护人电话">{{ elderlyInfo.guardianPhone }}</el-descriptions-item>
        <el-descriptions-item label="关系">{{ elderlyInfo.guardianRelation }}</el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <!-- 快捷操作 - 家属可用功能 -->
      <div class="section-title">
        <el-icon><Operation /></el-icon>
        <span>快捷操作</span>
      </div>
      <div class="quick-actions">
        <el-button 
          v-if="canViewHealth" 
          type="primary" 
          :icon="Document"
          @click="viewHealthRecords"
        >
          查看健康记录
        </el-button>
        <el-button 
          v-if="canViewHealth" 
          type="warning" 
          :icon="Warning"
          @click="viewHealthWarnings"
        >
          查看健康预警
        </el-button>
        <el-button 
          type="success" 
          :icon="ChatDotRound"
          @click="contactStaff"
        >
          联系工作人员
        </el-button>
      </div>

      <el-divider />

      <!-- 系统信息 -->
      <div class="section-title">
        <el-icon><Clock /></el-icon>
        <span>系统信息</span>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="创建时间">{{ elderlyInfo.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ elderlyInfo.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          <div class="remark-text">
            {{ elderlyInfo.remark || '无' }}
          </div>
        </el-descriptions-item>
      </el-descriptions>
      
    </el-card>
    
    <el-empty v-else-if="!loading" description="暂无数据">
      <el-button type="primary" @click="goBack">返回列表</el-button>
    </el-empty>
    
    <div v-loading="loading" class="loading-container" v-if="loading">
      <div style="height: 400px;"></div>
    </div>
  </div>
</template>

<script setup>
import { getElderlyById } from '@/api/elderly'
import { ElMessage } from 'element-plus'
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { 
  User, InfoFilled, Monitor, Phone, Operation, 
  Clock, Document, Warning, ChatDotRound 
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const elderlyInfo = ref(null)
const loading = ref(false)

// 家属访问权限级别 (从family_elderly_relation表获取)
// 实际应用中应从后端API获取,这里先用模拟数据
const accessLevel = ref('full') // 可选值: 'basic' | 'health' | 'full'

// 权限检查
const canViewHealth = computed(() => {
  return ['health', 'full'].includes(accessLevel.value)
})

const canViewFull = computed(() => {
  return accessLevel.value === 'full'
})

// 获取访问级别文本
const getAccessLevelText = () => {
  const levelMap = {
    'basic': '基础信息访问权限',
    'health': '健康信息访问权限',
    'full': '完全访问权限'
  }
  return levelMap[accessLevel.value] || '未知权限'
}

// 获取访问级别标签类型
const getAccessLevelType = () => {
  const typeMap = {
    'basic': 'info',
    'health': 'warning',
    'full': 'success'
  }
  return typeMap[accessLevel.value] || 'info'
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

// 计算BMI
const calculateBMI = () => {
  if (!elderlyInfo.value?.height || !elderlyInfo.value?.weight) {
    return '未知'
  }
  const heightInMeters = elderlyInfo.value.height / 100
  const bmi = (elderlyInfo.value.weight / (heightInMeters * heightInMeters)).toFixed(1)
  return `${bmi} (${getBMICategory(bmi)})`
}

const getBMICategory = (bmi) => {
  if (bmi < 18.5) return '偏瘦'
  if (bmi < 24) return '正常'
  if (bmi < 28) return '偏胖'
  return '肥胖'
}

const getBMIType = () => {
  if (!elderlyInfo.value?.height || !elderlyInfo.value?.weight) {
    return 'info'
  }
  const heightInMeters = elderlyInfo.value.height / 100
  const bmi = elderlyInfo.value.weight / (heightInMeters * heightInMeters)
  
  if (bmi < 18.5 || bmi >= 28) return 'warning'
  if (bmi < 24) return 'success'
  return 'info'
}

// 获取老人信息
const fetchElderlyData = async () => {
  const elderlyId = route.params.id
  if (!elderlyId) {
    ElMessage.error('缺少老人ID参数')
    return
  }
  
  loading.value = true
  try {
    const response = await getElderlyById(elderlyId)
    if (response.code === 200) {
      elderlyInfo.value = response.data || {}
      
      // 从后端获取家属访问权限级别
      // 实际应从family_elderly_relation表查询access_level字段
      // 这里使用模拟数据作为示例
      // const accessResponse = await getFamilyAccessLevel(userStore.userId, elderlyId)
      // accessLevel.value = accessResponse.data.accessLevel
      
    } else {
      ElMessage.error('获取老人信息失败：' + (response.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取老人信息失败:', error)
    ElMessage.error('获取老人信息失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 查看健康记录
const viewHealthRecords = () => {
  router.push({
    path: '/health/records',
    query: { elderlyId: elderlyInfo.value.id }
  })
}

// 查看健康预警
const viewHealthWarnings = () => {
  router.push({
    path: '/health/warning',
    query: { elderlyId: elderlyInfo.value.id }
  })
}

// 联系工作人员
const contactStaff = () => {
  ElMessage.info('联系工作人员功能开发中...')
  // 实现在线联系或消息通知功能
}

// 返回列表
const goBack = () => {
  router.push('/elderly/family-view')
}

onMounted(() => {
  fetchElderlyData()
})
</script>

<style lang="scss" scoped>
.family-elderly-detail {
  padding: 20px;
  
  .detail-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .header-left {
        display: flex;
        align-items: center;
        gap: 12px;
        
        .header-icon {
          color: #409EFF;
        }
        
        h2 {
          margin: 0;
          color: #303133;
          font-size: 20px;
          font-weight: 600;
        }
      }
      
      .header-right {
        display: flex;
        align-items: center;
        gap: 12px;
      }
    }
  }
  
  .access-hint {
    margin-bottom: 20px;
    
    strong {
      color: #409EFF;
      font-weight: 600;
    }
  }
  
  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    margin: 24px 0 16px;
    padding-bottom: 8px;
    border-bottom: 2px solid #409EFF;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    
    .el-icon {
      color: #409EFF;
      font-size: 18px;
    }
  }
  
  .medical-info {
    padding: 8px 12px;
    background-color: #f5f7fa;
    border-radius: 4px;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-word;
    
    &.warning-text {
      background-color: #fef0f0;
      color: #c45656;
      border-left: 3px solid #f56c6c;
    }
  }
  
  .quick-actions {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
    padding: 16px;
    background-color: #f5f7fa;
    border-radius: 4px;
  }
  
  .remark-text {
    padding: 8px 12px;
    background-color: #f5f7fa;
    border-radius: 4px;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-word;
  }
  
  .loading-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;
  }
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  color: #606266;
  background-color: #fafafa;
}

:deep(.el-descriptions__content) {
  color: #303133;
}

:deep(.el-divider) {
  margin: 24px 0;
}

// 响应式设计
@media (max-width: 768px) {
  .family-elderly-detail {
    padding: 12px;
    
    .detail-card .card-header {
      flex-direction: column;
      gap: 16px;
      
      .header-right {
        width: 100%;
        justify-content: space-between;
      }
    }
    
    .quick-actions {
      flex-direction: column;
      
      .el-button {
        width: 100%;
      }
    }
  }
}
</style>
