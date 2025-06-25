<template>
  <el-dialog
    v-model="visible"
    title="预警详情"
    width="600px"
    @close="handleClose"
  >
    <div class="warning-detail" v-if="warningData">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="老人姓名">
          {{ warningData.elderlyName }}
        </el-descriptions-item>
        <el-descriptions-item label="年龄">
          {{ warningData.elderlyAge }}岁
        </el-descriptions-item>
        <el-descriptions-item label="联系电话">
          {{ warningData.elderlyPhone }}
        </el-descriptions-item>
        <el-descriptions-item label="预警类型">
          {{ warningData.warningType }}
        </el-descriptions-item>
        <el-descriptions-item label="预警级别">
          <el-tag :type="getWarningLevelType(warningData.warningLevel)">
            {{ getWarningLevelText(warningData.warningLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="触发时间">
          {{ warningData.triggerTime }}
        </el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="getStatusType(warningData.status)">
            {{ getStatusText(warningData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人" v-if="warningData.handlePerson">
          {{ warningData.handlePerson }}
        </el-descriptions-item>
        <el-descriptions-item label="处理时间" v-if="warningData.handleTime">
          {{ warningData.handleTime }}
        </el-descriptions-item>
      </el-descriptions>
      
      <div class="warning-content-section">
        <h4>预警内容</h4>
        <div class="warning-content">
          {{ warningData.warningContent }}
        </div>
      </div>
      
      <div class="handle-result-section" v-if="warningData.handleResult">
        <h4>处理结果</h4>
        <div class="handle-result">
          {{ warningData.handleResult }}
        </div>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button 
          v-if="warningData.status === 0 || warningData.status === 1"
          type="primary" 
          @click="handleProcess"
        >
          立即处理
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  warningData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:visible', 'process'])

const handleClose = () => {
  emit('update:visible', false)
}

const handleProcess = () => {
  emit('process', props.warningData)
  handleClose()
}

// 获取预警级别类型
const getWarningLevelType = (level) => {
  const typeMap = {
    1: 'success',
    2: 'warning',
    3: 'danger',
    4: 'danger'
  }
  return typeMap[level] || 'info'
}

// 获取预警级别文本
const getWarningLevelText = (level) => {
  const textMap = {
    1: '低风险',
    2: '中风险', 
    3: '高风险',
    4: '紧急'
  }
  return textMap[level] || '未知'
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    0: 'danger',
    1: 'warning',
    2: 'primary',
    3: 'success',
    4: 'info'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    0: '未处理',
    1: '已查看',
    2: '处理中',
    3: '已处理',
    4: '已忽略'
  }
  return textMap[status] || '未知'
}
</script>

<style scoped>
.warning-detail {
  padding: 20px 0;
}

.warning-content-section,
.handle-result-section {
  margin-top: 20px;
}

.warning-content-section h4,
.handle-result-section h4 {
  margin-bottom: 10px;
  color: #333;
  font-weight: bold;
}

.warning-content,
.handle-result {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 6px;
  line-height: 1.6;
  color: #606266;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
