<script setup lang="ts">
withDefaults(
  defineProps<{
    title: string
    kicker?: string
    accent?: 'cyan' | 'magenta' | 'green'
  }>(),
  { accent: 'cyan' },
)

const ACCENT_TEXT: Record<string, string> = {
  cyan: 'text-cyan',
  magenta: 'text-magenta',
  green: 'text-green',
}
</script>

<template>
  <div class="flex items-center gap-2">
    <span class="font-mono text-xs" :class="ACCENT_TEXT[accent]">//</span>
    <div>
      <p v-if="kicker" class="font-mono text-[10px] uppercase tracking-[0.2em] text-ink-500">
        {{ kicker }}
      </p>
      <h3 class="font-display text-sm font-semibold uppercase tracking-widest text-ink-100">
        {{ title }}
      </h3>
    </div>
  </div>
</template>
