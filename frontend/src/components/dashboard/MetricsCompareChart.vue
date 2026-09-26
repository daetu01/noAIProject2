<script setup lang="ts">
import { computed } from 'vue'
import VChart from 'vue-echarts'
import type { EChartsOption } from 'echarts'

import type { RegionMetricPoint } from '@/types/region'
import { AXIS_LABEL_STYLE, TOOLTIP_BASE } from '@/utils/chartTheme'
import { formatTime } from '@/utils/format'

const props = defineProps<{
  history: RegionMetricPoint[]
}>()

const option = computed<EChartsOption>(() => ({
  grid: { left: 8, right: 8, top: 30, bottom: 24, containLabel: true },
  legend: {
    top: 0,
    right: 0,
    textStyle: { color: '#a9bade', fontFamily: 'JetBrains Mono', fontSize: 10 },
    itemWidth: 10,
    itemHeight: 10,
    icon: 'roundRect',
  },
  tooltip: { ...TOOLTIP_BASE, trigger: 'axis' },
  xAxis: {
    type: 'category',
    data: props.history.map((point) => formatTime(point.timestamp).slice(0, 5)),
    axisLine: { lineStyle: { color: 'rgba(120,170,255,0.18)' } },
    axisTick: { show: false },
    axisLabel: { ...AXIS_LABEL_STYLE, interval: 3 },
  },
  yAxis: [
    {
      type: 'value',
      name: 'POP',
      nameTextStyle: { color: '#6c7aa0', fontFamily: 'JetBrains Mono', fontSize: 9 },
      splitLine: { show: false },
      axisLabel: AXIS_LABEL_STYLE,
    },
    {
      type: 'value',
      name: 'KM/H',
      nameTextStyle: { color: '#6c7aa0', fontFamily: 'JetBrains Mono', fontSize: 9 },
      splitLine: { show: false },
      axisLabel: AXIS_LABEL_STYLE,
    },
  ],
  series: [
    {
      name: '인구',
      type: 'line',
      yAxisIndex: 0,
      data: props.history.map((point) => point.population),
      smooth: true,
      showSymbol: false,
      lineStyle: { color: '#4d7dff', width: 2 },
      itemStyle: { color: '#4d7dff' },
    },
    {
      name: '교통 속도',
      type: 'line',
      yAxisIndex: 1,
      data: props.history.map((point) => point.trafficSpeed),
      smooth: true,
      showSymbol: false,
      lineStyle: { color: '#39f5a0', width: 2 },
      itemStyle: { color: '#39f5a0' },
    },
  ],
}))
</script>

<template>
  <VChart class="h-full w-full" :option="option" autoresize />
</template>
