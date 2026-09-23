# Java Utils Toolkit

基于 **Spring Boot 4.1 + Java 21** 的通用工具集：Java 工具类、浏览器前端工具、日常办公工作台、屏幕/键鼠检测。支持服务器 JAR、Windows / Linux / macOS 安装包，以及 Chrome 新标签页扩展。分层、接口约定、控制台路由和发行流程见 [技术方案](docs/技术方案.md)。

## 快速开始

本机有 **JDK 21+** 时，下载 [v1.3.0](https://github.com/mengzhihua/utils/releases/tag/v1.3.0)（[Releases](https://github.com/mengzhihua/utils/releases) 的 Latest）里的 JAR：

```bash
java -jar utils-1.3.0.jar
```

浏览器打开 <http://localhost:8080/>。首页只有三个入口：**日常办公**、**硬件检测**、**特效实验室**。要找某个工具，在页面中间的搜索框或左侧「搜索菜单」里输入名字，例如个税、JWT、身份证。

没有 JDK 时，按芯片下载安装包（exe / `.app` / `.dmg` / deb / tar.gz），见下方「四种运行方式」。v1.1.0 的附件是打散的运行时文件，不要用那一版。

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
│   ├── i18n               # Locale、资源包、数字日期、排序、Accept-Language
│   └── spring             # SpringContextHolder / SpEL
├── web
│   ├── controller         # 演示接口 /api/utils/**
│   ├── advice             # GlobalExceptionHandler
│   └── filter             # TraceIdFilter
└── config                 # Spring / OpenAPI 装配
```

仓库根目录还有 `frontend/`（Vue 控制台）和 `extension/`（Chrome 新标签页扩展，用 `chrome.system` 展示整机配置与占用）。

## 能力一览

### Web 基础

| 模块 | 类 | 说明 |
| --- | --- | --- |
| 统一返回 | `Result` / `PageResult` / `ResultCode` | `{code, message, data, timestamp}` |
| 国际化 | `I18nUtil` / `LocaleUtil` / `I18nFormatUtil` / `CollationUtil` / `AcceptLanguageUtil` | 资源包 `i18n/messages*`（en/zh/ja/de/fr/ko）、时区/货币名/列表、异常文案 |
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

### 对标 Commons Codec / Sonyflake / RFC 6266 / GS1

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 读音 | `NysiisUtil` / `CaverphoneUtil` | Commons Codec | NYSIIS（`Miller`→`MALAR`）、Caverphone 2（`Stevenson`→`STFNSN1111`） |
| ID / HTTP | `SonyflakeUtil` / `ContentDispositionUtil` | sony/sonyflake / RFC 6266 | 10ms 时间序 ID、`filename*` |
| 历法 / 校验 | `JulianDayUtil` / `CpfUtil` / `CnpjUtil` / `PeselUtil` / `UpcEUtil` | Meeus / Receita / PESEL / GS1 | 儒略日 `2451545`、巴西税号、波兰身份证、UPC-E |
| 哈希 | `HashUtil.crc32Mpeg2` / `HashUtil.murmur128` / `Sm3Util.hmac` | CRC-32/MPEG-2 / Guava / HMAC | `123456789`→`0376e6e7`、Murmur3-128、HMAC-SM3 |

### 对标 Commons Codec / INSEE / ISO 27729 / Bencode

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 读音 | `DoubleMetaphoneUtil` / `MatchRatingUtil` | Commons Codec | `Smith`→`SM0`、`Smith`/`Smyth` 相似 |
| 企业 / 证件 | `SirenUtil` / `SiretUtil` / `NifUtil` / `IsniUtil` | INSEE / AEAT / ISO 27729 | 法国 SIREN/SIRET、西班牙 NIF/NIE、ISNI |
| 编码 / HTTP | `Base91Util` / `BencodeUtil` / `HttpAcceptUtil` | basE91 / BitTorrent / RFC 9110 | basE91 往返、`4:spam`、Accept 协商 |

### 对标 Commons Codec / Text / INSEE / ISO 7064 / RFC 7239

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 读音 / 词干 | `RefinedSoundexUtil` / `PorterStemmerUtil` | Commons Codec / Text | `testing`→`T6036084`、`relational`→`relat` |
| 证件 / 税号 | `NirUtil` / `CodiceFiscaleUtil` / `SteuerIdUtil` / `EoriUtil` | INSEE / Agenzia Entrate / BZSt / EU | 法国 NIR、意大利税号、德国税号、EORI |
| 学术 / SIM / HTTP | `DoiUtil` / `PmidUtil` / `IccidUtil` / `ForwardedUtil` / `HashUtil.fletcher16` | ISO 26324 / PubMed / ITU / RFC 7239 | DOI、PMID、ICCID、Forwarded、Fletcher-16 `1ede` |

### 对标北欧证件 / GS1 / ATO / RFC 8288 / 9110

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证件 | `PersonnummerUtil` / `HetuUtil` / `FodselsnummerUtil` | Skatteverket / VRK / Skatteetaten | 瑞典 `19811218-9876`、芬兰 `131052-308T`、挪威 `11077941012` |
| 作品 / 物流 / 税号 | `IswcUtil` / `SsccUtil` / `AbnUtil` / `TfnUtil` | ISO 15707 / GS1 / ATO | ISWC、SSCC 18 位、ABN `51824753556`、TFN `123456782` |
| HTTP / 编码 | `LinkHeaderUtil` / `EtagUtil` / `UuencodeUtil` | RFC 8288 / 9110 / Commons Codec | Link `rel=previous`、ETag 弱比较、`Cat`→`#0V%T` |

### 对标欧盟 VAT / AHV / Aadhaar / RFC 32 / 9111

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 | `VatUtil` / `AhvUtil` / `NipUtil` | EU / AHV / MF | `DE136695976`、`FR44732829320`、瑞士 `756.1234.5678.97`、波兰 NIP |
| 证件 | `AadhaarUtil` / `PanUtil` / `SinUtil` / `PpsUtil` | UIDAI / ITD / CRA / DSP | Verhoeff Aadhaar、PAN、加拿大 SIN、爱尔兰 `1234567T` |
| HTTP / 编码 | `CacheControlUtil` / `Z85Util` / `HashUtil.crc16Xmodem` | RFC 9111 / 32 / XMODEM | `max-age=3600`、`HelloWorld`→`864fd26f...`、`31c3` |

### 对标丹麦 CPR / 比利时 NRN / 葡希税号 / yEnc / Base36

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证件 | `CprUtil` / `NrnUtil` / `SvnrUtil` / `RrnUtil` | CPR / NRN / SVNR / 주민등록 | 丹麦 `010170-0003`、比利时 `93.05.18-223.61`、奥地利 `1237010180`、韩国 `900101-1234568` |
| 税号 / 保险号 | `PtNifUtil` / `AfmUtil` / `NinoUtil` / `MyNumberUtil` | AT / ΑΑΔΕ / HMRC / マイナンバー | 葡萄牙 NIF、希腊 AFM、英国 `AB123456C`、日本 `123456789019` |
| 编码 / HTTP / ID | `YencUtil` / `Base36Util` / `RetryAfterUtil` / `IdUtil.uuidV1` | yEnc / Hutool / RFC 9110 / 4122 | `Hello`→`728f969699`、`kf12oi`、`Retry-After: 120`、UUID v1 |

### 对标智利 RUT / 阿根廷 CUIT / 南非 ID / CORS

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证件 / 税号 | `RutUtil` / `CuitUtil` / `SaIdUtil` / `IrdUtil` / `MyKadUtil` | SII / AFIP / DHA / IRD / JPN | 智利 `12.345.678-5`、阿根廷 `20-12345678-6`、南非 `8001015009087`、IRD `49091850`、MyKad |
| HTTP | `CorsUtil` / `WwwAuthenticateUtil` | Fetch CORS / RFC 9110 | Allow-Origin 匹配、`Bearer realm="api"` |

### 对标土耳其 TCKN / 罗马尼亚 CNP / HSTS / CSP

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证件 | `TcKimlikUtil` / `IsraeliIdUtil` / `CnpUtil` / `OibUtil` / `EgnUtil` / `ThaiIdUtil` | NVI / MOI / CNP / OIB / GRAO | 土耳其 `10000000146`、以色列 Luhn、罗马尼亚 `1800101010015`、克罗地亚 OIB、保加利亚 EGN、泰国身份证 |
| 税号 | `RegonUtil` / `IcoUtil` | GUS / ČSÚ | 波兰 REGON `123456785`、捷克 IČO `25596641` |
| HTTP / CRC | `HstsUtil` / `CspUtil` / `AcceptEncodingUtil` / `HashUtil.crc16Kermit` | RFC 6797 / CSP / 9110 / KERMIT | `max-age=31536000`、`default-src 'self'`、`2189` / `fc891918` |

### 对标 JMBG / isikukood / kennitala / NIT / Referrer-Policy

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证件 | `JmbgUtil` / `IsikukoodUtil` / `KennitalaUtil` / `TajUtil` / `IpnUtil` | JMBG / RR / Þjóðskrá / NEAK / ДПС | 南斯拉夫 `0101980500005`、爱沙尼亚 `37601010003`、冰岛 `120174-3399`、匈牙利 TAJ、乌克兰 IPN |
| 税号 | `NitUtil` | DIAN | 哥伦比亚 NIT `800197268-4` |
| HTTP / CRC | `ReferrerPolicyUtil` / `XFrameOptionsUtil` / `PermissionsPolicyUtil` / `HashUtil.crc16Arc` | Referrer-Policy / XFO / Permissions-Policy / ARC | `strict-origin-when-cross-origin`、`SAMEORIGIN`、`geolocation=()`、`bb3d` |

### 对标 personas kods / asmens kodas / EMŠO / RFC / Age

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证件 | `LatvianPkUtil` / `LithuanianAkUtil` / `EmsoUtil` / `PeDniUtil` | PMLP / Registrų / CRP / RENIEC | 拉脱维亚 `111111-11111`、立陶宛 `33309240064`、斯洛文尼亚 `0101006500006`、秘鲁 DNI `713903006` |
| 税号 | `MxRfcUtil` | SAT | 墨西哥 RFC `GODE561231GR8` |
| HTTP / CRC | `HttpAgeUtil` / `WarningUtil` / `HashUtil.crc16Maxim` | RFC 9111 / 7234 / MAXIM | `Age: 3600`、`110 - "Response is Stale"`、`44c2` |

### 对标 BSN / rodné číslo / Base92 / Structured Fields

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证件 | `BsnUtil` / `RodneCisloUtil` | BRP / MVČR | 荷兰 `111222333`、捷克 `680101/0007` |
| 编码 / HTTP | `Base92Util` / `StructuredFieldUtil` | Base92 / RFC 8941 | `Hello` → `Q2Aeq)`、`abc=123, def=?0` |

### 对标 Y-tunnus / orgnr / CVR / CIF

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 企业号 | `YTunnusUtil` / `OrgnrUtil` / `CvrUtil` / `SeOrgNrUtil` | PRH / Brønnøysund / CVR / Bolagsverket | 芬兰 `1234567-1`、挪威 `123456785`、丹麦 `35408002`、瑞典 `556036-0793` |
| 税号 / CRC | `CifUtil` / `HashUtil.crc32Posix` | AEAT / POSIX | 西班牙 `A58818501`、`123456789` → `765e7680` |

### 对标 CHE-UID / EIK / CUI / KBO

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 企业号 | `CheUidUtil` / `EikUtil` / `CuiUtil` / `AdoszamUtil` / `KboUtil` | UID / BULSTAT / ANAF / NAV / KBO | 瑞士 `CHE-109.322.551`、保加利亚 `831641791`、罗马尼亚 `18547290`、匈牙利 `18154111-2-41`、比利时 `0123.456.749` |
| HTTP / CRC | `ClearSiteDataUtil` / `HashUtil.crc16Usb` | Clear-Site-Data / USB | `"cache","cookies"`、`123456789` → `b4c8` |

### 对标 法人番号 / BRN / GUI / EDRPOU / PIB

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 企业号 | `HojinUtil` / `KrBrnUtil` / `TwGuiUtil` / `EdrpouUtil` / `PibUtil` | NTA / NTS / 财政 / Ukrstat / PU | 日本 `8700110005901`、韩国 `120-81-47521`、台湾 `53212539`、乌克兰 `14360570`、塞尔维亚 `101134702` |
| HTTP / CRC | `NelUtil` / `HashUtil.crc8Smbus` | NEL / SMBUS | `report_to=nel`、`123456789` → `f4` |

### 对标 GSTIN / ACN / VKN / NPWP

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 企业号 | `GstinUtil` / `AcnUtil` / `VknUtil` / `NpwpUtil` / `RegistrikoodUtil` | GSTN / ASIC / GİB / DJP / RIK | 印度 `27AAPFU0939F1ZV`、澳大利亚 `000 000 019`、土耳其 `4540536920`、印尼 `01.312.166.0-091.000`、爱沙尼亚 `12345678` |
| HTTP / CRC | `ReportToUtil` / `HashUtil.crc16Genibus` | Report-To / GENIBUS | `group=nel`、`123456789` → `d64e` |

### 对标 NZBN / UEN / ח.פ. / JA

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 企业号 | `NzbnUtil` / `UenUtil` / `IlHpUtil` / `LtJaUtil` | MBIE / ACRA / רשם / JAR | 新西兰 `9429000000000`、新加坡 `T01FC6132D`、以色列 `516179157`、立陶宛 `119511515` |
| HTTP / CRC | `CoopUtil` / `CoepUtil` / `HashUtil.crc32Jamcrc` | COOP / COEP / JAMCRC | `same-origin`、`require-corp`、`123456789` → `340bc6d9` |

### 对标 ИНН / RUC / NIK / MST

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 证件 | `InnUtil` / `PeRucUtil` / `NikUtil` / `VnMstUtil` | ФНС / SUNAT / Dukcapil / Tổng cục Thuế | 俄罗斯 `7707083893`、秘鲁 `20512333797`、印尼 `3171011708450001`、越南 `0100233488` |
| HTTP / CRC | `CorpUtil` / `XctoUtil` / `HashUtil.crc16Dnp` | CORP / XCTO / DNP | `same-origin`、`nosniff`、`123456789` → `ea82` |

### 对标 EIN / OGRN / SNILS / NIPT

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 注册号 | `EinUtil` / `OgrnUtil` / `SnilsUtil` / `NiptUtil` | IRS / ФНС / ПФР / TATIME | 美国 `91-1144442`、俄罗斯 `1022200525819` / `112-233-445 95`、阿尔巴尼亚 `J91402501L` |
| HTTP / CRC | `TimingAllowUtil` / `OacUtil` / `HashUtil.crc16Cms` | TAO / OAC / CMS | `*`、`?1`、`123456789` → `aee7` |

### 对标 RIF / RNC / UNP / ITIN

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 | `RifUtil` / `RncUtil` / `UnpUtil` / `ItinUtil` | SENIAT / DGII / МНС / IRS | 委内瑞拉 `V-11470283-4`、多米尼加 `1-01-85004-3`、白俄罗斯 `200988541`、美国 `912-90-3456` |
| HTTP / CRC | `ReportingEndpointsUtil` / `AcceptChUtil` / `HashUtil.crc16Cdma2000` | Reporting-Endpoints / Accept-CH / CDMA2000 | `csp=`、`DPR`、`123456789` → `4c06` |

### 对标 CNIC / IDNO / GH TIN / KE PIN

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 证件 / 税号 | `CnicUtil` / `IdnoUtil` / `GhTinUtil` / `KePinUtil` | NADRA / IDNO.md / GRA / KRA | 巴基斯坦 `34201-0891231-8`、摩尔多瓦 `1008600038413`、加纳 `C0000803561`、肯尼亚 `P051365947M` |
| HTTP / CRC | `DocumentPolicyUtil` / `HashUtil.crc32Autosar` | Document-Policy / AUTOSAR | `unsized-media=?0`、`123456789` → `1697d06a` |

### 对标 ICE / VÖEN / UY RUT / PY RUC

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 企业号 | `MaIceUtil` / `VoenUtil` / `UyRutUtil` / `PyRucUtil` | ICE.gov.ma / e-taxes / DGI / SET | 摩洛哥 `001561191000066`、阿塞拜疆 `1401555071`、乌拉圭 `21-100342-001-7`、巴拉圭 `80028061-0` |
| HTTP / CRC | `XRobotsTagUtil` / `HashUtil.crc16Gsm` | X-Robots-Tag / GSM | `noindex`、`123456789` → `ce3c` |

### 国际化

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 资源包 | `I18nUtil` | Hutool `ResourceBundleUtil` / Spring `MessageSource` | `hello` + `zh-CN` → `你好，{0}`，默认英文 |
| Locale | `LocaleUtil` / `AcceptLanguageUtil` | BCP 47 / RFC 9110 | `zh_CN` → `zh-CN`，`zh-CN,en;q=0.8` 协商 |
| 格式 / 排序 | `I18nFormatUtil` / `CollationUtil` / `TimezoneUtil` / `PluralUtil` / `BidiUtil` / `CalendarLocaleUtil` | JDK `CompactNumberFormat` / `Bidi` / `WeekFields` | 紧凑数字、复数选择、RTL、周起始日 |
| 相对时间 / 大小写 | `RelativeTimeUtil` / `LocaleCaseUtil` | MessageFormat / `Locale` case | `3 minutes ago`、土耳其 `istanbul` → `İSTANBUL` |
| 解析 / 分词 | `I18nParseUtil` / `BreakIteratorUtil` | `NumberFormat` / `BreakIterator` | `1.234,5` → `1234.5`、`Hello, world` |
| 数字 / 历法 | `NativeDigitUtil` / `ChronologyUtil` / `OrdinalUtil` | `DecimalFormatSymbols` / 和历 | `ar-EG` 本地数字、`令和`、`21st` |
| 异常 | `GlobalExceptionHandler` | Spring `MessageSource` | 缺参 / 404 / 500 跟 `Accept-Language` |
| 控制台 | Vue `useI18n` | 无额外前端依赖 | 侧栏中 / 英 / 日 / 德，请求带 `Accept-Language` |

### 对标危地马拉 NIT / 哥斯达黎加 CPF·CPJ / 突尼斯 MF

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 证件 | `GtNitUtil` / `CrCpfUtil` / `CrCpjUtil` / `TnMfUtil` | SAT / Hacienda / Matricule Fiscal | 危地马拉 `576937-K`、哥斯达黎加 `3-0455-0175` / `3-101-999999`、突尼斯 `1234567/M/A/E/001` |
| HTTP / CRC | `ServerTimingUtil` / `HashUtil.crc8SaeJ1850` | RFC 8594 / SAE J1850 | `db;dur=53`、`123456789` → `4b` |

### 对标埃及税号 / 卢森堡 TVA / 萨尔瓦多 NIT / 北马其顿 ЕДБ

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 | `EgTnUtil` / `LuTvaUtil` / `SvNitUtil` / `MkEdbUtil` | ETA / n° TVA / MH / УЈП | 埃及 `100-531-385`、卢森堡 `15027442`、萨尔瓦多 `0614-050707-104-8`、北马其顿 `4030000375897` |
| HTTP / CRC | `ContentLanguageUtil` / `HashUtil.crc8Darc` | RFC 9110 / DARC | `zh-CN, en`、`123456789` → `15` |

### 对标黑山 PIB / 阿曼 VAT / 塞浦路斯 VAT / 马耳他 VAT

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 | `MePibUtil` / `OmVatUtil` / `CyVatUtil` / `MtVatUtil` | CRPS / Tax Oman / ΑΦΠ / CFR | 黑山 `02655284`、阿曼 `OM1100006083`、塞浦路斯 `CY-10259033P`、马耳他 `11679112` |
| HTTP / CRC | `VaryUtil` / `HashUtil.crc8Icode` | RFC 9110 / I-CODE | `Accept-Encoding, User-Agent`、`123456789` → `7e` |

### 对标安道尔 NRT / 列支敦士登 PEID / 阿尔及利亚 NIF / 塞内加尔 NINEA

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 | `AdNrtUtil` / `LiPeidUtil` / `DzNifUtil` / `SnNineaUtil` | NRT / PEID / DGI / ANSD | 安道尔 `U-132950-X`、列支敦士登 `1234567`、阿尔及利亚 `416001000000007`、塞内加尔 `30672212G2` |
| HTTP / CRC | `OriginUtil` / `HashUtil.crc8Rohc` | RFC 6454 / ROHC | `https://example.com:8443`、`123456789` → `d0` |

### 对标莫桑比克 NUIT / 古巴 NI / 几内亚 NIFp / 圣马力诺 COE

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 证件 | `MzNuitUtil` / `CuNiUtil` / `GnNifpUtil` / `SmCoeUtil` | AT / Carnet / DGI / COE | 莫桑比克 `400339910`、古巴 `91021027775`、几内亚 `693-770-885`、圣马力诺 `24165` |
| HTTP / CRC | `AltSvcUtil` / `HashUtil.crc16Mcrf4xx` | RFC 7838 / MCRF4XX | `h3=":443"`、`123456789` → `6f91` |

### 对标法罗 V 号 / 法国 TVA / 摩纳哥 TVA / 毛里求斯身份证

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 证件 | `FoVnUtil` / `FrTvaUtil` / `McTvaUtil` / `MuNidUtil` | TAKS / n° TVA / n° TVA / NID | 法罗 `623857`、法国 `40 303 265 045`、摩纳哥 `53 0000 04605`、毛里求斯 `B150390123456A` |
| HTTP / CRC | `DnsPrefetchUtil` / `HashUtil.crc8Wcdma` | X-DNS-Prefetch-Control / WCDMA | `on` / `off`、`123456789` → `25` |

### 对标厄瓜多尔 CI / 厄瓜多尔 RUC / 意大利 IVA / 爱尔兰 VAT

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 证件 | `EcCiUtil` / `EcRucUtil` / `ItIvaUtil` / `IeVatUtil` | CI / RUC / Partita IVA / VAT | 厄瓜多尔 `171430710-3`、`1792060346-001`、意大利 `IT 00743110157`、爱尔兰 `IE 6433435F` |
| HTTP / CRC | `XssProtectionUtil` / `HashUtil.crc8Maxim` | X-XSS-Protection / MAXIM-DOW | `1; mode=block`、`123456789` → `a1` |

### 对标英国 VAT / 加拿大 BN / 捷克 DIČ / ISO 11649

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 / 参考号 | `GbVatUtil` / `CaBnUtil` / `CzDicUtil` / `Iso11649Util` | VAT / BN / DIČ / RF | 英国 `GB 980 7806 84`、加拿大 `12302 6635 RC 0001`、捷克 `CZ 25123891`、`RF18 5390 0754 7034` |
| HTTP / CRC | `ExpectCtUtil` / `HashUtil.crc16X25` | Expect-CT / X-25 | `max-age=86400, enforce`、`123456789` → `906e` |

### 对标奥地利 UID / 斯洛伐克 DPH / 斯洛文尼亚 DDV / 荷兰 BTW

| 模块 | 类 | 对标 | 说明 |
| --- | --- | --- | --- |
| 税号 | `AtUidUtil` / `SkDphUtil` / `SiDdvUtil` / `NlBtwUtil` | UID / IČ DPH / DDV / BTW | 奥地利 `AT U13585627`、斯洛伐克 `SK 202 274 96 19`、斯洛文尼亚 `SI 5022 3054`、荷兰 `NL004495445B01` |
| HTTP / CRC | `PriorityUtil` / `HashUtil.crc16DectR` | RFC 9218 / DECT-R | `u=1, i`、`123456789` → `007e` |

### 前端本地工具（浏览器 Web API）

| 分组 | 工具 | 说明 |
| --- | --- | --- |
| JSON / 配置 | 扁平化、TypeScript 推断、JSON Pointer、`.env`、Diff / Merge / 键排序 | 不走后端，样例可在 `/#/c/json-diff-local` 自测 |
| 密码学 | NanoID、TOTP、AES-GCM、SHA-1/384/512、密码强度 | Web Crypto + PBKDF2，密钥不上传 |
| 页面 / 资源 | UTM、MIME、浏览器信息、色板、CSS px/rem、Data URL、URL 正规化、Query / Cookie / mailto | `navigator` / `Intl` / `URL` |
| 文本 / 日历 | 零宽字符、换行符、Unicode 正规化、Markdown TOC、占位文、vCard、ICS、去 HTML、模板、CSV→MD、OG、文件名、阅读时长、头像缩写 | 纯本地生成与清洗 |

## 前端控制台（Vue 3）

启动后打开 <http://localhost:8080/>。源码在 `frontend/`，构建产物输出到 `src/main/resources/static/`。具体 Java 类见上面的能力一览；控制台里的同名工具用搜索打开。后端演示是 `/#/t/:id`，浏览器本地工具是 `/#/c/:id`。

### 首页

默认三张入口卡片，下面是应用健康、Java 版本、服务器时间，以及三个快捷按钮（生成 UUID、雪花 ID、业务单号）：

| 卡片 | 地址 | 里面有什么 |
| --- | --- | --- |
| 日常办公 | `/#/office` | 个税、房贷、发票、待办等，计算留在浏览器 |
| 硬件检测 | `/#/hw` | 配置、占用、屏幕、键鼠、摄像头、麦克风 |
| 特效实验室 | `/#/fx` | 玻璃拟态、光晕、礼花、打字机、涟漪、聚光跟随 |

页面中间的搜索框覆盖办公、硬件、浏览器本地工具和 Java 工具。有匹配就紧挨搜索框列出；没有匹配时显示「没有匹配的工具」。

### 左侧菜单

一级是分组，默认收起。当前页所在的分组会自动展开，并滚到正在看的那一项。

| 一级 | 二级 |
| --- | --- |
| 概览 | 首页 |
| 日常办公 | 办公工作台，以及每个办公工具 |
| 硬件检测 | 检测台，以及每个检测页 |
| 特效实验室 | 直接进入 `/#/fx` |
| 前端工具 | 编解码、文本、哈希、生成、前端实用、时间 / 颜色、网络 |
| Java 工具 | 与上面能力一览相同的领域分组（字符串、脱敏、日期、证件等） |

「搜索菜单」最多列出 12 条，每条带分组名，其余显示「还有 N 项」。

工具页右上角可以返回：办公工具回到办公工作台，硬件单项回到检测台，其它回到概览。

侧栏底部可切换 **中文 / English / 日本語 / Deutsch**（请求会带 `Accept-Language`），以及深色模式。复制结果时有 Toast。

未内置（需额外依赖，可按项目再加）：Excel、邮件、二维码、Redis、OSS。

本地改前端：

```bash
cd frontend
npm install
npm run dev      # 开发（代理到 8080）
npm run build    # 构建进 Spring Boot 静态资源
```

### 日常办公（浏览器直接用）

打开 <http://localhost:8080/#/office>。个税、房贷、发票、社保、加班、报销、工作日、会议纪要、周报、待办、便签、番茄钟、快递单号、对公转账、Excel 列号、图片压缩/水印、抽签均在浏览器本地计算，不上传。

| 模块 | 类 / 入口 | 说明 |
| --- | --- | --- |
| 个税 / 房贷 / 发票 | `PitUtil` / `MortgageUtil` / `InvoiceVatUtil` | 也可走 `/api/utils/pit` `/mortgage` `/invoice-vat` |
| 办公工作台 | `/#/office` | 搜索 + 分类卡片，适合每天开着当桌面工具 |
| 硬件检测台 | `/#/hw` | 电脑配置 / 资源占用 / 屏幕 / 键盘 / 鼠标 / 触控 / 连点 / 手柄 / 扬声器 / 摄像头 / 麦克风，全在浏览器本机完成 |

### 屏幕与键鼠检测（浏览器直接用）

打开 <http://localhost:8080/#/hw>。这些能力必须是可交互页面（不能只靠表单 + JSON）：

| 入口 | 说明 |
| --- | --- |
| `/#/hw/screen` | 分辨率、DPR、色域、HDR、色条 / 灰阶 / 棋盘、全屏坏点（Esc 退出）、实时帧率 |
| `/#/hw/keyboard` | 按 `event.code` 点亮 QWERTY，记录 key/code/location/repeat/修饰键 |
| `/#/hw/mouse` | 轨迹、左右中/前进后退、滚轮、双击；区域内禁用右键菜单 |
| `/#/hw/pointer` | 多点触控、手写笔压感与倾斜 |
| `/#/hw/click` | 5 秒连点 CPS，最好成绩存在 `localStorage` |
| `/#/hw/gamepad` | `navigator.getGamepads()` 按键与摇杆，可选震动 |
| `/#/hw/audio` | Web Audio 左右声道与扫频 |
| `/#/hw/system` | Chrome UA-CH、`deviceMemory`、逻辑核数、WebGL/WebGPU 渲染器（网页看不到整机任务管理器；整机占用用下面的 Chrome 扩展） |
| `/#/hw/resources` | `performance.memory` JS 堆、`PressureObserver` CPU 压力、Storage 配额、电池、网络 |
| `/#/hw/camera` | `getUserMedia` 预览、切换设备、镜像、抓拍（不上传） |
| `/#/hw/mic` | 麦克风电平 / 波形 / 试听 / 本机录 4 秒回放 |
| `/#/c/screen-info-local` | 屏幕快照 JSON（分辨率 / 色域 / 触控点数） |
| `/#/c/media-devices-local` | 摄像头 / 麦克风 / 扬声器设备列表快照 |
| `/#/c/system-info-local` | 电脑配置 JSON 快照 |
| `/#/c/resources-info-local` | 资源占用 JSON 快照 |

### Chrome 扩展（新标签页展示整机配置 / 占用）

普通网页读不到任务管理器里的整机 CPU / 内存。仓库里的 `extension/` 是 Manifest V3 扩展，用 `chrome.system.cpu` / `memory` / `storage` / `display` 读本机，并覆盖 Chrome **新标签页（首页）**。

1. 打开 `chrome://extensions`
2. 打开右上角「开发者模式」
3. 点「加载已解压的扩展程序」，选中本仓库的 `extension/` 目录
4. 新建标签页：顶部是时钟和 Google 搜索（也可直接输入网址回车），下面是 CPU / 内存 / 电池 / 系统卡片，以及每核占用、磁盘、显示器、GPU
5. 点工具栏图标可打开精简弹层（CPU + 内存）

没有加载扩展时，同一套页面会降级为网页接口（核数、`deviceMemory`、视口、GPU），并提示去 `chrome://extensions` 加载。文案支持中 / 英 / 日 / 德。

## 发布版（推荐）

需要本机已安装 **JDK 21+**。从 [GitHub Releases](https://github.com/mengzhihua/utils/releases) 下载 **Latest**（当前 [v1.3.0](https://github.com/mengzhihua/utils/releases/tag/v1.3.0)）后直接运行。v1.1.0 的附件是打散的运行时文件，不要用那一版。

```bash
java -jar utils-1.3.0.jar
```

浏览器打开 <http://localhost:8080/> 即可使用控制台（后端演示 + 浏览器本地工具 + 日常办公 + 屏幕/键鼠检测）。可用端口：

```bash
java -jar utils-1.3.0.jar --server.port=9090
```

`main` 上的提交在 **CI 测试通过后会自动发 GitHub Release**（补丁号 +1；若 `pom.xml` 版本更新，则以 pom 为准）。也可打 `v*` 标签或在 Actions 里手动选版本。提交说明里写 `[skip release]` 可跳过本次发行。

本地打可执行包（产物在 `dist/`）：

```bash
chmod +x mvnw scripts/package.sh
./scripts/package.sh            # 使用 pom 版本
./scripts/package.sh 1.3.0      # 指定发行版本号
java -jar dist/utils-1.3.0.jar
```

可选 Docker（先执行 `./scripts/package.sh`）：

```bash
docker build -t mengzhihua/utils:1.3.0 .
docker run --rm -p 8080:8080 mengzhihua/utils:1.3.0
```

## 四种运行方式

### 1. 服务器部署 Spring Boot JAR

```bash
./scripts/package.sh
java -jar dist/utils-1.3.0.jar
```

浏览器打开 <http://localhost:8080/>。

### 2. 本机桌面模式（Win / Linux / macOS 同一 JAR）

```bash
./scripts/start.sh      # Linux / macOS，自动打开办公工作台
start.bat               # Windows
```

监听 `127.0.0.1:18765`，并打开 `/#/office`。需要本机 **JDK 21+**。

### 3. Windows / Linux / macOS 原生包（自带运行时）

GitHub Release 按**芯片**各打一份，打开 [Releases](https://github.com/mengzhihua/utils/releases) 下载对应文件：

| 平台 | 芯片 | 发行文件 | 怎么用 |
| --- | --- | --- | --- |
| Windows | x64 | `utils-x.y.z-windows-x64.exe` | 双击安装（当前用户，一般不用管理员），开始菜单打开 **Utils** |
| macOS | Apple Silicon（M1/M2/M3/M4，arm64） | `utils-x.y.z-macos-arm64.app.zip` 或同名 `.dmg` | zip 解压得到 `Utils.app`；dmg 打开后把 `Utils.app` 拖进应用程序 |
| macOS | Intel（x64） | `utils-x.y.z-macos-x64.app.zip` 或同名 `.dmg` | 同上 |
| Linux | x64 | `utils-x.y.z-linux-x64.deb` | 双击或 `sudo dpkg -i`，应用菜单打开 Utils |
| Linux | x64 | `utils-x.y.z-linux-x64.tar.gz` | 解压后在目录里执行 `./Utils` |

Apple Silicon 用 **macos-arm64**，Intel 用 **macos-x64**。macOS 同一芯片有 `.app.zip` 和 `.dmg` 两份。第一次打开若被拦截，在访达里右键「打开」。Windows 安装包由 WiX 打成 exe；Linux 同时给 deb 安装包和可直接跑的便携包。

本机用 **JDK 21+**（含 `jpackage`）重打：

```bash
./scripts/package-native.sh            # 当前系统 / 当前芯片
scripts\package-native.bat             # Windows：生成 exe（需 WiX 3.14）
./scripts/package-native.sh 1.3.0 deb  # Linux：deb + tar.gz
```

产物在 `dist/native/`。CI 不能跨平台交叉编译：exe 在 Windows 打，`.app` 在对应芯片的 macOS 打，deb / tar.gz 在 Linux 打。

### 4. 开发启动 / 源码


```bash
chmod +x mvnw
./mvnw spring-boot:run
```

启动后：

- 办公工作台：<http://localhost:8080/#/office>
- 硬件检测台：<http://localhost:8080/#/hw>
- 首页：<http://localhost:8080/>
- Swagger UI：<http://localhost:8080/swagger-ui.html>
- 健康检查：<http://localhost:8080/actuator/health>
- 构建信息：<http://localhost:8080/actuator/info>

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
