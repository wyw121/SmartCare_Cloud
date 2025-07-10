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
import { useTagsViewStore } from '@/store/tagsView'
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const tagsViewStore = useTagsViewStore()

// 计算属性
const cachedViews = computed(() => tagsViewStore.cachedViews)
const key = computed(() => route.path)
</script>

<style lang="scss" scoped>
.app-main {
  position: relative;
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  box-sizing: border-box;
  width: 100%;
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
