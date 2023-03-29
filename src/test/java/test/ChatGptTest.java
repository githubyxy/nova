package test;

import com.alibaba.fastjson.JSONObject;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ChatGptTest {
    @Test
    public void test() {
        // 消息列表
        List<ChatMessage> list = new ArrayList<>();
        // 定义一个用户身份，content是用户写的内容
        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent("你好");
        list.add(userMessage);

        OpenAiService service = new OpenAiService("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");
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(list)
                .model("gpt-3.5-turbo")
                .build();
        service.createChatCompletion(completionRequest).getChoices().forEach(
                item -> {
                    System.out.println(item.getMessage().getContent());
                    System.out.println(JSONObject.toJSONString(item.getMessage()));
                    System.out.println(JSONObject.toJSONString(item));
                }
        );
    }
}
