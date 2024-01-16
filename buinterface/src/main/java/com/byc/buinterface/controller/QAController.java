package com.byc.buinterface.controller;

import com.byc.buinterface.client.OpenAIClient;
import com.byc.buinterface.exception.BusinessException;
import com.byc.common.model.BaseResponse;
import com.byc.common.model.ErrorCode;
import com.byc.common.model.ResultUtils;
import com.plexpt.chatgpt.entity.chat.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/qa")
public class QAController {

    @Resource
    private OpenAIClient openAIClient;

    @GetMapping("/poetry")
    public BaseResponse<String> translate(@RequestParam String description) {
        if (description == null || StringUtils.isBlank(description)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误：文本description内容为空");
        }
        List<Message> messages = new ArrayList<>();
        messages.add(Message.of("根据以下描述生成中文诗句：" + description));
        String result = openAIClient.doChat(messages);
        return ResultUtils.success(result);
    }
}
