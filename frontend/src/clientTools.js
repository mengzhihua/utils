export const clientTools = [
  {
    id: 'json-format',
    title: 'JSON 格式化',
    summary: '格式化 / 压缩，不走后端',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'pretty', options: [
        { value: 'pretty', label: '格式化' },
        { value: 'minify', label: '压缩' }
      ] },
      { name: 'text', label: 'JSON', type: 'textarea', value: '{"name":"Ada","skills":["java","vue"]}' }
    ]
  },
  {
    id: 'base64',
    title: 'Base64',
    summary: 'UTF-8 编解码',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'encode', options: [
        { value: 'encode', label: '编码' },
        { value: 'decode', label: '解码' }
      ] },
      { name: 'text', label: '文本', type: 'textarea', value: 'hello 工具集' }
    ]
  },
  {
    id: 'url-codec',
    title: 'URL 编解码',
    summary: 'encodeURIComponent',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'encode', options: [
        { value: 'encode', label: '编码' },
        { value: 'decode', label: '解码' }
      ] },
      { name: 'text', label: '文本', type: 'textarea', value: 'https://example.com?q=工具' }
    ]
  },
  {
    id: 'timestamp',
    title: '时间戳转换',
    summary: '秒 / 毫秒 / 日期互转',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'now', options: [
        { value: 'now', label: '当前时间戳' },
        { value: 'toDate', label: '时间戳 → 日期' },
        { value: 'fromDate', label: '日期 → 时间戳' }
      ] },
      { name: 'text', label: '输入', value: '' }
    ]
  },
  {
    id: 'uuid-local',
    title: '浏览器 UUID',
    summary: 'crypto.randomUUID',
    fields: []
  },
  {
    id: 'password-gen',
    title: '密码生成',
    summary: '本地随机，不上传',
    fields: [{ name: 'length', label: '长度', value: '16' }]
  },
  {
    id: 'word-count',
    title: '字数统计',
    summary: '字符 / 词 / 行 / 字节',
    fields: [{ name: 'text', label: '文本', type: 'textarea', value: 'Spring Boot 通用工具集' }]
  },
  {
    id: 'regex-test',
    title: '正则测试',
    summary: 'matchAll 查看捕获组',
    fields: [
      { name: 'pattern', label: '正则', value: '1[3-9]\\d{9}' },
      { name: 'flags', label: 'flags', value: 'g' },
      { name: 'text', label: '文本', type: 'textarea', value: '联系电话 13812345678 和 13900001111' }
    ]
  },
  {
    id: 'color-convert',
    title: '颜色转换',
    summary: 'HEX → RGB / HSL',
    fields: [{ name: 'hex', label: 'HEX', value: '#0f766e' }]
  },
  {
    id: 'sha256',
    title: 'SHA-256',
    summary: 'Web Crypto，本地哈希',
    fields: [{ name: 'text', label: '原文', type: 'textarea', value: 'hello' }]
  },
  {
    id: 'image-base64',
    title: '图片转 Base64',
    summary: '生成 data URL',
    fields: [{ name: 'file', label: '图片', type: 'file' }]
  }
]

export function getClientTool(id) {
  return clientTools.find((item) => item.id === id)
}
