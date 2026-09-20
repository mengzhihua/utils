import { collectSnapshot, formatBytes } from './lib/collect.js'
import { localePack } from './lib/i18n.js'

const i18n = localePack()

function el(html) {
  const wrap = document.createElement('div')
  wrap.innerHTML = html.trim()
  return wrap.firstElementChild
}

document.documentElement.lang = navigator.language || 'zh-CN'
document.querySelector('.top h1').textContent = i18n.popupTitle
document.getElementById('hint').textContent = i18n.popupHint

let prevCpu = null
async function tick() {
  const snap = await collectSnapshot(prevCpu)
  prevCpu = snap.cpu.raw
  const hero = document.getElementById('hero')
  const ram = snap.memory.total ? `${formatBytes(snap.memory.used)} / ${formatBytes(snap.memory.total)}` : '—'
  hero.replaceChildren(
    el(`<article class="card"><h2>${i18n.cpu}</h2><b>${snap.cpu.percents.length ? `${snap.cpu.average}%` : '—'}</b><small>${snap.cpu.cores} ${i18n.cores}</small><div class="bar"><i style="width:${snap.cpu.average}%"></i></div></article>`),
    el(`<article class="card"><h2>${i18n.memory}</h2><b>${snap.memory.percent ? `${snap.memory.percent}%` : '—'}</b><small>${ram}</small><div class="bar"><i style="width:${snap.memory.percent}%"></i></div></article>`)
  )
}

tick()
window.setInterval(tick, 1000)
