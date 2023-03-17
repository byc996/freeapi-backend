package com.byc.gateway;

import com.byc.gateway.service.ConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootTest
class GatewayApplicationTests {

    @Resource
    private ConsumerService consumerService;

    @Test
    void contextLoads() {
        String s = consumerService.doSayHello("harvey");
        System.out.println(s);
    }

}
