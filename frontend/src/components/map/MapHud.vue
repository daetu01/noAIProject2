<script setup lang="ts">
import { computed } from 'vue'

import type { ConnectionStatus } from '@/stores/regionStore'
import type { SystemStatus } from '@/types/region'
import { timeAgo } from '@/utils/format'

const props = defineProps<{
  selectedName?: string
  systemStatus: SystemStatus
  connectionStatus: ConnectionStatus
  lastSyncedAt: Date | null
}>()

const systemColor = computed(() => {
  if (props.systemStatus === 'CRITICAL') return 'text-status-critical'
  if (props.systemStatus === 'WARNING') return 'text-status-warning'
  return 'text-cyan'
})

const dataColor = computed(() => (props.connectionStatus === 'error' ? 'text-status-critical' : 'text-status-normal'))
const dataLabel = computed(() => {
  if (props.connectionStatus === 'live') return 'DATA LIVE'
  if (props.connectionStatus === 'error') return 'DATA OFFLINE'
  return 'DATA SYNCING'
})
</script>

<template>
  <div class="pointer-events-none absolute inset-0 select-none font-mono">
    <!-- top-left: brand -->
    <div class="absolute left-3 top-3 text-[11px] tracking-[0.2em] text-ink-300">
      URBAN<span class="text-cyan">SIGNAL</span> <span class="text-ink-700">/</span> SEOUL
    </div>

    <!-- top-right: live monitoring -->
    <div class="absolute right-3 top-3 flex items-center gap-1.5 text-[10px] tracking-widest text-ink-500">
      <span class="h-1.5 w-1.5 rounded-full bg-cyan" :class="connectionStatus === 'live' ? 'animate-pulse' : ''" />
      LIVE MONITORING
    </div>

    <!-- bottom-left: system status stack -->
    <div class="absolute bottom-3 left-3 space-y-0.5 text-[10px] tracking-widest">
      <p :class="systemColor">SYSTEM {{ systemStatus }}</p>
      <p :class="dataColor">{{ dataLabel }}</p>
      <p v-if="lastSyncedAt" class="text-ink-700">LAST UPDATE {{ timeAgo(lastSyncedAt.toISOString()) }}</p>
    </div>

    <!-- bottom-right: current selection -->
    <div v-if="selectedName" class="absolute bottom-3 right-3 text-right text-[10px] tracking-widest text-ink-500">
      <p class="text-ink-700">SELECTED</p>
      <p class="text-cyan">{{ selectedName }}</p>
    </div>
  </div>
</template>
