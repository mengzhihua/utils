package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.PriorityUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AtUidUtil;
import com.mengzhihua.utils.common.validate.NlBtwUtil;
import com.mengzhihua.utils.common.validate.SiDdvUtil;
import com.mengzhihua.utils.common.validate.SkDphUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtAlignUtilsTest {

    @Test
    void austriaSlovakiaSloveniaNetherlands() {
        assertTrue(AtUidUtil.isValid("AT U13585627"));
        assertTrue(AtUidUtil.isValid("U10223006"));
        assertEquals("U13585627", AtUidUtil.complete("U1358562"));
        assertEquals("AT U13585627", AtUidUtil.format("U13585627"));
        assertFalse(AtUidUtil.isValid("U13585626"));
        assertTrue(RegexUtil.is("atuid", "ATU13585627"));

        assertTrue(SkDphUtil.isValid("SK 202 274 96 19"));
        assertEquals("2022749619", SkDphUtil.normalize("SK 202 274 96 19"));
        assertEquals("SK 202 274 96 19", SkDphUtil.format("2022749619"));
        assertFalse(SkDphUtil.isValid("SK 202 274 96 18"));
        assertFalse(SkDphUtil.isValid("0122749614"));
        assertTrue(RegexUtil.is("skdph", "2022749619"));

        assertTrue(SiDdvUtil.isValid("SI 5022 3054"));
        assertEquals("50223054", SiDdvUtil.complete("5022305"));
        assertEquals("SI 5022 3054", SiDdvUtil.format("50223054"));
        assertFalse(SiDdvUtil.isValid("SI 50223055"));
        assertTrue(RegexUtil.is("siddv", "50223054"));

        assertTrue(NlBtwUtil.isValid("NL004495445B01"));
        assertTrue(NlBtwUtil.isValid("NL4495445B01"));
        assertTrue(NlBtwUtil.isValid("NL002455799B11"));
        assertEquals("004495445B01", NlBtwUtil.normalize("NL4495445B01"));
        assertFalse(NlBtwUtil.isValid("123456789B90"));
        assertTrue(RegexUtil.is("nlbtw", "004495445B01"));
    }

    @Test
    void priorityCrcDectR() {
        assertTrue(PriorityUtil.isValid("u=1, i"));
        assertEquals(1, PriorityUtil.urgency("u=1, i"));
        assertTrue(PriorityUtil.incremental("u=1, i"));
        assertFalse(PriorityUtil.incremental("u=3"));
        assertFalse(PriorityUtil.isValid("i"));
        assertFalse(PriorityUtil.isValid("u=8"));
        assertEquals("007e", HashUtil.crc16DectRHex("123456789"));
    }
}
