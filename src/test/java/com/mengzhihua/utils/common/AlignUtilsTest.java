package com.mengzhihua.utils.common;


import java.time.LocalDate;
import java.util.List;

import com.mengzhihua.utils.common.codec.Base32Util;
import com.mengzhihua.utils.common.codec.RadixUtil;
import com.mengzhihua.utils.common.crypto.BasicAuthUtil;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.Pbkdf2Util;
import com.mengzhihua.utils.common.extra.CaptchaUtil;
import com.mengzhihua.utils.common.id.ObjectIdUtil;
import com.mengzhihua.utils.common.json.JsonPathUtil;
import com.mengzhihua.utils.common.lang.CharUtil;
import com.mengzhihua.utils.common.lang.CollectionUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.math.ExprUtil;
import com.mengzhihua.utils.common.net.IdnUtil;
import com.mengzhihua.utils.common.text.UnicodeUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import com.mengzhihua.utils.common.time.LunarUtil;
import com.mengzhihua.utils.common.validate.IdCardUtil;
import com.mengzhihua.utils.common.validate.IsbnUtil;
import com.mengzhihua.utils.common.validate.MacUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlignUtilsTest {

    @Test
    void exprUnicodeRadixBase32() {
        assertEquals("7", ExprUtil.evalPlain("1+2*3"));
        assertEquals("9", ExprUtil.evalPlain("(1+2)*3"));
        assertEquals("10", ExprUtil.evalPlain("2.5*4"));
        assertEquals("\\u5de5\\u5177", UnicodeUtil.toUnicode("工具"));
        assertEquals("工具", UnicodeUtil.fromUnicode("\\u5de5\\u5177"));
        assertEquals("ff", RadixUtil.convert("255", 10, 16));
        assertEquals(255L, RadixUtil.parse("ff", 16));
        String encoded = Base32Util.encode("hello");
        assertEquals("NBSWY3DP", encoded);
        assertEquals("hello", Base32Util.decodeToString(encoded));
    }

    @Test
    void objectIdPbkdf2IdnJsonPath() {
        String id = ObjectIdUtil.next();
        assertTrue(ObjectIdUtil.isValid(id));
        assertNotNull(ObjectIdUtil.timestamp(id));
        String hash = Pbkdf2Util.hash("secret-pass");
        assertTrue(Pbkdf2Util.matches("secret-pass", hash));
        assertFalse(Pbkdf2Util.matches("wrong", hash));
        String ascii = IdnUtil.toAscii("清华大学.cn");
        assertTrue(ascii.startsWith("xn--"));
        assertEquals("清华大学.cn", IdnUtil.toUnicode(ascii));
        assertEquals("Ada", JsonPathUtil.getStr("{\"user\":{\"name\":\"Ada\"}}", "user.name"));
        assertTrue(JsonPathUtil.exists("{\"user\":{\"name\":\"Ada\"}}", "/user/name"));
    }

    @Test
    void lunarIsbnMacIdCardCaptcha() {
        LunarUtil.Lunar newYear = LunarUtil.of("2024-02-10");
        assertEquals(1, newYear.month());
        assertEquals(1, newYear.day());
        assertFalse(newYear.leap());
        assertEquals("龙", newYear.animal());
        assertTrue(newYear.display().contains("正月初一"));
        assertEquals(1, DateTimeUtil.quarter(LocalDate.of(2024, 2, 10)));

        assertTrue(IsbnUtil.isValid("9780306406157"));
        assertTrue(IsbnUtil.isValid("0-306-40615-2"));
        assertEquals("00:1a:2b:3c:4d:5e", MacUtil.normalize("00-1A-2B-3C-4D-5E"));
        assertEquals("110101199003078937", IdCardUtil.convert15To18("110101900307893"));
        assertTrue(IdCardUtil.isValid("110101900307893"));

        CaptchaUtil.ImageCaptcha captcha = CaptchaUtil.create();
        assertEquals(4, captcha.code().length());
        assertTrue(captcha.dataUrl().startsWith("data:image/png;base64,"));
        assertTrue(CaptchaUtil.matches(captcha.code(), captcha.code()));

        assertEquals(List.of("a", "b", "c"), CollectionUtil.union(List.of("a", "b"), List.of("b", "c")));
        assertEquals(List.of("b"), CollectionUtil.intersection(List.of("a", "b"), List.of("b", "c")));
        assertEquals("hello…", StringUtil.brief("hello world", 6));
        assertEquals("001", StringUtil.pad("1", 3, '0'));
        assertTrue(CharUtil.isChinese('工'));
        String header = BasicAuthUtil.header("ada", "secret");
        assertEquals("ada", BasicAuthUtil.parse(header)[0]);
        assertNotNull(HashUtil.fnv1a32("hello"));
        assertTrue(HashUtil.crc16("hello") > 0);
    }
}
