export const clientGroups = [
  { id: 'codec', label: '编解码' },
  { id: 'text', label: '文本' },
  { id: 'hash', label: '哈希' },
  { id: 'gen', label: '生成' },
  { id: 'time', label: '时间 / 颜色' },
  { id: 'net', label: '网络' }
]

export const clientTools = [
  {
    id: 'json-format',
    group: 'codec',
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
    group: 'codec',
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
    group: 'codec',
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
    id: 'html-escape',
    group: 'codec',
    title: 'HTML 转义',
    summary: '本地 escape / unescape',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'escape', options: [
        { value: 'escape', label: '转义' },
        { value: 'unescape', label: '反转义' }
      ] },
      { name: 'text', label: '文本', type: 'textarea', value: '<b>hello</b>' }
    ]
  },
  {
    id: 'unicode',
    group: 'codec',
    title: 'Unicode',
    summary: '中文 ↔ \\uXXXX',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'encode', options: [
        { value: 'encode', label: '转 Unicode' },
        { value: 'decode', label: '解码' }
      ] },
      { name: 'text', label: '文本', type: 'textarea', value: '工具集' }
    ]
  },
  {
    id: 'hex-codec',
    group: 'codec',
    title: 'HEX 编解码',
    summary: 'UTF-8 ↔ hex',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'encode', options: [
        { value: 'encode', label: '编码' },
        { value: 'decode', label: '解码' }
      ] },
      { name: 'text', label: '文本', type: 'textarea', value: 'hello' }
    ]
  },
  {
    id: 'jwt-decode',
    group: 'codec',
    title: 'JWT 解码',
    summary: '只拆 header / payload，不验签',
    fields: [{ name: 'token', label: 'JWT', type: 'textarea', value: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZGEiLCJpYXQiOjE3MTAwMDAwMDB9.signature' }]
  },
  {
    id: 'json-csv',
    group: 'codec',
    title: 'JSON ↔ CSV',
    summary: '对象数组互转',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'toCsv', options: [
        { value: 'toCsv', label: 'JSON → CSV' },
        { value: 'toJson', label: 'CSV → JSON' }
      ] },
      { name: 'text', label: '内容', type: 'textarea', value: '[{"name":"Ada","age":18},{"name":"Bob","age":20}]' }
    ]
  },
  {
    id: 'xml-format',
    group: 'codec',
    title: 'XML 格式化',
    summary: '浏览器 DOMParser',
    fields: [{ name: 'text', label: 'XML', type: 'textarea', value: '<root><item id="1">Ada</item></root>' }]
  },
  {
    id: 'markdown',
    group: 'codec',
    title: 'Markdown 预览',
    summary: '轻量转换，防 XSS',
    fields: [{ name: 'text', label: 'Markdown', type: 'textarea', value: '# 标题\n**粗体** 和 `code`\n\n- 列表' }]
  },
  {
    id: 'word-count',
    group: 'text',
    title: '字数统计',
    summary: '字符 / 词 / 行 / 字节',
    fields: [{ name: 'text', label: '文本', type: 'textarea', value: 'Spring Boot 通用工具集' }]
  },
  {
    id: 'case-convert',
    group: 'text',
    title: '命名风格',
    summary: 'camel / snake / kebab / Pascal',
    fields: [{ name: 'text', label: '文本', value: 'userName' }]
  },
  {
    id: 'width-convert',
    group: 'text',
    title: '全角 / 半角',
    summary: 'ASCII 全半角互转',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'half', options: [
        { value: 'half', label: '全角 → 半角' },
        { value: 'full', label: '半角 → 全角' }
      ] },
      { name: 'text', label: '文本', type: 'textarea', value: 'Ｈｅｌｌｏ　１２３' }
    ]
  },
  {
    id: 'line-tools',
    group: 'text',
    title: '行处理',
    summary: '排序 / 去重 / 反转 / 去空',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'unique', options: [
        { value: 'unique', label: '去重' },
        { value: 'sort', label: '排序' },
        { value: 'reverse', label: '反转' },
        { value: 'trim', label: '去空行' }
      ] },
      { name: 'text', label: '文本', type: 'textarea', value: 'banana\napple\nbanana\n\ncherry' }
    ]
  },
  {
    id: 'text-diff',
    group: 'text',
    title: '文本对比',
    summary: '按行 added / removed',
    fields: [
      { name: 'left', label: '原文', type: 'textarea', value: 'a\nb\nc' },
      { name: 'right', label: '新文', type: 'textarea', value: 'b\nc\nd' }
    ]
  },
  {
    id: 'regex-test',
    group: 'text',
    title: '正则测试',
    summary: 'matchAll 查看捕获组',
    fields: [
      { name: 'pattern', label: '正则', value: '1[3-9]\\d{9}' },
      { name: 'flags', label: 'flags', value: 'g' },
      { name: 'text', label: '文本', type: 'textarea', value: '联系电话 13812345678 和 13900001111' }
    ]
  },
  {
    id: 'sha256',
    group: 'hash',
    title: 'SHA-256',
    summary: 'Web Crypto，本地哈希',
    fields: [{ name: 'text', label: '原文', type: 'textarea', value: 'hello' }]
  },
  {
    id: 'md5',
    group: 'hash',
    title: 'MD5',
    summary: '本地实现，仅校验用',
    fields: [{ name: 'text', label: '原文', type: 'textarea', value: 'hello' }]
  },
  {
    id: 'hmac',
    group: 'hash',
    title: 'HMAC-SHA256',
    summary: 'Web Crypto 签名',
    fields: [
      { name: 'text', label: '原文', type: 'textarea', value: 'hello' },
      { name: 'secret', label: '密钥', value: 'secret' }
    ]
  },
  {
    id: 'crc32',
    group: 'hash',
    title: 'CRC32',
    summary: '本地校验和',
    fields: [{ name: 'text', label: '原文', value: 'hello' }]
  },
  {
    id: 'uuid-local',
    group: 'gen',
    title: '浏览器 UUID',
    summary: 'crypto.randomUUID',
    fields: []
  },
  {
    id: 'password-gen',
    group: 'gen',
    title: '密码生成',
    summary: '本地随机，不上传',
    fields: [{ name: 'length', label: '长度', value: '16' }]
  },
  {
    id: 'ulid-local',
    group: 'gen',
    title: '浏览器 ULID',
    summary: '时间有序 ID',
    fields: []
  },
  {
    id: 'timestamp',
    group: 'time',
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
    id: 'color-convert',
    group: 'time',
    title: '颜色转换',
    summary: 'HEX ↔ RGB / HSL',
    fields: [
      { name: 'mode', label: '模式', type: 'select', value: 'hex', options: [
        { value: 'hex', label: 'HEX → RGB' },
        { value: 'rgb', label: 'RGB → HEX' }
      ] },
      { name: 'hex', label: 'HEX 或 rgb()', value: '#0f766e' }
    ]
  },
  {
    id: 'query-parse',
    group: 'net',
    title: 'Query 解析',
    summary: 'URLSearchParams',
    fields: [{ name: 'text', label: 'URL / Query', type: 'textarea', value: 'https://example.com?q=工具&x=1' }]
  },
  {
    id: 'cookie-parse',
    group: 'net',
    title: 'Cookie 解析',
    summary: 'name=value; 拆成对象',
    fields: [{ name: 'text', label: 'Cookie', type: 'textarea', value: 'token=abc; theme=dark; lang=zh' }]
  },
  {
    id: 'ua-parse',
    group: 'net',
    title: 'UA 解析',
    summary: '浏览器 / 系统粗分',
    fields: [{ name: 'text', label: 'User-Agent', type: 'textarea', value: 'Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 Chrome/120.0.0.0 Mobile Safari/604.1' }]
  },
  {
    id: 'cidr-calc',
    group: 'net',
    title: 'CIDR 计算',
    summary: '网段 / 广播 / 容量',
    fields: [
      { name: 'ip', label: 'IP', value: '172.16.0.10' },
      { name: 'cidr', label: 'CIDR', value: '172.16.0.0/24' }
    ]
  },
  {
    id: 'image-base64',
    group: 'net',
    title: '图片转 Base64',
    summary: '生成 data URL',
    fields: [{ name: 'file', label: '图片', type: 'file' }]
  },
  {
    id: 'uuid-v7',
    group: 'gen',
    title: 'UUID v7',
    summary: '时间有序 UUID，本地生成',
    fields: []
  },
  {
    id: 'js-escape',
    group: 'codec',
    title: 'JS / CSV 转义',
    summary: '本地转义，不走后端',
    fields: [{ name: 'text', label: '文本', type: 'textarea', value: 'a"b\'c' }]
  },
  {
    id: 'highlight-local',
    group: 'text',
    title: '关键字高亮',
    summary: '本地 HTML 高亮',
    fields: [
      { name: 'text', label: '文本', type: 'textarea', value: 'Spring Boot 工具集' },
      { name: 'keyword', label: '关键字', value: '工具' }
    ]
  },
  {
    id: 'zodiac-local',
    group: 'time',
    title: '星座 / 生肖',
    summary: '本地按日期计算',
    fields: [{ name: 'date', label: '日期', value: '1990-03-07' }]
  },
  {
    id: 'duration-local',
    group: 'time',
    title: '时长解析',
    summary: '1h30m / 90s / PT15M',
    fields: [{ name: 'text', label: '时长', value: '1h30m' }]
  },
  {
    id: 'slug-local',
    group: 'text',
    title: 'URL slug',
    summary: '本地生成 slug',
    fields: [{ name: 'text', label: '文本', value: 'Spring Boot 工具集' }]
  },
  {
    id: 'murmur-local',
    group: 'hash',
    title: 'Murmur3-32',
    summary: '本地非加密哈希',
    fields: [{ name: 'text', label: '原文', value: 'hello' }]
  }
]

export function getClientTool(id) {
  return clientTools.find((item) => item.id === id)
}

export function groupedClientTools() {
  return clientGroups
    .map((group) => ({
      ...group,
      tools: clientTools.filter((item) => item.group === group.id)
    }))
    .filter((group) => group.tools.length > 0)
}
