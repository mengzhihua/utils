package com.mengzhihua.utils.common;


import java.time.LocalDate;
import java.util.Map;

import com.mengzhihua.utils.common.codec.Base92Util;
import com.mengzhihua.utils.common.net.StructuredFieldUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.BsnUtil;
import com.mengzhihua.utils.common.validate.RodneCisloUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WestAlignUtilsTest {

    @Test
    void bsnRodne() {
        assertTrue(BsnUtil.isValid("111222333"));
        assertTrue(BsnUtil.isValid("123456782"));
        assertFalse(BsnUtil.isValid("123456789"));
        assertEquals("111222333", BsnUtil.complete("11122233"));
        assertTrue(RodneCisloUtil.isValid("680101/0007"));
        assertEquals("6801010007", RodneCisloUtil.complete("680101000"));
        assertFalse(RodneCisloUtil.female("680101/0007"));
        assertEquals(LocalDate.of(1968, 1, 1), RodneCisloUtil.birthDate("680101/0007"));
        assertTrue(RegexUtil.is("bsn", "111222333"));
        assertTrue(RegexUtil.is("rodne", "680101/0007"));
    }

    @Test
    void base92StructuredFields() {
        assertEquals("Q2Aeq)", Base92Util.encode("Hello"));
        assertEquals("Hello", Base92Util.decodeToString("Q2Aeq)"));
        Map<String, Object> dict = StructuredFieldUtil.parseDictionary("abc=123, def=?0, title=\"hi\"");
        assertEquals(123L, dict.get("abc"));
        assertEquals(Boolean.FALSE, dict.get("def"));
        assertEquals("hi", dict.get("title"));
        assertTrue(StructuredFieldUtil.has("abc=123, def=?0", "def"));
    }
}
