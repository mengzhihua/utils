# Java Utils Toolkit

基于 **Spring Boot 4.1 + Java 21** 的通用工具集。既可直接运行演示服务，也可把 `com.mengzhihua.utils.util` 下的工具类复用到业务项目。

## 能力一览

### Web 基础

| 模块 | 类 | 说明 |
| --- | --- | --- |
| 统一返回 | `Result` / `PageResult` / `ResultCode` | `{code, message, data, timestamp}` |
| 异常处理 | `BizException` / `GlobalExceptionHandler` | 业务异常与校验失败转统一响应 |
| 链路 | `TraceIdUtil` / `TraceIdFilter` | 自动透传 `X-Trace-Id` |
| Spring | `SpringContextHolder` / `SpelUtil` | 静态取 Bean、SpEL 表达式 |

### 数据与文本

| 模块 | 类 | 说明 |
| --- | --- | --- |
| 字符串 / 脱敏 | `StringUtil` / `DesensitizeUtil` / `HtmlUtil` / `XmlUtil` / `TextUtil` / `SensitiveWordUtil` | 驼峰/短横线、模板占位、全角半角、相似度、敏感词 |
| 对象 / 集合 | `ObjectUtil` / `ArrayUtil` / `CollectionUtil` / `MapUtil` / `BeanUtil` | 判空、分组分页、集合 diff、Bean 拷贝（含忽略 null） |
| 日期 | `DateTimeUtil` / `CronUtil` | 格式化、工作日、相对时间、Cron 下次触发 |
| JSON / CSV / YAML | `JsonUtil` / `CsvUtil` / `YamlUtil` / `CloneUtil` | Jackson 3、CSV、SnakeYAML、深拷贝 |
| 数字 | `NumberUtil` / `MoneyUtil` / `ByteSizeUtil` / `ConvertUtil` / `BooleanUtil` / `ChineseNumberUtil` / `PageUtil` / `VersionUtil` | 精确运算、元/分、字节大小、人民币大写 |
| 校验 / 断言 | `RegexUtil` / `AssertUtil` / `SqlUtil` / `IdCardUtil` / `BankCardUtil` / `CreditCodeUtil` / `PhoneUtil` / `PasswordUtil` | 身份证、Luhn、统一社会信用代码、运营商 |

### 安全、ID、文件、网络

| 模块 | 类 | 说明 |
| --- | --- | --- |
| 加解密 | `EncryptUtil` / `RsaUtil` / `JwtUtil` / `HexUtil` / `SignUtil` / `TotpUtil` | MD5/SHA/CRC32、AES-GCM、HMAC、接口签名、TOTP |
| ID / 单号 | `IdUtil` / `SnowflakeIdGenerator` / `OrderNoUtil` / `ShortCodeUtil` / `RandomUtil` | UUID、ULID、雪花、Base62 短码 |
| 文件 | `FileUtil` / `IoUtil` / `ZipUtil` / `MimeUtil` / `PathUtil` / `DownloadUtil` | 读写、压缩（防 zip-slip）、下载头 |
| 网络 / Web | `IpUtil` / `NetUtil` / `UrlUtil` / `HttpUtil` / `ServletUtil` / `CookieUtil` / `UserAgentUtil` | 客户端 IP、CIDR、Query 解析、UA |
| 树 / 地理 | `TreeUtil` / `GeoUtil` | 建树、距离、WGS84 / GCJ-02 / BD-09 |
| 并发 / 系统 | `ThreadUtil` / `RetryUtil` / `StopWatchUtil` / `LocalCacheUtil` / `SystemUtil` / `KeyedLockUtil` / `RateLimiterUtil` / `CircuitBreakerUtil` | 虚拟线程、指数退避重试、计时、本地 TTL 缓存（容量上限 + 同 key 加载锁）、按 key 串行、令牌桶、熔断 |
| 反射 | `ReflectUtil` / `EnumUtil` / `ClassUtil` / `ExceptionUtil` | 字段读写、枚举查找、堆栈 |

### 运行时补充

| 模块 | 类 | 说明 |
| --- | --- | --- |
| 路径 / 结构 | `AntPathUtil` / `MapPathUtil` / `BatchUtil` | Ant 匹配、点路径取值与扁平化、分批处理 |
| 文件 / 资源 | `FileTypeUtil` / `ImageUtil` / `ResourceUtil` / `HashUtil` | Magic 探测、图片尺寸、classpath 读取、文件摘要 / Murmur3 |
| 文本 | `EscapeUtil` / `HighlightUtil` / `SlugUtil` / `DurationUtil` | JS/CSV/JSON 转义、关键字高亮、URL slug、时长解析 |
| 其它 | `ColorUtil` / `ZodiacUtil` / `WeightRandomUtil` / `BloomFilterUtil` / `VerifyCodeUtil` / `PercentUtil` | 亮度、星座生肖、加权随机、布隆过滤器、验证码、百分比 |
| ID 增强 | `IdUtil.uuidV7` / `SnowflakeIdGenerator.parse` / `JwtUtil.decode` / `PasswordUtil.generate` / `FileUtil.sanitize` | UUID v7、雪花解析、JWT 剩余 TTL、强密码、安全文件名 |

### 对标 Hutool 常用能力

