package org.cmcc;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@EnableDiscoveryClient
@SpringBootApplication
public class RestDubboApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(RestDubboApp.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
