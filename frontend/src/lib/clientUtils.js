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
    case 'regex-extract-local':
      return extractFromText(values.text || '')
    case 'regex-escape-local':
      return { escaped: escapeRegex(values.text || '') }
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
    case 'soundex-local': {
      const left = soundex(values.left || '')
      const right = soundex(values.right || '')
      return { left, right, similar: left !== '' && left === right }
    }
    case 'jaro-local':
      return { jaroWinkler: jaroWinkler(values.left || '', values.right || '') }
    case 'iban-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isIban(compact) }
    }
    case 'ean-local': {
      const compact = String(values.value || '').replace(/\D/g, '')
      return { normalized: compact, valid: isEan(compact) }
    }
    case 'age-local': {
      const birthday = new Date(`${values.birthday || '1990-03-07'}T00:00:00`)
      if (Number.isNaN(birthday.getTime())) {
        throw new Error('无效日期')
      }
      const today = new Date()
      let age = today.getFullYear() - birthday.getFullYear()
      const md = today.getMonth() - birthday.getMonth()
      if (md < 0 || (md === 0 && today.getDate() < birthday.getDate())) {
        age--
      }
      return { age }
    }
    case 'rot13-local':
      return { rot13: rot13(values.text || '') }
    case 'morse-local': {
      const encoded = encodeMorse(values.text || 'SOS')
      return { encoded, decoded: decodeMorse(encoded) }
    }
    case 'wildcard-local':
      return { matched: wildcardMatch(values.text || '', values.pattern || '') }
    case 'isin-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isIsin(compact) }
    }
    case 'humanize-local':
      return {
        compact: compactNumber(Number(values.value) || 0),
        ordinal: ordinal(Number(values.ordinal) || 0)
      }
    case 'qp-local': {
      const encoded = encodeQuotedPrintable(values.text || '')
      return { encoded, decoded: decodeQuotedPrintable(encoded) }
    }
    case 'contrast-local':
      return { ratio: contrastRatio(values.left || '#FFFFFF', values.right || '#000000') }
    case 'base45-local': {
      const encoded = encodeBase45(values.text || '')
      return { encoded, decoded: decodeBase45(encoded) }
    }
    case 'accent-local':
      return { stripped: stripAccents(values.text || '') }
    case 'plate-local': {
      const plate = String(values.value || '')
      return { valid: isPlate(plate), newEnergy: /^[\u4e00-\u9fa5][A-Z][A-Z0-9]{5}[A-Z0-9挂学警港澳]$/.test(plate) }
    }
    case 'emoji-local': {
      const extracted = extractEmoji(values.text || '')
      return { contains: extracted.length > 0, count: extracted.length, extracted, removed: removeEmoji(values.text || '') }
    }
    case 'cusip-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isCusip(compact) }
    }
    case 'isbn-convert-local':
      return convertIsbn(values.code || '')
    case 'ipv6-local':
      return formatIpv6(values.ip || '')
    case 'bank-brand-local':
      return { brand: cardBrand(values.cardNo || ''), valid: luhn(String(values.cardNo || '').replace(/\s+/g, '')) }
    case 'between-local':
      return { formatted: formatBetween(Number(values.seconds) || 0) }
    case 'sub-between-local':
      return {
        first: subBetween(values.text || '', values.before || '', values.after || ''),
        all: subBetweenAll(values.text || '', values.before || '', values.after || '')
      }
    case 'pinyin-local':
      return { firstLetters: pinyinFirstLetters(values.text || '') }
    case 'hkid-local': {
      const compact = String(values.value || '').replace(/[()\s]/g, '').toUpperCase()
      return { normalized: compact, valid: isHkId(values.value || '') }
    }
    case 'twid-local':
      return { normalized: String(values.value || '').trim().toUpperCase(), valid: isTwId(values.value || '') }
    case 'org-code-local': {
      const compact = String(values.code || '').replace(/[-\s]/g, '').toUpperCase()
      const complete = compact.length === 8 ? compact + orgCodeCheck(compact) : compact
      return { normalized: compact, complete, valid: isOrgCode(complete) }
    }
    case 'solar-term-local': {
      const year = Number(values.year) || 2026
      return { date: solarTermDate(year, values.name || '清明'), qingming: solarTermDate(year, '清明') }
    }
    case 'ganzhi-local': {
      const year = Number(values.year) || 2026
      return { ganzhi: ganZhiYear(year), animal: ganZhiAnimal(year), formatted: ganZhiYear(year) + ganZhiAnimal(year) }
    }
    case 'aba-local': {
      const digits = String(values.number || '').replace(/\D/g, '')
      return { normalized: digits, valid: isAba(digits) }
    }
    case 'iso6346-local': {
      const compact = String(values.code || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isIso6346(compact) }
    }
    case 'jump-hash-local':
      return { bucket: jumpHash(Number(values.key) || 0, Number(values.buckets) || 100) }
    case 'figi-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isFigi(compact) }
    }
    case 'lei-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isLei(compact) }
    }
    case 'nhs-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isNhs(digits) }
    }
    case 'npi-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isNpi(digits) }
    }
    case 'ismn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isIsmn(digits) }
    }
    case 'nric-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isNric(compact) }
    }
    case 'cologne-local':
      return { code: colognePhonetic(values.text || '') }
    case 'hamming-local': {
      const left = String(values.left || '')
      const right = String(values.right || '')
      if (left.length !== right.length) {
        throw new Error('hamming requires equal length')
      }
      let distance = 0
      for (let i = 0; i < left.length; i++) {
        if (left[i] !== right[i]) distance++
      }
      return { distance }
    }
    case 'uuid-v8-local':
      return { uuid: uuidV8() }
    case 'nysiis-local':
      return { code: nysiis(values.text || '') }
    case 'caverphone-local':
      return { code: caverphone(values.text || '') }
    case 'cpf-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isCpf(digits) }
    }
    case 'cnpj-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isCnpj(digits) }
    }
    case 'pesel-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isPesel(digits), birthDate: isPesel(digits) ? peselBirth(digits) : null }
    }
    case 'upc-e-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      const upcA = isUpcE(digits) ? expandUpcE(digits) : null
      return { normalized: digits, valid: Boolean(upcA), upcA }
    }
    case 'julian-local': {
      const iso = String(values.date || '2000-01-01')
      return { julianDayNumber: julianDayNumber(iso), iso }
    }
    case 'double-metaphone-local': {
      const primary = doubleMetaphone(values.text || '')
      return { primary }
    }
    case 'match-rating-local':
      return {
        left: matchRating(values.left || ''),
        right: matchRating(values.right || ''),
        similar: matchRating(values.left || '') === matchRating(values.right || '')
          || matchRatingSimilar(values.left || '', values.right || '')
      }
    case 'siren-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isSiren(digits) }
    }
    case 'siret-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isSiret(digits), siren: digits.slice(0, 9) }
    }
    case 'nif-local': {
      const compact = String(values.value || '').replace(/[\s.-]/g, '').toUpperCase()
      return { normalized: compact, valid: isNif(compact) }
    }
    case 'isni-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isIsni(compact) }
    }
    case 'bencode-local': {
      const text = String(values.text || '')
      return { encoded: `${text.length}:${text}` }
    }
    case 'refined-soundex-local':
      return { code: refinedSoundex(values.text || '') }
    case 'nir-local': {
      const compact = String(values.value || '').replace(/[\s.-]/g, '').toUpperCase()
      return { normalized: compact, valid: isNir(compact), female: isNir(compact) && '248'.includes(compact[0]) }
    }
    case 'codice-fiscale-local': {
      const compact = String(values.value || '').replace(/[\s.-]/g, '').toUpperCase()
      return { normalized: compact, valid: isCodiceFiscale(compact) }
    }
    case 'steuer-id-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isSteuerId(digits) }
    }
    case 'eori-local': {
      const compact = String(values.value || '').replace(/[\s.-]/g, '').toUpperCase()
      const identifier = compact.slice(2)
      const valid = /^[A-Z]{2}[A-Z0-9]{1,15}$/.test(compact)
        && (compact.startsWith('FR') ? isSiret(identifier) : true)
      return { normalized: compact, country: compact.slice(0, 2), identifier, valid }
    }
    case 'doi-local': {
      let compact = String(values.value || '').trim()
      const lower = compact.toLowerCase()
      if (lower.startsWith('https://doi.org/')) compact = compact.slice(16)
      else if (lower.startsWith('http://doi.org/')) compact = compact.slice(15)
      else if (lower.startsWith('doi:')) compact = compact.slice(4)
      return { normalized: compact.trim(), valid: /^10\.\d{4,9}\/\S+$/i.test(compact.trim()) }
    }
    case 'pmid-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: /^[1-9]\d{0,9}$/.test(digits) }
    }
    case 'iccid-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: /^89\d{17,18}$/.test(digits) && luhnAny(digits) }
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

