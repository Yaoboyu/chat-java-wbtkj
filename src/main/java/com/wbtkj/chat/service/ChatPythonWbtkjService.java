package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContent;
import org.springframework.web.multipart.MultipartFile;

public interface ChatPythonWbtkjService {

    void extractUrl(String url, long userFileId, long userId);

    void extractFile(MultipartFile multipartFile, long userFileId, long userId);

    /**
     * embedding python返回的内容并存储
     * @param fileContent 返回的内容
     * @param userFileId user_file表的id列
     * @param userId
     */
    void storage(FileContent fileContent, long userFileId, long userId);
}
