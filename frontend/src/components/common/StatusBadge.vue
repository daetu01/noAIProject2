<script setup lang="ts">
import { computed } from 'vue'

import type { AnomalyStatus } from '@/types/region'
import { STATUS_META } from '@/utils/status'

const props = withDefaults(
  defineProps<{
    status: AnomalyStatus
    size?: 'sm' | 'md'
  }>(),
  { size: 'md' },
)

const meta = computed(() => STATUS_META[props.status])
const isAlert = computed(() => props.status === 'critical' || props.status === 'warning')
</script>

<template>
  <span
    class="inline-flex items-center gap-1.5 rounded-sm border font-mono uppercase tracking-widest"
    :class="[
      meta.bg,
      meta.border,
      meta.color,
      size === 'sm' ? 'px-1.5 py-0.5 text-[10px]' : 'px-2 py-1 text-xs',
    ]"
  >
    <span class="relative flex h-1.5 w-1.5">
      <span
        v-if="isAlert"
        class="absolute inline-flex h-full w-full animate-ping rounded-full opacity-75"
        :class="meta.dot"
      />
      <span class="relative inline-flex h-1.5 w-1.5 rounded-full" :class="meta.dot" />
    </span>
    {{ meta.label }}
  </span>
</template>
