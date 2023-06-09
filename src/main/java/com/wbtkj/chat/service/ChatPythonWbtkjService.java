package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContent;
import org.springframework.web.multipart.MultipartFile;

public interface ChatPythonWbtkjService {

    void extractUrl(String url, long userFileId);

    void extractFile(MultipartFile multipartFile, long userFileId);

    void storage(FileContent fileContent, long userFileId);
}
