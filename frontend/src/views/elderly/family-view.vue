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
          shadow="hover"
        >
          <template #header>
            <div class="card-header">
              <div class="elderly-info">
                <el-avatar 
                  :size="50" 
                  :src="elderly.avatar" 
                  :icon="elderly.gender === 1 ? 'el-icon-male' : 'el-icon-female'"
                  class="elderly-avatar"
                ></el-avatar>
                <div class="basic-info">
                  <h4>{{ elderly.name }}</h4>
                  <p class="age-gender">
                    {{ calculateAge(elderly.birthDate) }}岁 
                    {{ elderly.gender === 1 ? '男' : '女' }}
                  </p>
                </div>
              </div>
              <div class="status-badges">
                <el-tag 
                  :type="getHealthStatusType(elderly.healthStatus)" 
                  size="small"
                >
                  {{ getHealthStatusText(elderly.healthStatus) }}
                </el-tag>
                <el-tag 
                  v-if="elderly.warnings && elderly.warnings.length > 0"
                  type="warning" 
                  size="small"
                >
                  {{ elderly.warnings.length }}条预警
                </el-tag>
              </div>
            </div>
          </template>

          <!-- 卡片内容 -->
          <div class="card-content">
            <!-- 基本信息 -->
            <div class="info-section">
              <div class="info-item">
                <span class="label">房间号：</span>
                <span class="value">{{ elderly.roomNumber || '未分配' }}</span>
              </div>
              <div class="info-item">
                <span class="label">护理等级：</span>
                <span class="value">{{ getCareLevel(elderly.careLevel) }}</span>
              </div>
              <div class="info-item">
                <span class="label">我的关系：</span>
                <span class="value">{{ elderly.relationship || '家属' }}</span>
              </div>
            </div>

            <!-- 健康状况 -->
            <div class="health-section" v-if="elderly.latestVitals">
              <h5>最新体征</h5>
              <div class="vitals-grid">
                <div class="vital-item">
                  <span class="vital-label">血压</span>
                  <span class="vital-value">{{ elderly.latestVitals.bloodPressure || '--' }}</span>
                </div>
                <div class="vital-item">
                  <span class="vital-label">心率</span>
                  <span class="vital-value">{{ elderly.latestVitals.heartRate || '--' }} bpm</span>
                </div>
                <div class="vital-item">
                  <span class="vital-label">体温</span>
                  <span class="vital-value">{{ elderly.latestVitals.temperature || '--' }}°C</span>
                </div>
              </div>
            </div>

            <!-- 预警信息 -->
            <div class="warning-section" v-if="elderly.warnings && elderly.warnings.length > 0">
              <h5>健康预警</h5>
              <div class="warning-list">
                <div 
                  v-for="warning in elderly.warnings.slice(0, 2)" 
                  :key="warning.id"
                  class="warning-item"
                >
                  <el-icon class="warning-icon"><Warning /></el-icon>
                  <span class="warning-text">{{ warning.message }}</span>
                </div>
                <div v-if="elderly.warnings.length > 2" class="more-warnings">
                  还有{{ elderly.warnings.length - 2 }}条预警...
                </div>
              </div>
            </div>
          </div>

          <!-- 操作按钮 -->
          <template #footer>
            <div class="card-actions">
              <el-button 
                type="primary" 
                size="small" 
                @click="viewElderlyDetail(elderly)"
                :icon="View"
              >
                查看详情
              </el-button>
              <el-button 
                type="success" 
                size="small" 
                @click="viewHealthRecord(elderly)"
                :icon="Document"
                :disabled="!hasPermission(elderly, 'VIEW_HEALTH_RECORD')"
              >
                健康档案
                <el-icon v-if="!hasPermission(elderly, 'VIEW_HEALTH_RECORD')" style="margin-left: 5px;">
                  <Lock />
                </el-icon>
              </el-button>
              <el-button 
                v-if="elderly.warnings && elderly.warnings.length > 0"
                type="warning" 
                size="small" 
                @click="viewWarnings(elderly)"
                :icon="Bell"
              >
                查看预警
              </el-button>
              <el-button 
                type="info" 
                size="small" 
                @click="contactMedical(elderly)"
                :icon="Message"
              >
                联系医护
              </el-button>
              <el-dropdown trigger="click" @command="(cmd) => handleMoreActions(cmd, elderly)">
                <el-button size="small" type="default">
                  更多 <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="permissions" :icon="Setting">
                      权限设置
                    </el-dropdown-item>
                    <el-dropdown-item command="subscribe" :icon="Bell">
                      {{ elderly.subscribed ? '取消预警推送' : '订阅预警推送' }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>
        </el-card>
      </div>

      <!-- 空状态 -->
      <el-empty 
        v-if="!loading && elderlyList.length === 0"
        description="暂无关联长辈信息"
        :image-size="100"
      >
        <el-button type="primary" @click="loadElderlyList">刷新数据</el-button>
      </el-empty>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
    </div>

    <!-- 联系医护对话框 -->
    <el-dialog 
      v-model="contactDialogVisible"
      title="联系医护人员"
      width="500px"
    >
      <el-form :model="contactForm" label-width="80px">
        <el-form-item label="长辈姓名">
          <el-input v-model="contactForm.elderlyName" disabled />
        </el-form-item>
        <el-form-item label="紧急程度">
          <el-select v-model="contactForm.urgency" placeholder="请选择紧急程度">
            <el-option label="非常紧急" value="urgent" />
            <el-option label="比较紧急" value="high" />
            <el-option label="一般紧急" value="normal" />
            <el-option label="不紧急" value="low" />
          </el-select>
        </el-form-item>
        <el-form-item label="详细描述">
          <el-input 
            v-model="contactForm.message"
            type="textarea"
            :rows="4"
            placeholder="请详细描述需要医护人员关注的情况..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="contactDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitContactRequest">发送请求</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 权限设置对话框 -->
    <el-dialog 
      v-model="permissionDialogVisible"
      title="权限设置"
      width="600px"
    >
      <div class="permission-dialog-content">
        <el-alert
          title="权限说明"
          type="info"
          :closable="false"
          style="margin-bottom: 20px;"
        >
          <template #default>
            作为家属,您当前拥有以下权限。部分敏感权限需要长辈本人或管理员授权。
          </template>
        </el-alert>
        
        <div class="current-permissions">
          <h4>当前权限状态</h4>
          <el-table :data="currentElderlyPermissions" style="width: 100%">
            <el-table-column prop="name" label="权限名称" width="200" />
            <el-table-column prop="description" label="权限说明" />
            <el-table-column label="状态" width="100" align="center">
              <template #default="scope">
                <el-tag :type="scope.row.granted ? 'success' : 'info'" size="small">
                  {{ scope.row.granted ? '已授权' : '未授权' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template #default="scope">
                <el-button 
                  v-if="!scope.row.granted && scope.row.canRequest"
                  type="primary" 
                  size="small"
                  @click="requestPermission(scope.row)"
                >
                  申请权限
                </el-button>
                <span v-else-if="!scope.row.canRequest" class="text-gray">
                  需管理员授权
                </span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 预警推送订阅对话框 -->
    <el-dialog 
      v-model="subscribeDialogVisible"
      title="预警推送设置"
      width="500px"
    >
      <el-form :model="subscribeForm" label-width="120px">
        <el-form-item label="长辈姓名">
          <el-input v-model="subscribeForm.elderlyName" disabled />
        </el-form-item>
        <el-form-item label="推送方式">
          <el-checkbox-group v-model="subscribeForm.methods">
            <el-checkbox label="app" border>应用内通知</el-checkbox>
            <el-checkbox label="sms" border>短信通知</el-checkbox>
            <el-checkbox label="email" border>邮件通知</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="推送级别">
          <el-radio-group v-model="subscribeForm.level">
            <el-radio label="all">全部预警</el-radio>
            <el-radio label="medium">中级及以上</el-radio>
            <el-radio label="high">高级及紧急</el-radio>
            <el-radio label="urgent">仅紧急预警</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="免打扰时段">
          <el-time-picker
            v-model="subscribeForm.quietHours"
            is-range
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="HH:mm"
            value-format="HH:mm"
          />
        </el-form-item>
        <el-form-item>
          <el-alert
            title="提示"
            type="warning"
            :closable="false"
          >
            紧急预警将忽略免打扰时段,确保您能及时收到重要通知
          </el-alert>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="subscribeDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveSubscribeSettings">保存设置</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
    getFamilyElderlyList,
    getFamilyPermissions,
    getLatestVitals,
    getWarnings,
    requestFamilyPermission,
    sendContactRequest,
    subscribeWarningNotification,
    unsubscribeWarningNotification
} from '@/api/family'
import { useUserStore } from '@/store/user'
import { ArrowDown, Bell, Document, Lock, Message, Setting, View, Warning } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const elderlyList = ref([])
const loading = ref(false)

// 联系医护相关
const contactDialogVisible = ref(false)
const contactForm = ref({
  elderlyName: '',
  urgency: 'normal',
  message: ''
})

// 权限管理相关
const permissionDialogVisible = ref(false)
const currentElderlyId = ref(null)
const currentElderlyPermissions = ref([])

// 预警订阅相关
const subscribeDialogVisible = ref(false)
const subscribeForm = ref({
  elderlyId: null,
  elderlyName: '',
  methods: ['app'],
  level: 'medium',
  quietHours: null
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
    
    console.log('开始加载家属关联的老人信息...')

    // 调用家属专用API获取关联老人列表
    try {
      const response = await getFamilyElderlyList()
      console.log('API响应:', response)
      
      if (response && response.code === 200) {
        elderlyList.value = response.data || []
        console.log('成功获取老人信息:', elderlyList.value.length, '条记录')
        
        // 获取每个老人的最新健康数据和预警信息
        for (const elderly of elderlyList.value) {
          await loadElderlyHealth(elderly)
        }
      } else {
        console.error('API返回错误:', response)
        ElMessage.error('获取老人信息失败')
        elderlyList.value = []
      }
    } catch (apiError) {
      console.error('调用API失败:', apiError)
      ElMessage.error('网络请求失败')
      elderlyList.value = []
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
    'WARNING': 'warning'
  }
  return typeMap[status] || 'info'
}

const getHealthStatusText = (status) => {
  const textMap = {
    'HEALTHY': '健康',
    'SUBHEALTH': '亚健康',
    'SICK': '患病',
    'SERIOUS': '严重',
    'DANGER': '危险',
    'WARNING': '预警'
  }
  return textMap[status] || '未知'
}

const getCareLevel = (level) => {
  const levelMap = {
    1: '一级护理',
    2: '二级护理',
    3: '三级护理',
    4: '特级护理'
  }
  return levelMap[level] || '未知'
}

const viewElderlyDetail = (elderly) => {
  // 家属访问专用详情页
  router.push({
    path: `/elderly/family-detail/${elderly.id}`
  })
}

const viewHealthRecord = (elderly) => {
  // 检查权限
  if (!hasPermission(elderly, 'VIEW_HEALTH_RECORD')) {
    ElMessage.warning('您暂无健康档案查看权限,请先申请权限')
    handleMoreActions('permissions', elderly)
    return
  }
  
  router.push({
    path: '/health/record',
    query: { elderlyId: elderly.id }
  })
}

const viewWarnings = (elderly) => {
  router.push({
    path: '/health/warning',
    query: { elderlyId: elderly.id }
  })
}

const contactMedical = (elderly) => {
  contactForm.value = {
    elderlyName: elderly.name,
    urgency: 'normal',
    message: ''
  }
  contactDialogVisible.value = true
}

const submitContactRequest = async () => {
  try {
    if (!contactForm.value.message.trim()) {
      ElMessage.warning('请填写详细描述')
      return
    }
    
    await sendContactRequest(contactForm.value)
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

// 检查权限
const hasPermission = (elderly, permissionCode) => {
  if (!elderly.permissions) {
    return false
  }
  return elderly.permissions.some(p => p.code === permissionCode && p.granted)
}

// 处理更多操作菜单
const handleMoreActions = async (command, elderly) => {
  if (command === 'permissions') {
    await showPermissionDialog(elderly)
  } else if (command === 'subscribe') {
    await showSubscribeDialog(elderly)
  }
}

// 显示权限设置对话框
const showPermissionDialog = async (elderly) => {
  try {
    currentElderlyId.value = elderly.id
    
    // 获取家属对该老人的权限列表
    const response = await getFamilyPermissions(elderly.id)
    
    if (response && response.code === 200) {
      currentElderlyPermissions.value = response.data.map(perm => ({
        code: perm.code,
        name: perm.name,
        description: perm.description,
        granted: perm.granted,
        canRequest: perm.canRequest !== false // 默认可以申请
      }))
    } else {
      // 使用默认权限列表
      currentElderlyPermissions.value = getDefaultPermissions(elderly)
    }
    
    permissionDialogVisible.value = true
  } catch (error) {
    console.error('获取权限列表失败:', error)
    // 使用默认权限列表
    currentElderlyPermissions.value = getDefaultPermissions(elderly)
    permissionDialogVisible.value = true
  }
}

// 获取默认权限列表
const getDefaultPermissions = (elderly) => {
  const permissions = [
    {
      code: 'VIEW_BASIC_INFO',
      name: '查看基本信息',
      description: '查看长辈的姓名、年龄等基础信息',
      granted: true,
      canRequest: false
    },
    {
      code: 'VIEW_HEALTH_RECORD',
      name: '查看健康档案',
      description: '查看长辈的健康记录、体检报告等',
      granted: elderly.permissions?.some(p => p.code === 'VIEW_HEALTH_RECORD') || false,
      canRequest: true
    },
    {
      code: 'VIEW_MEDICATION',
      name: '查看用药记录',
      description: '查看长辈的用药历史和当前用药情况',
      granted: elderly.permissions?.some(p => p.code === 'VIEW_MEDICATION') || false,
      canRequest: true
    },
    {
      code: 'RECEIVE_WARNINGS',
      name: '接收健康预警',
      description: '接收长辈的健康异常预警通知',
      granted: elderly.permissions?.some(p => p.code === 'RECEIVE_WARNINGS') || true,
      canRequest: true
    },
    {
      code: 'CONTACT_MEDICAL',
      name: '联系医护人员',
      description: '代表长辈联系医护人员',
      granted: true,
      canRequest: false
    }
  ]
  
  return permissions
}

// 申请权限
const requestPermission = async (permission) => {
  try {
    const response = await requestFamilyPermission({
      elderlyId: currentElderlyId.value,
      permissionCode: permission.code,
      reason: `申请${permission.name}权限`
    })
    
    if (response && response.code === 200) {
      ElMessage.success('权限申请已提交,等待审核')
      permission.granted = false // 等待审核状态
    } else {
      ElMessage.error('权限申请失败,请重试')
    }
  } catch (error) {
    console.error('申请权限失败:', error)
    ElMessage.error('权限申请失败,请重试')
  }
}

// 显示预警订阅对话框
const showSubscribeDialog = async (elderly) => {
  subscribeForm.value = {
    elderlyId: elderly.id,
    elderlyName: elderly.name,
    methods: elderly.subscribed ? (elderly.subscribeMethods || ['app']) : ['app'],
    level: elderly.subscribeLevel || 'medium',
    quietHours: elderly.quietHours || null
  }
  subscribeDialogVisible.value = true
}

// 保存订阅设置
const saveSubscribeSettings = async () => {
  try {
    if (subscribeForm.value.methods.length === 0) {
      ElMessage.warning('请至少选择一种推送方式')
      return
    }
    
    const params = {
      elderlyId: subscribeForm.value.elderlyId,
      methods: subscribeForm.value.methods,
      level: subscribeForm.value.level,
      quietHours: subscribeForm.value.quietHours
    }
    
    const response = await subscribeWarningNotification(params)
    
    if (response && response.code === 200) {
      ElMessage.success('预警推送设置已保存')
      subscribeDialogVisible.value = false
      
      // 更新列表中的订阅状态
      const elderly = elderlyList.value.find(e => e.id === subscribeForm.value.elderlyId)
      if (elderly) {
        elderly.subscribed = true
        elderly.subscribeMethods = subscribeForm.value.methods
        elderly.subscribeLevel = subscribeForm.value.level
        elderly.quietHours = subscribeForm.value.quietHours
      }
    } else {
      ElMessage.error('保存设置失败,请重试')
    }
  } catch (error) {
    console.error('保存订阅设置失败:', error)
    ElMessage.error('保存设置失败,请重试')
  }
}

// Mock数据函数(用于测试)
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
        bloodPressure: '140/90',
        heartRate: 85,
        temperature: 36.8
      },
      warnings: [
        {
          id: 1,
          message: '血压偏高，建议注意休息',
          level: 'WARNING',
          createTime: new Date(),
          isRead: false
        }
      ],
      updateTime: new Date().toISOString()
    }
  ]
}
</script>

<style scoped>
.family-elderly-view {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

/* 欢迎头部样式 */
.welcome-header {
  margin-bottom: 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
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

/* 长辈卡片样式 */
.elderly-cards {
  background: white;
  border-radius: 8px;
  padding: 24px;
}

.cards-header {
  margin-bottom: 24px;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 16px;
}

.cards-header h3 {
  margin: 0 0 8px 0;
  font-size: 18px;
  color: #303133;
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
  border: 1px solid #ebeef5;
  transition: all 0.3s ease;
}

.elderly-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.elderly-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.elderly-avatar {
  border: 2px solid #f0f0f0;
}

.basic-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  color: #303133;
}

.age-gender {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.status-badges {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-end;
}

/* 卡片内容 */
.card-content {
  padding: 16px 0;
}

.info-section {
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.info-item .label {
  color: #909399;
  font-size: 14px;
}

.info-item .value {
  color: #303133;
  font-size: 14px;
  font-weight: 500;
}

/* 健康状况 */
.health-section h5,
.warning-section h5 {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #303133;
  border-left: 3px solid #409eff;
  padding-left: 8px;
}

.vitals-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.vital-item {
  text-align: center;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 4px;
}

.vital-label {
  display: block;
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.vital-value {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

/* 预警样式 */
.warning-section {
  margin-top: 16px;
}

.warning-list {
  max-height: 80px;
  overflow-y: auto;
}

.warning-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  padding: 6px;
  background: #fef0f0;
  border-radius: 4px;
}

.warning-icon {
  color: #f56c6c;
}

.warning-text {
  font-size: 12px;
  color: #303133;
  flex: 1;
}

.more-warnings {
  font-size: 12px;
  color: #909399;
  text-align: center;
  padding: 4px;
}

/* 操作按钮 */
.card-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.card-actions .el-button {
  flex: 1;
  min-width: 80px;
}

/* 加载状态 */
.loading-container {
  padding: 40px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .family-elderly-view {
    padding: 12px;
  }
  
  .header-content {
    flex-direction: column;
    gap: 16px;
  }
  
  .elderly-grid {
    grid-template-columns: 1fr;
  }
  
  .quick-stats {
    gap: 12px;
  }
  
  .stat-item {
    padding: 12px;
    min-width: 60px;
  }
  
  .card-actions {
    gap: 4px;
  }
  
  .card-actions .el-button {
    min-width: 70px;
    font-size: 12px;
  }
}
</style>
