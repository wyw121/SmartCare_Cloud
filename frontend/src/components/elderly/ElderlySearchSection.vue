<!-- 老人档案搜索区域 -->
<template>
  <SearchForm
    v-model="searchData"
    :fields="searchFields"
    @search="handleSearch"
    @reset="handleReset"
  >
    <template #buttons>
      <el-button type="success" @click="handleExport">
        <el-icon><Download /></el-icon>
        导出数据
      </el-button>
    </template>
  </SearchForm>
</template>

<script setup>
import SearchForm from '@/components/common/SearchForm.vue'
import { Download } from '@element-plus/icons-vue'
import { computed, ref } from 'vue'

// Props
const props = defineProps({
  modelValue: { type: Object, default: () => ({}) }
})

// Emits
const emit = defineEmits(['update:modelValue', 'search', 'reset', 'export'])

// 搜索数据
const searchData = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 搜索字段配置
const searchFields = ref([
  {
    prop: 'name',
    label: '姓名',
    type: 'input',
    placeholder: '请输入老人姓名',
    span: 6
  },
  {
    prop: 'idCard',
    label: '身份证号',
    type: 'input',
    placeholder: '请输入身份证号',
    span: 6
  },
  {
    prop: 'phone',
    label: '联系电话',
    type: 'input',
    placeholder: '请输入联系电话',
    span: 6
  },
  {
    prop: 'healthStatus',
    label: '健康状态',
    type: 'select',
    placeholder: '请选择健康状态',
    span: 6,
    options: [
      { label: '健康', value: 'HEALTHY' },
      { label: '亚健康', value: 'SUBHEALTH' },
      { label: '轻微异常', value: 'MILD' },
      { label: '需要关注', value: 'ATTENTION' },
      { label: '重点监护', value: 'CRITICAL' }
    ]
  },
  {
    prop: 'gender',
    label: '性别',
    type: 'select',
    placeholder: '请选择性别',
    span: 6,
    options: [
      { label: '男', value: 'MALE' },
      { label: '女', value: 'FEMALE' }
    ]
  },
  {
    prop: 'ageRange',
    label: '年龄范围',
    type: 'select',
    placeholder: '请选择年龄范围',
    span: 6,
    options: [
      { label: '60-65岁', value: '60-65' },
      { label: '66-70岁', value: '66-70' },
      { label: '71-75岁', value: '71-75' },
      { label: '76-80岁', value: '76-80' },
      { label: '81-85岁', value: '81-85' },
      { label: '86岁以上', value: '86+' }
    ]
  },
  {
    prop: 'createTimeRange',
    label: '创建时间',
    type: 'daterange',
    placeholder: '请选择创建时间范围',
    span: 8
  },
  {
    prop: 'address',
    label: '居住地址',
    type: 'input',
    placeholder: '请输入居住地址',
    span: 8
  }
])

// 搜索处理
const handleSearch = (data) => {
  emit('search', data)
}

// 重置处理
const handleReset = (data) => {
  emit('reset', data)
}

// 导出处理
const handleExport = () => {
  emit('export', searchData.value)
}
</script>
