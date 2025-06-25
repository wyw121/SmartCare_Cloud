<template>
  <el-scrollbar ref="scrollContainer" :vertical="false" class="scroll-container" @wheel.prevent="handleScroll">
    <slot />
  </el-scrollbar>
</template>

<script setup>
import { ref, nextTick } from 'vue'

// 响应式数据
const scrollContainer = ref()

// Emits
const emit = defineEmits(['scroll'])

/**
 * 处理滚动
 */
const handleScroll = (e) => {
  const eventDelta = e.wheelDelta || -e.deltaY * 40
  const scrollDom = scrollContainer.value.wrapRef
  scrollDom.scrollLeft = scrollDom.scrollLeft + eventDelta / 4
  emit('scroll')
}

/**
 * 移动到目标元素
 */
const moveToTarget = (currentTag) => {
  const scrollDom = scrollContainer.value.wrapRef
  const containerWidth = scrollDom.offsetWidth
  const scrollLeft = scrollDom.scrollLeft
  
  nextTick(() => {
    let firstTag = null
    let lastTag = null
    
    // 找到第一个和最后一个标签
    if (scrollDom.children[0]) {
      firstTag = scrollDom.children[0].children[0]
      lastTag = scrollDom.children[0].children[scrollDom.children[0].children.length - 1]
    }
    
    if (firstTag === currentTag) {
      scrollDom.scrollLeft = 0
    } else if (lastTag === currentTag) {
      scrollDom.scrollLeft = scrollDom.scrollWidth - containerWidth
    } else {
      const currentIndex = Array.from(scrollDom.children[0].children).findIndex(item => item === currentTag)
      const prevTag = scrollDom.children[0].children[currentIndex - 1]
      const nextTag = scrollDom.children[0].children[currentIndex + 1]
      
      const afterNextTagOffsetLeft = nextTag ? nextTag.offsetLeft + nextTag.offsetWidth + 4 : 0
      const beforePrevTagOffsetLeft = prevTag ? prevTag.offsetLeft - 4 : 0
      
      if (afterNextTagOffsetLeft > scrollLeft + containerWidth) {
        scrollDom.scrollLeft = afterNextTagOffsetLeft - containerWidth
      } else if (beforePrevTagOffsetLeft < scrollLeft) {
        scrollDom.scrollLeft = beforePrevTagOffsetLeft
      }
    }
  })
}

// 暴露方法
defineExpose({
  moveToTarget
})
</script>

<style lang="scss" scoped>
.scroll-container {
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  width: 100%;
  
  :deep(.el-scrollbar__bar) {
    bottom: 0px;
  }
  
  :deep(.el-scrollbar__wrap) {
    height: 49px;
  }
}
</style>
