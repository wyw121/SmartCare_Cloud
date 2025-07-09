<template>
  <div class="family-health-warning">
    <!-- 开发环境用户切换工具 -->
    <div v-if="isDev" class="dev-toolbar">
      <el-alert 
        title="开发环境 - 用户切换工具" 
        type="info" 
        :closable="false"
        style="margin-bottom: 10px"
      >
        <template #default>
          <div style="display: flex; align-items: center; gap: 10px;">
            <span>当前用户: {{ userStore.userName }} ({{ userStore.userRoleText }})</span>
            <el-button size="small" @click="switchToAdmin">切换到管理员</el-button>
            <el-button size="small" @click="switchToDoctor">切换到医生</el-button>
            <el-button size="small" @click="switchToFamily">切换到家属</el-button>
          </div>
        </template>
      </el-alert>
    </div>

    <!-- 页面头部 -->
    <el-card class="page-header">
      <div class="header-content">
        <h2>🏥 我的关联长辈健康预警</h2>
        <p>查看关联长辈的健康预警信息，及时了解长辈的健康状况</p>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistic-card urgent">
          <div class="statistic-content">
            <div class="statistic-number">{{ statistics.urgent || 0 }}</div>
            <div class="statistic-label">紧急预警</div>
          </div>
          <el-icon class="statistic-icon"><Warning /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card high">
          <div class="statistic-content">
            <div class="statistic-number">{{ statistics.high || 0 }}</div>
            <div class="statistic-label">高风险</div>
          </div>
          <el-icon class="statistic-icon"><Warning /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card medium">
          <div class="statistic-content">
            <div class="statistic-number">{{ statistics.medium || 0 }}</div>
            <div class="statistic-label">中风险</div>
          </div>
          <el-icon class="statistic-icon"><InfoFilled /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card low">
          <div class="statistic-content">
            <div class="statistic-number">{{ statistics.low || 0 }}</div>
            <div class="statistic-label">低风险</div>
          </div>
          <el-icon class="statistic-icon"><CircleCheck /></el-icon>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" ref="searchFormRef" :inline="true" label-width="80px">
        <el-form-item label="长辈姓名" prop="elderlyName">
          <el-input
            v-model="searchForm.elderlyName"
            placeholder="请输入长辈姓名"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="预警类型" prop="warningType">
          <el-select
            v-model="searchForm.warningType"
            placeholder="预警类型"
            clearable
            multiple
            collapse-tags
            collapse-tags-tooltip
            style="width: 180px"
          >
            <el-option label="血压异常" value="血压异常" />
            <el-option label="血糖异常" value="血糖异常" />
            <el-option label="心率异常" value="心率异常" />
            <el-option label="体温异常" value="体温异常" />
            <el-option label="用药提醒" value="用药提醒" />
            <el-option label="活动异常" value="活动异常" />
            <el-option label="跌倒检测" value="跌倒检测" />
            <el-option label="紧急求助" value="紧急求助" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别" prop="warningLevel">
          <el-select
            v-model="searchForm.warningLevel"
            placeholder="预警级别"
            clearable
            multiple
            collapse-tags
            collapse-tags-tooltip
            style="width: 120px"
          >
            <el-option label="低风险" :value="1" />
            <el-option label="中风险" :value="2" />
            <el-option label="高风险" :value="3" />
            <el-option label="紧急" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="处理状态"
            clearable
            multiple
            collapse-tags
            collapse-tags-tooltip
            style="width: 120px"
          >
            <el-option label="未处理" :value="0" />
            <el-option label="已查看" :value="1" />
            <el-option label="处理中" :value="2" />
            <el-option label="已处理" :value="3" />
            <el-option label="已忽略" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间" prop="triggerTime">
          <el-date-picker
            v-model="searchForm.triggerTime"
            type="datetimerange"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 280px"
          />
        </el-form-item>
        <el-form-item class="search-buttons">
          <el-button type="primary" @click="handleSearch" :icon="Search" size="default">
            搜索
          </el-button>
          <el-button @click="handleReset" :icon="Refresh" size="default">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-alert
            title="家属提示"
            type="info"
            show-icon
            :closable="false"
            description="您正在查看关联长辈的健康预警信息。如有紧急情况，请及时联系医护人员。"
          />
        </div>
        <div class="toolbar-right">
          <el-button type="primary" @click="handleRefresh">
            <el-icon><Refresh /></el-icon>
            刷新数据
          </el-button>
          <el-button type="success" @click="handleContactMedical">
            <el-icon><Phone /></el-icon>
            联系医护
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        :empty-text="tableData.length === 0 ? '暂无预警信息' : '加载中...'"
      >
        <el-table-column prop="elderlyName" label="长辈姓名" min-width="100" />
        <el-table-column prop="warningType" label="预警类型" min-width="120" />
        <el-table-column prop="warningLevel" label="预警级别" width="100">
          <template #default="scope">
            <el-tag :type="getWarningLevelType(scope.row.warningLevel)">
              {{ getWarningLevelText(scope.row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="预警标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="content" label="预警内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="triggerData" label="监测数据" min-width="150" show-overflow-tooltip />
        <el-table-column prop="dataSource" label="数据来源" width="120" />
        <el-table-column prop="status" label="处理状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="triggerTime" label="触发时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <el-button 
                text 
                type="primary" 
                size="small" 
                @click="handleView(scope.row)"
              >
                查看详情
              </el-button>
              <el-button 
                v-if="scope.row.status === 0"
                text 
                type="success" 
                size="small" 
                @click="handleMarkRead(scope.row)"
              >
                标记已读
              </el-button>
              <el-button 
                text 
                type="warning" 
                size="small" 
                @click="handleContactDoctor(scope.row)"
              >
                联系医生
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageInfo.pageNum"
          v-model:page-size="pageInfo.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="pageInfo.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog
      title="预警详情"
      v-model="detailDialogVisible"
      width="800px"
    >
      <div v-if="currentWarning" class="warning-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="长辈姓名">{{ currentWarning.elderlyName }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">{{ currentWarning.warningType }}</el-descriptions-item>
          <el-descriptions-item label="预警级别">
            <el-tag :type="getWarningLevelType(currentWarning.warningLevel)">
              {{ getWarningLevelText(currentWarning.warningLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="数据来源">{{ currentWarning.dataSource }}</el-descriptions-item>
          <el-descriptions-item label="触发时间">{{ currentWarning.triggerTime }}</el-descriptions-item>
          <el-descriptions-item label="处理状态">
            <el-tag :type="getStatusType(currentWarning.status)">
              {{ getStatusText(currentWarning.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理人员">{{ currentWarning.handlerName || '暂无' }}</el-descriptions-item>
          <el-descriptions-item label="处理时间">{{ currentWarning.handleTime || '暂无' }}</el-descriptions-item>
        </el-descriptions>
        <el-divider>预警内容</el-divider>
        <div class="warning-content">
          <h4>{{ currentWarning.title }}</h4>
          <p>{{ currentWarning.content }}</p>
        </div>
        <el-divider>监测数据</el-divider>
        <div class="trigger-data">
          <p>{{ currentWarning.triggerData }}</p>
        </div>
        <el-divider v-if="currentWarning.handleRemark">处理记录</el-divider>
        <div v-if="currentWarning.handleRemark" class="handle-remark">
          <p>{{ currentWarning.handleRemark }}</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button 
          v-if="currentWarning && currentWarning.status === 0" 
          type="success" 
          @click="handleMarkCurrentRead"
        >
          标记已读
        </el-button>
      </template>
    </el-dialog>

    <!-- 联系医护对话框 -->
    <el-dialog
      title="联系医护人员"
      v-model="contactDialogVisible"
      width="500px"
    >
      <el-form :model="contactForm" :rules="contactRules" ref="contactFormRef" label-width="80px">
        <el-form-item label="长辈姓名" prop="elderlyName">
          <el-input v-model="contactForm.elderlyName" disabled />
        </el-form-item>
        <el-form-item label="紧急程度" prop="urgency">
          <el-select v-model="contactForm.urgency" placeholder="请选择紧急程度" style="width: 100%">
            <el-option label="一般" value="一般" />
            <el-option label="紧急" value="紧急" />
            <el-option label="非常紧急" value="非常紧急" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系原因" prop="message">
          <el-input
            v-model="contactForm.message"
            type="textarea"
            :rows="4"
            placeholder="请描述需要联系医护人员的原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="contactDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleContactSubmit" :loading="contactLoading">发送请求</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
    getFamilyWarningsPage,
    getFamilyWarningStatistics,
    markWarningsAsRead,
    sendContactRequest
} from '@/api/family'
import { useUserStore } from '@/store/user'
import {
    CircleCheck,
    InfoFilled,
    Phone,
    Refresh,
    Search,
    Warning
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

// 用户Store
const userStore = useUserStore()

// 开发环境标识
const isDev = ref(import.meta.env.DEV)

// 开发环境用户切换方法
const switchToAdmin = () => {
  userStore.switchToDemoUser('admin')
}

const switchToDoctor = () => {
  userStore.switchToDemoUser('doctor')
}

const switchToFamily = () => {
  userStore.switchToDemoUser('family')
}

// 响应式数据
const tableLoading = ref(false)
const contactLoading = ref(false)
const detailDialogVisible = ref(false)
const contactDialogVisible = ref(false)
const tableData = ref([])
const currentWarning = ref(null)

// 统计数据
const statistics = reactive({
  urgent: 0,
  high: 0,
  medium: 0,
  low: 0,
  total: 0
})

// 搜索表单
const searchForm = reactive({
  elderlyName: '',
  warningType: [],
  warningLevel: [],
  status: [],
  triggerTime: []
})

// 分页信息
const pageInfo = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 联系表单
const contactForm = reactive({
  elderlyName: '',
  urgency: '',
  message: ''
})

// 联系表单验证规则
const contactRules = {
  urgency: [
    { required: true, message: '请选择紧急程度', trigger: 'change' }
  ],
  message: [
    { required: true, message: '请输入联系原因', trigger: 'blur' },
    { min: 10, message: '联系原因至少10个字符', trigger: 'blur' }
  ]
}

// 引用
const searchFormRef = ref()
const contactFormRef = ref()

// 获取预警级别类型
const getWarningLevelType = (level) => {
  const types = {
    1: 'info',
    2: 'warning',
    3: 'danger',
    4: 'danger'
  }
  return types[level] || 'info'
}

// 获取预警级别文本
const getWarningLevelText = (level) => {
  const texts = {
    1: '低风险',
    2: '中风险',
    3: '高风险',
    4: '紧急'
  }
  return texts[level] || '未知'
}

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    0: 'danger',
    1: 'warning',
    2: 'primary',
    3: 'success',
    4: 'info'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    0: '未处理',
    1: '已查看',
    2: '处理中',
    3: '已处理',
    4: '已忽略'
  }
  return texts[status] || '未知'
}

// 获取统计数据
const getStatistics = async () => {
  try {
    const response = await getFamilyWarningStatistics()
    console.log('家属统计API响应:', response)
    
    if (response.code === 200 || (response.data && response.data.code === 200)) {
      // 兼容不同的响应格式
      const stats = response.data?.data || response.data || {}
      statistics.urgent = stats.urgent || 0
      statistics.high = stats.high || 0
      statistics.medium = stats.medium || 0
      statistics.low = stats.low || 0
      statistics.total = stats.total || 0
      
      console.log('家属统计数据:', statistics)
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 处理搜索参数
const processSearchParams = (form) => {
  const params = {
    ...form,
    pageNum: pageInfo.pageNum,
    pageSize: pageInfo.pageSize
  }
  
  // 处理时间范围
  if (form.triggerTime && form.triggerTime.length === 2) {
    params.startTime = form.triggerTime[0]
    params.endTime = form.triggerTime[1]
  }
  delete params.triggerTime
  
  return params
}

// 处理多选参数
const processMultiSelectParams = (params) => {
  const processParam = (key) => {
    if (Array.isArray(params[key])) {
      if (params[key].length > 0) {
        params[key] = params[key].join(',')
      } else {
        delete params[key]
      }
    } else if (!params[key] || params[key] === '') {
      delete params[key]
    }
  }
  
  processParam('warningType')
  processParam('warningLevel')
  processParam('status')
  
  // 清理空字符串参数
  if (!params.elderlyName || params.elderlyName.trim() === '') {
    delete params.elderlyName
  }
  
  return params
}

// 获取预警列表
const getList = async () => {
  tableLoading.value = true
  try {
    let params = processSearchParams(searchForm)
    params = processMultiSelectParams(params)
    
    console.log('家属查询预警参数:', params)
    
    const response = await getFamilyWarningsPage(params)
    console.log('家属预警API响应:', response)
    
    if (response.code === 200 || (response.data && response.data.code === 200)) {
      // 兼容不同的响应格式
      const data = response.data?.data || response.data
      tableData.value = data.list || []
      pageInfo.total = data.total || 0
      
      console.log('家属预警数据:', { list: tableData.value, total: pageInfo.total })
    } else {
      const errorMsg = response.message || response.data?.message || '获取预警列表失败'
      console.error('API错误响应:', response)
      ElMessage.error(errorMsg)
    }
  } catch (error) {
    console.error('获取预警列表失败:', error)
    ElMessage.error('获取预警列表失败：' + (error.message || '网络错误'))
  } finally {
    tableLoading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pageInfo.pageNum = 1
  getList()
}

// 重置
const handleReset = () => {
  searchFormRef.value?.resetFields()
  pageInfo.pageNum = 1
  getList()
}

// 查看详情
const handleView = (row) => {
  currentWarning.value = row
  detailDialogVisible.value = true
}

// 标记已读
const handleMarkRead = async (row) => {
  try {
    const response = await markWarningsAsRead([row.id])
    if (response.data.code === 200) {
      ElMessage.success('标记成功')
      row.status = 1
      getStatistics()
    } else {
      ElMessage.error(response.data.message || '标记失败')
    }
  } catch (error) {
    console.error('标记失败:', error)
    ElMessage.error('标记失败')
  }
}

// 标记当前详情中的预警已读
const handleMarkCurrentRead = async () => {
  if (currentWarning.value) {
    await handleMarkRead(currentWarning.value)
    detailDialogVisible.value = false
  }
}

// 联系医生
const handleContactDoctor = (row) => {
  contactForm.elderlyName = row.elderlyName
  contactForm.urgency = ''
  contactForm.message = `关于${row.elderlyName}的${row.warningType}预警，需要医生关注。预警内容：${row.content || row.title}`
  contactDialogVisible.value = true
}

// 联系医护
const handleContactMedical = () => {
  contactForm.elderlyName = '我的关联长辈'
  contactForm.urgency = ''
  contactForm.message = ''
  contactDialogVisible.value = true
}

// 提交联系请求
const handleContactSubmit = async () => {
  try {
    await contactFormRef.value?.validate()
    
    contactLoading.value = true
    const response = await sendContactRequest(contactForm)
    
    if (response.data.code === 200) {
      ElMessage.success('联系请求已发送，医护人员会尽快回复')
      contactDialogVisible.value = false
    } else {
      ElMessage.error(response.data.message || '发送失败')
    }
  } catch (error) {
    console.error('发送联系请求失败:', error)
    ElMessage.error('发送失败')
  } finally {
    contactLoading.value = false
  }
}

// 刷新数据
const handleRefresh = () => {
  getList()
  getStatistics()
  ElMessage.success('数据已刷新')
}

// 分页变更
const handleSizeChange = (size) => {
  pageInfo.pageSize = size
  pageInfo.pageNum = 1
  getList()
}

const handleCurrentChange = (page) => {
  pageInfo.pageNum = page
  getList()
}

// 页面初始化
onMounted(() => {
  getList()
  getStatistics()
})
</script>

<style scoped>
.family-health-warning {
  padding: 20px;
}

.dev-toolbar {
  margin-bottom: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.header-content {
  text-align: center;
}

.header-content h2 {
  margin: 0 0 10px 0;
  color: #2c5aa0;
  font-size: 24px;
  font-weight: 600;
}

.header-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.statistics-row {
  margin-bottom: 20px;
}

.statistic-card {
  position: relative;
  cursor: pointer;
  transition: all 0.3s;
}

.statistic-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.statistic-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.statistic-number {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 8px;
}

.statistic-label {
  font-size: 14px;
  color: #666;
}

.statistic-icon {
  position: absolute;
  top: 15px;
  right: 15px;
  font-size: 24px;
  opacity: 0.3;
}

.statistic-card.urgent .statistic-number {
  color: #ff4d4f;
}

.statistic-card.high .statistic-number {
  color: #ff7a45;
}

.statistic-card.medium .statistic-number {
  color: #faad14;
}

.statistic-card.low .statistic-number {
  color: #52c41a;
}

.search-card {
  margin-bottom: 20px;
}

.search-buttons {
  margin-left: 20px;
}

.toolbar-card {
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.toolbar-left {
  flex: 1;
}

.toolbar-right {
  display: flex;
  gap: 10px;
}

.table-card {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.warning-detail {
  padding: 10px 0;
}

.warning-content h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.warning-content p,
.trigger-data p,
.handle-remark p {
  margin: 0;
  color: #666;
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .family-health-warning {
    padding: 10px;
  }
  
  .statistics-row {
    gap: 10px;
  }
  
  .toolbar {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 4px;
  }
}
</style>
