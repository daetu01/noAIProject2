import type { Map as MaplibreMap } from 'maplibre-gl'

// OpenFreeMap's "liberty" style (OpenMapTiles schema) re-themed into a dark,
// restrained control-room basemap: near-black navy ground, muted slate roads
// and buildings, cyan-tinted accents, place/boundary labels only — no POI
// icons, no shop names, no 3D buildings, no route shields.

const LAYERS_TO_DROP = [
  'natural_earth',
  'building-3d',
  'poi_r20',
  'poi_r7',
  'poi_r1',
  'poi_transit',
  'highway-name-path',
  'highway-name-minor',
  'highway-name-major',
  'highway-shield-non-us',
  'highway-shield-us-interstate',
  'road_shield_us',
  'road_one_way_arrow',
  'road_one_way_arrow_opposite',
  'airport',
  'waterway_line_label',
  'landuse_pitch',
  'landuse_track',
  'landuse_cemetery',
  'landuse_hospital',
  'landuse_school',
  'boundary_disputed',
]

const VOID = '#05070d'
const WATER = '#0b2036'
const LAND = '#0e1424'
const PARK = '#0c2420'
const BUILDING = '#182238'
const ROAD_CASING = '#05070d'
const ROAD_MAJOR = '#2a8aa3'
const ROAD_MINOR = '#1c3348'
const ROAD_RAIL = '#1a3850'
const BOUNDARY = '#2ee6ff'
const LABEL = '#8fc9dc'
const LABEL_HALO = '#05070d'
const WATER_LABEL = '#4fd4e8'

// setPaintProperty's real signature is keyed per layer type (fill/line/symbol
// each have a disjoint paint-property union) — this helper intentionally
// spans all of them, so the property name is typed loosely here on purpose.
const setPaintProperty = (map: MaplibreMap, id: string, name: string, value: unknown): void => {
  ;(map.setPaintProperty as (layerId: string, name: string, value: unknown) => void)(id, name, value)
}

function setPaint(map: MaplibreMap, id: string, paint: Record<string, unknown>) {
  if (!map.getLayer(id)) return
  for (const [key, value] of Object.entries(paint)) {
    setPaintProperty(map, id, key, value)
  }
}

export function applyDarkTheme(map: MaplibreMap): void {
  for (const id of LAYERS_TO_DROP) {
    if (map.getLayer(id)) map.removeLayer(id)
  }

  setPaint(map, 'background', { 'background-color': VOID })
  setPaint(map, 'water', { 'fill-color': WATER })
  setPaint(map, 'waterway_tunnel', { 'line-color': WATER })
  setPaint(map, 'waterway_river', { 'line-color': WATER })
  setPaint(map, 'waterway_other', { 'line-color': WATER })

  setPaint(map, 'landuse_residential', { 'fill-color': LAND, 'fill-opacity': 0.85 })
  setPaint(map, 'landcover_wood', { 'fill-color': PARK, 'fill-opacity': 0.75 })
  setPaint(map, 'landcover_grass', { 'fill-color': PARK, 'fill-opacity': 0.6 })
  setPaint(map, 'landcover_wetland', { 'fill-color': PARK, 'fill-opacity': 0.6 })
  setPaint(map, 'landcover_ice', { 'fill-color': LAND, 'fill-opacity': 0.5 })
  setPaint(map, 'landcover_sand', { 'fill-color': LAND, 'fill-opacity': 0.5 })
  setPaint(map, 'park', { 'fill-color': PARK, 'fill-opacity': 0.75 })
  setPaint(map, 'park_outline', { 'line-color': PARK, 'line-opacity': 0.5 })

  setPaint(map, 'aeroway_fill', { 'fill-color': BUILDING })
  setPaint(map, 'aeroway_runway', { 'line-color': ROAD_MINOR })
  setPaint(map, 'aeroway_taxiway', { 'line-color': ROAD_MINOR })

  setPaint(map, 'building', { 'fill-color': BUILDING, 'fill-opacity': 0.75 })

  // road hierarchy: casings go near-black (bezel effect), fills get a
  // restrained cyan tint on the major roads only — minor roads stay dim.
  const casingLayers = [
    'tunnel_motorway_link_casing',
    'tunnel_service_track_casing',
    'tunnel_link_casing',
    'tunnel_street_casing',
    'tunnel_secondary_tertiary_casing',
    'tunnel_trunk_primary_casing',
    'tunnel_motorway_casing',
    'road_motorway_link_casing',
    'road_service_track_casing',
    'road_link_casing',
    'road_minor_casing',
    'road_secondary_tertiary_casing',
    'road_trunk_primary_casing',
    'road_motorway_casing',
    'bridge_motorway_link_casing',
    'bridge_service_track_casing',
    'bridge_link_casing',
    'bridge_street_casing',
    'bridge_path_pedestrian_casing',
    'bridge_secondary_tertiary_casing',
    'bridge_trunk_primary_casing',
    'bridge_motorway_casing',
  ]
  for (const id of casingLayers) setPaint(map, id, { 'line-color': ROAD_CASING })

  const majorRoadLayers = [
    'tunnel_trunk_primary',
    'tunnel_motorway',
    'road_trunk_primary',
    'road_motorway',
    'road_motorway_link',
    'bridge_trunk_primary',
    'bridge_motorway',
    'bridge_motorway_link',
  ]
  for (const id of majorRoadLayers) setPaint(map, id, { 'line-color': ROAD_MAJOR })

  const minorRoadLayers = [
    'tunnel_path_pedestrian',
    'tunnel_motorway_link',
    'tunnel_service_track',
    'tunnel_link',
    'tunnel_minor',
    'tunnel_secondary_tertiary',
    'road_area_pattern',
    'road_path_pedestrian',
    'road_service_track',
    'road_link',
    'road_minor',
    'road_secondary_tertiary',
    'bridge_path_pedestrian',
    'bridge_service_track',
    'bridge_link',
    'bridge_street',
    'bridge_secondary_tertiary',
  ]
  for (const id of minorRoadLayers) {
    setPaint(map, id, { [id.includes('area_pattern') ? 'fill-color' : 'line-color']: ROAD_MINOR })
  }

  const railLayers = [
    'tunnel_major_rail',
    'tunnel_major_rail_hatching',
    'tunnel_transit_rail',
    'tunnel_transit_rail_hatching',
    'road_major_rail',
    'road_major_rail_hatching',
    'road_transit_rail',
    'road_transit_rail_hatching',
    'bridge_major_rail',
    'bridge_major_rail_hatching',
    'bridge_transit_rail',
    'bridge_transit_rail_hatching',
  ]
  for (const id of railLayers) setPaint(map, id, { 'line-color': ROAD_RAIL })

  setPaint(map, 'boundary_2', { 'line-color': BOUNDARY, 'line-opacity': 0.35 })
  setPaint(map, 'boundary_3', { 'line-color': BOUNDARY, 'line-opacity': 0.2 })

  setPaint(map, 'water_name_point_label', { 'text-color': WATER_LABEL, 'text-halo-color': LABEL_HALO })
  setPaint(map, 'water_name_line_label', { 'text-color': WATER_LABEL, 'text-halo-color': LABEL_HALO })

  const placeLabelLayers = [
    'label_other',
    'label_village',
    'label_town',
    'label_state',
    'label_city',
    'label_city_capital',
    'label_country_3',
    'label_country_2',
    'label_country_1',
  ]
  for (const id of placeLabelLayers) {
    setPaint(map, id, { 'text-color': LABEL, 'text-halo-color': LABEL_HALO, 'text-halo-width': 1.2 })
  }
}
