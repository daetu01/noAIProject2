// Mirrors the backend DTOs exactly (see monitoring/src/main/java/.../observation/dto, city/dto).
// Field names keep the backend's raw JSON keys (Seoul Open API convention) where the
// backend passes them through untouched.

export type AreaCategory =
  | 'TOURIST_ZONE'
  | 'CULTURAL_HERITAGE'
  | 'DENSE_POPULATION_AREA'
  | 'DEVELOPED_COMMERCIAL_AREA'
  | 'PARK'

export interface AreaSummary {
  id: number
  category: AreaCategory
  areaId: string
  areaName: string
  englishName: string | null
}

export interface LivePopulationDto {
  AREA_PPLTN_MIN: number
  AREA_PPLTN_MAX: number
}

export interface AvgRoadDataDto {
  ROAD_MSG: string
  ROAD_TRAFFIC_SPD: number
}

export interface RoadTrafficDto {
  AVG_ROAD_DATA: AvgRoadDataDto
}

export interface WeatherForecastDto {
  FCST_DT: string
  TEMP: number
  RAIN_CHANCE: number
  SKY_STTS: string
}

export interface SeoulWeatherDto {
  TEMP: number
  MAX_TEMP: number
  MIN_TEMP: number
  PCP_MSG: string
  SUNRISE: string
  SUNSET: string
  UV_INDEX: number
  UV_INDEX_LVL: string
  UV_MSG: string
  PM25: number
  PM25_INDEX: string
  PM10: number
  PM10_INDEX: string
  FCST24HOURS: WeatherForecastDto[]
}

export interface SeoulCityDataDto {
  AREA_NM: string
  AREA_CD: string
  LIVE_PPLTN_STTS: LivePopulationDto[]
  ROAD_TRAFFIC_STTS: RoadTrafficDto
  WEATHER_STTS: SeoulWeatherDto[]
}

export interface SeoulDataResponse {
  list_total_count: number
  RESULT: { 'RESULT.CODE': string; 'RESULT.MESSAGE': string }
  CITYDATA: SeoulCityDataDto
}
