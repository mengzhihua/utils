<template>
  <div v-if="tool" class="hw-page">
    <div class="topbar">
      <div>
        <h1>{{ tool.title }}</h1>
        <p>{{ tool.summary }} · {{ t('localCompute') }}</p>
      </div>
      <RouterLink class="btn secondary" to="/hw">{{ t('hwBack') }}</RouterLink>
    </div>
    <SystemTestPanel v-if="id === 'system'" />
    <ResourcesTestPanel v-else-if="id === 'resources'" />
    <ScreenTestPanel v-else-if="id === 'screen'" />
    <KeyboardTestPanel v-else-if="id === 'keyboard'" />
    <MouseTestPanel v-else-if="id === 'mouse'" />
    <PointerTestPanel v-else-if="id === 'pointer'" />
    <ClickSpeedPanel v-else-if="id === 'click'" />
    <GamepadTestPanel v-else-if="id === 'gamepad'" />
    <AudioTestPanel v-else-if="id === 'audio'" />
    <CameraTestPanel v-else-if="id === 'camera'" />
    <MicTestPanel v-else-if="id === 'mic'" />
  </div>
  <div v-else class="panel">{{ t('unknownTool') }}</div>
</template>

<script setup>
import { computed } from 'vue'
import { getHardwareTool } from '../hardwareTools'
import { useI18n } from '../composables/useI18n'
import SystemTestPanel from './hw/SystemTestPanel.vue'
import ResourcesTestPanel from './hw/ResourcesTestPanel.vue'
import ScreenTestPanel from './hw/ScreenTestPanel.vue'
import KeyboardTestPanel from './hw/KeyboardTestPanel.vue'
import MouseTestPanel from './hw/MouseTestPanel.vue'
import PointerTestPanel from './hw/PointerTestPanel.vue'
import ClickSpeedPanel from './hw/ClickSpeedPanel.vue'
import GamepadTestPanel from './hw/GamepadTestPanel.vue'
import AudioTestPanel from './hw/AudioTestPanel.vue'
import CameraTestPanel from './hw/CameraTestPanel.vue'
import MicTestPanel from './hw/MicTestPanel.vue'

const props = defineProps({
  id: { type: String, required: true }
})

const { t } = useI18n()
const tool = computed(() => getHardwareTool(props.id))
</script>
