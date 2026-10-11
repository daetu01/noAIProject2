<script setup lang="ts">
import { computed } from 'vue'
import VChart from 'vue-echarts'
import type { EChartsOption } from 'echarts'

import type { PopulationForecastPoint } from '@/types/region'
import { AXIS_LABEL_STYLE, SPLIT_LINE_STYLE, TOOLTIP_BASE } from '@/utils/chartTheme'
import { formatTime } from '@/utils/format'

const props = defineProps<{
  forecast: PopulationForecastPoint[]
}>()

const accent = '#ff3ec9'

const option = computed<EChartsOption>(() => ({
  grid: { left: 8, right: 12, top: 18, bottom: 24, containLabel: true },
  tooltip: {
    ...TOOLTIP_BASE,
    trigger: 'axis',
    formatter: (params) => {
      const items = Array.isArray(params) ? params : [params]
      const point = props.forecast[items[0]?.dataIndex ?? 0]
      if (!point) return ''
      return `${formatTime(point.time).slice(0, 5)}<br/>${point.populationMin.toLocaleString()} ~ ${point.populationMax.toLocaleString()}명<br/>${point.congestionLevel}`
    },
  },
  xAxis: {
    type: 'category',
    data: props.forecast.map((point) => formatTime(point.time).slice(0, 5)),
    axisLine: { lineStyle: { color: 'rgba(120,170,255,0.18)' } },
    axisTick: { show: false },
    axisLabel: {
      ...AXIS_LABEL_STYLE,
      interval: Math.max(0, Math.ceil(props.forecast.length / 4) - 1),
    },
  },
  yAxis: {
    type: 'value',
    splitLine: SPLIT_LINE_STYLE,
    axisLabel: AXIS_LABEL_STYLE,
  },
  series: [
    {
      name: '최저 예측',
      type: 'line',
      stack: 'band',
      data: props.forecast.map((point) => point.populationMin),
      symbol: 'none',
      lineStyle: { opacity: 0 },
      areaStyle: { opacity: 0 },
    },
    {
      name: '예측 범위',
      type: 'line',
      stack: 'band',
      data: props.forecast.map((point) => point.populationMax - point.populationMin),
      symbol: 'none',
      lineStyle: { opacity: 0 },
      areaStyle: { color: `${accent}22` },
    },
    {
      name: '예상 인구',
      type: 'line',
      data: props.forecast.map((point) => Math.round((point.populationMin + point.populationMax) / 2)),
      smooth: true,
      showSymbol: false,
      lineStyle: { color: accent, width: 2.5, shadowColor: accent, shadowBlur: 10 },
      itemStyle: { color: accent },
    },
  ],
}))
</script>

<template>
  <VChart class="h-full w-full" :option="option" autoresize />
</template>
