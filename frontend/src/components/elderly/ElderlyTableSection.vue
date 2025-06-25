<!-- 老人档案表格区域 -->
<template>
  <DataTable
    :data="tableData"
    :columns="tableColumns"
    :loading="loading"
    :current-page="pagination.current"
    :page-size="pagination.size"
    :total="pagination.total"
    add-text="新增老人档案"
    @add="handleAdd"
    @edit="handleEdit"
    @delete="handleDelete"
    @batch-delete="handleBatchDelete"
    @refresh="handleRefresh"
    @page-change="handlePageChange"
    @size-change="handleSizeChange"
    @selection-change="handleSelectionChange"
    @row-click="handleRowClick"
  >
    <!-- 健康状态列 -->
    <template #healthStatus="{ row }">
      <el-tag :type="getHealthStatusType(row.healthStatus)">
        {{ getHealthStatusText(row.healthStatus) }}
      </el-tag>
    </template>

    <!-- 年龄列 -->
    <template #age="{ row }">
      {{ calculateAge(row.birthDate) }}岁
    </template>

    <!-- 性别列 -->
    <template #gender="{ row }">
      <el-icon v-if="row.gender === 'MALE'" color="#409EFF">
        <Male />
      </el-icon>
      <el-icon v-else-if="row.gender === 'FEMALE'" color="#F56C6C">
        <Female />
      </el-icon>
      {{ row.gender === 'MALE' ? '男' : '女' }}
    </template>

    <!-- 操作列 -->
    <template #actions="{ row }">
      <el-button type="primary" size="small" @click="handleView(row)">
        查看
      </el-button>
      <el-button type="success" size="small" @click="handleEdit(row)">
        编辑
      </el-button>
      <el-button type="warning" size="small" @click="handleHealthRecord(row)">
        健康记录
      </el-button>
      <el-dropdown @command="(command) => handleMoreAction(command, row)">
        <el-button type="info" size="small">
          更多<el-icon><ArrowDown /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="family">家属管理</el-dropdown-item>
            <el-dropdown-item command="device">设备绑定</el-dropdown-item>
            <el-dropdown-item command="care">护理计划</el-dropdown-item>
            <el-dropdown-item command="delete" divided>删除档案</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </template>
  </DataTable>
</template>

<script setup>
import DataTable from '@/components/common/DataTable.vue'
import { ArrowDown, Female, Male } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, ref } from 'vue'

// Props
const props = defineProps({
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  pagination: { 
    type: Object, 
    default: () => ({ current: 1, size: 10, total: 0 }) 
  }
})

// Emits
const emit = defineEmits([
  'add', 'edit', 'delete', 'batch-delete', 'refresh',
  'view', 'health-record', 'more-action',
  'page-change', 'size-change', 'selection-change', 'row-click'
])

// 表格数据
const tableData = computed(() => props.data)

// 表格列配置
const tableColumns = ref([
  {
    prop: 'name',
    label: '姓名',
    width: '120',
    sortable: true
  },
  {
    prop: 'gender',
    label: '性别',
    width: '80'
  },
  {
    prop: 'age',
    label: '年龄',
    width: '80',
    sortable: true
  },
  {
    prop: 'phone',
    label: '联系电话',
    width: '130'
  },
  {
    prop: 'healthStatus',
    label: '健康状态',
    width: '120'
  },
  {
    prop: 'emergencyContact',
    label: '紧急联系人',
    width: '120'
  },
  {
    prop: 'emergencyPhone',
    label: '紧急联系电话',
    width: '130'
  },
  {
    prop: 'address',
    label: '居住地址',
    minWidth: '200',
    showOverflowTooltip: true
  },
  {
    prop: 'createdTime',
    label: '创建时间',
    width: '160',
    sortable: true,
    formatter: (value) => value ? new Date(value).toLocaleString() : '-'
  }
])

// 获取健康状态类型
const getHealthStatusType = (status) => {
  const typeMap = {
    'HEALTHY': 'success',
    'SUBHEALTH': 'info',
    'MILD': 'warning',
    'ATTENTION': 'warning',
    'CRITICAL': 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取健康状态文本
const getHealthStatusText = (status) => {
  const textMap = {
    'HEALTHY': '健康',
    'SUBHEALTH': '亚健康',
    'MILD': '轻微异常',
    'ATTENTION': '需要关注',
    'CRITICAL': '重点监护'
  }
  return textMap[status] || '未知'
}

// 计算年龄
const calculateAge = (birthDate) => {
  if (!birthDate) return 0
  const birth = new Date(birthDate)
  const now = new Date()
  const age = now.getFullYear() - birth.getFullYear()
  const monthDiff = now.getMonth() - birth.getMonth()
  
  if (monthDiff < 0 || (monthDiff === 0 && now.getDate() < birth.getDate())) {
    return age - 1
  }
  return age
}

// 事件处理
const handleAdd = () => emit('add')
const handleEdit = (row) => emit('edit', row)
const handleView = (row) => emit('view', row)
const handleHealthRecord = (row) => emit('health-record', row)
const handleRefresh = () => emit('refresh')

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除老人档案"${row.name}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    emit('delete', row)
  } catch {
    ElMessage.info('已取消删除')
  }
}

const handleBatchDelete = async (rows) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的${rows.length}条老人档案吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    emit('batch-delete', rows)
  } catch {
    ElMessage.info('已取消删除')
  }
}

const handleMoreAction = (command, row) => {
  emit('more-action', { command, row })
}

// 分页事件
const handlePageChange = (page) => emit('page-change', page)
const handleSizeChange = (size) => emit('size-change', size)
const handleSelectionChange = (selection) => emit('selection-change', selection)
const handleRowClick = (row) => emit('row-click', row)
</script>

<style scoped>
/* 自定义样式 */
</style>
