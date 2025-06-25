<template>
  <div class="chart-test">
    <h2>ECharts 测试页面</h2>
    
    <div class="test-section">
      <h3>组件是否注册：</h3>
      <p>VChart 组件：{{ typeof VChart === 'object' ? '✓ 已注册' : '✗ 未注册' }}</p>
    </div>
    
    <div class="test-section">
      <h3>简单折线图测试：</h3>
      <div style="height: 300px; border: 1px solid #ddd;">
        <v-chart :option="simpleOption" style="height: 100%;" />
      </div>
    </div>
    
    <div class="test-section">
      <h3>原始图表选项：</h3>
      <pre>{{ JSON.stringify(simpleOption, null, 2) }}</pre>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import VChart from 'vue-echarts';

const simpleOption = reactive({
  title: {
    text: '简单测试图表'
  },
  tooltip: {},
  xAxis: {
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {},
  series: [{
    name: '销量',
    type: 'line',
    data: [120, 200, 150, 80, 70, 110, 130]
  }]
})
</script>

<style scoped>
.chart-test {
  padding: 20px;
}

.test-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 4px;
}

pre {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  max-height: 200px;
  overflow: auto;
}
</style>
