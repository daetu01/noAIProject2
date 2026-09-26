<script setup lang="ts">
import { computed } from 'vue'

import type { AnomalyStatus } from '@/types/region'
import { STATUS_META } from '@/utils/status'

const props = withDefaults(
  defineProps<{
    value: number
    status?: AnomalyStatus
    unit?: string
    size?: 'sm' | 'lg'
  }>(),
  { size: 'lg' },
)

const meta = computed(() => (props.status ? STATUS_META[props.status] : null))
</script>

<template>
  <div class="flex items-baseline gap-1 font-mono" :class="meta?.color ?? 'text-cyan'">
    <span
      class="tabular-nums font-bold tracking-tight transition-all duration-500"
      :class="size === 'lg' ? 'text-5xl text-glow-cyan' : 'text-xl'"
      :style="meta ? { textShadow: '0 0 14px currentColor' } : undefined"
    >
      {{ value }}
    </span>
    <span v-if="unit" class="text-xs text-ink-500">{{ unit }}</span>
  </div>
</template>
