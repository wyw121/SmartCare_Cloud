<template>
  <div class="report-statistics" v-loading="loading">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1><el-icon><DataAnalysis /></el-icon> 智慧医养统计报表</h1>
      <div class="header-actions">
        <el-button @click="refreshAllData" :loading="loading" type="primary">
          <el-icon><Refresh /></el-icon> 刷新数据
        </el-button>
        <el-button @click="exportReport" type="success">
          <el-icon><Download /></el-icon> 导出报表
        </el-button>
      </div>
    </div>

    <!-- 数据概览 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="overview-card">
          <template #header>
            <div class="card-header">
              <h2><el-icon><DataAnalysis /></el-icon> 数据概览</h2>
              <span class="update-time">更新时间：{{ updateTime }}</span>
            </div>
          </template>
          <el-row :gutter="16" class="overview-row">
            <el-col :span="4" v-for="item in overviewList" :key="item.label">
              <div class="overview-item" :class="item.trend">
                <div class="overview-icon">
                  <el-icon><component :is="item.icon" /></el-icon>
                </div>
                <div class="overview-content">
                  <div class="overview-value">{{ item.value }}</div>
                  <div class="overview-label">{{ item.label }}</div>
                  <div class="overview-change" v-if="item.change">
                    <span :class="item.trend">{{ item.change }}</span>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 健康状况与预警分析 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3><el-icon><CircleCheck /></el-icon> 健康状况分布</h3>
          </template>
          <v-chart :option="healthStatusOption" autoresize style="height:320px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3><el-icon><Warning /></el-icon> 预警级别分布</h3>
          </template>
          <v-chart :option="warningLevelOption" autoresize style="height:320px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 疾病统计与预警类型 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3><el-icon><Document /></el-icon> 常见疾病统计</h3>
          </template>
          <v-chart :option="diseaseOption" autoresize style="height:320px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3><el-icon><Bell /></el-icon> 预警类型统计</h3>
          </template>
          <v-chart :option="warningTypeOption" autoresize style="height:320px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 医疗服务与照护等级 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3><el-icon><User /></el-icon> 医疗服务统计</h3>
          </template>
          <v-chart :option="medicalServiceOption" autoresize style="height:320px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3><el-icon><UserFilled /></el-icon> 照护等级分布</h3>
          </template>
          <v-chart :option="careLevelOption" autoresize style="height:320px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 趋势分析 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <h3><el-icon><TrendCharts /></el-icon> 趋势分析</h3>
              <el-select v-model="trendRange" placeholder="选择时间范围" style="width: 160px;">
                <el-option label="近7天" value="7" />
                <el-option label="近30天" value="30" />
                <el-option label="近90天" value="90" />
              </el-select>
            </div>
          </template>
          <v-chart :option="trendOption" autoresize style="height:360px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 设备使用统计 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <h3><el-icon><Monitor /></el-icon> 设备使用统计</h3>
          </template>
          <el-row :gutter="16">
            <el-col :span="16">
              <v-chart :option="equipmentTypeOption" autoresize style="height:300px" />
            </el-col>
            <el-col :span="8">
              <div class="equipment-status">
                <div class="status-item">
                  <div class="status-title">设备总数</div>
                  <div class="status-value">{{ equipmentStatus.totalCount }}</div>
                </div>
                <div class="status-item">
                  <div class="status-title">在线率</div>
                  <div class="status-value success">{{ equipmentStatus.onlineRate }}%</div>
                </div>
                <div class="status-item">
                  <div class="status-title">故障设备</div>
                  <div class="status-value danger">{{ equipmentStatus.faultCount }}</div>
                </div>
                <div class="status-item">
                  <div class="status-title">今日数据点</div>
                  <div class="status-value">{{ equipmentStatus.dailyDataPoints }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 操作按钮 -->
    <el-row style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <div class="operation-buttons">
            <el-button type="primary" @click="refreshAllData" :loading="loading">
              <el-icon><Refresh /></el-icon> 刷新数据
            </el-button>
            <el-button type="success" @click="exportReport">
              <el-icon><Download /></el-icon> 导出报表
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  DataAnalysis, Warning, Document, Bell, User, UserFilled, 
  TrendCharts, Monitor, Refresh, Download, CircleCheck 
} from '@element-plus/icons-vue'
import VChart from 'vue-echarts'
import * as echarts from 'echarts'
import {
  getOverviewStatistics,
  getHealthStatusStatistics,
  getWarningAnalysis,
  getTrendAnalysis,
  getMedicalServiceStatistics,
  getCareLevelStatistics,
  getEquipmentUsageStatistics
} from '@/api/reports'

