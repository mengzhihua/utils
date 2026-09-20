import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from './views/HomeView.vue'
import ToolView from './views/ToolView.vue'
import ClientToolView from './views/ClientToolView.vue'
import EffectsLabView from './views/EffectsLabView.vue'
import OfficeView from './views/OfficeView.vue'

export default createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/office', name: 'office', component: OfficeView },
    { path: '/fx', name: 'effects', component: EffectsLabView },
    { path: '/c/:id', name: 'client-tool', component: ClientToolView, props: true },
    { path: '/t/:id', name: 'tool', component: ToolView, props: true }
  ]
})
