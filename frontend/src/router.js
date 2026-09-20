import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from './views/HomeView.vue'
import ToolView from './views/ToolView.vue'
import ClientToolView from './views/ClientToolView.vue'
import EffectsLabView from './views/EffectsLabView.vue'
import OfficeView from './views/OfficeView.vue'
import HardwareLabView from './views/HardwareLabView.vue'
import HardwareTestView from './views/HardwareTestView.vue'

export default createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/office', name: 'office', component: OfficeView },
    { path: '/hw', name: 'hardware', component: HardwareLabView },
    { path: '/hw/:id', name: 'hardware-test', component: HardwareTestView, props: true },
    { path: '/fx', name: 'effects', component: EffectsLabView },
    { path: '/c/:id', name: 'client-tool', component: ClientToolView, props: true },
    { path: '/t/:id', name: 'tool', component: ToolView, props: true }
  ]
})
