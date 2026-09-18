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

## 前端控制台（Vue 3）

启动后打开 <http://localhost:8080/> 即可在页面上自测常用工具：脱敏、身份证、JWT、AES、人民币大写、雪花 ID 等。源码在 `frontend/`，构建产物输出到 `src/main/resources/static/`。控制台包含：

- 后端 Java 工具演示（脱敏、JWT、AES、身份证、信用代码、坐标系、签名、TOTP、Cron、Ant 路径、限流、星座、slug、时长、农历、表达式、ISBN、验证码、单位换算、IMEI、Hashids、罗马数字、URL 解析、IBAN、VIN、EAN、Soundex、Jaro-Winkler、SHA3、KSUID、Sqids、UUID v5/v6、Metaphone、ISIN、BIC、Bech32、HKDF、摩斯电码、CaseFormat、HOTP、Base45、JSON Patch、节假日、WCAG 对比度、CUSIP、SEDOL、ORCID、ISRC、JSON Merge Patch、HTTP Date、车牌、URI Template、正则抽取、SM3、ISBN 互转、IPv6、SemVer、CRC-16、SM4、BLAKE2s、CMAC、香港/台湾身份证、组织机构代码、节气、拼音、XML、ChaCha20、CRC-64、BLAKE2b、AES-KW、Ed25519、TSID、干支、Jump Hash、集装箱号、Bech32m、ABA、X25519、RIPEMD-160、FIGI、LEI、NHS、SHAKE、NPI、ISMN、NRIC、科隆拼音、HTTP Range、Hamming、UUID v8、NYSIIS、Caverphone、Sonyflake、Content-Disposition、儒略日、CPF、CNPJ、PESEL、UPC-E、CRC-32/MPEG-2、HMAC-SM3、Murmur3-128、Double Metaphone、Match Rating、SIREN、SIRET、NIF、ISNI、basE91、Bencode、HTTP Accept、Refined Soundex、Porter、NIR、意大利税号、德国税号、EORI、DOI、PMID、ICCID、HTTP Forwarded、Fletcher、瑞典/芬兰/挪威个人号、ISWC、SSCC、ABN、TFN、HTTP Link、ETag、uuencode、VAT、AHV、NIP、Aadhaar、PAN、SIN、PPS、Cache-Control、Z85、CRC-16/XMODEM、丹麦 CPR、比利时 NRN、奥地利 SVNR、葡萄牙 NIF、希腊 AFM、英国 NINO、韩国居民号、日本个人番号、yEnc、Base36、Retry-After、UUID v1、智利 RUT、阿根廷 CUIT、南非身份证、新西兰 IRD、马来西亚 MyKad、CORS、WWW-Authenticate、土耳其 TCKN、以色列身份证、罗马尼亚 CNP、克罗地亚 OIB、保加利亚 EGN、泰国身份证、波兰 REGON、捷克 IČO、HSTS、CSP、Accept-Encoding、CRC-16/KERMIT、南斯拉夫 JMBG、爱沙尼亚个人号、冰岛 kennitala、匈牙利 TAJ、乌克兰 IPN、哥伦比亚 NIT、Referrer-Policy、X-Frame-Options、Permissions-Policy、CRC-16/ARC、拉脱维亚个人号、立陶宛个人号、斯洛文尼亚 EMŠO、秘鲁 DNI、墨西哥 RFC、HTTP Age、HTTP Warning、CRC-16/MAXIM、荷兰 BSN、捷克出生号、Base92、Structured Fields、芬兰企业号、挪威企业号、丹麦 CVR、西班牙 CIF、瑞典企业号、CRC-32/POSIX、瑞士企业号、保加利亚 EIK、罗马尼亚 CUI、匈牙利税号、比利时 KBO、Clear-Site-Data、CRC-16/USB、日本法人番号、韩国事业者号、台湾统一编号、乌克兰 EDRPOU、塞尔维亚 PIB、HTTP NEL、CRC-8/SMBUS、印度 GSTIN、澳大利亚公司号、土耳其税号、印尼税号、爱沙尼亚企业号、HTTP Report-To、CRC-16/GENIBUS、新西兰企业号、新加坡 UEN、以色列公司号、立陶宛企业号、HTTP COOP、HTTP COEP、CRC-32/JAMCRC、俄罗斯税号、秘鲁税号、印尼身份证、越南税号、HTTP CORP、HTTP X-Content-Type-Options、CRC-16/DNP、美国雇主识别号、俄罗斯统一注册号、俄罗斯养老金号、阿尔巴尼亚税号、HTTP Timing-Allow-Origin、HTTP Origin-Agent-Cluster、CRC-16/CMS、委内瑞拉税号、多米尼加税号、白俄罗斯税号、美国个人税号、HTTP Reporting-Endpoints、HTTP Accept-CH、CRC-16/CDMA2000、巴基斯坦身份证、摩尔多瓦企业号、加纳税号、肯尼亚税号、HTTP Document-Policy、CRC-32/AUTOSAR 等）
- 浏览器本地工具：JSON/XML、Base64、JWT 解码、Markdown、命名风格、全角半角、行处理、文本对比、MD5/HMAC/CRC32/Murmur3、ULID、UUID v7、CIDR、UA/Cookie、星座、高亮、四则运算、进制、ISBN、Punycode、罗马数字、单位换算、IMEI、URL 解析、Soundex、Jaro-Winkler、IBAN、EAN、年龄、ROT13、摩斯、通配符、ISIN、Humanize、Quoted-Printable、Base45、WCAG 对比度、去音调、车牌、Emoji、CUSIP、正则抽取/转义、ISBN 互转、IPv6、卡组织、时长差、拼音首字母、港澳台证件、组织机构代码、节气、干支、ABA、集装箱号、Jump Hash、FIGI、LEI、NHS、NPI、ISMN、NRIC、科隆拼音、Hamming、UUID v8、NYSIIS、Caverphone、CPF、CNPJ、PESEL、UPC-E、儒略日、Double Metaphone、Match Rating、SIREN、SIRET、NIF、ISNI、Bencode、Refined Soundex、NIR、意大利税号、德国税号、EORI、DOI、PMID、ICCID、瑞典/芬兰/挪威个人号、ISWC、ABN、TFN、SSCC、VAT、AHV、NIP、Aadhaar、PAN、SIN、PPS、丹麦 CPR、葡萄牙 NIF、英国 NINO、韩国居民号、希腊 AFM、智利 RUT、阿根廷 CUIT、南非身份证、土耳其 TCKN、罗马尼亚 CNP、泰国身份证、克罗地亚 OIB、南斯拉夫 JMBG、冰岛 kennitala、匈牙利 TAJ、哥伦比亚 NIT、拉脱维亚个人号、斯洛文尼亚 EMŠO、秘鲁 DNI、墨西哥 RFC、荷兰 BSN、捷克出生号、芬兰企业号、丹麦 CVR、西班牙 CIF、瑞士企业号、罗马尼亚 CUI、比利时 KBO、日本法人番号、韩国事业者号、台湾统一编号、乌克兰 EDRPOU、塞尔维亚 PIB、印度 GSTIN、澳大利亚公司号、土耳其税号、印尼税号、爱沙尼亚企业号、新西兰企业号、新加坡 UEN、以色列公司号、立陶宛企业号、俄罗斯税号、秘鲁税号、印尼身份证、越南税号、美国雇主识别号、俄罗斯统一注册号、俄罗斯养老金号、阿尔巴尼亚税号、委内瑞拉税号、多米尼加税号、白俄罗斯税号、美国个人税号、巴基斯坦身份证、摩尔多瓦企业号、加纳税号、肯尼亚税号 等
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
