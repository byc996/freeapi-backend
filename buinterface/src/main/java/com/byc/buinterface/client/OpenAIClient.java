package com.byc.buinterface.client;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 调用open ai
 * https://github.com/PlexPt/chatgpt-java/tree/main
 */
@Component
public class OpenAIClient {

    private static final String apiKey = "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";

    private final ChatGPT chatGPT;

    public OpenAIClient() {
        chatGPT = ChatGPT.builder()
                .apiKey(apiKey)
                .timeout(900)
                .build()
                .init();
    }

    public String doChat(List<Message> messages) {

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(messages)
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        Message res = response.getChoices().get(0).getMessage();
        return res.getContent();
    }
}
