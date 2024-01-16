package com.byc.service.impl;

import com.byc.common.model.entity.User;
import com.byc.common.service.InnerUserInterfaceInfoService;
import com.byc.common.service.InnerUserService;
import com.byc.service.UserInterfaceInfoService;
import com.byc.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
public class UserInterfaceInfoServiceImplTest {

    @Resource
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @Resource
    private InnerUserService innerUserService;

    @Test
    public void testUser() {
        User user = innerUserService.getInvokeUser("d0ce29317c91c4dbbe2e9a3535ebcbcf");
        Assertions.assertTrue(user != null);
    }

    @Test
    public void increment() {
        boolean b = innerUserInterfaceInfoService.increment(1, 2, "限次");
        Assertions.assertTrue(b);
    }
}