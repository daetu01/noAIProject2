<script setup lang="ts">
import SectionHeading from '@/components/common/SectionHeading.vue'

const steps = [
  {
    tag: '01 · COLLECT',
    title: '실시간 데이터 수집',
    desc: '인구, 교통, 날씨 센서 데이터를 초 단위로 수집하여 지역별 실시간 지표를 구성합니다.',
  },
  {
    tag: '02 · ANALYZE',
    title: '패턴 융합 분석',
    desc: '평시 패턴과 현재 데이터를 비교해 인구·교통·기상 변화를 하나의 이상징후 점수로 환산합니다.',
  },
  {
    tag: '03 · DETECT',
    title: '이상징후 탐지 & 경보',
    desc: '임계치를 초과하면 자동으로 경보 등급을 산정하고, 예상 원인 요약과 함께 관제 화면에 표시합니다.',
  },
]
</script>

<template>
  <section id="concept" class="relative px-4 py-20 sm:px-6">
    <div class="mx-auto max-w-6xl">
      <SectionHeading kicker="HOW IT WORKS" title="이상징후 탐지 개념" accent="cyan" />
      <p class="mt-4 max-w-2xl text-sm leading-relaxed text-ink-300 sm:text-base">
        Urban Signal은 단순 수치 나열이 아니라, <strong class="text-ink-100">"무엇이 · 왜 · 얼마나"</strong>
        평소와 다른지를 계산해 하나의 신호로 보여줍니다. 아래 3단계 파이프라인으로 지역별 이상 상태를
        실시간으로 산출합니다.
      </p>

      <div class="relative mt-12 grid grid-cols-1 gap-6 md:grid-cols-3">
        <div class="absolute left-0 right-0 top-6 hidden h-px bg-gradient-to-r from-transparent via-cyan/30 to-transparent md:block" />
        <div
          v-for="step in steps"
          :key="step.tag"
          class="relative rounded-xl border border-line bg-panel/60 p-5 backdrop-blur-md transition-colors duration-300 hover:border-cyan/40"
        >
          <span class="font-mono text-[11px] tracking-[0.2em] text-cyan">{{ step.tag }}</span>
          <h4 class="mt-2 font-display text-lg font-semibold text-ink-100">{{ step.title }}</h4>
          <p class="mt-2 text-sm leading-relaxed text-ink-500">{{ step.desc }}</p>
        </div>
      </div>

      <!-- score gauge illustration -->
      <div class="mt-10 rounded-xl border border-line bg-panel/50 p-5 backdrop-blur-md">
        <div class="flex flex-wrap items-center justify-between gap-3">
          <span class="font-mono text-[11px] uppercase tracking-widest text-ink-500">Anomaly Index Scale</span>
          <span class="font-mono text-[11px] text-ink-500">0 — 100</span>
        </div>
        <div class="mt-4 h-2.5 w-full overflow-hidden rounded-full bg-panel-alt">
          <div class="flex h-full w-full">
            <div class="h-full w-[35%] bg-status-normal" />
            <div class="h-full w-[25%] bg-status-caution" />
            <div class="h-full w-[20%] bg-status-warning" />
            <div class="h-full w-[20%] bg-status-critical" />
          </div>
        </div>
        <div class="mt-2 flex justify-between font-mono text-[10px] text-ink-500">
          <span>NORMAL</span>
          <span>CAUTION</span>
          <span>WARNING</span>
          <span>CRITICAL</span>
        </div>
      </div>
    </div>
  </section>
</template>
