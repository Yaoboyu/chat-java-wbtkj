package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.dto.file.UserFileType;
import com.wbtkj.chat.pojo.model.UserFile;

import java.util.List;

public interface FileService {

    /**
     * 添加UserFile
     * @param originalName
     * @param type
     * @return
     */
    long addUserFile(String originalName, UserFileType type);

    /**
     * 在解析后添加UserFile的name
     * @param id
     * @param name
     * @return
     */
    boolean addNameAfterParse(long id, String name);

    /**
     * 获取UserFile
     * @return
     */
    List<UserFile> getUserFile();
}
