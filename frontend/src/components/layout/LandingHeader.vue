<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink } from 'vue-router'

import { useRegionStore } from '@/stores/regionStore'
import { systemStatusMeta } from '@/utils/status'
import NeonButton from '@/components/common/NeonButton.vue'

const store = useRegionStore()
const statusMeta = computed(() => systemStatusMeta(store.systemStatus))
</script>

<template>
  <header class="sticky top-0 z-30 border-b border-line bg-abyss/70 backdrop-blur-xl">
    <div class="mx-auto flex h-16 max-w-6xl items-center gap-6 px-4 sm:px-6">
      <div class="flex items-center gap-2.5">
        <span class="relative flex h-8 w-8 items-center justify-center rounded-sm border border-cyan/50 bg-cyan/10">
          <span class="absolute inset-0 animate-pulse rounded-sm border border-cyan/30" />
          <svg viewBox="0 0 24 24" class="h-4 w-4 text-cyan" fill="none" stroke="currentColor" stroke-width="1.8">
            <path d="M3 12h4l2-7 4 14 2-7h6" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
        </span>
        <span class="font-display text-base font-bold tracking-[0.08em] text-ink-100">
          URBAN<span class="text-cyan text-glow-cyan">SIGNAL</span>
        </span>
      </div>

      <nav class="hidden items-center gap-6 font-mono text-xs uppercase tracking-widest text-ink-500 md:flex">
        <a href="#concept" class="transition-colors hover:text-cyan">Concept</a>
        <a href="#features" class="transition-colors hover:text-cyan">Features</a>
        <a href="#regions" class="transition-colors hover:text-cyan">Zones</a>
      </nav>

      <div
        class="ml-auto hidden items-center gap-2 rounded-sm border px-2.5 py-1 font-mono text-[10px] sm:flex"
        :class="[statusMeta.color, 'border-current/30']"
      >
        <span class="relative flex h-1.5 w-1.5">
          <span class="absolute inline-flex h-full w-full animate-ping rounded-full opacity-70" :class="statusMeta.dot" />
          <span class="relative inline-flex h-1.5 w-1.5 rounded-full" :class="statusMeta.dot" />
        </span>
        SYSTEM {{ store.systemStatus }}
      </div>

      <RouterLink to="/dashboard">
        <NeonButton class="!px-4 !py-2 !text-[11px]">관제 시스템 진입</NeonButton>
      </RouterLink>
    </div>
  </header>
</template>
