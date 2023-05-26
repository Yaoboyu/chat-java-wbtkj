package com.wbtkj.chat;

import com.wbtkj.chat.pojo.model.Messages;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
@EnableConfigurationProperties
class Springboot17MongodbApplicationTests {
    @Resource
    private MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        Messages messages = Messages.builder()
                .roleId(10l).userId(5l).createDate(new Date())
                .messages("{'user':'你好'},{'assistant':'你好，有什么可以帮助你的？'}")
                .build();
        mongoTemplate.save(messages);
    }
    @Test
    void find(){
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Query query = new Query(Criteria.where("roleId").is(10l).and("userId").is(5l))
                .with(sort);

        List<Messages> messagesList = mongoTemplate.find(query, Messages.class);
        System.out.println(messagesList);

    }
}