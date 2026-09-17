package com.mengzhihua.utils.common;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.Sm3Util;
import com.mengzhihua.utils.common.lang.CollectionUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.math.SemverUtil;
import com.mengzhihua.utils.common.net.Ipv6Util;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import com.mengzhihua.utils.common.validate.BankCardUtil;
import com.mengzhihua.utils.common.validate.IsbnUtil;
import com.mengzhihua.utils.common.validate.PhoneUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptimizeAlignUtilsTest {

    @Test
    void sm3OfficialVector() {
        assertEquals("66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0", Sm3Util.hash("abc"));
        assertEquals(Sm3Util.hash("abc"), EncryptUtil.sm3("abc"));
        assertEquals("1ab21d8355cfa17f8e61194831e81a8f22bec8c728fefb747ed035eb5082aa2b", Sm3Util.hash(""));
    }

    @Test
    void isbnConvertAndCrc16() {
        assertEquals("9780306406157", IsbnUtil.toIsbn13("0-306-40615-2"));
        assertEquals("0306406152", IsbnUtil.toIsbn10("9780306406157"));
        assertEquals("4b37", HashUtil.crc16Hex("123456789"));
        assertEquals("29b1", HashUtil.crc16CcittHex("123456789"));
    }

    @Test
    void ipv6ExpandCompress() {
        assertTrue(Ipv6Util.isValid("2001:db8::1"));
        assertTrue(RegexUtil.isIpv6("::1"));
        assertEquals("2001:0db8:0000:0000:0000:0000:0000:0001", Ipv6Util.expand("2001:db8::1"));
        assertEquals("2001:db8::1", Ipv6Util.compress(Ipv6Util.expand("2001:db8::1")));
        assertEquals("::1", Ipv6Util.compress(Ipv6Util.expand("::1")));
        assertTrue(Ipv6Util.isValid("::ffff:192.168.1.1"));
        assertFalse(Ipv6Util.isValid("192.168.1.1"));
        assertFalse(Ipv6Util.isValid(":::1"));
    }

    @Test
    void semverOrder() {
        assertTrue(SemverUtil.compare("1.0.0-alpha", "1.0.0") < 0);
        assertTrue(SemverUtil.compare("1.0.0-alpha.1", "1.0.0-alpha.beta") < 0);
        assertTrue(SemverUtil.compare("1.0.0-beta.2", "1.0.0-beta.11") < 0);
        assertEquals(0, SemverUtil.compare("1.0.0+build", "1.0.0"));
        assertTrue(SemverUtil.isValid("1.2.3-rc.1+001"));
        assertFalse(SemverUtil.isValid("01.0.0"));
    }

    @Test
    void stringBetweenAndPhoneBank() {
        assertEquals("Ada", StringUtil.subBetween("name=<Ada>", "<", ">"));
        assertEquals(List.of("Ada", "18"), StringUtil.subBetweenAll("name=<Ada> age=<18>", "<", ">"));
        assertEquals("hello...", StringUtil.abbreviate("hello world", 8));
        assertEquals("ab  ", StringUtil.padEnd("ab", 4, ' '));
        assertEquals("a b", StringUtil.normalizeSpace("  a   b  "));
        assertEquals("[Ada]", StringUtil.wrap("Ada", "[", "]"));
        assertEquals("Ada", StringUtil.unWrap("[Ada]", "[", "]"));

        assertEquals("2天3小时5分钟", DateTimeUtil.formatBetween(Duration.ofSeconds(183900)));
        assertEquals("0秒", DateTimeUtil.formatBetween(Duration.ZERO));
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        assertEquals("1天", DateTimeUtil.formatBetween(start, start.plusDays(1)));

        assertEquals("HK", PhoneUtil.region("51234567"));
        assertTrue(PhoneUtil.isMobileHk("+852 51234567"));
        assertTrue(PhoneUtil.isMobileTw("0912345678"));
        assertTrue(PhoneUtil.isMobileMo("66123456"));
        assertTrue(PhoneUtil.isTel400("4001234567"));
        assertEquals("UnionPay", BankCardUtil.brand("6222021234567890"));
        assertEquals("Visa", BankCardUtil.brand("4111111111111111"));
        assertEquals(List.of("a", "c"), CollectionUtil.disjunction(List.of("a", "b"), List.of("b", "c")));
        assertTrue(RegexUtil.is("mobilehk", "51234567"));
    }
}
