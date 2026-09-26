<script setup lang="ts">
// Shared decorative backdrop: grid + radial glow + scanline sweep.
// Keeps the landing page and dashboard visually consistent.
</script>

<template>
  <div class="pointer-events-none fixed inset-0 -z-10 overflow-hidden bg-void">
    <div class="absolute inset-0 bg-radial-glow" />
    <div class="absolute inset-0 bg-grid opacity-70" />
    <div class="scanline-overlay" />
    <div
      class="absolute -top-40 left-1/4 h-[32rem] w-[32rem] rounded-full bg-blue/10 blur-[120px]"
    />
    <div
      class="absolute top-1/3 right-0 h-[26rem] w-[26rem] rounded-full bg-magenta/10 blur-[130px]"
    />
    <div
      class="absolute bottom-0 left-0 h-[24rem] w-[24rem] rounded-full bg-cyan/10 blur-[110px]"
    />
    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-void" />
  </div>
</template>
