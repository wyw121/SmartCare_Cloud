<!-- 通用搜索表单组件 -->
<template>
  <el-card class="search-form-card">
    <el-form
      ref="formRef"
      :model="formData"
      :inline="inline"
      :label-width="labelWidth"
      @submit.prevent="handleSearch"
    >
      <el-row :gutter="gutter">
        <el-col 
          v-for="field in fields" 
          :key="field.prop"
          :span="field.span || defaultSpan"
        >
          <el-form-item 
            :label="field.label" 
            :prop="field.prop"
            :rules="field.rules"
          >
            <!-- 输入框 -->
            <el-input
              v-if="field.type === 'input'"
              v-model="formData[field.prop]"
              :placeholder="field.placeholder || `请输入${field.label}`"
              :clearable="field.clearable !== false"
              :disabled="field.disabled"
              @keyup.enter="handleSearch"
            />
            
            <!-- 选择器 -->
            <el-select
              v-else-if="field.type === 'select'"
              v-model="formData[field.prop]"
              :placeholder="field.placeholder || `请选择${field.label}`"
              :clearable="field.clearable !== false"
              :disabled="field.disabled"
              :multiple="field.multiple"
              style="width: 100%"
            >
              <el-option
                v-for="option in field.options"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
            
            <!-- 日期选择器 -->
            <el-date-picker
              v-else-if="field.type === 'date'"
              v-model="formData[field.prop]"
              type="date"
              :placeholder="field.placeholder || `请选择${field.label}`"
              :disabled="field.disabled"
              style="width: 100%"
            />
            
            <!-- 日期范围选择器 -->
            <el-date-picker
              v-else-if="field.type === 'daterange'"
              v-model="formData[field.prop]"
              type="daterange"
              :start-placeholder="field.startPlaceholder || '开始日期'"
              :end-placeholder="field.endPlaceholder || '结束日期'"
              :disabled="field.disabled"
              style="width: 100%"
            />
            
            <!-- 数字输入框 -->
            <el-input-number
              v-else-if="field.type === 'number'"
              v-model="formData[field.prop]"
              :min="field.min"
              :max="field.max"
              :step="field.step"
              :disabled="field.disabled"
              style="width: 100%"
            />
            
            <!-- 开关 -->
            <el-switch
              v-else-if="field.type === 'switch'"
              v-model="formData[field.prop]"
              :disabled="field.disabled"
            />
            
            <!-- 自定义插槽 -->
            <slot 
              v-else-if="field.type === 'slot'" 
              :name="field.prop" 
              :field="field"
              :value="formData[field.prop]"
            />
          </el-form-item>
        </el-col>
        
        <!-- 操作按钮 -->
        <el-col :span="buttonSpan">
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              {{ searchText }}
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              {{ resetText }}
            </el-button>
            <el-button 
              v-if="showToggle && fields.length > showFieldCount" 
              type="text" 
              @click="toggleExpanded"
            >
              {{ expanded ? '收起' : '展开' }}
              <el-icon>
                <ArrowUp v-if="expanded" />
                <ArrowDown v-else />
              </el-icon>
            </el-button>
            <slot name="buttons" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-card>
</template>

<script setup>
import { ArrowDown, ArrowUp, Refresh, Search } from '@element-plus/icons-vue'
import { computed, nextTick, ref, watch } from 'vue'

// Props
const props = defineProps({
  // 表单字段配置
  fields: { type: Array, required: true },
  modelValue: { type: Object, default: () => ({}) },
  
  // 布局配置
  inline: { type: Boolean, default: true },
  labelWidth: { type: String, default: '80px' },
  gutter: { type: Number, default: 20 },
  defaultSpan: { type: Number, default: 6 },
  buttonSpan: { type: Number, default: 6 },
  
  // 功能配置
  showToggle: { type: Boolean, default: true },
  showFieldCount: { type: Number, default: 3 },
  searchText: { type: String, default: '搜索' },
  resetText: { type: String, default: '重置' },
  
  // 初始值
  defaultValues: { type: Object, default: () => ({}) }
})

// Emits
const emit = defineEmits(['update:modelValue', 'search', 'reset'])

// 响应式数据
const formRef = ref()
const expanded = ref(false)
const formData = ref({})

// 计算属性
const visibleFields = computed(() => {
  if (!props.showToggle || expanded.value) {
    return props.fields
  }
  return props.fields.slice(0, props.showFieldCount)
})

// 初始化表单数据
const initFormData = () => {
  const data = {}
  props.fields.forEach(field => {
    data[field.prop] = props.modelValue[field.prop] ?? 
                      props.defaultValues[field.prop] ?? 
                      field.defaultValue ?? 
                      (field.multiple ? [] : '')
  })
  formData.value = data
}

// 监听器
watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    Object.assign(formData.value, newVal)
  }
}, { immediate: true, deep: true })

watch(formData, (newVal) => {
  emit('update:modelValue', newVal)
}, { deep: true })

// 方法
const handleSearch = () => {
  emit('search', formData.value)
}

const handleReset = () => {
  formRef.value?.resetFields()
  initFormData()
  nextTick(() => {
    emit('reset', formData.value)
  })
}

const toggleExpanded = () => {
  expanded.value = !expanded.value
}

// 初始化
initFormData()

// 暴露方法
defineExpose({
  validate: () => formRef.value?.validate(),
  resetFields: () => formRef.value?.resetFields(),
  clearValidate: () => formRef.value?.clearValidate()
})
</script>

<style scoped>
.search-form-card {
  margin-bottom: 16px;
}

.search-form-card :deep(.el-card__body) {
  padding: 16px;
}

.search-form-card :deep(.el-form-item) {
  margin-bottom: 16px;
}

.search-form-card :deep(.el-form-item:last-child) {
  margin-bottom: 0;
}
</style>
