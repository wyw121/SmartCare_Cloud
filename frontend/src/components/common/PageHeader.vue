<!-- 通用页面头部组件 -->
<template>
  <el-card class="page-header" :shadow="shadow">
    <div class="header-content">
      <div class="header-info">
        <h1 class="page-title">
          <el-icon v-if="icon" :size="24">
            <component :is="icon" />
          </el-icon>
          {{ title }}
        </h1>
        <p v-if="subtitle" class="page-subtitle">{{ subtitle }}</p>
        <div v-if="breadcrumb" class="page-breadcrumb">
          <el-breadcrumb :separator="breadcrumbSeparator">
            <el-breadcrumb-item
              v-for="item in breadcrumb"
              :key="item.path"
              :to="item.path"
            >
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </div>
      <div v-if="$slots.extra" class="header-extra">
        <slot name="extra" />
      </div>
    </div>
    <div v-if="$slots.content" class="header-extended">
      <slot name="content" />
    </div>
  </el-card>
</template>

<script setup>
// Props
const props = defineProps({
  title: { type: String, required: true },
  subtitle: { type: String, default: '' },
  icon: { type: [String, Object], default: null },
  shadow: { type: String, default: 'never' },
  breadcrumb: { type: Array, default: null },
  breadcrumbSeparator: { type: String, default: '/' }
})
</script>

<style scoped>
.page-header {
  margin-bottom: 20px;
  border: none;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.page-header :deep(.el-card__body) {
  padding: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 20px;
}

.header-info {
  flex: 1;
  min-width: 0;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  line-height: 1.2;
  color: white;
}

.page-subtitle {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.4;
}

.page-breadcrumb {
  margin-top: 8px;
}

.page-breadcrumb :deep(.el-breadcrumb__inner) {
  color: rgba(255, 255, 255, 0.8) !important;
}

.page-breadcrumb :deep(.el-breadcrumb__inner:hover) {
  color: white !important;
}

.page-breadcrumb :deep(.el-breadcrumb__separator) {
  color: rgba(255, 255, 255, 0.6) !important;
}

.header-extra {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.header-extended {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: stretch;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .page-subtitle {
    font-size: 14px;
  }
  
  .header-extra {
    justify-content: flex-end;
  }
}
</style>
