export const CHART_FONT = "'JetBrains Mono', ui-monospace, monospace"

export const AXIS_LABEL_STYLE = {
  color: '#6c7aa0',
  fontFamily: CHART_FONT,
  fontSize: 10,
}

export const SPLIT_LINE_STYLE = {
  lineStyle: { color: 'rgba(120, 170, 255, 0.08)' },
}

export const TOOLTIP_BASE = {
  backgroundColor: 'rgba(7, 11, 22, 0.92)',
  borderColor: 'rgba(46, 230, 255, 0.35)',
  borderWidth: 1,
  padding: 10,
  textStyle: { color: '#eef4ff', fontFamily: CHART_FONT, fontSize: 11 },
  extraCssText: 'box-shadow: 0 0 20px rgba(46,230,255,0.15); backdrop-filter: blur(6px);',
}
