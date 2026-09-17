package com.mengzhihua.utils.common;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import com.mengzhihua.utils.common.codec.Base58Util;
import com.mengzhihua.utils.common.codec.HashidsUtil;
import com.mengzhihua.utils.common.codec.HexUtil;
import com.mengzhihua.utils.common.codec.RomanUtil;
import com.mengzhihua.utils.common.concurrent.ConsistentHashUtil;
import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.crypto.SignUtil;
import com.mengzhihua.utils.common.crypto.TotpUtil;
import com.mengzhihua.utils.common.extra.GeoUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.id.SeqUtil;
import com.mengzhihua.utils.common.id.ShortCodeUtil;
import com.mengzhihua.utils.common.io.ImageUtil;
import com.mengzhihua.utils.common.json.YamlUtil;
import com.mengzhihua.utils.common.lang.CollectionUtil;
import com.mengzhihua.utils.common.lang.RandomUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.math.ByteSizeUtil;
import com.mengzhihua.utils.common.math.MathUtil;
import com.mengzhihua.utils.common.math.MoneyUtil;
import com.mengzhihua.utils.common.math.UnitConvertUtil;
import com.mengzhihua.utils.common.net.IpUtil;
import com.mengzhihua.utils.common.net.UrlBuilder;
import com.mengzhihua.utils.common.net.UrlUtil;
import com.mengzhihua.utils.common.text.ReUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.text.SensitiveWordUtil;
import com.mengzhihua.utils.common.text.TextDiffUtil;
import com.mengzhihua.utils.common.text.TextUtil;
import com.mengzhihua.utils.common.time.CronUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import com.mengzhihua.utils.common.time.WeekUtil;
import com.mengzhihua.utils.common.validate.BankCardUtil;
import com.mengzhihua.utils.common.validate.CreditCodeUtil;
import com.mengzhihua.utils.common.validate.ImeiUtil;
import com.mengzhihua.utils.common.validate.PhoneUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    void mathReUnitRoman() {
        assertEquals(6, MathUtil.gcd(12, 18));
        assertEquals(36, MathUtil.lcm(12, 18));
        assertTrue(MathUtil.isPrime(13));
        assertFalse(MathUtil.isPrime(12));
        assertEquals("220", MathUtil.combination(12, 3).toString());
        assertEquals("2.0000", MathUtil.sqrt(4, 4).toPlainString());
        assertEquals("1000", UnitConvertUtil.convert(1, "km", "m").stripTrailingZeros().toPlainString());
        assertEquals("100", UnitConvertUtil.convert(1, "m", "cm").stripTrailingZeros().toPlainString());
        assertEquals("33.80000000", UnitConvertUtil.convert(1, "c", "f").toPlainString());
        assertEquals("MCMXCIV", RomanUtil.toRoman(1994));
        assertEquals(1994, RomanUtil.fromRoman("MCMXCIV"));
        assertTrue(RomanUtil.isValid("XIV"));
        assertFalse(RomanUtil.isValid("ABC"));
    }

    @Test
    void imeiUrlHashidsWeekSeq() {
        assertTrue(ImeiUtil.isValid("490154203237518"));
        assertTrue(ImeiUtil.isValid(ImeiUtil.generate()));
        assertFalse(ImeiUtil.isValid("490154203237519"));
        assertTrue(RegexUtil.isImei("490154203237518"));

        assertEquals("example.com", UrlUtil.getHost("https://example.com:8443/search?q=1#top"));
        assertEquals("/search", UrlUtil.getPath("https://example.com:8443/search?q=1#top"));
        assertEquals(8443, UrlUtil.getPort("https://example.com:8443/search?q=1#top"));
        assertEquals("top", UrlUtil.getFragment("https://example.com:8443/search?q=1#top"));
        String built = UrlBuilder.of("https://example.com/a")
                .appendPath("b")
                .query("q", "工具")
                .build();
        assertTrue(built.startsWith("https://example.com/a/b?"));
        assertTrue(built.contains("q="));

        String hash = HashidsUtil.encode(123L);
        assertEquals(123L, HashidsUtil.decodeOne(hash));
        assertEquals(List.of(1L, 2L, 3L).toString(),
                java.util.Arrays.stream(HashidsUtil.decode(HashidsUtil.encode(1, 2, 3))).boxed().toList().toString());

        LocalDate date = LocalDate.of(2024, 2, 10);
        assertEquals(6, WeekUtil.isoWeek(date));
        assertEquals("星期六", WeekUtil.chineseDayOfWeek(date));
        assertTrue(WeekUtil.isWeekend(date));
        assertEquals(LocalDate.of(2024, 2, 5), WeekUtil.startOfIsoWeek(date));

        String first = SeqUtil.next("UT");
        String second = SeqUtil.next("UT");
        assertTrue(first.startsWith("UT"));
        assertNotEquals(first, second);
        assertEquals(first.substring(0, 10), second.substring(0, 10));
    }

    @Test
    void reBase58DiffHashHexUuidImage() throws Exception {
        assertEquals(List.of("12", "34"), ReUtil.findAll("\\d+", "ab12cd34"));
        assertEquals("12", ReUtil.getFirstNumber("ab12cd34"));
        assertEquals("abcd", ReUtil.delAll("\\d+", "ab12cd34"));
        assertEquals("a\\.b", ReUtil.escape("a.b"));
        assertTrue(ReUtil.contains("\\d+", "ab12"));
        assertFalse(ReUtil.isMatch("\\d+", "ab12"));

        String encoded = Base58Util.encode("hello");
        assertEquals("hello", Base58Util.decodeToString(encoded));
        assertFalse(encoded.contains("0"));
        assertFalse(encoded.contains("O"));

        String unified = TextDiffUtil.unified("a\nb\nc", "a\nc\nd");
        assertTrue(unified.contains("- b"));
        assertTrue(unified.contains("+ d"));
        assertEquals(2, TextDiffUtil.changedLines("a\nb\nc", "a\nc\nd"));

        ConsistentHashUtil.Ring<String> ring = ConsistentHashUtil.of(List.of("n1", "node-b", "node-c"));
        String node = ring.get("user-1");
        assertNotNull(node);
        assertEquals(node, ring.get("user-1"));
        assertEquals(3, ring.size());

        assertTrue(HexUtil.isHex("0a1b"));
        assertFalse(HexUtil.isHex("0a1"));
        assertTrue(RegexUtil.isUuid("550e8400-e29b-41d4-a716-446655440000"));
        assertTrue(RegexUtil.isUuid("550e8400e29b41d4a716446655440000"));
        assertFalse(RegexUtil.isUuid("not-a-uuid"));

        assertEquals("b", RandomUtil.randomEle(List.of("b")));
        assertEquals(2, RandomUtil.randomEles(List.of("a", "b", "c"), 2).size());

        BufferedImage image = new BufferedImage(10, 8, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] png = out.toByteArray();
        ImageUtil.Size scaled = ImageUtil.size(ImageUtil.scale(png, 20, 0));
        assertEquals(20, scaled.width());
        assertEquals(16, scaled.height());
        ImageUtil.Size marked = ImageUtil.size(ImageUtil.watermark(png, "mark"));
        assertEquals(10, marked.width());
        assertEquals(8, marked.height());
    }
}
