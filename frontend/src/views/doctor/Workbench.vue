<template>
  <div class="doctor-workbench">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>医生工作台</h2>
      <p class="subtitle">管理您负责的老人和待处理事项</p>
    </div>

    <!-- 数据统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#409EFF"><User /></el-icon>
            <div class="stat-info">
              <p class="stat-label">负责老人</p>
              <p class="stat-value">{{ stats.totalPatients }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#F56C6C"><Warning /></el-icon>
            <div class="stat-info">
              <p class="stat-label">待处理预警</p>
              <p class="stat-value">{{ stats.pendingWarnings }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#67C23A"><Document /></el-icon>
            <div class="stat-info">
              <p class="stat-label">今日待办</p>
              <p class="stat-value">{{ stats.todayTasks }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#E6A23C"><Calendar /></el-icon>
            <div class="stat-info">
              <p class="stat-label">本周巡诊</p>
              <p class="stat-value">{{ stats.weeklyVisits }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <div class="quick-actions">
      <h3 class="section-title">快捷操作</h3>
      <el-row :gutter="15">
        <el-col :span="6">
          <el-button type="primary" @click="handleQuickAction('patrol')" class="action-btn">
            <el-icon><Document /></el-icon>
            <span>记录巡诊</span>
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="success" @click="handleQuickAction('medication')" class="action-btn">
            <el-icon><Medicine /></el-icon>
            <span>开具处方</span>
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="warning" @click="handleQuickAction('warning')" class="action-btn">
            <el-icon><Warning /></el-icon>
            <span>查看预警</span>
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="info" @click="handleQuickAction('assessment')" class="action-btn">
            <el-icon><DataAnalysis /></el-icon>
            <span>健康评估</span>
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 我负责的老人列表 -->
    <div class="patient-list">
      <div class="section-header">
        <h3 class="section-title">我负责的老人</h3>
        <el-button type="primary" size="small" @click="refreshPatients">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>

      <!-- 搜索和筛选 -->
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索老人姓名或编号"
          style="width: 300px"
          clearable
          @clear="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="filterStatus"
          placeholder="健康状态"
          style="width: 150px; margin-left: 10px"
          clearable
          @change="handleSearch"
        >
          <el-option label="良好" value="good" />
          <el-option label="一般" value="fair" />
          <el-option label="较差" value="poor" />
          <el-option label="危重" value="critical" />
        </el-select>
        <el-button type="primary" @click="handleSearch" style="margin-left: 10px">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
      </div>

      <!-- 老人表格 -->
      <el-table
        v-loading="loading"
        :data="patients"
        stripe
        border
        style="width: 100%; margin-top: 15px"
        :default-sort="{ prop: 'lastVisit', order: 'descending' }"
      >
        <el-table-column prop="elderlyCode" label="编号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="age" label="年龄" width="80" align="center" />
        <el-table-column prop="gender" label="性别" width="80" align="center">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="relationshipType" label="关系类型" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.relationshipType === 'primary'" type="danger">主治医生</el-tag>
            <el-tag v-else-if="row.relationshipType === 'assistant'" type="warning">协助医生</el-tag>
            <el-tag v-else type="info">会诊医生</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="healthStatus" label="健康状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="getHealthStatusType(row.healthStatus)"
              effect="dark"
            >
              {{ getHealthStatusText(row.healthStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="careLevel" label="照护等级" width="100" align="center">
          <template #default="{ row }">
            <el-tag type="warning">{{ getCareLevelText(row.careLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pendingWarnings" label="待处理预警" width="120" align="center">
          <template #default="{ row }">
            <el-badge v-if="row.pendingWarnings > 0" :value="row.pendingWarnings" type="danger">
              <el-button size="small" type="danger" plain>查看</el-button>
            </el-badge>
            <span v-else class="text-muted">无</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastVisit" label="最后巡诊" width="120" sortable />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleViewDetail(row)">
              <el-icon><View /></el-icon>
              查看详情
            </el-button>
            <el-button size="small" type="success" @click="handleRecordVisit(row)">
              <el-icon><Document /></el-icon>
              记录巡诊
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </div>
  </div>
</template>

<script setup>
import { getMyPatients } from '@/api/elderly'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 统计数据
const stats = reactive({
  totalPatients: 0,
  pendingWarnings: 0,
  todayTasks: 0,
  weeklyVisits: 0
})

// 搜索和筛选
const searchKeyword = ref('')
const filterStatus = ref('')

// 加载状态
const loading = ref(false)

// 老人列表数据
const patients = ref([])

// 分页
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

/**
 * 获取我负责的老人列表
 */
const fetchMyPatients = async () => {
  loading.value = true
  try {
    // 调用后端API获取我负责的老人
    const response = await getMyPatients(1, 100)
    
    if (response.code === 200 && response.data) {
      patients.value = response.data.records || []
      
      // 更新统计数据
      stats.value.totalPatients = patients.value.length
      stats.value.pendingWarnings = patients.value.reduce((sum, p) => sum + (p.pendingWarnings || 0), 0)
    } else {
      ElMessage.warning('获取患者列表失败,使用模拟数据')
      loadMockPatients()
    }
  } catch (error) {
    console.error('获取患者列表失败:', error)
    ElMessage.warning('网络连接失败,显示模拟数据')
    loadMockPatients()
  } finally {
    loading.value = false
  }
}

/**
 * 加载模拟患者数据(用于演示)
 */
const loadMockPatients = () => {
  patients.value = [
    {
      id: 1,
      elderlyCode: 'E202401001',
      name: '张三',
      age: 78,
      gender: 1,
      relationshipType: 'primary',
      healthStatus: 'good',
      careLevel: 2,
      pendingWarnings: 0,
      lastVisit: '2024-10-25'
    },
    {
      id: 2,
      elderlyCode: 'E202401002',
      name: '李四',
      age: 82,
      gender: 0,
      relationshipType: 'primary',
      healthStatus: 'fair',
      careLevel: 3,
      pendingWarnings: 2,
      lastVisit: '2024-10-24'
    },
    {
      id: 3,
      elderlyCode: 'E202401003',
      name: '王五',
      age: 75,
      gender: 1,
      relationshipType: 'assistant',
      healthStatus: 'poor',
      careLevel: 3,
      pendingWarnings: 1,
      lastVisit: '2024-10-26'
    }
  ]

  pagination.total = 3

  // 更新统计数据
  stats.value.totalPatients = patients.value.length
  stats.value.pendingWarnings = patients.value.reduce((sum, p) => sum + p.pendingWarnings, 0)
  stats.value.todayTasks = 5 // 模拟数据
  stats.value.weeklyVisits = 12 // 模拟数据
}

/**
 * 刷新老人列表
 */
const refreshPatients = () => {
  fetchMyPatients()
  ElMessage.success('刷新成功')
}

/**
 * 搜索处理
 */
const handleSearch = () => {
  console.log('搜索:', searchKeyword.value, '状态:', filterStatus.value)
  fetchMyPatients()
}

/**
 * 分页大小改变
 */
const handleSizeChange = (size) => {
  pagination.size = size
  fetchMyPatients()
}

/**
 * 页码改变
 */
const handlePageChange = (page) => {
  pagination.current = page
  fetchMyPatients()
}

/**
 * 快捷操作处理
 */
const handleQuickAction = (action) => {
  switch (action) {
    case 'patrol':
      router.push('/patrol/record')
      break
    case 'medication':
      router.push('/medication/record')
      break
    case 'warning':
      router.push('/health/warning')
      break
    case 'assessment':
      router.push('/health/assessment')
      break
  }
}

/**
 * 查看老人详情
 */
const handleViewDetail = (row) => {
  router.push(`/elderly/detail/${row.id}`)
}

/**
 * 记录巡诊
 */
const handleRecordVisit = (row) => {
  router.push({
    path: '/patrol/record',
    query: { elderlyId: row.id, elderlyName: row.name }
  })
}

/**
 * 获取健康状态类型
 */
const getHealthStatusType = (status) => {
  const typeMap = {
    good: 'success',
    fair: 'warning',
    poor: 'danger',
    critical: 'danger'
  }
  return typeMap[status] || 'info'
}

/**
 * 获取健康状态文本
 */
const getHealthStatusText = (status) => {
  const textMap = {
    good: '良好',
    fair: '一般',
    poor: '较差',
    critical: '危重'
  }
  return textMap[status] || '未知'
}

/**
 * 获取照护等级文本
 */
const getCareLevelText = (level) => {
  const textMap = {
    1: '一级',
    2: '二级',
    3: '三级',
    4: '四级'
  }
  return textMap[level] || '未定级'
}

// 页面加载时获取数据
onMounted(() => {
  fetchMyPatients()
})
</script>

<style lang="scss" scoped>
.doctor-workbench {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
    }

    .subtitle {
      font-size: 14px;
      color: #909399;
      margin: 0;
    }
  }

  // 统计卡片
  .stats-cards {
    margin-bottom: 25px;

    .stat-card {
      cursor: pointer;
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .stat-content {
        display: flex;
        align-items: center;
        gap: 15px;

        .stat-icon {
          font-size: 40px;
        }

        .stat-info {
          .stat-label {
            font-size: 14px;
            color: #909399;
            margin: 0 0 5px 0;
          }

          .stat-value {
            font-size: 28px;
            font-weight: 600;
            color: #303133;
            margin: 0;
          }
        }
      }
    }
  }

  // 快捷操作
  .quick-actions {
    margin-bottom: 25px;

    .section-title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 15px 0;
    }

    .action-btn {
      width: 100%;
      height: 60px;
      font-size: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;

      .el-icon {
        font-size: 20px;
      }
    }
  }

  // 老人列表
  .patient-list {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .section-title {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0;
      }
    }

    .filter-bar {
      display: flex;
      align-items: center;
      margin-bottom: 15px;
    }

    .text-muted {
      color: #909399;
    }
  }
}
</style>
