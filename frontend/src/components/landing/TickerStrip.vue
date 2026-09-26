<script setup lang="ts">
import { demoPreviewRegions } from '@/data/demoPreview'
import { STATUS_META } from '@/utils/status'

const items = [...demoPreviewRegions, ...demoPreviewRegions]
</script>

<template>
  <div id="regions" class="relative overflow-hidden border-y border-line bg-panel/40 py-3">
    <div class="flex w-max animate-marquee gap-10">
      <div v-for="(region, index) in items" :key="`${region.id}-${index}`" class="flex items-center gap-2 font-mono text-xs">
        <span class="h-1.5 w-1.5 rounded-full" :class="STATUS_META[region.status].dot" />
        <span class="uppercase tracking-widest text-ink-300">{{ region.nameEn }}</span>
        <span class="font-bold" :class="STATUS_META[region.status].color">{{ region.anomalyScore }}</span>
        <span class="text-ink-700">//</span>
      </div>
    </div>
  </div>
</template>
