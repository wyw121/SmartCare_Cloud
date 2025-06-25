<template>
  <div v-if="!item.meta?.hidden">
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.meta?.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <el-icon v-if="onlyOneChild.meta.icon">
            <component :is="onlyOneChild.meta.icon" />
          </el-icon>
          <template #title>
            <span class="menu-title">{{ onlyOneChild.meta.title }}</span>
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" teleported>
      <template #title>
        <el-icon v-if="item.meta?.icon">
          <component :is="item.meta.icon" />
        </el-icon>
        <span class="menu-title">{{ item.meta?.title }}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { isExternal } from '@/utils/validate'
import { ref } from 'vue'
import AppLink from './Link.vue'

// Props
const props = defineProps({
  // 路由项
  item: {
    type: Object,
    required: true
  },
  // 是否嵌套
  isNest: {
    type: Boolean,
    default: false
  },
  // 基础路径
  basePath: {
    type: String,
    default: ''
  }
})

// 响应式数据
const onlyOneChild = ref({})

/**
 * 检查是否有一个显示的子路由
 */
const hasOneShowingChild = (children = [], parent) => {
  if (!children) {
    children = []
  }
  
  const showingChildren = children.filter(item => {
    if (item.meta?.hidden) {
      return false
    } else {
      // 临时设置 (用于判断是否只有一个可显示的子路由)
      onlyOneChild.value = item
      return true
    }
  })

  // 当只有一个子路由时，默认显示子路由
  if (showingChildren.length === 1) {
    return true
  }

  // 如果没有子路由可显示，则显示父路由
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false
}

/**
 * 解析路径
 */
const resolvePath = (routePath, routeQuery) => {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  if (routeQuery) {
    let query = JSON.parse(routeQuery)
    return { path: path.resolve(props.basePath, routePath), query: query }
  }
  return path.resolve(props.basePath, routePath)
}

// 导入path工具（这里用简单的字符串拼接替代）
const path = {
  resolve: (base, relative) => {
    if (relative.startsWith('/')) {
      return relative
    }
    return base + '/' + relative
  }
}
</script>

<style lang="scss" scoped>
.menu-title {
  font-size: 14px;
}

.nest-menu :deep(.el-sub-menu > .el-sub-menu__title),
.nest-menu :deep(.el-menu-item) {
  background-color: #1f2d3d !important;
  
  &:hover {
    background-color: #001528 !important;
  }
}
</style>
