<template>
  <div class="shell" :class="{ dark: isDark }">
    <div class="fx-bg" aria-hidden="true">
      <span class="orb orb-a"></span>
      <span class="orb orb-b"></span>
      <span class="orb orb-c"></span>
    </div>
    <aside class="sidebar" :class="{ open: open }">
      <div class="brand">
        <strong>Java Utils</strong>
        <span>Spring Boot 工具控制台</span>
      </div>
      <nav>
        <RouterLink class="nav-link" to="/" exact-active-class="active" @click="open = false">概览</RouterLink>
        <RouterLink class="nav-link" to="/fx" active-class="active" @click="open = false">
          特效实验室
          <small>光晕 / 礼花 / 玻璃拟态</small>
        </RouterLink>
        <div class="nav-group">
          <h3>前端工具</h3>
          <RouterLink
            v-for="tool in clientTools"
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
        <button class="theme-toggle" type="button" @click="toggle">{{ isDark ? '浅色模式' : '深色模式' }}</button>
        <a href="/swagger-ui.html" target="_blank" rel="noreferrer">Swagger UI</a>
        <a href="/actuator/health" target="_blank" rel="noreferrer">Health</a>
      </div>
    </aside>
    <div class="main">
      <button class="menu-btn" type="button" @click="open = !open">菜单</button>
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
import { ref } from 'vue'
import { groupedTools } from './tools'
import { clientTools } from './clientTools'
import { useTheme } from './composables/useTheme'
import { useToast } from './composables/useToast'

const open = ref(false)
const catalog = groupedTools()
const { isDark, toggle } = useTheme()
const { message, visible } = useToast()
</script>
