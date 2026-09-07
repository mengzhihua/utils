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
}
