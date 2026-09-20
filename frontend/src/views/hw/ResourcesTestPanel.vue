<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ heapUsed }}</b><span>{{ t('hwHeapUsed') }}</span></div>
      <div class="hw-stat"><b>{{ heapLimit }}</b><span>{{ t('hwHeapLimit') }}</span></div>
      <div class="hw-stat"><b>{{ pressureLabel }}</b><span>{{ t('hwCpuPressure') }}</span></div>
      <div class="hw-stat"><b>{{ storageUsed }}</b><span>{{ t('hwStorageUsed') }}</span></div>
      <div class="hw-stat"><b>{{ batteryText }}</b><span>{{ t('hwBattery') }}</span></div>
    </div>
    <p class="hw-hint">{{ t('hwResourcesHint') }}</p>
    <div class="hw-bar-block">
      <div class="hw-bar-label">
        <span>{{ t('hwHeap') }}</span>
        <span>{{ heapPct }}%</span>
      </div>
      <div class="hw-meter"><i :style="{ width: `${heapPct}%` }"></i></div>
    </div>
    <div class="hw-bar-block">
      <div class="hw-bar-label">
        <span>{{ t('hwStorage') }}</span>
        <span>{{ storagePct }}%</span>
      </div>
      <div class="hw-meter"><i :style="{ width: `${storagePct}%` }"></i></div>
    </div>
    <canvas ref="sparkRef" class="hw-wave" width="720" height="120"></canvas>
    <table class="hw-table" style="margin-top: 14px">
      <tbody>
        <tr v-for="row in rows" :key="row.label">
          <th>{{ row.label }}</th>
          <td>{{ row.value }}</td>
        </tr>
      </tbody>
    </table>
  </section>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { collectChromeResources, formatBytes } from '../../lib/chromeSystem'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const data = ref(null)
const pressure = ref('')
const sparkRef = ref(null)
const history = []

const heapUsed = computed(() => formatBytes(data.value?.heap?.used))
const heapLimit = computed(() => formatBytes(data.value?.heap?.limit))
const heapPct = computed(() => {
  const heap = data.value?.heap
  if (!heap?.limit) return 0
  return Math.min(100, Math.round((heap.used / heap.limit) * 100))
})
const storageUsed = computed(() => formatBytes(data.value?.storage?.usage))
const storagePct = computed(() => {
  const store = data.value?.storage
  if (!store?.quota) return 0
  return Math.min(100, Math.round((store.usage / store.quota) * 100))
})
const pressureLabel = computed(() => pressure.value || t('hwUnavailable'))
const batteryText = computed(() => {
  const bat = data.value?.battery
  if (!bat) return '—'
  return `${bat.level}%${bat.charging ? ` · ${t('hwCharging')}` : ''}`
})

const rows = computed(() => {
  const snap = data.value
  if (!snap) return []
  const net = snap.network || {}
  const nav = snap.navigation || {}
  return [
    { label: t('hwHeapUsed'), value: formatBytes(snap.heap?.used) },
    { label: t('hwHeapTotal'), value: formatBytes(snap.heap?.total) },
    { label: t('hwHeapLimit'), value: snap.heap ? formatBytes(snap.heap.limit) : t('hwChromeOnly') },
    { label: t('hwStorageUsed'), value: `${formatBytes(snap.storage?.usage)} / ${formatBytes(snap.storage?.quota)}` },
    { label: t('hwNetwork'), value: [net.effectiveType, net.downlink != null ? `${net.downlink} Mbps` : '', net.rtt != null ? `RTT ${net.rtt} ms` : ''].filter(Boolean).join(' · ') || '—' },
    { label: t('hwBattery'), value: batteryText.value },
    { label: t('hwNavTiming'), value: nav.load ? `${t('hwTtfb')} ${nav.ttfb} ms · load ${nav.load} ms` : '—' },
    { label: t('hwResourceCount'), value: String(snap.resources ?? 0) },
    { label: t('hwCpuPressure'), value: pressureLabel.value }
  ]
})

function draw() {
  const canvas = sparkRef.value
  if (!canvas || history.length < 2) return
  const ctx = canvas.getContext('2d')
  const w = canvas.width
  const h = canvas.height
  ctx.clearRect(0, 0, w, h)
  const max = Math.max(...history, 1)
  ctx.strokeStyle = '#0f766e'
  ctx.lineWidth = 2
  ctx.beginPath()
  history.forEach((value, i) => {
    const x = (i / (history.length - 1)) * w
    const y = h - (value / max) * (h * 0.82) - 8
    if (i === 0) ctx.moveTo(x, y)
    else ctx.lineTo(x, y)
  })
  ctx.stroke()
}

async function tick() {
  data.value = await collectChromeResources()
  const used = data.value?.heap?.used
  if (Number.isFinite(used)) {
    history.push(used)
    if (history.length > 40) history.shift()
    draw()
  }
}

let timer = 0
let observer

onMounted(async () => {
  await tick()
  timer = window.setInterval(tick, 1000)
  if (typeof PressureObserver === 'function') {
    try {
      observer = new PressureObserver((records) => {
        const last = records[records.length - 1]
        if (last?.state) pressure.value = last.state
      })
      observer.observe('cpu', { sampleInterval: 1000 })
    } catch {
      pressure.value = ''
    }
  }
})

onUnmounted(() => {
  window.clearInterval(timer)
  observer?.disconnect?.()
})
</script>
