package com.mengzhihua.utils.common;


import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HexFormat;

import com.mengzhihua.utils.common.codec.Base32Util;
import com.mengzhihua.utils.common.crypto.Blake2sUtil;
import com.mengzhihua.utils.common.crypto.CmacUtil;
import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.Sm4Util;
import com.mengzhihua.utils.common.lang.ByteUtil;
import com.mengzhihua.utils.common.text.PinyinUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.text.XmlUtil;
import com.mengzhihua.utils.common.time.HolidayUtil;
import com.mengzhihua.utils.common.time.SolarTermUtil;
import com.mengzhihua.utils.common.validate.HkIdUtil;
import com.mengzhihua.utils.common.validate.OrgCodeUtil;
import com.mengzhihua.utils.common.validate.TwIdUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContinueAlignUtilsTest {

    @Test
    void sm4OfficialVectorAndRoundTrip() {
        assertEquals("681edf34d206965e86b3e94f536e4246",
                Sm4Util.encryptEcbHex("0123456789ABCDEFFEDCBA9876543210", "0123456789ABCDEFFEDCBA9876543210"));
        assertEquals("0123456789abcdeffedcba9876543210",
                Sm4Util.decryptEcbHex("0123456789ABCDEFFEDCBA9876543210", "681EDF34D206965E86B3E94F536E4246"));
        String cipher = Sm4Util.encrypt("hello 工具", "secret");
        assertEquals("hello 工具", Sm4Util.decrypt(cipher, "secret"));
        assertEquals("round", EncryptUtil.sm4Decrypt(EncryptUtil.sm4Encrypt("round", "pw"), "pw"));
    }

    @Test
    void blake2sRfc7693() {
        assertEquals("69217a3079908094e11121d042354a7c1f55b6482ca1a51e1b250dfd1cd0c48d", Blake2sUtil.hash(""));
        assertEquals("508c5e8c327c14e2e1a72ba34eeb452f37458b209ed63a294d999b4c86675982", Blake2sUtil.hash("abc"));
    }

    @Test
    void cmacRfc4493() {
        byte[] key = HexFormat.of().parseHex("2b7e151628aed2a6abf7158809cf4f3c");
        assertEquals("bb1d6929e95937287fa37d129b756746", CmacUtil.hex("", "2b7e151628aed2a6abf7158809cf4f3c"));
        byte[] msg16 = HexFormat.of().parseHex("6bc1bee22e409f96e93d7e117393172a");
        assertEquals("070a16b46b4d4144f79bdd9dd04a287c", HexFormat.of().formatHex(CmacUtil.mac(key, msg16)));
    }

    @Test
    void hashCrcFnvCrockfordBytes() {
        assertEquals("cbf29ce484222325", HashUtil.fnv1a64Hex(""));
        assertEquals("85944171f73967e8", HashUtil.fnv1a64Hex("foobar"));
        assertEquals("a1", HashUtil.crc8Hex("123456789"));
        assertEquals("6c40df5f0b497347", HashUtil.crc64Hex("123456789"));
        assertEquals("D1JPRV3F", Base32Util.encodeCrockford("hello"));
        assertEquals("hello", Base32Util.decodeCrockfordToString("D1JPRV3F"));
        assertEquals(0x01020304, ByteUtil.toInt(ByteUtil.fromInt(0x01020304)));
        assertEquals(0x0102030405060708L, ByteUtil.toLong(ByteUtil.fromLong(0x0102030405060708L)));
        assertEquals(1, ByteUtil.indexOf(new byte[]{1, 2, 3, 4}, new byte[]{2, 3}));
    }

    @Test
    void hkTwOrgAndPinyin() {
        assertTrue(HkIdUtil.isValid("A123456(3)"));
        assertTrue(HkIdUtil.isValid("A1234563"));
        assertFalse(HkIdUtil.isValid("A123456(4)"));
        assertTrue(TwIdUtil.isValid("A123456789"));
        assertFalse(TwIdUtil.isValid("A123456788"));
        assertEquals("123456788", OrgCodeUtil.complete("12345678"));
        assertTrue(OrgCodeUtil.isValid("12345678-8"));
        assertTrue(RegexUtil.is("hkid", "A123456(3)"));
        assertTrue(RegexUtil.is("twid", "A123456789"));
        assertTrue(RegexUtil.is("orgcode", "123456788"));
        assertEquals("ZG", PinyinUtil.firstLetters("中国"));
    }

    @Test
    void solarTermQingmingAndXml() {
        assertEquals(LocalDate.of(2026, 4, 5), SolarTermUtil.date(2026, "清明"));
        assertEquals("清明节", HolidayUtil.name(LocalDate.of(2026, 4, 5)));
        assertEquals("Ada", XmlUtil.xpath("<root><n>Ada</n></root>", "/root/n"));
        assertTrue(XmlUtil.pretty("<root><n>Ada</n></root>").contains("<n>Ada</n>"));
    }

    @Test
    void chachaRoundTrip() {
        String cipher = EncryptUtil.chachaEncrypt("hello 工具", "secret");
        assertEquals("hello 工具", EncryptUtil.chachaDecrypt(cipher, "secret"));
        assertEquals(32, Blake2sUtil.digest("abc".getBytes(StandardCharsets.UTF_8)).length);
    }
}
