package com.mengzhihua.utils.common;


import java.time.LocalDate;
import java.util.List;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.PermissionsPolicyUtil;
import com.mengzhihua.utils.common.net.ReferrerPolicyUtil;
import com.mengzhihua.utils.common.net.XFrameOptionsUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.IpnUtil;
import com.mengzhihua.utils.common.validate.IsikukoodUtil;
import com.mengzhihua.utils.common.validate.JmbgUtil;
import com.mengzhihua.utils.common.validate.KennitalaUtil;
import com.mengzhihua.utils.common.validate.NitUtil;
import com.mengzhihua.utils.common.validate.TajUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BalticsAlignUtilsTest {

    @Test
    void jmbgIsikukoodKennitala() {
        assertTrue(JmbgUtil.isValid("0101980500005"));
        assertEquals("0101980500005", JmbgUtil.complete("010198050000"));
        assertFalse(JmbgUtil.female("0101980500005"));
        assertEquals(LocalDate.of(1980, 1, 1), JmbgUtil.birthDate("0101980500005"));
        assertTrue(IsikukoodUtil.isValid("37601010003"));
        assertEquals("37601010003", IsikukoodUtil.complete("3760101000"));
        assertFalse(IsikukoodUtil.female("37601010003"));
        assertEquals(LocalDate.of(1976, 1, 1), IsikukoodUtil.birthDate("37601010003"));
        assertTrue(KennitalaUtil.isValid("120174-3399"));
        assertEquals("1201743399", KennitalaUtil.complete("12017433"));
        assertEquals(LocalDate.of(1974, 1, 12), KennitalaUtil.birthDate("120174-3399"));
        assertTrue(RegexUtil.is("jmbg", "0101980500005"));
        assertTrue(RegexUtil.is("isikukood", "37601010003"));
        assertTrue(RegexUtil.is("kennitala", "120174-3399"));
    }

    @Test
    void tajIpnNit() {
        assertTrue(TajUtil.isValid("123456788"));
        assertEquals("123456788", TajUtil.complete("12345678"));
        assertTrue(IpnUtil.isValid("2922000110"));
        assertEquals("2922000110", IpnUtil.complete("292200011"));
        assertFalse(IpnUtil.female("2922000110"));
        assertEquals(LocalDate.of(1980, 1, 1), IpnUtil.birthDate("2922000110"));
        assertTrue(NitUtil.isValid("800197268-4"));
        assertEquals("8001972684", NitUtil.complete("800197268"));
        assertFalse(NitUtil.isValid("800197268-5"));
        assertTrue(RegexUtil.is("taj", "123456788"));
        assertTrue(RegexUtil.is("ipn", "2922000110"));
        assertTrue(RegexUtil.is("nit", "800197268-4"));
    }

    @Test
    void referrerFramePermissionsCrc() {
        assertTrue(ReferrerPolicyUtil.has("strict-origin-when-cross-origin", "strict-origin-when-cross-origin"));
        assertTrue(ReferrerPolicyUtil.known("no-referrer"));
        assertTrue(XFrameOptionsUtil.sameOrigin("SAMEORIGIN"));
        assertTrue(XFrameOptionsUtil.deny("DENY"));
        assertEquals("https://example.com", XFrameOptionsUtil.parse("ALLOW-FROM https://example.com").allowFrom());
        String policy = "geolocation=(), camera=(self)";
        assertTrue(PermissionsPolicyUtil.disabled(policy, "geolocation"));
        assertEquals(List.of("self"), PermissionsPolicyUtil.allowlist(policy, "camera"));
        assertEquals("bb3d", HashUtil.crc16ArcHex("123456789"));
    }
}
