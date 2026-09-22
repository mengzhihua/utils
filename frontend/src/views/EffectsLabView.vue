<template>
  <div class="fx-lab">
    <div class="topbar">
      <div>
        <h1 class="gradient-text">{{ t('fxTitle') }}</h1>
        <p>{{ t('fxLead') }}</p>
      </div>
      <RouterLink class="btn secondary" to="/">{{ t('backOverview') }}</RouterLink>
    </div>

    <div class="fx-grid">
      <article class="glass-card tilt" @mousemove="tilt" @mouseleave="untilt">
        <h2>玻璃拟态</h2>
        <p>半透明毛玻璃卡片，适合叠加在动效背景上。</p>
      </article>
      <article class="glow-card">
        <h2>呼吸光晕</h2>
        <p>边框与投影持续呼吸，用来强调主操作。</p>
        <button class="btn glow-btn" type="button" @click="burst">放礼花</button>
      </article>
      <article class="panel">
        <h2>打字机</h2>
        <p class="typewriter">{{ typed }}</p>
      </article>
      <article class="panel">
        <h2>数字跳动</h2>
        <b class="count">{{ displayCount }}</b>
        <p>请求耗时 / 计数器可用同一套缓动。</p>
      </article>
      <article class="ripple-box" @click="ripple">
        <h2>点击涟漪</h2>
        <p>在区域内点击，看波纹扩散。</p>
        <span v-for="item in ripples" :key="item.id" class="ripple" :style="item.style"></span>
      </article>
      <article class="spotlight-card" :style="spotStyle" @mousemove="spot">
        <h2>聚光跟随</h2>
        <p>鼠标在卡片上移动时，高光会跟着走。</p>
      </article>
    </div>
    <canvas ref="canvasRef" class="confetti"></canvas>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useI18n } from '../composables/useI18n'

const { t } = useI18n()

const typed = ref('')
const displayCount = ref(0)
const canvasRef = ref(null)
const ripples = ref([])
const mouse = ref({ x: 80, y: 70 })
const fullText = 'Vue 3 · 过渡、光晕、玻璃拟态、礼花'
let typeTimer
let countTimer
let rippleId = 0

const spotStyle = computed(() => ({
  '--x': `${mouse.value.x}px`,
  '--y': `${mouse.value.y}px`
}))

function spot(event) {
  const rect = event.currentTarget.getBoundingClientRect()
  mouse.value = {
    x: event.clientX - rect.left,
    y: event.clientY - rect.top
  }
}

function tilt(event) {
  const card = event.currentTarget
  const rect = card.getBoundingClientRect()
  const x = (event.clientX - rect.left) / rect.width
  const y = (event.clientY - rect.top) / rect.height
  card.style.transform = `rotateX(${(0.5 - y) * 10}deg) rotateY(${(x - 0.5) * 12}deg)`
}

function untilt(event) {
  event.currentTarget.style.transform = ''
}

function ripple(event) {
  const rect = event.currentTarget.getBoundingClientRect()
  const id = ++rippleId
  ripples.value.push({
    id,
    style: {
      left: `${event.clientX - rect.left}px`,
      top: `${event.clientY - rect.top}px`
    }
  })
  setTimeout(() => {
    ripples.value = ripples.value.filter((item) => item.id !== id)
  }, 700)
}

function burst() {
  const canvas = canvasRef.value
  if (!canvas) {
    return
  }
  const ctx = canvas.getContext('2d')
  canvas.width = window.innerWidth
  canvas.height = window.innerHeight
  const pieces = Array.from({ length: 80 }, () => ({
    x: canvas.width / 2,
    y: canvas.height / 3,
    vx: (Math.random() - 0.5) * 14,
    vy: Math.random() * -12 - 4,
    size: Math.random() * 6 + 3,
    color: ['#0f766e', '#f2c14e', '#67e8f9', '#fb7185'][Math.floor(Math.random() * 4)],
    life: 80
  }))
  function frame() {
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    pieces.forEach((p) => {
      p.vy += 0.28
      p.x += p.vx
      p.y += p.vy
      p.life -= 1
      ctx.globalAlpha = Math.max(0, p.life / 80)
      ctx.fillStyle = p.color
      ctx.fillRect(p.x, p.y, p.size, p.size)
    })
    if (pieces.some((p) => p.life > 0)) {
      requestAnimationFrame(frame)
    } else {
      ctx.clearRect(0, 0, canvas.width, canvas.height)
    }
  }
  frame()
}

onMounted(() => {
  let i = 0
  typeTimer = setInterval(() => {
    typed.value = fullText.slice(0, i)
    i += 1
    if (i > fullText.length) {
      i = 0
    }
  }, 90)
  let n = 0
  countTimer = setInterval(() => {
    if (n < 128) {
      n += 4
      displayCount.value = n
    }
  }, 30)
})

onUnmounted(() => {
  clearInterval(typeTimer)
  clearInterval(countTimer)
})
</script>
