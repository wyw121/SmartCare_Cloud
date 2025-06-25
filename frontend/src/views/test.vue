<template>
  <div style="padding: 20px;">
    <h1>测试页面</h1>
    <p>如果您看到这个页面，说明基本的Vue路由和组件渲染是正常的。</p>
    
    <el-button type="primary">这是一个Element Plus按钮</el-button>
    
    <div style="margin-top: 20px;">
      <el-icon style="font-size: 20px; color: #409EFF;"><Platform /></el-icon>
      <span style="margin-left: 10px;">图标测试</span>
    </div>
    
    <div style="margin-top: 20px;">
      <el-card shadow="hover">
        <template #header>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span>卡片测试</span>
            <el-button type="text">操作</el-button>
          </div>
        </template>
        <p>这是卡片内容</p>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { Platform } from '@element-plus/icons-vue';
</script>
