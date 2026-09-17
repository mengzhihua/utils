package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ForwardedUtil;
import com.mengzhihua.utils.common.text.PorterStemmerUtil;
import com.mengzhihua.utils.common.text.RefinedSoundexUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CodiceFiscaleUtil;
import com.mengzhihua.utils.common.validate.DoiUtil;
import com.mengzhihua.utils.common.validate.EoriUtil;
import com.mengzhihua.utils.common.validate.IccidUtil;
import com.mengzhihua.utils.common.validate.NirUtil;
import com.mengzhihua.utils.common.validate.PmidUtil;
import com.mengzhihua.utils.common.validate.SteuerIdUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CivicAlignUtilsTest {

    @Test
    void refinedSoundexAndPorter() {
        assertEquals("T6036084", RefinedSoundexUtil.encode("testing"));
        assertEquals("T60", RefinedSoundexUtil.encode("The"));
        assertEquals("F205", RefinedSoundexUtil.encode("fox"));
        assertTrue(RefinedSoundexUtil.similar("Robert", "Rupert"));
        assertEquals("relat", PorterStemmerUtil.stem("relational"));
        assertEquals("caress", PorterStemmerUtil.stem("caresses"));
        assertEquals("poni", PorterStemmerUtil.stem("ponies"));
        assertEquals("cat", PorterStemmerUtil.stem("cats"));
        assertEquals("motor", PorterStemmerUtil.stem("motoring"));
        assertEquals("plaster", PorterStemmerUtil.stem("plastered"));
        assertEquals("connect", PorterStemmerUtil.stem("connection"));
        assertEquals("run", PorterStemmerUtil.stem("running"));
    }

    @Test
    void nirCodiceSteuerEori() {
        assertTrue(NirUtil.isValid("255081416812535"));
        assertEquals("255081416812535", NirUtil.complete("2550814168125"));
        assertTrue(NirUtil.female("255081416812535"));
        assertTrue(NirUtil.isValid("188072A00123417"));
        assertFalse(NirUtil.isValid("255081416812536"));
        assertTrue(CodiceFiscaleUtil.isValid("RSSMRA80A01H501U"));
        assertEquals("RSSMRA80A01H501U", CodiceFiscaleUtil.complete("RSSMRA80A01H501"));
        assertFalse(CodiceFiscaleUtil.female("RSSMRA80A01H501U"));
        assertEquals(LocalDate.of(1980, 1, 1), CodiceFiscaleUtil.birthDate("RSSMRA80A01H501U"));
        assertTrue(SteuerIdUtil.isValid("86095742719"));
        assertEquals("86095742719", SteuerIdUtil.complete("8609574271"));
        assertFalse(SteuerIdUtil.isValid("86095742718"));
        assertTrue(EoriUtil.isValid("FR73282932000074"));
        assertEquals("FR", EoriUtil.country("FR73282932000074"));
        assertFalse(EoriUtil.isValid("FR73282932000075"));
        assertTrue(RegexUtil.is("nir", "255081416812535"));
        assertTrue(RegexUtil.is("codicefiscale", "RSSMRA80A01H501U"));
        assertTrue(RegexUtil.is("steuerid", "86095742719"));
        assertTrue(RegexUtil.is("eori", "FR73282932000074"));
    }

    @Test
    void doiPmidIccidForwardedFletcher() {
        assertTrue(DoiUtil.isValid("10.1000/182"));
        assertTrue(DoiUtil.isValid("https://doi.org/10.1038/nphys1170"));
        assertEquals("10.1000/182", DoiUtil.normalize("doi:10.1000/182"));
        assertTrue(PmidUtil.isValid("12345678"));
        assertFalse(PmidUtil.isValid("0123"));
        assertTrue(IccidUtil.isValid("89014103211118510720"));
        assertEquals("89014103211118510720", IccidUtil.complete("8901410321111851072"));
        ForwardedUtil.Element element = ForwardedUtil.parse(
                "for=192.0.2.60;proto=http;by=203.0.113.43").get(0);
        assertEquals("192.0.2.60", element.forParam());
        assertEquals("http", element.proto());
        assertEquals("192.0.2.60", ForwardedUtil.clientIp("for=192.0.2.60;proto=http;by=203.0.113.43"));
        assertEquals("[2001:db8:cafe::17]:4711", ForwardedUtil.parse(
                "For=\"[2001:db8:cafe::17]:4711\"").get(0).forParam());
        assertEquals("1ede", HashUtil.fletcher16Hex("123456789"));
        assertEquals("091501dd", HashUtil.fletcher32Hex("123456789"));
        assertTrue(RegexUtil.is("doi", "10.1000/182"));
        assertTrue(RegexUtil.is("pmid", "12345678"));
        assertTrue(RegexUtil.is("iccid", "89014103211118510720"));
    }
}
