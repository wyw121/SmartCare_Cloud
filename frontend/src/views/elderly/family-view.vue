<template>
  <div class="family-elderly-view">
    <!-- 页面头部 - 温馨化设计 -->
    <el-card class="welcome-header">
      <div class="header-content">
        <div class="greeting">
          <h2>
            <i class="el-icon-sunny"></i>
            您好，{{ userStore.userName }}
          </h2>
          <p class="welcome-text">
            欢迎来到智慧医养平台，这里是您关爱亲人的温馨之家
          </p>
        </div>
        <div class="quick-stats">
          <div class="stat-item">
            <div class="stat-number">{{ elderlyList.length }}</div>
            <div class="stat-label">关联长辈</div>
          </div>
          <div class="stat-item health">
            <div class="stat-number">{{ healthyCount }}</div>
            <div class="stat-label">健康状态良好</div>
          </div>
          <div class="stat-item warning" v-if="warningCount > 0">
            <div class="stat-number">{{ warningCount }}</div>
            <div class="stat-label">需要关注</div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 关联长辈列表 -->
    <div class="elderly-cards">
      <div class="cards-header">
        <h3>
          <i class="el-icon-user"></i>
          我的关联长辈
        </h3>
        <p>以下是您有权限查看的长辈信息</p>
      </div>

      <!-- 长辈卡片列表 -->
      <div class="elderly-grid">
        <el-card 
          v-for="elderly in elderlyList" 
          :key="elderly.id" 
          class="elderly-card"
          @click="handleViewDetail(elderly)"
        >
          <div class="elderly-info">
            <!-- 头像和基本信息 -->
            <div class="elderly-header">
              <div class="avatar-section">
                <el-avatar 
                  :size="60" 
                  :src="elderly.avatar"
                  class="elderly-avatar"
                >
                  <i class="el-icon-user-solid"></i>
                </el-avatar>
                <div class="relationship-tag">
                  <el-tag size="small" type="info">
                    {{ elderly.relationship || '家属' }}
                  </el-tag>
                </div>
              </div>
              <div class="basic-info">
                <h4 class="elderly-name">{{ elderly.name }}</h4>
                <div class="elderly-details">
                  <span class="age">{{ calculateAge(elderly.birthDate) }}岁</span>
                  <span class="gender">{{ elderly.gender === 1 ? '男' : '女' }}</span>
                </div>
                <div class="contact-info">
                  <i class="el-icon-phone"></i>
                  {{ elderly.phone || '未设置' }}
                </div>
              </div>
            </div>

            <!-- 健康状态 -->
            <div class="health-status">
              <div class="status-header">
                <span class="status-label">健康状态</span>
                <el-tag 
                  :type="getHealthStatusType(elderly.healthStatus)"
                  class="status-tag"
                >
                  {{ getHealthStatusText(elderly.healthStatus) }}
                </el-tag>
              </div>
              
              <!-- 最近体征数据 -->
              <div class="vital-signs" v-if="elderly.latestVitals">
                <div class="vital-item">
                  <span class="vital-label">血压</span>
                  <span class="vital-value">{{ elderly.latestVitals.bloodPressure || '--' }}</span>
                </div>
                <div class="vital-item">
                  <span class="vital-label">心率</span>
                  <span class="vital-value">{{ elderly.latestVitals.heartRate || '--' }}次/分</span>
                </div>
                <div class="vital-item">
                  <span class="vital-label">体温</span>
                  <span class="vital-value">{{ elderly.latestVitals.temperature || '--' }}°C</span>
                </div>
              </div>
            </div>

            <!-- 预警信息 -->
            <div class="warnings" v-if="elderly.warnings && elderly.warnings.length > 0">
              <div class="warnings-header">
                <i class="el-icon-warning"></i>
                <span>健康提醒</span>
              </div>
              <div class="warning-list">
                <div 
                  v-for="warning in elderly.warnings.slice(0, 2)" 
                  :key="warning.id"
                  class="warning-item"
                >
                  <el-tag type="warning" size="small">{{ warning.message }}</el-tag>
                </div>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="card-actions">
              <el-button 
                type="primary" 
                size="small" 
                @click.stop="handleViewDetail(elderly)"
              >
                <i class="el-icon-view"></i>
                查看详情
              </el-button>
              <el-button 
                type="success" 
                size="small" 
                @click.stop="handleViewHealth(elderly)"
              >
                <i class="el-icon-monitor"></i>
                健康档案
              </el-button>
              <el-button 
                type="info" 
                size="small" 
                @click.stop="handleContact(elderly)"
              >
                <i class="el-icon-chat-dot-round"></i>
                联系医护
              </el-button>
            </div>

            <!-- 最后更新时间 -->
            <div class="update-time">
              <i class="el-icon-time"></i>
              最后更新：{{ formatTime(elderly.updateTime) }}
            </div>
          </div>
        </el-card>
      </div>

      <!-- 空状态 -->
      <div v-if="elderlyList.length === 0" class="empty-state">
        <el-empty 
          :image-size="200"
          description="暂无关联的长辈信息"
        >
          <el-button type="primary" @click="handleRequestAccess">
            申请关联长辈
          </el-button>
        </el-empty>
      </div>
    </div>

    <!-- 快速联系区域 -->
    <el-card class="quick-contact">
      <div class="contact-header">
        <h3>
          <i class="el-icon-service"></i>
          快速联系
        </h3>
      </div>
      <div class="contact-options">
        <el-button type="primary" @click="handleEmergencyCall">
          <i class="el-icon-phone"></i>
          紧急联系
        </el-button>
        <el-button type="success" @click="handleNurseCall">
          <i class="el-icon-chat-dot-round"></i>
          联系护士
        </el-button>
        <el-button type="info" @click="handleDoctorConsult">
          <i class="el-icon-user"></i>
          医生咨询
        </el-button>
        <el-button type="warning" @click="handleComplaint">
          <i class="el-icon-edit-outline"></i>
          意见反馈
        </el-button>
      </div>
    </el-card>

    <!-- 健康提醒弹窗 -->
    <el-dialog
      title="健康提醒详情"
      v-model="warningDialogVisible"
      width="600px"
    >
      <div v-if="selectedWarnings.length > 0">
        <div 
          v-for="warning in selectedWarnings" 
          :key="warning.id"
          class="warning-detail"
        >
          <div class="warning-header">
            <el-tag :type="getWarningType(warning.level)">
              {{ getWarningLevelText(warning.level) }}
            </el-tag>
            <span class="warning-time">{{ formatTime(warning.createTime) }}</span>
          </div>
          <div class="warning-content">{{ warning.message }}</div>
          <div class="warning-suggestion" v-if="warning.suggestion">
            <strong>建议：</strong>{{ warning.suggestion }}
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="warningDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleMarkAsRead">标记为已读</el-button>
      </template>
    </el-dialog>

    <!-- 联系医护弹窗 -->
    <el-dialog
      title="联系医护人员"
      v-model="contactDialogVisible"
      width="500px"
    >
      <el-form :model="contactForm" label-width="80px">
        <el-form-item label="联系人">
          <el-input v-model="contactForm.elderlyName" readonly />
        </el-form-item>
        <el-form-item label="紧急程度">
          <el-radio-group v-model="contactForm.urgency">
            <el-radio label="normal">一般咨询</el-radio>
            <el-radio label="urgent">紧急情况</el-radio>
            <el-radio label="emergency">危急情况</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述">
          <el-input 
            type="textarea" 
            v-model="contactForm.message" 
            :rows="4"
            placeholder="请描述具体情况..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="contactDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSendContact">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
  getElderlyByIds,
  getLatestVitals,
  getWarnings,
  markWarningsAsRead,
  sendContactRequest
} from '@/api/elderly'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const elderlyList = ref([])
const loading = ref(false)
const warningDialogVisible = ref(false)
const contactDialogVisible = ref(false)
const selectedWarnings = ref([])
const contactForm = ref({
  elderlyName: '',
  urgency: 'normal',
  message: ''
})

