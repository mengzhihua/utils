# Java Utils Toolkit

基于 **Spring Boot 4.1 + Java 21** 的通用工具集。既可直接运行演示服务，也可把 `com.mengzhihua.utils.common` 下按领域分包的工具类复用到业务项目。

## 工程结构

按《阿里巴巴 Java 开发手册》分层：Web 与 Common 分离，工具类按领域分包，不再平铺在单一 `util` 包。本仓库是工具库，不伪造 DAO / Service 空壳；演示接口直接调用 common 静态方法，HTTP 路径仍为 `/api/utils/**`。

```
com.mengzhihua.utils
├── common                 # 通用层（可复用，无 Servlet 依赖除 net.ServletUtil）
│   ├── api                # Result / PageResult / ResultCode
│   ├── exception           # BizException
│   ├── lang               # 字符串、集合、断言、随机
│   ├── bean               # Bean、反射、类型转换
│   ├── text               # 脱敏、正则、HTML、敏感词
│   ├── codec              # Base32/45/58/64/85、Hex
│   ├── crypto             # 加解密、哈希、JWT、TOTP
│   ├── id                 # UUID、雪花、KSUID、Sqids
│   ├── time               # 日期、农历、节假日
│   ├── io                 # 文件、压缩、MIME
│   ├── net                # IP、URL、HTTP、UA
│   ├── json               # JSON / CSV / YAML / Patch
│   ├── validate           # 身份证、银行卡、ISBN 等
│   ├── math               # 金额、版本、表达式
│   ├── concurrent         # 重试、限流、本地缓存
│   ├── extra              # 验证码、树、地理、链路
│   └── spring             # SpringContextHolder / SpEL
├── web
│   ├── controller         # 演示接口 /api/utils/**
│   ├── advice             # GlobalExceptionHandler
│   └── filter             # TraceIdFilter
└── config                 # Spring / OpenAPI 装配
```

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
| 校验 / 断言 | `RegexUtil` / `ReUtil` / `AssertUtil` / `SqlUtil` / `IdCardUtil` / `BankCardUtil` / `CreditCodeUtil` / `PhoneUtil` / `PasswordUtil` | 身份证、常用格式正则、抽取手机/邮箱/URL、Luhn、信用代码 |

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
| 正则 / 数学 | `ReUtil` / `MathUtil` / `UnitConvertUtil` / `WeekUtil` | 正则提取替换命名分组、公约数组合、单位换算、ISO 周 |
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

### 对标 Sqids / Commons Codec / Guava / Hutool

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| ID | `SqidsUtil` / `IdUtil.uuidV5` / `Cuid2Util` / `IdUtil.nanoId()` | sqids.org / RFC 4122 / cuid2 / nanoid | 可逆短 ID、UUID v3/v5、CUID2、NanoID |
| 读音 / 校验 | `MetaphoneUtil` / `IsinUtil` / `BicUtil` / `CheckDigitUtil` | Commons Codec / Validator | Metaphone、ISIN、SWIFT BIC、Luhn/Verhoeff/Damm |
| 文本 / 格式 | `CaseFormatUtil` / `MediaTypeUtil` / `MorseUtil` / `RotUtil` / `HumanizeUtil` / `InflectorUtil` | Guava / Hutool / Commons Text | 命名风格、MIME、摩斯、ROT13、1.2K / 21st、英文复数 |
| 编码 / 密钥 | `Bech32Util` / `HkdfUtil` / `HashUtil.crc32c` / `ZipUtil.gzip` | BIP-173 / RFC 5869 / Guava | Bech32、HKDF-SHA256、CRC-32C、Gzip |
| 其它 | `WildcardUtil` / `EmailUtil` / `BitUtil` / `TextUtil.dice` | Commons IO / Validator / Hutool | 通配符、邮箱解析、位标记、Dice / Cosine / Damerau |

