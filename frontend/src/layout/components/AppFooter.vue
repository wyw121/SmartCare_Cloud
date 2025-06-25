<template>
  <footer class="app-footer">
    <div class="footer-content">
      <div class="footer-left">
        <span>© 2024 智慧医养大数据公共服务平台 版权所有</span>
      </div>
      <div class="footer-right">
        <span>技术支持：GitHub Copilot</span>
        <el-divider direction="vertical" />
        <span>版本：v1.0.0</span>
      </div>
    </div>
  </footer>
</template>

<script setup>
// 组件逻辑（如果需要的话）
</script>

<style lang="scss" scoped>
.app-footer {
  height: 50px;
  background: #fff;
  border-top: 1px solid #e6e6e6;
  
  .footer-content {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    font-size: 14px;
    color: #666;
    
    .footer-left {
      color: #666;
    }
    
    .footer-right {
      display: flex;
      align-items: center;
      gap: 10px;
    }
  }
}
</style>
