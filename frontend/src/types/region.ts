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

export interface CityRegion {
  id: string
  name: string
  nameEn: string
  /** placeholder-map position, percentage of container (0-100) */
  position: { x: number; y: number }
  anomalyScore: number
  status: AnomalyStatus
  population: number
  populationDelta: number
  trafficSpeed: number
  trafficDelta: number
  temperature: number
  /** Backend only exposes a precipitation status message (PCP_MSG), not a numeric mm value. */
  weatherMessage: string
  causeSummary: string
  updatedAt: string
  history: RegionMetricPoint[]
  events: RegionEvent[]
  /** false until the first successful live fetch for this zone — all fields above are placeholders until then. */
  hasLiveData: boolean
}

export type SystemStatus = 'ONLINE' | 'WARNING' | 'CRITICAL'
