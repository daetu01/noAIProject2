<script setup lang="ts">
import { computed } from 'vue'

import { useRegionStore } from '@/stores/regionStore'
import { STATUS_META } from '@/utils/status'
import GlassPanel from '@/components/common/GlassPanel.vue'
import SectionHeading from '@/components/common/SectionHeading.vue'
import AnomalyTrendChart from '@/components/dashboard/AnomalyTrendChart.vue'
import MetricsCompareChart from '@/components/dashboard/MetricsCompareChart.vue'
import EventTimeline from '@/components/dashboard/EventTimeline.vue'

const store = useRegionStore()
const region = computed(() => store.selectedRegion)
const accentColor = computed(() => {
  const map: Record<string, string> = {
    normal: '#39f5a0',
    caution: '#ffd23f',
    warning: '#ffb640',
    critical: '#ff4361',
  }
  return region.value ? map[region.value.status] : '#2ee6ff'
})
</script>

<template>
  <GlassPanel v-if="region" accent="blue" no-padding class="overflow-hidden">
    <template #header>
      <SectionHeading kicker="24H TELEMETRY" :title="`${region.name} · 추이 분석`" />
      <span class="font-mono text-[10px] uppercase tracking-widest" :class="STATUS_META[region.status].color">
        {{ STATUS_META[region.status].label }}
      </span>
    </template>

    <div v-if="!region.hasLiveData" class="flex h-48 items-center justify-center">
      <p class="font-mono text-xs text-ink-700">실시간 데이터 조회 중...</p>
    </div>

    <template v-else>
      <div class="grid grid-cols-1 gap-px bg-line lg:grid-cols-2">
        <div class="min-w-0 bg-panel/70 p-4">
          <p class="mb-2 font-mono text-[10px] uppercase tracking-widest text-ink-500">Anomaly Score (24H)</p>
          <div class="h-48">
            <AnomalyTrendChart :history="region.history" :color="accentColor" />
          </div>
        </div>
        <div class="min-w-0 bg-panel/70 p-4">
          <p class="mb-2 font-mono text-[10px] uppercase tracking-widest text-ink-500">인구 / 교통 속도 비교</p>
          <div class="h-48">
            <MetricsCompareChart :history="region.history" />
          </div>
        </div>
      </div>
    </template>

    <div class="border-t border-line bg-panel/70 p-4">
      <p class="mb-1 font-mono text-[10px] uppercase tracking-widest text-ink-500">전체 구역 이벤트 타임라인</p>
      <EventTimeline />
    </div>
  </GlassPanel>
</template>
