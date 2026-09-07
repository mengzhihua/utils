package com.mengzhihua.utils.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoreUtilsTest {

    @Test
    void creditCodeChecksumRoundTrip() {
        String body = "91110000710930405";
        String code = CreditCodeUtil.complete(body);
        assertEquals(18, code.length());
        assertTrue(CreditCodeUtil.isValid(code));
        assertFalse(CreditCodeUtil.isValid(body + "0"));
        assertTrue(RegexUtil.isCreditCode(code));
    }

    @Test
    void moneyYuanFenAndSplit() {
        assertEquals(12350L, MoneyUtil.yuanToFen("123.5"));
        assertEquals("123.50", MoneyUtil.fenToYuanString(12350));
        List<Long> parts = MoneyUtil.split(100, 4);
        assertEquals(4, parts.size());
        assertEquals(100L, parts.stream().mapToLong(Long::longValue).sum());
        parts.forEach(part -> assertTrue(part >= 1));
    }

    @Test
    void phoneCarrierAndBankLuhn() {
        assertEquals("中国移动", PhoneUtil.carrier("13812345678"));
        assertEquals("中国联通", PhoneUtil.carrier("13012345678"));
        assertEquals("虚拟运营商", PhoneUtil.carrier("17012345678"));
        assertTrue(BankCardUtil.isValid("4111111111111111"));
    }

    @Test
    void geoTransformRoundTripNearBeijing() {
        double lat = 39.9087;
        double lon = 116.3975;
        double[] gcj = GeoUtil.wgs84ToGcj02(lat, lon);
        double[] back = GeoUtil.gcj02ToWgs84(gcj[0], gcj[1]);
        assertTrue(GeoUtil.distanceMeters(lat, lon, back[0], back[1]) < 1);
        double[] bd = GeoUtil.gcj02ToBd09(gcj[0], gcj[1]);
        double[] gcj2 = GeoUtil.bd09ToGcj02(bd[0], bd[1]);
        assertTrue(Math.abs(gcj[0] - gcj2[0]) < 1e-6);
        assertTrue(Math.abs(gcj[1] - gcj2[1]) < 1e-6);
    }

    @Test
    void signTotpCronIpCidr() {
        Map<String, String> params = Map.of("b", "2", "a", "1", "sign", "ignore");
        assertEquals("a=1&b=2&secret=s", SignUtil.canonical(params, "s"));
        assertEquals(32, SignUtil.md5(params, "s").length());

        String secret = "GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ";
        assertEquals("94287082", TotpUtil.generate(secret, 59, 30, 8));
        assertTrue(TotpUtil.verify(TotpUtil.generate(secret), secret));

        assertTrue(CronUtil.isValid("0 0 9 * * MON-FRI"));
        assertFalse(CronUtil.isValid("not-a-cron"));
        List<String> next = CronUtil.nextTimes("0 0 9 * * MON-FRI",
                ZonedDateTime.of(2026, 9, 7, 8, 0, 0, 0, DateTimeUtil.DEFAULT_ZONE), 2);
        assertEquals(2, next.size());
        assertTrue(next.get(0).contains("09:00:00"));

        assertTrue(IpUtil.inCidr("172.16.0.10", "172.16.0.0/24"));
        assertFalse(IpUtil.inCidr("10.0.0.4", "172.16.0.0/24"));
        assertEquals("172.16.0.0", IpUtil.cidrNetwork("172.16.0.10/24"));
        assertEquals("172.16.0.255", IpUtil.cidrBroadcast("172.16.0.0/24"));
        assertEquals(256, IpUtil.cidrHostCount("172.16.0.0/24"));
        assertEquals("172.16.0.10", IpUtil.longToIpv4(IpUtil.ipv4ToLong("172.16.0.10")));
    }

    @Test
    void idUlidShortCodeByteSizeYaml() {
        String ulid = IdUtil.ulid();
        assertEquals(26, ulid.length());
        assertEquals("w7e", ShortCodeUtil.encode(123456));
        assertEquals(123456L, ShortCodeUtil.decode("w7e"));
        assertEquals("1.00 KB", ByteSizeUtil.format(1024));
        assertEquals(1572864L, ByteSizeUtil.parse("1.5 MB"));
        String yaml = YamlUtil.jsonToYaml("{\"name\":\"Ada\",\"n\":1}");
        assertTrue(yaml.contains("name"));
        Map<String, Object> map = YamlUtil.toMap(yaml);
        assertEquals("Ada", String.valueOf(map.get("name")));
        assertEquals(8, EncryptUtil.crc32("123").length());
    }

    @Test
    void stringTemplateWidthWorkdayTextSensitive() {
        assertEquals("user-name", StringUtil.toKebab("userName"));
        assertEquals("UserName", StringUtil.toPascal("user_name"));
        assertEquals("你好，Ada", StringUtil.format("你好，{name}", Map.of("name", "Ada")));
        assertEquals("Hello 123", StringUtil.toHalfWidth("Ｈｅｌｌｏ　１２３"));
        assertEquals("刚刚", DateTimeUtil.fromNow(DateTimeUtil.now()));
        LocalDate monday = LocalDate.of(2026, 9, 7);
        assertTrue(DateTimeUtil.isWorkday(monday));
        assertEquals(LocalDate.of(2026, 9, 10), DateTimeUtil.plusWorkdays(monday, 3));
        assertEquals(5, DateTimeUtil.workdaysBetween(monday, LocalDate.of(2026, 9, 14)));
        assertEquals(1, TextUtil.levenshtein("kit", "kat"));
        assertTrue(TextUtil.similarity("hello", "hello") > 0.99);
        assertTrue(SensitiveWordUtil.contains("开发票"));
        assertTrue(SensitiveWordUtil.replace("开发票", '*').contains("*"));
        CollectionUtil.Diff<String> diff = CollectionUtil.diff(List.of("a", "b"), List.of("b", "c"));
        assertEquals(List.of("c"), diff.added());
        assertEquals(List.of("a"), diff.removed());
        Map<String, String> query = UrlUtil.parseQuery("https://a.com?q=%E5%B7%A5%E5%85%B7&x=1");
        assertEquals("工具", query.get("q"));
        assertEquals("1", query.get("x"));
    }

    @Test
    void regexPlateAndRelativePast() {
        assertTrue(RegexUtil.isPlate("京A12345"));
        assertTrue(RegexUtil.isZipcode("100000"));
        String relative = DateTimeUtil.fromNow(LocalDateTime.now(DateTimeUtil.DEFAULT_ZONE).minusMinutes(5));
        assertTrue(relative.contains("分钟前"));
    }
}
