import { computed, ref, watchEffect } from 'vue'
import { messages } from '../i18n/messages'

const KEY = 'utils-locale'
const SUPPORTED = ['zh', 'en', 'ja', 'de']
const ACCEPT = {
  zh: 'zh-CN,zh;q=0.9,en;q=0.8',
  en: 'en-US,en;q=0.9',
  ja: 'ja,en;q=0.8',
  de: 'de-DE,de;q=0.9,en;q=0.8'
}
const HTML_LANG = { zh: 'zh-CN', en: 'en', ja: 'ja', de: 'de' }

function normalize(value) {
  return SUPPORTED.includes(value) ? value : 'zh'
}

const locale = ref(normalize(localStorage.getItem(KEY)))

watchEffect(() => {
  localStorage.setItem(KEY, locale.value)
  if (typeof document !== 'undefined') {
    document.documentElement.lang = HTML_LANG[locale.value] || 'zh-CN'
  }
})

export function t(key, fallback) {
  const table = messages[locale.value] || messages.zh
  if (key.startsWith('group.')) {
    const id = key.slice(6)
    return table.group?.[id] || fallback || id
  }
  return table[key] || fallback || key
}

export function acceptLanguage() {
  return ACCEPT[locale.value] || ACCEPT.zh
}

export function useI18n() {
  const dict = computed(() => messages[locale.value] || messages.zh)

  function translate(key, fallback) {
    return t(key, fallback)
  }

  function setLocale(next) {
    locale.value = normalize(next)
  }

  function toggle() {
    const index = SUPPORTED.indexOf(locale.value)
    setLocale(SUPPORTED[(index + 1) % SUPPORTED.length])
  }

  return { locale, locales: SUPPORTED, dict, t: translate, setLocale, toggle, acceptLanguage }
}
