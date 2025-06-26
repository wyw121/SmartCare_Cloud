<template>
  <div class="simple-doctor-test">
    <h1>医生数据测试</h1>
    
    <div class="status">
      <p>Loading: {{ loading }}</p>
      <p>Error: {{ error }}</p>
      <p>Data Count: {{ doctorList.length }}</p>
    </div>
    
    <button @click="loadData">加载医生数据</button>
    
    <div v-if="loading" class="loading">加载中...</div>
    
    <div v-if="error" class="error">
      错误: {{ error }}
    </div>
    
    <div v-if="doctorList.length > 0" class="data">
      <h3>医生列表:</h3>
      <ul>
        <li v-for="doctor in doctorList" :key="doctor.id">
          {{ doctor.name }} - {{ doctor.department }} - {{ doctor.title }}
        </li>
      </ul>
      
      <h4>原始数据:</h4>
      <pre>{{ JSON.stringify(doctorList, null, 2) }}</pre>
    </div>
  </div>
</template>

<script setup>
import { getDoctorPageList } from '@/api/doctor'
import { ref } from 'vue'

const loading = ref(false)
const error = ref('')
const doctorList = ref([])

const loadData = async () => {
  loading.value = true
  error.value = ''
  doctorList.value = []
  
  try {
    console.log('开始请求医生数据...')
    
    const params = {
      pageNum: 1,
      pageSize: 10
    }
    
    console.log('请求参数:', params)
    
    const response = await getDoctorPageList(params)
    
    console.log('API响应:', response)
    
    if (response.code === 200) {
      doctorList.value = response.data.list || []
      console.log('医生数据:', doctorList.value)
    } else {
      error.value = response.message || '获取数据失败'
    }
  } catch (err) {
    console.error('请求失败:', err)
    error.value = err.message || '网络请求失败'
  } finally {
    loading.value = false
  }
}

// 自动加载数据
loadData()
</script>

<style scoped>
.simple-doctor-test {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.status {
  background: #f5f5f5;
  padding: 10px;
  margin: 10px 0;
  border-radius: 5px;
}

.loading {
  color: #409eff;
  font-weight: bold;
}

.error {
  color: #f56c6c;
  font-weight: bold;
}

.data {
  margin-top: 20px;
}

pre {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 5px;
  overflow-x: auto;
  font-size: 12px;
}

button {
  background: #409eff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background: #337ab7;
}
</style>
