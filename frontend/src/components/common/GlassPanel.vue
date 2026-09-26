<script setup lang="ts">
import { computed } from 'vue'

type Accent = 'cyan' | 'magenta' | 'green' | 'amber' | 'red' | 'blue'

const props = withDefaults(
  defineProps<{
    accent?: Accent
    noPadding?: boolean
    interactive?: boolean
  }>(),
  {
    accent: 'cyan',
    noPadding: false,
    interactive: false,
  },
)

const ACCENT_BORDER: Record<Accent, string> = {
  cyan: 'border-cyan/25 hover:border-cyan/45',
  magenta: 'border-magenta/25 hover:border-magenta/45',
  green: 'border-green/25 hover:border-green/45',
  amber: 'border-amber/25 hover:border-amber/45',
  red: 'border-red/30 hover:border-red/50',
  blue: 'border-blue/25 hover:border-blue/45',
}

const ACCENT_LINE: Record<Accent, string> = {
  cyan: 'via-cyan',
  magenta: 'via-magenta',
  green: 'via-green',
  amber: 'via-amber',
  red: 'via-red',
  blue: 'via-blue',
}

const borderClass = computed(() => ACCENT_BORDER[props.accent])
const lineClass = computed(() => ACCENT_LINE[props.accent])
</script>

<template>
  <section
    class="group relative flex flex-col rounded-xl border bg-panel/70 backdrop-blur-md transition-colors duration-300"
    :class="[borderClass, interactive ? 'cursor-pointer' : '']"
  >
    <span
      class="absolute inset-x-4 -top-px h-px bg-gradient-to-r from-transparent to-transparent"
      :class="lineClass"
    />
    <span class="pointer-events-none absolute left-0 top-0 h-3 w-3 border-l border-t border-inherit" />
    <span class="pointer-events-none absolute right-0 top-0 h-3 w-3 border-r border-t border-inherit" />
    <span class="pointer-events-none absolute bottom-0 left-0 h-3 w-3 border-b border-l border-inherit" />
    <span class="pointer-events-none absolute bottom-0 right-0 h-3 w-3 border-b border-r border-inherit" />

    <div
      v-if="$slots.header"
      class="flex shrink-0 items-center justify-between border-b border-line px-4 py-3"
    >
      <slot name="header" />
    </div>

    <div class="flex min-h-0 flex-1 flex-col" :class="noPadding ? '' : 'p-4'">
      <slot />
    </div>
  </section>
</template>
