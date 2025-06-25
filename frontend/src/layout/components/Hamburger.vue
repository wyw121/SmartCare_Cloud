<template>
  <div class="hamburger" @click="toggleClick">
    <el-icon class="hamburger-icon" :class="{ 'is-active': isActive }">
      <component :is="isActive ? 'Fold' : 'Expand'" />
    </el-icon>
  </div>
</template>

<script setup>
import { Fold, Expand } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  isActive: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['toggleClick'])

/**
 * 切换点击事件
 */
const toggleClick = () => {
  emit('toggleClick')
}
</script>

<style lang="scss" scoped>
.hamburger {
  display: inline-block;
  vertical-align: middle;
  width: 20px;
  height: 20px;
  cursor: pointer;
  
  .hamburger-icon {
    width: 20px;
    height: 20px;
    transition: all 0.3s;
    
    &.is-active {
      transform: rotate(90deg);
    }
  }
}
</style>
