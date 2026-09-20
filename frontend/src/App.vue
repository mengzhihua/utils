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

        <div class="nav-group" :class="{ open: isOpen('office') }">
          <button class="nav-parent" type="button" :class="{ current: isOpen('office') }" :aria-expanded="isOpen('office')" @click="toggle('office')">
            <span>{{ t('officeNav') }}</span>
            <small>{{ officeTools.length }}</small>
            <i class="nav-chevron" aria-hidden="true"></i>
          </button>
          <div v-show="isOpen('office')" class="nav-children">
            <RouterLink class="nav-link nav-child" to="/office" active-class="active" @click="open = false">{{ t('navAllOffice') }}</RouterLink>
            <RouterLink
              v-for="tool in officeTools"
              :key="tool.id"
              class="nav-link nav-child"
              :to="`/c/${tool.id}`"
              active-class="active"
              @click="open = false"
            >{{ tool.title }}</RouterLink>
          </div>
        </div>

        <div class="nav-group" :class="{ open: isOpen('hw') }">
          <button class="nav-parent" type="button" :class="{ current: isOpen('hw') }" :aria-expanded="isOpen('hw')" @click="toggle('hw')">
            <span>{{ t('hwNav') }}</span>
            <small>{{ hardwareTools.length }}</small>
            <i class="nav-chevron" aria-hidden="true"></i>
          </button>
          <div v-show="isOpen('hw')" class="nav-children">
            <RouterLink class="nav-link nav-child" to="/hw" exact-active-class="active" @click="open = false">{{ t('navAllHw') }}</RouterLink>
            <RouterLink
              v-for="tool in hardwareTools"
              :key="tool.id"
              class="nav-link nav-child"
              :to="`/hw/${tool.id}`"
              active-class="active"
              @click="open = false"
            >{{ tool.title }}</RouterLink>
          </div>
        </div>

        <RouterLink class="nav-link" to="/fx" active-class="active" @click="open = false">{{ t('effects') }}</RouterLink>

        <p class="nav-section">{{ t('navFrontend') }}</p>
        <div v-for="group in clientCatalog" :key="`c-${group.id}`" class="nav-group" :class="{ open: isOpen(`client:${group.id}`) }">
          <button class="nav-parent" type="button" :class="{ current: isOpen(`client:${group.id}`) }" :aria-expanded="isOpen(`client:${group.id}`)" @click="toggle(`client:${group.id}`)">
            <span>{{ group.label }}</span>
            <small>{{ group.tools.length }}</small>
            <i class="nav-chevron" aria-hidden="true"></i>
          </button>
          <div v-show="isOpen(`client:${group.id}`)" class="nav-children">
            <RouterLink
              v-for="tool in group.tools"
              :key="tool.id"
              class="nav-link nav-child"
              :to="`/c/${tool.id}`"
              active-class="active"
              @click="open = false"
            >{{ tool.title }}</RouterLink>
          </div>
        </div>

        <p class="nav-section">{{ t('navJava') }}</p>
        <div v-for="group in catalog" :key="`j-${group.id}`" class="nav-group" :class="{ open: isOpen(`java:${group.id}`) }">
          <button class="nav-parent" type="button" :class="{ current: isOpen(`java:${group.id}`) }" :aria-expanded="isOpen(`java:${group.id}`)" @click="toggle(`java:${group.id}`)">
            <span>{{ group.label }}</span>
            <small>{{ group.tools.length }}</small>
            <i class="nav-chevron" aria-hidden="true"></i>
          </button>
          <div v-show="isOpen(`java:${group.id}`)" class="nav-children">
            <RouterLink
              v-for="tool in group.tools"
              :key="tool.id"
              class="nav-link nav-child"
              :to="`/t/${tool.id}`"
              active-class="active"
              @click="open = false"
            >{{ tool.title }}</RouterLink>
          </div>
        </div>
      </nav>
      <div class="sidebar-links">
        <div class="locale-switch">
          <button type="button" :class="{ active: locale === 'zh' }" @click="setLocale('zh')">{{ t('localeZh') }}</button>
          <button type="button" :class="{ active: locale === 'en' }" @click="setLocale('en')">{{ t('localeEn') }}</button>
          <button type="button" :class="{ active: locale === 'ja' }" @click="setLocale('ja')">{{ t('localeJa') }}</button>
          <button type="button" :class="{ active: locale === 'de' }" @click="setLocale('de')">{{ t('localeDe') }}</button>
        </div>
        <button class="theme-toggle" type="button" @click="toggleTheme">{{ isDark ? t('themeDark') : t('themeLight') }}</button>
        <a href="/swagger-ui.html" target="_blank" rel="noreferrer">{{ t('swagger') }}</a>
        <a href="/actuator/health" target="_blank" rel="noreferrer">{{ t('health') }}</a>
      </div>
    </aside>
    <div class="main">
      <button class="menu-btn" type="button" @click="open = !open">{{ t('menu') }}</button>
      <RouterView v-slot="{ Component, route: viewRoute }">
        <Transition name="page" mode="out-in">
          <component :is="Component" :key="viewRoute.fullPath" />
        </Transition>
      </RouterView>
    </div>
    <div class="toast" :class="{ show: visible }">{{ message }}</div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { groupedTools, getTool } from './tools'
import { groupedClientTools, getClientTool } from './clientTools'
import { hardwareTools } from './hardwareTools'
import { useTheme } from './composables/useTheme'
import { useToast } from './composables/useToast'
import { useI18n } from './composables/useI18n'

const open = ref(false)
const opened = ref('')
const route = useRoute()
const { t, locale, setLocale } = useI18n()
const catalog = computed(() => groupedTools().map((group) => ({
  ...group,
  label: t(`group.${group.id}`, group.label)
})))
const clientCatalog = computed(() => groupedClientTools()
  .filter((group) => group.id !== 'office')
  .map((group) => ({
    ...group,
    label: t(`group.${group.id}`, group.label)
  })))
const officeTools = computed(() => groupedClientTools().find((group) => group.id === 'office')?.tools || [])
const { isDark, toggle: toggleTheme } = useTheme()
const { message, visible } = useToast()

function currentKey() {
  if (route.path === '/office') return 'office'
  if (route.path.startsWith('/hw')) return 'hw'
  if (route.name === 'client-tool') {
    const group = getClientTool(route.params.id)?.group
    if (group === 'office') return 'office'
    return group ? `client:${group}` : ''
  }
  if (route.name === 'tool') {
    const group = getTool(route.params.id)?.group
    return group ? `java:${group}` : ''
  }
  return ''
}

function isOpen(key) {
  return opened.value === key
}

function toggle(key) {
  opened.value = opened.value === key ? '' : key
}

watch(() => route.fullPath, () => {
  const key = currentKey()
  if (key) opened.value = key
}, { immediate: true })
</script>