// 响应式数据
const updateTime = ref(new Date().toLocaleString())
const loading = ref(false)

// 数据概览
const overviewList = ref([
  { label: '老人总数', value: 0, icon: 'User', trend: 'up', change: '+5' },
  { label: '医生总数', value: 0, icon: 'UserFilled', trend: 'up', change: '+2' },
  { label: '健康预警数', value: 0, icon: 'Warning', trend: 'down', change: '-3' },
  { label: '今日新增老人', value: 0, icon: 'TrendCharts', trend: 'up', change: '+1' },
  { label: '服务满意度', value: '4.6', icon: 'CircleCheck', trend: 'up', change: '+0.1' }
])

// 图表配置
const healthStatusOption = ref({})
const diseaseOption = ref({})
const warningLevelOption = ref({})
const warningTypeOption = ref({})
const medicalServiceOption = ref({})
const careLevelOption = ref({})
const trendOption = ref({})
const equipmentTypeOption = ref({})
const trendRange = ref('30')

// 设备状态
const equipmentStatus = ref({
  totalCount: 0,
  onlineRate: 0,
  faultCount: 0,
  dailyDataPoints: 0
})

// 获取数据概览
const fetchOverview = async () => {
  try {
    const res = await getOverviewStatistics()
    if (res.code === 200) {
      overviewList.value = [
        { 
          label: '老人总数', 
          value: res.data.elderlyTotal || 0, 
          icon: 'User', 
          trend: 'up', 
          change: `+${res.data.todayElderlyCount || 0}` 
        },
        { 
          label: '医生总数', 
          value: res.data.doctorTotal || 0, 
          icon: 'UserFilled', 
          trend: 'stable', 
          change: '' 
        },
        { 
          label: '健康预警数', 
          value: res.data.warningTotal || 0, 
          icon: 'Warning', 
          trend: res.data.unhandledWarnings > 5 ? 'up' : 'down', 
          change: `未处理:${res.data.unhandledWarnings || 0}` 
        },
        { 
          label: '健康覆盖率', 
          value: `${res.data.healthCoverageRate || 0}%`, 
          icon: 'TrendCharts', 
          trend: 'up', 
          change: '+2.5%' 
        },
        { 
          label: '预警处理率', 
          value: `${res.data.warningHandlingRate || 0}%`, 
          icon: 'CircleCheck', 
          trend: res.data.warningHandlingRate > 80 ? 'up' : 'down', 
          change: '+5.2%' 
        }
      ]
    }
  } catch (error) {
    console.error('获取数据概览失败:', error)
    ElMessage.error('获取数据概览失败')
  }
}

