<template>
  <div class="health-records">
    <!-- 页面头部 -->
    <el-card class="page-header">
      <div class="header-content">
        <div class="patient-info">
          <el-avatar :size="60" :src="elderlyInfo.avatar">
            <span>{{ elderlyInfo.name?.charAt(0) }}</span>
          </el-avatar>
          <div class="info-details">
            <h2>{{ elderlyInfo.name }} - 健康档案</h2>
            <div class="meta-info">
              <el-tag v-if="elderlyInfo.gender !== undefined" :type="elderlyInfo.gender === 1 ? 'primary' : 'danger'">
                {{ elderlyInfo.gender === 1 ? '男' : '女' }}
              </el-tag>
              <el-tag type="info">{{ elderlyInfo.age }}岁</el-tag>
              <el-tag :type="getHealthStatusType(elderlyInfo.healthStatus)">
                {{ getHealthStatusText(elderlyInfo.healthStatus) }}
              </el-tag>
              <el-tag type="warning">照护等级{{ elderlyInfo.careLevel }}</el-tag>
            </div>
            <p class="contact-info">
              <i class="el-icon-phone"></i> {{ elderlyInfo.phone }} | 
              <i class="el-icon-location"></i> {{ elderlyInfo.address }}
            </p>
          </div>
        </div>
        <div class="actions">
          <el-button type="primary" @click="handleAddRecord">
            <i class="el-icon-plus"></i>
            添加健康记录
          </el-button>
          <el-button type="success" @click="handleExportRecords">
            <i class="el-icon-download"></i>
            导出档案
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-document" style="color: #409EFF;"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalRecords }}</div>
              <div class="stat-label">总记录数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-date" style="color: #67C23A;"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.recentRecords }}</div>
              <div class="stat-label">近30天记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-warning" style="color: #E6A23C;"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.warningRecords }}</div>
              <div class="stat-label">异常记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-user" style="color: #F56C6C;"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.doctorCount }}</div>
              <div class="stat-label">就诊医生</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 主要内容 -->
    <el-row :gutter="20" class="main-content">
      <!-- 左侧 - 记录列表 -->
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>健康记录</span>
              <div class="filter-buttons">
                <el-radio-group v-model="currentRecordType" size="small" @change="handleTypeChange">
                  <el-radio-button :label="0">全部</el-radio-button>
                  <el-radio-button :label="1">体检记录</el-radio-button>
                  <el-radio-button :label="2">就诊记录</el-radio-button>
                  <el-radio-button :label="3">用药记录</el-radio-button>
                  <el-radio-button :label="4">监测记录</el-radio-button>
                </el-radio-group>
              </div>
            </div>
          </template>

          <div class="timeline-container">
            <el-timeline v-if="healthRecords.length > 0">
              <el-timeline-item
                v-for="record in healthRecords"
                :key="record.id"
                :timestamp="formatDateTime(record.recordDate)"
                :type="getRecordTimelineType(record.recordType)"
                :icon="getRecordIcon(record.recordType)"
                placement="top"
              >
                <el-card class="record-card" shadow="hover">
                  <div class="record-header">
                    <div class="record-title">
                      <el-tag :type="getRecordTypeTag(record.recordType)" size="small">
                        {{ getRecordTypeText(record.recordType) }}
                      </el-tag>
                      <span class="record-name">{{ record.title || '健康记录' }}</span>
                    </div>
                    <div class="record-actions">
                      <el-button text size="small" @click="viewRecord(record)">
                        <i class="el-icon-view"></i> 查看
                      </el-button>
                      <el-button text size="small" @click="editRecord(record)">
                        <i class="el-icon-edit"></i> 编辑
                      </el-button>
                      <el-button text size="small" type="danger" @click="deleteRecord(record)">
                        <i class="el-icon-delete"></i> 删除
                      </el-button>
                    </div>
                  </div>
                  
                  <div class="record-content">
                    <div class="record-info">
                      <div v-if="record.doctorName" class="doctor-info">
                        <i class="el-icon-user"></i>
                        <span>医生：{{ record.doctorName }}</span>
                      </div>
                      <div v-if="record.diagnosis" class="diagnosis">
                        <i class="el-icon-document"></i>
                        <span>诊断：{{ record.diagnosis }}</span>
                      </div>
                      <div v-if="record.description" class="description">
                        {{ record.description }}
                      </div>
                    </div>
                    
                    <!-- 生命体征 -->
                    <div v-if="hasVitalSigns(record)" class="vital-signs">
                      <div class="vital-item" v-if="record.systolicPressure">
                        <span class="vital-label">血压:</span>
                        <span class="vital-value">{{ record.systolicPressure }}/{{ record.diastolicPressure }} mmHg</span>
                      </div>
                      <div class="vital-item" v-if="record.heartRate">
                        <span class="vital-label">心率:</span>
                        <span class="vital-value">{{ record.heartRate }} 次/分</span>
                      </div>
                      <div class="vital-item" v-if="record.bodyTemperature">
                        <span class="vital-label">体温:</span>
                        <span class="vital-value">{{ record.bodyTemperature }} ℃</span>
                      </div>
                      <div class="vital-item" v-if="record.bloodGlucose">
                        <span class="vital-label">血糖:</span>
                        <span class="vital-value">{{ record.bloodGlucose }} mmol/L</span>
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>
            
            <el-empty v-else description="暂无健康记录" />
          </div>

          <!-- 分页 -->
          <div class="pagination-container" v-if="total > 0">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :total="total"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-card>
      </el-col>

      <!-- 右侧 - 信息面板 -->
      <el-col :span="8">
        <!-- 最新生命体征 -->
        <el-card class="info-panel">
          <template #header>
            <span>最新生命体征</span>
          </template>
          <div v-if="latestVitalSigns" class="vital-signs-panel">
            <div class="vital-item-large">
              <div class="vital-icon">
                <i class="el-icon-monitor" style="color: #F56C6C;"></i>
              </div>
              <div class="vital-content">
                <div class="vital-value-large">{{ latestVitalSigns.systolicPressure }}/{{ latestVitalSigns.diastolicPressure }}</div>
                <div class="vital-label">血压 (mmHg)</div>
              </div>
            </div>
            
            <div class="vital-item-large">
              <div class="vital-icon">
                <i class="el-icon-stopwatch" style="color: #E6A23C;"></i>
              </div>
              <div class="vital-content">
                <div class="vital-value-large">{{ latestVitalSigns.heartRate }}</div>
                <div class="vital-label">心率 (次/分)</div>
              </div>
            </div>
            
            <div class="vital-item-large">
              <div class="vital-icon">
                <i class="el-icon-thermometer" style="color: #67C23A;"></i>
              </div>
              <div class="vital-content">
                <div class="vital-value-large">{{ latestVitalSigns.bodyTemperature }}</div>
                <div class="vital-label">体温 (℃)</div>
              </div>
            </div>
            
            <div class="vital-item-large">
              <div class="vital-icon">
                <i class="el-icon-sugar" style="color: #409EFF;"></i>
              </div>
              <div class="vital-content">
                <div class="vital-value-large">{{ latestVitalSigns.bloodGlucose }}</div>
                <div class="vital-label">血糖 (mmol/L)</div>
              </div>
            </div>
            
            <div class="update-time">
              更新时间：{{ formatDateTime(latestVitalSigns.recordDate) }}
            </div>
          </div>
          <el-empty v-else description="暂无生命体征数据" :image-size="100" />
        </el-card>

        <!-- 健康趋势 -->
        <el-card class="info-panel">
          <template #header>
            <span>健康趋势</span>
          </template>
          <div class="trend-container">
            <div class="trend-item">
              <div class="trend-label">健康状况</div>
              <div class="trend-value">
                <el-tag :type="getHealthStatusType(elderlyInfo.healthStatus)">
                  {{ getHealthStatusText(elderlyInfo.healthStatus) }}
                </el-tag>
              </div>
            </div>
            <div class="trend-item">
              <div class="trend-label">照护等级</div>
              <div class="trend-value">等级 {{ elderlyInfo.careLevel }}</div>
            </div>
            <div class="trend-item">
              <div class="trend-label">最近就诊</div>
              <div class="trend-value">{{ getLastVisitTime() }}</div>
            </div>
          </div>
        </el-card>

        <!-- 紧急联系人 -->
        <el-card class="info-panel">
          <template #header>
            <span>紧急联系人</span>
          </template>
          <div v-if="emergencyContacts.length > 0" class="contact-list">
            <div v-for="contact in emergencyContacts" :key="contact.id" class="contact-item">
              <div class="contact-info">
                <div class="contact-name">{{ contact.name }}</div>
                <div class="contact-relation">{{ contact.relationship }}</div>
              </div>
              <div class="contact-actions">
                <el-button text size="small" @click="callContact(contact.phone)">
                  <i class="el-icon-phone"></i> {{ contact.phone }}
                </el-button>
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无紧急联系人" :image-size="100" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加/编辑健康记录对话框 -->
    <health-record-form
      v-model:visible="recordFormVisible"
      :elderly-id="elderlyId"
      :record="currentRecord"
      @success="handleRecordSuccess"
    />

    <!-- 查看记录详情对话框 -->
    <health-record-detail
      v-model:visible="recordDetailVisible"
      :record="currentRecord"
    />
  </div>
