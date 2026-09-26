<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { RouterLink } from 'vue-router'

import { useRegionStore } from '@/stores/regionStore'
import { formatClock, formatDate, timeAgo } from '@/utils/format'
import { systemStatusMeta } from '@/utils/status'

const store = useRegionStore()
const clock = ref(new Date())
let timer: ReturnType<typeof setInterval> | undefined

onMounted(() => {
  timer = setInterval(() => {
    clock.value = new Date()
  }, 1000)
})
onUnmounted(() => {
  if (timer) clearInterval(timer)
})

const statusMeta = computed(() => systemStatusMeta(store.systemStatus))

const CONNECTION_META: Record<string, { label: string; color: string; dot: string }> = {
  idle: { label: 'CONNECTING', color: 'text-ink-500', dot: 'bg-ink-500' },
  connecting: { label: 'CONNECTING', color: 'text-ink-500', dot: 'bg-ink-500' },
  live: { label: 'LIVE DATA', color: 'text-green', dot: 'bg-green' },
  error: { label: 'OFFLINE', color: 'text-red', dot: 'bg-red' },
}
const connectionMeta = computed(() => CONNECTION_META[store.connectionStatus])
const connectionTitle = computed(() => {
  if (store.connectionStatus === 'error') return store.lastError ?? '백엔드에 연결할 수 없습니다'
  if (store.lastSyncedAt) return `마지막 동기화: ${timeAgo(store.lastSyncedAt.toISOString())}`
  return '백엔드 연결 대기 중'
})
const statusText = computed(() =>
  store.systemStatus === 'ONLINE'
    ? '모든 구역 정상 감시 중'
    : store.systemStatus === 'WARNING'
      ? `${store.warningCount + store.criticalCount}개 구역 이상 감지`
      : `긴급 — ${store.criticalCount}개 구역 위험 수준`,
)
</script>

<template>
  <header
    class="sticky top-0 z-30 border-b border-line bg-abyss/85 backdrop-blur-xl"
  >
    <div class="flex h-16 items-center gap-6 px-4 sm:px-6">
      <RouterLink to="/" class="flex shrink-0 items-center gap-2.5">
        <span class="relative flex h-8 w-8 items-center justify-center rounded-sm border border-cyan/50 bg-cyan/10">
          <span class="absolute inset-0 animate-pulse rounded-sm border border-cyan/30" />
          <svg viewBox="0 0 24 24" class="h-4 w-4 text-cyan" fill="none" stroke="currentColor" stroke-width="1.8">
            <path d="M3 12h4l2-7 4 14 2-7h6" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
        </span>
        <span class="font-display text-base font-bold tracking-[0.08em] text-ink-100">
          URBAN<span class="text-cyan text-glow-cyan">SIGNAL</span>
        </span>
      </RouterLink>

      <div class="hidden items-center gap-2 border-l border-line pl-6 font-mono text-xs text-ink-500 md:flex">
        <span>{{ formatDate(clock) }}</span>
        <span class="text-ink-700">/</span>
        <span class="tabular-nums text-ink-300">{{ formatClock(clock) }}</span>
      </div>

      <div class="relative ml-auto flex max-w-xs flex-1 items-center">
        <svg viewBox="0 0 24 24" class="pointer-events-none absolute left-3 h-4 w-4 text-ink-500" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="7" />
          <path d="m21 21-4.3-4.3" stroke-linecap="round" />
        </svg>
        <input
          v-model="store.searchQuery"
          type="text"
          placeholder="지역 검색..."
          class="w-full rounded-sm border border-line bg-panel/60 py-2 pl-9 pr-3 font-mono text-xs text-ink-100 placeholder:text-ink-700 outline-none transition-colors focus:border-cyan/50 focus:shadow-glow-cyan"
        />
      </div>

      <div
        class="hidden items-center gap-1.5 rounded-sm border border-current/20 px-2.5 py-1.5 font-mono text-[10px] sm:flex"
        :class="connectionMeta.color"
        :title="connectionTitle"
      >
        <span
          class="h-1.5 w-1.5 rounded-full"
          :class="[connectionMeta.dot, store.connectionStatus === 'connecting' || store.connectionStatus === 'idle' ? 'animate-pulse' : '']"
        />
        {{ connectionMeta.label }}
      </div>

      <div
        class="flex items-center gap-2 rounded-sm border px-3 py-1.5 font-mono text-xs"
        :class="[statusMeta.color, 'border-current/30']"
        :title="statusText"
      >
        <span class="relative flex h-2 w-2">
          <span class="absolute inline-flex h-full w-full animate-ping rounded-full opacity-70" :class="statusMeta.dot" />
          <span class="relative inline-flex h-2 w-2 rounded-full" :class="statusMeta.dot" />
        </span>
        <span class="hidden font-semibold tracking-widest sm:inline">{{ store.systemStatus }}</span>
      </div>
    </div>
  </header>
</template>
