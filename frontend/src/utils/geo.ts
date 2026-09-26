import areaGeo from '@/data/areaGeo.json'

export interface AreaGeoPoint {
  areaId: string
  areaName: string
  category: string
  lat: number
  lon: number
  /** Simplified boundary ring, as [lon, lat] pairs (source: Seoul open-data GIS, WGS84). */
  ring: [number, number][]
}

const GEO_BOUNDS = areaGeo.bounds
const GEO_AREAS = areaGeo.areas as AreaGeoPoint[]

const geoByName = new Map(GEO_AREAS.map((area) => [area.areaName, area]))

export function findAreaGeo(areaName: string): AreaGeoPoint | undefined {
  return geoByName.get(areaName)
}

export function allAreaGeo(): AreaGeoPoint[] {
  return GEO_AREAS
}

/**
 * Projects a real lat/lon into a 0-100 percentage position within the
 * placeholder map canvas, using Seoul's full 121-area bounding box (with a
 * small margin so edge zones don't sit flush against the panel border).
 */
export function projectToCanvas(lat: number, lon: number, margin = 8): { x: number; y: number } {
  const { minLon, maxLon, minLat, maxLat } = GEO_BOUNDS
  const span = 100 - margin * 2

  const x = margin + ((lon - minLon) / (maxLon - minLon)) * span
  // latitude increases northward, screen y increases downward — flip the axis
  const y = margin + (1 - (lat - minLat) / (maxLat - minLat)) * span

  return { x: Number(x.toFixed(2)), y: Number(y.toFixed(2)) }
}

/** Projects a boundary ring's vertices into 0-100 canvas coordinates. */
export function projectRing(ring: [number, number][], margin = 8): { x: number; y: number }[] {
  return ring.map(([lon, lat]) => projectToCanvas(lat, lon, margin))
}

/** Projects a boundary ring to an SVG path `d` string in the same 0-100 viewBox. */
export function ringToSvgPath(ring: [number, number][], margin = 8): string {
  return projectRing(ring, margin)
    .map(({ x, y }, index) => `${index === 0 ? 'M' : 'L'}${x},${y}`)
    .join(' ')
    .concat(' Z')
}

/** Half-diagonal of a projected ring's bounding box — a rough "how big does this zone read on the map" radius. */
export function ringExtentRadius(ring: [number, number][], margin = 8): number {
  const points = projectRing(ring, margin)
  const xs = points.map((p) => p.x)
  const ys = points.map((p) => p.y)
  const width = Math.max(...xs) - Math.min(...xs)
  const height = Math.max(...ys) - Math.min(...ys)
  return Math.hypot(width, height) / 2
}