### 对标 RFC / Commons Codec / Guava

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| OTP / 编码 | `HotpUtil` / `QuotedPrintableUtil` / `Base45Util` / `Base85Util` | RFC 4226 / 2045 / 9285 / Ascii85 | HOTP、QP、Base45、Ascii85 |
| 哈希 | `XxHashUtil` / `SipHashUtil` / `HashUtil.adler32` | xxHash / Guava SipHash | xxHash32、SipHash-2-4、Adler32 |
| 日历 / 无障碍 | `HolidayUtil` / `ColorUtil.contrastRatio` / `IdUtil.uuidV6` | Hutool / WCAG / RFC 9562 | 中国法定节假日、对比度、UUID v6 |
| 数据 | `JsonPatchUtil` / `IniUtil` / `PemUtil` / `LanguageTagUtil` / `ZipUtil.zlib` | RFC 6902 / BCP 47 | JSON Patch、INI、PEM、语言标签、zlib |

### 对标 Commons Validator / RFC 6238 / 7396 / 6570

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证券 / 学术 | `CusipUtil` / `SedolUtil` / `OrcidUtil` / `IsrcUtil` | Commons Validator / ISO | CUSIP、SEDOL、ORCID、ISRC |
| JSON / HTTP | `JsonMergePatchUtil` / `HttpDateUtil` / `UriTemplateUtil` / `EncodedWordUtil` | RFC 7396 / 7231 / 6570 / 2047 | Merge Patch、HTTP Date、URI 模板、MIME encoded-word |
| 文本 / 号牌 | `EmojiUtil` / `AccentUtil` / `PlateUtil` | Hutool / Commons Lang | Emoji、去音调、车牌 |
| OTP / 哈希 | `TotpUtil` RFC 6238 / `XxHashUtil.hash64` | RFC 6238 / xxHash | Appendix B 8 位 TOTP、xxHash64 |

### 对标 Hutool / GM/T / SemVer / RFC 4291

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 国密 / 哈希 | `Sm3Util` / `HashUtil.crc16Ccitt` | Hutool SmUtil / CRC-16 | SM3（`abc` 官方向量）、MODBUS / CCITT-FALSE |
| 版本 / 网络 | `SemverUtil` / `Ipv6Util` | semver.org / RFC 5952 | 预发行比较、IPv6 展开压缩 |
| 号段 / 书号 | `PhoneUtil` 港澳台 / `IsbnUtil.toIsbn13` / `BankCardUtil.brand` | Hutool PhoneUtil / Commons Validator | HK/TW/MO、ISBN-10↔13、卡组织 |
| 文本 / 日期 | `StringUtil.subBetween` / `DateTimeUtil.formatBetween` | Hutool StrUtil / DateUtil | 提取中间串、`2天3小时5分钟` |

### 对标 GM/T / RFC 7693 / 4493 / 港澳台证件

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 国密 / 哈希 | `Sm4Util` / `Blake2sUtil` / `CmacUtil` | GM/T 0002 / RFC 7693 / 4493 | SM4 官方向量、BLAKE2s-256、AES-CMAC |
| 证件 / 机构 | `HkIdUtil` / `TwIdUtil` / `OrgCodeUtil` | Hutool IdcardUtil / GB 11714 | 香港身份证、台湾身份证、组织机构代码 |
| 历法 / 文本 | `SolarTermUtil` / `PinyinUtil` / `XmlUtil.pretty` | Hutool SolarTerms / PinyinUtil | 二十四节气、拼音首字母、XML 格式化 |
| 编码 / 字节 | `HashUtil.crc64` / `Base32Util.encodeCrockford` / `ByteUtil` / `EncryptUtil.chachaEncrypt` | CRC-64/ECMA / Crockford / RFC 8439 | CRC-8/64、FNV-1a 64、ChaCha20-Poly1305 |

