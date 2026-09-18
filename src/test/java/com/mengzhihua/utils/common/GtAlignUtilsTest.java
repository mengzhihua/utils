package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ServerTimingUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CrCpfUtil;
import com.mengzhihua.utils.common.validate.CrCpjUtil;
import com.mengzhihua.utils.common.validate.GtNitUtil;
import com.mengzhihua.utils.common.validate.TnMfUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GtAlignUtilsTest {

    @Test
    void gtNitAndCostaRica() {
        assertTrue(GtNitUtil.isValid("576937-K"));
        assertTrue(GtNitUtil.isValid("7108-0"));
        assertEquals("576937K", GtNitUtil.complete("576937"));
        assertEquals("576937-K", GtNitUtil.format("576937K"));
        assertFalse(GtNitUtil.isValid("8977112-0"));
        assertTrue(RegexUtil.is("gtnit", "576937-K"));

        assertTrue(CrCpfUtil.isValid("3-0455-0175"));
        assertEquals("0304550175", CrCpfUtil.normalize("3-0455-0175"));
        assertEquals("07-0161-0395", CrCpfUtil.format("701610395"));
        assertFalse(CrCpfUtil.isValid("30-1234-1234"));
        assertTrue(RegexUtil.is("crcpf", "3-0455-0175"));

        assertTrue(CrCpjUtil.isValid("3-101-999999"));
        assertEquals("3101999999", CrCpjUtil.normalize("3-101-999999"));
        assertEquals("4-000-042138", CrCpjUtil.format("4 000 042138"));
        assertEquals("3", CrCpjUtil.personClass("3-101-999999"));
        assertFalse(CrCpjUtil.isValid("3-534-123559"));
        assertTrue(RegexUtil.is("crcpj", "3101999999"));
    }

    @Test
    void tunisiaMfServerTimingCrc() {
        assertTrue(TnMfUtil.isValid("1234567/M/A/E/001"));
        assertEquals("1234567MAE001", TnMfUtil.normalize("1234567/M/A/E/001"));
        assertTrue(TnMfUtil.isValid("1282182 W"));
        assertEquals("0000121J", TnMfUtil.normalize("121J"));
        assertEquals("0000121/J", TnMfUtil.format("121J"));
        assertEquals("1496298/T/P/N/000", TnMfUtil.format("1496298 T P N 000"));
        assertFalse(TnMfUtil.isValid("1219773U"));
        assertFalse(TnMfUtil.isValid("1234567/M/A/X/000"));
        assertTrue(RegexUtil.is("tnmf", "1234567MAE001"));

        assertEquals("miss", ServerTimingUtil.first("miss, db;dur=53, app;dur=47.2"));
        assertEquals("53", ServerTimingUtil.duration("miss, db;dur=53, app;dur=47.2", "db"));
        assertTrue(ServerTimingUtil.has("miss, db;dur=53, app;desc=\"App Layer\"", "app"));
        assertEquals("App Layer", ServerTimingUtil.parse("app;desc=\"App Layer\"").get(0).description());
        assertEquals("4b", HashUtil.crc8SaeJ1850Hex("123456789"));
    }
}
