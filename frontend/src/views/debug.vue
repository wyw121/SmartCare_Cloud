<template>
  <div class="debug-page">
    <h1>调试页面</h1>
    <div class="debug-info">
      <h2>当前路由信息</h2>
      <p><strong>路径:</strong> {{ $route.path }}</p>
      <p><strong>名称:</strong> {{ $route.name }}</p>
      <p><strong>参数:</strong> {{ JSON.stringify($route.params) }}</p>
      <p><strong>查询:</strong> {{ JSON.stringify($route.query) }}</p>
    </div>
    
    <div class="test-section">
      <h2>ECharts 测试</h2>
      <div style="height: 400px; border: 1px solid #ccc; margin: 20px 0;">
        <v-chart :option="testOption" style="height: 100%;" />
      </div>
    </div>
    
    <div class="navigation-test">
      <h2>导航测试</h2>
      <el-button @click="goTo('/dashboard/index')">前往仪表板</el-button>
      <el-button @click="goTo('/test')">前往测试页</el-button>
      <el-button @click="goTo('/chart-test')">前往图表测试</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const testOption = reactive({
  title: {
    text: 'ECharts 调试测试'
  },
  tooltip: {},
  xAxis: {
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {},
  series: [{
    name: '销量',
    type: 'bar',
    data: [120, 200, 150, 80, 70, 110, 130],
    itemStyle: {
      color: '#409EFF'
    }
  }]
})

const goTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.debug-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.debug-info {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 4px;
  margin: 20px 0;
}

.test-section {
  margin: 20px 0;
}

.navigation-test {
  margin: 20px 0;
}

.navigation-test .el-button {
  margin-right: 10px;
}
</style>
