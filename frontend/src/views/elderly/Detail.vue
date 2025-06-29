<template>
  <div class="app-container">
    <el-card v-if="elderlyInfo" class="detail-card">
      <!-- 页面标题 -->
      <template #header>
        <div class="card-header">
          <h2>老人详细信息</h2>
          <el-button type="primary" @click="goBack">返回列表</el-button>
        </div>
      </template>

      <!-- 基本信息 -->
      <el-descriptions title="基本信息" :column="3" border>
        <el-descriptions-item label="姓名">{{ elderlyInfo.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <el-tag :type="elderlyInfo.gender === 1 ? 'primary' : 'warning'">
            {{ elderlyInfo.gender === 1 ? '男' : '女' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="年龄">{{ elderlyInfo.age }} 岁</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ elderlyInfo.idCard }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ elderlyInfo.phone }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ elderlyInfo.birthDate }}</el-descriptions-item>
        <el-descriptions-item label="居住地址" :span="3">{{ elderlyInfo.address }}</el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <!-- 健康信息 -->
      <el-descriptions title="健康信息" :column="3" border>
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
        <el-descriptions-item label="房间号">{{ elderlyInfo.roomNumber }}</el-descriptions-item>
        <el-descriptions-item label="既往病史" :span="3">{{ elderlyInfo.medicalHistory || '无' }}</el-descriptions-item>
        <el-descriptions-item label="过敏史" :span="3">{{ elderlyInfo.allergyHistory || '无' }}</el-descriptions-item>
        <el-descriptions-item label="用药史" :span="3">{{ elderlyInfo.medicationHistory || '无' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <!-- 紧急联系人信息 -->
      <el-descriptions title="紧急联系人信息" :column="3" border>
        <el-descriptions-item label="紧急联系人">{{ elderlyInfo.emergencyContactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ elderlyInfo.emergencyContactPhone }}</el-descriptions-item>
        <el-descriptions-item label="监护人">{{ elderlyInfo.guardianName }}</el-descriptions-item>
        <el-descriptions-item label="监护人电话">{{ elderlyInfo.guardianPhone }}</el-descriptions-item>
        <el-descriptions-item label="关系">{{ elderlyInfo.guardianRelation }}</el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <!-- 系统信息 -->
      <el-descriptions title="系统信息" :column="2" border>
        <el-descriptions-item label="创建时间">{{ elderlyInfo.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ elderlyInfo.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ elderlyInfo.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      
    </el-card>
    
    <el-empty v-else-if="!loading" description="暂无数据"></el-empty>
    
    <div v-loading="loading" class="loading-container" v-if="loading">
      <div style="height: 400px;"></div>
    </div>
  </div>
</template>

<script setup>
import { getElderlyById } from '@/api/elderly'
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const elderlyInfo = ref(null)
const loading = ref(false)

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
    } else {
      ElMessage.error('获取老人信息失败：' + (response.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取老人信息失败:', error)
    // 使用mock数据作为fallback
    const mockData = {
      id: elderlyId,
      name: '张明',
      gender: 1,
      age: 78,
      idCard: '110101194501010001',
      phone: '13800138001',
      address: '北京市朝阳区长安街1号',
      birthDate: '1945-01-01',
      emergencyContactName: '张小红',
      emergencyContactPhone: '13800138002',
      healthStatus: 'HEALTHY',
      careLevel: 1,
      bloodType: 'A',
      height: 175,
      weight: 68.5,
      roomNumber: 'A101',
      medicalHistory: '高血压病史5年',
      allergyHistory: '对青霉素过敏',
      medicationHistory: '降压药：氨氯地平片 每日一次',
      guardianName: '张小红',
      guardianPhone: '13800138002',
      guardianRelation: '女儿',
      createTime: '2024-01-01 10:00:00',
      updateTime: '2024-01-15 15:30:00',
      remark: '身体状况良好，定期体检'
    }
    elderlyInfo.value = mockData
    ElMessage.warning('API服务暂时不可用，显示模拟数据')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/elderly/list')
}

onMounted(() => {
  fetchElderlyData()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.detail-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    h2 {
      margin: 0;
      color: #303133;
      font-size: 20px;
      font-weight: 600;
    }
  }
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  color: #606266;
}

:deep(.el-descriptions__content) {
  color: #303133;
}

:deep(.el-divider__text) {
  font-weight: 600;
  color: #409EFF;
}
</style>
