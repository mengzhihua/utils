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
          { value: 'ip', label: 'IP' }
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
    summary: 'IdUtil.uuid',
    method: 'GET',
    path: '/api/utils/id/uuid',
    fields: []
  },
  {
    id: 'snowflake',
    group: 'id',
    title: '雪花 ID',
    summary: 'IdUtil.snowflakeId',
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
    title: 'MD5 / SHA-256',
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
    summary: '手机 / 邮箱 / 身份证 / IPv4 / URL',
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
          { value: 'username', label: '用户名' }
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
