package com.mengzhihua.utils.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexUtilsTest {

    @Test
    void commonFormats() {
        assertTrue(RegexUtil.is("hexcolor", "#0f766e"));
        assertTrue(RegexUtil.isHexColor("#0F7"));
        assertTrue(RegexUtil.isDate("2026-09-17"));
        assertFalse(RegexUtil.isDate("2026-13-01"));
        assertTrue(RegexUtil.isTime("09:30:00"));
        assertTrue(RegexUtil.isChinese("工具"));
        assertTrue(RegexUtil.isChineseName("张三"));
        assertFalse(RegexUtil.isChinese("Ada"));
        assertTrue(RegexUtil.isDomain("example.com"));
        assertTrue(RegexUtil.isMoney("12.30"));
        assertFalse(RegexUtil.isMoney("12.345"));
        assertTrue(RegexUtil.isWechat("wxid_hello"));
        assertTrue(RegexUtil.isStrongPassword("Abcd1234"));
        assertFalse(RegexUtil.isStrongPassword("abcdef"));
        assertTrue(RegexUtil.isJwt("aaa.bbb.ccc"));
        assertTrue(RegexUtil.isCidr("172.16.0.0/24"));
        assertTrue(RegexUtil.isSemver("1.2.3"));
        assertTrue(RegexUtil.is("md5", "d41d8cd98f00b204e9800998ecf8427e"));
        assertTrue(RegexUtil.types().contains("mobile"));
        assertThrows(IllegalArgumentException.class, () -> RegexUtil.is("unknown", "x"));
    }

    @Test
    void extractFromText() {
        String text = "联系 Ada ada@example.com 电话 13812345678 打开 https://example.com 颜色 #0F766E 日期 2026-09-17";
        assertEquals(List.of("13812345678"), ReUtil.extractMobiles(text));
        assertEquals(List.of("ada@example.com"), ReUtil.extractEmails(text));
        assertEquals(List.of("https://example.com"), ReUtil.extractUrls(text));
        assertEquals(List.of("2026-09-17"), ReUtil.extractDates(text));
        assertEquals(List.of("#0F766E"), ReUtil.extractHexColors(text));
    }

    @Test
    void reUtilNamedGroupsSplitReplace() {
        assertFalse(ReUtil.isValid("["));
        assertTrue(ReUtil.isValid("\\d+"));
        assertEquals(List.of("a", "b"), ReUtil.split(",", "a,b"));
        assertEquals("ab*cd34", ReUtil.replaceFirst("ab12cd34", "\\d+", "*"));
        assertEquals("ab*cd*", ReUtil.replaceAll("ab12cd34", "\\d+", "*"));
        Map<String, String> named = ReUtil.getNamedGroups("(?<area>\\d{3})-(?<local>\\d{4})", "010-1234 extra");
        assertEquals("010", named.get("area"));
        assertEquals("1234", named.get("local"));
        assertArrayEquals(new String[]{"12", "12"}, ReUtil.getAllGroups("(\\d+)", "ab12cd34"));
        assertEquals(2, ReUtil.findAllGroups("\\d+", "ab12cd34").size());
    }
}
