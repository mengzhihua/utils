# Java Utils Toolkit

基于 **Spring Boot 4.1 + Java 21** 的通用工具集。既可直接运行演示服务，也可把 `com.mengzhihua.utils.util` 下的工具类复用到业务项目。

## 能力一览

| 模块 | 类 | 说明 |
| --- | --- | --- |
| 统一返回 | `Result` / `PageResult` / `ResultCode` | 接口统一包装 `{code, message, data, timestamp}` |
| 异常处理 | `BizException` / `GlobalExceptionHandler` | 业务异常与校验失败转统一响应 |
| 字符串 | `StringUtil` | 空白判断、驼峰/下划线、脱敏、拼接拆分 |
| 对象/集合 | `ObjectUtil` / `CollectionUtil` / `BeanUtil` | 判空、映射分组、Bean 拷贝 |
| 日期 | `DateTimeUtil` | 格式化、解析、起止时间、时间戳 |
| JSON | `JsonUtil` | Jackson 3 序列化 / 反序列化 |
| 加解密 | `EncryptUtil` | MD5/SHA、HMAC、Base64、AES-GCM |
| ID | `IdUtil` / `SnowflakeIdGenerator` | UUID、NanoId、雪花 ID |
| 文件 / IP | `FileUtil` / `IpUtil` | 扩展名、大小、读写、客户端 IP |
| 校验 / 数字 | `RegexUtil` / `NumberUtil` / `ConvertUtil` / `AssertUtil` | 格式校验、精确运算、断言 |
| 树 / HTTP | `TreeUtil` / `HttpUtil` / `ServletUtil` | 扁平列表建树、RestClient、当前请求 |
| Spring | `SpringContextHolder` | 静态取 Bean（优先使用构造注入） |

## 快速开始

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

启动后：

- 首页：<http://localhost:8080/>
- Swagger UI：<http://localhost:8080/swagger-ui.html>
- 健康检查：<http://localhost:8080/actuator/health>

示例：

```bash
curl "http://localhost:8080/api/utils/string/mask-phone?phone=13812345678"
curl "http://localhost:8080/api/utils/id/snowflake"
```

代码内直接调用：

```java
String phone = StringUtil.maskPhone("13812345678");
String json = JsonUtil.toJson(user);
long id = IdUtil.snowflakeId();
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
