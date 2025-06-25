<template>
  <div class="user-search-section">
    <el-form :model="searchForm" inline class="search-form">
      <el-form-item label="关键词">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入用户名或姓名"
          clearable
          style="width: 200px;"
          @keyup.enter="handleSearch"
        />
      </el-form-item>

      <el-form-item label="状态">
        <el-select 
          v-model="searchForm.status" 
          placeholder="请选择状态" 
          clearable
          style="width: 120px;"
        >
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>

      <el-form-item label="角色">
        <el-select 
          v-model="searchForm.roleId" 
          placeholder="请选择角色" 
          clearable
          style="width: 150px;"
        >
          <el-option 
            v-for="role in roles" 
            :key="role.id" 
            :label="role.name" 
            :value="role.id" 
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { Refresh, Search } from '@element-plus/icons-vue'
import { reactive } from 'vue'

const props = defineProps({
  roles: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['search', 'reset'])

const searchForm = reactive({
  keyword: '',
  status: null,
  roleId: null
})

const handleSearch = () => {
  emit('search', { ...searchForm })
}

const handleReset = () => {
  Object.assign(searchForm, {
    keyword: '',
    status: null,
    roleId: null
  })
  emit('reset')
}
</script>

<style scoped>
.user-search-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.search-form {
  margin-bottom: 0;
}
</style>