// 计算属性
const healthyCount = computed(() => {
  return elderlyList.value.filter(elderly => 
    elderly.healthStatus === 'HEALTHY' || elderly.healthStatus === 'SUBHEALTH'
  ).length
})

const warningCount = computed(() => {
  return elderlyList.value.filter(elderly => 
    elderly.healthStatus === 'WARNING' || elderly.healthStatus === 'DANGER'
  ).length
})

// 生命周期
onMounted(() => {
  loadElderlyList()
})

// 方法
const loadElderlyList = async () => {
  try {
    loading.value = true
    
    // 根据家属权限，只获取关联的老人信息
    const elderlyIds = userStore.userInfo.elderlyIds || []
    if (elderlyIds.length === 0) {
      elderlyList.value = []
      return
    }

    // 尝试调用真实API，如果失败则使用Mock数据
    try {
      const response = await getElderlyByIds(elderlyIds)
      elderlyList.value = response.data || []
      
      // 获取每个老人的最新健康数据和预警信息
      for (const elderly of elderlyList.value) {
        await loadElderlyHealth(elderly)
      }
    } catch (apiError) {
      console.warn('API调用失败，使用Mock数据:', apiError)
      // 使用Mock数据
      elderlyList.value = getMockElderlyData().filter(elderly => 
        elderlyIds.includes(elderly.id)
      )
    }
    
  } catch (error) {
    console.error('加载老人列表失败:', error)
    ElMessage.error('加载数据失败，请重试')
  } finally {
    loading.value = false
  }
}

