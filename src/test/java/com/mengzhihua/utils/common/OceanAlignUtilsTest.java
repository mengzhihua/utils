package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.CoepUtil;
import com.mengzhihua.utils.common.net.CoopUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.IlHpUtil;
import com.mengzhihua.utils.common.validate.LtJaUtil;
import com.mengzhihua.utils.common.validate.NzbnUtil;
import com.mengzhihua.utils.common.validate.UenUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OceanAlignUtilsTest {

    @Test
    void nzbnUenHp() {
        assertTrue(NzbnUtil.isValid("9429000000000"));
        assertEquals("9429000000000", NzbnUtil.complete("942900000000"));
        assertFalse(NzbnUtil.isValid("9429000000001"));
        assertTrue(UenUtil.isValid("00192200M"));
        assertTrue(UenUtil.isValid("197401143C"));
        assertTrue(UenUtil.isValid("T01FC6132D"));
        assertTrue(UenUtil.isValid("S16FC0121D"));
        assertEquals("T01FC6132D", UenUtil.complete("T01FC6132"));
        assertFalse(UenUtil.isValid("T01FC6132A"));
        assertTrue(IlHpUtil.isValid("516179157"));
        assertEquals("516179157", IlHpUtil.complete("51617915"));
        assertFalse(IlHpUtil.isValid("516179150"));
        assertTrue(RegexUtil.is("nzbn", "9429000000000"));
        assertTrue(RegexUtil.is("uen", "T01FC6132D"));
        assertTrue(RegexUtil.is("ilhp", "516179157"));
    }

    @Test
    void ltJaCoopCrc() {
        assertTrue(LtJaUtil.isValid("119511515"));
        assertEquals("119511515", LtJaUtil.complete("11951151"));
        assertFalse(LtJaUtil.isValid("119511516"));
        assertEquals("same-origin", CoopUtil.parse("same-origin"));
        assertTrue(CoopUtil.sameOrigin("same-origin"));
        assertTrue(CoepUtil.requireCorp("require-corp"));
        assertTrue(CoepUtil.known("credentialless"));
        assertTrue(RegexUtil.is("ltja", "119511515"));
        assertEquals("340bc6d9", HashUtil.crc32JamcrcHex("123456789"));
    }
}
