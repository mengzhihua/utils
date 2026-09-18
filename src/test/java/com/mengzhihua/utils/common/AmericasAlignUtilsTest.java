package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.net.CorsUtil;
import com.mengzhihua.utils.common.net.WwwAuthenticateUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CuitUtil;
import com.mengzhihua.utils.common.validate.IrdUtil;
import com.mengzhihua.utils.common.validate.MyKadUtil;
import com.mengzhihua.utils.common.validate.RutUtil;
import com.mengzhihua.utils.common.validate.SaIdUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmericasAlignUtilsTest {

    @Test
    void rutCuitSaId() {
        assertTrue(RutUtil.isValid("12.345.678-5"));
        assertEquals("123456785", RutUtil.complete("12345678"));
        assertFalse(RutUtil.isValid("12.345.678-4"));
        assertTrue(CuitUtil.isValid("20-12345678-6"));
        assertEquals("20123456786", CuitUtil.complete("2012345678"));
        assertFalse(CuitUtil.isValid("20-12345678-7"));
        assertTrue(SaIdUtil.isValid("8001015009087"));
        assertEquals("8001015009087", SaIdUtil.complete("800101500908"));
        assertFalse(SaIdUtil.female("8001015009087"));
        assertTrue(SaIdUtil.citizen("8001015009087"));
        assertEquals(LocalDate.of(1980, 1, 1), SaIdUtil.birthDate("8001015009087"));
        assertTrue(RegexUtil.is("rut", "12.345.678-5"));
        assertTrue(RegexUtil.is("cuit", "20-12345678-6"));
        assertTrue(RegexUtil.is("said", "8001015009087"));
    }

    @Test
    void irdMyKadCorsAuth() {
        assertTrue(IrdUtil.isValid("49091850"));
        assertEquals("49091850", IrdUtil.complete("4909185"));
        assertFalse(IrdUtil.isValid("49091851"));
        assertTrue(MyKadUtil.isValid("900101-14-5671"));
        assertFalse(MyKadUtil.female("900101-14-5671"));
        assertEquals("14", MyKadUtil.placeCode("900101-14-5671"));
        assertEquals(LocalDate.of(1990, 1, 1), MyKadUtil.birthDate("900101-14-5671"));
        assertTrue(CorsUtil.allowsOrigin("*", "https://a.example"));
        assertTrue(CorsUtil.allowsOrigin("https://example.com", "https://example.com"));
        assertFalse(CorsUtil.allowsOrigin("https://example.com", "https://other.example"));
        assertTrue(CorsUtil.allowsMethod("GET, POST", "get"));
        assertTrue(CorsUtil.allowsHeader("Content-Type, X-Request-Id", "content-type"));
        WwwAuthenticateUtil.Challenge challenge =
                WwwAuthenticateUtil.parse("Bearer realm=\"api\", error=\"invalid_token\"");
        assertEquals("Bearer", challenge.scheme());
        assertEquals("api", challenge.realm());
        assertEquals("invalid_token", challenge.params().get("error"));
        assertTrue(RegexUtil.is("ird", "49091850"));
        assertTrue(RegexUtil.is("mykad", "900101-14-5671"));
    }
}
