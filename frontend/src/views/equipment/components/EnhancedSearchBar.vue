<template>
  <div class="enhanced-search-bar">
    <el-form inline class="search-form">
      <!-- 设备类型 - 多选支持 -->
      <el-form-item label="设备类型">
        <el-select 
          v-model="searchForm.deviceTypes" 
          :multiple="isMultipleMode"
          :placeholder="isMultipleMode ? '可多选设备类型' : '请选择设备类型'"
          :collapse-tags="isMultipleMode"
          :collapse-tags-tooltip="isMultipleMode"
          :max-collapse-tags="2"
          clearable 
          :class="isMultipleMode ? 'search-select-multiple' : 'search-select'"
          @change="handleSearch"
        >
          <el-option label="智能血压计" value="BLOOD_PRESSURE" />
          <el-option label="血糖仪" value="BLOOD_GLUCOSE" />
          <el-option label="智能手环" value="SMART_WATCH" />
          <el-option label="体温计" value="THERMOMETER" />
          <el-option label="心电监护仪" value="ECG_MONITOR" />
          <el-option label="智能体重秤" value="WEIGHT_SCALE" />
          <el-option label="定位器" value="LOCATOR" />
          <el-option label="紧急呼叫器" value="EMERGENCY_BUTTON" />
          <el-option label="环境监测器" value="ENVIRONMENT_SENSOR" />
        </el-select>
      </el-form-item>

      <!-- 设备状态 - 多选支持 -->
      <el-form-item label="设备状态">
        <el-select 
          v-model="searchForm.statuses" 
          :multiple="isMultipleMode"
          :placeholder="isMultipleMode ? '可多选设备状态' : '请选择设备状态'"
          :collapse-tags="isMultipleMode"
          :collapse-tags-tooltip="isMultipleMode"
          :max-collapse-tags="2"
          clearable 
          :class="isMultipleMode ? 'search-select-multiple' : 'search-select'"
          @change="handleSearch"
        >
          <el-option label="在线" value="ONLINE" />
          <el-option label="离线" value="OFFLINE" />
          <el-option label="故障" value="FAULT" />
          <el-option label="维护中" value="MAINTENANCE" />
        </el-select>
      </el-form-item>

      <!-- 设备品牌 - 多选支持 -->
      <el-form-item label="设备品牌">
        <el-select 
          v-model="searchForm.brands" 
          :multiple="isMultipleMode"
          :placeholder="isMultipleMode ? '可多选设备品牌' : '请选择设备品牌'"
          :collapse-tags="isMultipleMode"
          :collapse-tags-tooltip="isMultipleMode"
          :max-collapse-tags="2"
          clearable 
          :class="isMultipleMode ? 'search-select-multiple' : 'search-select'"
          @change="handleSearch"
        >
          <el-option label="华为" value="HUAWEI" />
          <el-option label="小米" value="XIAOMI" />
          <el-option label="苹果" value="APPLE" />
          <el-option label="三星" value="SAMSUNG" />
          <el-option label="欧姆龙" value="OMRON" />
          <el-option label="鱼跃" value="YUWELL" />
          <el-option label="九安" value="ANDON" />
          <el-option label="乐心" value="LIFESENSE" />
        </el-select>
      </el-form-item>

      <!-- 搜索关键词 -->
      <el-form-item label="搜索">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入设备名称、编号或品牌"
          style="width: 250px"
          @keyup.enter="handleSearch"
          clearable
        />
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
        <el-button 
          :type="isMultipleMode ? 'success' : 'info'"
          @click="toggleMultipleMode"
        >
          <el-icon><Setting /></el-icon>
          {{ isMultipleMode ? '单选模式' : '多选模式' }}
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 已选择的筛选条件展示 -->
    <div v-if="hasActiveFilters" class="active-filters">
      <span class="filter-label">当前筛选：</span>
      <el-tag
        v-for="filter in activeFilters"
        :key="filter.key"
        :type="filter.type"
        closable
        @close="removeFilter(filter.key, filter.value)"
        class="filter-tag"
      >
        {{ filter.label }}
      </el-tag>
      <el-button size="small" type="danger" link @click="clearAllFilters">
        清除所有筛选
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { Refresh, Search, Setting } from '@element-plus/icons-vue'
import { computed, reactive, ref } from 'vue'

// 定义事件
const emit = defineEmits(['search', 'reset'])

// 搜索表单
const searchForm = reactive({
  deviceTypes: [],
  statuses: [],
  brands: [],
  keyword: ''
})

// 多选模式开关
const isMultipleMode = ref(false)

// 设备类型映射
const deviceTypeMap = {
  'BLOOD_PRESSURE': '智能血压计',
  'BLOOD_GLUCOSE': '血糖仪',
  'SMART_WATCH': '智能手环',
  'THERMOMETER': '体温计',
  'ECG_MONITOR': '心电监护仪',
  'WEIGHT_SCALE': '智能体重秤',
  'LOCATOR': '定位器',
  'EMERGENCY_BUTTON': '紧急呼叫器',
  'ENVIRONMENT_SENSOR': '环境监测器'
}

// 状态映射
const statusMap = {
  'ONLINE': '在线',
  'OFFLINE': '离线',
  'FAULT': '故障',
  'MAINTENANCE': '维护中'
}

// 品牌映射
const brandMap = {
  'HUAWEI': '华为',
  'XIAOMI': '小米',
  'APPLE': '苹果',
  'SAMSUNG': '三星',
  'OMRON': '欧姆龙',
  'YUWELL': '鱼跃',
  'ANDON': '九安',
  'LIFESENSE': '乐心'
}

