<template>
  <div id="tags-view-container" class="tags-view-container">
    <scroll-pane ref="scrollPaneRef" class="tags-view-wrapper" @scroll="handleScroll">
      <router-link
        v-for="tag in visitedViews"
        ref="tag"
        :key="tag.path"
        :class="isActive(tag) ? 'active' : ''"
        :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
        tag="span"
        class="tags-view-item"
        @click.middle="!isAffix(tag) ? closeSelectedTag(tag) : ''"
        @contextmenu.prevent="openMenu(tag, $event)"
      >
        {{ tag.title }}
        <el-icon
          v-if="!isAffix(tag)"
          class="el-icon-close"
          @click.prevent.stop="closeSelectedTag(tag)"
        >
          <Close />
        </el-icon>
      </router-link>
    </scroll-pane>
    <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">刷新页面</li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">关闭当前</li>
      <li @click="closeOthersTags">关闭其他</li>
      <li @click="closeAllTags(selectedTag)">关闭所有</li>
    </ul>
  </div>
</template>

<script setup>
import { useTagsViewStore } from '@/store/tagsView'
import { Close } from '@element-plus/icons-vue'
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ScrollPane from './ScrollPane.vue'

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()

// 响应式数据
const visible = ref(false)
const top = ref(0)
const left = ref(0)
const selectedTag = ref({})
const affixTags = ref([])
const scrollPaneRef = ref()

// 计算属性
const visitedViews = computed(() => tagsViewStore.visitedViews)

/**
 * 判断是否为当前激活的标签
 */
const isActive = (tag) => {
  return tag.path === route.path
}

/**
 * 判断是否为固定标签
 */
const isAffix = (tag) => {
  return tag.meta && tag.meta.affix
}

/**
 * 过滤固定标签
 */
