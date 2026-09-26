<script setup lang="ts">
import SectionHeading from '@/components/common/SectionHeading.vue'
import GlassPanel from '@/components/common/GlassPanel.vue'

const ICON_WRAP: Record<string, string> = {
  cyan: 'border-cyan/40 bg-cyan/10 text-cyan',
  green: 'border-green/40 bg-green/10 text-green',
  blue: 'border-blue/40 bg-blue/10 text-blue',
  magenta: 'border-magenta/40 bg-magenta/10 text-magenta',
}

const features = [
  {
    accent: 'cyan' as const,
    icon: 'M12 2v6M12 16v6M4.9 4.9l4.2 4.2M14.9 14.9l4.2 4.2M2 12h6M16 12h6M4.9 19.1l4.2-4.2M14.9 9.1l4.2-4.2',
    title: '실시간 인구 모니터링',
    desc: '주요 지역의 유동인구 밀도를 초 단위로 추적하고 평시 대비 증감률을 계산합니다.',
  },
  {
    accent: 'green' as const,
    icon: 'M3 12h4l2-7 4 14 2-7h6',
    title: '교통 흐름 분석',
    desc: '도로 구간별 평균 통행 속도와 혼잡도를 실시간으로 집계해 정체 패턴을 포착합니다.',
  },
  {
    accent: 'blue' as const,
    icon: 'M6 15a4 4 0 1 1 1.2-7.8A5.5 5.5 0 0 1 17.8 9.5 3.5 3.5 0 0 1 17 16H7',
    title: '날씨 데이터 통합',
    desc: '기온·강수 등 기상 변화가 인구·교통 지표에 미치는 영향을 함께 분석합니다.',
  },
  {
    accent: 'magenta' as const,
    icon: 'M12 3l2.5 5.5L20 9l-4.2 4 1 5.6L12 16l-4.8 2.6 1-5.6L4 9l5.5-.5L12 3z',
    title: 'AI 이상징후 탐지 엔진',
    desc: '다중 지표를 융합해 이상징후 점수를 산출하고, 원인 요약까지 자동으로 생성합니다.',
  },
]
</script>

<template>
  <section id="features" class="relative px-4 py-20 sm:px-6">
    <div class="mx-auto max-w-6xl">
      <SectionHeading kicker="CORE CAPABILITIES" title="주요 기능" accent="magenta" />

      <div class="mt-10 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <GlassPanel
          v-for="feature in features"
          :key="feature.title"
          :accent="feature.accent"
          interactive
          class="transition-transform duration-300 hover:-translate-y-1"
        >
          <span
            class="inline-flex h-10 w-10 items-center justify-center rounded-sm border"
            :class="ICON_WRAP[feature.accent]"
          >
            <svg viewBox="0 0 24 24" class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="1.8">
              <path :d="feature.icon" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
          </span>
          <h4 class="mt-3 font-display text-sm font-semibold text-ink-100">{{ feature.title }}</h4>
          <p class="mt-2 text-xs leading-relaxed text-ink-500">{{ feature.desc }}</p>
        </GlassPanel>
      </div>
    </div>
  </section>
</template>
