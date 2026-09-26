<script setup lang="ts">
import BackgroundFx from '@/components/common/BackgroundFx.vue'
import LandingHeader from '@/components/layout/LandingHeader.vue'
import LandingFooter from '@/components/layout/LandingFooter.vue'
import HeroSection from '@/components/landing/HeroSection.vue'
import TickerStrip from '@/components/landing/TickerStrip.vue'
import ConceptSection from '@/components/landing/ConceptSection.vue'
import FeatureGrid from '@/components/landing/FeatureGrid.vue'
import FinalCta from '@/components/landing/FinalCta.vue'
</script>

<template>
  <div class="flex min-h-screen flex-col">
    <BackgroundFx />
    <LandingHeader />

    <main class="flex-1">
      <HeroSection />
      <TickerStrip />
      <ConceptSection />
      <FeatureGrid />
      <FinalCta />
    </main>

    <LandingFooter />
  </div>
</template>
