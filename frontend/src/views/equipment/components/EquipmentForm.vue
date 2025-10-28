<template>
  <div>Equipment Form Component</div>
</template>

<script setup>
const props = defineProps({
  equipment: Object,
  isEdit: Boolean
})

const emit = defineEmits(['success', 'cancel'])
</script>
