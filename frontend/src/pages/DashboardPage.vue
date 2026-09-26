<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'

import { useRegionStore } from '@/stores/regionStore'
import BackgroundFx from '@/components/common/BackgroundFx.vue'
import HeaderBar from '@/components/layout/HeaderBar.vue'
import RegionSidebar from '@/components/dashboard/RegionSidebar.vue'
import CityMapPanel from '@/components/dashboard/CityMapPanel.vue'
import DetailPanel from '@/components/dashboard/DetailPanel.vue'
import MetricsChartPanel from '@/components/dashboard/MetricsChartPanel.vue'

const store = useRegionStore()
let timer: ReturnType<typeof setInterval> | undefined

onMounted(() => {
  store.initCatalog()
  timer = setInterval(() => store.syncLiveData(), 20000)
})
onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<template>
  <div class="flex min-h-screen flex-col">
    <BackgroundFx />
    <HeaderBar />

    <div class="grid flex-1 grid-cols-1 lg:grid-cols-[260px_1fr_340px] xl:grid-cols-[280px_1fr_380px]">
      <RegionSidebar class="order-2 lg:order-1 lg:h-[calc(100vh-4rem)] lg:sticky lg:top-16" />

      <main class="order-1 flex min-w-0 flex-col gap-4 p-4 lg:order-2">
        <div class="h-[420px] lg:h-[46vh]">
          <CityMapPanel />
        </div>
        <MetricsChartPanel />
      </main>

      <DetailPanel class="order-3 lg:h-[calc(100vh-4rem)] lg:sticky lg:top-16" />
    </div>
  </div>
</template>
