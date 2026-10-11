// Seoul Open Data's four standard population-congestion levels.
const CONGESTION_TEXT: Record<string, { color: string; dot: string }> = {
  여유: { color: 'text-status-normal', dot: 'bg-status-normal' },
  보통: { color: 'text-cyan', dot: 'bg-cyan' },
  '약간 붐빔': { color: 'text-status-warning', dot: 'bg-status-warning' },
  붐빔: { color: 'text-status-critical', dot: 'bg-status-critical' },
}

const FALLBACK = { color: 'text-ink-500', dot: 'bg-ink-500' }

export function congestionMeta(level: string) {
  return CONGESTION_TEXT[level] ?? FALLBACK
}
