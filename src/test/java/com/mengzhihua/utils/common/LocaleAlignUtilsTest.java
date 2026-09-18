package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.Sm3Util;
import com.mengzhihua.utils.common.id.SonyflakeUtil;
import com.mengzhihua.utils.common.net.ContentDispositionUtil;
import com.mengzhihua.utils.common.text.CaverphoneUtil;
import com.mengzhihua.utils.common.text.NysiisUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.time.JulianDayUtil;
import com.mengzhihua.utils.common.validate.CnpjUtil;
import com.mengzhihua.utils.common.validate.CpfUtil;
import com.mengzhihua.utils.common.validate.PeselUtil;
import com.mengzhihua.utils.common.validate.UpcEUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocaleAlignUtilsTest {

    @Test
    void nysiisCaverphone() {
        assertEquals("MALAR", NysiisUtil.encode("Miller"));
        assertEquals("NAT", NysiisUtil.encode("Knuth"));
        assertEquals("WASTARLAD", NysiisUtil.encode("WESTERLUND"));
        assertEquals("STFNSN1111", CaverphoneUtil.encode("Stevenson"));
        assertEquals("TMPSN11111", CaverphoneUtil.encode("Thompson"));
        assertTrue(NysiisUtil.similar("Miller", "Muller"));
    }

    @Test
    void cpfCnpjPeselUpcE() {
        assertTrue(CpfUtil.isValid("111.444.777-35"));
        assertEquals("11144477735", CpfUtil.complete("111444777"));
        assertFalse(CpfUtil.isValid("111.111.111-11"));
        assertTrue(CnpjUtil.isValid("00.000.000/0001-91"));
        assertEquals("00000000000191", CnpjUtil.complete("000000000001"));
        assertFalse(CnpjUtil.isValid("11.111.111/1111-11"));
        assertTrue(PeselUtil.isValid("44051401359"));
        assertEquals(LocalDate.of(1944, 5, 14), PeselUtil.birthDate("44051401359"));
        assertFalse(PeselUtil.female("44051401359"));
        assertFalse(PeselUtil.isValid("44051401350"));
        assertTrue(UpcEUtil.isValid("04252614"));
        assertEquals("042100005264", UpcEUtil.expand("04252614"));
        assertTrue(RegexUtil.is("cpf", "111.444.777-35"));
        assertTrue(RegexUtil.is("cnpj", "00.000.000/0001-91"));
        assertTrue(RegexUtil.is("pesel", "44051401359"));
        assertTrue(RegexUtil.is("upce", "04252614"));
    }

    @Test
    void julianSonyflakeDispositionHash() {
        assertEquals(2451545L, JulianDayUtil.of("2000-01-01"));
        assertEquals(2440588L, JulianDayUtil.of("1970-01-01"));
        assertEquals(LocalDate.of(2000, 1, 1), JulianDayUtil.toLocalDate(2451545L));
        long id = SonyflakeUtil.next(1_704_067_200_000L, 42, 7);
        assertEquals(42, SonyflakeUtil.machine(id));
        assertEquals(7, SonyflakeUtil.sequence(id));
        assertEquals(1_704_067_200_000L, SonyflakeUtil.unixMillis(id));
        String header = ContentDispositionUtil.attachment("报表.txt");
        ContentDispositionUtil.Parsed parsed = ContentDispositionUtil.parse(header);
        assertEquals("attachment", parsed.type());
        assertEquals("报表.txt", parsed.filename());
        ContentDispositionUtil.Parsed quoted = ContentDispositionUtil.parse("inline; filename=\"a.txt\"");
        assertEquals("inline", quoted.type());
        assertEquals("a.txt", quoted.filename());
        assertEquals("0376e6e7", HashUtil.crc32Mpeg2Hex("123456789"));
        assertEquals("00000000000000000000000000000000", HashUtil.murmur128Hex(""));
        assertEquals("6778ad3f3f3f96b4522dca264174a23b", HashUtil.murmur128Hex("abc"));
        assertEquals("6c1b07bc7bbc4be347939ac4a93c437a",
                HashUtil.murmur128Hex("The quick brown fox jumps over the lazy dog"));
        assertEquals("28e63256e7c5a087b1f073265dc53092163f7b82729735d06f28f10af9d52393",
                Sm3Util.hmac("abc", "key"));
    }
}
