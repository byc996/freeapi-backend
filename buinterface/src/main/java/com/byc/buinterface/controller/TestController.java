package com.byc.buinterface.controller;

import com.byc.common.model.BaseResponse;
import com.byc.common.model.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public BaseResponse<String> testGet() {
        return ResultUtils.success("测试GET成功");
    }

    @PostMapping("/post")
    public BaseResponse<String> testPost() {
        return ResultUtils.success("测试POST成功");
    }
}
