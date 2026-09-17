package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.codec.Base64Util;
import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.id.KsuidUtil;
import com.mengzhihua.utils.common.id.TypeIdUtil;
import com.mengzhihua.utils.common.lang.CompareUtil;
import com.mengzhihua.utils.common.lang.RangeUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.net.HostAndPortUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.text.SimHashUtil;
import com.mengzhihua.utils.common.text.SoundexUtil;
import com.mengzhihua.utils.common.text.TextUtil;
import com.mengzhihua.utils.common.text.WordUtil;
import com.mengzhihua.utils.common.time.AgeUtil;
import com.mengzhihua.utils.common.validate.EanUtil;
import com.mengzhihua.utils.common.validate.IbanUtil;
import com.mengzhihua.utils.common.validate.IssnUtil;
import com.mengzhihua.utils.common.validate.VinUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenSourceUtilsTest {

    @Test
    void jaroSoundexWordString() {
        assertEquals(0.9611, TextUtil.jaroWinkler("MARTHA", "MARHTA"), 0.001);
        assertEquals("MAR", TextUtil.longestCommonSubsequence("MARTHA", "MARHTA").substring(0, 3));
        assertEquals(1D, TextUtil.jaccard("hello", "hello"));
        assertTrue(TextUtil.jaccard("hello", "hallo") > 0.3);
        assertEquals(3, TextUtil.hamming("karolin", "kathrin"));
        assertEquals("R163", SoundexUtil.encode("Robert"));
        assertEquals("R163", SoundexUtil.encode("Rupert"));
        assertTrue(SoundexUtil.similar("Robert", "Rupert"));
        assertEquals("SBU", WordUtil.initials("spring boot utils"));
        assertEquals("Spring Boot", WordUtil.capitalizeFully("spring BOOT"));
        assertEquals("abc", StringUtil.commonPrefix("abcdef", "abcxyz"));
        assertEquals("xyz", StringUtil.difference("abc", "abcxyz"));
        assertEquals("aBC", StringUtil.swapCase("Abc"));
        assertEquals(3, StringUtil.countMatches("ababab", "ab"));
        assertEquals("cab", StringUtil.rotate("abc", 1));
    }

    @Test
    void ibanVinIssnEan() {
        assertTrue(IbanUtil.isValid("GB82WEST12345698765432"));
        assertTrue(IbanUtil.isValid("DE89 3704 0044 0532 0130 00"));
        assertFalse(IbanUtil.isValid("GB82WEST12345698765433"));
        assertTrue(RegexUtil.isIban("GB82WEST12345698765432"));

        assertTrue(VinUtil.isValid("1M8GDM9AXKP042788"));
        assertFalse(VinUtil.isValid("1M8GDM9A0KP042788"));
        assertTrue(RegexUtil.isVin("1M8GDM9AXKP042788"));

        assertTrue(IssnUtil.isValid("0317-8471"));
        assertTrue(RegexUtil.isIssn("0317-8471"));

        assertTrue(EanUtil.isValid("5901234123457"));
        assertTrue(RegexUtil.isEan("5901234123457"));
    }

    @Test
    void ageRangeHostBase64KsuidDigest() {
        LocalDate birthday = LocalDate.of(2000, 1, 1);
        assertEquals(26, AgeUtil.age(birthday, LocalDate.of(2026, 9, 17)));
        assertEquals(LocalDate.of(2027, 1, 1), AgeUtil.nextBirthday(birthday, LocalDate.of(2026, 9, 17)));
        assertTrue(AgeUtil.isBirthday(birthday, LocalDate.of(2026, 1, 1)));

        assertTrue(RangeUtil.closed(1, 10).contains(5));
        assertFalse(RangeUtil.open(1, 10).contains(10));
        assertEquals("a", CompareUtil.min("c", "a", "b"));
        assertEquals("c", CompareUtil.max(null, "c", "a"));

        var parsed = HostAndPortUtil.parse("example.com:8443");
        assertEquals("example.com", parsed.host());
        assertEquals(8443, parsed.port());
        assertEquals("[::1]:443", HostAndPortUtil.parse("[::1]:443").toString());

        String encoded = Base64Util.encodeUrl("hello 工具");
        assertEquals("hello 工具", Base64Util.decodeToString(encoded));
        assertTrue(Base64Util.isBase64(Base64Util.encode("hello")));

        String ksuid = KsuidUtil.next();
        assertTrue(KsuidUtil.isValid(ksuid));
        assertEquals(27, ksuid.length());
        assertTrue(KsuidUtil.timestamp(ksuid) > 1_400_000_000L);
        String typeId = TypeIdUtil.next("user");
        assertTrue(TypeIdUtil.isValid(typeId));
        assertEquals("user", TypeIdUtil.prefix(typeId));

        assertEquals("3338be694f50c5f338814986cdf0686453a888b84f424d792af4b9202398f392", EncryptUtil.sha3_256("hello"));
        assertTrue(SimHashUtil.similar("the cat sat", "the cat sat on", 16));
    }
}
