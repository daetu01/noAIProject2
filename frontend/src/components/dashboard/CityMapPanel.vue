<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import * as L from 'leaflet'
import 'leaflet/dist/leaflet.css'

import { useRegionStore } from '@/stores/regionStore'
import { STATUS_HEX, STATUS_META } from '@/utils/status'
import { allAreaGeo, findAreaGeo } from '@/utils/geo'
import type { CityRegion } from '@/types/region'
import GlassPanel from '@/components/common/GlassPanel.vue'
import SectionHeading from '@/components/common/SectionHeading.vue'
import StatusBadge from '@/components/common/StatusBadge.vue'

const store = useRegionStore()
const mapEl = ref<HTMLDivElement>()

// Only tracked zones (default 5 + whatever the user has clicked) get a live
// marker/highlighted boundary — the other zones stay as faint background
// texture until selected, so the map doesn't turn into 121 pulsing dots.
const trackedRegions = computed(() => store.regions.filter((r) => store.trackedIds.has(r.id)))

let map: L.Map | undefined
const polygonLayers = new Map<string, L.Polygon>()
const markerLayers = new Map<string, L.Marker>()

function toLatLngs(ring: [number, number][]): L.LatLngExpression[] {
  return ring.map(([lon, lat]) => [lat, lon])
}

function markerHtml(region: CityRegion, isSelected: boolean): string {
  const dot = STATUS_META[region.status].dot
  const isAlert = region.status === 'critical' || region.status === 'warning'
  return `
    <div class="relative flex h-6 w-6 items-center justify-center">
      ${isAlert ? `<span class="absolute inline-flex h-full w-full rounded-full opacity-70 ${dot} animate-pulse-ring"></span>` : ''}
      <span class="relative inline-flex h-2.5 w-2.5 rounded-full ring-2 ring-void ${dot}"></span>
      ${isSelected ? `<span class="absolute h-9 w-9 rounded-full border ${STATUS_META[region.status].border}"></span>` : ''}
    </div>
  `
}

function buildDivIcon(region: CityRegion, isSelected: boolean) {
  return L.divIcon({
    html: markerHtml(region, isSelected),
    className: 'urban-signal-marker',
    iconSize: [24, 24],
    iconAnchor: [12, 12],
  })
}

function styleForZone(region: CityRegion, isSelected: boolean): L.PathOptions {
  const hex = STATUS_HEX[region.status]
  return {
    color: hex,
    weight: isSelected ? 3 : 1.6,
    opacity: 0.9,
    fillColor: hex,
    fillOpacity: isSelected ? 0.28 : 0.16,
    className: `zone-glow zone-glow--${region.status}`,
  }
}

function ensureAndRefreshLayers() {
  if (!map) return
  const selectedId = store.selectedRegion?.id

  for (const region of trackedRegions.value) {
    const isSelected = region.id === selectedId

    let polygon = polygonLayers.get(region.id)
    let marker = markerLayers.get(region.id)

    if (!polygon || !marker) {
      const geo = findAreaGeo(region.name)
      if (!geo) continue

      if (!polygon) {
        polygon = L.polygon(toLatLngs(geo.ring), { interactive: false }).addTo(map)
        polygonLayers.set(region.id, polygon)
      }
      if (!marker) {
        marker = L.marker([geo.lat, geo.lon], { icon: buildDivIcon(region, isSelected) })
          .addTo(map)
          .on('click', () => store.selectRegion(region.id))
        markerLayers.set(region.id, marker)
      }
    }

    polygon.setStyle(styleForZone(region, isSelected))
    marker.setIcon(buildDivIcon(region, isSelected))
  }
}

function findRegionByAreaName(name: string) {
  return store.regions.find((r) => r.name === name)
}

onMounted(() => {
  if (!mapEl.value) return

  const geoBounds = L.latLngBounds(
    allAreaGeo().flatMap((area) => toLatLngs(area.ring) as L.LatLngTuple[]),
  )

  map = L.map(mapEl.value, {
    zoomControl: true,
    attributionControl: true,
  }).fitBounds(geoBounds, { padding: [16, 16] })

  // Standard OSM tiles, no API key required — CSS-inverted into a dark theme below.
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    subdomains: 'abc',
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors',
  }).addTo(map)

  // all 121 Seoul open-data zones — faint texture, but clickable to inspect any of them
  for (const area of allAreaGeo()) {
    const polygon = L.polygon(toLatLngs(area.ring), {
      color: '#2ee6ff',
      weight: 1,
      opacity: 0.3,
      fillColor: '#2ee6ff',
      fillOpacity: 0.035,
      interactive: true,
      bubblingMouseEvents: false,
    })
      .addTo(map)
      .on('mouseover', () => polygon.setStyle({ fillOpacity: 0.12, opacity: 0.7 }))
      .on('mouseout', () => polygon.setStyle({ fillOpacity: 0.035, opacity: 0.3 }))
      .on('click', () => {
        const region = findRegionByAreaName(area.areaName)
        if (region) store.selectRegion(region.id)
      })
  }

  ensureAndRefreshLayers()
})