</template>

<script setup>
import { deleteHealthRecord, getElderlyById, getElderlyHealthRecords } from '@/api/elderly'
import HealthRecordDetail from '@/components/health/HealthRecordDetail.vue'
import HealthRecordForm from '@/components/health/HealthRecordForm.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 响应式数据
const elderlyId = ref(route.params.id)
const elderlyInfo = ref({})
const healthRecords = ref([])
const emergencyContacts = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const currentRecordType = ref(0)

// 对话框状态
const recordFormVisible = ref(false)
const recordDetailVisible = ref(false)
const currentRecord = ref(null)

// 统计数据
const stats = reactive({
  totalRecords: 0,
  recentRecords: 0,
  warningRecords: 0,
  doctorCount: 0
})

// 最新生命体征
const latestVitalSigns = ref(null)

// 初始化
onMounted(() => {
  loadElderlyInfo()
  loadHealthRecords()
  loadEmergencyContacts()
})

// 加载老人信息
const loadElderlyInfo = async () => {
  try {
    const { data } = await getElderlyById(elderlyId.value)
    elderlyInfo.value = data
    console.log('老人信息:', data)
  } catch (error) {
    console.error('获取老人信息失败:', error)
    ElMessage.error('获取老人信息失败')
  }
}

// 加载健康记录
const loadHealthRecords = async () => {
  try {
    loading.value = true
    const params = {
      current: currentPage.value,
      size: pageSize.value,
      recordType: currentRecordType.value === 0 ? undefined : currentRecordType.value
    }
    
    const response = await getElderlyHealthRecords(elderlyId.value, params)
    
    if (response.code === 200) {
      const data = response.data
      healthRecords.value = data.records || data.data || []
      total.value = data.total || 0
      
      // 更新统计数据
      updateStats()
      
      // 获取最新生命体征
      findLatestVitalSigns()
    } else {
      ElMessage.error('获取健康记录失败：' + response.message)
      // 如果后端失败，显示模拟数据以便测试
      loadMockData()
    }
  } catch (error) {
    console.error('获取健康记录失败:', error)
    ElMessage.warning('后端服务连接失败，显示模拟数据')
    // 如果后端失败，显示模拟数据以便测试
    loadMockData()
  } finally {
    loading.value = false
  }
}

