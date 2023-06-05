package com.wbtkj.chat.pojo.dto.chatPythonWbtkj;

import lombok.Data;

import java.util.List;

@Data
public class FileContent {
    private List<String> contents;
    private String lang;
    private String hashId;
}