onUnmounted(() => {
  map?.remove()
  map = undefined
  polygonLayers.clear()
  markerLayers.clear()
})

watch(trackedRegions, () => ensureAndRefreshLayers())
watch(
  () => trackedRegions.value.map((r) => `${r.id}:${r.status}:${r.anomalyScore}`).join('|'),
  () => ensureAndRefreshLayers(),
)
watch(() => store.selectedRegionId, () => ensureAndRefreshLayers())
</script>

<template>
  <GlassPanel accent="cyan" no-padding class="relative h-full overflow-hidden">
    <template #header>
      <SectionHeading kicker="LIVE FEED · SEOUL METROPOLITAN AREA" title="관제 지도" />
      <span class="font-mono text-[10px] text-ink-500">
        LIVE {{ trackedRegions.length }} / SCAN {{ store.regions.length }} ZONES
      </span>
    </template>

    <div class="relative min-h-0 flex-1 overflow-hidden bg-abyss">
      <div ref="mapEl" class="absolute inset-0 z-0" />

      <!-- cyberpunk atmosphere layer, sits above the real tiles -->
      <div class="pointer-events-none absolute inset-0 z-[400] bg-radial-glow opacity-60" />
      <div class="pointer-events-none absolute inset-0 z-[400] scanline-overlay" />

      <div
        class="pointer-events-none absolute right-3 top-3 z-[400] rounded-sm border border-line bg-panel/70 px-2 py-1 font-mono text-[10px] text-ink-500 backdrop-blur-sm"
      >
        LIVE MAP · SEOUL OPEN DATA (121 ZONES)
      </div>

      <!-- selected region overlay card -->
      <Transition
        enter-active-class="transition duration-300 ease-out"
        enter-from-class="opacity-0 translate-y-2"
        enter-to-class="opacity-100 translate-y-0"
      >
        <div
          v-if="store.selectedRegion"
          :key="store.selectedRegion.id"
          class="absolute bottom-3 left-3 z-[400] w-64 rounded-sm border bg-panel/90 p-3 backdrop-blur-md sm:bottom-4 sm:left-4"
          :class="STATUS_META[store.selectedRegion.status].border"
        >
          <div class="flex items-center justify-between">
            <p class="font-display text-sm font-semibold text-ink-100">{{ store.selectedRegion.name }}</p>
            <StatusBadge :status="store.selectedRegion.status" size="sm" />
          </div>
          <p class="mt-1 font-mono text-[10px] uppercase tracking-wide text-ink-500">
            {{ store.selectedRegion.nameEn }}
          </p>
          <div class="mt-2 flex items-end gap-1.5 font-mono" :class="STATUS_META[store.selectedRegion.status].color">
            <span class="text-3xl font-bold leading-none text-glow-cyan tabular-nums">
              {{ store.selectedRegion.anomalyScore }}
            </span>
            <span class="pb-0.5 text-[10px] text-ink-500">ANOMALY IDX</span>
          </div>
        </div>
      </Transition>
    </div>
  </GlassPanel>
</template>

<style>
/* Leaflet controls restyled to match the console theme (global — Leaflet renders
   its controls outside this component's scoped root). */
.leaflet-container {
  background: #04060c;
  font-family: 'JetBrains Mono', ui-monospace, monospace;
}
.leaflet-control-zoom a {
  background: rgba(10, 16, 32, 0.85) !important;
  color: #7df3ff !important;
  border-color: rgba(120, 170, 255, 0.2) !important;
}
.leaflet-control-zoom a:hover {
  background: rgba(46, 230, 255, 0.15) !important;
}
.leaflet-control-attribution {
  background: rgba(10, 16, 32, 0.75) !important;
  color: #6c7aa0 !important;
  font-size: 9px !important;
}
.leaflet-control-attribution a {
  color: #a9bade !important;
}
.leaflet-tile-pane {
  filter: invert(94%) hue-rotate(190deg) brightness(0.8) contrast(1.15) saturate(0.15);
}
.urban-signal-marker {
  background: transparent;
  border: none;
}
path.zone-glow {
  filter: drop-shadow(0 0 3px currentColor);
}
.zone-glow--normal {
  color: #39f5a0;
}
.zone-glow--caution {
  color: #ffd23f;
}
.zone-glow--warning {
  color: #ffb640;
}
.zone-glow--critical {
  color: #ff4361;
}
</style>
