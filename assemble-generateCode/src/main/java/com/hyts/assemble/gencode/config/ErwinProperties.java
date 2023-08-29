package com.hyts.assemble.gencode.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties("erwin")
public class ErwinProperties {

    private App app;

    @Data
    public static class App {
        private String name;
        private String version;
    }

}
