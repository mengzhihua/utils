package com.mengzhihua.utils.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

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
        String code = com.mengzhihua.utils.util.CreditCodeUtil.complete("91110000710930405");
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
}
