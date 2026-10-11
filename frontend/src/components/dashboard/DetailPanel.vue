<script setup lang="ts">
import { computed } from 'vue'

import { useRegionStore } from '@/stores/regionStore'
import { STATUS_META } from '@/utils/status'
import { congestionMeta } from '@/utils/congestion'
import { formatNumber, timeAgo } from '@/utils/format'
import GlassPanel from '@/components/common/GlassPanel.vue'
import SectionHeading from '@/components/common/SectionHeading.vue'
import StatusBadge from '@/components/common/StatusBadge.vue'
import MetricStat from '@/components/common/MetricStat.vue'
import EventLogTerminal from '@/components/dashboard/EventLogTerminal.vue'

const store = useRegionStore()
const region = computed(() => store.selectedRegion)
</script>

<template>
  <GlassPanel v-if="region" :accent="region.status === 'normal' ? 'green' : region.status === 'caution' ? 'amber' : region.status === 'warning' ? 'amber' : 'red'" no-padding class="flex h-full flex-col overflow-hidden">
    <template #header>
      <SectionHeading kicker="ZONE DETAIL" :title="region.name" />
      <StatusBadge :status="region.status" />
    </template>

    <div
      v-if="!region.hasLiveData"
      class="flex min-h-0 flex-1 flex-col items-center justify-center gap-3 p-6 text-center"
    >
      <span class="relative flex h-10 w-10 items-center justify-center">
        <span class="absolute inline-flex h-full w-full animate-ping rounded-full bg-cyan/40" />
        <span class="relative inline-flex h-3 w-3 rounded-full bg-cyan" />
      </span>
      <p class="font-mono text-xs uppercase tracking-widest text-ink-500">실시간 데이터 조회 중...</p>
      <p class="font-mono text-[10px] text-ink-700">POST /api/seoul/area · {{ region.name }}</p>
    </div>

    <div v-else class="min-h-0 flex-1 space-y-5 overflow-y-auto px-4 py-4">
      <!-- HUD anomaly score -->
      <div class="rounded-sm border p-4 text-center" :class="STATUS_META[region.status].border" style="background: radial-gradient(circle at 50% 0%, rgba(46,230,255,0.06), transparent 70%)">
        <p class="font-mono text-[10px] uppercase tracking-[0.25em] text-ink-500">ANOMALY SCORE</p>
        <p
          class="mt-1 font-mono text-6xl font-bold tabular-nums leading-none"
          :class="STATUS_META[region.status].color"
          :style="{ textShadow: '0 0 22px currentColor' }"
        >
          {{ region.anomalyScore }}
        </p>
        <div class="mx-auto mt-3 h-1.5 w-full max-w-[14rem] overflow-hidden rounded-full bg-panel-alt">
          <div
            class="h-full rounded-full transition-all duration-700"
            :class="STATUS_META[region.status].dot"
            :style="{ width: `${region.anomalyScore}%` }"
          />
        </div>
        <p class="mt-2 font-mono text-[10px] text-ink-500">UPDATED {{ timeAgo(region.updatedAt) }}</p>
      </div>

      <!-- population congestion -->
      <div
        class="flex items-center gap-2 rounded-sm border border-line bg-panel-alt/60 px-3 py-2"
        :title="region.congestionMessage"
      >
        <span class="h-2 w-2 shrink-0 rounded-full" :class="congestionMeta(region.congestionLevel).dot" />
        <span class="font-mono text-[10px] uppercase tracking-widest text-ink-500">혼잡도</span>
        <span class="font-mono text-xs font-bold" :class="congestionMeta(region.congestionLevel).color">
          {{ region.congestionLevel }}
        </span>
        <span class="truncate text-[11px] text-ink-500">{{ region.congestionMessage }}</span>
      </div>

      <!-- key metrics grid -->
      <div class="grid grid-cols-2 gap-2.5">
        <MetricStat
          label="실시간 인구"
          :value="formatNumber(region.population)"
          :delta="region.populationDelta"
          delta-suffix="%"
        />
        <MetricStat
          label="교통 속도"
          :value="`${region.trafficSpeed.toFixed(1)} km/h`"
          :delta="region.trafficDelta"
          delta-suffix="%"
        />
        <MetricStat label="기온" :value="`${region.temperature.toFixed(1)}°C`" />
        <MetricStat label="강수 상태" :value="region.weatherMessage" />
      </div>

      <!-- cause summary -->
      <div>
        <SectionHeading kicker="AI ANALYSIS" title="예상 원인" accent="magenta" />
        <p class="mt-2 rounded-sm border border-magenta/20 bg-magenta/5 p-3 text-xs leading-relaxed text-ink-300">
          {{ region.causeSummary }}
        </p>
      </div>

      <!-- event log -->
      <div>
        <SectionHeading kicker="EVENT STREAM" title="최근 감지 로그" accent="green" />
        <div class="mt-2">
          <EventLogTerminal :events="region.events" />
        </div>
      </div>
    </div>
  </GlassPanel>
</template>
