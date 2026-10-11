<script setup lang="ts">
import { computed, ref } from 'vue'

import { useRegionStore } from '@/stores/regionStore'
import { useUrbanMap } from '@/composables/useUrbanMap'
import MapHud from '@/components/map/MapHud.vue'

const store = useRegionStore()
const mapContainer = ref<HTMLDivElement>()

const mapRegions = computed(() =>
  store.regions.map((r) => ({ id: r.id, anomalyScore: r.anomalyScore, hasLiveData: r.hasLiveData })),
)
const selectedId = computed(() => store.selectedRegionId)

const { hoverInfo } = useUrbanMap({
  container: mapContainer,
  regions: mapRegions,
  selectedId,
  onSelect: (id) => store.selectRegion(id),
})
</script>

<template>
  <div class="relative h-full w-full overflow-hidden">
    <!--
      Inline style (not Tailwind's `absolute inset-0`) is deliberate: maplibre-gl.css
      sets `.maplibregl-map { position: relative }` on this exact element, and since
      it loads after our Tailwind utilities, it was winning the cascade and collapsing
      this container to 0 height. An inline style always wins over any stylesheet rule
      without !important, so it's immune to that load-order fight.
    -->
    <div ref="mapContainer" style="position: absolute; inset: 0" />

    <MapHud
      :selected-name="store.selectedRegion?.name"
      :system-status="store.systemStatus"
      :connection-status="store.connectionStatus"
      :last-synced-at="store.lastSyncedAt"
    />

    <!-- hover tooltip: follows the cursor, shows the area name only -->
    <div
      v-if="hoverInfo"
      class="pointer-events-none absolute z-10 -translate-x-1/2 -translate-y-[calc(100%+10px)] whitespace-nowrap rounded-sm border border-cyan/30 bg-panel/90 px-2 py-1 font-mono text-[11px] text-cyan backdrop-blur-sm"
      :style="{ left: `${hoverInfo.x}px`, top: `${hoverInfo.y}px` }"
    >
      {{ hoverInfo.areaName }}
    </div>
  </div>
</template>

<style>
.maplibregl-ctrl-attrib {
  background: rgba(10, 16, 32, 0.7) !important;
  color: #6c7aa0 !important;
}
.maplibregl-ctrl-attrib a {
  color: #a9bade !important;
}
.maplibregl-ctrl-group {
  background: rgba(10, 16, 32, 0.85) !important;
  border: 1px solid rgba(120, 170, 255, 0.2) !important;
  box-shadow: none !important;
}
.maplibregl-ctrl-group button {
  background: transparent !important;
}
.maplibregl-ctrl-group button + button {
  border-top: 1px solid rgba(120, 170, 255, 0.15) !important;
}
.maplibregl-ctrl-icon {
  filter: invert(70%) sepia(70%) saturate(1000%) hue-rotate(150deg) brightness(1.1);
}
</style>
