<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div v-for="item in stats" :key="item.label" class="hw-stat">
        <b>{{ item.value }}</b>
        <span>{{ item.label }}</span>
      </div>
    </div>
    <div class="actions" style="margin: 0 0 16px">
      <button class="btn" type="button" @click="enterPurity">{{ t('hwDeadPixel') }}</button>
      <button class="btn secondary" type="button" @click="pattern = 'bars'">{{ t('hwColorBars') }}</button>
      <button class="btn secondary" type="button" @click="pattern = 'gradient'">{{ t('hwGradient') }}</button>
      <button class="btn secondary" type="button" @click="pattern = 'checker'">{{ t('hwChecker') }}</button>
      <button class="btn secondary" type="button" @click="pattern = 'gamma'">{{ t('hwGamma') }}</button>
    </div>
    <p class="hw-hint">{{ t('hwScreenHint') }}</p>
    <div class="hw-screen-stage" :class="pattern">
      <div v-if="pattern === 'bars'" class="hw-bars">
        <span v-for="color in barColors" :key="color" :style="{ background: color }"></span>
      </div>
      <div v-else-if="pattern === 'gradient'" class="hw-gradient"></div>
      <div v-else-if="pattern === 'checker'" class="hw-checker"></div>
      <div v-else class="hw-gamma">
        <span v-for="n in 10" :key="n" :style="{ background: `rgb(${n * 25},${n * 25},${n * 25})` }"></span>
      </div>
    </div>
  </section>
  <Teleport to="body">
    <div
      v-if="purity"
      class="hw-purity"
      :style="{ background: purityColors[purityIndex] }"
      @click="cyclePurity"
    >
      <div class="hw-purity-hud">
        {{ purityColors[purityIndex] }} · {{ t('hwPurityHud') }}
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { onMounted, onUnmounted, reactive, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const pattern = ref('bars')
const purity = ref(false)
const purityIndex = ref(0)
const fps = ref(0)
const refresh = ref(0)
const stats = reactive([])
const barColors = ['#ffffff', '#ffff00', '#00ffff', '#00ff00', '#ff00ff', '#ff0000', '#0000ff', '#000000']
const purityColors = ['#ff0000', '#00ff00', '#0000ff', '#ffffff', '#000000', '#808080', '#ffff00', '#00ffff', '#ff00ff']

let raf = 0
let frames = 0
let last = 0
let peak = 0

function media(query) {
  return window.matchMedia(query).matches
}

function gamut() {
  if (media('(color-gamut: rec2020)')) return 'rec2020'
  if (media('(color-gamut: p3)')) return 'p3'
  if (media('(color-gamut: srgb)')) return 'srgb'
  return 'unknown'
}

function refreshStats() {
  const s = window.screen
  const orientation = s.orientation || {}
  stats.splice(0, stats.length,
    { label: t('hwResolution'), value: `${s.width}×${s.height}` },
    { label: t('hwAvail'), value: `${s.availWidth}×${s.availHeight}` },
    { label: t('hwViewport'), value: `${window.innerWidth}×${window.innerHeight}` },
    { label: t('hwDpr'), value: String(window.devicePixelRatio || 1) },
    { label: t('hwColorDepth'), value: `${s.colorDepth || 0} bit` },
    { label: t('hwOrientation'), value: orientation.type || (window.innerWidth >= window.innerHeight ? 'landscape' : 'portrait') },
    { label: t('hwGamut'), value: gamut() },
    { label: t('hwHdr'), value: media('(dynamic-range: high)') ? 'HDR' : 'SDR' },
    { label: t('hwFps'), value: String(fps.value) },
    { label: t('hwRefresh'), value: `${refresh.value} Hz` }
  )
}

function tick(now) {
  if (!last) last = now
  frames += 1
  if (now - last >= 1000) {
    fps.value = frames
    if (frames > peak) peak = frames
    refresh.value = peak
    frames = 0
    last = now
    refreshStats()
  }
  raf = requestAnimationFrame(tick)
}

async function enterPurity() {
  purity.value = true
  purityIndex.value = 0
  try {
    await document.documentElement.requestFullscreen()
  } catch {
    // overlay still covers the viewport
  }
}

function exitPurity() {
  purity.value = false
  if (document.fullscreenElement) {
    document.exitFullscreen().catch(() => {})
  }
}

function cyclePurity() {
  purityIndex.value = (purityIndex.value + 1) % purityColors.length
}

function onKey(event) {
  if (!purity.value) return
  if (event.code === 'Escape') {
    event.preventDefault()
    exitPurity()
    return
  }
  if (event.code === 'Space' || event.code === 'ArrowRight') {
    event.preventDefault()
    cyclePurity()
  }
  if (event.code === 'ArrowLeft') {
    event.preventDefault()
    purityIndex.value = (purityIndex.value + purityColors.length - 1) % purityColors.length
  }
}

onMounted(() => {
  refreshStats()
  raf = requestAnimationFrame(tick)
  window.addEventListener('resize', refreshStats)
  window.addEventListener('keydown', onKey)
  document.addEventListener('fullscreenchange', () => {
    if (!document.fullscreenElement && purity.value) purity.value = false
  })
})

onUnmounted(() => {
  cancelAnimationFrame(raf)
  window.removeEventListener('resize', refreshStats)
  window.removeEventListener('keydown', onKey)
  if (purity.value) exitPurity()
})
</script>
