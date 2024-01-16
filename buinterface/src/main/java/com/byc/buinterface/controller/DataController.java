package com.byc.buinterface.controller;


import com.byc.buinterface.client.OpenAIClient;
import com.byc.buinterface.exception.BusinessException;
import com.byc.buinterface.model.dto.TextAbstractRequest;
import com.byc.common.model.BaseResponse;
import com.byc.common.model.ErrorCode;
import com.byc.common.model.ResultUtils;
import com.plexpt.chatgpt.entity.chat.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Resource
    private OpenAIClient openAIClient;

    @PostMapping("/text-abstract")
    public BaseResponse<String> textAbstract(@RequestBody TextAbstractRequest textAbstractRequest) {
        String text = textAbstractRequest.getText();
        String length = textAbstractRequest.getLength();
        if (text == null || StringUtils.isBlank(text)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误：文本text内容为空");
        }
        if (!StringUtils.isNumeric(length)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误：长度length为空或者格式不正确");
        }

        List<Message> messages = new ArrayList<>();
        messages.add(Message.of("帮我把以下这段内容总结成摘要(长度必须小于或等于" +
                textAbstractRequest.getLength() + "个字): " + textAbstractRequest.getText()));
        String result = openAIClient.doChat(messages);
        return ResultUtils.success(result);
    }

    @GetMapping("/translate")
    public BaseResponse<String> translate(@RequestParam String text) {
        if (text == null || StringUtils.isBlank(text)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误：文本text内容为空");
        }
        List<Message> messages = new ArrayList<>();
        messages.add(Message.of("翻译成英文：" + text));
        String result = openAIClient.doChat(messages);
        return ResultUtils.success(result);
    }
}
