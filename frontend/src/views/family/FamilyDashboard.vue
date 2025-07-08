<template>
  <div class="family-dashboard-container">
    <!-- 顶部欢迎信息 -->
    <div class="welcome-section">
      <el-card class="welcome-card">
        <div class="welcome-content">
          <div class="welcome-info">
            <h2>{{ greeting }}，{{ userStore.userName }}</h2>
            <p class="welcome-subtitle">您可以查看关联老人的基本信息和健康状况</p>
          </div>
          <div class="quick-actions">
            <el-button type="primary" icon="View" @click="viewElderlyList">
              查看老人信息
            </el-button>
            <el-button type="warning" icon="Warning" @click="checkEmergencyAlerts">
              紧急预警
            </el-button>
            <el-button type="success" icon="Phone" @click="emergencyContact">
              紧急联系
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 关联老人概览 -->
    <div class="elderly-overview">
      <el-card>
        <template #header>
          <div class="card-header">
            <span class="card-title">关联老人概览</span>
            <el-button size="small" @click="refreshElderlyList">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </template>
        <div v-if="elderlyList.length === 0" class="empty-state">
          <el-empty description="暂无关联的老人信息">
            <el-button type="primary" @click="contactAdmin">联系管理员</el-button>
          </el-empty>
        </div>
        <div v-else class="elderly-grid">
          <div 
            v-for="elderly in elderlyList" 
            :key="elderly.elderlyId"
            class="elderly-card"
            @click="viewElderlyDetail(elderly)"
          >
            <div class="elderly-info">
              <div class="elderly-header">
                <h3>{{ elderly.name }}</h3>
                <el-tag 
                  :type="getStatusTagType(elderly.status)"
                  size="small"
                >
                  {{ elderly.status }}
                </el-tag>
              </div>
              <div class="elderly-details">
                <p><span class="label">年龄：</span>{{ elderly.age }}岁</p>
                <p><span class="label">性别：</span>{{ elderly.gender }}</p>
                <p><span class="label">房间：</span>{{ elderly.room }}</p>
                <p><span class="label">关系：</span>{{ elderly.relationship }}</p>
              </div>
              <div class="health-summary">
                <el-alert
                  v-if="elderly.alertLevel && elderly.alertLevel !== '无'"
                  :title="`健康预警：${elderly.alertLevel}`"
                  :type="getAlertType(elderly.alertLevel)"
                  size="small"
                  show-icon
                  :closable="false"
                />
                <p class="health-text">{{ elderly.healthSummary || '暂无健康信息' }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 最近预警信息 -->
    <div class="recent-alerts">
      <el-card>
        <template #header>
          <div class="card-header">
            <span class="card-title">最近预警信息</span>
            <el-button size="small" @click="viewAllAlerts">查看全部</el-button>
          </div>
        </template>
        <div v-if="recentAlerts.length === 0" class="empty-state">
          <p>近期暂无预警信息</p>
        </div>
        <div v-else class="alerts-list">
          <div 
            v-for="alert in recentAlerts" 
            :key="alert.warningId"
            class="alert-item"
          >
            <div class="alert-content">
              <div class="alert-header">
                <el-tag 
                  :type="getAlertTagType(alert.warningLevel)"
                  size="small"
                >
                  {{ alert.warningLevel }}
                </el-tag>
                <span class="alert-time">{{ formatTime(alert.warningTime) }}</span>
              </div>
              <p class="alert-message">{{ alert.warningMessage }}</p>
              <p class="alert-type">类型：{{ alert.warningType }}</p>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 快捷功能 -->
    <div class="quick-functions">
      <el-row :gutter="16">
        <el-col :span="8">
          <el-card class="function-card" @click="scheduleVisit">
            <div class="function-content">
              <el-icon class="function-icon" color="#409EFF"><Calendar /></el-icon>
              <h4>探视预约</h4>
              <p>预约探视时间</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="function-card" @click="viewHealthSummary">
            <div class="function-content">
              <el-icon class="function-icon" color="#67C23A"><TrendCharts /></el-icon>
              <h4>健康概要</h4>
              <p>查看健康状况</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="function-card" @click="emergencyContact">
            <div class="function-content">
              <el-icon class="function-icon" color="#F56C6C"><Phone /></el-icon>
              <h4>紧急联系</h4>
              <p>紧急情况联系</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 家属须知 -->
    <div class="family-notice">
      <el-card>
        <template #header>
          <span class="card-title">家属须知</span>
        </template>
        <div class="notice-content">
          <el-alert
            title="隐私保护提醒"
            type="info"
            show-icon
            :closable="false"
          >
            <p>为保护老人隐私，您只能查看与您有关联关系的老人的基本信息和健康概要。</p>
            <p>详细医疗记录和敏感信息需要联系医生或管理员获取。</p>
          </el-alert>
          <div class="notice-list">
            <h4>使用说明：</h4>
            <ul>
              <li>您可以查看关联老人的基本信息、健康概要和紧急预警</li>
              <li>如需查看详细医疗记录，请联系医生或护理人员</li>
              <li>紧急情况下可使用紧急联系功能立即通知工作人员</li>
              <li>探视需要提前预约，请合理安排探视时间</li>
              <li>您的所有操作都会被记录，请合理使用系统功能</li>
            </ul>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 紧急联系对话框 -->
    <el-dialog
      v-model="emergencyDialogVisible"
      title="紧急联系"
      width="500px"
      @close="resetEmergencyForm"
    >
      <el-form ref="emergencyFormRef" :model="emergencyForm" :rules="emergencyRules">
        <el-form-item label="相关老人" prop="elderlyId">
          <el-select 
            v-model="emergencyForm.elderlyId" 
            placeholder="请选择相关老人"
            style="width: 100%"
          >
            <el-option
              v-for="elderly in elderlyList"
              :key="elderly.elderlyId"
              :label="elderly.name"
              :value="elderly.elderlyId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="紧急程度" prop="urgencyLevel">
          <el-radio-group v-model="emergencyForm.urgencyLevel">
            <el-radio label="urgent">紧急</el-radio>
            <el-radio label="normal">一般</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="联系事由" prop="message">
          <el-input
            v-model="emergencyForm.message"
            type="textarea"
            :rows="4"
            placeholder="请详细描述需要联系的事由..."
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="emergencyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEmergencyContact" :loading="submitting">
          发送联系
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { useUserStore } from '@/store/user'
import {
    getEmergencyAlertsForFamily,
    getFamilyElderlyList,
    handleEmergencyContact,
    logFamilyAction
} from '@/utils/family-permission'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const elderlyList = ref([])
const recentAlerts = ref([])
const emergencyDialogVisible = ref(false)
const submitting = ref(false)

// 表单数据
const emergencyForm = reactive({
  elderlyId: '',
  urgencyLevel: 'normal',
  message: ''
})

// 表单验证规则
const emergencyRules = {
  elderlyId: [
    { required: true, message: '请选择相关老人', trigger: 'change' }
  ],
  message: [
    { required: true, message: '请输入联系事由', trigger: 'blur' },
    { min: 10, message: '请详细描述事由，至少输入10个字符', trigger: 'blur' }
  ]
}

// 计算属性
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return '上午好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

// 生命周期
onMounted(() => {
  loadData()
})

// 方法
const loadData = async () => {
  try {
    await Promise.all([
      loadElderlyList(),
      loadRecentAlerts()
    ])
  } catch (error) {
    ElMessage.error('加载数据失败：' + error.message)
  }
}

const loadElderlyList = async () => {
  try {
    elderlyList.value = await getFamilyElderlyList(userStore.userId)
    
    // 记录操作日志
    await logFamilyAction('view_elderly_list', {
      count: elderlyList.value.length
    })
  } catch (error) {
    ElMessage.error('获取老人列表失败：' + error.message)
    elderlyList.value = []
  }
}

const loadRecentAlerts = async () => {
  try {
    const allAlerts = []
    
    // 获取所有关联老人的预警信息
    for (const elderly of elderlyList.value) {
      const alerts = await getEmergencyAlertsForFamily(elderly.elderlyId, userStore.userId)
      allAlerts.push(...alerts)
    }
    
    // 按时间排序，取最近5条
    recentAlerts.value = allAlerts
      .sort((a, b) => new Date(b.warningTime) - new Date(a.warningTime))
      .slice(0, 5)
      
  } catch (error) {
    console.error('获取预警信息失败:', error)
    recentAlerts.value = []
  }
}

const refreshElderlyList = () => {
  loadElderlyList()
}

const viewElderlyList = () => {
  router.push('/family/elderly-list')
}

const viewElderlyDetail = (elderly) => {
  router.push(`/family/elderly-detail/${elderly.elderlyId}`)
}

const checkEmergencyAlerts = () => {
  router.push('/family/emergency-alerts')
}

const viewAllAlerts = () => {
  router.push('/family/emergency-alerts')
}

const scheduleVisit = () => {
  router.push('/family/visit-schedule')
}

const viewHealthSummary = () => {
  if (elderlyList.value.length === 1) {
    router.push(`/family/health-summary/${elderlyList.value[0].elderlyId}`)
  } else {
    router.push('/family/health-summary')
  }
}

const emergencyContact = () => {
  if (elderlyList.value.length === 0) {
    ElMessage.warning('暂无关联的老人信息，无法进行紧急联系')
    return
  }
  emergencyDialogVisible.value = true
}

const contactAdmin = () => {
  ElMessageBox.alert(
    '请联系系统管理员添加您与老人的关联关系。\n\n联系电话：400-xxx-xxxx\n邮箱：admin@smartcare.com',
    '联系管理员',
    {
      confirmButtonText: '确定',
      type: 'info'
    }
  )
}

const submitEmergencyContact = async () => {
  try {
    // 表单验证
    await emergencyFormRef.value.validate()
    
    submitting.value = true
    
    // 发送紧急联系
    await handleEmergencyContact(
      userStore.userId,
      emergencyForm.elderlyId,
      emergencyForm.message
    )
    
    ElMessage.success('紧急联系已发送，工作人员将尽快回复')
    emergencyDialogVisible.value = false
    resetEmergencyForm()
    
  } catch (error) {
    ElMessage.error('发送失败：' + error.message)
  } finally {
    submitting.value = false
  }
}

const resetEmergencyForm = () => {
  emergencyForm.elderlyId = ''
  emergencyForm.urgencyLevel = 'normal'
  emergencyForm.message = ''
}

// 工具方法
const getStatusTagType = (status) => {
  const typeMap = {
    '在住': 'success',
    '临时外出': 'warning',
    '已离开': 'info'
  }
  return typeMap[status] || 'info'
}

const getAlertType = (level) => {
  const typeMap = {
    '高': 'error',
    '中': 'warning',
    '低': 'info'
  }
  return typeMap[level] || 'info'
}

const getAlertTagType = (level) => {
  const typeMap = {
    '紧急': 'danger',
    '高': 'danger',
    '中': 'warning',
    '低': 'info'
  }
  return typeMap[level] || 'info'
}

const formatTime = (timeStr) => {
  try {
    const date = new Date(timeStr)
    const now = new Date()
    const diff = now - date
    
    if (diff < 60000) { // 1分钟内
      return '刚刚'
    } else if (diff < 3600000) { // 1小时内
      return Math.floor(diff / 60000) + '分钟前'
    } else if (diff < 86400000) { // 24小时内
      return Math.floor(diff / 3600000) + '小时前'
    } else {
      return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
    }
  } catch (error) {
    return timeStr
  }
}
</script>

<style scoped>
.family-dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.welcome-section {
  margin-bottom: 20px;
}

.welcome-card {
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.welcome-info h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.welcome-subtitle {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.quick-actions {
  display: flex;
  gap: 12px;
}

.elderly-overview,
.recent-alerts,
.quick-functions,
.family-notice {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.elderly-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.elderly-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.elderly-card:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
}

.elderly-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.elderly-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.elderly-details p {
  margin: 6px 0;
  font-size: 14px;
  color: #606266;
}

.elderly-details .label {
  color: #909399;
  margin-right: 4px;
}

.health-summary {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.health-text {
  margin: 8px 0 0 0;
  font-size: 13px;
  color: #606266;
  line-height: 1.4;
}

.alerts-list {
  max-height: 300px;
  overflow-y: auto;
}

.alert-item {
  padding: 12px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  margin-bottom: 8px;
}

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.alert-time {
  font-size: 12px;
  color: #909399;
}

.alert-message {
  margin: 0 0 4px 0;
  color: #303133;
  font-size: 14px;
}

.alert-type {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.function-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.function-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.function-content {
  text-align: center;
  padding: 20px 10px;
}

.function-icon {
  font-size: 32px;
  margin-bottom: 12px;
}

.function-content h4 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 16px;
}

.function-content p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

.notice-content {
  line-height: 1.6;
}

.notice-list {
  margin-top: 16px;
}

.notice-list h4 {
  margin: 0 0 8px 0;
  color: #303133;
}

.notice-list ul {
  margin: 0;
  padding-left: 20px;
}

.notice-list li {
  margin-bottom: 4px;
  color: #606266;
  font-size: 14px;
}

@media (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .quick-actions {
    flex-wrap: wrap;
  }
  
  .elderly-grid {
    grid-template-columns: 1fr;
  }
}
</style>
