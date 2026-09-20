<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ pos.x }}, {{ pos.y }}</b><span>{{ t('hwPosition') }}</span></div>
      <div class="hw-stat"><b>{{ pos.dx }}, {{ pos.dy }}</b><span>{{ t('hwMovement') }}</span></div>
      <div class="hw-stat"><b>{{ wheel.y }}</b><span>{{ t('hwWheel') }}</span></div>
      <div class="hw-stat"><b>{{ clicks }}</b><span>{{ t('hwClicks') }}</span></div>
      <div class="hw-stat"><b>{{ doubles }}</b><span>{{ t('hwDblclick') }}</span></div>
    </div>
    <div class="hw-mods">
      <span :class="{ on: buttons[0] }">{{ t('hwLeft') }}</span>
      <span :class="{ on: buttons[1] }">{{ t('hwMiddle') }}</span>
      <span :class="{ on: buttons[2] }">{{ t('hwRight') }}</span>
      <span :class="{ on: buttons[3] }">{{ t('hwMouseBack') }}</span>
      <span :class="{ on: buttons[4] }">{{ t('hwForward') }}</span>
    </div>
    <p class="hw-hint">{{ t('hwMouseHint') }}</p>
    <div
      class="hw-pad"
      ref="padRef"
      @pointerdown="onDown"
      @pointerup="onUp"
      @pointermove="onMove"
      @pointerleave="onLeave"
      @wheel.prevent="onWheel"
      @dblclick="doubles += 1"
      @contextmenu.prevent
    >
      <canvas ref="canvasRef"></canvas>
      <div class="hw-cross" :style="{ left: `${local.x}px`, top: `${local.y}px` }"></div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, onUnmounted, reactive, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const padRef = ref(null)
const canvasRef = ref(null)
const pos = reactive({ x: 0, y: 0, dx: 0, dy: 0 })
const local = reactive({ x: 0, y: 0 })
const wheel = reactive({ x: 0, y: 0, z: 0 })
const buttons = reactive([false, false, false, false, false])
const clicks = ref(0)
const doubles = ref(0)
const trail = []

function sizeCanvas() {
  const canvas = canvasRef.value
  const pad = padRef.value
  if (!canvas || !pad) return
  canvas.width = pad.clientWidth
  canvas.height = pad.clientHeight
}

function draw() {
  const canvas = canvasRef.value
  if (!canvas) return
  const ctx = canvas.getContext('2d')
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  if (trail.length < 2) return
  ctx.lineWidth = 2
  ctx.lineJoin = 'round'
  ctx.lineCap = 'round'
  for (let i = 1; i < trail.length; i += 1) {
    ctx.strokeStyle = `rgba(15, 118, 110, ${i / trail.length})`
    ctx.beginPath()
    ctx.moveTo(trail[i - 1].x, trail[i - 1].y)
    ctx.lineTo(trail[i].x, trail[i].y)
    ctx.stroke()
  }
}

function setButtons(event) {
  for (let i = 0; i < 5; i += 1) {
    buttons[i] = (event.buttons & (1 << i)) !== 0
  }
}

function onMove(event) {
  const rect = padRef.value.getBoundingClientRect()
  pos.x = event.clientX
  pos.y = event.clientY
  pos.dx = event.movementX
  pos.dy = event.movementY
  local.x = event.clientX - rect.left
  local.y = event.clientY - rect.top
  setButtons(event)
  trail.push({ x: local.x, y: local.y })
  if (trail.length > 80) trail.shift()
  draw()
}

function onDown(event) {
  setButtons(event)
  clicks.value += 1
  padRef.value?.setPointerCapture?.(event.pointerId)
}

function onUp(event) {
  setButtons(event)
}

function onLeave() {
  trail.length = 0
  draw()
}

function onWheel(event) {
  wheel.x = event.deltaX
  wheel.y = event.deltaY
  wheel.z = event.deltaZ
}

onMounted(() => {
  sizeCanvas()
  window.addEventListener('resize', sizeCanvas)
})

onUnmounted(() => {
  window.removeEventListener('resize', sizeCanvas)
})
</script>
