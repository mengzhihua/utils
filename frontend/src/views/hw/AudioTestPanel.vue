<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ freq }} Hz</b><span>{{ t('hwFrequency') }}</span></div>
      <div class="hw-stat"><b>{{ channelLabel }}</b><span>{{ t('hwChannel') }}</span></div>
    </div>
    <p class="hw-hint">{{ t('hwAudioHint') }}</p>
    <label class="hw-slider">
      <span>{{ t('hwFrequency') }}</span>
      <input v-model.number="freq" type="range" min="120" max="2000" step="10" />
    </label>
    <div class="actions">
      <button class="btn" type="button" @click="play(-1)">{{ t('hwLeft') }}</button>
      <button class="btn" type="button" @click="play(1)">{{ t('hwRight') }}</button>
      <button class="btn secondary" type="button" @click="play(0)">{{ t('hwCenter') }}</button>
      <button class="btn secondary" type="button" @click="sweep">{{ t('hwSweep') }}</button>
      <button class="btn secondary" type="button" @click="stop">{{ t('hwStop') }}</button>
    </div>
  </section>
</template>

<script setup>
import { computed, onUnmounted, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const freq = ref(440)
const pan = ref(0)
const channelLabel = computed(() => (pan.value < 0 ? t('hwLeft') : pan.value > 0 ? t('hwRight') : t('hwCenter')))

let ctx
let osc
let gain
let panner
let sweepTimer = 0

function ensure() {
  if (!ctx) {
    ctx = new (window.AudioContext || window.webkitAudioContext)()
  }
  return ctx.resume()
}

function stop() {
  window.clearInterval(sweepTimer)
  try { osc?.stop() } catch { /* already stopped */ }
  osc = null
}

async function play(side) {
  await ensure()
  stop()
  pan.value = side
  osc = ctx.createOscillator()
  gain = ctx.createGain()
  panner = ctx.createStereoPanner()
  osc.type = 'sine'
  osc.frequency.value = freq.value
  gain.gain.value = 0.18
  panner.pan.value = side
  osc.connect(gain).connect(panner).connect(ctx.destination)
  osc.start()
  window.setTimeout(() => {
    if (osc) stop()
  }, 900)
}

async function sweep() {
  await ensure()
  stop()
  pan.value = 0
  osc = ctx.createOscillator()
  gain = ctx.createGain()
  panner = ctx.createStereoPanner()
  osc.type = 'sine'
  osc.frequency.value = 180
  gain.gain.value = 0.14
  panner.pan.value = 0
  osc.connect(gain).connect(panner).connect(ctx.destination)
  osc.start()
  let hz = 180
  sweepTimer = window.setInterval(() => {
    hz += 40
    freq.value = hz
    if (osc) osc.frequency.value = hz
    if (hz >= 1600) stop()
  }, 40)
}

onUnmounted(() => {
  stop()
  ctx?.close?.()
})
</script>
