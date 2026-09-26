<script setup lang="ts">
import { RouterLink } from 'vue-router'

import NeonButton from '@/components/common/NeonButton.vue'
</script>

<template>
  <section class="relative px-4 py-20 sm:px-6">
    <div class="mx-auto max-w-4xl">
      <div class="clip-corner relative overflow-hidden rounded-xl border border-cyan/25 bg-panel/60 p-10 text-center backdrop-blur-md sm:p-14">
        <div class="absolute inset-0 bg-grid-fine opacity-40" />
        <div class="absolute -inset-16 -z-10 rounded-full bg-cyan/10 blur-[100px]" />

        <p class="font-mono text-xs uppercase tracking-[0.25em] text-cyan">Ready to observe</p>
        <h3 class="mt-3 font-display text-2xl font-bold text-ink-100 sm:text-3xl">
          지금 서울의 실시간 신호를 확인하세요
        </h3>
        <p class="mx-auto mt-3 max-w-md text-sm text-ink-500">
          5개 핵심 지역의 인구·교통·날씨·이상징후 데이터를 하나의 관제 화면에서 실시간으로 살펴보세요.
        </p>

        <RouterLink to="/dashboard" class="mt-8 inline-block">
          <NeonButton accent="cyan">Launch Monitoring System</NeonButton>
        </RouterLink>
      </div>
    </div>
  </section>
</template>
