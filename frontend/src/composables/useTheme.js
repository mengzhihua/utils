import { ref, watchEffect } from 'vue'

const isDark = ref(localStorage.getItem('utils-theme') === 'dark')

watchEffect(() => {
  localStorage.setItem('utils-theme', isDark.value ? 'dark' : 'light')
})

export function useTheme() {
  function toggle() {
    isDark.value = !isDark.value
  }
  return { isDark, toggle }
}
