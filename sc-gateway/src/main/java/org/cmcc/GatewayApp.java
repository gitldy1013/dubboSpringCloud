package org.cmcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

    /**
     * 自定义转发规则 这里主要为了转发到不受nacos管理的rest服务
     *
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.method(HttpMethod.GET, HttpMethod.POST).and()
                        .path("/customerMsg/downMsg")
//                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("https://sms.liudongyang.top"))
                .route(p -> p.method(HttpMethod.GET, HttpMethod.POST).and()
                        .path("/customerMsg/upMsg")
//                        .filters(f -> f.addRequestParameter("Hello", "World"))
                        .uri("https://sms.liudongyang.top"))
                .route(p -> p.method(HttpMethod.GET, HttpMethod.POST).and()
                        .path("/customerMsg/upMsg1")
//                        .filters(f -> f.addRequestParameter("Hello", "World"))
                        .uri("https://sms.liudongyang.top"))
                .build();
    }
}
