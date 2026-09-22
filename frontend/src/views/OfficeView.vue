<template>
  <div class="home office-page">
    <div class="topbar">
      <div>
        <h1 class="gradient-text">{{ t('officeTitle') }}</h1>
        <p>{{ t('officeLead') }}</p>
      </div>
      <RouterLink class="btn secondary" to="/">{{ t('backOverview') }}</RouterLink>
    </div>
    <div class="office-search">
      <input v-model="query" type="search" :placeholder="t('officeSearch')" />
    </div>
    <p v-if="query && !sections.length" class="home-hint">{{ t('homeNoMatch') }}</p>
    <template v-for="section in sections" :key="section.id">
      <h3 class="section-title">{{ section.label }}</h3>
      <div class="grid">
        <RouterLink
          v-for="(tool, index) in section.tools"
          :key="tool.id"
          class="card reveal office-card"
          :style="{ '--d': `${index * 30}ms` }"
          :to="`/c/${tool.id}`"
        >
          <h2>{{ tool.title }}</h2>
          <p>{{ tool.summary }}</p>
        </RouterLink>
      </div>
    </template>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { clientTools, officeCategories } from '../clientTools'
import { useI18n } from '../composables/useI18n'

const { t } = useI18n()
const query = ref('')

const sections = computed(() => {
  const q = query.value.trim().toLowerCase()
  return officeCategories
    .map((section) => ({
      ...section,
      tools: section.ids
        .map((id) => clientTools.find((tool) => tool.id === id))
        .filter(Boolean)
        .filter((tool) => !q || `${tool.title} ${tool.summary}`.toLowerCase().includes(q))
    }))
    .filter((section) => section.tools.length > 0)
})
</script>
