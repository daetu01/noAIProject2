import { onMounted, onUnmounted, shallowRef, watch, type Ref } from 'vue'
import {
  Map as MaplibreMap,
  LngLatBounds,
  NavigationControl,
  AttributionControl,
  type MapLayerMouseEvent,
  type ExpressionSpecification,
} from 'maplibre-gl'
import 'maplibre-gl/dist/maplibre-gl.css'

import seoulAreas from '@/data/seoul-areas.geojson'
import { applyDarkTheme } from '@/composables/urbanMapTheme'

export interface UrbanMapRegion {
  id: string
  anomalyScore: number
  hasLiveData: boolean
}

export interface HoverInfo {
  areaName: string
  x: number
  y: number
}

const SOURCE_ID = 'seoul-areas'
const FILL_LAYER = 'seoul-areas-fill'
const LINE_LAYER = 'seoul-areas-line'
const STYLE_URL = 'https://tiles.openfreemap.org/styles/liberty'

// anomalyScore -> color/opacity ramps (0-30 near-transparent cyan, 31-60
// cyan/teal, 61-80 amber, 81-100 red/magenta) — driven entirely by
// feature-state so live updates never touch the GeoJSON source itself.
// MapLibre expressions are just nested arrays; the official types are a
// deeply-discriminated union that's impractical to hand-author against, so
// we build them as plain data and cast once at the point of use.
type Expr = ExpressionSpecification
const expr = (value: unknown) => value as Expr

const SCORE = ['coalesce', ['feature-state', 'anomalyScore'], 0]
const FILL_COLOR_EXPR = expr([
  'interpolate',
  ['linear'],
  SCORE,
  0,
  '#2ee6ff',
  30,
  '#2ee6ff',
  60,
  '#14c9c9',
  80,
  '#ffb640',
  100,
  '#ff3ec9',
])
const FILL_OPACITY_BASE = ['interpolate', ['linear'], SCORE, 0, 0.04, 30, 0.1, 60, 0.18, 80, 0.32, 100, 0.48]
const FILL_OPACITY_EXPR = expr([
  'case',
  ['boolean', ['feature-state', 'hover'], false],
  ['min', 0.6, ['+', FILL_OPACITY_BASE, 0.15]],
  FILL_OPACITY_BASE,
])
const LINE_COLOR_EXPR = expr([
  'interpolate',
  ['linear'],
  SCORE,
  0,
  '#2ee6ff',
  30,
  '#2ee6ff',
  60,
  '#14c9c9',
  80,
  '#ffb640',
  100,
  '#ff4361',
])
const LINE_WIDTH_EXPR = expr([
  'case',
  ['boolean', ['feature-state', 'selected'], false],
  ['+', 2, ['*', 0.8, ['coalesce', ['feature-state', 'pulse'], 0]]],
  ['boolean', ['feature-state', 'hover'], false],
  1.8,
  0.7,
])
const LINE_OPACITY_EXPR = expr([
  'case',
  ['boolean', ['feature-state', 'selected'], false],
  1,
  ['boolean', ['feature-state', 'hover'], false],
  0.9,
  0.45,
])