// 计算当前激活的筛选条件
const activeFilters = computed(() => {
  const filters = []
  
  // 设备类型筛选
  if (isMultipleMode.value && Array.isArray(searchForm.deviceTypes)) {
    searchForm.deviceTypes.forEach(type => {
      filters.push({
        key: 'deviceTypes',
        value: type,
        label: `类型: ${deviceTypeMap[type]}`,
        type: 'primary'
      })
    })
  } else if (!isMultipleMode.value && searchForm.deviceTypes) {
    filters.push({
      key: 'deviceTypes',
      value: searchForm.deviceTypes,
      label: `类型: ${deviceTypeMap[searchForm.deviceTypes]}`,
      type: 'primary'
    })
  }
  
  // 状态筛选
  if (isMultipleMode.value && Array.isArray(searchForm.statuses)) {
    searchForm.statuses.forEach(status => {
      filters.push({
        key: 'statuses',
        value: status,
        label: `状态: ${statusMap[status]}`,
        type: 'success'
      })
    })
  } else if (!isMultipleMode.value && searchForm.statuses) {
    filters.push({
      key: 'statuses',
      value: searchForm.statuses,
      label: `状态: ${statusMap[searchForm.statuses]}`,
      type: 'success'
    })
  }
  
  // 品牌筛选
  if (isMultipleMode.value && Array.isArray(searchForm.brands)) {
    searchForm.brands.forEach(brand => {
      filters.push({
        key: 'brands',
        value: brand,
        label: `品牌: ${brandMap[brand]}`,
        type: 'warning'
      })
    })
  } else if (!isMultipleMode.value && searchForm.brands) {
    filters.push({
      key: 'brands',
      value: searchForm.brands,
      label: `品牌: ${brandMap[searchForm.brands]}`,
      type: 'warning'
    })
  }
  
  // 关键词筛选
  if (searchForm.keyword) {
    filters.push({
      key: 'keyword',
      value: searchForm.keyword,
      label: `关键词: ${searchForm.keyword}`,
      type: 'info'
    })
  }
  
  return filters
})

// 是否有激活的筛选条件
const hasActiveFilters = computed(() => activeFilters.value.length > 0)

// 切换多选模式
const toggleMultipleMode = () => {
  isMultipleMode.value = !isMultipleMode.value
  
  // 切换模式时重置搜索条件
  if (isMultipleMode.value) {
    // 切换到多选模式：将单选值转换为数组
    searchForm.deviceTypes = searchForm.deviceTypes ? [searchForm.deviceTypes] : []
    searchForm.statuses = searchForm.statuses ? [searchForm.statuses] : []
    searchForm.brands = searchForm.brands ? [searchForm.brands] : []
  } else {
    // 切换到单选模式：取数组第一个值
    searchForm.deviceTypes = Array.isArray(searchForm.deviceTypes) ? searchForm.deviceTypes[0] || '' : searchForm.deviceTypes || ''
    searchForm.statuses = Array.isArray(searchForm.statuses) ? searchForm.statuses[0] || '' : searchForm.statuses || ''
    searchForm.brands = Array.isArray(searchForm.brands) ? searchForm.brands[0] || '' : searchForm.brands || ''
  }
  
  handleSearch()
}

// 搜索处理
const handleSearch = () => {
  emit('search', { ...searchForm, isMultipleMode: isMultipleMode.value })
}

// 重置搜索
const resetSearch = () => {
  searchForm.deviceTypes = isMultipleMode.value ? [] : ''
  searchForm.statuses = isMultipleMode.value ? [] : ''
  searchForm.brands = isMultipleMode.value ? [] : ''
  searchForm.keyword = ''
  emit('reset')
}

// 移除单个筛选条件
const removeFilter = (key, value) => {
  if (isMultipleMode.value) {
    if (Array.isArray(searchForm[key])) {
      const index = searchForm[key].indexOf(value)
      if (index > -1) {
        searchForm[key].splice(index, 1)
      }
    }
  } else {
    if (key === 'keyword') {
      searchForm[key] = ''
    } else {
      searchForm[key] = ''
    }
  }
  handleSearch()
}

// 清除所有筛选条件
const clearAllFilters = () => {
  resetSearch()
}

// 暴露方法给父组件
defineExpose({
  searchForm,
  isMultipleMode,
  resetSearch,
  toggleMultipleMode
})
</script>

<style scoped>
.enhanced-search-bar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 10px;
}

.search-select {
  width: 160px;
  min-width: 160px;
}

.search-select-multiple {
  width: 220px;
  min-width: 220px;
}

.active-filters {
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.filter-label {
  font-size: 14px;
  color: #606266;
  margin-right: 10px;
}

.filter-tag {
  margin-right: 8px;
  margin-bottom: 4px;
}

/* 深度选择器样式 */
:deep(.search-select .el-input__inner) {
  width: 100%;
  min-width: 160px;
}

:deep(.search-select-multiple .el-input__inner) {
  width: 100%;
  min-width: 220px;
}

:deep(.search-select-multiple .el-select__tags) {
  max-width: 200px;
  overflow: hidden;
}

:deep(.search-select-multiple .el-tag) {
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

:deep(.search-select-multiple .el-input) {
  height: auto;
  min-height: 32px;
}

:deep(.search-select-multiple .el-input__inner) {
  height: auto;
  min-height: 32px;
  line-height: 1.5;
  padding-top: 6px;
  padding-bottom: 6px;
}

/* 当有多个标签时增加高度 */
:deep(.search-select-multiple .el-select__tags .el-tag) {
  margin: 2px 4px 2px 0;
  height: 24px;
  line-height: 22px;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .search-select,
  .search-select-multiple {
    width: 100%;
    min-width: 120px;
  }
  
  .search-form {
    display: block;
  }
  
  .search-form .el-form-item {
    display: block;
    margin-bottom: 15px;
  }
}
</style>
