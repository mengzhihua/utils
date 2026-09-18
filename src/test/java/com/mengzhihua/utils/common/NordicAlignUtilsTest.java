package com.mengzhihua.utils.common;


import java.time.LocalDate;
import java.util.List;

import com.mengzhihua.utils.common.codec.UuencodeUtil;
import com.mengzhihua.utils.common.net.EtagUtil;
import com.mengzhihua.utils.common.net.LinkHeaderUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AbnUtil;
import com.mengzhihua.utils.common.validate.FodselsnummerUtil;
import com.mengzhihua.utils.common.validate.HetuUtil;
import com.mengzhihua.utils.common.validate.IswcUtil;
import com.mengzhihua.utils.common.validate.PersonnummerUtil;
import com.mengzhihua.utils.common.validate.SsccUtil;
import com.mengzhihua.utils.common.validate.TfnUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NordicAlignUtilsTest {

    @Test
    void personnummerHetuFodselsnummer() {
        assertTrue(PersonnummerUtil.isValid("19811218-9876"));
        assertTrue(PersonnummerUtil.isValid("8112189876"));
        assertEquals("8112189876", PersonnummerUtil.complete("811218987"));
        assertFalse(PersonnummerUtil.female("19811218-9876"));
        assertEquals(LocalDate.of(1981, 12, 18), PersonnummerUtil.birthDate("19811218-9876"));
        assertFalse(PersonnummerUtil.isValid("19811218-9877"));
        assertTrue(HetuUtil.isValid("131052-308T"));
        assertEquals("131052-308T", HetuUtil.complete("131052-308"));
        assertTrue(HetuUtil.female("131052-308T"));
        assertEquals(LocalDate.of(1952, 10, 13), HetuUtil.birthDate("131052-308T"));
        assertTrue(FodselsnummerUtil.isValid("11077941012"));
        assertEquals("11077941012", FodselsnummerUtil.complete("110779410"));
        assertTrue(FodselsnummerUtil.female("11077941012"));
        assertFalse(FodselsnummerUtil.isValid("11077941013"));
        assertTrue(RegexUtil.is("personnummer", "19811218-9876"));
        assertTrue(RegexUtil.is("hetu", "131052-308T"));
        assertTrue(RegexUtil.is("fodselsnummer", "11077941012"));
    }

    @Test
    void iswcSsccAbnTfn() {
        assertTrue(IswcUtil.isValid("T-034.524.680-8"));
        assertEquals("T0345246808", IswcUtil.complete("034524680"));
        assertEquals("T-034.524.680-8", IswcUtil.format("T0345246808"));
        assertTrue(SsccUtil.isValid("106141411234567897"));
        assertEquals("106141411234567897", SsccUtil.complete("10614141123456789"));
        assertTrue(AbnUtil.isValid("51 824 753 556"));
        assertFalse(AbnUtil.isValid("51 824 753 557"));
        assertTrue(TfnUtil.isValid("123456782"));
        assertFalse(TfnUtil.isValid("123456783"));
        assertTrue(RegexUtil.is("iswc", "T-034.524.680-8"));
        assertTrue(RegexUtil.is("sscc", "106141411234567897"));
        assertTrue(RegexUtil.is("abn", "51 824 753 556"));
        assertTrue(RegexUtil.is("tfn", "123456782"));
    }

    @Test
    void linkEtagUuencode() {
        String header = "<https://example.com/TheBook/chapter2>; rel=\"previous\"; title=\"previous chapter\"";
        List<LinkHeaderUtil.Link> links = LinkHeaderUtil.parse(header);
        assertEquals("https://example.com/TheBook/chapter2", links.get(0).uri());
        assertEquals("previous", links.get(0).rel());
        assertEquals("https://example.com/TheBook/chapter2", LinkHeaderUtil.rel(header, "previous"));
        EtagUtil.Etag weak = EtagUtil.parse("W/\"abc\"");
        assertTrue(weak.weak());
        assertEquals("abc", weak.tag());
        assertTrue(EtagUtil.matches("W/\"abc\"", "\"abc\", W/\"xyz\""));
        assertFalse(EtagUtil.matches("\"abc\"", "\"def\""));
        assertEquals("#0V%T", UuencodeUtil.encode("Cat"));
        assertEquals("Cat", UuencodeUtil.decodeToString("#0V%T"));
        assertEquals("hello", UuencodeUtil.decodeToString(UuencodeUtil.encode("hello")));
    }
}
