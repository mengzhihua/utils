<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ clicks }}</b><span>{{ t('hwClicks') }}</span></div>
      <div class="hw-stat"><b>{{ cps }}</b><span>CPS</span></div>
      <div class="hw-stat"><b>{{ remain.toFixed(1) }}s</b><span>{{ t('hwRemain') }}</span></div>
      <div class="hw-stat"><b>{{ best }}</b><span>{{ t('hwBest') }}</span></div>
    </div>
    <p class="hw-hint">{{ t('hwClickHint') }}</p>
    <button
      class="hw-click-btn"
      type="button"
      :disabled="ended"
      @click="hit"
    >{{ label }}</button>
    <div class="actions" style="margin-top: 16px">
      <button class="btn secondary" type="button" @click="reset">{{ t('reset') }}</button>
    </div>
  </section>
</template>

<script setup>
import { computed, onUnmounted, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const DURATION = 5
const KEY = 'utils-hw-click-best'
const clicks = ref(0)
const remain = ref(DURATION)
const running = ref(false)
const ended = ref(false)
const best = ref(Number(localStorage.getItem(KEY) || 0))
let timer = 0
let startedAt = 0

const cps = computed(() => {
  const elapsed = DURATION - remain.value
  if (elapsed <= 0) return '0.00'
  return (clicks.value / elapsed).toFixed(2)
})

const label = computed(() => {
  if (ended.value) return `${t('hwClickDone')} ${cps.value} CPS`
  if (running.value) return t('hwClickGo')
  return t('hwClickStart')
})

function hit() {
  if (ended.value) return
  if (!running.value) {
    running.value = true
    startedAt = Date.now()
    timer = window.setInterval(() => {
      const left = Math.max(0, DURATION - (Date.now() - startedAt) / 1000)
      remain.value = left
      if (left <= 0) finish()
    }, 50)
  }
  clicks.value += 1
}

function finish() {
  running.value = false
  ended.value = true
  remain.value = 0
  window.clearInterval(timer)
  const score = Number((clicks.value / DURATION).toFixed(2))
  if (score > best.value) {
    best.value = score
    localStorage.setItem(KEY, String(score))
  }
}

function reset() {
  window.clearInterval(timer)
  clicks.value = 0
  remain.value = DURATION
  running.value = false
  ended.value = false
}

onUnmounted(() => window.clearInterval(timer))
</script>
