package org.cmcc;

import org.cmcc.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication
public class WebDubboApp {
    public static void main(String[] args) {
        SpringApplication.run(WebDubboApp.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
