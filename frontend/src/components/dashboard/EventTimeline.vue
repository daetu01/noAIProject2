<script setup lang="ts">
import { computed } from 'vue'

import { useRegionStore } from '@/stores/regionStore'
import { formatTime } from '@/utils/format'

const store = useRegionStore()

const timeline = computed(() =>
  store.regions
    .flatMap((region) => region.events.map((event) => ({ ...event, regionName: region.name })))
    .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime())
    .slice(0, 12),
)

const LEVEL_STYLE: Record<string, string> = {
  info: 'bg-cyan border-cyan/50',
  warning: 'bg-amber border-amber/50',
  critical: 'bg-red border-red/50',
}
</script>

<template>
  <div class="relative overflow-x-auto pb-2">
    <div class="relative flex min-w-max gap-6 px-1 pt-2">
      <div class="absolute left-0 right-0 top-[9px] h-px bg-line-strong" />
      <div
        v-for="item in timeline"
        :key="item.id"
        class="relative flex w-40 flex-col gap-1.5"
      >
        <span
          class="z-10 h-2.5 w-2.5 rounded-full border-2 border-void ring-1"
          :class="LEVEL_STYLE[item.level]"
        />
        <span class="font-mono text-[10px] text-ink-500">{{ formatTime(item.timestamp) }}</span>
        <span class="font-mono text-[10px] font-semibold uppercase tracking-wide text-ink-300">
          {{ item.regionName }}
        </span>
        <span class="text-[11px] leading-snug text-ink-500">{{ item.message }}</span>
      </div>
    </div>
  </div>
</template>
