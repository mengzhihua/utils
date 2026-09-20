<template>
  <section class="panel hw-panel">
    <div class="hw-stats">
      <div class="hw-stat"><b>{{ cores }}</b><span>{{ t('hwCores') }}</span></div>
      <div class="hw-stat"><b>{{ ram }}</b><span>{{ t('hwDeviceRam') }}</span></div>
      <div class="hw-stat"><b>{{ arch }}</b><span>{{ t('hwArch') }}</span></div>
      <div class="hw-stat"><b>{{ platform }}</b><span>{{ t('hwOs') }}</span></div>
      <div class="hw-stat"><b>{{ gpuShort }}</b><span>{{ t('hwGpu') }}</span></div>
    </div>
    <p class="hw-hint">{{ t('hwSystemHint') }}</p>
    <div class="actions" style="margin-bottom: 14px">
      <button class="btn" type="button" @click="load">{{ t('hwRefresh') }}</button>
    </div>
    <table class="hw-table">
      <tbody>
        <tr v-for="row in rows" :key="row.label">
          <th>{{ row.label }}</th>
          <td>{{ row.value }}</td>
        </tr>
      </tbody>
    </table>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { collectChromeConfig } from '../../lib/chromeSystem'
import { useI18n } from '../../composables/useI18n'

const { t } = useI18n()
const cfg = ref(null)

const cores = computed(() => cfg.value?.cpu?.cores || '—')
const ram = computed(() => {
  const gb = cfg.value?.memory?.deviceMemoryGB
  return gb ? `${gb} GB` : '—'
})
const arch = computed(() => {
  const hints = cfg.value?.clientHints || {}
  return [hints.architecture, hints.bitness && `${hints.bitness}-bit`].filter(Boolean).join(' ') || '—'
})
const platform = computed(() => {
  const hints = cfg.value?.clientHints || {}
  return [hints.platform, hints.platformVersion].filter(Boolean).join(' ') || cfg.value?.platform || '—'
})
const gpuShort = computed(() => {
  const renderer = cfg.value?.gpu?.renderer || cfg.value?.webgpu?.description || ''
  if (!renderer) return '—'
  return renderer.length > 28 ? `${renderer.slice(0, 26)}…` : renderer
})

const rows = computed(() => {
  const data = cfg.value
  if (!data) return []
  const hints = data.clientHints || {}
  const brands = (hints.brands || []).map((item) => `${item.brand} ${item.version}`).join(', ')
  const net = data.network || {}
  return [
    { label: t('hwCores'), value: String(data.cpu?.cores || '—') },
    { label: t('hwDeviceRam'), value: data.memory?.deviceMemoryGB ? `${data.memory.deviceMemoryGB} GB` : t('hwChromeOnly') },
    { label: t('hwArch'), value: [hints.architecture, hints.bitness].filter(Boolean).join(' / ') || '—' },
    { label: t('hwOs'), value: [hints.platform, hints.platformVersion].filter(Boolean).join(' ') || data.platform || '—' },
    { label: t('hwBrowser'), value: brands || data.vendor || '—' },
    { label: t('hwUaFull'), value: hints.uaFullVersion || '—' },
    { label: t('hwGpuVendor'), value: data.gpu?.vendor || data.webgpu?.vendor || '—' },
    { label: t('hwGpu'), value: data.gpu?.renderer || data.webgpu?.description || '—' },
    { label: t('hwWebgpu'), value: data.webgpu?.available ? (data.webgpu.description || data.webgpu.architecture || 'yes') : t('hwUnavailable') },
    { label: t('hwResolution'), value: `${data.screen?.width || 0}×${data.screen?.height || 0} @ ${data.screen?.dpr || 1}` },
    { label: t('hwNetwork'), value: [net.effectiveType, net.downlink != null ? `${net.downlink} Mbps` : '', net.rtt != null ? `${net.rtt} ms` : ''].filter(Boolean).join(' · ') || '—' },
    { label: t('hwLanguage'), value: (data.languages || []).join(', ') || data.language || '—' },
    { label: t('hwSecure'), value: data.secure ? 'https / localhost' : t('hwInsecure') }
  ]
})

async function load() {
  cfg.value = await collectChromeConfig()
}

onMounted(load)
</script>
