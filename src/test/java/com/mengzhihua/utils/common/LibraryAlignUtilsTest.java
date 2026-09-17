package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.codec.Bech32Util;
import com.mengzhihua.utils.common.codec.HexUtil;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.HkdfUtil;
import com.mengzhihua.utils.common.id.Cuid2Util;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.id.SqidsUtil;
import com.mengzhihua.utils.common.io.ZipUtil;
import com.mengzhihua.utils.common.lang.BitUtil;
import com.mengzhihua.utils.common.net.EmailUtil;
import com.mengzhihua.utils.common.net.MediaTypeUtil;
import com.mengzhihua.utils.common.text.CaseFormatUtil;
import com.mengzhihua.utils.common.text.HumanizeUtil;
import com.mengzhihua.utils.common.text.InflectorUtil;
import com.mengzhihua.utils.common.text.MetaphoneUtil;
import com.mengzhihua.utils.common.text.MorseUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.text.RotUtil;
import com.mengzhihua.utils.common.text.TextUtil;
import com.mengzhihua.utils.common.text.WildcardUtil;
import com.mengzhihua.utils.common.validate.BicUtil;
import com.mengzhihua.utils.common.validate.CheckDigitUtil;
import com.mengzhihua.utils.common.validate.IsinUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryAlignUtilsTest {

    @Test
    void sqidsRoundTrip() {
        String encoded = SqidsUtil.encode(1, 2, 3);
        assertEquals("86Rf07", encoded);
        assertArrayEquals(new long[]{1, 2, 3}, SqidsUtil.decode(encoded));
        assertEquals(42L, SqidsUtil.decodeOne(SqidsUtil.encode(42)));
    }

    @Test
    void uuidV3V5NanoCuid() {
        assertEquals("5df41881-3aed-3515-88a7-2f4a814cf09e", IdUtil.uuidV3("www.example.com"));
        assertEquals("886313e1-3b8a-5372-9b90-0c9aee199e5d", IdUtil.uuidV5("python.org"));
        assertEquals(21, IdUtil.nanoId().length());
        String cuid = Cuid2Util.next();
        assertTrue(Cuid2Util.isValid(cuid));
        assertEquals(24, cuid.length());
    }

    @Test
    void metaphoneIbanStylePhonetics() {
        assertTrue(MetaphoneUtil.similar("Philip", "Phillip"));
        assertEquals("N0", MetaphoneUtil.encode("KNUTH"));
        assertEquals("FLP", MetaphoneUtil.encode("Philip"));
    }

    @Test
    void isinBicEmail() {
        assertTrue(IsinUtil.isValid("US0378331005"));
        assertFalse(IsinUtil.isValid("US0378331006"));
        assertTrue(RegexUtil.isIsin("US0378331005"));
        assertTrue(BicUtil.isValid("DEUTDEFF"));
        assertTrue(BicUtil.isValid("DEUTDEFF500"));
        assertFalse(BicUtil.isValid("DEUTDE0F"));
        assertTrue(RegexUtil.isBic("DEUTDEFF"));
        var email = EmailUtil.parse("Ada+dev@Example.com");
        assertEquals("Ada+dev", email.local());
        assertEquals("Ada", email.localBase());
        assertEquals("dev", email.plusTag());
        assertEquals("example.com", email.domain());
        assertEquals("ada@example.com", email.normalized());
    }

    @Test
    void caseFormatMediaType() {
        assertEquals("TEST_ME", CaseFormatUtil.toUpperUnderscore("testMe"));
        assertEquals("test-me", CaseFormatUtil.toLowerHyphen("testMe"));
        assertEquals("testMe", CaseFormatUtil.toLowerCamel("TEST_ME"));
        assertEquals("TestMe", CaseFormatUtil.toUpperCamel("test-me"));
        var media = MediaTypeUtil.parse("application/json; charset=UTF-8");
        assertEquals("application", media.type());
        assertEquals("json", media.subtype());
        assertEquals("UTF-8", media.parameter("charset"));
        assertTrue(MediaTypeUtil.isJson("application/json"));
    }

    @Test
    void morseRotWildcardHumanizeInflector() {
        assertEquals("... --- ...", MorseUtil.encode("SOS"));
        assertEquals("SOS", MorseUtil.decode("... --- ..."));
        assertEquals("Uryyb", RotUtil.rot13("Hello"));
        assertEquals("Hello", RotUtil.rot13(RotUtil.rot13("Hello")));
        assertTrue(WildcardUtil.match("Foo.java", "*.java"));
        assertTrue(WildcardUtil.match("Foo.java", "F??.java"));
        assertFalse(WildcardUtil.match("Foo.java", "*.txt"));
        assertEquals("1.2K", HumanizeUtil.compact(1234));
        assertEquals("1.5M", HumanizeUtil.compact(1_500_000));
        assertEquals("1st", HumanizeUtil.ordinal(1));
        assertEquals("11th", HumanizeUtil.ordinal(11));
        assertEquals("21st", HumanizeUtil.ordinal(21));
        assertEquals("cities", InflectorUtil.pluralize("city"));
        assertEquals("boxes", InflectorUtil.pluralize("box"));
        assertEquals("children", InflectorUtil.pluralize("child"));
        assertEquals("city", InflectorUtil.singularize("cities"));
        assertEquals(2, BitUtil.count(BitUtil.set(0b0010, 0)));
    }

    @Test
    void checkDigitCrcGzipBech32Hkdf() {
        assertTrue(CheckDigitUtil.luhn("79927398713"));
        assertFalse(CheckDigitUtil.luhn("79927398714"));
        String body = "123456789";
        assertTrue(CheckDigitUtil.verhoeff(body + CheckDigitUtil.verhoeffCheckDigit(body)));
        assertTrue(CheckDigitUtil.damm("5724"));
        assertEquals("e3069283", HashUtil.crc32cHex("123456789"));

        String gzipped = ZipUtil.gzipBase64("hello 工具");
        assertEquals("hello 工具", ZipUtil.ungzipBase64(gzipped));

        String bech = Bech32Util.encodeText("xyz", "hello");
        assertEquals("hello", Bech32Util.decodeToString(bech));
        assertEquals("bc", Bech32Util.decode("bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4").hrp());

        byte[] okm = HkdfUtil.derive(
                HexUtil.decode("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b"),
                HexUtil.decode("000102030405060708090a0b0c"),
                HexUtil.decode("f0f1f2f3f4f5f6f7f8f9"),
                42
        );
        assertEquals("3cb25f25faacd57a90434f64d0362f2a2d2d0a90cf1a5a4c5db02d56ecc4c5bf34007208d5b887185865",
                HexUtil.encode(okm));
    }

    @Test
    void textDiceCosineDamerauFuzzy() {
        assertEquals(1, TextUtil.damerauLevenshtein("ca", "ac"));
        assertEquals(0.25, TextUtil.dice("night", "nacht"), 0.001);
        assertEquals(1D, TextUtil.cosine("hello", "hello"), 1e-9);
        assertTrue(TextUtil.cosine("hello", "hallo") > 0.8);
        assertEquals(22, TextUtil.fuzzyScore("Workshop", "workshop"));
        assertTrue(TextUtil.dice("MARTHA", "MARHTA") > 0.3);
    }
}
