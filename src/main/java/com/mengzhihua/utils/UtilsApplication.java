package com.mengzhihua.utils;


import com.mengzhihua.utils.config.DesktopLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UtilsApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UtilsApplication.class);
        if (DesktopLauncher.requested(args)) {
            app.setAdditionalProfiles("desktop");
        }
        app.run(args);
    }
}