### 对标 RFC 7693 / 3394 / 8032 / 7748 / ISO

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 哈希 / 密钥 | `Blake2bUtil` / `AesKwUtil` / `Ed25519Util` / `X25519Util` / `Ripemd160Util` | RFC 7693 / 3394 / 8032 / 7748 / ISO 10118-3 | BLAKE2b-512、AES-KW、Ed25519、X25519、RIPEMD-160 |
| ID / 路由 | `TsidUtil` / `JumpHashUtil` / `GanZhiUtil` | Hypersistence TSID / Guava / Hutool | 42+10+12 时间序 ID、Jump Hash、天干地支 |
| 编码 / 箱号 | `Bech32Util.encodeM` / `Iso6346Util` / `AbaRoutingUtil` | BIP-350 / ISO 6346 / ABA | Bech32m、集装箱号、美国银行路由号 |
| 金融 / 医疗 | `FigiUtil` / `LeiUtil` / `NhsNumberUtil` | OpenFIGI / ISO 17442 / NHS | FIGI、法人识别码、英国 NHS 号 |

### 对标 FIPS 202 / CMS / ISO 10957 / Commons Codec / RFC 7233

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 哈希 | `ShakeUtil` | FIPS 202 SHAKE128/256 | 空串与 `abc` 对齐 hashlib |
| 校验 | `NpiUtil` / `IsmnUtil` / `NricUtil` | CMS / ISO 10957 / ICA | 美国 NPI、ISMN-13、新加坡 NRIC |
| 读音 / HTTP | `ColognePhoneticUtil` / `HttpRangeUtil` / `TextUtil.hamming` | Commons Codec / RFC 7233 | 科隆拼音、`bytes=0-499`、Hamming |
| ID | `IdUtil.uuidV8` | RFC 9562 | UUID version 8 |

## 前端控制台（Vue 3）

启动后打开 <http://localhost:8080/> 即可在页面上自测常用工具：脱敏、身份证、JWT、AES、人民币大写、雪花 ID 等。源码在 `frontend/`，构建产物输出到 `src/main/resources/static/`。控制台包含：

- 后端 Java 工具演示（脱敏、JWT、AES、身份证、信用代码、坐标系、签名、TOTP、Cron、Ant 路径、限流、星座、slug、时长、农历、表达式、ISBN、验证码、单位换算、IMEI、Hashids、罗马数字、URL 解析、IBAN、VIN、EAN、Soundex、Jaro-Winkler、SHA3、KSUID、Sqids、UUID v5/v6、Metaphone、ISIN、BIC、Bech32、HKDF、摩斯电码、CaseFormat、HOTP、Base45、JSON Patch、节假日、WCAG 对比度、CUSIP、SEDOL、ORCID、ISRC、JSON Merge Patch、HTTP Date、车牌、URI Template、正则抽取、SM3、ISBN 互转、IPv6、SemVer、CRC-16、SM4、BLAKE2s、CMAC、香港/台湾身份证、组织机构代码、节气、拼音、XML、ChaCha20、CRC-64、BLAKE2b、AES-KW、Ed25519、TSID、干支、Jump Hash、集装箱号、Bech32m、ABA、X25519、RIPEMD-160、FIGI、LEI、NHS、SHAKE、NPI、ISMN、NRIC、科隆拼音、HTTP Range、Hamming、UUID v8 等）
- 浏览器本地工具：JSON/XML、Base64、JWT 解码、Markdown、命名风格、全角半角、行处理、文本对比、MD5/HMAC/CRC32/Murmur3、ULID、UUID v7、CIDR、UA/Cookie、星座、高亮、四则运算、进制、ISBN、Punycode、罗马数字、单位换算、IMEI、URL 解析、Soundex、Jaro-Winkler、IBAN、EAN、年龄、ROT13、摩斯、通配符、ISIN、Humanize、Quoted-Printable、Base45、WCAG 对比度、去音调、车牌、Emoji、CUSIP、正则抽取/转义、ISBN 互转、IPv6、卡组织、时长差、拼音首字母、港澳台证件、组织机构代码、节气、干支、ABA、集装箱号、Jump Hash、FIGI、LEI、NHS、NPI、ISMN、NRIC、科隆拼音、Hamming、UUID v8 等
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

代码内按领域包引用：

```java
import com.mengzhihua.utils.common.crypto.JwtUtil;
import com.mengzhihua.utils.common.extra.GeoUtil;
import com.mengzhihua.utils.common.extra.TreeUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.math.ChineseNumberUtil;
import com.mengzhihua.utils.common.validate.IdCardUtil;

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