| 模块 | 类 | 说明 |
| --- | --- | --- |
| 编码 / 进制 | `UnicodeUtil` / `RadixUtil` / `Base32Util` / `CharsetUtil` / `CharUtil` | Unicode 转义、2-62 进制、Base32、字符集、字符判断 |
| 标识 / 密码 | `ObjectIdUtil` / `Pbkdf2Util` / `IdnUtil` / `BasicAuthUtil` | Mongo ObjectId、PBKDF2、Punycode、Basic Auth |
| 日期 / 计算 | `LunarUtil` / `ExprUtil` / `JsonPathUtil` | 农历（1900-2099）、四则运算、JSON Pointer |
| 校验 / 图形 | `IsbnUtil` / `MacUtil` / `CaptchaUtil` / `IdCardUtil.convert15To18` | ISBN、MAC、图片验证码、15 升 18 位身份证 |
| 正则 / 数学 | `ReUtil` / `MathUtil` / `UnitConvertUtil` / `WeekUtil` | 正则提取、公约数组合、单位换算、ISO 周 |
| 编码 / ID | `Base58Util` / `HashidsUtil` / `SeqUtil` / `RomanUtil` | Base58、混淆 ID、日期序列、罗马数字 |
| URL / 文本 | `UrlBuilder` / `UrlUtil.parse` / `TextDiffUtil` / `ImeiUtil` | URL 拼接解析、行 diff、IMEI |
| 分布 / 图形 | `ConsistentHashUtil` / `ImageUtil.scale` / `RandomUtil.randomEle` | 一致性哈希、缩放水印、随机抽样 |

### 对标 Commons / Guava / Hutool

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 文本相似 | `TextUtil.jaroWinkler` / `SimHashUtil` / `SoundexUtil` | Commons Text / Codec | Jaro-Winkler、Jaccard、SimHash、Soundex |
| 国际校验 | `IbanUtil` / `VinUtil` / `IssnUtil` / `EanUtil` | Commons Validator | IBAN、VIN、ISSN、EAN/GTIN |
| 日期 / 比较 | `AgeUtil` / `CompareUtil` / `RangeUtil` | Hutool / Guava | 年龄、空值比较、区间 |
| 编码 / ID | `Base64Util` / `KsuidUtil` / `TypeIdUtil` / `EncryptUtil.sha3_256` | Commons Codec / Segment / TypeID | URL Base64、KSUID、TypeID、SHA3 |
| 网络 / 词 | `HostAndPortUtil` / `WordUtil` | Guava / Commons Text | host:port、首字母与标题化 |

## 前端控制台（Vue 3）

启动后打开 <http://localhost:8080/> 即可在页面上自测常用工具：脱敏、身份证、JWT、AES、人民币大写、雪花 ID 等。源码在 `frontend/`，构建产物输出到 `src/main/resources/static/`。控制台包含：

- 后端 Java 工具演示（脱敏、JWT、AES、身份证、信用代码、坐标系、签名、TOTP、Cron、Ant 路径、限流、星座、slug、时长、农历、表达式、ISBN、验证码、单位换算、IMEI、Hashids、罗马数字、URL 解析、IBAN、VIN、EAN、Soundex、Jaro-Winkler、SHA3、KSUID 等）
- 浏览器本地工具：JSON/XML、Base64、JWT 解码、Markdown、命名风格、全角半角、行处理、文本对比、MD5/HMAC/CRC32/Murmur3、ULID、UUID v7、CIDR、UA/Cookie、星座、高亮、四则运算、进制、ISBN、Punycode、罗马数字、单位换算、IMEI、URL 解析、Soundex、Jaro-Winkler、IBAN、EAN、年龄等
- 特效实验室：玻璃拟态、光晕、礼花、打字机、涟漪、聚光跟随
- 深色模式、页面过渡、复制 Toast

未内置（需额外依赖，可按项目再加）：Excel、邮件、二维码、Redis、OSS。

本地改前端：

```bash
cd frontend
npm install
npm run dev      # 开发（代理到 8080）
npm run build    # 构建进 Spring Boot 静态资源
```

## 快速开始

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

启动后：

- 首页：<http://localhost:8080/>
- Swagger UI：<http://localhost:8080/swagger-ui.html>
- 健康检查：<http://localhost:8080/actuator/health>

```bash
curl "http://localhost:8080/api/utils/string/mask-phone?phone=13812345678"
curl "http://localhost:8080/api/utils/id/snowflake"
curl "http://localhost:8080/api/utils/idcard/parse?idNo=110101199003078937"
curl "http://localhost:8080/api/utils/jwt?subject=ada"
```

代码内直接调用：

```java
String phone = StringUtil.maskPhone("13812345678");
boolean ok = IdCardUtil.isValid(idNo);
String token = JwtUtil.create(Map.of("sub", userId), secret, Duration.ofHours(2));
double km = GeoUtil.distanceKm(39.9, 116.4, 31.2, 121.5);
String rmb = ChineseNumberUtil.toRmb("1024.50");
List<Dept> tree = TreeUtil.build(list, Dept::getId, Dept::getParentId, Dept::setChildren, 0L);
```

## 配置

```yaml
utils:
  snowflake:
    worker-id: 1      # 0-31
    datacenter-id: 1  # 0-31
```

## 测试

```bash
./mvnw test
```

## 技术栈

- Java 21
- Spring Boot 4.1.1
- Jackson 3 (`tools.jackson`)
- SpringDoc OpenAPI
- Maven Wrapper
