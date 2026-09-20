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

function readGpu() {
  try {
    const canvas = document.createElement('canvas')
    const gl = canvas.getContext('webgl') || canvas.getContext('experimental-webgl')
    if (!gl) return { webgl: false }
    const ext = gl.getExtension('WEBGL_debug_renderer_info')
    const info = {
      webgl: true,
      vendor: ext ? gl.getParameter(ext.UNMASKED_VENDOR_WEBGL) : gl.getParameter(gl.VENDOR),
      renderer: ext ? gl.getParameter(ext.UNMASKED_RENDERER_WEBGL) : gl.getParameter(gl.RENDERER),
      version: String(gl.getParameter(gl.VERSION) || ''),
      shading: String(gl.getParameter(gl.SHADING_LANGUAGE_VERSION) || '')
    }
    const lose = gl.getExtension('WEBGL_lose_context')
    lose?.loseContext?.()
    return info
  } catch {
    return { webgl: false }
  }
}

async function readWebGpu() {
  if (!navigator.gpu?.requestAdapter) return { available: false }
  try {
    const adapter = await navigator.gpu.requestAdapter()
    if (!adapter) return { available: false }
    const info = adapter.info || {}
    return {
      available: true,
      vendor: info.vendor || '',
      architecture: info.architecture || '',
      device: info.device || '',
      description: info.description || ''
    }
  } catch (error) {
    return { available: false, error: error?.message || String(error) }
  }
}

async function readClientHints() {
  const uaData = navigator.userAgentData
  if (!uaData) {
    return { supported: false, brands: [], platform: '', mobile: null }
  }
  let high = {}
  try {
    high = await uaData.getHighEntropyValues([
      'architecture',
      'bitness',
      'model',
      'platform',
      'platformVersion',
      'uaFullVersion',
      'fullVersionList',
      'wow64',
      'formFactors'
    ])
  } catch {
    high = {}
  }
  return {
    supported: true,
    brands: uaData.brands || [],
    mobile: uaData.mobile ?? null,
    platform: uaData.platform || high.platform || '',
    architecture: high.architecture || '',
    bitness: high.bitness || '',
    model: high.model || '',
    platformVersion: high.platformVersion || '',
    uaFullVersion: high.uaFullVersion || '',
    wow64: high.wow64 ?? null,
    formFactors: high.formFactors || [],
    fullVersionList: (high.fullVersionList || []).map((item) => `${item.brand} ${item.version}`)
  }
}

export async function collectChromeConfig() {
  const hints = await readClientHints()
  const gpu = readGpu()
  const webgpu = await readWebGpu()
  const connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection
  return {
    cpu: { cores: navigator.hardwareConcurrency || 0 },
    memory: { deviceMemoryGB: navigator.deviceMemory || 0 },
    clientHints: hints,
    userAgent: navigator.userAgent,
    language: navigator.language,
    languages: [...(navigator.languages || [])],
    platform: navigator.platform || '',
    vendor: navigator.vendor || '',
    gpu,
    webgpu,
    screen: {
      width: window.screen.width,
      height: window.screen.height,
      availWidth: window.screen.availWidth,
      availHeight: window.screen.availHeight,
      colorDepth: window.screen.colorDepth || 0,
      dpr: window.devicePixelRatio || 1
    },
    network: connection
      ? {
        effectiveType: connection.effectiveType || '',
        downlink: connection.downlink ?? null,
        rtt: connection.rtt ?? null,
        saveData: Boolean(connection.saveData),
        type: connection.type || ''
      }
      : {},
    maxTouchPoints: navigator.maxTouchPoints || 0,
    cookieEnabled: navigator.cookieEnabled,
    onLine: navigator.onLine,
    pdfViewer: navigator.pdfViewerEnabled ?? null,
    secure: window.isSecureContext,
    crossOriginIsolated: window.crossOriginIsolated
  }
}

export async function collectChromeResources() {
  const mem = performance.memory
  const connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection
  let storage = { usage: 0, quota: 0, persisted: null }
  try {
    const estimate = await navigator.storage?.estimate?.()
    storage = {
      usage: estimate?.usage || 0,
      quota: estimate?.quota || 0,
      persisted: navigator.storage?.persisted ? await navigator.storage.persisted() : null
    }
  } catch {
    // Storage API may be blocked
  }
  let battery = null
  try {
    if (navigator.getBattery) {
      const bat = await navigator.getBattery()
      battery = {
        level: Math.round((bat.level || 0) * 100),
        charging: Boolean(bat.charging),
        chargingTime: bat.chargingTime,
        dischargingTime: bat.dischargingTime
      }
    }
  } catch {
    battery = null
  }
  const nav = performance.getEntriesByType?.('navigation')?.[0]
  return {
    heap: mem
      ? {
        used: mem.usedJSHeapSize,
        total: mem.totalJSHeapSize,
        limit: mem.jsHeapSizeLimit
      }
      : null,
    storage,
    network: connection
      ? {
        effectiveType: connection.effectiveType || '',
        downlink: connection.downlink ?? null,
        rtt: connection.rtt ?? null,
        saveData: Boolean(connection.saveData)
      }
      : {},
    battery,
    navigation: nav
      ? {
        ttfb: Math.round(nav.responseStart || 0),
        domContentLoaded: Math.round(nav.domContentLoadedEventEnd || 0),
        load: Math.round(nav.loadEventEnd || 0),
        transferSize: nav.transferSize || 0
      }
      : {},
    resources: performance.getEntriesByType?.('resource')?.length || 0,
    now: Date.now()
  }
}
