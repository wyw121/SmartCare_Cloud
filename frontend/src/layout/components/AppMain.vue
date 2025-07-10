<template>
  <section class="app-main">
    <!-- 调试信息 -->
    <div v-if="false" style="position: fixed; top: 0; right: 0; background: rgba(0,0,0,0.8); color: white; padding: 10px; z-index: 9999; font-size: 12px;">
      <div>Route: {{ route.path }}</div>
      <div>Name: {{ route.name }}</div>
      <div>Key: {{ key }}</div>
      <div>Cached: {{ cachedViews.length }}</div>
      <div>Views: {{ cachedViews.join(', ') }}</div>
    </div>
    
    <transition name="fade-transform" mode="out-in">
      <router-view v-slot="{ Component }">
        <keep-alive :include="cachedViews">
          <component 
            :is="Component" 
            :key="key" 
            v-if="Component"
            @vue:mounted="() => console.log('🎭 [AppMain] 子组件挂载:', route.name)"
            @vue:unmounted="() => console.log('💀 [AppMain] 子组件卸载:', route.name)"
          />
        </keep-alive>
      </router-view>
    </transition>
  </section>
</template>

<script setup>
import { useTagsViewStore } from '@/store/tagsView'
import { computed, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const tagsViewStore = useTagsViewStore()

// 计算属性
const cachedViews = computed(() => {
  const cached = tagsViewStore.cachedViews
  console.log('🎭 [AppMain] cachedViews 计算属性更新', {
    cachedCount: cached.length,
    cached: cached,
    currentRoute: route.path,
    currentName: route.name
  })
  return cached
})

const key = computed(() => {
  const routeKey = route.path
  console.log('🔑 [AppMain] key 计算属性更新', { key: routeKey })
  return routeKey
})

// 监听路由变化
watch(route, (newRoute, oldRoute) => {
  console.log('🔄 [AppMain] 路由变化监听', {
    from: oldRoute?.path,
    to: newRoute.path,
    fromName: oldRoute?.name,
    toName: newRoute.name
  })
}, { immediate: true })

// 监听缓存变化
watch(cachedViews, (newCached, oldCached) => {
  console.log('💾 [AppMain] 缓存视图变化', {
    oldCount: oldCached?.length || 0,
    newCount: newCached.length,
    oldCached: oldCached,
    newCached: newCached
  })
}, { immediate: true })

onMounted(() => {
  console.log('🚀 [AppMain] 组件挂载完成', {
    currentRoute: route.path,
    cachedViewsCount: cachedViews.value.length
  })
})

onUnmounted(() => {
  console.log('💀 [AppMain] 组件卸载')
})
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
