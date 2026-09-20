<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ last.key || '—' }}</b><span>key</span></div>
      <div class="hw-stat"><b>{{ last.code || '—' }}</b><span>code</span></div>
      <div class="hw-stat"><b>{{ locationName }}</b><span>location</span></div>
      <div class="hw-stat"><b>{{ last.repeat ? 'yes' : 'no' }}</b><span>repeat</span></div>
      <div class="hw-stat"><b>{{ heldCount }}</b><span>{{ t('hwHeld') }}</span></div>
    </div>
    <div class="hw-mods">
      <span :class="{ on: mods.ctrl }">Ctrl</span>
      <span :class="{ on: mods.shift }">Shift</span>
      <span :class="{ on: mods.alt }">Alt</span>
      <span :class="{ on: mods.meta }">Meta</span>
      <span :class="{ on: caps }">Caps</span>
    </div>
    <p class="hw-hint">{{ t('hwKeyboardHint') }}</p>
    <div class="hw-keyboard" tabindex="0" ref="padRef">
      <div v-for="(row, ri) in rows" :key="ri" class="hw-key-row">
        <button
          v-for="key in row"
          :key="key.code"
          type="button"
          class="hw-key"
          :class="{ wide: key.wide, space: key.space, on: !!held[key.code] }"
          tabindex="-1"
        >{{ key.label }}</button>
      </div>
      <div class="hw-key-nav">
        <div class="hw-key-row">
          <button v-for="key in navTop" :key="key.code" type="button" class="hw-key" :class="{ on: !!held[key.code] }" tabindex="-1">{{ key.label }}</button>
        </div>
        <div class="hw-key-row">
          <button v-for="key in navMid" :key="key.code" type="button" class="hw-key" :class="{ on: !!held[key.code] }" tabindex="-1">{{ key.label }}</button>
        </div>
        <div class="hw-key-row hw-arrows">
          <span></span>
          <button type="button" class="hw-key" :class="{ on: !!held.ArrowUp }" tabindex="-1">↑</button>
          <span></span>
          <button type="button" class="hw-key" :class="{ on: !!held.ArrowLeft }" tabindex="-1">←</button>
          <button type="button" class="hw-key" :class="{ on: !!held.ArrowDown }" tabindex="-1">↓</button>
          <button type="button" class="hw-key" :class="{ on: !!held.ArrowRight }" tabindex="-1">→</button>
        </div>
      </div>
    </div>
    <div class="result" style="margin-top: 16px">
      <div class="result-head"><span>{{ t('hwEventLog') }}</span></div>
      <pre>{{ logText }}</pre>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, onUnmounted, reactive, ref } from 'vue'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const padRef = ref(null)
const held = reactive({})
const mods = reactive({ ctrl: false, shift: false, alt: false, meta: false })
const last = reactive({ key: '', code: '', location: 0, repeat: false })
const caps = ref(false)
const log = ref([])
const heldCount = computed(() => Object.keys(held).length)

const k = (code, label, extra = {}) => ({ code, label, ...extra })
const rows = [
  [k('Escape', 'Esc'), k('F1', 'F1'), k('F2', 'F2'), k('F3', 'F3'), k('F4', 'F4'), k('F5', 'F5'), k('F6', 'F6'), k('F7', 'F7'), k('F8', 'F8'), k('F9', 'F9'), k('F10', 'F10'), k('F11', 'F11'), k('F12', 'F12')],
  [k('Backquote', '`'), k('Digit1', '1'), k('Digit2', '2'), k('Digit3', '3'), k('Digit4', '4'), k('Digit5', '5'), k('Digit6', '6'), k('Digit7', '7'), k('Digit8', '8'), k('Digit9', '9'), k('Digit0', '0'), k('Minus', '-'), k('Equal', '='), k('Backspace', '⌫', { wide: true })],
  [k('Tab', 'Tab', { wide: true }), k('KeyQ', 'Q'), k('KeyW', 'W'), k('KeyE', 'E'), k('KeyR', 'R'), k('KeyT', 'T'), k('KeyY', 'Y'), k('KeyU', 'U'), k('KeyI', 'I'), k('KeyO', 'O'), k('KeyP', 'P'), k('BracketLeft', '['), k('BracketRight', ']'), k('Backslash', '\\', { wide: true })],
  [k('CapsLock', 'Caps', { wide: true }), k('KeyA', 'A'), k('KeyS', 'S'), k('KeyD', 'D'), k('KeyF', 'F'), k('KeyG', 'G'), k('KeyH', 'H'), k('KeyJ', 'J'), k('KeyK', 'K'), k('KeyL', 'L'), k('Semicolon', ';'), k('Quote', '\''), k('Enter', 'Enter', { wide: true })],
  [k('ShiftLeft', 'Shift', { wide: true }), k('KeyZ', 'Z'), k('KeyX', 'X'), k('KeyC', 'C'), k('KeyV', 'V'), k('KeyB', 'B'), k('KeyN', 'N'), k('KeyM', 'M'), k('Comma', ','), k('Period', '.'), k('Slash', '/'), k('ShiftRight', 'Shift', { wide: true })],
  [k('ControlLeft', 'Ctrl'), k('MetaLeft', 'Win'), k('AltLeft', 'Alt'), k('Space', 'Space', { space: true }), k('AltRight', 'Alt'), k('MetaRight', 'Win'), k('ContextMenu', 'Menu'), k('ControlRight', 'Ctrl')]
]
const navTop = [k('Insert', 'Ins'), k('Home', 'Home'), k('PageUp', 'PgUp')]
const navMid = [k('Delete', 'Del'), k('End', 'End'), k('PageDown', 'PgDn')]

const locationName = computed(() => ({ 0: 'standard', 1: 'left', 2: 'right', 3: 'numpad' }[last.location] || String(last.location)))
const logText = computed(() => log.value.join('\n') || '—')

function syncMods(event) {
  mods.ctrl = event.ctrlKey
  mods.shift = event.shiftKey
  mods.alt = event.altKey
  mods.meta = event.metaKey
}

function onDown(event) {
  if (['Space', 'ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight', 'PageUp', 'PageDown', 'Tab'].includes(event.code)) {
    event.preventDefault()
  }
  held[event.code] = true
  last.key = event.key
  last.code = event.code
  last.location = event.location
  last.repeat = event.repeat
  syncMods(event)
  if (event.code === 'CapsLock') {
    caps.value = event.getModifierState ? event.getModifierState('CapsLock') : !caps.value
  }
  log.value = [
    `${event.type} key=${JSON.stringify(event.key)} code=${event.code} loc=${event.location} repeat=${event.repeat}`,
    ...log.value
  ].slice(0, 16)
}

function onUp(event) {
  delete held[event.code]
  syncMods(event)
}

onMounted(() => {
  window.addEventListener('keydown', onDown)
  window.addEventListener('keyup', onUp)
  padRef.value?.focus()
})

onUnmounted(() => {
  window.removeEventListener('keydown', onDown)
  window.removeEventListener('keyup', onUp)
})
</script>
