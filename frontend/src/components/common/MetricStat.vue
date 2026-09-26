<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  label: string
  value: string
  delta?: number
  deltaSuffix?: string
}>()

const deltaTone = computed(() => {
  if (props.delta === undefined) return ''
  if (props.delta > 0) return 'text-red'
  if (props.delta < 0) return 'text-green'
  return 'text-ink-500'
})

const deltaText = computed(() => {
  if (props.delta === undefined) return ''
  const sign = props.delta > 0 ? '+' : ''
  return `${sign}${props.delta.toFixed(1)}${props.deltaSuffix ?? ''}`
})
</script>

<template>
  <div class="rounded-sm border border-line bg-panel-alt/60 px-3 py-2.5">
    <p class="font-mono text-[10px] uppercase tracking-widest text-ink-500">{{ label }}</p>
    <div class="mt-1 flex items-baseline gap-2">
      <span class="font-mono text-lg font-semibold tabular-nums text-ink-100">{{ value }}</span>
      <span v-if="delta !== undefined" class="font-mono text-[11px] font-medium" :class="deltaTone">
        {{ deltaText }}
      </span>
    </div>
  </div>
</template>
