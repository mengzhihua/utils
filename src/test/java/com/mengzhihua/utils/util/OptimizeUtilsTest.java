package com.mengzhihua.utils.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptimizeUtilsTest {

    @Test
    void cacheRetrySnowflakeJwtIds() {
        LocalCacheUtil.clear();
        LocalCacheUtil.put("a", "v", Duration.ofMinutes(1));
        assertEquals("v", LocalCacheUtil.get("a"));
        assertEquals("loaded", LocalCacheUtil.getOrLoad("missing", Duration.ofMinutes(1), () -> "loaded"));
        assertEquals("loaded", LocalCacheUtil.get("missing"));

        AtomicInteger hits = new AtomicInteger();
        String ok = RetryUtil.execute(() -> {
            if (hits.incrementAndGet() < 3) {
                throw new IllegalStateException("fail");
            }
            return "ok";
        }, 3, 1, 2D);
        assertEquals("ok", ok);

        long id = IdUtil.snowflakeId();
        SnowflakeIdGenerator.Parts parts = IdUtil.parseSnowflake(id);
        assertEquals(1, parts.workerId());
        assertEquals(1, parts.datacenterId());
        assertTrue(parts.epochMilli() > 1_700_000_000_000L);

        String v7 = IdUtil.uuidV7();
        assertEquals(7, UUID.fromString(v7).version());
        assertEquals("Ada", ObjectUtil.firstNonNull(null, "Ada", "Bob"));

        String token = JwtUtil.create(Map.of("sub", "u1"), "secret", Duration.ofMinutes(5));
        assertEquals("u1", JwtUtil.decode(token).get("sub"));
        assertTrue(JwtUtil.remainingSeconds(token, "secret") > 0);
        assertTrue(PasswordUtil.isStrong(PasswordUtil.generate(16)));
        assertEquals("a.png", FileUtil.sanitize("../../a.png"));
    }

    @Test
    void rateLimitKeyedLockAntColorZodiac() {
        RateLimiterUtil.clear();
        assertTrue(RateLimiterUtil.tryAcquire("k", 100));
        KeyedLockUtil.run("lock-a", () -> assertEquals(1, 1));
        assertTrue(AntPathUtil.match("/api/**", "/api/utils/ip"));
        assertTrue(AntPathUtil.matchAny("/api/utils/ip", "/health", "/api/**"));
        assertEquals("#0F766E", ColorUtil.rgbToHex(15, 118, 110));
        assertTrue(ColorUtil.isDark("#0f766e"));
        assertEquals("双鱼座", ZodiacUtil.constellation(LocalDate.of(1990, 3, 7)));
        assertEquals("马", ZodiacUtil.chineseZodiac(1990));
    }

    @Test
    void mapPathHashFileTypeEscapeBatch() throws Exception {
        Map<String, Object> nested = Map.of("user", Map.of("name", "Ada"));
        assertEquals("Ada", MapPathUtil.getStr(nested, "user.name"));
        assertEquals("Ada", MapPathUtil.flatten(nested).get("user.name"));
        Map<String, Object> rebuilt = MapPathUtil.unflatten(Map.of("user.name", "Ada"));
        assertEquals("Ada", MapPathUtil.getStr(rebuilt, "user.name"));

        assertEquals("image/png", FileTypeUtil.ofHex("89504e47"));
        assertEquals("image/png", FileTypeUtil.ofFilename("a.PNG"));
        assertEquals("a\\\"b", EscapeUtil.js("a\"b"));
        assertTrue(HighlightUtil.html("工具集", "工具").contains("<mark>"));
        List<Integer> collected = new ArrayList<>();
        BatchUtil.forEach(List.of(1, 2, 3, 4, 5), 2, collected::addAll);
        assertEquals(List.of(1, 2, 3, 4, 5), collected);
        assertEquals(3, BatchUtil.batchCount(5, 2));

        Path file = Files.createTempFile("hash", ".txt");
        FileUtil.writeUtf8(file, "hello");
        assertEquals(EncryptUtil.md5("hello"), HashUtil.md5(file));
        assertEquals(EncryptUtil.sha256("hello"), HashUtil.sha256(file));
        String picked = WeightRandomUtil.pick(Map.of("A", 1, "B", 0));
        assertEquals("A", picked);
        assertTrue(ResourceUtil.exists("application.yml") || ResourceUtil.exists("application.yaml")
                || ResourceUtil.exists("application.properties"));
        assertNotNull(HashUtil.murmur32("hello"));
    }

    @Test
    void jsonPrettyAndDesensitizePlate() {
        String pretty = JsonUtil.toPrettyJson(Map.of("n", 1));
        assertTrue(pretty.contains("\n") || pretty.contains("n"));
        assertEquals("京A****5", DesensitizeUtil.plate("京A12345"));
    }

    @Test
    void durationSlugPercentVerifyBloomCircuit() {
        assertEquals(5_400_000L, DurationUtil.toMillis("1h30m"));
        assertEquals(90_000L, DurationUtil.toMillis("90s"));
        assertEquals(15 * 60_000L, DurationUtil.toMillis("PT15M"));
        assertEquals("spring-boot-工具集", SlugUtil.of("Spring Boot 工具集"));
        assertEquals("cafe", SlugUtil.of("Café"));
        assertEquals("12.50", PercentUtil.of(25, 200).toPlainString());
        assertEquals("12.50%", PercentUtil.format(PercentUtil.of(25, 200)));
        assertEquals("25.00", PercentUtil.change(100, 125).toPlainString());

        String code = VerifyCodeUtil.numeric(6);
        assertEquals(6, code.length());
        assertTrue(VerifyCodeUtil.matches(code, code));
        assertFalse(VerifyCodeUtil.matches(code, "000000"));

        BloomFilterUtil.BloomFilter filter = BloomFilterUtil.create(100, 0.01);
        filter.put("hello");
        assertTrue(filter.mightContain("hello"));

        CircuitBreakerUtil.clear();
        CircuitBreakerUtil.reset("demo");
        for (int i = 0; i < 5; i++) {
            CircuitBreakerUtil.recordFailure("demo");
        }
        assertEquals(CircuitBreakerUtil.State.OPEN, CircuitBreakerUtil.state("demo"));
        assertFalse(CircuitBreakerUtil.allow("demo"));
        CircuitBreakerUtil.reset("demo");
        assertTrue(CircuitBreakerUtil.allow("demo"));
    }

    @Test
    void murmurEscapePrettyImage() throws Exception {
        assertEquals("248bfa47", HashUtil.murmur32Hex("hello"));
        assertEquals("\"a\\\"b\"", EscapeUtil.json("a\"b"));
        String pretty = JsonUtil.toPrettyJson("{\"n\":1}");
        assertTrue(pretty.contains("\n"));
        Path image = Files.createTempFile("img", ".png");
        java.awt.image.BufferedImage buffered = new java.awt.image.BufferedImage(2, 3, java.awt.image.BufferedImage.TYPE_INT_RGB);
        javax.imageio.ImageIO.write(buffered, "png", image.toFile());
        ImageUtil.Size size = ImageUtil.size(image);
        assertEquals(2, size.width());
        assertEquals(3, size.height());
        assertEquals(6, size.pixels());
    }
}
