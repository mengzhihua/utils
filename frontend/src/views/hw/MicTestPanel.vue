<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ live ? t('hwLive') : t('hwIdle') }}</b><span>{{ t('hwStatus') }}</span></div>
      <div class="hw-stat"><b>{{ level }}%</b><span>{{ t('hwLevel') }}</span></div>
      <div class="hw-stat"><b>{{ peak }} dB</b><span>{{ t('hwPeak') }}</span></div>
      <div class="hw-stat"><b>{{ clipping ? t('hwClipYes') : t('hwClipNo') }}</b><span>{{ t('hwClip') }}</span></div>
      <div class="hw-stat"><b>{{ mics.length }}</b><span>{{ t('hwMics') }}</span></div>
    </div>
    <p class="hw-hint">{{ t('hwMicHint') }}</p>
    <p v-if="error" class="curl" style="color: var(--danger)">{{ error }}</p>
    <label v-if="mics.length" class="hw-slider">
      <span>{{ t('hwDevice') }}</span>
      <select v-model="deviceId" @change="onDevice">
        <option v-for="item in mics" :key="item.deviceId" :value="item.deviceId">{{ item.label }}</option>
      </select>
    </label>
    <div class="actions" style="margin-bottom: 14px">
      <button class="btn" type="button" :disabled="live" @click="start">{{ t('hwStartMic') }}</button>
      <button class="btn secondary" type="button" :disabled="!live" @click="stop">{{ t('hwStop') }}</button>
      <button class="btn secondary" type="button" :disabled="!live" @click="toggleMonitor">{{ monitor ? t('hwMonitorOff') : t('hwMonitorOn') }}</button>
      <button class="btn secondary" type="button" :disabled="!live || recording" @click="record">{{ recording ? t('hwRecording') : t('hwRecord') }}</button>
    </div>
    <div class="hw-meter" :class="{ clip: clipping }">
      <i :style="{ width: `${level}%` }"></i>
    </div>
    <canvas ref="waveRef" class="hw-wave" width="720" height="120"></canvas>
    <audio v-if="clipUrl" class="hw-playback" :src="clipUrl" controls></audio>
  </section>
</template>

<script setup>
import { onUnmounted, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const waveRef = ref(null)
const live = ref(false)
const error = ref('')
const mics = ref([])
const deviceId = ref('')
const level = ref(0)
const peak = ref(-96)
const clipping = ref(false)
const monitor = ref(false)
const recording = ref(false)
const clipUrl = ref('')

let stream
let ctx
let source
let analyser
let gain
let raf = 0
let recorder
let chunks = []

function labelOf(device, index) {
  return device.label || `${t('hwMic')} ${index + 1}`
}

async function listMics() {
  if (!navigator.mediaDevices?.enumerateDevices) return
  const all = await navigator.mediaDevices.enumerateDevices()
  mics.value = all
    .filter((item) => item.kind === 'audioinput')
    .map((item, index) => ({ deviceId: item.deviceId, label: labelOf(item, index) }))
  if (!deviceId.value && mics.value[0]) {
    deviceId.value = mics.value[0].deviceId
  }
}

function draw() {
  if (!analyser) return
  const bins = new Uint8Array(analyser.fftSize)
  analyser.getByteTimeDomainData(bins)
  let sum = 0
  let max = 0
  for (const value of bins) {
    const amp = (value - 128) / 128
    sum += amp * amp
    max = Math.max(max, Math.abs(amp))
  }
  const rms = Math.sqrt(sum / bins.length)
  level.value = Math.min(100, Math.round(rms * 220))
  peak.value = rms > 0 ? Math.round(20 * Math.log10(rms)) : -96
  clipping.value = max > 0.98

  const canvas = waveRef.value
  if (canvas) {
    const g = canvas.getContext('2d')
    const w = canvas.width
    const h = canvas.height
    g.clearRect(0, 0, w, h)
    g.strokeStyle = clipping.value ? '#b42318' : '#0f766e'
    g.lineWidth = 2
    g.beginPath()
    bins.forEach((value, i) => {
      const x = (i / (bins.length - 1)) * w
      const y = ((value - 128) / 128) * (h * 0.42) + h / 2
      if (i === 0) g.moveTo(x, y)
      else g.lineTo(x, y)
    })
    g.stroke()
  }
  raf = requestAnimationFrame(draw)
}

async function start() {
  error.value = ''
  if (!navigator.mediaDevices?.getUserMedia) {
    error.value = t('hwNoMedia')
    return
  }
  stop()
  try {
    stream = await navigator.mediaDevices.getUserMedia({
      audio: {
        deviceId: deviceId.value ? { exact: deviceId.value } : undefined,
        echoCancellation: true,
        noiseSuppression: true
      },
      video: false
    })
    ctx = new (window.AudioContext || window.webkitAudioContext)()
    await ctx.resume()
    source = ctx.createMediaStreamSource(stream)
    analyser = ctx.createAnalyser()
    analyser.fftSize = 2048
    gain = ctx.createGain()
    gain.gain.value = 0
    source.connect(analyser)
    analyser.connect(gain).connect(ctx.destination)
    live.value = true
    await listMics()
    raf = requestAnimationFrame(draw)
  } catch (err) {
    error.value = err?.name === 'NotAllowedError' ? t('hwDenied') : (err?.message || String(err))
    stop()
  }
}

function toggleMonitor() {
  monitor.value = !monitor.value
  if (gain) gain.gain.value = monitor.value ? 0.35 : 0
}

function record() {
  if (!stream || recording.value) return
  if (typeof MediaRecorder === 'undefined') {
    error.value = t('hwNoMedia')
    return
  }
  chunks = []
  if (clipUrl.value) URL.revokeObjectURL(clipUrl.value)
  clipUrl.value = ''
  const mime = MediaRecorder.isTypeSupported('audio/webm') ? 'audio/webm' : ''
  recorder = new MediaRecorder(stream, mime ? { mimeType: mime } : undefined)
  recorder.ondataavailable = (event) => {
    if (event.data.size) chunks.push(event.data)
  }
  recorder.onstop = () => {
    recording.value = false
    if (chunks.length) {
      clipUrl.value = URL.createObjectURL(new Blob(chunks, { type: recorder.mimeType || 'audio/webm' }))
    }
  }
  recording.value = true
  recorder.start()
  window.setTimeout(() => {
    if (recorder && recorder.state === 'recording') recorder.stop()
  }, 4000)
}

function stop() {
  cancelAnimationFrame(raf)
  raf = 0
  if (recorder && recorder.state === 'recording') recorder.stop()
  recorder = null
  stream?.getTracks?.().forEach((track) => track.stop())
  stream = null
  try { ctx?.close?.() } catch { /* already closed */ }
  ctx = null
  source = null
  analyser = null
  gain = null
  live.value = false
  monitor.value = false
  level.value = 0
  peak.value = -96
  clipping.value = false
}

function onDevice() {
  if (live.value) start()
}

onUnmounted(() => {
  if (clipUrl.value) URL.revokeObjectURL(clipUrl.value)
  stop()
})
</script>
