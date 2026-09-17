package com.mengzhihua.utils.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "utils.snowflake")
public class SnowflakeProperties {

    /**
     * Worker id in [0, 31].
     */
    private long workerId = 1;

    /**
     * Datacenter id in [0, 31].
     */
    private long datacenterId = 1;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }
}
