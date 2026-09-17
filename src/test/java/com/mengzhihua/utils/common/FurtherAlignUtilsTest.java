package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.Ripemd160Util;
import com.mengzhihua.utils.common.crypto.X25519Util;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.FigiUtil;
import com.mengzhihua.utils.common.validate.LeiUtil;
import com.mengzhihua.utils.common.validate.NhsNumberUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FurtherAlignUtilsTest {

    @Test
    void x25519Rfc7748() {
        assertEquals("4a5d9d5ba4ce2de1728e3bf480350f25e07e21c947d19e3376f09b3c1e161742",
                X25519Util.sharedSecretHex(
                        "77076d0a7318a57d3c16c17251b26645df4c2f87ebc0992ab177fba51db92c2a",
                        "de9edb7d7b7dc1b4d35b61c2ece435373f8343c85b78674dadfc7e146f882b4f"));
    }

    @Test
    void ripemd160Official() {
        assertEquals("9c1185a5c5e9fc54612808977ee8f548b2258d31", Ripemd160Util.hash(""));
        assertEquals("8eb208f7e05d987a9b044a8e98c6b087f15a0bfc", Ripemd160Util.hash("abc"));
    }

    @Test
    void figiLeiNhs() {
        assertTrue(FigiUtil.isValid("BBG000B9XRY4"));
        assertEquals("4", String.valueOf(FigiUtil.checkDigit("BBG000B9XRY")));
        assertFalse(FigiUtil.isValid("BBG000B9XRY0"));
        assertTrue(LeiUtil.isValid("5493001KJTIIGC8Y1R12"));
        assertEquals("12", LeiUtil.checkDigits("5493001KJTIIGC8Y1R"));
        assertFalse(LeiUtil.isValid("5493001KJTIIGC8Y1R13"));
        assertTrue(NhsNumberUtil.isValid("943 476 5919"));
        assertEquals(9, NhsNumberUtil.checkDigit("943476591"));
        assertFalse(NhsNumberUtil.isValid("9434765910"));
        assertTrue(RegexUtil.is("figi", "BBG000B9XRY4"));
        assertTrue(RegexUtil.is("lei", "5493001KJTIIGC8Y1R12"));
        assertTrue(RegexUtil.is("nhs", "9434765919"));
    }
}
