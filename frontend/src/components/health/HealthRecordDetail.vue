<template>
  <el-dialog
    v-model="dialogVisible"
    title="健康记录详情"
    width="700px"
    :before-close="handleClose"
  >
    <div v-if="record" class="record-detail">
      <!-- 基本信息 -->
      <div class="detail-section">
        <h3 class="section-title">基本信息</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="记录类型">
            <el-tag :type="getRecordTypeTag(record.recordType)">
              {{ getRecordTypeText(record.recordType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="记录日期">
            {{ formatDateTime(record.recordDate) }}
          </el-descriptions-item>
          <el-descriptions-item label="标题" v-if="record.title">
            {{ record.title }}
          </el-descriptions-item>
          <el-descriptions-item label="医生" v-if="record.doctorName">
            {{ record.doctorName }}
          </el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskLevelType(record.riskLevel)">
              {{ getRiskLevelText(record.riskLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="record.isAbnormal ? 'danger' : 'success'">
              {{ record.isAbnormal ? '异常' : '正常' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 生命体征 -->
      <div v-if="hasVitalSigns" class="detail-section">
        <h3 class="section-title">生命体征</h3>
        <div class="vital-signs-grid">
          <div v-if="record.bloodPressureHigh" class="vital-item">
            <div class="vital-icon">
              <i class="el-icon-monitor" style="color: #F56C6C;"></i>
            </div>
            <div class="vital-content">
              <div class="vital-label">血压</div>
              <div class="vital-value">{{ record.bloodPressureHigh }}/{{ record.bloodPressureLow }} mmHg</div>
            </div>
          </div>
          
          <div v-if="record.heartRate" class="vital-item">
            <div class="vital-icon">
              <i class="el-icon-stopwatch" style="color: #E6A23C;"></i>
            </div>
            <div class="vital-content">
              <div class="vital-label">心率</div>
              <div class="vital-value">{{ record.heartRate }} 次/分</div>
            </div>
          </div>
          
          <div v-if="record.temperature" class="vital-item">
            <div class="vital-icon">
              <i class="el-icon-thermometer" style="color: #67C23A;"></i>
            </div>
            <div class="vital-content">
              <div class="vital-label">体温</div>
              <div class="vital-value">{{ record.temperature }} ℃</div>
            </div>
          </div>
          
          <div v-if="record.bloodSugar" class="vital-item">
            <div class="vital-icon">
              <i class="el-icon-sugar" style="color: #409EFF;"></i>
            </div>
            <div class="vital-content">
              <div class="vital-label">血糖</div>
              <div class="vital-value">{{ record.bloodSugar }} mmol/L</div>
            </div>
          </div>
          
          <div v-if="record.weight" class="vital-item">
            <div class="vital-icon">
              <i class="el-icon-scale" style="color: #909399;"></i>
            </div>
            <div class="vital-content">
              <div class="vital-label">体重</div>
              <div class="vital-value">{{ record.weight }} kg</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 诊断信息 -->
      <div v-if="hasMedicalInfo" class="detail-section">
        <h3 class="section-title">诊断信息</h3>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="主要症状" v-if="record.symptoms">
            <div class="text-content">{{ record.symptoms }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="诊断结果" v-if="record.diagnosis">
            <div class="text-content">{{ record.diagnosis }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="治疗方案" v-if="record.treatment">
            <div class="text-content">{{ record.treatment }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="用药情况" v-if="record.medication">
            <div class="text-content">{{ record.medication }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 详细描述 -->
      <div v-if="record.description" class="detail-section">
        <h3 class="section-title">详细描述</h3>
        <div class="description-content">
          {{ record.description }}
        </div>
      </div>

      <!-- 备注 -->
      <div v-if="record.remarks" class="detail-section">
        <h3 class="section-title">备注</h3>
        <div class="remarks-content">
          {{ record.remarks }}
        </div>
      </div>

      <!-- 操作记录 -->
      <div class="detail-section">
        <h3 class="section-title">操作记录</h3>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="创建时间" v-if="record.createTime">
            {{ formatDateTime(record.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" v-if="record.updateTime">
            {{ formatDateTime(record.updateTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="创建人" v-if="record.createBy">
            {{ record.createBy }}
          </el-descriptions-item>
          <el-descriptions-item label="更新人" v-if="record.updateBy">
            {{ record.updateBy }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button type="primary" @click="handleEdit">编辑记录</el-button>
        <el-button type="success" @click="handlePrint">打印记录</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  record: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'edit'])

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 计算属性
const hasVitalSigns = computed(() => {
  if (!props.record) return false
  return props.record.bloodPressureHigh || props.record.heartRate || 
         props.record.temperature || props.record.bloodSugar || props.record.weight
})

const hasMedicalInfo = computed(() => {
  if (!props.record) return false
  return props.record.symptoms || props.record.diagnosis || 
         props.record.treatment || props.record.medication
})

// 工具函数
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

const getRiskLevelText = (level) => {
  const textMap = {
    0: '正常',
    1: '轻度',
    2: '中度',
    3: '重度'
  }
  return textMap[level] || '未知'
}

const getRiskLevelType = (level) => {
  const typeMap = {
    0: 'success',
    1: 'warning',
    2: 'danger',
    3: 'danger'
  }
  return typeMap[level] || 'info'
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 事件处理
const handleClose = () => {
  emit('update:visible', false)
}

const handleEdit = () => {
  emit('edit', props.record)
  handleClose()
}

const handlePrint = () => {
  window.print()
}
</script>

<style scoped>
.record-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 25px;
}

.section-title {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 2px solid #409EFF;
  padding-bottom: 8px;
}

/* 生命体征网格 */
.vital-signs-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.vital-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.vital-icon {
  font-size: 24px;
  margin-right: 12px;
}

.vital-content {
  flex: 1;
}

.vital-label {
  color: #6c757d;
  font-size: 12px;
  margin-bottom: 4px;
}

.vital-value {
  color: #212529;
  font-size: 18px;
  font-weight: 600;
}

/* 文本内容 */
.text-content,
.description-content,
.remarks-content {
  line-height: 1.6;
  color: #495057;
  background-color: #f8f9fa;
  padding: 12px;
  border-radius: 4px;
  border-left: 4px solid #409EFF;
}

/* 对话框底部 */
.dialog-footer {
  text-align: right;
}

/* 响应式 */
@media (max-width: 768px) {
  .vital-signs-grid {
    grid-template-columns: 1fr;
  }
  
  .vital-item {
    padding: 12px;
  }
  
  .vital-icon {
    font-size: 20px;
    margin-right: 10px;
  }
  
  .vital-value {
    font-size: 16px;
  }
}

/* 打印样式 */
@media print {
  .dialog-footer {
    display: none;
  }
  
  .record-detail {
    max-height: none;
    overflow: visible;
  }
  
  .detail-section {
    page-break-inside: avoid;
  }
}
</style>
