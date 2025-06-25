<template>
  <div class="warning-table-section">
    <!-- 操作工具栏 -->
    <el-card class="operation-card">
      <div class="operation-toolbar">
        <div class="toolbar-left">
          <el-button 
            type="success" 
            @click="handleBatchHandle" 
            :disabled="!selectedWarnings.length"
          >
            批量处理
          </el-button>
          <el-button 
            type="warning" 
            @click="handleBatchIgnore" 
            :disabled="!selectedWarnings.length"
          >
            批量忽略
          </el-button>
          <el-button 
            type="info" 
            @click="handleExport"
          >
            导出数据
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button @click="handleRefresh" :icon="Refresh">刷新</el-button>
        </div>
      </div>
    </el-card>

    <!-- 预警列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="warningList"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column prop="elderlyName" label="老人姓名" width="100" />
        
        <el-table-column prop="warningType" label="预警类型" width="120" />
        
        <el-table-column prop="warningLevel" label="级别" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="getWarningLevelType(row.warningLevel)" size="small">
              {{ getWarningLevelText(row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="warningContent" label="预警内容" min-width="200" />
        
        <el-table-column prop="triggerTime" label="触发时间" width="160" />
        
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="handlePerson" label="处理人" width="100" />
        
        <el-table-column prop="handleTime" label="处理时间" width="160" />
        
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button 
              size="small" 
              type="primary" 
              @click="handleView(row)"
            >
              查看
            </el-button>
            <el-button 
              v-if="row.status === 0 || row.status === 1" 
              size="small" 
              type="success" 
              @click="handleProcess(row)"
            >
              处理
            </el-button>
            <el-button 
              v-if="row.status === 0" 
              size="small" 
              type="warning" 
              @click="handleIgnore(row)"
            >
              忽略
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlePageSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Refresh } from '@element-plus/icons-vue'
import { ref } from 'vue'

const props = defineProps({
  warningList: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  pagination: {
    type: Object,
    default: () => ({
      current: 1,
      size: 10,
      total: 0
    })
  }
})

const emit = defineEmits([
  'view', 'process', 'ignore', 'batch-handle', 'batch-ignore', 
  'export', 'refresh', 'page-change', 'page-size-change'
])

const selectedWarnings = ref([])

const handleSelectionChange = (selection) => {
  selectedWarnings.value = selection
}

const handleView = (row) => {
  emit('view', row)
}

const handleProcess = (row) => {
  emit('process', row)
}

const handleIgnore = (row) => {
  emit('ignore', row)
}

const handleBatchHandle = () => {
  emit('batch-handle', selectedWarnings.value)
}

const handleBatchIgnore = () => {
  emit('batch-ignore', selectedWarnings.value)
}

const handleExport = () => {
  emit('export')
}

const handleRefresh = () => {
  emit('refresh')
}

const handlePageChange = (page) => {
  emit('page-change', page)
}

const handlePageSizeChange = (size) => {
  emit('page-size-change', size)
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
.warning-table-section {
  margin-bottom: 20px;
}

.operation-card {
  margin-bottom: 20px;
}

.operation-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.toolbar-left {
  display: flex;
  gap: 10px;
}

.toolbar-right {
  display: flex;
  gap: 10px;
}

.table-card {
  min-height: 400px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
