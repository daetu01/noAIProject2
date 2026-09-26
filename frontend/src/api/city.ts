import { apiGet, apiPost } from '@/api/http'
import type { AreaSummary, SeoulDataResponse } from '@/types/api'

export function fetchAreaCatalog(): Promise<AreaSummary[]> {
  return apiGet<AreaSummary[]>('/city/area')
}

export function fetchAreaObservation(areaName: string): Promise<SeoulDataResponse> {
  return apiPost<SeoulDataResponse>('/seoul/area', { areaName })
}
