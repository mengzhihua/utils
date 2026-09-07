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
| 字符串 / 脱敏 | `StringUtil` / `DesensitizeUtil` / `HtmlUtil` / `XmlUtil` | 驼峰转换、姓名电话身份证银行卡脱敏、XSS 转义 |
| 对象 / 集合 | `ObjectUtil` / `ArrayUtil` / `CollectionUtil` / `MapUtil` / `BeanUtil` | 判空、分组分页、Bean 拷贝（含忽略 null） |
| 日期 | `DateTimeUtil` | 格式化、起止日/周/月、年龄、周末、时长 |
| JSON / CSV | `JsonUtil` / `CsvUtil` / `CloneUtil` | Jackson 3、CSV 编解码、深拷贝 |
| 数字 | `NumberUtil` / `ConvertUtil` / `BooleanUtil` / `ChineseNumberUtil` / `PageUtil` / `VersionUtil` | 精确运算、人民币大写、分页、版本比较 |
| 校验 / 断言 | `RegexUtil` / `AssertUtil` / `SqlUtil` / `IdCardUtil` / `BankCardUtil` / `PasswordUtil` | 格式校验、身份证校验码、Luhn、密码强度、ORDER BY 白名单 |

### 安全、ID、文件、网络

| 模块 | 类 | 说明 |
| --- | --- | --- |
| 加解密 | `EncryptUtil` / `RsaUtil` / `JwtUtil` / `HexUtil` | MD5/SHA、AES-GCM、RSA-OAEP、HS256 JWT |
| ID / 单号 | `IdUtil` / `SnowflakeIdGenerator` / `OrderNoUtil` / `RandomUtil` | UUID、雪花、业务单号 |
| 文件 | `FileUtil` / `IoUtil` / `ZipUtil` / `MimeUtil` / `PathUtil` / `DownloadUtil` | 读写、压缩（防 zip-slip）、下载头 |
| 网络 / Web | `IpUtil` / `NetUtil` / `UrlUtil` / `HttpUtil` / `ServletUtil` / `CookieUtil` / `UserAgentUtil` | 客户端 IP、URL、Cookie、UA |
| 树 / 地理 | `TreeUtil` / `GeoUtil` | 扁平列表建树、经纬度距离 |
| 并发 / 系统 | `ThreadUtil` / `RetryUtil` / `StopWatchUtil` / `LocalCacheUtil` / `SystemUtil` | 虚拟线程、重试、计时、本地 TTL 缓存 |
| 反射 | `ReflectUtil` / `EnumUtil` / `ClassUtil` / `ExceptionUtil` | 字段读写、枚举查找、堆栈 |

## 前端控制台（Vue 3）

启动后打开 <http://localhost:8080/> 即可在页面上自测常用工具：脱敏、身份证、JWT、AES、人民币大写、雪花 ID 等。源码在 `frontend/`，构建产物输出到 `src/main/resources/static/`。

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
