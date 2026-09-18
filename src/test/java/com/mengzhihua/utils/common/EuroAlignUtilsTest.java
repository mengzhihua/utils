package com.mengzhihua.utils.common;


import java.util.List;
import java.util.Map;

import com.mengzhihua.utils.common.codec.Base91Util;
import com.mengzhihua.utils.common.codec.BencodeUtil;
import com.mengzhihua.utils.common.net.HttpAcceptUtil;
import com.mengzhihua.utils.common.text.DoubleMetaphoneUtil;
import com.mengzhihua.utils.common.text.MatchRatingUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.IsniUtil;
import com.mengzhihua.utils.common.validate.NifUtil;
import com.mengzhihua.utils.common.validate.SirenUtil;
import com.mengzhihua.utils.common.validate.SiretUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EuroAlignUtilsTest {

    @Test
    void doubleMetaphoneMatchRating() {
        assertEquals("FLP", DoubleMetaphoneUtil.encode("Philip"));
        assertEquals("FLP", DoubleMetaphoneUtil.encode("Phillip"));
        assertEquals("SM0", DoubleMetaphoneUtil.encode("Smith"));
        assertEquals("XMT", DoubleMetaphoneUtil.encode("Schmidt"));
        assertTrue(DoubleMetaphoneUtil.similar("Smith", "Schmidt"));
        assertEquals("SMTH", MatchRatingUtil.encode("Smith"));
        assertTrue(MatchRatingUtil.similar("Smith", "Smyth"));
        assertEquals("BYRN", MatchRatingUtil.encode("Byrne"));
    }

    @Test
    void sirenSiretNifIsni() {
        assertTrue(SirenUtil.isValid("732829320"));
        assertEquals("732829320", SirenUtil.complete("73282932"));
        assertFalse(SirenUtil.isValid("732829321"));
        assertTrue(SiretUtil.isValid("73282932000074"));
        assertEquals("732829320", SiretUtil.siren("73282932000074"));
        assertTrue(NifUtil.isValid("12345678Z"));
        assertEquals("12345678Z", NifUtil.complete("12345678"));
        assertTrue(NifUtil.isValid("X1234567L"));
        assertFalse(NifUtil.isValid("12345678A"));
        assertTrue(IsniUtil.isValid("0000 0001 2146 358X"));
        assertEquals("000000012146358X", IsniUtil.complete("000000012146358"));
        assertTrue(RegexUtil.is("siren", "732829320"));
        assertTrue(RegexUtil.is("siret", "73282932000074"));
        assertTrue(RegexUtil.is("nif", "12345678Z"));
        assertTrue(RegexUtil.is("isni", "0000 0001 2146 358X"));
    }

    @Test
    void base91BencodeAccept() {
        assertEquals("Hello World", Base91Util.decodeToString(Base91Util.encode("Hello World")));
        assertEquals("", Base91Util.encode(""));
        assertEquals("4:spam", BencodeUtil.encode("spam"));
        assertEquals("spam", BencodeUtil.decode("4:spam"));
        assertEquals("i3e", BencodeUtil.encode(3));
        assertEquals(3L, BencodeUtil.decode("i3e"));
        assertEquals("l4:spam4:eggse", BencodeUtil.encode(List.of("spam", "eggs")));
        assertEquals("d3:bar4:spam3:fooi3ee", BencodeUtil.encode(Map.of("foo", 3, "bar", "spam")));
        List<HttpAcceptUtil.MediaRange> ranges = HttpAcceptUtil.parse("text/html,application/json;q=0.9,*/*;q=0.8");
        assertEquals("text/html", ranges.get(0).type());
        assertEquals(1.0, ranges.get(0).q());
        assertEquals("application/json", ranges.get(1).type());
        assertEquals("text/html", HttpAcceptUtil.negotiate(
                "text/html,application/json;q=0.9", "application/json", "text/html"));
    }
}
