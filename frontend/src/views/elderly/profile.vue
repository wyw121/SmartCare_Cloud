<template>
  <div class="elderly-profile" v-loading="loading">
    <el-card class="profile-header">
      <div class="profile-info">
        <el-avatar :size="80" :src="elderlyInfo.avatar" />
        <div class="info-content">
          <h2>{{ elderlyInfo.name }}</h2>
          <p class="info-item">
            <span class="label">性别：</span>
            <span>{{ elderlyInfo.gender === 1 ? '男' : '女' }}</span>
          </p>
          <p class="info-item">
            <span class="label">年龄：</span>
            <span>{{ elderlyInfo.age }}岁</span>
          </p>
          <p class="info-item">
            <span class="label">健康状况：</span>
            <el-tag :type="getHealthStatusType(elderlyInfo.healthStatus)">
              {{ getHealthStatusText(elderlyInfo.healthStatus) }}
            </el-tag>
          </p>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card title="基本信息">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="身份证号">{{ elderlyInfo.idCard }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ elderlyInfo.phone }}</el-descriptions-item>
            <el-descriptions-item label="家庭地址">{{ elderlyInfo.address }}</el-descriptions-item>
            <el-descriptions-item label="紧急联系人">{{ elderlyInfo.emergencyContactName }}</el-descriptions-item>
            <el-descriptions-item label="联系人电话">{{ elderlyInfo.emergencyContactPhone }}</el-descriptions-item>
            <el-descriptions-item label="照护等级">
              <el-tag>{{ getCareLevelText(elderlyInfo.careLevel) }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card title="医疗信息">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="医保类型">{{ elderlyInfo.insuranceType || '暂无' }}</el-descriptions-item>
            <el-descriptions-item label="既往病史">{{ elderlyInfo.medicalHistory || '暂无' }}</el-descriptions-item>
            <el-descriptions-item label="过敏史">{{ elderlyInfo.allergyHistory || '暂无' }}</el-descriptions-item>
            <el-descriptions-item label="家庭医生">{{ elderlyInfo.familyDoctorName || '暂无' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <el-card title="最近健康记录" class="health-records">
      <el-table :data="healthRecords" border>
        <el-table-column prop="recordDate" label="记录日期" width="120" />
        <el-table-column prop="recordType" label="记录类型" width="100" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="diagnosis" label="诊断结果" show-overflow-tooltip />
        <el-table-column prop="abnormalStatus" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.abnormalStatus === 0 ? 'success' : 'danger'">
              {{ scope.row.abnormalStatus === 0 ? '正常' : '异常' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { getElderlyById, getElderlyHealthRecords } from '@/api/elderly'
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const elderlyId = route.params.id

const elderlyInfo = ref({})
const healthRecords = ref([])
const loading = ref(false)

// 获取健康状况类型
const getHealthStatusType = (status) => {
  const types = {
    'HEALTHY': 'success',
    'SUBHEALTH': 'warning',
    'SICK': 'danger',
    'SERIOUS': 'danger',
    'DANGER': 'danger',
    'WARNING': 'warning'
  }
  return types[status] || 'info'
}

// 获取健康状况文本
const getHealthStatusText = (status) => {
  const texts = {
    'HEALTHY': '健康',
    'SUBHEALTH': '亚健康',
    'SICK': '疾病',
    'SERIOUS': '重病',
    'DANGER': '危险',
    'WARNING': '预警'
  }
  return texts[status] || '未知'
}

// 获取照护等级文本
const getCareLevelText = (level) => {
  const texts = {
    1: '自理',
    2: '半自理',
    3: '不能自理'
  }
  return texts[level] || '未知'
}

// 获取老人详情
const getElderlyInfo = async () => {
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
      ElMessage.error(response.message || '获取老人信息失败')
    }
  } catch (error) {
    console.error('获取老人信息失败:', error)
    // 使用mock数据作为fallback
    const mockData = {
      id: elderlyId,
      name: '张明',
      gender: 1,
      age: 78,
      healthStatus: 'HEALTHY',
      careLevel: 1,
      phone: '13800138001',
      address: '北京市朝阳区长安街1号',
      avatar: null
    }
    elderlyInfo.value = mockData
    ElMessage.warning('API服务暂时不可用，显示模拟数据')
  } finally {
    loading.value = false
  }
}

// 获取健康记录
const getHealthRecords = async () => {
  if (!elderlyId) return
  try {
    const response = await getElderlyHealthRecords(elderlyId)
    if (response.code === 200) {
      healthRecords.value = response.data || []
    } else {
      console.error('获取健康记录失败:', response.message)
      ElMessage.warning(response.message || '获取健康记录失败')
    }
  } catch (error) {
    console.error('获取健康记录失败:', error)
    // 使用mock数据作为fallback
    const mockHealthRecords = [
      {
        id: 1,
        recordDate: '2024-01-15',
        recordType: 'CHECKUP',
        systolicPressure: 120,
        diastolicPressure: 80,
        heartRate: 72,
        bodyTemperature: 36.5,
        symptoms: '无异常症状',
        diagnosis: '健康状况良好',
        treatment: '继续保持健康生活方式',
        doctorAdvice: '定期体检，注意饮食'
      }
    ]
    healthRecords.value = mockHealthRecords
    ElMessage.info('健康记录API暂不可用，显示模拟数据')
  }
}

onMounted(() => {
  getElderlyInfo()
  getHealthRecords()
})
</script>

<style scoped>
.elderly-profile {
  padding: 20px;
}

.profile-header {
  margin-bottom: 20px;
}

.profile-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.info-content h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.info-item {
  margin: 5px 0;
  color: #606266;
}

.label {
  font-weight: bold;
  margin-right: 8px;
}

.health-records {
  margin-top: 20px;
}
</style>
