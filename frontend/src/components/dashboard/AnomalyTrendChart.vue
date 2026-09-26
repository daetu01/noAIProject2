<script setup lang="ts">
import { computed } from 'vue'
import VChart from 'vue-echarts'
import type { EChartsOption } from 'echarts'

import type { RegionMetricPoint } from '@/types/region'
import { AXIS_LABEL_STYLE, SPLIT_LINE_STYLE, TOOLTIP_BASE } from '@/utils/chartTheme'
import { formatTime } from '@/utils/format'

const props = defineProps<{
  history: RegionMetricPoint[]
  color?: string
}>()

const option = computed<EChartsOption>(() => {
  const accent = props.color ?? '#2ee6ff'
  return {
    grid: { left: 8, right: 12, top: 18, bottom: 24, containLabel: true },
    tooltip: {
      ...TOOLTIP_BASE,
      trigger: 'axis',
      valueFormatter: (v) => `${v} idx`,
    },
    xAxis: {
      type: 'category',
      data: props.history.map((point) => formatTime(point.timestamp).slice(0, 5)),
      axisLine: { lineStyle: { color: 'rgba(120,170,255,0.18)' } },
      axisTick: { show: false },
      axisLabel: { ...AXIS_LABEL_STYLE, interval: 3 },
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 100,
      splitLine: SPLIT_LINE_STYLE,
      axisLabel: AXIS_LABEL_STYLE,
    },
    series: [
      {
        type: 'line',
        data: props.history.map((point) => point.anomalyScore),
        smooth: true,
        symbol: 'circle',
        symbolSize: 5,
        showSymbol: false,
        lineStyle: { color: accent, width: 2.5, shadowColor: accent, shadowBlur: 10 },
        itemStyle: { color: accent, borderColor: '#04060c', borderWidth: 2 },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: `${accent}55` },
              { offset: 1, color: `${accent}00` },
            ],
          },
        },
        markLine: {
          silent: true,
          symbol: 'none',
          lineStyle: { color: 'rgba(255,67,97,0.4)', type: 'dashed', width: 1 },
          label: { color: '#ff4361', fontFamily: 'JetBrains Mono', fontSize: 9 },
          data: [{ yAxis: 80, name: 'CRITICAL' }],
        },
      },
    ],
  }
})
</script>

<template>
  <VChart class="h-full w-full" :option="option" autoresize />
</template>
