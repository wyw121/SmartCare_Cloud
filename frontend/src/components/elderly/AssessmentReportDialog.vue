<template>
  <el-dialog
    v-model="visible"
    title="健康评估报告"
    width="90%"
    class="assessment-dialog"
    :close-on-click-modal="false"
    destroy-on-close
  >
    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading">
        <Loading />
      </el-icon>
      <p>正在生成评估报告...</p>
    </div>

    <div v-else-if="reportData" class="assessment-report">
      <!-- 报告头部 -->
      <div class="report-header">
        <div class="header-info">
          <h2>{{ reportData.elderlyName }} - 健康评估报告</h2>
          <p class="assessment-date">评估日期：{{ formatDateTime(reportData.assessmentDate) }}</p>
        </div>
        <div class="overall-score">
          <div class="score-circle" :class="getScoreClass(reportData.overallScore)">
            <span class="score">{{ reportData.overallScore }}</span>
            <span class="score-text">分</span>
          </div>
          <div class="score-level">{{ reportData.overallLevel }}</div>
        </div>
      </div>

      <!-- 基本信息 -->
      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="section-title">
            <el-icon><User /></el-icon>
            <span>基本信息</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <label>年龄：</label>
              <span>{{ reportData.basicInfo?.age || '--' }} 岁</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>性别：</label>
              <span>{{ reportData.basicInfo?.gender || '--' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>身高：</label>
              <span>{{ reportData.basicInfo?.height || '--' }} cm</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>体重：</label>
              <span>{{ reportData.basicInfo?.weight || '--' }} kg</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>BMI：</label>
              <span>{{ reportData.basicInfo?.bmi || '--' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>BMI状态：</label>
              <span :class="getBMIClass(reportData.basicInfo?.bmiStatus)">{{ reportData.basicInfo?.bmiStatus || '--' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>健康状态：</label>
              <span :class="getHealthStatusClass(reportData.basicInfo?.healthStatus)">{{ reportData.basicInfo?.healthStatus || '--' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>照护等级：</label>
              <span>{{ reportData.basicInfo?.careLevel || '--' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 生理指标评估 -->
      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="section-title">
            <el-icon><Monitor /></el-icon>
            <span>生理指标评估</span>
            <div class="section-score" v-if="reportData.physiological?.totalScore">
              <el-tag :type="getScoreTagType(reportData.physiological.totalScore)">
                {{ reportData.physiological.totalScore }}分 - {{ reportData.physiological.level }}
              </el-tag>
            </div>
          </div>
        </template>
        
        <div v-if="!reportData.physiological" class="no-data">
          <p>暂无生理指标数据</p>
        </div>
        
        <el-row :gutter="20" v-else>
          <el-col :span="12" v-if="reportData.physiological?.bloodPressure">
            <div class="vital-sign-card">
              <h4>血压</h4>
              <div class="vital-value">{{ reportData.physiological.bloodPressure.currentValue }}</div>
              <div class="vital-normal">正常范围：{{ reportData.physiological.bloodPressure.normalRange }}</div>
              <div class="vital-status">
                <el-tag :type="getStatusTagType(reportData.physiological.bloodPressure.status)">
                  {{ reportData.physiological.bloodPressure.status }}
                </el-tag>
                <span class="vital-score">{{ reportData.physiological.bloodPressure.score }}分</span>
              </div>
              <div class="vital-evaluation">{{ reportData.physiological.bloodPressure.evaluation }}</div>
            </div>
          </el-col>
          
          <el-col :span="12" v-if="reportData.physiological?.heartRate">
            <div class="vital-sign-card">
              <h4>心率</h4>
              <div class="vital-value">{{ reportData.physiological.heartRate.currentValue }}</div>
              <div class="vital-normal">正常范围：{{ reportData.physiological.heartRate.normalRange }}</div>
              <div class="vital-status">
                <el-tag :type="getStatusTagType(reportData.physiological.heartRate.status)">
                  {{ reportData.physiological.heartRate.status }}
                </el-tag>
                <span class="vital-score">{{ reportData.physiological.heartRate.score }}分</span>
              </div>
              <div class="vital-evaluation">{{ reportData.physiological.heartRate.evaluation }}</div>
            </div>
          </el-col>
          
          <el-col :span="12" v-if="reportData.physiological?.bodyTemperature">
            <div class="vital-sign-card">
              <h4>体温</h4>
              <div class="vital-value">{{ reportData.physiological.bodyTemperature.currentValue }}</div>
              <div class="vital-normal">正常范围：{{ reportData.physiological.bodyTemperature.normalRange }}</div>
              <div class="vital-status">
                <el-tag :type="getStatusTagType(reportData.physiological.bodyTemperature.status)">
                  {{ reportData.physiological.bodyTemperature.status }}
                </el-tag>
                <span class="vital-score">{{ reportData.physiological.bodyTemperature.score }}分</span>
              </div>
              <div class="vital-evaluation">{{ reportData.physiological.bodyTemperature.evaluation }}</div>
            </div>
          </el-col>
          
          <el-col :span="12" v-if="reportData.physiological?.bloodGlucose">
            <div class="vital-sign-card">
              <h4>血糖</h4>
              <div class="vital-value">{{ reportData.physiological.bloodGlucose.currentValue }}</div>
              <div class="vital-normal">正常范围：{{ reportData.physiological.bloodGlucose.normalRange }}</div>
              <div class="vital-status">
                <el-tag :type="getStatusTagType(reportData.physiological.bloodGlucose.status)">
                  {{ reportData.physiological.bloodGlucose.status }}
                </el-tag>
                <span class="vital-score">{{ reportData.physiological.bloodGlucose.score }}分</span>
              </div>
              <div class="vital-evaluation">{{ reportData.physiological.bloodGlucose.evaluation }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 健康风险评估 -->
      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="section-title">
            <el-icon><Warning /></el-icon>
            <span>健康风险评估</span>
          </div>
        </template>
        
        <div v-if="!reportData.healthRisks || reportData.healthRisks.length === 0" class="no-data">
          <p>暂无健康风险数据</p>
        </div>
        
        <div v-else class="risks-container">
          <div v-for="risk in reportData.healthRisks" :key="risk.riskType" class="risk-item">
            <div class="risk-header">
              <h4>{{ risk.riskType }}</h4>
              <el-tag :type="getRiskTagType(risk.riskLevel)">{{ risk.riskLevel }}风险</el-tag>
              <span class="risk-score">{{ risk.riskScore }}分</span>
            </div>
            <div class="risk-description">{{ risk.description }}</div>
            <div class="risk-prevention">
              <strong>预防措施：</strong>{{ risk.prevention }}
            </div>
          </div>
        </div>
      </el-card>

      <!-- 健康建议 -->
      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="section-title">
            <el-icon><Document /></el-icon>
            <span>健康建议</span>
          </div>
        </template>
        
        <div v-if="!reportData.recommendations || reportData.recommendations.length === 0" class="no-data">
          <p>暂无健康建议数据</p>
        </div>
        
        <div v-else class="recommendations-container">
          <div v-for="rec in reportData.recommendations" :key="rec.type" class="recommendation-item">
            <div class="rec-header">
              <h4>{{ rec.type }}</h4>
              <el-tag :type="getPriorityTagType(rec.priority)">{{ rec.priority }}优先级</el-tag>
              <span class="rec-cycle">{{ rec.cycle }}</span>
            </div>
            <div class="rec-content">{{ rec.content }}</div>
          </div>
        </div>
      </el-card>

      <!-- 健康趋势 -->
      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="section-title">
            <el-icon><TrendCharts /></el-icon>
            <span>健康趋势分析</span>
          </div>
        </template>
        
        <div v-if="!reportData.trend" class="no-data">
          <p>暂无健康趋势数据</p>
        </div>
        
        <div v-else class="trend-container">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="trend-item">
                <label>总体趋势：</label>
                <span :class="getTrendClass(reportData.trend.overallTrend)">{{ reportData.trend.overallTrend || '--' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="trend-item">
                <label>血压趋势：</label>
                <span :class="getTrendClass(reportData.trend.bloodPressureTrend)">{{ reportData.trend.bloodPressureTrend || '--' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="trend-item">
                <label>心率趋势：</label>
                <span :class="getTrendClass(reportData.trend.heartRateTrend)">{{ reportData.trend.heartRateTrend || '--' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="trend-item">
                <label>血糖趋势：</label>
                <span :class="getTrendClass(reportData.trend.bloodGlucoseTrend)">{{ reportData.trend.bloodGlucoseTrend || '--' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="trend-item">
                <label>体重趋势：</label>
                <span :class="getTrendClass(reportData.trend.weightTrend)">{{ reportData.trend.weightTrend || '--' }}</span>
              </div>
            </el-col>
          </el-row>
          <div class="trend-analysis" v-if="reportData.trend.analysis">
            <strong>趋势分析：</strong>{{ reportData.trend.analysis }}
          </div>
        </div>
      </el-card>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handlePrint">
          <el-icon><Printer /></el-icon>
          打印报告
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出PDF
        </el-button>
        <el-button type="primary" @click="visible = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { generateAssessmentReport } from '@/api/elderly'
import {
  User,
  Monitor,
  Warning,
  Document,
  TrendCharts,
  Loading,
  Printer,
  Download
} from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  elderlyId: {
    type: Number,
    default: null
  }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(false)
const loading = ref(false)
const reportData = ref(null)

// 监听弹窗显示状态
watch(() => props.modelValue, (newVal) => {
  visible.value = newVal
  if (newVal && props.elderlyId) {
    loadReport()
  }
})

watch(visible, (newVal) => {
  emit('update:modelValue', newVal)
  if (!newVal) {
    reportData.value = null
  }
})

// 加载评估报告
const loadReport = async () => {
  loading.value = true
  try {
    const response = await generateAssessmentReport(props.elderlyId)
    if (response.code === 200) {
      reportData.value = response.data
    } else {
      ElMessage.error('生成报告失败：' + response.message)
    }
  } catch (error) {
    console.error('加载评估报告失败：', error)
    ElMessage.error('生成报告失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '--'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 获取评分样式类
const getScoreClass = (score) => {
  if (score >= 85) return 'excellent'
  if (score >= 70) return 'good'
  if (score >= 60) return 'normal'
  if (score >= 40) return 'poor'
  return 'bad'
}

// 获取评分标签类型
const getScoreTagType = (score) => {
  if (score >= 85) return 'success'
  if (score >= 70) return ''
  if (score >= 60) return 'warning'
  return 'danger'
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case '正常': return 'success'
    case '偏高':
    case '偏低': return 'warning'
    case '异常': return 'danger'
    default: return 'info'
  }
}

// 获取BMI样式类
const getBMIClass = (status) => {
  switch (status) {
    case '正常': return 'text-success'
    case '偏瘦':
    case '偏胖': return 'text-warning'
    case '肥胖': return 'text-danger'
    default: return ''
  }
}

// 获取健康状态样式类
const getHealthStatusClass = (status) => {
  switch (status) {
    case '健康': return 'text-success'
    case '亚健康': return 'text-warning'
    case '疾病':
    case '重病':
    case '危险':
    case '预警': return 'text-danger'
    default: return ''
  }
}

// 获取风险标签类型
const getRiskTagType = (level) => {
  switch (level) {
    case '低等': return 'success'
    case '中等': return 'warning'
    case '高等': return 'danger'
    default: return 'info'
  }
}

// 获取优先级标签类型
const getPriorityTagType = (priority) => {
  switch (priority) {
    case '高': return 'danger'
    case '中': return 'warning'
    case '低': return 'success'
    default: return 'info'
  }
}

// 获取趋势样式类
const getTrendClass = (trend) => {
  switch (trend) {
    case '上升':
    case '改善': return 'text-success'
    case '稳定': return 'text-info'
    case '下降':
    case '恶化': return 'text-danger'
    default: return ''
  }
}

// 打印报告
const handlePrint = () => {
  window.print()
}

// 导出PDF
const handleExport = () => {
  ElMessage.info('PDF导出功能开发中...')
}
</script>

<style scoped lang="scss">
.assessment-dialog {
  .loading-container {
    text-align: center;
    padding: 40px;
    
    .el-icon {
      font-size: 24px;
      margin-bottom: 10px;
    }
  }
}

.assessment-report {
  .no-data {
    text-align: center;
    padding: 40px;
    color: #909399;
    
    p {
      margin: 0;
      font-size: 14px;
    }
  }
  
  .report-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 8px;
    color: white;
    
    .header-info {
      h2 {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 600;
      }
      
      .assessment-date {
        margin: 0;
        opacity: 0.9;
        font-size: 14px;
      }
    }
    
    .overall-score {
      text-align: center;
      
      .score-circle {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        margin-bottom: 8px;
        
        &.excellent {
          background: rgba(103, 194, 58, 0.2);
          border: 3px solid #67c23a;
        }
        
        &.good {
          background: rgba(230, 162, 60, 0.2);
          border: 3px solid #e6a23c;
        }
        
        &.normal {
          background: rgba(245, 108, 108, 0.2);
          border: 3px solid #f56c6c;
        }
        
        &.poor,
        &.bad {
          background: rgba(245, 108, 108, 0.3);
          border: 3px solid #f56c6c;
        }
        
        .score {
          font-size: 24px;
          font-weight: bold;
          line-height: 1;
        }
        
        .score-text {
          font-size: 12px;
          line-height: 1;
        }
      }
      
      .score-level {
        font-size: 14px;
        font-weight: 500;
      }
    }
  }
  
  .section-card {
    margin-bottom: 20px;
    
    .section-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      
      .section-score {
        margin-left: auto;
      }
    }
  }
  
  .info-item {
    margin-bottom: 12px;
    
    label {
      font-weight: 500;
      color: #606266;
      margin-right: 8px;
    }
  }
  
  .vital-sign-card {
    padding: 16px;
    border: 1px solid #ebeef5;
    border-radius: 6px;
    margin-bottom: 16px;
    
    h4 {
      margin: 0 0 12px 0;
      color: #303133;
      font-size: 16px;
    }
    
    .vital-value {
      font-size: 20px;
      font-weight: bold;
      color: #409eff;
      margin-bottom: 6px;
    }
    
    .vital-normal {
      font-size: 12px;
      color: #909399;
      margin-bottom: 8px;
    }
    
    .vital-status {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
      
      .vital-score {
        font-size: 12px;
        color: #606266;
      }
    }
    
    .vital-evaluation {
      font-size: 13px;
      color: #606266;
      line-height: 1.4;
    }
  }
  
  .risks-container {
    .risk-item {
      padding: 16px;
      border: 1px solid #ebeef5;
      border-radius: 6px;
      margin-bottom: 12px;
      
      .risk-header {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 8px;
        
        h4 {
          margin: 0;
          color: #303133;
        }
        
        .risk-score {
          font-size: 12px;
          color: #909399;
          margin-left: auto;
        }
      }
      
      .risk-description {
        color: #606266;
        margin-bottom: 8px;
        line-height: 1.4;
      }
      
      .risk-prevention {
        color: #606266;
        font-size: 13px;
        line-height: 1.4;
      }
    }
  }
  
  .recommendations-container {
    .recommendation-item {
      padding: 16px;
      border: 1px solid #ebeef5;
      border-radius: 6px;
      margin-bottom: 12px;
      
      .rec-header {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 8px;
        
        h4 {
          margin: 0;
          color: #303133;
        }
        
        .rec-cycle {
          font-size: 12px;
          color: #909399;
          margin-left: auto;
        }
      }
      
      .rec-content {
        color: #606266;
        line-height: 1.4;
      }
    }
  }
  
  .trend-container {
    .trend-item {
      margin-bottom: 12px;
      
      label {
        font-weight: 500;
        color: #606266;
        margin-right: 8px;
      }
    }
    
    .trend-analysis {
      margin-top: 16px;
      padding: 12px;
      background: #f5f7fa;
      border-radius: 4px;
      color: #606266;
      line-height: 1.4;
    }
  }
}

// 文本颜色类
.text-success {
  color: #67c23a;
}

.text-warning {
  color: #e6a23c;
}

.text-danger {
  color: #f56c6c;
}

.text-info {
  color: #909399;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

// 打印样式
@media print {
  .assessment-dialog {
    .el-dialog__header,
    .el-dialog__footer {
      display: none !important;
    }
    
    .el-dialog__body {
      padding: 0 !important;
    }
  }
  
  .assessment-report {
    .report-header {
      background: #667eea !important;
      color: white !important;
      -webkit-print-color-adjust: exact;
      print-color-adjust: exact;
    }
  }
}
</style>
