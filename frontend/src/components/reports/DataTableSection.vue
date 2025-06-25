<template>
  <div class="data-table-section">
    <div class="table-toolbar">
      <div class="filter-section">
        <el-date-picker
          v-model="localDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleDateChange"
        />
        <el-select v-model="localAnalysisType" placeholder="分析类型" style="margin-left: 10px;">
          <el-option label="健康指标" value="health" />
          <el-option label="预警分析" value="warning" />
          <el-option label="满意度" value="satisfaction" />
          <el-option label="服务质量" value="quality" />
        </el-select>
        <el-button type="primary" @click="handleAnalyze" style="margin-left: 10px;">
          开始分析
        </el-button>
      </div>
      
      <div class="export-section">
        <el-button @click="handleExport('excel')">
          <el-icon><Document /></el-icon>
          导出Excel
        </el-button>
        <el-button @click="handleExport('pdf')">
          <el-icon><Printer /></el-icon>
          导出PDF
        </el-button>
      </div>
    </div>

    <el-table
      v-loading="loading"
      :data="tableData"
      stripe
      border
      style="width: 100%; margin-top: 20px;"
    >
      <el-table-column prop="indicator" label="指标名称" width="150" />
      <el-table-column prop="value" label="数值" width="100" align="center" />
      <el-table-column prop="unit" label="单位" width="80" align="center" />
      <el-table-column prop="trend" label="趋势" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getTrendType(row.trend)">
            {{ getTrendText(row.trend) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="comparison" label="同比" width="100" align="center">
        <template #default="{ row }">
          <span :class="getComparisonClass(row.comparison)">
            {{ row.comparison }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="说明" min-width="200" />
      <el-table-column prop="updateTime" label="更新时间" width="160" />
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
  </div>
</template>

<script setup>
import { Document, Printer } from '@element-plus/icons-vue'
import { ref } from 'vue'

const props = defineProps({
  tableData: {
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
  },
  dateRange: {
    type: Array,
    default: () => []
  },
  analysisType: {
    type: String,
    default: 'health'
  }
})

const emit = defineEmits(['date-change', 'analyze', 'export', 'page-change', 'page-size-change'])

const localDateRange = ref(props.dateRange)
const localAnalysisType = ref(props.analysisType)

const handleDateChange = (value) => {
  emit('date-change', value)
}

const handleAnalyze = () => {
  emit('analyze', {
    dateRange: localDateRange.value,
    analysisType: localAnalysisType.value
  })
}

const handleExport = (type) => {
  emit('export', type)
}

const handlePageChange = (page) => {
  emit('page-change', page)
}

const handlePageSizeChange = (size) => {
  emit('page-size-change', size)
}

const getTrendType = (trend) => {
  const typeMap = {
    'up': 'success',
    'down': 'danger',
    'stable': 'info'
  }
  return typeMap[trend] || 'info'
}

const getTrendText = (trend) => {
  const textMap = {
    'up': '上升',
    'down': '下降',
    'stable': '稳定'
  }
  return textMap[trend] || '未知'
}

const getComparisonClass = (comparison) => {
  if (comparison.includes('+')) {
    return 'comparison-positive'
  } else if (comparison.includes('-')) {
    return 'comparison-negative'
  }
  return 'comparison-neutral'
}
</script>

<style scoped>
.data-table-section {
  margin-bottom: 30px;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-section {
  display: flex;
  align-items: center;
}

.export-section {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.comparison-positive {
  color: #67c23a;
  font-weight: bold;
}

.comparison-negative {
  color: #f56c6c;
  font-weight: bold;
}

.comparison-neutral {
  color: #909399;
}
</style>
