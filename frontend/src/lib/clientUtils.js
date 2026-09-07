function toBase64(text) {
  return btoa(unescape(encodeURIComponent(text)))
}

function fromBase64(text) {
  return decodeURIComponent(escape(atob(text)))
}

export async function runClientTool(id, values) {
  switch (id) {
    case 'json-format': {
      const parsed = JSON.parse(values.text || '{}')
      return {
        result: values.mode === 'minify'
          ? JSON.stringify(parsed)
          : JSON.stringify(parsed, null, 2)
      }
    }
    case 'base64': {
      const text = values.text || ''
      return values.mode === 'decode'
        ? { decoded: fromBase64(text) }
        : { encoded: toBase64(text) }
    }
    case 'url-codec': {
      const text = values.text || ''
      return values.mode === 'decode'
        ? { decoded: decodeURIComponent(text) }
        : { encoded: encodeURIComponent(text) }
    }
    case 'timestamp': {
      const now = Date.now()
      if (values.mode === 'toDate') {
        const ms = Number(values.text)
        const date = new Date(String(values.text).length <= 10 ? ms * 1000 : ms)
        return { iso: date.toISOString(), local: date.toLocaleString() }
      }
      if (values.mode === 'fromDate') {
        const date = new Date(values.text)
        return { millis: date.getTime(), seconds: Math.floor(date.getTime() / 1000) }
      }
      return { millis: now, seconds: Math.floor(now / 1000), iso: new Date(now).toISOString() }
    }
    case 'uuid-local':
      return {
        uuid: crypto.randomUUID(),
        nano: Array.from(crypto.getRandomValues(new Uint8Array(12)))
          .map((n) => '0123456789abcdefghijklmnopqrstuvwxyz'[n % 36])
          .join('')
      }
    case 'password-gen': {
      const length = Math.min(64, Math.max(4, Number(values.length) || 16))
      const alphabet = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%^&*'
      const bytes = crypto.getRandomValues(new Uint8Array(length))
      return { password: Array.from(bytes, (n) => alphabet[n % alphabet.length]).join(''), length }
    }
    case 'word-count': {
      const text = values.text || ''
      const chars = [...text].length
      const words = text.trim() ? text.trim().split(/\s+/).length : 0
      const lines = text ? text.split(/\n/).length : 0
      return { chars, words, lines, bytes: new TextEncoder().encode(text).length }
    }
    case 'regex-test': {
      const flags = values.flags || 'g'
      const regex = new RegExp(values.pattern || '', flags)
      const text = values.text || ''
      const matches = [...text.matchAll(regex)].map((item) => ({
        match: item[0],
        index: item.index,
        groups: item.slice(1)
      }))
      return { matched: matches.length > 0, count: matches.length, matches }
    }
    case 'color-convert': {
      let hex = String(values.hex || '#0f766e').trim()
      if (!hex.startsWith('#')) {
        hex = `#${hex}`
      }
      if (!/^#([0-9a-fA-F]{6})$/.test(hex)) {
        throw new Error('请输入 6 位 HEX，例如 #0f766e')
      }
      const n = Number.parseInt(hex.slice(1), 16)
      const r = (n >> 16) & 255
      const g = (n >> 8) & 255
      const b = n & 255
      const rn = r / 255
      const gn = g / 255
      const bn = b / 255
      const max = Math.max(rn, gn, bn)
      const min = Math.min(rn, gn, bn)
      const l = (max + min) / 2
      let h = 0
      let s = 0
      if (max !== min) {
        const d = max - min
        s = l > 0.5 ? d / (2 - max - min) : d / (max + min)
        if (max === rn) {
          h = (gn - bn) / d + (gn < bn ? 6 : 0)
        } else if (max === gn) {
          h = (bn - rn) / d + 2
        } else {
          h = (rn - gn) / d + 4
        }
        h *= 60
      }
      return {
        hex: hex.toUpperCase(),
        rgb: `rgb(${r}, ${g}, ${b})`,
        hsl: `hsl(${Math.round(h)}, ${Math.round(s * 100)}%, ${Math.round(l * 100)}%)`,
        preview: hex
      }
    }
    case 'sha256': {
      const data = new TextEncoder().encode(values.text || '')
      const hash = await crypto.subtle.digest('SHA-256', data)
      const hex = [...new Uint8Array(hash)].map((b) => b.toString(16).padStart(2, '0')).join('')
      return { sha256: hex }
    }
    case 'image-base64':
      if (!values.dataUrl) {
        throw new Error('请先选择图片')
      }
      return {
        mime: String(values.dataUrl).slice(5, String(values.dataUrl).indexOf(';')),
        length: values.dataUrl.length,
        dataUrl: values.dataUrl
      }
    default:
      throw new Error('unknown client tool')
  }
}
