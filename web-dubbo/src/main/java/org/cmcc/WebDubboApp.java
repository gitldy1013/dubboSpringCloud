package org.cmcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@EnableDiscoveryClient
@SpringBootApplication
public class WebDubboApp {
    public static void main(String[] args) {
        SpringApplication.run(WebDubboApp.class, args);
    }
}
