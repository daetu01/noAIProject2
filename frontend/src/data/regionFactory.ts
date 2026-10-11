import type { AnomalyStatus, CityRegion } from '@/types/region'
import type { AreaSummary } from '@/types/api'

export function resolveStatus(anomalyScore: number): AnomalyStatus {
  if (anomalyScore >= 80) return 'critical'
  if (anomalyScore >= 60) return 'warning'
  if (anomalyScore >= 35) return 'caution'
  return 'normal'
}

/** Builds an unscanned placeholder region from the /api/city/area catalog entry — real values arrive on first live fetch. */
export function createBlankRegion(area: AreaSummary): CityRegion {
  return {
    id: area.areaId,
    name: area.areaName,
    nameEn: area.englishName ?? area.areaName,
    anomalyScore: 0,
    status: 'normal',
    population: 0,
    populationDelta: 0,
    trafficSpeed: 0,
    trafficDelta: 0,
    temperature: 0,
    weatherMessage: '—',
    congestionLevel: '—',
    congestionMessage: '',
    populationForecast: [],
    causeSummary: '아직 조회되지 않은 구역입니다. 지역을 선택하면 실시간 데이터를 불러옵니다.',
    updatedAt: '',
    history: [],
    events: [],
    hasLiveData: false,
  }
}
