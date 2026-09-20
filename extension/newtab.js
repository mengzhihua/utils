import { collectSnapshot, formatBytes } from './lib/collect.js'
import { localePack } from './lib/i18n.js'

const i18n = localePack()

function el(html) {
  const wrap = document.createElement('div')
  wrap.innerHTML = html.trim()
  return wrap.firstElementChild
}

function bar(pct) {
  return `<div class="bar"><i style="width:${Math.min(100, Math.max(0, pct))}%"></i></div>`
}

function diskLabel(item) {
  if (item.available && item.capacity) {
    return `${formatBytes(item.capacity - item.available)} / ${formatBytes(item.capacity)}`
  }
  return formatBytes(item.capacity)
}

function renderClock() {
  const now = new Date()
  const hh = String(now.getHours()).padStart(2, '0')
  const mm = String(now.getMinutes()).padStart(2, '0')
  document.getElementById('clock').textContent = `${hh}:${mm}`
}

function applyChrome() {
  document.documentElement.lang = navigator.language || 'zh-CN'
  document.title = i18n.brand
  document.querySelector('.top h1').textContent = i18n.brand
  document.getElementById('subtitle').textContent = i18n.subtitle
  document.getElementById('q').placeholder = i18n.search
}

function render(snap) {
  const hero = document.getElementById('hero')
  const grid = document.getElementById('grid')
  const ramLabel = snap.memory.total
    ? `${formatBytes(snap.memory.used)} / ${formatBytes(snap.memory.total)}`
    : (snap.memory.deviceMemoryGB ? `≈ ${snap.memory.deviceMemoryGB} GB` : '—')
  const cpuLabel = snap.cpu.percents.length ? `${snap.cpu.average}%` : '—'
  const bat = snap.battery
    ? `${snap.battery.level}%${snap.battery.charging ? ` ${i18n.charging}` : ''}`
    : '—'
  const net = snap.network.type
    ? `${snap.network.type}${snap.network.downlink != null ? ` · ${snap.network.downlink} Mbps` : ''}`
    : '—'

  hero.replaceChildren(
    el(`<article class="card"><h2>${i18n.cpu}</h2><b>${cpuLabel}</b><small>${snap.cpu.cores || '—'} ${i18n.cores}</small>${bar(snap.cpu.average)}</article>`),
    el(`<article class="card"><h2>${i18n.memory}</h2><b>${snap.memory.percent ? `${snap.memory.percent}%` : '—'}</b><small>${ramLabel}</small>${bar(snap.memory.percent)}</article>`),
    el(`<article class="card"><h2>${i18n.battery}</h2><b>${bat}</b><small>${net}</small></article>`),
    el(`<article class="card"><h2>${i18n.system}</h2><b>${snap.hints.platform || '—'}</b><small>${[snap.cpu.arch, snap.hints.bitness && `${snap.hints.bitness}-bit`].filter(Boolean).join(' · ') || snap.hints.platformVersion || '—'}</small></article>`)
  )

  const cores = snap.cpu.percents.map((pct, i) => `
    <div class="core"><span>#${i + 1}</span>${bar(pct)}<span>${pct}%</span></div>
  `).join('') || `<p class="note">${i18n.sampling}</p>`

  const disks = snap.storage.length
    ? `<ul class="list">${snap.storage.map((item) => `<li><span>${item.name} · ${item.type}</span><span>${diskLabel(item)}</span></li>`).join('')}</ul>`
    : `<p class="note">${i18n.noDisks}</p>`

  const screens = snap.displays.length
    ? `<ul class="list">${snap.displays.map((item) => `<li><span>${item.name || (item.primary ? 'Primary' : 'Display')}${item.internal ? ` · ${i18n.builtin}` : ''}</span><span>${item.width}×${item.height}${item.dpi ? ` @ ${item.dpi}dpi` : ''}</span></li>`).join('')}</ul>`
    : `<ul class="list"><li><span>${i18n.viewport}</span><span>${window.screen.width}×${window.screen.height} @ ${window.devicePixelRatio}</span></li></ul>`

  grid.replaceChildren(
    el(`<article class="card"><h2>${i18n.processor}</h2><div>${snap.cpu.model || '—'}</div><div class="cores" style="margin-top:12px">${cores}</div></article>`),
    el(`<article class="card"><h2>${i18n.storage}</h2>${disks}<div style="margin-top:12px">${screens}</div><p class="note" style="margin-top:12px">${snap.gpu.renderer || snap.gpu.vendor || '—'}</p></article>`)
  )

  document.getElementById('note').textContent = snap.extensionApis ? i18n.noteExt : i18n.noteWeb
}

const search = document.getElementById('q')
search.addEventListener('keydown', (event) => {
  if (event.key !== 'Enter') return
  const value = search.value.trim()
  if (/^https?:\/\//i.test(value) || /^[\w-]+\.[\w.-]+(\/|$)/.test(value)) {
    event.preventDefault()
    const url = /^https?:\/\//i.test(value) ? value : `https://${value}`
    location.href = url
  }
})

let prevCpu = null
async function tick() {
  const snap = await collectSnapshot(prevCpu)
  prevCpu = snap.cpu.raw
  render(snap)
}

applyChrome()
renderClock()
window.setInterval(renderClock, 1000)
tick()
window.setInterval(tick, 1000)
