package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.DocumentPolicyUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CnicUtil;
import com.mengzhihua.utils.common.validate.GhTinUtil;
import com.mengzhihua.utils.common.validate.IdnoUtil;
import com.mengzhihua.utils.common.validate.KePinUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CnicAlignUtilsTest {

    @Test
    void cnicIdnoGhTin() {
        assertTrue(CnicUtil.isValid("34201-0891231-8"));
        assertTrue(CnicUtil.isValid("42201-0397640-8"));
        assertEquals("F", CnicUtil.gender("42201-0397640-8"));
        assertEquals("Sindh", CnicUtil.province("42201-0397640-8"));
        assertEquals("34201-0891231-8", CnicUtil.format("3420108912318"));
        assertFalse(CnicUtil.isValid("34201-0891231-0"));
        assertFalse(CnicUtil.isValid("84201-0891231-8"));
        assertTrue(IdnoUtil.isValid("1008600038413"));
        assertEquals("1008600038413", IdnoUtil.complete("100860003841"));
        assertFalse(IdnoUtil.isValid("1008600038412"));
        assertTrue(GhTinUtil.isValid("C0000803561"));
        assertEquals("C0000803561", GhTinUtil.complete("C000080356"));
        assertFalse(GhTinUtil.isValid("C0000803562"));
        assertTrue(RegexUtil.is("cnic", "34201-0891231-8"));
        assertTrue(RegexUtil.is("idno", "1008600038413"));
        assertTrue(RegexUtil.is("ghtin", "C0000803561"));
    }

    @Test
    void kePinDocumentCrc() {
        assertTrue(KePinUtil.isValid("P051365947M"));
        assertTrue(KePinUtil.isValid("A004416331M"));
        assertTrue(KePinUtil.individual("A004416331M"));
        assertFalse(KePinUtil.individual("P051365947M"));
        assertFalse(KePinUtil.isValid("V1234567890"));
        assertEquals("unsized-media", DocumentPolicyUtil.first("unsized-media=?0, max-image-bpp=2.0"));
        assertEquals("?0", DocumentPolicyUtil.value("unsized-media=?0, max-image-bpp=2.0", "unsized-media"));
        assertTrue(DocumentPolicyUtil.has("unsized-media=?0, max-image-bpp=2.0", "max-image-bpp"));
        assertTrue(RegexUtil.is("kepin", "P051365947M"));
        assertEquals("1697d06a", HashUtil.crc32AutosarHex("123456789"));
    }
}
