<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </section>
</template>

<script setup>
import { useAppStore } from '@/store/app'
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const appStore = useAppStore()

// 计算属性
const cachedViews = computed(() => appStore.cachedViews)
const key = computed(() => route.path)
</script>

<style lang="scss" scoped>
.app-main {
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
  padding: 20px;
  background-color: #f0f2f5;
}

.fixed-header + .app-main {
  padding-top: 70px;
}

.hasTagsView {
  .app-main {
    min-height: calc(100vh - 84px);
  }
  
  .fixed-header + .app-main {
    padding-top: 104px;
  }
}

/* fade-transform */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.5s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
