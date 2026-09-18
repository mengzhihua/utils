import { acceptLanguage } from '../composables/useI18n'

export async function callTool(tool, values) {
  const started = performance.now()
  let url = tool.path
  const init = {
    method: tool.method,
    headers: { Accept: 'application/json', 'Accept-Language': acceptLanguage() }
  }

  if (tool.method === 'GET') {
    const params = new URLSearchParams()
    for (const field of tool.fields || []) {
      const value = values[field.name]
      if (value !== undefined && value !== null && String(value).length > 0) {
        params.set(field.name, String(value))
      }
    }
    const query = params.toString()
    if (query) {
      url += (url.includes('?') ? '&' : '?') + query
    }
  } else {
    init.headers['Content-Type'] = 'application/json'
    const body = {}
    for (const field of tool.fields || []) {
      body[field.name] = values[field.name]
    }
    init.body = JSON.stringify(body)
  }

  const response = await fetch(url, init)
  const elapsed = Math.round(performance.now() - started)
  const traceId = response.headers.get('X-Trace-Id')
  let payload
  const text = await response.text()
  try {
    payload = text ? JSON.parse(text) : null
  } catch {
    payload = { code: response.status, message: text || 'invalid json', data: null }
  }
  return {
    ok: response.ok && payload?.code === 200,
    status: response.status,
    elapsed,
    traceId,
    payload,
    curl: toCurl(tool, values)
  }
}

function toCurl(tool, values) {
  if (tool.method === 'GET') {
    const params = new URLSearchParams()
    for (const field of tool.fields || []) {
      const value = values[field.name]
      if (value !== undefined && value !== null && String(value).length > 0) {
        params.set(field.name, String(value))
      }
    }
    const query = params.toString()
    const url = query ? `${tool.path}?${query}` : tool.path
    return `curl "${url}"`
  }
  const body = {}
  for (const field of tool.fields || []) {
    body[field.name] = values[field.name]
  }
  return `curl -X POST "${tool.path}" -H "Content-Type: application/json" -d '${JSON.stringify(body)}'`
}
