import { ref } from 'vue'

const message = ref('')
const visible = ref(false)
let timer

export function useToast() {
  function show(text) {
    message.value = text
    visible.value = true
    clearTimeout(timer)
    timer = setTimeout(() => {
      visible.value = false
    }, 1800)
  }
  return { message, visible, show }
}
