package com.byc.gateway;

import com.byc.common.model.entity.User;
import com.byc.common.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootTest
class GatewayApplicationTests {

    @DubboReference
    private InnerUserService innerUserService;

    @Test
    void contextLoads() {
        User invokeUser = innerUserService.getInvokeUser("d0ce29317c91c4dbbe2e9a3535ebcbcf");
        System.out.println(invokeUser);
    }

}
