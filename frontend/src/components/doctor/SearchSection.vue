<template>
  <div class="doctor-search-section">
    <el-card class="search-card">
      <el-form :model="searchForm" ref="searchFormRef" :inline="true" label-width="80px">
        <el-form-item label="医生姓名" prop="name">
          <el-input 
            v-model="searchForm.name" 
            placeholder="请输入医生姓名" 
            clearable 
            style="width: 150px;"
          />
        </el-form-item>
        
        <el-form-item label="工号" prop="employeeNumber">
          <el-input 
            v-model="searchForm.employeeNumber" 
            placeholder="请输入医生工号" 
            clearable 
            style="width: 150px;"
          />
        </el-form-item>
        
        <el-form-item label="科室" prop="department">
          <el-select v-model="searchForm.department" placeholder="请选择科室" clearable style="width: 120px;">
            <el-option label="内科" value="内科" />
            <el-option label="外科" value="外科" />
            <el-option label="儿科" value="儿科" />
            <el-option label="妇科" value="妇科" />
            <el-option label="眼科" value="眼科" />
            <el-option label="耳鼻喉科" value="耳鼻喉科" />
            <el-option label="皮肤科" value="皮肤科" />
            <el-option label="神经科" value="神经科" />
            <el-option label="心血管科" value="心血管科" />
            <el-option label="骨科" value="骨科" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="职称" prop="title">
          <el-select v-model="searchForm.title" placeholder="请选择职称" clearable style="width: 120px;">
            <el-option label="主任医师" value="主任医师" />
            <el-option label="副主任医师" value="副主任医师" />
            <el-option label="主治医师" value="主治医师" />
            <el-option label="住院医师" value="住院医师" />
            <el-option label="实习医师" value="实习医师" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 100px;">
            <el-option label="在职" value="1" />
            <el-option label="离职" value="0" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { Refresh, Search } from '@element-plus/icons-vue'
import { reactive, ref } from 'vue'

const emit = defineEmits(['search', 'reset'])

const searchFormRef = ref()
const searchForm = reactive({
  name: '',
  employeeNumber: '',
  department: '',
  title: '',
  status: ''
})

const handleSearch = () => {
  emit('search', { ...searchForm })
}

const handleReset = () => {
  if (searchFormRef.value) {
    searchFormRef.value.resetFields()
  }
  Object.assign(searchForm, {
    name: '',
    employeeNumber: '',
    department: '',
    title: '',
    status: ''
  })
  emit('reset')
}
</script>

<style scoped>
.doctor-search-section {
  margin-bottom: 20px;
}

.search-card {
  padding: 10px;
}

.search-card :deep(.el-form-item) {
  margin-bottom: 10px;
}
</style>
