<script setup lang="ts">
import { useRegionStore } from '@/stores/regionStore'
import { STATUS_META } from '@/utils/status'
import StatusBadge from '@/components/common/StatusBadge.vue'
import SectionHeading from '@/components/common/SectionHeading.vue'

const store = useRegionStore()
</script>

<template>
  <aside class="flex h-full flex-col border-r border-line bg-panel/40">
    <div class="flex items-center justify-between border-b border-line px-4 py-3.5">
      <SectionHeading kicker="MONITORED ZONES" title="지역 목록" />
      <span class="font-mono text-[10px] text-ink-500">
        LIVE {{ store.liveRegions.length }} / {{ store.regions.length }}
      </span>
    </div>

    <div class="flex-1 space-y-1.5 overflow-y-auto px-2.5 py-2.5">
      <button
        v-for="region in store.filteredRegions"
        :key="region.id"
        type="button"
        class="group relative w-full rounded-sm border px-3 py-2.5 text-left transition-all duration-200"
        :class="[
          region.id === store.selectedRegion?.id
            ? [STATUS_META[region.status].border, 'bg-panel-raised']
            : 'border-transparent hover:border-line-strong hover:bg-panel-raised/60',
          !region.hasLiveData && 'opacity-60',
        ]"
        @click="store.selectRegion(region.id)"
      >
        <span
          v-if="region.id === store.selectedRegion?.id"
          class="absolute inset-y-1 left-0 w-0.5"
          :class="region.hasLiveData ? STATUS_META[region.status].dot : 'bg-ink-500'"
        />
        <div class="flex items-center justify-between gap-2">
          <div class="min-w-0">
            <p class="truncate text-sm font-medium text-ink-100">{{ region.name }}</p>
            <p class="truncate font-mono text-[10px] uppercase tracking-wide text-ink-500">
              {{ region.nameEn }}
            </p>
          </div>
          <span
            v-if="region.hasLiveData"
            class="shrink-0 font-mono text-lg font-bold tabular-nums"
            :class="STATUS_META[region.status].color"
          >
            {{ region.anomalyScore }}
          </span>
          <span v-else class="shrink-0 font-mono text-[10px] text-ink-700">SCAN</span>
        </div>

        <div v-if="region.hasLiveData" class="mt-2 flex items-center justify-between gap-2">
          <StatusBadge :status="region.status" size="sm" />
          <div class="h-1 flex-1 overflow-hidden rounded-full bg-panel-alt">
            <div
              class="h-full rounded-full transition-all duration-700"
              :class="STATUS_META[region.status].dot"
              :style="{ width: `${region.anomalyScore}%` }"
            />
          </div>
        </div>
        <p v-else class="mt-1.5 font-mono text-[10px] text-ink-700">클릭하여 실시간 조회</p>
      </button>

      <p v-if="store.catalogStatus === 'loading'" class="px-3 py-6 text-center font-mono text-xs text-ink-500">
        지역 목록 불러오는 중...
      </p>
      <p
        v-else-if="!store.filteredRegions.length"
        class="px-3 py-6 text-center font-mono text-xs text-ink-500"
      >
        검색 결과가 없습니다
      </p>
    </div>
  </aside>
</template>
