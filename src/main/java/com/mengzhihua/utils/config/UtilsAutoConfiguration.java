package com.mengzhihua.utils.config;

import com.mengzhihua.utils.util.IdUtil;
import com.mengzhihua.utils.util.SnowflakeIdGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SnowflakeProperties.class)
public class UtilsAutoConfiguration {

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator(SnowflakeProperties properties) {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(properties.getWorkerId(), properties.getDatacenterId());
        IdUtil.setSnowflake(generator);
        return generator;
    }
}
