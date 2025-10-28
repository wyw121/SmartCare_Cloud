<template>
  <div class="health-warning-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1><el-icon><Warning /></el-icon> 健康预警管理</h1>
      <el-button type="primary" @click="handleRefresh">
        <el-icon><Refresh /></el-icon> 刷新
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="stat in statistics" :key="stat.level">
        <el-card class="stat-card" :class="stat.class" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: stat.bgColor, color: stat.color }">
              <el-icon><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.count }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline>
        <el-form-item label="老人姓名">
          <el-input v-model="queryForm.elderlyName" placeholder="请输入老人姓名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="queryForm.warningType" placeholder="请选择预警类型" clearable style="width: 200px">
            <el-option label="全部" value="" />
            <el-option label="血压异常" value="血压异常" />
            <el-option label="心率异常" value="心率异常" />
            <el-option label="血糖异常" value="血糖异常" />
            <el-option label="体温异常" value="体温异常" />
            <el-option label="用药提醒" value="用药提醒" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别">
          <el-select v-model="queryForm.warningLevel" placeholder="请选择预警级别" clearable style="width: 200px">
            <el-option label="全部" value="" />
            <el-option label="紧急" :value="4" />
            <el-option label="高" :value="3" />
            <el-option label="中" :value="2" />
            <el-option label="低" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 200px">
            <el-option label="全部" value="" />
            <el-option label="待处理" :value="0" />
            <el-option label="处理中" :value="1" />
            <el-option label="已处理" :value="2" />
            <el-option label="已忽略" :value="3" />
          </el-select>
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

    <!-- 预警列表 -->
    <el-card class="table-card" shadow="never">
      <el-table 
        v-loading="loading" 
        :data="warningList" 
        stripe 
        border
        @row-click="handleRowClick"
        style="width: 100%">
        <el-table-column prop="id" label="预警ID" width="80" />
        <el-table-column prop="elderlyName" label="老人姓名" width="120" />
        <el-table-column prop="warningType" label="预警类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.warningType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningLevel" label="预警级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.warningLevel)">
              {{ getLevelText(row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningContent" label="预警内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="warningTime" label="预警时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click.stop="handleView(row)">
              <el-icon><View /></el-icon> 查看
            </el-button>
            <el-button 
              v-if="row.status === 0" 
              link 
              type="success" 
              size="small" 
              @click.stop="handleProcess(row)">
              <el-icon><CircleCheck /></el-icon> 处理
            </el-button>
            <el-button 
              v-if="row.status === 0" 
              link 
              type="warning" 
              size="small" 
              @click.stop="handleIgnore(row)">
              <el-icon><CircleClose /></el-icon> 忽略
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="预警详情" width="800px">
      <el-descriptions :column="2" border v-if="currentWarning">
        <el-descriptions-item label="预警ID">{{ currentWarning.id }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ currentWarning.elderlyName }}</el-descriptions-item>
        <el-descriptions-item label="预警类型">
          <el-tag>{{ currentWarning.warningType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警级别">
          <el-tag :type="getLevelType(currentWarning.warningLevel)">
            {{ getLevelText(currentWarning.warningLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警时间" :span="2">
          {{ currentWarning.warningTime }}
        </el-descriptions-item>
        <el-descriptions-item label="预警内容" :span="2">
          {{ currentWarning.warningContent }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentWarning.status)">
            {{ getStatusText(currentWarning.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人">
          {{ currentWarning.handlerName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="处理时间" :span="2">
          {{ currentWarning.handleTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2">
          {{ currentWarning.handleResult || '-' }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button 
          v-if="currentWarning && currentWarning.status === 0" 
          type="primary" 
          @click="handleProcessFromDetail">
          立即处理
        </el-button>
      </template>
    </el-dialog>

    <!-- 处理对话框 -->
    <el-dialog v-model="processVisible" title="处理预警" width="600px">
      <el-form :model="processForm" :rules="processRules" ref="processFormRef" label-width="100px">
        <el-form-item label="处理结果" prop="handleResult">
          <el-input 
            v-model="processForm.handleResult" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProcess" :loading="submitting">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Warning, Refresh, Search, RefreshLeft, View, 
  CircleCheck, CircleClose
} from '@element-plus/icons-vue'
import { getWarningList, getWarningDetail, handleWarning, ignoreWarning } from '@/api/healthWarning'

// 统计数据
const statistics = ref([
  { level: 'urgent', label: '紧急预警', count: 0, icon: 'WarningFilled', class: 'urgent', bgColor: 'rgba(245, 108, 108, 0.2)', color: '#F56C6C' },
  { level: 'high', label: '高级预警', count: 0, icon: 'Warning', class: 'high', bgColor: 'rgba(230, 162, 60, 0.2)', color: '#E6A23C' },
  { level: 'medium', label: '中级预警', count: 0, icon: 'InfoFilled', class: 'medium', bgColor: 'rgba(64, 158, 255, 0.2)', color: '#409EFF' },
  { level: 'low', label: '低级预警', count: 0, icon: 'SuccessFilled', class: 'low', bgColor: 'rgba(103, 194, 58, 0.2)', color: '#67C23A' }
])

// 查询表单
const queryForm = reactive({
  elderlyName: '',
  warningType: '',
  warningLevel: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 列表数据
const loading = ref(false)
const warningList = ref([])

// 详情对话框
const detailVisible = ref(false)
const currentWarning = ref(null)

// 处理对话框
const processVisible = ref(false)
const submitting = ref(false)
const processFormRef = ref(null)
const processForm = reactive({
  handleResult: ''
})
const processRules = {
  handleResult: [
    { required: true, message: '请输入处理结果', trigger: 'blur' }
  ]
}

// 获取预警列表
const fetchWarningList = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getWarningList(params)
    if (res.code === 200) {
      warningList.value = res.data.list || []
      pagination.total = res.data.total || 0
      updateStatistics()
    }
  } catch (error) {
    console.error('获取预警列表失败:', error)
    ElMessage.error('获取预警列表失败')
  } finally {
    loading.value = false
  }
}

// 更新统计数据
const updateStatistics = () => {
  const counts = { 4: 0, 3: 0, 2: 0, 1: 0 }
  for (const item of warningList.value) {
    if (counts.hasOwnProperty(item.warningLevel)) {
      counts[item.warningLevel]++
    }
  }
  statistics.value[0].count = counts[4] // 紧急
  statistics.value[1].count = counts[3] // 高
  statistics.value[2].count = counts[2] // 中
  statistics.value[3].count = counts[1] // 低
}

// 查询
const handleQuery = () => {
  pagination.pageNum = 1
  fetchWarningList()
}

// 重置
const handleReset = () => {
  queryForm.elderlyName = ''
  queryForm.warningType = ''
  queryForm.warningLevel = ''
  queryForm.status = ''
  handleQuery()
}

// 刷新
const handleRefresh = () => {
  fetchWarningList()
}

// 分页改变
const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchWarningList()
}

const handleCurrentChange = (page) => {
  pagination.pageNum = page
  fetchWarningList()
}

// 行点击
const handleRowClick = (row) => {
  handleView(row)
}

// 查看详情
const handleView = async (row) => {
  try {
    const res = await getWarningDetail(row.id)
    if (res.code === 200) {
      currentWarning.value = res.data
      detailVisible.value = true
    }
  } catch (error) {
    console.error('获取预警详情失败:', error)
    ElMessage.error('获取预警详情失败')
  }
}

// 处理预警
const handleProcess = (row) => {
  currentWarning.value = row
  processForm.handleResult = ''
  processVisible.value = true
}

// 从详情页处理
const handleProcessFromDetail = () => {
  detailVisible.value = false
  processForm.handleResult = ''
  processVisible.value = true
}

// 提交处理
const submitProcess = async () => {
  if (!processFormRef.value) return
  
  await processFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const res = await handleWarning(currentWarning.value.id, {
          handleResult: processForm.handleResult
        })
        if (res.code === 200) {
          ElMessage.success('处理成功')
          processVisible.value = false
          fetchWarningList()
        }
      } catch (error) {
        console.error('处理预警失败:', error)
        ElMessage.error('处理预警失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

// 忽略预警
const handleIgnore = async (row) => {
  try {
    await ElMessageBox.confirm('确定要忽略此预警吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await ignoreWarning(row.id)
    if (res.code === 200) {
      ElMessage.success('已忽略')
      fetchWarningList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('忽略预警失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 获取级别类型
const getLevelType = (level) => {
  const typeMap = {
    4: 'danger',
    3: 'warning',
    2: 'primary',
    1: 'success'
  }
  return typeMap[level] || 'info'
}

// 获取级别文本
const getLevelText = (level) => {
  const textMap = {
    4: '紧急',
    3: '高',
    2: '中',
    1: '低'
  }
  return textMap[level] || '未知'
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'info'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    0: '待处理',
    1: '处理中',
    2: '已处理',
    3: '已忽略'
  }
  return textMap[status] || '未知'
}

onMounted(() => {
  fetchWarningList()
})
</script>

<style scoped lang="scss">
.health-warning-container {
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
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-4px);
      }

      .stat-content {
        display: flex;
        align-items: center;
        gap: 16px;

        .stat-icon {
          width: 60px;
          height: 60px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 28px;
        }

        .stat-info {
          flex: 1;

          .stat-value {
            font-size: 28px;
            font-weight: bold;
            line-height: 1;
            margin-bottom: 8px;
          }

          .stat-label {
            font-size: 14px;
            color: #909399;
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
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
}
</style>
