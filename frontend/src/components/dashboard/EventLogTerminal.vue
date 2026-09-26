<script setup lang="ts">
import type { RegionEvent } from '@/types/region'
import { formatTime } from '@/utils/format'

defineProps<{
  events: RegionEvent[]
}>()

const LEVEL_STYLE: Record<RegionEvent['level'], string> = {
  info: 'text-cyan',
  warning: 'text-amber',
  critical: 'text-red',
}

const LEVEL_TAG: Record<RegionEvent['level'], string> = {
  info: 'INFO',
  warning: 'WARN',
  critical: 'CRIT',
}
</script>

<template>
  <div
    class="max-h-56 space-y-1 overflow-y-auto rounded-sm border border-line bg-black/40 p-3 font-mono text-[11px] leading-relaxed"
  >
    <p v-if="!events.length" class="text-ink-700">// no events recorded</p>
    <div v-for="event in events" :key="event.id" class="flex gap-2">
      <span class="shrink-0 text-ink-700">{{ formatTime(event.timestamp) }}</span>
      <span class="shrink-0 font-bold" :class="LEVEL_STYLE[event.level]">[{{ LEVEL_TAG[event.level] }}]</span>
      <span class="text-ink-300">{{ event.message }}</span>
    </div>
    <div class="flex items-center gap-1 pt-1 text-ink-700">
      <span>&gt;</span>
      <span class="h-3 w-1.5 animate-pulse bg-cyan/70" />
    </div>
  </div>
</template>
