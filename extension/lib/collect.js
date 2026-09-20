export function formatBytes(value) {
  const n = Number(value)
  if (!Number.isFinite(n) || n < 0) return '—'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let size = n
  let index = 0
  while (size >= 1024 && index < units.length - 1) {
    size /= 1024
    index += 1
  }
  return `${size.toFixed(index === 0 ? 0 : 1)} ${units[index]}`
}

export function cpuPercents(prev, next) {
  if (!prev?.processors?.length || !next?.processors?.length) return []
  return next.processors.map((proc, i) => {
    const before = prev.processors[i]?.usage
    const after = proc.usage
    if (!before || !after) return 0
    const idle = after.idle - before.idle
    const total = after.total - before.total
    if (total <= 0) return 0
    return Math.min(100, Math.max(0, Math.round((1 - idle / total) * 100)))
  })
}

function hasChromeSystem() {
  return Boolean(globalThis.chrome?.system)
}

function readGpu() {
  try {
    const canvas = document.createElement('canvas')
    const gl = canvas.getContext('webgl') || canvas.getContext('experimental-webgl')
    if (!gl) return { vendor: '', renderer: '' }
    const ext = gl.getExtension('WEBGL_debug_renderer_info')
    const info = {
      vendor: ext ? gl.getParameter(ext.UNMASKED_VENDOR_WEBGL) : gl.getParameter(gl.VENDOR),
      renderer: ext ? gl.getParameter(ext.UNMASKED_RENDERER_WEBGL) : gl.getParameter(gl.RENDERER)
    }
    gl.getExtension('WEBGL_lose_context')?.loseContext?.()
    return info
  } catch {
    return { vendor: '', renderer: '' }
  }
}

export async function collectSnapshot(prevCpu) {
  const api = hasChromeSystem()
  const [cpu, memory, storage, displays] = await Promise.all([
    api && chrome.system.cpu?.getInfo ? chrome.system.cpu.getInfo() : Promise.resolve(null),
    api && chrome.system.memory?.getInfo ? chrome.system.memory.getInfo() : Promise.resolve(null),
    api && chrome.system.storage?.getInfo ? chrome.system.storage.getInfo() : Promise.resolve([]),
    api && chrome.system.display?.getInfo ? chrome.system.display.getInfo() : Promise.resolve([])
  ])

  if (api && chrome.system.storage?.getAvailableCapacity && storage?.length) {
    await Promise.all(storage.map(async (item) => {
      try {
        const free = await chrome.system.storage.getAvailableCapacity(item.id)
        item.availableCapacity = free?.availableCapacity || 0
      } catch {
        item.availableCapacity = 0
      }
    }))
  }

  const cores = cpu?.numOfProcessors || navigator.hardwareConcurrency || 0
  const percents = cpuPercents(prevCpu, cpu)
  const avgCpu = percents.length ? Math.round(percents.reduce((a, b) => a + b, 0) / percents.length) : 0
  const ramUsed = memory ? memory.capacity - memory.availableCapacity : 0
  const ramTotal = memory?.capacity || 0
  const ramPct = ramTotal ? Math.round((ramUsed / ramTotal) * 100) : 0

  let hints = {}
  try {
    hints = await navigator.userAgentData?.getHighEntropyValues?.([
      'architecture', 'bitness', 'platform', 'platformVersion', 'model'
    ]) || {}
  } catch {
    hints = {}
  }

  let battery = null
  try {
    if (navigator.getBattery) {
      const bat = await navigator.getBattery()
      battery = { level: Math.round(bat.level * 100), charging: bat.charging }
    }
  } catch {
    battery = null
  }

  const connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection
  const gpu = readGpu()

  return {
    extensionApis: api,
    cpu: {
      model: cpu?.modelName || '',
      arch: cpu?.archName || hints.architecture || '',
      cores,
      features: cpu?.features || [],
      percents,
      average: avgCpu,
      raw: cpu
    },
    memory: {
      used: ramUsed,
      available: memory?.availableCapacity || 0,
      total: ramTotal,
      percent: ramPct,
      deviceMemoryGB: navigator.deviceMemory || 0
    },
    storage: (storage || []).map((item) => ({
      id: item.id,
      name: item.name || item.id,
      type: item.type || 'unknown',
      capacity: item.capacity || 0,
      available: item.availableCapacity || 0
    })),
    displays: (displays || []).map((item) => ({
      name: item.name || '',
      primary: Boolean(item.isPrimary),
      internal: Boolean(item.isInternal),
      width: item.bounds?.width || 0,
      height: item.bounds?.height || 0,
      dpi: Math.round(item.dpiX || 0)
    })),
    gpu,
    hints: {
      platform: hints.platform || navigator.userAgentData?.platform || navigator.platform || '',
      platformVersion: hints.platformVersion || '',
      bitness: hints.bitness || '',
      model: hints.model || ''
    },
    battery,
    network: connection
      ? { type: connection.effectiveType || '', downlink: connection.downlink ?? null, rtt: connection.rtt ?? null }
      : {},
    time: Date.now()
  }
}
