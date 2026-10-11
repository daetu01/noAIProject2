export function formatNumber(value: number): string {
  return new Intl.NumberFormat('ko-KR').format(Math.round(value))
}

export function formatSigned(value: number, digits = 1): string {
  const sign = value > 0 ? '+' : ''
  return `${sign}${value.toFixed(digits)}`
}

export function formatTime(iso: string): string {
  return new Date(iso).toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
  })
}

export function formatClock(date: Date): string {
  return date.toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
  })
}

export function formatDate(date: Date): string {
  return date.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    weekday: 'short',
  })
}

/** Backend sends "yyyy-MM-dd HH:mm" (no timezone) — treat as local time and normalize to ISO. */
export function parseBackendDateTime(raw: string): string {
  const iso = raw.includes('T') ? raw : raw.replace(' ', 'T')
  const date = new Date(iso)
  return Number.isNaN(date.getTime()) ? new Date().toISOString() : date.toISOString()
}

export function timeAgo(iso: string): string {
  const diffMs = Date.now() - new Date(iso).getTime()
  const minutes = Math.floor(diffMs / 60000)
  if (minutes < 1) return '방금 전'
  if (minutes < 60) return `${minutes}분 전`
  const hours = Math.floor(minutes / 60)
  return `${hours}시간 전`
}
