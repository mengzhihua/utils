function toBase64(text) {
  return btoa(unescape(encodeURIComponent(text)))
}

function fromBase64(text) {
  return decodeURIComponent(escape(atob(text)))
}

function escapeHtml(text) {
  return String(text)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;')
}

function unescapeHtml(text) {
  return String(text)
    .replaceAll('&lt;', '<')
    .replaceAll('&gt;', '>')
    .replaceAll('&quot;', '"')
    .replaceAll('&#39;', "'")
    .replaceAll('&amp;', '&')
}

function toHalfWidth(text) {
  return [...text].map((ch) => {
    const code = ch.codePointAt(0)
    if (code === 12288) {
      return ' '
    }
    if (code >= 65281 && code <= 65374) {
      return String.fromCharCode(code - 65248)
    }
    return ch
  }).join('')
}

function toFullWidth(text) {
  return [...text].map((ch) => {
    const code = ch.codePointAt(0)
    if (code === 32) {
      return String.fromCharCode(12288)
    }
    if (code >= 33 && code <= 126) {
      return String.fromCharCode(code + 65248)
    }
    return ch
  }).join('')
}

function ipv4ToLong(ip) {
  const parts = ip.split('.').map(Number)
  if (parts.length !== 4 || parts.some((n) => Number.isNaN(n) || n < 0 || n > 255)) {
    throw new Error('无效 IPv4')
  }
  return ((parts[0] << 24) | (parts[1] << 16) | (parts[2] << 8) | parts[3]) >>> 0
}

function longToIpv4(value) {
  return [24, 16, 8, 0].map((shift) => (value >>> shift) & 255).join('.')
}

function hexFromBytes(bytes) {
  return [...bytes].map((b) => b.toString(16).padStart(2, '0')).join('')
}

