<template>
  <div class="home">
    <div class="topbar">
      <div>
        <h1 class="gradient-text">工具控制台</h1>
        <p>后端 Java 工具 + 浏览器本地工具。左侧选择能力，或从下面卡片进入。</p>
      </div>
    </div>

    <div class="stats">
      <div class="stat reveal" style="--d: 0ms">
        <b>{{ health || '...' }}</b>
        <span>应用健康</span>
      </div>
      <div class="stat reveal" style="--d: 80ms">
        <b>{{ system.java || '...' }}</b>
        <span>Java 版本</span>
      </div>
      <div class="stat reveal" style="--d: 160ms">
        <b>{{ now.dateTime || '...' }}</b>
        <span>服务器时间</span>
      </div>
    </div>

    <div class="actions" style="margin: 0 0 18px">
      <button class="btn glow-btn" type="button" @click="quick('uuid')">生成 UUID</button>
      <button class="btn secondary" type="button" @click="quick('snowflake')">雪花 ID</button>
      <button class="btn secondary" type="button" @click="quick('order-no')">业务单号</button>
      <RouterLink class="btn secondary" to="/fx">打开特效实验室</RouterLink>
    </div>

    <div v-if="quickResult" class="result result-enter" style="margin-bottom: 22px">
      <div class="result-head">
        <span>快捷结果</span>
        <button class="btn secondary" type="button" @click="copy(quickResult)">复制</button>
      </div>
      <pre>{{ quickResult }}</pre>
    </div>

    <template v-for="group in clientCatalog" :key="group.id">
      <h3 class="section-title">前端 · {{ group.label }}</h3>
      <div class="grid">
        <RouterLink
          v-for="(tool, index) in group.tools"
          :key="tool.id"
          class="card reveal"
          :style="{ '--d': `${index * 40}ms` }"
          :to="`/c/${tool.id}`"
        >
          <h2>{{ tool.title }}</h2>
          <p>{{ tool.summary }} · 本地</p>
        </RouterLink>
      </div>
    </template>

    <h3 class="section-title">后端 Java 工具</h3>
    <div class="grid">
      <RouterLink
        v-for="(tool, index) in tools"
        :key="tool.id"
        class="card reveal"
        :style="{ '--d': `${index * 18}ms` }"
        :to="`/t/${tool.id}`"
      >
        <h2>{{ tool.title }}</h2>
        <p>{{ tool.summary }} · {{ tool.method }}</p>
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { callTool } from '../api/http'
import { getTool, tools } from '../tools'
import { groupedClientTools } from '../clientTools'
import { useToast } from '../composables/useToast'

const health = ref('')
const now = reactive({})
const system = reactive({})
const quickResult = ref('')
const toast = useToast()
const clientCatalog = groupedClientTools()

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
  toast.show('已复制')
}

onMounted(load)
</script>