function uuidV8() {
  const bytes = crypto.getRandomValues(new Uint8Array(16))
  const time = BigInt(Date.now())
  bytes[0] = Number((time >> 40n) & 0xffn)
  bytes[1] = Number((time >> 32n) & 0xffn)
  bytes[2] = Number((time >> 24n) & 0xffn)
  bytes[3] = Number((time >> 16n) & 0xffn)
  bytes[4] = Number((time >> 8n) & 0xffn)
  bytes[5] = Number(time & 0xffn)
  bytes[6] = (bytes[6] & 0x0f) | 0x80
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

function isEan(compact) {
  if (![8, 12, 13, 14].includes(compact.length) || !/^\d+$/.test(compact)) {
    return false
  }
  let sum = 0
  let factor = 3
  for (let i = compact.length - 2; i >= 0; i--) {
    sum += Number(compact[i]) * factor
    factor = 4 - factor
  }
  return Number(compact[compact.length - 1]) === (10 - (sum % 10)) % 10
}

function isIban(compact) {
  if (compact.length < 15 || compact.length > 34 || !/^[A-Z]{2}\d{2}[A-Z0-9]+$/.test(compact)) {
    return false
  }
  const rearranged = compact.slice(4) + compact.slice(0, 4)
  let numeric = ''
  for (const ch of rearranged) {
    numeric += /[A-Z]/.test(ch) ? String(ch.charCodeAt(0) - 55) : ch
  }
  let remainder = 0
  for (const ch of numeric) {
    remainder = (remainder * 10 + Number(ch)) % 97
  }
  return remainder === 1
}

function soundex(text) {
  const map = '01230120022455012623010202'
  const letters = String(text).toUpperCase().replace(/[^A-Z]/g, '')
  if (!letters) {
    return ''
  }
  let out = letters[0]
  let last = map[letters.charCodeAt(0) - 65]
  for (let i = 1; i < letters.length && out.length < 4; i++) {
    const mapped = map[letters.charCodeAt(i) - 65]
    if (mapped !== '0' && mapped !== last) {
      out += mapped
    }
    if (mapped !== '0') {
      last = mapped
    }
  }
  return (out + '000').slice(0, 4)
}

function jaroWinkler(left, right) {
  const j = jaro(left, right)
  let prefix = 0
  const limit = Math.min(4, left.length, right.length)
  while (prefix < limit && left[prefix] === right[prefix]) {
    prefix++
  }
  return j + prefix * 0.1 * (1 - j)
}

function jaro(a, b) {
  if (a === b) {
    return 1
  }
  if (!a.length || !b.length) {
    return 0
  }
  const matchDistance = Math.max(a.length, b.length) / 2 - 1
  const aMatch = Array(a.length).fill(false)
  const bMatch = Array(b.length).fill(false)
  let matches = 0
  for (let i = 0; i < a.length; i++) {
    const from = Math.max(0, i - matchDistance)
    const to = Math.min(i + matchDistance + 1, b.length)
    for (let j = from; j < to; j++) {
      if (bMatch[j] || a[i] !== b[j]) {
        continue
      }
      aMatch[i] = true
      bMatch[j] = true
      matches++
      break
    }
  }
  if (!matches) {
    return 0
  }
  let transpositions = 0
  let k = 0
  for (let i = 0; i < a.length; i++) {
    if (!aMatch[i]) {
      continue
    }
    while (!bMatch[k]) {
      k++
    }
    if (a[i] !== b[k]) {
      transpositions++
    }
    k++
  }
  const m = matches
  return (m / a.length + m / b.length + (m - transpositions / 2) / m) / 3
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

function rot13(text) {
  return String(text).replace(/[a-zA-Z]/g, (ch) => {
    const base = ch <= 'Z' ? 65 : 97
    return String.fromCharCode(base + ((ch.charCodeAt(0) - base + 13) % 26))
  })
}

const MORSE_LETTERS = ['.-', '-...', '-.-.', '-..', '.', '..-.', '--.', '....', '..', '.---', '-.-', '.-..', '--', '-.', '---', '.--.', '--.-', '.-.', '...', '-', '..-', '...-', '.--', '-..-', '-.--', '--..']
const MORSE_DIGITS = ['-----', '.----', '..---', '...--', '....-', '.....', '-....', '--...', '---..', '----.']
const MORSE_DECODE = Object.fromEntries([
  ...MORSE_LETTERS.map((code, i) => [code, String.fromCharCode(65 + i)]),
  ...MORSE_DIGITS.map((code, i) => [code, String(i)])
])

function encodeMorse(text) {
  return String(text).toUpperCase().trim().split(/\s+/).map((word) => [...word].map((ch) => {
    if (ch >= 'A' && ch <= 'Z') return MORSE_LETTERS[ch.charCodeAt(0) - 65]
    if (ch >= '0' && ch <= '9') return MORSE_DIGITS[Number(ch)]
    return ''
  }).filter(Boolean).join(' ')).join(' / ')
}

function decodeMorse(morse) {
  return String(morse).trim().split(/\s+\/\s+/).map((word) => word.split(/\s+/).map((code) => MORSE_DECODE[code] || '').join('')).join(' ')
}

function wildcardMatch(text, pattern) {
  const regex = '^' + String(pattern).replace(/[.+^${}()|[\]\\]/g, '\\$&').replaceAll('*', '.*').replaceAll('?', '.') + '$'
  return new RegExp(regex).test(text)
}

function isIsin(compact) {
  if (compact.length !== 12 || !/^[A-Z]{2}[A-Z0-9]{9}[0-9]$/.test(compact)) {
    return false
  }
  let numeric = ''
  for (const ch of compact.slice(0, 11)) {
    numeric += /[A-Z]/.test(ch) ? String(ch.charCodeAt(0) - 55) : ch
  }
  numeric += compact[11]
  let sum = 0
  let doubleDigit = false
  for (let i = numeric.length - 1; i >= 0; i--) {
    let n = Number(numeric[i])
    if (doubleDigit) {
      n *= 2
      if (n > 9) n -= 9
    }
    sum += n
    doubleDigit = !doubleDigit
  }
  return sum % 10 === 0
}

function compactNumber(value) {
  const abs = Math.abs(value)
  if (abs < 1000) return String(value)
  const units = ['K', 'M', 'B', 'T']
  let scaled = value
  let unit = -1
  while (Math.abs(scaled) >= 1000 && unit + 1 < units.length) {
    scaled /= 1000
    unit++
  }
  const formatted = scaled.toFixed(1).replace(/\.0$/, '')
  return formatted + units[unit]
}

function ordinal(value) {
  const abs = Math.abs(value)
  const mod100 = abs % 100
  let suffix = 'th'
  if (mod100 < 11 || mod100 > 13) {
    suffix = abs % 10 === 1 ? 'st' : abs % 10 === 2 ? 'nd' : abs % 10 === 3 ? 'rd' : 'th'
  }
  return `${value}${suffix}`
}

function encodeQuotedPrintable(text) {
  const bytes = new TextEncoder().encode(text)
  let out = ''
  for (let i = 0; i < bytes.length; i++) {
    const b = bytes[i]
    const encode = b > 126 || b < 32 || b === 61 || ((b === 32 || b === 9) && i === bytes.length - 1)
    out += encode ? `=${b.toString(16).toUpperCase().padStart(2, '0')}` : String.fromCharCode(b)
  }
  return out
}

function decodeQuotedPrintable(text) {
  const compact = String(text).replace(/=\r?\n/g, '')
  const bytes = []
  for (let i = 0; i < compact.length; i++) {
    if (compact[i] === '=' && i + 2 < compact.length) {
      bytes.push(Number.parseInt(compact.slice(i + 1, i + 3), 16))
      i += 2
    } else {
      bytes.push(compact.charCodeAt(i))
    }
  }
  return new TextDecoder().decode(new Uint8Array(bytes))
}

function channel(value) {
  const s = value / 255
  return s <= 0.03928 ? s / 12.92 : ((s + 0.055) / 1.055) ** 2.4
}

function luminance(hex) {
  const value = String(hex).replace('#', '')
  const n = Number.parseInt(value, 16)
  const r = (n >> 16) & 255
  const g = (n >> 8) & 255
  const b = n & 255
  return 0.2126 * channel(r) + 0.7152 * channel(g) + 0.0722 * channel(b)
}

function contrastRatio(left, right) {
  const a = luminance(left)
  const b = luminance(right)
  const light = Math.max(a, b)
  const dark = Math.min(a, b)
  return (light + 0.05) / (dark + 0.05)
}

const BASE45 = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:'

function encodeBase45(text) {
  const bytes = new TextEncoder().encode(text)
  let out = ''
  for (let i = 0; i < bytes.length; i += 2) {
    if (i + 1 < bytes.length) {
      let value = (bytes[i] << 8) | bytes[i + 1]
      const c = value % 45
      value = Math.floor(value / 45)
      const d = value % 45
      const e = Math.floor(value / 45)
      out += BASE45[c] + BASE45[d] + BASE45[e]
    } else {
      const value = bytes[i]
      out += BASE45[value % 45] + BASE45[Math.floor(value / 45)]
    }
  }
  return out
}

function decodeBase45(text) {
  const bytes = []
  for (let i = 0; i < text.length; ) {
    if (i + 2 < text.length) {
      const value = BASE45.indexOf(text[i]) + BASE45.indexOf(text[i + 1]) * 45 + BASE45.indexOf(text[i + 2]) * 2025
      bytes.push((value >> 8) & 255, value & 255)
      i += 3
    } else {
      bytes.push(BASE45.indexOf(text[i]) + BASE45.indexOf(text[i + 1]) * 45)
      i += 2
    }
  }
  return new TextDecoder().decode(new Uint8Array(bytes))
}

function stripAccents(text) {
  return String(text).normalize('NFD').replace(/\p{M}+/gu, '')
}

function isPlate(plate) {
  return /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4,6}[A-HJ-NP-Z0-9挂学警港澳]$/.test(plate)
}

function extractEmoji(text) {
  const chars = []
  for (const ch of text) {
    if (/\p{Extended_Pictographic}/u.test(ch)) {
      chars.push(ch)
    }
  }
  return chars
}

function removeEmoji(text) {
  return String(text).replace(/\p{Extended_Pictographic}/gu, '')
}

function isCusip(compact) {
  if (compact.length !== 9 || !/^[0-9A-Z*@#]{9}$/.test(compact)) {
    return false
  }
  const valueOf = (c) => {
    if (c >= '0' && c <= '9') return c.charCodeAt(0) - 48
    if (c >= 'A' && c <= 'Z') return c.charCodeAt(0) - 55
    if (c === '*') return 36
    if (c === '@') return 37
    if (c === '#') return 38
    return -1
  }
  let sum = 0
  for (let i = 0; i < 8; i++) {
    const weighted = valueOf(compact[i]) * ((9 - i) % 2 === 0 ? 2 : 1)
    sum += Math.floor(weighted / 10) + (weighted % 10)
  }
  return compact[8] === String((10 - (sum % 10)) % 10)
}

function extractFromText(text) {
  const source = String(text || '')
  return {
    mobiles: source.match(/(?<!\d)1[3-9]\d{9}(?!\d)/g) || [],
    emails: source.match(/[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}/g) || [],
    urls: source.match(/https?:\/\/[\w.-]+(?:\.[\w.-]+)+(?:[/#?][^\s]*)?/gi) || [],
    ipv4: source.match(/(?:(?:25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3}(?:25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)/g) || [],
    dates: source.match(/\d{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\d|3[01])/g) || [],
    hexColors: source.match(/#(?:[0-9a-fA-F]{8}|[0-9a-fA-F]{6}|[0-9a-fA-F]{3})(?![0-9A-Fa-f])/g) || []
  }
}

function escapeRegex(text) {
  return String(text).replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
}

function convertIsbn(code) {
  const compact = String(code || '').replace(/[^0-9Xx]/g, '').toUpperCase()
  const valid = isIsbn(compact)
  const out = { normalized: compact, valid }
  if (!valid) {
    return out
  }
  if (compact.length === 10) {
    out.isbn10 = compact
    out.isbn13 = isbn10To13(compact)
  } else {
    out.isbn13 = compact
    if (compact.startsWith('978')) {
      out.isbn10 = isbn13To10(compact)
    }
  }
  return out
}

function isbn10To13(isbn10) {
  const body = `978${compact9(isbn10)}`
  let sum = 0
  for (let i = 0; i < 12; i++) {
    sum += Number(body[i]) * (i % 2 === 0 ? 1 : 3)
  }
  return body + String((10 - (sum % 10)) % 10)
}

function compact9(isbn10) {
  return isbn10.slice(0, 9)
}

function isbn13To10(isbn13) {
  const body = isbn13.slice(3, 12)
  let sum = 0
  for (let i = 0; i < 9; i++) {
    sum += Number(body[i]) * (10 - i)
  }
  const check = (11 - (sum % 11)) % 11
  return body + (check === 10 ? 'X' : String(check))
}

function parseIpv6(ip) {
  let value = String(ip || '').trim()
  const zone = value.indexOf('%')
  if (zone >= 0) {
    value = value.slice(0, zone)
  }
  if (!value || (value.startsWith(':') && !value.startsWith('::')) || (value.endsWith(':') && !value.endsWith('::'))) {
    return null
  }
  if (value.indexOf('::') !== value.lastIndexOf('::')) {
    return null
  }
  let ipv4 = null
  const lastColon = value.lastIndexOf(':')
  const lastDot = value.lastIndexOf('.')
  if (lastDot > lastColon) {
    ipv4 = value.slice(lastColon + 1)
    if (!/^(?:(?:25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3}(?:25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/.test(ipv4)) {
      return null
    }
    value = `${lastColon >= 0 ? value.slice(0, lastColon + 1) : ''}0:0`
  }
  const fold = value.indexOf('::')
  const toGroups = (part) => {
    if (!part) {
      return []
    }
    const items = part.split(':')
    const groups = []
    for (const item of items) {
      if (!item || item.length > 4 || !/^[0-9a-fA-F]+$/.test(item)) {
        return null
      }
      groups.push(Number.parseInt(item, 16))
    }
    return groups
  }
  let groups
  if (fold >= 0) {
    const left = toGroups(value.slice(0, fold))
    const right = toGroups(value.slice(fold + 2))
    if (!left || !right) {
      return null
    }
    const missing = 8 - left.length - right.length
    if (missing < 1) {
      return null
    }
    groups = [...left, ...Array(missing).fill(0), ...right]
  } else {
    groups = toGroups(value)
    if (!groups || groups.length !== 8) {
      return null
    }
  }
  if (ipv4) {
    const parts = ipv4.split('.').map(Number)
    groups[6] = (parts[0] << 8) | parts[1]
    groups[7] = (parts[2] << 8) | parts[3]
  }
  return groups
}

function formatIpv6(ip) {
  const groups = parseIpv6(ip)
  if (!groups) {
    return { valid: false, expanded: '', compressed: '' }
  }
  const expanded = groups.map((g) => g.toString(16).padStart(4, '0')).join(':')
  let bestStart = -1
  let bestLen = 0
  let run = -1
  for (let i = 0; i <= 8; i++) {
    if (i < 8 && groups[i] === 0) {
      if (run < 0) run = i
    } else if (run >= 0) {
      const len = i - run
      if (len > bestLen) {
        bestStart = run
        bestLen = len
      }
      run = -1
    }
  }
  if (bestLen < 2) {
    bestStart = -1
    bestLen = 0
  }
  let compressed = ''
  for (let i = 0; i < 8; ) {
    if (i === bestStart) {
      compressed += '::'
      i += bestLen
      continue
    }
    if (compressed && !compressed.endsWith(':')) {
      compressed += ':'
    }
    compressed += groups[i].toString(16)
    i++
  }
  return { valid: true, expanded, compressed }
}

function cardBrand(cardNo) {
  const digits = String(cardNo || '').replace(/\s+/g, '')
  if (!/^\d+$/.test(digits)) {
    return '未知'
  }
  if (digits.startsWith('4')) return 'Visa'
  if (digits.startsWith('34') || digits.startsWith('37')) return 'American Express'
  if (digits.startsWith('62')) return 'UnionPay'
  if (digits.startsWith('35')) return 'JCB'
  if (digits.startsWith('6011') || digits.startsWith('65')) return 'Discover'
  const prefix2 = Number(digits.slice(0, 2))
  if (prefix2 >= 51 && prefix2 <= 55) return 'Mastercard'
  const prefix4 = Number(digits.slice(0, 4))
  if (prefix4 >= 2221 && prefix4 <= 2720) return 'Mastercard'
  return '未知'
}

function luhn(digits) {
  if (digits.length < 12 || digits.length > 19 || !/^\d+$/.test(digits)) {
    return false
  }
  let sum = 0
  let doubleDigit = false
  for (let i = digits.length - 1; i >= 0; i--) {
    let n = Number(digits[i])
    if (doubleDigit) {
      n *= 2
      if (n > 9) n -= 9
    }
    sum += n
    doubleDigit = !doubleDigit
  }
  return sum % 10 === 0
}

function formatBetween(seconds) {
  const abs = Math.abs(seconds)
  const days = Math.floor(abs / 86400)
  const hours = Math.floor((abs % 86400) / 3600)
  const minutes = Math.floor((abs % 3600) / 60)
  const secs = abs % 60
  let out = ''
  if (days) out += `${days}天`
  if (hours) out += `${hours}小时`
  if (minutes) out += `${minutes}分钟`
  if (secs || !out) out += `${secs}秒`
  return out
}

function subBetween(text, before, after) {
  const start = text.indexOf(before)
  if (start < 0) return null
  const from = start + before.length
  const end = text.indexOf(after, from)
  return end < 0 ? null : text.slice(from, end)
}

function subBetweenAll(text, before, after) {
  const all = []
  let from = 0
  while (true) {
    const start = text.indexOf(before, from)
    if (start < 0) return all
    const begin = start + before.length
    const end = text.indexOf(after, begin)
    if (end < 0) return all
    all.push(text.slice(begin, end))
    from = end + after.length
  }
}

function pinyinFirstLetters(text) {
  const known = { 中: 'Z', 国: 'G', 北: 'B', 京: 'J', 上: 'S', 海: 'H', 工: 'G', 具: 'G' }
  return [...String(text || '')].map((ch) => {
    if (/[a-z]/i.test(ch)) return ch.toUpperCase()
    if (known[ch]) return known[ch]
    return ch
  }).join('')
}

function isHkId(id) {
  const compact = String(id || '').replace(/[()\s]/g, '').toUpperCase()
  if (compact.length < 8 || compact.length > 9) return false
  const body = compact.slice(0, -1)
  const check = compact.slice(-1)
  if (!/^[A-Z]{1,2}\d{6}$/.test(body)) return false
  const padded = body.length === 7 ? ' ' + body : body
  let sum = 0
  let weight = 9
  for (const ch of padded) {
    let code
    if (ch === ' ') code = 36
    else if (ch >= 'A' && ch <= 'Z') code = ch.charCodeAt(0) - 65 + 10
    else code = Number(ch)
    sum += code * weight
    weight--
  }
  const rem = sum % 11
  const digit = (11 - rem) % 11
  const expected = digit === 10 ? 'A' : String(digit)
  return check === expected
}

function isTwId(id) {
  const compact = String(id || '').trim().toUpperCase()
  if (!/^[A-Z][12]\d{8}$/.test(compact)) return false
  const letters = [10, 11, 12, 13, 14, 15, 16, 17, 34, 18, 19, 20, 21, 22, 35, 23, 24, 25, 26, 27, 28, 29, 32, 30, 31, 33]
  const n = letters[compact.charCodeAt(0) - 65]
  let sum = Math.floor(n / 10) + (n % 10) * 9
  const weights = [8, 7, 6, 5, 4, 3, 2, 1, 1]
  for (let i = 0; i < 9; i++) {
    sum += Number(compact[i + 1]) * weights[i]
  }
  return sum % 10 === 0
}

function orgCodeCheck(body8) {
  const charset = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  const weights = [3, 7, 9, 10, 5, 8, 4, 2]
  let sum = 0
  for (let i = 0; i < 8; i++) {
    const idx = charset.indexOf(body8[i])
    if (idx < 0) return ''
    sum += idx * weights[i]
  }
  const c9 = 11 - (sum % 11)
  if (c9 === 11) return '0'
  if (c9 === 10) return 'X'
  return String(c9)
}

function isOrgCode(code) {
  const compact = String(code || '').replace(/[-\s]/g, '').toUpperCase()
  if (compact.length !== 9) return false
  return compact[8] === orgCodeCheck(compact.slice(0, 8))
}

function ganZhiYear(year) {
  const stems = '甲乙丙丁戊己庚辛壬癸'
  const branches = '子丑寅卯辰巳午未申酉戌亥'
  const idx = ((year - 1984) % 60 + 60) % 60
  return stems[idx % 10] + branches[idx % 12]
}

function ganZhiAnimal(year) {
  const animals = '鼠牛虎兔龙蛇马羊猴鸡狗猪'
  return animals[((year - 1984) % 12 + 12) % 12]
}

function isAba(number) {
  if (!/^\d{9}$/.test(number)) return false
  const d = [...number].map(Number)
  return (3 * (d[0] + d[3] + d[6]) + 7 * (d[1] + d[4] + d[7]) + (d[2] + d[5] + d[8])) % 10 === 0
}

function iso6346Value(c) {
  if (c >= '0' && c <= '9') return c.charCodeAt(0) - 48
  const n = c.charCodeAt(0) - 65 + 10
  return n + Math.floor(n / 11)
}

function isIso6346(code) {
  if (!/^[A-Z]{3}[UJZ]\d{7}$/.test(code)) return false
  let sum = 0
  let weight = 1
  for (let i = 0; i < 10; i++) {
    sum += iso6346Value(code[i]) * weight
    weight *= 2
  }
  const check = sum % 11
  return code[10] === String(check === 10 ? 0 : check)
}

function jumpHash(key, buckets) {
  if (buckets <= 0) throw new Error('buckets must be > 0')
  let b = -1n
  let j = 0n
  let k = BigInt(key)
  const n = BigInt(buckets)
  while (j < n) {
    b = j
    k = (k * 2862933555777941757n + 1n) & 0xffffffffffffffffn
    j = BigInt(Math.floor(Number(b + 1n) * (2147483648 / (Number(k >> 33n) + 1))))
  }
  return Number(b)
}

function isFigi(figi) {
  if (!/^[0-9BCDFGHJKLMNPQRSTVWXYZ]{12}$/.test(figi)) return false
  const body = figi.slice(0, 11)
  let sum = 0
  for (let i = 0; i < 11; i++) {
    const ch = body[i]
    const value = ch >= '0' && ch <= '9' ? Number(ch) : ch.charCodeAt(0) - 55
    if ((10 - i) % 2 === 0) {
      const weighted = value * 2
      sum += Math.floor(weighted / 10) + (weighted % 10)
    } else {
      sum += value
    }
  }
  return figi[11] === String((10 - (sum % 10)) % 10)
}

function isLei(lei) {
  if (!/^[A-Z0-9]{18}\d{2}$/.test(lei)) return false
  let numeric = ''
  for (const ch of lei) {
    numeric += /[A-Z]/.test(ch) ? String(ch.charCodeAt(0) - 55) : ch
  }
  return BigInt(numeric) % 97n === 1n
}

function isNhs(number) {
  if (!/^\d{10}$/.test(number)) return false
  let sum = 0
  for (let i = 0; i < 9; i++) {
    sum += Number(number[i]) * (10 - i)
  }
  let check = 11 - (sum % 11)
  if (check === 11) check = 0
  if (check === 10) return false
  return check === Number(number[9])
}

function isNpi(number) {
  return /^\d{10}$/.test(number) && luhn('80840' + number)
}

function isIsmn(number) {
  if (!/^9790\d{9}$/.test(number)) return false
  let sum = 0
  let factor = 3
  for (let i = number.length - 2; i >= 0; i--) {
    sum += Number(number[i]) * factor
    factor = 4 - factor
  }
  return number[12] === String((10 - (sum % 10)) % 10)
}

function isNric(nric) {
  if (!/^[STFGM]\d{7}[A-Z]$/.test(nric)) return false
  const weights = [2, 7, 6, 5, 4, 3, 2]
  const prefix = nric[0]
  let sum = prefix === 'T' || prefix === 'G' || prefix === 'M' ? 4 : 0
  for (let i = 0; i < 7; i++) {
    sum += Number(nric[i + 1]) * weights[i]
  }
  const table = prefix === 'F' || prefix === 'G' || prefix === 'M' ? 'XWUTRQPNMLK' : 'JZIHGFEDCBA'
  return nric[8] === table[sum % 11]
}

function colognePhonetic(text) {
  const word = String(text || '')
    .toUpperCase()
    .replaceAll('Ä', 'A')
    .replaceAll('Ö', 'O')
    .replaceAll('Ü', 'U')
    .replaceAll('ß', '8')
  const codes = []
  let last = ''
  const prev = (i) => (i === 0 ? '' : word[i - 1])
  const next = (i) => (i + 1 < word.length ? word[i + 1] : '')
  const codeOf = (i) => {
    const c = word[i]
    const n = next(i)
    if ('AEIJOUY'.includes(c)) return '0'
    if (c === 'B') return '1'
    if (c === 'P') return n === 'H' ? '3' : '1'
    if (c === 'D' || c === 'T') return 'CSZ'.includes(n) ? '8' : '2'
    if ('FVW'.includes(c)) return '3'
    if ('GKQ'.includes(c)) return '4'
    if (c === 'C') {
      if (i === 0) return 'AHKLOQRUX'.includes(n) ? '4' : '8'
      if ('AHKOQUX'.includes(n) && prev(i) !== 'S' && prev(i) !== 'Z') return '4'
      return '8'
    }
    if (c === 'X') return 'CKQ'.includes(prev(i)) ? '8' : '48'
    if (c === 'L') return '5'
    if (c === 'M' || c === 'N') return '6'
    if (c === 'R') return '7'
    if (c === 'S' || c === 'Z' || c === '8') return '8'
    return ''
  }
  for (let i = 0; i < word.length; i++) {
    const c = word[i]
    if ((c < 'A' || c > 'Z') && c !== '8') continue
    const code = codeOf(i)
    for (const ch of code) {
      if (ch && ch !== last) {
        codes.push(ch)
        last = ch
      }
    }
  }
  return codes.filter((ch, i) => ch !== '0' || i === 0).join('')
}

function solarTermDate(year, name) {
  const names = ['小寒', '大寒', '立春', '雨水', '惊蛰', '春分', '清明', '谷雨', '立夏', '小满', '芒种', '夏至', '小暑', '大暑', '立秋', '处暑', '白露', '秋分', '寒露', '霜降', '立冬', '小雪', '大雪', '冬至']
  const c21 = [5.4055, 20.12, 3.87, 18.73, 5.63, 20.646, 4.81, 20.1, 5.52, 21.04, 5.678, 21.37, 7.108, 22.83, 7.5, 23.13, 7.646, 23.042, 8.318, 23.438, 7.438, 22.36, 7.18, 21.94]
  const c20 = [6.11, 20.84, 4.6295, 19.4599, 6.3826, 21.4155, 5.59, 20.888, 6.318, 21.86, 6.5, 22.2, 7.928, 23.65, 8.35, 23.95, 8.44, 23.822, 9.098, 24.218, 8.218, 23.08, 7.9, 22.6]
  const idx = names.indexOf(name)
  if (idx < 0) return null
  const c = year < 2000 ? c20 : c21
  const y = year % 100
  const day = Math.floor(y * 0.2422 + c[idx]) - Math.floor(y / 4)
  const month = Math.floor(idx / 2) + 1
  return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
}

function nysiis(text) {
  let word = String(text || '').toUpperCase().replace(/[^A-Z]/g, '')
  if (!word) return ''
  if (word.startsWith('MAC')) word = 'MCC' + word.slice(3)
  else if (word.startsWith('KN')) word = 'NN' + word.slice(2)
  else if (word.startsWith('K')) word = 'C' + word.slice(1)
  else if (word.startsWith('PH') || word.startsWith('PF')) word = 'FF' + word.slice(2)
  else if (word.startsWith('SCH')) word = 'SSS' + word.slice(3)
  if (word.endsWith('EE') || word.endsWith('IE')) word = word.slice(0, -2) + 'Y'
  else if (/(DT|RT|RD|NT|ND)$/.test(word)) word = word.slice(0, -2) + 'D'
  const chars = word.split('')
  const vowel = (c) => 'AEIOU'.includes(c)
  const transcode = (prev, current, next, next2) => {
    if (current === 'E' && next === 'V') return ['A', 'F']
    if (vowel(current)) return ['A']
    if (current === 'Q') return ['G']
    if (current === 'Z') return ['S']
    if (current === 'M') return ['N']
    if (current === 'K') return next === 'N' ? ['N', 'N'] : ['C']
    if (current === 'S' && next === 'C' && next2 === 'H') return ['S', 'S', 'S']
    if (current === 'P' && next === 'H') return ['F', 'F']
    if (current === 'H' && (!vowel(prev) || !vowel(next))) return [prev]
    if (current === 'W' && vowel(prev)) return [prev]
    return [current]
  }
  let key = chars[0]
  for (let i = 1; i < chars.length; i++) {
    const coded = transcode(chars[i - 1], chars[i], chars[i + 1] || ' ', chars[i + 2] || ' ')
    for (let j = 0; j < coded.length && i + j < chars.length; j++) chars[i + j] = coded[j]
    if (chars[i] !== chars[i - 1]) key += chars[i]
  }
  if (key.length > 1 && key.endsWith('S')) key = key.slice(0, -1)
  if (key.length > 2 && key.endsWith('AY')) key = key.slice(0, -2) + 'Y'
  if (key.length > 1 && key.endsWith('A')) key = key.slice(0, -1)
  return key
}

function caverphone(text) {
  if (!text) return '1111111111'
  let word = String(text).toLowerCase().replace(/[^a-z]/g, '')
  if (!word) return '1111111111'
  word = word.replace(/e$/, '')
  word = word.replace(/^cough/, 'cou2f').replace(/^rough/, 'rou2f').replace(/^tough/, 'tou2f')
  word = word.replace(/^enough/, 'enou2f').replace(/^trough/, 'trou2f').replace(/^gn/, '2n')
  word = word.replace(/mb$/, 'm2')
  word = word.replace(/cq/g, '2q').replace(/ci/g, 'si').replace(/ce/g, 'se').replace(/cy/g, 'sy')
  word = word.replace(/tch/g, '2ch').replace(/c/g, 'k').replace(/q/g, 'k').replace(/x/g, 'k').replace(/v/g, 'f')
  word = word.replace(/dg/g, '2g').replace(/tio/g, 'sio').replace(/tia/g, 'sia').replace(/d/g, 't')
  word = word.replace(/ph/g, 'fh').replace(/b/g, 'p').replace(/sh/g, 's2').replace(/z/g, 's')
  word = word.replace(/^[aeiou]/, 'A').replace(/[aeiou]/g, '3')
  word = word.replace(/j/g, 'y').replace(/^y3/, 'Y3').replace(/^y/, 'A').replace(/y/g, '3')
  word = word.replace(/3gh3/g, '3kh3').replace(/gh/g, '22').replace(/g/g, 'k')
  word = word.replace(/s+/g, 'S').replace(/t+/g, 'T').replace(/p+/g, 'P').replace(/k+/g, 'K')
  word = word.replace(/f+/g, 'F').replace(/m+/g, 'M').replace(/n+/g, 'N')
  word = word.replace(/w3/g, 'W3').replace(/wh3/g, 'Wh3').replace(/w$/, '3').replace(/w/g, '2')
  word = word.replace(/^h/, 'A').replace(/h/g, '2')
  word = word.replace(/r3/g, 'R3').replace(/r$/, '3').replace(/r/g, '2')
  word = word.replace(/l3/g, 'L3').replace(/l$/, '3').replace(/l/g, '2')
  word = word.replace(/2/g, '').replace(/3$/, 'A').replace(/3/g, '')
  return (word + '1111111111').slice(0, 10)
}

function sameDigits(digits) {
  return digits.split('').every((ch) => ch === digits[0])
}

function isCpf(digits) {
  if (!/^\d{11}$/.test(digits) || sameDigits(digits)) return false
  const digit = (body) => {
    const weight = body.length + 1
    let sum = 0
    for (let i = 0; i < body.length; i++) sum += Number(body[i]) * (weight - i)
    const rem = sum % 11
    return String(rem < 2 ? 0 : 11 - rem)
  }
  return digits[9] === digit(digits.slice(0, 9)) && digits[10] === digit(digits.slice(0, 10))
}

function isCnpj(digits) {
  if (!/^\d{14}$/.test(digits) || sameDigits(digits)) return false
  const w12 = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]
  const w13 = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]
  const digit = (body, weights) => {
    let sum = 0
    for (let i = 0; i < body.length; i++) sum += Number(body[i]) * weights[i]
    const rem = sum % 11
    return String(rem < 2 ? 0 : 11 - rem)
  }
  return digits[12] === digit(digits.slice(0, 12), w12) && digits[13] === digit(digits.slice(0, 13), w13)
}

function isPesel(digits) {
  if (!/^\d{11}$/.test(digits)) return false
  const weights = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3]
  let sum = 0
  for (let i = 0; i < 10; i++) sum += Number(digits[i]) * weights[i]
  return digits[10] === String((10 - (sum % 10)) % 10)
}

function peselBirth(digits) {
  let month = Number(digits.slice(2, 4))
  const year = Number(digits.slice(0, 2))
  const day = digits.slice(4, 6)
  let century = 1900
  if (month >= 21 && month <= 32) { century = 2000; month -= 20 }
  else if (month >= 41 && month <= 52) { century = 2100; month -= 40 }
  else if (month >= 61 && month <= 72) { century = 2200; month -= 60 }
  else if (month >= 81 && month <= 92) { century = 1800; month -= 80 }
  return `${century + year}-${String(month).padStart(2, '0')}-${day}`
}

function eanCheckDigit(body) {
  let sum = 0
  let factor = 3
  for (let i = body.length - 1; i >= 0; i--) {
    sum += Number(body[i]) * factor
    factor = 4 - factor
  }
  return String((10 - (sum % 10)) % 10)
}

function expandUpcE(digits) {
  if (!/^[01]\d{7}$/.test(digits)) return null
  const ns = digits[0]
  const body = digits.slice(1, 7)
  const last = body[5]
  let manufacturer
  let product
  if (last <= '2') {
    manufacturer = body.slice(0, 2) + last + '00'
    product = '00' + body.slice(2, 5)
  } else if (last === '3') {
    manufacturer = body.slice(0, 3) + '00'
    product = '000' + body.slice(3, 5)
  } else if (last === '4') {
    manufacturer = body.slice(0, 4) + '0'
    product = '0000' + body[4]
  } else {
    manufacturer = body.slice(0, 5)
    product = '0000' + last
  }
  const upcA = ns + manufacturer + product + eanCheckDigit(ns + manufacturer + product)
  return upcA[11] === digits[7] ? upcA : null
}

function isUpcE(digits) {
  return Boolean(expandUpcE(digits))
}

function julianDayNumber(iso) {
  const [y, m, d] = iso.split('-').map(Number)
  const a = Math.floor((14 - m) / 12)
  const y2 = y + 4800 - a
  const m2 = m + 12 * a - 3
  return d + Math.floor((153 * m2 + 2) / 5) + 365 * y2 + Math.floor(y2 / 4) - Math.floor(y2 / 100) + Math.floor(y2 / 400) - 32045
}

function matchRating(name) {
  let word = String(name || '').toUpperCase().replace(/[^A-Z]/g, '')
  if (!word || word.length === 1) return ''
  const first = word[0]
  let rest = word.replace(/[AEIOU]/g, '')
  if ('AEIOU'.includes(first)) rest = first + rest
  const doubles = ['BB', 'CC', 'DD', 'FF', 'GG', 'HH', 'JJ', 'KK', 'LL', 'MM', 'NN', 'PP', 'QQ', 'RR', 'SS', 'TT', 'VV', 'WW', 'XX', 'YY', 'ZZ']
  for (const pair of doubles) rest = rest.replaceAll(pair, pair[0])
  return rest.length > 6 ? rest.slice(0, 3) + rest.slice(-3) : rest
}

function matchRatingSimilar(left, right) {
  const a = matchRating(left)
  const b = matchRating(right)
  return Boolean(a) && a === b
}

function isSiren(digits) {
  return /^\d{9}$/.test(digits) && luhnAny(digits)
}

function isSiret(digits) {
  return /^\d{14}$/.test(digits) && luhnAny(digits) && isSiren(digits.slice(0, 9))
}

function luhnAny(digits) {
  if (!/^\d+$/.test(digits)) return false
  let sum = 0
  let doubleDigit = false
  for (let i = digits.length - 1; i >= 0; i--) {
    let n = Number(digits[i])
    if (doubleDigit) {
      n *= 2
      if (n > 9) n -= 9
    }
    sum += n
    doubleDigit = !doubleDigit
  }
  return sum % 10 === 0
}

function isNif(compact) {
  const letters = 'TRWAGMYFPDXBNJZSQVHLCKE'
  if (/^\d{8}[A-Z]$/.test(compact)) {
    return compact[8] === letters[Number(compact.slice(0, 8)) % 23]
  }
  if (/^[XYZ]\d{7}[A-Z]$/.test(compact)) {
    const mapped = compact[0] === 'X' ? '0' : compact[0] === 'Y' ? '1' : '2'
    return compact[8] === letters[Number(mapped + compact.slice(1, 8)) % 23]
  }
  return false
}

function isIsni(compact) {
  if (!/^\d{15}[\dX]$/.test(compact)) return false
  let p = 0
  for (let i = 0; i < 15; i++) p = (p + Number(compact[i])) * 2
  const check = (12 - (p % 11)) % 11
  const expected = check === 10 ? 'X' : String(check)
  return compact[15] === expected
}

function refinedSoundex(text) {
  const map = '01360240043788015936020505'
  const letters = String(text || '').toUpperCase().replace(/[^A-Z]/g, '')
  if (!letters) return ''
  let out = letters[0]
  let last = '*'
  for (const ch of letters) {
    const current = map[ch.charCodeAt(0) - 65]
    if (current === last) continue
    out += current
    last = current
  }
  return out
}

function isNir(compact) {
  if (!/^[1-8]\d{4}(?:\d{2}|2[AB])\d{6}\d{2}$/.test(compact)) return false
  const body = compact.slice(0, 13)
  const dept = compact.slice(5, 7)
  const numeric = dept === '2A' ? `${body.slice(0, 5)}19${body.slice(7)}` : dept === '2B' ? `${body.slice(0, 5)}18${body.slice(7)}` : body
  const key = String(97 - (Number(numeric) % 97)).padStart(2, '0')
  return compact.slice(13) === key
}

function isCodiceFiscale(compact) {
  if (!/^[A-Z]{6}\d{2}[A-EHLMPRST]\d{2}[A-Z]\d{3}[A-Z]$/.test(compact)) return false
  const oddDigit = [1, 0, 5, 7, 9, 13, 15, 17, 19, 21]
  const oddLetter = {
    A: 1, B: 0, C: 5, D: 7, E: 9, F: 13, G: 15, H: 17, I: 19, J: 21,
    K: 2, L: 4, M: 18, N: 20, O: 11, P: 3, Q: 6, R: 8, S: 12, T: 14,
    U: 16, V: 10, W: 22, X: 25, Y: 24, Z: 23
  }
  let sum = 0
  for (let i = 0; i < 15; i++) {
    const c = compact[i]
    if (i % 2 === 0) sum += /\d/.test(c) ? oddDigit[Number(c)] : oddLetter[c]
    else sum += /\d/.test(c) ? Number(c) : c.charCodeAt(0) - 65
  }
  return compact[15] === String.fromCharCode(65 + (sum % 26))
}

function isSteuerId(digits) {
  if (!/^[1-9]\d{10}$/.test(digits)) return false
  const counts = Array(10).fill(0)
  for (const ch of digits.slice(0, 10)) counts[Number(ch)]++
  const missing = counts.filter((n) => n === 0).length
  const twice = counts.filter((n) => n === 2).length
  if (missing !== 1 || twice !== 1 || counts.some((n) => n > 2)) return false
  let product = 10
  for (const ch of digits.slice(0, 10)) {
    let sum = (Number(ch) + product) % 10
    if (sum === 0) sum = 10
    product = (sum * 2) % 11
  }
  const check = 11 - product
  return digits[10] === String(check === 10 ? 0 : check)
}

function doubleMetaphone(text) {
  const word = String(text || '').toUpperCase().replace(/[^A-Z]/g, '')
  if (!word) return ''
  let i = /^(GN|KN|PN|WR|PS)/.test(word) ? 1 : 0
  let out = ''
  const vowel = (c) => 'AEIOUY'.includes(c)
  const at = (n) => word[n] || ''
  while (out.length < 4 && i < word.length) {
    const c = word[i]
    if ('AEIOUY'.includes(c)) {
      if (i === 0) out += 'A'
      i++
      continue
    }
    if (c === 'B') { out += 'P'; i += at(i + 1) === 'B' ? 2 : 1; continue }
    if (word.startsWith('CH', i) || word.startsWith('CIA', i) || word.startsWith('SCH', i) || word.startsWith('SH', i) || word.startsWith('SIO', i) || word.startsWith('SIA', i)) {
      out += 'X'
      i += word.startsWith('SCH', i) || word.startsWith('SIO', i) || word.startsWith('SIA', i) || word.startsWith('CIA', i) ? 3 : 2
      continue
    }
    if (c === 'C') { out += (at(i + 1) === 'I' || at(i + 1) === 'E' || at(i + 1) === 'Y') ? 'S' : 'K'; i += at(i + 1) === 'C' ? 2 : 1; continue }
    if (word.startsWith('DGE', i) || word.startsWith('DGI', i) || word.startsWith('DGY', i)) { out += 'J'; i += 3; continue }
    if (c === 'D') { out += 'T'; i += (at(i + 1) === 'D' || at(i + 1) === 'T') ? 2 : 1; continue }
    if (c === 'F') { out += 'F'; i += at(i + 1) === 'F' ? 2 : 1; continue }
    if (c === 'G' && at(i + 1) === 'H') { i += 2; continue }
    if (c === 'G' && (at(i + 1) === 'I' || at(i + 1) === 'E' || at(i + 1) === 'Y')) { out += 'J'; i += 2; continue }
    if (c === 'G') { out += 'K'; i += at(i + 1) === 'G' ? 2 : 1; continue }
    if (c === 'H') { i += vowel(at(i + 1)) && (i === 0 || vowel(at(i - 1))) ? 2 : 1; if ((i === 0 || vowel(at(i - 1))) && vowel(at(i + 1))) out += 'H'; continue }
    if (c === 'J') { out += 'J'; i += at(i + 1) === 'J' ? 2 : 1; continue }
    if (c === 'K') { out += 'K'; i += at(i + 1) === 'K' ? 2 : 1; continue }
    if (c === 'L') { out += 'L'; i += at(i + 1) === 'L' ? 2 : 1; continue }
    if (c === 'M') { out += 'M'; i += at(i + 1) === 'M' ? 2 : 1; continue }
    if (c === 'N') { out += 'N'; i += at(i + 1) === 'N' ? 2 : 1; continue }
    if (c === 'P' && at(i + 1) === 'H') { out += 'F'; i += 2; continue }
    if (c === 'P') { out += 'P'; i += (at(i + 1) === 'P' || at(i + 1) === 'B') ? 2 : 1; continue }
    if (c === 'Q') { out += 'K'; i += 1; continue }
    if (c === 'R') { out += 'R'; i += at(i + 1) === 'R' ? 2 : 1; continue }
    if (c === 'S') { out += 'S'; i += at(i + 1) === 'S' ? 2 : 1; continue }
    if (word.startsWith('TH', i) || word.startsWith('TTH', i)) { out += '0'; i += word.startsWith('TTH', i) ? 3 : 2; continue }
    if (c === 'T') { out += 'T'; i += (at(i + 1) === 'T' || at(i + 1) === 'D') ? 2 : 1; continue }
    if (c === 'V') { out += 'F'; i += 1; continue }
    if (c === 'W' && i === 0 && (vowel(at(1)) || word.startsWith('WH'))) { out += 'A'; i += word.startsWith('WH') ? 2 : 1; continue }
    if (c === 'W') { i += 1; continue }
    if (c === 'X') { out += i === 0 ? 'S' : 'KS'; i += 1; continue }
    if (c === 'Z') { out += 'S'; i += 1; continue }
    i++
  }
  return out.slice(0, 4)
}
