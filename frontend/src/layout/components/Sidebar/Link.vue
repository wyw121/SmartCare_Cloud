<template>
  <component :is="linkProps.is" v-bind="linkProps.props">
    <slot />
  </component>
</template>

<script setup>
import { isExternal } from '@/utils/validate'
import { computed } from 'vue'
import { RouterLink } from 'vue-router'

// Props
const props = defineProps({
  to: {
    type: [String, Object],
    required: true
  }
})

// 计算链接属性
const linkProps = computed(() => {
  if (isExternal(props.to)) {
    return {
      is: 'a',
      props: {
        href: props.to,
        target: '_blank',
        rel: 'noopener noreferrer'
      }
    }
  }
  
  return {
    is: RouterLink,
    props: {
      to: props.to
    }
  }
})
</script>
