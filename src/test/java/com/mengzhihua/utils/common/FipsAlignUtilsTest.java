package com.mengzhihua.utils.common;


import java.util.List;

import com.mengzhihua.utils.common.crypto.ShakeUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.net.HttpRangeUtil;
import com.mengzhihua.utils.common.text.ColognePhoneticUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.text.TextUtil;
import com.mengzhihua.utils.common.validate.IsmnUtil;
import com.mengzhihua.utils.common.validate.NpiUtil;
import com.mengzhihua.utils.common.validate.NricUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FipsAlignUtilsTest {

    @Test
    void shakeFips202() {
        assertEquals("7f9c2ba4e88f827d616045507605853ed73b8093f6efbc88eb1a6eacfa66ef26",
                ShakeUtil.shake128(""));
        assertEquals("5881092dd818bf5cf8a3ddb793fbcba74097d5c526a6d35f97b83351940f2cc8",
                ShakeUtil.shake128("abc"));
        assertEquals("46b9dd2b0ba88d13233b3feb743eeb243fcd52ea62b81b82b50c27646ed5762fd75dc4ddd8c0f200cb05019d67b592f6fc821c49479ab48640292eacb3b7c4be",
                ShakeUtil.shake256(""));
        assertEquals("483366601360a8771c6863080cc4114d8db44530f8f1e1ee4f94ea37e78b5739d5a15bef186a5386c75744c0527e1faa9f8726e462a12a4feb06bd8801e751e4",
                ShakeUtil.shake256("abc"));
    }

    @Test
    void npiIsmnNric() {
        assertTrue(NpiUtil.isValid("1234567893"));
        assertEquals('3', NpiUtil.checkDigit("123456789"));
        assertFalse(NpiUtil.isValid("1234567890"));
        assertTrue(IsmnUtil.isValid("979-0-2600-0043-8"));
        assertEquals("9790260000438", IsmnUtil.complete("979026000043"));
        assertFalse(IsmnUtil.isValid("9790260000430"));
        assertTrue(NricUtil.isValid("S1234567D"));
        assertEquals('D', NricUtil.checkLetter("S1234567"));
        assertFalse(NricUtil.isValid("S1234567A"));
        assertTrue(RegexUtil.is("npi", "1234567893"));
        assertTrue(RegexUtil.is("ismn", "979-0-2600-0043-8"));
        assertTrue(RegexUtil.is("nric", "S1234567D"));
    }

    @Test
    void cologneHammingRangeUuidV8() {
        assertEquals("657", ColognePhoneticUtil.encode("Müller"));
        assertEquals("862", ColognePhoneticUtil.encode("Schmidt"));
        assertEquals(3, TextUtil.hamming("karolin", "kathrin"));
        List<HttpRangeUtil.ByteRange> ranges = HttpRangeUtil.parse("bytes=0-499,500-999");
        assertEquals(2, ranges.size());
        assertEquals(0L, ranges.get(0).start());
        assertEquals(499L, ranges.get(0).end());
        assertTrue(HttpRangeUtil.parse("bytes=-500").get(0).suffix());
        String uuid = IdUtil.uuidV8();
        assertEquals('8', uuid.charAt(14));
    }
}