// 加载模拟数据（用于演示和测试）
const loadMockData = () => {
  healthRecords.value = [
    {
      id: 1,
      recordType: 1,
      recordDate: '2024-01-15',
      title: '常规体检',
      doctorName: '李医生',
      bloodPressureHigh: 120,
      bloodPressureLow: 80,
      heartRate: 75,
      temperature: 36.5,
      bloodSugar: 5.2,
      weight: 65.5,
      symptoms: '无明显症状',
      diagnosis: '身体健康',
      treatment: '继续保持良好生活习惯',
      medication: '无',
      description: '年度常规体检，各项指标正常',
      riskLevel: 0,
      isAbnormal: false,
      remarks: '建议每半年复查一次',
      createTime: '2024-01-15 09:00:00'
    },
    {
      id: 2,
      recordType: 2,
      recordDate: '2024-01-20',
      title: '感冒就诊',
      doctorName: '王医生',
      bloodPressureHigh: 125,
      bloodPressureLow: 82,
      heartRate: 78,
      temperature: 37.2,
      symptoms: '咳嗽、鼻塞、轻微发热',
      diagnosis: '普通感冒',
      treatment: '多休息，多喝水',
      medication: '感冒清热颗粒，一日三次',
      description: '普通感冒症状，建议观察',
      riskLevel: 1,
      isAbnormal: true,
      remarks: '如症状加重请及时就医',
      createTime: '2024-01-20 14:30:00'
    }
  ]
  total.value = 2
  
  // 更新统计数据
  updateStats()
  
  // 获取最新生命体征
  findLatestVitalSigns()
}

