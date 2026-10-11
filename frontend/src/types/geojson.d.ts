interface SeoulAreaProperties {
  areaId: string
  areaName: string
  category: string
  lat: number
  lon: number
}

interface SeoulAreaFeature {
  type: 'Feature'
  id: string
  properties: SeoulAreaProperties
  geometry: { type: 'Polygon'; coordinates: number[][][] }
}

interface SeoulAreaCollection {
  type: 'FeatureCollection'
  features: SeoulAreaFeature[]
}

declare module '*.geojson' {
  const value: SeoulAreaCollection
  export default value
}
