<script setup lang="ts">
withDefaults(
  defineProps<{
    variant?: 'solid' | 'ghost'
    accent?: 'cyan' | 'magenta'
  }>(),
  { variant: 'solid', accent: 'cyan' },
)
</script>

<template>
  <button
    class="group relative inline-flex items-center gap-2 overflow-hidden rounded-sm border px-5 py-2.5 font-mono text-xs font-semibold uppercase tracking-[0.15em] transition-all duration-300"
    :class="[
      variant === 'solid'
        ? accent === 'cyan'
          ? 'border-cyan/60 bg-cyan/10 text-cyan shadow-glow-cyan hover:bg-cyan/20'
          : 'border-magenta/60 bg-magenta/10 text-magenta shadow-glow-magenta hover:bg-magenta/20'
        : 'border-line-strong bg-transparent text-ink-300 hover:border-cyan/50 hover:text-cyan',
    ]"
  >
    <span
      class="absolute inset-0 -translate-x-full bg-gradient-to-r from-transparent via-white/10 to-transparent transition-transform duration-700 group-hover:translate-x-full"
    />
    <slot />
  </button>
</template>
