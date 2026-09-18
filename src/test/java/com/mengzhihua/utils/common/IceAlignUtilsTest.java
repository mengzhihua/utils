package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.XRobotsTagUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.MaIceUtil;
import com.mengzhihua.utils.common.validate.PyRucUtil;
import com.mengzhihua.utils.common.validate.UyRutUtil;
import com.mengzhihua.utils.common.validate.VoenUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IceAlignUtilsTest {

    @Test
    void iceVoenUyRut() {
        assertTrue(MaIceUtil.isValid("001561191000066"));
        assertTrue(MaIceUtil.isValid("00 21 36 09 30 00 040"));
        assertEquals("001561191000066", MaIceUtil.complete("0015611910000"));
        assertFalse(MaIceUtil.isValid("001561191000065"));
        assertTrue(VoenUtil.isValid("140 155 5071"));
        assertEquals("1401555071", VoenUtil.complete("14015550"));
        assertTrue(VoenUtil.legalPerson("1401555071"));
        assertFalse(VoenUtil.isValid("140 155 5081"));
        assertFalse(VoenUtil.isValid("1400057424"));
        assertTrue(UyRutUtil.isValid("21-100342-001-7"));
        assertTrue(UyRutUtil.isValid("UY 21 140634 001 1"));
        assertEquals("211003420017", UyRutUtil.complete("21100342001"));
        assertEquals("21-100342-001-7", UyRutUtil.format("211003420017"));
        assertFalse(UyRutUtil.isValid("210303670014"));
        assertTrue(RegexUtil.is("maice", "001561191000066"));
        assertTrue(RegexUtil.is("voen", "1401555071"));
        assertTrue(RegexUtil.is("uyrut", "21-100342-001-7"));
    }

    @Test
    void pyRucRobotsCrc() {
        assertTrue(PyRucUtil.isValid("80028061-0"));
        assertTrue(PyRucUtil.isValid("2660-3"));
        assertEquals("800280610", PyRucUtil.complete("80028061"));
        assertEquals("80028061-0", PyRucUtil.format("800280610"));
        assertFalse(PyRucUtil.isValid("800532492"));
        assertEquals("noindex", XRobotsTagUtil.first("noindex, nofollow"));
        assertTrue(XRobotsTagUtil.noindex("googlebot: noindex"));
        assertTrue(XRobotsTagUtil.has("noindex, nofollow", "nofollow"));
        assertTrue(RegexUtil.is("pyruc", "800280610"));
        assertEquals("ce3c", HashUtil.crc16GsmHex("123456789"));
    }
}
