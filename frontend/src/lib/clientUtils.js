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
  const nBlocks = ((((n + 8) >> 6) + 1) * 16)
  const words = new Array(nBlocks).fill(0)
  for (let i = 0; i < n; i++) {
    words[i >> 2] |= (bytes.charCodeAt(i) & 0xff) << ((i % 4) * 8)
  }
  words[n >> 2] |= 0x80 << ((n % 4) * 8)
  words[nBlocks - 2] = n * 8
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
    case 'uuid-v7':
      return { uuid: uuidV7() }
    case 'js-escape': {
      const text = values.text || ''
      return { js: escapeJs(text), csv: escapeCsv(text) }
    }
    case 'highlight-local': {
      const html = highlightHtml(values.text || '', values.keyword || '')
      return { html, preview: true }
    }
    case 'zodiac-local': {
      const date = values.date || '1990-03-07'
      const parsed = new Date(`${date}T00:00:00`)
      if (Number.isNaN(parsed.getTime())) {
        throw new Error('无效日期')
      }
      return {
        constellation: constellation(parsed.getMonth() + 1, parsed.getDate()),
        chineseZodiac: chineseZodiac(parsed.getFullYear())
      }
    }
    case 'duration-local': {
      const millis = parseDuration(values.text || '0s')
      const hours = Math.floor(millis / 3_600_000)
      const minutes = Math.floor((millis % 3_600_000) / 60_000)
      const seconds = Math.floor((millis % 60_000) / 1000)
      return { millis, iso: `PT${hours}H${minutes}M${seconds}S`, formatted: `${hours}h ${minutes}m ${seconds}s` }
    }
    case 'slug-local':
      return { slug: toSlug(values.text || '') }
    case 'murmur-local': {
      const hash = murmur32(values.text || '')
      return { murmur32: hash, hex: (hash >>> 0).toString(16).padStart(8, '0') }
    }
    case 'expr-local': {
      const expression = String(values.expression || '').trim()
      if (!/^[\d+\-*/().\s]+$/.test(expression)) {
        throw new Error('只支持数字和 + - * / ( )')
      }
      const result = Function(`"use strict"; return (${expression})`)()
      return { result }
    }
    case 'radix-local': {
      const from = Number(values.from) || 10
      const to = Number(values.to) || 16
      if (from < 2 || from > 36 || to < 2 || to > 36) {
        throw new Error('进制范围 2-36')
      }
      const num = Number.parseInt(String(values.value || '0'), from)
      if (Number.isNaN(num)) {
        throw new Error('无效数值')
      }
      return { converted: num.toString(to), hex: num.toString(16) }
    }
    case 'isbn-local': {
      const compact = String(values.code || '').replace(/[^0-9Xx]/g, '').toUpperCase()
      return { normalized: compact, valid: isIsbn(compact) }
    }
    case 'punycode-local': {
      const domain = String(values.domain || '').trim()
      const ascii = new URL(`http://${domain}`).hostname
      return { ascii, href: `http://${ascii}` }
    }
    case 'roman-local': {
      const raw = String(values.value || '').trim()
      if (/^\d+$/.test(raw)) {
        const roman = toRoman(Number(raw))
        return { roman, number: fromRoman(roman) }
      }
      const number = fromRoman(raw)
      return { number, roman: toRoman(number) }
    }
    case 'unit-local':
      return { value: convertUnit(values.value, values.from, values.to) }
    case 'gcd-local': {
      const a = Math.abs(Number(values.a) || 0)
      const b = Math.abs(Number(values.b) || 0)
      const g = gcd(a, b)
      return { gcd: g, lcm: a === 0 || b === 0 ? 0 : (a / g) * b }
    }
    case 'imei-local': {
      const compact = String(values.value || '').replace(/\D/g, '')
      return { normalized: compact, valid: isImei(compact) }
    }
    case 'url-parse-local': {
      const parsed = new URL(values.url || 'https://example.com')
      return {
        scheme: parsed.protocol.replace(':', ''),
        host: parsed.hostname,
        port: parsed.port || null,
        path: parsed.pathname,
        query: parsed.search.slice(1),
        fragment: parsed.hash.slice(1)
      }
    }
    default:
      throw new Error('unknown client tool')
  }
}

