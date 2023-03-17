package com.byc.gateway.controller;


import com.byc.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference
    private DemoService demoService;

    @GetMapping("/")
    public String test(@RequestParam String name){
        return demoService.sayHello(name);
    }
}
