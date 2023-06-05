package com.wbtkj.chat.controller;

import com.wbtkj.chat.api.ChatPythonWbtkjClient;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContent;
import com.wbtkj.chat.pojo.dto.openai.embeddings.TextAndEmbedding;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/file")
@ResponseBody
public class FileController {
    @Resource
    ChatPythonWbtkjClient chatPythonWbtkjClient;
    @Resource
    OpenAIService openAIService;

    @PostMapping("/upload")
    public Result processFile(@RequestBody MultipartFile file) {
        // 检查上传的文件是否为空
        if (file.isEmpty()) {
            return Result.error();
        }


        FileContent fileContent = chatPythonWbtkjClient.extractFile(file);


        return Result.success(fileContent);
    }

    @PostMapping("/url")
    public Result processUrl(@RequestParam String url) {
        FileContent fileContent = chatPythonWbtkjClient.extractUrl(url);
        List<TextAndEmbedding> embeddings = openAIService.embeddings(fileContent.getContents());

        return Result.success(fileContent);
    }
}
