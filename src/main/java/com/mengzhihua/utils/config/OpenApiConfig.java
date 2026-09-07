package com.mengzhihua.utils.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI utilsOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Java Utils Toolkit")
                .description("Spring Boot 通用 Java 工具集，提供字符串、日期、JSON、加解密、ID、树结构等常用能力。")
                .version("1.0.0")
                .contact(new Contact().name("mengzhihua")));
    }
}
