<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ live ? t('hwLive') : t('hwIdle') }}</b><span>{{ t('hwStatus') }}</span></div>
      <div class="hw-stat"><b>{{ resolution }}</b><span>{{ t('hwCamRes') }}</span></div>
      <div class="hw-stat"><b>{{ fps }}</b><span>{{ t('hwFps') }}</span></div>
      <div class="hw-stat"><b>{{ cameras.length }}</b><span>{{ t('hwCameras') }}</span></div>
      <div class="hw-stat"><b>{{ facing || '—' }}</b><span>{{ t('hwFacing') }}</span></div>
    </div>
    <p class="hw-hint">{{ t('hwCameraHint') }}</p>
    <p v-if="error" class="curl" style="color: var(--danger)">{{ error }}</p>
    <label v-if="cameras.length" class="hw-slider">
      <span>{{ t('hwDevice') }}</span>
      <select v-model="deviceId" @change="onDevice">
        <option v-for="item in cameras" :key="item.deviceId" :value="item.deviceId">{{ item.label }}</option>
      </select>
    </label>
    <div class="actions" style="margin-bottom: 14px">
      <button class="btn" type="button" :disabled="live" @click="start">{{ t('hwStartCamera') }}</button>
      <button class="btn secondary" type="button" :disabled="!live" @click="stop">{{ t('hwStop') }}</button>
      <button class="btn secondary" type="button" @click="mirrored = !mirrored">{{ t('hwMirror') }}</button>
      <button class="btn secondary" type="button" :disabled="!live" @click="snap">{{ t('hwSnapshot') }}</button>
    </div>
    <div class="hw-video-wrap">
      <video ref="videoRef" class="hw-video" :class="{ mirror: mirrored }" autoplay playsinline muted></video>
      <div v-if="!live" class="hw-video-empty">{{ t('hwCameraEmpty') }}</div>
    </div>
    <img v-if="shot" class="image-preview" :src="shot" alt="snapshot" />
  </section>
</template>

<script setup>
import { onUnmounted, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const videoRef = ref(null)
const live = ref(false)
const error = ref('')
const cameras = ref([])
const deviceId = ref('')
const resolution = ref('—')
const fps = ref('—')
const facing = ref('')
const mirrored = ref(true)
const shot = ref('')

let stream
let raf = 0
let frames = 0
let last = 0

function labelOf(device, index) {
  return device.label || `${t('hwCamera')} ${index + 1}`
}

async function listCameras() {
  if (!navigator.mediaDevices?.enumerateDevices) return
  const all = await navigator.mediaDevices.enumerateDevices()
  cameras.value = all
    .filter((item) => item.kind === 'videoinput')
    .map((item, index) => ({ deviceId: item.deviceId, label: labelOf(item, index) }))
  if (!deviceId.value && cameras.value[0]) {
    deviceId.value = cameras.value[0].deviceId
  }
}

function readSettings() {
  const track = stream?.getVideoTracks?.()[0]
  const settings = track?.getSettings?.() || {}
  if (settings.width && settings.height) {
    resolution.value = `${settings.width}×${settings.height}`
  }
  facing.value = settings.facingMode || ''
}

function tick(now) {
  if (!last) last = now
  frames += 1
  if (now - last >= 1000) {
    fps.value = String(frames)
    frames = 0
    last = now
    readSettings()
  }
  raf = requestAnimationFrame(tick)
}

async function start() {
  error.value = ''
  if (!navigator.mediaDevices?.getUserMedia) {
    error.value = t('hwNoMedia')
    return
  }
  stop()
  try {
    const constraints = {
      video: deviceId.value
        ? { deviceId: { exact: deviceId.value } }
        : { facingMode: 'user' },
      audio: false
    }
    stream = await navigator.mediaDevices.getUserMedia(constraints)
    const video = videoRef.value
    if (video) {
      video.srcObject = stream
      await video.play().catch(() => {})
    }
    live.value = true
    await listCameras()
    readSettings()
    frames = 0
    last = 0
    raf = requestAnimationFrame(tick)
  } catch (err) {
    error.value = err?.name === 'NotAllowedError' ? t('hwDenied') : (err?.message || String(err))
    stop()
  }
}

function stop() {
  cancelAnimationFrame(raf)
  raf = 0
  stream?.getTracks?.().forEach((track) => track.stop())
  stream = null
  if (videoRef.value) videoRef.value.srcObject = null
  live.value = false
  fps.value = '—'
}

function onDevice() {
  if (live.value) start()
}

function snap() {
  const video = videoRef.value
  if (!video || !video.videoWidth) return
  const canvas = document.createElement('canvas')
  canvas.width = video.videoWidth
  canvas.height = video.videoHeight
  const ctx = canvas.getContext('2d')
  if (mirrored.value) {
    ctx.translate(canvas.width, 0)
    ctx.scale(-1, 1)
  }
  ctx.drawImage(video, 0, 0)
  shot.value = canvas.toDataURL('image/png')
}

onUnmounted(stop)
</script>
