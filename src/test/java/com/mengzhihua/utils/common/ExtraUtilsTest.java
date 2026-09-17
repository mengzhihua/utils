package com.mengzhihua.utils.common;


import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.mengzhihua.utils.common.bean.BeanUtil;
import com.mengzhihua.utils.common.bean.ClassUtil;
import com.mengzhihua.utils.common.bean.CloneUtil;
import com.mengzhihua.utils.common.bean.EnumUtil;
import com.mengzhihua.utils.common.bean.ReflectUtil;
import com.mengzhihua.utils.common.codec.HexUtil;
import com.mengzhihua.utils.common.concurrent.LocalCacheUtil;
import com.mengzhihua.utils.common.concurrent.RetryUtil;
import com.mengzhihua.utils.common.crypto.JwtUtil;
import com.mengzhihua.utils.common.crypto.PasswordUtil;
import com.mengzhihua.utils.common.crypto.RsaUtil;
import com.mengzhihua.utils.common.extra.GeoUtil;
import com.mengzhihua.utils.common.extra.TraceIdUtil;
import com.mengzhihua.utils.common.id.OrderNoUtil;
import com.mengzhihua.utils.common.io.FileUtil;
import com.mengzhihua.utils.common.io.MimeUtil;
import com.mengzhihua.utils.common.io.PathUtil;
import com.mengzhihua.utils.common.io.ZipUtil;
import com.mengzhihua.utils.common.json.CsvUtil;
import com.mengzhihua.utils.common.lang.ArrayUtil;
import com.mengzhihua.utils.common.lang.BooleanUtil;
import com.mengzhihua.utils.common.lang.ExceptionUtil;
import com.mengzhihua.utils.common.lang.MapUtil;
import com.mengzhihua.utils.common.lang.PageUtil;
import com.mengzhihua.utils.common.math.ChineseNumberUtil;
import com.mengzhihua.utils.common.math.VersionUtil;
import com.mengzhihua.utils.common.net.UrlUtil;
import com.mengzhihua.utils.common.net.UserAgentUtil;
import com.mengzhihua.utils.common.spring.SpelUtil;
import com.mengzhihua.utils.common.text.DesensitizeUtil;
import com.mengzhihua.utils.common.text.HtmlUtil;
import com.mengzhihua.utils.common.text.XmlUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import com.mengzhihua.utils.common.validate.BankCardUtil;
import com.mengzhihua.utils.common.validate.IdCardUtil;
import com.mengzhihua.utils.common.validate.SqlUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExtraUtilsTest {

    private static final String VALID_ID = "110101199003078937";

    enum Color {RED, GREEN}

    public static class Person {
        private String name;
        private Integer age;

        public Person() {
        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    @Test
    void arrayMapBooleanHexUrlHtmlSql() {
        String[] arr = {"a", "b"};
        assertTrue(ArrayUtil.contains(arr, "b"));
        assertEquals("b", ArrayUtil.last(arr));
        assertEquals(3, ArrayUtil.add(arr, "c").length);

        Map<String, Object> map = MapUtil.of("n", 8, "s", "ok");
        assertEquals(Integer.valueOf(8), MapUtil.getInt(map, "n", 0));
        assertEquals("ok", MapUtil.getStr(map, "s"));
        assertTrue(BooleanUtil.isTrue("yes"));
        assertEquals("hello", HexUtil.decodeToString(HexUtil.encode("hello")));
        assertTrue(UrlUtil.appendQuery("https://a.com", Map.of("q", "一")).contains("%"));
        assertEquals("&lt;b&gt;", HtmlUtil.escape("<b>"));
        assertEquals("hi", HtmlUtil.stripTags("<p>hi</p>"));
        assertEquals("created_at", SqlUtil.column("created_at"));
        assertEquals("DESC", SqlUtil.direction("desc"));
        assertThrows(IllegalArgumentException.class, () -> SqlUtil.escapeOrderBy("id;drop"));
    }

    @Test
    void ioExceptionReflectEnumPageVersion() {
        assertEquals("boom", ExceptionUtil.getMessage(new IllegalStateException("boom")));
        assertTrue(ExceptionUtil.stacktrace(new RuntimeException("x")).contains("RuntimeException"));

        Person person = new Person("Ada", 1);
        assertEquals("Ada", ReflectUtil.getFieldValue(person, "name"));
        ReflectUtil.setFieldValue(person, "name", "Bob");
        assertEquals("Bob", person.getName());
        assertEquals(Color.GREEN, EnumUtil.fromNameIgnoreCase(Color.class, "green"));
        assertEquals(10, PageUtil.offset(3, 5));
        assertEquals(3, PageUtil.pages(11, 5));
        assertTrue(VersionUtil.isGreater("1.2.10", "1.2.9"));
        assertTrue(ClassUtil.isPresent("java.lang.String"));
        assertEquals("a/b/c", PathUtil.join("a", "b", "c"));
    }

    @Test
    void idCardJwtRsaDesensitizeBank() {
        assertTrue(IdCardUtil.isValid(VALID_ID));
        assertEquals(LocalDate.of(1990, 3, 7), IdCardUtil.getBirthday(VALID_ID));
        assertEquals("M", IdCardUtil.getGender(VALID_ID));
        assertEquals("北京", IdCardUtil.getProvince(VALID_ID));
        assertEquals("张*", DesensitizeUtil.chineseName("张三"));
        assertEquals("6222********7890", DesensitizeUtil.bankCard("6222021234567890"));
        assertTrue(BankCardUtil.isValid("4111111111111111"));
        assertFalse(BankCardUtil.isValid("4111111111111112"));

        String token = JwtUtil.create(Map.of("sub", "u1"), "secret", Duration.ofMinutes(5));
        assertEquals("u1", JwtUtil.parse(token, "secret").get("sub"));
        assertThrows(IllegalArgumentException.class, () -> JwtUtil.parse(token, "wrong"));

        RsaUtil.KeyPairKeys keys = RsaUtil.generateKeys();
        String cipher = RsaUtil.encrypt("hello", keys.publicKey());
        assertEquals("hello", RsaUtil.decrypt(cipher, keys.privateKey()));
        String sign = RsaUtil.sign("hello", keys.privateKey());
        assertTrue(RsaUtil.verify("hello", sign, keys.publicKey()));
        assertFalse(RsaUtil.verify("other", sign, keys.publicKey()));
    }

    @Test
    void retryCacheZipCsvSpelCloneGeoChinese() throws Exception {
        AtomicInteger hits = new AtomicInteger();
        String ok = RetryUtil.execute(() -> {
            if (hits.incrementAndGet() < 3) {
                throw new IllegalStateException("fail");
            }
            return "ok";
        }, 3, 0);
        assertEquals("ok", ok);

        LocalCacheUtil.put("k", "v", Duration.ofMinutes(1));
        assertEquals("v", LocalCacheUtil.get("k"));
        LocalCacheUtil.evict("k");
        assertNull(LocalCacheUtil.get("k"));

        Path dir = Files.createTempDirectory("zip-src");
        Path file = dir.resolve("a.txt");
        FileUtil.writeUtf8(file, "hello");
        Path zip = Files.createTempDirectory("zip-out").resolve("a.zip");
        ZipUtil.zip(file, zip);
        Path unzipTo = Files.createTempDirectory("zip-unzip");
        ZipUtil.unzip(zip, unzipTo);
        assertEquals("hello", FileUtil.readUtf8(unzipTo.resolve("a.txt")));

        String csv = CsvUtil.toCsv(List.of("n", "a"), List.of(List.of("Ada", "20")));
        List<List<String>> parsed = CsvUtil.parse(csv);
        assertEquals("n", parsed.get(0).get(0));
        assertEquals("Ada", parsed.get(1).get(0));

        Person person = new Person("Ada", 18);
        assertEquals("Ada", SpelUtil.eval("name", person, String.class));
        Person cloned = CloneUtil.deep(person, Person.class);
        cloned.setName("Bob");
        assertEquals("Ada", person.getName());

        double km = GeoUtil.distanceKm(39.9, 116.4, 31.2, 121.5);
        assertTrue(km > 1000 && km < 1200);
        assertEquals("十二", ChineseNumberUtil.toChinese(12));
        assertEquals("十", ChineseNumberUtil.toChinese(10));
        assertTrue(ChineseNumberUtil.toRmb("1.5").contains("壹元"));
        assertTrue(PasswordUtil.isStrong("Abcdef1!xyz"));
        assertTrue(UserAgentUtil.isWeChat("Mozilla/5.0 MicroMessenger"));
        assertTrue(OrderNoUtil.next("SO").startsWith("SO"));
        assertEquals("application/json", MimeUtil.getByFilename("a.json"));
        assertEquals("&lt;a&gt;", XmlUtil.escape("<a>"));
        assertNotNull(TraceIdUtil.get());
        TraceIdUtil.clear();

        Person patch = new Person();
        patch.setName("Neo");
        Person target = new Person("Ada", 20);
        BeanUtil.copyIgnoreNull(patch, target);
        assertEquals("Neo", target.getName());
        assertEquals(Integer.valueOf(20), target.getAge());
        assertTrue(DateTimeUtil.isWeekend(LocalDate.of(2026, 9, 5)));
        assertTrue(DateTimeUtil.age(LocalDate.of(2000, 1, 1)) >= 26);
    }
}