function uuidV7() {
  const bytes = crypto.getRandomValues(new Uint8Array(16))
  const time = BigInt(Date.now())
  bytes[0] = Number((time >> 40n) & 0xffn)
  bytes[1] = Number((time >> 32n) & 0xffn)
  bytes[2] = Number((time >> 24n) & 0xffn)
  bytes[3] = Number((time >> 16n) & 0xffn)
  bytes[4] = Number((time >> 8n) & 0xffn)
  bytes[5] = Number(time & 0xffn)
  bytes[6] = (bytes[6] & 0x0f) | 0x70
  bytes[8] = (bytes[8] & 0x3f) | 0x80
  const hex = [...bytes].map((b) => b.toString(16).padStart(2, '0')).join('')
  return `${hex.slice(0, 8)}-${hex.slice(8, 12)}-${hex.slice(12, 16)}-${hex.slice(16, 20)}-${hex.slice(20)}`
}

function escapeJs(text) {
  return String(text)
    .replaceAll('\\', '\\\\')
    .replaceAll("'", "\\'")
    .replaceAll('"', '\\"')
    .replaceAll('\n', '\\n')
    .replaceAll('\r', '\\r')
    .replaceAll('\t', '\\t')
    .replaceAll('/', '\\/')
}

function escapeCsv(text) {
  const value = String(text)
  if (/[",\n\r]/.test(value)) {
    return `"${value.replaceAll('"', '""')}"`
  }
  return value
}

function highlightHtml(text, keyword) {
  const escaped = escapeHtml(text)
  const needle = escapeHtml(keyword)
  if (!needle) {
    return escaped
  }
  const lower = escaped.toLowerCase()
  const needleLower = needle.toLowerCase()
  let from = 0
  let html = ''
  let index = lower.indexOf(needleLower, from)
  while (index >= 0) {
    html += escaped.slice(from, index) + `<mark>${escaped.slice(index, index + needle.length)}</mark>`
    from = index + needle.length
    index = lower.indexOf(needleLower, from)
  }
  return html + escaped.slice(from)
}

function chineseZodiac(year) {
  const animals = ['鼠', '牛', '虎', '兔', '龙', '蛇', '马', '羊', '猴', '鸡', '狗', '猪']
  return animals[((year - 4) % 12 + 12) % 12]
}

function constellation(month, day) {
  const md = month * 100 + day
  if (md >= 321 && md < 420) return '白羊座'
  if (md >= 420 && md < 521) return '金牛座'
  if (md >= 521 && md < 622) return '双子座'
  if (md >= 622 && md < 723) return '巨蟹座'
  if (md >= 723 && md < 823) return '狮子座'
  if (md >= 823 && md < 923) return '处女座'
  if (md >= 923 && md < 1024) return '天秤座'
  if (md >= 1024 && md < 1123) return '天蝎座'
  if (md >= 1123 && md < 1222) return '射手座'
  if (md >= 1222 || md < 120) return '摩羯座'
  if (md >= 120 && md < 219) return '水瓶座'
  return '双鱼座'
}

function parseDuration(text) {
  const trimmed = String(text).trim()
  if (/^P/i.test(trimmed)) {
    const iso = trimmed.toUpperCase()
    const match = iso.match(/^P(?:(\d+)D)?(?:T(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?)?$/)
    if (!match) {
      throw new Error('无效 ISO 时长')
    }
    return ((Number(match[1]) || 0) * 86400 + (Number(match[2]) || 0) * 3600 + (Number(match[3]) || 0) * 60 + (Number(match[4]) || 0)) * 1000
  }
  if (/^\d+$/.test(trimmed)) {
    return Number(trimmed)
  }
  const token = /(\d+)\s*(ms|s|m|h|d)(?![a-zA-Z])/gi
  let millis = 0
  let consumed = ''
  let found = false
  let match
  while ((match = token.exec(trimmed))) {
    found = true
    consumed += match[0]
    const value = Number(match[1])
    const unit = match[2].toLowerCase()
    millis += unit === 'ms' ? value
      : unit === 's' ? value * 1000
        : unit === 'm' ? value * 60_000
          : unit === 'h' ? value * 3_600_000
            : value * 86_400_000
  }
  if (!found || trimmed.replace(/\s+/g, '') !== consumed.replaceAll(' ', '')) {
    throw new Error('无效时长')
  }
  return millis
}

function toSlug(text) {
  return String(text)
    .trim()
    .normalize('NFKD')
    .replace(/\p{M}/gu, '')
    .replace(/[^\p{L}\p{N}]+/gu, '-')
    .replace(/-{2,}/g, '-')
    .replace(/^-|-$/g, '')
    .toLowerCase()
}

function murmur32(text) {
  const data = new TextEncoder().encode(text)
  const c1 = 0xcc9e2d51
  const c2 = 0x1b873593
  let h1 = 0
  const roundedEnd = (data.length >> 2) << 2
  for (let i = 0; i < roundedEnd; i += 4) {
    let k1 = (data[i] | (data[i + 1] << 8) | (data[i + 2] << 16) | (data[i + 3] << 24)) >>> 0
    k1 = Math.imul(k1, c1)
    k1 = (k1 << 15) | (k1 >>> 17)
    k1 = Math.imul(k1, c2)
    h1 ^= k1
    h1 = (h1 << 13) | (h1 >>> 19)
    h1 = (Math.imul(h1, 5) + 0xe6546b64) | 0
  }
  let k1 = 0
  switch (data.length & 3) {
    case 3:
      k1 ^= data[roundedEnd + 2] << 16
    // fall through
    case 2:
      k1 ^= data[roundedEnd + 1] << 8
    // fall through
    case 1:
      k1 |= data[roundedEnd]
      k1 = Math.imul(k1, c1)
      k1 = (k1 << 15) | (k1 >>> 17)
      k1 = Math.imul(k1, c2)
      h1 ^= k1
      break
    default:
      break
  }
  h1 ^= data.length
  h1 ^= h1 >>> 16
  h1 = Math.imul(h1, 0x85ebca6b)
  h1 ^= h1 >>> 13
  h1 = Math.imul(h1, 0xc2b2ae35)
  h1 ^= h1 >>> 16
  return h1 | 0
}

function toRoman(number) {
  if (number < 1 || number > 3999) {
    throw new Error('罗马数字范围 1-3999')
  }
  const table = [
    [1000, 'M'], [900, 'CM'], [500, 'D'], [400, 'CD'], [100, 'C'], [90, 'XC'],
    [50, 'L'], [40, 'XL'], [10, 'X'], [9, 'IX'], [5, 'V'], [4, 'IV'], [1, 'I']
  ]
  let remaining = number
  let out = ''
  for (const [value, symbol] of table) {
    while (remaining >= value) {
      out += symbol
      remaining -= value
    }
  }
  return out
}

function fromRoman(roman) {
  const text = String(roman).trim().toUpperCase()
  const map = { M: 1000, D: 500, C: 100, L: 50, X: 10, V: 5, I: 1 }
  let value = 0
  for (let i = 0; i < text.length; i++) {
    const cur = map[text[i]]
    const next = map[text[i + 1]]
    if (!cur) {
      throw new Error('无效罗马数字')
    }
    value += next && cur < next ? -cur : cur
  }
  if (toRoman(value) !== text) {
    throw new Error('无效罗马数字')
  }
  return value
}

function gcd(a, b) {
  while (b) {
    const t = a % b
    a = b
    b = t
  }
  return a
}

function convertUnit(value, from, to) {
  const amount = Number(value)
  if (Number.isNaN(amount)) {
    throw new Error('无效数值')
  }
  const length = { mm: 0.001, cm: 0.01, m: 1, km: 1000, in: 0.0254, ft: 0.3048 }
  const mass = { mg: 0.001, g: 1, kg: 1000, t: 1_000_000, lb: 453.59237 }
  const src = String(from || '').toLowerCase()
  const dest = String(to || '').toLowerCase()
  if (length[src] && length[dest]) {
    return amount * length[src] / length[dest]
  }
  if (mass[src] && mass[dest]) {
    return amount * mass[src] / mass[dest]
  }
  const toC = src === 'c' ? amount : src === 'f' ? (amount - 32) * 5 / 9 : src === 'k' ? amount - 273.15 : null
  if (toC == null || !['c', 'f', 'k'].includes(dest)) {
    throw new Error('无法换算这两个单位')
  }
  return dest === 'c' ? toC : dest === 'f' ? toC * 9 / 5 + 32 : toC + 273.15
}

function isImei(compact) {
  if (compact.length !== 15) {
    return false
  }
  let sum = 0
  let doubleDigit = false
  for (let i = compact.length - 1; i >= 0; i--) {
    let n = Number(compact[i])
    if (Number.isNaN(n)) {
      return false
    }
    if (doubleDigit) {
      n *= 2
      if (n > 9) {
        n -= 9
      }
    }
    sum += n
    doubleDigit = !doubleDigit
  }
  return sum % 10 === 0
}

function isIsbn(compact) {
  if (compact.length === 10) {
    let sum = 0
    for (let i = 0; i < 9; i++) {
      const digit = Number(compact[i])
      if (Number.isNaN(digit)) return false
      sum += digit * (10 - i)
    }
    const last = compact[9] === 'X' ? 10 : Number(compact[9])
    return last >= 0 && (sum + last) % 11 === 0
  }
  if (compact.length === 13) {
    let sum = 0
    for (let i = 0; i < 13; i++) {
      const digit = Number(compact[i])
      if (Number.isNaN(digit)) return false
      sum += digit * (i % 2 === 0 ? 1 : 3)
    }
    return sum % 10 === 0
  }
  return false
}
