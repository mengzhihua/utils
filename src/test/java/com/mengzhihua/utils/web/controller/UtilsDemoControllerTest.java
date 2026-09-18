package com.mengzhihua.utils.web.controller;


import com.mengzhihua.utils.common.validate.CreditCodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UtilsDemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void maskPhoneReturnsUnifiedResult() throws Exception {
        mockMvc.perform(get("/api/utils/string/mask-phone").param("phone", "13812345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("138****5678"));
    }

    @Test
    void snowflakeReturnsPositiveId() throws Exception {
        mockMvc.perform(get("/api/utils/id/snowflake"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.idStr").isString());
    }

    @Test
    void aesRoundTrip() throws Exception {
        mockMvc.perform(post("/api/utils/encrypt/aes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"hello\",\"password\":\"secret-key\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.plainText").value("hello"))
                .andExpect(jsonPath("$.data.cipherText", not(is("hello"))));
    }

    @Test
    void treeSampleHasChildren() throws Exception {
        mockMvc.perform(get("/api/utils/tree/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].label").value("总部"))
                .andExpect(jsonPath("$.data[0].children", hasSize(2)));
    }

    @Test
    void validationErrorUsesUnifiedEnvelope() throws Exception {
        mockMvc.perform(post("/api/utils/encrypt/aes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"\",\"password\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(422))
                .andExpect(jsonPath("$.message", containsString("must not be blank")));
    }

    @Test
    void regexValidateRejectsUnknownType() throws Exception {
        mockMvc.perform(get("/api/utils/regex/validate")
                        .param("value", "x")
                        .param("type", "unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void regexExtractAndHexColor() throws Exception {
        mockMvc.perform(get("/api/utils/regex/validate")
                        .param("type", "hexcolor")
                        .param("value", "#0f766e"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.matched").value(true));

        mockMvc.perform(get("/api/utils/regex/extract")
                        .param("text", "Ada ada@example.com 13812345678 https://example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mobiles[0]").value("13812345678"))
                .andExpect(jsonPath("$.data.emails[0]").value("ada@example.com"))
                .andExpect(jsonPath("$.data.urls[0]").value("https://example.com"));
    }

    @Test
    void vueConsoleIsServedAtRoot() throws Exception {
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"app\"")))
                .andExpect(content().string(containsString("Java Utils")));
    }

    @Test
    void extraUtilsIdCardJwtAndTraceHeader() throws Exception {
        mockMvc.perform(get("/api/utils/idcard/parse").param("idNo", "110101199003078937"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Trace-Id"))
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.gender").value("M"))
                .andExpect(jsonPath("$.data.province").value("北京"));

        mockMvc.perform(get("/api/utils/jwt").param("subject", "ada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").isString())
                .andExpect(jsonPath("$.data.payload.sub").value("ada"));

        mockMvc.perform(get("/api/utils/html/escape").param("text", "<b>x</b>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.escaped").value("&lt;b&gt;x&lt;/b&gt;"))
                .andExpect(jsonPath("$.data.stripped").value("x"));
    }

    @Test
    void businessUtilsCreditMoneyAndCidr() throws Exception {
        String code = com.mengzhihua.utils.common.validate.CreditCodeUtil.complete("91110000710930405");
        mockMvc.perform(get("/api/utils/credit-code/parse").param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/money/fen").param("yuan", "12.3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fen").value(1230))
                .andExpect(jsonPath("$.data.back").value("12.30"));

        mockMvc.perform(get("/api/utils/ip/cidr").param("ip", "172.16.0.10").param("cidr", "172.16.0.0/24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.inCidr").value(true))
                .andExpect(jsonPath("$.data.network").value("172.16.0.0"));
    }

    @Test
    void runtimeUtilsAntPathZodiacDuration() throws Exception {
        mockMvc.perform(get("/api/utils/ant-path").param("pattern", "/api/**").param("path", "/api/utils/ip"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.matched").value(true));

        mockMvc.perform(get("/api/utils/zodiac").param("date", "1990-03-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.constellation").value("双鱼座"))
                .andExpect(jsonPath("$.data.chineseZodiac").value("马"));

        mockMvc.perform(get("/api/utils/duration").param("text", "1h30m"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.millis").value(5_400_000));

        mockMvc.perform(get("/api/utils/slug").param("text", "Spring Boot 工具集"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.slug").value("spring-boot-工具集"));
    }

    @Test
    void alignUtilsExprLunarIsbn() throws Exception {
        mockMvc.perform(get("/api/utils/expr").param("expression", "(1+2)*3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("9"));

        mockMvc.perform(get("/api/utils/lunar").param("date", "2024-02-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.month").value(1))
                .andExpect(jsonPath("$.data.day").value(1))
                .andExpect(jsonPath("$.data.animal").value("龙"));

        mockMvc.perform(get("/api/utils/isbn").param("code", "9780306406157"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));
    }

    @Test
    void moreUtilsMathImeiRoman() throws Exception {
        mockMvc.perform(get("/api/utils/math").param("a", "12").param("b", "18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.gcd").value(6))
                .andExpect(jsonPath("$.data.lcm").value(36));

        mockMvc.perform(get("/api/utils/imei").param("value", "490154203237518"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/roman").param("value", "1994"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.roman").value("MCMXCIV"));

        mockMvc.perform(get("/api/utils/unit").param("value", "1").param("from", "km").param("to", "m"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.value").value("1000"));

        mockMvc.perform(get("/api/utils/url/parse").param("url", "https://example.com:8443/search?q=1#top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.host").value("example.com"))
                .andExpect(jsonPath("$.data.port").value(8443));

        mockMvc.perform(get("/api/utils/week").param("date", "2024-02-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isoWeek").value(6))
                .andExpect(jsonPath("$.data.chinese").value("星期六"));
    }

    @Test
    void openSourceIbanSoundexSha3() throws Exception {
        mockMvc.perform(get("/api/utils/iban").param("value", "GB82WEST12345698765432"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/soundex").param("left", "Robert").param("right", "Rupert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.left").value("R163"))
                .andExpect(jsonPath("$.data.similar").value(true));

        mockMvc.perform(get("/api/utils/similarity").param("left", "MARTHA").param("right", "MARHTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.jaroWinkler").value(org.hamcrest.Matchers.closeTo(0.9611, 0.001)));

        mockMvc.perform(get("/api/utils/digest").param("text", "hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sha3_256")
                        .value("3338be694f50c5f338814986cdf0686453a888b84f424d792af4b9202398f392"));

        mockMvc.perform(get("/api/utils/vin").param("value", "1M8GDM9AXKP042788"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));
    }

    @Test
    void sm3IsbnIpv6Between() throws Exception {
        mockMvc.perform(get("/api/utils/sm3").param("text", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sm3")
                        .value("66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0"));

        mockMvc.perform(get("/api/utils/isbn/convert").param("code", "0306406152"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isbn13").value("9780306406157"));

        mockMvc.perform(get("/api/utils/ipv6").param("ip", "2001:db8::1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.expanded").value("2001:0db8:0000:0000:0000:0000:0000:0001"));

        mockMvc.perform(get("/api/utils/between").param("seconds", "183900"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.formatted").value("2天3小时5分钟"));
    }

    @Test
    void sm4Blake2sHkidSolar() throws Exception {
        mockMvc.perform(get("/api/utils/blake2s").param("text", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.blake2s")
                        .value("508c5e8c327c14e2e1a72ba34eeb452f37458b209ed63a294d999b4c86675982"));

        mockMvc.perform(get("/api/utils/hkid").param("value", "A123456(3)"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/solar-term").param("year", "2026").param("name", "清明"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.qingming").value("2026-04-05"));
    }

    @Test
    void blake2bTsidFigiNhs() throws Exception {
        mockMvc.perform(get("/api/utils/blake2b").param("text", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.blake2b")
                        .value("ba80a53f981c4d0d6a2797b69f12f6e94c212f14685ac4b74b12bb6fdbffa2d17d87c5392aab792dc252d5de4533cc9518d38aa8dbf1925ab92386edd4009923"));

        mockMvc.perform(get("/api/utils/aes-kw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.wrapped")
                        .value("1FA68B0A8112B447AEF34BD8FB5A7B829D3E862371D2CFE5"));

        mockMvc.perform(get("/api/utils/ganzhi").param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ganzhi").value("丙午"))
                .andExpect(jsonPath("$.data.animal").value("马"));

        mockMvc.perform(get("/api/utils/iso6346").param("code", "CSQU3054383"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/bech32m").param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.empty").value("a1lqfn3a"));

        mockMvc.perform(get("/api/utils/ripemd160").param("text", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ripemd160").value("8eb208f7e05d987a9b044a8e98c6b087f15a0bfc"));

        mockMvc.perform(get("/api/utils/figi").param("value", "BBG000B9XRY4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/nhs").param("value", "943 476 5919"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));
    }

    @Test
    void shakeNpiCologneRange() throws Exception {
        mockMvc.perform(get("/api/utils/shake").param("text", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.shake128")
                        .value("5881092dd818bf5cf8a3ddb793fbcba74097d5c526a6d35f97b83351940f2cc8"));

        mockMvc.perform(get("/api/utils/npi").param("value", "1234567893"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/nric").param("value", "S1234567D"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/cologne").param("text", "Müller"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("657"));

        mockMvc.perform(get("/api/utils/http-range").param("header", "bytes=0-499"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ranges[0].start").value(0))
                .andExpect(jsonPath("$.data.ranges[0].end").value(499));

        mockMvc.perform(get("/api/utils/hamming").param("left", "karolin").param("right", "kathrin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.distance").value(3));
    }

    @Test
    void nysiisCpfJulianCrcMpeg2() throws Exception {
        mockMvc.perform(get("/api/utils/nysiis").param("text", "Miller"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("MALAR"));

        mockMvc.perform(get("/api/utils/caverphone").param("text", "Stevenson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("STFNSN1111"));

        mockMvc.perform(get("/api/utils/cpf").param("value", "111.444.777-35"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/pesel").param("value", "44051401359"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.birthDate").value("1944-05-14"));

        mockMvc.perform(get("/api/utils/julian").param("date", "2000-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.julianDayNumber").value(2451545));

        mockMvc.perform(get("/api/utils/crc32-mpeg2").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.crc32Mpeg2").value("0376e6e7"));

        mockMvc.perform(get("/api/utils/upc-e").param("value", "04252614"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.upcA").value("042100005264"));

        mockMvc.perform(get("/api/utils/murmur128").param("text", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.murmur128").value("6778ad3f3f3f96b4522dca264174a23b"));

        mockMvc.perform(get("/api/utils/hmac-sm3").param("text", "abc").param("key", "key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.hmacSm3")
                        .value("28e63256e7c5a087b1f073265dc53092163f7b82729735d06f28f10af9d52393"));
    }

    @Test
    void doubleMetaphoneSirenNifBencode() throws Exception {
        mockMvc.perform(get("/api/utils/double-metaphone").param("text", "Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.primary").value("SM0"));

        mockMvc.perform(get("/api/utils/match-rating").param("left", "Smith").param("right", "Smyth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.similar").value(true));

        mockMvc.perform(get("/api/utils/siren").param("value", "732829320"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/nif").param("value", "12345678Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/bencode").param("text", "spam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.encoded").value("4:spam"));

        mockMvc.perform(get("/api/utils/http-accept")
                        .param("header", "text/html,application/json;q=0.9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.negotiated").value("text/html"));
    }

    @Test
    void refinedSoundexNirCodiceDoi() throws Exception {
        mockMvc.perform(get("/api/utils/refined-soundex").param("text", "testing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("T6036084"));

        mockMvc.perform(get("/api/utils/porter").param("text", "relational"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stem").value("relat"));

        mockMvc.perform(get("/api/utils/nir").param("value", "255081416812535"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/codice-fiscale").param("value", "RSSMRA80A01H501U"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/doi").param("value", "10.1000/182"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/fletcher").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fletcher16").value("1ede"));
    }

    @Test
    void personnummerHetuIswcUuencode() throws Exception {
        mockMvc.perform(get("/api/utils/personnummer").param("value", "19811218-9876"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/hetu").param("value", "131052-308T"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/iswc").param("value", "T-034.524.680-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/abn").param("value", "51 824 753 556"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/uuencode").param("text", "Cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.encoded").value("#0V%T"));

        mockMvc.perform(get("/api/utils/link")
                        .param("header", "<https://example.com/x>; rel=\"previous\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.previous").value("https://example.com/x"));
    }

    @Test
    void vatAhvAadhaarZ85() throws Exception {
        mockMvc.perform(get("/api/utils/vat").param("value", "DE136695976"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/ahv").param("value", "756.1234.5678.97"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/aadhaar").param("value", "234123412346"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/z85").param("text", "HelloWorld"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.decodedHex").value("864fd26fb559f75b"));

        mockMvc.perform(get("/api/utils/crc16-xmodem").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.crc16Xmodem").value("31c3"));
    }

    @Test
    void cprPtNifYencBase36() throws Exception {
        mockMvc.perform(get("/api/utils/cpr").param("value", "010170-0003"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/nrn").param("value", "93.05.18-223.61"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/pt-nif").param("value", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/yenc").param("text", "Hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.encodedHex").value("728f969699"));

        mockMvc.perform(get("/api/utils/base36").param("text", "hello").param("value", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.encodedLong").value("kf12oi"));
    }

    @Test
    void rutCuitSaIdCors() throws Exception {
        mockMvc.perform(get("/api/utils/rut").param("value", "12.345.678-5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/cuit").param("value", "20-12345678-6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/sa-id").param("value", "8001015009087"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/ird").param("value", "49091850"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/cors")
                        .param("allowOrigin", "*")
                        .param("origin", "https://example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.allowsOrigin").value(true));
    }

    @Test
    void tcknCnpThaiHsts() throws Exception {
        mockMvc.perform(get("/api/utils/tckn").param("value", "10000000146"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/cnp").param("value", "1800101010015"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/thai-id").param("value", "1234567890121"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/hsts")
                        .param("header", "max-age=31536000; includeSubDomains; preload"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.maxAge").value(31536000));

        mockMvc.perform(get("/api/utils/crc16-kermit").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.crc16Kermit").value("2189"));
    }

    @Test
    void jmbgKennitalaNit() throws Exception {
        mockMvc.perform(get("/api/utils/jmbg").param("value", "0101980500005"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.birthDate").value("1980-01-01"));

        mockMvc.perform(get("/api/utils/kennitala").param("value", "120174-3399"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.birthDate").value("1974-01-12"));

        mockMvc.perform(get("/api/utils/nit").param("value", "800197268-4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/crc16-arc").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.crc16Arc").value("bb3d"));
    }

    @Test
    void lvPkEmsoMxRfc() throws Exception {
        mockMvc.perform(get("/api/utils/lv-pk").param("value", "111111-11111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.birthDate").value("1911-11-11"));

        mockMvc.perform(get("/api/utils/emso").param("value", "0101006500006"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.birthDate").value("2006-01-01"));

        mockMvc.perform(get("/api/utils/mx-rfc").param("value", "GODE561231GR8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/crc16-maxim").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.crc16Maxim").value("44c2"));
    }

    @Test
    void bsnRodneBase92() throws Exception {
        mockMvc.perform(get("/api/utils/bsn").param("value", "111222333"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/rodne").param("value", "680101/0007"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.birthDate").value("1968-01-01"));

        mockMvc.perform(get("/api/utils/base92").param("action", "encode").param("text", "Hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.encoded").value("Q2Aeq)"));
    }

    @Test
    void yTunnusCvrCif() throws Exception {
        mockMvc.perform(get("/api/utils/y-tunnus").param("value", "1234567-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/cvr").param("value", "35408002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/cif").param("value", "A58818501"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/crc32-posix").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.crc32Posix").value("765e7680"));
    }

    @Test
    void cheUidCuiKbo() throws Exception {
        mockMvc.perform(get("/api/utils/che-uid").param("value", "CHE-109.322.551"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/ro-cui").param("value", "18547290"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/kbo").param("value", "0123.456.749"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/crc16-usb").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.crc16Usb").value("b4c8"));
    }

    @Test
    void hojinBrnEdrpou() throws Exception {
        mockMvc.perform(get("/api/utils/hojin").param("value", "8700110005901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/kr-brn").param("value", "120-81-47521"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/edrpou").param("value", "14360570"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valid").value(true));

        mockMvc.perform(get("/api/utils/crc8-smbus").param("text", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.crc8Smbus").value("f4"));
    }
}
