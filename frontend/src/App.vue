<template>
  <div class="shell">
    <aside class="sidebar" :class="{ open: open }">
      <div class="brand">
        <strong>Java Utils</strong>
        <span>Spring Boot 工具控制台</span>
      </div>
      <nav>
        <RouterLink class="nav-link" to="/" exact-active-class="active" @click="open = false">概览</RouterLink>
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
        <a href="/swagger-ui.html" target="_blank" rel="noreferrer">Swagger UI</a>
        <a href="/actuator/health" target="_blank" rel="noreferrer">Health</a>
      </div>
    </aside>
    <div class="main">
      <button class="menu-btn" type="button" @click="open = !open">菜单</button>
      <RouterView />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { groupedTools } from './tools'

const open = ref(false)
const catalog = groupedTools()
</script>