export function useUrbanMap(options: {
  container: Ref<HTMLElement | undefined>
  regions: Ref<UrbanMapRegion[]>
  selectedId: Ref<string>
  onSelect: (id: string) => void
}) {
  const { container, regions, selectedId, onSelect } = options

  const map = shallowRef<MaplibreMap>()
  const mapReady = shallowRef(false)
  const hoverInfo = shallowRef<HoverInfo | null>(null)

  let hoveredId: string | null = null
  let pulseTimer: ReturnType<typeof setInterval> | undefined
  let pulseTick = 0

  function fitToSeoul(m: MaplibreMap) {
    const bounds = new LngLatBounds()
    for (const feature of seoulAreas.features) {
      for (const [lon, lat] of feature.geometry.coordinates[0]) {
        bounds.extend([lon, lat])
      }
    }
    m.fitBounds(bounds, { padding: 32, duration: 0 })
  }

  function stopPulse() {
    if (pulseTimer) clearInterval(pulseTimer)
    pulseTimer = undefined
  }

  function startPulse(m: MaplibreMap, id: string) {
    stopPulse()
    pulseTick = 0
    pulseTimer = setInterval(() => {
      pulseTick += 1
      const pulse = (Math.sin(pulseTick / 6) + 1) / 2 // 0..1, gentle — not a strobe
      m.setFeatureState({ source: SOURCE_ID, id }, { selected: true, pulse })
    }, 80)
  }

  function applySelection(m: MaplibreMap, id: string, previousId: string, flyTo: boolean) {
    if (previousId && previousId !== id) {
      m.setFeatureState({ source: SOURCE_ID, id: previousId }, { selected: false, pulse: 0 })
    }
    if (!id) {
      stopPulse()
      return
    }
    startPulse(m, id)
    if (!flyTo) return

    const feature = seoulAreas.features.find((f) => f.id === id)
    if (feature) {
      m.flyTo({
        center: [feature.properties.lon, feature.properties.lat],
        zoom: Math.max(m.getZoom(), 13.5),
        duration: 700,
        essential: true,
      })
    }
  }

  function syncFeatureStates(m: MaplibreMap, list: UrbanMapRegion[]) {
    for (const region of list) {
      m.setFeatureState(
        { source: SOURCE_ID, id: region.id },
        { anomalyScore: region.anomalyScore, live: region.hasLiveData },
      )
    }
  }

  onMounted(() => {
    if (!container.value) return

    const m = new MaplibreMap({
      container: container.value,
      style: STYLE_URL,
      center: [126.978, 37.5665],
      zoom: 10.5,
      minZoom: 9,
      maxZoom: 16,
      pitchWithRotate: false,
      dragRotate: false,
      touchPitch: false,
      maxPitch: 0,
      attributionControl: false,
    })
    map.value = m

    m.addControl(new NavigationControl({ showCompass: false }), 'top-left')
    m.addControl(new AttributionControl({ compact: true }), 'bottom-right')

    m.on('load', () => {
      applyDarkTheme(m)
      fitToSeoul(m)

      m.addSource(SOURCE_ID, {
        type: 'geojson',
        data: seoulAreas as unknown as GeoJSON.FeatureCollection,
        promoteId: 'areaId',
      })

      m.addLayer({
        id: FILL_LAYER,
        type: 'fill',
        source: SOURCE_ID,
        paint: {
          'fill-color': FILL_COLOR_EXPR,
          'fill-opacity': FILL_OPACITY_EXPR,
        },
      })

      m.addLayer({
        id: LINE_LAYER,
        type: 'line',
        source: SOURCE_ID,
        paint: {
          'line-color': LINE_COLOR_EXPR,
          'line-width': LINE_WIDTH_EXPR,
          'line-opacity': LINE_OPACITY_EXPR,
        },
      })

      syncFeatureStates(m, regions.value)
      // highlight the initial selection without flying — the map should open
      // on the full-Seoul view; flyTo only kicks in for user-driven changes.
      if (selectedId.value) applySelection(m, selectedId.value, '', false)

      m.on('mousemove', FILL_LAYER, (e: MapLayerMouseEvent) => {
        const feature = e.features?.[0]
        if (!feature) return
        const id = String(feature.id)
        if (id !== hoveredId) {
          if (hoveredId) m.setFeatureState({ source: SOURCE_ID, id: hoveredId }, { hover: false })
          hoveredId = id
          m.setFeatureState({ source: SOURCE_ID, id }, { hover: true })
        }
        hoverInfo.value = {
          areaName: (feature.properties as { areaName: string }).areaName,
          x: e.point.x,
          y: e.point.y,
        }
        m.getCanvas().style.cursor = 'pointer'
      })

      m.on('mouseleave', FILL_LAYER, () => {
        if (hoveredId) m.setFeatureState({ source: SOURCE_ID, id: hoveredId }, { hover: false })
        hoveredId = null
        hoverInfo.value = null
        m.getCanvas().style.cursor = ''
      })

      m.on('click', FILL_LAYER, (e: MapLayerMouseEvent) => {
        const feature = e.features?.[0]
        if (!feature) return
        onSelect(String(feature.id))
      })

      mapReady.value = true
    })
  })

  onUnmounted(() => {
    stopPulse()
    map.value?.remove()
    map.value = undefined
  })

  watch(
    regions,
    (list) => {
      if (map.value && mapReady.value) syncFeatureStates(map.value, list)
    },
    { deep: true },
  )

  let previousSelected = ''
  watch(selectedId, (id) => {
    if (map.value && mapReady.value) applySelection(map.value, id, previousSelected, true)
    previousSelected = id
  })

  return { map, mapReady, hoverInfo }
}
