package com.mengzhihua.utils.common;


import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.mengzhihua.utils.common.bean.BeanUtil;
import com.mengzhihua.utils.common.bean.ConvertUtil;
import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.exception.BizException;
import com.mengzhihua.utils.common.extra.TreeNode;
import com.mengzhihua.utils.common.extra.TreeUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.io.FileUtil;
import com.mengzhihua.utils.common.json.JsonUtil;
import com.mengzhihua.utils.common.lang.AssertUtil;
import com.mengzhihua.utils.common.lang.CollectionUtil;
import com.mengzhihua.utils.common.lang.ObjectUtil;
import com.mengzhihua.utils.common.lang.RandomUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.math.NumberUtil;
import com.mengzhihua.utils.common.net.IpUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoreUtilsTest {

    @Test
    void stringUtilCoversBlankCaseAndMask() {
        assertTrue(StringUtil.isBlank("  "));
        assertFalse(StringUtil.isBlank("ok"));
        assertEquals("user_name", StringUtil.camelToSnake("userName"));
        assertEquals("userName", StringUtil.snakeToCamel("user_name"));
        assertEquals("138****5678", StringUtil.maskPhone("13812345678"));
        assertEquals("a***@example.com", StringUtil.maskEmail("abcd@example.com"));
        assertEquals("110***********001X", StringUtil.maskIdCard("11010119900101001X"));
        assertEquals(List.of("a", "b"), StringUtil.split("a, b", ","));
        assertEquals("a-b-c", StringUtil.join("-", List.of("a", "b", "c")));
        assertEquals("Hello", StringUtil.capitalize("hello"));
        assertEquals("world", StringUtil.removePrefix("hello-world", "hello-"));
    }

    @Test
    void objectAndCollectionUtils() {
        assertTrue(ObjectUtil.isEmpty(""));
        assertTrue(ObjectUtil.isEmpty(List.of()));
        assertEquals("x", ObjectUtil.defaultIfNull(null, "x"));
        List<String> names = List.of("ann", "bob", "ann");
        assertEquals(List.of("ANN", "BOB", "ANN"), CollectionUtil.map(names, String::toUpperCase));
        assertEquals(List.of("ann", "bob"), CollectionUtil.distinct(names));
        assertEquals(2, CollectionUtil.groupBy(names, s -> s).get("ann").size());
        assertEquals(List.of("ann", "bob"), CollectionUtil.page(names, 1, 2));
        assertEquals(3, CollectionUtil.partition(List.of(1, 2, 3, 4, 5), 2).size());
    }

    @Test
    void dateTimeAndConvert() {
        LocalDateTime now = DateTimeUtil.parseDateTime("2026-09-07 08:00:00");
        assertEquals("2026-09-07 08:00:00", DateTimeUtil.format(now));
        assertEquals(1, DateTimeUtil.daysBetween(LocalDate.of(2026, 9, 7), LocalDate.of(2026, 9, 8)));
        assertEquals("2026-09-07 00:00:00", DateTimeUtil.format(DateTimeUtil.startOfDay(LocalDate.of(2026, 9, 7))));
        assertEquals(Integer.valueOf(12), ConvertUtil.toInt("12"));
        assertTrue(ConvertUtil.toBool("yes", false));
        assertEquals("fallback", ConvertUtil.toStr(null, "fallback"));
        assertEquals("12.30", NumberUtil.formatMoney("12.3"));
        assertEquals("15.0", NumberUtil.add("7.5", "7.5").toPlainString());
        assertTrue(NumberUtil.gt("2", "1"));
        assertEquals("50.0%", NumberUtil.percent("0.5", 1));
        assertNotNull(now);
    }

    @Test
    void jsonBeanAndTree() {
        DemoUser source = new DemoUser(1L, "Ada");
        String json = JsonUtil.toJson(source);
        DemoUser parsed = JsonUtil.fromJson(json, DemoUser.class);
        assertEquals("Ada", parsed.getName());
        assertTrue(JsonUtil.isJson(json));
        assertFalse(JsonUtil.isJson("not-json"));

        DemoUser copy = BeanUtil.copy(source, DemoUser.class);
        assertEquals(source.getName(), copy.getName());
        Map<String, Object> map = BeanUtil.toMap(source);
        assertEquals("Ada", map.get("name"));

        List<TreeNode<Long>> nodes = List.of(
                new TreeNode<>(1L, 0L, "root"),
                new TreeNode<>(2L, 1L, "child")
        );
        List<TreeNode<Long>> tree = TreeUtil.build(nodes, 0L);
        assertEquals(1, tree.size());
        assertEquals("child", tree.get(0).getChildren().get(0).getLabel());
    }

    @Test
    void encryptIdFileAndRegex() throws Exception {
        assertEquals(32, EncryptUtil.md5("hello").length());
        assertEquals(64, EncryptUtil.sha256("hello").length());
        String secret = "s3cret-key";
        String cipher = EncryptUtil.aesEncrypt("payload", secret);
        assertEquals("payload", EncryptUtil.aesDecrypt(cipher, secret));
        assertNotEquals("payload", cipher);
        assertEquals("hello", EncryptUtil.decodeBase64(EncryptUtil.encodeBase64("hello")));

        assertEquals(32, IdUtil.simpleUuid().length());
        assertTrue(IdUtil.snowflakeId() > 0);
        assertEquals(12, IdUtil.nanoId(12).length());
        assertEquals(6, RandomUtil.digits(6).length());

        Path temp = Files.createTempFile("utils-", ".txt");
        FileUtil.writeUtf8(temp, "hi");
        assertEquals("hi", FileUtil.readUtf8(temp));
        assertEquals("txt", FileUtil.getExtension(temp.getFileName().toString()));
        assertEquals("1.00 KB", FileUtil.formatSize(1024));
        Files.deleteIfExists(temp);

        assertTrue(RegexUtil.isMobile("13812345678"));
        assertTrue(RegexUtil.isEmail("a@b.com"));
        assertTrue(RegexUtil.isIpv4("192.168.1.3"));
        assertFalse(RegexUtil.isMobile("23812345678"));
        assertTrue(IpUtil.isInternalIp("192.168.1.3"));
        assertEquals(3232235777L, IpUtil.ipv4ToLong("192.168.1.1"));
    }

    @Test
    void assertUtilThrowsBizException() {
        assertThrows(BizException.class, () -> AssertUtil.notBlank(" ", "blank"));
        assertThrows(BizException.class, () -> AssertUtil.notEmpty(List.of(), "empty"));
        AssertUtil.isTrue(true, "ok");
        assertNull(JsonUtil.fromJson(" ", DemoUser.class));
    }

    public static class DemoUser {
        private Long id;
        private String name;

        public DemoUser() {
        }

        public DemoUser(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
