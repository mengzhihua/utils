export const clientGroups = [
  { id: 'codec', label: '编解码' },
  { id: 'text', label: '文本' },
  { id: 'hash', label: '哈希' },
  { id: 'gen', label: '生成' },
  { id: 'fe', label: '前端实用' },
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
    id: 'regex-extract-local',
    group: 'text',
    title: '正则抽取',
    summary: '本地提取手机 / 邮箱 / URL',
    fields: [{
      name: 'text',
      label: '文本',
      type: 'textarea',
      value: '联系 Ada ada@example.com 电话 13812345678 打开 https://example.com 颜色 #0F766E 日期 2026-09-17'
    }]
  },
  {
    id: 'regex-escape-local',
    group: 'text',
    title: '正则转义',
    summary: '转义元字符',
    fields: [{ name: 'text', label: '文本', value: 'a.b+(c)' }]
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
  },
  {
    id: 'expr-local',
    group: 'gen',
    title: '四则运算',
    summary: '本地计算 1+2*3',
    fields: [{ name: 'expression', label: '表达式', value: '(1+2)*3' }]
  },
  {
    id: 'radix-local',
    group: 'hash',
    title: '进制转换',
    summary: '2-36 进制',
    fields: [
      { name: 'value', label: '数值', value: '255' },
      { name: 'from', label: '源进制', value: '10' },
      { name: 'to', label: '目标进制', value: '16' }
    ]
  },
  {
    id: 'isbn-local',
    group: 'net',
    title: 'ISBN 校验',
    summary: 'ISBN-10 / 13',
    fields: [{ name: 'code', label: 'ISBN', value: '9780306406157' }]
  },
  {
    id: 'punycode-local',
    group: 'net',
    title: 'Punycode',
    summary: '浏览器 IDN',
    fields: [{ name: 'domain', label: '域名', value: '清华大学.cn' }]
  },
  {
    id: 'roman-local',
    group: 'gen',
    title: '罗马数字',
    summary: '本地 1-3999',
    fields: [{ name: 'value', label: '数字或罗马', value: '1994' }]
  },
  {
    id: 'unit-local',
    group: 'gen',
    title: '单位换算',
    summary: '长度 / 质量 / 温度',
    fields: [
      { name: 'value', label: '数值', value: '1' },
      { name: 'from', label: '源单位', type: 'select', value: 'km', options: [
        { value: 'km', label: 'km' }, { value: 'm', label: 'm' }, { value: 'cm', label: 'cm' },
        { value: 'kg', label: 'kg' }, { value: 'g', label: 'g' },
        { value: 'c', label: '°C' }, { value: 'f', label: '°F' }
      ] },
      { name: 'to', label: '目标单位', type: 'select', value: 'm', options: [
        { value: 'm', label: 'm' }, { value: 'km', label: 'km' }, { value: 'cm', label: 'cm' },
        { value: 'kg', label: 'kg' }, { value: 'g', label: 'g' },
        { value: 'c', label: '°C' }, { value: 'f', label: '°F' }
      ] }
    ]
  },
  {
    id: 'gcd-local',
    group: 'gen',
    title: '最大公约数',
    summary: '本地 gcd / lcm',
    fields: [
      { name: 'a', label: 'A', value: '12' },
      { name: 'b', label: 'B', value: '18' }
    ]
  },
  {
    id: 'imei-local',
    group: 'net',
    title: 'IMEI 校验',
    summary: '15 位 Luhn',
    fields: [{ name: 'value', label: 'IMEI', value: '490154203237518' }]
  },
  {
    id: 'url-parse-local',
    group: 'net',
    title: 'URL 解析',
    summary: '浏览器 URL',
    fields: [{ name: 'url', label: 'URL', value: 'https://example.com:8443/search?q=工具#top' }]
  },
  {
    id: 'soundex-local',
    group: 'text',
    title: 'Soundex',
    summary: '本地读音码',
    fields: [
      { name: 'left', label: '姓名 A', value: 'Robert' },
      { name: 'right', label: '姓名 B', value: 'Rupert' }
    ]
  },
  {
    id: 'jaro-local',
    group: 'text',
    title: 'Jaro-Winkler',
    summary: '本地相似度',
    fields: [
      { name: 'left', label: '文本 A', value: 'MARTHA' },
      { name: 'right', label: '文本 B', value: 'MARHTA' }
    ]
  },
  {
    id: 'iban-local',
    group: 'net',
    title: 'IBAN 校验',
    summary: 'MOD-97',
    fields: [{ name: 'value', label: 'IBAN', value: 'GB82 WEST 1234 5698 7654 32' }]
  },
  {
    id: 'ean-local',
    group: 'net',
    title: 'EAN 校验',
    summary: 'GTIN 校验位',
    fields: [{ name: 'value', label: '条码', value: '5901234123457' }]
  },
    {
      id: 'age-local',
      group: 'time',
      title: '年龄',
      summary: '本地按生日计算',
      fields: [{ name: 'birthday', label: '生日', value: '1990-03-07' }]
    },
    {
      id: 'rot13-local',
      group: 'codec',
      title: 'ROT13',
      summary: '本地 ROT13',
      fields: [{ name: 'text', label: '文本', value: 'Hello' }]
    },
    {
      id: 'morse-local',
      group: 'codec',
      title: '摩斯电码',
      summary: '本地编解码',
      fields: [{ name: 'text', label: '文本', value: 'SOS' }]
    },
    {
      id: 'wildcard-local',
      group: 'text',
      title: '通配符',
      summary: '* / ? 匹配',
      fields: [
        { name: 'text', label: '文本', value: 'Foo.java' },
        { name: 'pattern', label: '模式', value: '*.java' }
      ]
    },
    {
      id: 'isin-local',
      group: 'net',
      title: 'ISIN 校验',
      summary: 'ISO 6166',
      fields: [{ name: 'value', label: 'ISIN', value: 'US0378331005' }]
    },
    {
      id: 'humanize-local',
      group: 'text',
      title: 'Humanize',
      summary: '1.2K / 21st',
      fields: [
        { name: 'value', label: '数字', value: '1234' },
        { name: 'ordinal', label: '序数', value: '21' }
      ]
    },
    {
      id: 'qp-local',
      group: 'codec',
      title: 'Quoted-Printable',
      summary: '本地编解码',
      fields: [{ name: 'text', label: '文本', value: 'Hello =' }]
    },
    {
      id: 'contrast-local',
      group: 'time',
      title: 'WCAG 对比度',
      summary: '本地亮度对比',
      fields: [
        { name: 'left', label: '颜色 A', value: '#FFFFFF' },
        { name: 'right', label: '颜色 B', value: '#000000' }
      ]
    },
    {
      id: 'base45-local',
      group: 'codec',
      title: 'Base45',
      summary: 'RFC 9285',
      fields: [{ name: 'text', label: '文本', value: 'AB' }]
    },
    {
      id: 'accent-local',
      group: 'text',
      title: '去音调',
      summary: 'café → cafe',
      fields: [{ name: 'text', label: '文本', value: 'café naïve' }]
    },
    {
      id: 'plate-local',
      group: 'net',
      title: '车牌校验',
      summary: '京A12345',
      fields: [{ name: 'value', label: '车牌', value: '京A12345' }]
    },
    {
      id: 'emoji-local',
      group: 'text',
      title: 'Emoji',
      summary: '提取 / 移除',
      fields: [{ name: 'text', label: '文本', value: 'hello 😀 工具' }]
    },
    {
      id: 'cusip-local',
      group: 'net',
      title: 'CUSIP 校验',
      summary: 'Apple 037833100',
      fields: [{ name: 'value', label: 'CUSIP', value: '037833100' }]
    },
    {
      id: 'isbn-convert-local',
      group: 'net',
      title: 'ISBN 互转',
      summary: '10 ↔ 13',
      fields: [{ name: 'code', label: 'ISBN', value: '0306406152' }]
    },
    {
      id: 'ipv6-local',
      group: 'net',
      title: 'IPv6 展开',
      summary: '本地压缩 / 展开',
      fields: [{ name: 'ip', label: 'IPv6', value: '2001:db8::1' }]
    },
    {
      id: 'bank-brand-local',
      group: 'net',
      title: '卡组织',
      summary: 'Visa / 银联',
      fields: [{ name: 'cardNo', label: '卡号', value: '4111111111111111' }]
    },
    {
      id: 'between-local',
      group: 'time',
      title: '时长差',
      summary: '秒 → 天小时分',
      fields: [{ name: 'seconds', label: '秒数', value: '183900' }]
    },
    {
      id: 'sub-between-local',
      group: 'text',
      title: '提取中间串',
      summary: 'subBetween',
      fields: [
        { name: 'text', label: '文本', value: 'name=<Ada> age=<18>' },
        { name: 'before', label: '前', value: '<' },
        { name: 'after', label: '后', value: '>' }
      ]
    },
    {
      id: 'pinyin-local',
      group: 'text',
      title: '拼音首字母',
      summary: '中国 → ZG',
      fields: [{ name: 'text', label: '中文', value: '中国' }]
    },
    {
      id: 'hkid-local',
      group: 'net',
      title: '香港身份证',
      summary: 'A123456(3)',
      fields: [{ name: 'value', label: '证件号', value: 'A123456(3)' }]
    },
    {
      id: 'twid-local',
      group: 'net',
      title: '台湾身份证',
      summary: 'A123456789',
      fields: [{ name: 'value', label: '证件号', value: 'A123456789' }]
    },
    {
      id: 'org-code-local',
      group: 'net',
      title: '组织机构代码',
      summary: 'GB 11714',
      fields: [{ name: 'code', label: '代码', value: '12345678' }]
    },
    {
      id: 'solar-term-local',
      group: 'time',
      title: '二十四节气',
      summary: '清明日期',
      fields: [
        { name: 'year', label: '年份', value: '2026' },
        { name: 'name', label: '节气', value: '清明' }
      ]
    },
    {
      id: 'ganzhi-local',
      group: 'time',
      title: '天干地支',
      summary: '1984=甲子鼠',
      fields: [{ name: 'year', label: '年份', value: '2026' }]
    },
    {
      id: 'aba-local',
      group: 'net',
      title: 'ABA 路由号',
      summary: '021000021',
      fields: [{ name: 'number', label: '路由号', value: '021000021' }]
    },
    {
      id: 'iso6346-local',
      group: 'net',
      title: '集装箱号',
      summary: 'CSQU3054383',
      fields: [{ name: 'code', label: '箱号', value: 'CSQU3054383' }]
    },
    {
      id: 'jump-hash-local',
      group: 'gen',
      title: 'Jump Hash',
      summary: 'Guava consistentHash',
      fields: [
        { name: 'key', label: 'key', value: '42' },
        { name: 'buckets', label: '分桶', value: '100' }
      ]
    },
    {
      id: 'figi-local',
      group: 'net',
      title: 'FIGI',
      summary: 'BBG000B9XRY4',
      fields: [{ name: 'value', label: 'FIGI', value: 'BBG000B9XRY4' }]
    },
    {
      id: 'lei-local',
      group: 'net',
      title: 'LEI',
      summary: 'ISO 17442',
      fields: [{ name: 'value', label: 'LEI', value: '5493001KJTIIGC8Y1R12' }]
    },
    {
      id: 'nhs-local',
      group: 'net',
      title: 'NHS 号码',
      summary: '943 476 5919',
      fields: [{ name: 'value', label: 'NHS', value: '943 476 5919' }]
    },
    {
      id: 'npi-local',
      group: 'net',
      title: 'NPI',
      summary: '1234567893',
      fields: [{ name: 'value', label: 'NPI', value: '1234567893' }]
    },
    {
      id: 'ismn-local',
      group: 'net',
      title: 'ISMN',
      summary: '979-0-2600-0043-8',
      fields: [{ name: 'value', label: 'ISMN', value: '979-0-2600-0043-8' }]
    },
    {
      id: 'nric-local',
      group: 'net',
      title: '新加坡 NRIC',
      summary: 'S1234567D',
      fields: [{ name: 'value', label: 'NRIC', value: 'S1234567D' }]
    },
    {
      id: 'cologne-local',
      group: 'text',
      title: '科隆拼音',
      summary: 'Müller → 657',
      fields: [{ name: 'text', label: '姓名', value: 'Müller' }]
    },
    {
      id: 'hamming-local',
      group: 'text',
      title: 'Hamming 距离',
      summary: 'karolin / kathrin',
      fields: [
        { name: 'left', label: '左', value: 'karolin' },
        { name: 'right', label: '右', value: 'kathrin' }
      ]
    },
    {
      id: 'uuid-v8-local',
      group: 'gen',
      title: 'UUID v8',
      summary: 'RFC 9562 custom',
      fields: []
    },
    {
      id: 'nysiis-local',
      group: 'text',
      title: 'NYSIIS',
      summary: 'Miller → MALAR',
      fields: [{ name: 'text', label: '姓名', value: 'Miller' }]
    },
    {
      id: 'caverphone-local',
      group: 'text',
      title: 'Caverphone 2',
      summary: 'Stevenson → STFNSN1111',
      fields: [{ name: 'text', label: '姓名', value: 'Stevenson' }]
    },
    {
      id: 'cpf-local',
      group: 'net',
      title: '巴西 CPF',
      summary: '111.444.777-35',
      fields: [{ name: 'value', label: 'CPF', value: '111.444.777-35' }]
    },
    {
      id: 'cnpj-local',
      group: 'net',
      title: '巴西 CNPJ',
      summary: '00.000.000/0001-91',
      fields: [{ name: 'value', label: 'CNPJ', value: '00.000.000/0001-91' }]
    },
    {
      id: 'pesel-local',
      group: 'net',
      title: '波兰 PESEL',
      summary: '44051401359',
      fields: [{ name: 'value', label: 'PESEL', value: '44051401359' }]
    },
    {
      id: 'upc-e-local',
      group: 'net',
      title: 'UPC-E',
      summary: '04252614 → 042100005264',
      fields: [{ name: 'value', label: 'UPC-E', value: '04252614' }]
    },
    {
      id: 'julian-local',
      group: 'time',
      title: '儒略日',
      summary: '2000-01-01 → 2451545',
      fields: [{ name: 'date', label: '日期', value: '2000-01-01' }]
    },
    {
      id: 'double-metaphone-local',
      group: 'text',
      title: 'Double Metaphone',
      summary: 'Smith → SM0',
      fields: [{ name: 'text', label: '姓名', value: 'Smith' }]
    },
    {
      id: 'match-rating-local',
      group: 'text',
      title: 'Match Rating',
      summary: 'Smith / Smyth',
      fields: [
        { name: 'left', label: '左', value: 'Smith' },
        { name: 'right', label: '右', value: 'Smyth' }
      ]
    },
    {
      id: 'siren-local',
      group: 'net',
      title: '法国 SIREN',
      summary: '732829320',
      fields: [{ name: 'value', label: 'SIREN', value: '732829320' }]
    },
    {
      id: 'siret-local',
      group: 'net',
      title: '法国 SIRET',
      summary: '73282932000074',
      fields: [{ name: 'value', label: 'SIRET', value: '73282932000074' }]
    },
    {
      id: 'nif-local',
      group: 'net',
      title: '西班牙 NIF',
      summary: '12345678Z',
      fields: [{ name: 'value', label: 'NIF', value: '12345678Z' }]
    },
    {
      id: 'isni-local',
      group: 'net',
      title: 'ISNI',
      summary: '0000 0001 2146 358X',
      fields: [{ name: 'value', label: 'ISNI', value: '0000 0001 2146 358X' }]
    },
    {
      id: 'bencode-local',
      group: 'codec',
      title: 'Bencode',
      summary: 'spam → 4:spam',
      fields: [{ name: 'text', label: '字符串', value: 'spam' }]
    },
    {
      id: 'refined-soundex-local',
      group: 'text',
      title: 'Refined Soundex',
      summary: 'testing → T6036084',
      fields: [{ name: 'text', label: '词', value: 'testing' }]
    },
    {
      id: 'nir-local',
      group: 'net',
      title: '法国 NIR',
      summary: '255081416812535',
      fields: [{ name: 'value', label: 'NIR', value: '255081416812535' }]
    },
    {
      id: 'codice-fiscale-local',
      group: 'net',
      title: '意大利税号',
      summary: 'RSSMRA80A01H501U',
      fields: [{ name: 'value', label: '税号', value: 'RSSMRA80A01H501U' }]
    },
    {
      id: 'steuer-id-local',
      group: 'net',
      title: '德国税号',
      summary: '86095742719',
      fields: [{ name: 'value', label: 'IdNr', value: '86095742719' }]
    },
    {
      id: 'eori-local',
      group: 'net',
      title: '欧盟 EORI',
      summary: 'FR73282932000074',
      fields: [{ name: 'value', label: 'EORI', value: 'FR73282932000074' }]
    },
    {
      id: 'doi-local',
      group: 'net',
      title: 'DOI',
      summary: '10.1000/182',
      fields: [{ name: 'value', label: 'DOI', value: '10.1000/182' }]
    },
    {
      id: 'pmid-local',
      group: 'net',
      title: 'PMID',
      summary: '12345678',
      fields: [{ name: 'value', label: 'PMID', value: '12345678' }]
    },
    {
      id: 'iccid-local',
      group: 'net',
      title: 'SIM ICCID',
      summary: '89014103211118510720',
      fields: [{ name: 'value', label: 'ICCID', value: '89014103211118510720' }]
    },
    {
      id: 'personnummer-local',
      group: 'net',
      title: '瑞典个人号',
      summary: '19811218-9876',
      fields: [{ name: 'value', label: '个人号', value: '19811218-9876' }]
    },
    {
      id: 'hetu-local',
      group: 'net',
      title: '芬兰个人号',
      summary: '131052-308T',
      fields: [{ name: 'value', label: 'HETU', value: '131052-308T' }]
    },
    {
      id: 'fodselsnummer-local',
      group: 'net',
      title: '挪威个人号',
      summary: '11077941012',
      fields: [{ name: 'value', label: 'FNR', value: '11077941012' }]
    },
    {
      id: 'iswc-local',
      group: 'net',
      title: 'ISWC',
      summary: 'T-034.524.680-8',
      fields: [{ name: 'value', label: 'ISWC', value: 'T-034.524.680-8' }]
    },
    {
      id: 'abn-local',
      group: 'net',
      title: '澳大利亚 ABN',
      summary: '51 824 753 556',
      fields: [{ name: 'value', label: 'ABN', value: '51 824 753 556' }]
    },
    {
      id: 'tfn-local',
      group: 'net',
      title: '澳大利亚 TFN',
      summary: '123456782',
      fields: [{ name: 'value', label: 'TFN', value: '123456782' }]
    },
    {
      id: 'sscc-local',
      group: 'net',
      title: 'GS1 SSCC',
      summary: '106141411234567897',
      fields: [{ name: 'value', label: 'SSCC', value: '106141411234567897' }]
    },
    {
      id: 'vat-local',
      group: 'net',
      title: '欧盟 VAT',
      summary: 'DE136695976',
      fields: [{ name: 'value', label: 'VAT', value: 'DE136695976' }]
    },
    {
      id: 'ahv-local',
      group: 'net',
      title: '瑞士 AHV',
      summary: '756.1234.5678.97',
      fields: [{ name: 'value', label: 'AHV', value: '756.1234.5678.97' }]
    },
    {
      id: 'nip-local',
      group: 'net',
      title: '波兰 NIP',
      summary: '1234563218',
      fields: [{ name: 'value', label: 'NIP', value: '1234563218' }]
    },
    {
      id: 'aadhaar-local',
      group: 'net',
      title: '印度 Aadhaar',
      summary: '234123412346',
      fields: [{ name: 'value', label: 'Aadhaar', value: '234123412346' }]
    },
    {
      id: 'pan-local',
      group: 'net',
      title: '印度 PAN',
      summary: 'ABCPE1234F',
      fields: [{ name: 'value', label: 'PAN', value: 'ABCPE1234F' }]
    },
    {
      id: 'sin-local',
      group: 'net',
      title: '加拿大 SIN',
      summary: '046454286',
      fields: [{ name: 'value', label: 'SIN', value: '046454286' }]
    },
    {
      id: 'pps-local',
      group: 'net',
      title: '爱尔兰 PPS',
      summary: '1234567T',
      fields: [{ name: 'value', label: 'PPS', value: '1234567T' }]
    },
    {
      id: 'cpr-local',
      group: 'net',
      title: '丹麦 CPR',
      summary: '010170-0003',
      fields: [{ name: 'value', label: 'CPR', value: '010170-0003' }]
    },
    {
      id: 'pt-nif-local',
      group: 'net',
      title: '葡萄牙 NIF',
      summary: '123456789',
      fields: [{ name: 'value', label: 'NIF', value: '123456789' }]
    },
    {
      id: 'nino-local',
      group: 'net',
      title: '英国 NINO',
      summary: 'AB123456C',
      fields: [{ name: 'value', label: 'NINO', value: 'AB123456C' }]
    },
    {
      id: 'rrn-local',
      group: 'net',
      title: '韩国居民登记号',
      summary: '900101-1234568',
      fields: [{ name: 'value', label: 'RRN', value: '900101-1234568' }]
    },
    {
      id: 'afm-local',
      group: 'net',
      title: '希腊 AFM',
      summary: '090000045',
      fields: [{ name: 'value', label: 'AFM', value: '090000045' }]
    },
    {
      id: 'rut-local',
      group: 'net',
      title: '智利 RUT',
      summary: '12.345.678-5',
      fields: [{ name: 'value', label: 'RUT', value: '12.345.678-5' }]
    },
    {
      id: 'cuit-local',
      group: 'net',
      title: '阿根廷 CUIT',
      summary: '20-12345678-6',
      fields: [{ name: 'value', label: 'CUIT', value: '20-12345678-6' }]
    },
    {
      id: 'said-local',
      group: 'net',
      title: '南非身份证',
      summary: '8001015009087',
      fields: [{ name: 'value', label: 'ID', value: '8001015009087' }]
    },
    {
      id: 'tckn-local',
      group: 'net',
      title: '土耳其身份证',
      summary: '10000000146',
      fields: [{ name: 'value', label: 'TCKN', value: '10000000146' }]
    },
    {
      id: 'cnp-local',
      group: 'net',
      title: '罗马尼亚 CNP',
      summary: '1800101010015',
      fields: [{ name: 'value', label: 'CNP', value: '1800101010015' }]
    },
    {
      id: 'thai-id-local',
      group: 'net',
      title: '泰国身份证',
      summary: '1234567890121',
      fields: [{ name: 'value', label: 'ID', value: '1234567890121' }]
    },
    {
      id: 'oib-local',
      group: 'net',
      title: '克罗地亚 OIB',
      summary: '12345678903',
      fields: [{ name: 'value', label: 'OIB', value: '12345678903' }]
    },
    {
      id: 'jmbg-local',
      group: 'net',
      title: '南斯拉夫 JMBG',
      summary: '0101980500005',
      fields: [{ name: 'value', label: 'JMBG', value: '0101980500005' }]
    },
    {
      id: 'kennitala-local',
      group: 'net',
      title: '冰岛 kennitala',
      summary: '120174-3399',
      fields: [{ name: 'value', label: 'kennitala', value: '120174-3399' }]
    },
    {
      id: 'taj-local',
      group: 'net',
      title: '匈牙利 TAJ',
      summary: '123456788',
      fields: [{ name: 'value', label: 'TAJ', value: '123456788' }]
    },
    {
      id: 'nit-local',
      group: 'net',
      title: '哥伦比亚 NIT',
      summary: '800197268-4',
      fields: [{ name: 'value', label: 'NIT', value: '800197268-4' }]
    },
    {
      id: 'lv-pk-local',
      group: 'net',
      title: '拉脱维亚个人号',
      summary: '111111-11111',
      fields: [{ name: 'value', label: 'personas kods', value: '111111-11111' }]
    },
    {
      id: 'emso-local',
      group: 'net',
      title: '斯洛文尼亚 EMŠO',
      summary: '0101006500006',
      fields: [{ name: 'value', label: 'EMŠO', value: '0101006500006' }]
    },
    {
      id: 'pe-dni-local',
      group: 'net',
      title: '秘鲁 DNI',
      summary: '713903006',
      fields: [{ name: 'value', label: 'DNI', value: '713903006' }]
    },
    {
      id: 'mx-rfc-local',
      group: 'net',
      title: '墨西哥 RFC',
      summary: 'GODE561231GR8',
      fields: [{ name: 'value', label: 'RFC', value: 'GODE561231GR8' }]
    },
    {
      id: 'bsn-local',
      group: 'net',
      title: '荷兰 BSN',
      summary: '111222333',
      fields: [{ name: 'value', label: 'BSN', value: '111222333' }]
    },
    {
      id: 'rodne-local',
      group: 'net',
      title: '捷克出生号',
      summary: '680101/0007',
      fields: [{ name: 'value', label: 'rodné číslo', value: '680101/0007' }]
    },
    {
      id: 'y-tunnus-local',
      group: 'net',
      title: '芬兰企业号',
      summary: '1234567-1',
      fields: [{ name: 'value', label: 'Y-tunnus', value: '1234567-1' }]
    },
    {
      id: 'cvr-local',
      group: 'net',
      title: '丹麦 CVR',
      summary: '35408002',
      fields: [{ name: 'value', label: 'CVR', value: '35408002' }]
    },
    {
      id: 'cif-local',
      group: 'net',
      title: '西班牙 CIF',
      summary: 'A58818501',
      fields: [{ name: 'value', label: 'CIF', value: 'A58818501' }]
    },
    {
      id: 'che-uid-local',
      group: 'net',
      title: '瑞士企业号',
      summary: 'CHE-109.322.551',
      fields: [{ name: 'value', label: 'CHE-UID', value: 'CHE-109.322.551' }]
    },
    {
      id: 'cui-local',
      group: 'net',
      title: '罗马尼亚 CUI',
      summary: '18547290',
      fields: [{ name: 'value', label: 'CUI', value: '18547290' }]
    },
    {
      id: 'kbo-local',
      group: 'net',
      title: '比利时企业号',
      summary: '0123456749',
      fields: [{ name: 'value', label: 'KBO', value: '0123.456.749' }]
    },
    {
      id: 'hojin-local',
      group: 'net',
      title: '日本法人番号',
      summary: '8700110005901',
      fields: [{ name: 'value', label: '法人番号', value: '8700110005901' }]
    },
    {
      id: 'kr-brn-local',
      group: 'net',
      title: '韩国事业者号',
      summary: '120-81-47521',
      fields: [{ name: 'value', label: 'BRN', value: '120-81-47521' }]
    },
    {
      id: 'tw-gui-local',
      group: 'net',
      title: '台湾统一编号',
      summary: '53212539',
      fields: [{ name: 'value', label: 'GUI', value: '53212539' }]
    },
    {
      id: 'edrpou-local',
      group: 'net',
      title: '乌克兰 EDRPOU',
      summary: '14360570',
      fields: [{ name: 'value', label: 'EDRPOU', value: '14360570' }]
    },
    {
      id: 'pib-local',
      group: 'net',
      title: '塞尔维亚 PIB',
      summary: '101134702',
      fields: [{ name: 'value', label: 'PIB', value: '101134702' }]
    },
    {
      id: 'gstin-local',
      group: 'net',
      title: '印度 GSTIN',
      summary: '27AAPFU0939F1ZV',
      fields: [{ name: 'value', label: 'GSTIN', value: '27AAPFU0939F1ZV' }]
    },
    {
      id: 'acn-local',
      group: 'net',
      title: '澳大利亚公司号',
      summary: '000000019',
      fields: [{ name: 'value', label: 'ACN', value: '000 000 019' }]
    },
    {
      id: 'vkn-local',
      group: 'net',
      title: '土耳其税号',
      summary: '4540536920',
      fields: [{ name: 'value', label: 'VKN', value: '4540536920' }]
    },
    {
      id: 'npwp-local',
      group: 'net',
      title: '印尼税号',
      summary: '013121660091000',
      fields: [{ name: 'value', label: 'NPWP', value: '01.312.166.0-091.000' }]
    },
    {
      id: 'registrikood-local',
      group: 'net',
      title: '爱沙尼亚企业号',
      summary: '12345678',
      fields: [{ name: 'value', label: 'registrikood', value: '12345678' }]
    },
    {
      id: 'nzbn-local',
      group: 'net',
      title: '新西兰企业号',
      summary: '9429000000000',
      fields: [{ name: 'value', label: 'NZBN', value: '9429000000000' }]
    },
    {
      id: 'uen-local',
      group: 'net',
      title: '新加坡 UEN',
      summary: 'T01FC6132D',
      fields: [{ name: 'value', label: 'UEN', value: 'T01FC6132D' }]
    },
    {
      id: 'il-hp-local',
      group: 'net',
      title: '以色列公司号',
      summary: '516179157',
      fields: [{ name: 'value', label: 'ח.פ.', value: '516179157' }]
    },
    {
      id: 'lt-ja-local',
      group: 'net',
      title: '立陶宛企业号',
      summary: '119511515',
      fields: [{ name: 'value', label: 'JA kodas', value: '119511515' }]
    },
    {
      id: 'inn-local',
      group: 'net',
      title: '俄罗斯税号',
      summary: '7707083893',
      fields: [{ name: 'value', label: 'ИНН', value: '7707083893' }]
    },
    {
      id: 'pe-ruc-local',
      group: 'net',
      title: '秘鲁税号',
      summary: '20512333797',
      fields: [{ name: 'value', label: 'RUC', value: '20512333797' }]
    },
    {
      id: 'nik-local',
      group: 'net',
      title: '印尼身份证',
      summary: '3171011708450001',
      fields: [{ name: 'value', label: 'NIK', value: '3171011708450001' }]
    },
    {
      id: 'vn-mst-local',
      group: 'net',
      title: '越南税号',
      summary: '0100233488',
      fields: [{ name: 'value', label: 'MST', value: '0100233488' }]
    },
    {
      id: 'ein-local',
      group: 'net',
      title: '美国雇主识别号',
      summary: '91-1144442',
      fields: [{ name: 'value', label: 'EIN', value: '91-1144442' }]
    },
    {
      id: 'ogrn-local',
      group: 'net',
      title: '俄罗斯统一注册号',
      summary: '1022200525819',
      fields: [{ name: 'value', label: 'ОГРН', value: '1022200525819' }]
    },
    {
      id: 'snils-local',
      group: 'net',
      title: '俄罗斯养老金号',
      summary: '11223344595',
      fields: [{ name: 'value', label: 'СНИЛС', value: '112-233-445 95' }]
    },
    {
      id: 'nipt-local',
      group: 'net',
      title: '阿尔巴尼亚税号',
      summary: 'J91402501L',
      fields: [{ name: 'value', label: 'NIPT', value: 'J91402501L' }]
    },
    {
      id: 'rif-local',
      group: 'net',
      title: '委内瑞拉税号',
      summary: 'V114702834',
      fields: [{ name: 'value', label: 'RIF', value: 'V-11470283-4' }]
    },
    {
      id: 'rnc-local',
      group: 'net',
      title: '多米尼加税号',
      summary: '101850043',
      fields: [{ name: 'value', label: 'RNC', value: '1-01-85004-3' }]
    },
    {
      id: 'unp-local',
      group: 'net',
      title: '白俄罗斯税号',
      summary: '200988541',
      fields: [{ name: 'value', label: 'УНП', value: '200988541' }]
    },
    {
      id: 'itin-local',
      group: 'net',
      title: '美国个人税号',
      summary: '912-90-3456',
      fields: [{ name: 'value', label: 'ITIN', value: '912-90-3456' }]
    },
    {
      id: 'cnic-local',
      group: 'net',
      title: '巴基斯坦身份证',
      summary: '34201-0891231-8',
      fields: [{ name: 'value', label: 'CNIC', value: '34201-0891231-8' }]
    },
    {
      id: 'idno-local',
      group: 'net',
      title: '摩尔多瓦企业号',
      summary: '1008600038413',
      fields: [{ name: 'value', label: 'IDNO', value: '1008600038413' }]
    },
    {
      id: 'gh-tin-local',
      group: 'net',
      title: '加纳税号',
      summary: 'C0000803561',
      fields: [{ name: 'value', label: 'TIN', value: 'C0000803561' }]
    },
    {
      id: 'ke-pin-local',
      group: 'net',
      title: '肯尼亚税号',
      summary: 'P051365947M',
      fields: [{ name: 'value', label: 'PIN', value: 'P051365947M' }]
    },
    {
      id: 'ma-ice-local',
      group: 'net',
      title: '摩洛哥企业号',
      summary: '001561191000066',
      fields: [{ name: 'value', label: 'ICE', value: '001561191000066' }]
    },
    {
      id: 'voen-local',
      group: 'net',
      title: '阿塞拜疆税号',
      summary: '1401555071',
      fields: [{ name: 'value', label: 'VÖEN', value: '140 155 5071' }]
    },
    {
      id: 'uy-rut-local',
      group: 'net',
      title: '乌拉圭税号',
      summary: '21-100342-001-7',
      fields: [{ name: 'value', label: 'RUT', value: '21-100342-001-7' }]
    },
    {
      id: 'py-ruc-local',
      group: 'net',
      title: '巴拉圭税号',
      summary: '80028061-0',
      fields: [{ name: 'value', label: 'RUC', value: '80028061-0' }]
    },
    {
      id: 'i18n-local',
      group: 'text',
      title: '本地化格式',
      summary: 'Intl.NumberFormat',
      fields: [
        { name: 'locale', label: 'Locale', value: 'zh-CN' },
        { name: 'amount', label: '金额', value: '1234.5' },
        { name: 'currency', label: '货币', value: 'CNY' }
      ]
    },
    {
      id: 'gt-nit-local',
      group: 'net',
      title: '危地马拉税号',
      summary: '576937-K',
      fields: [{ name: 'value', label: 'NIT', value: '576937-K' }]
    },
    {
      id: 'cr-cpf-local',
      group: 'net',
      title: '哥斯达黎加身份证',
      summary: '3-0455-0175',
      fields: [{ name: 'value', label: 'CPF', value: '3-0455-0175' }]
    },
    {
      id: 'cr-cpj-local',
      group: 'net',
      title: '哥斯达黎加税号',
      summary: '3-101-999999',
      fields: [{ name: 'value', label: 'CPJ', value: '3-101-999999' }]
    },
    {
      id: 'tn-mf-local',
      group: 'net',
      title: '突尼斯税号',
      summary: '1234567/M/A/E/001',
      fields: [{ name: 'value', label: 'MF', value: '1234567/M/A/E/001' }]
    },
    {
      id: 'i18n-plural-local',
      group: 'text',
      title: '复数选择',
      summary: 'Intl.PluralRules',
      fields: [
        { name: 'locale', label: 'Locale', value: 'en' },
        { name: 'count', label: '数量', value: '3' }
      ]
    },
    {
      id: 'i18n-bidi-local',
      group: 'text',
      title: '双向文本',
      summary: 'RTL / LTR',
      fields: [
        { name: 'text', label: '文本', value: 'مرحبا' },
        { name: 'locale', label: 'Locale', value: 'ar' }
      ]
    },
    {
      id: 'i18n-timezone-local',
      group: 'text',
      title: '时区显示名',
      summary: 'Intl.DateTimeFormat',
      fields: [
        { name: 'zone', label: '时区', value: 'Europe/Berlin' },
        { name: 'locale', label: 'Locale', value: 'de' }
      ]
    },
    {
      id: 'i18n-relative-local',
      group: 'text',
      title: '相对时间',
      summary: '3 minutes ago',
      fields: [
        { name: 'locale', label: 'Locale', value: 'zh-CN' },
        { name: 'seconds', label: '秒（过去）', value: '180' }
      ]
    },
    {
      id: 'i18n-case-local',
      group: 'text',
      title: '本地化大小写',
      summary: 'tr: istanbul → İSTANBUL',
      fields: [
        { name: 'locale', label: 'Locale', value: 'tr' },
        { name: 'text', label: '文本', value: 'istanbul' }
      ]
    },
    {
      id: 'i18n-digits-local',
      group: 'text',
      title: '本地数字',
      summary: '1234 → ١٢٣٤',
      fields: [
        { name: 'locale', label: 'Locale', value: 'ar-EG' },
        { name: 'text', label: '数字', value: '1234' }
      ]
    },
    {
      id: 'eg-tn-local',
      group: 'net',
      title: '埃及税号',
      summary: '100-531-385',
      fields: [{ name: 'value', label: 'TN', value: '100-531-385' }]
    },
    {
      id: 'lu-tva-local',
      group: 'net',
      title: '卢森堡税号',
      summary: '15027442',
      fields: [{ name: 'value', label: 'TVA', value: 'LU 150 274 42' }]
    },
    {
      id: 'sv-nit-local',
      group: 'net',
      title: '萨尔瓦多税号',
      summary: '0614-050707-104-8',
      fields: [{ name: 'value', label: 'NIT', value: '0614-050707-104-8' }]
    },
    {
      id: 'mk-edb-local',
      group: 'net',
      title: '北马其顿税号',
      summary: '4030000375897',
      fields: [{ name: 'value', label: 'ЕДБ', value: '4030000375897' }]
    },
    {
      id: 'me-pib-local',
      group: 'net',
      title: '黑山税号',
      summary: '02655284',
      fields: [{ name: 'value', label: 'PIB', value: '02655284' }]
    },
    {
      id: 'om-vat-local',
      group: 'net',
      title: '阿曼税号',
      summary: 'OM1100006083',
      fields: [{ name: 'value', label: 'VATIN', value: 'OM1100006083' }]
    },
    {
      id: 'cy-vat-local',
      group: 'net',
      title: '塞浦路斯税号',
      summary: 'CY-10259033P',
      fields: [{ name: 'value', label: 'VAT', value: 'CY-10259033P' }]
    },
    {
      id: 'mt-vat-local',
      group: 'net',
      title: '马耳他税号',
      summary: '11679112',
      fields: [{ name: 'value', label: 'VAT', value: 'MT 1167-9112' }]
    },
    {
      id: 'ad-nrt-local',
      group: 'net',
      title: '安道尔税号',
      summary: 'U-132950-X',
      fields: [{ name: 'value', label: 'NRT', value: 'U-132950-X' }]
    },
    {
      id: 'li-peid-local',
      group: 'net',
      title: '列支敦士登识别号',
      summary: '1234567',
      fields: [{ name: 'value', label: 'PEID', value: '00001234567' }]
    },
    {
      id: 'dz-nif-local',
      group: 'net',
      title: '阿尔及利亚税号',
      summary: '416001000000007',
      fields: [{ name: 'value', label: 'NIF', value: '416001000000007' }]
    },
    {
      id: 'sn-ninea-local',
      group: 'net',
      title: '塞内加尔企业号',
      summary: '30672212G2',
      fields: [{ name: 'value', label: 'NINEA', value: '30672212G2' }]
    },
    {
      id: 'mz-nuit-local',
      group: 'net',
      title: '莫桑比克税号',
      summary: '400339910',
      fields: [{ name: 'value', label: 'NUIT', value: '400339910' }]
    },
    {
      id: 'cu-ni-local',
      group: 'net',
      title: '古巴身份证',
      summary: '91021027775',
      fields: [{ name: 'value', label: 'NI', value: '91021027775' }]
    },
    {
      id: 'gn-nifp-local',
      group: 'net',
      title: '几内亚税号',
      summary: '693770885',
      fields: [{ name: 'value', label: 'NIFp', value: '693-770-885' }]
    },
    {
      id: 'sm-coe-local',
      group: 'net',
      title: '圣马力诺经营者号',
      summary: '24165',
      fields: [{ name: 'value', label: 'COE', value: '024165' }]
    },
    {
      id: 'fo-vn-local',
      group: 'net',
      title: '法罗企业号',
      summary: '623857',
      fields: [{ name: 'value', label: 'V-number', value: 'FO 602 590' }]
    },
    {
      id: 'fr-tva-local',
      group: 'net',
      title: '法国税号',
      summary: '40303265045',
      fields: [{ name: 'value', label: 'TVA', value: 'Fr 40 303 265 045' }]
    },
    {
      id: 'mc-tva-local',
      group: 'net',
      title: '摩纳哥税号',
      summary: '53000004605',
      fields: [{ name: 'value', label: 'TVA', value: '53 0000 04605' }]
    },
    {
      id: 'mu-nid-local',
      group: 'net',
      title: '毛里求斯身份证',
      summary: 'B150390123456A',
      fields: [{ name: 'value', label: 'NID', value: 'B150390123456A' }]
    },
    {
      id: 'ec-ci-local',
      group: 'net',
      title: '厄瓜多尔身份证',
      summary: '1714307103',
      fields: [{ name: 'value', label: 'CI', value: '171430710-3' }]
    },
    {
      id: 'ec-ruc-local',
      group: 'net',
      title: '厄瓜多尔税号',
      summary: '1792060346001',
      fields: [{ name: 'value', label: 'RUC', value: '1792060346-001' }]
    },
    {
      id: 'it-iva-local',
      group: 'net',
      title: '意大利增值税号',
      summary: '00743110157',
      fields: [{ name: 'value', label: 'P.IVA', value: 'IT 00743110157' }]
    },
    {
      id: 'ie-vat-local',
      group: 'net',
      title: '爱尔兰税号',
      summary: '6433435F',
      fields: [{ name: 'value', label: 'VAT', value: 'IE 6433435OA' }]
    },
    {
      id: 'gb-vat-local',
      group: 'net',
      title: '英国税号',
      summary: '980780684',
      fields: [{ name: 'value', label: 'VAT', value: 'GB 980 7806 84' }]
    },
    {
      id: 'ca-bn-local',
      group: 'net',
      title: '加拿大企业号',
      summary: '123026635',
      fields: [{ name: 'value', label: 'BN', value: '12302 6635 RC 0001' }]
    },
    {
      id: 'cz-dic-local',
      group: 'net',
      title: '捷克税号',
      summary: '25123891',
      fields: [{ name: 'value', label: 'DIČ', value: 'CZ 25123891' }]
    },
    {
      id: 'iso11649-local',
      group: 'net',
      title: 'ISO 11649 债权参考号',
      summary: 'RF18539007547034',
      fields: [{ name: 'value', label: 'RF', value: 'RF18 5390 0754 7034' }]
    },
    {
      id: 'json-flatten-local',
      group: 'fe',
      title: 'JSON 扁平化',
      summary: '嵌套对象 flatten / unflatten',
      fields: [
        { name: 'mode', label: '模式', type: 'select', value: 'flatten', options: [
          { value: 'flatten', label: '扁平化' },
          { value: 'unflatten', label: '还原嵌套' }
        ] },
        { name: 'text', label: 'JSON', type: 'textarea', value: '{"user":{"name":"Ada","skills":["java","vue"]}}' }
      ]
    },
    {
      id: 'json-ts-local',
      group: 'fe',
      title: 'JSON → TypeScript',
      summary: '从样例推断 interface',
      fields: [{ name: 'text', label: 'JSON', type: 'textarea', value: '{"name":"Ada","age":18,"skills":["java"],"ok":true}' }]
    },
    {
      id: 'json-pointer-local',
      group: 'fe',
      title: 'JSON Pointer',
      summary: 'RFC 6901 取值',
      fields: [
        { name: 'pointer', label: '指针', value: '/user/skills/0' },
        { name: 'text', label: 'JSON', type: 'textarea', value: '{"user":{"skills":["java","vue"]}}' }
      ]
    },
    {
      id: 'env-parse-local',
      group: 'fe',
      title: '.env 解析',
      summary: 'KEY=value / export',
      fields: [
        { name: 'mode', label: '模式', type: 'select', value: 'parse', options: [
          { value: 'parse', label: '解析' },
          { value: 'stringify', label: '对象 → .env' }
        ] },
        { name: 'text', label: '内容', type: 'textarea', value: 'APP_NAME=utils\nexport PORT=8080\n# comment\nTOKEN="ab=cd"' }
      ]
    },
    {
      id: 'nanoid-local',
      group: 'fe',
      title: 'NanoID',
      summary: 'URL 安全随机 ID',
      fields: [
        { name: 'length', label: '长度', value: '21' },
        { name: 'count', label: '数量', value: '5' }
      ]
    },
    {
      id: 'totp-local',
      group: 'fe',
      title: '本地 TOTP',
      summary: 'Web Crypto HMAC-SHA1，不上传密钥',
      fields: [
        { name: 'secret', label: 'Base32 密钥', value: 'JBSWY3DPEHPK3PXP' },
        { name: 'digits', label: '位数', value: '6' },
        { name: 'step', label: '步长秒', value: '30' }
      ]
    },
    {
      id: 'aes-gcm-local',
      group: 'fe',
      title: 'AES-GCM',
      summary: 'PBKDF2 + Web Crypto，本地加解密',
      fields: [
        { name: 'mode', label: '模式', type: 'select', value: 'encrypt', options: [
          { value: 'encrypt', label: '加密' },
          { value: 'decrypt', label: '解密' }
        ] },
        { name: 'password', label: '口令', value: 'change-me' },
        { name: 'text', label: '明文 / 密文', type: 'textarea', value: 'hello 工具集' }
      ]
    },
    {
      id: 'sha-more-local',
      group: 'fe',
      title: 'SHA-1 / 384 / 512',
      summary: 'Web Crypto 摘要',
      fields: [{ name: 'text', label: '原文', type: 'textarea', value: 'hello' }]
    },
    {
      id: 'utm-local',
      group: 'fe',
      title: 'UTM 链接',
      summary: '拼 campaign 参数',
      fields: [
        { name: 'url', label: '基础 URL', value: 'https://example.com/app' },
        { name: 'source', label: 'utm_source', value: 'newsletter' },
        { name: 'medium', label: 'utm_medium', value: 'email' },
        { name: 'campaign', label: 'utm_campaign', value: 'spring-launch' },
        { name: 'term', label: 'utm_term', value: '' },
        { name: 'content', label: 'utm_content', value: 'hero' }
      ]
    },
    {
      id: 'mime-local',
      group: 'fe',
      title: 'MIME / 扩展名',
      summary: '文件名或类型互查',
      fields: [{ name: 'text', label: '文件名或 MIME', value: 'photo.webp' }]
    },
    {
      id: 'browser-info-local',
      group: 'fe',
      title: '浏览器信息',
      summary: 'UA / 视口 / 时区 / 语言',
      fields: []
    },
    {
      id: 'zero-width-local',
      group: 'fe',
      title: '零宽字符',
      summary: '检测 / 清除不可见字符',
      fields: [
        { name: 'mode', label: '模式', type: 'select', value: 'scan', options: [
          { value: 'scan', label: '检测' },
          { value: 'strip', label: '清除' }
        ] },
        { name: 'text', label: '文本', type: 'textarea', value: 'hello\u200bworld\ufeff' }
      ]
    },
    {
      id: 'eol-local',
      group: 'fe',
      title: '换行符',
      summary: 'CRLF / LF / CR 互转',
      fields: [
        { name: 'mode', label: '目标', type: 'select', value: 'lf', options: [
          { value: 'lf', label: '→ LF' },
          { value: 'crlf', label: '→ CRLF' },
          { value: 'cr', label: '→ CR' }
        ] },
        { name: 'text', label: '文本', type: 'textarea', value: 'line1\r\nline2\nline3' }
      ]
    },
    {
      id: 'unicode-norm-local',
      group: 'fe',
      title: 'Unicode 正规化',
      summary: 'NFC / NFD / NFKC / NFKD',
      fields: [
        { name: 'form', label: '形式', type: 'select', value: 'NFC', options: [
          { value: 'NFC', label: 'NFC' },
          { value: 'NFD', label: 'NFD' },
          { value: 'NFKC', label: 'NFKC' },
          { value: 'NFKD', label: 'NFKD' }
        ] },
        { name: 'text', label: '文本', type: 'textarea', value: 'café' }
      ]
    },
    {
      id: 'markdown-toc-local',
      group: 'fe',
      title: 'Markdown 目录',
      summary: '从标题生成 TOC',
      fields: [{ name: 'text', label: 'Markdown', type: 'textarea', value: '# 概述\n## 安装\n### npm\n## 用法' }]
    },
    {
      id: 'lorem-local',
      group: 'fe',
      title: '占位文本',
      summary: '英文 / 中文假文',
      fields: [
        { name: 'lang', label: '语言', type: 'select', value: 'en', options: [
          { value: 'en', label: 'English' },
          { value: 'zh', label: '中文' }
        ] },
        { name: 'paragraphs', label: '段落', value: '2' }
      ]
    },
    {
      id: 'css-unit-local',
      group: 'fe',
      title: 'CSS 单位',
      summary: 'px ↔ rem，可设 root',
      fields: [
        { name: 'value', label: '数值', value: '16' },
        { name: 'from', label: '从', type: 'select', value: 'px', options: [
          { value: 'px', label: 'px' },
          { value: 'rem', label: 'rem' }
        ] },
        { name: 'root', label: 'root px', value: '16' }
      ]
    },
    {
      id: 'color-palette-local',
      group: 'fe',
      title: '色板',
      summary: '从 HEX 生成深浅阶',
      fields: [{ name: 'hex', label: 'HEX', value: '#0f766e' }]
    },
    {
      id: 'vcard-local',
      group: 'fe',
      title: 'vCard',
      summary: '生成 vCard 3.0',
      fields: [
        { name: 'name', label: '姓名', value: 'Ada Lovelace' },
        { name: 'org', label: '组织', value: 'Analytical Engine' },
        { name: 'tel', label: '电话', value: '+44 20 7946 0958' },
        { name: 'email', label: '邮箱', value: 'ada@example.com' },
        { name: 'url', label: '网址', value: 'https://example.com' }
      ]
    },
    {
      id: 'ics-local',
      group: 'fe',
      title: 'ICS 日程',
      summary: '生成 VEVENT',
      fields: [
        { name: 'title', label: '标题', value: 'Sprint Review' },
        { name: 'start', label: '开始', value: '2026-09-20T10:00' },
        { name: 'end', label: '结束', value: '2026-09-20T11:00' },
        { name: 'location', label: '地点', value: 'Zoom' }
      ]
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
