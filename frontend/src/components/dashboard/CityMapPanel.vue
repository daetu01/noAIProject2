<script setup lang="ts">
import { useRegionStore } from '@/stores/regionStore'
import GlassPanel from '@/components/common/GlassPanel.vue'
import SectionHeading from '@/components/common/SectionHeading.vue'
import UrbanMap from '@/components/map/UrbanMap.vue'

const store = useRegionStore()
</script>

<template>
  <GlassPanel accent="cyan" no-padding class="relative h-full overflow-hidden">
    <template #header>
      <SectionHeading kicker="LIVE FEED · SEOUL METROPOLITAN AREA" title="관제 지도" />
      <span class="font-mono text-[10px] text-ink-500">
        LIVE {{ store.liveRegions.length }} / SCAN {{ store.regions.length }} ZONES
      </span>
    </template>

    <div class="relative min-h-0 flex-1 overflow-hidden bg-abyss">
      <UrbanMap />
    </div>
  </GlassPanel>
</template>
