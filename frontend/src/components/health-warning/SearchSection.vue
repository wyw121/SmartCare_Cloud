<template>
  <div class="warning-search-section">
    <el-card class="search-card">
      <el-form :model="searchForm" ref="searchFormRef" :inline="true" label-width="80px">
        <el-form-item label="老人姓名" prop="elderlyName">
          <el-input 
            v-model="searchForm.elderlyName" 
            placeholder="请输入老人姓名" 
            clearable 
            style="width: 150px;"
          />
        </el-form-item>
        
        <el-form-item label="预警类型" prop="warningType">
          <el-select v-model="searchForm.warningType" placeholder="请选择预警类型" clearable style="width: 150px;">
            <el-option label="血压异常" value="血压异常" />
            <el-option label="血糖异常" value="血糖异常" />
            <el-option label="心率异常" value="心率异常" />
            <el-option label="体温异常" value="体温异常" />
            <el-option label="长期不活动" value="长期不活动" />
            <el-option label="药物未按时服用" value="药物未按时服用" />
            <el-option label="跌倒风险" value="跌倒风险" />
            <el-option label="其他异常" value="其他异常" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="预警级别" prop="warningLevel">
          <el-select v-model="searchForm.warningLevel" placeholder="请选择预警级别" clearable style="width: 120px;">
            <el-option label="低风险" :value="1" />
            <el-option label="中风险" :value="2" />
            <el-option label="高风险" :value="3" />
            <el-option label="紧急" :value="4" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="处理状态" prop="status">
          <el-select v-model="searchForm.status" placeholder="请选择处理状态" clearable style="width: 120px;">
            <el-option label="未处理" :value="0" />
            <el-option label="已查看" :value="1" />
            <el-option label="处理中" :value="2" />
            <el-option label="已处理" :value="3" />
            <el-option label="已忽略" :value="4" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="触发时间" prop="triggerTime">
          <el-date-picker
            v-model="searchForm.triggerTime"
            type="datetimerange"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 350px;"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { Refresh, Search } from '@element-plus/icons-vue'
import { reactive, ref } from 'vue'

const emit = defineEmits(['search', 'reset'])

const searchFormRef = ref()
const searchForm = reactive({
  elderlyName: '',
  warningType: '',
  warningLevel: null,
  status: null,
  triggerTime: []
})

const handleSearch = () => {
  emit('search', { ...searchForm })
}

const handleReset = () => {
  if (searchFormRef.value) {
    searchFormRef.value.resetFields()
  }
  Object.assign(searchForm, {
    elderlyName: '',
    warningType: '',
    warningLevel: null,
    status: null,
    triggerTime: []
  })
  emit('reset')
}
</script>

<style scoped>
.warning-search-section {
  margin-bottom: 20px;
}

.search-card {
  padding: 10px;
}

.search-card :deep(.el-form-item) {
  margin-bottom: 10px;
}
</style>
