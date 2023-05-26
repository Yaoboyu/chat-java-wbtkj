package com.wbtkj.chat;

import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.model.ChatSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
@EnableConfigurationProperties
class Springboot17MongodbApplicationTests {
    @Resource
    private MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        ChatSession messages = ChatSession.builder()
                .roleId(1l).userId(1l).createDate(new Date())
                .messages(Arrays.asList(new Message("user","你好",""), new Message("assistant","你好，有什么可以帮助你的？","")))
                .build();
        mongoTemplate.save(messages);
    }
    @Test
    void find(){
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Query query = new Query(Criteria.where("roleId").is(10l).and("userId").is(5l))
                .with(sort);

        List<ChatSession> messagesList = mongoTemplate.find(query, ChatSession.class);
        System.out.println(messagesList);

    }
}