// 加载紧急联系人
const loadEmergencyContacts = async () => {
  try {
    // 这里应该调用获取紧急联系人的API
    // const response = await getEmergencyContacts(elderlyId.value)
    // 暂时使用模拟数据
    emergencyContacts.value = [
      { id: 1, name: '李明', relationship: '儿子', phone: '13800138001' },
      { id: 2, name: '王芳', relationship: '女儿', phone: '13800138002' }
    ]
  } catch (error) {
    console.error('获取紧急联系人失败:', error)
  }
}

// 更新统计数据
const updateStats = () => {
  stats.totalRecords = healthRecords.value.length
  
  const now = new Date()
  const thirtyDaysAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
  
  stats.recentRecords = healthRecords.value.filter(record => 
    new Date(record.recordDate) >= thirtyDaysAgo
  ).length
  
  stats.warningRecords = healthRecords.value.filter(record => 
    record.isAbnormal || record.riskLevel > 1
  ).length
  
  const doctorIds = new Set(healthRecords.value.map(record => record.doctorId).filter(id => id))
  stats.doctorCount = doctorIds.size
}

// 查找最新生命体征
const findLatestVitalSigns = () => {
  const recordsWithVitals = healthRecords.value.filter(record => hasVitalSigns(record))
  if (recordsWithVitals.length > 0) {
    latestVitalSigns.value = recordsWithVitals[0] // 假设列表已按日期排序
  }
}

// 判断记录是否包含生命体征
const hasVitalSigns = (record) => {
  return record.systolicPressure || record.heartRate || record.bodyTemperature || record.bloodGlucose
}

// 工具函数
const getHealthStatusType = (status) => {
  const typeMap = {
    'HEALTHY': 'success',
    'SUBHEALTH': 'warning',
    'SICK': 'danger',
    'SERIOUS': 'danger',
    'DANGER': 'danger',
    'WARNING': 'warning'
  }
  return typeMap[status] || 'info'
}

const getHealthStatusText = (status) => {
  const textMap = {
    'HEALTHY': '健康',
    'SUBHEALTH': '亚健康',
    'SICK': '疾病',
    'SERIOUS': '重病',
    'DANGER': '危险',
    'WARNING': '预警'
  }
  return textMap[status] || '未知'
}

const getRecordTypeText = (type) => {
  const textMap = {
    1: '体检记录',
    2: '就诊记录',
    3: '用药记录',
    4: '监测记录'
  }
  return textMap[type] || '其他记录'
}

const getRecordTypeTag = (type) => {
  const tagMap = {
    1: 'success',
    2: 'primary',
    3: 'warning',
    4: 'info'
  }
  return tagMap[type] || 'info'
}

const getRecordIcon = (type) => {
  const iconMap = {
    1: 'el-icon-document',
    2: 'el-icon-user',
    3: 'el-icon-medicine',
    4: 'el-icon-monitor'
  }
  return iconMap[type] || 'el-icon-document'
}

