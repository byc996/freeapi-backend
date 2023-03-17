package com.byc.gateway;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableDubbo
public class GatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(GatewayApplication.class, args);
    }



//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("tobaidu", r -> r.path("/baidu")
//                        .uri("https://www.baidu.com"))
//                .route("host_route", r -> r.path("/yupiicu")
//                        .uri("http://yupi.icu"))
//                .build();
//    }
}
