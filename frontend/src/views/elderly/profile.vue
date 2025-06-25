<template>
  <div class="elderly-profile">
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
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const elderlyId = route.params.id

const elderlyInfo = ref({})
const healthRecords = ref([])

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
  try {
    // 这里应该调用真实的API
    // const { data } = await getElderlyById(elderlyId)
    
    // 模拟数据
    elderlyInfo.value = {
      name: '张三',
      gender: 1,
      age: 74,
      idCard: '110101195001011234',
      phone: '13800138001',
      address: '北京市朝阳区某某街道1号',
      emergencyContactName: '张小明',
      emergencyContactPhone: '13900139001',
      healthStatus: 'HEALTHY',
      careLevel: 1,
      insuranceType: '城镇职工医保',
      medicalHistory: '高血压病史10年',
      allergyHistory: '青霉素过敏',
      familyDoctorName: '刘医生'
    }
  } catch (error) {
    console.error('获取老人信息失败:', error)
    ElMessage.error('获取老人信息失败')
  }
}

// 获取健康记录
const getHealthRecords = async () => {
  try {
    // 这里应该调用真实的API
    // const { data } = await getHealthRecordsByElderlyId(elderlyId)
    
    // 模拟数据
    healthRecords.value = [
      {
        recordDate: '2024-01-15',
        recordType: '体检',
        doctorName: '刘医生',
        diagnosis: '血压偏高，建议注意饮食',
        abnormalStatus: 1
      },
      {
        recordDate: '2024-01-10',
        recordType: '门诊',
        doctorName: '陈医生',
        diagnosis: '感冒，开具感冒药',
        abnormalStatus: 0
      }
    ]
  } catch (error) {
    console.error('获取健康记录失败:', error)
    ElMessage.error('获取健康记录失败')
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