const filterAffixTags = (routes, basePath = '/') => {
  let tags = []
  routes.forEach(route => {
    if (route.meta && route.meta.affix) {
      const tagPath = (basePath.endsWith('/') ? basePath : basePath + '/') + route.path.replace(/^\//, '')
      tags.push({
        fullPath: tagPath,
        path: tagPath,
        name: route.name,
        meta: { ...route.meta }
      })
    }
    if (route.children) {
      const tempTags = filterAffixTags(route.children, (basePath.endsWith('/') ? basePath : basePath + '/') + route.path.replace(/^\//, ''))
      if (tempTags.length >= 1) {
        tags = [...tags, ...tempTags]
      }
    }
  })
  return tags
}

/**
 * 初始化固定标签
 */
const initTags = () => {
  const affixTagsList = affixTags.value = filterAffixTags(router.options.routes)
  
  // 按优先级排序固定标签，确保首页仪表板在最前面
  affixTagsList.sort((a, b) => {
    if (a.name === 'Dashboard') return -1
    if (b.name === 'Dashboard') return 1
    return 0
  })
  
  for (const tag of affixTagsList) {
    if (tag.name) {
      tagsViewStore.addVisitedView(tag)
    }
  }
}

/**
 * 添加标签
 */
const addTags = () => {
  console.log('➕ [TagsView] addTags 调用', {
    routeName: route.name,
    routePath: route.path,
    routeTitle: route.meta?.title
  })
  
  const { name } = route
  if (name && route.meta?.title) {
    console.log('✅ [TagsView] 满足添加条件，调用 tagsViewStore.addView')
    tagsViewStore.addView(route)
  } else {
    console.log('❌ [TagsView] 不满足添加条件', { 
      hasName: !!name, 
      hasTitle: !!route.meta?.title 
    })
  }
  return false
}

/**
 * 移动到当前标签
 */
const moveToCurrentTag = () => {
  const tags = [...document.querySelectorAll('.tags-view-item')]
  nextTick(() => {
    for (const tag of tags) {
      if (tag.to === route.path) {
        scrollPaneRef.value.moveToTarget(tag)
        if (tag.to !== route.path) {
          tagsViewStore.updateVisitedView(route)
        }
        break
      }
    }
  })
}

/**
 * 刷新选中的标签
 */
const refreshSelectedTag = (view) => {
  tagsViewStore.delCachedView(view).then(() => {
    const { fullPath } = view
    nextTick(() => {
      router.replace({
        path: '/redirect' + fullPath
      })
    })
  })
}

/**
 * 关闭选中的标签
 */
const closeSelectedTag = (view) => {
  tagsViewStore.delView(view).then(({ visitedViews }) => {
    if (isActive(view)) {
      toLastView(visitedViews, view)
    }
  })
}

/**
 * 关闭其他标签
 */
const closeOthersTags = () => {
  router.push(selectedTag.value)
  tagsViewStore.delOthersViews(selectedTag.value).then(() => {
    moveToCurrentTag()
  })
}

/**
 * 关闭所有标签
 */
const closeAllTags = (view) => {
  tagsViewStore.delAllViews().then(({ visitedViews }) => {
    if (affixTags.value.some(tag => tag.path === view.path)) {
      return
    }
    toLastView(visitedViews, view)
  })
}

/**
 * 跳转到最后一个视图
 */
const toLastView = (visitedViews, view) => {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath)
  } else {
    if (view.name === 'Dashboard') {
      router.replace({ path: '/redirect' + view.fullPath })
    } else {
      router.push('/')
    }
  }
}

/**
 * 打开右键菜单
 */
const openMenu = (tag, e) => {
  const menuMinWidth = 105
  const offsetLeft = document.querySelector('#tags-view-container').offsetLeft
  const offsetWidth = document.querySelector('#tags-view-container').offsetWidth
  const maxLeft = offsetWidth - menuMinWidth
  const left = e.clientX - offsetLeft + 15

  if (left > maxLeft) {
    left.value = maxLeft
  } else {
    left.value = left
  }

  top.value = e.clientY
  visible.value = true
  selectedTag.value = tag
}

/**
 * 关闭菜单
 */
const closeMenu = () => {
  visible.value = false
}

/**
 * 处理滚动
 */
const handleScroll = () => {
  closeMenu()
}

// 监听路由变化
watch(route, (newRoute, oldRoute) => {
  console.log('🏷️ [TagsView] 路由变化监听', {
    from: oldRoute?.path,
    to: newRoute.path,
    fromName: oldRoute?.name,
    toName: newRoute.name
  })
  addTags()
  moveToCurrentTag()
})

// 监听访问的视图变化
watch(visitedViews, (newViews, oldViews) => {
  console.log('🏷️ [TagsView] 访问视图列表变化', {
    oldCount: oldViews?.length || 0,
    newCount: newViews.length,
    newViews: newViews.map(v => ({ path: v.path, name: v.name, title: v.title }))
  })
}, { immediate: true, deep: true })

// 监听可见性变化
watch(visible, (value) => {
  if (value) {
    document.body.addEventListener('click', closeMenu)
  } else {
    document.body.removeEventListener('click', closeMenu)
  }
})

// 组件挂载时初始化
onMounted(() => {
  console.log('🚀 [TagsView] 组件挂载，开始初始化')
  initTags()
  addTags()
  console.log('✅ [TagsView] 初始化完成')
})
</script>

<style lang="scss" scoped>
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);
  
  .tags-view-wrapper {
    .tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 26px;
      line-height: 26px;
      border: 1px solid #d8dce5;
      color: #495057;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 5px;
      margin-top: 4px;
      
      &:first-of-type {
        margin-left: 15px;
      }
      
      &:last-of-type {
        margin-right: 15px;
      }
      
      &.active {
        background-color: #42b983;
        color: #fff;
        border-color: #42b983;
        
        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 2px;
        }
      }
      
      .el-icon-close {
        width: 16px;
        height: 16px;
        vertical-align: 2px;
        border-radius: 50%;
        text-align: center;
        transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
        transform-origin: 100% 50%;
        
        &:before {
          transform: scale(0.6);
          display: inline-block;
          vertical-align: -3px;
        }
        
        &:hover {
          background-color: #b4bccc;
          color: #fff;
        }
      }
    }
  }
  
  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
    
    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;
      
      &:hover {
        background: #eee;
      }
    }
  }
}
</style>
