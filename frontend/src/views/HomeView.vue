<template>
  <div class="home">
    <div class="topbar">
      <div>
        <h1 class="gradient-text">{{ t('homeTitle') }}</h1>
        <p>{{ t('homeLead') }}</p>
      </div>
    </div>
    <div class="office-search">
      <input v-model="query" type="search" :placeholder="t('homeSearch')" />
    </div>
    <p v-if="!needle" class="home-hint">{{ t('homeDefaultHint') }}</p>
    <p v-else-if="!hasMatches" class="home-hint">{{ t('homeNoMatch') }}</p>

    <div v-if="!needle" class="grid">
      <RouterLink class="card reveal launcher-card" to="/office">
        <h2>{{ t('officeNav') }} <small>{{ officeCount }}</small></h2>
        <p>{{ t('officeHint') }}</p>
      </RouterLink>
      <RouterLink class="card reveal launcher-card" to="/hw">
        <h2>{{ t('hwNav') }} <small>{{ hwCount }}</small></h2>
        <p>{{ t('hwHint') }}</p>
      </RouterLink>
      <RouterLink class="card reveal launcher-card" to="/fx">
        <h2>{{ t('effects') }}</h2>
        <p>{{ t('effectsHint') }}</p>
      </RouterLink>
    </div>

    <template v-if="needle && hwCatalog.length">
      <h3 class="section-title">{{ t('hwPrefix') }} {{ t('hwNav') }}</h3>
      <div class="grid">
        <RouterLink
          v-for="(tool, index) in hwCatalog"
          :key="tool.id"
          class="card reveal hw-card"
          :style="{ '--d': `${index * 40}ms` }"
          :to="`/hw/${tool.id}`"
        >
          <h2>{{ tool.title }}</h2>
          <p>{{ tool.summary }} · {{ t('localSuffix') }}</p>
        </RouterLink>
      </div>
    </template>

    <template v-if="needle">
      <template v-for="group in clientCatalog" :key="group.id">
      <h3 class="section-title">{{ group.id === 'office' ? t('officePrefix') : t('frontendPrefix') }} {{ group.label }}</h3>
      <div class="grid">
        <RouterLink
          v-for="(tool, index) in group.tools"
          :key="tool.id"
          class="card reveal"
          :class="{ 'office-card': group.id === 'office' }"
          :style="{ '--d': `${index * 40}ms` }"
          :to="`/c/${tool.id}`"
        >
          <h2>{{ tool.title }}</h2>
          <p>{{ tool.summary }} · {{ t('localSuffix') }}</p>
        </RouterLink>
      </div>
      </template>
    </template>

    <h3 v-if="needle && backendTools.length" class="section-title">{{ t('backendTitle') }}</h3>
    <div v-if="needle && backendTools.length" class="grid">
      <RouterLink
        v-for="(tool, index) in backendTools"
        :key="tool.id"
        class="card reveal"
        :style="{ '--d': `${index * 18}ms` }"
        :to="`/t/${tool.id}`"
      >
        <h2>{{ tool.title }}</h2>
        <p>{{ tool.summary }} · {{ tool.method }}</p>
      </RouterLink>
    </div>

    <div class="stats">
      <div class="stat reveal" style="--d: 0ms">
        <b>{{ health || '...' }}</b>
        <span>{{ t('healthLabel') }}</span>
      </div>
      <div class="stat reveal" style="--d: 80ms">
        <b>{{ system.java || '...' }}</b>
        <span>{{ t('javaLabel') }}</span>
      </div>
      <div class="stat reveal" style="--d: 160ms">
        <b>{{ now.dateTime || '...' }}</b>
        <span>{{ t('timeLabel') }}</span>
      </div>
    </div>

    <div class="actions" style="margin: 18px 0">
      <button class="btn glow-btn" type="button" @click="quick('uuid')">{{ t('quickUuid') }}</button>
      <button class="btn secondary" type="button" @click="quick('snowflake')">{{ t('quickSnowflake') }}</button>
      <button class="btn secondary" type="button" @click="quick('order-no')">{{ t('quickOrder') }}</button>
    </div>

    <div v-if="quickResult" class="result result-enter" style="margin-bottom: 22px">
      <div class="result-head">
        <span>{{ t('quickResult') }}</span>
        <button class="btn secondary" type="button" @click="copy(quickResult)">{{ t('copy') }}</button>
      </div>
      <pre>{{ quickResult }}</pre>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { callTool } from '../api/http'
import { getTool, tools } from '../tools'
import { groupedClientTools } from '../clientTools'
import { hardwareTools } from '../hardwareTools'
import { useToast } from '../composables/useToast'
import { useI18n } from '../composables/useI18n'

const health = ref('')
const now = reactive({})
const system = reactive({})
const quickResult = ref('')
const query = ref('')
const toast = useToast()
const { t } = useI18n()
const needle = computed(() => query.value.trim().toLowerCase())
const matchTool = (tool) => !needle.value || `${tool.title} ${tool.summary} ${tool.id}`.toLowerCase().includes(needle.value)
const clientCatalog = computed(() => groupedClientTools()
  .filter((group) => needle.value || group.id === 'office')
  .map((group) => ({
    ...group,
    label: t(`group.${group.id}`, group.label),
    tools: group.tools.filter(matchTool)
  }))
  .filter((group) => group.tools.length > 0))
const backendTools = computed(() => (needle.value ? tools.filter(matchTool) : []))
const hwCatalog = computed(() => hardwareTools.filter(matchTool))
const officeCount = computed(() => groupedClientTools().find((group) => group.id === 'office')?.tools.length || 0)
const hwCount = hardwareTools.length
const hasMatches = computed(() => clientCatalog.value.length > 0 || backendTools.value.length > 0 || hwCatalog.value.length > 0)

async function load() {
  const [healthRes, nowRes, sysRes] = await Promise.all([
    fetch('/actuator/health').then((r) => r.json()).catch(() => ({ status: 'DOWN' })),
    callTool(getTool('datetime-now'), {}),
    callTool(getTool('system'), {})
  ])
  health.value = healthRes.status || 'UNKNOWN'
  Object.assign(now, nowRes.payload?.data || {})
  Object.assign(system, sysRes.payload?.data || {})
}

async function quick(id) {
  const result = await callTool(getTool(id), {})
  quickResult.value = JSON.stringify(result.payload, null, 2)
}

async function copy(text) {
  await navigator.clipboard.writeText(text)
  toast.show(t('copied'))
}

onMounted(load)
</script>
