<template>
  <div class="shell" :class="{ dark: isDark }">
    <div class="fx-bg" aria-hidden="true">
      <span class="orb orb-a"></span>
      <span class="orb orb-b"></span>
      <span class="orb orb-c"></span>
    </div>
    <aside class="sidebar" :class="{ open: open }">
      <div class="brand">
        <strong>{{ t('brand') }}</strong>
        <span>{{ t('brandSub') }}</span>
      </div>
      <nav>
        <RouterLink class="nav-link" to="/" exact-active-class="active" @click="open = false">{{ t('overview') }}</RouterLink>
        <RouterLink class="nav-link" to="/office" active-class="active" @click="open = false">
          {{ t('officeNav') }}
          <small>{{ t('officeHint') }}</small>
        </RouterLink>
        <RouterLink class="nav-link" to="/hw" active-class="active" @click="open = false">
          {{ t('hwNav') }}
          <small>{{ t('hwHint') }}</small>
        </RouterLink>
        <RouterLink class="nav-link" to="/fx" active-class="active" @click="open = false">
          {{ t('effects') }}
          <small>{{ t('effectsHint') }}</small>
        </RouterLink>
        <div class="nav-group" v-for="group in clientCatalog" :key="group.id">
          <h3>{{ group.id === 'office' ? t('officePrefix') : t('frontendPrefix') }} {{ group.label }}</h3>
          <RouterLink
            v-for="tool in group.tools"
            :key="tool.id"
            class="nav-link"
            :to="`/c/${tool.id}`"
            active-class="active"
            @click="open = false"
          >
            {{ tool.title }}
            <small>{{ tool.summary }}</small>
          </RouterLink>
        </div>
        <div v-for="group in catalog" :key="group.id" class="nav-group">
          <h3>{{ group.label }}</h3>
          <RouterLink
            v-for="tool in group.tools"
            :key="tool.id"
            class="nav-link"
            :to="`/t/${tool.id}`"
            active-class="active"
            @click="open = false"
          >
            {{ tool.title }}
            <small>{{ tool.summary }}</small>
          </RouterLink>
        </div>
      </nav>
      <div class="sidebar-links">
        <div class="locale-switch">
          <button type="button" :class="{ active: locale === 'zh' }" @click="setLocale('zh')">{{ t('localeZh') }}</button>
          <button type="button" :class="{ active: locale === 'en' }" @click="setLocale('en')">{{ t('localeEn') }}</button>
          <button type="button" :class="{ active: locale === 'ja' }" @click="setLocale('ja')">{{ t('localeJa') }}</button>
          <button type="button" :class="{ active: locale === 'de' }" @click="setLocale('de')">{{ t('localeDe') }}</button>
        </div>
        <button class="theme-toggle" type="button" @click="toggle">{{ isDark ? t('themeDark') : t('themeLight') }}</button>
        <a href="/swagger-ui.html" target="_blank" rel="noreferrer">{{ t('swagger') }}</a>
        <a href="/actuator/health" target="_blank" rel="noreferrer">{{ t('health') }}</a>
      </div>
    </aside>
    <div class="main">
      <button class="menu-btn" type="button" @click="open = !open">{{ t('menu') }}</button>
      <RouterView v-slot="{ Component, route }">
        <Transition name="page" mode="out-in">
          <component :is="Component" :key="route.fullPath" />
        </Transition>
      </RouterView>
    </div>
    <div class="toast" :class="{ show: visible }">{{ message }}</div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { groupedTools } from './tools'
import { groupedClientTools } from './clientTools'
import { useTheme } from './composables/useTheme'
import { useToast } from './composables/useToast'
import { useI18n } from './composables/useI18n'

const open = ref(false)
const { t, locale, setLocale } = useI18n()
const catalog = computed(() => groupedTools().map((group) => ({
  ...group,
  label: t(`group.${group.id}`, group.label)
})))
const clientCatalog = computed(() => groupedClientTools().map((group) => ({
  ...group,
  label: t(`group.${group.id}`, group.label)
})))
const { isDark, toggle } = useTheme()
const { message, visible } = useToast()
</script>
