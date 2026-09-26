import type { AnomalyStatus, SystemStatus } from '@/types/region'

export interface StatusMeta {
  label: string
  labelKo: string
  color: string
  glow: string
  bg: string
  border: string
  dot: string
}

export const STATUS_META: Record<AnomalyStatus, StatusMeta> = {
  normal: {
    label: 'NORMAL',
    labelKo: '정상',
    color: 'text-status-normal',
    glow: 'shadow-glow-green',
    bg: 'bg-status-normal/10',
    border: 'border-status-normal/40',
    dot: 'bg-status-normal',
  },
  caution: {
    label: 'CAUTION',
    labelKo: '주의',
    color: 'text-status-caution',
    glow: 'shadow-glow-amber',
    bg: 'bg-status-caution/10',
    border: 'border-status-caution/40',
    dot: 'bg-status-caution',
  },
  warning: {
    label: 'WARNING',
    labelKo: '경고',
    color: 'text-status-warning',
    glow: 'shadow-glow-amber',
    bg: 'bg-status-warning/10',
    border: 'border-status-warning/40',
    dot: 'bg-status-warning',
  },
  critical: {
    label: 'CRITICAL',
    labelKo: '위험',
    color: 'text-status-critical',
    glow: 'shadow-glow-red',
    bg: 'bg-status-critical/10',
    border: 'border-status-critical/40',
    dot: 'bg-status-critical',
  },
}

/** Real hex values (mirrors the CSS tokens in main.css) for contexts that can't use Tailwind classes — Leaflet styles, canvas, etc. */
export const STATUS_HEX: Record<AnomalyStatus, string> = {
  normal: '#39f5a0',
  caution: '#ffd23f',
  warning: '#ffb640',
  critical: '#ff4361',
}

export function systemStatusMeta(status: SystemStatus) {
  switch (status) {
    case 'CRITICAL':
      return { color: 'text-status-critical', dot: 'bg-status-critical', glow: 'shadow-glow-red' }
    case 'WARNING':
      return { color: 'text-status-warning', dot: 'bg-status-warning', glow: 'shadow-glow-amber' }
    default:
      return { color: 'text-cyan', dot: 'bg-cyan', glow: 'shadow-glow-cyan' }
  }
}
