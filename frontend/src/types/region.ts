export type AnomalyStatus = 'normal' | 'caution' | 'warning' | 'critical'

export type EventLevel = 'info' | 'warning' | 'critical'

export interface RegionMetricPoint {
  /** ISO timestamp for this sample */
  timestamp: string
  anomalyScore: number
  population: number
  trafficSpeed: number
  temperature: number
}

export interface RegionEvent {
  id: string
  timestamp: string
  level: EventLevel
  message: string
}

/** Real Seoul Open Data population congestion levels. */
export type CongestionLevel = '여유' | '보통' | '약간 붐빔' | '붐빔' | string

export interface PopulationForecastPoint {
  /** ISO timestamp, converted from the backend's "yyyy-MM-dd HH:mm" */
  time: string
  congestionLevel: CongestionLevel
  populationMin: number
  populationMax: number
}

export interface CityRegion {
  id: string
  name: string
  nameEn: string
  anomalyScore: number
  status: AnomalyStatus
  population: number
  populationDelta: number
  trafficSpeed: number
  trafficDelta: number
  temperature: number
  /** Backend only exposes a precipitation status message (PCP_MSG), not a numeric mm value. */
  weatherMessage: string
  congestionLevel: CongestionLevel
  congestionMessage: string
  populationForecast: PopulationForecastPoint[]
  causeSummary: string
  updatedAt: string
  history: RegionMetricPoint[]
  events: RegionEvent[]
  /** false until the first successful live fetch for this zone — all fields above are placeholders until then. */
  hasLiveData: boolean
}

export type SystemStatus = 'ONLINE' | 'WARNING' | 'CRITICAL'
