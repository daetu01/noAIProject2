<script setup lang="ts">
import { RouterLink } from 'vue-router'

import { demoPreviewRegions } from '@/data/demoPreview'
import { STATUS_META } from '@/utils/status'
import NeonButton from '@/components/common/NeonButton.vue'

const preview = [...demoPreviewRegions].sort((a, b) => b.anomalyScore - a.anomalyScore)
</script>

<template>
  <section class="relative overflow-hidden px-4 pb-20 pt-16 sm:px-6 sm:pt-24">
    <div class="mx-auto max-w-6xl">
      <div class="grid grid-cols-1 items-center gap-12 lg:grid-cols-[1.1fr_0.9fr]">
        <div>
          <span
            class="inline-flex items-center gap-2 rounded-full border border-cyan/30 bg-cyan/5 px-3 py-1 font-mono text-[11px] uppercase tracking-[0.2em] text-cyan"
          >
            <span class="h-1.5 w-1.5 animate-pulse rounded-full bg-cyan" />
            Seoul Metropolitan Anomaly Grid
          </span>

          <h1 class="relative mt-6 font-display text-4xl font-extrabold leading-[1.08] tracking-tight text-ink-100 sm:text-5xl lg:text-6xl">
            도시의 이상징후를<br />
            <span class="relative inline-block text-cyan text-glow-cyan">
              실시간으로 포착한다
              <span
                class="absolute inset-0 text-magenta animate-glitch"
                aria-hidden="true"
              >
                실시간으로 포착한다
              </span>
            </span>
          </h1>

          <p class="mt-6 max-w-xl text-sm leading-relaxed text-ink-300 sm:text-base">
            Urban Signal은 인구, 교통, 날씨 데이터를 실시간으로 융합 분석하여 서울 주요 지역의
            이상징후를 자동으로 탐지하고, <span class="text-ink-100">어디서 · 왜 · 어떻게</span>
            변화가 발생했는지 한눈에 보여주는 도시 관제 시스템입니다.
          </p>

          <div class="mt-9 flex flex-wrap items-center gap-4">
            <RouterLink to="/dashboard">
              <NeonButton accent="cyan">
                Launch Monitoring System
                <svg viewBox="0 0 24 24" class="h-3.5 w-3.5" fill="none" stroke="currentColor" stroke-width="2.2">
                  <path d="M5 12h14M13 6l6 6-6 6" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </NeonButton>
            </RouterLink>
            <a href="#concept">
              <NeonButton variant="ghost">탐지 원리 보기</NeonButton>
            </a>
          </div>

          <dl class="mt-10 grid grid-cols-3 gap-4 border-t border-line pt-6 sm:max-w-md">
            <div>
              <dt class="font-mono text-[10px] uppercase tracking-widest text-ink-500">Zones</dt>
              <dd class="mt-1 font-mono text-2xl font-bold text-ink-100">05</dd>
            </div>
            <div>
              <dt class="font-mono text-[10px] uppercase tracking-widest text-ink-500">Signals</dt>
              <dd class="mt-1 font-mono text-2xl font-bold text-ink-100">4</dd>
            </div>
            <div>
              <dt class="font-mono text-[10px] uppercase tracking-widest text-ink-500">Refresh</dt>
              <dd class="mt-1 font-mono text-2xl font-bold text-cyan">4s</dd>
            </div>
          </dl>
        </div>

        <!-- HUD preview card -->
        <div class="relative">
          <div class="absolute -inset-6 -z-10 rounded-full bg-cyan/10 blur-[80px]" />
          <div class="clip-corner rounded-xl border border-cyan/25 bg-panel/70 p-4 backdrop-blur-md">
            <div class="flex items-center justify-between border-b border-line pb-3">
              <span class="font-mono text-[11px] uppercase tracking-widest text-ink-500">Live Anomaly Rank</span>
              <span class="flex items-center gap-1.5 font-mono text-[10px] text-cyan">
                <span class="h-1.5 w-1.5 animate-pulse rounded-full bg-cyan" /> LIVE
              </span>
            </div>

            <ul class="mt-3 space-y-2.5">
              <li
                v-for="(region, index) in preview"
                :key="region.id"
                class="flex items-center gap-3 rounded-sm border border-line bg-panel-alt/60 px-3 py-2.5"
              >
                <span class="font-mono text-xs text-ink-700">0{{ index + 1 }}</span>
                <div class="min-w-0 flex-1">
                  <p class="truncate text-sm text-ink-100">{{ region.name }}</p>
                  <p class="font-mono text-[10px] uppercase tracking-wide" :class="STATUS_META[region.status].color">
                    {{ STATUS_META[region.status].label }}
                  </p>
                </div>
                <span class="font-mono text-xl font-bold tabular-nums" :class="STATUS_META[region.status].color">
                  {{ region.anomalyScore }}
                </span>
              </li>
            </ul>

            <div class="mt-4 flex items-center justify-between rounded-sm border border-line bg-black/30 px-3 py-2 font-mono text-[10px] text-ink-500">
              <span>&gt; scanning grid_seoul.map ...</span>
              <span class="h-3 w-1.5 animate-pulse bg-cyan/70" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
