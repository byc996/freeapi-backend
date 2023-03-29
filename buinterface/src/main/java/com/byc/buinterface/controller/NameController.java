package com.byc.buinterface.controller;


import com.byc.clientsdk.model.User;
import com.byc.clientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getName(String name, HttpServletRequest request) {
        System.out.println(request.getHeader("gateway"));
        return "GET Your name is :" + name;
    }

    @PostMapping("/post")
    public String getNamePost(@RequestParam String name) {
        return "POST Your name is :" + name;
    }

    @PostMapping("/user")
    public String getUserNamePost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//        // todo 数据库中查
//        if (!accessKey.equals("harvey") ) {
//            throw new RuntimeException("无权限");
//        }
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
//        // todo 时间和当前时间不能超过五分钟
//
//        // todo 数据库中根据accessKey 查询 secretKey
//        String serverSign = SignUtils.getSign(body, "12345678");
//        if (!sign.equals(serverSign)) {
//            throw new RuntimeException("无权限");
//        }
        String result = " POST Username is :" + user.getUsername();

        // 调用成功后， 次数 + 1
        return result;
    }
}
