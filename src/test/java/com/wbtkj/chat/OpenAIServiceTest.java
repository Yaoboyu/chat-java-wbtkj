package com.wbtkj.chat;

import com.wbtkj.chat.listener.OpenAIWebSocketEventSourceListener;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.service.OpenAIService;
import okhttp3.sse.EventSourceListener;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@EnableConfigurationProperties
public class OpenAIServiceTest {
    @Resource
    OpenAIService openAIService;


    @Test
    public void streamChatCompletionWithFileTest() {
        List<String> fileNames = Arrays.asList("file-725a548267bd9ccf874b88e779923277&en","file-68b4f71bbec80ae70778cd267c81412d&en");
        ChatCompletion chatCompletion = new ChatCompletion();
        chatCompletion.setMessages(new ArrayList<>());
        chatCompletion.getMessages().add(Message.builder().role(Message.Role.SYSTEM).content("原来的system").build());
        chatCompletion.getMessages().add(Message.builder().role(Message.Role.USER).content("这篇文章的整体结构是什么？").build());
        EventSourceListener eventSourceListener = new OpenAIWebSocketEventSourceListener();

        openAIService.streamChatCompletionWithFile(fileNames, chatCompletion, eventSourceListener);
    }
}
