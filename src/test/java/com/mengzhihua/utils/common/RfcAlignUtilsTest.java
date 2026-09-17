package com.mengzhihua.utils.common;


import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

import com.mengzhihua.utils.common.codec.Base45Util;
import com.mengzhihua.utils.common.codec.Base85Util;
import com.mengzhihua.utils.common.codec.QuotedPrintableUtil;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.HotpUtil;
import com.mengzhihua.utils.common.crypto.PemUtil;
import com.mengzhihua.utils.common.crypto.SipHashUtil;
import com.mengzhihua.utils.common.crypto.XxHashUtil;
import com.mengzhihua.utils.common.extra.ColorUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.io.BomUtil;
import com.mengzhihua.utils.common.io.ZipUtil;
import com.mengzhihua.utils.common.json.IniUtil;
import com.mengzhihua.utils.common.json.JsonPatchUtil;
import com.mengzhihua.utils.common.lang.UnsignedUtil;
import com.mengzhihua.utils.common.net.LanguageTagUtil;
import com.mengzhihua.utils.common.text.EscapeUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import com.mengzhihua.utils.common.time.HolidayUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RfcAlignUtilsTest {

    @Test
    void hotpRfc4226() {
        byte[] key = "12345678901234567890".getBytes(StandardCharsets.US_ASCII);
        assertEquals("755224", HotpUtil.generate(key, 0));
        assertEquals("287082", HotpUtil.generate(key, 1));
        assertEquals("359152", HotpUtil.generate(key, 2));
        assertTrue(HotpUtil.verify("755224", key, 0));
    }

    @Test
    void base45QuotedPrintableBase85() {
        assertEquals("BB8", Base45Util.encode("AB"));
        assertEquals("AB", Base45Util.decodeToString("BB8"));
        assertEquals("ietf!", Base45Util.decodeToString(Base45Util.encode("ietf!")));
        assertEquals("=3D", QuotedPrintableUtil.encode("="));
        assertEquals("Hello = 工具", QuotedPrintableUtil.decodeToString(QuotedPrintableUtil.encode("Hello = 工具")));
        assertEquals("Man", Base85Util.decodeToString(Base85Util.encode("Man")));
        assertTrue(Base85Util.encode("Man").contains("9jqo"));
    }

    @Test
    void xxhashSiphashAdler() {
        assertEquals("02cc5d05", String.format("%08x", XxHashUtil.hash32(new byte[0], 0)));
        assertEquals("ef46db3751d8e999", String.format("%016x", XxHashUtil.hash64(new byte[0], 0)));
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++) {
            key[i] = (byte) i;
        }
        assertEquals("726fdb47dd0e0e31", String.format("%016x", SipHashUtil.hash(key, new byte[0])));
        assertEquals("091e01de", HashUtil.adler32Hex("123456789"));
        assertEquals("4294967295", UnsignedUtil.toUnsignedString(-1));
    }

    @Test
    void holidayContrastJsonPatchIniPem() {
        assertEquals("国庆节", HolidayUtil.name(LocalDate.of(2026, 10, 1)));
        assertTrue(HolidayUtil.isHoliday(LocalDate.of(2026, 1, 1)));
        assertTrue(HolidayUtil.isHoliday(LocalDate.of(2026, 4, 5)));
        assertEquals("清明节", HolidayUtil.name(LocalDate.of(2026, 4, 5)));
        assertFalse(HolidayUtil.isHoliday(LocalDate.of(2026, 9, 17)));

        assertEquals(21D, ColorUtil.contrastRatio("#FFFFFF", "#000000"), 0.001);
        assertTrue(ColorUtil.aaa("#FFFFFF", "#000000"));

        String patched = JsonPatchUtil.apply("{\"name\":\"Bob\"}",
                "[{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"Ada\"}]");
        assertTrue(patched.contains("Ada"));
        String added = JsonPatchUtil.apply("{\"a\":1}", "[{\"op\":\"add\",\"path\":\"/b\",\"value\":2}]");
        assertTrue(added.contains("\"b\":2") || added.contains("\"b\": 2"));

        String ini = "[database]\nhost=localhost\nport=3306";
        assertEquals("localhost", IniUtil.get(ini, "database", "host"));

        String pem = PemUtil.wrap("DATA", "hello");
        assertEquals("DATA", PemUtil.parse(pem).type());
        assertEquals("hello", PemUtil.decodeToString(pem));

        assertEquals("zh", LanguageTagUtil.parse("zh-CN").language());
        assertEquals("CN", LanguageTagUtil.parse("zh-CN").region());
        UUID v6 = UUID.fromString(IdUtil.uuidV6());
        assertEquals(6, v6.version());
        assertEquals("hello%20%E5%B7%A5%E5%85%B7", EscapeUtil.percent("hello 工具"));
        assertEquals("hello 工具", ZipUtil.unzlibBase64(ZipUtil.zlibBase64("hello 工具")));
        assertEquals("UTF-8", BomUtil.detect(BomUtil.prependUtf8("hi".getBytes(StandardCharsets.UTF_8))));
        assertEquals(3, DateTimeUtil.quarter(LocalDate.of(2026, 9, 17)));
    }
}
