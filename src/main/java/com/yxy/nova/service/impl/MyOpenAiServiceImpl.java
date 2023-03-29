package com.yxy.nova.service.impl;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyOpenAiServiceImpl implements MyOpenAiService {

    @Value("${openai.api-keys}")
    private String apiKey;
    @Override
    public List<String> chat(String content) {
        // 消息列表
        List<ChatMessage> list = new ArrayList<>();
        // 定义一个用户身份，content是用户写的内容
        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent(content);
        list.add(userMessage);

        OpenAiService service = new OpenAiService(apiKey);
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .messages(list)
                .model("gpt-3.5-turbo")
                .build();
        List<ChatCompletionChoice> choices = service.createChatCompletion(request).getChoices();

        List<String> response = new ArrayList<>();
        choices.forEach(item -> {
            response.add(item.getMessage().getContent());
        });
        return response;
    }
}
