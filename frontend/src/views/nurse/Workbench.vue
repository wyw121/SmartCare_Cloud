<template>
  <div class="nurse-workbench">
    <h2>护工工作台</h2>
    <p class="subtitle">管理今日护理任务和照护老人</p>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="stat">
            <el-icon class="icon"><User /></el-icon>
            <div>
              <p>照护老人</p>
              <h3>{{ stats.elderlyCount }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat">
            <el-icon class="icon"><Clock /></el-icon>
            <div>
              <p>今日任务</p>
              <h3>{{ stats.todayTasks }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat">
            <el-icon class="icon"><Finished /></el-icon>
            <div>
              <p>已完成</p>
              <h3>{{ stats.completedTasks }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 今日护理任务 -->
    <el-card style="margin-top: 20px">
      <template #header>
        <div style="display: flex; justify-content: space-between">
          <h3>今日护理任务</h3>
          <el-button type="primary" @click="handleAddRecord">新增记录</el-button>
        </div>
      </template>
      <el-table :data="tasks">
        <el-table-column prop="elderlyName" label="老人姓名" />
        <el-table-column prop="taskType" label="护理类型" />
        <el-table-column prop="scheduledTime" label="计划时间" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'completed' ? 'success' : 'warning'">
              {{ row.status === 'completed' ? '已完成' : '待完成' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button size="small" @click="handleComplete(row)">执行任务</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const stats = reactive({
  elderlyCount: 5,
  todayTasks: 15,
  completedTasks: 8
})

const tasks = ref([
  { id: 1, elderlyName: '张三', taskType: '晨间护理', scheduledTime: '07:00', status: 'completed' },
  { id: 2, elderlyName: '李四', taskType: '喂食', scheduledTime: '12:00', status: 'pending' },
  { id: 3, elderlyName: '王五', taskType: '翻身', scheduledTime: '14:00', status: 'pending' }
])

const handleAddRecord = () => {
  router.push('/nursing/record')
}

const handleComplete = (row) => {
  console.log('执行任务:', row)
}
</script>

<style scoped>
.nurse-workbench {
  padding: 20px;
}
.stat {
  display: flex;
  align-items: center;
  gap: 15px;
}
.icon {
  font-size: 40px;
  color: #409EFF;
}
</style>
