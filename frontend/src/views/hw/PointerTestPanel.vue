<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ pointers.length }}</b><span>{{ t('hwPointers') }}</span></div>
      <div class="hw-stat"><b>{{ navigatorTouch }}</b><span>maxTouchPoints</span></div>
      <div class="hw-stat"><b>{{ last.type || '—' }}</b><span>{{ t('hwPointerType') }}</span></div>
      <div class="hw-stat"><b>{{ last.pressure.toFixed(2) }}</b><span>{{ t('hwPressure') }}</span></div>
      <div class="hw-stat"><b>{{ last.tiltX }}, {{ last.tiltY }}</b><span>{{ t('hwTilt') }}</span></div>
    </div>
    <p class="hw-hint">{{ t('hwPointerHint') }}</p>
    <div
      class="hw-pad hw-touch-pad"
      ref="padRef"
      @pointerdown.prevent="onPointer"
      @pointermove.prevent="onPointer"
      @pointerup.prevent="onEnd"
      @pointercancel.prevent="onEnd"
      @contextmenu.prevent
    >
      <div
        v-for="point in pointers"
        :key="point.id"
        class="hw-touch"
        :style="{
          left: `${point.x}px`,
          top: `${point.y}px`,
          width: `${24 + point.pressure * 36}px`,
          height: `${24 + point.pressure * 36}px`,
          background: colorFor(point.type)
        }"
      >
        {{ point.id }}
      </div>
    </div>
    <div class="result" style="margin-top: 16px">
      <div class="result-head"><span>{{ t('hwEventLog') }}</span></div>
      <pre>{{ logText }}</pre>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const padRef = ref(null)
const pointers = ref([])
const last = reactive({ type: '', pressure: 0, tiltX: 0, tiltY: 0 })
const log = ref([])
const navigatorTouch = navigator.maxTouchPoints || 0

const logText = computed(() => log.value.join('\n') || '—')

function colorFor(type) {
  if (type === 'pen') return 'rgba(242, 193, 78, 0.75)'
  if (type === 'touch') return 'rgba(15, 118, 110, 0.65)'
  return 'rgba(103, 232, 249, 0.7)'
}

function localPoint(event) {
  const rect = padRef.value.getBoundingClientRect()
  return { x: event.clientX - rect.left, y: event.clientY - rect.top }
}

function onPointer(event) {
  const { x, y } = localPoint(event)
  last.type = event.pointerType
  last.pressure = event.pressure || 0
  last.tiltX = event.tiltX || 0
  last.tiltY = event.tiltY || 0
  const next = pointers.value.filter((item) => item.id !== event.pointerId)
  next.push({
    id: event.pointerId,
    type: event.pointerType,
    x,
    y,
    pressure: event.pressure || 0
  })
  pointers.value = next
  if (event.type === 'pointerdown') {
    padRef.value?.setPointerCapture?.(event.pointerId)
    log.value = [
      `${event.pointerType}#${event.pointerId} ${event.type} p=${(event.pressure || 0).toFixed(2)}`,
      ...log.value
    ].slice(0, 12)
  }
}

function onEnd(event) {
  pointers.value = pointers.value.filter((item) => item.id !== event.pointerId)
}
</script>
