package com.wbtkj.chat.controller;

import cn.hutool.core.util.ReUtil;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.ExtractUrl;
import com.wbtkj.chat.pojo.dto.file.UserFileType;
import com.wbtkj.chat.service.FileService;
import com.wbtkj.chat.service.impl.ChatPythonWbtkjServiceImpl;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContent;
import com.wbtkj.chat.pojo.dto.openai.embeddings.TextAndEmbedding;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/file")
@ResponseBody
public class FileController {
    @Resource
    ChatPythonWbtkjServiceImpl chatPythonWbtkjServiceImpl;
    @Resource
    OpenAIService openAIService;
    @Resource
    FileService fileService;

    @PostMapping("/parse/file")
    public Result processFile(@RequestBody MultipartFile file) {
        // 检查上传的文件是否为空
        if (file.isEmpty()) {
            throw new MyServiceException("文件为空");
        }
        String originalFilename = file.getOriginalFilename();
        if (!(originalFilename.endsWith(".pdf") || originalFilename.endsWith(".doc") || originalFilename.endsWith(".txt"))) {
            throw new MyServiceException("目前只支持解析pdf、doc、txt类型文件");
        }

        long userFileId = fileService.addUserFile(originalFilename, UserFileType.FILE);

        MultipartFile MockMultipartFile = null;
        try {
            MockMultipartFile = new MockMultipartFile(
                    file.getOriginalFilename(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        chatPythonWbtkjServiceImpl.extractFile(MockMultipartFile, userFileId);

        return Result.success();
    }

    @PostMapping("/parse/url")
    public Result processUrl(@RequestBody ExtractUrl extractUrl) {
        if (!ReUtil.isMatch("(https?)://([^/]+)(/.*)?", extractUrl.getUrl())) {
            throw new MyServiceException("url网址不正确");
        }

        long userFileId = fileService.addUserFile(extractUrl.getUrl(), UserFileType.URL);

        chatPythonWbtkjServiceImpl.extractUrl(extractUrl.getUrl(), userFileId);

        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        return Result.success(fileService.getUserFile());
    }
}
