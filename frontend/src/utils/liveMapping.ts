import type { AnomalyStatus } from '@/types/region'

// Rough "busy hotspot" ceiling shared across all 121 zones — a quiet park at 2am
// and a packed Gangnam Station both get judged against the same bar, which is
// the point (a quiet zone SHOULD score low).
const POPULATION_CEILING = 40000

/**
 * The backend (POST /api/seoul/area) only relays raw population/traffic/weather
 * figures — there is no anomaly-detection endpoint yet. This is a client-side
 * placeholder heuristic so the UI has a meaningful score to render; swap it out
 * once a real scoring endpoint exists.
 */
export function computeAnomalyScore(population: number, trafficSpeed: number): number {
  const populationFactor = clamp(population / POPULATION_CEILING, 0, 1)
  const trafficFactor = clamp(1 - trafficSpeed / 30, 0, 1)
  return Math.round(100 * (0.55 * populationFactor + 0.45 * trafficFactor))
}

export function percentDelta(current: number, previous: number): number {
  if (!previous) return 0
  return Number((((current - previous) / previous) * 100).toFixed(1))
}

export function buildCauseSummary(
  status: AnomalyStatus,
  roadMsg: string,
  weatherMessage: string,
): string {
  if (status === 'normal') {
    return `${roadMsg} · ${weatherMessage} — 주요 지표가 평시 범위 내에서 안정적으로 유지되고 있습니다.`
  }
  if (status === 'caution') {
    return `${roadMsg} · ${weatherMessage} 상태로, 유동인구가 평시 대비 증가하는 추세입니다.`
  }
  return `${roadMsg} · ${weatherMessage} 상태와 함께 유동인구 밀집이 감지되어 이상 지수가 상승했습니다.`
}

function clamp(value: number, min: number, max: number): number {
  return Math.min(max, Math.max(min, value))
}
