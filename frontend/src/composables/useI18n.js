import { computed, ref, watchEffect } from 'vue'
import { messages } from '../i18n/messages'

const KEY = 'utils-locale'
const locale = ref(localStorage.getItem(KEY) === 'en' ? 'en' : 'zh')

watchEffect(() => {
  localStorage.setItem(KEY, locale.value)
  if (typeof document !== 'undefined') {
    document.documentElement.lang = locale.value === 'en' ? 'en' : 'zh-CN'
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
  return locale.value === 'en' ? 'en-US,en;q=0.9' : 'zh-CN,zh;q=0.9,en;q=0.8'
}

export function useI18n() {
  const dict = computed(() => messages[locale.value] || messages.zh)

  function translate(key, fallback) {
    return t(key, fallback)
  }

  function setLocale(next) {
    locale.value = next === 'en' ? 'en' : 'zh'
  }

  function toggle() {
    setLocale(locale.value === 'en' ? 'zh' : 'en')
  }

  return { locale, dict, t: translate, setLocale, toggle, acceptLanguage }
}
