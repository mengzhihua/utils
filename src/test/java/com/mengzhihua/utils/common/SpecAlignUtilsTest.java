package com.mengzhihua.utils.common;


import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import com.mengzhihua.utils.common.crypto.TotpUtil;
import com.mengzhihua.utils.common.crypto.XxHashUtil;
import com.mengzhihua.utils.common.json.JsonMergePatchUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.net.UriTemplateUtil;
import com.mengzhihua.utils.common.text.AccentUtil;
import com.mengzhihua.utils.common.text.EmojiUtil;
import com.mengzhihua.utils.common.text.EncodedWordUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.time.HttpDateUtil;
import com.mengzhihua.utils.common.validate.CusipUtil;
import com.mengzhihua.utils.common.validate.IsrcUtil;
import com.mengzhihua.utils.common.validate.OrcidUtil;
import com.mengzhihua.utils.common.validate.PlateUtil;
import com.mengzhihua.utils.common.validate.SedolUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecAlignUtilsTest {

    @Test
    void cusipSedolOrcidIsrc() {
        assertTrue(CusipUtil.isValid("037833100"));
        assertEquals('0', CusipUtil.checkDigit("03783310"));
        assertFalse(CusipUtil.isValid("037833101"));
        assertTrue(RegexUtil.isCusip("037833100"));

        assertTrue(SedolUtil.isValid("1234565"));
        assertEquals('5', SedolUtil.checkDigit("123456"));
        assertFalse(SedolUtil.isValid("1234560"));
        assertTrue(RegexUtil.isSedol("1234565"));

        assertTrue(OrcidUtil.isValid("0000-0002-1825-0097"));
        assertEquals("0000-0002-1825-0097", OrcidUtil.format("0000000218250097"));
        assertFalse(OrcidUtil.isValid("0000-0002-1825-0098"));
        assertTrue(RegexUtil.isOrcid("0000-0002-1825-0097"));

        assertTrue(IsrcUtil.isValid("US-S1Z-99-00001"));
        assertEquals("US-S1Z-99-00001", IsrcUtil.format("USS1Z9900001"));
        assertEquals("US", IsrcUtil.country("USS1Z9900001"));
        assertTrue(RegexUtil.isIsrc("USS1Z9900001"));
    }

    @Test
    void jsonMergePatchRfc7396() {
        assertTrue(JsonMergePatchUtil.apply("{\"a\":\"b\"}", "{\"a\":\"c\"}").contains("\"a\":\"c\"")
                || JsonMergePatchUtil.apply("{\"a\":\"b\"}", "{\"a\":\"c\"}").contains("\"a\": \"c\""));
        String removed = JsonMergePatchUtil.apply("{\"a\":\"b\"}", "{\"a\":null}");
        assertTrue(removed.contains("{}") || "{}".equals(removed.replace(" ", "")));
        String nested = JsonMergePatchUtil.apply("{\"a\":{\"b\":\"c\"}}", "{\"a\":{\"b\":\"d\",\"c\":null}}");
        assertTrue(nested.contains("\"b\":\"d\"") || nested.contains("\"b\": \"d\""));
        assertFalse(nested.contains("\"c\""));
    }

    @Test
    void httpDateEmojiAccentPlateUri() {
        assertEquals("Thu, 01 Jan 1970 00:00:00 GMT", HttpDateUtil.formatEpoch());
        assertEquals(0L, HttpDateUtil.parse("Thu, 01 Jan 1970 00:00:00 GMT").toEpochMilli());
        assertEquals(Instant.EPOCH, HttpDateUtil.parse(HttpDateUtil.format(Instant.EPOCH)));

        assertTrue(EmojiUtil.contains("hello 😀"));
        assertFalse(EmojiUtil.remove("hello 😀").contains("😀"));
        assertEquals(1, EmojiUtil.count("hello 😀"));

        assertEquals("cafe", AccentUtil.strip("café"));
        assertEquals("naive", AccentUtil.strip("naïve"));
        assertEquals("cafe", StringUtil.stripAccents("café"));

        assertTrue(PlateUtil.isValid("京A12345"));
        assertEquals("京", PlateUtil.province("京A12345"));
        assertFalse(PlateUtil.isNewEnergy("京A12345"));
        assertFalse(PlateUtil.isValid("XX12345"));

        assertEquals("/users/42", UriTemplateUtil.expand("/users/{id}", Map.of("id", 42)));
        assertEquals("42", UriTemplateUtil.match("/users/{id}", "/users/42").get("id"));
        assertTrue(UriTemplateUtil.match("/users/{id}", "/orders/42").isEmpty());
    }

    @Test
    void totpRfc6238AndEncodedWordXxhash64() {
        byte[] key = "12345678901234567890".getBytes(StandardCharsets.US_ASCII);
        assertEquals("94287082", TotpUtil.generate(key, 59, 30, 8, "HmacSHA1"));
        assertEquals("07081804", TotpUtil.generate(key, 1111111109L, 30, 8, "HmacSHA1"));
        assertEquals("14050471", TotpUtil.generate(key, 1111111111L, 30, 8, "HmacSHA1"));

        String encoded = EncodedWordUtil.encode("工具");
        assertTrue(encoded.startsWith("=?UTF-8?B?"));
        assertEquals("工具", EncodedWordUtil.decode(encoded));

        assertEquals("ef46db3751d8e999", String.format("%016x", XxHashUtil.hash64(new byte[0], 0)));
    }
}
