<template>
  <section class="panel hw-panel">
    <div v-if="!pads.length" class="hw-empty">{{ t('hwGamepadEmpty') }}</div>
    <article v-for="pad in pads" :key="pad.index" class="hw-gamepad">
      <header>
        <strong>{{ pad.id }}</strong>
        <span>#{{ pad.index }} · {{ pad.mapping || 'custom' }}</span>
      </header>
      <div class="hw-axes">
        <div v-for="(axis, i) in pad.axes" :key="i" class="hw-axis">
          <div class="hw-axis-cross">
            <i :style="{ left: `${50 + axis * 42}%` }"></i>
          </div>
          <span>axis {{ i }} {{ axis.toFixed(2) }}</span>
        </div>
      </div>
      <div class="hw-gp-buttons">
        <span
          v-for="(btn, i) in pad.buttons"
          :key="i"
          class="hw-gp-btn"
          :class="{ on: btn.pressed }"
        >{{ i }}</span>
      </div>
      <button class="btn secondary" type="button" @click="rumble(pad.index)">{{ t('hwRumble') }}</button>
    </article>
  </section>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const pads = ref([])
let raf = 0

function snapshot() {
  const list = navigator.getGamepads ? [...navigator.getGamepads()].filter(Boolean) : []
  pads.value = list.map((pad) => ({
    index: pad.index,
    id: pad.id,
    mapping: pad.mapping,
    axes: [...pad.axes],
    buttons: pad.buttons.map((btn) => ({ pressed: btn.pressed, value: btn.value }))
  }))
  raf = requestAnimationFrame(snapshot)
}

function rumble(index) {
  const pad = navigator.getGamepads?.()[index]
  pad?.vibrationActuator?.playEffect('dual-rumble', {
    duration: 280,
    strongMagnitude: 1,
    weakMagnitude: 0.4
  }).catch(() => {})
}

onMounted(() => {
  window.addEventListener('gamepadconnected', snapshot)
  window.addEventListener('gamepaddisconnected', snapshot)
  raf = requestAnimationFrame(snapshot)
})

onUnmounted(() => {
  cancelAnimationFrame(raf)
  window.removeEventListener('gamepadconnected', snapshot)
  window.removeEventListener('gamepaddisconnected', snapshot)
})
</script>
