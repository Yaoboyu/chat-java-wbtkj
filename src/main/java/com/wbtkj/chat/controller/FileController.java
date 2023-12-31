package com.wbtkj.chat.controller;

import cn.hutool.core.util.ReUtil;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.ExtractUrl;
import com.wbtkj.chat.pojo.dto.file.UserFileType;
import com.wbtkj.chat.service.FileService;
import com.wbtkj.chat.service.UserService;
import com.wbtkj.chat.service.impl.ChatPythonWbtkjServiceImpl;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

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
    @Resource
    UserService userService;

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

        if (!userService.checkBalance(ThreadLocalConfig.getUser().getId(), GeneralConstant.PARSE_FILE_VALUE)) {
            throw new MyServiceException("余额不足");
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
        chatPythonWbtkjServiceImpl.extractFile(MockMultipartFile, userFileId, ThreadLocalConfig.getUser().getId());

        return Result.success();
    }

    @PostMapping("/parse/url")
    public Result processUrl(@RequestBody ExtractUrl extractUrl) {
        if (!ReUtil.isMatch("(https?)://([^/]+)(/.*)?", extractUrl.getUrl())) {
            throw new MyServiceException("url网址不正确");
        }

        if (!userService.checkBalance(ThreadLocalConfig.getUser().getId(), GeneralConstant.PARSE_FILE_VALUE)) {
            throw new MyServiceException("余额不足");
        }

        long userFileId = fileService.addUserFile(extractUrl.getUrl(), UserFileType.URL);

        chatPythonWbtkjServiceImpl.extractUrl(extractUrl.getUrl(), userFileId, ThreadLocalConfig.getUser().getId());

        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        return Result.success(fileService.getUserFile());
    }
}
