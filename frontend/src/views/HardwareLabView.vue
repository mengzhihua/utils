<template>
  <div class="home office-page">
    <div class="topbar">
      <div>
        <h1 class="gradient-text">{{ t('hwTitle') }}</h1>
        <p>{{ t('hwLead') }}</p>
      </div>
    </div>
    <div class="office-search">
      <input v-model="query" type="search" :placeholder="t('hwSearch')" />
    </div>
    <div class="grid">
      <RouterLink
        v-for="(tool, index) in filtered"
        :key="tool.id"
        class="card reveal office-card hw-card"
        :style="{ '--d': `${index * 30}ms` }"
        :to="`/hw/${tool.id}`"
      >
        <h2>{{ tool.title }}</h2>
        <p>{{ tool.summary }}</p>
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { hardwareTools } from '../hardwareTools'
import { useI18n } from '../composables/useI18n'

const { t } = useI18n()
const query = ref('')

const filtered = computed(() => {
  const q = query.value.trim().toLowerCase()
  return hardwareTools.filter((tool) => !q || `${tool.title} ${tool.summary} ${tool.id}`.toLowerCase().includes(q))
})
</script>