function md5(text) {
  function cmn(q, a, b, x, s, t) {
    a = (a + q + x + t) | 0
    return (((a << s) | (a >>> (32 - s))) + b) | 0
  }
  const ff = (a, b, c, d, x, s, t) => cmn((b & c) | (~b & d), a, b, x, s, t)
  const gg = (a, b, c, d, x, s, t) => cmn((b & d) | (c & ~d), a, b, x, s, t)
  const hh = (a, b, c, d, x, s, t) => cmn(b ^ c ^ d, a, b, x, s, t)
  const ii = (a, b, c, d, x, s, t) => cmn(c ^ (b | ~d), a, b, x, s, t)
  const bytes = unescape(encodeURIComponent(text))
  const n = bytes.length
  const words = []
  for (let i = 0; i < n; i++) {
    words[i >> 2] |= (bytes.charCodeAt(i) & 0xff) << ((i % 4) * 8)
  }
  words[n >> 2] |= 0x80 << ((n % 4) * 8)
  words[(((n + 8) >> 6) << 4) + 14] = n * 8
  let a = 1732584193
  let b = -271733879
  let c = -1732584194
  let d = 271733878
  for (let i = 0; i < words.length; i += 16) {
    const oa = a
    const ob = b
    const oc = c
    const od = d
    a = ff(a, b, c, d, words[i], 7, -680876936)
    d = ff(d, a, b, c, words[i + 1], 12, -389564586)
    c = ff(c, d, a, b, words[i + 2], 17, 606105819)
    b = ff(b, c, d, a, words[i + 3], 22, -1044525330)
    a = ff(a, b, c, d, words[i + 4], 7, -176418897)
    d = ff(d, a, b, c, words[i + 5], 12, 1200080426)
    c = ff(c, d, a, b, words[i + 6], 17, -1473231341)
    b = ff(b, c, d, a, words[i + 7], 22, -45705983)
    a = ff(a, b, c, d, words[i + 8], 7, 1770035416)
    d = ff(d, a, b, c, words[i + 9], 12, -1958414417)
    c = ff(c, d, a, b, words[i + 10], 17, -42063)
    b = ff(b, c, d, a, words[i + 11], 22, -1990404162)
    a = ff(a, b, c, d, words[i + 12], 7, 1804603682)
    d = ff(d, a, b, c, words[i + 13], 12, -40341101)
    c = ff(c, d, a, b, words[i + 14], 17, -1502002290)
    b = ff(b, c, d, a, words[i + 15], 22, 1236535329)
    a = gg(a, b, c, d, words[i + 1], 5, -165796510)
    d = gg(d, a, b, c, words[i + 6], 9, -1069501632)
    c = gg(c, d, a, b, words[i + 11], 14, 643717713)
    b = gg(b, c, d, a, words[i], 20, -373897302)
    a = gg(a, b, c, d, words[i + 5], 5, -701558691)
    d = gg(d, a, b, c, words[i + 10], 9, 38016083)
    c = gg(c, d, a, b, words[i + 15], 14, -660478335)
    b = gg(b, c, d, a, words[i + 4], 20, -405537848)
    a = gg(a, b, c, d, words[i + 9], 5, 568446438)
    d = gg(d, a, b, c, words[i + 14], 9, -1019803690)
    c = gg(c, d, a, b, words[i + 3], 14, -187363961)
    b = gg(b, c, d, a, words[i + 8], 20, 1163531501)
    a = gg(a, b, c, d, words[i + 13], 5, -1444681467)
    d = gg(d, a, b, c, words[i + 2], 9, -51403784)
    c = gg(c, d, a, b, words[i + 7], 14, 1735328473)
    b = gg(b, c, d, a, words[i + 12], 20, -1926607734)
    a = hh(a, b, c, d, words[i + 5], 4, -378558)
    d = hh(d, a, b, c, words[i + 8], 11, -2022574463)
    c = hh(c, d, a, b, words[i + 11], 16, 1839030562)
    b = hh(b, c, d, a, words[i + 14], 23, -35309556)
    a = hh(a, b, c, d, words[i + 1], 4, -1530992060)
    d = hh(d, a, b, c, words[i + 4], 11, 1272893353)
    c = hh(c, d, a, b, words[i + 7], 16, -155497632)
    b = hh(b, c, d, a, words[i + 10], 23, -1094730640)
    a = hh(a, b, c, d, words[i + 13], 4, 681279174)
    d = hh(d, a, b, c, words[i], 11, -358537222)
    c = hh(c, d, a, b, words[i + 3], 16, -722521979)
    b = hh(b, c, d, a, words[i + 6], 23, 76029189)
    a = hh(a, b, c, d, words[i + 9], 4, -640364487)
    d = hh(d, a, b, c, words[i + 12], 11, -421815835)
    c = hh(c, d, a, b, words[i + 15], 16, 530742520)
    b = hh(b, c, d, a, words[i + 2], 23, -995338651)
    a = ii(a, b, c, d, words[i], 6, -198630844)
    d = ii(d, a, b, c, words[i + 7], 10, 1126891415)
    c = ii(c, d, a, b, words[i + 14], 15, -1416354905)
    b = ii(b, c, d, a, words[i + 5], 21, -57434055)
    a = ii(a, b, c, d, words[i + 12], 6, 1700485571)
    d = ii(d, a, b, c, words[i + 3], 10, -1894986606)
    c = ii(c, d, a, b, words[i + 10], 15, -1051523)
    b = ii(b, c, d, a, words[i + 1], 21, -2054922799)
    a = ii(a, b, c, d, words[i + 8], 6, 1873313359)
    d = ii(d, a, b, c, words[i + 15], 10, -30611744)
    c = ii(c, d, a, b, words[i + 6], 15, -1560198380)
    b = ii(b, c, d, a, words[i + 13], 21, 1309151649)
    a = ii(a, b, c, d, words[i + 4], 6, -145523070)
    d = ii(d, a, b, c, words[i + 11], 10, -1120210379)
    c = ii(c, d, a, b, words[i + 2], 15, 718787259)
    b = ii(b, c, d, a, words[i + 9], 21, -343485551)
    a = (a + oa) | 0
    b = (b + ob) | 0
    c = (c + oc) | 0
    d = (d + od) | 0
  }
  function toHex(num) {
    return [0, 8, 16, 24].map((s) => ((num >>> s) & 0xff).toString(16).padStart(2, '0')).join('')
  }
  return toHex(a) + toHex(b) + toHex(c) + toHex(d)
}

function crc32(text) {
  let crc = 0xffffffff
  const bytes = new TextEncoder().encode(text)
  for (const byte of bytes) {
    crc ^= byte
    for (let i = 0; i < 8; i++) {
      crc = (crc >>> 1) ^ (0xedb88320 & -(crc & 1))
    }
  }
  return ((crc ^ 0xffffffff) >>> 0).toString(16).padStart(8, '0')
}

