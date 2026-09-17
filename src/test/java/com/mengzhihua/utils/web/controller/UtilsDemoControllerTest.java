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
}
