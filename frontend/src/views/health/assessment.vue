<template>
  <div class="health-assessment">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1><el-icon><DataAnalysis /></el-icon> 健康评估报告</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleRefresh">
          <el-icon><Refresh /></el-icon> 刷新
        </el-button>
        <el-button type="success" @click="handleExport">
          <el-icon><Download /></el-icon> 导出
        </el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline>
        <el-form-item label="老人姓名">
          <el-input 
            v-model="queryForm.elderlyName" 
            placeholder="请输入老人姓名" 
            clearable 
            style="width: 200px" />
        </el-form-item>
        <el-form-item label="评估日期">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon><Search /></el-icon> 查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshLeft /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 评估列表 -->
    <el-card class="table-card" shadow="never">
      <div class="card-header">
        <h3>评估记录列表</h3>
        <el-button type="primary" @click="handleCreateAssessment">
          <el-icon><Plus /></el-icon> 新建评估
        </el-button>
      </div>

      <el-table 
        v-loading="loading" 
        :data="assessmentList" 
        stripe 
        border
        @row-click="handleRowClick"
        style="width: 100%">
        <el-table-column prop="elderlyId" label="老人ID" width="100" />
        <el-table-column prop="elderlyName" label="老人姓名" width="120" />
        <el-table-column prop="assessmentDate" label="评估日期" width="180" />
        <el-table-column prop="overallScore" label="综合评分" width="120">
          <template #default="{ row }">
            <el-tag :type="getScoreType(row.overallScore)" effect="dark">
              {{ row.overallScore }} 分
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="overallLevel" label="健康等级" width="120">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.overallLevel)">
              {{ row.overallLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskCount" label="风险项" width="100">
          <template #default="{ row }">
            <span :style="{ color: row.riskCount > 0 ? '#F56C6C' : '#67C23A' }">
              {{ row.riskCount }} 项
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click.stop="handleViewReport(row)">
              <el-icon><View /></el-icon> 查看报告
            </el-button>
            <el-button link type="success" size="small" @click.stop="handleDownloadReport(row)">
              <el-icon><Download /></el-icon> 下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 评估报告详情对话框 -->
    <el-dialog 
      v-model="reportVisible" 
      :title="`${currentReport?.elderlyName} - 健康评估报告`"
      width="1000px"
      top="5vh">
      <div v-if="currentReport" class="report-detail" v-loading="reportLoading">
        <!-- 综合评分 -->
        <el-card class="score-card" shadow="never">
          <div class="score-content">
            <div class="score-circle">
              <div class="score-value">{{ currentReport.overallScore }}</div>
              <div class="score-label">综合评分</div>
            </div>
            <div class="score-info">
              <div class="info-item">
                <label>健康等级:</label>
                <el-tag :type="getLevelType(currentReport.overallLevel)" size="large">
                  {{ currentReport.overallLevel }}
                </el-tag>
              </div>
              <div class="info-item">
                <label>评估日期:</label>
                <span>{{ currentReport.assessmentDate }}</span>
              </div>
              <div class="info-item">
                <label>风险项数:</label>
                <span :style="{ color: currentReport.healthRisks?.length > 0 ? '#F56C6C' : '#67C23A' }">
                  {{ currentReport.healthRisks?.length || 0 }} 项
                </span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 基本信息 -->
        <el-card class="section-card" shadow="never" v-if="currentReport.basicInfo">
          <template #header>
            <h3><el-icon><User /></el-icon> 基本信息</h3>
          </template>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="姓名">{{ currentReport.basicInfo.name }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ currentReport.basicInfo.age }} 岁</el-descriptions-item>
            <el-descriptions-item label="性别">{{ currentReport.basicInfo.gender }}</el-descriptions-item>
            <el-descriptions-item label="身高">{{ currentReport.basicInfo.height }} cm</el-descriptions-item>
            <el-descriptions-item label="体重">{{ currentReport.basicInfo.weight }} kg</el-descriptions-item>
            <el-descriptions-item label="BMI">{{ currentReport.basicInfo.bmi }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 生理指标 -->
        <el-card class="section-card" shadow="never" v-if="currentReport.physiological">
          <template #header>
            <h3><el-icon><Monitor /></el-icon> 生理指标评估</h3>
          </template>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="indicator-item">
                <div class="indicator-header">
                  <span class="indicator-name">血压</span>
                  <el-tag :type="getIndicatorType(currentReport.physiological.bloodPressureLevel)">
                    {{ currentReport.physiological.bloodPressureLevel }}
                  </el-tag>
                </div>
                <div class="indicator-value">
                  {{ currentReport.physiological.bloodPressure }}
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="indicator-item">
                <div class="indicator-header">
                  <span class="indicator-name">心率</span>
                  <el-tag :type="getIndicatorType(currentReport.physiological.heartRateLevel)">
                    {{ currentReport.physiological.heartRateLevel }}
                  </el-tag>
                </div>
                <div class="indicator-value">
                  {{ currentReport.physiological.heartRate }} 次/分
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="indicator-item">
                <div class="indicator-header">
                  <span class="indicator-name">血糖</span>
                  <el-tag :type="getIndicatorType(currentReport.physiological.bloodSugarLevel)">
                    {{ currentReport.physiological.bloodSugarLevel }}
                  </el-tag>
                </div>
                <div class="indicator-value">
                  {{ currentReport.physiological.bloodSugar }} mmol/L
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="indicator-item">
                <div class="indicator-header">
                  <span class="indicator-name">体温</span>
                  <el-tag :type="getIndicatorType(currentReport.physiological.temperatureLevel)">
                    {{ currentReport.physiological.temperatureLevel }}
                  </el-tag>
                </div>
                <div class="indicator-value">
                  {{ currentReport.physiological.temperature }} ℃
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 健康风险 -->
        <el-card class="section-card" shadow="never" v-if="currentReport.healthRisks?.length">
          <template #header>
            <h3><el-icon><Warning /></el-icon> 健康风险评估</h3>
          </template>
          <div class="risk-list">
            <div 
              v-for="(risk, index) in currentReport.healthRisks" 
              :key="index" 
              class="risk-item"
              :class="risk.level">
              <div class="risk-header">
                <el-tag :type="getRiskType(risk.level)" effect="dark">
                  {{ risk.level }}
                </el-tag>
                <span class="risk-name">{{ risk.riskType }}</span>
              </div>
              <div class="risk-description">{{ risk.description }}</div>
            </div>
          </div>
        </el-card>

        <!-- 健康建议 -->
        <el-card class="section-card" shadow="never" v-if="currentReport.recommendations?.length">
          <template #header>
            <h3><el-icon><CircleCheck /></el-icon> 健康建议</h3>
          </template>
          <div class="recommendations-list">
            <div 
              v-for="(rec, index) in currentReport.recommendations" 
              :key="index" 
              class="recommendation-item">
              <div class="recommendation-header">
                <el-tag type="primary">{{ rec.category }}</el-tag>
                <span class="recommendation-title">{{ rec.title }}</span>
              </div>
              <div class="recommendation-content">{{ rec.content }}</div>
            </div>
          </div>
        </el-card>
      </div>
      <template #footer>
        <el-button @click="reportVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleDownloadReport(currentReport)">
          <el-icon><Download /></el-icon> 下载报告
        </el-button>
      </template>
    </el-dialog>

    <!-- 新建评估对话框 -->
    <el-dialog 
      v-model="createVisible" 
      title="新建健康评估"
      width="600px">
      <el-form :model="createForm" :rules="createRules" ref="createFormRef" label-width="100px">
        <el-form-item label="选择老人" prop="elderlyId">
          <el-select 
            v-model="createForm.elderlyId" 
            placeholder="请选择老人" 
            filterable
            style="width: 100%">
            <el-option 
              v-for="elderly in elderlyList" 
              :key="elderly.id" 
              :label="elderly.name" 
              :value="elderly.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估说明">
          <el-input 
            v-model="createForm.remark" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入评估说明（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCreateAssessment" :loading="submitting">
          生成评估报告
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  DataAnalysis, Refresh, Download, Search, RefreshLeft, 
  Plus, View, User, Monitor, Warning, CircleCheck 
} from '@element-plus/icons-vue'
import { getElderlyList } from '@/api/elderly'

// 模拟数据 - 实际应该从后端获取
const mockAssessmentList = ref([
  {
    id: 1,
    elderlyId: 1,
    elderlyName: '张三',
    assessmentDate: '2024-01-15 10:30:00',
    overallScore: 85,
    overallLevel: '良好',
    riskCount: 1
  },
  {
    id: 2,
    elderlyId: 2,
    elderlyName: '李四',
    assessmentDate: '2024-01-14 14:20:00',
    overallScore: 72,
    overallLevel: '一般',
    riskCount: 3
  },
  {
    id: 3,
    elderlyId: 3,
    elderlyName: '王五',
    assessmentDate: '2024-01-13 09:15:00',
    overallScore: 92,
    overallLevel: '优秀',
    riskCount: 0
  }
])

const mockReportDetail = {
  elderlyId: 1,
  elderlyName: '张三',
  assessmentDate: '2024-01-15 10:30:00',
  overallScore: 85,
  overallLevel: '良好',
  basicInfo: {
    name: '张三',
    age: 75,
    gender: '男',
    height: 170,
    weight: 65,
    bmi: 22.5
  },
  physiological: {
    bloodPressure: '130/85',
    bloodPressureLevel: '正常',
    heartRate: 72,
    heartRateLevel: '正常',
    bloodSugar: 5.8,
    bloodSugarLevel: '正常',
    temperature: 36.5,
    temperatureLevel: '正常'
  },
  healthRisks: [
    {
      level: '中等风险',
      riskType: '高血压风险',
      description: '收缩压偏高，建议控制盐分摄入，定期监测血压'
    }
  ],
  recommendations: [
    {
      category: '饮食建议',
      title: '低盐低脂饮食',
      content: '每日盐摄入量控制在6克以内，减少油腻食物摄入，多食用蔬菜水果'
    },
    {
      category: '运动建议',
      title: '适量有氧运动',
      content: '每天坚持30分钟散步或太极拳，避免剧烈运动'
    },
    {
      category: '生活建议',
      title: '规律作息',
      content: '保持良好的睡眠习惯，每天睡眠时间7-8小时'
    }
  ]
}

// 查询表单
const queryForm = reactive({
  elderlyName: '',
  dateRange: []
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 列表数据
const loading = ref(false)
const assessmentList = ref([])

// 报告详情
const reportVisible = ref(false)
const reportLoading = ref(false)
const currentReport = ref(null)

// 新建评估
const createVisible = ref(false)
const submitting = ref(false)
const createFormRef = ref(null)
const elderlyList = ref([])
const createForm = reactive({
  elderlyId: null,
  remark: ''
})
const createRules = {
  elderlyId: [
    { required: true, message: '请选择老人', trigger: 'change' }
  ]
}

// 获取评估列表
const fetchAssessmentList = async () => {
  loading.value = true
  try {
    // TODO: 调用真实API
    // const res = await getAssessmentList(queryForm)
    
    // 模拟延迟
    await new Promise(resolve => setTimeout(resolve, 500))
    
    assessmentList.value = mockAssessmentList.value
    pagination.total = mockAssessmentList.value.length
  } catch (error) {
    console.error('获取评估列表失败:', error)
    ElMessage.error('获取评估列表失败')
  } finally {
    loading.value = false
  }
}

// 获取老人列表
const fetchElderlyList = async () => {
  try {
    const res = await getElderlyList({ pageNum: 1, pageSize: 100 })
    if (res.code === 200) {
      elderlyList.value = res.data.records || []
    }
  } catch (error) {
    console.error('获取老人列表失败:', error)
  }
}

// 查询
const handleQuery = () => {
  pagination.pageNum = 1
  fetchAssessmentList()
}

// 重置
const handleReset = () => {
  queryForm.elderlyName = ''
  queryForm.dateRange = []
  handleQuery()
}

// 刷新
const handleRefresh = () => {
  fetchAssessmentList()
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 分页改变
const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchAssessmentList()
}

const handleCurrentChange = (page) => {
  pagination.pageNum = page
  fetchAssessmentList()
}

// 行点击
const handleRowClick = (row) => {
  handleViewReport(row)
}

// 查看报告
const handleViewReport = async (row) => {
  reportLoading.value = true
  reportVisible.value = true
  
  try {
    // TODO: 调用真实API
    // const res = await getAssessmentDetail(row.id)
    
    // 模拟延迟
    await new Promise(resolve => setTimeout(resolve, 300))
    
    currentReport.value = mockReportDetail
  } catch (error) {
    console.error('获取评估详情失败:', error)
    ElMessage.error('获取评估详情失败')
  } finally {
    reportLoading.value = false
  }
}

// 下载报告
const handleDownloadReport = (row) => {
  ElMessage.info(`下载 ${row.elderlyName} 的评估报告...`)
  // TODO: 实现下载功能
}

// 新建评估
const handleCreateAssessment = () => {
  createForm.elderlyId = null
  createForm.remark = ''
  createVisible.value = true
  if (elderlyList.value.length === 0) {
    fetchElderlyList()
  }
}

// 提交新建评估
const submitCreateAssessment = async () => {
  if (!createFormRef.value) return
  
  await createFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        // TODO: 调用真实API
        // const res = await createAssessment(createForm)
        
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        ElMessage.success('评估报告生成成功')
        createVisible.value = false
        fetchAssessmentList()
      } catch (error) {
        console.error('生成评估报告失败:', error)
        ElMessage.error('生成评估报告失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

// 获取评分类型
const getScoreType = (score) => {
  if (score >= 90) return 'success'
  if (score >= 80) return 'primary'
  if (score >= 70) return 'warning'
  return 'danger'
}

// 获取等级类型
const getLevelType = (level) => {
  const typeMap = {
    '优秀': 'success',
    '良好': 'primary',
    '一般': 'warning',
    '较差': 'danger'
  }
  return typeMap[level] || 'info'
}

// 获取指标类型
const getIndicatorType = (level) => {
  const typeMap = {
    '正常': 'success',
    '偏高': 'warning',
    '偏低': 'warning',
    '异常': 'danger'
  }
  return typeMap[level] || 'info'
}

// 获取风险类型
const getRiskType = (level) => {
  const typeMap = {
    '高风险': 'danger',
    '中等风险': 'warning',
    '低风险': 'primary'
  }
  return typeMap[level] || 'info'
}

onMounted(() => {
  fetchAssessmentList()
})
</script>

<style scoped lang="scss">
.health-assessment {
  padding: 20px;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h1 {
      font-size: 24px;
      font-weight: 500;
      color: #303133;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 500;
      }
    }

    :deep(.el-table) {
      cursor: pointer;

      .el-table__row {
        &:hover {
          background-color: #f5f7fa;
        }
      }
    }
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  // 报告详情样式
  .report-detail {
    .score-card {
      margin-bottom: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;

      :deep(.el-card__body) {
        padding: 30px;
      }

      .score-content {
        display: flex;
        align-items: center;
        gap: 40px;

        .score-circle {
          width: 150px;
          height: 150px;
          border-radius: 50%;
          background: rgba(255, 255, 255, 0.2);
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          border: 3px solid rgba(255, 255, 255, 0.5);

          .score-value {
            font-size: 48px;
            font-weight: bold;
            line-height: 1;
          }

          .score-label {
            font-size: 14px;
            margin-top: 8px;
            opacity: 0.9;
          }
        }

        .score-info {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: 16px;

          .info-item {
            display: flex;
            align-items: center;
            gap: 12px;
            font-size: 16px;

            label {
              font-weight: 500;
              opacity: 0.9;
            }
          }
        }
      }
    }

    .section-card {
      margin-bottom: 20px;

      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .indicator-item {
        padding: 15px;
        border: 1px solid #EBEEF5;
        border-radius: 4px;
        margin-bottom: 15px;

        .indicator-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 10px;

          .indicator-name {
            font-size: 14px;
            color: #606266;
          }
        }

        .indicator-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
        }
      }

      .risk-list {
        .risk-item {
          padding: 15px;
          border-left: 4px solid #409EFF;
          background-color: #f5f7fa;
          margin-bottom: 15px;
          border-radius: 4px;

          &.高风险 {
            border-left-color: #F56C6C;
          }

          &.中等风险 {
            border-left-color: #E6A23C;
          }

          &.低风险 {
            border-left-color: #409EFF;
          }

          .risk-header {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 8px;

            .risk-name {
              font-size: 15px;
              font-weight: 500;
              color: #303133;
            }
          }

          .risk-description {
            font-size: 14px;
            color: #606266;
            line-height: 1.6;
          }
        }
      }

      .recommendations-list {
        .recommendation-item {
          padding: 15px;
          border: 1px solid #EBEEF5;
          border-radius: 4px;
          margin-bottom: 15px;
          transition: all 0.3s;

          &:hover {
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
          }

          .recommendation-header {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 10px;

            .recommendation-title {
              font-size: 15px;
              font-weight: 500;
              color: #303133;
            }
          }

          .recommendation-content {
            font-size: 14px;
            color: #606266;
            line-height: 1.6;
          }
        }
      }
    }
  }
}
</style>