function hexToRgbHsl(hex) {
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

export async function runClientTool(id, values) {
  switch (id) {
    case 'json-format': {
      const parsed = JSON.parse(values.text || '{}')
      if (values.mode === 'minify') {
        return { minified: JSON.stringify(parsed) }
      }
      return parsed
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
    case 'html-escape': {
      const text = values.text || ''
      return values.mode === 'unescape'
        ? { result: unescapeHtml(text) }
        : { result: escapeHtml(text) }
    }
    case 'unicode': {
      const text = values.text || ''
      if (values.mode === 'decode') {
        return { result: text.replace(/\\u([0-9a-fA-F]{4})/g, (_, hex) => String.fromCharCode(Number.parseInt(hex, 16))) }
      }
      return {
        result: [...text].map((ch) => {
          const code = ch.codePointAt(0)
          return code > 127 ? `\\u${code.toString(16).padStart(4, '0')}` : ch
        }).join('')
      }
    }
    case 'hex-codec': {
      const text = values.text || ''
      if (values.mode === 'decode') {
        const hex = text.replace(/\s+/g, '')
        if (hex.length % 2 !== 0) {
          throw new Error('HEX 长度必须为偶数')
        }
        const bytes = hex.match(/.{2}/g).map((h) => Number.parseInt(h, 16))
        return { decoded: new TextDecoder().decode(new Uint8Array(bytes)) }
      }
      return { encoded: hexFromBytes(new TextEncoder().encode(text)) }
    }
    case 'jwt-decode': {
      const parts = String(values.token || '').split('.')
      if (parts.length < 2) {
        throw new Error('不是有效 JWT')
      }
      const decode = (part) => {
        const padded = part.replace(/-/g, '+').replace(/_/g, '/') + '='.repeat((4 - (part.length % 4)) % 4)
        return JSON.parse(fromBase64(padded))
      }
      return { header: decode(parts[0]), payload: decode(parts[1]), signed: parts.length >= 3 }
    }
    case 'json-csv': {
      const text = values.text || ''
      if (values.mode === 'toJson') {
        const lines = text.trim().split(/\r?\n/).filter(Boolean)
        const headers = lines[0].split(',').map((h) => h.trim())
        const rows = lines.slice(1).map((line) => {
          const cols = line.split(',')
          return Object.fromEntries(headers.map((h, i) => [h, (cols[i] || '').trim()]))
        })
        return { rows }
      }
      const parsed = JSON.parse(text)
      const list = Array.isArray(parsed) ? parsed : [parsed]
      const keys = [...new Set(list.flatMap((row) => Object.keys(row)))]
      const csv = [keys.join(','), ...list.map((row) => keys.map((key) => row[key] ?? '').join(','))].join('\n')
      return { csv }
    }
    case 'xml-format': {
      const parser = new DOMParser()
      const doc = parser.parseFromString(values.text || '', 'application/xml')
      if (doc.querySelector('parsererror')) {
        throw new Error(doc.querySelector('parsererror').textContent || 'XML 解析失败')
      }
      const pretty = new XMLSerializer().serializeToString(doc)
      return { xml: pretty.replace(/></g, '>\n<') }
    }
    case 'markdown': {
      const escaped = escapeHtml(values.text || '')
      const html = escaped
        .replace(/^### (.+)$/gm, '<h3>$1</h3>')
        .replace(/^## (.+)$/gm, '<h2>$1</h2>')
        .replace(/^# (.+)$/gm, '<h1>$1</h1>')
        .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
        .replace(/`([^`]+)`/g, '<code>$1</code>')
        .replace(/^- (.+)$/gm, '<li>$1</li>')
        .replace(/\n/g, '<br>')
      return { html, preview: true }
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
    case 'ulid-local': {
      const time = Date.now()
      const alphabet = '0123456789ABCDEFGHJKMNPQRSTVWXYZ'
      const bytes = crypto.getRandomValues(new Uint8Array(10))
      let out = ''
      let value = time
      for (let i = 0; i < 10; i++) {
        out = alphabet[value & 31] + out
        value = Math.floor(value / 32)
      }
      for (const byte of bytes) {
        out += alphabet[byte % 32]
      }
      return { ulid: out.slice(0, 26) }
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
    case 'case-convert': {
      const text = values.text || ''
      const snake = text.replace(/[A-Z]/g, (c, i) => (i ? '_' : '') + c.toLowerCase()).replace(/-/g, '_')
      const camel = snake.replace(/_([a-z])/g, (_, c) => c.toUpperCase())
      return {
        camel,
        snake: snake.toLowerCase(),
        kebab: snake.toLowerCase().replaceAll('_', '-'),
        pascal: camel ? camel[0].toUpperCase() + camel.slice(1) : ''
      }
    }
    case 'width-convert': {
      const text = values.text || ''
      return { result: values.mode === 'full' ? toFullWidth(text) : toHalfWidth(text) }
    }
    case 'line-tools': {
      let lines = String(values.text || '').split(/\r?\n/)
      if (values.mode === 'trim') {
        lines = lines.map((line) => line.trim()).filter(Boolean)
      } else if (values.mode === 'unique') {
        lines = [...new Set(lines)]
      } else if (values.mode === 'sort') {
        lines = [...lines].sort((a, b) => a.localeCompare(b, 'zh'))
      } else if (values.mode === 'reverse') {
        lines = [...lines].reverse()
      }
      return { result: lines.join('\n'), lines: lines.length }
    }
    case 'text-diff': {
      const left = String(values.left || '').split(/\r?\n/)
      const right = String(values.right || '').split(/\r?\n/)
      const added = right.filter((line) => !left.includes(line))
      const removed = left.filter((line) => !right.includes(line))
      const kept = right.filter((line) => left.includes(line))
      return { added, removed, kept }
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
      const raw = String(values.hex || '#0f766e').trim()
      if (values.mode === 'rgb') {
        const match = raw.match(/(\d+)\s*,\s*(\d+)\s*,\s*(\d+)/)
        if (!match) {
          throw new Error('请输入 rgb，例如 15, 118, 110')
        }
        const hex = `#${[match[1], match[2], match[3]].map((n) => Number(n).toString(16).padStart(2, '0')).join('')}`
        return hexToRgbHsl(hex)
      }
      let hex = raw.startsWith('#') ? raw : `#${raw}`
      if (!/^#([0-9a-fA-F]{6})$/.test(hex)) {
        throw new Error('请输入 6 位 HEX，例如 #0f766e')
      }
      return hexToRgbHsl(hex)
    }
    case 'sha256': {
      const data = new TextEncoder().encode(values.text || '')
      const hash = await crypto.subtle.digest('SHA-256', data)
      return { sha256: hexFromBytes(new Uint8Array(hash)) }
    }
    case 'md5':
      return { md5: md5(values.text || '') }
    case 'hmac': {
      const key = await crypto.subtle.importKey(
        'raw',
        new TextEncoder().encode(values.secret || ''),
        { name: 'HMAC', hash: 'SHA-256' },
        false,
        ['sign']
      )
      const sig = await crypto.subtle.sign('HMAC', key, new TextEncoder().encode(values.text || ''))
      return { hmacSha256: hexFromBytes(new Uint8Array(sig)) }
    }
    case 'crc32':
      return { crc32: crc32(values.text || '') }
    case 'query-parse': {
      const text = values.text || ''
      const query = text.includes('?') ? text.slice(text.indexOf('?') + 1) : text
      return Object.fromEntries(new URLSearchParams(query.split('#')[0]))
    }
    case 'cookie-parse': {
      const map = {}
      for (const part of String(values.text || '').split(';')) {
        const trimmed = part.trim()
        if (!trimmed) {
          continue
        }
        const eq = trimmed.indexOf('=')
        map[eq >= 0 ? trimmed.slice(0, eq) : trimmed] = eq >= 0 ? trimmed.slice(eq + 1) : ''
      }
      return map
    }
    case 'ua-parse': {
      const ua = values.text || ''
      return {
        mobile: /Mobile|Android|iPhone/i.test(ua),
        wechat: /MicroMessenger/i.test(ua),
        chrome: /Chrome/i.test(ua) && !/Edg/i.test(ua),
        firefox: /Firefox/i.test(ua),
        safari: /Safari/i.test(ua) && !/Chrome/i.test(ua),
        ios: /iPhone|iPad/i.test(ua),
        android: /Android/i.test(ua)
      }
    }
    case 'cidr-calc': {
      const ip = values.ip || ''
      const cidr = values.cidr || ''
      const prefix = Number(cidr.split('/')[1])
      const base = ipv4ToLong(cidr.split('/')[0] || ip)
      const mask = prefix === 0 ? 0 : (0xffffffff << (32 - prefix)) >>> 0
      const network = (base & mask) >>> 0
      const broadcast = (network | (~mask >>> 0)) >>> 0
      const value = ipv4ToLong(ip)
      return {
        inCidr: value >= network && value <= broadcast,
        network: longToIpv4(network),
        broadcast: longToIpv4(broadcast),
        hostCount: broadcast - network + 1
      }
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