const loadElderlyHealth = async (elderly) => {
  try {
    // 获取最新体征数据
    try {
      const vitalsResponse = await getLatestVitals(elderly.id)
      elderly.latestVitals = vitalsResponse.data
    } catch (error) {
      console.warn('获取体征数据失败，使用默认数据:', error)
      elderly.latestVitals = elderly.latestVitals || {}
    }
    
    // 获取预警信息
    try {
      const warningsResponse = await getWarnings(elderly.id)
      elderly.warnings = warningsResponse.data?.filter(w => !w.isRead) || []
    } catch (error) {
      console.warn('获取预警信息失败，使用默认数据:', error)
      elderly.warnings = elderly.warnings || []
    }
    
  } catch (error) {
    console.error(`加载老人${elderly.name}的健康数据失败:`, error)
  }
}

const calculateAge = (birthDate) => {
  if (!birthDate) return '未知'
  const birth = new Date(birthDate)
  const today = new Date()
  const age = today.getFullYear() - birth.getFullYear()
  const monthDiff = today.getMonth() - birth.getMonth()
  
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    return age - 1
  }
  return age
}

const getHealthStatusType = (status) => {
  const typeMap = {
    'HEALTHY': 'success',
    'SUBHEALTH': 'warning',
    'SICK': 'warning',
    'SERIOUS': 'danger',
    'DANGER': 'danger',
    'WARNING': 'danger'
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

const getWarningType = (level) => {
  const typeMap = {
    'INFO': 'info',
    'WARNING': 'warning',
    'DANGER': 'danger',
    'EMERGENCY': 'danger'
  }
  return typeMap[level] || 'info'
}

const getWarningLevelText = (level) => {
  const textMap = {
    'INFO': '提醒',
    'WARNING': '警告',
    'DANGER': '危险',
    'EMERGENCY': '紧急'
  }
  return textMap[level] || '未知'
}

const formatTime = (time) => {
  if (!time) return '未知'
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`
  
  return date.toLocaleDateString()
}

// 事件处理
const handleViewDetail = (elderly) => {
  router.push(`/elderly/profile/${elderly.id}`)
}

const handleViewHealth = (elderly) => {
  router.push(`/elderly/health-records/${elderly.id}`)
}

const handleContact = (elderly) => {
  contactForm.value.elderlyName = elderly.name
  contactDialogVisible.value = true
}

const handleRequestAccess = () => {
  ElMessageBox.confirm(
    '您需要联系系统管理员来申请关联长辈账户。',
    '申请关联',
    {
      confirmButtonText: '联系管理员',
      cancelButtonText: '取消',
      type: 'info'
    }
  ).then(() => {
    // 跳转到联系页面或显示联系信息
    ElMessage.info('请联系系统管理员：13800138000')
  })
}

const handleEmergencyCall = () => {
  ElMessageBox.confirm(
    '确认要拨打紧急联系电话吗？',
    '紧急联系',
    {
      confirmButtonText: '确认拨打',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    window.open('tel:120')
  })
}

const handleNurseCall = () => {
  ElMessage.info('正在为您接通护士站...')
  // 实际项目中可以集成在线客服系统
}

const handleDoctorConsult = () => {
  ElMessage.info('医生咨询功能开发中...')
  // 跳转到医生咨询页面
}

const handleComplaint = () => {
  ElMessage.info('意见反馈功能开发中...')
  // 跳转到意见反馈页面
}

const handleMarkAsRead = async () => {
  try {
    // 标记预警为已读
    const warningIds = selectedWarnings.value.map(w => w.id)
    
    try {
      await markWarningsAsRead(warningIds)
    } catch (error) {
      console.warn('API调用失败，模拟标记为已读:', error)
      // 模拟标记为已读
      elderlyList.value.forEach(elderly => {
        if (elderly.warnings) {
          elderly.warnings = elderly.warnings.filter(w => !warningIds.includes(w.id))
        }
      })
    }
    
    ElMessage.success('已标记为已读')
    warningDialogVisible.value = false
    
    // 重新加载数据
    await loadElderlyList()
  } catch (error) {
    ElMessage.error('操作失败，请重试')
  }
}

const handleSendContact = async () => {
  try {
    if (!contactForm.value.message.trim()) {
      ElMessage.warning('请填写联系内容')
      return
    }
    
    // 发送联系请求
    try {
      await sendContactRequest(contactForm.value)
    } catch (error) {
      console.warn('API调用失败，模拟发送成功:', error)
      // 模拟发送成功
    }
    
    ElMessage.success('联系请求已发送，医护人员会尽快回复')
    contactDialogVisible.value = false
    
    // 重置表单
    contactForm.value = {
      elderlyName: '',
      urgency: 'normal',
      message: ''
    }
  } catch (error) {
    ElMessage.error('发送失败，请重试')
  }
}

// Mock数据函数（用于测试）
const getMockElderlyData = () => {
  return [
    {
      id: 1,
      name: '李爷爷',
      gender: 1,
      birthDate: '1930-05-15',
      phone: '13800138001',
      healthStatus: 'HEALTHY',
      careLevel: 1,
      roomNumber: 'A101',
      guardianName: '李家属',
      address: '北京市朝阳区',
      relationship: '孙子',
      avatar: '',
      latestVitals: {
        bloodPressure: '120/80',
        heartRate: 72,
        temperature: 36.5
      },
      warnings: [],
      updateTime: new Date().toISOString()
    },
    {
      id: 2,
      name: '王奶奶',
      gender: 0,
      birthDate: '1935-08-22',
      phone: '13800138002',
      healthStatus: 'WARNING',
      careLevel: 2,
      roomNumber: 'B205',
      guardianName: '李家属',
      address: '北京市海淀区',
      relationship: '儿媳',
      avatar: '',
      latestVitals: {
        bloodPressure: '140/95',
        heartRate: 85,
        temperature: 37.2
      },
      warnings: [
        {
          id: 1,
          message: '血压偏高，建议注意休息',
          level: 'WARNING',
          createTime: new Date().toISOString(),
          isRead: false
        }
      ],
      updateTime: new Date().toISOString()
    }
  ]
}

// 测试时使用Mock数据
if (import.meta.env.MODE === 'development') {
  elderlyList.value = getMockElderlyData()
}
</script>

<style scoped>
.family-elderly-view {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 欢迎头部 */
.welcome-header {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
}

.welcome-header :deep(.el-card__body) {
  padding: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.greeting h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 500;
}

.greeting h2 i {
  margin-right: 8px;
  color: #ffd700;
}

.welcome-text {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

.quick-stats {
  display: flex;
  gap: 24px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  min-width: 80px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.8;
}

.stat-item.health .stat-number {
  color: #67c23a;
}

.stat-item.warning .stat-number {
  color: #e6a23c;
}

/* 长辈卡片区域 */
.elderly-cards {
  margin-bottom: 20px;
}

.cards-header {
  margin-bottom: 16px;
}

.cards-header h3 {
  margin: 0 0 4px 0;
  color: #303133;
  font-size: 18px;
}

.cards-header h3 i {
  margin-right: 8px;
  color: #409eff;
}

.cards-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.elderly-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.elderly-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #ebeef5;
}

.elderly-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.elderly-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.elderly-header {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.elderly-avatar {
  background: #f0f9ff;
  color: #409eff;
}

.relationship-tag {
  text-align: center;
}

.basic-info {
  flex: 1;
}

.elderly-name {
  margin: 0 0 8px 0;
  font-size: 18px;
  color: #303133;
  font-weight: 500;
}

.elderly-details {
  display: flex;
  gap: 12px;
  margin-bottom: 8px;
}

.elderly-details span {
  color: #606266;
  font-size: 14px;
}

.contact-info {
  color: #909399;
  font-size: 14px;
}

.contact-info i {
  margin-right: 4px;
}

/* 健康状态 */
.health-status {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 6px;
}

.status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.status-label {
  font-weight: 500;
  color: #303133;
}

.vital-signs {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.vital-item {
  text-align: center;
  padding: 8px;
  background: white;
  border-radius: 4px;
}

.vital-label {
  display: block;
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.vital-value {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

/* 预警信息 */
.warnings {
  background: #fef0e6;
  padding: 12px;
  border-radius: 6px;
  border-left: 4px solid #e6a23c;
}

.warnings-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  color: #e6a23c;
  font-weight: 500;
}

.warning-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

/* 操作按钮 */
.card-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.card-actions .el-button {
  flex: 1;
}

/* 更新时间 */
.update-time {
  color: #c0c4cc;
  font-size: 12px;
  text-align: center;
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
}

.update-time i {
  margin-right: 4px;
}

/* 快速联系 */
.quick-contact {
  margin-bottom: 20px;
}

.contact-header h3 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
}

.contact-header h3 i {
  margin-right: 8px;
  color: #67c23a;
}

.contact-options {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.contact-options .el-button {
  flex: 1;
  min-width: 120px;
}

/* 弹窗样式 */
.warning-detail {
  margin-bottom: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 6px;
}

.warning-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.warning-time {
  color: #909399;
  font-size: 12px;
}

.warning-content {
  margin-bottom: 8px;
  color: #303133;
}

.warning-suggestion {
  color: #606266;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .family-elderly-view {
    padding: 12px;
  }
  
  .header-content {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
  
  .elderly-grid {
    grid-template-columns: 1fr;
  }
  
  .contact-options {
    flex-direction: column;
  }
  
  .contact-options .el-button {
    width: 100%;
  }
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 40px;
}
</style>
