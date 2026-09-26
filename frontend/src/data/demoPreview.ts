import type { AnomalyStatus } from '@/types/region'

// Static illustrative data for the landing page only (hero HUD card, ticker strip).
// The real dashboard pulls all 121 zones live — this is just marketing flavor,
// so it doesn't need to hit the API or carry chart history.
export interface DemoPreviewRegion {
  id: string
  name: string
  nameEn: string
  anomalyScore: number
  status: AnomalyStatus
}

export const demoPreviewRegions: DemoPreviewRegion[] = [
  { id: 'gangnam', name: '강남역', nameEn: 'Gangnam Station', anomalyScore: 78, status: 'warning' },
  { id: 'gwanghwamun', name: '광화문·덕수궁', nameEn: 'Gwanghwamun · Deoksugung', anomalyScore: 65, status: 'warning' },
  { id: 'hongdae', name: '홍대 관광특구', nameEn: 'Hongdae Special Tourist Zone', anomalyScore: 42, status: 'caution' },
  { id: 'jamsil', name: '잠실 관광특구', nameEn: 'Jamsil Special Tourist Zone', anomalyScore: 33, status: 'normal' },
  { id: 'yeouido', name: '여의도', nameEn: 'Yeouido', anomalyScore: 21, status: 'normal' },
]
