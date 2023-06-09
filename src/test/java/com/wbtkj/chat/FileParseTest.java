package com.wbtkj.chat;

import com.wbtkj.chat.mapper.FileEmbeddingMapper;
import com.wbtkj.chat.pojo.model.FileEmbedding;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@EnableConfigurationProperties
public class FileParseTest {
    @Resource
    FileEmbeddingMapper fileEmbeddingMappeer;

    @Test
    public void addFile() {
//        FileEmbedding fileEmbedding = new FileEmbedding();
//        fileEmbedding.setName("file-123jkbfnmbm23brmr12r");
//        fileEmbedding.setText("fdsjkhkja;klsdjfl;asdf,kmsd");
//        double[] arr = new double[1536];
//        Arrays.fill(arr, 0.1);
//        fileEmbedding.setEmbedding(arr);
//
//        int insert = fileEmbeddingMappeer.insert(fileEmbedding);
//
//        System.out.println(insert);
    }
}