const getRecordTimelineType = (type) => {
  const typeMap = {
    1: 'success',
    2: 'primary',
    3: 'warning',
    4: 'info'
  }
  return typeMap[type] || 'info'
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}

const getLastVisitTime = () => {
  const visitRecords = healthRecords.value.filter(record => record.recordType === 2)
  if (visitRecords.length > 0) {
    return formatDateTime(visitRecords[0].recordDate)
  }
  return '暂无记录'
}

// 事件处理
const handleTypeChange = () => {
  currentPage.value = 1
  loadHealthRecords()
}

const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
  loadHealthRecords()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  loadHealthRecords()
}

const handleAddRecord = () => {
  currentRecord.value = null
  recordFormVisible.value = true
}

const viewRecord = (record) => {
  currentRecord.value = record
  recordDetailVisible.value = true
}

const editRecord = (record) => {
  currentRecord.value = record
  recordFormVisible.value = true
}

const deleteRecord = async (record) => {
  try {
    await ElMessageBox.confirm('确定要删除这条健康记录吗？', '确认删除', {
      type: 'warning'
    })
    
    await deleteHealthRecord(record.id)
    ElMessage.success('删除成功')
    loadHealthRecords()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleRecordSuccess = () => {
  loadHealthRecords()
}

const handleExportRecords = () => {
  ElMessage.info('导出功能开发中...')
}

const callContact = (phone) => {
  window.open(`tel:${phone}`)
}
</script>

<style scoped>
.health-records {
  padding: 20px;
}

/* 页面头部 */
.page-header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.patient-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.info-details h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.meta-info {
  margin: 10px 0;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.contact-info {
  margin: 8px 0 0 0;
  color: #606266;
  font-size: 14px;
}

.contact-info i {
  margin-right: 4px;
}

/* 统计卡片 */
.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  height: 100px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  font-size: 36px;
  margin-right: 15px;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

/* 主要内容区域 */
.main-content {
  gap: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 时间线样式 */
.timeline-container {
  max-height: 600px;
  overflow-y: auto;
}

.record-card {
  margin-bottom: 10px;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.record-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.record-name {
  font-weight: 500;
  color: #303133;
}

.record-content {
  line-height: 1.6;
}

.record-info > div {
  margin-bottom: 6px;
  color: #606266;
}

.record-info i {
  margin-right: 4px;
  color: #909399;
}

.description {
  color: #303133;
  margin-top: 8px;
}

/* 生命体征 */
.vital-signs {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-top: 10px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.vital-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.vital-label {
  color: #909399;
  font-size: 13px;
}

.vital-value {
  color: #303133;
  font-weight: 500;
}

/* 右侧信息面板 */
.info-panel {
  margin-bottom: 20px;
}

.vital-item-large {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.vital-item-large:last-child {
  border-bottom: none;
}

.vital-icon {
  font-size: 24px;
  margin-right: 15px;
}

.vital-content {
  flex: 1;
}

.vital-value-large {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.vital-label {
  color: #909399;
  font-size: 14px;
  margin-top: 4px;
}

.update-time {
  text-align: center;
  color: #909399;
  font-size: 12px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

/* 健康趋势 */
.trend-container > .trend-item + .trend-item {
  margin-top: 15px;
}

.trend-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.trend-item:last-child {
  border-bottom: none;
}

.trend-label {
  color: #606266;
  font-size: 14px;
}

.trend-value {
  color: #303133;
  font-weight: 500;
}

/* 联系人列表 */
.contact-list > .contact-item + .contact-item {
  margin-top: 10px;
}

.contact-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.contact-item:last-child {
  border-bottom: none;
}

.contact-name {
  font-weight: 500;
  color: #303133;
}

.contact-relation {
  color: #909399;
  font-size: 13px;
}

/* 分页 */
.pagination-container {
  margin-top: 20px;
  text-align: center;
}

/* 响应式 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 15px;
  }
  
  .patient-info {
    flex-direction: column;
    text-align: center;
  }
  
  .stats-row .el-col {
    margin-bottom: 10px;
  }
  
  .main-content .el-col {
    margin-bottom: 20px;
  }
}
</style>
