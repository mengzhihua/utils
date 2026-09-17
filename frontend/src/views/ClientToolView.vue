<template>
  <div v-if="tool" class="tool-page">
    <div class="topbar">
      <div>
        <h1>{{ tool.title }}</h1>
        <p>{{ tool.summary }} · 浏览器本地计算</p>
      </div>
    </div>
    <section class="panel">
      <form class="form-grid" @submit.prevent="run">
        <div
          v-for="field in tool.fields"
          :key="field.name"
          class="field"
          :class="{ full: field.type === 'textarea' || field.type === 'file' || tool.fields.length === 1 }"
        >
          <label :for="field.name">{{ field.label }}</label>
          <select v-if="field.type === 'select'" :id="field.name" v-model="values[field.name]">
            <option v-for="option in field.options" :key="option.value" :value="option.value">
              {{ option.label }}
            </option>
          </select>
          <textarea v-else-if="field.type === 'textarea'" :id="field.name" v-model="values[field.name]" />
          <input v-else-if="field.type === 'file'" :id="field.name" type="file" accept="image/*" @change="onFile" />
          <input v-else :id="field.name" v-model="values[field.name]" />
        </div>
        <div class="field full">
          <div class="actions">
            <button class="btn" type="submit" :disabled="loading">{{ loading ? '计算中...' : '运行' }}</button>
            <button class="btn secondary" type="button" @click="reset">重置样例</button>
          </div>
        </div>
      </form>
      <p v-if="error" class="curl" style="color: var(--danger)">{{ error }}</p>
      <div v-if="colorPreview" class="color-preview" :style="{ background: colorPreview }"></div>
      <img v-if="imagePreview" class="image-preview" :src="imagePreview" alt="preview" />
      <div v-if="htmlPreview" class="html-preview" v-html="htmlPreview"></div>
      <div v-if="pretty" class="result result-enter">
        <div class="result-head">
          <span class="badge ok">local</span>
          <button class="btn secondary" type="button" @click="copy">复制 JSON</button>
        </div>
        <pre>{{ pretty }}</pre>
      </div>
    </section>
  </div>
  <div v-else class="panel">未找到该工具</div>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { getClientTool } from '../clientTools'
import { runClientTool } from '../lib/clientUtils'
import { useToast } from '../composables/useToast'

const props = defineProps({
  id: { type: String, required: true }
})

const toast = useToast()
const tool = computed(() => getClientTool(props.id))
const values = reactive({})
const loading = ref(false)
const error = ref('')
const output = ref(null)
const pretty = computed(() => output.value ? JSON.stringify(output.value, null, 2) : '')
const colorPreview = computed(() => output.value?.preview && !output.value?.html ? output.value.preview : '')
const imagePreview = computed(() => output.value?.dataUrl || '')
const htmlPreview = computed(() => output.value?.html || '')

function hydrate() {
  Object.keys(values).forEach((key) => delete values[key])
  for (const field of tool.value?.fields || []) {
    values[field.name] = field.value ?? ''
  }
  output.value = null
  error.value = ''
}

function onFile(event) {
  const file = event.target.files?.[0]
  if (!file) {
    return
  }
  const reader = new FileReader()
  reader.onload = () => {
    values.dataUrl = String(reader.result)
  }
  reader.readAsDataURL(file)
}

async function run() {
  loading.value = true
  error.value = ''
  try {
    output.value = await runClientTool(props.id, values)
  } catch (err) {
    output.value = null
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