// 获取健康状况统计
const fetchHealthStatus = async () => {
  try {
    const res = await getHealthStatusStatistics()
    if (res.code === 200) {
      const data = res.data.healthStatusDistribution || []
      healthStatusOption.value = {
        title: { text: '健康状况分布', left: 'center', top: 20 },
        tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
        legend: { top: 'bottom' },
        color: ['#52c41a', '#faad14', '#ff7a45', '#ff4d4f'],
        series: [{
          name: '健康状况',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '55%'],
          data: data.map(i => ({ name: i.status, value: i.count })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      
      const disease = res.data.commonDiseases || []
      diseaseOption.value = {
        title: { text: '常见疾病统计', left: 'center', top: 20 },
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { 
          type: 'category', 
          data: disease.map(i => i.name),
          axisLabel: { rotate: 45 }
        },
        yAxis: { type: 'value' },
        series: [{
          name: '患病人数',
          type: 'bar',
          data: disease.map(i => i.count),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      }
    }
  } catch (error) {
    console.error('获取健康状况统计失败:', error)
    ElMessage.error('获取健康状况统计失败')
  }
}

// 获取预警分析
const fetchWarningAnalysis = async () => {
  try {
    const res = await getWarningAnalysis()
    if (res.code === 200) {
      const level = res.data.levelStatistics || []
      warningLevelOption.value = {
        title: { text: '预警级别分布', left: 'center', top: 20 },
        tooltip: { trigger: 'item' },
        legend: { top: 'bottom' },
        color: ['#52c41a', '#faad14', '#ff7a45', '#ff4d4f'],
        series: [{
          name: '预警级别',
          type: 'pie',
          radius: '60%',
          center: ['50%', '55%'],
          data: level.map(i => ({ 
            name: ['', '低风险', '中风险', '高风险', '紧急'][i.level] || '未知', 
            value: i.count 
          }))
        }]
      }
      
      const type = res.data.typeStatistics || []
      warningTypeOption.value = {
        title: { text: '预警类型统计', left: 'center', top: 20 },
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { 
          type: 'category', 
          data: type.map(i => i.type),
          axisLabel: { rotate: 30 }
        },
        yAxis: { type: 'value' },
        series: [{
          name: '预警数量',
          type: 'bar',
          data: type.map(i => i.count),
          itemStyle: { color: '#ff7a45' }
        }]
      }
    }
  } catch (error) {
    console.error('获取预警统计失败:', error)
    ElMessage.error('获取预警统计失败')
  }
}

// 获取医疗服务统计
const fetchMedicalService = async () => {
  try {
    const res = await getMedicalServiceStatistics()
    if (res.code === 200) {
      const serviceTypes = res.data.serviceTypes || []
      medicalServiceOption.value = {
        title: { text: '医疗服务类型分布', left: 'center', top: 20 },
        tooltip: { trigger: 'item' },
        legend: { top: 'bottom' },
        series: [{
          name: '服务类型',
          type: 'pie',
          radius: '60%',
          center: ['50%', '55%'],
          data: serviceTypes.map(i => ({ name: i.serviceName, value: i.count }))
        }]
      }
    }
  } catch (error) {
    console.error('获取医疗服务统计失败:', error)
    ElMessage.error('获取医疗服务统计失败')
  }
}

// 获取照护等级统计
const fetchCareLevel = async () => {
  try {
    const res = await getCareLevelStatistics()
    if (res.code === 200) {
      const levels = res.data.careLevelDistribution || []
      careLevelOption.value = {
        title: { text: '照护等级分布', left: 'center', top: 20 },
        tooltip: { trigger: 'item' },
        legend: { top: 'bottom' },
        color: ['#52c41a', '#faad14', '#ff7a45', '#ff4d4f'],
        series: [{
          name: '照护等级',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '55%'],
          data: levels.map(i => ({ name: i.level, value: i.count }))
        }]
      }
    }
  } catch (error) {
    console.error('获取照护等级统计失败:', error)
    ElMessage.error('获取照护等级统计失败')
  }
}

// 获取设备使用统计
const fetchEquipmentUsage = async () => {
  try {
    const res = await getEquipmentUsageStatistics()
    if (res.code === 200) {
      const equipment = res.data.equipmentTypes || []
      equipmentTypeOption.value = {
        title: { text: '设备类型及使用率', left: 'center', top: 20 },
        tooltip: { trigger: 'axis' },
        legend: { data: ['设备数量', '使用率'], top: 50 },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { 
          type: 'category', 
          data: equipment.map(i => i.type),
          axisLabel: { rotate: 30 }
        },
        yAxis: [
          { type: 'value', name: '设备数量' },
          { type: 'value', name: '使用率(%)', min: 0, max: 100 }
        ],
        series: [
          {
            name: '设备数量',
            type: 'bar',
            data: equipment.map(i => i.count),
            itemStyle: { color: '#5470c6' }
          },
          {
            name: '使用率',
            type: 'line',
            yAxisIndex: 1,
            data: equipment.map(i => i.usageRate),
            itemStyle: { color: '#91cc75' }
          }
        ]
      }
      
      equipmentStatus.value = res.data.equipmentStatus || {}
    }
  } catch (error) {
    console.error('获取设备统计失败:', error)
    ElMessage.error('获取设备统计失败')
  }
}

// 获取趋势分析
const fetchTrend = async () => {
  try {
    const res = await getTrendAnalysis(trendRange.value)
    if (res.code === 200) {
      const warning = res.data.warningTrend || []
      const elderly = res.data.elderlyTrend || []
      const health = res.data.healthScoreTrend || []
      
      trendOption.value = {
        title: { text: '关键指标趋势分析', left: 'center', top: 20 },
        tooltip: { trigger: 'axis' },
        legend: { 
          data: ['预警数量', '新增老人', '健康评分'], 
          top: 50 
        },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { 
          type: 'category', 
          data: warning.map(i => i.date) 
        },
        yAxis: [
          { type: 'value', name: '数量' },
          { type: 'value', name: '评分', min: 0, max: 100 }
        ],
        series: [
          {
            name: '预警数量',
            type: 'line',
            data: warning.map(i => i.value),
            smooth: true,
            itemStyle: { color: '#ff7a45' }
          },
          {
            name: '新增老人',
            type: 'bar',
            data: elderly.map(i => i.value),
            itemStyle: { color: '#5470c6' }
          },
          {
            name: '健康评分',
            type: 'line',
            yAxisIndex: 1,
            data: health.map(i => i.value),
            smooth: true,
            itemStyle: { color: '#73c0de' }
          }
        ]
      }
    }
  } catch (error) {
    console.error('获取趋势分析失败:', error)
    ElMessage.error('获取趋势分析失败')
  }
}

// 刷新所有数据
const refreshAllData = async () => {
  loading.value = true
  try {
    await Promise.all([
      fetchOverview(),
      fetchHealthStatus(),
      fetchWarningAnalysis(),
      fetchTrend(),
      fetchMedicalService(),
      fetchCareLevel(),
      fetchEquipmentUsage()
    ])
    updateTime.value = new Date().toLocaleString()
    ElMessage.success('数据刷新完成')
  } catch (error) {
    console.error('数据刷新失败:', error)
    ElMessage.error('数据刷新失败')
  } finally {
    loading.value = false
  }
}

// 导出报表
const exportReport = () => {
  ElMessageBox.confirm('确定要导出当前报表数据吗？', '导出确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(() => {
    // 实现基本的JSON导出功能
    const reportData = {
      updateTime: updateTime.value,
      overview: overviewList.value,
      timestamp: new Date().toISOString()
    }
    
    const dataStr = JSON.stringify(reportData, null, 2)
    const dataBlob = new Blob([dataStr], { type: 'application/json' })
    const url = URL.createObjectURL(dataBlob)
    
    const link = document.createElement('a')
    link.href = url
    link.download = `智慧医养统计报表_${new Date().toISOString().split('T')[0]}.json`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
    
    ElMessage.success('报表已导出')
  }).catch(() => {
    // 取消导出
  })
}

// 初始化数据
const initData = async () => {
  loading.value = true
  try {
    await Promise.all([
      fetchOverview(),
      fetchHealthStatus(),
      fetchWarningAnalysis(),
      fetchMedicalService(),
      fetchCareLevel(),
      fetchEquipmentUsage(),
      fetchTrend()
    ])
    updateTime.value = new Date().toLocaleString()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  initData()
})

watch(trendRange, fetchTrend)
</script>

<style scoped>
.report-statistics {
  padding: 24px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 页面头部样式 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 20px 0;
}

.page-header h1 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.8em;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2, .card-header h3 {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  color: #303133;
}

.update-time {
  color: #909399;
  font-size: 14px;
}

.overview-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
}

.overview-card :deep(.el-card__header) {
  background: transparent;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.overview-card .card-header h2 {
  color: white;
  margin: 0;
}

.overview-card .update-time {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9em;
}

.overview-row {
  margin-bottom: 10px;
}

.overview-item {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  color: white;
  transition: transform 0.3s ease;
  display: flex;
  align-items: center;
  gap: 15px;
}

.overview-item:hover {
  transform: translateY(-5px);
}

.overview-item.up {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.overview-item.down {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
}

.overview-item.stable {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.overview-icon {
  font-size: 2.5em;
  opacity: 0.8;
}

.overview-content {
  flex: 1;
  text-align: left;
}

.overview-value {
  font-size: 2.2em;
  font-weight: bold;
  line-height: 1.2;
}

.overview-label {
  font-size: 1em;
  margin-top: 5px;
  opacity: 0.9;
}

.overview-change {
  font-size: 0.85em;
  margin-top: 3px;
}

.overview-change .up {
  color: #67c23a;
}

.overview-change .down {
  color: #f56c6c;
}

.overview-change .stable {
  color: #e6a23c;
}

.equipment-status {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.status-item {
  text-align: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 10px;
}

.status-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.status-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.status-value.success {
  color: #67c23a;
}

.status-value.danger {
  color: #f56c6c;
}

.status-value.warning {
  color: #e6a23c;
}

:deep(.el-card) {
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
  transition: box-shadow 0.3s ease;
}

:deep(.el-card:hover) {
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.12);
}

:deep(.el-card__header) {
  background: #fafbfc;
  border-bottom: 1px solid #ebeef5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .report-statistics {
    padding: 16px;
  }
  
  .overview-item {
    flex-direction: column;
    text-align: center;
    gap: 10px;
  }
  
  .overview-content {
    text-align: center;
  }
  
  .overview-icon {
    font-size: 2em;
  }
  
  .overview-value {
    font-size: 1.8em;
  }
}
</style>
