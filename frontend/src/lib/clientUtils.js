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
    case 'personnummer-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      const ten = digits.length === 12 ? digits.slice(2) : digits
      return { normalized: digits, valid: isPersonnummer(ten) }
    }
    case 'hetu-local': {
      const compact = String(values.value || '').replace(/[\s.]/g, '').toUpperCase()
      return { normalized: compact, valid: isHetu(compact) }
    }
    case 'fodselsnummer-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isFodselsnummer(digits) }
    }
    case 'iswc-local': {
      const compact = String(values.value || '').replace(/[\s.-]/g, '').toUpperCase()
      return { normalized: compact, valid: isIswc(compact) }
    }
    case 'abn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isAbn(digits) }
    }
    case 'tfn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isTfn(digits) }
    }
    case 'sscc-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isSscc(digits) }
    }
    case 'vat-local': {
      const compact = String(values.value || '').replace(/[\s.-]/g, '').toUpperCase()
      return { normalized: compact, country: compact.slice(0, 2), valid: isVat(compact) }
    }
    case 'ahv-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isAhv(digits) }
    }
    case 'nip-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isNip(digits) }
    }
    case 'aadhaar-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: /^[2-9]\d{11}$/.test(digits) && verhoeff(digits) }
    }
    case 'pan-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: /^[A-Z]{3}[PCHFATBLJG][A-Z]\d{4}[A-Z]$/.test(compact) }
    }
    case 'sin-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: /^\d{9}$/.test(digits) && digits !== '000000000' && luhnAny(digits) }
    }
    case 'pps-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isPps(compact) }
    }
    case 'cpr-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isCpr(digits) }
    }
    case 'pt-nif-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isPtNif(digits) }
    }
    case 'nino-local': {
      const compact = String(values.value || '').replace(/[\s.-]/g, '').toUpperCase()
      return { normalized: compact, prefix: compact.slice(0, 2), valid: isNino(compact) }
    }
    case 'rrn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isRrn(digits) }
    }
    case 'afm-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isAfm(digits) }
    }
    case 'rut-local': {
      const compact = String(values.value || '').replace(/[.\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isRut(compact) }
    }
    case 'cuit-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isCuit(digits) }
    }
    case 'said-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: /^\d{13}$/.test(digits) && luhnAny(digits) }
    }
    case 'tckn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isTckn(digits) }
    }
    case 'cnp-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isCnp(digits) }
    }
    case 'thai-id-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isThaiId(digits) }
    }
    case 'oib-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isOib(digits) }
    }
    case 'jmbg-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isJmbg(digits) }
    }
    case 'kennitala-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isKennitala(digits) }
    }
    case 'taj-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isTaj(digits) }
    }
    case 'nit-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isNit(digits) }
    }
    case 'lv-pk-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isLatvianPk(digits) }
    }
    case 'emso-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isEmso(digits) }
    }
    case 'pe-dni-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isPeDni(compact) }
    }
    case 'mx-rfc-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isMxRfc(compact) }
    }
    case 'bsn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isBsn(digits) }
    }
    case 'rodne-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isRodne(digits) }
    }
    case 'y-tunnus-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isYTunnus(digits) }
    }
    case 'cvr-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isCvr(digits) }
    }
    case 'cif-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isCif(compact) }
    }
    case 'che-uid-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isCheUid(digits) }
    }
    case 'cui-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isCui(digits) }
    }
    case 'kbo-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isKbo(digits) }
    }
    case 'hojin-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isHojin(digits) }
    }
    case 'kr-brn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isKrBrn(digits) }
    }
    case 'tw-gui-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isTwGui(digits) }
    }
    case 'edrpou-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isEdrpou(digits) }
    }
    case 'pib-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isPib(digits) }
    }
    case 'gstin-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isGstin(compact) }
    }
    case 'acn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isAcn(digits) }
    }
    case 'vkn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isVkn(digits) }
    }
    case 'npwp-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isNpwp(digits) }
    }
    case 'registrikood-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isRegistrikood(digits) }
    }
    case 'nzbn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isNzbn(digits) }
    }
    case 'uen-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isUen(compact) }
    }
    case 'il-hp-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isIlHp(digits) }
    }
    case 'lt-ja-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isLtJa(digits) }
    }
    case 'inn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isInn(digits) }
    }
    case 'pe-ruc-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isPeRuc(digits) }
    }
    case 'nik-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isNik(digits) }
    }
    case 'vn-mst-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isVnMst(digits) }
    }
    case 'ein-local': {
      const raw = String(values.value || '').trim()
      const digits = raw.replace(/\D/g, '')
      return { normalized: digits, formatted: digits.length === 9 ? `${digits.slice(0, 2)}-${digits.slice(2)}` : raw, valid: isEin(raw) }
    }
    case 'ogrn-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isOgrn(digits) }
    }
    case 'snils-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isSnils(digits) }
    }
    case 'nipt-local': {
      const compact = String(values.value || '').replace(/[\s().-]/g, '').toUpperCase().replace(/^AL/, '')
      return { normalized: compact, valid: isNipt(compact) }
    }
    case 'rif-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isRif(compact) }
    }
    case 'rnc-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isRnc(digits) }
    }
    case 'unp-local': {
      const compact = normalizeUnp(values.value)
      return { normalized: compact, valid: isUnp(compact) }
    }
    case 'itin-local': {
      const raw = String(values.value || '').trim()
      const digits = raw.replace(/\D/g, '')
      return { normalized: digits, formatted: digits.length === 9 ? `${digits.slice(0, 3)}-${digits.slice(3, 5)}-${digits.slice(5)}` : raw, valid: isItin(raw) }
    }
    case 'cnic-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, formatted: digits.length === 13 ? `${digits.slice(0, 5)}-${digits.slice(5, 12)}-${digits.slice(12)}` : digits, valid: isCnic(digits) }
    }
    case 'idno-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isIdno(digits) }
    }
    case 'gh-tin-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isGhTin(compact) }
    }
    case 'ke-pin-local': {
      const compact = String(values.value || '').replace(/[\s-]/g, '').toUpperCase()
      return { normalized: compact, valid: isKePin(compact) }
    }
    case 'ma-ice-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: isMaIce(digits) }
    }
    case 'voen-local': {
      const digits = normalizeVoen(values.value)
      return { normalized: digits, valid: isVoen(digits) }
    }
    case 'uy-rut-local': {
      const digits = normalizeUyRut(values.value)
      return { normalized: digits, formatted: digits.length === 12 ? `${digits.slice(0, 2)}-${digits.slice(2, 8)}-${digits.slice(8, 11)}-${digits.slice(11)}` : digits, valid: isUyRut(digits) }
    }
    case 'py-ruc-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, formatted: digits.length >= 2 ? `${digits.slice(0, -1)}-${digits.slice(-1)}` : digits, valid: isPyRuc(digits) }
    }
    case 'i18n-local': {
      const locale = String(values.locale || 'zh-CN')
      const amount = Number(values.amount || 0)
      const currency = String(values.currency || 'CNY')
      let currencyName = currency
      try {
        currencyName = new Intl.DisplayNames([locale], { type: 'currency' }).of(currency)
      } catch {
        currencyName = currency
      }
      const list = typeof Intl.ListFormat === 'function'
        ? new Intl.ListFormat(locale, { style: 'long', type: 'conjunction' }).format(['apples', 'oranges', 'pears'])
        : 'apples, oranges, pears'
      return {
        locale,
        number: new Intl.NumberFormat(locale).format(amount),
        currency: new Intl.NumberFormat(locale, { style: 'currency', currency }).format(amount),
        currencyName,
        percent: new Intl.NumberFormat(locale, { style: 'percent', maximumFractionDigits: 2 }).format(0.125),
        date: new Intl.DateTimeFormat(locale, { dateStyle: 'medium' }).format(new Date('2026-09-18T00:00:00')),
        list
      }
    }
    case 'gt-nit-local': {
      const compact = normalizeGtNit(values.value)
      return { normalized: compact, formatted: compact.length >= 2 ? `${compact.slice(0, -1)}-${compact.slice(-1)}` : compact, valid: isGtNit(compact) }
    }
    case 'cr-cpf-local': {
      const digits = normalizeCrCpf(values.value)
      return { normalized: digits, formatted: digits.length === 10 ? `${digits.slice(0, 2)}-${digits.slice(2, 6)}-${digits.slice(6)}` : digits, valid: isCrCpf(digits) }
    }
    case 'cr-cpj-local': {
      const digits = String(values.value || '').replace(/[\s-]/g, '')
      return { normalized: digits, formatted: digits.length === 10 ? `${digits[0]}-${digits.slice(1, 4)}-${digits.slice(4)}` : digits, valid: isCrCpj(digits) }
    }
    case 'tn-mf-local': {
      const compact = normalizeTnMf(values.value)
      return { normalized: compact, formatted: formatTnMf(compact), valid: isTnMf(compact) }
    }
    case 'i18n-plural-local': {
      const locale = String(values.locale || 'en')
      const count = Number(values.count || 0)
      const category = new Intl.PluralRules(locale).select(count)
      const items = locale.startsWith('zh')
        ? (count === 0 ? '没有条目' : `${count} 条`)
        : count === 0 ? 'no items' : count === 1 ? 'one item' : `${count} items`
      return { locale, count, category, items }
    }
    case 'i18n-bidi-local': {
      const text = String(values.text || '')
      const locale = String(values.locale || 'en')
      const rtl = /[\u0590-\u08FF]/.test(text)
      const localeRtl = ['ar', 'he', 'fa', 'ur'].includes(locale.split('-')[0])
      return { direction: rtl ? 'rtl' : 'ltr', rtl, ltr: !rtl, localeRtl }
    }
    case 'i18n-timezone-local': {
      const locale = String(values.locale || 'en')
      const zone = String(values.zone || 'UTC')
      const fmt = new Intl.DateTimeFormat(locale, { timeZone: zone, timeZoneName: 'long' })
      const parts = fmt.formatToParts(new Date('2026-09-18T08:15:00Z'))
      const displayName = parts.find((part) => part.type === 'timeZoneName')?.value || zone
      return { id: zone, displayName, compact: new Intl.NumberFormat(locale, { notation: 'compact' }).format(1234.5) }
    }
    case 'i18n-relative-local': {
      const locale = String(values.locale || 'zh-CN')
      const seconds = Number(values.seconds || 0)
      const rtf = new Intl.RelativeTimeFormat(locale, { numeric: 'auto' })
      const abs = Math.abs(seconds)
      const sign = seconds >= 0 ? -1 : 1
      let value
      let unit
      if (abs < 45) {
        value = 0
        unit = 'second'
      } else if (abs < 3600) {
        value = Math.max(1, Math.round(abs / 60))
        unit = 'minute'
      } else if (abs < 86400) {
        value = Math.max(1, Math.round(abs / 3600))
        unit = 'hour'
      } else {
        value = Math.max(1, Math.round(abs / 86400))
        unit = 'day'
      }
      return { locale, seconds, text: rtf.format(sign * value, unit) }
    }
    case 'i18n-case-local': {
      const locale = String(values.locale || 'tr')
      const text = String(values.text || '')
      return { upper: text.toLocaleUpperCase(locale), lower: text.toLocaleLowerCase(locale), rootUpper: text.toLocaleUpperCase('en') }
    }
    case 'i18n-digits-local': {
      const locale = String(values.locale || 'ar-EG')
      const text = String(values.text || '1234')
      let native = text
      try {
        native = new Intl.NumberFormat(locale, { useGrouping: false }).format(Number(text))
      } catch {
        native = text
      }
      return { locale, native, latin: text }
    }
    case 'eg-tn-local': {
      const digits = normalizeEgTn(values.value)
      return { normalized: digits, formatted: digits.length === 9 ? `${digits.slice(0, 3)}-${digits.slice(3, 6)}-${digits.slice(6)}` : digits, valid: /^\d{9}$/.test(digits) }
    }
    case 'lu-tva-local': {
      const digits = normalizeLuTva(values.value)
      return { normalized: digits, valid: isLuTva(digits) }
    }
    case 'sv-nit-local': {
      const digits = normalizeSvNit(values.value)
      return { normalized: digits, formatted: digits.length === 14 ? `${digits.slice(0, 4)}-${digits.slice(4, 10)}-${digits.slice(10, 13)}-${digits.slice(13)}` : digits, valid: isSvNit(digits) }
    }
    case 'mk-edb-local': {
      const digits = normalizeMkEdb(values.value)
      return { normalized: digits, valid: isMkEdb(digits) }
    }
    case 'me-pib-local': {
      const digits = normalizeMePib(values.value)
      return { normalized: digits, valid: isMePib(digits) }
    }
    case 'om-vat-local': {
      const compact = normalizeOmVat(values.value)
      return { normalized: compact, valid: isOmVat(compact) }
    }
    case 'cy-vat-local': {
      const compact = normalizeCyVat(values.value)
      return { normalized: compact, formatted: compact.length === 9 ? `CY-${compact}` : compact, valid: isCyVat(compact) }
    }
    case 'mt-vat-local': {
      const digits = normalizeMtVat(values.value)
      return { normalized: digits, formatted: digits.length === 8 ? `${digits.slice(0, 4)}-${digits.slice(4)}` : digits, valid: isMtVat(digits) }
    }
    case 'ad-nrt-local': {
      const compact = normalizeAdNrt(values.value)
      return { normalized: compact, formatted: compact.length === 8 ? `${compact[0]}-${compact.slice(1, 7)}-${compact[7]}` : compact, valid: isAdNrt(compact) }
    }
    case 'li-peid-local': {
      const digits = normalizeLiPeid(values.value)
      return { normalized: digits, valid: /^\d{4,12}$/.test(digits) }
    }
    case 'dz-nif-local': {
      const digits = String(values.value || '').replace(/\D/g, '')
      return { normalized: digits, valid: /^\d{15}$|^\d{20}$/.test(digits) }
    }
    case 'sn-ninea-local': {
      const compact = normalizeSnNinea(values.value)
      return { normalized: compact, formatted: compact.length > 9 ? `${compact.slice(0, -3)} ${compact.slice(-3)}` : compact, valid: isSnNinea(compact) }
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

function isPersonnummer(ten) {
  return /^\d{10}$/.test(ten) && luhnAny(ten)
}

function isHetu(compact) {
  if (!/^\d{6}[-+A-GU-Y]\d{3}[0-9A-Z]$/.test(compact)) return false
  const check = '0123456789ABCDEFHJKLMNPRSTUVWXY'
  const body = compact.slice(0, 6) + compact.slice(7, 10)
  return compact[10] === check[Number(body) % 31]
}

function isFodselsnummer(digits) {
  if (!/^\d{11}$/.test(digits)) return false
  const w1 = [3, 7, 6, 1, 8, 9, 4, 5, 2]
  const w2 = [5, 4, 3, 2, 7, 6, 5, 4, 3, 2]
  const mod11 = (weights, len) => {
    let sum = 0
    for (let i = 0; i < len; i++) sum += Number(digits[i]) * weights[i]
    const rem = sum % 11
    if (rem === 0) return 0
    const check = 11 - rem
    return check === 10 ? -1 : check
  }
  return mod11(w1, 9) === Number(digits[9]) && mod11(w2, 10) === Number(digits[10])
}

function isIswc(compact) {
  if (!/^T\d{10}$/.test(compact)) return false
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(compact[i + 1]) * (i + 1)
  return compact[10] === String(sum % 10)
}

function isAbn(digits) {
  if (!/^\d{11}$/.test(digits)) return false
  const w = [10, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19]
  let sum = (Number(digits[0]) - 1) * w[0]
  for (let i = 1; i < 11; i++) sum += Number(digits[i]) * w[i]
  return sum % 89 === 0
}

function isTfn(digits) {
  if (!/^\d{9}$/.test(digits)) return false
  const w = [1, 4, 3, 7, 5, 8, 6, 9, 10]
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(digits[i]) * w[i]
  return sum % 11 === 0
}

function isVat(compact) {
  if (compact.startsWith('DE') && /^\d{9}$/.test(compact.slice(2))) {
    let product = 10
    const body = compact.slice(2)
    for (let i = 0; i < 8; i++) {
      let sum = (Number(body[i]) + product) % 10
      if (sum === 0) sum = 10
      product = (sum * 2) % 11
    }
    const check = 11 - product
    return (check === 10 ? 0 : check) === Number(body[8])
  }
  if (compact.startsWith('FR') && /^\d{11}$/.test(compact.slice(2))) {
    const siren = compact.slice(4)
    const key = (12 + 3 * (Number(siren) % 97)) % 97
    return isSiren(siren) && Number(compact.slice(2, 4)) === key
  }
  if (compact.startsWith('NL') && /^\d{9}B\d{2}$/.test(compact.slice(2))) {
    const body = compact.slice(2)
    let sum = 0
    for (let i = 0; i < 8; i++) sum += Number(body[i]) * (9 - i)
    return sum % 11 === Number(body[8])
  }
  return false
}

function isAhv(digits) {
  return /^756\d{10}$/.test(digits) && isEan13(digits)
}

function isEan13(digits) {
  if (!/^\d{13}$/.test(digits)) return false
  let sum = 0
  let factor = 3
  for (let i = 11; i >= 0; i--) {
    sum += Number(digits[i]) * factor
    factor = 4 - factor
  }
  return digits[12] === String((10 - (sum % 10)) % 10)
}

function isNip(digits) {
  if (!/^\d{10}$/.test(digits)) return false
  const w = [6, 5, 7, 2, 3, 4, 5, 6, 7]
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(digits[i]) * w[i]
  const rem = sum % 11
  return rem !== 10 && rem === Number(digits[9])
}

function verhoeff(digits) {
  const d = [
    [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
    [1, 2, 3, 4, 0, 6, 7, 8, 9, 5],
    [2, 3, 4, 0, 1, 7, 8, 9, 5, 6],
    [3, 4, 0, 1, 2, 8, 9, 5, 6, 7],
    [4, 0, 1, 2, 3, 9, 5, 6, 7, 8],
    [5, 9, 8, 7, 6, 0, 4, 3, 2, 1],
    [6, 5, 9, 8, 7, 1, 0, 4, 3, 2],
    [7, 6, 5, 9, 8, 2, 1, 0, 4, 3],
    [8, 7, 6, 5, 9, 3, 2, 1, 0, 4],
    [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
  ]
  const p = [
    [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
    [1, 5, 7, 6, 2, 8, 3, 0, 9, 4],
    [5, 8, 0, 3, 7, 9, 6, 1, 4, 2],
    [8, 9, 1, 6, 0, 4, 3, 5, 2, 7],
    [9, 4, 5, 3, 1, 2, 6, 8, 7, 0],
    [4, 2, 8, 6, 5, 7, 9, 3, 0, 1],
    [2, 7, 9, 3, 8, 0, 6, 4, 1, 5],
    [7, 0, 4, 6, 9, 1, 3, 2, 5, 8]
  ]
  let check = 0
  for (let i = 0; i < digits.length; i++) {
    check = d[check][p[i % 8][Number(digits[digits.length - 1 - i])]]
  }
  return check === 0
}

function isPps(compact) {
  const letters = 'WABCDEFGHIJKLMNOPQRSTUV'
  const sum = (body, extra = 0) => {
    let total = extra * 9
    for (let i = 0; i < 7; i++) total += Number(body[i]) * (8 - i)
    return letters[total % 23]
  }
  if (/^\d{7}[A-W]$/.test(compact)) return compact[7] === sum(compact.slice(0, 7))
  return false
}

function isCpr(digits) {
  if (!/^\d{10}$/.test(digits)) return false
  const w = [4, 3, 2, 7, 6, 5, 4, 3, 2, 1]
  let sum = 0
  for (let i = 0; i < 10; i++) sum += Number(digits[i]) * w[i]
  return sum % 11 === 0
}

function isPtNif(digits) {
  if (!/^[1-9]\d{8}$/.test(digits)) return false
  const w = [9, 8, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  const rem = sum % 11
  return Number(digits[8]) === (rem < 2 ? 0 : 11 - rem)
}

function isNino(compact) {
  if (!/^[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z]\d{6}[A-D]$/.test(compact)) return false
  return !['BG', 'GB', 'KN', 'NK', 'NT', 'TN', 'ZZ'].includes(compact.slice(0, 2))
}

function isRrn(digits) {
  if (!/^\d{13}$/.test(digits)) return false
  const w = [2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5]
  let sum = 0
  for (let i = 0; i < 12; i++) sum += Number(digits[i]) * w[i]
  return digits[12] === String((11 - (sum % 11)) % 10)
}

function isAfm(digits) {
  if (!/^\d{9}$/.test(digits)) return false
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * (2 ** (8 - i))
  return Number(digits[8]) === (sum % 11) % 10
}

function isRut(compact) {
  if (!/^\d{7,8}[0-9K]$/.test(compact)) return false
  const body = compact.slice(0, -1)
  let sum = 0
  let weight = 2
  for (let i = body.length - 1; i >= 0; i--) {
    sum += Number(body[i]) * weight
    weight = weight === 7 ? 2 : weight + 1
  }
  const rem = 11 - (sum % 11)
  const check = rem === 11 ? '0' : rem === 10 ? 'K' : String(rem)
  return compact.slice(-1) === check
}

function isCuit(digits) {
  if (!/^\d{11}$/.test(digits)) return false
  const w = [5, 4, 3, 2, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 10; i++) sum += Number(digits[i]) * w[i]
  let rem = 11 - (sum % 11)
  if (rem === 11) rem = 0
  if (rem === 10) rem = 9
  return Number(digits[10]) === rem
}

function isTckn(digits) {
  if (!/^[1-9]\d{10}$/.test(digits)) return false
  let odd = 0
  let even = 0
  for (let i = 0; i < 9; i++) {
    if (i % 2 === 0) odd += Number(digits[i])
    else even += Number(digits[i])
  }
  const d10 = (odd * 7 - even) % 10
  let total = d10
  for (let i = 0; i < 9; i++) total += Number(digits[i])
  return Number(digits[9]) === d10 && Number(digits[10]) === total % 10
}

function isCnp(digits) {
  if (!/^[1-8]\d{12}$/.test(digits)) return false
  const w = [2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9]
  let sum = 0
  for (let i = 0; i < 12; i++) sum += Number(digits[i]) * w[i]
  const rem = sum % 11
  return Number(digits[12]) === (rem === 10 ? 1 : rem)
}

function isThaiId(digits) {
  if (!/^[1-8]\d{12}$/.test(digits)) return false
  let sum = 0
  for (let i = 0; i < 12; i++) sum += Number(digits[i]) * (13 - i)
  return Number(digits[12]) === (11 - (sum % 11)) % 10
}

function isOib(digits) {
  if (!/^\d{11}$/.test(digits)) return false
  let product = 10
  for (let i = 0; i < 10; i++) {
    let sum = (Number(digits[i]) + product) % 10
    if (sum === 0) sum = 10
    product = (sum * 2) % 11
  }
  const check = 11 - product
  return Number(digits[10]) === (check === 10 ? 0 : check)
}

function isJmbg(digits) {
  if (!/^\d{13}$/.test(digits)) return false
  const w = [7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 12; i++) sum += Number(digits[i]) * w[i]
  let rem = 11 - (sum % 11)
  if (rem >= 10) rem = 0
  return Number(digits[12]) === rem
}

function isKennitala(digits) {
  if (!/^\d{10}$/.test(digits) || (digits[9] !== '9' && digits[9] !== '0')) return false
  const w = [3, 2, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  let rem = 11 - (sum % 11)
  if (rem === 10) return false
  if (rem === 11) rem = 0
  return Number(digits[8]) === rem
}

function isTaj(digits) {
  if (!/^\d{9}$/.test(digits)) return false
  const w = [3, 7, 3, 7, 3, 7, 3, 7]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  return Number(digits[8]) === sum % 10
}

function isNit(digits) {
  if (!/^\d{8,16}$/.test(digits)) return false
  const factors = [3, 7, 13, 17, 19, 23, 29, 37, 41, 43, 47, 53, 59, 67, 71]
  const body = digits.slice(0, -1)
  let sum = 0
  for (let i = 0; i < body.length; i++) {
    sum += Number(body[body.length - 1 - i]) * factors[i]
  }
  const rem = sum % 11
  const check = rem <= 1 ? rem : 11 - rem
  return Number(digits[digits.length - 1]) === check
}

function isLatvianPk(digits) {
  if (!/^\d{11}$/.test(digits)) return false
  const w = [1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1]
  let sum = 0
  for (let i = 0; i < 11; i++) sum += Number(digits[i]) * w[i]
  return sum % 11 === 1
}

function isEmso(digits) {
  if (!/^\d{13}$/.test(digits)) return false
  const region = Number(digits.slice(7, 9))
  if (region < 50 || region > 59) return false
  const w = [7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 12; i++) sum += Number(digits[i]) * w[i]
  let rem = 11 - (sum % 11)
  if (rem >= 10) rem = 0
  return Number(digits[12]) === rem
}

function isPeDni(compact) {
  if (!/^\d{8}[0-9A-JK]$/.test(compact)) return false
  const w = [3, 2, 7, 6, 5, 4, 3, 2]
  const numeric = ['6', '7', '8', '9', '0', '1', '1', '2', '3', '4', '5']
  const letters = ['K', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J']
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(compact[i]) * w[i]
  let key = 11 - (sum % 11)
  if (key === 11) key = 0
  const last = compact[8]
  return last === numeric[key] || last === letters[key]
}

function isBsn(digits) {
  const padded = digits.length === 8 ? `0${digits}` : digits
  if (!/^[1-9]\d{8}$/.test(padded) && !/^0[1-9]\d{7}$/.test(padded)) return false
  const w = [9, 8, 7, 6, 5, 4, 3, 2, -1]
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(padded[i]) * w[i]
  return sum !== 0 && sum % 11 === 0
}

function isYTunnus(digits) {
  if (!/^\d{8}$/.test(digits)) return false
  const w = [7, 9, 10, 5, 8, 4, 2]
  let sum = 0
  for (let i = 0; i < 7; i++) sum += Number(digits[i]) * w[i]
  const rem = sum % 11
  if (rem === 1) return false
  const check = rem === 0 ? 0 : 11 - rem
  return Number(digits[7]) === check
}

function isCvr(digits) {
  if (!/^\d{8}$/.test(digits)) return false
  const w = [2, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 7; i++) sum += Number(digits[i]) * w[i]
  const rem = sum % 11
  if (rem === 1) return false
  const check = rem === 0 ? 0 : 11 - rem
  return Number(digits[7]) === check
}

function isCheUid(digits) {
  if (!/^\d{9}$/.test(digits)) return false
  const w = [5, 4, 3, 2, 7, 6, 5, 4]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  const rem = sum % 11
  if (rem === 1) return false
  const check = rem === 0 ? 0 : 11 - rem
  return Number(digits[8]) === check
}

function isCui(digits) {
  if (!/^[1-9]\d{1,9}$/.test(digits)) return false
  const body = digits.slice(0, -1)
  const padded = body.padStart(9, '0')
  const w = [7, 5, 3, 2, 1, 7, 5, 3, 2]
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(padded[i]) * w[i]
  return Number(digits[digits.length - 1]) === ((sum * 10) % 11 % 10)
}

function isKbo(digits) {
  if (!/^[01]\d{9}$/.test(digits)) return false
  const rem = Number(digits.slice(0, 8)) % 97
  const check = String(97 - rem).padStart(2, '0')
  return digits.slice(8) === check
}

function isHojin(digits) {
  if (!/^[1-9]\d{12}$/.test(digits)) return false
  let sum = 0
  for (let i = 0; i < 12; i++) {
    const n = 12 - i
    sum += Number(digits[i + 1]) * (n % 2 === 0 ? 2 : 1)
  }
  return Number(digits[0]) === 9 - (sum % 9)
}

function isKrBrn(digits) {
  if (!/^\d{10}$/.test(digits)) return false
  const w = [1, 3, 7, 1, 3, 7, 1, 3, 5]
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(digits[i]) * w[i]
  sum += Math.floor((Number(digits[8]) * 5) / 10)
  return Number(digits[9]) === (10 - (sum % 10)) % 10
}

function isTwGui(digits) {
  if (!/^\d{8}$/.test(digits)) return false
  const w = [1, 2, 1, 2, 1, 2, 4, 1]
  let sum = 0
  for (let i = 0; i < 8; i++) {
    const n = Number(digits[i]) * w[i]
    sum += Math.floor(n / 10) + (n % 10)
  }
  if (sum % 5 === 0) return true
  return digits[6] === '7' && (sum + 1) % 5 === 0
}

function isEdrpou(digits) {
  if (!/^\d{8}$/.test(digits)) return false
  const first = Number(digits[0])
  let w = first >= 3 && first <= 5 ? [7, 1, 2, 3, 4, 5, 6] : [1, 2, 3, 4, 5, 6, 7]
  let sum = 0
  for (let i = 0; i < 7; i++) sum += Number(digits[i]) * w[i]
  let rem = sum % 11
  if (rem >= 10) {
    w = w.map((n) => n + 2)
    sum = 0
    for (let i = 0; i < 7; i++) sum += Number(digits[i]) * w[i]
    rem = (sum % 11) % 10
  }
  return Number(digits[7]) === rem
}

function isGstin(compact) {
  if (!/^\d{2}[A-Z]{5}\d{4}[A-Z][1-9A-Z]Z[0-9A-Z]$/.test(compact)) return false
  const state = Number(compact.slice(0, 2))
  if (state < 1 || (state > 38 && state !== 97)) return false
  if (!/^[A-Z]{3}[PCHFATBLJG][A-Z]\d{4}[A-Z]$/.test(compact.slice(2, 12))) return false
  const alphabet = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  let sum = 0
  let doubleDigit = false
  for (let i = compact.length - 1; i >= 0; i--) {
    let index = alphabet.indexOf(compact[i])
    if (index < 0) return false
    if (doubleDigit) {
      const doubled = index * 2
      index = Math.floor(doubled / 36) + (doubled % 36)
    }
    sum += index
    doubleDigit = !doubleDigit
  }
  return sum % 36 === 0
}

function isAcn(digits) {
  if (!/^\d{9}$/.test(digits)) return false
  const w = [8, 7, 6, 5, 4, 3, 2, 1]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  return Number(digits[8]) === (10 - (sum % 10)) % 10
}

function isVkn(digits) {
  if (!/^\d{10}$/.test(digits)) return false
  let sum = 0
  for (let i = 1; i <= 9; i++) {
    const n = Number(digits[9 - i])
    const c1 = (n + i) % 10
    if (c1 === 0) continue
    let c2 = (c1 * 2 ** i) % 9
    if (c2 === 0) c2 = 9
    sum += c2
  }
  return Number(digits[9]) === (10 - (sum % 10)) % 10
}

function isNpwp(digits) {
  if (!/^\d{15}$/.test(digits)) return false
  return luhnAny(digits.slice(0, 9))
}

function isNzbn(digits) {
  if (!/^94\d{11}$/.test(digits)) return false
  let sum = 0
  let factor = 3
  for (let i = digits.length - 2; i >= 0; i--) {
    sum += Number(digits[i]) * factor
    factor = 4 - factor
  }
  return Number(digits[12]) === (10 - (sum % 10)) % 10
}

function isUen(compact) {
  if (/^\d{8}[A-Z]$/.test(compact)) {
    const w = [10, 4, 9, 3, 8, 2, 7, 1]
    let sum = 0
    for (let i = 0; i < 8; i++) sum += Number(compact[i]) * w[i]
    return compact[8] === 'XMKECAWLJDB'[sum % 11]
  }
  if (/^\d{9}[A-Z]$/.test(compact)) {
    if (Number(compact.slice(0, 4)) > new Date().getFullYear()) return false
    const w = [10, 8, 6, 4, 9, 7, 5, 3, 1]
    let sum = 0
    for (let i = 0; i < 9; i++) sum += Number(compact[i]) * w[i]
    return compact[9] === 'ZKCMDNERGWH'[sum % 11]
  }
  if (!/^[RST]\d{2}[A-Z]{2}\d{4}[A-Z]$/.test(compact)) return false
  const types = new Set(['CC','CD','CH','CL','CM','CP','CS','CX','DP','FB','FC','FM','FN','GA','GB','GS','HS','LL','LP','MB','MC','MD','MH','MM','MQ','NB','NR','PA','PB','PF','RF','RP','SM','SS','TC','TU','VH','XL'])
  if (!types.has(compact.slice(3, 5))) return false
  if (compact[0] === 'T' && Number(compact.slice(1, 3)) > new Date().getFullYear() % 100) return false
  const alphabet = 'ABCDEFGHJKLMNPQRSTUVWX0123456789'
  const w = [4, 3, 5, 3, 10, 2, 2, 5, 7]
  let sum = 0
  for (let i = 0; i < 9; i++) sum += alphabet.indexOf(compact[i]) * w[i]
  const rem = ((sum - 5) % 11 + 11) % 11
  return compact[9] === alphabet[rem]
}

function isIlHp(digits) {
  return /^5\d{8}$/.test(digits) && luhnAny(digits)
}

function isLtJa(digits) {
  if (!/^\d{7}1\d$/.test(digits)) return false
  const w = [1, 2, 3, 4, 5, 6, 7, 8]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  const rem = sum % 11
  return rem !== 10 && Number(digits[8]) === rem
}

function innWeighted(digits, weights) {
  let sum = 0
  for (let i = 0; i < weights.length; i++) sum += Number(digits[i]) * weights[i]
  return sum % 11 % 10
}

function isInn(digits) {
  if (/^\d{10}$/.test(digits)) {
    return Number(digits[9]) === innWeighted(digits, [2, 4, 10, 3, 5, 9, 4, 6, 8])
  }
  if (!/^\d{12}$/.test(digits)) return false
  const d1 = innWeighted(digits, [7, 2, 4, 10, 3, 5, 9, 4, 6, 8])
  const d2 = innWeighted(digits.slice(0, 10) + String(d1), [3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8])
  return digits.slice(10) === `${d1}${d2}`
}

function isPeRuc(digits) {
  if (!/^(10|15|17|20)\d{9}$/.test(digits)) return false
  const w = [5, 4, 3, 2, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 10; i++) sum += Number(digits[i]) * w[i]
  return Number(digits[10]) === (11 - (sum % 11)) % 10
}

function isNik(digits) {
  if (!/^\d{16}$/.test(digits)) return false
  const provinces = new Set([
    '11', '12', '13', '14', '15', '16', '17', '18', '19', '21',
    '31', '32', '33', '34', '35', '36',
    '51', '52', '53',
    '61', '62', '63', '64', '65',
    '71', '72', '73', '74', '75', '76',
    '81', '82',
    '91', '92', '93', '94', '95', '96'
  ])
  if (!provinces.has(digits.slice(0, 2))) return false
  const day = Number(digits.slice(6, 8)) % 40
  const month = Number(digits.slice(8, 10))
  const year = Number(digits.slice(10, 12))
  const validDate = (y) => {
    const date = new Date(Date.UTC(y, month - 1, day))
    return date.getUTCFullYear() === y && date.getUTCMonth() === month - 1 && date.getUTCDate() === day
  }
  return validDate(year + 1900) || validDate(year + 2000)
}

function isVnMst(digits) {
  if (!/^\d{10}$/.test(digits) && !/^\d{13}$/.test(digits)) return false
  if (digits.slice(2, 9) === '0000000') return false
  if (digits.length === 13 && digits.slice(10) === '000') return false
  const w = [31, 29, 23, 19, 17, 13, 7, 5, 3]
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(digits[i]) * w[i]
  const check = 10 - (sum % 11)
  return check !== 10 && Number(digits[9]) === check
}

const EIN_PREFIXES = new Set([
  '01', '02', '03', '04', '05', '06', '10', '11', '12', '13', '14', '15', '16',
  '20', '21', '22', '23', '24', '25', '26', '27', '30', '31', '32', '33', '34',
  '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47',
  '48', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61',
  '62', '63', '64', '65', '66', '67', '68', '71', '72', '73', '74', '75', '76',
  '77', '80', '81', '82', '83', '84', '85', '86', '87', '88', '90', '91', '92',
  '93', '94', '95', '98', '99'
])

function isEin(raw) {
  if (raw.includes('-') && !/^\d{2}-\d{7}$/.test(raw.trim())) return false
  const digits = String(raw || '').replace(/\D/g, '')
  return /^\d{9}$/.test(digits) && EIN_PREFIXES.has(digits.slice(0, 2))
}

function isOgrn(digits) {
  if (/^[1-9]\d{12}$/.test(digits)) {
    return Number(digits[12]) === Number(digits.slice(0, 12)) % 11 % 10
  }
  if (!/^[34]\d{14}$/.test(digits)) return false
  const rem = Number(digits.slice(0, 14)) % 13
  return rem <= 9 && Number(digits[14]) === rem
}

function isSnils(digits) {
  if (!/^\d{11}$/.test(digits) || digits.startsWith('000000000')) return false
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(digits[i]) * (9 - i)
  let check
  if (sum < 100) check = String(sum).padStart(2, '0')
  else if (sum === 100 || sum === 101) check = '00'
  else {
    const rem = sum % 101
    check = rem === 100 || rem === 101 ? '00' : String(rem).padStart(2, '0')
  }
  return digits.slice(9) === check
}

function isNipt(compact) {
  return /^[A-M]\d{8}[A-Z]$/.test(compact)
}

function isRif(compact) {
  if (!/^[VEJPG]\d{9}$/.test(compact)) return false
  const types = { V: 4, E: 8, J: 12, P: 16, G: 20 }
  const w = [3, 2, 7, 6, 5, 4, 3, 2]
  let sum = types[compact[0]]
  for (let i = 0; i < 8; i++) sum += Number(compact[i + 1]) * w[i]
  return compact[9] === '00987654321'[sum % 11]
}

function isRnc(digits) {
  const whitelist = new Set(['101581601', '101582245', '101595422', '101595785', '10233317', '131188691', '401007374'])
  if (whitelist.has(digits)) return true
  if (!/^\d{9}$/.test(digits)) return false
  const w = [7, 9, 8, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  return Number(digits[8]) === (10 - (sum % 11)) % 9 + 1
}

function normalizeUnp(value) {
  let compact = String(value || '').replace(/\s+/g, '').toUpperCase()
  if (compact.startsWith('UNP') || compact.startsWith('УНП')) compact = compact.slice(3)
  const cyr = { А: 'A', В: 'B', Е: 'E', К: 'K', М: 'M', Н: 'H', О: 'O', Р: 'P', С: 'C', Т: 'T' }
  return [...compact].map((ch) => cyr[ch] || ch).join('')
}

function isUnp(compact) {
  if (compact.length !== 9 || !/^\d{7}$/.test(compact.slice(2))) return false
  if (!'1234567ABCEHKM'.includes(compact[0])) return false
  if (!/\d/.test(compact[1]) && !'ABCEHKMOPT'.includes(compact[1])) return false
  const alphabet = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  const letters = 'ABCEHKMOPT'
  const w = [29, 23, 19, 17, 13, 7, 5, 3]
  let body = compact.slice(0, 8)
  if (!/\d/.test(body[1])) body = body[0] + String(letters.indexOf(body[1])) + body.slice(2)
  let sum = 0
  for (let i = 0; i < 8; i++) sum += alphabet.indexOf(body[i]) * w[i]
  const rem = sum % 11
  return rem <= 9 && compact[8] === String(rem)
}

function isItin(raw) {
  if (raw.includes('-') && !/^\d{3}-\d{2}-\d{4}$/.test(raw.trim())) return false
  const digits = String(raw || '').replace(/\D/g, '')
  if (!/^9\d{8}$/.test(digits)) return false
  const group = Number(digits.slice(3, 5))
  return group >= 70 && group <= 99 && group !== 89 && group !== 93
}

function isCnic(digits) {
  if (!/^[1-7]\d{11}[1-9]$/.test(digits)) return false
  return '123456789'.includes(digits[12])
}

function isIdno(digits) {
  if (!/^\d{13}$/.test(digits)) return false
  const w = [7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1]
  let sum = 0
  for (let i = 0; i < 12; i++) sum += Number(digits[i]) * w[i]
  return digits[12] === String(sum % 10)
}

function isGhTin(compact) {
  if (!/^[PCGQV]00[A-Z0-9]{8}$/.test(compact)) return false
  const body = compact.slice(1, 10)
  if (!/^\d{9}$/.test(body)) return false
  let sum = 0
  for (let i = 0; i < 9; i++) sum += (i + 1) * Number(body[i])
  const rem = sum % 11
  return compact[10] === (rem === 10 ? 'X' : String(rem))
}

function isKePin(compact) {
  return /^[AP]\d{9}[A-Z]$/.test(compact)
}

function isMaIce(digits) {
  return /^\d{15}$/.test(digits) && BigInt(digits) % 97n === 0n
}

function normalizeVoen(value) {
  let digits = String(value || '').replace(/\D/g, '')
  return digits.length === 9 ? `0${digits}` : digits
}

function isVoen(digits) {
  if (!/^\d{9}[12]$/.test(digits)) return false
  const w = [4, 1, 8, 6, 2, 7, 5, 3]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  const rem = sum % 11
  return rem <= 9 && digits[8] === String(rem)
}

function normalizeUyRut(value) {
  let compact = String(value || '').replace(/[\s-]/g, '').toUpperCase()
  if (compact.startsWith('UY')) compact = compact.slice(2)
  return compact.replace(/\D/g, '')
}

function isUyRut(digits) {
  if (!/^\d{12}$/.test(digits)) return false
  const prefix = Number(digits.slice(0, 2))
  if (prefix < 1 || prefix > 22 || digits.slice(2, 8) === '000000' || digits.slice(8, 11) !== '001') return false
  const w = [4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 11; i++) sum += Number(digits[i]) * w[i]
  const rem = ((-sum % 11) + 11) % 11
  return rem <= 9 && digits[11] === String(rem)
}

function isPyRuc(digits) {
  if (!/^\d{2,9}$/.test(digits)) return false
  const body = digits.slice(0, -1)
  let sum = 0
  for (let i = 0; i < body.length; i++) sum += (i + 2) * Number(body[body.length - 1 - i])
  return digits[digits.length - 1] === String((((-sum % 11) + 11) % 11) % 10)
}

function normalizeGtNit(value) {
  return String(value || '').replace(/[\s-]/g, '').toUpperCase().replace(/^0+/, '')
}

function isGtNit(compact) {
  if (compact.length < 2 || compact.length > 12) return false
  const body = compact.slice(0, -1)
  const check = compact.slice(-1)
  if (!/^\d+$/.test(body) || (check !== 'K' && !/^\d$/.test(check))) return false
  let sum = 0
  for (let i = 0; i < body.length; i++) sum += Number(body[body.length - 1 - i]) * (i + 2)
  const rem = ((-sum % 11) + 11) % 11
  return check === (rem === 10 ? 'K' : String(rem))
}

function normalizeCrCpf(value) {
  let text = String(value || '').replace(/\s+/g, '').toUpperCase()
  const parts = text.split('-')
  if (parts.length === 3) {
    text = parts[0].replace(/\D/g, '').padStart(2, '0') + parts[1].replace(/\D/g, '').padStart(4, '0') + parts[2].replace(/\D/g, '').padStart(4, '0')
  } else {
    text = text.replace(/\D/g, '')
  }
  return text.length === 9 ? `0${text}` : text
}

function isCrCpf(digits) {
  return /^0\d{9}$/.test(digits)
}

function isCrCpj(digits) {
  if (!/^\d{10}$/.test(digits)) return false
  const cls = digits[0]
  const type = digits.slice(1, 4)
  if (cls === '2') return ['100', '200', '300', '400'].includes(type)
  if (cls === '3') {
    return ['002', '003', '004', '005', '006', '007', '008', '009', '010', '011', '012', '013', '014',
      '101', '102', '103', '104', '105', '106', '107', '108', '109', '110'].includes(type)
  }
  if (cls === '4') return type === '000'
  if (cls === '5') return type === '001'
  return false
}

function normalizeTnMf(value) {
  const compact = String(value || '').replace(/[\s/.-]/g, '').toUpperCase()
  const match = compact.match(/^(\d+)(.*)$/)
  if (!match) return compact
  return match[1].padStart(7, '0') + match[2]
}

function formatTnMf(compact) {
  if (compact.length === 8) return `${compact.slice(0, 7)}/${compact[7]}`
  if (compact.length === 13) return `${compact.slice(0, 7)}/${compact[7]}/${compact[8]}/${compact[9]}/${compact.slice(10)}`
  return compact
}

function isTnMf(compact) {
  if (compact.length !== 8 && compact.length !== 13) return false
  if (!/^\d{7}/.test(compact)) return false
  if (!'ABCDEFGHJKLMNPQRSTVWXYZ'.includes(compact[7])) return false
  if (compact.length === 8) return true
  if (!'APBDN'.includes(compact[8]) || !'MPCNE'.includes(compact[9])) return false
  if (!/^\d{3}$/.test(compact.slice(10))) return false
  return compact.slice(10) === '000' || compact[9] === 'E'
}

function normalizeEgTn(value) {
  const mapped = String(value || '').replace(/[\u0660-\u0669]/g, (ch) => String(ch.charCodeAt(0) - 0x0660)).replace(/[\u06F0-\u06F9]/g, (ch) => String(ch.charCodeAt(0) - 0x06F0))
  return mapped.replace(/\D/g, '')
}

function normalizeLuTva(value) {
  let compact = String(value || '').replace(/[\s:.-]/g, '').toUpperCase()
  if (compact.startsWith('LU')) compact = compact.slice(2)
  return compact.replace(/\D/g, '')
}

function isLuTva(digits) {
  return /^\d{8}$/.test(digits) && Number(digits.slice(0, 6)) % 89 === Number(digits.slice(6))
}

function normalizeSvNit(value) {
  let compact = String(value || '').replace(/[\s-]/g, '').toUpperCase()
  if (compact.startsWith('SV')) compact = compact.slice(2)
  return compact.replace(/\D/g, '')
}

function isSvNit(digits) {
  if (!/^[019]\d{13}$/.test(digits)) return false
  const body = digits.slice(0, 13)
  const old = body.slice(10) <= '100'
  const weights = old ? [14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2] : [2, 7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 13; i++) sum += Number(body[i]) * weights[i]
  const rem = old ? (sum % 11) % 10 : (((-sum % 11) + 11) % 11) % 10
  return digits[13] === String(rem)
}

function normalizeMkEdb(value) {
  let compact = String(value || '').replace(/[\s-]/g, '').toUpperCase()
  if (compact.startsWith('MK') || compact.startsWith('МК')) compact = compact.slice(2)
  return compact.replace(/\D/g, '')
}

function isMkEdb(digits) {
  if (!/^\d{13}$/.test(digits)) return false
  const w = [7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 12; i++) sum += Number(digits[i]) * w[i]
  return digits[12] === String((((-sum % 11) + 11) % 11) % 10)
}

function normalizeMePib(value) {
  let compact = String(value || '').replace(/[\s.-]/g, '').toUpperCase()
  if (compact.startsWith('ME')) compact = compact.slice(2)
  return compact.replace(/\D/g, '')
}

function isMePib(digits) {
  if (!/^\d{8}$/.test(digits)) return false
  const w = [8, 7, 6, 5, 4, 3, 2]
  let sum = 0
  for (let i = 0; i < 7; i++) sum += Number(digits[i]) * w[i]
  return digits[7] === String((((-sum % 11) + 11) % 11) % 10)
}

function normalizeOmVat(value) {
  return String(value || '').replace(/[\s-]/g, '').toUpperCase()
}

function isOmVat(compact) {
  if (!/^OM\d{9}[0-9X]$/.test(compact)) return false
  const w = [1, 6, 3, 7, 9]
  let sum = 1
  for (let i = 0; i < 5; i++) sum += Number(compact[6 + i]) * w[i]
  const rem = sum % 11
  return compact[11] === (rem === 10 ? 'X' : String(rem))
}

function normalizeCyVat(value) {
  let compact = String(value || '').replace(/[\s-]/g, '').toUpperCase()
  if (compact.startsWith('CY')) compact = compact.slice(2)
  return compact
}

function isCyVat(compact) {
  if (!/^\d{8}[A-Z]$/.test(compact) || compact.startsWith('12')) return false
  const even = [1, 0, 5, 7, 9, 13, 15, 17, 19, 21]
  let sum = 0
  for (let i = 0; i < 8; i++) {
    const n = Number(compact[i])
    sum += i % 2 === 0 ? even[n] : n
  }
  return compact[8] === String.fromCharCode(65 + (sum % 26))
}

function normalizeMtVat(value) {
  let compact = String(value || '').replace(/[\s-]/g, '').toUpperCase()
  if (compact.startsWith('MT')) compact = compact.slice(2)
  return compact.replace(/\D/g, '')
}

function isMtVat(digits) {
  if (!/^[1-9]\d{7}$/.test(digits)) return false
  const w = [3, 4, 6, 7, 8, 9, 10, 1]
  let sum = 0
  for (let i = 0; i < 8; i++) sum += Number(digits[i]) * w[i]
  return sum % 37 === 0
}

function normalizeAdNrt(value) {
  return String(value || '').replace(/[\s.-]/g, '').toUpperCase()
}

function isAdNrt(compact) {
  if (!/^[A-Z]\d{6}[A-Z]$/.test(compact)) return false
  if (!'ACDEFGLOPU'.includes(compact[0])) return false
  const mid = compact.slice(1, 7)
  if (compact[0] === 'F' && mid > '699999') return false
  if ((compact[0] === 'A' || compact[0] === 'L') && !(mid > '699999' && mid < '800000')) return false
  return true
}

function normalizeLiPeid(value) {
  return String(value || '').replace(/\D/g, '').replace(/^0+/, '')
}

function normalizeSnNinea(value) {
  return String(value || '').replace(/[\s/,\-]/g, '').toUpperCase()
}

function isSnNinea(compact) {
  let body = compact
  let cofi = ''
  if (compact.length > 9) {
    cofi = compact.slice(-3)
    body = compact.slice(0, -3)
  }
  if (!/^\d{7}$|^\d{9}$/.test(body)) return false
  if (cofi && !/^[012][ABCDEFGHJKLMNPQRSTUVWZ]\d$/.test(cofi)) return false
  const padded = body.padStart(9, '0')
  const w = [1, 2, 1, 2, 1, 2, 1, 2, 1]
  let sum = 0
  for (let i = 0; i < 9; i++) sum += Number(padded[i]) * w[i]
  return sum % 10 === 0
}

function isRegistrikood(digits) {
  if (!/^[1789]\d{7}$/.test(digits)) return false
  const primary = [1, 2, 3, 4, 5, 6, 7]
  const secondary = [3, 4, 5, 6, 7, 8, 9]
  let sum = 0
  for (let i = 0; i < 7; i++) sum += Number(digits[i]) * primary[i]
  let rem = sum % 11
  if (rem === 10) {
    sum = 0
    for (let i = 0; i < 7; i++) sum += Number(digits[i]) * secondary[i]
    rem = sum % 11
    if (rem === 10) rem = 0
  }
  return Number(digits[7]) === rem
}

function isPib(digits) {
  if (!/^[1-9]\d{8}$/.test(digits)) return false
  let product = 10
  for (let i = 0; i < 8; i++) {
    let s = (Number(digits[i]) + product) % 10
    if (s === 0) s = 10
    product = (s * 2) % 11
  }
  const check = 11 - product
  return Number(digits[8]) === (check === 10 ? 0 : check)
}

function isCif(compact) {
  if (!/^[ABCDEFGHJNPQRSUVW]\d{7}[0-9A-J]$/.test(compact)) return false
  const body = compact.slice(1, 8)
  let sum = 0
  for (let i = 0; i < 7; i++) {
    let n = Number(body[i])
    if (i % 2 === 0) {
      n *= 2
      sum += Math.floor(n / 10) + (n % 10)
    } else {
      sum += n
    }
  }
  const digit = String((10 - (sum % 10)) % 10)
  const letter = 'JABCDEFGHI'[Number(digit)]
  const type = compact[0]
  const last = compact[8]
  if ('KPQS'.includes(type)) return last === letter
  if ('ABEH'.includes(type)) return last === digit
  return last === digit || last === letter
}

function isRodne(digits) {
  if (!/^\d{9,10}$/.test(digits)) return false
  if (digits.length === 9) return true
  const n = Number(digits)
  if (n % 11 === 0) return true
  return Number(digits.slice(0, 9)) % 11 === 10 && digits[9] === '0'
}

function isMxRfc(compact) {
  if (compact === 'XAXX010101000' || compact === 'XEXX010101000') return true
  if (!/^[A-ZÑ&]{3,4}\d{6}[A-Z0-9]{3}$/.test(compact)) return false
  const map = '0123456789ABCDEFGHIJKLMN&OPQRSTUVWXYZ '
  let body = compact.slice(0, -1)
  if (body.length === 11) body = ` ${body}`
  if (body.length !== 12) return false
  let sum = 0
  for (let i = 0; i < 12; i++) {
    const ch = body[i]
    const value = ch === 'Ñ' ? 38 : Math.max(0, map.indexOf(ch))
    sum += value * (13 - i)
  }
  const rem = sum % 11
  const check = rem === 0 ? '0' : rem === 1 ? 'A' : String(11 - rem)
  return compact[compact.length - 1] === check
}

function isSscc(digits) {
  if (!/^\d{18}$/.test(digits)) return false
  let sum = 0
  let factor = 3
  for (let i = 16; i >= 0; i--) {
    sum += Number(digits[i]) * factor
    factor = 4 - factor
  }
  return digits[17] === String((10 - (sum % 10)) % 10)
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
