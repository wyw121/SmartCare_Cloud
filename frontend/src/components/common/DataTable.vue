<!-- 通用数据表格组件 -->
<template>
  <div class="data-table">
    <!-- 工具栏 -->
    <div class="table-toolbar" v-if="showToolbar">
      <div class="toolbar-left">
        <slot name="toolbar-left">
          <el-button 
            v-if="showAdd" 
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            {{ addText }}
          </el-button>
          <el-button 
            v-if="showBatchDelete" 
            type="danger" 
            :disabled="selectedRows.length === 0" 
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </slot>
      </div>
      <div class="toolbar-right">
        <slot name="toolbar-right">
          <el-button @click="refreshData">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </slot>
      </div>
    </div>

    <!-- 表格 -->
    <el-table
      :data="tableData"
      v-loading="loading"
      :stripe="stripe"
      :border="border"
      :size="size"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
      @row-click="handleRowClick"
      style="width: 100%"
    >
      <el-table-column 
        v-if="showSelection" 
        type="selection" 
        width="55" 
      />
      <el-table-column 
        v-if="showIndex" 
        type="index" 
        label="序号" 
        width="80" 
      />
      
      <!-- 动态列 -->
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :min-width="column.minWidth"
        :sortable="column.sortable"
        :show-overflow-tooltip="column.showOverflowTooltip !== false"
      >
        <template #default="scope">
          <slot 
            :name="column.prop" 
            :row="scope.row" 
            :column="column" 
            :index="scope.$index"
          >
            {{ formatCellValue(scope.row[column.prop], column) }}
          </slot>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column 
        v-if="showActions" 
        label="操作" 
        :width="actionWidth"
        fixed="right"
      >
        <template #default="scope">
          <slot name="actions" :row="scope.row" :index="scope.$index">
            <el-button 
              v-if="showEdit" 
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="showDelete" 
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </slot>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="table-pagination" v-if="showPagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="pageSizes"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { Delete, Plus, Refresh } from '@element-plus/icons-vue'
import { computed, ref } from 'vue'

// Props
const props = defineProps({
  // 表格数据
  data: { type: Array, default: () => [] },
  columns: { type: Array, required: true },
  loading: { type: Boolean, default: false },
  
  // 表格配置
  stripe: { type: Boolean, default: true },
  border: { type: Boolean, default: true },
  size: { type: String, default: 'default' },
  
  // 功能开关
  showToolbar: { type: Boolean, default: true },
  showSelection: { type: Boolean, default: true },
  showIndex: { type: Boolean, default: true },
  showActions: { type: Boolean, default: true },
  showPagination: { type: Boolean, default: true },
  
  // 操作按钮
  showAdd: { type: Boolean, default: true },
  showEdit: { type: Boolean, default: true },
  showDelete: { type: Boolean, default: true },
  showBatchDelete: { type: Boolean, default: true },
  
  // 文本配置
  addText: { type: String, default: '新增' },
  actionWidth: { type: String, default: '180' },
  
  // 分页配置
  currentPage: { type: Number, default: 1 },
  pageSize: { type: Number, default: 10 },
  total: { type: Number, default: 0 },
  pageSizes: { type: Array, default: () => [10, 20, 50, 100] }
})

// Emits
const emit = defineEmits([
  'add', 'edit', 'delete', 'batch-delete', 'refresh',
  'selection-change', 'sort-change', 'row-click',
  'page-change', 'size-change'
])

// 响应式数据
const selectedRows = ref([])
const tableData = computed(() => props.data)

// 选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
  emit('selection-change', selection)
}

// 排序变化
const handleSortChange = (sortInfo) => {
  emit('sort-change', sortInfo)
}

// 行点击
const handleRowClick = (row) => {
  emit('row-click', row)
}

// 操作方法
const handleAdd = () => emit('add')
const handleEdit = (row) => emit('edit', row)
const handleDelete = (row) => emit('delete', row)
const handleBatchDelete = () => emit('batch-delete', selectedRows.value)
const refreshData = () => emit('refresh')

// 分页方法
const handleCurrentChange = (page) => {
  emit('page-change', page)
}

const handleSizeChange = (size) => {
  emit('size-change', size)
}

// 格式化单元格值
const formatCellValue = (value, column) => {
  if (column.formatter && typeof column.formatter === 'function') {
    return column.formatter(value)
  }
  return value
}
</script>

<style scoped>
.data-table {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
}

.toolbar-left, .toolbar-right {
  display: flex;
  gap: 8px;
}

.table-pagination {
  padding: 16px;
  text-align: right;
  border-top: 1px solid #ebeef5;
}
</style>
