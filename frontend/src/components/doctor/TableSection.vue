<template>
  <div class="doctor-table-section">
    <!-- 操作工具栏 -->
    <el-card class="operation-card">
      <div class="operation-toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="handleAdd" :icon="Plus">新增医生</el-button>
          <el-button 
            type="danger" 
            @click="handleBatchDelete" 
            :disabled="!selectedDoctors.length"
            :icon="Delete"
          >
            批量删除
          </el-button>
          <el-button type="success" @click="handleExport" :icon="Download">导出数据</el-button>
        </div>
        <div class="toolbar-right">
          <el-button @click="handleRefresh" :icon="Refresh">刷新</el-button>
        </div>
      </div>
    </el-card>

    <!-- 医生表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="doctorList"
        @selection-change="handleSelectionChange"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="employeeNumber" label="工号" min-width="120" />
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="phone" label="联系电话" min-width="120" />
        <el-table-column prop="department" label="科室" min-width="100" />
        <el-table-column prop="title" label="职称" min-width="120" />
        <el-table-column prop="specialization" label="专长" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)" :icon="View">
              查看
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(row)" :icon="Edit">
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(row)" 
              :icon="Delete"
            >
              删除
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
import { Delete, Download, Edit, Plus, Refresh, View } from '@element-plus/icons-vue'
import { ref } from 'vue'

const props = defineProps({
  doctorList: {
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
  'add', 'edit', 'delete', 'view', 'batch-delete', 'export', 'refresh',
  'page-change', 'page-size-change'
])

const selectedDoctors = ref([])

const handleSelectionChange = (selection) => {
  selectedDoctors.value = selection
}

const handleAdd = () => {
  emit('add')
}

const handleEdit = (row) => {
  emit('edit', row)
}

const handleDelete = (row) => {
  emit('delete', row)
}

const handleView = (row) => {
  emit('view', row)
}

const handleBatchDelete = () => {
  emit('batch-delete', selectedDoctors.value)
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
</script>

<style scoped>
.doctor-table-section {
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
