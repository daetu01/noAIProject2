import { computed, ref } from 'vue'
import { defineStore } from 'pinia'

import { fetchAreaCatalog, fetchAreaObservation } from '@/api/city'
import { ApiError } from '@/api/http'
import { createBlankRegion, resolveStatus } from '@/data/regionFactory'
import type { CityRegion, RegionEvent, SystemStatus } from '@/types/region'
import { buildCauseSummary, computeAnomalyScore, percentDelta } from '@/utils/liveMapping'

export type ConnectionStatus = 'idle' | 'connecting' | 'live' | 'error'
export type CatalogStatus = 'idle' | 'loading' | 'ready' | 'error'

const HISTORY_LIMIT = 24
const EVENT_LIMIT = 8

// Pre-tracked on load so the dashboard isn't empty on first paint — every other
// zone is fetched on demand the moment it's selected.
const DEFAULT_TRACKED_NAMES = ['강남역', '홍대 관광특구', '여의도', '광화문·덕수궁', '잠실 관광특구']

export const useRegionStore = defineStore('region', () => {
  const regions = ref<CityRegion[]>([])
  const selectedRegionId = ref<string>('')
  const searchQuery = ref('')

  const catalogStatus = ref<CatalogStatus>('idle')
  const trackedIds = ref<Set<string>>(new Set())

  const connectionStatus = ref<ConnectionStatus>('idle')
  const lastError = ref<string | null>(null)
  const lastSyncedAt = ref<Date | null>(null)
  const isSyncing = ref(false)

  const liveRegions = computed(() => regions.value.filter((r) => r.hasLiveData))

  const sortedRegions = computed(() =>
    [...regions.value].sort((a, b) => {
      if (a.hasLiveData !== b.hasLiveData) return a.hasLiveData ? -1 : 1
      if (a.hasLiveData) return b.anomalyScore - a.anomalyScore
      return a.name.localeCompare(b.name, 'ko')
    }),
  )

  const filteredRegions = computed(() => {
    const query = searchQuery.value.trim().toLowerCase()
    if (!query) return sortedRegions.value
    return sortedRegions.value.filter(
      (region) =>
        region.name.toLowerCase().includes(query) || region.nameEn.toLowerCase().includes(query),
    )
  })

  const selectedRegion = computed(() =>
    regions.value.find((region) => region.id === selectedRegionId.value),
  )

  const peakAnomalyScore = computed(() =>
    liveRegions.value.reduce((max, region) => Math.max(max, region.anomalyScore), 0),
  )

  const systemStatus = computed<SystemStatus>(() => {
    if (peakAnomalyScore.value >= 80) return 'CRITICAL'
    if (peakAnomalyScore.value >= 60) return 'WARNING'
    return 'ONLINE'
  })

  const criticalCount = computed(
    () => liveRegions.value.filter((region) => region.status === 'critical').length,
  )
  const warningCount = computed(
    () => liveRegions.value.filter((region) => region.status === 'warning').length,
  )

  async function initCatalog() {
    if (catalogStatus.value !== 'idle') return
    catalogStatus.value = 'loading'
    try {
      const areas = await fetchAreaCatalog()
      regions.value = areas.map(createBlankRegion)

      const defaults = regions.value.filter((r) => DEFAULT_TRACKED_NAMES.includes(r.name))
      for (const region of defaults) trackedIds.value.add(region.id)

      selectedRegionId.value = defaults[0]?.id ?? regions.value[0]?.id ?? ''
      catalogStatus.value = 'ready'

      await syncLiveData()
    } catch {
      catalogStatus.value = 'error'
    }
  }

  function selectRegion(id: string) {
    selectedRegionId.value = id
    const region = regions.value.find((r) => r.id === id)
    if (region && !trackedIds.value.has(id)) {
      trackedIds.value.add(id)
      fetchOne(region)
    }
  }

  function applyObservation(
    region: CityRegion,
    data: Awaited<ReturnType<typeof fetchAreaObservation>>,
  ) {
    const cityData = data.CITYDATA
    const live = cityData.LIVE_PPLTN_STTS[0]
    const road = cityData.ROAD_TRAFFIC_STTS.AVG_ROAD_DATA
    const weather = cityData.WEATHER_STTS[0]
    if (!live || !road || !weather) return

    const population = Math.round((live.AREA_PPLTN_MIN + live.AREA_PPLTN_MAX) / 2)
    const trafficSpeed = road.ROAD_TRAFFIC_SPD
    const anomalyScore = computeAnomalyScore(population, trafficSpeed)
    const status = resolveStatus(anomalyScore)
    const previousStatus = region.status
    const wasLive = region.hasLiveData
    const now = new Date().toISOString()

    const nextHistory = [
      ...region.history,
      { timestamp: now, anomalyScore, population, trafficSpeed, temperature: weather.TEMP },
    ].slice(-HISTORY_LIMIT)

    const nextEvents =
      !wasLive || status === previousStatus
        ? region.events
        : ([
            {
              id: `${region.id}-${Date.now()}`,
              timestamp: now,
              level: status === 'critical' ? 'critical' : status === 'normal' ? 'info' : 'warning',
              message: `이상징후 상태 전이: ${previousStatus.toUpperCase()} → ${status.toUpperCase()}`,
            } satisfies RegionEvent,
            ...region.events,
          ].slice(0, EVENT_LIMIT))

    Object.assign(region, {
      anomalyScore,
      status,
      population,
      populationDelta: wasLive ? percentDelta(population, region.population) : 0,
      trafficSpeed,
      trafficDelta: wasLive ? percentDelta(trafficSpeed, region.trafficSpeed) : 0,
      temperature: weather.TEMP,
      weatherMessage: weather.PCP_MSG,
      causeSummary: buildCauseSummary(status, road.ROAD_MSG, weather.PCP_MSG),
      updatedAt: now,
      history: nextHistory,
      events: nextEvents,
      hasLiveData: true,
    })
  }

  async function fetchOne(region: CityRegion) {
    try {
      const data = await fetchAreaObservation(region.name)
      applyObservation(region, data)
      connectionStatus.value = 'live'
      lastSyncedAt.value = new Date()
    } catch (err) {
      lastError.value = err instanceof ApiError ? `${region.name}: ${err.message}` : `${region.name}: 알 수 없는 오류`
    }
  }

  async function syncLiveData() {
    if (isSyncing.value) return
    isSyncing.value = true
    if (connectionStatus.value === 'idle') connectionStatus.value = 'connecting'

    const targets = regions.value.filter((r) => trackedIds.value.has(r.id))
    const results = await Promise.allSettled(
      targets.map((region) => fetchAreaObservation(region.name).then((data) => ({ region, data }))),
    )

    let successCount = 0
    let firstError: string | null = null

    results.forEach((result, index) => {
      const region = targets[index]
      if (result.status === 'rejected') {
        firstError =
          firstError ??
          (result.reason instanceof ApiError
            ? `${region.name}: ${result.reason.message}`
            : `${region.name}: 알 수 없는 오류`)
        return
      }
      successCount += 1
      applyObservation(region, result.value.data)
    })

    if (successCount > 0) {
      connectionStatus.value = 'live'
      lastSyncedAt.value = new Date()
      lastError.value = firstError
    } else if (targets.length > 0) {
      connectionStatus.value = 'error'
      lastError.value = firstError ?? '데이터를 가져오지 못했습니다'
    }

    isSyncing.value = false
  }

  return {
    regions,
    selectedRegionId,
    searchQuery,
    catalogStatus,
    trackedIds,
    connectionStatus,
    lastError,
    lastSyncedAt,
    isSyncing,
    liveRegions,
    sortedRegions,
    filteredRegions,
    selectedRegion,
    peakAnomalyScore,
    systemStatus,
    criticalCount,
    warningCount,
    initCatalog,
    selectRegion,
    syncLiveData,
  }
})
