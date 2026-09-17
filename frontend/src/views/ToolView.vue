<template>
  <div v-if="tool">
    <div class="topbar">
      <div>
        <h1>{{ tool.title }}</h1>
        <p>{{ tool.summary }} · {{ tool.method }} {{ tool.path }}</p>
      </div>
    </div>

    <section class="panel">
      <form class="form-grid" @submit.prevent="run">
        <div
          v-for="field in tool.fields"
          :key="field.name"
          class="field"
          :class="{ full: field.type === 'textarea' || tool.fields.length === 1 }"
        >
          <label :for="field.name">{{ field.label }}</label>
          <select v-if="field.type === 'select'" :id="field.name" v-model="values[field.name]">
            <option v-for="option in field.options" :key="option.value" :value="option.value">
              {{ option.label }}
            </option>
          </select>
          <textarea
            v-else-if="field.type === 'textarea'"
            :id="field.name"
            v-model="values[field.name]"
          />
          <input v-else :id="field.name" v-model="values[field.name]" />
        </div>
        <div class="field full">
          <div class="actions">
            <button class="btn" type="submit" :disabled="loading">{{ loading ? '执行中...' : '运行' }}</button>
            <button class="btn secondary" type="button" @click="reset">重置样例</button>
          </div>
        </div>
      </form>

      <p v-if="error" class="curl" style="color: var(--danger)">{{ error }}</p>

      <div v-if="result" class="result result-enter">
        <div class="result-head">
          <div>
            <span class="badge" :class="result.ok ? 'ok' : 'bad'">
              {{ result.ok ? 'success' : 'failed' }}
            </span>
            <span class="badge">HTTP {{ result.status }}</span>
            <span class="badge">{{ result.elapsed }}ms</span>
            <span v-if="result.traceId" class="badge">trace {{ result.traceId }}</span>
          </div>
          <button class="btn secondary" type="button" @click="copy">复制 JSON</button>
        </div>
        <pre>{{ pretty }}</pre>
      </div>
      <p v-if="result" class="curl">{{ result.curl }}</p>
    </section>
  </div>
  <div v-else class="panel">未找到该工具</div>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { callTool } from '../api/http'
import { getTool } from '../tools'
import { useToast } from '../composables/useToast'

const props = defineProps({
  id: { type: String, required: true }
})

const toast = useToast()

const tool = computed(() => getTool(props.id))
const values = reactive({})
const loading = ref(false)
const error = ref('')
const result = ref(null)
const pretty = computed(() => JSON.stringify(result.value?.payload, null, 2))

function hydrate() {
  Object.keys(values).forEach((key) => delete values[key])
  for (const field of tool.value?.fields || []) {
    values[field.name] = field.value ?? ''
  }
  result.value = null
  error.value = ''
}

async function run() {
  if (!tool.value) {
    return
  }
  loading.value = true
  error.value = ''
  try {
    result.value = await callTool(tool.value, values)
  } catch (err) {
    error.value = err instanceof Error ? err.message : String(err)
  } finally {
    loading.value = false
  }
}

function reset() {
  hydrate()
}

async function copy() {
  if (pretty.value) {
    await navigator.clipboard.writeText(pretty.value)
    toast.show('已复制')
  }
}

watch(() => props.id, hydrate, { immediate: true })
</script>
