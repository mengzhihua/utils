<template>
  <div>
    <div class="topbar">
      <div>
        <h1>工具控制台</h1>
        <p>在页面上直接调用后端通用工具，方便自测和演示。左侧选择具体能力。</p>
      </div>
    </div>

    <div class="stats">
      <div class="stat">
        <b>{{ health || '...' }}</b>
        <span>应用健康</span>
      </div>
      <div class="stat">
        <b>{{ system.java || '...' }}</b>
        <span>Java 版本</span>
      </div>
      <div class="stat">
        <b>{{ now.dateTime || '...' }}</b>
        <span>服务器时间</span>
      </div>
    </div>

    <div class="actions" style="margin: 0 0 18px">
      <button class="btn" type="button" @click="quick('uuid')">生成 UUID</button>
      <button class="btn secondary" type="button" @click="quick('snowflake')">雪花 ID</button>
      <button class="btn secondary" type="button" @click="quick('order-no')">业务单号</button>
    </div>

    <div v-if="quickResult" class="result" style="margin-bottom: 22px">
      <div class="result-head">
        <span>快捷结果</span>
        <button class="btn secondary" type="button" @click="copy(quickResult)">复制</button>
      </div>
      <pre>{{ quickResult }}</pre>
    </div>

    <div class="grid">
      <RouterLink v-for="tool in tools" :key="tool.id" class="card" :to="`/t/${tool.id}`">
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

const health = ref('')
const now = reactive({})
const system = reactive({})
const quickResult = ref('')

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
}

onMounted(load)
</script>
