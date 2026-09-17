export const groups = [
  { id: 'string', label: '字符串' },
  { id: 'desensitize', label: '脱敏' },
  { id: 'datetime', label: '日期' },
  { id: 'id', label: 'ID / 单号' },
  { id: 'crypto', label: '加解密' },
  { id: 'json', label: 'JSON' },
  { id: 'validate', label: '校验' },
  { id: 'number', label: '数字金额' },
  { id: 'structure', label: '结构' },
  { id: 'web', label: 'Web / 系统' }
]

export const tools = [
  {
    id: 'mask-phone',
    group: 'string',
    title: '手机号脱敏',
    summary: 'StringUtil.maskPhone',
    method: 'GET',
    path: '/api/utils/string/mask-phone',
    fields: [{ name: 'phone', label: '手机号', value: '13812345678' }]
  },
  {
    id: 'case',
    group: 'string',
    title: '驼峰 / 下划线',
    summary: 'camelToSnake / snakeToCamel',
    method: 'GET',
    path: '/api/utils/string/case',
    fields: [{ name: 'text', label: '文本', value: 'userName' }]
  },
  {
    id: 'html-escape',
    group: 'string',
    title: 'HTML 转义',
    summary: 'HtmlUtil.escape / stripTags',
    method: 'GET',
    path: '/api/utils/html/escape',
    fields: [{ name: 'text', label: 'HTML', type: 'textarea', value: '<b>hello</b>' }]
  },
  {
    id: 'desensitize',
    group: 'desensitize',
    title: '常用脱敏',
    summary: '姓名 / 手机 / 邮箱 / 身份证 / 银行卡',
    method: 'GET',
    path: '/api/utils/desensitize',
    fields: [
      {
        name: 'type',
        label: '类型',
        type: 'select',
        value: 'name',
        options: [
          { value: 'name', label: '姓名' },
          { value: 'phone', label: '手机' },
          { value: 'email', label: '邮箱' },
          { value: 'idcard', label: '身份证' },
          { value: 'bank', label: '银行卡' },
          { value: 'address', label: '地址' },
          { value: 'ip', label: 'IP' },
          { value: 'plate', label: '车牌' },
          { value: 'password', label: '密码' }
        ]
      },
      { name: 'value', label: '原文', value: '张三丰' }
    ]
  },
  {
    id: 'datetime-now',
    group: 'datetime',
    title: '当前时间',
    summary: 'DateTimeUtil.now',
    method: 'GET',
    path: '/api/utils/datetime/now',
    fields: []
  },
  {
    id: 'uuid',
    group: 'id',
    title: 'UUID / NanoId',
    summary: 'IdUtil.uuid / uuidV7',
    method: 'GET',
    path: '/api/utils/id/uuid',
    fields: []
  },
  {
    id: 'snowflake',
    group: 'id',
    title: '雪花 ID',
    summary: '生成并解析 worker / 时间',
    method: 'GET',
    path: '/api/utils/id/snowflake',
    fields: []
  },
  {
    id: 'order-no',
    group: 'id',
    title: '业务单号',
    summary: 'OrderNoUtil.next',
    method: 'GET',
    path: '/api/utils/order-no',
    fields: []
  },
  {
    id: 'digest',
    group: 'crypto',
    title: 'MD5 / SHA-256 / SHA3',
    summary: 'EncryptUtil 摘要',
    method: 'GET',
    path: '/api/utils/encrypt/digest',
    fields: [{ name: 'text', label: '原文', value: 'hello' }]
  },
  {
    id: 'aes',
    group: 'crypto',
    title: 'AES-GCM',
    summary: '加密后再解密，确认可逆',
    method: 'POST',
    path: '/api/utils/encrypt/aes',
    fields: [
      { name: 'text', label: '明文', type: 'textarea', value: 'hello' },
      { name: 'password', label: '密码', value: 'secret-key' }
    ]
  },
  {
    id: 'jwt',
    group: 'crypto',
    title: 'JWT HS256',
    summary: '签发并解析 token',
    method: 'GET',
    path: '/api/utils/jwt',
    fields: [{ name: 'subject', label: 'subject', value: 'ada' }]
  },
  {
    id: 'json-parse',
    group: 'json',
    title: '解析 JSON',
    summary: 'JsonUtil.toMap',
    method: 'POST',
    path: '/api/utils/json/parse',
    fields: [{ name: 'json', label: 'JSON', type: 'textarea', value: '{"name":"Ada","age":18}' }]
  },
  {
    id: 'regex',
    group: 'validate',
    title: '格式校验',
    summary: '手机 / 邮箱 / 颜色 / 中文 / 日期',
    method: 'GET',
    path: '/api/utils/regex/validate',
    fields: [
      {
        name: 'type',
        label: '类型',
        type: 'select',
        value: 'mobile',
        options: [
          { value: 'mobile', label: '手机' },
          { value: 'email', label: '邮箱' },
          { value: 'idcard', label: '身份证' },
          { value: 'ipv4', label: 'IPv4' },
          { value: 'url', label: 'URL' },
          { value: 'username', label: '用户名' },
          { value: 'credit', label: '信用代码' },
          { value: 'plate', label: '车牌' },
          { value: 'ipv6', label: 'IPv6' },
          { value: 'zipcode', label: '邮编' },
          { value: 'qq', label: 'QQ' },
          { value: 'landline', label: '固话' },
          { value: 'mac', label: 'MAC' },
          { value: 'isbn', label: 'ISBN' },
          { value: 'hexcolor', label: 'HEX 颜色' },
          { value: 'date', label: '日期' },
          { value: 'time', label: '时间' },
          { value: 'chinese', label: '中文' },
          { value: 'chinesename', label: '中文姓名' },
          { value: 'domain', label: '域名' },
          { value: 'money', label: '金额' },
          { value: 'wechat', label: '微信号' },
          { value: 'bankcard', label: '银行卡号' },
          { value: 'strongpassword', label: '强密码' },
          { value: 'jwt', label: 'JWT' },
          { value: 'md5', label: 'MD5' },
          { value: 'cidr', label: 'CIDR' },
          { value: 'semver', label: 'SemVer' }
        ]
      },
      { name: 'value', label: '值', value: '13812345678' }
    ]
  },
  {
    id: 'idcard',
    group: 'validate',
    title: '身份证解析',
    summary: '校验码、生日、性别、省份',
    method: 'GET',
    path: '/api/utils/idcard/parse',
    fields: [{ name: 'idNo', label: '身份证号', value: '110101199003078937' }]
  },
  {
    id: 'credit-code',
    group: 'validate',
    title: '统一社会信用代码',
    summary: 'CreditCodeUtil.isValid',
    method: 'GET',
    path: '/api/utils/credit-code/parse',
    fields: [{ name: 'code', label: '代码', value: '91110000710930405L' }]
  },
  {
    id: 'bankcard',
    group: 'validate',
    title: '银行卡 Luhn',
    summary: 'BankCardUtil.isValid',
    method: 'GET',
    path: '/api/utils/bankcard/luhn',
    fields: [{ name: 'cardNo', label: '卡号', value: '4111111111111111' }]
  },
  {
    id: 'phone-carrier',
    group: 'validate',
    title: '手机运营商',
    summary: 'PhoneUtil.carrier',
    method: 'GET',
    path: '/api/utils/phone/carrier',
    fields: [{ name: 'mobile', label: '手机号', value: '13812345678' }]
  },
  {
    id: 'password',
    group: 'validate',
    title: '密码强度',
    summary: 'PasswordUtil.score',
    method: 'GET',
    path: '/api/utils/password/strength',
    fields: [{ name: 'password', label: '密码', value: 'Abcdef1!xyz' }]
  },
  {
    id: 'money',
    group: 'number',
    title: '金额格式化',
    summary: 'NumberUtil.formatMoney',
    method: 'GET',
    path: '/api/utils/number/money',
    fields: [{ name: 'amount', label: '金额', value: '12.3' }]
  },
  {
    id: 'chinese-number',
    group: 'number',
    title: '人民币大写',
    summary: 'ChineseNumberUtil.toRmb',
    method: 'GET',
    path: '/api/utils/chinese/number',
    fields: [{ name: 'amount', label: '金额', value: '1024.50' }]
  },
  {
    id: 'version',
    group: 'number',
    title: '版本比较',
    summary: 'VersionUtil.compare',
    method: 'GET',
    path: '/api/utils/version/compare',
    fields: [
      { name: 'left', label: '左侧', value: '1.2.10' },
      { name: 'right', label: '右侧', value: '1.2.9' }
    ]
  },
  {
    id: 'money-fen',
    group: 'number',
    title: '元 / 分互转',
    summary: 'MoneyUtil.yuanToFen',
    method: 'GET',
    path: '/api/utils/money/fen',
    fields: [{ name: 'yuan', label: '元', value: '12.3' }]
  },
  {
    id: 'byte-size',
    group: 'number',
    title: '字节大小',
    summary: 'ByteSizeUtil.format',
    method: 'GET',
    path: '/api/utils/bytesize',
    fields: [{ name: 'bytes', label: '字节', value: '1536000' }]
  },
  {
    id: 'hmac',
    group: 'crypto',
    title: 'HMAC / CRC32',
    summary: 'EncryptUtil.hmacSha256',
    method: 'GET',
    path: '/api/utils/encrypt/hmac',
    fields: [
      { name: 'text', label: '原文', value: 'hello' },
      { name: 'secret', label: '密钥', value: 'secret' }
    ]
  },
  {
    id: 'totp',
    group: 'crypto',
    title: 'TOTP 动态口令',
    summary: 'TotpUtil.generate',
    method: 'GET',
    path: '/api/utils/totp',
    fields: [{ name: 'secret', label: 'Base32 密钥', value: 'GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ' }]
  },
  {
    id: 'sign',
    group: 'crypto',
    title: '接口签名',
    summary: 'SignUtil.md5',
    method: 'GET',
    path: '/api/utils/sign/md5',
    fields: [
      { name: 'appId', label: 'appId', value: 'demo' },
      { name: 'timestamp', label: 'timestamp', value: '1710000000' },
      { name: 'secret', label: 'secret', value: 'demo-secret' }
    ]
  },
  {
    id: 'ulid',
    group: 'id',
    title: 'ULID / 短码',
    summary: 'IdUtil.ulid / ShortCodeUtil',
    method: 'GET',
    path: '/api/utils/id/ulid',
    fields: [{ name: 'id', label: '数字 ID', value: '123456' }]
  },
  {
    id: 'workday',
    group: 'datetime',
    title: '工作日 / 相对时间',
    summary: 'plusWorkdays / fromNow',
    method: 'GET',
    path: '/api/utils/workday',
    fields: [
      { name: 'date', label: '日期', value: '2026-09-07' },
      { name: 'days', label: '加工作日', value: '3' }
    ]
  },
  {
    id: 'cron',
    group: 'datetime',
    title: 'Cron 下次触发',
    summary: 'CronUtil.nextTimes',
    method: 'GET',
    path: '/api/utils/cron/next',
    fields: [{ name: 'expression', label: '表达式', value: '0 0 9 * * MON-FRI' }]
  },
  {
    id: 'template',
    group: 'string',
    title: '模板 / 全角半角',
    summary: 'StringUtil.format',
    method: 'GET',
    path: '/api/utils/template',
    fields: [
      { name: 'template', label: '模板', value: '你好，{name}' },
      { name: 'name', label: 'name', value: 'Ada' }
    ]
  },
  {
    id: 'similar',
    group: 'string',
    title: '文本相似度',
    summary: 'TextUtil.similarity',
    method: 'GET',
    path: '/api/utils/text/similar',
    fields: [
      { name: 'left', label: '文本 A', value: 'hello' },
      { name: 'right', label: '文本 B', value: 'hallo' }
    ]
  },
  {
    id: 'sensitive',
    group: 'string',
    title: '敏感词替换',
    summary: 'SensitiveWordUtil.replace',
    method: 'GET',
    path: '/api/utils/sensitive',
    fields: [{ name: 'text', label: '文本', value: '请开发票' }]
  },
  {
    id: 'yaml',
    group: 'json',
    title: 'JSON → YAML',
    summary: 'YamlUtil.jsonToYaml',
    method: 'POST',
    path: '/api/utils/yaml/from-json',
    fields: [{ name: 'json', label: 'JSON', type: 'textarea', value: '{"name":"Ada","skills":["java","vue"]}' }]
  },
  {
    id: 'geo-transform',
    group: 'structure',
    title: '坐标系转换',
    summary: 'WGS84 / GCJ-02 / BD-09',
    method: 'GET',
    path: '/api/utils/geo/transform',
    fields: [
      { name: 'lat', label: '纬度', value: '39.9087' },
      { name: 'lon', label: '经度', value: '116.3975' }
    ]
  },
  {
    id: 'cidr',
    group: 'web',
    title: 'CIDR 计算',
    summary: 'IpUtil.inCidr',
    method: 'GET',
    path: '/api/utils/ip/cidr',
    fields: [
      { name: 'ip', label: 'IP', value: '172.16.0.10' },
      { name: 'cidr', label: 'CIDR', value: '172.16.0.0/24' }
    ]
  },
  {
    id: 'url-query',
    group: 'web',
    title: 'Query 解析',
    summary: 'UrlUtil.parseQuery',
    method: 'GET',
    path: '/api/utils/url/query',
    fields: [{ name: 'url', label: 'URL', value: 'https://example.com?q=工具&x=1' }]
  },
  {
    id: 'tree',
    group: 'structure',
    title: '树结构示例',
    summary: 'TreeUtil.build',
    method: 'GET',
    path: '/api/utils/tree/sample',
    fields: []
  },
  {
    id: 'page',
    group: 'structure',
    title: '内存分页',
    summary: 'CollectionUtil.page',
    method: 'GET',
    path: '/api/utils/page/sample',
    fields: [
      { name: 'page', label: '页码', value: '1' },
      { name: 'size', label: '每页', value: '5' }
    ]
  },
  {
    id: 'geo',
    group: 'structure',
    title: '经纬度距离',
    summary: '北京 → 上海',
    method: 'GET',
    path: '/api/utils/geo/distance',
    fields: [
      { name: 'lat1', label: '起点纬度', value: '39.9' },
      { name: 'lon1', label: '起点经度', value: '116.4' },
      { name: 'lat2', label: '终点纬度', value: '31.2' },
      { name: 'lon2', label: '终点经度', value: '121.5' }
    ]
  },
  {
    id: 'ip',
    group: 'web',
    title: '客户端 IP',
    summary: 'IpUtil.getClientIp',
    method: 'GET',
    path: '/api/utils/ip',
    fields: []
  },
  {
    id: 'system',
    group: 'web',
    title: '运行环境',
    summary: 'SystemUtil / TraceId',
    method: 'GET',
    path: '/api/utils/system',
    fields: []
  },
  {
    id: 'ant-path',
    group: 'web',
    title: 'Ant 路径匹配',
    summary: 'AntPathUtil.match',
    method: 'GET',
    path: '/api/utils/ant-path',
    fields: [
      { name: 'pattern', label: 'pattern', value: '/api/**' },
      { name: 'path', label: 'path', value: '/api/utils/ip' }
    ]
  },
  {
    id: 'color-java',
    group: 'web',
    title: '颜色亮度',
    summary: 'ColorUtil.isDark',
    method: 'GET',
    path: '/api/utils/color',
    fields: [{ name: 'hex', label: 'HEX', value: '#0f766e' }]
  },
  {
    id: 'zodiac',
    group: 'datetime',
    title: '星座 / 生肖',
    summary: 'ZodiacUtil',
    method: 'GET',
    path: '/api/utils/zodiac',
    fields: [{ name: 'date', label: '日期', value: '1990-03-07' }]
  },
  {
    id: 'map-path',
    group: 'structure',
    title: 'Map 点路径',
    summary: 'MapPathUtil.get',
    method: 'GET',
    path: '/api/utils/map-path',
    fields: []
  },
  {
    id: 'file-type',
    group: 'web',
    title: '文件类型 / 文件名',
    summary: 'FileTypeUtil / FileUtil.sanitize',
    method: 'GET',
    path: '/api/utils/file-type',
    fields: [
      { name: 'filename', label: '文件名', value: '../../a.png' },
      { name: 'hex', label: 'Magic HEX', value: '89504e47' }
    ]
  },
  {
    id: 'murmur',
    group: 'crypto',
    title: 'Murmur3-32',
    summary: 'HashUtil.murmur32',
    method: 'GET',
    path: '/api/utils/hash/murmur',
    fields: [{ name: 'text', label: '原文', value: 'hello' }]
  },
  {
    id: 'escape-js',
    group: 'string',
    title: 'JS / CSV 转义',
    summary: 'EscapeUtil',
    method: 'GET',
    path: '/api/utils/escape',
    fields: [{ name: 'text', label: '文本', value: 'a"b\'c' }]
  },
  {
    id: 'highlight',
    group: 'string',
    title: '关键字高亮',
    summary: 'HighlightUtil.html',
    method: 'GET',
    path: '/api/utils/highlight',
    fields: [
      { name: 'text', label: '文本', value: 'Spring Boot 工具集' },
      { name: 'keyword', label: '关键字', value: '工具' }
    ]
  },
  {
    id: 'weight-random',
    group: 'structure',
    title: '加权随机',
    summary: 'WeightRandomUtil.pick',
    method: 'GET',
    path: '/api/utils/weight-random',
    fields: []
  },
  {
    id: 'rate-limit',
    group: 'web',
    title: '令牌桶限流',
    summary: 'RateLimiterUtil.tryAcquire',
    method: 'GET',
    path: '/api/utils/rate-limit',
    fields: [
      { name: 'key', label: 'key', value: 'demo' },
      { name: 'qps', label: 'QPS', value: '3' }
    ]
  },
  {
    id: 'duration',
    group: 'datetime',
    title: '时长解析',
    summary: 'DurationUtil.parse',
    method: 'GET',
    path: '/api/utils/duration',
    fields: [{ name: 'text', label: '时长', value: '1h30m' }]
  },
  {
    id: 'slug',
    group: 'string',
    title: 'URL slug',
    summary: 'SlugUtil.of',
    method: 'GET',
    path: '/api/utils/slug',
    fields: [{ name: 'text', label: '文本', value: 'Spring Boot 工具集' }]
  },
  {
    id: 'verify-code',
    group: 'id',
    title: '验证码',
    summary: 'VerifyCodeUtil.numeric',
    method: 'GET',
    path: '/api/utils/verify-code',
    fields: [{ name: 'length', label: '长度', value: '6' }]
  },
  {
    id: 'percent',
    group: 'number',
    title: '百分比',
    summary: 'PercentUtil.of',
    method: 'GET',
    path: '/api/utils/percent',
    fields: [
      { name: 'part', label: '部分', value: '25' },
      { name: 'total', label: '总量', value: '200' }
    ]
  },
  {
    id: 'circuit',
    group: 'web',
    title: '熔断器',
    summary: 'CircuitBreakerUtil.allow',
    method: 'GET',
    path: '/api/utils/circuit',
    fields: [{ name: 'name', label: 'name', value: 'demo' }]
  },
  {
    id: 'unicode-java',
    group: 'string',
    title: 'Unicode 转义',
    summary: 'UnicodeUtil.toUnicode',
    method: 'GET',
    path: '/api/utils/unicode',
    fields: [{ name: 'text', label: '文本', value: '工具集' }]
  },
  {
    id: 'radix',
    group: 'number',
    title: '进制转换',
    summary: 'RadixUtil.convert',
    method: 'GET',
    path: '/api/utils/radix',
    fields: [
      { name: 'value', label: '数值', value: '255' },
      { name: 'from', label: '源进制', value: '10' },
      { name: 'to', label: '目标进制', value: '16' }
    ]
  },
  {
    id: 'object-id',
    group: 'id',
    title: 'ObjectId',
    summary: 'IdUtil.objectId',
    method: 'GET',
    path: '/api/utils/object-id',
    fields: []
  },
  {
    id: 'idn',
    group: 'web',
    title: '国际化域名',
    summary: 'IdnUtil.toAscii',
    method: 'GET',
    path: '/api/utils/idn',
    fields: [{ name: 'domain', label: '域名', value: '清华大学.cn' }]
  },
  {
    id: 'expr',
    group: 'number',
    title: '四则运算',
    summary: 'ExprUtil.eval',
    method: 'GET',
    path: '/api/utils/expr',
    fields: [{ name: 'expression', label: '表达式', value: '(1+2)*3' }]
  },
  {
    id: 'lunar',
    group: 'datetime',
    title: '农历',
    summary: 'LunarUtil.of',
    method: 'GET',
    path: '/api/utils/lunar',
    fields: [{ name: 'date', label: '公历', value: '2024-02-10' }]
  },
  {
    id: 'json-path',
    group: 'json',
    title: 'JSON Pointer',
    summary: 'JsonPathUtil.getStr',
    method: 'POST',
    path: '/api/utils/json-path',
    fields: [
      { name: 'json', label: 'JSON', type: 'textarea', value: '{"user":{"name":"Ada"}}' },
      { name: 'path', label: '路径', value: 'user.name' }
    ]
  },
  {
    id: 'captcha',
    group: 'validate',
    title: '图片验证码',
    summary: 'CaptchaUtil.create',
    method: 'GET',
    path: '/api/utils/captcha',
    fields: []
  },
  {
    id: 'isbn',
    group: 'validate',
    title: 'ISBN',
    summary: 'IsbnUtil.isValid',
    method: 'GET',
    path: '/api/utils/isbn',
    fields: [{ name: 'code', label: 'ISBN', value: '9780306406157' }]
  },
  {
    id: 'mac',
    group: 'web',
    title: 'MAC 地址',
    summary: 'MacUtil.normalize',
    method: 'GET',
    path: '/api/utils/mac',
    fields: [{ name: 'value', label: 'MAC', value: '00-1A-2B-3C-4D-5E' }]
  },
  {
    id: 'idcard-convert',
    group: 'validate',
    title: '身份证 15 升 18',
    summary: 'IdCardUtil.convert15To18',
    method: 'GET',
    path: '/api/utils/idcard/convert',
    fields: [{ name: 'idNo', label: '15 位身份证', value: '110101900307893' }]
  },
  {
    id: 'base32',
    group: 'crypto',
    title: 'Base32',
    summary: 'Base32Util.encode',
    method: 'GET',
    path: '/api/utils/base32',
    fields: [{ name: 'text', label: '原文', value: 'hello' }]
  },
  {
    id: 'math',
    group: 'number',
    title: '最大公约数',
    summary: 'MathUtil.gcd / lcm',
    method: 'GET',
    path: '/api/utils/math',
    fields: [
      { name: 'a', label: 'A', value: '12' },
      { name: 'b', label: 'B', value: '18' }
    ]
  },
  {
    id: 'unit',
    group: 'number',
    title: '单位换算',
    summary: 'UnitConvertUtil.convert',
    method: 'GET',
    path: '/api/utils/unit',
    fields: [
      { name: 'value', label: '数值', value: '1' },
      { name: 'from', label: '源单位', type: 'select', value: 'km', options: [
        { value: 'km', label: 'km' },
        { value: 'm', label: 'm' },
        { value: 'cm', label: 'cm' },
        { value: 'mm', label: 'mm' },
        { value: 'ft', label: 'ft' },
        { value: 'in', label: 'in' },
        { value: 'kg', label: 'kg' },
        { value: 'g', label: 'g' },
        { value: 'lb', label: 'lb' },
        { value: 'c', label: '°C' },
        { value: 'f', label: '°F' },
        { value: 'k', label: 'K' }
      ] },
      { name: 'to', label: '目标单位', type: 'select', value: 'm', options: [
        { value: 'm', label: 'm' },
        { value: 'km', label: 'km' },
        { value: 'cm', label: 'cm' },
        { value: 'mm', label: 'mm' },
        { value: 'ft', label: 'ft' },
        { value: 'in', label: 'in' },
        { value: 'kg', label: 'kg' },
        { value: 'g', label: 'g' },
        { value: 'lb', label: 'lb' },
        { value: 'c', label: '°C' },
        { value: 'f', label: '°F' },
        { value: 'k', label: 'K' }
      ] }
    ]
  },
  {
    id: 'imei',
    group: 'validate',
    title: 'IMEI',
    summary: 'ImeiUtil.isValid',
    method: 'GET',
    path: '/api/utils/imei',
    fields: [{ name: 'value', label: 'IMEI', value: '490154203237518' }]
  },
  {
    id: 'url-parse',
    group: 'web',
    title: 'URL 解析',
    summary: 'UrlUtil.parse',
    method: 'GET',
    path: '/api/utils/url/parse',
    fields: [{ name: 'url', label: 'URL', value: 'https://example.com:8443/search?q=工具#top' }]
  },
  {
    id: 'url-build',
    group: 'web',
    title: 'URL 拼接',
    summary: 'UrlBuilder.build',
    method: 'GET',
    path: '/api/utils/url/build',
    fields: [
      { name: 'scheme', label: '协议', value: 'https' },
      { name: 'host', label: '主机', value: 'example.com' },
      { name: 'path', label: '路径', value: '/search' },
      { name: 'key', label: '参数名', value: 'q' },
      { name: 'value', label: '参数值', value: '工具' }
    ]
  },
  {
    id: 'text-diff-java',
    group: 'string',
    title: '文本行 Diff',
    summary: 'TextDiffUtil.unified',
    method: 'POST',
    path: '/api/utils/text-diff',
    fields: [
      { name: 'left', label: '原文', type: 'textarea', value: 'a\nb\nc' },
      { name: 'right', label: '新文', type: 'textarea', value: 'a\nc\nd' }
    ]
  },
  {
    id: 'base58',
    group: 'crypto',
    title: 'Base58',
    summary: 'Base58Util.encode',
    method: 'GET',
    path: '/api/utils/base58',
    fields: [{ name: 'text', label: '原文', value: 'hello' }]
  },
  {
    id: 'roman',
    group: 'number',
    title: '罗马数字',
    summary: 'RomanUtil.toRoman',
    method: 'GET',
    path: '/api/utils/roman',
    fields: [{ name: 'value', label: '数字或罗马', value: '1994' }]
  },
  {
    id: 'hashids',
    group: 'id',
    title: 'Hashids',
    summary: 'HashidsUtil.encode',
    method: 'GET',
    path: '/api/utils/hashids',
    fields: [{ name: 'id', label: '数字 ID', value: '123' }]
  },
  {
    id: 'week',
    group: 'datetime',
    title: 'ISO 周',
    summary: 'WeekUtil.isoWeek',
    method: 'GET',
    path: '/api/utils/week',
    fields: [{ name: 'date', label: '日期', value: '2024-02-10' }]
  },
  {
    id: 'seq',
    group: 'id',
    title: '日期序列号',
    summary: 'SeqUtil.next',
    method: 'GET',
    path: '/api/utils/seq',
    fields: [{ name: 'prefix', label: '前缀', value: 'ORD' }]
  },
  {
    id: 're',
    group: 'string',
    title: '正则提取',
    summary: 'ReUtil.findAll',
    method: 'GET',
    path: '/api/utils/re',
    fields: [
      { name: 'pattern', label: '正则', value: '\\d+' },
      { name: 'text', label: '文本', value: 'ab12cd34' },
      { name: 'replacement', label: '替换为', value: '*' }
    ]
  },
  {
    id: 'regex-extract',
    group: 'validate',
    title: '正则抽取',
    summary: 'ReUtil.extractMobiles / emails / urls',
    method: 'GET',
    path: '/api/utils/regex/extract',
    fields: [{
      name: 'text',
      label: '文本',
      type: 'textarea',
      value: '联系 Ada ada@example.com 电话 13812345678 打开 https://example.com 颜色 #0F766E 日期 2026-09-17'
    }]
  },
  {
    id: 'similarity',
    group: 'string',
    title: '文本相似度',
    summary: 'Jaro-Winkler / Jaccard',
    method: 'GET',
    path: '/api/utils/similarity',
    fields: [
      { name: 'left', label: '文本 A', value: 'MARTHA' },
      { name: 'right', label: '文本 B', value: 'MARHTA' }
    ]
  },
  {
    id: 'soundex',
    group: 'string',
    title: 'Soundex',
    summary: 'SoundexUtil.encode',
    method: 'GET',
    path: '/api/utils/soundex',
    fields: [
      { name: 'left', label: '姓名 A', value: 'Robert' },
      { name: 'right', label: '姓名 B', value: 'Rupert' }
    ]
  },
  {
    id: 'iban',
    group: 'validate',
    title: 'IBAN',
    summary: 'IbanUtil.isValid',
    method: 'GET',
    path: '/api/utils/iban',
    fields: [{ name: 'value', label: 'IBAN', value: 'GB82WEST12345698765432' }]
  },
  {
    id: 'vin',
    group: 'validate',
    title: 'VIN 车架号',
    summary: 'VinUtil.isValid',
    method: 'GET',
    path: '/api/utils/vin',
    fields: [{ name: 'value', label: 'VIN', value: '1M8GDM9AXKP042788' }]
  },
  {
    id: 'ean',
    group: 'validate',
    title: 'EAN / GTIN',
    summary: 'EanUtil.isValid',
    method: 'GET',
    path: '/api/utils/ean',
    fields: [{ name: 'value', label: '条码', value: '5901234123457' }]
  },
  {
    id: 'issn',
    group: 'validate',
    title: 'ISSN',
    summary: 'IssnUtil.isValid',
    method: 'GET',
    path: '/api/utils/issn',
    fields: [{ name: 'value', label: 'ISSN', value: '0317-8471' }]
  },
  {
    id: 'age',
    group: 'datetime',
    title: '年龄',
    summary: 'AgeUtil.age',
    method: 'GET',
    path: '/api/utils/age',
    fields: [{ name: 'birthday', label: '生日', value: '1990-03-07' }]
  },
  {
    id: 'sha3',
    group: 'crypto',
    title: 'SHA3-256',
    summary: 'EncryptUtil.sha3_256',
    method: 'GET',
    path: '/api/utils/digest',
    fields: [{ name: 'text', label: '原文', value: 'hello' }]
  },
  {
    id: 'ksuid',
    group: 'id',
    title: 'KSUID / TypeID',
    summary: 'KsuidUtil / TypeIdUtil',
    method: 'GET',
    path: '/api/utils/ksuid',
    fields: []
  },
  {
    id: 'host-port',
    group: 'web',
    title: 'host:port',
    summary: 'HostAndPortUtil.parse',
    method: 'GET',
    path: '/api/utils/host-port',
    fields: [{ name: 'value', label: '主机端口', value: 'example.com:8443' }]
  },
    {
      id: 'word',
      group: 'string',
      title: '单词处理',
      summary: 'WordUtil.initials',
      method: 'GET',
      path: '/api/utils/word',
      fields: [{ name: 'text', label: '文本', value: 'spring BOOT utils' }]
    },
    {
      id: 'sqids',
      group: 'id',
      title: 'Sqids',
      summary: 'SqidsUtil.encode',
      method: 'GET',
      path: '/api/utils/sqids',
      fields: [{ name: 'numbers', label: '数字', value: '1,2,3' }]
    },
    {
      id: 'uuid-name',
      group: 'id',
      title: 'UUID v3 / v5 / NanoID / CUID2',
      summary: 'IdUtil.uuidV5',
      method: 'GET',
      path: '/api/utils/uuid-name',
      fields: [{ name: 'name', label: '名称', value: 'www.example.com' }]
    },
    {
      id: 'metaphone',
      group: 'string',
      title: 'Metaphone',
      summary: 'MetaphoneUtil.encode',
      method: 'GET',
      path: '/api/utils/metaphone',
      fields: [
        { name: 'left', label: '姓名 A', value: 'Philip' },
        { name: 'right', label: '姓名 B', value: 'Phillip' }
      ]
    },
    {
      id: 'isin',
      group: 'validate',
      title: 'ISIN',
      summary: 'IsinUtil.isValid',
      method: 'GET',
      path: '/api/utils/isin',
      fields: [{ name: 'value', label: 'ISIN', value: 'US0378331005' }]
    },
    {
      id: 'bic',
      group: 'validate',
      title: 'SWIFT BIC',
      summary: 'BicUtil.isValid',
      method: 'GET',
      path: '/api/utils/bic',
      fields: [{ name: 'value', label: 'BIC', value: 'DEUTDEFF' }]
    },
    {
      id: 'case-format',
      group: 'string',
      title: 'CaseFormat',
      summary: 'CaseFormatUtil.to',
      method: 'GET',
      path: '/api/utils/case-format',
      fields: [{ name: 'text', label: '文本', value: 'springBootUtils' }]
    },
    {
      id: 'media-type',
      group: 'web',
      title: 'MediaType',
      summary: 'MediaTypeUtil.parse',
      method: 'GET',
      path: '/api/utils/media-type',
      fields: [{ name: 'value', label: '类型', value: 'application/json; charset=utf-8' }]
    },
    {
      id: 'morse',
      group: 'string',
      title: '摩斯电码',
      summary: 'MorseUtil.encode',
      method: 'GET',
      path: '/api/utils/morse',
      fields: [{ name: 'text', label: '文本', value: 'SOS' }]
    },
    {
      id: 'gzip',
      group: 'crypto',
      title: 'Gzip',
      summary: 'ZipUtil.gzipBase64',
      method: 'GET',
      path: '/api/utils/gzip',
      fields: [{ name: 'text', label: '文本', value: 'hello 工具' }]
    },
    {
      id: 'bech32',
      group: 'crypto',
      title: 'Bech32',
      summary: 'Bech32Util.encodeText',
      method: 'GET',
      path: '/api/utils/bech32',
      fields: [
        { name: 'hrp', label: 'HRP', value: 'xyz' },
        { name: 'text', label: '文本', value: 'hello' }
      ]
    },
    {
      id: 'hkdf',
      group: 'crypto',
      title: 'HKDF-SHA256',
      summary: 'HkdfUtil.deriveHex',
      method: 'GET',
      path: '/api/utils/hkdf',
      fields: [
        { name: 'ikm', label: 'IKM', value: 'hello' },
        { name: 'salt', label: 'Salt', value: 'salt' },
        { name: 'info', label: 'Info', value: 'info' },
        { name: 'length', label: '长度', value: '32' }
      ]
    },
    {
      id: 'check-digit',
      group: 'validate',
      title: '校验位',
      summary: 'Luhn / Verhoeff / Damm',
      method: 'GET',
      path: '/api/utils/check-digit',
      fields: [{ name: 'value', label: '数字', value: '79927398713' }]
    },
    {
      id: 'humanize',
      group: 'number',
      title: 'Humanize',
      summary: 'compact / ordinal',
      method: 'GET',
      path: '/api/utils/humanize',
      fields: [
        { name: 'value', label: '数字', value: '1234' },
        { name: 'ordinal', label: '序数', value: '21' }
      ]
    },
    {
      id: 'rot13',
      group: 'string',
      title: 'ROT13',
      summary: 'RotUtil.rot13',
      method: 'GET',
      path: '/api/utils/rot13',
      fields: [{ name: 'text', label: '文本', value: 'Hello' }]
    },
    {
      id: 'wildcard',
      group: 'web',
      title: '通配符',
      summary: 'WildcardUtil.match',
      method: 'GET',
      path: '/api/utils/wildcard',
      fields: [
        { name: 'text', label: '文本', value: 'Foo.java' },
        { name: 'pattern', label: '模式', value: '*.java' }
      ]
    },
    {
      id: 'email-parse',
      group: 'validate',
      title: '邮箱解析',
      summary: 'EmailUtil.parse',
      method: 'GET',
      path: '/api/utils/email-parse',
      fields: [{ name: 'value', label: '邮箱', value: 'ada+dev@example.com' }]
    },
    {
      id: 'crc32c',
      group: 'crypto',
      title: 'CRC-32C',
      summary: 'HashUtil.crc32c',
      method: 'GET',
      path: '/api/utils/hash/crc32c',
      fields: [{ name: 'text', label: '文本', value: '123456789' }]
    },
    {
      id: 'hotp',
      group: 'crypto',
      title: 'HOTP',
      summary: 'HotpUtil.generate',
      method: 'GET',
      path: '/api/utils/hotp',
      fields: [
        { name: 'key', label: '密钥', value: '12345678901234567890' },
        { name: 'counter', label: '计数', value: '0' }
      ]
    },
    {
      id: 'quoted-printable',
      group: 'string',
      title: 'Quoted-Printable',
      summary: 'QuotedPrintableUtil.encode',
      method: 'GET',
      path: '/api/utils/quoted-printable',
      fields: [{ name: 'text', label: '文本', value: 'Hello = 工具' }]
    },
    {
      id: 'base45',
      group: 'crypto',
      title: 'Base45',
      summary: 'Base45Util.encode',
      method: 'GET',
      path: '/api/utils/base45',
      fields: [{ name: 'text', label: '文本', value: 'AB' }]
    },
    {
      id: 'base85',
      group: 'crypto',
      title: 'Ascii85',
      summary: 'Base85Util.encode',
      method: 'GET',
      path: '/api/utils/base85',
      fields: [{ name: 'text', label: '文本', value: 'Man' }]
    },
    {
      id: 'xxhash',
      group: 'crypto',
      title: 'xxHash / SipHash',
      summary: 'XxHashUtil / SipHashUtil',
      method: 'GET',
      path: '/api/utils/xxhash',
      fields: [{ name: 'text', label: '文本', value: 'hello' }]
    },
    {
      id: 'holiday',
      group: 'datetime',
      title: '中国节假日',
      summary: 'HolidayUtil.name',
      method: 'GET',
      path: '/api/utils/holiday',
      fields: [{ name: 'date', label: '日期', value: '2026-10-01' }]
    },
    {
      id: 'json-patch',
      group: 'json',
      title: 'JSON Patch',
      summary: 'JsonPatchUtil.apply',
      method: 'GET',
      path: '/api/utils/json-patch',
      fields: [
        { name: 'json', label: 'JSON', type: 'textarea', value: '{"name":"Bob"}' },
        { name: 'patch', label: 'Patch', type: 'textarea', value: '[{"op":"replace","path":"/name","value":"Ada"}]' }
      ]
    },
    {
      id: 'contrast',
      group: 'web',
      title: 'WCAG 对比度',
      summary: 'ColorUtil.contrastRatio',
      method: 'GET',
      path: '/api/utils/contrast',
      fields: [
        { name: 'left', label: '颜色 A', value: '#FFFFFF' },
        { name: 'right', label: '颜色 B', value: '#000000' }
      ]
    },
    {
      id: 'ini',
      group: 'json',
      title: 'INI',
      summary: 'IniUtil.parse',
      method: 'GET',
      path: '/api/utils/ini',
      fields: [{ name: 'text', label: 'INI', type: 'textarea', value: '[database]\nhost=localhost\nport=3306' }]
    },
    {
      id: 'language-tag',
      group: 'web',
      title: 'BCP 47',
      summary: 'LanguageTagUtil.parse',
      method: 'GET',
      path: '/api/utils/language-tag',
      fields: [{ name: 'tag', label: '语言标签', value: 'zh-CN' }]
    },
    {
      id: 'uuid-v6',
      group: 'id',
      title: 'UUID v6',
      summary: 'IdUtil.uuidV6',
      method: 'GET',
      path: '/api/utils/uuid-v6',
      fields: []
    },
    {
      id: 'cusip',
      group: 'validate',
      title: 'CUSIP',
      summary: 'CusipUtil.isValid',
      method: 'GET',
      path: '/api/utils/cusip',
      fields: [{ name: 'value', label: 'CUSIP', value: '037833100' }]
    },
    {
      id: 'sedol',
      group: 'validate',
      title: 'SEDOL',
      summary: 'SedolUtil.isValid',
      method: 'GET',
      path: '/api/utils/sedol',
      fields: [{ name: 'value', label: 'SEDOL', value: '1234565' }]
    },
    {
      id: 'orcid',
      group: 'validate',
      title: 'ORCID',
      summary: 'OrcidUtil.isValid',
      method: 'GET',
      path: '/api/utils/orcid',
      fields: [{ name: 'value', label: 'ORCID', value: '0000-0002-1825-0097' }]
    },
    {
      id: 'isrc',
      group: 'validate',
      title: 'ISRC',
      summary: 'IsrcUtil.isValid',
      method: 'GET',
      path: '/api/utils/isrc',
      fields: [{ name: 'value', label: 'ISRC', value: 'US-S1Z-99-00001' }]
    },
    {
      id: 'json-merge-patch',
      group: 'json',
      title: 'JSON Merge Patch',
      summary: 'JsonMergePatchUtil.apply',
      method: 'GET',
      path: '/api/utils/json-merge-patch',
      fields: [
        { name: 'json', label: 'JSON', type: 'textarea', value: '{"a":"b"}' },
        { name: 'patch', label: 'Patch', type: 'textarea', value: '{"a":"c"}' }
      ]
    },
    {
      id: 'http-date',
      group: 'web',
      title: 'HTTP Date',
      summary: 'HttpDateUtil.format',
      method: 'GET',
      path: '/api/utils/http-date',
      fields: [{ name: 'epochMilli', label: 'Epoch 毫秒', value: '0' }]
    },
    {
      id: 'emoji',
      group: 'string',
      title: 'Emoji',
      summary: 'EmojiUtil.remove',
      method: 'GET',
      path: '/api/utils/emoji',
      fields: [{ name: 'text', label: '文本', value: 'hello 😀 工具' }]
    },
    {
      id: 'accent',
      group: 'string',
      title: '去音调',
      summary: 'AccentUtil.strip',
      method: 'GET',
      path: '/api/utils/accent',
      fields: [{ name: 'text', label: '文本', value: 'café naïve' }]
    },
    {
      id: 'plate',
      group: 'validate',
      title: '车牌号',
      summary: 'PlateUtil.isValid',
      method: 'GET',
      path: '/api/utils/plate',
      fields: [{ name: 'value', label: '车牌', value: '京A12345' }]
    },
    {
      id: 'uri-template',
      group: 'web',
      title: 'URI Template',
      summary: 'UriTemplateUtil.expand',
      method: 'GET',
      path: '/api/utils/uri-template',
      fields: [
        { name: 'template', label: '模板', value: '/users/{id}' },
        { name: 'id', label: 'id', value: '42' }
      ]
    },
    {
      id: 'totp-rfc6238',
      group: 'crypto',
      title: 'RFC 6238 TOTP',
      summary: 'TotpUtil RFC Appendix B',
      method: 'GET',
      path: '/api/utils/totp-rfc6238',
      fields: [
        { name: 'key', label: '密钥', value: '12345678901234567890' },
        { name: 'unixSeconds', label: 'Unix 秒', value: '59' }
      ]
    },
    {
      id: 'encoded-word',
      group: 'string',
      title: 'Encoded-Word',
      summary: 'EncodedWordUtil.encode',
      method: 'GET',
      path: '/api/utils/encoded-word',
      fields: [{ name: 'text', label: '文本', value: '工具' }]
    },
    {
      id: 'sm3',
      group: 'crypto',
      title: 'SM3 国密',
      summary: 'Sm3Util.hash GM/T 0004',
      method: 'GET',
      path: '/api/utils/sm3',
      fields: [{ name: 'text', label: '原文', value: 'abc' }]
    },
    {
      id: 'isbn-convert',
      group: 'validate',
      title: 'ISBN 互转',
      summary: 'IsbnUtil.toIsbn13 / toIsbn10',
      method: 'GET',
      path: '/api/utils/isbn/convert',
      fields: [{ name: 'code', label: 'ISBN', value: '0306406152' }]
    },
    {
      id: 'between',
      group: 'datetime',
      title: '时长差',
      summary: 'DateTimeUtil.formatBetween',
      method: 'GET',
      path: '/api/utils/between',
      fields: [{ name: 'seconds', label: '秒数', value: '183900' }]
    },
    {
      id: 'sub-between',
      group: 'string',
      title: '提取中间串',
      summary: 'StringUtil.subBetween',
      method: 'GET',
      path: '/api/utils/sub-between',
      fields: [
        { name: 'text', label: '文本', value: 'name=<Ada> age=<18>' },
        { name: 'before', label: '前', value: '<' },
        { name: 'after', label: '后', value: '>' }
      ]
    },
    {
      id: 'ipv6',
      group: 'web',
      title: 'IPv6 展开',
      summary: 'Ipv6Util.expand / compress',
      method: 'GET',
      path: '/api/utils/ipv6',
      fields: [{ name: 'ip', label: 'IPv6', value: '2001:db8::1' }]
    },
    {
      id: 'semver-compare',
      group: 'number',
      title: 'SemVer 比较',
      summary: 'SemverUtil.compare',
      method: 'GET',
      path: '/api/utils/semver',
      fields: [
        { name: 'left', label: '版本 A', value: '1.0.0-alpha' },
        { name: 'right', label: '版本 B', value: '1.0.0' }
      ]
    },
    {
      id: 'phone-region',
      group: 'validate',
      title: '港澳台手机号',
      summary: 'PhoneUtil.isMobileHk / Tw / Mo',
      method: 'GET',
      path: '/api/utils/phone/region',
      fields: [{ name: 'mobile', label: '号码', value: '51234567' }]
    },
    {
      id: 'bank-brand',
      group: 'validate',
      title: '银行卡组织',
      summary: 'BankCardUtil.brand',
      method: 'GET',
      path: '/api/utils/bankcard/brand',
      fields: [{ name: 'cardNo', label: '卡号', value: '6222021234567890' }]
    },
    {
      id: 'crc16',
      group: 'crypto',
      title: 'CRC-16',
      summary: 'MODBUS / CCITT-FALSE',
      method: 'GET',
      path: '/api/utils/crc16',
      fields: [{ name: 'text', label: '原文', value: '123456789' }]
    },
    {
      id: 'sm4',
      group: 'crypto',
      title: 'SM4 国密',
      summary: 'Sm4Util GM/T 0002',
      method: 'GET',
      path: '/api/utils/sm4',
      fields: [
        { name: 'text', label: '原文', value: 'hello' },
        { name: 'password', label: '密码', value: 'secret' }
      ]
    },
    {
      id: 'blake2s',
      group: 'crypto',
      title: 'BLAKE2s',
      summary: 'Blake2sUtil RFC 7693',
      method: 'GET',
      path: '/api/utils/blake2s',
      fields: [{ name: 'text', label: '原文', value: 'abc' }]
    },
    {
      id: 'cmac',
      group: 'crypto',
      title: 'AES-CMAC',
      summary: 'CmacUtil RFC 4493',
      method: 'GET',
      path: '/api/utils/cmac',
      fields: [{ name: 'text', label: '原文', value: '' }]
    },
    {
      id: 'hkid',
      group: 'validate',
      title: '香港身份证',
      summary: 'HkIdUtil.isValid',
      method: 'GET',
      path: '/api/utils/hkid',
      fields: [{ name: 'value', label: '证件号', value: 'A123456(3)' }]
    },
    {
      id: 'twid',
      group: 'validate',
      title: '台湾身份证',
      summary: 'TwIdUtil.isValid',
      method: 'GET',
      path: '/api/utils/twid',
      fields: [{ name: 'value', label: '证件号', value: 'A123456789' }]
    },
    {
      id: 'org-code',
      group: 'validate',
      title: '组织机构代码',
      summary: 'OrgCodeUtil GB 11714',
      method: 'GET',
      path: '/api/utils/org-code',
      fields: [{ name: 'code', label: '代码', value: '12345678' }]
    },
    {
      id: 'solar-term',
      group: 'datetime',
      title: '二十四节气',
      summary: 'SolarTermUtil',
      method: 'GET',
      path: '/api/utils/solar-term',
      fields: [
        { name: 'year', label: '年份', value: '2026' },
        { name: 'name', label: '节气', value: '清明' }
      ]
    },
    {
      id: 'pinyin',
      group: 'string',
      title: '拼音首字母',
      summary: 'PinyinUtil.firstLetters',
      method: 'GET',
      path: '/api/utils/pinyin',
      fields: [{ name: 'text', label: '中文', value: '中国' }]
    },
    {
      id: 'xml-pretty',
      group: 'json',
      title: 'XML 格式化',
      summary: 'XmlUtil.pretty / xpath',
      method: 'GET',
      path: '/api/utils/xml/pretty',
      fields: [
        { name: 'xml', label: 'XML', type: 'textarea', value: '<root><n>Ada</n></root>' },
        { name: 'xpath', label: 'XPath', value: '/root/n' }
      ]
    },
    {
      id: 'chacha',
      group: 'crypto',
      title: 'ChaCha20',
      summary: 'EncryptUtil.chachaEncrypt',
      method: 'GET',
      path: '/api/utils/chacha',
      fields: [
        { name: 'text', label: '原文', value: 'hello' },
        { name: 'password', label: '密码', value: 'secret' }
      ]
    },
    {
      id: 'crc64',
      group: 'crypto',
      title: 'CRC-64 / FNV-64',
      summary: 'HashUtil.crc64 / fnv1a64',
      method: 'GET',
      path: '/api/utils/crc64',
      fields: [{ name: 'text', label: '原文', value: '123456789' }]
    },
    {
      id: 'blake2b',
      group: 'crypto',
      title: 'BLAKE2b',
      summary: 'Blake2bUtil RFC 7693',
      method: 'GET',
      path: '/api/utils/blake2b',
      fields: [{ name: 'text', label: '原文', value: 'abc' }]
    },
    {
      id: 'aes-kw',
      group: 'crypto',
      title: 'AES Key Wrap',
      summary: 'AesKwUtil RFC 3394',
      method: 'GET',
      path: '/api/utils/aes-kw',
      fields: []
    },
    {
      id: 'ed25519',
      group: 'crypto',
      title: 'Ed25519',
      summary: 'Ed25519Util RFC 8032',
      method: 'GET',
      path: '/api/utils/ed25519',
      fields: [{ name: 'text', label: '原文', value: 'hello' }]
    },
    {
      id: 'tsid',
      group: 'id',
      title: 'TSID',
      summary: 'TsidUtil 时间有序 ID',
      method: 'GET',
      path: '/api/utils/tsid',
      fields: []
    },
    {
      id: 'ganzhi',
      group: 'datetime',
      title: '天干地支',
      summary: 'GanZhiUtil 生肖',
      method: 'GET',
      path: '/api/utils/ganzhi',
      fields: [{ name: 'year', label: '年份', value: '2026' }]
    },
    {
      id: 'jump-hash',
      group: 'structure',
      title: 'Jump Hash',
      summary: 'JumpHashUtil Guava',
      method: 'GET',
      path: '/api/utils/jump-hash',
      fields: [
        { name: 'key', label: 'key', value: '42' },
        { name: 'buckets', label: '分桶', value: '100' }
      ]
    },
    {
      id: 'iso6346',
      group: 'validate',
      title: '集装箱号',
      summary: 'Iso6346Util ISO 6346',
      method: 'GET',
      path: '/api/utils/iso6346',
      fields: [{ name: 'code', label: '箱号', value: 'CSQU3054383' }]
    },
    {
      id: 'bech32m',
      group: 'crypto',
      title: 'Bech32m',
      summary: 'Bech32Util.encodeM BIP-350',
      method: 'GET',
      path: '/api/utils/bech32m',
      fields: [{ name: 'text', label: '原文', value: 'hello' }]
    },
    {
      id: 'aba',
      group: 'validate',
      title: 'ABA 路由号',
      summary: 'AbaRoutingUtil',
      method: 'GET',
      path: '/api/utils/aba',
      fields: [{ name: 'number', label: '路由号', value: '021000021' }]
    },
    {
      id: 'x25519',
      group: 'crypto',
      title: 'X25519',
      summary: 'X25519Util RFC 7748',
      method: 'GET',
      path: '/api/utils/x25519',
      fields: []
    },
    {
      id: 'ripemd160',
      group: 'crypto',
      title: 'RIPEMD-160',
      summary: 'Ripemd160Util',
      method: 'GET',
      path: '/api/utils/ripemd160',
      fields: [{ name: 'text', label: '原文', value: 'abc' }]
    },
    {
      id: 'figi',
      group: 'validate',
      title: 'FIGI',
      summary: 'FigiUtil OpenFIGI',
      method: 'GET',
      path: '/api/utils/figi',
      fields: [{ name: 'value', label: 'FIGI', value: 'BBG000B9XRY4' }]
    },
    {
      id: 'lei',
      group: 'validate',
      title: 'LEI',
      summary: 'LeiUtil ISO 17442',
      method: 'GET',
      path: '/api/utils/lei',
      fields: [{ name: 'value', label: 'LEI', value: '5493001KJTIIGC8Y1R12' }]
    },
    {
      id: 'nhs',
      group: 'validate',
      title: 'NHS 号码',
      summary: 'NhsNumberUtil Mod 11',
      method: 'GET',
      path: '/api/utils/nhs',
      fields: [{ name: 'value', label: 'NHS', value: '943 476 5919' }]
    },
    {
      id: 'shake',
      group: 'crypto',
      title: 'SHAKE',
      summary: 'ShakeUtil FIPS 202',
      method: 'GET',
      path: '/api/utils/shake',
      fields: [{ name: 'text', label: '原文', value: 'abc' }]
    },
    {
      id: 'npi',
      group: 'validate',
      title: 'NPI',
      summary: 'NpiUtil CMS Luhn',
      method: 'GET',
      path: '/api/utils/npi',
      fields: [{ name: 'value', label: 'NPI', value: '1234567893' }]
    },
    {
      id: 'ismn',
      group: 'validate',
      title: 'ISMN',
      summary: 'IsmnUtil ISO 10957',
      method: 'GET',
      path: '/api/utils/ismn',
      fields: [{ name: 'value', label: 'ISMN', value: '979-0-2600-0043-8' }]
    },
    {
      id: 'nric',
      group: 'validate',
      title: '新加坡 NRIC',
      summary: 'NricUtil',
      method: 'GET',
      path: '/api/utils/nric',
      fields: [{ name: 'value', label: 'NRIC', value: 'S1234567D' }]
    },
    {
      id: 'cologne',
      group: 'string',
      title: '科隆拼音',
      summary: 'ColognePhoneticUtil',
      method: 'GET',
      path: '/api/utils/cologne',
      fields: [{ name: 'text', label: '姓名', value: 'Müller' }]
    },
    {
      id: 'http-range',
      group: 'web',
      title: 'HTTP Range',
      summary: 'HttpRangeUtil RFC 7233',
      method: 'GET',
      path: '/api/utils/http-range',
      fields: [{ name: 'header', label: 'Range', value: 'bytes=0-499' }]
    },
    {
      id: 'hamming',
      group: 'string',
      title: 'Hamming 距离',
      summary: 'TextUtil.hamming',
      method: 'GET',
      path: '/api/utils/hamming',
      fields: [
        { name: 'left', label: '左', value: 'karolin' },
        { name: 'right', label: '右', value: 'kathrin' }
      ]
    },
    {
      id: 'uuid-v8',
      group: 'id',
      title: 'UUID v8',
      summary: 'IdUtil.uuidV8 RFC 9562',
      method: 'GET',
      path: '/api/utils/uuid-v8',
      fields: []
    },
    {
      id: 'nysiis',
      group: 'string',
      title: 'NYSIIS',
      summary: 'NysiisUtil Commons Codec',
      method: 'GET',
      path: '/api/utils/nysiis',
      fields: [{ name: 'text', label: '姓名', value: 'Miller' }]
    },
    {
      id: 'caverphone',
      group: 'string',
      title: 'Caverphone 2',
      summary: 'CaverphoneUtil',
      method: 'GET',
      path: '/api/utils/caverphone',
      fields: [{ name: 'text', label: '姓名', value: 'Stevenson' }]
    },
    {
      id: 'sonyflake',
      group: 'id',
      title: 'Sonyflake',
      summary: 'SonyflakeUtil 10ms ID',
      method: 'GET',
      path: '/api/utils/sonyflake',
      fields: []
    },
    {
      id: 'content-disposition',
      group: 'web',
      title: 'Content-Disposition',
      summary: 'ContentDispositionUtil RFC 6266',
      method: 'GET',
      path: '/api/utils/content-disposition',
      fields: [{ name: 'filename', label: '文件名', value: '报表.txt' }]
    },
    {
      id: 'julian',
      group: 'datetime',
      title: '儒略日',
      summary: 'JulianDayUtil',
      method: 'GET',
      path: '/api/utils/julian',
      fields: [{ name: 'date', label: '日期', value: '2000-01-01' }]
    },
    {
      id: 'cpf',
      group: 'validate',
      title: '巴西 CPF',
      summary: 'CpfUtil',
      method: 'GET',
      path: '/api/utils/cpf',
      fields: [{ name: 'value', label: 'CPF', value: '111.444.777-35' }]
    },
    {
      id: 'cnpj',
      group: 'validate',
      title: '巴西 CNPJ',
      summary: 'CnpjUtil',
      method: 'GET',
      path: '/api/utils/cnpj',
      fields: [{ name: 'value', label: 'CNPJ', value: '00.000.000/0001-91' }]
    },
    {
      id: 'pesel',
      group: 'validate',
      title: '波兰 PESEL',
      summary: 'PeselUtil',
      method: 'GET',
      path: '/api/utils/pesel',
      fields: [{ name: 'value', label: 'PESEL', value: '44051401359' }]
    },
    {
      id: 'upc-e',
      group: 'validate',
      title: 'UPC-E',
      summary: 'UpcEUtil → UPC-A',
      method: 'GET',
      path: '/api/utils/upc-e',
      fields: [{ name: 'value', label: 'UPC-E', value: '04252614' }]
    },
    {
      id: 'crc32-mpeg2',
      group: 'crypto',
      title: 'CRC-32/MPEG-2',
      summary: 'HashUtil.crc32Mpeg2',
      method: 'GET',
      path: '/api/utils/crc32-mpeg2',
      fields: [{ name: 'text', label: '原文', value: '123456789' }]
    },
    {
      id: 'hmac-sm3',
      group: 'crypto',
      title: 'HMAC-SM3',
      summary: 'Sm3Util.hmac',
      method: 'GET',
      path: '/api/utils/hmac-sm3',
      fields: [
        { name: 'text', label: '原文', value: 'abc' },
        { name: 'key', label: '密钥', value: 'key' }
      ]
    },
    {
      id: 'murmur128',
      group: 'crypto',
      title: 'Murmur3-128',
      summary: 'HashUtil.murmur128',
      method: 'GET',
      path: '/api/utils/murmur128',
      fields: [{ name: 'text', label: '原文', value: 'abc' }]
    },
    {
      id: 'double-metaphone',
      group: 'string',
      title: 'Double Metaphone',
      summary: 'DoubleMetaphoneUtil',
      method: 'GET',
      path: '/api/utils/double-metaphone',
      fields: [{ name: 'text', label: '姓名', value: 'Smith' }]
    },
    {
      id: 'match-rating',
      group: 'string',
      title: 'Match Rating',
      summary: 'MatchRatingUtil',
      method: 'GET',
      path: '/api/utils/match-rating',
      fields: [
        { name: 'left', label: '左', value: 'Smith' },
        { name: 'right', label: '右', value: 'Smyth' }
      ]
    },
    {
      id: 'siren',
      group: 'validate',
      title: '法国 SIREN',
      summary: 'SirenUtil Luhn',
      method: 'GET',
      path: '/api/utils/siren',
      fields: [{ name: 'value', label: 'SIREN', value: '732829320' }]
    },
    {
      id: 'siret',
      group: 'validate',
      title: '法国 SIRET',
      summary: 'SiretUtil Luhn',
      method: 'GET',
      path: '/api/utils/siret',
      fields: [{ name: 'value', label: 'SIRET', value: '73282932000074' }]
    },
    {
      id: 'nif',
      group: 'validate',
      title: '西班牙 NIF',
      summary: 'NifUtil DNI / NIE',
      method: 'GET',
      path: '/api/utils/nif',
      fields: [{ name: 'value', label: 'NIF', value: '12345678Z' }]
    },
    {
      id: 'isni',
      group: 'validate',
      title: 'ISNI',
      summary: 'IsniUtil ISO 27729',
      method: 'GET',
      path: '/api/utils/isni',
      fields: [{ name: 'value', label: 'ISNI', value: '0000 0001 2146 358X' }]
    },
    {
      id: 'base91',
      group: 'crypto',
      title: 'basE91',
      summary: 'Base91Util',
      method: 'GET',
      path: '/api/utils/base91',
      fields: [{ name: 'text', label: '原文', value: 'Hello World' }]
    },
    {
      id: 'bencode',
      group: 'json',
      title: 'Bencode',
      summary: 'BencodeUtil',
      method: 'GET',
      path: '/api/utils/bencode',
      fields: [{ name: 'text', label: '字符串', value: 'spam' }]
    },
    {
      id: 'http-accept',
      group: 'web',
      title: 'HTTP Accept',
      summary: 'HttpAcceptUtil RFC 9110',
      method: 'GET',
      path: '/api/utils/http-accept',
      fields: [{ name: 'header', label: 'Accept', value: 'text/html,application/json;q=0.9,*/*;q=0.8' }]
    }
  ]

export function getTool(id) {
  return tools.find((item) => item.id === id)
}

export function groupedTools() {
  return groups
    .map((group) => ({
      ...group,
      tools: tools.filter((item) => item.group === group.id)
    }))
    .filter((group) => group.tools.length > 0)
}
